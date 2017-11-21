package beagle.compiler.tree;

public interface IName extends Comparable<IName>, ITreeElement
{

	public String qualifiedName();

	public String name( int index );

	public int count();

	public IName slice( int start, int length );

	public IName slice( int start );

	public boolean isQualified();

}
