package beagle.compiler.tree;

public interface ITypeImport extends ITreeElement
{

	public String qualifiedName();

	public IName name();

	IPackage namespace();

	void namespace(IPackage value);

	void name(IName value);

}
