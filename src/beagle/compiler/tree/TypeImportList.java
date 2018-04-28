package beagle.compiler.tree;

public class TypeImportList extends TreeElementList<TypeImport>
{

	private static final long serialVersionUID = -753519263293915297L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (TypeImport item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}