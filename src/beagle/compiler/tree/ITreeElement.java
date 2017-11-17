package beagle.compiler.tree;

public interface ITreeElement
{

	public void accept( ITreeVisitor visitor );

	public void accept( ITreeVisitor visitor, ITreeElement child );

	//public void accept( ITreeVisitor visitor, List<ITreeElement> child );

}
