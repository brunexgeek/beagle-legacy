package beagle.compiler.tree;

import java.util.List;

public class CompilationUnit extends TreeElement implements ICompilationUnit
{

	private String fileName;

	private ITypeImportList importList;

	private IPackage pack;

	private ITypeDeclarationList typeList;

	public CompilationUnit( String fileName, IPackage pack )
	{
		this.fileName = fileName;
		this.pack = pack;
		importList = new TypeImportList();
		typeList = new TypeDeclarationList();
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, pack);
			accept(visitor, importList);
			accept(visitor, typeList);
		}
		visitor.finish(this);
	}

	@Override
	public void filename(String value)
	{
		this.fileName = value;
	}

	@Override
	public String fileName()
	{
		return fileName;
	}

	@Override
	public List<ITypeImport> imports()
	{
		return importList;
	}

	@Override
	public IPackage namespace()
	{
		return pack;
	}

	@Override
	public void namespace(IPackage value)
	{
		this.pack = value;
	}

	@Override
	public ITypeDeclarationList types()
	{
		return typeList;
	}

}
