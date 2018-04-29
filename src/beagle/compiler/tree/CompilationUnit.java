package beagle.compiler.tree;

public class CompilationUnit extends TreeElement
{

	private String fileName;

	private TypeImportList importList;

	private Package pack;

	private TypeDeclarationList typeList;

	private FunctionList functionList;

	public CompilationUnit( String fileName, Package pack )
	{
		this.fileName = fileName;
		this.pack = pack;
		importList = new TypeImportList();
		typeList = new TypeDeclarationList();
		functionList = new FunctionList();
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
		{
			accept(visitor, pack);
			accept(visitor, importList);
			accept(visitor, typeList);
			accept(visitor, functionList);
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

	public FunctionList functions()
	{
		return functionList;
	}

}
