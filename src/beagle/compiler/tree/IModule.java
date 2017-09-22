package beagle.compiler.tree;

import java.util.Map;

public interface IModule extends ITreeElement
{

	public Map<String, ICompilationUnit> getCompilationUnits();
	
	public IName getName();

	public void addCompilationUnit(ICompilationUnit unit);
	
}
