package beagle.compiler;

import java.io.OutputStream;
import java.io.PrintStream;

import beagle.compiler.tree.MethodDeclaration;
import beagle.compiler.tree.Module;
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

		for (VariableDeclaration field : current.body().variables())
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
	public boolean visit(MethodDeclaration target)
	{
		printer.println("; method of '" + target.parent().parent().name().qualifiedName() + "'");
		printer.println("define void @" + target.name().qualifiedName() + "() {}");
		return true;
	}

}
