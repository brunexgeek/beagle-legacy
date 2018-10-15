package beagle.compiler;

import beagle.compiler.tree.AtomicExpression;
import beagle.compiler.tree.BinaryExpression;
import beagle.compiler.tree.Block;
import beagle.compiler.tree.BooleanLiteral;
import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.Function;
import beagle.compiler.tree.FunctionList;
import beagle.compiler.tree.IExpression;
import beagle.compiler.tree.IStatement;
import beagle.compiler.tree.IntegerLiteral;
import beagle.compiler.tree.NameLiteral;
import beagle.compiler.tree.ReturnStmt;
import beagle.compiler.tree.StorageDeclaration;
import beagle.compiler.tree.StorageList;
import beagle.compiler.tree.StringLiteral;
import beagle.compiler.tree.Structure;
import beagle.compiler.tree.StructureList;
import beagle.compiler.tree.TypeReference;
import beagle.compiler.tree.UnaryExpression;
import jdk.nashorn.internal.ir.FunctionCall;

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
			typeInference(item);
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
			typeInference(function);
		}
	}

	public void typeInference( Function function )
	{
		typeInference(function.body(), function);
	}

	public void typeInference( Block block, Function function )
	{
		for (IStatement statement: block )
		{
			if (statement instanceof Block) typeInference((Block) statement, function);
			if (statement instanceof StorageDeclaration) typeInference((StorageDeclaration) statement);
			if (statement instanceof ReturnStmt)
			{
				TypeReference type = evaluateExpression(((ReturnStmt) statement).expression());
				if (function.returnType() == null)
					function.returnType(type);
				else
				if (!function.returnType().equals(type))
				{
					context.listener.onError(statement.location(), "Returning value with wrong type");
					return;
				}
			}
		}
	}

	public void typeInference( StorageDeclaration storage )
	{
		if (storage.type() != null) return;
		if (storage.initializer() == null)
		{
			context.listener.onError(storage.location(), "Missing type or initializer");
			return;
		}
		storage.type( evaluateExpression(storage.initializer()) );
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

			if (left != right)
			{
				context.listener.onError(expr.location(), "Binary expression with arguments of different type");
				return null;
			}
			return left;
		}
		else
		if (expr instanceof UnaryExpression)
		{
			return evaluateExpression(((UnaryExpression)expr).expression());
		}
		else
		if (expr instanceof NameLiteral)
			return TypeReference.INT32;
		else
		if (expr instanceof AtomicExpression)
			return evaluateExpression(((AtomicExpression)expr).value());

		context.listener.onError(null, "Unrecognized expression");
		return null;
	}

}
