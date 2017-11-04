package beagle.compiler.tree;

public interface IModifiers extends ITreeElement
{

	public static int PUBLIC = 0x01;

	public static int PROTECTED = 0x02;

	public static int PRIVATE = 0x04;

	public static int INTERNAL = 0x08;

	public static int PACKAGE = 0x10;

	public static int STATIC = 0x20;

	public int getModifiers();

	public boolean hasModifier( int flags );

}
