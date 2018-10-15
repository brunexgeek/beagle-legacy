package beagle.compiler.tree;

public class ForEachStmt extends TreeElement implements IStatement
{

	public IStatement statement;

	public StorageDeclaration iterator;

	public IExpression expression;

	public ForEachStmt(StorageDeclaration iterator, IExpression expression, IStatement statement)
	{
		this.iterator = iterator;
		this.expression = expression;
		this.statement = statement;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		visitor.finish(this);
	}

}
