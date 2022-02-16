package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
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