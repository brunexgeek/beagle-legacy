package beagle.compiler.tree;

public class TypeBody extends TreeElement
{

	public StorageList storages;

	public FunctionList functions;

	public TypeDeclaration parent;

	public TypeBody()
	{
		this(null);
	}

	public TypeBody( TypeDeclaration parent )
	{
		this.functions = new FunctionList();
		this.storages = new StorageList();
		this.parent = parent;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			for (StorageDeclaration item : storages)
				item.accept(visitor);
			for (Function item : functions)
				item.accept(visitor);
		}
		visitor.finish(this);
	}


}
