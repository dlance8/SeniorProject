package demos;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import main.Lexer;
import main.Parser;
import main.Token;
import tree.NonterminalNode;
import java.util.ArrayList;
public class ParserDemo extends Application {
	public void start(Stage primaryStage) {
		ArrayList<Token> tokens = new Lexer().lexFromFile("in/MyClass.java");
		for (Token token : tokens)
			System.out.println(token);
		Parser parser = new Parser();
		NonterminalNode start;
		try {
			parser.parse(tokens);
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

