package main;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import tree.NonTerminalNode;
import tree.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
public class ParserDemo extends Application {
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

		for (Token token : tokens)
			System.out.println(token);

		ParserV3 parser = new ParserV3(tokens);

		NonTerminalNode start = parser.parse();

		x(new ArrayList<>(), start);



		System.out.println(start);

		primaryStage.setScene(new Scene(new Group(new TreeView<String>(parser.debugRoot){{
			prefWidthProperty().bind(primaryStage.widthProperty());
			prefHeightProperty().bind(primaryStage.heightProperty());
		}}), 900, 900));

		primaryStage.show();
	}

	private void x(ArrayList<NonTerminalNode> treeNodes, TreeNode treeNode) {
		if (!(treeNode instanceof NonTerminalNode)) return;

		final NonTerminalNode nonTerminalNode = (NonTerminalNode) treeNode;

		if (treeNodes.contains(treeNode)) {
			System.err.println("YES\n" + nonTerminalNode.getValue());
			System.exit(1);
		}

		treeNodes.add(nonTerminalNode);

		for (TreeNode child : nonTerminalNode.getChildren()) {
			x(treeNodes, child);
		}
	}
}