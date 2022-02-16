package main;
import javafx.scene.control.TreeItem;
import tree.NonTerminalNode;
import tree.TerminalNode;
import java.util.ArrayList;
public class ParserV1 {
	private final ArrayList<Token> tokens;
	private boolean parsing;
	private int currentIndex;
	private final NonTerminalNode root = new NonTerminalNode(NonTerminal.MY_ROOT);
	private NonTerminalNode currentParent;
	private Token currentToken;

	/* UnannClassType = Identifier , [ TypeArguments ] , { "." , { Annotation } , Identifier , [ TypeArguments ] } ;
	 *
	 *
	 * Identifier , [ TypeArguments]
	 * Identifier , [ TypeArguments] , "." , { Annotation } , Identifier , [ TypeArguments ]
	 * Identifier , [ TypeArguments] , "." , { Annotation } , Identifier , [ TypeArguments ] , "." , { Annotation } , Identifier , [ TypeArguments ]
	 * Identifier , [ TypeArguments] , "." , { Annotation } , Identifier , [ TypeArguments ] , "." , { Annotation } , Identifier , [ TypeArguments ] , "." , { Annotation } , Identifier , [ TypeArguments ]
	 *
	 *
	 * UnannClassOrInterfaceType = UnannClassType | UnannClassType ;
	 * */

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

	public ParserV1(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}
	public NonTerminalNode parse() {
		start();
		accept(this::compilationUnit);
		return root;
	}
	private boolean identifier() {
		TreeItem<String> x = addMessage("IDENTIFIER");
		String text = currentToken.getText();
		final boolean accepted = acceptAppendAdvance(currentToken.getType() == TokenType.IDENTIFIER);
		x.setValue(accepted ? "\u2713 " + x.getValue() + ": " + text : "\u2717 " + x.getValue());
		return accepted;
	}
	private boolean assignmentOperator() {
		TreeItem<String> x = addMessage("ASSIGNMENT OPERATOR");
		String text = currentToken.getText();
		final boolean accepted = acceptAppendAdvance(
			   currentToken.getValue() == Terminal.ASSIGN
			|| currentToken.getValue() == Terminal.ASSIGN_MULTIPLY
			|| currentToken.getValue() == Terminal.ASSIGN_DIVIDE
			|| currentToken.getValue() == Terminal.ASSIGN_MOD
			|| currentToken.getValue() == Terminal.ASSIGN_ADD
			|| currentToken.getValue() == Terminal.ASSIGN_SUBTRACT
			|| currentToken.getValue() == Terminal.ASSIGN_LEFT_SHIFT
			|| currentToken.getValue() == Terminal.ASSIGN_RIGHT_SHIFT
			|| currentToken.getValue() == Terminal.ASSIGN_UNSIGNED_RIGHT_SHIFT
			|| currentToken.getValue() == Terminal.ASSIGN_AND
			|| currentToken.getValue() == Terminal.ASSIGN_XOR
			|| currentToken.getValue() == Terminal.ASSIGN_OR);
		x.setValue(accepted ? "\u2713 " + x.getValue() + ": " + text : "\u2717 " + x.getValue());
		return accepted;
	}
	private boolean literal() {
		TreeItem<String> x = addMessage("LITERAL");
		String text = currentToken.getText();
		final boolean accepted =  acceptAppendAdvance(
			   currentToken.getType() == TokenType.INTEGER_LITERAL
			|| currentToken.getType() == TokenType.FLOATING_POINT_LITERAL
			|| currentToken.getType() == TokenType.BOOLEAN_LITERAL
			|| currentToken.getType() == TokenType.CHARACTER_LITERAL
			|| currentToken.getType() == TokenType.STRING_LITERAL
			|| currentToken.getType() == TokenType.NULL_LITERAL);
		x.setValue(accepted ? "\u2713 " + x.getValue() + ": " + text : "\u2717 " + x.getValue());
		return accepted;
	}

	private interface Acceptor { boolean accept(); }
boolean theBoolean = false;
	private boolean accept(NonTerminal value, Acceptor... acceptors) {
//		final TreeItem<String> oldCurrent = debugCurrent;
//		final TreeItem<String> thisRoot = newCurrent(value.toString());
//
//		currentParent = new NonTerminalNode(value);
//
//
//		boolean accepted = false;
//		int n = 0, k = acceptors.length;
//		for (Acceptor acceptor : acceptors) {
//			TreeItem<String> x = debugCurrent = addMessage(thisRoot, "option " + ++n + " of " + k);
//
//			final boolean acceptedThis = acceptor.accept();
//
//			x.setValue((acceptedThis ? '\u2713' : '\u2717') + " " + x.getValue());
//
//			if (acceptedThis) {
//				accepted = true;
//				break;
//			}
//		}
//
//		thisRoot.setValue((accepted ? '\u2713' : '\u2717') + " " + thisRoot.getValue());
//		debugCurrent = oldCurrent;
//
//		return accepted;

		boolean myBool = false;
		if (theBoolean) {
			System.out.println(acceptors.length);
			theBoolean = false;
			myBool = true;
		}

		final TreeItem<String> oldCurrent = debugCurrent;
		final TreeItem<String> thisRoot = newCurrent(value.toString());



		final boolean parsingAtStart = parsing;
		final int currentIndexAtStart = currentIndex;
		final NonTerminalNode currentParentAtStart = currentParent == null ? root : currentParent;
		final Token currentTokenAtStart = currentToken;

		boolean parsingAtAccepted = parsing;
		int currentIndexAtAccepted = currentIndex;
		NonTerminalNode currentParentAtAccepted = currentParent;
		Token currentTokenAtAccepted = currentToken;

		currentParent = new NonTerminalNode(value);

		boolean acceptedAny = false;
		int n = 0, k = acceptors.length;
		for (Acceptor acceptor : acceptors) {
			TreeItem<String> x = debugCurrent = addMessage(thisRoot, (myBool ? "A" : "") + "option " + ++n + " of " + k);


			if (value.toString().equals("UNANN_ARRAY_TYPE") && n == 2) {
				theBoolean = true;
			}
			final boolean acceptedThis = acceptor.accept();

			x.setValue((acceptedThis ? '\u2713' : '\u2717') + " " + x.getValue());

			if (acceptedThis) {
				acceptedAny = true;

				parsingAtAccepted = parsing;
				currentIndexAtAccepted = currentIndex;
				currentParentAtAccepted = currentParent;
				currentTokenAtAccepted = currentToken;

				parsing = parsingAtStart;
				currentIndex = currentIndexAtStart;
				currentParent = currentParentAtStart;
				currentToken = currentTokenAtStart;
			}
		}

		parsing = parsingAtAccepted;
		currentIndex = currentIndexAtAccepted;
		currentParent = currentParentAtAccepted;
		currentToken = currentTokenAtAccepted;

		final boolean accepted = acceptedAny;

		thisRoot.setValue((accepted ? '\u2713' : '\u2717') + " " + thisRoot.getValue());
		debugCurrent = oldCurrent;

		return accepted;
	}

