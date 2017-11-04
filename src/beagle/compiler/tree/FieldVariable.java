package beagle.compiler.tree;

public class FieldVariable implements IFieldVariable
{

	protected IName name;
	
	public FieldVariable( IName name )
	{
		this.name = name;
	}
	
	@Override
	public IName getName()
	{
		return name;
	}

}
