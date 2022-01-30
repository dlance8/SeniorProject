package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Lexer {
    private static final HashMap<String, Terminal> TERMINALS;
    static {
        TERMINALS = new HashMap<>();

        // RESERVED WORDS
        TERMINALS.put("abstract",       Terminal.ABSTRACT                   );
        TERMINALS.put("assert",         Terminal.ASSERT                     );
        TERMINALS.put("boolean",        Terminal.BOOLEAN                    );
        TERMINALS.put("break",          Terminal.BREAK                      );
        TERMINALS.put("byte",           Terminal.BYTE                       );
        TERMINALS.put("case",           Terminal.CASE                       );
        TERMINALS.put("catch",          Terminal.CATCH                      );
        TERMINALS.put("char",           Terminal.CHAR                       );
        TERMINALS.put("class",          Terminal.CLASS                      );
        TERMINALS.put("const",          Terminal.CONST                      );
        TERMINALS.put("continue",       Terminal.CONTINUE                   );
        TERMINALS.put("default",        Terminal.DEFAULT                    );
        TERMINALS.put("do",             Terminal.DO                         );
        TERMINALS.put("double",         Terminal.DOUBLE                     );
        TERMINALS.put("else",           Terminal.ELSE                       );
        TERMINALS.put("enum",           Terminal.ENUM                       );
        TERMINALS.put("extends",        Terminal.EXTENDS                    );
        TERMINALS.put("final",          Terminal.FINAL                      );
        TERMINALS.put("finally",        Terminal.FINALLY                    );
        TERMINALS.put("float",          Terminal.FLOAT                      );
        TERMINALS.put("for",            Terminal.FOR                        );
        TERMINALS.put("goto",           Terminal.GOTO                       );
        TERMINALS.put("if",             Terminal.IF                         );
        TERMINALS.put("implements",     Terminal.IMPLEMENTS                 );
        TERMINALS.put("import",         Terminal.IMPORT                     );
        TERMINALS.put("instanceof",     Terminal.INSTANCEOF                 );
        TERMINALS.put("int",            Terminal.INT                        );
        TERMINALS.put("interface",      Terminal.INTERFACE                  );
        TERMINALS.put("long",           Terminal.LONG                       );
        TERMINALS.put("native",         Terminal.NATIVE                     );
        TERMINALS.put("new",            Terminal.NEW                        );
        TERMINALS.put("null",           Terminal.NULL                       );
        TERMINALS.put("package",        Terminal.PACKAGE                    );
        TERMINALS.put("private",        Terminal.PRIVATE                    );
        TERMINALS.put("protected",      Terminal.PROTECTED                  );
        TERMINALS.put("public",         Terminal.PUBLIC                     );
        TERMINALS.put("return",         Terminal.RETURN                     );
        TERMINALS.put("short",          Terminal.SHORT                      );
        TERMINALS.put("static",         Terminal.STATIC                     );
        TERMINALS.put("strictfp",       Terminal.STRICTFP                   );
        TERMINALS.put("super",          Terminal.SUPER                      );
        TERMINALS.put("switch",         Terminal.SWITCH                     );
        TERMINALS.put("synchronized",   Terminal.SYNCHRONIZED               );
        TERMINALS.put("this",           Terminal.THIS                       );
        TERMINALS.put("throw",          Terminal.THROW                      );
        TERMINALS.put("throws",         Terminal.THROWS                     );
        TERMINALS.put("transient",      Terminal.TRANSIENT                  );
        TERMINALS.put("try",            Terminal.TRY                        );
        TERMINALS.put("void",           Terminal.VOID                       );
        TERMINALS.put("volatile",       Terminal.VOLATILE                   );
        TERMINALS.put("while",          Terminal.WHILE                      );


        // ARITHMETIC
        TERMINALS.put("+",              Terminal.ADD                        );
        TERMINALS.put("-",              Terminal.SUBTRACT                   );
        TERMINALS.put("*",              Terminal.MULTIPLY                   );
        TERMINALS.put("/",              Terminal.DIVIDE                     );
        TERMINALS.put("%",              Terminal.MODULO                     );
        TERMINALS.put("++",             Terminal.INCREMENT                  );
        TERMINALS.put("--",             Terminal.DECREMENT                  );

        // BITWISE
        TERMINALS.put("&",              Terminal.AND                        );
        TERMINALS.put("|",              Terminal.OR                         );
        TERMINALS.put("^",              Terminal.XOR                        );
        TERMINALS.put("~",              Terminal.BITWISE_COMPLEMENT         );

        // LOGIC
        TERMINALS.put("&&",             Terminal.AND_GATE                   );
        TERMINALS.put("||",             Terminal.OR_GATE                    );
        TERMINALS.put("!",              Terminal.NOT                        );

        // SHIFT
        TERMINALS.put("<<",             Terminal.LEFT_SHIFT                 );
        TERMINALS.put(">>",             Terminal.RIGHT_SHIFT                );
        TERMINALS.put(">>>",            Terminal.UNSIGNED_RIGHT_SHIFT       );

        // ASSIGNMENT
        TERMINALS.put("=",              Terminal.ASSIGN                     );

        TERMINALS.put("+=",             Terminal.ASSIGN_ADD                 );
        TERMINALS.put("-=",             Terminal.ASSIGN_SUBTRACT);
        TERMINALS.put("*=",             Terminal.ASSIGN_MULTIPLY);
        TERMINALS.put("/=",             Terminal.ASSIGN_DIVIDE);

        TERMINALS.put("%=",             Terminal.ASSIGN_MOD                 );
        TERMINALS.put("&=",             Terminal.ASSIGN_AND                 );
        TERMINALS.put("|=",             Terminal.ASSIGN_OR                  );
        TERMINALS.put("^=",             Terminal.ASSIGN_XOR                 );

        TERMINALS.put("<<=",            Terminal.ASSIGN_LEFT_SHIFT);
        TERMINALS.put(">>=",            Terminal.ASSIGN_RIGHT_SHIFT);
        TERMINALS.put(">>>=",           Terminal.ASSIGN_UNSIGNED_RIGHT_SHIFT);

        // COMPARISON
        TERMINALS.put("==",             Terminal.EQUAL_TO                   );
        TERMINALS.put("!=",             Terminal.NOT_EQUAL_TO               );
        TERMINALS.put("<",              Terminal.LESS_THAN                  );
        TERMINALS.put("<=",             Terminal.LESS_THAN_OR_EQUAL_TO      );
        TERMINALS.put(">",              Terminal.GREATER_THAN               );
        TERMINALS.put(">=",             Terminal.GREATER_THAN_OR_EQUAL_TO   );

        // OTHER
        TERMINALS.put(";",              Terminal.SEMICOLON                  );

        TERMINALS.put("(",              Terminal.OPEN_PARENTHESIS           );
        TERMINALS.put(")",              Terminal.CLOSE_PARENTHESIS          );
        TERMINALS.put("[",              Terminal.OPEN_BRACKET               );
        TERMINALS.put("]",              Terminal.CLOSE_BRACKET              );
        TERMINALS.put("{",              Terminal.OPEN_BRACE                 );
        TERMINALS.put("}",              Terminal.CLOSE_BRACE                );

        TERMINALS.put(".",              Terminal.DOT                        );
        TERMINALS.put(",",              Terminal.COMMA                      );

        TERMINALS.put(":",              Terminal.COLON                      );
        TERMINALS.put("?",              Terminal.TERNARY                    );

        TERMINALS.put("'",              Terminal.CHARACTER_MARKER           );
        TERMINALS.put("\"",             Terminal.STRING_MARKER              );

        TERMINALS.put("->",             Terminal.LAMBDA                     );
        TERMINALS.put("::",             Terminal.DOUBLE_COLON               );

        TERMINALS.put("//",             Terminal.LINE_COMMENT               );
        TERMINALS.put("/*",             Terminal.BLOCK_COMMENT_START        );
        TERMINALS.put("*/",             Terminal.BLOCK_COMMENT_END          );
    }


    private String path, currentLine;
    private boolean hasNextChar;
    private char currentChar, lookAhead;
    private int currentIndex, line, column;

    private void startLine() {
        currentIndex = -1;
        column = 0;
        readNextChar();
    }
    private boolean nextChar() {
        final boolean hasMore = hasNextChar;
        ++currentIndex;
        if (hasMore) {
            ++column;
            currentChar = lookAhead;
            readNextChar();
        }
        return hasMore;
    }

    private void readNextChar() {
        hasNextChar = currentIndex < currentLine.length() - 1;
        lookAhead = hasNextChar ? currentLine.charAt(currentIndex + 1) : 0;
    }


    private boolean characterIsWhitespace(char character) {
        return character == ' ' || character == '\t' || character == '\n';
    }
    private boolean characterIsWhitespace() {
        return characterIsWhitespace(currentChar);
    }
    private boolean characterIsBracket(char character) {
        return character == '(' || character == ')' || character == '[' || character == ']'
                || character == '{' || character == '}';
    }
    private boolean characterIsBracket() {
        return characterIsBracket(currentChar);
    }
    private boolean characterIsDSU(char character) {
        return character == '$' || character == '_';
    }
    private boolean characterIsDSU() {
        return characterIsDSU(currentChar);
    }
    private boolean characterIsDigit(char character) {
        // character = 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        return 48 <= character && character < 58;
    }
    private boolean characterIsDigit() {
        return characterIsDigit(currentChar);
    }
    private boolean characterIsBinaryDigit(char character) {
        return character == '0' || character == '1';
    }
    private boolean characterIsHexDigit(char character) {
        if (characterIsDigit(character))
            return true;
        final char upperCase = Character.toUpperCase(character);
        return 65 <= upperCase && upperCase < 71;
    }
    private boolean characterIsHexDigit() {
        return characterIsHexDigit(currentChar);
    }
    private boolean characterIsLetter(char character) {
        // character = a, ..., z OR character = A, ..., Z
        return (65 <= character && character < 91) || (97 <= character && character < 123);
    }
    private boolean characterIsLetter() {
        return characterIsLetter(currentChar);
    }
    private boolean characterIsSemicolon(char character) {
        return character == ';';
    }
    private boolean characterIsSemicolon() {
        return characterIsSemicolon(currentChar);
    }
    private boolean characterIsSeparator(char character) {
        return character == ';' || character == '.' || character == ',' || characterIsBracket(character);
    }
    private boolean characterIsSeparator() {
        return characterIsSeparator(currentChar);
    }

    public List<Token> lexJava(String filePath) {
        this.path = filePath;

        final Scanner scanner;
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
            System.exit(1);
            return null;
        }

        ArrayList<Token> tokens = new ArrayList<>();

        boolean inBlockComment = false;
        for (line = 1; scanner.hasNextLine(); ++line) {
            currentLine = scanner.nextLine();

            startLine();
            nextChar();

            if (inBlockComment) {
                char prevChar = currentChar;
                while (nextChar()) {
                    if (prevChar == '*' && currentChar == '/') {
                        inBlockComment = false;
                        nextChar();
                        break;
                    }
                    prevChar = currentChar;
                }

                if (inBlockComment) {
                    // We are still in the block comment. Go to the next line. Else, keep reading this line as normal.
                    continue;
                }
            }

            while (currentIndex < currentLine.length()) {
                if (characterIsWhitespace()) {
                    nextChar();
                    continue;
                }

                column = currentIndex + 1;

                // Check if this is the start of a comment
                boolean inLineComment = false;
                if (currentChar == '/' && hasNextChar) {
                    switch (lookAhead) {
                        case '/':
                            inLineComment  = true;
                            break;
                        case '*':
                            inBlockComment = true;
                            break;
                    }
                }
                // If we are in a line comment, skip to the next line.
                if (inLineComment) {
                    break;
                }

                // If we are in a block comment, see if the block ends on this line.
                if (inBlockComment) {
                    // currentChar points to the / of the /*, so advance twice to see what's next.
                    nextChar();
                    nextChar();
                    char prevChar = currentChar;
                    while (nextChar()) {
                        if (prevChar == '*' && currentChar == '/') {
                            inBlockComment = false;
                            break;
                        }
                        prevChar = currentChar;
                    }
                    break;
                }

                // We are guaranteed to be at the start of a symbol here
                if (currentChar == ';') {
                    tokens.add(new Token(TokenType.SEMICOLON, Terminal.SEMICOLON, ";", line, column));
                    nextChar();
                } else if (characterIsBracket()) {
                    tokens.add(new Token(TokenType.BRACKET, TERMINALS.get(Character.toString(currentChar)), Character.toString(currentChar), line, column));
                    nextChar();
                } else if (currentChar == '.') {
                    if (characterIsDigit(lookAhead)) {
                        //tokens.add(readDecimalLiteral());
                    } else {
                        tokens.add(new Token(TokenType.OPERATOR, Terminal.DOT, ".", line, column));
                    }
                    nextChar();
                } else if (characterIsLetter() || characterIsDSU()) {
                    tokens.add(readIdentifierOrKeyWord());
                } else if (characterIsDigit()) {
                    tokens.add(readNumericLiteral());
                } else if (currentChar == '\'' || currentChar == '"') {
                    tokens.add(readCharacterLiteralOrStringLiteral());
                    nextChar();
                } else {
                    tokens.add(readOther());
                }
            }
        }

        return tokens;
    }

    public Token readNumericLiteral() {
        StringBuilder stringBuilder = new StringBuilder();

        boolean foundDecimal = false;
        do {
            if (currentChar == '.') {
                if (foundDecimal) {
                    // TODO - error
                } else {
                    foundDecimal = true;
                }
            } else if (!Character.isDigit(currentChar)) {
                break;
            }
            stringBuilder.append(currentChar);
        } while (nextChar());

        return new Token(TokenType.NUMERIC_LITERAL, Terminal.FLOATING_POINT_LITERAL, stringBuilder.toString(), line, column);
    }

    private Token readOther() {
        // Identify the symbol
        // O(n) for invalid symbol, O(n*k) for valid symbol
        // where n is terminals.size() and k is the length of the valid symbol
        // note that k is never more than 4 and n is < 100 (generally)

        LinkedList<String> possibleSymbols = new LinkedList<>();
        for (String symbol : TERMINALS.keySet()) {
            if (symbol.charAt(0) == currentChar) {
                possibleSymbols.add(symbol);
            }
        }

        if (possibleSymbols.size() == 0) {
            error("Unexpected symbol.");
        }

        StringBuilder stringBuilder = new StringBuilder().append(currentChar);
        int n = 1;
        while (nextChar()) {

            ListIterator<String> itr = possibleSymbols.listIterator();
            while (itr.hasNext()){
                final String token = itr.next();
                if (token.length() <= n || token.charAt(n) != currentChar) {
                    itr.remove();
                }
            }

            ++n;
            if (possibleSymbols.size() > 0) {
                stringBuilder.append(currentChar);
            } else {
                break;
            }
        }

        Terminal value = TERMINALS.get(stringBuilder.toString());
        if (value == null) {
            error("Unexpected symbol " + currentChar);
        }

        return new Token(TokenType.OPERATOR, value, stringBuilder.toString(), line, column);
    }

    private Token readCharacterLiteralOrStringLiteral() {
        final boolean isString = currentChar == '"';
        final char marker = currentChar;
        boolean canClose = true, reachedLineEnd = true;
        StringBuilder stringBuilder = new StringBuilder();
        while (nextChar()) {
            if (canClose && currentChar == marker) {
                reachedLineEnd = false;
                break;
            }
            stringBuilder.append(currentChar);
            canClose = !canClose || currentChar != '\\';
        }
        if (reachedLineEnd) {
            if (isString) {
                error("Unclosed string literal.");
            } else {
                error("Unclosed character literal.");
            }
        }
        if (isString) {
            return new Token(TokenType.STRING_LITERAL, Terminal.STRING_LITERAL, stringBuilder.toString(), line, column);
        } else {
            return new Token(TokenType.CHARACTER_LITERAL, Terminal.CHARACTER_LITERAL, stringBuilder.toString(), line, column);
        }
    }
    private Token readIdentifierOrKeyWord() {
        /* Identifier or keyword. Starts with letter, $, or _, then continues with letter, digit, $, or _.
         * Accept the current character, then accept all characters until a character that is not a letter,
         * digit, $, or _ is encountered, or until the line ends. (Note that the end of the line is not
         * marked by a linebreak if and only if it is the last line of the file. Also note that a keyword
         * will never contain a digit, $, or _, but this distinction is not implemented.) */

        StringBuilder stringBuilder = new StringBuilder();
        do {
            stringBuilder.append(currentChar);
        } while (nextChar() && (characterIsLetter() || characterIsDigit() || characterIsDSU()));


        Terminal terminalValue = TERMINALS.get(stringBuilder.toString());

        if (terminalValue == null) {
            return new Token(TokenType.IDENTIFIER, Terminal.IDENTIFIER, stringBuilder.toString(), line, column);
        } else {
            return new Token(TokenType.KEYWORD, terminalValue, stringBuilder.toString(), line, column);
        }
    }



    private void error(String message) {
        System.err.println(path + ":" + line + ":" + column + "\n" + message);
        System.exit(1);
    }
}