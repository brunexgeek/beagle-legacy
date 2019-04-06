
namespace beagle.compiler.tree {


export class CompilationUnit
{
    fileName : string;
	importList : TypeImport[];
	members : Namespace;
}

export class Comment
{
	text : string;
	isDoc : boolean;

	constructor( text : string, isDoc : boolean = false )
	{
		this.text = text;
		this.isDoc = isDoc;
	}
}

export class Name
{
    names : string[] = [];
	qualifiedName : string;
	location : SourceLocation;
}

export function createName(value : string) : Name
{
    if (value === "") return null;

    let output = new Name();
    output.names.push(value);
    output.qualifiedName = value;
    return output;
}

export function appendName(self : Name, value : string )
{
    self.names.push(value);
    self.qualifiedName = self.qualifiedName + "." + value;
}

export function isQualified( self : Name ) : boolean
{
	return self.names.length > 1;
}

class Package
{
    name : Name;
    types : TypeDeclaration[];
}

export class TypeImport
{
    package : Package;
	name : Name;
	isWildcard : boolean;
	alias : Name;
}

export function createTypeImport( name : Name, isWildcard : boolean = false, alias : Name = null ) : TypeImport
{
	let temp = new TypeImport;
	temp.alias = alias;
	temp.name = name;
	temp.isWildcard = isWildcard;
	return temp;
}

class TypeDeclaration
{
    package : Package;
	isComplete : boolean;
	parent : CompilationUnit;
	name : Name;
	inherit : TypeReference[];
	body : TypeBody;
}

export class TypeReference
{
    package : Package;
	type : TypeDeclaration;
	isPrimitive : boolean = false;
}

export function createTypeReference( name : Name ) : TypeReference
{
	let temp = new TypeReference();
	temp.type = null;
	temp.package = null;
	temp.isPrimitive = false;
	return temp;
}

class TypeBody
{
    storages : StorageDeclaration[];
	functions : Function[];
	parent : TypeDeclaration;
}

export class StorageDeclaration implements IStatement
{
	type : TypeReference;
	name : Name;
	initializer : IExpression;
	isConst : boolean;
	annots : Annotation[];
}

export function createStorageDeclaration( annots : Annotation[], name : Name, type : TypeReference, isConst : boolean, expr : IExpression = null ) : StorageDeclaration
{
	let temp = new StorageDeclaration();
	temp.annots = annots;
	temp.name = name;
	temp.type = type;
	temp.initializer = expr;
	temp.isConst = isConst;
	return temp;
}

export class Function
{
	name : Name;
	parameters : FormalParameter[];
	type : TypeReference;
	body : IStatement[];
	parent : CompilationUnit | TypeDeclaration;
}

interface IStatement
{
}

class FormalParameter
{
    type : TypeReference;
    name : Name;
}


export class Annotation
{
	name : Name;
}

export function createAnnotation( name : Name ) : Annotation
{
	let temp = new Annotation();
	temp.name = name;
	return temp;
}

export class Namespace
{
	name : Name;
	structures : Structure[];
	functions : Function[];
	storages : StorageDeclaration[];
	namespaces : Namespace[];
}

export function createNamespace( name : Name) : Namespace
{
	let temp = new Namespace();
	temp.functions = [];
	temp.storages = [];
	temp.structures = [];
	temp.namespaces = [];
	temp.name = name;
	return temp;
}

export interface IExpression
{
}

export class BinaryExpression implements IExpression
{
	public left : IExpression;
	public right : IExpression;
	public operation : TokenType;

	public constructor(left : IExpression, type : TokenType, right : IExpression )
	{
		this.left = left;
		this.right = right;
		this.operation = type;
	}
}

export class NameLiteral
{
	value : Name;
}

export function createNameLiteral( value : Name ) : NameLiteral
{
	let temp = new NameLiteral();
	temp.value = value;
	return temp;
}

export enum UnaryDirection
{
	PREFIX,
	POSTFIX
}

export class UnaryExpression implements IExpression
{
	operation : TokenType;
	direction : UnaryDirection;
	expression : IExpression;
	extra : IExpression = null;
}


export function createUnaryExpression(expression : IExpression, operation : TokenType, direction : UnaryDirection) : UnaryExpression
{
	let temp = new UnaryExpression();
	this.operation = operation;
	this.expression = expression;
	this.direction = direction;
	return temp;
}


export class ArgumentList
{
	args : Argument[];
}

export function createArgumentList( item : Argument = null ) : ArgumentList
{
	let temp = new ArgumentList();
	this.args = [];
	if (item != null) this.args.push(item);
	return temp;
}

export class Argument
{
	name : Name;
	value : IExpression;
}

export function createArgument( name : Name, value : IExpression ) : Argument
{
	let temp = new Argument();
	this.name = name;
	this.value = value;
	return temp;
}

export class ExpressionList
{
	expressions : IExpression[];
}

export function createExpressionList( item : IExpression = null ) : ExpressionList
{
	let temp = new ExpressionList();
	this.expressions = [];
	if (item != null) this.args.push(item);
	return temp;
}

export class AtomicExpression
{
	name : Name;
	value : IExpression;
}

export function createAtomicExpression( value : IExpression ) : AtomicExpression
{
	let temp = new AtomicExpression();
	this.value = value;
	return temp;
}

export class NullLiteral
{
}

export class IntegerLiteral
{
	value : string;
	base : number;
}

export function createIntegerLiteral( value : string, base : number ) : IntegerLiteral
{
	let temp = new IntegerLiteral();
	this.value = value;
	this.base = base;
	return temp;
}

export class BooleanLiteral
{
	value : boolean;
}

export function createBooleanLiteral( value : boolean ) : BooleanLiteral
{
	let temp = new BooleanLiteral();
	this.value = value;
	return temp;
}

export class StringLiteral
{
	value : string;
}

export function createStringLiteral( value : string ) : StringLiteral
{
	let temp = new StringLiteral();
	this.value = value;
	return temp;
}

export class FloatLiteral
{
	value : string;
}

export function createFloatLiteral( value : string ) : FloatLiteral
{
	let temp = new FloatLiteral();
	this.value = value;
	return temp;
}

export enum AccessMode
{
	PUBLIC,
	PROTECTED,
	PRIVATE
}

export class Structure
{
	annotations : Annotation[];
	access : AccessMode;
	name : Name;
	parent : TypeReference;
	properties : Property[];
}

export function createStructure() : Structure
{
	let temp = new Structure();
	temp.annotations = [];
	temp.access = AccessMode.PRIVATE;
	temp.name = null;
	temp.parent = null;
	temp.properties = [];
	return temp;
}

export class Property
{
	name : Name;
	access : AccessMode;
	annotations : Annotation[];
	type : TypeReference;
	initializer : IExpression;
}

export function createProperty( annots : Annotation[], access : AccessMode, name : Name,
	type : TypeReference, initializer : IExpression = null ) : Property
{
	let temp = new Property();
	temp.name = name;
	temp.access = access;
	temp.annotations = annots;
	temp.type = type;
	temp.initializer = initializer;
	return temp;
}

}
