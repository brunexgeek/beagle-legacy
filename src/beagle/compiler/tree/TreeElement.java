package beagle.compiler.tree;

import beagle.compiler.SourceLocation;

public abstract class TreeElement implements ITreeElement
{

	protected ITreeElement parent;

	protected SourceLocation location;

	@Override
	public void accept(ITreeVisitor visitor, ITreeElement child)
	{
		if (child != null) child.accept(visitor);
	}

	@Override
	public ITreeElement parent()
	{
		return parent;
	}

	@Override
	public void parent(ITreeElement parent)
	{
		this.parent = parent;
	}

	@Override
	public SourceLocation location()
	{
		return location;
	}

	public void location( SourceLocation location )
	{
		this.location = location;
	}

	/*@Override
	public <T extends ITreeElement> void accept(ITreeVisitor visitor, List<T> child)
	{
		if (child == null) return;

		for (T item : child)
			item.accept(visitor);
	}*/

}
