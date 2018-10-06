package beagle.compiler.tree;

import java.util.LinkedList;

import beagle.compiler.SourceLocation;

public abstract class TreeElementList<T extends ITreeElement> extends LinkedList<T> implements ITreeElementList<T>
{

	private static final long serialVersionUID = -7963184275285204232L;

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
	public boolean add(T value)
	{
		if (value == null) return false;
		if (super.add(value))
		{
			value.parent(this);
			return true;
		}
		return false;
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
}
