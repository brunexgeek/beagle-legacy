package beagle.compiler.tree;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class FieldDeclaration implements IFieldDeclaration
{

	protected IModifiers modifiers;

	protected ITypeReference type;

	protected LinkedList<IFieldVariable> variables;

	protected List<IAnnotation> annotations;

	protected ITypeBody parent;

	public FieldDeclaration( List<IAnnotation> annotations, IModifiers modifiers, ITypeReference type, IName name )
	{
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.type = type;
		this.variables = new LinkedList<>();
		if (name != null)
			this.variables.add( new FieldVariable(name) );
	}

	public FieldDeclaration( List<IAnnotation> annotations, IModifiers modifiers, ITypeReference type )
	{
		this(annotations, modifiers, type, null);
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
