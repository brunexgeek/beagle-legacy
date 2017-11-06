package beagle.compiler;

import java.util.LinkedList;
import java.util.List;

import beagle.compiler.tree.Annotation;
import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.FieldDeclaration;
import beagle.compiler.tree.FormalParameter;
import beagle.compiler.tree.IAnnotation;
import beagle.compiler.tree.ICompilationUnit;
import beagle.compiler.tree.IFieldDeclaration;
import beagle.compiler.tree.IFormalParameter;
import beagle.compiler.tree.IMethodDeclaration;
import beagle.compiler.tree.IModifiers;
import beagle.compiler.tree.IName;
import beagle.compiler.tree.IPackage;
import beagle.compiler.tree.ITypeBody;
import beagle.compiler.tree.ITypeDeclaration;
import beagle.compiler.tree.ITypeImport;
import beagle.compiler.tree.ITypeReference;
import beagle.compiler.tree.MethodDeclaration;
import beagle.compiler.tree.Name;
import beagle.compiler.tree.Package;
import beagle.compiler.tree.TypeBody;
import beagle.compiler.tree.TypeDeclaration;
import beagle.compiler.tree.TypeImport;
import beagle.compiler.tree.TypeReference;

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
	 * Parse the following grammar:
	 *
	 *   CompilationUnit := [ PackageDeclaration ] ImportDeclaration* TypeDeclaration+
	 *
	 * @return
	 */
	@Override
	public ICompilationUnit parse()
	{
		Token current = tokens.peek();
		IPackage pack = null;

		// FIXME: 'package' must be optional
		if (expected(TokenType.TOK_PACKAGE))
			pack = parsePackage();
		else
			return null;

		ICompilationUnit unit = new CompilationUnit(fileName, pack);

		current = tokens.peek();
		while (current != null && current.type == TokenType.TOK_IMPORT)
		{
			ITypeImport imp = parseImport();
			unit.addImport(imp);
			if (imp == null) break;
			current = tokens.peek();
		}

		while (tokens.peek().type != TokenType.TOK_EOF)
		{
			ITypeDeclaration type = parseType(unit);
			if (type == null) return null;
			unit.addType(type);
		}
		return unit;
	}

	IName parseName()
	{
		return parseName(true);
	}


	/**
	 * Parse the following grammar:
	 *
	 *   QualifiedName := TOK_NAME [ "." TOK_NAME ]*
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
	 * Parse the following grammar:
	 *
	 *  PackageDeclaration := "package" QualifiedName
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
	 * Parse the following grammar:
	 *
	 *  ImportDeclaration := "import" QualifiedName [ "." "*" ] LF
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
	 * Parse the following grammar:
	 *
	 *   TypeDeclaration := Modifiers [ "class" | "interface" | "enumration" ] Name
	 *
	 * @return
	 */
	ITypeDeclaration parseType( ICompilationUnit unit )
	{
		List<IAnnotation> annots = parseAnnotations();
		//IModifiers modifiers = parseModifiers(false);

		if (tokens.peekType() == TokenType.TOK_CLASS)
			return parseClass(unit, annots, null);

		return null;
	}


	ITypeDeclaration parseClass( ICompilationUnit unit, List<IAnnotation> annots, IModifiers modifiers )
	{
		if (!expected(TokenType.TOK_CLASS))
			return null;
		tokens.discard();

		List<ITypeReference> extended = null;

		IName name = parseName();

		if (tokens.peekType() == TokenType.TOK_COLON)
			extended = parseExtends();

		if (tokens.peekType() == TokenType.TOK_EOL) tokens.discard();

		TypeBody body = parseClassBody();

		return new TypeDeclaration(unit, annots, modifiers, name, extended, body);
	}

	TypeBody parseClassBody()
	{
		if (tokens.peekType() == TokenType.TOK_LEFT_BRACE)
		{
			tokens.discard();

			TypeBody body = new TypeBody();

			// parse every class member
			while (tokens.peekType() != TokenType.TOK_RIGHT_BRACE)
			{
				List<IAnnotation> annots = parseAnnotations();
				//IModifiers modifiers = parseModifiers();

				// variable or constant
				if (tokens.peekType() == TokenType.TOK_VAR || tokens.peekType() == TokenType.TOK_CONST)
				{
					body.getFields().add( parseField(annots, null) );
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
	 * @return
	 */
	IFieldDeclaration parseField( List<IAnnotation> annots, IModifiers modifiers )
	{
		// discard 'var' or 'const' keyword
		tokens.discard();

		IName name = parseName(false);
		ITypeReference type = null;

		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard(1);
			type = new TypeReference( parseName() );
		}

		return new FieldDeclaration(annots, modifiers, type, name);
	}

	void parseMethodDeclaration( IModifiers modifiers, ITypeReference type, ITypeBody body )
	{
		IName name = null;

		if (tokens.peekType() == TokenType.TOK_LEFT_PAR)
		{
			// assume we have a constructor
			name = type.getName();
		}
		else
			name = parseName();

		tokens.discard();
		List<IFormalParameter> params = parseFormatParameters();
		tokens.discard(TokenType.TOK_RIGHT_PAR);

		IMethodDeclaration method = new MethodDeclaration(modifiers, type, name, params, null);
		method.setParent(body);
		body.getMethods().add(method);
		tokens.discard(); // :
		tokens.discard(); // EOL
	}

	List<IFormalParameter> parseFormatParameters()
	{
		IName typeName = parseName();
		IName name = parseName();

		if (typeName == null || name == null)
			return null;

		List<IFormalParameter> result = new LinkedList<>();
		result.add( new FormalParameter(name, new TypeReference(typeName)) );

		while (tokens.peekType() == TokenType.TOK_COMA)
		{
			tokens.discard();
			typeName = parseName();
			name = parseName();
			result.add( new FormalParameter(name, new TypeReference(typeName)) );
		}

		return result;
	}

	/**
	 * Parse the following grammar:
	 *
	 *    Extends = ":" QualifiedName [ "," QualifiedName ]*
	 *
	 * @return
	 */
	List<ITypeReference> parseExtends()
	{
		List<ITypeReference> extended = null;

		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard();

			if (tokens.peekType() != TokenType.TOK_NAME)
			{
				context.listener.onError(null, "Expected identifier");
				return null;
			}

			extended = new LinkedList<ITypeReference>();
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

	List<IAnnotation> parseAnnotations()
	{
		if (tokens.peekType() != TokenType.TOK_AT)
		{
			return new LinkedList<IAnnotation>();
		}

		LinkedList<IAnnotation> output = new LinkedList<>();

		while (tokens.peekType() == TokenType.TOK_AT)
		{
			tokens.discard();
			IName name = parseName();
			if (name == null) return null;
			output.add( new Annotation( new TypeReference(name)));
		}

		return output;
	}

}
