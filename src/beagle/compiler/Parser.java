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
import beagle.compiler.tree.Modifiers;
import beagle.compiler.tree.Name;
import beagle.compiler.tree.Package;
import beagle.compiler.tree.TypeBody;
import beagle.compiler.tree.TypeDeclaration;
import beagle.compiler.tree.TypeImport;
import beagle.compiler.tree.TypeReference;

public class Parser
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

	public boolean expected( TokenType... types )
	{
		for (TokenType type : types )
		{
			if (tokens.peek().type == type) return true;
		}

		context.throwExpected(tokens.peek(), types);
		return false;
	}

	public void discardComments()
	{
		while (tokens.peek().type == TokenType.TOK_DOCSTRING ||
			tokens.peek().type == TokenType.TOK_COMMENT ||
			tokens.peek().type == TokenType.TOK_EOL)
			tokens.discard();
	}

	public void discardWhiteSpaces()
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
	 *   CompilationUnit := PackageDeclaration ImportDeclaration* TypeDeclaration+
	 *
	 * @return
	 */
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

	public IName parseName()
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
	public IName parseName( boolean isQualified )
	{
		Name result = null;
		Token current = tokens.peek();
		if (current.type == TokenType.TOK_NAME)
		{
			result = new Name(current.value);
			tokens.discard();

			while (isQualified)
			{
				if (!tokens.lookahead(TokenType.TOK_DOT, TokenType.TOK_NAME))
					break;
				result.append(tokens.peek(1).value);
				tokens.discard(2);
			}
		}
		else
			context.listener.onError(null, "Invalid name");

		return result;
	}

	/**
	 * Parse the following grammar:
	 *
	 *  PackageDeclaration := "package" QualifiedName LF
	 *
	 * @return
	 */
	public IPackage parsePackage()
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
	public ITypeImport parseImport()
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
	public ITypeDeclaration parseType( ICompilationUnit unit )
	{
		List<IAnnotation> annots = parseAnnotations();
		//IModifiers modifiers = parseModifiers(false);

		// ensures we have a 'class' or 'interface' keyword
		TokenType type = tokens.peekType();
		if (!expected(TokenType.TOK_CLASS)) return null;
		tokens.discard();

		// parse the type name
		IName name = parseName();

		if (type == TokenType.TOK_CLASS)
			return parseClass(unit, annots, null, name);

		return null;
	}


	public ITypeDeclaration parseClass( ICompilationUnit unit, List<IAnnotation> annots, IModifiers modifiers, IName name )
	{
		List<ITypeReference> extended = null;

		if (tokens.peekType() == TokenType.TOK_COLON)
			extended = parseExtended();

		if (tokens.peekType() == TokenType.TOK_EOL) tokens.discard();

		TypeBody body = parseClassBody();

		return new TypeDeclaration(unit, annots, modifiers, name, extended, body);
	}

	private TypeBody parseClassBody()
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
	private IFieldDeclaration parseField( List<IAnnotation> annots, IModifiers modifiers )
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

	private void parseMethodDeclaration( IModifiers modifiers, ITypeReference type, ITypeBody body )
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

	private List<IFormalParameter> parseFormatParameters()
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
	 *    Implements = ":" QualifiedName [ "," QualifiedName ]*
	 *
	 * @return
	 */
	public List<ITypeReference> parseExtended()
	{
		List<ITypeReference> implemented = null;

		if (tokens.peekType() == TokenType.TOK_COLON)
		{
			tokens.discard();

			if (tokens.peekType() != TokenType.TOK_NAME)
			{
				context.listener.onError(null, "Expected identifier");
				return null;
			}

			implemented = new LinkedList<ITypeReference>();
			implemented.add( new TypeReference( parseName() ) );

			while (true)
			{
				if (tokens.peekType() != TokenType.TOK_COMA)
					break;

				tokens.discard();
				implemented.add( new TypeReference( parseName() ) );
			}
		}

		return implemented;
	}

	/**
	 * Parse the following grammar:
	 *
	 *    Modifiers = [ TOK_PUBLIC | TOK_PROTECTED | TOK_PRIVATE | TOK_CONST ]
	 *
	 * @param required
	 * @return
	 */
	public IModifiers parseModifiers()
	{
		return parseModifiers(false);
	}

	public List<IAnnotation> parseAnnotations()
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

	/**
	 * Parse the following grammar:
	 *
	 *    Modifiers = [ TOK_PUBLIC | TOK_PROTECTED | TOK_PRIVATE | TOK_CONST ]
	 *
	 * @param required
	 * @return
	 */
	public IModifiers parseModifiers( boolean required )
	{
		int flags = 0;
		boolean done = false;

		// TODO: detect duplicated modifiers

		while (!done)
		{
			switch (tokens.peekType(0))
			{
				case TOK_PUBLIC:
					flags |= IModifiers.PUBLIC;
					tokens.discard();
					break;
				case TOK_PROTECTED:
					flags |= IModifiers.PROTECTED;
					tokens.discard();
					break;
				case TOK_PRIVATE:
					flags |= IModifiers.PRIVATE;
					tokens.discard();
					break;
				case TOK_INTERNAL:
					flags |= IModifiers.INTERNAL;
					tokens.discard();
					break;
				case TOK_PACKAGE:
					flags |= IModifiers.PACKAGE;
					tokens.discard();
					break;
				case TOK_STATIC:
					flags |= IModifiers.STATIC;
					tokens.discard();
					break;
				default:
					done = true;
			}
		}

		//if (flags != 0 || !required)
			return new Modifiers(flags);
		/*else
			return null;*/
	}

}
