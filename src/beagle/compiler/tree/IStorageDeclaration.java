package beagle.compiler.tree;

public interface IStorageDeclaration extends ITreeElement, IStatement
{

	IAnnotationList annotations();

	IModifiers modifiers();

	void modifiers(IModifiers value);

	IName name();

	void name(IName value);

	ITypeReference type();

	void type(ITypeReference value);

	IExpression initializer();

	void initializer(IExpression value);

}
