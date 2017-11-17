package beagle.compiler.tree;

import java.util.List;

public interface IBlock extends ITreeElement
{

	List<IStatement> getStatements();

}
