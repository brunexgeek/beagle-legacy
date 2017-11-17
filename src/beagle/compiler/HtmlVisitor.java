package beagle.compiler;

import java.io.PrintStream;

import beagle.compiler.tree.ICompilationUnit;
import beagle.compiler.tree.TreeVisitor;

public class HtmlVisitor extends TreeVisitor
{

	PrintStream out;

	public HtmlVisitor( PrintStream out )
	{
		this.out = out;
	}

	protected void span( String value, String clazz )
	{
		if (clazz == null)
			span(value);
		else
		{
			out.append("<span class='");
			out.append(clazz);
			out.append("'>");
			out.append(value);
			out.append("</span>");
			out.flush();
		}
	}

	protected void span( String value )
	{
		out.append("<span>");
		out.append(value);
		out.append("</span>");
		out.flush();
	}


	@Override
	public boolean visit(ICompilationUnit compilationUnit)
	{
		span("bla");
		return true;
	}

}
