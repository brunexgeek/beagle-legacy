package beagle.compiler.tree;

public class StorageList extends TreeElementList<StorageDeclaration>
{

	private static final long serialVersionUID = -6380763666101108206L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			for (StorageDeclaration item : this)
				visitor.visit(item);
		}
		visitor.finish(this);
	}

}
