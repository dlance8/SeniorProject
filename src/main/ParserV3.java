package main;

import javafx.scene.control.TreeItem;
import tree.NonTerminalNode;
import tree.TerminalNode;

import java.util.ArrayList;

public class ParserV3 {
	private final ArrayList<Token> tokens;
	private boolean parsing;
	private int currentIndex;
	private final NonTerminalNode root = new NonTerminalNode(NonTerminal.MY_ROOT);
	private NonTerminalNode currentParent;
	private Token currentToken;

	public TreeItem<String> debugRoot;
	private TreeItem<String> debugCurrent;

	private TreeItem<String> addMessage(String message) {
		return addMessage(debugCurrent, message);
	}
	private TreeItem<String> addMessage(TreeItem<String> parent, String message) {
		TreeItem<String> newItem = new TreeItem<>(message);
		parent.getChildren().add(newItem);
		return newItem;
	}
	private TreeItem<String> newCurrent(String message) {
		return debugCurrent = debugCurrent == null ? debugRoot = new TreeItem<>(message) : addMessage(message);
	}

	private boolean stringLiteral() {
		return acceptAppendAdvance(parsing && currentToken.getType() == TokenType.STRING_LITERAL);
	}

	private void $(TreeItem<String> node, boolean accepted) {
		$(node, accepted, null);
	}
	private void $(TreeItem<String> node, boolean accepted, String text) {
		node.setValue((accepted ? '\u2713' : '\u2717') + node.getValue() + (text == null ? "" : " | " + text));
	}

	public ParserV3(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}

	private interface NonTerminalAcceptor { boolean accept(); }

	private boolean lookAhead(int k, NonTerminalAcceptor acceptor) {
		return !accept(() -> !accept(() -> {
			if (!parsing) return false;
			for (int i = 0; i < k; ++i) {
				advance();
				if (!parsing) return false;
			}
			return acceptor.accept();
		}));
	}

	private boolean lookAhead(int k, Terminal value) {
		final int index = currentIndex + k;
		return index < tokens.size() && tokens.get(index).getValue() == value;
	}

