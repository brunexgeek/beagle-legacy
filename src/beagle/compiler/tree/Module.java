package beagle.compiler.tree;

import java.util.HashMap;
import java.util.Map;

public class Module extends TreeElement implements IModule
{

	protected IName name;

	protected HashMap<String, ICompilationUnit> units;

	public Module( IName name )
	{
		this.name = name;
		this.units = new HashMap<>();
	}

//	@Override
//	public void print(Printer out, int level)
//	{
//		out.printTag(getClass().getSimpleName(), level);
//		out.printAttribute("value", name.getQualifiedName());
//		out.println();
//	}

	@Override
	public Map<String, ICompilationUnit> getCompilationUnits()
	{
		return units;
	}

	@Override
	public IName getName()
	{
		return name;
	}

	@Override
	public void addCompilationUnit(ICompilationUnit unit)
	{
		units.put(unit.getFileName(), unit);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		visitor.visit(this);
		for (ICompilationUnit item : units.values())
			item.accept(visitor);
	}

}
