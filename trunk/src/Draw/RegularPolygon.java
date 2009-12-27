/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Draw;

import java.awt.geom.Point2D;
import java.awt.Polygon;

/**
 *
 * @author Boris Dadachev & Jean-Denis Koeck
 */
public class RegularPolygon extends SceneShape
{
    int nbSides;
    final Polygon polygon;

    public RegularPolygon (View v, double x, double y, double rad, int n)
    {
        super(v);

        nbSides = n;

        int[][] tab = new int[2][nbSides];
        int[] origin = {(int) x, (int) y};
        int r = (int) rad;
        double angle = Math.PI/2;
        for (int i=0; i<this.nbSides; i++) {
            /* Calcul des coordonnees du point : utilisation des coordonnees polaires */
            tab[0][i] = origin[0] - (int)((double)r*Math.cos(angle));
            tab[1][i] = origin[1] - (int)((double)r*Math.sin(angle));
            angle += 2*Math.PI/this.nbSides;
        }

        polygon = new Polygon(tab[0], tab[1], nbSides);
        shape = polygon;
    }

    public void setLocation(Point2D p)
    {
        //Well, not implemented
        //We should use the method Polygon::translate(int deltaX, int deltaY) 
    }
}