package beagle.compiler.tree;

public class FunctionList extends TreeElementList<Function>
{

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (Function item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}
