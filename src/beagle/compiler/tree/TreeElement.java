package beagle.compiler.tree;


public abstract class TreeElement implements ITreeElement
{

	@Override
	public void accept(ITreeVisitor visitor, ITreeElement child)
	{
		if (child != null) child.accept(visitor);
	}

	/*@Override
	public <T extends ITreeElement> void accept(ITreeVisitor visitor, List<T> child)
	{
		if (child == null) return;

		for (T item : child)
			item.accept(visitor);
	}*/

}
