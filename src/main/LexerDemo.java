package main;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import tree.TreeNode;
import java.io.IOException;
import java.util.ArrayList;
public class LexerDemo {
	public static void main(String[] args) throws IOException {
//		final long startTime = System.nanoTime();
//		ArrayList<Token> tokens = new Lexer().lex("in/String.java");
//		final long stopTime = System.nanoTime();
//		demo2(tokens);
//		System.out.println((stopTime - startTime) / 1e6 + "ms");

		ArrayList<Token> tokens ;
	}

	public static class OtherDemo extends Application {
		public static void main(String[] args) {
			launch(args);
		}
		@Override
		public void start(Stage primaryStage) {
			ArrayList<Token> tokens;
			try {
				tokens = new Lexer().lex("in/MyClass.java");
			} catch (IOException e) {
				return;
			}
			ParserV1 parser = new ParserV1(tokens);
			TreeNode tree = parser.parse();
			System.out.println(tree);

			TreeView<String> treeView = new TreeView<>(parser.debugRoot);
			primaryStage.setScene(new Scene(new Group(treeView), 900, 900));


			treeView.prefWidthProperty().bind(primaryStage.widthProperty());
			treeView.prefHeightProperty().bind(primaryStage.heightProperty());

			primaryStage.show();
		}
	}

	private static void demo1(ArrayList<Token> tokens) {
		for (Token token : tokens) {
			System.out.println(token);
		}
	}
	private static void demo2(ArrayList<Token> tokens) {
		if (tokens.size() == 0) return;

		StringBuilder stringBuilder = new StringBuilder(), newLine = new StringBuilder().append('\n');
		boolean inFor = false;
		int parenthesis = 0;

		for (Token token : tokens) {
			final byte color;
			switch (token.getType()) {
				case IDENTIFIER:
				case SEPARATOR:
				case OPERATOR:
					color = 0;
					break;
				case KEYWORD:
				case BOOLEAN_LITERAL:
				case NULL_LITERAL:
					color = 36;
					break;
				case INTEGER_LITERAL:
				case FLOATING_POINT_LITERAL:
					color = 34;
					break;
				case CHARACTER_LITERAL:
				case STRING_LITERAL:
					color = 32;
					break;
				default:
					color = 0;
			}
			final String text = "\u001B[" + color + "m" + token.getText();
			switch (token.getValue()) {
				case SEMICOLON:
					stringBuilder.append(token.getText()).append(inFor ? ' ' : newLine);
					break;
				case FOR:
					inFor = true;
					parenthesis = 0;
					stringBuilder.append(text).append(' ');
					break;
				case OPEN_PARENTHESIS:
					if (inFor) ++parenthesis;
					stringBuilder.append(text).append(' ');
					break;
				case CLOSE_PARENTHESIS:
					inFor &= --parenthesis != 0;
					stringBuilder.append(text).append(' ');
					break;
				case OPEN_BRACE:
					stringBuilder.append(text).append(newLine.append('\t'));
					break;
				case CLOSE_BRACE:
					newLine.setLength(newLine.length() - 1);
					stringBuilder.setLength(stringBuilder.length() - 1);
					stringBuilder.append(text).append(newLine);
					break;
				default:
					stringBuilder.append(text).append(' ');
			}
		}
		System.out.println(stringBuilder);
	}
}