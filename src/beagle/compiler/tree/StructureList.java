package beagle.compiler.tree;

public class StructureList extends TreeElementList<Structure>
{

	private static final long serialVersionUID = -7528096390537975721L;

	public StructureList()
	{
		super();
	}

	public StructureList(Structure... items)
	{
		super();
		for (Structure item : items)
			add(item);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (Structure item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}
