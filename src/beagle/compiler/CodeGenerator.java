package beagle.compiler;

import java.io.OutputStream;
import java.io.PrintStream;

import beagle.compiler.tree.Function;
import beagle.compiler.tree.Module;
import beagle.compiler.tree.StorageDeclaration;
import beagle.compiler.tree.TreeVisitor;
import beagle.compiler.tree.TypeDeclaration;
import beagle.compiler.tree.VariableDeclaration;

public class CodeGenerator extends TreeVisitor
{

	protected OutputStream output;

	protected PrintStream printer;

	public CodeGenerator( OutputStream output )
	{
		this.output = output;
		this.printer = new PrintStream(output);
	}

	@Override
	public boolean visit(TypeDeclaration current)
	{
		printer.println("; Type '" + current.qualifiedName() + "'");

		printer.print("%.dyn.");
		printer.print(current.qualifiedName());
		printer.print(" = type { %.classref");

		for (StorageDeclaration field : current.body().storages)
		{
			printer.print(", %.dyn.");
			printer.print(field.type().qualifiedName());
		}

		printer.println(" }");
		return true;
	}

	@Override
	public boolean visit(Module target)
	{
		printer.println("; module '" + target.name().qualifiedName() + "'");
		return true;
	}

	@Override
	public boolean visit(Function target)
	{
		printer.println("; method of '" + "'");
		printer.println("define void @" + target.name().qualifiedName() + "() {}");
		return true;
	}

}
