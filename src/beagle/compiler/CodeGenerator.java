package beagle.compiler;

import java.io.OutputStream;
import java.io.PrintStream;

import beagle.compiler.tree.ICompilationUnit;
import beagle.compiler.tree.IMethodDeclaration;
import beagle.compiler.tree.IModule;
import beagle.compiler.tree.IPackage;
import beagle.compiler.tree.ITypeBody;
import beagle.compiler.tree.ITypeDeclaration;
import beagle.compiler.tree.IVariableDeclaration;
import beagle.compiler.tree.TreeVisitor;

public class CodeGenerator extends TreeVisitor<Object>
{

	protected OutputStream output;

	protected PrintStream printer;

	public CodeGenerator( OutputStream output )
	{
		this.output = output;
		this.printer = new PrintStream(output);
	}

	@Override
	public void visitCompilationUnit(ICompilationUnit target, Object context)
	{
		super.visitCompilationUnit(target, context);
	}

	@Override
	public void visitTypeDeclaration(ITypeDeclaration current, Object context)
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

		super.visitTypeDeclaration(current, context);
	}

	@Override
	public void visitImport(IPackage target, Object context)
	{
		super.visitImport(target, context);
	}

	@Override
	public void visitPackage(IPackage target, Object context)
	{
		super.visitPackage(target, context);
	}

	@Override
	public void visitModule(IModule target, Object context)
	{
		printer.println("; module '" + target.getName().getQualifiedName() + "'");
		super.visitModule(target, context);
	}

	@Override
	public void visitTypeBody(ITypeBody target, Object context)
	{
		super.visitTypeBody(target, context);
	}

	@Override
	public void visitMethodDeclaration(IMethodDeclaration target, Object context)
	{
		printer.println("; method of '" + target.getParent().getParent().getName().getQualifiedName() + "'");
		printer.println("define void @" + target.getName().getQualifiedName() + "() {}");
		super.visitMethodDeclaration(target, context);
	}

}
