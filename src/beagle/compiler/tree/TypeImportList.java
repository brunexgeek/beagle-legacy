package beagle.compiler.tree;

public class TypeImportList extends TreeElementList<ITypeImport> implements ITypeImportList
{

	private static final long serialVersionUID = -753519263293915297L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (ITypeImport item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}