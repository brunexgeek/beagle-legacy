package beagle.compiler;

import java.io.OutputStream;
import java.io.PrintStream;

import beagle.compiler.tree.ITypeBody;
import beagle.compiler.tree.ICompilationUnit;
import beagle.compiler.tree.IMethodDeclaration;
import beagle.compiler.tree.IModule;
import beagle.compiler.tree.IPackage;
import beagle.compiler.tree.ITypeDeclaration;
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
	public void visitCompilationUnit(ICompilationUnit target)
	{
		super.visitCompilationUnit(target);
	}

	@Override
	public void visitTypeDeclaration(ITypeDeclaration current)
	{
		super.visitTypeDeclaration(current);
	}

	@Override
	public void visitImport(IPackage target)
	{
		super.visitImport(target);
	}

	@Override
	public void visitPackage(IPackage target)
	{
		super.visitPackage(target);
	}

	@Override
	public void visitModule(IModule target)
	{
		printer.println("; module '" + target.getName().getQualifiedName() + "'");
		super.visitModule(target);
	}

	@Override
	public void visitTypeBody(ITypeBody target)
	{
		super.visitTypeBody(target);
	}

	@Override
	public void visitMethodDeclaration(IMethodDeclaration target)
	{
		printer.println("; method of '" + target.getParent().getParent().getName().getQualifiedName() + "'");
		printer.println("define void @" + target.getName().getQualifiedName() + "() {}");
		super.visitMethodDeclaration(target);
	}

}
