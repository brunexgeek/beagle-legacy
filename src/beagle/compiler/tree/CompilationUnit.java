package beagle.compiler.tree;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class CompilationUnit implements ICompilationUnit
{

	private IPackage pack;
	
	private Map<String, ITypeDeclaration> importedTypes;
	
	private Map<String, IPackage> importedPackages;
	
	private Map<String, ITypeDeclaration> types;
	
	private String fileName;
	
	public CompilationUnit( String fileName, IPackage pack )
	{
		this.fileName = fileName;
		this.pack = pack;
		importedPackages = new HashMap<String, IPackage>();
		importedTypes = new HashMap<String, ITypeDeclaration>();
		types = new HashMap<String, ITypeDeclaration>();
	}
	
	@Override
	public IPackage getPackage()
	{
		return pack;
	}

	@Override
	public Map<String, ITypeDeclaration> getImportedTypes()
	{
		return importedTypes;
	}

	@Override
	public Map<String, IPackage> getImportedPackages()
	{
		return importedPackages;
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

	@Override
	public void print( PrintStream out, int level )
	{
		Printer.indent(out, level);
		out.print("[");
		out.print(getClass().getSimpleName());
		out.println("]");
		pack.print(out, level + 1);

		Printer.indent(out, level + 1);
		out.println("[Imports]");
		
		for (ITypeDeclaration current : importedTypes.values())
			current.print(out, level + 2);

		for (IPackage current : importedPackages.values())
			current.print(out, level + 2);
		
		for (ITypeDeclaration current : types.values())
			current.print(out, level + 1);
	}

	@Override
	public void addImport(ITypeImport typeImport)
	{
		if (typeImport == null) return;
		
		ITypeDeclaration type = typeImport.getType();
		if (type != null)
			importedTypes.put(type.getQualifiedName(), type);
		else
		{
			IPackage pack = typeImport.getPackage();
			importedPackages.put(pack.getQualifiedName(), pack);
		}
	}

	@Override
	public void addType(ITypeDeclaration type)
	{
		if (type == null) return;
		
		types.put(type.getQualifiedName(), type);
	}

}
