/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Draw;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.Polygon;

/**
 *
 * @author Boris Dadachev & Jean-Denis Koeck
 */
public class IrregularPolygon extends SceneShape {
    protected Polygon polygon;

    public IrregularPolygon (View v, int[][] vertices)
    {
        super(v);

        polygon = new Polygon(vertices[0], vertices[1], vertices[0].length);
        shape = polygon;
        baseShape = polygon;
    }
}