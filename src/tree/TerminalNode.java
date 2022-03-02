package tree;
import constants.Terminal;
import main.Token;

public class TerminalNode implements TreeNode {
	private final Token token;
	public TerminalNode(Token token) {
		this.token = token;
	}
	public Terminal getValue() {
		return token.getValue();
	}
	@Override
	public String valueString() {
		return "\u001B[32m" + token.getType().toString() + ": " + token.getText() + "\u001B[0m";
	}
	@Override
	public String toString() {
		return valueString();
	}
	public Token getToken() {
		return token;
	}
}