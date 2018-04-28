package beagle.compiler.tree;

import java.util.HashMap;
import java.util.Map;

public class Module extends TreeElement
{

	protected Name name;

	protected HashMap<String, CompilationUnit> units;

	public Module( Name name )
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

	public Map<String, CompilationUnit> compilationUnits()
	{
		return units;
	}

	public Name name()
	{
		return name;
	}

	public void addCompilationUnit(CompilationUnit unit)
	{
		units.put(unit.fileName(), unit);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		visitor.visit(this);
		for (CompilationUnit item : units.values())
			item.accept(visitor);
	}

}
