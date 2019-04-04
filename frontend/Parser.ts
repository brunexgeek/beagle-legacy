/// <reference path="tree.ts" />
/// <reference path="ScanString.ts" />
/// <reference path="context.ts" />

namespace beagle.compiler {


/**
 * Ring array of tokens used to get tokens from input data
 * and to perform lookahead searchs.
 */
export class TokenArray
{
	private scanner : Scanner;
	private buffer : Token[] = [];
	private current : number;
	private size : number;

	public constructor( scanner : Scanner, size : number = 5)
	{
		this.current = 0;
		this.size = Math.max(5, size);
		this.buffer.length = this.size;
		this.scanner = scanner;

		// fill the ring array with tokens
		for (let i = 0; i < size; ++i)
			if ((this.buffer[i] = scanner.readToken()) == null)
				break;
	}

	/**
	 * Return the current token and advances the cursor.
	 *
	 * @return
	 */
	public read() : Token
	{
		if (this.buffer[this.current] != null)
		{
			let value = this.buffer[this.current];
			this.buffer[this.current] = this.scanner.readToken();
			this.current = (this.current + 1) % this.size;
			return value;
		}
		return null;
	}

	public peek() : Token
	{
		return this.peekAt(0);
	}

	public peekAt( index : number ) : Token
	{
		let pos = (this.current + index) % this.size;
		return this.buffer[pos];
	}

	public peekType() : TokenType
	{
		return this.peekTypeAt(0);
	}

	public peekTypeAt( index : number ) : TokenType
	{
		let result = this.peekAt(index);
		if (result == null)
			return null;
		else
			return result.type;
	}

	/**
	 * Check if the next tokens in the input sequence corresponds to the given ones.
	 *
	 * If isRequired is true and the lookahead fails, this function send a
	 * compilation error to the current listener before returns.
	 *
	 * @param types
	 * @return
	 */
	public lookahead( isRequired : boolean, ...types : TokenType[] ) : boolean
	{
		if (types.length == 0)
			return false;
		if (types.length >= this.size)
			return false;

		let first = this.buffer[this.current];
		let count = types.length;

		for (let i = 0; i < count; ++i)
		{
			let pos = (this.current + i) % this.size;
			let entry = this.buffer[pos];
			if (i != 0 && entry == first)
				return false;

			if (entry == null || entry.type != types[i])
				return false;
		}
		return true;
	}

	public discard( count : number = 1 )
	{
        for (let i = 0; i < count; ++i) this.read();
	}

	public discardIf( type : TokenType ) : boolean
	{
		let current = this.peek();
		if (current != null && current.type == type)
		{
			this.read();
			return true;
		}
		else
		{
			this.scanner.context.listener.onError(null, "Syntax error, expected '" + type + "'");
			return false;
		}
	}
}



export class Parser
{
	private fileName : string;
	private tokens : TokenArray;
	private context : CompilationContext;

	public constructor( context : CompilationContext, scanner : Scanner )
	{
		this.fileName = "";
		this.tokens = new TokenArray(scanner, 16);
		this.context = context;
	}

	private expectedOneOf( ...types : TokenType[] ) : boolean
	{
		for (let type of types)
		{
			if (this.tokens.peek().type == type) return true;
		}

		this.context.throwExpected.apply(this.tokens.peek(), types);
		return false;
	}

	/**
	 * Parse a compilation unit.
	 *
	 *   Unit: Package? Import* Type+
	 *
	 * @return
	 */
	public parse() : tree.CompilationUnit
	{
		let current = this.tokens.peek();
		let pack = null;

		//if (this.tokens.peekType() == TokenType.TOK_PACKAGE)
		//	pack = this.parsePackage();

		let unit = new tree.CompilationUnit();

		current = this.tokens.peek();
		while (current != null && current.type == TokenType.TOK_IMPORT)
		{
			let imp = this.parseImport();
			if (imp == null) break;
            unit.importList.push(imp);
			current = this.tokens.peek();
		}

		while (this.tokens.peek().type != TokenType.TOK_EOF)
		{
			this.parseHighLevelEntity(unit);
        }
		return unit;
	}

	parseName( isQualified : boolean = true ) : tree.Name
	{
		if (!this.expectedOneOf(TokenType.TOK_NAME))
			return null;

		let location = this.tokens.peek().location;
		let result = tree.createName(this.tokens.peek().value);
		this.tokens.discard();

		while (isQualified)
		{
			if (!this.tokens.lookahead(false, TokenType.TOK_DOT, TokenType.TOK_NAME))
				break;
			tree.appendName(result, this.tokens.peekAt(1).value);
			this.tokens.discard(2);
		}

		result.location = location;
		return result;
	}

	parseImport() : tree.TypeImport
	{
		if (this.tokens.peekType() == TokenType.TOK_IMPORT)
		{
			this.tokens.discard();
            let alias = null;
			let qualifiedName = this.parseName();
			if (qualifiedName == null) return null;

            let isWildcard = false;
			if (this.tokens.lookahead(false, TokenType.TOK_DOT, TokenType.TOK_MUL))
			{
				this.tokens.discard(2);
				isWildcard = true;
			}
			else
			{
				if (this.tokens.peekType() == TokenType.TOK_AS)
				{
					this.tokens.discard();
					alias = this.parseName();
					if (alias == null) return null;
				}
			}
            if (this.tokens.peekType() != TokenType.TOK_SEMICOLON) return null;
            this.tokens.discard(1);

            return tree.createTypeImport(qualifiedName, isWildcard, alias);
		}
		return null;
	}

    private parseHighLevelEntity( unit : tree.CompilationUnit )
    {
        let decors = null;
        if (this.tokens.peek().type == TokenType.TOK_AT)
            decors = this.parseDecorators();
/*
        // parse functions
        if (this.tokens.peek().type == TokenType.TOK_DEF)
        {
            unit.functions.push(this.parseFunction(decors, null));
        }
        else
        // parse variables and constants
        if (this.tokens.peek().type == TokenType.TOK_VAR || tokens.peek().type == TokenType.TOK_CONST)
        {
            unit.storages().add((StorageDeclaration)parseVariableOrConstant(annots));
        }
        else
        // parse structures
        if (this.tokens.peek().type == TokenType.TOK_STRUCT)
        {
            unit.structures.add( this.parseStructure(decors) );
        }
        else
        // parse block comments (originally a multiline string literal)
        if (this.tokens.peek().type == TokenType.TOK_MSTRING_LITERAL)
        {
            this.context.stringTable.add(tokens.peek().value);
            this.tokens.discard();
        }
        //else
        //{
        //	TypeDeclaration type = parseType(unit, annots);
        //	unit.types().add(type);
        //}
        else
        {
            this.context.listener.onError(null, "Unrecognized statement");
            return null;
        }*/
    }

    private parseDecorators() : tree.Decorator[]
    {
        let decors = [];

        while (this.tokens.peekType() == TokenType.TOK_AT)
        {
            this.tokens.discard();
            let name = this.parseName();
            //if (!this.expectedOneOf(TokenType.TOK_SEMICOLON)) return null;
            //this.tokens.discard();

            decors.push( tree.createDecorator(name) );
        }

        return decors;
    }

}

}