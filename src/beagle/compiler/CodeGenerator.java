package beagle.compiler;

import java.io.OutputStream;
import java.io.PrintStream;

import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.Function;
import beagle.compiler.tree.Module;
import beagle.compiler.tree.Name;
import beagle.compiler.tree.StorageDeclaration;
import beagle.compiler.tree.Structure;
import beagle.compiler.tree.StructureList;
import beagle.compiler.tree.TypeDeclaration;
import beagle.compiler.tree.TypeReference;

public class CodeGenerator
{

	protected OutputStream output;

	protected PrintStream printer;

	public CodeGenerator( OutputStream output )
	{
		this.output = output;
		this.printer = new PrintStream(output);
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
		print(" *type__;");
		print("\n   // no dynamic fields\n} ");
		print(nativeTypeName(item.name.qualifiedName(), false));
		print(";\n\n");
		// TODO: generate static fields

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
		print(".typeInfo__.staticSize = 0,\n");
		// size of dynamic information
		print("   ");
		print(".typeInfo__.dynamicSize = 0,\n");
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

	public void generateStringTable()
	{
		comment("STRING TABLE");
		print("static dynamic_string_ STRING_TABLE[] =\n{\n");

		print("   { .type__ = &type_string_, .length = ");
		print("9");
		print(", .content = \"");
		print("My string");
		print("\"},\n");

		print("};");
	}

	public boolean visit(Module target)
	{
		printer.println("; module '" + target.name().qualifiedName() + "'");
		return true;
	}

	public boolean visit(Function target)
	{
		printer.println("; method of '" + "'");
		printer.println("define void @" + target.name().qualifiedName() + "() {}");
		return true;
	}

}
