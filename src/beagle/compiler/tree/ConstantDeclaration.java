package beagle.compiler.tree;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class ConstantDeclaration implements IConstantDeclaration
{

	protected IModifiers modifiers;

	protected ITypeReference type;

	protected LinkedList<IFieldVariable> variables;

	protected List<IAnnotation> annotations;

	protected ITypeBody parent;

	public ConstantDeclaration( List<IAnnotation> annotations, ITypeReference type, IName name )
	{
		this.annotations = annotations;
		this.modifiers = null;
		this.type = type;
		this.variables = new LinkedList<>();
		if (name != null)
			this.variables.add( new FieldVariable(name) );
	}

	public ConstantDeclaration( List<IAnnotation> annotations, ITypeReference type )
	{
		this(annotations, type, null);
	}

	@Override
	public void print(PrintStream out, int level)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public IModifiers getModifiers()
	{
		return modifiers;
	}

	@Override
	public ITypeReference getType()
	{
		return type;
	}

	@Override
	public List<IFieldVariable> getVariables()
	{
		return variables;
	}

	public void addVariable( IName name )
	{
		variables.add( new FieldVariable(name) );
	}

	@Override
	public ITypeBody getParent()
	{
		return parent;
	}

	@Override
	public void setParent(ITypeBody parent)
	{
		this.parent = parent;
	}

	@Override
	public List<IAnnotation> getAnnotations()
	{
		return annotations;
	}

}
