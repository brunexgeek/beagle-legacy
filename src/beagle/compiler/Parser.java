package beagle.compiler;

import static beagle.compiler.TokenType.TOK_AND;
import static beagle.compiler.TokenType.TOK_ASSIGN;
import static beagle.compiler.TokenType.TOK_COMA;
import static beagle.compiler.TokenType.TOK_ELIF;
import static beagle.compiler.TokenType.TOK_ELSE;
import static beagle.compiler.TokenType.TOK_IF;
import static beagle.compiler.TokenType.TOK_IN;
import static beagle.compiler.TokenType.TOK_IS;
import static beagle.compiler.TokenType.TOK_LEFT_BRACE;
import static beagle.compiler.TokenType.TOK_LEFT_BRACKET;
import static beagle.compiler.TokenType.TOK_LEFT_PAR;
import static beagle.compiler.TokenType.TOK_NAME;
import static beagle.compiler.TokenType.TOK_NIN;
import static beagle.compiler.TokenType.TOK_NIS;
import static beagle.compiler.TokenType.TOK_NOT;
import static beagle.compiler.TokenType.TOK_OR;
import static beagle.compiler.TokenType.TOK_RETURN;
import static beagle.compiler.TokenType.TOK_RIGHT_BRACKET;
import static beagle.compiler.TokenType.TOK_RIGHT_PAR;
import static beagle.compiler.TokenType.TOK_THEN;
import static beagle.compiler.TokenType.TOK_TRUE;
import static beagle.compiler.TokenType.TOK_FOR;
import static beagle.compiler.TokenType.TOK_FP_LITERAL;

import beagle.compiler.tree.Annotation;
import beagle.compiler.tree.AnnotationList;
import beagle.compiler.tree.Argument;
import beagle.compiler.tree.ArgumentList;
import beagle.compiler.tree.AtomicExpression;
import beagle.compiler.tree.BinaryExpression;
import beagle.compiler.tree.Block;
import beagle.compiler.tree.BooleanLiteral;
import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.ConstantDeclaration;
import beagle.compiler.tree.ExpressionList;
import beagle.compiler.tree.ExpressionStmt;
import beagle.compiler.tree.FloatLiteral;
import beagle.compiler.tree.ForEachStmt;
import beagle.compiler.tree.FormalParameter;
import beagle.compiler.tree.FormalParameterList;
import beagle.compiler.tree.Function;
import beagle.compiler.tree.IExpression;
import beagle.compiler.tree.IStatement;
import beagle.compiler.tree.ITreeVisitor;
import beagle.compiler.tree.IfThenElseStmt;
import beagle.compiler.tree.IntegerLiteral;
import beagle.compiler.tree.Modifiers;
import beagle.compiler.tree.Name;
import beagle.compiler.tree.NameLiteral;
import beagle.compiler.tree.NullLiteral;
import beagle.compiler.tree.Package;
import beagle.compiler.tree.ReturnStmt;
import beagle.compiler.tree.StorageDeclaration;
import beagle.compiler.tree.StringLiteral;
import beagle.compiler.tree.Structure;
import beagle.compiler.tree.TypeBody;
import beagle.compiler.tree.TypeDeclaration;
import beagle.compiler.tree.TypeImport;
import beagle.compiler.tree.TypeReference;
import beagle.compiler.tree.TypeReferenceList;
import beagle.compiler.tree.UnaryExpression;
import beagle.compiler.tree.VariableDeclaration;


public class Parser implements IParser
{

	private String fileName;

	private TokenArray tokens;

	private CompilationContext context;

	public Parser( CompilationContext context, IScanner scanner )
	{
		fileName = scanner.getFileName();
		tokens = new TokenArray(scanner, 16);
		this.context = context;
	}

	boolean expected( TokenType... types )
	{
		for (TokenType type : types )
		{
			if (tokens.peek().type == type) return true;
		}

		context.throwExpected(tokens.peek(), types);
		return false;
	}

	void discardWhiteSpaces()
	{
		while (true)
		{
			TokenType current = tokens.peek().type;
			if (current == TokenType.TOK_EOL)
				continue;
			break;
		}
	}

