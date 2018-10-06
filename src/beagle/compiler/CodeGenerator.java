package beagle.compiler;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import beagle.compiler.tree.Block;
import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.FormalParameter;
import beagle.compiler.tree.FormalParameterList;
import beagle.compiler.tree.Function;
import beagle.compiler.tree.FunctionList;
import beagle.compiler.tree.Module;
import beagle.compiler.tree.StorageDeclaration;
import beagle.compiler.tree.StorageList;
import beagle.compiler.tree.Structure;
import beagle.compiler.tree.StructureList;
import beagle.compiler.tree.TypeReference;

public class CodeGenerator
{

	protected OutputStream output;

	protected PrintStream printer;

	protected CompilationContext context;

	public CodeGenerator( CompilationContext context, OutputStream output )
	{
		this.output = output;
		this.printer = new PrintStream(output);
		this.context = context;
	}

	protected void println()
	{
		printer.println();
	}

	protected void println( String value )
	{
		printer.println(value);
	}

	protected void print( String value )
	{
		printer.print(value);
	}

	protected void comment( String value )
	{
		if (value.contains("\n"))
		{
			String lines[] = value.split("\r\n|\n");
			for (String item : lines)
				printer.println("// " + item);
		}
		else
			printer.println("// " + value);
	}

	public void generate( Module module )
	{
		comment(" Beagle Compiler");
		comment(" AUTO-GENERATED CODE - Do not edit!");

		println("\n#include <beagle/base.h>");

		generateStringTable();
		for (CompilationUnit item : module.units.values())
			generateUnit(item);
	}

	public void generateUnit( CompilationUnit unit )
	{
		generateStructures(unit.structures);
		generateFunctions(unit.functions);
	}

	private void generateFunctions(FunctionList functions)
	{
		println();
		comment("\nFUNCTIONS\n ");
		println();

		// generate C types and global variables;
		for (Function item : functions)
		{
			generateFunction(item);
		}
	}

	private void generateFunction(Function function)
	{
		comment(function.name().qualifiedName());
		if (function.returnType() != null)
			printTypeReference(function.returnType());
		print("  ");
		print(nativeName("def_", function.name().qualifiedName()));
		generateParameterList(function.parameters());
		generateBlock(function.body());
		println();
	}

	private void generateParameterList( FormalParameterList params )
	{
		print("(");
		for (Iterator<FormalParameter> it = params.iterator(); it.hasNext();)
		{
			FormalParameter current = it.next();
			printTypeReference(current.type());
			print(current.name().qualifiedName());

			if (it.hasNext()) print(", ");
		}
		print(")");
	}

	private void generateBlock( Block block )
	{
		print("{ }");
	}

	private void generateStructures(StructureList structures)
	{
		println();
		comment("\nSTRUCTURES\n ");
		println();

		// generate C types and global variables;
		for (Structure item : structures)
		{
			generateStructure(item);
		}
	}

	private void generateStructure(Structure item)
	{
		comment(item.name.qualifiedName());

		// static data
		print("typedef struct\n{\n   ");
		if (item.parent != null)
		{
			print(nativeTypeName(item.parent.qualifiedName(), true));
			print(" *base__;\n");
		}
		else
			print("void *base__; // no base type\n");
		print("   struct TypeInfo typeInfo__;");
		print("\n   // no static fields\n} ");
		print(nativeTypeName(item.name.qualifiedName(), true));
		print(";\n\n");
		// TODO: generate static fields

		// dynamic data
		print("typedef struct\n{\n   ");
		if (item.parent != null)
		{
			print(nativeTypeName(item.parent.qualifiedName(), false));
			print(" base__;\n");
		}
		else
			comment("no base type");
		print("   ");
		print(nativeTypeName(item.name.qualifiedName(), true));
		print(" *type__;\n");
		//print("\n   // no dynamic fields\n} ");
		generateStorageList(item.body.storages);
		print("\n} ");
		print(nativeTypeName(item.name.qualifiedName(), false));
		print(";\n\n");


		String typeGlobal = nativeName("type_", item.name.qualifiedName());
		String parentGlobal = null;
		if (item.parent != null)
			parentGlobal = nativeName("type_", item.parent.qualifiedName());

		//
		// global storage for static information
		//
		print("static ");
		print(nativeTypeName(item.name.qualifiedName(), true));
		print(" ");
		print(typeGlobal);
		print(" = \n");

		//
		// static initialization for global storage
		//

		// pointer to base type information
		print("{\n   .typeInfo__.base = ");
		if (item.parent == null)
			print("NULL,\n");
		else
		{
			print("&(");
			print(parentGlobal);
			print(".typeInfo__),\n");
		}
		// size of static information
		print("   ");
		print(".typeInfo__.staticSize = sizeof(");
		print(nativeTypeName(item.name.qualifiedName(), true));
		print("),\n");
		// size of dynamic information
		print("   ");
		print(".typeInfo__.dynamicSize = sizeof(");
		print(nativeTypeName(item.name.qualifiedName(), false));
		print("),\n");
		// qualified name
		print("   ");
		print(".typeInfo__.name = \"");
		print(item.name.qualifiedName());
		print("\",\n");
		// pointer to base static information
		print("   ");
		print(".base__ = ");
		if (item.parent == null)
			print("NULL,\n");
		else
		{
			print("&");
			print(parentGlobal);
			print(",\n");
		}
		print("};\n\n");
	}

	void generateStorageList( StorageList storages )
	{
		if (storages.isEmpty())
		{
			print("   // no dynamic fields\n");
			return;
		}
		for (StorageDeclaration storage : storages)
		{
			printTypeReference(storage.type());
			print(storage.name().qualifiedName());
			print(";\n");
		}
	}

	String nativeName( String prefix, String qualified )
	{
		String value = qualified.replaceAll("\\.", "_");
		return prefix + value + "_";
	}

	String nativeTypeName( String qualified, boolean isStatic )
	{
		String value = qualified.replaceAll("\\.", "_");
		if (isStatic)
			return "static_" + value + "_";
		else
			return "dynamic_" + value + "_";
	}

	void printTypeReference( TypeReference ref )
	{
		if (ref.isPrimitive)
		{
			print("beagle_");
			print(ref.qualifiedName());
		}
		else
		{
			print(nativeTypeName(ref.qualifiedName(), false));
			print("*");
		}
		print(" ");
	}

	public void generateStringTable()
	{
		comment("STRING TABLE");
		print("static const dynamic_string_ STRING_TABLE[] =\n{\n");

		for (String item : context.stringTable)
		{
			print("   { .type__ = &type_string_, .length = ");
			print(Integer.toString(item.length()));
			print(", .content = \"");
			print(item);
			print("\"},\n");
		}

		print("};");
	}

}
