package beagle.compiler.tree;

public class FormalParameterList extends TreeElementList<FormalParameter>
{

	private static final long serialVersionUID = -6858099182458630730L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (FormalParameter item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}
