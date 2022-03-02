package constants;
public enum Terminal {
	// IDENTIFIER
	IDENTIFIER,

	// KEYWORDS
	ABSTRACT, ASSERT, BOOLEAN, BREAK, BYTE, CASE, CATCH, CHAR, CLASS, CONST, CONTINUE, DEFAULT, DO, DOUBLE, ELSE, ENUM,
	EXTENDS, FINAL, FINALLY, FLOAT, FOR, GOTO, IF, IMPLEMENTS, IMPORT, INSTANCEOF, INT, INTERFACE, LONG, NATIVE, NEW,
	PACKAGE, PRIVATE, PROTECTED, PUBLIC, RETURN, SHORT, STATIC, STRICTFP, SUPER, SWITCH, SYNCHRONIZED, THIS, THROW,
	THROWS, TRANSIENT, TRY, VOID, VOLATILE, WHILE,

	// SEPARATORS
	OPEN_PARENTHESIS, CLOSE_PARENTHESIS, OPEN_BRACE, CLOSE_BRACE, OPEN_BRACKET, CLOSE_BRACKET, SEMICOLON, COMMA, DOT,
	ELLIPSES, AT_SYMBOL, DOUBLE_COLON,

	// OPERATORS
	ASSIGN, LESS_THAN, GREATER_THAN, NOT, BITWISE_COMPLEMENT, TERNARY, COLON, LAMBDA, EQUAL_TO,
	GREATER_THAN_OR_EQUAL_TO, LESS_THAN_OR_EQUAL_TO, NOT_EQUAL_TO, AND_GATE, OR_GATE, INCREMENT, DECREMENT, ADD,
	SUBTRACT, MULTIPLY, DIVIDE, AND, OR, XOR, MODULO, LEFT_SHIFT, RIGHT_SHIFT, UNSIGNED_RIGHT_SHIFT, ASSIGN_ADD,
	ASSIGN_SUBTRACT, ASSIGN_MULTIPLY, ASSIGN_DIVIDE, ASSIGN_AND, ASSIGN_OR, ASSIGN_XOR, ASSIGN_MOD, ASSIGN_LEFT_SHIFT,
	ASSIGN_RIGHT_SHIFT, ASSIGN_UNSIGNED_RIGHT_SHIFT,

	// LITERALS
	INTEGER_LITERAL, FLOATING_POINT_LITERAL, STRING_LITERAL, CHARACTER_LITERAL, BOOLEAN_LITERAL, NULL_LITERAL,


	// From EbnfToJava (for dev purposes only, will not be used in final product)
	SYMBOL,
}