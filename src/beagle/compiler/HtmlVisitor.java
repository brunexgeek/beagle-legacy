package beagle.compiler;

import java.io.PrintStream;

import beagle.compiler.tree.Annotation;
import beagle.compiler.tree.AnnotationList;
import beagle.compiler.tree.Argument;
import beagle.compiler.tree.ArgumentList;
import beagle.compiler.tree.AtomicExpression;
import beagle.compiler.tree.BinaryExpression;
import beagle.compiler.tree.Block;
import beagle.compiler.tree.BooleanLiteral;
import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.ConstantDeclaration;
import beagle.compiler.tree.ExpressionList;
import beagle.compiler.tree.FormalParameter;
import beagle.compiler.tree.FormalParameterList;
import beagle.compiler.tree.Block;
import beagle.compiler.tree.IExpression;
import beagle.compiler.tree.Package;
import beagle.compiler.tree.IStatement;
import beagle.compiler.tree.TypeDeclarationList;
import beagle.compiler.tree.TypeImport;
import beagle.compiler.tree.TypeReference;
import beagle.compiler.tree.IfThenElseStmt;
import beagle.compiler.tree.IntegerLiteral;
import beagle.compiler.tree.Function;
import beagle.compiler.tree.FunctionList;
import beagle.compiler.tree.Modifiers;
import beagle.compiler.tree.Name;
import beagle.compiler.tree.NameLiteral;
import beagle.compiler.tree.NullLiteral;
import beagle.compiler.tree.ReturnStmt;
import beagle.compiler.tree.StorageDeclaration;
import beagle.compiler.tree.StringLiteral;
import beagle.compiler.tree.TreeVisitor;
import beagle.compiler.tree.TypeBody;
import beagle.compiler.tree.TypeDeclaration;
import beagle.compiler.tree.TypeImportList;
import beagle.compiler.tree.TypeReference;
import beagle.compiler.tree.TypeReferenceList;
import beagle.compiler.tree.UnaryExpression;
import beagle.compiler.tree.VariableDeclaration;

public class HtmlVisitor extends TreeVisitor
{

	PrintStream out;

	String currentName = null;

	public HtmlVisitor( PrintStream out )
	{
		this.out = out;
	}

	void currentName(String value)
	{
		this.currentName = value;
	}

	String currentName()
	{
		String out = currentName;
		currentName = null;
		return out;
	}

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
		out.append("<div class='container'><div class='attribute'>");

		if (name != null)
		{
			out.append("<span class='name'>");
			out.append(name);
			out.append("</span> &rarr; ");
		}

		out.append("<span class='title'>");
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

