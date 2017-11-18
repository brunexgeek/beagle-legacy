package beagle.compiler.tree;

import java.util.LinkedList;

public class TypeImportList extends LinkedList<ITypeImport> implements ITypeImportList
{

	private static final long serialVersionUID = -753519263293915297L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (ITypeImport item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

	@Override
	public void accept(ITreeVisitor visitor, ITreeElement child)
	{
		if (child != null) child.accept(visitor);
	}

}