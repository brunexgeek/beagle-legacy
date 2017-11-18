package beagle.compiler.tree;

public interface IFormalParameter extends ITreeElement
{

	public IName name();

	public ITypeReference type();

	void name(IName value);

	void type(ITypeReference value);

}
