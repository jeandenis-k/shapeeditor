package Draw;

import java.awt.geom.Area;

/**
 *
 * @author Boris Dadachev & Jean-Denis Koeck
 */
public class Intersection extends BinaryOperation {

    /**
     *
     * @param sg1
     * @param sg2
     */
    public Intersection(SceneGraph sg1, SceneGraph sg2) {
        super("Intersection", sg1, sg2);
        Area area = new Area(sg1.getShape());
        area.intersect(new Area(sg2.getShape()));
        shape = area;
        baseShape = area;
    }
}
