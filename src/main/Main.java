package main;
import java.util.List;
public class Main {
	public static void main(String[] args) {
		//List<Token> tokens = new Lexer().lexJava("src/main/Lexer.java");
		List<Token> tokens = new Lexer().lexJava("src/main/Lexer.java");

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
}