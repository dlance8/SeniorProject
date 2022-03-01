package main;//package main;
//import javafx.scene.control.TreeItem;
//import tree.NonterminalNode;
//import tree.TerminalNode;
//import java.util.ArrayList;
//public class Parser {
//	private final ArrayList<Token> tokens;
//	private final NonterminalNode root = new NonterminalNode(Nonterminal.MY_ROOT);
//	private boolean parsing;
//	private int currentIndex;
//	private NonterminalNode currentParent;
//	private Token currentToken;
//
//	public TreeItem<String> debugRoot;
//	private TreeItem<String> debugCurrent;
//	private TreeItem<String> addMessage(String message) {
//		return addMessage(debugCurrent, message);
//	}
//	private TreeItem<String> addMessage(TreeItem<String> parent, String message) {
//		TreeItem<String> newItem = new TreeItem<>(message);
//		parent.getChildren().add(newItem);
//		return newItem;
//	}
//	private TreeItem<String> newCurrent(String message) {
//		return debugCurrent = debugCurrent == null ? debugRoot = new TreeItem<>(message) : addMessage(message);
//	}
//	private void $(TreeItem<String> node, boolean accepted) {
//		$(node, accepted, null);
//	}
//	private void $(TreeItem<String> node, boolean accepted, String text) {
//		node.setValue((accepted ? '\u2713' : '\u2717') + node.getValue() + (text == null ? "" : " | " + text));
//	}
//
//	public Parser(ArrayList<Token> tokens) {
//		this.tokens = tokens;
//	}
//
//	private interface NonterminalAcceptor { boolean accept(); }
//
//	public NonterminalNode parse() {
//		start();
//		accept(this::compilationUnit);
//		return root;
//	}
//	private boolean identifier() {
//		System.out.println("IDENTIFIER: " + currentToken.getType());
//		return accept(Nonterminal.IDENTIFIER,
//			() -> acceptAppendAdvance(parsing && currentToken.getType() == TokenType.IDENTIFIER));
//	}
//	private boolean identifier(String value) {
//		return accept(Nonterminal.IDENTIFIER,
//			() -> {
//			System.out.println("\"" + currentToken.getText() + "\", \"" + value + "\"");
//			return acceptAppendAdvance(parsing && currentToken.getType() == TokenType.IDENTIFIER && currentToken.getText().equals(value));
//
//		});
//	}
//	private boolean assignmentOperator() {
//		System.out.println("ASSIGNMENT OPERATOR");
//		if (!parsing) return false;
//
//		TreeItem<String> x = addMessage("ASSIGNMENT OPERATOR");
//
//		final String text = currentToken.getText();
//		final boolean accepted = acceptAppendAdvance(TextConstants.ASSIGNMENT_OPERATORS.contains(currentToken.getValue()));
//		$(x, accepted, text);
//		return accepted;
//	}
//	private boolean literal() {
//		return accept(Nonterminal.LITERAL,
//			() -> acceptAppendAdvance(TextConstants.LITERAL_TYPES.contains(currentToken.getType()))
//		);
//	}
//	private boolean check(TokenType type) {
//		return parsing && currentToken.getType() == type;
//	}
//	private boolean check(Terminal value) {
//		return parsing && currentToken.getValue() == value;
//	}
//	private boolean accept(Nonterminal value, Terminal... terminals) {
//		final NonterminalNode currentParentAtStart = currentParent;
//		currentParent = new NonterminalNode(value);
//		boolean acceptedAny = false;
//		for (Terminal terminal : terminals) {
//			if (accept(terminal)) {
//				acceptedAny = true;
//				break;
//			}
//		}
//		if (acceptedAny)
//			currentParentAtStart.addChild(currentParent);
//		currentParent = currentParentAtStart;
//		return acceptedAny;
//	}
//	private boolean accept(Nonterminal value, NonterminalAcceptor... nonTerminals) {
//		System.out.println("NONTERMINAL: " + value);
//		final TreeItem<String> oldCurrent = debugCurrent, thisRoot = newCurrent(value.toString());
//
//		final NonterminalNode parentAtStart = currentParent;
//
//		currentParent = new NonterminalNode(value);
//
//		boolean acceptedAny = false;
//		int n = 0, k = nonTerminals.length;
//		for (NonterminalAcceptor acceptor : nonTerminals) {
//			TreeItem<String> x = debugCurrent = addMessage(thisRoot, "option " + ++n + " of " + k + " | currentToken = \"" + currentToken.getText() + "\"");
//
//			final boolean acceptedThis = accept(acceptor);
//
//			$(x, acceptedThis);
//
//			if (acceptedThis) {
//				acceptedAny = true;
//			}
//		}
//
//		final boolean accepted = acceptedAny;
//		if (accepted)
//			parentAtStart.addChild(currentParent);
//		currentParent = parentAtStart;
//
//		$(thisRoot, accepted);
//		debugCurrent = oldCurrent;
//
//		return accepted;
//	}
//	private boolean acceptAny(NonterminalAcceptor... acceptors) {
//		for (NonterminalAcceptor acceptor : acceptors)
//			if (accept(acceptor)) return true;
//		return false;
//	}
//	private boolean acceptAll(NonterminalAcceptor... acceptors) {
//		for (NonterminalAcceptor acceptor : acceptors)
//			if (!accept(acceptor)) return false;
//		return true;
//	}
//	private boolean accept(Terminal terminal) {
//		System.out.println("   TERMINAL: " + terminal);
//		final String text = currentToken.getText();
//		final boolean accepted = acceptAppendAdvance(currentToken.getValue() == terminal);
//		TreeItem<String> newTreeItem = addMessage("TERMINAL: " + terminal.toString());
//		$(newTreeItem, accepted, text);
//		return accepted;
//	}
//	private boolean accept(NonterminalAcceptor nonterminal) {
//		System.out.println("ACCEPT");
//		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT");
//		final boolean parsingAtStart = parsing;
//		final int indexAtStart = currentIndex;
//		final NonterminalNode parentAtStart = currentParent;
//		final Token tokenAtStart = currentToken;
//		final boolean accepted = nonterminal.accept();
//		if (!accepted) {
//			parsing = parsingAtStart;
//			currentIndex = indexAtStart;
//			currentParent = parentAtStart;
//			currentToken = tokenAtStart;
//		}
//		if (newCurrent.getChildren().size() == 1) {
//			if (oldCurrent != null) {
//				oldCurrent.getChildren().remove(oldCurrent.getChildren().size() - 1);
//				oldCurrent.getChildren().add(newCurrent.getChildren().get(0));
//			}
//		} else {
//			$(newCurrent, accepted);
//		}
//		debugCurrent = oldCurrent;
//		return accepted;
//	}
//	private boolean acceptRepeating(NonterminalAcceptor acceptor) {
//		System.out.println("ACCEPT REPEATING");
//		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT REPEATING");
//
//		int n = 0;
//		while (true)
//			if (accept(acceptor)) n++; else break;
//
//		$(newCurrent, n > 0);
//		debugCurrent = oldCurrent;
//
//		return true;
//	}
//	private boolean acceptRepeating(Terminal value) {
//		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT REPEATING");
//
//		int n = 0;
//		while (true)
//			if (accept(value)) n++; else break;
//
//		$(newCurrent, n > 0);
//		debugCurrent = oldCurrent;
//
//		return true;
//	}
//	private boolean acceptOptional(NonterminalAcceptor nonterminal) {
//		System.out.println("ACCEPT OPTIONAL");
//		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT OPTIONAL");
//		final boolean accepted = accept(nonterminal);
//		if (newCurrent.getChildren().size() == 1) {
//			final TreeItem<String> child = newCurrent.getChildren().get(0);
//			child.setValue(new StringBuilder(child.getValue()).insert(1, '[').append(']').toString());
//			oldCurrent.getChildren().set(oldCurrent.getChildren().size() - 1, child);
//		} else $(newCurrent, accepted);
//		debugCurrent = oldCurrent;
//		return true;
//	}
//	private boolean acceptOptional(Terminal value) {
//		final TreeItem<String> oldCurrent = debugCurrent, newCurrent = newCurrent("ACCEPT OPTIONAL");
//		final boolean accepted = accept(value);
//		if (newCurrent.getChildren().size() == 1) {
//			final TreeItem<String> child = newCurrent.getChildren().get(0);
//			child.setValue(new StringBuilder(child.getValue()).insert(2, '[').append(']').toString());
//			oldCurrent.getChildren().set(oldCurrent.getChildren().size() - 1, child);
//		} else $(newCurrent, accepted);
//		debugCurrent = oldCurrent;
//		return true;
//	}
//	private void start() {
//		currentParent = root;
//		currentIndex = -1;
//		advance();
//	}
//	private void advance() {
//		currentToken = (parsing = ++currentIndex < tokens.size()) ? tokens.get(currentIndex) : new Token(null);
//	}
//	private boolean acceptAppendAdvance(boolean accepted) {
//		if (accepted &= parsing) {
//			currentParent.addChild(new TerminalNode(currentToken));
//			advance();
//		}
//		return accepted;
//	}
//
//	private boolean compilationUnit() {
//		return accept(Nonterminal.COMPILATION_UNIT, this::classDeclaration);
//	}
//	private boolean classDeclaration() {
//		return accept(Nonterminal.CLASS_DECLARATION, () -> acceptAll(() -> acceptRepeating(this::classModifier), () -> accept(Terminal.CLASS), this::identifier, this::classBody));
//	}
//	private boolean classModifier() {
//		return accept(Nonterminal.CLASS_MODIFIER, () -> acceptAny(() -> accept(Terminal.PUBLIC), () -> accept(Terminal.PRIVATE), () -> accept(Terminal.PROTECTED), () -> accept(Terminal.ABSTRACT), () -> accept(Terminal.STATIC)));
//	}
//	private boolean classBody() {
//		return accept(Nonterminal.CLASS_BODY, () -> acceptAll(() -> accept(Terminal.OPEN_BRACE), () -> acceptRepeating(this::classBodyDeclaration), () -> accept(Terminal.CLOSE_BRACE)));
//	}
//	private boolean classBodyDeclaration() {
//		return accept(Nonterminal.CLASS_BODY_DECLARATION, () -> acceptAny(this::mainMethodDeclaration, this::methodDeclaration));
//	}
//	private boolean mainMethodDeclaration() {
//		return accept(Nonterminal.MAIN_METHOD_DECLARATION, () -> acceptAll(() -> accept(Terminal.PUBLIC), () -> accept(Terminal.STATIC), () -> accept(Terminal.VOID), () -> identifier("main"), () -> accept(Terminal.OPEN_PARENTHESIS), () -> identifier("String"), () -> accept(Terminal.OPEN_BRACKET), () -> accept(Terminal.CLOSE_BRACKET), this::identifier, () -> accept(Terminal.CLOSE_PARENTHESIS), this::methodBody));
//	}
//	private boolean methodDeclaration() {
//		return accept(Nonterminal.METHOD_DECLARATION, () -> acceptAll(() -> acceptRepeating(this::methodModifier), this::methodHeader, this::methodBody));
//	}
//	private boolean methodHeader() {
//		return accept(Nonterminal.METHOD_HEADER, () -> acceptAll(this::result, this::methodDeclarator));
//	}
//	private boolean methodDeclarator() {
//		return accept(Nonterminal.METHOD_DECLARATOR, () -> acceptAll(this::identifier, () -> accept(Terminal.OPEN_PARENTHESIS), () -> acceptOptional(this::formalParameterList), () -> accept(Terminal.CLOSE_PARENTHESIS)));
//	}
//	private boolean formalParameterList() {
//		return accept(Nonterminal.FORMAL_PARAMETER_LIST, () -> acceptAll(this::formalParameter, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.COMMA), this::formalParameter))));
//	}
//	private boolean formalParameter() {
//		return accept(Nonterminal.FORMAL_PARAMETER, () -> acceptAll(() -> acceptOptional(() -> accept(Terminal.FINAL)), this::variableType, this::identifier));
//	}
//	private boolean classLiteral() {
//		return accept(Nonterminal.CLASS_LITERAL, () -> acceptAny(() -> acceptAll(() -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.OPEN_BRACKET), () -> accept(Terminal.CLOSE_BRACKET))), () -> accept(Terminal.DOT), () -> accept(Terminal.CLASS)), () -> acceptAll(this::numericType, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.OPEN_BRACKET), () -> accept(Terminal.CLOSE_BRACKET))), () -> accept(Terminal.DOT), () -> accept(Terminal.CLASS)), () -> acceptAll(() -> accept(Terminal.BOOLEAN), () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.OPEN_BRACKET), () -> accept(Terminal.CLOSE_BRACKET))), () -> accept(Terminal.DOT), () -> accept(Terminal.CLASS)), () -> acceptAll(() -> accept(Terminal.VOID), () -> accept(Terminal.DOT), () -> accept(Terminal.CLASS))));
//	}
//	private boolean numericType() {
//		return accept(Nonterminal.NUMERIC_TYPE, () -> acceptAny(() -> accept(Terminal.INT), () -> accept(Terminal.SHORT), () -> accept(Terminal.CHAR), () -> accept(Terminal.LONG), () -> accept(Terminal.FLOAT), () -> accept(Terminal.DOUBLE), () -> accept(Terminal.BYTE)));
//	}
//	private boolean argumentList() {
//		return accept(Nonterminal.ARGUMENT_LIST, () -> acceptAll(this::expression, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.COMMA), this::expression))));
//	}
//	private boolean methodModifier() {
//		return accept(Nonterminal.METHOD_MODIFIER, () -> acceptAny(() -> accept(Terminal.PUBLIC), () -> accept(Terminal.PRIVATE), () -> accept(Terminal.PROTECTED), () -> accept(Terminal.STATIC)));
//	}
//	private boolean result() {
//		return accept(Nonterminal.RESULT, () -> acceptAny(this::variableType, () -> accept(Terminal.VOID)));
//	}
//	private boolean methodBody() {
//		return accept(Nonterminal.METHOD_BODY, () -> acceptAny(this::block, () -> accept(Terminal.SEMICOLON)));
//	}
//	private boolean block() {
//		return accept(Nonterminal.BLOCK, () -> acceptAll(() -> accept(Terminal.OPEN_BRACE), () -> acceptRepeating(this::blockStatement), () -> accept(Terminal.CLOSE_BRACE)));
//	}
//	private boolean blockStatement() {
//		return accept(Nonterminal.BLOCK_STATEMENT, () -> acceptAny(this::localVariableDeclarationStatement, this::classDeclaration, this::statement));
//	}
//	private boolean localVariableDeclarationStatement() {
//		return accept(Nonterminal.LOCAL_VARIABLE_DECLARATION_STATEMENT, () -> acceptAll(this::localVariableDeclaration, () -> accept(Terminal.SEMICOLON)));
//	}
//	private boolean localVariableDeclaration() {
//		return accept(Nonterminal.LOCAL_VARIABLE_DECLARATION, () -> acceptAll(() -> acceptOptional(() -> accept(Terminal.FINAL)), this::variableType, this::variableDeclaratorList));
//	}
//	private boolean variableDeclaratorList() {
//		return accept(Nonterminal.VARIABLE_DECLARATOR_LIST, () -> acceptAll(this::variableDeclarator, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.COMMA), this::variableDeclarator))));
//	}
//	private boolean variableDeclarator() {
//		return accept(Nonterminal.VARIABLE_DECLARATOR, () -> acceptAll(this::variableDeclaratorId, () -> acceptOptional(() -> acceptAll(() -> accept(Terminal.ASSIGN), this::variableInitializer))));
//	}
//	private boolean variableDeclaratorId() {
//		return accept(Nonterminal.VARIABLE_DECLARATOR_ID, () -> acceptAll(this::identifier, () -> acceptOptional(this::dims)));
//	}
//	private boolean variableInitializer() {
//		return accept(Nonterminal.VARIABLE_INITIALIZER, () -> acceptAny(this::expression, this::arrayInitializer));
//	}
//	private boolean arrayInitializer() {
//		return accept(Nonterminal.ARRAY_INITIALIZER, () -> acceptAll(() -> accept(Terminal.OPEN_BRACE), () -> acceptOptional(this::variableInitializerList), () -> acceptOptional(() -> accept(Terminal.COMMA)), () -> accept(Terminal.CLOSE_BRACE)));
//	}
//	private boolean variableInitializerList() {
//		return accept(Nonterminal.VARIABLE_INITIALIZER_LIST, () -> acceptAll(this::variableInitializer, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.COMMA), this::variableInitializer))));
//	}
//	private boolean expression() {
//		return accept(Nonterminal.EXPRESSION, this::assignmentExpression);
//	}
//	private boolean assignmentExpression() {
//		return accept(Nonterminal.ASSIGNMENT_EXPRESSION, this::conditionalOrExpression);
//	}
//	private boolean conditionalOrExpression() {
//		return accept(Nonterminal.CONDITIONAL_OR_EXPRESSION, () -> acceptAll(this::conditionalAndExpression, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.OR_GATE), this::conditionalAndExpression))));
//	}
//	private boolean conditionalAndExpression() {
//		return accept(Nonterminal.CONDITIONAL_AND_EXPRESSION, () -> acceptAll(this::inclusiveOrExpression, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.AND_GATE), this::inclusiveOrExpression))));
//	}
//	private boolean inclusiveOrExpression() {
//		return accept(Nonterminal.INCLUSIVE_OR_EXPRESSION, () -> acceptAll(this::exclusiveOrExpression, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.OR), this::exclusiveOrExpression))));
//	}
//	private boolean exclusiveOrExpression() {
//		return accept(Nonterminal.EXCLUSIVE_OR_EXPRESSION, () -> acceptAll(this::andExpression, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.XOR), this::andExpression))));
//	}
//	private boolean andExpression() {
//		return accept(Nonterminal.AND_EXPRESSION, () -> acceptAll(this::equalityExpression, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.AND), this::equalityExpression))));
//	}
//	private boolean equalityExpression() {
//		return accept(Nonterminal.EQUALITY_EXPRESSION, () -> acceptAll(this::relationalExpression, () -> acceptRepeating(() -> acceptAll(() -> acceptAny(() -> accept(Terminal.EQUAL_TO), () -> accept(Terminal.NOT_EQUAL_TO)), this::relationalExpression))));
//	}
//	private boolean relationalExpression() {
//		return accept(Nonterminal.RELATIONAL_EXPRESSION, () -> acceptAny(() -> acceptAll(this::shiftExpression, () -> accept(Terminal.INSTANCEOF), this::referenceType), () -> acceptAll(this::shiftExpression, () -> acceptRepeating(() -> acceptAll(() -> acceptAny(() -> accept(Terminal.LESS_THAN), () -> accept(Terminal.GREATER_THAN), () -> accept(Terminal.LESS_THAN_OR_EQUAL_TO), () -> accept(Terminal.GREATER_THAN_OR_EQUAL_TO)), this::shiftExpression)))));
//	}
//	private boolean shiftExpression() {
//		return accept(Nonterminal.SHIFT_EXPRESSION, () -> acceptAll(this::additiveExpression, () -> acceptRepeating(() -> acceptAll(() -> acceptAny(() -> accept(Terminal.LEFT_SHIFT), () -> accept(Terminal.RIGHT_SHIFT), () -> accept(Terminal.UNSIGNED_RIGHT_SHIFT)), this::additiveExpression))));
//	}
//	private boolean additiveExpression() {
//		return accept(Nonterminal.ADDITIVE_EXPRESSION, () -> acceptAll(this::multiplicativeExpression, () -> acceptRepeating(() -> acceptAll(() -> acceptAny(() -> accept(Terminal.ADD), () -> accept(Terminal.SUBTRACT)), this::multiplicativeExpression))));
//	}
//	private boolean multiplicativeExpression() {
//		return accept(Nonterminal.MULTIPLICATIVE_EXPRESSION, () -> acceptAll(this::unaryExpression, () -> acceptRepeating(() -> acceptAll(() -> acceptAny(() -> accept(Terminal.MULTIPLY), () -> accept(Terminal.DIVIDE), () -> accept(Terminal.MODULO)), this::unaryExpression))));
//	}
//	private boolean unaryExpression() {
//		return accept(Nonterminal.UNARY_EXPRESSION, () -> acceptAny(this::preIncrementExpression, this::preDecrementExpression, () -> acceptAll(() -> accept(Terminal.ADD), this::unaryExpression), () -> acceptAll(() -> accept(Terminal.SUBTRACT), this::unaryExpression), this::unaryExpressionNotPlusMinus));
//	}
//	private boolean preIncrementExpression() {
//		return accept(Nonterminal.PRE_INCREMENT_EXPRESSION, () -> acceptAll(() -> accept(Terminal.INCREMENT), this::unaryExpression));
//	}
//	private boolean preDecrementExpression() {
//		return accept(Nonterminal.PRE_DECREMENT_EXPRESSION, () -> acceptAll(() -> accept(Terminal.DECREMENT), this::unaryExpression));
//	}
//	private boolean unaryExpressionNotPlusMinus() {
//		return accept(Nonterminal.UNARY_EXPRESSION_NOT_PLUS_MINUS, () -> acceptAny(this::postfixExpression, () -> acceptAll(() -> accept(Terminal.BITWISE_COMPLEMENT), this::unaryExpression), () -> acceptAll(() -> accept(Terminal.NOT), this::unaryExpression)));
//	}
//	private boolean postfixExpression() {
//		return accept(Nonterminal.POSTFIX_EXPRESSION, () -> acceptAny(this::primary, this::expressionName));
//	}
//	private boolean methodName() {
//		return accept(Nonterminal.METHOD_NAME, this::identifier);
//	}
//	private boolean expressionName() {
//		return accept(Nonterminal.EXPRESSION_NAME, this::identifier);
//	}
//	private boolean primary() {
//		return accept(Nonterminal.PRIMARY, () -> acceptAny(this::primaryNoNewArray, this::arrayCreationExpression));
//	}
//	private boolean classOrInterfaceType() {
//		return accept(Nonterminal.CLASS_OR_INTERFACE_TYPE, this::identifier);
//	}
//	private boolean primaryNoNewArray() {
//		if (!(
//			   check(Terminal.INTEGER_LITERAL)
//			|| check(Terminal.FLOATING_POINT_LITERAL)
//			|| check(Terminal.CHARACTER_LITERAL)
//			|| check(Terminal.STRING_LITERAL)
//			|| check(Terminal.TRUE_LITERAL)
//			|| check(Terminal.FALSE_LITERAL)
//			|| check(Terminal.NULL_LITERAL)
//			|| check(Terminal.THIS)
//			|| check(Terminal.OPEN_PARENTHESIS)
//			|| check(Terminal.NEW)
//			|| check(Terminal.SUPER)
//			|| check(Terminal.IDENTIFIER)
//		)) return false;
//
//
//		return accept(Nonterminal.PRIMARY_NO_NEW_ARRAY, () -> acceptAny(this::literal, this::classLiteral, () -> accept(Terminal.THIS), () -> acceptAll(() -> accept(Terminal.OPEN_PARENTHESIS), this::expression, () -> accept(Terminal.CLOSE_PARENTHESIS)), this::classInstanceCreationExpression, this::fieldAccess, this::arrayAccess, this::methodInvocation));
//	}
//	private boolean classInstanceCreationExpression() {
//		return accept(Nonterminal.CLASS_INSTANCE_CREATION_EXPRESSION, () -> acceptAll(() -> accept(Terminal.NEW), this::identifier, () -> accept(Terminal.OPEN_PARENTHESIS), () -> acceptOptional(this::argumentList), () -> accept(Terminal.CLOSE_PARENTHESIS)));
//	}
//	private boolean fieldAccess() {
//		return accept(Nonterminal.FIELD_ACCESS, () -> acceptAny(() -> acceptAll(this::primary, () -> accept(Terminal.DOT), this::identifier), () -> acceptAll(() -> accept(Terminal.SUPER), () -> accept(Terminal.DOT), this::identifier)));
//	}
//	private boolean arrayAccess() {
//		return accept(Nonterminal.ARRAY_ACCESS, () -> acceptAny(() -> acceptAll(this::expressionName, () -> accept(Terminal.OPEN_BRACKET), this::expression, () -> accept(Terminal.CLOSE_BRACKET)), () -> acceptAll(this::primaryNoNewArray, () -> accept(Terminal.OPEN_BRACKET), this::expression, () -> accept(Terminal.CLOSE_BRACKET))));
//	}
//	private boolean methodInvocation() {
//		return accept(Nonterminal.METHOD_INVOCATION, () -> acceptAny(() -> acceptAll(this::methodName, () -> accept(Terminal.OPEN_PARENTHESIS), () -> acceptOptional(this::argumentList), () -> accept(Terminal.CLOSE_PARENTHESIS)), () -> acceptAll(this::expressionName, () -> accept(Terminal.DOT), this::identifier, () -> accept(Terminal.OPEN_PARENTHESIS), () -> acceptOptional(this::argumentList), () -> accept(Terminal.CLOSE_PARENTHESIS)), () -> acceptAll(this::primary, () -> accept(Terminal.DOT), this::identifier, () -> accept(Terminal.OPEN_PARENTHESIS), () -> acceptOptional(this::argumentList), () -> accept(Terminal.CLOSE_PARENTHESIS)), () -> acceptAll(() -> accept(Terminal.SUPER), () -> accept(Terminal.DOT), this::identifier, () -> accept(Terminal.OPEN_PARENTHESIS), () -> acceptOptional(this::argumentList), () -> accept(Terminal.CLOSE_PARENTHESIS))));
//	}
//	private boolean arrayCreationExpression() {
//		return accept(Nonterminal.ARRAY_CREATION_EXPRESSION, () -> acceptAny(() -> acceptAll(() -> accept(Terminal.NEW), this::primitiveType, this::dimExprs, () -> acceptOptional(this::dims)), () -> acceptAll(() -> accept(Terminal.NEW), this::classOrInterfaceType, this::dimExprs, () -> acceptOptional(this::dims)), () -> acceptAll(() -> accept(Terminal.NEW), this::primitiveType, this::dims, this::arrayInitializer), () -> acceptAll(() -> accept(Terminal.NEW), this::classOrInterfaceType, this::dims, this::arrayInitializer)));
//	}
//	private boolean dimExprs() {
//		return accept(Nonterminal.DIM_EXPRS, () -> acceptAll(this::dimExpr, () -> acceptRepeating(this::dimExpr)));
//	}
//	private boolean dimExpr() {
//		return accept(Nonterminal.DIM_EXPR, () -> acceptAll(() -> accept(Terminal.OPEN_BRACKET), this::expression, () -> accept(Terminal.CLOSE_BRACKET)));
//	}
//	private boolean statement() {
//		return accept(Nonterminal.STATEMENT, () -> acceptAny(this::printStatement, this::statementWithoutTrailingSubstatement));
//	}
//	private boolean statementWithoutTrailingSubstatement() {
//		return accept(Nonterminal.STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT, () -> acceptAny(this::block, this::emptyStatement));
//	}
//	private boolean emptyStatement() {
//		return accept(Nonterminal.EMPTY_STATEMENT, () -> accept(Terminal.SEMICOLON));
//	}
//	private boolean printStatement() {
//		return accept(Nonterminal.PRINT_STATEMENT, () -> acceptAll(() -> identifier("System"), () -> accept(Terminal.DOT), () -> identifier("out"), () -> accept(Terminal.DOT), () -> identifier("println"), () -> accept(Terminal.OPEN_PARENTHESIS), this::literal, () -> accept(Terminal.CLOSE_PARENTHESIS), () -> accept(Terminal.SEMICOLON)));
//	}
//	private boolean variableType() {
//		return accept(Nonterminal.VARIABLE_TYPE, () -> acceptAny(this::referenceType, this::primitiveType));
//	}
//	private boolean referenceType() {
//		return accept(Nonterminal.REFERENCE_TYPE, () -> acceptAny(this::arrayType, this::classType));
//	}
//	private boolean primitiveType() {
//		return accept(Nonterminal.PRIMITIVE_TYPE, () -> acceptAny(() -> accept(Terminal.INT), () -> accept(Terminal.DOUBLE), () -> accept(Terminal.FLOAT), () -> accept(Terminal.CHAR), () -> accept(Terminal.BOOLEAN), () -> accept(Terminal.SHORT), () -> accept(Terminal.LONG), () -> accept(Terminal.BYTE)));
//	}
//	private boolean arrayType() {
//		return accept(Nonterminal.ARRAY_TYPE, () -> acceptAny(() -> acceptAll(this::primitiveType, this::dims), () -> acceptAll(this::classType, this::dims)));
//	}
//	private boolean classType() {
//		return accept(Nonterminal.CLASS_TYPE, this::identifier);
//	}
//	private boolean dims() {
//		return accept(Nonterminal.DIMS, () -> acceptAll(() -> accept(Terminal.OPEN_BRACKET), () -> accept(Terminal.CLOSE_BRACKET), () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.OPEN_BRACKET), () -> accept(Terminal.CLOSE_BRACKET)))));
//	}
//}