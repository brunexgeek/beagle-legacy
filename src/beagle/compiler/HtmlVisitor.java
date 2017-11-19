package beagle.compiler;

import java.io.PrintStream;

import beagle.compiler.tree.AtomicExpression;
import beagle.compiler.tree.BinaryExpression;
import beagle.compiler.tree.BooleanLiteral;
import beagle.compiler.tree.IAnnotation;
import beagle.compiler.tree.IAnnotationList;
import beagle.compiler.tree.IBlock;
import beagle.compiler.tree.ICompilationUnit;
import beagle.compiler.tree.IConstantDeclaration;
import beagle.compiler.tree.IFormalParameter;
import beagle.compiler.tree.IFormalParameterList;
import beagle.compiler.tree.IMethodDeclaration;
import beagle.compiler.tree.IModifiers;
import beagle.compiler.tree.IModule;
import beagle.compiler.tree.IName;
import beagle.compiler.tree.IPackage;
import beagle.compiler.tree.ITypeBody;
import beagle.compiler.tree.ITypeDeclaration;
import beagle.compiler.tree.ITypeDeclarationList;
import beagle.compiler.tree.ITypeImport;
import beagle.compiler.tree.ITypeImportList;
import beagle.compiler.tree.ITypeReference;
import beagle.compiler.tree.ITypeReferenceList;
import beagle.compiler.tree.IVariableDeclaration;
import beagle.compiler.tree.IntegerLiteral;
import beagle.compiler.tree.NameLiteral;
import beagle.compiler.tree.NullLiteral;
import beagle.compiler.tree.StringLiteral;
import beagle.compiler.tree.TreeVisitor;
import beagle.compiler.tree.UnaryExpression;

public class HtmlVisitor extends TreeVisitor
{

	PrintStream out;

	public HtmlVisitor( PrintStream out )
	{
		this.out = out;
	}


	protected void attribute( String value )
	{
		out.append("<div class='attribute'><span class='value'>");
		out.append(value);
		out.append("</span></div>");
		out.flush();
	}

	protected void attribute( String name, String value )
	{
		out.append("<div class='container'><div class='attribute'><span class='name'>");
		out.append(name);
		out.append("</span> &rarr; (<span class='value'>");
		out.append(value);
		out.append("</span>)</div></div>");
		out.flush();
	}

	protected void attribute( String name, Class<?> clazz )
	{
		out.append("<div class='container'><div class='attribute'><span class='name'>");
		out.append(name);
		out.append("</span> &rarr; <span class='title'>");
		out.append(clazz.getSimpleName());
		out.append("</span></div></div>");
		out.flush();
	}

	protected void attribute( String name, Class<?> clazz, String value )
	{
		out.append("<div class='container'><div class='attribute'><span class='name'>");
		out.append(name);
		out.append("</span> &rarr; <span class='title'>");
		out.append(clazz.getSimpleName());
		out.append("</span>(<span class='value'>");
		out.append(value);
		out.append("</span>)</div></div>");
		out.flush();
	}

/*
	protected void title( String name, Class<?> clazz )
	{
		out.append("<div class='attribute'><span class='name'>");
		out.append(name);
		out.append("</span> &rarr; <span class='title'>");
		out.append(clazz.getSimpleName());
		out.append("</span></div>");
		out.flush();
	}*/

	protected void close()
	{
		out.append("</div>");
	}

	@Override
	public void finish(IAnnotation target)
	{
		close();
	}

	@Override
	public void finish(IAnnotationList target)
	{
		if (target.size() != 0) close();
	}

	@Override
	public void finish(IBlock target)
	{
		if (target.size() != 0) close();
	}

	@Override
	public void finish( ICompilationUnit target )
	{
		out.append("</body></html>");
	}

	@Override
	public void finish(IConstantDeclaration target)
	{
		close();
	}


	@Override
	public void finish(IFormalParameter target)
	{
		close();
	}


	@Override
	public void finish(IFormalParameterList target)
	{
		if (target.size() != 0) close();
	}


	@Override
	public void finish(IMethodDeclaration target)
	{
		close();
	}


	@Override
	public void finish(IModifiers target)
	{
		close();
	}


	@Override
	public void finish(IModule target)
	{
		close();
	}

	@Override
	public void finish(ITypeBody target)
	{
		close();
	}


	@Override
	public void finish(ITypeDeclaration target)
	{
		close();
	}


	@Override
	public void finish(ITypeDeclarationList target)
	{
		if (target.size() != 0) close();
	}


	@Override
	public void finish(ITypeImport target)
	{
		close();
	}


	@Override
	public void finish(ITypeImportList target)
	{
		if (target.size() != 0) close();
	}


	@Override
	public void finish(ITypeReference target)
	{
		if (target.parent() instanceof ITypeReferenceList) close();
	}


	@Override
	public void finish(ITypeReferenceList target)
	{
		if (target.size() == 0) return;
		close();
	}


	@Override
	public void finish(IVariableDeclaration target)
	{
		close();
	}

	protected boolean open( String type )
	{
		out.append("<div class='container'>");
		if (type != null)
		{
			out.append("<div class='title'>");
			out.append(type);
			out.append("</div>");
		}
		return true;
	}

