package beagle.compiler.tree;

import java.util.List;

public class TypeBody extends TreeElement
{

	public List<StorageDeclaration> storages;

	public List<Function> functions;

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
