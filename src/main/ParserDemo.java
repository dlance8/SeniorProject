package main;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import tree.NonterminalNode;
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

		ParserV2 parser = new ParserV2(tokens);

		NonterminalNode start;

		try {
			parser.parse();
		} catch (StackOverflowError e) {
			System.err.println("Caught StackOverflowError. This is almost certainly due to an error in the input.");
		}

		start = parser.root;

		//new TreeTightener().tighten(start);

		System.out.println(start);

		primaryStage.setScene(new Scene(new Group(new TreeView<String>(parser.debugRoot){{
			prefWidthProperty().bind(primaryStage.widthProperty());
			prefHeightProperty().bind(primaryStage.heightProperty());
		}}), 900, 900));

		primaryStage.show();
	}
}