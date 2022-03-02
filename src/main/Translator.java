package main;
// package main;
import tree.NonterminalNode;
import tree.TerminalNode;
import tree.TreeNode;

import java.io.IOException;

public class Translator {
	public static void main(String[] args) throws IOException {
		new Translator().translate(new ParserV2(new Lexer().lex("in/Example2.java")).parse());
	}


	private final StringBuilder indent = new StringBuilder();
	private final StringBuilder out = new StringBuilder();

	public void translate(NonterminalNode root) {
		compilationUnit(root.getNonTerminalChild(0));

		System.out.println("PYTHON CODE:\n" + out.toString());
	}

	private void compilationUnit(NonterminalNode parent) {
		// CompilationUnit = { TypeDeclaration } ;

		for (int i = 0; i < parent.getChildren().size(); ++i) {
			typeDeclaration(parent.getNonTerminalChild(i));
		}
	}

	private void typeDeclaration(NonterminalNode parent) {
    // TypeDeclaration = ClassDeclaration
    //                | InterfaceDeclaration
    //                | ";" ;

		classDeclaration(parent.getNonTerminalChild(0));
	}
	private void classDeclaration(NonterminalNode parent) {
		// ClassDeclaration = NormalClassDeclaration
		//                  | EnumDeclaration ;
		normalClassDeclaration(parent.getNonTerminalChild(0));
	}
	private void normalClassDeclaration(NonterminalNode parent) {
		// NormalClassDeclaration = { ClassModifier } , "class" , Identifier , [ TypeParameters ] , [ Superclass ] , [ Superinterfaces ] , ClassBody ;


		int index = 0;

		for (; index < parent.getChildren().size(); ++index) {
			if (parent.getChildren().get(index) instanceof TerminalNode) break;
			// else skip. we won't translate class modifiers
		}


		print("class ");
		index++;

		String name = parent.getNonTerminalChild(index).getTerminalChild(0).getToken().getText();
		print(name);

		increaseIndent();

		println(":");

		while (parent.getNonTerminalChild(index).getValue() != Nonterminal.CLASS_BODY)
			++index;

		classBody(parent.getNonTerminalChild(index));
	}
	private void classBody(NonterminalNode parent) {
		// ClassBody = "{" , { ClassBodyDeclaration } , "}" ;

		// Skip the first and last children, as they are braces, which Python does not require
		for (int i = 1; i < parent.getChildren().size() - 1; ++i) {
			classBodyDeclaration(parent.getNonTerminalChild(i));
		}
	}
	private void classBodyDeclaration(NonterminalNode parent) {
		// ClassBodyDeclaration = ClassMemberDeclaration
		//                      | InstanceInitializer
		//                      | StaticInitializer
		//                      | ConstructorDeclaration ;

		NonterminalNode child = parent.getNonTerminalChild(0);

		switch (child.getValue()) {
			case CLASS_MEMBER_DECLARATION:
				classMemberDeclaration(child);
				break;
			case INSTANCE_INITIALIZER:
			case STATIC_INITIALIZER:
				// skip, don't support these
				break;
			case CONSTRUCTOR_DECLARATION:
				constructorDeclaration(child);
				break;
		}
	}
	private void classMemberDeclaration(NonterminalNode parent) {
		// ClassMemberDeclaration = FieldDeclaration
		//                        | MethodDeclaration
		//                        | ClassDeclaration
		//                        | InterfaceDeclaration
		//                        | ";" ;

		if (parent.getChildren().get(0) instanceof TerminalNode)
			return; // SKIP

		NonterminalNode child = parent.getNonTerminalChild(0);
		switch (child.getValue()) {
			case FIELD_DECLARATION:
//				fieldDeclaration(child);
				break;
			case METHOD_DECLARATION:
				methodDeclaration(child);
		}
	}
	private void constructorDeclaration(NonterminalNode parent) {

	}

	private void methodDeclaration(NonterminalNode parent){
		// MethodDeclaration = { MethodModifier } , MethodHeader , MethodBody ;
		NonterminalNode child = parent.getNonTerminalChild(3);
		methodBody(child);
//		print(parent.getChildren().toString());
	}

