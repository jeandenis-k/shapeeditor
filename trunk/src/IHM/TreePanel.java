package IHM;

import Draw.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class TreePanel extends JPanel {

    /* La vue associee au graphe de scene */
    public JTree arbre;
    public SceneGraph sceneGraph = null;
    /* Le menu contextuel ouvert lors du clic droit sur un noeud de l'arbre */
    private JPopupMenu jpm = new JPopupMenu();
    private JMenuItem copyItem = new JMenuItem("Copy");
    private JMenuItem delItem = new JMenuItem("Delete");

    public TreePanel() {
        super();

        this.arbre = new JTree(Window.sceneGraph);
        //Permet de selectionner un noeud par un clic gauche sur le JTree
        this.arbre.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.arbre.setShowsRootHandles(true);

        /* Pour que l'arbre prenne toute la place disponible par defaut */
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(arbre), BorderLayout.CENTER);
    }

    public void initializeListeners(final DrawPanel drawZone) {
        // Selection du noeud sur lequel on clique
        arbre.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent event) {
                if (arbre.getLastSelectedPathComponent() != null) {
                    drawZone.getSelection().clear();
                    SceneGraph node = (SceneGraph) arbre.getLastSelectedPathComponent();
                    if (!node.isRoot()) {
                        drawZone.getSelection().add(node);
                    }
                    //System.out.println("Selection " + node.toString());
                    drawZone.repaint();
                }
            }
            /*
            // Recupere les parents
            private String getAbsolutePath(TreePath treePath){
            String str = "";
            //On balaie le contenu de notre TreePath
            for(Object name : treePath.getPath()){
            //Si l'objet à un nom, on l'ajoute au chemin
            if(name.toString() != null)
            str += name.toString();
            }
            return str;
            }
             */
        });


        //Ajout du menu contextuel ouvert par clic droit sur un noeud
        //Copie
        jpm.add(copyItem);
        copyItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawZone.copyCurrentSelection();
                drawZone.repaint();
            }
        });
        //Suppression
        jpm.add(delItem);
        delItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawZone.deleteCurrentSelection();
                drawZone.repaint();
            }
        });
        // Ajout du MouseListener a l'arbre
        arbre.addMouseListener(new MouseAdapter() {

            @Override
            //Methode declenchee lors d'un clic droit sous linux
            public void mousePressed(MouseEvent e) {
                // Detection du clic droit
                if (e.isPopupTrigger()) {
                    int selRow = arbre.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = arbre.getPathForLocation(e.getX(), e.getY());
                    // Verification de la validite du clic (sur un noeud et pas sur la racine)
                    if (selRow != -1 && selRow != 0) {
                        arbre.clearSelection();
                        arbre.setSelectionPath(selPath);
                        // Affichage du menu contextuel
                        jpm.show(arbre, e.getX(), e.getY());
                    }
                }
            }
            //Methode declenchee lors d'un clic droit sous windows

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed(e);
            }
        });

    }

    public void repaintJTree() {
        ((DefaultTreeModel) arbre.getModel()).reload();
    }
}