	public NonTerminalNode parse() {
		start();
		accept(this::compilationUnit);
		return root;
	}
	private boolean identifier() {
		System.out.println("IDENTIFIER");

		if (!parsing) return false;

		TreeItem<String> x = addMessage("IDENTIFIER");

		final String text = currentToken.getText();
		final boolean accepted = acceptAppendAdvance(currentToken.getType() == TokenType.IDENTIFIER);
		$(x, accepted, text);
		return accepted;
	}
	private boolean assignmentOperator() {
		System.out.println("ASSIGNMENT OPERATOR");
		if (!parsing) return false;

		TreeItem<String> x = addMessage("ASSIGNMENT OPERATOR");

		final String text = currentToken.getText();
		final boolean accepted = acceptAppendAdvance(TextConstants.ASSIGNMENT_OPERATORS.contains(currentToken.getValue()));
		$(x, accepted, text);
		return accepted;
	}
	private boolean literal() {
		System.out.println("LITERAL");
		if (!parsing) return false;

		TreeItem<String> x = addMessage("LITERAL");

		final String text = currentToken.getText();
		final boolean accepted = TextConstants.LITERAL_TYPES.contains(currentToken.getType());
		$(x, accepted, text);
		return accepted;
	}
	private boolean accept(NonTerminal value, NonTerminalAcceptor... acceptors) {
		System.out.println("NONTERMINAL: " + value);
		final TreeItem<String> oldCurrent = debugCurrent, thisRoot = newCurrent(value.toString());


		final NonTerminalNode currentParentAtStart = currentParent;

		final boolean parsingAtStart = parsing;
		final int currentIndexAtStart = currentIndex;
		final Token currentTokenAtStart = currentToken;


		NonTerminalNode currentParentAtAccepted = currentParent;

		boolean parsingAtAccepted = parsing;
		int currentIndexAtAccepted = currentIndex;
		Token currentTokenAtAccepted = currentToken;

		//currentParent = new NonTerminalNode(value);

		boolean acceptedAny = false;
		int n = 0, k = acceptors.length;
		for (NonTerminalAcceptor acceptor : acceptors) {
			currentParent = new NonTerminalNode(value);

			parsing = parsingAtStart;
			currentIndex = currentIndexAtStart;
			currentToken = currentTokenAtStart;

			TreeItem<String> x = debugCurrent = addMessage(thisRoot, "\"" + currentToken.getText() + "\": option " + ++n + " of " + k);

			final boolean acceptedThis = accept(acceptor);

			$(x, acceptedThis);

			if (acceptedThis) {
				acceptedAny = true;


				currentParentAtAccepted = currentParent;
				parsingAtAccepted = parsing;
				currentIndexAtAccepted = currentIndex;
				currentTokenAtAccepted = currentToken;
			}
		}



		currentParent = currentParentAtAccepted;
		parsing = parsingAtAccepted;
		currentIndex = currentIndexAtAccepted;
		currentToken = currentTokenAtAccepted;

		currentParent = currentParentAtStart;

		if (acceptedAny) {
			if (currentParentAtAccepted == currentParentAtStart) {
				System.err.println(1);
				System.exit(1);
			}
			currentParent.addChild(currentParentAtAccepted);
		}

		final boolean accepted = acceptedAny;

		$(thisRoot, accepted);

		debugCurrent = oldCurrent;

		return accepted;
	}
	private boolean accept(NonTerminalAcceptor acceptor) {
		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT");

		final boolean parsingAtStart = parsing;
		final int currentIndexAtStart = currentIndex;
		final NonTerminalNode currentParentAtStart = currentParent;
		final Token currentTokenAtStart = currentToken;

		final boolean accepted = acceptor.accept();
		if (accepted) {
			//System.out.println(currentParentAtStart.getValue() + ", " + currentParent.getValue());
			System.out.println(currentParentAtStart == currentParent);
			//currentParentAtStart.addChild(currentParent);
		} else {
			parsing = parsingAtStart;
			currentIndex = currentIndexAtStart;
			currentParent = currentParentAtStart;
			currentToken = currentTokenAtStart;
		}
		//currentParent = currentParentAtStart;


		if (newCurrent.getChildren().size() == 1) {
			if (oldCurrent != null) {
				oldCurrent.getChildren().remove(oldCurrent.getChildren().size() - 1);
				oldCurrent.getChildren().add(newCurrent.getChildren().get(0));
			}
		} else {
			$(newCurrent, accepted);
		}
		debugCurrent = oldCurrent;


		return accepted;
	}
	private boolean accept(Terminal value) {
		System.out.println("   TERMINAL: " + value);
		final String text = currentToken.getText();

		final boolean accepted = acceptAppendAdvance(currentToken.getValue() == value);

		TreeItem<String> x = addMessage("TERMINAL: " + value.toString());

		$(x, accepted, text);

		return accepted;
	}
	boolean canPrint = true;
	private boolean acceptRepeating(NonTerminalAcceptor acceptor) {
		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT REPEATING");

		int n = 0;
		while (true) {
			canPrint = true;
			if (!accept(acceptor)) {
				break;
			} else {
				n++;
			}
		}

		$(newCurrent, n > 0);
		debugCurrent = oldCurrent;

		return true;
	}
	private boolean acceptRepeating(Terminal value) {
		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT REPEATING");

		int n = 0;
		while (true) {
			if (!accept(value)) {
				break;
			} else {
				n++;
			}
		}

		$(newCurrent, n > 0);
		debugCurrent = oldCurrent;

		return true;
	}
	private boolean acceptOptional(NonTerminalAcceptor acceptor) {
		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT OPTIONAL");
		final boolean accepted = accept(acceptor);
		if (newCurrent.getChildren().size() == 1) {
			final TreeItem<String> child = newCurrent.getChildren().get(0);
			child.setValue(new StringBuilder(child.getValue()).insert(1, '[').append(']').toString());
			oldCurrent.getChildren().set(oldCurrent.getChildren().size() - 1, child);
		} else $(newCurrent, accepted);
		debugCurrent = oldCurrent;
		return true;
	}
	private boolean acceptOptional(Terminal value) {
		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT OPTIONAL");
		final boolean accepted = accept(value);
		if (newCurrent.getChildren().size() == 1) {
			final TreeItem<String> child = newCurrent.getChildren().get(0);
			child.setValue(new StringBuilder(child.getValue()).insert(2, '[').append(']').toString());
			oldCurrent.getChildren().set(oldCurrent.getChildren().size() - 1, child);
		} else $(newCurrent, accepted);
		debugCurrent = oldCurrent;
		return true;
	}
	private void start() {
		currentParent = root;
		currentIndex = -1;
		advance();
	}
	private void advance() {
		currentToken = (parsing = ++currentIndex < tokens.size()) ? tokens.get(currentIndex) : new Token(null);
	}
	private boolean acceptAppendAdvance(boolean accepted) {
		if (accepted &= parsing) {
			currentParent.addChild(new TerminalNode(currentToken));
			advance();
		}
		return accepted;
	}


