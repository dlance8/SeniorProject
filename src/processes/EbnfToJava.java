package processes;
import constants.Nonterminal;
import constants.Terminal;
import constants.TextConstants;
import datastructures.Token;
import datastructures.tree.NonterminalNode;
import datastructures.tree.TerminalNode;
import datastructures.tree.TreeNode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
public class EbnfToJava {
	public static void main(String[] args) {
		EbnfParser parser = new EbnfParser();
		NonterminalNode root = parser.parse(new EbnfLexer("in/ebnf.txt"));

		root.tighten();

		String result = new EbnfTranslator().translate(root, parser.ruleComments);

		System.out.println(result);
		int x = 0;
		(x)++;
	}

	private static void error(String message) {
		System.err.println(message);
		System.exit(1);
	}

	private static class EbnfTranslator {
		StringBuilder enumList, javaCode;
		String translate(NonterminalNode grammar, LinkedList<String> ruleComments) {
			if (grammar.getValue() != Nonterminal.GRAMMAR)
				return null;

			LinkedList<String> enumValues = new LinkedList<>();

			javaCode = new StringBuilder();

			int i = 0;
			for (TreeNode child : grammar) {
				NonterminalNode rule = (NonterminalNode) child; // Instance is guaranteed by the Parser

				String identifier = rule.getTerminalChild(0).getText();
				String allCaps = toCase2(identifier);

				enumValues.add(allCaps);

				//javaCode.append("private void ").append(toCase1(identifier)).append("(NonterminalNode parent) {\n").append(ruleComments.get(i++)).append("\n\terror(\"Nonterminal \" + parent.getValue() + \" is not supported.\");\n}\n");

				javaCode.append("private boolean ").append(toCase1(identifier)).append("() {\n\treturn accept(Nonterminal.").append(allCaps).append(", ");

				a(rule.get(1));

				javaCode.setLength(javaCode.length() - 2);

				javaCode.append(");\n}\n");
			}

			enumList = new StringBuilder();
			for (String enumValue : enumValues)
				enumList.append(enumValue).append(", ");

			return enumList + "\n\n" + javaCode;
		}
		void a(TreeNode node) {
			if (node instanceof TerminalNode) {
				TerminalNode terminalNode = (TerminalNode) node;
				switch (terminalNode.getValue()) {
					case IDENTIFIER:
						javaCode.append("this::").append(toCase1(terminalNode.getText()));
						break;
					case STRING_LITERAL:
						boolean found = false;
						for (HashMap<String, Terminal> map : TextConstants.ALL_MAPS) {
							Terminal value = map.get(terminalNode.getText());
							if (value != null) {
								found = true;
								javaCode.append("() -> accept(Terminal.").append(value).append(')');
								break;
							}
						}
						if (!found) {
							javaCode.append("() -> identifier(\"").append(terminalNode.getText()).append("\")");
						}
						break;
				}
			} else {
				NonterminalNode nonterminalNode = (NonterminalNode) node;
				boolean acceptChildren = false;
				switch (nonterminalNode.getValue()) {
					case OPTIONAL:
						javaCode.append("() -> acceptOptional(");
						a(nonterminalNode.get(0));
						break;
					case REPEATING:
						javaCode.append("() -> acceptRepeating(");
						a(nonterminalNode.get(0));
						break;
					case GROUPING:
						javaCode.append("() -> accept(");
						a(nonterminalNode.get(0));
						break;
					case EXPRESSION:
						javaCode.append("() -> acceptAny(");
						acceptChildren = true;
						break;
					case SUBEXPRESSION:
						javaCode.append("() -> acceptAll(");
						acceptChildren = true;
						break;
				}
				if (acceptChildren) {
					if (nonterminalNode.size() > 0 ) {
						for (TreeNode child : nonterminalNode) {
							a(child);
						}
					}
				}
				javaCode.setLength(javaCode.length() - 2);
				javaCode.append(")");
			}
			javaCode.append(", ");
		}
		String toCase1(String string) {
			StringBuilder stringBuilder = new StringBuilder(string);
			stringBuilder.setCharAt(0, Character.toLowerCase(stringBuilder.charAt(0)));

			for (HashMap<String, Terminal> map : TextConstants.ALL_MAPS) {
				if (map.containsKey(stringBuilder.toString())) {
					stringBuilder.append('_');
					break;
				}
			}

			return stringBuilder.toString();
		}
		String toCase2(String string) {
			StringBuilder stringBuilder = new StringBuilder();
			if (string.length() > 0)
				stringBuilder.append(Character.toUpperCase(string.charAt(0)));
			for (int i = 1; i < string.length(); ++i) {
				final char character = string.charAt(i);
				if (Character.isUpperCase(character))
					stringBuilder.append('_');
				stringBuilder.append(Character.toUpperCase(character));
			}
			return stringBuilder.toString();
		}
	}
	private static class EbnfParser {
		boolean parsing;
		EbnfLexer lexer;
		LinkedList<String> ruleComments;
		Token currentToken;
		NonterminalNode parse(EbnfLexer lexer) {
			this.lexer = lexer;

			parsing = true;
			ruleComments = new LinkedList<>();
			currentToken = lexer.nextToken();
			NonterminalNode grammar = new NonterminalNode(Nonterminal.GRAMMAR);
			while (true)
				if (!rule(grammar)) break;
			return grammar;
		}
		boolean rule(NonterminalNode parent) {
			final NonterminalNode child = new NonterminalNode(Nonterminal.RULE);
			final boolean accepted = accept(child, Terminal.IDENTIFIER) && expect("=") && expression(child) && expect(";");
			if (accepted) {
				parent.add(child);
				ruleComments.add(lexer.copyRuleToComment());
			}
			return accepted;
		}
		boolean expression(NonterminalNode parent) {
			final NonterminalNode child = new NonterminalNode(Nonterminal.EXPRESSION);
			final boolean accepted = subexpression(child);
			if (accepted) {
				while (true) {
					if (accept("|"))
						subexpression(child);
					else break;
				}
				parent.add(child);
			}
			lexer.setIndexOfNextRule();
			return accepted;
		}
		boolean subexpression(NonterminalNode parent) {
			final NonterminalNode child = new NonterminalNode(Nonterminal.SUBEXPRESSION);
			final boolean accepted = element(child);
			if (accepted) {
				while (true) {
					if (accept(","))
						element(child);
					else break;
				}
				parent.add(child);
			}
			return accepted;
		}
		boolean element(NonterminalNode parent) {
			enforceInput();
			if (accept(parent, Terminal.IDENTIFIER) || accept(parent, Terminal.STRING_LITERAL)) return true;
			switch (currentToken.getText()) {
				case "[": return otherElementType(parent, Nonterminal.OPTIONAL, "[", "]");
				case "{": return otherElementType(parent, Nonterminal.REPEATING, "{", "}");
				case "(": return otherElementType(parent, Nonterminal.GROUPING, "(", ")");
			}
			error("Unexpected symbol.");
			return false;
		}
		boolean otherElementType(NonterminalNode parent, Nonterminal value, String open, String close) {
			NonterminalNode child = new NonterminalNode(value);
			accept(open);
			final boolean accepted = expression(child);
			if (accepted) {
				enforceInput();
				expect(close);
				parent.add(child);
			}
			return accepted;
		}

