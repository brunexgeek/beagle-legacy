package beagle.compiler.tree;

import java.util.List;

public class CompilationUnit extends TreeElement implements ICompilationUnit
{

	private IPackage pack;

	private ITypeImportList importList;

	private ITypeDeclarationList typeList;

	private String fileName;

	public CompilationUnit( String fileName, IPackage pack )
	{
		this.fileName = fileName;
		this.pack = pack;
		importList = new TypeImportList();
		typeList = new TypeDeclarationList();
	}

	@Override
	public IPackage getPackage()
	{
		return pack;
	}

	@Override
	public List<ITypeImport> getImports()
	{
		return importList;
	}

	@Override
	public ITypeDeclarationList getTypes()
	{
		return typeList;
	}

	@Override
	public String getFileName()
	{
		return fileName;
	}

	@Override
	public ITypeDeclaration findImportedType(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addImport(ITypeImport typeImport)
	{
		if (typeImport == null) return;

		importList.add(typeImport);

		/*ITypeDeclaration type = typeImport.getType();
		if (type != null)
			importedTypes.put(type.getQualifiedName(), type);
		else
		{
			IPackage pack = typeImport.getPackage();
			importedPackages.put(pack.getQualifiedName(), pack);
		}*/
	}

	@Override
	public void addType(ITypeDeclaration type)
	{
		if (type == null) return;

		typeList.add(type);
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

}
