/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphicElements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.text.Position;

/**
 *
 * @author rajan
 */
public class ThickArrow {

        double x, y, width, height;
        Color color;
        double[] xs = new double[7];
        double[] ys = new double[7];
        int[] xarray = new int[7];
        int[] yarray = new int[7];

        public void setColor(Color color) {
            this.color = color;
        }

        public ThickArrow(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            
            xs[0] = x;
            ys[0] = (y + 0.5 * height);
            xs[1] = (x +  (width - 0.7 * height));
            ys[1] = ys[0];
            xs[2] = xs[1];
            ys[2] = (y + height * 0.9);
            xs[3] = x + width;
            ys[3]= y;
            xs[4] = xs[2];
            ys[4] = (y - height * 0.9);
            xs[5] = xs[4];
            ys[5] = (y - 0.5 * height);
            xs[6] = x;
            ys[6] = ys[5];
        }
        public void update(double xfactor,double yfactor){
            for (int i = 0; i < 7; i++) {
                xarray[i] = (int) (xfactor * xs[i]);
                yarray[i] = (int) (yfactor * ys[i]);
            }
        }
        public void draw(Graphics2D g) {
            Color color1 = g.getColor();
            g.setColor(color);
            
            g.drawPolygon(xarray, yarray, 7);
            g.fillPolygon(xarray, yarray, 7);
            g.setColor(color1);
        }
    }
