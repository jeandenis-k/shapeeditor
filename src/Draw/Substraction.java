package Draw;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 *
 * @author Boris Dadachev & Jean-Denis Koeck
 */
public class Substraction extends BinaryOperation
{

    /**
     *
     * @param sg1
     * @param sg2
     */
    public Substraction(SceneGraph sg1, SceneGraph sg2)
    {
        super("Substraction", sg1, sg2);
        Area area = new Area(sg1.getShape());
        area.subtract(new Area(sg2.getShape()));
        shape = area;
        baseShape = area;
    }

}
