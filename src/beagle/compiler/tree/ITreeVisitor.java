package beagle.compiler.tree;

public interface ITreeVisitor
{

	void finish();

	void finish(BooleanLiteral target);

	void finish(Annotation target);

	void finish(AnnotationList target);

	void finish(Block target);

	void finish(CompilationUnit target);

	void finish(ConstantDeclaration target);

	void finish(FormalParameter target);

	void finish(FormalParameterList target);

	void finish(Function target);

	void finish(Modifiers target);

	void finish(Module target);

	void finish(Name target);

	void finish(IntegerLiteral target);

	void finish(Package target);

	void finish(TypeBody target);

	void finish(TypeDeclaration target);

	void finish(TypeDeclarationList target);

	void finish(TypeImport target);

	void finish(TypeImportList target);

	void finish(TypeReference target);

	void finish(TypeReferenceList target);

	void finish(VariableDeclaration target);

	void finish(StringLiteral target);

	boolean visit();

	boolean visit(BooleanLiteral target);

	boolean visit(Annotation target);

	boolean visit(AnnotationList target);

	boolean visit(Block target);

	boolean visit(CompilationUnit target);

	boolean visit(ConstantDeclaration target);

	boolean visit(FormalParameter target);

	boolean visit(FormalParameterList target);

	boolean visit(Function target);

	boolean visit(Modifiers target);

	boolean visit(Module target);

	boolean visit(Name target);

	boolean visit(IntegerLiteral target);

	boolean visit(Package target);

	boolean visit(TypeBody target);

	boolean visit(TypeDeclaration target);

	boolean visit(TypeDeclarationList target);

	boolean visit(TypeImport target);

	boolean visit(TypeImportList target);

	boolean visit(TypeReference target);

	boolean visit(TypeReferenceList target);

	boolean visit(VariableDeclaration target);

	boolean visit(StringLiteral target);

	void finish(NullLiteral target);

	boolean visit(NullLiteral target);

	void finish(NameLiteral target);

	boolean visit(NameLiteral target);

	void finish(UnaryExpression target);

	boolean visit(UnaryExpression target);

	boolean visit(AtomicExpression target);

	void finish(AtomicExpression target);

	boolean visit(BinaryExpression target);

	void finish(BinaryExpression target);

	boolean visit(ExpressionList target);

	void finish(ExpressionList target);

	boolean visit(ReturnStmt target);

	void finish(ReturnStmt target);

	boolean visit(IfThenElseStmt target);

	void finish(IfThenElseStmt target);

	boolean visit(Argument target);

	void finish(Argument target);

	boolean visit(ArgumentList target);

	void finish(ArgumentList target);

	boolean visit(FunctionList functionList);

	void finish(FunctionList functionList);

	void finish(StorageList storageList);

	boolean visit(StorageList storageList);

	boolean visit(StorageDeclaration item);

	boolean visit(Structure structure);

	void finish(Structure structure);

	boolean visit(StructureList structureList);

	void finish(StructureList structureList);
}
