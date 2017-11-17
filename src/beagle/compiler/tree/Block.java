package beagle.compiler.tree;

import java.util.LinkedList;
import java.util.List;

public class Block extends TreeElement implements IBlock
{

	List<IStatement> statements;

	public Block()
	{
		statements = new LinkedList<>();
	}

	@Override
	public List<IStatement> getStatements()
	{
		return statements;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			for (IStatement item : statements)
				item.accept(visitor);
		}
		visitor.finish(this);
	}

}
