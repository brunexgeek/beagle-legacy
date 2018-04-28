package beagle.compiler.tree;

public class TypeReferenceList extends TreeElementList<TypeReference>
{
	private static final long serialVersionUID = -8292374162255444568L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (TypeReference item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}
