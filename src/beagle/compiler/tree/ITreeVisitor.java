package beagle.compiler.tree;

public interface ITreeVisitor
{

	void finish();

	void finish(IAnnotation target);

	void finish(IAnnotationList target);

	void finish(IBlock target);

	void finish(ICompilationUnit target);

	void finish(IConstantDeclaration target);

	void finish(IFormalParameter target);

	void finish(IMethodDeclaration target);

	void finish(IModifiers target);

	void finish(IModule target);

	void finish(IName name);

	void finish(IPackage target1);

	void finish(ITypeBody target);

	void finish(ITypeDeclaration target);

	void finish(ITypeDeclarationList target);

	void finish(ITypeImport target);

	void finish(ITypeImportList target);

	void finish(ITypeReference target);

	void finish(ITypeReferenceList target);

	void finish(IVariableDeclaration target);

	boolean visit();

	boolean visit(IAnnotation target);

	boolean visit(IAnnotationList target);

	boolean visit(IBlock target);

	boolean visit(ICompilationUnit target);

	boolean visit(IConstantDeclaration target);

	boolean visit(IFormalParameter target);

	boolean visit(IMethodDeclaration target);

	boolean visit(IModifiers target);

	boolean visit(IModule target);

	boolean visit(IName name);

	boolean visit(IPackage target);

	boolean visit(ITypeBody target);

	boolean visit(ITypeDeclaration target);

	boolean visit(ITypeDeclarationList target);

	boolean visit(ITypeImport target);

	boolean visit(ITypeImportList target);

	boolean visit(ITypeReference target);

	boolean visit(ITypeReferenceList target);

	boolean visit(IVariableDeclaration target);
}