	protected boolean open( String type, String description )
	{
		out.append("<div class='container'>");
		if (type != null && description != null)
		{
			out.append("<div class='dedent'>");

			out.append("<span class='description'>");
			out.append(description);
			out.append("</span>");

			out.append(" &rarr; <span class='title'>");
			out.append(type);
			out.append("</span>");

			out.append("</div>");
		}
		return true;
	}


	@Override
	public boolean visit(IAnnotation target)
	{
		open(target.getClass().getSimpleName());
		return true;
	}


	@Override
	public boolean visit(IAnnotationList target)
	{
		if (target.size() == 0) return false;

		open(target.getClass().getSimpleName(), "annotations");

		return true;
	}

	@Override
	public boolean visit(IBlock target)
	{
		if (target.size() == 0) return false;

		return open(target.getClass().getSimpleName());
	}

	@Override
	public boolean visit(ICompilationUnit target)
	{
		out.append("<html><head><title>Beagle AST</title>");
		writeCSS();
		out.append("</head><body>");

		open(target.getClass().getSimpleName());

		attribute("fileName", target.fileName());

		return true;
	}

	@Override
	public boolean visit(IConstantDeclaration target)
	{
		return open(target.getClass().getSimpleName());
	}

	@Override
	public boolean visit(IFormalParameter target)
	{
		return open(target.getClass().getSimpleName());
	}

	@Override
	public boolean visit(IFormalParameterList target)
	{
		return open(target.getClass().getSimpleName(), "parameters");
	}

	@Override
	public boolean visit(IMethodDeclaration target)
	{
		return open(target.getClass().getSimpleName());
	}

	@Override
	public boolean visit(IModifiers target)
	{
		return open(target.getClass().getSimpleName());
	}

	@Override
	public boolean visit(IModule target)
	{
		return open(target.getClass().getSimpleName());
	}

	@Override
	public boolean visit(IName target)
	{
		attribute("name", target.getClass(), target.getQualifiedName());
		return false;
	}

	@Override
	public boolean visit(IPackage target)
	{
		attribute("package", target.getClass(), target.getQualifiedName());
		return false;
	}

	@Override
	public boolean visit(ITypeBody target)
	{
		open(target.getClass().getSimpleName(), "body");
		return true;
	}


	@Override
	public boolean visit(ITypeDeclaration target)
	{
		open(target.getClass().getSimpleName());
		return true;
	}


	@Override
	public boolean visit(ITypeDeclarationList target)
	{
		open(target.getClass().getSimpleName(), "types");
		return true;
	}


	@Override
	public boolean visit(ITypeImport target)
	{
		open(target.getClass().getSimpleName());
		return true;
	}


	@Override
	public boolean visit(ITypeImportList target)
	{
		if (target.size() == 0) return false;

		open(target.getClass().getSimpleName(), "imports");
		return true;
	}


	@Override
	public boolean visit(ITypeReference target)
	{
		if (target.parent() instanceof ITypeReferenceList)
		{
			open(target.getClass().getSimpleName());
			return true;
		}
		else
		{
			attribute("type", target.getClass(), target.getQualifiedName());
			return false;
		}
	}

	@Override
	public boolean visit(ITypeReferenceList target)
	{
		return open(target.getClass().getSimpleName(), "inherit");
	}

	@Override
	public boolean visit(IVariableDeclaration target)
	{
		open(target.getClass().getSimpleName());
		return true;
	}

	public void writeCSS()
	{
		out.append("<style>"
			+ "html * {font-family: monospace; font-size: 14px; line-height: 20px}"
			+ ".container {/*border: 1px solid red; margin-right: -1px; margin-bottom: -1px;*/ border-left: 1px dashed #ddd; padding-left: 1.5em; background-color: #fff}"
			+ ".title {font-weight: 600;}"
			+ ".attribute .name {color: blue}"
			+ ".container .description {color: blue}"
			+ ".attribute .value {font-style: italic; color: green}"
			+ "</style>");
	}


	@Override
	public boolean visit(BooleanLiteral target)
	{
		attribute("expression", target.getClass(), Boolean.toString(target.value()));
		return false;
	}

	@Override
	public boolean visit(AtomicExpression target)
	{
		open(target.getClass().getSimpleName(), "expression");
		return true;
	}


	@Override
	public void finish(AtomicExpression target)
	{
		close();
	}


	@Override
	public boolean visit(StringLiteral target)
	{
		attribute("expression", target.getClass(), target.value());
		return false;
	}


	@Override
	public boolean visit(IntegerLiteral target)
	{
		attribute("expression", target.getClass(), String.valueOf(target.value()));
		return false;
	}


	@Override
	public boolean visit(NullLiteral target)
	{
		attribute("expression", target.getClass());
		return false;
	}


	@Override
	public boolean visit(NameLiteral target)
	{
		attribute("expression", target.getClass(), target.value().getQualifiedName());
		return false;
	}


	@Override
	public boolean visit(UnaryExpression target)
	{
		open(target.getClass().getSimpleName(), "expression");
		attribute("operation", target.operation().toString());
		attribute("direction", target.direction().toString());
		return true;
	}


	@Override
	public void finish(UnaryExpression target)
	{
		close();
	}


	@Override
	public boolean visit(BinaryExpression target)
	{
		open(target.getClass().getSimpleName(), "expression");
		attribute("operation", target.operation().toString());
		return true;
	}


	@Override
	public void finish(BinaryExpression binaryExpression)
	{
		close();
	}

}
