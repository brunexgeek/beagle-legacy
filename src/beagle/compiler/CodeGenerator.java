package beagle.compiler;

import java.io.OutputStream;
import java.io.PrintStream;

import beagle.compiler.tree.IMethodDeclaration;
import beagle.compiler.tree.IModule;
import beagle.compiler.tree.ITypeDeclaration;
import beagle.compiler.tree.IVariableDeclaration;
import beagle.compiler.tree.TreeVisitor;

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
	public boolean visit(ITypeDeclaration current)
	{
		printer.println("; Type '" + current.qualifiedName() + "'");

		printer.print("%.dyn.");
		printer.print(current.qualifiedName());
		printer.print(" = type { %.classref");

		for (IVariableDeclaration field : current.body().getVariables())
		{
			printer.print(", %.dyn.");
			printer.print(field.type().getQualifiedName());
		}

		printer.println(" }");
		return true;
	}

	@Override
	public boolean visit(IModule target)
	{
		printer.println("; module '" + target.getName().qualifiedName() + "'");
		return true;
	}

	@Override
	public boolean visit(IMethodDeclaration target)
	{
		printer.println("; method of '" + target.parent().getParent().name().qualifiedName() + "'");
		printer.println("define void @" + target.name().qualifiedName() + "() {}");
		return true;
	}

}
