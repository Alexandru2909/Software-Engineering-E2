package Shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class NodeShape extends Ellipse2D.Double {

    private Boolean isSelected;

    public NodeShape(Integer x, Integer y, Integer radius) {
        super(x - radius, y - radius, radius * 2, radius * 2);
        this.isSelected = false;
    }

    public void select(int x, int y) {
        if (this.contains(x, y)) {
            this.isSelected = false;
        }
    }

    public void unselect() {
        this.isSelected = false;
    }
}
