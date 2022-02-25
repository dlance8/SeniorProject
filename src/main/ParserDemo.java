//package main;
//import javafx.application.Application;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.control.TreeView;
//import javafx.stage.Stage;
//import tree.NonterminalNode;
//import tree.TreeNode;
//import tree.TreeTightener;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ParserDemo extends Application {
//	public static void main(String[] args) {
//		launch(args);
//	}
//	@Override
//	public void start(Stage primaryStage) {
//		ArrayList<Token> tokens;
//		try {
//			tokens = new Lexer().lex("in/MyClass.java");
//		} catch (IOException e) {
//			return;
//		}
//
//		for (Token token : tokens)
//			System.out.println(token);
//
//		Parser parser = new Parser(tokens);
//
//		NonterminalNode start = parser.parse();
//
//		//new TreeTightener().tighten(start);
//
//		System.out.println(start);
//
//		primaryStage.setScene(new Scene(new Group(new TreeView<String>(parser.debugRoot){{
//			prefWidthProperty().bind(primaryStage.widthProperty());
//			prefHeightProperty().bind(primaryStage.heightProperty());
//		}}), 900, 900));
//
//		primaryStage.show();
//
//
//	}
//}
package main;
import java.io.IOException;
public class ParserDemo {
	public static void main(String[] args) throws IOException {
		System.out.println(new Parser(new Lexer().lex("in/MyClass.java")).parse());
	}
}