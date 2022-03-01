package main;
import tree.NonterminalNode;
import tree.TerminalNode;
import java.io.IOException;
import java.util.ArrayList;
public class SimpleParser {
	public static void main(String[] args) throws IOException {
		ArrayList<Token> tokens = new Lexer().lex("in/MyClass.java");

		for (Token token : tokens)
			System.out.println(token);

		SimpleParser parser = new SimpleParser(tokens);

		NonterminalNode start = parser.parse();

		System.out.println(start);
	}

	private final ArrayList<Token> tokens;
	public final NonterminalNode root = new NonterminalNode(Nonterminal.MY_ROOT);
	private boolean parsing;
	private int currentIndex;
	private NonterminalNode currentParent;
	private Token currentToken;

	public SimpleParser(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}

	private interface NonterminalAcceptor { boolean accept(); }

	public NonterminalNode parse() {
		start();
		accept(this::compilationUnit);
		return root;
	}
	private boolean identifier() {
		return accept(Nonterminal.IDENTIFIER, () -> acceptAppendAdvance(parsing && currentToken.getType() == TokenType.IDENTIFIER));
	}
	private boolean identifier(String value) {
		return accept(Nonterminal.IDENTIFIER, () -> acceptAppendAdvance(parsing && currentToken.getType() == TokenType.IDENTIFIER && currentToken.getText().equals(value)));
	}
	private boolean literal() {
		return accept(Nonterminal.LITERAL, () -> acceptAppendAdvance(currentToken.getType() == TokenType.LITERAL));
	}
	private boolean accept(Nonterminal value, NonterminalAcceptor acceptor) {
		final NonterminalNode parentAtStart = currentParent;

		currentParent = new NonterminalNode(value);

		final boolean accepted = accept(acceptor);

		if (accepted)
			parentAtStart.addChild(currentParent);
		currentParent = parentAtStart;

		return accepted;
	}
	private boolean acceptAny(NonterminalAcceptor... acceptors) {
		for (NonterminalAcceptor acceptor : acceptors)
			if (accept(acceptor)) return true;
		return false;
	}
	private boolean acceptAll(NonterminalAcceptor... acceptors) {
		final int childrenAtStart = currentParent.getChildren().size();
		final boolean accepted = accept(() -> {
			for (NonterminalAcceptor acceptor : acceptors)
				if (!accept(acceptor)) return false;
			return true;
		});
		if (!accepted) {
			while (currentParent.getChildren().size() > childrenAtStart) {
				currentParent.getChildren().remove(currentParent.getChildren().size() - 1);
			}
		}
		return accepted;
	}
	private boolean accept(Terminal terminal) {
		return acceptAppendAdvance(currentToken.getValue() == terminal);
	}
	private boolean accept(NonterminalAcceptor nonterminal) {
		final boolean parsingAtStart = parsing;
		final int indexAtStart = currentIndex;
		final NonterminalNode parentAtStart = currentParent;
		final Token tokenAtStart = currentToken;
		final boolean accepted = nonterminal.accept();
		if (!accepted) {
			parsing = parsingAtStart;
			currentIndex = indexAtStart;
			currentParent = parentAtStart;
			currentToken = tokenAtStart;
		}
		return accepted;
	}
	private boolean acceptRepeating(NonterminalAcceptor acceptor) {
		while (true)
			if (!accept(acceptor)) break;
		return true;
	}
	private boolean acceptOptional(NonterminalAcceptor nonterminal) {
		accept(nonterminal);
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
		return accept(Nonterminal.COMPILATION_UNIT, this::classDeclaration);
	}
	private boolean classDeclaration() {
		return accept(Nonterminal.CLASS_DECLARATION, () -> acceptAll(() -> acceptRepeating(this::classModifier), () -> accept(Terminal.CLASS), this::className, () -> acceptOptional(this::superClass), () -> acceptOptional(this::superInterfaces), this::classBody));
	}
	private boolean classModifier() {
		return accept(Nonterminal.CLASS_MODIFIER, () -> acceptAny(() -> accept(Terminal.PUBLIC), () -> accept(Terminal.PROTECTED), () -> accept(Terminal.PRIVATE), () -> accept(Terminal.ABSTRACT), () -> accept(Terminal.STATIC)));
	}
	private boolean className() {
		return accept(Nonterminal.CLASS_NAME, this::identifier);
	}
	private boolean superClass() {
		return accept(Nonterminal.SUPER_CLASS, () -> acceptAll(() -> accept(Terminal.EXTENDS), this::className));
	}
	private boolean superInterfaces() {
		return accept(Nonterminal.SUPER_INTERFACES, () -> acceptAll(() -> accept(Terminal.IMPLEMENTS), this::interfaceName, () -> acceptRepeating(() -> acceptAll(() -> accept(Terminal.COMMA), this::interfaceName))));
	}
	private boolean interfaceName() {
		return accept(Nonterminal.INTERFACE_NAME, this::identifier);
	}
	private boolean classBody() {
		return accept(Nonterminal.CLASS_BODY, () -> acceptAll(() -> accept(Terminal.OPEN_BRACE), () -> acceptRepeating(this::classMemberDeclaration), () -> accept(Terminal.CLOSE_BRACE)));
	}
	private boolean classMemberDeclaration() {
		return accept(Nonterminal.CLASS_MEMBER_DECLARATION, () -> acceptAny(this::mainMethodDeclaration, () -> accept(Terminal.SEMICOLON)));
	}
	private boolean mainMethodDeclaration() {
		return accept(Nonterminal.MAIN_METHOD_DECLARATION, () -> acceptAll(() -> accept(Terminal.PUBLIC), () -> accept(Terminal.STATIC), () -> accept(Terminal.VOID), () -> identifier("main"), () -> accept(Terminal.OPEN_PARENTHESIS), () -> identifier("String"), () -> accept(Terminal.OPEN_BRACKET), () -> accept(Terminal.CLOSE_BRACKET), this::identifier, () -> accept(Terminal.CLOSE_PARENTHESIS), this::methodBody));
	}
	private boolean methodBody() {
		return accept(Nonterminal.METHOD_BODY, () -> acceptAll(() -> accept(Terminal.OPEN_BRACE), () -> acceptRepeating(this::methodStatement), () -> accept(Terminal.CLOSE_BRACE)));
	}
	private boolean methodStatement() {
		return accept(Nonterminal.METHOD_STATEMENT, () -> acceptAny(this::printStatement, () -> accept(Terminal.SEMICOLON)));
	}
	private boolean printStatement() {
		return accept(Nonterminal.PRINT_STATEMENT, () -> acceptAll(() -> identifier("System"), () -> accept(Terminal.DOT), () -> identifier("out"), () -> accept(Terminal.DOT), () -> identifier("println"), () -> accept(Terminal.OPEN_PARENTHESIS), this::expression, () -> accept(Terminal.CLOSE_PARENTHESIS), () -> accept(Terminal.SEMICOLON)));
	}
	private boolean expression() {
		return accept(Nonterminal.EXPRESSION, this::literal);
	}
}