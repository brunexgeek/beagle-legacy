package beagle.compiler.tree;

public class TypeReferenceList extends TreeElementList<ITypeReference> implements ITypeReferenceList
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

}
