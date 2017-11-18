package beagle.compiler.tree;

public interface IMethodDeclaration extends ITreeElement
{

	IModifiers modifiers();

	ITypeReference returnType();

	IName name();

	IFormalParameterList parameters();

	IBlock body();

	boolean isContructor();

	ITypeBody parent();

	void parent( ITypeBody parent );

	IAnnotationList annotations();

	void returnType(ITypeReference value);

	void name(IName value);

	void body(IBlock value);

	void modifiers(IModifiers value);

}
