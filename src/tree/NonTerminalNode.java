package tree;

import main.NonTerminal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class NonTerminalNode implements TreeNode {
	// only used for printing the tree, irrelevant to any scanning, parsing, or interpreting algorithm

	private static final EnumMap<NonTerminal, String> NONTERMINAL_STRINGS = new EnumMap<>(NonTerminal.class);
	static {

	}

	private final List<TreeNode> children = new ArrayList<>();
	private final NonTerminal value;
	public NonTerminalNode(NonTerminal value) {
		this.value = value;
	}


	public void addChild(TreeNode child) {
		if (child == null) {
			new Exception().printStackTrace();
		}
		getChildren().add(child);
	}
	public void addChildren(TreeNode... children) {
		for (TreeNode child : children) {
			addChild(child);
		}
	}

	// A shorthand for ((NonTerminalNode) getChildren().get(index)) when we KNOW children.get(index) exists and is of
	// type NonTerminalNode
	public NonTerminalNode getNonTerminalChild(int index) {
		return (NonTerminalNode) children.get(index);
	}

	// A shorthand for ((TerminalNode) getChildren().get(index)) when we KNOW children.get(index) exists and is of
	// type TerminalNode
	public TerminalNode getTerminalChild(int index) {
		return (TerminalNode) children.get(index);
	}

	public int size() {
		return getChildren().size();
	}

	@Override
	public String valueString() {
		final String original = value.toString();
		StringBuilder stringBuilder = new StringBuilder();
		boolean capitalize = false;
		for (int i = 0; i < original.length(); ++i) {
			final char c = original.charAt(i);
			if (c == '_') {
				capitalize = true;
			} else {
				stringBuilder.append(capitalize || i == 0 ? c : Character.toLowerCase(c));
				capitalize = false;
			}
		}
		return stringBuilder.toString();
	}

	@Override
	public void buildString(StringBuilder full, StringBuilder indent, boolean isLastChild) {
		// Recursive method to build strings from tree nodes
		// Mimics the format of the "tree" command in Windows command prompt

		TreeNode.super.buildString(full, indent, isLastChild);

		if (children.size() == 0) return;
		indent.append(isLastChild ? ' ' : '\u2502').append("   ");
		TreeNode lastChild = children.get(children.size() - 1);
		for (TreeNode child : children) {
			child.buildString(full, indent, child == lastChild);
		}
		indent.setLength(indent.length() - 4);
	}

	@Override
	public String toString() {
		StringBuilder full = new StringBuilder();
		StringBuilder indent = new StringBuilder();

		full.append(valueString()).append('\n');

		if (children.size() == 0) return full.toString();

		TreeNode lastChild = children.get(children.size() - 1);
		for (TreeNode child : children) {
			child.buildString(full, indent, child == lastChild);
		}

		return full.toString();
	}

	public NonTerminal getValue() {
		return value;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
}
