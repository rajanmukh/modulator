/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphicElements;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.LineBorder;

/**
 *
 * @author rajan
 */
public class RoundedBorder extends LineBorder {

    private final int arc;

//    public RoundedBorder(Color color, int thickness, boolean roundedCorners) {
//        super(color, thickness, roundedCorners);
//    }
    public RoundedBorder(Color color, int thickness, int arcdim) {
        super(color, thickness, true);
        arc=arcdim;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
         //To change body of generated methods, choose Tools | Templates.
        if ((this.thickness > 0) && (g instanceof Graphics2D)) {
            Graphics2D g2d = (Graphics2D) g;

            Color oldColor = g2d.getColor();
            g2d.setColor(this.lineColor);

            Shape outer;
            Shape inner;

            int offs = this.thickness;
            int size = offs + offs;

            
            outer = new RoundRectangle2D.Float(x, y, width, height, arc, arc);
            inner = new RoundRectangle2D.Float(x + offs, y + offs, width - size, height - size, arc, arc);

            Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
            path.append(outer, false);
            path.append(inner, false);
            g2d.fill(path);
            g2d.setColor(oldColor);
        }
    }

}
