package beagle.compiler.tree;

public class NullLiteral extends TreeElement implements IExpression
{

	@Override
	public void accept(ITreeVisitor visitor)
	{
		visitor.visit(this);
		visitor.finish(this);
	}


}