	private void methodBody(NonterminalNode parent){
		//MethodBody = Block
		//           | ";" ;
		NonterminalNode child = parent.getNonTerminalChild(0);
		block(child);
	}

	private void block(NonterminalNode parent){
		// Block = "{" , [ BlockStatements ] , "}" ;
		NonterminalNode child = parent.getNonTerminalChild(1);
		blockStatements(child);
	}

	private void blockStatements(NonterminalNode parent){
		// BlockStatements = BlockStatement , { BlockStatement } ;
		NonterminalNode child = parent.getNonTerminalChild(0);
		blockStatement(child);
	}

	private void blockStatement(NonterminalNode parent){
		// BlockStatement = LocalVariableDeclarationStatement
		//               | ClassDeclaration
		//               | Statement ;
		NonterminalNode child = parent.getNonTerminalChild(0);

		switch (child.getValue()) {
			case LOCAL_VARIABLE_DECLARATION_STATEMENT:
			case CLASS_DECLARATION:
			case STATEMENT:
				statement(child);
		}
	}

	private void statement(NonterminalNode parent) {
//		Statement = StatementWithoutTrailingSubstatement
//				| LabeledStatement
//				| IfThenStatement
//				| IfThenElseStatement
//				| WhileStatement
//				| ForStatement ;
		NonterminalNode child = parent.getNonTerminalChild(0);
		switch (child.getValue()){
			case STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT:
				statementWithoutTrailingSubstatement(child);
		}
	}

	private void statementWithoutTrailingSubstatement(NonterminalNode parent){
//		StatementWithoutTrailingSubstatement = Block
//				| EmptyStatement
//				| ExpressionStatement
//				| AssertStatement
//				| SwitchStatement
//				| DoStatement
//				| BreakStatement
//				| ContinueStatement
//				| ReturnStatement
//				| SynchronizedStatement
//				| ThrowStatement
//				| TryStatement ;
		NonterminalNode child = parent.getNonTerminalChild(0);
		switch (child.getValue()){
			case EXPRESSION_STATEMENT:
				expressionStatement(child);
		}
	}

	private void expressionStatement(NonterminalNode parent){
//		ExpressionStatement = StatementExpression , ";" ;
		NonterminalNode child = parent.getNonTerminalChild(0);
		statementExpression(child);
		}

	private void statementExpression(NonterminalNode parent){
//		StatementExpression = Assignment
//				| PreIncrementExpression
//				| PreDecrementExpression
//				| PostIncrementExpression
//				| PostDecrementExpression
//				| MethodInvocation
//				| ClassInstanceCreationExpression ;
		NonterminalNode child = parent.getNonTerminalChild(0);
		switch(child.getValue()){
			case METHOD_INVOCATION:
				methodInvocation(child);
		}
	}

	private void methodInvocation(NonterminalNode parent) {
//		MethodInvocation = MethodName , "(" , [ArgumentList ] , ")"
//				| TypeName , "." , [ TypeArguments ] , Identifier , "(" , [ ArgumentList ] , ")"
//				| ExpressionName , "." , [ TypeArguments ] , Identifier , "(" , [ArgumentList ] , ")"
//				| Primary , "." , [ TypeArguments ] , Identifier , "(" , [ArgumentList ] , ")"
//				| "super" , "." , [ TypeArguments ] , Identifier , "(" , [ArgumentList ] , ")"
//				| TypeName , "." , "super" , "." , [ TypeArguments ] , Identifier , "(" , [ArgumentList ] , ")" ;

		NonterminalNode child = (parent.getNonTerminalChild(6));
		expression(child);
	}

	private void expression(NonterminalNode parent){
		NonterminalNode child = (parent.getNonTerminalChild(0));
	}


	private void print(String text) {
		out.append(text);
	}
	private void println(String text) {
		out.append(text).append('\n').append(indent);
	}

	private void increaseIndent() {
		indent.append('\t');
	}
	private void decreaseIndent() {
		if (indent.length() < 1) return;
		indent.setLength(indent.length() - 1);
	}
}