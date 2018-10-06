package beagle.compiler.tree;

public class CompilationUnit extends TreeElement
{

	private String fileName;

	private TypeImportList importList;

	private Package pack;

	private TypeDeclarationList typeList;

	public FunctionList functions;

	/**
	 * Global variables and constants.
	 */
	private StorageList storage;

	public StructureList structures;

	public CompilationUnit( String fileName, Package pack )
	{
		this.fileName = fileName;
		this.pack = pack;
		importList = new TypeImportList();
		typeList = new TypeDeclarationList();
		functions = new FunctionList();
		storage = new StorageList();
		structures = new StructureList();
	}

	public StorageList storages()
	{
		return storage;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, pack);
			accept(visitor, importList);
			accept(visitor, typeList);
			accept(visitor, functions);
		}
		visitor.finish(this);
	}

	public void filename(String value)
	{
		this.fileName = value;
	}

	public String fileName()
	{
		return fileName;
	}

	public TypeImportList imports()
	{
		return importList;
	}

	public Package namespace()
	{
		return pack;
	}

	public void namespace(Package value)
	{
		this.pack = value;
	}

	public TypeDeclarationList types()
	{
		return typeList;
	}

}