	private boolean accept(Acceptor acceptor) {
		final boolean wasParsing = parsing;
		final int oldCurrentIndex = currentIndex;
		final NonTerminalNode oldParent = currentParent == null ? root : currentParent;

		final TreeItem<String> oldCurrent = debugCurrent;
		final TreeItem<String> newCurrent = newCurrent("ACCEPT");

		final boolean accepted = acceptor.accept();
		if (accepted) {
			oldParent.addChild(currentParent);
		} else {
			parsing = wasParsing;
			currentIndex = oldCurrentIndex;
			currentToken = tokens.get(currentIndex);
		}
		currentParent = oldParent;

		if (newCurrent.getChildren().size() == 1) {
			if (oldCurrent != null) {
				oldCurrent.getChildren().remove(oldCurrent.getChildren().size() - 1);
				oldCurrent.getChildren().add(newCurrent.getChildren().get(0));
			}
		} else {
			newCurrent.setValue((accepted ? '\u2713' : '\u2717') + " " + newCurrent.getValue());
		}
		debugCurrent = oldCurrent;
		return accepted;
	}
	private boolean accept(Terminal value) {
		final boolean accepted = acceptAppendAdvance(currentToken.getValue() == value);
		addMessage((accepted ? '\u2713' : '\u2717') + " TERMINAL: " + value.toString() + (accepted ? "" : " | " + currentToken.getValue().toString()));
		return accepted;
	}
	private boolean acceptRepeating(Acceptor acceptor) {
		final TreeItem<String> oldCurrent = debugCurrent;
		final TreeItem<String> newCurrent = newCurrent("ACCEPT REPEATING");
		int n = 0;
		while (true)
			if (!acceptor.accept()) break;
			else n++;
		newCurrent.setValue((n > 0 ? '\u2713' : '\u2717') + " " + newCurrent.getValue());
		debugCurrent = oldCurrent;
		return true;
	}
	private boolean acceptRepeating(Terminal value) {
		final TreeItem<String> oldCurrent = debugCurrent;
		final TreeItem<String> newCurrent = newCurrent("ACCEPT REPEATING");
		int n = 0;
		while (true)
			if (!accept(value)) break;
			else n++;
		newCurrent.setValue((n > 0 ? '\u2713' : '\u2717') + " " + newCurrent.getValue());
		debugCurrent = oldCurrent;
		return true;
	}
	private boolean acceptOptional(Acceptor acceptor) {
		final TreeItem<String> oldCurrent = debugCurrent;
		final TreeItem<String> newCurrent = newCurrent("ACCEPT OPTIONAL");
		final boolean accepted = acceptor.accept();
		if (newCurrent.getChildren().size() == 1) {
			oldCurrent.getChildren().remove(oldCurrent.getChildren().size() - 1);

			TreeItem<String> child = newCurrent.getChildren().get(0);
			String value = child.getValue();
			child.setValue(value.substring(0, 2) + "[" + value.substring(2) + "]");
			oldCurrent.getChildren().add(child);
		} else {
			newCurrent.setValue((accepted ? '\u2713' : '\u2717') + " " + newCurrent.getValue());
		}
		debugCurrent = oldCurrent;
		return true;
	}
	private boolean acceptOptional(Terminal value) {
		final TreeItem<String> oldCurrent = debugCurrent;
		final TreeItem<String> newCurrent = newCurrent("ACCEPT OPTIONAL");
		final boolean accepted = accept(value);
		if (newCurrent.getChildren().size() == 1) {
			oldCurrent.getChildren().remove(oldCurrent.getChildren().size() - 1);

			TreeItem<String> child = newCurrent.getChildren().get(0);
			String string = child.getValue();
			child.setValue(string.substring(0, 2) + "[" + string.substring(2) + "]");
			oldCurrent.getChildren().add(child);
		} else {
			newCurrent.setValue((accepted ? '\u2713' : '\u2717') + " " + newCurrent.getValue());
		}
		debugCurrent = oldCurrent;
		return true;
	}
	private void start() {
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









	/*===============================================================================================================*
	 *                                      ABANDON ALL HOPE, YE WHO ENTER HERE                                      *
	 *===============================================================================================================*/

	private boolean type() {
		return accept(NonTerminal.TYPE,
			() -> accept(this::primitiveType),
			() -> accept(this::referenceType));
	}
	private boolean primitiveType() {
		return accept(NonTerminal.PRIMITIVE_TYPE,
			() -> acceptRepeating(this::annotation) && accept(this::numericType),
			() -> acceptRepeating(this::annotation) && accept(Terminal.BOOLEAN));
	}
	private boolean numericType() {
		return accept(NonTerminal.NUMERIC_TYPE,
			() -> accept(this::integralType),
			() -> accept(this::floatingPointType));
	}
	private boolean integralType() {
		return accept(NonTerminal.INTEGRAL_TYPE,
			() -> accept(Terminal.BYTE),
			() -> accept(Terminal.SHORT),
			() -> accept(Terminal.INT),
			() -> accept(Terminal.LONG),
			() -> accept(Terminal.CHAR));
	}
	private boolean floatingPointType() {
		return accept(NonTerminal.FLOATING_POINT_TYPE,
			() -> accept(Terminal.FLOAT),
			() -> accept(Terminal.DOUBLE));
	}
	private boolean referenceType() {
		return accept(NonTerminal.REFERENCE_TYPE,
			() -> accept(this::classOrInterfaceType),
			() -> accept(this::typeVariable),
			() -> accept(this::arrayType));
	}
	private boolean classOrInterfaceType() {
		return accept(NonTerminal.CLASS_OR_INTERFACE_TYPE,
			() -> accept(this::classType),
			() -> accept(this::interfaceType));
	}
	private boolean classType() {
		return accept(NonTerminal.CLASS_TYPE,
			() -> acceptRepeating(this::annotation) && accept(this::identifier) && acceptOptional(this::typeArguments),
			() -> accept(this::classOrInterfaceType) && accept(Terminal.DOT) && acceptRepeating(this::annotation) && accept(this::identifier) && acceptOptional(this::typeArguments));
	}
	private boolean interfaceType() {
		return accept(NonTerminal.INTERFACE_TYPE,
			() -> accept(this::classType));
	}
	private boolean typeVariable() {
		return accept(NonTerminal.TYPE_VARIABLE,
			() -> acceptRepeating(this::annotation) && accept(this::identifier));
	}
	private boolean arrayType() {
		return accept(NonTerminal.ARRAY_TYPE,
			() -> accept(this::primitiveType) && accept(this::dims),
			() -> accept(this::classOrInterfaceType) && accept(this::dims),
			() -> accept(this::typeVariable) && accept(this::dims));
	}
	private boolean dims() {
		return accept(NonTerminal.DIMS,
			() -> acceptRepeating(this::annotation) && accept(Terminal.OPEN_BRACKET) && accept(Terminal.CLOSE_BRACKET) && acceptRepeating(() -> acceptRepeating(this::annotation) && accept(Terminal.OPEN_BRACKET) && accept(Terminal.CLOSE_BRACKET)));
	}
	private boolean typeParameter() {
		return accept(NonTerminal.TYPE_PARAMETER,
			() -> acceptRepeating(this::typeParameterModifier) && accept(this::identifier) && acceptOptional(this::typeBound));
	}
	private boolean typeParameterModifier() {
		return accept(NonTerminal.TYPE_PARAMETER_MODIFIER,
			() -> accept(this::annotation));
	}
	private boolean typeBound() {
		return accept(NonTerminal.TYPE_BOUND,
			() -> accept(Terminal.EXTENDS) && accept(this::typeVariable),
			() -> accept(Terminal.EXTENDS) && accept(this::classOrInterfaceType) && acceptRepeating(this::additionalBound));
	}
	private boolean additionalBound() {
		return accept(NonTerminal.ADDITIONAL_BOUND,
			() -> accept(Terminal.AND) && accept(this::interfaceType));
	}
	private boolean typeArguments() {
		return accept(NonTerminal.TYPE_ARGUMENTS,
			() -> accept(Terminal.LESS_THAN) && accept(this::typeArgumentList) && accept(Terminal.GREATER_THAN));
	}
	private boolean typeArgumentList() {
		return accept(NonTerminal.TYPE_ARGUMENT_LIST,
			() -> accept(this::typeArgument) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::typeArgument)));
	}
	private boolean typeArgument() {
		return accept(NonTerminal.TYPE_ARGUMENT,
			() -> accept(this::referenceType),
			() -> accept(this::wildcard));
	}
	private boolean wildcard() {
		return accept(NonTerminal.WILDCARD,
			() -> acceptRepeating(this::annotation) && accept(Terminal.TERNARY) && acceptOptional(this::wildcardBounds));
	}
	private boolean wildcardBounds() {
		return accept(NonTerminal.WILDCARD_BOUNDS,
			() -> accept(Terminal.EXTENDS) && accept(this::referenceType),
			() -> accept(Terminal.SUPER) && accept(this::referenceType));
	}
	private boolean typeName() {
		return accept(NonTerminal.TYPE_NAME,
			() -> accept(this::identifier),
			() -> accept(this::packageOrTypeName) && accept(Terminal.DOT) && accept(this::identifier));
	}
	private boolean packageOrTypeName() {
		return accept(NonTerminal.PACKAGE_OR_TYPE_NAME,
			() -> accept(this::identifier),
			() -> accept(this::packageOrTypeName) && accept(Terminal.DOT) && accept(this::identifier));
	}
	private boolean expressionName() {
		return accept(NonTerminal.EXPRESSION_NAME,
			() -> accept(this::identifier),
			() -> accept(this::ambiguousName) && accept(Terminal.DOT) && accept(this::identifier));
	}
	private boolean methodName() {
		return accept(NonTerminal.METHOD_NAME,
			() -> accept(this::identifier));
	}
	private boolean packageName() {
		return accept(NonTerminal.PACKAGE_NAME,
			() -> accept(this::identifier),
			() -> accept(this::packageName) && accept(Terminal.DOT) && accept(this::identifier));
	}
	private boolean ambiguousName() {
		return accept(NonTerminal.AMBIGUOUS_NAME,
			() -> accept(this::identifier),
			() -> accept(this::ambiguousName) && accept(Terminal.DOT) && accept(this::identifier));
	}
	private boolean compilationUnit() {
		return accept(NonTerminal.COMPILATION_UNIT,
			() -> acceptOptional(this::packageDeclaration) && acceptRepeating(this::importDeclaration) && acceptRepeating(this::typeDeclaration));
	}
	private boolean packageDeclaration() {
		return accept(NonTerminal.PACKAGE_DECLARATION,
			() -> acceptRepeating(this::packageModifier) && accept(Terminal.PACKAGE) && accept(this::identifier) && acceptRepeating(() -> accept(Terminal.DOT) && accept(this::identifier)) && accept(Terminal.SEMICOLON));
	}
	private boolean packageModifier() {
		return accept(NonTerminal.PACKAGE_MODIFIER,
			() -> accept(this::annotation));
	}
	private boolean importDeclaration() {
		return accept(NonTerminal.IMPORT_DECLARATION,
			() -> accept(this::singleTypeImportDeclaration),
			() -> accept(this::typeImportOnDemandDeclaration),
			() -> accept(this::singleStaticImportDeclaration),
			() -> accept(this::staticImportOnDemandDeclaration));
	}
	private boolean singleTypeImportDeclaration() {
		return accept(NonTerminal.SINGLE_TYPE_IMPORT_DECLARATION,
			() -> accept(Terminal.IMPORT) && accept(this::typeName) && accept(Terminal.SEMICOLON));
	}
	private boolean typeImportOnDemandDeclaration() {
		return accept(NonTerminal.TYPE_IMPORT_ON_DEMAND_DECLARATION,
			() -> accept(Terminal.IMPORT) && accept(this::packageOrTypeName) && accept(Terminal.DOT) && accept(Terminal.MULTIPLY) && accept(Terminal.SEMICOLON));
	}
	private boolean singleStaticImportDeclaration() {
		return accept(NonTerminal.SINGLE_STATIC_IMPORT_DECLARATION,
			() -> accept(Terminal.IMPORT) && accept(Terminal.STATIC) && accept(this::typeName) && accept(Terminal.DOT) && accept(this::identifier) && accept(Terminal.SEMICOLON));
	}
	private boolean staticImportOnDemandDeclaration() {
		return accept(NonTerminal.STATIC_IMPORT_ON_DEMAND_DECLARATION,
			() -> accept(Terminal.IMPORT) && accept(Terminal.STATIC) && accept(this::typeName) && accept(Terminal.DOT) && accept(Terminal.MULTIPLY) && accept(Terminal.SEMICOLON));
	}
	private boolean typeDeclaration() {
		return accept(NonTerminal.TYPE_DECLARATION,
			() -> accept(this::classDeclaration),
			() -> accept(this::interfaceDeclaration),
			() -> accept(Terminal.SEMICOLON));
	}
	private boolean classDeclaration() {
		return accept(NonTerminal.CLASS_DECLARATION,
			() -> accept(this::normalClassDeclaration),
			() -> accept(this::enumDeclaration));
	}
	private boolean normalClassDeclaration() {
		return accept(NonTerminal.NORMAL_CLASS_DECLARATION,
			() -> acceptRepeating(this::classModifier) && accept(Terminal.CLASS) && accept(this::identifier) && acceptOptional(this::typeParameters) && acceptOptional(this::superclass) && acceptOptional(this::superinterfaces) && accept(this::classBody));
	}
	private boolean classModifier() {
		return accept(NonTerminal.CLASS_MODIFIER,
			() -> accept(this::annotation),
			() -> accept(Terminal.PUBLIC),
			() -> accept(Terminal.PROTECTED),
			() -> accept(Terminal.PRIVATE),
			() -> accept(Terminal.ABSTRACT),
			() -> accept(Terminal.STATIC),
			() -> accept(Terminal.FINAL),
			() -> accept(Terminal.STRICTFP));
	}
	private boolean typeParameters() {
		return accept(NonTerminal.TYPE_PARAMETERS,
			() -> accept(Terminal.LESS_THAN) && accept(this::typeParameterList) && accept(Terminal.GREATER_THAN));
	}
	private boolean typeParameterList() {
		return accept(NonTerminal.TYPE_PARAMETER_LIST,
			() -> accept(this::typeParameter) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::typeParameter)));
	}
	private boolean superclass() {
		return accept(NonTerminal.SUPERCLASS,
			() -> accept(Terminal.EXTENDS) && accept(this::classType));
	}
	private boolean superinterfaces() {
		return accept(NonTerminal.SUPERINTERFACES,
			() -> accept(Terminal.IMPLEMENTS) && accept(this::interfaceTypeList));
	}
	private boolean interfaceTypeList() {
		return accept(NonTerminal.INTERFACE_TYPE_LIST,
			() -> accept(this::interfaceType) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::interfaceType)));
	}
	private boolean classBody() {
		return accept(NonTerminal.CLASS_BODY,
			() -> accept(Terminal.OPEN_BRACE) && acceptRepeating(this::classBodyDeclaration) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean classBodyDeclaration() {
		return accept(NonTerminal.CLASS_BODY_DECLARATION,
			() -> accept(this::classMemberDeclaration),
			() -> accept(this::instanceInitializer),
			() -> accept(this::staticInitializer),
			() -> accept(this::constructorDeclaration));
	}
	private boolean classMemberDeclaration() {
		return accept(NonTerminal.CLASS_MEMBER_DECLARATION,
			() -> accept(this::fieldDeclaration),
			() -> accept(this::methodDeclaration),
			() -> accept(this::classDeclaration),
			() -> accept(this::interfaceDeclaration),
			() -> accept(Terminal.SEMICOLON));
	}
	private boolean fieldDeclaration() {
		return accept(NonTerminal.FIELD_DECLARATION,
			() -> acceptRepeating(this::fieldModifier) && accept(this::unannType) && accept(this::variableDeclaratorList) && accept(Terminal.SEMICOLON));
	}
	private boolean fieldModifier() {
		return accept(NonTerminal.FIELD_MODIFIER,
			() -> accept(this::annotation),
			() -> accept(Terminal.PUBLIC),
			() -> accept(Terminal.PROTECTED),
			() -> accept(Terminal.PRIVATE),
			() -> accept(Terminal.STATIC),
			() -> accept(Terminal.FINAL),
			() -> accept(Terminal.TRANSIENT),
			() -> accept(Terminal.VOLATILE));
	}
	private boolean variableDeclaratorList() {
		return accept(NonTerminal.VARIABLE_DECLARATOR_LIST,
			() -> accept(this::variableDeclarator) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::variableDeclarator)));
	}
	private boolean variableDeclarator() {
		return accept(NonTerminal.VARIABLE_DECLARATOR,
			() -> accept(this::variableDeclaratorId) && acceptOptional(() -> accept(Terminal.ASSIGN) && accept(this::variableInitializer)));
	}
	private boolean variableDeclaratorId() {
		return accept(NonTerminal.VARIABLE_DECLARATOR_ID,
			() -> accept(this::identifier) && acceptOptional(this::dims));
	}
	private boolean variableInitializer() {
		return accept(NonTerminal.VARIABLE_INITIALIZER,
			() -> accept(this::expression),
			() -> accept(this::arrayInitializer));
	}
	private boolean unannType() {
		return accept(NonTerminal.UNANN_TYPE,
			() -> accept(this::unannPrimitiveType),
			() -> accept(this::unannReferenceType));
	}
	private boolean unannPrimitiveType() {
		return accept(NonTerminal.UNANN_PRIMITIVE_TYPE,
			() -> accept(this::numericType),
			() -> accept(Terminal.BOOLEAN));
	}
	private boolean unannReferenceType() {
		return accept(NonTerminal.UNANN_REFERENCE_TYPE,
			() -> accept(this::unannClassOrInterfaceType),
			() -> accept(this::unannTypeVariable),
			() -> accept(this::unannArrayType));
	}
	private boolean unannClassOrInterfaceType() {
		return accept(NonTerminal.UNANN_CLASS_OR_INTERFACE_TYPE,
			() -> accept(this::unannClassType),
			() -> accept(this::unannInterfaceType));
	}
	private boolean unannClassType() {
//		return accept(NonTerminal.UNANN_CLASS_TYPE,
//			() -> accept(this::identifier) && acceptOptional(this::typeArguments),
//			() -> accept(this::unannClassOrInterfaceType) && accept(Terminal.DOT) && acceptRepeating(this::annotation) && accept(this::identifier) && acceptOptional(this::typeArguments));
		return accept(NonTerminal.UNANN_CLASS_TYPE,
			() -> accept(this::identifier) && acceptOptional(this::typeArguments) && acceptRepeating(() -> accept(Terminal.DOT) && acceptRepeating(this::annotation) && accept(this::identifier) && acceptOptional(this::typeArguments))
		);
	}
	private boolean unannInterfaceType() {
		return accept(NonTerminal.UNANN_INTERFACE_TYPE,
			() -> accept(this::unannClassType));
	}
	private boolean unannTypeVariable() {
		return accept(NonTerminal.UNANN_TYPE_VARIABLE,
			() -> accept(this::identifier));
	}
	private boolean unannArrayType() {
		return accept(NonTerminal.UNANN_ARRAY_TYPE,
			() -> accept(this::unannPrimitiveType) && accept(this::dims),
			() -> accept(this::unannClassOrInterfaceType) && accept(this::dims),
			() -> accept(this::unannTypeVariable) && accept(this::dims));
	}
	private boolean methodDeclaration() {
		return accept(NonTerminal.METHOD_DECLARATION,
			() -> acceptRepeating(this::methodModifier) && accept(this::methodHeader) && accept(this::methodBody));
	}
	private boolean methodModifier() {
		return accept(NonTerminal.METHOD_MODIFIER,
			() -> accept(this::annotation),
			() -> accept(Terminal.PUBLIC),
			() -> accept(Terminal.PROTECTED),
			() -> accept(Terminal.PRIVATE),
			() -> accept(Terminal.ABSTRACT),
			() -> accept(Terminal.STATIC),
			() -> accept(Terminal.FINAL),
			() -> accept(Terminal.SYNCHRONIZED),
			() -> accept(Terminal.NATIVE),
			() -> accept(Terminal.STRICTFP));
	}
	private boolean methodHeader() {
		return accept(NonTerminal.METHOD_HEADER,
			() -> accept(this::result) && accept(this::methodDeclarator) && acceptOptional(this::throws_),
			() -> accept(this::typeParameters) && acceptRepeating(this::annotation) && accept(this::result) && accept(this::methodDeclarator) && acceptOptional(this::throws_));
	}
	private boolean result() {
		return accept(NonTerminal.RESULT,
			() -> accept(this::unannType),
			() -> accept(Terminal.VOID));
	}
	private boolean methodDeclarator() {
		return accept(NonTerminal.METHOD_DECLARATOR,
			() -> accept(this::identifier) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::formalParameterList) && accept(Terminal.CLOSE_PARENTHESIS) && acceptOptional(this::dims));
	}
	private boolean formalParameterList() {
		return accept(NonTerminal.FORMAL_PARAMETER_LIST,
			() -> accept(this::receiverParameter),
			() -> accept(this::formalParameters) && accept(Terminal.COMMA) && accept(this::lastFormalParameter),
			() -> accept(this::lastFormalParameter));
	}
	private boolean formalParameters() {
		return accept(NonTerminal.FORMAL_PARAMETERS,
			() -> accept(this::formalParameter) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::formalParameter)),
			() -> accept(this::receiverParameter) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::formalParameter)));
	}
	private boolean formalParameter() {
		return accept(NonTerminal.FORMAL_PARAMETER,
			() -> acceptRepeating(this::variableModifier) && accept(this::unannType) && accept(this::variableDeclaratorId));
	}
	private boolean variableModifier() {
		return accept(NonTerminal.VARIABLE_MODIFIER,
			() -> accept(this::annotation),
			() -> accept(Terminal.FINAL));
	}
	private boolean lastFormalParameter() {
		return accept(NonTerminal.LAST_FORMAL_PARAMETER,
			() -> acceptRepeating(this::variableModifier) && accept(this::unannType) && acceptRepeating(this::annotation) && accept(Terminal.ELLIPSES) && accept(this::variableDeclaratorId),
			() -> accept(this::formalParameter));
	}
	private boolean receiverParameter() {
		return accept(NonTerminal.RECEIVER_PARAMETER,
			() -> acceptRepeating(this::annotation) && accept(this::unannType) && acceptOptional(() -> accept(this::identifier) && accept(Terminal.DOT)) && accept(Terminal.THIS));
	}
	private boolean throws_() {
		return accept(NonTerminal.THROWS,
			() -> accept(Terminal.THROWS) && accept(this::exceptionTypeList));
	}
	private boolean exceptionTypeList() {
		return accept(NonTerminal.EXCEPTION_TYPE_LIST,
			() -> accept(this::exceptionType) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::exceptionType)));
	}
	private boolean exceptionType() {
		return accept(NonTerminal.EXCEPTION_TYPE,
			() -> accept(this::classType),
			() -> accept(this::typeVariable));
	}
	private boolean methodBody() {
		return accept(NonTerminal.METHOD_BODY,
			() -> accept(this::block),
			() -> accept(Terminal.SEMICOLON));
	}
	private boolean instanceInitializer() {
		return accept(NonTerminal.INSTANCE_INITIALIZER,
			() -> accept(this::block));
	}
	private boolean staticInitializer() {
		return accept(NonTerminal.STATIC_INITIALIZER,
			() -> accept(Terminal.STATIC) && accept(this::block));
	}
	private boolean constructorDeclaration() {
		return accept(NonTerminal.CONSTRUCTOR_DECLARATION,
			() -> acceptRepeating(this::constructorModifier) && accept(this::constructorDeclarator) && acceptOptional(this::throws_) && accept(this::constructorBody));
	}
	private boolean constructorModifier() {
		return accept(NonTerminal.CONSTRUCTOR_MODIFIER,
			() -> accept(this::annotation),
			() -> accept(Terminal.PUBLIC),
			() -> accept(Terminal.PROTECTED),
			() -> accept(Terminal.PRIVATE));
	}
	private boolean constructorDeclarator() {
		return accept(NonTerminal.CONSTRUCTOR_DECLARATOR,
			() -> acceptOptional(this::typeParameters) && accept(this::simpleTypeName) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::formalParameterList) && accept(Terminal.CLOSE_PARENTHESIS));
	}
	private boolean simpleTypeName() {
		return accept(NonTerminal.SIMPLE_TYPE_NAME,
			() -> accept(this::identifier));
	}
	private boolean constructorBody() {
		return accept(NonTerminal.CONSTRUCTOR_BODY,
			() -> accept(Terminal.OPEN_BRACE) && acceptOptional(this::explicitConstructorInvocation) && acceptOptional(this::blockStatements) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean explicitConstructorInvocation() {
		return accept(NonTerminal.EXPLICIT_CONSTRUCTOR_INVOCATION,
			() -> acceptOptional(this::typeArguments) && accept(Terminal.THIS) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS) && accept(Terminal.SEMICOLON),
			() -> acceptOptional(this::typeArguments) && accept(Terminal.SUPER) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS) && accept(Terminal.SEMICOLON),
			() -> accept(this::expressionName) && accept(Terminal.DOT) && acceptOptional(this::typeArguments) && accept(Terminal.SUPER) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS) && accept(Terminal.SEMICOLON),
			() -> accept(this::primary) && accept(Terminal.DOT) && acceptOptional(this::typeArguments) && accept(Terminal.SUPER) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS) && accept(Terminal.SEMICOLON));
	}
	private boolean enumDeclaration() {
		return accept(NonTerminal.ENUM_DECLARATION,
			() -> acceptRepeating(this::classModifier) && accept(Terminal.ENUM) && accept(this::identifier) && acceptOptional(this::superinterfaces) && accept(this::enumBody));
	}
	private boolean enumBody() {
		return accept(NonTerminal.ENUM_BODY,
			() -> accept(Terminal.OPEN_BRACE) && acceptOptional(this::enumConstantList) && acceptOptional(Terminal.COMMA) && acceptOptional(this::enumBodyDeclarations) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean enumConstantList() {
		return accept(NonTerminal.ENUM_CONSTANT_LIST,
			() -> accept(this::enumConstant) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::enumConstant)));
	}
	private boolean enumConstant() {
		return accept(NonTerminal.ENUM_CONSTANT,
			() -> acceptRepeating(this::enumConstantModifier) && accept(this::identifier) && acceptOptional(() -> accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS)) && acceptOptional(this::classBody));
	}
	private boolean enumConstantModifier() {
		return accept(NonTerminal.ENUM_CONSTANT_MODIFIER,
			() -> accept(this::annotation));
	}
	private boolean enumBodyDeclarations() {
		return accept(NonTerminal.ENUM_BODY_DECLARATIONS,
			() -> accept(Terminal.SEMICOLON) && acceptRepeating(this::classBodyDeclaration));
	}
	private boolean interfaceDeclaration() {
		return accept(NonTerminal.INTERFACE_DECLARATION,
			() -> accept(this::normalInterfaceDeclaration),
			() -> accept(this::annotationTypeDeclaration));
	}
	private boolean normalInterfaceDeclaration() {
		return accept(NonTerminal.NORMAL_INTERFACE_DECLARATION,
			() -> acceptRepeating(this::interfaceModifier) && accept(Terminal.INTERFACE) && accept(this::identifier) && acceptOptional(this::typeParameters) && acceptOptional(this::extendsInterfaces) && accept(this::interfaceBody));
	}
	private boolean interfaceModifier() {
		return accept(NonTerminal.INTERFACE_MODIFIER,
			() -> accept(this::annotation),
			() -> accept(Terminal.PUBLIC),
			() -> accept(Terminal.PROTECTED),
			() -> accept(Terminal.PRIVATE),
			() -> accept(Terminal.ABSTRACT),
			() -> accept(Terminal.STATIC),
			() -> accept(Terminal.STRICTFP));
	}
	private boolean extendsInterfaces() {
		return accept(NonTerminal.EXTENDS_INTERFACES,
			() -> accept(Terminal.EXTENDS) && accept(this::interfaceTypeList));
	}
	private boolean interfaceBody() {
		return accept(NonTerminal.INTERFACE_BODY,
			() -> accept(Terminal.OPEN_BRACE) && acceptRepeating(this::interfaceMemberDeclaration) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean interfaceMemberDeclaration() {
		return accept(NonTerminal.INTERFACE_MEMBER_DECLARATION,
			() -> accept(this::constantDeclaration),
			() -> accept(this::interfaceMethodDeclaration),
			() -> accept(this::classDeclaration),
			() -> accept(this::interfaceDeclaration),
			() -> accept(Terminal.SEMICOLON));
	}
	private boolean constantDeclaration() {
		return accept(NonTerminal.CONSTANT_DECLARATION,
			() -> acceptRepeating(this::constantModifier) && accept(this::unannType) && accept(this::variableDeclaratorList) && accept(Terminal.SEMICOLON));
	}
	private boolean constantModifier() {
		return accept(NonTerminal.CONSTANT_MODIFIER,
			() -> accept(this::annotation),
			() -> accept(Terminal.PUBLIC),
			() -> accept(Terminal.STATIC),
			() -> accept(Terminal.FINAL));
	}
	private boolean interfaceMethodDeclaration() {
		return accept(NonTerminal.INTERFACE_METHOD_DECLARATION,
			() -> acceptRepeating(this::interfaceMethodModifier) && accept(this::methodHeader) && accept(this::methodBody));
	}
	private boolean interfaceMethodModifier() {
		return accept(NonTerminal.INTERFACE_METHOD_MODIFIER,
			() -> accept(this::annotation),
			() -> accept(Terminal.PUBLIC),
			() -> accept(Terminal.ABSTRACT),
			() -> accept(Terminal.DEFAULT),
			() -> accept(Terminal.STATIC),
			() -> accept(Terminal.STRICTFP));
	}
	private boolean annotationTypeDeclaration() {
		return accept(NonTerminal.ANNOTATION_TYPE_DECLARATION,
			() -> acceptRepeating(this::interfaceModifier) && accept(Terminal.AT_SYMBOL) && accept(Terminal.INTERFACE) && accept(this::identifier) && accept(this::annotationTypeBody));
	}
	private boolean annotationTypeBody() {
		return accept(NonTerminal.ANNOTATION_TYPE_BODY,
			() -> accept(Terminal.OPEN_BRACE) && acceptRepeating(this::annotationTypeMemberDeclaration) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean annotationTypeMemberDeclaration() {
		return accept(NonTerminal.ANNOTATION_TYPE_MEMBER_DECLARATION,
			() -> accept(this::annotationTypeElementDeclaration),
			() -> accept(this::constantDeclaration),
			() -> accept(this::classDeclaration),
			() -> accept(this::interfaceDeclaration),
			() -> accept(Terminal.SEMICOLON));
	}
	private boolean annotationTypeElementDeclaration() {
		return accept(NonTerminal.ANNOTATION_TYPE_ELEMENT_DECLARATION,
			() -> acceptRepeating(this::annotationTypeElementModifier) && accept(this::unannType) && accept(this::identifier) && accept(Terminal.OPEN_PARENTHESIS) && accept(Terminal.CLOSE_PARENTHESIS) && acceptOptional(this::dims) && acceptOptional(this::defaultValue) && accept(Terminal.SEMICOLON));
	}
	private boolean annotationTypeElementModifier() {
		return accept(NonTerminal.ANNOTATION_TYPE_ELEMENT_MODIFIER,
			() -> accept(this::annotation),
			() -> accept(Terminal.PUBLIC),
			() -> accept(Terminal.ABSTRACT));
	}
	private boolean defaultValue() {
		return accept(NonTerminal.DEFAULT_VALUE,
			() -> accept(Terminal.DEFAULT) && accept(this::elementValue));
	}
	private boolean annotation() {
		return accept(NonTerminal.ANNOTATION,
			() -> accept(this::normalAnnotation),
			() -> accept(this::markerAnnotation),
			() -> accept(this::singleElementAnnotation));
	}
	private boolean normalAnnotation() {
		return accept(NonTerminal.NORMAL_ANNOTATION,
			() -> accept(Terminal.AT_SYMBOL) && accept(this::typeName) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::elementValuePairList) && accept(Terminal.CLOSE_PARENTHESIS));
	}
	private boolean elementValuePairList() {
		return accept(NonTerminal.ELEMENT_VALUE_PAIR_LIST,
			() -> accept(this::elementValuePair) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::elementValuePair)));
	}
	private boolean elementValuePair() {
		return accept(NonTerminal.ELEMENT_VALUE_PAIR,
			() -> accept(this::identifier) && accept(Terminal.ASSIGN) && accept(this::elementValue));
	}
	private boolean elementValue() {
		return accept(NonTerminal.ELEMENT_VALUE,
			() -> accept(this::conditionalExpression),
			() -> accept(this::elementValueArrayInitializer),
			() -> accept(this::annotation));
	}
	private boolean elementValueArrayInitializer() {
		return accept(NonTerminal.ELEMENT_VALUE_ARRAY_INITIALIZER,
			() -> accept(Terminal.OPEN_BRACE) && acceptOptional(this::elementValueList) && acceptOptional(Terminal.COMMA) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean elementValueList() {
		return accept(NonTerminal.ELEMENT_VALUE_LIST,
			() -> accept(this::elementValue) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::elementValue)));
	}
	private boolean markerAnnotation() {
		return accept(NonTerminal.MARKER_ANNOTATION,
			() -> accept(Terminal.AT_SYMBOL) && accept(this::typeName));
	}
	private boolean singleElementAnnotation() {
		return accept(NonTerminal.SINGLE_ELEMENT_ANNOTATION,
			() -> accept(Terminal.AT_SYMBOL) && accept(this::typeName) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::elementValue) && accept(Terminal.CLOSE_PARENTHESIS));
	}
	private boolean arrayInitializer() {
		return accept(NonTerminal.ARRAY_INITIALIZER,
			() -> accept(Terminal.OPEN_BRACE) && acceptOptional(this::variableInitializerList) && acceptOptional(Terminal.COMMA) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean variableInitializerList() {
		return accept(NonTerminal.VARIABLE_INITIALIZER_LIST,
			() -> accept(this::variableInitializer) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::variableInitializer)));
	}
	private boolean block() {
		return accept(NonTerminal.BLOCK,
			() -> accept(Terminal.OPEN_BRACE) && acceptOptional(this::blockStatements) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean blockStatements() {
		return accept(NonTerminal.BLOCK_STATEMENTS,
			() -> accept(this::blockStatement) && acceptRepeating(this::blockStatement));
	}
	private boolean blockStatement() {
		return accept(NonTerminal.BLOCK_STATEMENT,
			() -> accept(this::localVariableDeclarationStatement),
			() -> accept(this::classDeclaration),
			() -> accept(this::statement));
	}
	private boolean localVariableDeclarationStatement() {
		return accept(NonTerminal.LOCAL_VARIABLE_DECLARATION_STATEMENT,
			() -> accept(this::localVariableDeclaration) && accept(Terminal.SEMICOLON));
	}
	private boolean localVariableDeclaration() {
		return accept(NonTerminal.LOCAL_VARIABLE_DECLARATION,
			() -> acceptRepeating(this::variableModifier) && accept(this::unannType) && accept(this::variableDeclaratorList));
	}
	private boolean statement() {
		return accept(NonTerminal.STATEMENT,
			() -> accept(this::statementWithoutTrailingSubstatement),
			() -> accept(this::labeledStatement),
			() -> accept(this::ifThenStatement),
			() -> accept(this::ifThenElseStatement),
			() -> accept(this::whileStatement),
			() -> accept(this::forStatement));
	}
	private boolean statementNoShortIf() {
		return accept(NonTerminal.STATEMENT_NO_SHORT_IF,
			() -> accept(this::statementWithoutTrailingSubstatement),
			() -> accept(this::labeledStatementNoShortIf),
			() -> accept(this::ifThenElseStatementNoShortIf),
			() -> accept(this::whileStatementNoShortIf),
			() -> accept(this::forStatementNoShortIf));
	}
	private boolean statementWithoutTrailingSubstatement() {
		return accept(NonTerminal.STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT,
			() -> accept(this::block),
			() -> accept(this::emptyStatement),
			() -> accept(this::expressionStatement),
			() -> accept(this::assertStatement),
			() -> accept(this::switchStatement),
			() -> accept(this::doStatement),
			() -> accept(this::breakStatement),
			() -> accept(this::continueStatement),
			() -> accept(this::returnStatement),
			() -> accept(this::synchronizedStatement),
			() -> accept(this::throwStatement),
			() -> accept(this::tryStatement));
	}
	private boolean emptyStatement() {
		return accept(NonTerminal.EMPTY_STATEMENT,
			() -> accept(Terminal.SEMICOLON));
	}
	private boolean labeledStatement() {
		return accept(NonTerminal.LABELED_STATEMENT,
			() -> accept(this::identifier) && accept(Terminal.COLON) && accept(this::statement));
	}
	private boolean labeledStatementNoShortIf() {
		return accept(NonTerminal.LABELED_STATEMENT_NO_SHORT_IF,
			() -> accept(this::identifier) && accept(Terminal.COLON) && accept(this::statementNoShortIf));
	}
	private boolean expressionStatement() {
		return accept(NonTerminal.EXPRESSION_STATEMENT,
			() -> accept(this::statementExpression) && accept(Terminal.SEMICOLON));
	}
	private boolean statementExpression() {
		return accept(NonTerminal.STATEMENT_EXPRESSION,
			() -> accept(this::assignment),
			() -> accept(this::preIncrementExpression),
			() -> accept(this::preDecrementExpression),
			() -> accept(this::postIncrementExpression),
			() -> accept(this::postDecrementExpression),
			() -> accept(this::methodInvocation),
			() -> accept(this::classInstanceCreationExpression));
	}
	private boolean ifThenStatement() {
		return accept(NonTerminal.IF_THEN_STATEMENT,
			() -> accept(Terminal.IF) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::statement));
	}
	private boolean ifThenElseStatement() {
		return accept(NonTerminal.IF_THEN_ELSE_STATEMENT,
			() -> accept(Terminal.IF) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::statementNoShortIf) && accept(Terminal.ELSE) && accept(this::statement));
	}
	private boolean ifThenElseStatementNoShortIf() {
		return accept(NonTerminal.IF_THEN_ELSE_STATEMENT_NO_SHORT_IF,
			() -> accept(Terminal.IF) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::statementNoShortIf) && accept(Terminal.ELSE) && accept(this::statementNoShortIf));
	}
	private boolean assertStatement() {
		return accept(NonTerminal.ASSERT_STATEMENT,
			() -> accept(Terminal.ASSERT) && accept(this::expression) && accept(Terminal.SEMICOLON),
			() -> accept(Terminal.ASSERT) && accept(this::expression) && accept(Terminal.COLON) && accept(this::expression) && accept(Terminal.SEMICOLON));
	}
	private boolean switchStatement() {
		return accept(NonTerminal.SWITCH_STATEMENT,
			() -> accept(Terminal.SWITCH) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::switchBlock));
	}
	private boolean switchBlock() {
		return accept(NonTerminal.SWITCH_BLOCK,
			() -> accept(Terminal.OPEN_BRACE) && acceptRepeating(this::switchBlockStatementGroup) && acceptRepeating(this::switchLabel) && accept(Terminal.CLOSE_BRACE));
	}
	private boolean switchBlockStatementGroup() {
		return accept(NonTerminal.SWITCH_BLOCK_STATEMENT_GROUP,
			() -> accept(this::switchLabels) && accept(this::blockStatements));
	}
	private boolean switchLabels() {
		return accept(NonTerminal.SWITCH_LABELS,
			() -> accept(this::switchLabel) && acceptRepeating(this::switchLabel));
	}
	private boolean switchLabel() {
		return accept(NonTerminal.SWITCH_LABEL,
			() -> accept(Terminal.CASE) && accept(this::constantExpression) && accept(Terminal.COLON),
			() -> accept(Terminal.CASE) && accept(this::enumConstantName) && accept(Terminal.COLON),
			() -> accept(Terminal.DEFAULT) && accept(Terminal.COLON));
	}
	private boolean enumConstantName() {
		return accept(NonTerminal.ENUM_CONSTANT_NAME,
			() -> accept(this::identifier));
	}
	private boolean whileStatement() {
		return accept(NonTerminal.WHILE_STATEMENT,
			() -> accept(Terminal.WHILE) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::statement));
	}
	private boolean whileStatementNoShortIf() {
		return accept(NonTerminal.WHILE_STATEMENT_NO_SHORT_IF,
			() -> accept(Terminal.WHILE) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::statementNoShortIf));
	}
	private boolean doStatement() {
		return accept(NonTerminal.DO_STATEMENT,
			() -> accept(Terminal.DO) && accept(this::statement) && accept(Terminal.WHILE) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(Terminal.SEMICOLON));
	}
	private boolean forStatement() {
		return accept(NonTerminal.FOR_STATEMENT,
			() -> accept(this::basicForStatement),
			() -> accept(this::enhancedForStatement));
	}
	private boolean forStatementNoShortIf() {
		return accept(NonTerminal.FOR_STATEMENT_NO_SHORT_IF,
			() -> accept(this::basicForStatementNoShortIf),
			() -> accept(this::enhancedForStatementNoShortIf));
	}
	private boolean basicForStatement() {
		return accept(NonTerminal.BASIC_FOR_STATEMENT,
			() -> accept(Terminal.FOR) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::forInit) && accept(Terminal.SEMICOLON) && acceptOptional(this::expression) && accept(Terminal.SEMICOLON) && acceptOptional(this::forUpdate) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::statement));
	}
	private boolean basicForStatementNoShortIf() {
		return accept(NonTerminal.BASIC_FOR_STATEMENT_NO_SHORT_IF,
			() -> accept(Terminal.FOR) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::forInit) && accept(Terminal.SEMICOLON) && acceptOptional(this::expression) && accept(Terminal.SEMICOLON) && acceptOptional(this::forUpdate) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::statementNoShortIf));
	}
	private boolean forInit() {
		return accept(NonTerminal.FOR_INIT,
			() -> accept(this::statementExpressionList),
			() -> accept(this::localVariableDeclaration));
	}
	private boolean forUpdate() {
		return accept(NonTerminal.FOR_UPDATE,
			() -> accept(this::statementExpressionList));
	}
	private boolean statementExpressionList() {
		return accept(NonTerminal.STATEMENT_EXPRESSION_LIST,
			() -> accept(this::statementExpression) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::statementExpression)));
	}
	private boolean enhancedForStatement() {
		return accept(NonTerminal.ENHANCED_FOR_STATEMENT,
			() -> accept(Terminal.FOR) && accept(Terminal.OPEN_PARENTHESIS) && acceptRepeating(this::variableModifier) && accept(this::unannType) && accept(this::variableDeclaratorId) && accept(Terminal.COLON) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::statement));
	}
	private boolean enhancedForStatementNoShortIf() {
		return accept(NonTerminal.ENHANCED_FOR_STATEMENT_NO_SHORT_IF,
			() -> accept(Terminal.FOR) && accept(Terminal.OPEN_PARENTHESIS) && acceptRepeating(this::variableModifier) && accept(this::unannType) && accept(this::variableDeclaratorId) && accept(Terminal.COLON) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::statementNoShortIf));
	}
	private boolean breakStatement() {
		return accept(NonTerminal.BREAK_STATEMENT,
			() -> accept(Terminal.BREAK) && acceptOptional(this::identifier) && accept(Terminal.SEMICOLON));
	}
	private boolean continueStatement() {
		return accept(NonTerminal.CONTINUE_STATEMENT,
			() -> accept(Terminal.CONTINUE) && acceptOptional(this::identifier) && accept(Terminal.SEMICOLON));
	}
	private boolean returnStatement() {
		return accept(NonTerminal.RETURN_STATEMENT,
			() -> accept(Terminal.RETURN) && acceptOptional(this::expression) && accept(Terminal.SEMICOLON));
	}
	private boolean throwStatement() {
		return accept(NonTerminal.THROW_STATEMENT,
			() -> accept(Terminal.THROW) && accept(this::expression) && accept(Terminal.SEMICOLON));
	}
	private boolean synchronizedStatement() {
		return accept(NonTerminal.SYNCHRONIZED_STATEMENT,
			() -> accept(Terminal.SYNCHRONIZED) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::block));
	}
	private boolean tryStatement() {
		return accept(NonTerminal.TRY_STATEMENT,
			() -> accept(Terminal.TRY) && accept(this::block) && accept(this::catches),
			() -> accept(Terminal.TRY) && accept(this::block) && acceptOptional(this::catches) && accept(this::finally_),
			() -> accept(this::tryWithResourcesStatement));
	}
	private boolean catches() {
		return accept(NonTerminal.CATCHES,
			() -> accept(this::catchClause) && acceptRepeating(this::catchClause));
	}
	private boolean catchClause() {
		return accept(NonTerminal.CATCH_CLAUSE,
			() -> accept(Terminal.CATCH) && accept(Terminal.OPEN_PARENTHESIS) && accept(this::catchFormalParameter) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::block));
	}
	private boolean catchFormalParameter() {
		return accept(NonTerminal.CATCH_FORMAL_PARAMETER,
			() -> acceptRepeating(this::variableModifier) && accept(this::catchType) && accept(this::variableDeclaratorId));
	}
	private boolean catchType() {
		return accept(NonTerminal.CATCH_TYPE,
			() -> accept(this::unannClassType) && acceptRepeating(() -> accept(Terminal.OR) && accept(this::classType)));
	}
	private boolean finally_() {
		return accept(NonTerminal.FINALLY,
			() -> accept(Terminal.FINALLY) && accept(this::block));
	}
	private boolean tryWithResourcesStatement() {
		return accept(NonTerminal.TRY_WITH_RESOURCES_STATEMENT,
			() -> accept(Terminal.TRY) && accept(this::resourceSpecification) && accept(this::block) && acceptOptional(this::catches) && acceptOptional(this::finally_));
	}
	private boolean resourceSpecification() {
		return accept(NonTerminal.RESOURCE_SPECIFICATION,
			() -> accept(Terminal.OPEN_PARENTHESIS) && accept(this::resourceList) && acceptOptional(Terminal.SEMICOLON) && accept(Terminal.CLOSE_PARENTHESIS));
	}
	private boolean resourceList() {
		return accept(NonTerminal.RESOURCE_LIST,
			() -> accept(this::resource) && acceptRepeating(() -> accept(Terminal.SEMICOLON) && accept(this::resource)));
	}
	private boolean resource() {
		return accept(NonTerminal.RESOURCE,
			() -> acceptRepeating(this::variableModifier) && accept(this::unannType) && accept(this::variableDeclaratorId) && accept(Terminal.ASSIGN) && accept(this::expression));
	}
	private boolean primary() {
		return accept(NonTerminal.PRIMARY,
			() -> accept(this::primaryNoNewArray),
			() -> accept(this::arrayCreationExpression));
	}
	private boolean primaryNoNewArray() {
		return accept(NonTerminal.PRIMARY_NO_NEW_ARRAY,
			() -> accept(this::literal),
			() -> accept(this::classLiteral),
			() -> accept(Terminal.THIS),
			() -> accept(this::typeName) && accept(Terminal.DOT) && accept(Terminal.THIS),
			() -> accept(Terminal.OPEN_PARENTHESIS) && accept(this::expression) && accept(Terminal.CLOSE_PARENTHESIS),
			() -> accept(this::classInstanceCreationExpression),
			() -> accept(this::fieldAccess),
			() -> accept(this::arrayAccess),
			() -> accept(this::methodInvocation),
			() -> accept(this::methodReference));
	}
	private boolean classLiteral() {
		return accept(NonTerminal.CLASS_LITERAL,
			() -> accept(this::typeName) && acceptRepeating(() -> accept(Terminal.OPEN_BRACKET) && accept(Terminal.CLOSE_BRACKET)) && accept(Terminal.DOT) && accept(Terminal.CLASS),
			() -> accept(this::numericType) && acceptRepeating(() -> accept(Terminal.OPEN_BRACKET) && accept(Terminal.CLOSE_BRACKET)) && accept(Terminal.DOT) && accept(Terminal.CLASS),
			() -> accept(Terminal.BOOLEAN) && acceptRepeating(() -> accept(Terminal.OPEN_BRACKET) && accept(Terminal.CLOSE_BRACKET)) && accept(Terminal.DOT) && accept(Terminal.CLASS),
			() -> accept(Terminal.VOID) && accept(Terminal.DOT) && accept(Terminal.CLASS));
	}
	private boolean classInstanceCreationExpression() {
		return accept(NonTerminal.CLASS_INSTANCE_CREATION_EXPRESSION,
			() -> accept(this::unqualifiedClassInstanceCreationExpression),
			() -> accept(this::expressionName) && accept(Terminal.DOT) && accept(this::unqualifiedClassInstanceCreationExpression),
			() -> accept(this::primary) && accept(Terminal.DOT) && accept(this::unqualifiedClassInstanceCreationExpression));
	}
	private boolean unqualifiedClassInstanceCreationExpression() {
		return accept(NonTerminal.UNQUALIFIED_CLASS_INSTANCE_CREATION_EXPRESSION,
			() -> accept(Terminal.NEW) && acceptOptional(this::typeArguments) && accept(this::classOrInterfaceTypeToInstantiate) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS) && acceptOptional(this::classBody));
	}
	private boolean classOrInterfaceTypeToInstantiate() {
		return accept(NonTerminal.CLASS_OR_INTERFACE_TYPE_TO_INSTANTIATE,
			() -> acceptRepeating(this::annotation) && accept(this::identifier) && acceptRepeating(() -> accept(Terminal.DOT) && acceptRepeating(this::annotation) && accept(this::identifier)) && acceptOptional(this::typeArgumentsOrDiamond));
	}
	private boolean typeArgumentsOrDiamond() {
		return accept(NonTerminal.TYPE_ARGUMENTS_OR_DIAMOND,
			() -> accept(this::typeArguments),
			() -> accept(Terminal.LESS_THAN) && accept(Terminal.GREATER_THAN));
	}
	private boolean fieldAccess() {
		return accept(NonTerminal.FIELD_ACCESS,
			() -> accept(this::primary) && accept(Terminal.DOT) && accept(this::identifier),
			() -> accept(Terminal.SUPER) && accept(Terminal.DOT) && accept(this::identifier),
			() -> accept(this::typeName) && accept(Terminal.DOT) && accept(Terminal.SUPER) && accept(Terminal.DOT) && accept(this::identifier));
	}
	private boolean arrayAccess() {
		return accept(NonTerminal.ARRAY_ACCESS,
			() -> accept(this::expressionName) && accept(Terminal.OPEN_BRACKET) && accept(this::expression) && accept(Terminal.CLOSE_BRACKET),
			() -> accept(this::primaryNoNewArray) && accept(Terminal.OPEN_BRACKET) && accept(this::expression) && accept(Terminal.CLOSE_BRACKET));
	}
	private boolean methodInvocation() {
		return accept(NonTerminal.METHOD_INVOCATION,
			() -> accept(this::methodName) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS),
			() -> accept(this::typeName) && accept(Terminal.DOT) && acceptOptional(this::typeArguments) && accept(this::identifier) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS),
			() -> accept(this::expressionName) && accept(Terminal.DOT) && acceptOptional(this::typeArguments) && accept(this::identifier) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS),
			() -> accept(this::primary) && accept(Terminal.DOT) && acceptOptional(this::typeArguments) && accept(this::identifier) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS),
			() -> accept(Terminal.SUPER) && accept(Terminal.DOT) && acceptOptional(this::typeArguments) && accept(this::identifier) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS),
			() -> accept(this::typeName) && accept(Terminal.DOT) && accept(Terminal.SUPER) && accept(Terminal.DOT) && acceptOptional(this::typeArguments) && accept(this::identifier) && accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::argumentList) && accept(Terminal.CLOSE_PARENTHESIS));
	}
	private boolean argumentList() {
		return accept(NonTerminal.ARGUMENT_LIST,
			() -> accept(this::expression) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::expression)));
	}
	private boolean methodReference() {
		return accept(NonTerminal.METHOD_REFERENCE,
			() -> accept(this::expressionName) && accept(Terminal.DOUBLE_COLON) && acceptOptional(this::typeArguments) && accept(this::identifier),
			() -> accept(this::referenceType) && accept(Terminal.DOUBLE_COLON) && acceptOptional(this::typeArguments) && accept(this::identifier),
			() -> accept(this::primary) && accept(Terminal.DOUBLE_COLON) && acceptOptional(this::typeArguments) && accept(this::identifier),
			() -> accept(Terminal.SUPER) && accept(Terminal.DOUBLE_COLON) && acceptOptional(this::typeArguments) && accept(this::identifier),
			() -> accept(this::typeName) && accept(Terminal.DOT) && accept(Terminal.SUPER) && accept(Terminal.DOUBLE_COLON) && acceptOptional(this::typeArguments) && accept(this::identifier),
			() -> accept(this::classType) && accept(Terminal.DOUBLE_COLON) && acceptOptional(this::typeArguments) && accept(Terminal.NEW),
			() -> accept(this::arrayType) && accept(Terminal.DOUBLE_COLON) && accept(Terminal.NEW));
	}
	private boolean arrayCreationExpression() {
		return accept(NonTerminal.ARRAY_CREATION_EXPRESSION,
			() -> accept(Terminal.NEW) && accept(this::primitiveType) && accept(this::dimExprs) && acceptOptional(this::dims),
			() -> accept(Terminal.NEW) && accept(this::classOrInterfaceType) && accept(this::dimExprs) && acceptOptional(this::dims),
			() -> accept(Terminal.NEW) && accept(this::primitiveType) && accept(this::dims) && accept(this::arrayInitializer),
			() -> accept(Terminal.NEW) && accept(this::classOrInterfaceType) && accept(this::dims) && accept(this::arrayInitializer));
	}
	private boolean dimExprs() {
		return accept(NonTerminal.DIM_EXPRS,
			() -> accept(this::dimExpr) && acceptRepeating(this::dimExpr));
	}
	private boolean dimExpr() {
		return accept(NonTerminal.DIM_EXPR,
			() -> acceptRepeating(this::annotation) && accept(Terminal.OPEN_BRACKET) && accept(this::expression) && accept(Terminal.CLOSE_BRACKET));
	}
	private boolean expression() {
		return accept(NonTerminal.EXPRESSION,
			() -> accept(this::lambdaExpression),
			() -> accept(this::assignmentExpression));
	}
	private boolean lambdaExpression() {
		return accept(NonTerminal.LAMBDA_EXPRESSION,
			() -> accept(this::lambdaParameters) && accept(Terminal.LAMBDA) && accept(this::lambdaBody));
	}
	private boolean lambdaParameters() {
		return accept(NonTerminal.LAMBDA_PARAMETERS,
			() -> accept(this::identifier),
			() -> accept(Terminal.OPEN_PARENTHESIS) && acceptOptional(this::formalParameterList) && accept(Terminal.CLOSE_PARENTHESIS),
			() -> accept(Terminal.OPEN_PARENTHESIS) && accept(this::inferredFormalParameterList) && accept(Terminal.CLOSE_PARENTHESIS));
	}
	private boolean inferredFormalParameterList() {
		return accept(NonTerminal.INFERRED_FORMAL_PARAMETER_LIST,
			() -> accept(this::identifier) && acceptRepeating(() -> accept(Terminal.COMMA) && accept(this::identifier)));
	}
	private boolean lambdaBody() {
		return accept(NonTerminal.LAMBDA_BODY,
			() -> accept(this::expression),
			() -> accept(this::block));
	}
	private boolean assignmentExpression() {
		return accept(NonTerminal.ASSIGNMENT_EXPRESSION,
			() -> accept(this::conditionalExpression),
			() -> accept(this::assignment));
	}
	private boolean assignment() {
		return accept(NonTerminal.ASSIGNMENT,
			() -> accept(this::leftHandSide) && accept(this::assignmentOperator) && accept(this::expression));
	}
	private boolean leftHandSide() {
		return accept(NonTerminal.LEFT_HAND_SIDE,
			() -> accept(this::expressionName),
			() -> accept(this::fieldAccess),
			() -> accept(this::arrayAccess));
	}
	private boolean conditionalExpression() {
		return accept(NonTerminal.CONDITIONAL_EXPRESSION,
			() -> accept(this::conditionalOrExpression),
			() -> accept(this::conditionalOrExpression) && accept(Terminal.TERNARY) && accept(this::expression) && accept(Terminal.COLON) && accept(this::conditionalExpression),
			() -> accept(this::conditionalOrExpression) && accept(Terminal.TERNARY) && accept(this::expression) && accept(Terminal.COLON) && accept(this::lambdaExpression));
	}
	private boolean conditionalOrExpression() {
		return accept(NonTerminal.CONDITIONAL_OR_EXPRESSION,
			() -> accept(this::conditionalAndExpression),
			() -> accept(this::conditionalOrExpression) && accept(Terminal.OR_GATE) && accept(this::conditionalAndExpression));
	}
	private boolean conditionalAndExpression() {
		return accept(NonTerminal.CONDITIONAL_AND_EXPRESSION,
			() -> accept(this::inclusiveOrExpression),
			() -> accept(this::conditionalAndExpression) && accept(Terminal.AND_GATE) && accept(this::inclusiveOrExpression));
	}
	private boolean inclusiveOrExpression() {
		return accept(NonTerminal.INCLUSIVE_OR_EXPRESSION,
			() -> accept(this::exclusiveOrExpression),
			() -> accept(this::inclusiveOrExpression) && accept(Terminal.OR) && accept(this::exclusiveOrExpression));
	}
	private boolean exclusiveOrExpression() {
		return accept(NonTerminal.EXCLUSIVE_OR_EXPRESSION,
			() -> accept(this::andExpression),
			() -> accept(this::exclusiveOrExpression) && accept(Terminal.XOR) && accept(this::andExpression));
	}
	private boolean andExpression() {
		return accept(NonTerminal.AND_EXPRESSION,
			() -> accept(this::equalityExpression),
			() -> accept(this::andExpression) && accept(Terminal.AND) && accept(this::equalityExpression));
	}
	private boolean equalityExpression() {
		return accept(NonTerminal.EQUALITY_EXPRESSION,
			() -> accept(this::relationalExpression),
			() -> accept(this::equalityExpression) && accept(Terminal.EQUAL_TO) && accept(this::relationalExpression),
			() -> accept(this::equalityExpression) && accept(Terminal.NOT_EQUAL_TO) && accept(this::relationalExpression));
	}
	private boolean relationalExpression() {
		return accept(NonTerminal.RELATIONAL_EXPRESSION,
			() -> accept(this::shiftExpression),
			() -> accept(this::relationalExpression) && accept(Terminal.LESS_THAN) && accept(this::shiftExpression),
			() -> accept(this::relationalExpression) && accept(Terminal.GREATER_THAN) && accept(this::shiftExpression),
			() -> accept(this::relationalExpression) && accept(Terminal.LESS_THAN_OR_EQUAL_TO) && accept(this::shiftExpression),
			() -> accept(this::relationalExpression) && accept(Terminal.GREATER_THAN_OR_EQUAL_TO) && accept(this::shiftExpression),
			() -> accept(this::relationalExpression) && accept(Terminal.INSTANCEOF) && accept(this::referenceType));
	}
	private boolean shiftExpression() {
		return accept(NonTerminal.SHIFT_EXPRESSION,
			() -> accept(this::additiveExpression),
			() -> accept(this::shiftExpression) && accept(Terminal.LEFT_SHIFT) && accept(this::additiveExpression),
			() -> accept(this::shiftExpression) && accept(Terminal.RIGHT_SHIFT) && accept(this::additiveExpression),
			() -> accept(this::shiftExpression) && accept(Terminal.UNSIGNED_RIGHT_SHIFT) && accept(this::additiveExpression));
	}
	private boolean additiveExpression() {
		return accept(NonTerminal.ADDITIVE_EXPRESSION,
			() -> accept(this::multiplicativeExpression),
			() -> accept(this::additiveExpression) && accept(Terminal.ADD) && accept(this::multiplicativeExpression),
			() -> accept(this::additiveExpression) && accept(Terminal.SUBTRACT) && accept(this::multiplicativeExpression));
	}
	private boolean multiplicativeExpression() {
		return accept(NonTerminal.MULTIPLICATIVE_EXPRESSION,
			() -> accept(this::unaryExpression),
			() -> accept(this::multiplicativeExpression) && accept(Terminal.MULTIPLY) && accept(this::unaryExpression),
			() -> accept(this::multiplicativeExpression) && accept(Terminal.DIVIDE) && accept(this::unaryExpression),
			() -> accept(this::multiplicativeExpression) && accept(Terminal.MODULO) && accept(this::unaryExpression));
	}
	private boolean unaryExpression() {
		return accept(NonTerminal.UNARY_EXPRESSION,
			() -> accept(this::preIncrementExpression),
			() -> accept(this::preDecrementExpression),
			() -> accept(Terminal.ADD) && accept(this::unaryExpression),
			() -> accept(Terminal.SUBTRACT) && accept(this::unaryExpression),
			() -> accept(this::unaryExpressionNotPlusMinus));
	}
	private boolean preIncrementExpression() {
		return accept(NonTerminal.PRE_INCREMENT_EXPRESSION,
			() -> accept(Terminal.INCREMENT) && accept(this::unaryExpression));
	}
	private boolean preDecrementExpression() {
		return accept(NonTerminal.PRE_DECREMENT_EXPRESSION,
			() -> accept(Terminal.DECREMENT) && accept(this::unaryExpression));
	}
	private boolean unaryExpressionNotPlusMinus() {
		return accept(NonTerminal.UNARY_EXPRESSION_NOT_PLUS_MINUS,
			() -> accept(this::postfixExpression),
			() -> accept(Terminal.BITWISE_COMPLEMENT) && accept(this::unaryExpression),
			() -> accept(Terminal.NOT) && accept(this::unaryExpression),
			() -> accept(this::castExpression));
	}
	private boolean postfixExpression() {
		return accept(NonTerminal.POSTFIX_EXPRESSION,
			() -> accept(this::primary),
			() -> accept(this::expressionName),
			() -> accept(this::postIncrementExpression),
			() -> accept(this::postDecrementExpression));
	}
	private boolean postIncrementExpression() {
		return accept(NonTerminal.POST_INCREMENT_EXPRESSION,
			() -> accept(this::postfixExpression) && accept(Terminal.INCREMENT));
	}
	private boolean postDecrementExpression() {
		return accept(NonTerminal.POST_DECREMENT_EXPRESSION,
			() -> accept(this::postfixExpression) && accept(Terminal.DECREMENT));
	}
	private boolean castExpression() {
		return accept(NonTerminal.CAST_EXPRESSION,
			() -> accept(Terminal.OPEN_PARENTHESIS) && accept(this::primitiveType) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::unaryExpression),
			() -> accept(Terminal.OPEN_PARENTHESIS) && accept(this::referenceType) && acceptRepeating(this::additionalBound) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::unaryExpressionNotPlusMinus),
			() -> accept(Terminal.OPEN_PARENTHESIS) && accept(this::referenceType) && acceptRepeating(this::additionalBound) && accept(Terminal.CLOSE_PARENTHESIS) && accept(this::lambdaExpression));
	}
	private boolean constantExpression() {
		return accept(NonTerminal.CONSTANT_EXPRESSION,
			() -> accept(this::expression));
	}
}