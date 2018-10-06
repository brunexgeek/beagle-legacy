package beagle.compiler.tree;

import beagle.compiler.SourceLocation;

public interface ITreeElement
{

	public void accept( ITreeVisitor visitor );

	public void accept( ITreeVisitor visitor, ITreeElement child );

	ITreeElement parent();

	void parent(ITreeElement parent);

	SourceLocation location();

	//public void accept( ITreeVisitor visitor, List<ITreeElement> child );

}
