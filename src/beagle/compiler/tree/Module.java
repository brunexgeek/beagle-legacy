package beagle.compiler.tree;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Module implements IModule
{

	protected IName name;
	
	protected HashMap<String, ICompilationUnit> units;
	
	public Module( IName name )
	{
		this.name = name;
		this.units = new HashMap<>();
	}
	
	@Override
	public void print(PrintStream out, int level)
	{
		Printer.indent(out, level);
		out.print("[");
		out.print(getClass().getSimpleName());
		out.print("]  ");
		Printer.print(out, "value", name.getQualifiedName());
		out.println();
	}

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

}