	/**
	 * Parse a compilation unit.
	 *
	 *   Unit: Package? Import* Type+
	 *
	 * @return
	 */
	@Override
	public CompilationUnit parse()
	{
		Token current = tokens.peek();
		Package pack = null;

		if (tokens.peekType() == TokenType.TOK_PACKAGE)
			pack = parsePackage();

		CompilationUnit unit = new CompilationUnit(fileName, pack);

		current = tokens.peek();
		while (current != null && current.type == TokenType.TOK_IMPORT)
		{
			TypeImport imp = parseImport();
			unit.imports().add(imp);
			if (imp == null) break;
			current = tokens.peek();
		}

		while (tokens.peek().type != TokenType.TOK_EOF)
		{
			AnnotationList annots = null;
			if (tokens.peek().type == TokenType.TOK_AT)
				annots = parseAnnotations();

			// parse functions
			if (tokens.peek().type == TokenType.TOK_DEF)
			{
				unit.functions.add(parseFunction(annots, null));
			}
			else
			// parse variables and constants
			if (tokens.peek().type == TokenType.TOK_VAR || tokens.peek().type == TokenType.TOK_CONST)
			{
				unit.storages().add((StorageDeclaration)parseVariableOrConstant(annots));
			}
			else
			// parse structures
			if (tokens.peek().type == TokenType.TOK_STRUCT)
			{
				unit.structures.add( parseStructure(annots) );
			}
			else
			// parse block comments (originally a multiline string literal)
			if (tokens.peek().type == TokenType.TOK_MSTRING_LITERAL)
			{
				context.stringTable.add(tokens.peek().value);
				tokens.discard();
			}
			/*else
			{
				TypeDeclaration type = parseType(unit, annots);
				unit.types().add(type);
			}*/
			else
			{
				context.listener.onError(null, "Unrecognized statement");
				return null;
			}
		}
		return unit;
	}

	Structure parseStructure(AnnotationList annots)
	{
		tokens.discard();

		Structure current = new Structure();
		if (expected(TokenType.TOK_NAME))
			current.name = new Name(tokens.read().value);

		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard();
			current.parent = TypeReference.fromName(new Name(tokens.read().value));
		}

		current.body = parseTypeBody(false);
		return current;
	}
