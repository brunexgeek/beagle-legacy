package beagle.compiler;

import static beagle.compiler.TokenType.*;

import beagle.compiler.tree.Annotation;
import beagle.compiler.tree.AnnotationList;
import beagle.compiler.tree.AtomicExpression;
import beagle.compiler.tree.BinaryExpression;
import beagle.compiler.tree.Block;
import beagle.compiler.tree.BooleanLiteral;
import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.ConstantDeclaration;
import beagle.compiler.tree.FormalParameter;
import beagle.compiler.tree.FormalParameterList;
import beagle.compiler.tree.IAnnotationList;
import beagle.compiler.tree.IBlock;
import beagle.compiler.tree.ICompilationUnit;
import beagle.compiler.tree.IConstantDeclaration;
import beagle.compiler.tree.IExpression;
import beagle.compiler.tree.IFormalParameterList;
import beagle.compiler.tree.IMethodDeclaration;
import beagle.compiler.tree.IModifiers;
import beagle.compiler.tree.IName;
import beagle.compiler.tree.IPackage;
import beagle.compiler.tree.ITreeElement;
import beagle.compiler.tree.ITypeDeclaration;
import beagle.compiler.tree.ITypeImport;
import beagle.compiler.tree.ITypeReference;
import beagle.compiler.tree.ITypeReferenceList;
import beagle.compiler.tree.IVariableDeclaration;
import beagle.compiler.tree.IntegerLiteral;
import beagle.compiler.tree.MethodDeclaration;
import beagle.compiler.tree.Name;
import beagle.compiler.tree.NameLiteral;
import beagle.compiler.tree.NullLiteral;
import beagle.compiler.tree.Package;
import beagle.compiler.tree.StringLiteral;
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
	 *   Unit: PackageDeclaration? ImportDeclaration* TypeDeclaration+
	 *
	 * @return
	 */
	@Override
	public ICompilationUnit parse()
	{
		Token current = tokens.peek();
		IPackage pack = null;

		if (tokens.peekType() == TokenType.TOK_PACKAGE)
			pack = parsePackage();

		ICompilationUnit unit = new CompilationUnit(fileName, pack);

		current = tokens.peek();
		while (current != null && current.type == TokenType.TOK_IMPORT)
		{
			ITypeImport imp = parseImport();
			unit.imports().add(imp);
			if (imp == null) break;
			current = tokens.peek();
		}

		while (tokens.peek().type != TokenType.TOK_EOF)
		{
			ITypeDeclaration type = parseType(unit);
			if (type == null) return null;
			unit.types().add(type);
		}
		return unit;
	}

	/**
	 * Parse a name.
	 *
	 *   QualifiedName: Name ( "." Name )*
	 *
	 * @return
	 */
	IName parseName()
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
	IName parseName( boolean isQualified )
	{
		if (!expected(TokenType.TOK_NAME))
			return null;

		Name result = new Name(tokens.peek().value);
		tokens.discard();

		while (isQualified)
		{
			if (!tokens.lookahead(TokenType.TOK_DOT, TokenType.TOK_NAME))
				break;
			result.append(tokens.peek(1).value);
			tokens.discard(2);
		}

		return result;
	}

	/**
	 * Parse a package declaration.
	 *
	 *  Package: "package" QualifiedName
	 *
	 * @return
	 */
	IPackage parsePackage()
	{
		if (tokens.peekType() == TokenType.TOK_PACKAGE)
		{
			tokens.discard();
			IName name = parseName();
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
	ITypeImport parseImport()
	{
		if (tokens.peekType() == TokenType.TOK_IMPORT)
		{
			tokens.discard();
			IName qualifiedName = parseName();
			if (tokens.lookahead(TokenType.TOK_DOT, TokenType.TOK_MUL))
			{
				tokens.discard(2);
				//tokens.discard(TokenType.TOK_EOL);
				return new TypeImport(context, qualifiedName);
			}
			else
			{
				if (!qualifiedName.isQualified())
				{
					context.listener.onError(null, "Invalid qualified type name");
					return null;
				}
				IName typeName = qualifiedName.slice(qualifiedName.getCount() - 1);
				IName packageName = qualifiedName.slice(0, qualifiedName.getCount() - 1);
				//tokens.discard(TokenType.TOK_EOL);
				return new TypeImport(context, packageName, typeName);
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
	ITypeDeclaration parseType( ICompilationUnit unit )
	{
		IAnnotationList annots = parseAnnotations();
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
	ITypeDeclaration parseClass( ICompilationUnit unit, IAnnotationList annots, IModifiers modifiers )
	{
		if (!expected(TokenType.TOK_CLASS))
			return null;
		tokens.discard();

		ITypeReferenceList extended = null;

		IName name = parseName();

		if (tokens.peekType() == TokenType.TOK_COLON)
			extended = parseExtends();

		if (tokens.peekType() == TokenType.TOK_EOL) tokens.discard();

		TypeBody body = parseClassBody();

		return new TypeDeclaration(unit, annots, modifiers, name, extended, body);
	}

	/**
	 * Parse a class body.
	 *
	 *	  ClassBody: "{" Member* "}"
	 *
	 *    Member: ( Variable | Constant | Method )*
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
				IAnnotationList annots = parseAnnotations();
				//IModifiers modifiers = parseModifiers();

				// variable or constant
				if (tokens.peekType() == TokenType.TOK_CONST)
					body.getConstants().add( (IConstantDeclaration) parseVariableOrConstant(annots) );
				else
				if (tokens.peekType() == TokenType.TOK_VAR)
					body.getVariables().add( (IVariableDeclaration) parseVariableOrConstant(annots) );
				else
				if (tokens.peekType() == TokenType.TOK_DEF)
				{
					body.getMethods().add( parseMethod(annots, body) );
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
	 *    Variable: Annotation* "var" Name ( ":" TypeReference )? ( "=" Expression )?
	 *
	 *    Constant: Annotation* "const" Name ( ":" TypeReference )? "=" Expression
	 *
	 * @return
	 */
	ITreeElement parseVariableOrConstant( IAnnotationList annots )
	{
		// get 'var' or 'const' keyword
		TokenType kind = tokens.peekType();
		tokens.discard();

		IName name = parseName(false);
		ITypeReference type = null;
		IExpression initializer = null;

		// check whether we have the var/const type
		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard(1);
			type = new TypeReference( parseName() );
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

		if (kind == TokenType.TOK_CONST)
			return new ConstantDeclaration(annots, name, type, initializer);
		else
			return new VariableDeclaration(annots, name, type, initializer);
	}


	/**
	 * Parse a method.
	 *
	 *    Method: Annotation* “def” Name ParameterList ( ":" TypeReference )? Block?
	 *
	 * @param modifiers
	 * @param type
	 * @param body
	 */
	IMethodDeclaration parseMethod( IAnnotationList annots, TypeBody body )
	{
		if (!expected(TokenType.TOK_DEF)) return null;
		tokens.discard();

		ITypeReference type = null;
		IName name = parseName();

		if (!expected(TokenType.TOK_LEFT_PAR)) return null;
		IFormalParameterList params = parseFormalParameters();

		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard(1);
			type = new TypeReference( parseName() );
		}

		IBlock block = parseBlock();

		IMethodDeclaration method = new MethodDeclaration(annots, type, name, params, block);
		method.parent(body);

		return method;
	}

	/**
	 * Parse a parameter list.
	 *
	 *    ParameterList: "(" ")" | "(" Parameter ( "," Parameter )* ")"
	 *
	 *    Parameter: ( "var" | "const" )? Name ":" TypeReference
	 *
	 * @return
	 */
	IFormalParameterList parseFormalParameters()
	{
		if (tokens.peekType() != TokenType.TOK_LEFT_PAR) return null;
		tokens.discard();

		IFormalParameterList output = new FormalParameterList();
		IName typeName, name;

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
			output.add( new FormalParameter(name, new TypeReference(typeName)) );
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
	ITypeReferenceList parseExtends()
	{
		ITypeReferenceList extended = null;

		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard();

			if (tokens.peekType() != TokenType.TOK_NAME)
			{
				context.listener.onError(null, "Expected identifier");
				return null;
			}

			extended = new TypeReferenceList();
			extended.add( new TypeReference( parseName() ) );

			while (true)
			{
				if (tokens.peekType() != TokenType.TOK_COMA)
					break;

				tokens.discard();
				extended.add( new TypeReference( parseName() ) );
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
	IAnnotationList parseAnnotations()
	{
		if (tokens.peekType() != TokenType.TOK_AT)
		{
			return new AnnotationList();
		}

		AnnotationList output = new AnnotationList();

		while (tokens.peekType() == TokenType.TOK_AT)
		{
			tokens.discard();
			IName name = parseName();
			if (name == null) return null;
			output.add( new Annotation( new TypeReference(name)));
		}

		return output;
	}

	IBlock parseBlock()
	{
		if (!expected(TokenType.TOK_LEFT_BRACE)) return null;

		while (tokens.peekType() != TokenType.TOK_RIGHT_BRACE) tokens.discard();
		tokens.discard();

		Block block = new Block();
		return block;
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
	 * EqualityComparison: Comparison ( EqualityOperation EqualityComparison )*
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
	 * Comparison: NamedInfix ( ComparisonOperation Comparison )*
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
	 *   : AdditiveExpression ( InOperation AdditiveExpression )*
	 *   : AdditiveExpression IsOperation TypeReference
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

		return new BinaryExpression(left, type, right);
	}

	/**
	 * MultiplicativeExpression: PrefixUnaryExpression ( MultiplicativeOperation MultiplicativeExpression )*
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
	 * MultiplicativeExpression: PrefixUnaryExpression ( MultiplicativeOperation MultiplicativeExpression )*
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
	 * PrefixUnaryExpression: PrefixUnaryOperation? PostfixUnaryExpression
	 *
	 */
	IExpression parsePrefixUnaryExpression()
	{
		return parsePrefixUnaryExpression(null);
	}

	/**
	 *
	 * PrefixUnaryExpression: PrefixUnaryOperation? PostfixUnaryExpression
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
		// FIXME: broken in recursive case (should test for more prefixes)
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
	 * PostfixUnaryExpression: AtomicExpression PostfixUnaryOperation?
	 *
	 */
	IExpression parsePostfixUnaryExpression()
	{
		return parsePostfixUnaryExpression(null);
	}

	/**
	 *
	 * PostfixUnaryExpression: AtomicExpression PostfixUnaryOperation?
	 *
	 */
	IExpression parsePostfixUnaryExpression(IExpression leftValue)
	{
		IExpression expr = leftValue;

		if (leftValue == null)
			expr = parseAtomicExpression();

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

		if (type != null)
		{
			if (recursive)
				return new UnaryExpression(parsePostfixUnaryExpression(expr), type);
			else
				return new UnaryExpression(expr, type);
		}

		return expr;
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
				break;
			case TOK_NAME:
				result = new NameLiteral( parseName() );
				break;
			default:
				result = parseLiteralConstant();
		}

		return result;
	}

	/**
	 * LiteralConstant
	 *  : BooleanLiteral
	 *  : StringLiteral
	 *  : IntegerLiteral
	 *  : HexadecimalLiteral
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
		return new StringLiteral(tokens.read().value);
	}

	BooleanLiteral parseBooleanLiteral()
	{
		if (!expected(TOK_TRUE)) return null;

		boolean value = (tokens.read().type == TOK_TRUE);
		return new BooleanLiteral(value);
	}

}
