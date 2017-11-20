package beagle.compiler;

import java.io.PrintStream;
import java.util.LinkedList;

import beagle.compiler.tree.AtomicExpression;
import beagle.compiler.tree.BinaryExpression;
import beagle.compiler.tree.BooleanLiteral;
import beagle.compiler.tree.ExpressionList;
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
import beagle.compiler.tree.IfThenElseStmt;
import beagle.compiler.tree.IntegerLiteral;
import beagle.compiler.tree.NameLiteral;
import beagle.compiler.tree.NullLiteral;
import beagle.compiler.tree.ReturnStmt;
import beagle.compiler.tree.StringLiteral;
import beagle.compiler.tree.TreeVisitor;
import beagle.compiler.tree.UnaryExpression;

public class HtmlVisitor extends TreeVisitor
{

	PrintStream out;

	LinkedList<String> nameList = new LinkedList<>();

	public HtmlVisitor( PrintStream out )
	{
		this.out = out;
	}

	/*protected void attribute( String value )
	{
		out.append("<div class='attribute'><span class='value'>");
		out.append(value);
		out.append("</span></div>");
		out.flush();
	}*/

	/**
	 * name -> ClassName
	 *
	 * @param name
	 * @param clazz
	 */
	protected void attribute( String name, Class<?> clazz )
	{
		out.append("<div class='container'><div class='attribute'><span class='name'>");
		out.append(name);
		out.append("</span> &rarr; <span class='title'>");
		out.append(clazz.getSimpleName());
		out.append("</span></div></div>");
		out.flush();
	}

	/**
	 * name -> ClassName (value)
	 *
	 * @param name
	 * @param clazz
	 * @param value
	 */
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

