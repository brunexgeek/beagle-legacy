package beagle.compiler;

import java.io.PrintStream;

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
import beagle.compiler.tree.TreeVisitor;

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
	public void finish()
	{
	}
/*
	void writeAnnotations( List<IAnnotation> target )
	{
		if (target.size() == 0) return;
		title("Annotations");
		level++;
		for (IAnnotation item : target)
			attribute(item.getType().getQualifiedName());
		level--;
	}*/

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
		if (target.statements().size() != 0) close();
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
	public void finish(IName name)
	{
	}


	@Override
	public void finish(IPackage target1)
	{
		//close();
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


	protected boolean open()
	{
		return open(null);
	}


	protected boolean open( String title )
	{
		out.append("<div class='container'>");
		if (title != null)
		{
			out.append("<div class='title'>");
			out.append(title);
			out.append("</div>");
		}
		return true;
	}
/*
	protected boolean open( String title, String description )
	{
		out.append("<div class='container'>");
		if (title != null && description != null)
		{
			out.append("<div><span class='title'>");
			out.append(title);
			out.append("</span><span class='description'>(");
			out.append(description);
			out.append(")</span></div>");
		}
		return true;
	}*/

	protected boolean open( String title, String description )
	{
		out.append("<div class='container'>");
		if (title != null && description != null)
		{
			out.append("<div class='dedent'>");

			out.append("<span class='description'>");
			out.append(description);
			out.append("</span>");

			out.append(" &rarr; <span class='title'>");
			out.append(title);
			out.append("</span>");

			out.append("</div>");
		}
		return true;
	}

/*
	protected void title( String value )
	{
		out.append("<div class='title'>");
		out.append(value);
		out.append("</div>");
		out.flush();
	}


	protected void title( String value, String clazz )
	{
		if (clazz == null)
			title(value);
		else
		{
			out.append("<div class='title ");
			out.append(clazz);
			out.append("'>");
			out.append(value);
			out.append("</div>");
			out.flush();
		}
	}
*/

	@Override
	public boolean visit()
	{
		open(this.getClass().getSimpleName());
		return true;
	}


	@Override
	public boolean visit(IAnnotation target)
	{
		open(target.getClass().getSimpleName());
		//attribute("name", target.type().getQualifiedName());
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
		if (target.statements().size() == 0) return false;

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
		return open(target.getClass().getSimpleName(), "inheritance");
	}

	@Override
	public boolean visit(IVariableDeclaration target)
	{
		open(target.getClass().getSimpleName());
		return true;
	}

	public void writeCSS()
	{
		out.append("<style>\n");
		out.append("html * {font-family: monospace; font-size: 14px; line-height: 20px}");
		out.append(".container {/*border: 1px solid red; margin-right: -1px; margin-bottom: -1px;*/ border-left: 1px dashed #ddd; padding-left: 1.5em; background-color: #fff}");
		out.append(".title {font-weight: 600;}");
		//out.append(".container div:first-of-type, .container .dedent {margin-left: -1.5em}");
		out.append(".attribute .name {color: blue}");
		out.append(".container .description {color: blue}");
		out.append(".attribute .value {font-style: italic; color: green}");
		out.append("</style>");
	}

}