/*
	void parseStructureBody( Structure structure, boolean enableFunctions )
	{
		if (tokens.peekType() == TokenType.TOK_LEFT_BRACE)
		{
			tokens.discard();

			// parse every type member
			while (tokens.peekType() != TokenType.TOK_RIGHT_BRACE)
			{
				AnnotationList annots = parseAnnotations();
				//IModifiers modifiers = parseModifiers();

				// variable or constant
				if (tokens.peekType() == TokenType.TOK_CONST || tokens.peekType() == TokenType.TOK_VAR)
					structure.storages.add( parseVariableOrConstant(annots) );
				else
				if (enableFunctions && tokens.peekType() == TokenType.TOK_DEF)
				{
					// TODO: should set the parent
					structure.functions.add( parseFunction(annots, null) );
				}
				else
				{
					context.throwExpected(tokens.peek(), TokenType.TOK_VAR, TokenType.TOK_CONST);
					break;
				}
			}

			tokens.discard();
		}
	}*/


	TypeBody parseTypeBody( boolean enableFunctions )
	{
		if (tokens.peekType() == TokenType.TOK_LEFT_BRACE)
		{
			tokens.discard();

			TypeBody body = new TypeBody();

			// parse every type member
			while (tokens.peekType() != TokenType.TOK_RIGHT_BRACE)
			{
				AnnotationList annots = parseAnnotations();
				//IModifiers modifiers = parseModifiers();

				// variable or constant
				if (tokens.peekType() == TokenType.TOK_CONST || tokens.peekType() == TokenType.TOK_VAR)
					body.storages.add( parseVariableOrConstant(annots) );
				else
				if (enableFunctions && tokens.peekType() == TokenType.TOK_DEF)
				{
					body.functions.add( parseFunction(annots, body) );
				}
				else
				{
					context.throwExpected(tokens.peek(), TokenType.TOK_VAR, TokenType.TOK_CONST);
					break;
				}
			}

			tokens.discard();
			return body;
		}

		return null;
	}

	/**
	 * Parse a name.
	 *
	 *   QualifiedName: Name ( "." Name )*
	 *
	 * @return
	 */
	Name parseName()
	{
		return parseName(true);
	}


	/**
	 * Parse a name.
	 *
	 *   QualifiedName: Name ( "." Name )*
	 *
	 * @return
	 */
	Name parseName( boolean isQualified )
	{
		if (!expected(TokenType.TOK_NAME))
			return null;

		SourceLocation location = tokens.peek().location;
		Name result = new Name(tokens.peek().value);
		tokens.discard();

		while (isQualified)
		{
			if (!tokens.lookahead(TokenType.TOK_DOT, TokenType.TOK_NAME))
				break;
			result.append(tokens.peek(1).value);
			tokens.discard(2);
		}

		result.location(location);
		return result;
	}

	/**
	 * Parse a package declaration.
	 *
	 *  Package: "package" QualifiedName
	 *
	 * @return
	 */
	Package parsePackage()
	{
		if (tokens.peekType() == TokenType.TOK_PACKAGE)
		{
			tokens.discard();
			Name name = parseName();
			//tokens.discard(TokenType.TOK_EOL);
			return new Package(name);
		}
		return null;
	}

	/**
	 * Parse a import declaration.
	 *
	 *  Import: "import" QualifiedName ( "." "*" | "as" Name )?
	 *
	 * @return
	 */
	TypeImport parseImport()
	{
		if (tokens.peekType() == TokenType.TOK_IMPORT)
		{
			tokens.discard();
			Name qualifiedName = parseName();
			if (qualifiedName == null) return null;

			if (tokens.lookahead(TokenType.TOK_DOT, TokenType.TOK_MUL))
			{
				tokens.discard(2);
				return new TypeImport(context, qualifiedName);
			}
			else
			{
				Name alias = null;
				if (tokens.peekType() == TokenType.TOK_AS)
				{
					tokens.discard();
					alias = parseName();
					if (alias == null) return null;
				}
				if (!qualifiedName.isQualified())
				{
					context.listener.onError(null, "Invalid qualified type name");
					return null;
				}
				Name typeName = qualifiedName.slice(qualifiedName.count() - 1);
				Name packageName = qualifiedName.slice(0, qualifiedName.count() - 1);

				return new TypeImport(context, packageName, typeName, alias);
			}
		}
		return null;
	}


	/**
	 * Parse a type definition:
	 *
	 *   Type: Class
	 *
	 * @return
	 */
	TypeDeclaration parseType( CompilationUnit unit, AnnotationList annots )
	{
		//IModifiers modifiers = parseModifiers(false);

		if (tokens.peekType() == TokenType.TOK_CLASS)
			return parseClass(unit, annots, null);

		return null;
	}


	/**
	 * Parse a class definition.
	 *
	 *    Class: Annotation* "class" QualifiedName Extends? ClassBody?
	 *
	 * @param unit
	 * @param annots
	 * @param modifiers
	 * @return
	 */
	TypeDeclaration parseClass( CompilationUnit unit, AnnotationList annots, Modifiers modifiers )
	{
		if (!expected(TokenType.TOK_CLASS))
			return null;
		tokens.discard();

		TypeReferenceList extended = null;

		Name name = parseName();

		if (tokens.peekType() == TokenType.TOK_COLON)
			extended = parseExtends();

		if (tokens.peekType() == TokenType.TOK_EOL) tokens.discard();

		TypeBody body = parseClassBody();

		return new TypeDeclaration(unit, annots, modifiers, name, extended, body);
	}

	/**
	 * Parse a class body.
	 *
	 *	  ClassBody: "{" TypeMember* "}"
	 *
	 *    TypeMember: ( TypeVariable | TypeConstant | TypeFunction )*
	 *
	 * @return
	 */
	TypeBody parseClassBody()
	{
		if (tokens.peekType() == TokenType.TOK_LEFT_BRACE)
		{
			tokens.discard();

			TypeBody body = new TypeBody();

			// parse every class member
			while (tokens.peekType() != TokenType.TOK_RIGHT_BRACE)
			{
				AnnotationList annots = parseAnnotations();
				//IModifiers modifiers = parseModifiers();

				// variable or constant
				if (tokens.peekType() == TokenType.TOK_CONST || tokens.peekType() == TokenType.TOK_VAR)
					body.storages.add( (ConstantDeclaration) parseVariableOrConstant(annots) );
				else
				if (tokens.peekType() == TokenType.TOK_DEF)
				{
					body.functions.add( parseFunction(annots, body) );
				}
				else
				{
					context.throwExpected(tokens.peek(), TokenType.TOK_VAR, TokenType.TOK_CONST);
					break;
				}
			}

			tokens.discard();
			return body;
		}

		return null;
	}

	/**
	 * Parse a variable.
	 *
	 *    TypeVariable: Annotation* Variable
	 *
	 *    TypeConstant: Annotation* Constant
	 *
	 *    Variable: "var" Name ( ":" TypeReference )? ( "=" Expression )?
	 *
	 *    Constant: "const" Name ( ":" TypeReference )? "=" Expression
	 *
	 * @return
	 */
	StorageDeclaration parseVariableOrConstant( AnnotationList annots )
	{
		// get 'var' or 'const' keyword
		TokenType kind = tokens.peekType();
		tokens.discard();

		Name name = parseName(false);
		SourceLocation location = name.location();
		TypeReference type = null;
		IExpression initializer = null;

		// check whether we have the var/const type
		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard(1);
			type = TypeReference.fromName( parseName() );
		}

		if (tokens.peekType() == TokenType.TOK_ASSIGN)
		{
			tokens.discard(); // =
			initializer = parseExpression();
		}
		else
		if (kind == TokenType.TOK_CONST)
		{
			expected(TokenType.TOK_ASSIGN);
			return null;
		}

		StorageDeclaration result;
		if (kind == TokenType.TOK_CONST)
			result = new ConstantDeclaration(annots, name, type, initializer);
		else
			result = new VariableDeclaration(annots, name, type, initializer);
		result.location(location);
		return result;
	}


	/**
	 * Parse a function.
	 *
	 *    TypeFunction: Annotation* "def" Name ParameterList ( ":" TypeReference )? Block?
	 *
	 * @param modifiers
	 * @param type
	 * @param body
	 */
	Function parseFunction( AnnotationList annots, TypeBody body )
	{
		if (!expected(TokenType.TOK_DEF)) return null;
		tokens.discard();

		TypeReference type = null;
		Name name = parseName();

		if (!expected(TokenType.TOK_LEFT_PAR)) return null;
		FormalParameterList params = parseFormalParameters();

		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard(1);
			type = TypeReference.fromName( parseName() );
			type.location(type.name().location());
		}

		Block block = parseBlock();

		Function method = new Function(annots, type, name, params, block);
		method.parent(body);

		return method;
	}

	/**
	 * Parse a parameter list.
	 *
	 *    ParameterList:
	 *    	: "(" ")"
	 *      : "(" Parameter ( "," Parameter )* ")"
	 *      ;
	 *
	 *    Parameter: ( "var" | "const" )? Name ":" TypeReference
	 *
	 * @return
	 */
	FormalParameterList parseFormalParameters()
	{
		if (tokens.peekType() != TokenType.TOK_LEFT_PAR) return null;
		tokens.discard();

		FormalParameterList output = new FormalParameterList();
		Name typeName, name;

		while (tokens.peekType() != TokenType.TOK_RIGHT_PAR)
		{
			if (tokens.peekType() == TokenType.TOK_COMA)
			{
				tokens.discard();
				continue;
			}

			name = parseName();
			if (!expected(TokenType.TOK_COLON)) return null;
			tokens.discard();
			typeName = parseName();
			output.add( new FormalParameter(name, TypeReference.fromName(typeName)) );
		}

		tokens.discard(); // )
		return output;
	}

	/**
	 * Parse the following grammar:
	 *
	 *    Extends: ":" TypeReference ( "," TypeReference )*
	 *
	 * @return
	 */
	TypeReferenceList parseExtends()
	{
		TypeReferenceList extended = null;

		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard();

			if (tokens.peekType() != TokenType.TOK_NAME)
			{
				context.listener.onError(null, "Expected identifier");
				return null;
			}

			extended = new TypeReferenceList();
			extended.add( TypeReference.fromName( parseName() ) );

			while (true)
			{
				if (tokens.peekType() != TokenType.TOK_COMA)
					break;

				tokens.discard();
				extended.add( TypeReference.fromName( parseName() ) );
			}
		}

		return extended;
	}

	/**
	 * Parse a annatation.
	 *
	 *    Annotation: "@" QualifiedName
	 *
	 * @return
	 */
	AnnotationList parseAnnotations()
	{
		if (tokens.peekType() != TokenType.TOK_AT)
		{
			return new AnnotationList();
		}

		AnnotationList output = new AnnotationList();

		while (tokens.peekType() == TokenType.TOK_AT)
		{
			tokens.discard();
			Name name = parseName();
			if (name == null) return null;
			output.add( new Annotation( TypeReference.fromName(name)));
		}

		return output;
	}

	/**
	 * Parse a block of statements.
	 *
	 *    Block: "{" Statement* "}"
	 *
	 * @return
	 */
	Block parseBlock()
	{
		if (!expected(TokenType.TOK_LEFT_BRACE)) return null;
		tokens.discard();

		Block block = new Block();

		while (tokens.peekType() != TokenType.TOK_RIGHT_BRACE)
		{
			IStatement current = parseStatement();
			if (current == null) return null;

			block.add(current);
		}
		tokens.discard();

		return block;
	}

	/**
	 *
	 * Statement
	 *   : Variable
	 *   : Constant
	 *   : IfThenElse
	 *   : Return
	 *   : Expression
	 *   ;
	 *
	 */
	IStatement parseStatement()
	{
		switch (tokens.peekType())
		{
			case TOK_RETURN:
				return parseReturnStmt();
			case TOK_IF:
				return parseIfThenElseStmt();
			case TOK_VAR:
			case TOK_CONST:
				return (IStatement) parseVariableOrConstant(null);
			case TOK_FOR:
				return (IStatement) parseForEach();
			default:
				break;
		}

		IExpression expr = parseExpression();
		if (expr == null) return null;
		return new ExpressionStmt(expr);
	}


	/**
	 *
	 * IfThenElse: "if" Expression "then" BlockOrStatement ( "else" BlockOrStatement )?
	 *
	 * @return
	 */
	IStatement parseIfThenElseStmt()
	{
		if (!expected(TOK_IF, TOK_ELIF)) return null;
		tokens.discard();

		IExpression condition = parseExpression();

		if (!expected(TOK_THEN)) return null;
		tokens.discard();

		IStatement thenSide = null;
		IStatement elseSide = null;

		if (tokens.peekType() == TOK_LEFT_BRACE)
			thenSide = parseBlock();
		else
			thenSide = parseStatement();
		if (thenSide == null) return null;

		if (tokens.peekType() == TOK_ELIF)
		{
			Block block = new Block();
			block.add(parseIfThenElseStmt());
			elseSide = block;
		}
		else
		if (tokens.peekType() == TOK_ELSE)
		{
			tokens.discard();

			if (tokens.peekType() == TOK_LEFT_BRACE)
				elseSide = parseBlock();
			else
				elseSide = parseStatement();
			if (elseSide == null) return null;
		}

		return new IfThenElseStmt(condition, thenSide, elseSide);
	}

	/**
	 * Return: "return" Expression
	 *
	 * @return
	 */
	IStatement parseReturnStmt()
	{
		if (!expected(TOK_RETURN)) return null;
		SourceLocation location = tokens.peek().location;
		tokens.discard();

		IExpression expr = parseExpression();

		ReturnStmt result = new ReturnStmt(expr);
		result.location(location);
		return result;
	}

	/**
	 * Parse an expression.
	 *
	 * @return
	 */
	IExpression parseExpression()
	{
		return parseAssignment();
	}

	IExpression createBinaryExpression(IExpression left, TokenType type, IExpression right)
	{
		if (type == null || left == null || right == null)
			return null;
		else
			return new BinaryExpression(left, type, right);
	}

	/**
	 * Expression: Disjunction ( AssignmentOperator Expression )*
	 */
	IExpression parseAssignment()
	{
		IExpression left = parseDisjunction();
		if (left == null) return null;

		TokenType type = null;
		switch(tokens.peekType())
		{
			case TOK_ASSIGN:       // =
			case TOK_PLUS_ASSIGN:  // +=
			case TOK_MINUS_ASSIGN: // -=
			case TOK_MUL_ASSIGN:   // *=
			case TOK_DIV_ASSIGN:   // /=
			case TOK_BAND_ASSIGN:  // &=
			case TOK_BOR_ASSIGN:   // |=
			case TOK_XOR_ASSIGN:   // ^=
			case TOK_SHL_ASSIGN:   // <<=
			case TOK_SHR_ASSIGN:   // >>=
			case TOK_MOD_ASSIGN:   // %=
				type = tokens.read().type;
				break;
			default:
				return left;
		}

		return createBinaryExpression(left, type, parseAssignment());
	}

	/**
	 * Disjunction: Conjunction ( "or" Disjunction )*
	 */
	IExpression parseDisjunction()
	{
		IExpression left = parseConjunction();
		if (left == null) return null;

		TokenType type = null;
		if (tokens.peekType() == TOK_OR)
			type = tokens.read().type;
		else
			return left;

		return createBinaryExpression(left, type, parseDisjunction());
	}

	/**
	 * Conjunction: EqualityComparison ( "and" Conjunction )*
	 */
	IExpression parseConjunction()
	{
		IExpression left = parseEquality();
		if (left == null) return null;

		TokenType type = null;
		if (tokens.peekType() == TOK_AND)
			type = tokens.read().type;
		else
			return left;

		return createBinaryExpression(left, type, parseConjunction());
	}

	/**
	 * EqualityComparison: Comparison ( EqualityOperator EqualityComparison )*
	 */
	IExpression parseEquality()
	{
		IExpression left = parseComparison();
		if (left == null) return null;

		TokenType type = null;
		switch(tokens.peekType())
		{
			case TOK_EQ: // ==
			case TOK_NE: // !=
				type = tokens.read().type;
				break;
			default:
				return left;
		}

		return createBinaryExpression(left, type, parseEquality());
	}

	/**
	 * Comparison: NamedInfix ( ComparisonOperator Comparison )*
	 */
	IExpression parseComparison()
	{
		IExpression left = parseNamedInfix();
		if (left == null) return null;

		TokenType type = null;
		switch(tokens.peekType())
		{
			case TOK_GT: // >
			case TOK_GE: // >=
			case TOK_LT: // <
			case TOK_LE: // <=
				type = tokens.read().type;
				break;
			default:
				return left;
		}

		return createBinaryExpression(left, type, parseComparison());
	}

	/**
	 * NamedInfix
	 *   : AdditiveExpression ( InOperator AdditiveExpression )*
	 *   : AdditiveExpression IsOperator TypeReference
	 *   ;
	 *
	 */
	IExpression parseNamedInfix()
	{
		IExpression left = parseAdditiveExpression();
		if (left == null) return null;

		IExpression right = null;

		TokenType type = null;
		if (tokens.peekType() == TOK_NOT && tokens.peekType(1) == TOK_IN) // not in
		{
			tokens.discard(2);
			type = TOK_NIN;
		}
		else
		if (tokens.peekType() == TOK_NOT && tokens.peekType(1) == TOK_IS) // not is
		{
			tokens.discard(2);
			type = TOK_NIS;
		}
		else
		if (tokens.peekType() == TOK_IN ||  // in
		    tokens.peekType() == TOK_IS)    // is
		{
			type = tokens.read().type;
		}
		else
			return left;

		if (type == TOK_IN || type == TOK_NIN)
			right = parseAdditiveExpression();
		else
		if (type == TOK_IS || type == TOK_NIS)
			right = new NameLiteral(parseName());
		else
			return null;

		BinaryExpression result = new BinaryExpression(left, type, right);
		result.location(left.location());
		return result;
	}

	/**
	 * MultiplicativeExpression: PrefixUnaryExpression ( MultiplicativeOperator MultiplicativeExpression )*
	 *
	 */
	IExpression  parseAdditiveExpression()
	{
		IExpression left = parseMultiplicativeExpression();
		if (left == null) return null;

		TokenType type = null;
		switch(tokens.peekType())
		{
			case TOK_MINUS: // -
			case TOK_PLUS:  // +
				type = tokens.read().type;
				break;
			default:
				return left;
		}

		return createBinaryExpression(left, type, parseAdditiveExpression());
	}

	/**
	 * MultiplicativeExpression: PrefixUnaryExpression ( MultiplicativeOperator MultiplicativeExpression )*
	 *
	 */
	IExpression parseMultiplicativeExpression()
	{
		IExpression left = parsePrefixUnaryExpression();
		if (left == null) return null;

		TokenType type = null;
		switch(tokens.peekType())
		{
			case TOK_MUL: // *
			case TOK_DIV: // /
			case TOK_MOD: // %
				type = tokens.read().type;
				break;
			default:
				return left;
		}

		return createBinaryExpression(left, type, parseMultiplicativeExpression());
	}


	/**
	 *
	 * PrefixUnaryExpression: PrefixUnaryOperator? PostfixUnaryExpression
	 *
	 */
	IExpression parsePrefixUnaryExpression()
	{
		return parsePrefixUnaryExpression(null);
	}

	/**
	 *
	 * PrefixUnaryExpression: PrefixUnaryOperator? PostfixUnaryExpression
	 *
	 */
	IExpression parsePrefixUnaryExpression(IExpression leftValue)
	{
		boolean recursive = false;

		TokenType type = null;
		switch(tokens.peekType())
		{
			case TOK_INC:
			case TOK_DEC:
				type = tokens.read().type;
				break;
			default:
				type = null;
		}
		// FIXME: broken in recursive case (should test for more prefixes)?
		IExpression expr = leftValue;
		if (leftValue == null)
			expr = parsePostfixUnaryExpression();

		if (type != null)
		{
			if (recursive)
				return new UnaryExpression(type, parsePrefixUnaryExpression(expr));
			else
				return new UnaryExpression(type, expr);
		}

		return expr;
	}

	/**
	 *
	 * PostfixUnaryExpression: AtomicExpression PostfixUnaryOperator?
	 *
	 */
	IExpression parsePostfixUnaryExpression()
	{
		return parsePostfixUnaryExpression(null);
	}

	/**
	 *
	 * PostfixUnaryExpression: AtomicExpression PostfixUnaryOperator?
	 *
	 */
	IExpression parsePostfixUnaryExpression(IExpression leftValue)
	{
		IExpression expr = leftValue;
		IExpression extra = null;

		if (leftValue == null)
			expr = parseAtomicExpression();
		if (expr == null) return null;

		boolean recursive = false;

		TokenType type = null;
		switch(tokens.peekType())
		{
			case TOK_INC:
			case TOK_DEC:
				type = tokens.read().type;
				break;
			case TOK_LEFT_BRACKET:
				type = tokens.peekType();
				extra = parseArrayAccess(null);
				break;
			case TOK_LEFT_PAR:
				type = tokens.peekType();
				extra = parseArgumentList(null);
				break;
			default:
				type = null;
		}

		if (type != null)
		{
			UnaryExpression result = null;

			if (recursive)
				result =  new UnaryExpression(parsePostfixUnaryExpression(expr), type);
			else
				result =  new UnaryExpression(expr, type);

			if (extra != null) result.extra(extra);
			return result;
		}

		return expr;
	}

	/**
	 * ArgumentList:
	 *   : "(" ")"
	 *   : "(" Argument ( "," Argument )* ")"
	 */
	IExpression parseArgumentList(ArgumentList value)
	{
		if (tokens.lookahead(TOK_LEFT_PAR, TOK_RIGHT_PAR))
		{
			tokens.discard(2);
			return new ArgumentList();
		}

		if (value == null)
		{
			if (!expected(TOK_LEFT_PAR)) return null;
			tokens.discard();

			ArgumentList result = new ArgumentList( parseArgument() );
			if (tokens.peekType() == TOK_COMA)
				parseArgumentList(result);

			if (!expected(TOK_RIGHT_PAR)) return null;
			tokens.discard();

			return result;
		}
		else
		{
			if (!expected(TOK_COMA)) return null;
			tokens.discard();

			while (true)
			{
				value.add( parseArgument() );
				if (tokens.peekType() == TOK_COMA)
				{
					tokens.discard();
					continue;
				}
				break;
			}

			return value;
		}
	}

	Argument parseArgument()
	{
		Name name = null;

		if (tokens.lookahead(TOK_NAME, TOK_ASSIGN))
		{
			name = parseName();
			tokens.discard(2);
		}

		return new Argument(name, parseExpression());
	}

	/**
	 * ArrayAccess: "[" Expression ( "," Expression )* "]"
	 */
	IExpression parseArrayAccess(ExpressionList value)
	{
		if (value == null)
		{
			if (!expected(TOK_LEFT_BRACKET)) return null;
			tokens.discard();

			ExpressionList result = new ExpressionList( parseExpression() );
			if (tokens.peekType() == TOK_COMA)
				parseArrayAccess(result);

			if (!expected(TOK_RIGHT_BRACKET)) return null;
			tokens.discard();

			return result;
		}
		else
		{
			if (!expected(TOK_COMA)) return null;
			tokens.discard();

			value.add( parseExpression() );
			if (tokens.peekType() == TOK_COMA)
				return parseArrayAccess(value);
			else
				return value;
		}
	}

	/**
	 *
	 * AtomicExpression
	 *   : "(" Expression ")"
	 *   : LiteralConstant
	 *   : Name
	 *   ;
	 *
	 * @return
	 */
	IExpression parseAtomicExpression()
	{
		IExpression result = null;

		switch(tokens.peekType())
		{
			case TOK_LEFT_PAR:
				tokens.discard();
				result = new AtomicExpression(parseExpression());
				tokens.discard();
				return result;
			case TOK_NAME:
				return new NameLiteral( parseName() );
			default:
				return parseLiteralConstant();
		}
	}

	/**
	 * LiteralConstant
	 *  : BooleanLiteral
	 *  : StringLiteral
	 *  : IntegerLiteral
	 *  : "null"
	 *  ;
	 *
	 * @return
	 */
	IExpression parseLiteralConstant()
	{
		switch(tokens.peekType())
		{
			case TOK_NULL:
				tokens.discard();
				return new NullLiteral();
			case TOK_TRUE:
			case TOK_FALSE:
				return parseBooleanLiteral();
			case TOK_HEX_LITERAL:
			case TOK_BIN_LITERAL:
			case TOK_OCT_LITERAL:
			case TOK_DEC_LITERAL:
				return parseIntegerLiteral();
			case TOK_FP_LITERAL:
				return parseFloatLiteral();
			case TOK_STRING_LITERAL:
				return parseStringLiteral();
			default:
				context.listener.onError(tokens.peek().location, "Unexpected token '" + tokens.peek() + "'");
				return null;
		}

	}

	IntegerLiteral parseIntegerLiteral()
	{
		String value = tokens.peek().value;
		switch(tokens.read().type)
		{
			case TOK_HEX_LITERAL:
				value = value.substring(2);
				return new IntegerLiteral( Long.valueOf(value, 16));
			case TOK_BIN_LITERAL:
				value = value.substring(2);
				return new IntegerLiteral( Long.valueOf(value, 2));
			case TOK_OCT_LITERAL:
				return new IntegerLiteral( Long.valueOf(value, 8));
			case TOK_DEC_LITERAL:
				return new IntegerLiteral( Long.valueOf(value, 10));
			default:
				return null;
		}
	}

	StringLiteral parseStringLiteral()
	{
		String value = tokens.read().value;
		context.stringTable.add(value);
		return new StringLiteral(value);
	}

	BooleanLiteral parseBooleanLiteral()
	{
		if (!expected(TOK_TRUE)) return null;

		boolean value = (tokens.read().type == TOK_TRUE);
		return new BooleanLiteral(value);
	}

	ForEachStmt parseForEach()
	{
		if (!expected(TOK_FOR)) return null;
		tokens.discard();

		StorageDeclaration storage = new VariableDeclaration(null, parseName(), null, null);

		if (!expected(TOK_IN)) return null;
		tokens.discard();

		IExpression expr = parseExpression();

		IStatement stmts;

		if (tokens.peekType() == TOK_LEFT_BRACE)
			stmts = parseBlock();
		else
			stmts = parseStatement();

		return new ForEachStmt(storage, expr, stmts);
	}

	FloatLiteral parseFloatLiteral()
	{
		if (!expected(TOK_FP_LITERAL)) return null;
		Token tok = tokens.read();
		return new FloatLiteral(Float.valueOf(tok.value));
	}
}
