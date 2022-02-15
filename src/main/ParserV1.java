//package main;
//import tree.NonTerminalNode;
//import tree.TerminalNode;
//import tree.TreeNode;
//
//import java.util.ArrayList;
//public class ParserV1 {
//	private Token currentToken;
//	private ArrayList<Token> tokens;
//
//
//	private int currentIndex;
//	private boolean parsing;
//
//	private NonTerminalNode currentParent;
//
//
//
//	private void integralType() {
//
//	}
//
//	private boolean classDeclaration() {
//		currentParent = new NonTerminalNode(NonTerminal.CLASS_DECLARATION);
//
//
//	}
//
//	private interface Acceptor { TreeNode accept(); }
//
//	private void accept(Acceptor acceptor) {
//		final NonTerminalNode oldParent = currentParent;
//
//		TreeNode child = acceptor.accept();
//		if (child != null)
//			oldParent.addChild(currentParent);
//
//		currentParent = oldParent;
//	}
//
//	private boolean accept(Terminal value) {
//		return acceptAppendAdvance(currentToken.getValue() == value);
//	}
//
//	private void addChildOptional(NonTerminalNode parent, Acceptor acceptor) {
//		TreeNode child = acceptor.accept();
//		if (child != null) {
//			parent.addChild(child);
//		}
//	}
//	private void addChildRepeating(NonTerminalNode parent, Acceptor acceptor) {
//		while (true) {
//			TreeNode child = acceptor.accept();
//			if (child == null) {
//				break;
//			} else {
//				parent.addChild(child);
//			}
//		}
//	}
//
//	private void advance() {
//		currentToken = (parsing = ++currentIndex < tokens.size()) ? tokens.get(currentIndex) : null;
//	}
//
//	private boolean acceptAppendAdvance(boolean accepted) {
//		if (accepted &= parsing) {
//			currentParent.addChild(new TerminalNode(currentToken));
//			advance();
//		}
//		return accepted;
//	}
//}