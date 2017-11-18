package beagle.compiler.tree;

import java.util.LinkedList;

public class TypeReferenceList extends LinkedList<ITypeReference> implements ITypeReferenceList
{
	private static final long serialVersionUID = -8292374162255444568L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (ITypeReference item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

	@Override
	public void accept(ITreeVisitor visitor, ITreeElement child)
	{
		if (child != null) child.accept(visitor);

	}

}
