package tree;
public class TreeTightener {
	public void tighten(TreeNode node) {
		if (!(node instanceof NonterminalNode)) return;
		NonterminalNode parent = (NonterminalNode) node;

		for (TreeNode child : parent.getChildren())
			tighten(child);


		for (int i = 0; i < parent.size(); ++i) {
			TreeNode onlyChild = onlyChild(parent.getChildren().get(i));
			if (onlyChild != null)
				parent.getChildren().set(i, onlyChild);
		}
	}
	public TreeNode onlyChild(TreeNode node) {
		if (!(node instanceof NonterminalNode)) return null;
		NonterminalNode parent = (NonterminalNode) node;
		return parent.size() == 1 ? parent.getChildren().get(0) : null;
	}
}