package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class EbnfToJava {
	public static void main(String[] args) {
		NonterminalNode root = new Parser(new Lexer("in/ebnf.txt")).parse();

		new TreeTightener().tighten(root);

		try (PrintWriter x = new PrintWriter("in/out.txt")) {
			x.println(new Translator().translate(root));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static class TreeTightener {
		void tighten(TreeNode node) {
			if (!(node instanceof NonterminalNode)) return;
			NonterminalNode parent = (NonterminalNode) node;

			for (TreeNode child : parent)
				tighten(child);

			for (int i = 0; i < parent.size(); ++i) {
				TreeNode onlyChild = onlyChild(parent.get(i));
				if (onlyChild != null)
					parent.set(i, onlyChild);
			}
		}
		TreeNode onlyChild(TreeNode node) {
			if (!(node instanceof NonterminalNode)) return null;
			NonterminalNode parent = (NonterminalNode) node;
			switch (parent.value) {
				case OPTIONAL:
				case REPEATING:
					return null;
			}
			return parent.size() == 1 ? parent.get(0) : null;
		}
	}

	private static class Translator {
		StringBuilder javaCode;
		String translate(NonterminalNode grammar) {
			if (grammar.value != Nonterminal.GRAMMAR)
				return null;

			LinkedList<String> enumValues = new LinkedList<>();

			javaCode = new StringBuilder();
			for (TreeNode child : grammar) {
				NonterminalNode rule = (NonterminalNode) child; // Instance is guaranteed by the Parser

				String identifier = rule.getTerminal(0).text;
				String lowerCase = toCase1(identifier);
				String allCaps = toCase2(identifier);

				enumValues.add(allCaps);

				//javaCode.append("private boolean ").append(lowerCase).append("() {\n\treturn accept(Nonterminal.").append(allCaps).append(",\n");
				javaCode.append("private boolean ").append(lowerCase).append("() {\n\treturn accept(Nonterminal.").append(allCaps).append(", ");


				a(rule.get(1), new StringBuilder("\t"));

				javaCode.setLength(javaCode.length() - 2);

				//javaCode.append("\n\t);\n}\n");
				javaCode.append(");\n}\n");
			}

			for (String enumValue : enumValues)
				System.out.print(enumValue + ", ");
			System.out.println();

			return javaCode.toString();
		}
		void a(TreeNode node, StringBuilder indent) {
			//javaCode.append(indent.append('\t'));
			if (node instanceof TerminalNode) {
				TerminalNode terminalNode = (TerminalNode) node;
				switch (terminalNode.type) {
					case IDENTIFIER:
						javaCode.append("this::").append(toCase1(terminalNode.text));
						break;
					case STRING_LITERAL:
						boolean found = false;
						for (HashMap<String, main.Terminal> map : TextConstants.ALL_MAPS) {
							main.Terminal value = map.get(terminalNode.text);
							if (value != null) {
								found = true;
								javaCode.append("() -> accept(Terminal.").append(value).append(')');
								break;
							}
						}
						if (!found) {
							javaCode.append("() -> identifier(\"").append(terminalNode.text).append("\")");
						}
						break;
				}
			} else {
				NonterminalNode nonterminalNode = (NonterminalNode) node;
				boolean acceptChildren = false;
				switch (nonterminalNode.value) {
					case OPTIONAL:
						//javaCode.append("() -> acceptOptional(\n");
						javaCode.append("() -> acceptOptional(");
						a(nonterminalNode.get(0), indent);
						break;
					case REPEATING:
						//javaCode.append("() -> acceptRepeating(\n");
						javaCode.append("() -> acceptRepeating(");
						a(nonterminalNode.get(0), indent);
						break;
					case GROUPING:
						// GROUPING IS NEVER ACTUALLY SEEN
						//javaCode.append("() -> accept(\n");
						javaCode.append("() -> accept(");
						a(nonterminalNode.get(0), indent);
						break;
					case EXPRESSION:
						//javaCode.append("acceptAny(\n");
						javaCode.append("() -> acceptAny(");
						acceptChildren = true;
						break;
					case SUBEXPRESSION:
						//javaCode.append("acceptAll(\n");
						javaCode.append("() -> acceptAll(");
						acceptChildren = true;
						break;
				}
				if (acceptChildren) {
					if (nonterminalNode.size() > 0 ) {
						for (TreeNode child : nonterminalNode) {
							a(child, indent);
						}
					}
				}
				//javaCode.deleteCharAt(javaCode.length() - 2);
				javaCode.setLength(javaCode.length() - 2);
				//javaCode.append(indent).append(")");
				javaCode.append(")");
			}
			//indent.setLength(indent.length() - 1);
			//javaCode.append(",\n");
			javaCode.append(", ");
		}
		boolean translateTerminal(TreeNode node) {
			if (!(node instanceof TerminalNode)) return false;
			TerminalNode terminalNode = (TerminalNode) node;
			return true;
		}
		String toCase1(String string) {
			StringBuilder stringBuilder = new StringBuilder(string);
			stringBuilder.setCharAt(0, Character.toLowerCase(stringBuilder.charAt(0)));

			for (HashMap<String, main.Terminal> map : TextConstants.ALL_MAPS) {
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

	private static final class Lexer {
		boolean lexing;
		char currentChar;
		int currentIndex, line, column;
		String in;
		Lexer(String fileName) {
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
				} while (lexing && isLetterOrMore());
				text = token.toString();
				type = Terminal.IDENTIFIER;
			} else {
				error("Unrecognized symbol " + currentChar);
				return null;
			}

			return new Token(type, text, line, 0);
		}
		private void advance() {
			currentChar = (lexing = ++currentIndex < in.length()) ? in.charAt(currentIndex) : 0;
		}
		private boolean isLetter() {
			return (65 <= currentChar && currentChar <= 90) || (97 <= currentChar && currentChar <= 122);
		}
		private boolean isLetterOrMore() {
			return isLetter() || (48 <= currentChar && currentChar <= 57) || currentChar == '_';
		}
		private boolean isWhitespace() {
			return Character.isWhitespace(currentChar);
		}
		private boolean isSymbol() {
			switch (currentChar) {
				case '=': case '|': case ',': case ';': case '[': case ']': case '{': case '}': case '(': case ')':
					return true;
				default: return false;
			}
		}
	}

	private static class Parser {
		final Lexer lexer;
		boolean parsing;
		Token currentToken;
		Parser(Lexer lexer) {
			this.lexer = lexer;
		}
		NonterminalNode parse() {
			parsing = true;
			currentToken = lexer.nextToken();
			NonterminalNode grammar = new NonterminalNode(Nonterminal.GRAMMAR);
			while (true) {
				NonterminalNode rule = new NonterminalNode(Nonterminal.RULE);
				final boolean acceptingRule = accept(rule, Terminal.IDENTIFIER);
				if (!acceptingRule) break;
				expect(rule, "=");
				expression(rule);
				expect(rule, ";");
				grammar.add(rule);
			}
			return grammar;
		}
		boolean expression(NonterminalNode parent) {
			final NonterminalNode child = new NonterminalNode(Nonterminal.EXPRESSION);
			final boolean accepted = subexpression(child);
			if (accepted) {
				while (true) {
					if (accept(child, "|"))
						subexpression(child);
					else break;
				}
				parent.add(child);
			}
			return accepted;
		}
		boolean subexpression(NonterminalNode parent) {
			final NonterminalNode child = new NonterminalNode(Nonterminal.SUBEXPRESSION);
			final boolean accepted = element(child);
			if (accepted) {
				while (true) {
					if (accept(child, ","))
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
			switch (currentToken.text) {
				case "[": return otherElementType(parent, Nonterminal.OPTIONAL, "[", "]");
				case "{": return otherElementType(parent, Nonterminal.REPEATING, "{", "}");
				case "(": return otherElementType(parent, Nonterminal.GROUPING, "(", ")");
			}
			error("Unexpected symbol.");
			return false;
		}
		boolean otherElementType(NonterminalNode parent, Nonterminal value, String open, String close) {
			NonterminalNode child = new NonterminalNode(value);
			accept(child, open);
			final boolean accepted = expression(child);
			if (accepted) {
				enforceInput();
				expect(child, close);
				parent.add(child);
			}
			return accepted;
		}

		void expect(NonterminalNode parent, String text) {
			if (!accept(parent, text)) {
				error("Expected \"" + text + "\"" + ". Got \"" + currentToken.text + "\" at line " + currentToken.line);
			}
		}
		boolean accept(NonterminalNode parent, Terminal type) {
			final boolean accepted = parsing && currentToken.type == type;
			if (accepted) {
				parent.add(new TerminalNode(currentToken));
				parsing = (currentToken = lexer.nextToken()) != null;
			}
			return accepted;
		}
		boolean accept(NonterminalNode parent, String text) {
			final boolean accepted = parsing && currentToken.type == Terminal.SYMBOL && currentToken.text.equals(text);
			if (accepted) {
				//parent.add(new TerminalNode(currentToken));
				parsing = (currentToken = lexer.nextToken()) != null;
			}
			return accepted;

		}
		void enforceInput() {
			if (!parsing) error("Unexpected end of input.");
		}
	}

	private static void error(String message) {
		System.err.println(message);
		System.exit(1);
	}

	private enum Terminal {
		IDENTIFIER, STRING_LITERAL, SYMBOL
	}
	private enum Nonterminal {
		EXPRESSION, GRAMMAR, GROUPING, OPTIONAL, REPEATING, RULE, SUBEXPRESSION,
	}

	private static class Token {
		final int line, column;
		final String text;
		final Terminal type;
		Token(Terminal type, String text, int line, int column) {
			this.text = text;
			this.type = type;
			this.line = line;
			this.column = column;
		}
	}

	interface TreeNode {
		String valueString();
		default void buildString(StringBuilder full, StringBuilder indent, boolean isLastChild) {
			full.append(indent).append(isLastChild ? '\u2514' : '\u251c').append("\u2500\u2500\u2500").append(valueString()).append('\n');
		}
	}
	private static class TerminalNode extends Token implements TreeNode {
		public TerminalNode(Token token) {
			super(token.type, token.text, token.line, token.column);
		}
		@Override
		public String valueString() {
			return type + ": " + text;
		}
		@Override
		public String toString() {
			return valueString();
		}
	}

	public static class NonterminalNode extends ArrayList<TreeNode> implements TreeNode {
		private final Nonterminal value;
		public NonterminalNode(Nonterminal value) {
			this.value = value;
		}
		NonterminalNode getNonterminal(int n) {
			final TreeNode child = get(n);
			return child instanceof NonterminalNode ? (NonterminalNode) child : null;
		}
		TerminalNode getTerminal(int n) {
			final TreeNode child = get(n);
			return child instanceof TerminalNode ? (TerminalNode) child : null;
		}
		@Override
		public String valueString() {
			final String original = value.toString();
			StringBuilder stringBuilder = new StringBuilder();
			boolean capitalize = false;
			for (int i = 0; i < original.length(); ++i) {
				final char c = original.charAt(i);
				if (c == '_') {
					capitalize = true;
				} else {
					stringBuilder.append(capitalize || i == 0 ? c : Character.toLowerCase(c));
					capitalize = false;
				}
			}
			return stringBuilder.toString();
		}
		@Override
		public void buildString(StringBuilder full, StringBuilder indent, boolean isLastChild) {
			TreeNode.super.buildString(full, indent, isLastChild);
			if (size() > 0) {
				indent.append(isLastChild ? ' ' : '\u2502').append("   ");
				final TreeNode lastChild = get(size() - 1);
				for (TreeNode child : this)
					child.buildString(full, indent, child == lastChild);
				indent.setLength(indent.length() - 4);
			}
		}
		@Override
		public String toString() {
			final StringBuilder full = new StringBuilder().append(valueString()).append('\n');
			if (size() > 0) {
				final StringBuilder indent = new StringBuilder();
				final TreeNode lastChild = get(size() - 1);
				for (TreeNode child : this)
					child.buildString(full, indent, child == lastChild);
			}
			return full.toString();
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