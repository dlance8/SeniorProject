package main;

/**
 * The main.Token class represents an individual lexeme and is capable of representing identifiers, keywords,
 * operators and reserved words. The token class is used by the lexer and called whenever the lexer
 * detects a new main.Token as each main.Token is separated by a space.
 */

public class Token {
	private final int col, line;
	private final Terminal value;
	private final TokenType type;
	private final String text;

	/**
	 * Initializes a main.Token
	 *
	 * @param type  the type of the token, such as INT or KEYWORD.
	 * @param value specifies what keyword, operator, or data type the token represents as understood by the parser.
	 * @param text  the text of the token as it appears in the source code.
	 * @param line  the line number of the first character of the token, starting at 1.
	 * @param col   the column of the first character of the token, starting at 1.
	 */
	public Token(TokenType type, Terminal value, String text, int line, int col) {
		this.type = type;
		this.value = value;
		this.text = text;
		this.line = line;
		this.col = col;
	}

	@Override
	public String toString() {
		return type.toString() + "\n\t" + value.toString() + "\n\tLine " + line + ", Col " + col + "\n\t" + text;
	}

	public int getCol() {
		return col;
	}
	public int getLine() {
		return line;
	}
	public Terminal getValue() {
		return value;
	}
	public TokenType getType() {
		return type;
	}
	public String getText() {
		return text;
	}
}