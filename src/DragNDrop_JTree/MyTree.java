package DragNDrop_JTree;

import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * @author David Roussel
 */
public class MyTree {

    /**
     *
     */
    protected MutableTreeNode root;

    /**
     *  Returns a MyTree
     */
    @SuppressWarnings("unused")
    public MyTree() {
        root = new MyBranchNode();
        MyBranchNode cat1 = new MyBranchNode(root);
        MyBranchNode cat2 = new MyBranchNode(root);
        MyBranchNode cat3 = new MyBranchNode(root);

        MyLeafNode item11 = new MyLeafNode(cat1);
        MyLeafNode item12 = new MyLeafNode(cat1);
        MyLeafNode item21 = new MyLeafNode(cat2);
        MyLeafNode item31 = new MyLeafNode(cat3);

        MyBranchNode subcat3 = new MyBranchNode(cat3);
        MyLeafNode item311 = new MyLeafNode(subcat3);

    }

    /**
     *
     * @param node
     * @param level
     * @return chaîne de caractères répréntant l'arbre <tt>node</tt> jusqu'au niveau <tt>level</tt>
     */
    public static String toStringNode(TreeNode node, int level) {
        StringBuffer sb = new StringBuffer();

        if (node != null) {
            for (int i = 0; i < level; i++) {
                sb.append(" ");
            }
            sb.append(node.toString());

            if (node.getAllowsChildren()) {
                for (Enumeration<?> en = node.children(); en.hasMoreElements();) {
                    sb.append("\n");
                    sb.append(toStringNode((TreeNode) en.nextElement(),
                            level + 1));
                }
            }
        }
        return new String(sb);
    }

    /**
     * @return Racine de l'arbre
     */
    public MutableTreeNode getRoot() {
        return root;
    }
}
