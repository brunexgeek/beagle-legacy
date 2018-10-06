package beagle.compiler;

import beagle.compiler.tree.BinaryExpression;
import beagle.compiler.tree.BooleanLiteral;
import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.FormalParameter;
import beagle.compiler.tree.FormalParameterList;
import beagle.compiler.tree.Function;
import beagle.compiler.tree.FunctionList;
import beagle.compiler.tree.IExpression;
import beagle.compiler.tree.IntegerLiteral;
import beagle.compiler.tree.StorageDeclaration;
import beagle.compiler.tree.StorageList;
import beagle.compiler.tree.StringLiteral;
import beagle.compiler.tree.Structure;
import beagle.compiler.tree.StructureList;
import beagle.compiler.tree.TypeReference;
import beagle.compiler.tree.UnaryExpression;

public class Semantic
{

	CompilationContext context;

	public Semantic( CompilationContext context )
	{
		this.context = context;
	}

	public void typeInference( CompilationUnit unit )
	{
		// evaluate variables and constants
		typeInference(unit.storages());
		// evaluate structures
		typeInference(unit.structures);
		// evaluate functions
		typeInference(unit.functions);
	}

	public void typeInference( StorageList storages )
	{
		for (StorageDeclaration item : storages )
		{
			if (item.type() != null) continue;
			if (item.initializer() == null)
			{
				context.listener.onError(item.location(), "Missing type or initializer");
				return;
			}
			item.type( evaluateExpression(item.initializer()) );
		}
	}

	public void typeInference( StructureList structures )
	{
		for (Structure item : structures )
			typeInference(item.body.storages);
	}

	public void typeInference( FunctionList functions )
	{
		for (Function function : functions )
		{
			// TODO: iterate recursively into function blocks looking for storages
		}
	}

	public void processStorage( CompilationUnit unit )
	{
		for (StorageDeclaration item : unit.storages() )
		{
			if (item.type() != null) continue;
			if (item.initializer() == null)
			{
				context.listener.onError(item.location(), "Missing type");
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
						context.listener.onError(expr.location(), "Binary expression with arguments of different type");
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
