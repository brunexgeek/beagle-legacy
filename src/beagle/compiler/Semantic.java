package beagle.compiler;

import beagle.compiler.tree.BinaryExpression;
import beagle.compiler.tree.BooleanLiteral;
import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.IExpression;
import beagle.compiler.tree.IntegerLiteral;
import beagle.compiler.tree.StorageDeclaration;
import beagle.compiler.tree.StringLiteral;
import beagle.compiler.tree.TypeReference;
import beagle.compiler.tree.UnaryExpression;

public class Semantic
{

	CompilationContext context;

	public Semantic( CompilationContext context )
	{
		this.context = context;
	}

	public void processStogare( CompilationUnit unit )
	{
		for (StorageDeclaration item : unit.storages() )
		{
			if (item.type() != null) continue;
			if (item.initializer() == null)
			{
				context.listener.onError(null, "Missing type");
				return;
			}

			item.type( evaluateExpression(item.initializer()) );
		}
	}

	TypeReference evaluateExpression( IExpression expr )
	{
		if (expr instanceof StringLiteral)
		{
			return TypeReference.STRING;
		}
		else
		if (expr instanceof BooleanLiteral)
		{
			return TypeReference.BOOL;
		}
		else
		if (expr instanceof IntegerLiteral)
		{
			return TypeReference.INT32;
		}
		else
		if (expr instanceof BinaryExpression)
		{
			TypeReference left = evaluateExpression(((BinaryExpression)expr).left());
			TypeReference right= evaluateExpression(((BinaryExpression)expr).right());

			switch (((BinaryExpression)expr).operation())
			{
				case TOK_LE:
				case TOK_LT:
				case TOK_GE:
				case TOK_GT:
					if (left != right)
					{
						context.listener.onError(null, "Binary expression with argument of different type");
						return null;
					}
					return TypeReference.BOOL;
				default:
			}
			context.listener.onError(null, "Unhandled binary expression");
			return null;
		}
		else
		if (expr instanceof UnaryExpression)
		{
			return evaluateExpression(((UnaryExpression)expr).expression());
		}

		context.listener.onError(null, "Unrecognized expression");
		return null;
	}

}
