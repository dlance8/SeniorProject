package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LexerV2 {
	public static final HashMap<String, Terminal> BOOLEAN_LITERALS, NULL_LITERAL, KEYWORDS, SEPARATORS, OPERATORS;
	static {
		BOOLEAN_LITERALS = new HashMap<>();

		NULL_LITERAL = new HashMap<>();

		KEYWORDS = new HashMap<>();
		KEYWORDS.put("abstract",       Terminal.ABSTRACT                   );
		KEYWORDS.put("assert",         Terminal.ASSERT                     );
		KEYWORDS.put("boolean",        Terminal.BOOLEAN                    );
		KEYWORDS.put("break",          Terminal.BREAK                      );
		KEYWORDS.put("byte",           Terminal.BYTE                       );
		KEYWORDS.put("case",           Terminal.CASE                       );
		KEYWORDS.put("catch",          Terminal.CATCH                      );
		KEYWORDS.put("char",           Terminal.CHAR                       );
		KEYWORDS.put("class",          Terminal.CLASS                      );
		KEYWORDS.put("const",          Terminal.CONST                      );
		KEYWORDS.put("continue",       Terminal.CONTINUE                   );
		KEYWORDS.put("default",        Terminal.DEFAULT                    );
		KEYWORDS.put("do",             Terminal.DO                         );
		KEYWORDS.put("double",         Terminal.DOUBLE                     );
		KEYWORDS.put("else",           Terminal.ELSE                       );
		KEYWORDS.put("enum",           Terminal.ENUM                       );
		KEYWORDS.put("extends",        Terminal.EXTENDS                    );
		KEYWORDS.put("final",          Terminal.FINAL                      );
		KEYWORDS.put("finally",        Terminal.FINALLY                    );
		KEYWORDS.put("float",          Terminal.FLOAT                      );
		KEYWORDS.put("for",            Terminal.FOR                        );
		KEYWORDS.put("goto",           Terminal.GOTO                       );
		KEYWORDS.put("if",             Terminal.IF                         );
		KEYWORDS.put("implements",     Terminal.IMPLEMENTS                 );
		KEYWORDS.put("import",         Terminal.IMPORT                     );
		KEYWORDS.put("instanceof",     Terminal.INSTANCEOF                 );
		KEYWORDS.put("int",            Terminal.INT                        );
		KEYWORDS.put("interface",      Terminal.INTERFACE                  );
		KEYWORDS.put("long",           Terminal.LONG                       );
		KEYWORDS.put("native",         Terminal.NATIVE                     );
		KEYWORDS.put("new",            Terminal.NEW                        );
		KEYWORDS.put("package",        Terminal.PACKAGE                    );
		KEYWORDS.put("private",        Terminal.PRIVATE                    );
		KEYWORDS.put("protected",      Terminal.PROTECTED                  );
		KEYWORDS.put("public",         Terminal.PUBLIC                     );
		KEYWORDS.put("return",         Terminal.RETURN                     );
		KEYWORDS.put("short",          Terminal.SHORT                      );
		KEYWORDS.put("static",         Terminal.STATIC                     );
		KEYWORDS.put("strictfp",       Terminal.STRICTFP                   );
		KEYWORDS.put("super",          Terminal.SUPER                      );
		KEYWORDS.put("switch",         Terminal.SWITCH                     );
		KEYWORDS.put("synchronized",   Terminal.SYNCHRONIZED               );
		KEYWORDS.put("this",           Terminal.THIS                       );
		KEYWORDS.put("throw",          Terminal.THROW                      );
		KEYWORDS.put("throws",         Terminal.THROWS                     );
		KEYWORDS.put("transient",      Terminal.TRANSIENT                  );
		KEYWORDS.put("try",            Terminal.TRY                        );
		KEYWORDS.put("void",           Terminal.VOID                       );
		KEYWORDS.put("volatile",       Terminal.VOLATILE                   );
		KEYWORDS.put("while",          Terminal.WHILE                      );

		SEPARATORS = new HashMap<>();
		SEPARATORS.put("(",     Terminal.OPEN_PARENTHESIS   );
		SEPARATORS.put(")",     Terminal.CLOSE_PARENTHESIS  );
		SEPARATORS.put("{",     Terminal.OPEN_BRACE         );
		SEPARATORS.put("}",     Terminal.CLOSE_BRACE        );
		SEPARATORS.put("[",     Terminal.OPEN_BRACKET       );
		SEPARATORS.put("]",     Terminal.CLOSE_BRACKET      );
		SEPARATORS.put(";",     Terminal.SEMICOLON          );
		SEPARATORS.put(",",     Terminal.COMMA              );
		SEPARATORS.put(".",     Terminal.DOT                );
		SEPARATORS.put("...",   Terminal.ELLIPSES           );
		SEPARATORS.put("@",     Terminal.AT_SYMBOL          );
		SEPARATORS.put("::",    Terminal.DOUBLE_COLON       );

		OPERATORS = new HashMap<>();
		OPERATORS.put("=",      Terminal.ASSIGN                     );
		OPERATORS.put(">",      Terminal.GREATER_THAN               );
		OPERATORS.put("<",      Terminal.LESS_THAN                  );
		OPERATORS.put("!",      Terminal.NOT                        );
		OPERATORS.put("~",      Terminal.BITWISE_COMPLEMENT         );
		OPERATORS.put("?",      Terminal.TERNARY                    );
		OPERATORS.put(":",      Terminal.COLON                      );
		OPERATORS.put("->",     Terminal.LAMBDA                     );
		OPERATORS.put("==",     Terminal.EQUAL_TO                   );
		OPERATORS.put(">=",     Terminal.GREATER_THAN_OR_EQUAL_TO   );
		OPERATORS.put("<=",     Terminal.LESS_THAN_OR_EQUAL_TO      );
		OPERATORS.put("!=",     Terminal.NOT_EQUAL_TO               );
		OPERATORS.put("&&",     Terminal.AND_GATE                   );
		OPERATORS.put("||",     Terminal.OR_GATE                    );
		OPERATORS.put("++",     Terminal.INCREMENT                  );
		OPERATORS.put("--",     Terminal.DECREMENT                  );
		OPERATORS.put("+",      Terminal.ADD                        );
		OPERATORS.put("-",      Terminal.SUBTRACT                   );
		OPERATORS.put("*",      Terminal.MULTIPLY                   );
		OPERATORS.put("/",      Terminal.DIVIDE                     );
		OPERATORS.put("&",      Terminal.AND                        );
		OPERATORS.put("|",      Terminal.OR                         );
		OPERATORS.put("^",      Terminal.XOR                        );
		OPERATORS.put("%",      Terminal.MODULO                     );
		OPERATORS.put("<<",     Terminal.LEFT_SHIFT                 );
		OPERATORS.put(">>",     Terminal.RIGHT_SHIFT                );
		OPERATORS.put(">>>",    Terminal.UNSIGNED_RIGHT_SHIFT       );
		OPERATORS.put("+=",     Terminal.ASSIGN_ADD                 );
		OPERATORS.put("-=",     Terminal.ASSIGN_SUBTRACT);
		OPERATORS.put("*=",     Terminal.ASSIGN_MULTIPLY);
		OPERATORS.put("/=",     Terminal.ASSIGN_DIVIDE);
		OPERATORS.put("&=",     Terminal.ASSIGN_AND                 );
		OPERATORS.put("|=",     Terminal.ASSIGN_OR                  );
		OPERATORS.put("^=",     Terminal.ASSIGN_XOR                 );
		OPERATORS.put("%=",     Terminal.ASSIGN_MOD                 );
		OPERATORS.put("<<=",    Terminal.ASSIGN_LEFT_SHIFT);
		OPERATORS.put(">>=",    Terminal.ASSIGN_RIGHT_SHIFT);
		OPERATORS.put(">>>=",   Terminal.ASSIGN_UNSIGNED_RIGHT_SHIFT);
	}

	private boolean parsing;
	private int line, column;
	private final ArrayList<Token> tokens = new ArrayList<>();
	private final String in;
	private StringBuilder currentToken = new StringBuilder();
	public LexerV2(String filePath) throws FileNotFoundException {
		StringBuilder stringBuilder = new StringBuilder();

		Scanner file = new Scanner(new File(filePath));
		while (file.hasNextLine()) {
			stringBuilder.append(file.nextLine()).append('\n');
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.setLength(stringBuilder.length() - 1);
		}

		in = stringBuilder.toString();

		//size = in.length();

		System.out.println(in);
	}

	private int currentIndex;
	private char currentChar;

	public static void main(String[] args) throws FileNotFoundException {
		//List<Token> tokens = new Lexer().lexJava("src/main/Lexer.java");
		List<Token> tokens = new LexerV2("in/Sample.java").lex();

		for (Token token : tokens) {
			System.out.println(token);
		}

		System.exit(1);

		StringBuilder sb = new StringBuilder();

		StringBuilder newLine = new StringBuilder().append('\n');

		boolean inFor = false;
		int parenthesis = 0;

		for (Token token : tokens) {
			switch (token.getValue()) {
				case SEMICOLON:
					sb.append(token.getText());
					if (inFor) {
						sb.append(' ');
					} else {
						sb.append(newLine);
					}
					break;
				case FOR:
					inFor = true;
					parenthesis = 0;
					sb.append(token.getText()).append(' ');
					break;
				case OPEN_PARENTHESIS:
					if (inFor) {
						++parenthesis;
					}
					sb.append(token.getText()).append(' ');
					break;
				case CLOSE_PARENTHESIS:
					if (inFor && --parenthesis == 0) {
						inFor = false;
					}
					sb.append(token.getText()).append(' ');
					break;
				case OPEN_BRACE:
					sb.append(token.getText()).append(newLine.append('\t'));
					break;
				case CLOSE_BRACE:
					newLine.setLength(newLine.length() - 1);
					sb.setLength(sb.length() - 1);
					sb.append(token.getText()).append(newLine);
					break;
				case CHARACTER_LITERAL:
					sb.append('\'').append(token.getText()).append("' ");
					break;
				case STRING_LITERAL:
					sb.append('"').append(token.getText()).append("\" ");
					break;
				default:
					sb.append(token.getText()).append(' ');
			}
		}
		System.out.println(sb);

	}

	public ArrayList<Token> lex() {
		input();
		return tokens;
	}



	private boolean unicodeInputCharacter() {
		// unicode input character = unicode escape | raw input character ;
		return unicodeEscape() || rawInputCharacter();
	}
	private boolean unicodeEscape() {
		// unicode escape = "\" , unicode marker , hex digit , hex digit , hex digit , hex digit ;
		return acceptTerminal('\\') && unicodeMarker() && hexDigit() && hexDigit() && hexDigit() && hexDigit();
	}
	private boolean unicodeMarker() {
		// unicode marker = "u" , { "u" } ;
		return acceptTerminal('u') && acceptTerminalRepeating('u');
	}
	private boolean hexDigit() {
		return acceptAnyTerminal('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f',
				'A', 'B', 'C', 'D', 'E', 'F');
	}
	private boolean rawInputCharacter() {
		// raw input character = ? any Unicode character ? ;
		final boolean accepted = parsing;
		if (accepted)
			advance();
		return accepted;
	}



	private boolean lineTerminator() {
		/* line terminator = U+000A
		 *                 | U+000D
		 *                 | U+000D , U+000A ; */
		final boolean accepted = acceptTerminal((char) 0X000A) || (acceptTerminal((char) 0x000D) && acceptTerminalOptional((char) 0x000A));
		if (accepted)
			++line;
		return accepted;
	}
	private boolean inputCharacter() {
		// input character = unicode input character - ( U+000D | U+000A ) ;

		boolean accepted = acceptNonTerminal(this::unicodeInputCharacter);
		if (accepted) {
			final char result = currentToken.charAt(currentToken.length() - 1);
			if (result == 0x000D || result == 0x000A) {
				accepted = false;
			}
		}
		return accepted;
	}



	private boolean input() {
		// input = { input element } , [ sub ] ;
		while (parsing) {
			final int oldLine = line;
			currentToken.setLength(0);
			if (acceptNonTerminal(this::inputElement)) {
				break;
			}
			column = line == oldLine ? column + currentToken.length() : 0;
		}
		acceptNonTerminalOptional(this::sub);
		return true;
	}
	private boolean inputElement() {
		// input element = white space | comment | token ;
		return acceptNonTerminal(this::whiteSpace)
				|| acceptNonTerminal(this::comment)
				|| acceptNonTerminal(this::token);
	}
	private boolean token() {
//		// token = identifier | keyword | literal | separator | operator ;
		return     acceptNonTerminal(this::identifier)
				|| acceptNonTerminal(this::keyword)
				|| acceptNonTerminal(this::literal)
				|| acceptNonTerminal(this::separator)
				|| acceptNonTerminal(this::operator);
	}
	private boolean sub() {
		// sub = ASCII SUB ;
		return acceptTerminal('\u001A'); // SUB
	}



	private boolean whiteSpace() {
		// white space = U+0040 | U+0009 | U+000C | line terminator ;
		return acceptAnyTerminal(
					(char) 0x0040, // ASCII SP, space
					(char) 0x0009, // ASCII HT, horizontal tab
					(char) 0x000C) // ASCII FF, form feed
				|| lineTerminator();
	}



	private boolean comment() {
		// comment = traditional comment | end of line comment ;
		return acceptAnyNonTerminal(this::traditionalComment, this::endOfLineComment);
	}
	private boolean traditionalComment() {
		// traditional comment = "/*" , comment tail ;
		return acceptAllTerminals("/*") && commentTail();
	}
	private boolean commentTail() {
		/* comment tail = "*" , comment tail star
		 *              | not star , comment tail ; */
		return acceptTerminal('*') && acceptNonTerminal(this::commentTailStar)
				|| acceptNonTerminal(() -> notStar() && commentTail());
	}
	private boolean commentTailStar() {
		/* comment tail star = "/"
		 *                   | "*" , comment tail star
		 *                   | not star not slash , comment tail ; */
		return acceptTerminal('/')
				|| acceptNonTerminal(() -> acceptTerminal('*') && commentTailStar())
				|| acceptNonTerminal(() -> notStarNotSlash() && commentTail());
	}
	private boolean notStar() {
		/* not star = input character - "*"
		 *          | line terminator ; */
		return acceptNonTerminalExcepting(this::inputCharacter, "*")
				|| acceptNonTerminal(this::lineTerminator);
	}
	private boolean notStarNotSlash() {
		/* not star not slash = input character - ( "*" | "/" )
		 *                    | line terminator ;
		 */
		return acceptNonTerminalExcepting(this::inputCharacter, "*", "/")
				|| acceptNonTerminal(this::lineTerminator);
	}
	private boolean endOfLineComment() {
		// end of line comment = "//" , { input character }
		final boolean accepted = acceptAllTerminals("//");
		if (accepted)
			acceptNonTerminalRepeating(this::inputCharacter);
		return accepted;
	}



	private boolean identifier() {
		// identifier           = identifier chars - ( keyword | boolean literal | null literal ) ;
		// identifier chars     = java letter , { java letter or digit } ;
		// java letter          = ? any Unicode character for which Character.isJavaIdentifierStart(int) returns true ? ;
		// java letter or digit = ? any Unicode character for which Character.isJavaIdentifierPart(int) returns true ? ;
		StringBuilder stringBuilder = new StringBuilder();
		boolean accepted = Character.isJavaIdentifierStart(currentChar);
		if (accepted) {
			do {
				stringBuilder.append(currentChar);
			} while (advance() && Character.isJavaIdentifierPart(currentChar));
			final String result = stringBuilder.toString();
			accepted = !(KEYWORDS.containsKey(result) || BOOLEAN_LITERALS.containsKey(result) || NULL_LITERAL.containsKey(result));
			if (accepted) {
				tokens.add(new Token(TokenType.IDENTIFIER, Terminal.IDENTIFIER, result, line, column));
			}
		}
		return accepted;
	}



	private boolean keyword() {
		return acceptTokenFromSet(KEYWORDS, TokenType.KEYWORD);
	}



	private boolean literal() {
		/* literal = integer literal | floating point literal | boolean literal
		 *         | character literal | string literal | null literal ; */
		return acceptAnyNonTerminal(this::integerLiteral, this::floatingPointLiteral, this::booleanLiteral,
			this::characterLiteral, this::stringLiteral, this::nullLiteral);
	}

	private boolean integerLiteral() {
		/* integer literal = decimal integer literal | hex integer literal
		 *                 | octal integer literal | binary integer literal ; */
		final boolean accepted = acceptAnyNonTerminal(this::decimalIntegerLiteral, this::hexIntegerLiteral,
				this::octalIntegerLiteral, this::binaryIntegerLiteral);
		if (accepted)
			tokens.add(new Token(TokenType.INTEGER_LITERAL, Terminal.INTEGER_LITERAL, currentToken.toString(), line, column));
		return accepted;
	}
	private boolean decimalIntegerLiteral() {
		// decimal integer literal = decimal numeral , [ integer type suffix ] ;
		return acceptNonTerminal(this::decimalNumeral) && acceptNonTerminalOptional(this::integerTypeSuffix);
	}
	private boolean hexIntegerLiteral() {
		// hex integer literal = hex numeral , [ integer type suffix ] ;
		return acceptNonTerminal(this::hexNumeral) && acceptNonTerminalOptional(this::integerTypeSuffix);
	}
	private boolean octalIntegerLiteral() {
		// octal integer literal = octal numeral , [ integer type suffix ] ;
		return acceptNonTerminal(this::octalNumeral) && acceptNonTerminalOptional(this::integerTypeSuffix);
	}
	private boolean binaryIntegerLiteral() {
		// binary integer literal = binary numeral , [ integer type suffix ] ;
		return acceptNonTerminal(this::binaryNumeral) && acceptNonTerminalOptional(this::integerTypeSuffix);
	}
	private boolean integerTypeSuffix() {
		// integer type suffix = "l" | "L" ;
		return acceptAnyTerminal('l', 'L');
	}

	private boolean decimalNumeral() {
		/* decimal numeral = "0"
		 *                 | non zero digit , [ digits ]
		 *                 | non zero digit , underscores , digits ; */
		return acceptTerminal('0')
				|| (acceptNonTerminal(this::nonZeroDigit)
						&& (!acceptNonTerminal(this::underscores) | acceptNonTerminal(this::digits)));
	}
	private boolean nonZeroDigit() {
		// non zero digit = "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
		return acceptAnyTerminal('1', '2', '3', '4', '5', '6', '7', '8', '9');
	}
	private boolean digits() {
		/* digits = digit
		 *        | digit , [ digits and underscores ] , digit ; */
		return acceptNonTerminal(this::digit) && acceptNonTerminalOptional(
				() -> acceptNonTerminalOptional(this::digitsAndUnderscores) && digit());
	}
	private boolean digit() {
		// digit = "0" | non zero digit ;
		return acceptTerminal('0') || acceptNonTerminal(this::nonZeroDigit);
	}
	private boolean digitsAndUnderscores() {
		// digits and underscores = digit or underscore , { digit or underscore } ;
		return acceptNonTerminal(this::digitOrUnderscore) && acceptNonTerminalRepeating(this::digitOrUnderscore);
	}
	private boolean digitOrUnderscore() {
		// digit or underscore = digit | "_" ;
		return acceptNonTerminal(this::digit) || acceptTerminal('_');
	}
	private boolean underscores() {
		// underscores = "_" , { "_" } ;
		return acceptTerminal('_') && acceptTerminalRepeating('_');
	}

	private boolean hexNumeral() {
		/* hex numeral = "0" , "x" , hex digits
		 *             | "0" , "X" , hex digits ; */
		return acceptTerminal('0') && acceptAnyTerminal('x', 'X') && acceptNonTerminal(this::hexDigits);
	}
	private boolean hexDigits() {
		/* hex digits = hex digit
		 *            | hex digit , [ hex digits and underscores ] , hex digit ; */
		return acceptNonTerminal(this::hexDigit)
				&& (!acceptNonTerminal(this::hexDigitsAndUnderscores) | acceptNonTerminal(this::hexDigit));
	}
	private boolean hexDigitsAndUnderscores() {
		// hex digits and underscores = hex digit or underscore , { hex digit or underscore } ;
		return hexDigitOrUnderscore() && acceptNonTerminalRepeating(this::hexDigitOrUnderscore);
	}
	private boolean hexDigitOrUnderscore() {
		// hex digit or underscore = hex digit | "_" ;
		return acceptNonTerminal(this::hexDigit) || acceptTerminal('_');
	}

	private boolean octalNumeral() {
		// octal numeral = "0" , octal digits | "0" , underscores , octal digits ;
		return acceptTerminal('0') && acceptNonTerminalOptional(this::underscores) && acceptNonTerminal(this::octalDigits);
	}
	private boolean octalDigits() {
		/* octal digits = octal digit
		 *              | octal digit , [ octal digits and underscores ] , octal digit ; */
		return acceptNonTerminal(this::octalDigit)
				&& (!acceptNonTerminal(this::octalDigitsAndUnderscores) | acceptNonTerminal(this::octalDigit));
	}
	private boolean octalDigit() {
		// octal digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" ;
		return acceptAnyTerminal('0', '1', '2', '3', '4', '5', '6', '7');
	}
	private boolean octalDigitsAndUnderscores() {
		// octal digits and underscores = octal digit or underscore , { octal digit or underscore } ;
		return acceptNonTerminal(this::octalDigitOrUnderscore) && acceptNonTerminalRepeating(this::octalDigitOrUnderscore);
	}
	private boolean octalDigitOrUnderscore() {
		// octal digit or underscore = octal digit | "_" ;
		return acceptNonTerminal(this::octalDigit) || acceptTerminal('_');
	}

	private boolean binaryNumeral() {
		/* binary numeral = "0" , "b" , binary digits
		 *                | "0" , "B" , binary digits ; */
		return acceptTerminal('0') && acceptAnyTerminal('b', 'B') && acceptNonTerminal(this::binaryDigits);
	}
	private boolean binaryDigits() {
		/* binary digits = binary digit
		 *               | binary digit , binary digits and underscores , binary digit ; */
		return acceptNonTerminal(this::binaryDigit) &&
				(!acceptNonTerminal(this::binaryDigitsAndUnderscores) | acceptNonTerminal(this::binaryDigit));
	}
	private boolean binaryDigit() {
		// binary digit = "0" | "1" ;
		return acceptAnyTerminal('0', '1');
	}
	private boolean binaryDigitsAndUnderscores() {
		// binary digits and underscores = binary digit | underscore , { binary digits and underscores } ;
		return acceptNonTerminal(this::binaryDigitOrUnderscore)
				&& acceptNonTerminalRepeating(this::binaryDigitOrUnderscore);
	}
	private boolean binaryDigitOrUnderscore() {
		// binary digit or underscore = binary digit | "_" ;
		return acceptNonTerminal(this::binaryDigit) || acceptTerminal('_');
	}
	private boolean optional(boolean x) {
		return true;
	}
	private boolean floatingPointLiteral() {
		// floating point literal = decimal floating point literal | hexadecimal floating point literal ;
		final boolean accepted = acceptAnyNonTerminal(this::decimalFloatingPointLiteral, this::hexadecimalFloatingPointLiteral);
		if (accepted)
			tokens.add(new Token(TokenType.FLOATING_POINT_LITERAL, Terminal.FLOATING_POINT_LITERAL, currentToken.toString(), line, column));
		return accepted;
	}
	private boolean decimalFloatingPointLiteral() {
		/* decimal floating point literal = digits , "." , [ digits ] , [ exponent part ] , [ float type suffix ]
		 *                                | "." , digits , [ exponent part ] , [ float type suffix ]
		 *                                | digits , exponent part , [ float type suffix ],
		 *                                | digits , [ exponent part ] , float type suffix ; */
		return acceptAnyNonTerminal(
			() -> digits() && acceptTerminal('.') && acceptNonTerminalOptional(this::digits) && acceptNonTerminalOptional(this::exponentPart) && acceptNonTerminalOptional(this::floatTypeSuffix),
			() -> acceptTerminal('.') && digits() && acceptNonTerminalOptional(this::exponentPart) && acceptNonTerminalOptional(this::floatTypeSuffix),
			() -> digits() && (acceptNonTerminalOptional(this::exponentPart) | acceptNonTerminalOptional(this::floatTypeSuffix)));

		/*

		a & b & ( c | T ) | ( a & ( b | T ) )


		* */
	}
	private boolean exponentPart() {
		/* exponent part = exponent indicator , signed integer ;
		 * exponent indicator = "e" | "E" ; */
		return acceptAnyTerminal('e', 'E') && signedInteger();
	}
	private boolean signedInteger() {
		/* signed integer = [ sign ] , digits ;
		 * sign = "+" | "-" ; */
		acceptAnyTerminal('+', '-');
		return digits();
	}
	private boolean floatTypeSuffix() {
		// float type suffix = "f" | "F" | "d" | "D" ;
		return acceptAnyTerminal('f', 'F', 'd', 'D');
	}

	private boolean hexadecimalFloatingPointLiteral() {
		/* hexadecimal floating point literal = hex significand , binary exponent , [ float type suffix ] ;
		 * binary exponent = binary exponent indicator , signed integer ;
		 * binary exponent indicator = "p" | "P" ; */
		final boolean accepted = hexSignificand() && acceptAnyTerminal('p', 'P') && signedInteger();
		if (accepted)
			floatTypeSuffix();
		return accepted;
	}
	private boolean hexSignificand() {
		/* hex significand = hex numeral , [ "." ]
		 *                 | "0x" , [ hex digits ] , "." , hex digits
		 *                 | "0X" , [ hex digits ] , "." , hex digits */
		return acceptAnyNonTerminal(
				() -> hexNumeral() && acceptTerminalOptional('.'),
				() -> acceptTerminal('0') && acceptAnyTerminal('x', 'X') && acceptNonTerminalOptional(this::hexDigits) && acceptTerminal('.') && hexDigits());
	}

	private boolean booleanLiteral() {
		return acceptTokenFromSet(BOOLEAN_LITERALS, TokenType.BOOLEAN_LITERAL);
	}

	private boolean characterLiteral() {
		/* character literal = "'" , single character , "'"
		 *                   | "'" , escape sequence , "'" ; */
		final boolean accepted = acceptTerminal('\'')
				&& (acceptNonTerminal(this::singleCharacter) || acceptNonTerminal(this::escapeSequence))
				&& acceptTerminal('\'');
		if (accepted)
			tokens.add(new Token(TokenType.CHARACTER_LITERAL, Terminal.CHARACTER_LITERAL, currentToken.substring(1, currentToken.length() - 1), line, column));
		return accepted;
	}
	private boolean singleCharacter() {
		return acceptNonTerminalExcepting(this::inputCharacter, "'", "\\");
	}

	private boolean stringLiteral() {
		/* string literal = '"' , { string character } , '"' ;
		 * string character = input character - ('"' | '\')
		 *                  | escape sequence;
		 * (* It is a compile-time error for a line terminator to appear after the opening " and before the closing matching ". *) */
		final boolean accepted = acceptTerminal('"') && acceptNonTerminalRepeating(this::stringCharacter) && acceptTerminal('"');
		if (accepted) {
			tokens.add(new Token(TokenType.STRING_LITERAL, Terminal.STRING_LITERAL, currentToken.substring(1, currentToken.length() - 1), line, column));
		}
		return accepted;
	}
	private boolean stringCharacter() {
		if (acceptNonTerminal(this::lineTerminator)) {
			error("");// TODO
		}
		return acceptNonTerminalExcepting(this::inputCharacter, "\"", "\\") || acceptNonTerminal(this::escapeSequence);
	}

	private boolean escapeSequence() {
		/* escape sequence = "\" , "b" (* backspace BS, U+0008 *)
		 *                 | "\" , "t" (* horizontal tab HT, U+0009 *)
		 *                 | "\" , "n" (* linefeed LF, U+000A *)
		 *                 | "\" , "f" (* form feed FF, U+000C *)
		 *                 | "\" , "r" (* carriage return CR, U+000D *)
		 *                 | "\" , '"' (* double quote ", U+0022 *)
		 *                 | "\" , "'" (* single quote ', U+0027 *)
		 *                 | "\" , "\" (* backslash \, U+005C *)
		 *                 | octal escape ;
		 * octal escape = "\" , octal digit
		 *              | "\" , octal digit , octal digit
		 *              | "\" , zero to three , octal digit , octal digit ;
		 * zero to three = "0" | "1" | "2" | "3" ;
		 * (* It is a compile-time error if the character following a backslash in an escape sequence is not an ASCII
		 *    b, t, n, f, r, ", ', \, 0, 1, 2, 3, 4, 5, 6, or 7. The Unicode escape \u is processed earlier. *) */

		boolean accepted = acceptTerminal('\\');
		if (accepted) {
			if (acceptAnyTerminal('b', 't', 'n', 'f', 'r', '"', '\'', '\\')) {

			} else if (acceptAnyTerminal('0', '1', '2', '3')) {

			} else if (octalDigit()) {

			} else {

			}
		}

		boolean accepted = acceptTerminal('\\');
		if (accepted) {
			accepted = acceptAnyTerminal('b', 't', 'n', 'f', 'r', '"', '\'', '\\')
					|| acceptAnyNonTerminal(
							this::octalDigit,
							() -> octalDigit() && octalDigit(),
							() -> acceptAnyTerminal('0', '1', '2', '3') && octalDigit() && octalDigit());
			if (!accepted)
				error("");// TODO
		}
		return accepted;
	}

	private boolean nullLiteral() {
		return acceptTokenFromSet(NULL_LITERAL, TokenType.NULL_LITERAL);
	}



	private boolean separator() {
		return acceptTokenFromSet(SEPARATORS, TokenType.SEPARATOR);
	}

	private boolean operator() {
		return acceptTokenFromSet(OPERATORS, TokenType.OPERATOR);
	}


	/**
	 * Returns true and adds a new {@code Token} to {@code tokens} if the
	 * next token in the source code is defined in the key set of a given
	 * {@code HashMap}.
	 *
	 * @param   hashMap  the hashmap
	 * @param   tokenType  the type of token that could be added
	 * @return  {@code true} if a new {@code Token} was added;
	 *          {@code false} otherwise.
	 * */
	private boolean acceptTokenFromSet(HashMap<String, Terminal> hashMap, TokenType tokenType) {
		boolean accepted = false;
		for (String key : hashMap.keySet()) {
			if (acceptAllTerminals(key)) {
				accepted = true;
				tokens.add(new Token(tokenType, hashMap.get(key), key, line, column));
				break;
			}
		}
		return accepted;
	}


	private interface NonTerminalAcceptor {
		boolean acceptNonTerminal();
	}
	private boolean acceptNonTerminal(NonTerminalAcceptor acceptor) {
		final char oldCurrentChar = currentChar;
		final int oldCurrentIndex = currentIndex;

		final int oldCurrentTokenLength = currentToken.length();

		final boolean accepted = acceptor.acceptNonTerminal();
		if (!accepted) {
			currentChar = oldCurrentChar;
			currentIndex = oldCurrentIndex;
			currentToken.setLength(oldCurrentTokenLength);
		}
		return accepted;
	}
	private boolean acceptAnyNonTerminal(NonTerminalAcceptor... acceptors) {
		for (NonTerminalAcceptor acceptor : acceptors) {
			if (acceptNonTerminal(acceptor)) {
				return true;
			}
		}
		return false;
	}
	private boolean acceptAllNonTerminals(NonTerminalAcceptor... acceptors) {
		for (NonTerminalAcceptor acceptor : acceptors) {
			if (!acceptNonTerminal(acceptor)) {
				return false;
			}
		}
		return true;
	}

	private boolean acceptNonTerminalExcepting(NonTerminalAcceptor o, String... exceptions) {
		final StringBuilder oldCurrentToken = currentToken;
		currentToken = new StringBuilder();
		boolean accepted = acceptNonTerminal(o);
		if (accepted) {
			String result = currentToken.toString();
			for (String exception : exceptions) {
				if (result.equals(exception)) {
					accepted = false;
					break;
				}
			}
			if (accepted) {
				oldCurrentToken.append(currentToken);
			}
		}
		currentToken = oldCurrentToken;
		return accepted;
	}
	private boolean acceptNonTerminalOptional(NonTerminalAcceptor o) {
		acceptNonTerminal(o);
		return true;
	}
	private boolean acceptNonTerminalRepeating(NonTerminalAcceptor o) {
		while (true) {
			if (!acceptNonTerminal(o)) {
				break;
			}
		}
		return true;
	}
	private boolean acceptTerminal(char value) {
		final boolean accepted = parsing && currentChar == value;
		if (accepted) {
			currentToken.append(value);
			advance();
		}
		return accepted;
	}
	private boolean acceptAnyTerminal(char... values) {
		for (char value : values) {
			if (acceptTerminal(value)) {
				return true;
			}
		}
		return false;
	}
	private boolean acceptAllTerminals(char... values) {
		final char oldCurrentChar = currentChar;
		final int oldCurrentIndex = currentIndex;
		final int oldCurrentTokenLength = currentToken.length();
		boolean accepted = false;
		for (char value : values) {
			if (acceptTerminal(value)) {
				accepted = true;
				break;
			}
		}
		if (!accepted) {
			currentChar = oldCurrentChar;
			currentIndex = oldCurrentIndex;
			currentToken.setLength(oldCurrentTokenLength);
		}
		return accepted;
	}
	private boolean acceptAllTerminals(String string) {
		return acceptAllTerminals(string.toCharArray());
	}
	private boolean acceptTerminalOptional(char value) {
		acceptTerminal(value);
		return true;
	}
	private boolean acceptTerminalRepeating(char value) {
		while (true) {
			if (!acceptTerminal(value)) {
				break;
			}
		}
		return true;
	}

	private void error(String message) {
		System.err.println(message);
		System.exit(1);
	}
	private void start() {
		currentIndex = -1;
		currentChar = 0;
	}
	private boolean advance() {
		parsing = ++currentIndex < in.length();
		currentChar = parsing ? in.charAt(currentIndex) : 0;
		return parsing;
	}
}