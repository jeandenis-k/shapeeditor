package Draw;

import java.util.Enumeration;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Area;
import java.util.Stack;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * Implantation générique du graphe de scène.
 * Chaque noeud du graphe est lui-même un graphe de scène, et possède
 * un attribut <tt>view</tt> contenant les paramètres de dessin
 * (couleurs et épaisseur de trait).
 * Les feuilles de ce graphe sont des formes géométriques.
 * Chaque noeud est un regroupement de formes (groupe) ou une
 * transformation géométrique (binaire ou unaire).
 *
 * @author Boris Dadachev & Jean-Denis Koeck
 */

public class SceneGraph extends DefaultMutableTreeNode {

    /**
     * Paramètres de dessin : contient couleur et trait d'affichage.
     * @see View
     */
    protected View view;

    /**
     * Constructeur par défaut.
     * @param view     Contient couleur et trait d'affichage
     * @param name  Rôle du noeud dans le graphe de scène
     */
    public SceneGraph(View view, String name)
    {
	super(name);
	setView(view);
    }

    /**
     * Constructeur affectant des valeurs par défaut à la vue
     * @param name
     */
    public SceneGraph(String name)
    {
	super(name);
	view = new View(Color.BLACK, 1, Color.BLACK, null);
    }

    /**
     * Constructeur recevant une forme géométrique, qui
     * est reparentée pour être un sous-noeud de l'objet courant.
     * @param shape sous-noeud 
     * @see SceneShape
     */
    public SceneGraph(SceneShape shape)
    {
	super();
	view = new View(Color.BLACK, 1, Color.BLACK, null);
	this.add(shape);
    }

    /**
     * Copie profonde du graphe de scène.
     * Chaque sous-noeud est copié, il n'y a pas de partage
     * de données entre un graphe de scène et sa copie.
     */
    @Override
    public SceneGraph clone() {
        SceneGraph node = (SceneGraph) super.clone();
        for (Enumeration<SceneGraph> en = this.children(); en.hasMoreElements();) {
            node.add((en.nextElement()).clone());
        }

        return node;
    }

    /**
     * Getter pour la propriété <tt>view</tt>
     * @return  Retourne la vue
     */
    public View getView() {
	return view;
    }	

    /**
     * Setter pour la propriété <tt>view</tt>
     * @param view  La vue à appliquer.
     */
    public void setView(View view) {
	this.view = view;
    }
        
    /**
     * Affiche le graphe de scène en dessinant récursivement chaque sous-graphe
     * @param g2d objet graphique
     */
    public void draw(Graphics2D g2d) {
	for(Enumeration<SceneGraph> en = this.children(); en.hasMoreElements();) {
	    en.nextElement().draw(g2d);
	}
    }

    /**
     * Retourne <code>true</code> si le point p est contenu dans la forme géométrique
     * résultant de l'affichage du graphe de scène
     * @param  p point
     * @return true si p contenu dans le graphe de scène
     */
    public boolean contains(Point2D p)
    {
	//Retourne true si l'un des sous-graphes contient le point
	for(Enumeration<SceneGraph> en = this.children(); en.hasMoreElements();) {
	    if(en.nextElement().contains(p)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Retourne un rectangle englobant le graphe de scène.
     * @return <code>null</code> si le graphe est vide
     */
    public Rectangle2D getBounds2D()
    {
	Rectangle2D r;
	Enumeration<SceneGraph> en = this.children();

	if(!en.hasMoreElements()) {
	    return null;
	} else {
	    //Crée l'union des rectangles englobant chaque sous-graphe
	    r = en.nextElement().getBounds2D();
	    while(en.hasMoreElements()) {
		r = r.createUnion(en.nextElement().getBounds2D());
	    }
	    return r;
	}
    }

    /**
     * Monte le graphe de scène courant au premier plan.
     * N'a aucun effet si le graphe de scène courant ne possède pas de parent
     * (ce qui est le cas du noeud racine). 
     */
    public void moveToFront()
    {
	SceneGraph sgParent = (SceneGraph) this.parent;
	if(sgParent != null) {
	    sgParent.remove(this);
	    sgParent.add(this);
	}
    }

    /**
     * Applique des transformations géométriques aux formes contenues dans
     * le graphe de scène
     * @param unaryOps Pile contenant des transformations géométriques unaires
     */
    public void applyUnaryOperations(Stack<UnaryOperation> unaryOps)
    {
	for(Enumeration<SceneGraph> en = this.children(); en.hasMoreElements();) {
	    en.nextElement().applyUnaryOperations(unaryOps);
	}
    }

    /**
     * Applique toutes les transformations géométriques contenues dans le graphe de scène,
     * en partant de la racine pour descendre vers les sous-graphes puis les feuilles du graphe.
     */
    public void update()
    {
	((SceneGraph) this.getRoot()).applyUnaryOperations(new Stack());
    }

    /**
     * Calcule de manière approximative le barycentre du graphe de scène
     * @return coordonnée hozirontale du barycentre du graphe de scène.
     */
    public double getBarycenterX()
    {
	double x = 0;
	for(Enumeration<SceneGraph> en = this.children(); en.hasMoreElements();) {
	    x += en.nextElement().getBarycenterX();
	}
	return x / this.getChildCount();
    }

    /**
     * Calcule de manière approximative le barycentre du graphe de scène
     * @return coordonnée verticale du barycentre du graphe de scène
     */
    public double getBarycenterY()
    {
	double y = 0;
	for(Enumeration<SceneGraph> en = this.children(); en.hasMoreElements();) {
	    y += en.nextElement().getBarycenterY();
	}
	return y / this.getChildCount();
    }

    /**
     * Calcule une forme géométrique à partir des formes et transformations contenues
     * dans le graphe de scène
     * @return forme géométrique représentant le graphe de scène
     */
    public Shape getShape()
    {
	Area area = new Area();
	for(Enumeration<SceneGraph> en = this.children(); en.hasMoreElements();) {
	    SceneGraph sg = en.nextElement();
	    area.add(new Area(sg.getShape()));
	}
	return area;
    }
}
