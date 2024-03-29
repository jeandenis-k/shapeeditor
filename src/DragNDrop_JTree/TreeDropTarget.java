package DragNDrop_JTree;

import Draw.BinaryOperation;
import Draw.Group;
import Draw.SceneGraph;
import Draw.UnaryOperation;
import Draw.View;
import IHM.DrawPanel;
import IHM.Window;
import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A drag targer objet to drag'N'drop in the JTree
 * @author Boris Dadachev & Jean-Denis Koeck
 */
public class TreeDropTarget implements DropTargetListener {

    DropTarget target;
    JTree targetTree;
    private DrawPanel drawZone;

    /**
     * Returns a drag target object
     * @param tree
     * @param drawZone
     */
    public TreeDropTarget(JTree tree, DrawPanel drawZone) {
        targetTree = tree;
        target = new DropTarget(targetTree, this);
        this.drawZone = drawZone;
    }

    private Component getComponentForEvent(DropTargetDragEvent dtde) {
        DropTargetContext dtc = dtde.getDropTargetContext();
        return dtc.getComponent();
    }

    /*
     * Drop Event Handlers
     */
    private TreeNode getNodeForEvent(DropTargetDragEvent dtde) {
        Point p = dtde.getLocation();
        JTree tree = (JTree) getComponentForEvent(dtde);
        TreePath path = tree.getClosestPathForLocation(p.x, p.y);
        return (TreeNode) path.getLastPathComponent();
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        MutableTreeNode selectedNode = (MutableTreeNode) targetTree.getSelectionPath().getLastPathComponent();

        MutableTreeNode node = (MutableTreeNode) getNodeForEvent(dtde);

        MutableTreeNode tmp = node;

        while (null != tmp) {
            if (tmp.equals(selectedNode)) {
                dtde.rejectDrag();
                return;
            }
            tmp = (MutableTreeNode) tmp.getParent();
        }

        dtde.acceptDrag(dtde.getDropAction());
        return;
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        //System.out.println("dropExit");
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        //System.out.println("dropActionChanged");
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        Point pt = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent();
        TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
        MutableTreeNode parent = (MutableTreeNode) parentpath.getLastPathComponent();

        /*if (parent instanceof UnaryOperation || parent instanceof BinaryOperation){
        System.out.println("Drop impossible à cause de l'arité");
        return;
        }*/

        MutableTreeNode selectedNode = (MutableTreeNode) targetTree.getSelectionPath().getLastPathComponent();
        MutableTreeNode clonedNode = ((SceneGraph) selectedNode).clone();
        ((SceneGraph) selectedNode.getParent()).add((SceneGraph) clonedNode);

        SceneGraph tmpsel;

        MutableTreeNode tmp = parent;
        while (null != tmp) {
            if (tmp.equals(selectedNode)) {
                dtde.rejectDrop();
                return;
            }
            tmp = (MutableTreeNode) tmp.getParent();
        }

        dtde.acceptDrop(dtde.getDropAction());

        if (parent.isLeaf() || parent instanceof UnaryOperation || parent instanceof BinaryOperation) {
            System.out.println(parent);
            System.out.println(clonedNode);
            Vector<SceneGraph> sel = new Vector<SceneGraph>();

            sel.add((SceneGraph) parent);
            sel.add((SceneGraph) clonedNode);

            SceneGraph tmpGraph = (SceneGraph) parent.getParent();

            ((SceneGraph) clonedNode).removeFromParent();
            ((SceneGraph) parent).removeFromParent();

            Group gr = new Group(new View(Window.sceneGraph.getView()));
            for (Enumeration<SceneGraph> en = sel.elements(); en.hasMoreElements();) {
                SceneGraph g = en.nextElement();
                gr.add(g);
            }
            tmpGraph.add(gr);

            tmpsel = gr;
        } else {
            ((SceneGraph) parent).add((SceneGraph) clonedNode);
            tmpsel = (SceneGraph) clonedNode;
        }

        while (null != tmpsel.getParent() && null != tmpsel.getParent().getParent()) {
            tmpsel = (SceneGraph) tmpsel.getParent();
        }

        drawZone.getSelection().clear();
        drawZone.getSelection().add(tmpsel);
        drawZone.repaintPanel();
        dtde.dropComplete(true);

        return;
    }
}
