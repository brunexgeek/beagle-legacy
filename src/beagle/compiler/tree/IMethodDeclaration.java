package beagle.compiler.tree;

import java.util.List;

public interface IMethodDeclaration extends ITreeElement
{

	public ITypeReference getType();
	
	public List<IFormalParameter> getParameters();
	
	public IName getName();
	
	public IMethodBody getBody();
	
	public boolean isContructor();
	
	public IModifiers getModifiers();
	
	public ITypeBody getParent();
	
	public void setParent( ITypeBody parent );
	
}
