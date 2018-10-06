package beagle.compiler;

import java.util.HashMap;

public enum TokenType
{
	TOK_ABSTRACT("abstract", true),
	TOK_AND("and", true),
	TOK_AS("as", true),
	TOK_ASSIGN("=", false),
	TOK_AT("@", false),
	TOK_BACK_SLASH("\\", false),
	TOK_BAND("&", false),
	TOK_BAND_ASSIGN("&=", false),
	TOK_BANG("!", false),
	TOK_BIN_LITERAL,
	TOK_BOOL_LITERAL,
	TOK_BOOLEAN("bool", true),
	TOK_BOR("|", false),
	TOK_BOR_ASSIGN("|=", false),
	TOK_BREAK("break", true),
	TOK_CASE("case", true),
	TOK_CATCH("catch", true),
	TOK_CHAR("char", true),
	TOK_CLASS("class", true),
	TOK_COLON(":", false),
	TOK_COMA(",", false),
	TOK_COMMENT,
	TOK_CONST("const", true),
	TOK_CONTINUE("continue", true),
	TOK_DEC("--", false),
	TOK_DEC_LITERAL,
	TOK_DEDENT,
	TOK_DEF("def", true),
	TOK_DEFAULT("default", true),
	TOK_DIV("/", false),
	TOK_DIV_ASSIGN("/=", false),
	TOK_DOCSTRING,
	TOK_DOT(".", false),
	TOK_ELIF("elif", true),
	//TOK_DOUBLE("double", true),
	TOK_ELSE("else", true),
	TOK_EOF("end of file", false),
	TOK_EOL("end of line", false),
	TOK_EQ("==", false),
	TOK_EXTENDS("extends", true),
	TOK_FALSE("false", true),
	TOK_FINALLY("finally", true),
	//TOK_FLOAT("float", true),
	TOK_FOR("for", true),
	TOK_FP_LITERAL,
	TOK_GE(">=", false),
	TOK_GT(">", false),
	TOK_HEX_LITERAL,
	TOK_IF("if", true),
	TOK_IMPLEMENTS("implements", true),
	TOK_IMPORT("import", true),
	TOK_IN("in", true),
	TOK_INC("++", false),
	TOK_INDENT,
	TOK_INTERFACE("interface", true),
	TOK_IS("is", true),
	TOK_LE("<=", false),
	TOK_LEFT_BRACE("{", false),
	TOK_LEFT_BRACKET("[", false),
	TOK_LEFT_PAR("(", false),
	TOK_LONG("long", true),
	TOK_LT("<", false),
	TOK_MINUS("-", false),
	TOK_MINUS_ASSIGN("-=", false),
	TOK_MOD("%", false),
	TOK_MOD_ASSIGN("%=", false),
	TOK_MUL("*", false),
	TOK_MUL_ASSIGN("*=", false),
	TOK_NAME,
	TOK_NATIVE("native", true),
	TOK_NE("!=", false),
	TOK_NEG_ASSIGN("~=", false),
	TOK_NEW("new", true),
	TOK_NIN("not in", false),
	TOK_NIS("not is", false),
	TOK_NOT("not", true),
	TOK_NOT_ASSIGN("!=", false),
	TOK_NULL("null", true),
	TOK_OCT_LITERAL,
	TOK_OR("or", true),
	//TOK_INTERNAL("internal", true),
	TOK_PACKAGE("package", true),
	TOK_PLUS("+", false),
	TOK_PLUS_ASSIGN("+=", false),
	TOK_QUEST("?", false),
	TOK_READLOCK("readlock", true),
	//TOK_PRIVATE("private", true),
	//TOK_PROTECTED("protected", true),
	//TOK_PUBLIC("public", true),
	TOK_RETURN("return", true),
	TOK_RIGHT_BRACE("}", false),
	TOK_RIGHT_BRACKET("]", false),
	TOK_RIGHT_PAR(")", false),
	TOK_SEMICOLON(";", false),
	TOK_SHL("<<", false),
	TOK_SHL_ASSIGN("<<=", false),
	TOK_SHR(">>", false),
	TOK_SHR_ASSIGN(">>=", false),
	TOK_STATIC("static", true),
	TOK_STRING_LITERAL("string literal", false),
	TOK_MSTRING_LITERAL("multiline string literal", false),
	TOK_SUPER("super", true),
	TOK_SUSPEND("suspend", true),
	TOK_SWITCH("switch", true),
	TOK_THEN("then", true),
	TOK_THIS("this", true),
	TOK_THROW("throw", true),
	TOK_TILDE("~", false),
	//TOK_UINT8("uint8", true),
	//TOK_UINT16("uint16", true),
	//TOK_UINT32("uint32", true),
	//TOK_UINT64("uint64", true),
	//TOK_INT8("int8", true),
	//TOK_INT16("int16", true),
	//TOK_INT32("int32", true),
	//TOK_INT64("int64", true),
	TOK_TRUE("true", true),
	TOK_TRY("try", true),
	TOK_VAR("var", true),
	TOK_VARARG("vararg", true),
	TOK_WHILE("while", true),
	TOK_WRITELOCK("writelock", true),
	TOK_XOR("^", false),
	TOK_XOR_ASSIGN("^=", false),
	TOK_STRUCT("struct", true);

	private static HashMap<String, TokenType> lookup = new HashMap<>();

	static
	{
		for (TokenType item : TokenType.values())
		{
			if (item.isKeyword && item.name != null)
				lookup.put(item.name, item);
		}
	}

	/**
	 * Return a token type given the name.
	 *
	 * If no match is found, this method returns {@link TOK_NAME}.
	 *
	 * @param text
	 * @return
	 */
	public static TokenType fromString(String text)
	{
		TokenType item = lookup.get(text);
		if (item != null && item.isKeyword) return item;
		return TOK_NAME;
	}

	private boolean isKeyword = false;

	private String name = null;

	private TokenType()
	{
	}

	/**
	 * Create a token type for keywords, operators and symbols.
	 *
	 * If the token type contains a non-null name and the parameter {@code isKeyword} is {@code true},
	 * this name will be used when looking up for matches in the {@link fromString} method.
	 *
	 * The name is also used to output error/warning information.
	 *
	 * @param name
	 * @param isKeyword
	 */
	private TokenType(String name, boolean isKeyword)
	{
		this.name = name;
		this.isKeyword = isKeyword;
	}

	public String getName()
	{
		if (name == null || name.isEmpty()) return name();
		return name;
	}

}
