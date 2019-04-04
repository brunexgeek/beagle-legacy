
namespace beagle.compiler.tree {


export class CompilationUnit
{
    fileName : string;
	importList : TypeImport[] = [];
	typeList : TypeDeclaration[] = [];
	functions : Function[] = [];
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

class TypeReference
{
    package : Package;
	type : TypeDeclaration;
	isPrimitive : boolean = false;
}

class TypeBody
{
    storages : StorageDeclaration[];
	functions : Function[];
	parent : TypeDeclaration;
}

class StorageDeclaration implements IStatement
{
	type : TypeReference;
	name : Name;
	initializer : IExpression;
}

interface IExpression
{
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


export class Decorator
{
	name : Name;
}

export function createDecorator( name : Name ) : Decorator
{
	let temp = new Decorator();
	temp.name = name;
	return temp;
}

}