	/**
	 * name -> (value)
	 *
	 * @param name
	 * @param value
	 */
	protected void attribute( String name, String value )
	{
		out.append("<div class='container'><div class='attribute'><span class='name'>");
		out.append(name);
		out.append("</span> &rarr; (<span class='value'>");
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
	public void finish(AtomicExpression target)
	{
		close();
	}

	@Override
	public void finish(BinaryExpression binaryExpression)
	{
		close();
	}

	@Override
	public void finish(ExpressionList target)
	{
		close();
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
	public void finish(IfThenElseStmt target)
	{
		close();
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

	@Override
	public void finish(ReturnStmt target)
	{
		close();
	}

	@Override
	public void finish(UnaryExpression target)
	{
		close();
	}

	protected boolean open(Class<?> clazz)
	{
		out.append("<div class='container'>");
		if (clazz != null)
		{
			out.append("<div class='title'>");
			out.append(clazz.getSimpleName());
			out.append("</div>");
		}
		return true;
	}

	/**
	 * name -> ClassName
	 *
	 * @param name
	 * @param clazz
	 * @return
	 */
	protected boolean open( String name, Class<?> clazz )
	{
		out.append("<div class='container'>");
		if (name != null && clazz != null)
		{
			out.append("<div class='dedent'>");

			out.append("<span class='description'>");
			out.append(name);
			out.append("</span>");

			out.append(" &rarr; <span class='title'>");
			out.append(clazz.getSimpleName());
			out.append("</span>");

			out.append("</div>");
		}
		return true;
	}

	@Override
	public boolean visit(AtomicExpression target)
	{
		open("expression", target.getClass());
		return true;
	}

	@Override
	public boolean visit(BinaryExpression target)
	{
		open("expression", target.getClass());
		attribute("operation", target.operation().toString());
		return true;
	}

	@Override
	public boolean visit(BooleanLiteral target)
	{
		attribute("expression", target.getClass(), Boolean.toString(target.value()));
		return false;
	}

	@Override
	public boolean visit(ExpressionList target)
	{
		String name = "expressionList";
		if (target.parent() instanceof UnaryExpression) name = "extra";
		open(name, target.getClass());
		return true;
	}

	@Override
	public boolean visit(IAnnotation target)
	{
		open(target.getClass());
		return true;
	}

	@Override
	public boolean visit(IAnnotationList target)
	{
		if (target.size() == 0) return false;

		open("annotations", target.getClass());

		return true;
	}

	@Override
	public boolean visit(IBlock target)
	{
		if (target.size() == 0) return false;

		return open(target.getClass());
	}


	@Override
	public boolean visit(ICompilationUnit target)
	{
		out.append("<html><head><title>Beagle AST</title>");
		writeCSS();
		out.append("</head><body>");

		open(target.getClass());

		attribute("fileName", target.fileName());

		return true;
	}


	@Override
	public boolean visit(IConstantDeclaration target)
	{
		return open(target.getClass());
	}


	@Override
	public boolean visit(IFormalParameter target)
	{
		return open(target.getClass());
	}


	@Override
	public boolean visit(IFormalParameterList target)
	{
		return open("parameters", target.getClass());
	}

	@Override
	public boolean visit(IfThenElseStmt target)
	{
		open(target.getClass());
		//attribute("condition", target.condition().getClass());
		return true;
	}

	@Override
	public boolean visit(IMethodDeclaration target)
	{
		return open(target.getClass());
	}

	@Override
	public boolean visit(IModifiers target)
	{
		return open(target.getClass());
	}

	@Override
	public boolean visit(IModule target)
	{
		return open(target.getClass());
	}


	@Override
	public boolean visit(IName target)
	{
		attribute("name", target.getClass(), target.getQualifiedName());
		return false;
	}

	@Override
	public boolean visit(IntegerLiteral target)
	{
		attribute("expression", target.getClass(), String.valueOf(target.value()));
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
		open("body", target.getClass());
		return true;
	}


	@Override
	public boolean visit(ITypeDeclaration target)
	{
		open(target.getClass());
		return true;
	}


	@Override
	public boolean visit(ITypeDeclarationList target)
	{
		open("types", target.getClass());
		return true;
	}


	@Override
	public boolean visit(ITypeImport target)
	{
		open(target.getClass());
		return true;
	}


	@Override
	public boolean visit(ITypeImportList target)
	{
		if (target.size() == 0) return false;

		open("imports", target.getClass());
		return true;
	}


	@Override
	public boolean visit(ITypeReference target)
	{
		if (target.parent() instanceof ITypeReferenceList)
		{
			open(target.getClass());
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
		return open("inherit", target.getClass());
	}


	@Override
	public boolean visit(IVariableDeclaration target)
	{
		open(target.getClass());
		return true;
	}


	@Override
	public boolean visit(NameLiteral target)
	{
		attribute("expression", target.getClass(), target.value().getQualifiedName());
		return false;
	}


	@Override
	public boolean visit(NullLiteral target)
	{
		attribute("expression", target.getClass());
		return false;
	}


	@Override
	public boolean visit(ReturnStmt target)
	{
		return open(target.getClass());
	}


	@Override
	public boolean visit(StringLiteral target)
	{
		attribute("expression", target.getClass(), target.value());
		return false;
	}


	@Override
	public boolean visit(UnaryExpression target)
	{
		open("expression", target.getClass());
		attribute("operation", target.operation().toString());
		attribute("direction", target.direction().toString());
		return true;
	}

/*
	protected boolean printExpression(String name, IExpression expr)
	{
		if (expr instanceof NameLiteral)
		{
			attribute(name, NameLiteral.class, ((NameLiteral)expr).value().getQualifiedName());
			return false;
		}

		if (expr instanceof StringLiteral)
		{
			attribute(name, StringLiteral.class, ((StringLiteral)expr).value());
			return false;
		}

		if (expr instanceof IntegerLiteral)
		{
			attribute(name, StringLiteral.class, ((StringLiteral)expr).value());
			return false;
		}

		else
		if (expr instanceof StringLiteral)
			attribute(name, StringLiteral.class, ((StringLiteral)expr).value());

		open("expression", expr.getClass());

		if (expr instanceof NameLiteral)
		{
			attribute("name", NameLiteral.class, ((NameLiteral)expr).value().getQualifiedName());
		}
		else
		if (expr instanceof StringLiteral)
		{

		}
		close();
		return false;
	}
*/
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

}