	private boolean compilationUnit() {
		return accept(NonTerminal.COMPILATION_UNIT,
				() -> accept(this::classDeclaration));
	}
	private boolean classDeclaration() {
		return accept(NonTerminal.CLASS_DECLARATION,
				() -> acceptRepeating(this::classModifier) && accept(Terminal.CLASS) && accept(this::identifier) && accept(this::classBody));
	}
	private boolean classModifier() {
		return accept(NonTerminal.CLASS_MODIFIER,
				() -> accept(Terminal.PUBLIC),
				() -> accept(Terminal.PRIVATE),
				() -> accept(Terminal.PROTECTED),
				() -> accept(Terminal.ABSTRACT),
				() -> accept(Terminal.STATIC));
	}
	private boolean classBody() {
		return accept(NonTerminal.CLASS_BODY,
				() -> accept(Terminal.OPEN_BRACE) && acceptRepeating(this::classBodyDeclaration) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean classBodyDeclaration() {
		return accept(NonTerminal.CLASS_BODY_DECLARATION,
				() -> accept(this::methodDeclaration));
	}
	private boolean methodDeclaration() {
		return accept(NonTerminal.METHOD_DECLARATION,
				() -> acceptRepeating(this::methodModifier) && accept(this::result) && accept(this::identifier) && accept(this::methodArguments) && accept(this::methodBody));
	}
	private boolean methodModifier() {
		return accept(NonTerminal.METHOD_MODIFIER,
				() -> accept(Terminal.PUBLIC),
				() -> accept(Terminal.PRIVATE),
				() -> accept(Terminal.PROTECTED),
				() -> accept(Terminal.STATIC));
	}
	private boolean result() {
		return accept(NonTerminal.RESULT,
				() -> accept(this::variableType),
				() -> accept(Terminal.VOID));
	}
	private boolean methodArguments() {
		return accept(NonTerminal.METHOD_ARGUMENTS,
				() -> accept(Terminal.OPEN_PARENTHESIS) && acceptRepeating(this::methodVariableDeclaration) && accept(Terminal.CLOSE_PARENTHESIS));
	}
	private boolean methodVariableDeclaration() {
		return accept(NonTerminal.METHOD_VARIABLE_DECLARATION,
				() -> accept(this::variableType) && accept(this::identifier));
	}
	private boolean methodBody() {
		return accept(NonTerminal.METHOD_BODY,
				() -> accept(Terminal.OPEN_BRACE) && acceptRepeating(this::statement) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean statement() {
		return accept(NonTerminal.STATEMENT,
				() -> accept(this::identifier) && accept(Terminal.DOT) && accept(this::identifier) && accept(Terminal.DOT) && accept(this::identifier) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::stringLiteral) && accept(Terminal.CLOSE_PARENTHESIS) && accept(Terminal.SEMICOLON));
	}
	private boolean variableType() {
		return accept(NonTerminal.VARIABLE_TYPE,
				() -> accept(this::primitiveType),
				() -> accept(this::referenceType));
	}
	private boolean primitiveType() {
		return accept(NonTerminal.PRIMITIVE_TYPE,
				() -> accept(Terminal.INT),
				() -> accept(Terminal.DOUBLE),
				() -> accept(Terminal.FLOAT),
				() -> accept(Terminal.CHAR),
				() -> accept(Terminal.BOOLEAN),
				() -> accept(Terminal.SHORT),
				() -> accept(Terminal.LONG),
				() -> accept(Terminal.BYTE));
	}
	private boolean referenceType() {
		return accept(NonTerminal.REFERENCE_TYPE,
				() -> accept(this::classType),
				() -> accept(this::arrayType));
	}
	private boolean classType() {
		return accept(NonTerminal.CLASS_TYPE,
				() -> accept(this::identifier));
	}
	private boolean arrayType() {
		return accept(NonTerminal.ARRAY_TYPE,
				() -> accept(this::primitiveType) && accept(this::dims),
				() -> accept(this::classType) && accept(this::dims));
	}
	private boolean dims() {
		return accept(NonTerminal.DIMS,
				() -> accept(Terminal.OPEN_BRACKET) && accept(Terminal.CLOSE_BRACKET) && acceptRepeating(() -> accept(Terminal.OPEN_BRACKET) && accept(Terminal.CLOSE_BRACKET)));
	}


}