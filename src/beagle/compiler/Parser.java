package beagle.compiler;

import java.util.LinkedList;
import java.util.List;

import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.FormatParameter;
import beagle.compiler.tree.ICompilationUnit;
import beagle.compiler.tree.IFormalParameter;
import beagle.compiler.tree.IMethodDeclaration;
import beagle.compiler.tree.IModifiers;
import beagle.compiler.tree.IName;
import beagle.compiler.tree.IPackage;
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
	
	/**
	 * Parse the following grammar:
	 * 
	 *   CompilationUnit := PackageDeclaration ImportDeclaration* TypeDeclaration
	 * 
	 * @return
	 */
	public ICompilationUnit parse()
	{
		Token current = tokens.peek();
		IPackage pack = null;
		
		if (current.type == TokenType.TOK_PACKAGE)
			pack = parsePackage();
		else
		{
			context.listener.onError(null, "Syntax error, 'package' expected");
			return null;
		}
		
		ICompilationUnit unit = new CompilationUnit(fileName, pack);
		
		current = tokens.peek();
		while (current != null && current.type == TokenType.TOK_IMPORT)
		{
			ITypeImport imp = parseImport();
			unit.addImport(imp);
			if (imp == null) break;
			current = tokens.peek();
		}
		
		ITypeDeclaration type = parseType(unit);
		
		unit.addType(type);
		return unit;
	}
	
	/**
	 * Parse the following grammar:
	 * 
	 *   QualifiedName := TOK_NAME [ "." TOK_NAME ]*
	 * 
	 * @return
	 */
	public IName parseName()
	{
		Name result = null;
		Token current = tokens.peek();
		if (current.type == TokenType.TOK_NAME)
		{
			result = new Name(current.value);
			tokens.discard();
			
			while (true)
			{
				if (!tokens.lookahead(TokenType.TOK_DOT, TokenType.TOK_NAME))
					break;
				result.append(tokens.peek(1).value);
				tokens.discard(2);
			}
		}
		
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
			tokens.discard(TokenType.TOK_EOL);
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
				tokens.discard(TokenType.TOK_EOL);
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
				tokens.discard(TokenType.TOK_EOL);
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
		// parse the modifiers
		IModifiers modifiers = parseModifiers(false);
		
		// ensures we have a 'class' or 'interface' keyword
		TokenType type = tokens.peekType();
		if (type != TokenType.TOK_CLASS /*&& type.type != TokenType.TOK_INTERFACE*/)
		{
			context.listener.onError(null, "Expected 'class' or 'interface' keyword");
			return null;
		}
		tokens.discard();

		// parse the type name
		IName name = parseName();
		
		if (type == TokenType.TOK_CLASS)
			return parseClass(unit, modifiers, name);
		
		return null;
	}
	
	
	public ITypeDeclaration parseClass( ICompilationUnit unit, IModifiers modifiers, IName name )
	{
		ITypeReference extended = null;
		List<ITypeReference> implemented = null;
		
		if (tokens.peekType() == TokenType.TOK_EXTENDS)
		{
			tokens.discard();
			extended = new TypeReference( parseName() );
		}
		
		if (tokens.peekType() == TokenType.TOK_IMPLEMENTS)
		{
			implemented = parseImplements();
		}
		
		if (!tokens.lookahead(TokenType.TOK_COLON, TokenType.TOK_EOL))
		{
			context.listener.onError(null, "Syntax error, expected ':'");
			return null;
		}
		tokens.discard(2);
		
		TypeBody body = parseClassBody();
		
		return new TypeDeclaration(unit, modifiers, name, extended, implemented, body);
	}
	
	private TypeBody parseClassBody()
	{
		if (tokens.peekType() == TokenType.TOK_INDENT)
		{
			tokens.discard();
			
			TypeBody body = new TypeBody(); 
			
			IModifiers modifiers = parseModifiers();
			IName typeName = parseName();
			IName name = parseName();
			
			if (typeName != null && name == null)
			{
				// assume we have a constructor
				name = typeName;
			}
			
			// check whether we have a method or constructor
			if (tokens.peekType() == TokenType.TOK_LEFT_PAR)
			{
				tokens.discard();
				List<IFormalParameter> params = parseFormatParameters();
				tokens.discard(TokenType.TOK_RIGHT_PAR);
				
				ITypeReference type = new TypeReference(typeName);
				IMethodDeclaration method = new MethodDeclaration(modifiers, type, name, params, null);
				method.setParent(body);
				body.getMethods().add(method);
			}
			
			return body;
		}
		
		return null;
	}

	private List<IFormalParameter> parseFormatParameters()
	{
		IName typeName = parseName();
		IName name = parseName();

		if (typeName == null || name == null)
			return null;
		
		List<IFormalParameter> result = new LinkedList<>();
		result.add( new FormatParameter(name, new TypeReference(typeName)) );
		
		while (tokens.peekType() == TokenType.TOK_COMA)
		{
			tokens.discard();
			typeName = parseName();
			name = parseName();
			result.add( new FormatParameter(name, new TypeReference(typeName)) );
		}
		
		return result;
	}

	/**
	 * Parse the following grammar:
	 * 
	 *    Implements = "implements" QualifiedName [ "," QualifiedName ]*
	 * 
	 * @return
	 */
	public List<ITypeReference> parseImplements()
	{
		List<ITypeReference> implemented = null;
		
		if (tokens.peekType() == TokenType.TOK_IMPLEMENTS)
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
				case TOK_CONST:
					flags |= IModifiers.CONST;
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
