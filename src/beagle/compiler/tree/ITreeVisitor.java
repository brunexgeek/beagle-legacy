package beagle.compiler.tree;

public interface ITreeVisitor
{

	void finish(IAnnotation annotation);

	void finish(IBlock block);

	void finish(ICompilationUnit compilationUnit);

	void finish(IConstantDeclaration constantDeclaration);

	void finish(IFormalParameter formalParameter);

	void finish(IMethodDeclaration methodDeclaration);

	void finish(IModifiers modifiers);

	void finish(IModule module);

	void finish(IPackage package1);

	void finish(ITypeBody body);

	void finish(ITypeDeclaration typeDeclaration);

	void finish(ITypeImport typeImport);

	void finish(IVariableDeclaration variableDeclaration);

	void finish(TypeReference typeReference);

	boolean visit(IAnnotation annotation);

	boolean visit(IBlock block);

	boolean visit(ICompilationUnit compilationUnit);

	boolean visit(IConstantDeclaration constantDeclaration);

	boolean visit(IFormalParameter formalParameter);

	boolean visit(IMethodDeclaration methodDeclaration);

	boolean visit(IModifiers modifiers);

	boolean visit(IModule module);

	boolean visit(IPackage package1);

	boolean visit(ITypeBody body);

	boolean visit(ITypeDeclaration typeDeclaration);

	boolean visit(ITypeImport typeImport);

	boolean visit(ITypeReference typeReference);

	boolean visit(IVariableDeclaration variableDeclaration);
}
