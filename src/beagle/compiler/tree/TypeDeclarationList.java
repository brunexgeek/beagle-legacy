package beagle.compiler.tree;

public class TypeDeclarationList extends TreeElementList<ITypeDeclaration> implements ITypeDeclarationList
{

	private static final long serialVersionUID = -8292374162255444568L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (ITypeDeclaration item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}
