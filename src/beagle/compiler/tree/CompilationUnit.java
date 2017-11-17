package beagle.compiler.tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CompilationUnit extends TreeElement implements ICompilationUnit
{

	private IPackage pack;

	private List<ITypeImport> importList;

	private Map<String, ITypeDeclaration> types;

	private String fileName;

	public CompilationUnit( String fileName, IPackage pack )
	{
		this.fileName = fileName;
		this.pack = pack;
		importList = new LinkedList<>();
		types = new HashMap<String, ITypeDeclaration>();
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
	public Map<String, ITypeDeclaration> getTypes()
	{
		return types;
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

	/*@Override
	public void print( Printer out, int level )
	{
		out.printTag(getClass().getSimpleName(), level);
		pack.print(out, level + 1);

		out.printTag("Imports", level + 1);
		out.println();

		for (ITypeDeclaration current : importedTypes.values())
			current.print(out, level + 2);

		for (IPackage current : importedPackages.values())
			current.print(out, level + 2);

		for (ITypeDeclaration current : types.values())
			current.print(out, level + 1);
	}*/

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

		types.put(type.getQualifiedName(), type);
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, pack);
			for (ITypeImport item : importList)
				item.accept(visitor);
			for (ITypeDeclaration item : types.values())
				item.accept(visitor);
		}
		visitor.finish(this);
	}

}