		boolean expect(String text) {
			final boolean accepted = accept(text);
			if (!accepted)
				error("Expected \"" + text + "\"" + ". Got \"" + currentToken.getText() + "\" at line " + currentToken.getLine());
			return accepted;
		}
		boolean accept(NonterminalNode parent, Terminal value) {
			final boolean accepted = parsing && currentToken.getValue() == value;
			if (accepted) {
				parent.add(new TerminalNode(currentToken));
				parsing = (currentToken = lexer.nextToken()) != null;
			}
			return accepted;
		}
		boolean accept(String text) {
			final boolean accepted = parsing && currentToken.getValue() == Terminal.SYMBOL && currentToken.getText().equals(text);
			if (accepted)
				parsing = (currentToken = lexer.nextToken()) != null;
			return accepted;

		}
		void enforceInput() {
			if (!parsing) error("Unexpected end of input.");
		}
	}
	private static final class EbnfLexer {
		boolean lexing;
		char currentChar;
		int currentIndex, currentIndex2, line, column, indexOfNextRule;
		String in;
		EbnfLexer(String fileName) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(fileName));
				StringBuilder newIn = new StringBuilder();
				for (int num; (num = reader.read()) != -1; newIn.append((char) num));
				in = newIn.toString();
			} catch (IOException e) {
				error("IO Exception.");
			}
			lexing = true;
			currentIndex = -1;
			currentIndex2 = 0;
			advance();
		}
		Token nextToken() {
			if (!lexing) return null;
			while (isWhitespace()) {
				if (currentChar == '\n' || currentChar == 13)
					++line;
				advance();
			}
			while (isWhitespace())
				advance();
			final String text;
			final Terminal type;
			if (isSymbol()) {
				text = Character.toString(currentChar);
				type = Terminal.SYMBOL;
				advance();
			} else if (currentChar == '\'' || currentChar == '"') {
				final char stopChar = currentChar;
				final StringBuilder token = new StringBuilder();
				advance();
				while (true) {
					if (!lexing)
						error("Unclosed string literal.");
					if (currentChar == stopChar) {
						advance();
						break;
					} else {
						token.append(currentChar);
						advance();
					}
				}
				text = token.toString();
				type = Terminal.STRING_LITERAL;
			} else if (isLetter()) {
				final StringBuilder token = new StringBuilder();
				do {
					token.append(currentChar);
					advance();
				} while (lexing && isLetterOrDigitOrUnderscore());
				text = token.toString();
				type = Terminal.IDENTIFIER;
			} else {
				error("Unrecognized symbol " + currentChar);
				return null;
			}

			return new Token(null, type, text, line, 0);
		}
		void advance() {
			currentChar = (lexing = ++currentIndex < in.length()) ? in.charAt(currentIndex) : 0;
		}
		boolean isLetter() {
			return (65 <= currentChar && currentChar <= 90) || (97 <= currentChar && currentChar <= 122);
		}
		boolean isLetterOrDigitOrUnderscore() {
			return isLetter() || (48 <= currentChar && currentChar <= 57) || currentChar == '_';
		}
		boolean isWhitespace() {
			return Character.isWhitespace(currentChar);
		}
		boolean isSymbol() {
			switch (currentChar) {
				case '=':
				case '|':
				case ',':
				case ';':
				case '[':
				case ']':
				case '{':
				case '}':
				case '(':
				case ')':
					return true;
				default:
					return false;
			}
		}
		String copyRuleToComment() {
			while (currentIndex2 < indexOfNextRule && isLineBreak(in.charAt(currentIndex2)))
				currentIndex2++;

			if (currentIndex2 >= indexOfNextRule) return "";

			StringBuilder rule = new StringBuilder(indexOfNextRule - currentIndex2 + 20).append("\t// ");

			char nextChar = in.charAt(currentIndex2);
			while (true) {
				char theChar = nextChar;
				if (currentIndex2 == indexOfNextRule - 1) {
					if (!isLineBreak(theChar)) {
						rule.append(theChar);
					}
					++currentIndex2;
					break;
				} else {
					nextChar = in.charAt(++currentIndex2);
					if (isLineBreak(theChar)) {
						if (!isLineBreak(nextChar)) {
							rule.append("\n\t// ");
						}
					} else {
						rule.append(theChar);
					}
				}
			}
			return rule.toString();
		}
		boolean isLineBreak(char c) {
			return c == '\n' || c == '\r' || c == '\f';
		}
		void setIndexOfNextRule() {
			indexOfNextRule = currentIndex;
		}
	}
}
/* grammar = { rule } ;
 * rule = identifier , "=" , expression , ";" ;
 *
 * expression = subexpression , { "|" , subexpression } ;
 *
 * subexpression = element , { "," , element } ;
 *
 * element = identifier
 *         | string_literal
 *         | optional
 *         | repeating
 *         | grouping ;
 *
 * optional = "[" , expression , "]" ;
 *
 * repeating = "{" , expression , "}" ;
 * grouping = "(" , expression , ")" ;
 *
 * a , b | c
 *
 * expression
 *    subexpression
 *       identifier
 *          "a"
 *       ","
 *       identifier
 *          "b"
 *    "|"
 *    subexpression
 *
 * optional = "[" , expression , "]" ;
 * repeating = "{" , expression , "}" ;
 * grouping = "(" , expression , ")" ;
 *
 * Grammar = { Rule } ;
 * Rule = "Identifier" , "=" , Identifier , ";"
 *      | "Identifier" , "=" , StringLiteral , ";"
 *      | "Identifier" , "=" , Optional , ";"
 *      | "Identifier" , "=" , Repeating , ";"
 *      | "Identifier" , "=" , Grouping , ";" ;
 *      | "Identifier" , "=" , Expression , ";" ;
 * Expression = ( Identifier | StringLiteral | Optional | Repeating | Grouping ) , { "|" , ( Identifier | StringLiteral | Optional | Repeating | Grouping ) } ;
 *
 * */