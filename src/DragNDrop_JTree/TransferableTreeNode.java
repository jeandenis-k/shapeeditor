package DragNDrop_JTree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.tree.TreePath;

/**
 * @author Boris Dadachev & Jean-Denis Koeck
 *
 */
class TransferableTreeNode implements Transferable {

    public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class,
            "Tree Path");
    DataFlavor flavors[] = {TREE_PATH_FLAVOR};
    TreePath path;

    public TransferableTreeNode(TreePath tp) {
        path = tp;
    }

    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return (flavor.getRepresentationClass() == TreePath.class);
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return path;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
