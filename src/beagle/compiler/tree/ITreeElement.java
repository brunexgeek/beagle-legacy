package beagle.compiler.tree;

public interface ITreeElement
{

	public void accept( ITreeVisitor visitor );

	public void accept( ITreeVisitor visitor, ITreeElement child );

	ITreeElement parent();

	void parent(ITreeElement parent);

	//public void accept( ITreeVisitor visitor, List<ITreeElement> child );

}
