package beagle.compiler.tree;

public class Block extends TreeElementList<IStatement> implements IBlock
{

	private static final long serialVersionUID = -7176997723846769980L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			for (IStatement item : this)
				item.accept(visitor);
		}
		visitor.finish(this);
	}

}
