package beagle.compiler.tree;

public interface IName extends Comparable<IName>, ITreeElement
{

	public String getQualifiedName();

	public String getName( int index );

	public int getCount();

	public IName slice( int start, int length );

	public IName slice( int start );

	public boolean isQualified();

}
