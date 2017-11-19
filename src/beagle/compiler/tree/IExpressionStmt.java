package beagle.compiler.tree;

public interface IExpressionStmt extends IStatement
{

	IExpression expression();

	void expression(IExpression value);

}
