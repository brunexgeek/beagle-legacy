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
		printer.println("; Type '" + current.getQualifiedName() + "'");

		printer.print("%.dyn.");
		printer.print(current.getQualifiedName());
		printer.print(" = type { %.classref");

		for (IVariableDeclaration field : current.getBody().getVariables())
		{
			printer.print(", %.dyn.");
			printer.print(field.getType().getQualifiedName());
		}

		printer.println(" }");
		return true;
	}

	@Override
	public boolean visit(IModule target)
	{
		printer.println("; module '" + target.getName().getQualifiedName() + "'");
		return true;
	}

	@Override
	public boolean visit(IMethodDeclaration target)
	{
		printer.println("; method of '" + target.getParent().getParent().getName().getQualifiedName() + "'");
		printer.println("define void @" + target.getName().getQualifiedName() + "() {}");
		return true;
	}

}
