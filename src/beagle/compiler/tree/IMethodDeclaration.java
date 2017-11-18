package beagle.compiler.tree;

import java.util.List;

public interface IMethodDeclaration extends ITreeElement
{

	public IModifiers getModifiers();

	public ITypeReference getReturnType();

	public IName getName();

	public List<IFormalParameter> getParameters();

	public IBlock getBody();

	public boolean isContructor();

	public ITypeBody getParent();

	public void setParent( ITypeBody parent );

	List<IAnnotation> getAnnotations();

}
