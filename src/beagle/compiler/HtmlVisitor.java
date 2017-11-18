package beagle.compiler;

import java.io.PrintStream;
import java.util.List;

import beagle.compiler.tree.IAnnotation;
import beagle.compiler.tree.ICompilationUnit;
import beagle.compiler.tree.IConstantDeclaration;
import beagle.compiler.tree.IMethodDeclaration;
import beagle.compiler.tree.ITypeBody;
import beagle.compiler.tree.ITypeDeclaration;
import beagle.compiler.tree.IVariableDeclaration;
import beagle.compiler.tree.TreeVisitor;

public class HtmlVisitor extends TreeVisitor
{

	PrintStream out;

	int level = 0;

	public HtmlVisitor( PrintStream out )
	{
		this.out = out;
	}

	protected void title( String value, String clazz )
	{
		if (clazz == null)
			title(value);
		else
		{
			out.append("<div class='title ");
			out.append(clazz);
			out.append(" level");
			out.append(Integer.toString(level));
			out.append("'>");
			out.append(value);
			out.append("</div>");
			out.flush();
		}
	}

	protected void title( String value )
	{
		out.append("<div class='title level");
		out.append(Integer.toString(level));
		out.append("'>");
		out.append(value);
		out.append("</div>");
		out.flush();
	}

	protected void attribute( String name, String value )
	{
		out.append("<div class='attribute level");
		out.append(Integer.toString(level));
		out.append("'><span class='name'>");
		out.append(name);
		out.append("</span> &rarr; <span class='value'>");
		out.append(value);
		out.append("</span></div>");
		out.flush();
	}

	protected void attribute( String value )
	{
		out.append("<div class='attribute level");
		out.append(Integer.toString(level));
		out.append("'><span class='value'>");
		out.append(value);
		out.append("</span></div>");
		out.flush();
	}

	public void writeCSS()
	{
		out.append("<style>\n");
		out.append("html * {font-family: monospace; font-size: 14px; line-height: 20px}");
		for (int i = 0; i < 30; ++i)
		{
			out.append(".level");
			out.append(Integer.toString(i));
			out.append("{margin-left:");
			out.append(Integer.toString(i));
			out.append("em;}\n");
		}
		out.append(".title {font-weight: 600}");
		out.append(".attribute .name {color: blue}");
		out.append(".attribute .value {font-style: italic}");
		out.append("</style>");
	}


	@Override
	public boolean visit(ICompilationUnit target)
	{
		out.append("<html><head><title>Beagle AST</title>");
		writeCSS();
		out.append("</head><body>");

		title(target.getClass().getSimpleName());
		level++;

		attribute("fileName", target.getFileName());

		return true;
	}

	@Override
	public boolean visit()
	{
		level++;
		return true;
	}

	@Override
	public void finish()
	{
		level--;
	}

	void writeAnnotations( List<IAnnotation> target )
	{
		if (target.size() == 0) return;
		title("Annotations");
		level++;
		for (IAnnotation item : target)
			attribute(item.getType().getQualifiedName());
		level--;
	}

	@Override
	public boolean visit(ITypeDeclaration target)
	{
		title(target.getClass().getSimpleName());
		level++;

		attribute("name", target.getQualifiedName());
		writeAnnotations(target.getAnnotations());

		return true;
	}

	@Override
	public boolean visit(ITypeBody target)
	{
		title(target.getClass().getSimpleName());
		level++;
		return true;
	}

	@Override
	public boolean visit(IConstantDeclaration target)
	{
		title(target.getClass().getSimpleName());
		level++;

		attribute("name", target.getName().toString());
		if (target.getType() != null)
			attribute("type", target.getType().getQualifiedName());

		writeAnnotations(target.getAnnotations());
		return false;
	}

	@Override
	public boolean visit(IMethodDeclaration target)
	{
		title(target.getClass().getSimpleName());
		level++;

		attribute("name", target.getName().toString());
		if (target.getReturnType() != null)
			attribute("returnType", target.getReturnType().getQualifiedName());
		writeAnnotations(target.getAnnotations());

		return true;
	}

	@Override
	public boolean visit(IVariableDeclaration target)
	{
		title(target.getClass().getSimpleName());
		level++;

		attribute("name", target.getName().toString());
		attribute("type", target.getType().getQualifiedName());

		writeAnnotations(target.getAnnotations());
		return false;
	}

	@Override
	public void finish( ICompilationUnit target )
	{
		out.append("</body></html>");
		level--;
	}

}