	protected void missing()
	{
		out.append("<div class='container'><span class='missing'>missing</span></div>");
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

	public boolean visit(CompilationUnit target)
	{
		printCompilationUnit(target);
		return false;
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
		String temp = currentName();
		if (temp != null) name = temp;

		out.append("<div class='container'>");
		if (name != null && clazz != null)
		{
			out.append("<div class='dedent'>");

			if (name != null)
			{
				out.append("<span class='description'>");
				out.append(name);
				out.append("</span> &rarr; ");
			}

			out.append("<span class='title'>");
			out.append(clazz.getSimpleName());
			out.append("</span>");

			out.append("</div>");
		}
		return true;
	}

	public void printExpressionList(String name, ExpressionList target)
	{
		if (name == null) name = "expressionList";
		open(name, target.getClass());
		for (IExpression item : target)
			printExpression(null, item);
		close();
	}

	public void printAnnotationList(AnnotationList target)
	{
		if (target == null || target.size() == 0) return;

		open("annotations", target.getClass());
		for (Annotation item : target)
		{
			open(item.getClass());
			printTypeReference(item.type());
			close();
		}
		close();
	}

	public void printCompilationUnit(CompilationUnit target)
	{
		out.append("<html><head><title>Beagle AST</title>");
		writeCSS();
		out.append("</head><body>");

		open(target.getClass());
		attribute("fileName", target.fileName());
		printPackage(target.namespace());
		printTypeImportList(target.imports());
		printFunctions(target.functions());
		printTypes(target.types());
		out.append("</body></html>");
	}


	public void printFunctions( FunctionList target )
	{
		open("functions", target.getClass());
		for (Function func : target)
			printFunction(func);
		close();
	}


	public void printConstantOrVariable(boolean heading, StorageDeclaration target)
	{
		if (heading) open(target.getClass());
		printAnnotationList(target.annotations());
		printModifiers(target.modifiers());
		printName(target.name());
		printTypeReference(target.type());
		printExpression("initializer",target.initializer());
		if (heading) close();
	}

	public void printArgument(Argument target)
	{
		if (target == null) return;

		open(target.getClass());
		printName(target.name());
		printExpression("value", target.value());
		close();
	}

	public void printFormalParameterList(FormalParameterList target)
	{
		if (target == null) return;

		open("parameters", target.getClass());
		for (FormalParameter item : target)
		{
			open(item.getClass());
			printName(item.name());
			printTypeReference(item.type());
			close();
		}
		close();
	}

	public void printFunction(Function target)
	{
		open(target.getClass());
		printAnnotationList(target.annotations());
		printModifiers(target.modifiers());
		printName(target.name());
		printFormalParameterList(target.parameters());
		printTypeReference(target.returnType());
		printStatement("body",target.body());
		close();
	}

	public void printModifiers(Modifiers target)
	{
		if (target == null) return;
		attribute("modifiers", target.getClass(), target.toString());
	}

	public void printName(Name target)
	{
		printName(null, target);
	}

	public void printName(String name, Name target)
	{
		if (target == null) return;
		if (name == null) name = "name";
		attribute(name, target.getClass(), target.qualifiedName());
	}

	public void printTypeBody(TypeBody target)
	{
		open("body", target.getClass());

		for (ConstantDeclaration item : target.constants())
			printConstantOrVariable(true, item);

		for (VariableDeclaration item : target.variables())
			printConstantOrVariable(true, item);

		for (Function item : target.methods())
			printFunction(item);

		close();
	}


	public void printTypes(TypeDeclarationList target)
	{
		open("types", target.getClass());
		for (TypeDeclaration item : target)
		{
			open(item.getClass());
			printAnnotationList(item.annotations());
			printModifiers(item.modifiers());
			printName(item.name());
			printTypeReferenceList(item.extended());
			printTypeBody(item.body());
			close();
		}
		close();
	}

	public void printPackage(Package target)
	{
		if (target == null) return;
		attribute("package", target.getClass(), target.qualifiedName());
	}


	public void printTypeImportList(TypeImportList target)
	{
		if (target.size() == 0) return;

		open("imports", target.getClass());
		for (TypeImport item : target)
		{
			open(item.getClass());
			printPackage(item.namespace());
			printName(item.name());
			printName("alias", item.alias());
			close();
		}
		close();
	}


	public void printTypeReference(TypeReference target)
	{
		if (target == null) return;

		if (target.parent() instanceof TypeReferenceList)
		{
			open(target.getClass());
			attribute("type", target.getClass(), target.qualifiedName());
			close();
		}
		else
		{
			attribute("type", target.getClass(), target.qualifiedName());
		}
	}


	public void printTypeReferenceList(TypeReferenceList target)
	{
		open("inherit", target.getClass());
		for (TypeReference item : target)
			printTypeReference(item);
		close();
	}

	protected void printStatement(String name, IStatement stmt)
	{
		if (stmt == null) return;

		if (name == null)
			open(stmt.getClass());
		else
			open(name, stmt.getClass());

		if (stmt instanceof ReturnStmt)
		{
			printExpression("value", ((ReturnStmt)stmt).expression());
		}
		else
		if (stmt instanceof IfThenElseStmt)
		{
			printExpression("condition", ((IfThenElseStmt)stmt).condition());
			printStatement("then", ((IfThenElseStmt)stmt).thenSide());
			printStatement("else", ((IfThenElseStmt)stmt).elseSide());
		}
		else
		if (stmt instanceof Block)
		{
			for (IStatement item : (Block)stmt)
				printStatement(null, item);
		}
		else
		if (stmt instanceof StorageDeclaration)
		{
			printConstantOrVariable(false, (StorageDeclaration)stmt);
		}
		else
			missing();

		close();
	}

	protected void printExpression(String name, IExpression expr)
	{
		if (expr == null) return;

		if (expr instanceof NameLiteral)
		{
			attribute(name, NameLiteral.class, ((NameLiteral)expr).value().qualifiedName());
			return;
		}

		if (expr instanceof StringLiteral)
		{
			attribute(name, StringLiteral.class, ((StringLiteral)expr).value());
			return;
		}

		if (expr instanceof IntegerLiteral)
		{
			attribute(name, IntegerLiteral.class, Long.toString( ((IntegerLiteral)expr).value() ));
			return;
		}

		if (expr instanceof BooleanLiteral)
		{
			attribute(name, BooleanLiteral.class, Boolean.toString( ((BooleanLiteral)expr).value()) );
			return;
		}

		if (expr instanceof NullLiteral)
		{
			attribute("expression", NullLiteral.class);
			return;
		}

		open(name, expr.getClass());

		if (expr instanceof AtomicExpression)
		{
			printExpression("expression", ((AtomicExpression)expr).value());
		}
		else
		if (expr instanceof UnaryExpression)
		{
			attribute("operation", ((UnaryExpression)expr).operation().toString());
			printExpression("expression", ((UnaryExpression)expr).expression());
			printExpression("right", ((UnaryExpression)expr).extra());
		}
		else
		if (expr instanceof BinaryExpression)
		{
			attribute("operation", ((BinaryExpression)expr).operation().toString());
			printExpression("left", ((BinaryExpression)expr).left());
			printExpression("right", ((BinaryExpression)expr).right());
		}
		else
		if (expr instanceof ExpressionList)
		{
			for (IExpression item : ((ExpressionList)expr))
				printExpression(null, item);
		}
		else
		if (expr instanceof ArgumentList)
		{
			for (Argument item : ((ArgumentList)expr))
				printArgument(item);
		}
		else
			missing();

		close();
	}

	public void writeCSS()
	{
		out.append("<style>"
			+ "html * {font-family: monospace; font-size: 14px; line-height: 20px}"
			+ ".container {/*border: 1px solid red; margin-right: -1px; margin-bottom: -1px;*/ border-left: 1px dashed #ddd; padding-left: 1.5em; background-color: #fff}"
			+ ".container:hover {border-left: 1px dashed #888;}"
			+ "body div:first-of-type { border-left: none; }"
			+ "body div:first-of-type:hover { border-left: none; }"
			+ ".title {font-weight: 600;}"
			+ ".missing {color: red}"
			+ ".attribute .name {color: blue}"
			+ ".container .description {color: blue}"
			+ ".attribute .value {font-style: italic; color: green}"
			+ "</style>");
	}

}
