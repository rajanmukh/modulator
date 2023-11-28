/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphicElements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;

/**
 *
 * @author rajan
 */
public class Rectangle {

    public static final int TOP = 0, RIGHT = 1, BOTTOM = 2, LEFT = 3, MIDDLE = 4;
    public static final int CONT = 0, DASH = 1;
    public Text label;
    int normx, normy, normwidth, normheight;
    int x, y, width, height;
    int outline = CONT;
    private Color fillcolor;
    private boolean rounded = true;
    private int textanchor = MIDDLE;
    private final int rectLabelSize = 9;
    private boolean Dim3 = false;
    private boolean gradientcolor = false;
    private float stroke = 4.0f;

    public void setStroke(float stroke) {
        this.stroke = stroke;
    }

    public void setGradientcolor(boolean gradientcolor) {
        this.gradientcolor = gradientcolor;
    }

    public void setDim3(boolean Dim3) {
        this.Dim3 = Dim3;
        rounded = false;
    }
//        double xfactor,yfactor;
    private boolean raised=true;

    public void setRaised(boolean raised) {
        this.raised = raised;
    }

    public void setTextanchor(int textanchor) {
        this.textanchor = textanchor;
        this.setTextLoc();
    }

    public boolean isRounded() {
        return rounded;
    }

    public void setRounded(boolean rounded) {
        this.rounded = rounded;
        Dim3 = false;
    }

    public void setFillcolor(Color fillcolor) {
        this.fillcolor = fillcolor;
    }

    public Color getFillcolor() {
        return fillcolor;
    }

    public void settextcolor(Color txtcolor) {
        label.setColor(txtcolor);
    }

    public void setOutline(int outline) {
        this.outline = outline;
    }

    public Rectangle(String name) {
        this.normx = 0;
        this.normy = 0;
        this.normwidth = 0;
        this.normheight = 0;
        this.fillcolor = Color.yellow;
        label = new Text(name, rectLabelSize);
        label.setColor(Color.BLUE);
    }

    public Rectangle(String name, int x, int y, int width, int height) {
        this.normx = x;
        this.normy = y;
        this.normwidth = width;
        this.normheight = height;
        this.fillcolor = Color.yellow;
        label = new Text(name, rectLabelSize);
        label.setColor(Color.BLUE);
        setTextLoc();
    }

    public void update(double xfactor, double yfactor) {
//            this.xfactor=xfactor;
//            this.yfactor=yfactor;
        this.x = (int) (normx * xfactor);
        this.y = (int) (normy * yfactor);
        this.width = (int) (normwidth * xfactor);
        this.height = (int) (normheight * yfactor);
        label.update(xfactor, yfactor);
        //double factor = (xfactor < yfactor) ? xfactor : yfactor;
//        int fontsize=(int) (factor / 3.5 * rectLabelSize);
//        label.setfontSize(fontsize);
        //int noOfLines = label.text.length;
        //double spBtnLn = ((0.9*this.height) / noOfLines);
        //label.setSpbnln(spBtnLn);
    }

    public void defineLimites(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public boolean isContaining(Point p) {
        return ((p.x > this.x) && (p.x < (this.x + this.width)) && (p.y > this.y) && (p.y < this.y + this.height));
    }

    public Point getContactPoint(int side) {
        return getContactPoint(side, 0.5);
    }

    public Point getContactPoint(int side, double location) {
        Point p = new Point();
        switch (side) {
            case TOP:
                p.x = normx + (int) (location * normwidth);
                p.y = normy;
                break;
            case RIGHT:
                p.x = normx + normwidth;
                p.y = normy + (int) (location * normheight);
                break;
            case BOTTOM:
                p.x = normx + (int) (location * normwidth);
                p.y = normy + normheight;
                break;
            case LEFT:
                p.x = normx;
                p.y = normy + (int) (location * normheight);
        }
        return p;
    }

    private void setTextLoc() {
        Point loc = new Point(0, 0);
        int len = 0;
        for (int i = 0; i < label.text.length; i++) {
            int temp = label.text[i].length();
            if (len < temp) {
                len = temp;
            }
        }
        if (textanchor == MIDDLE) {
            loc.x = (int) (normx + (normwidth / 2.0) - (len * 0.04 * rectLabelSize));
        } else {
            loc.x = (int) (normx + (normwidth / 10.0));
        }
        loc.y = normy + (int) (normheight * 0.6);
        label.setLoc(loc);
    }

    public void draw(Graphics2D g) { //To change body of generated methods, choose Tools | Templates.
        Stroke stroke1 = g.getStroke();
        Color color = g.getColor();
        Paint paint = g.getPaint();
        int arcDim = 10;
        
        if (outline == DASH) {
            g.setStroke(new BasicStroke(this.stroke, 0, 1, 10.0f, new float[]{1.0f}, 0.0f));
        } else {

            g.setStroke(new BasicStroke(this.stroke, 0, 1, 10.0f));
        }

        if (rounded) {

            g.drawRoundRect(x, y, width, height, arcDim, arcDim);

        } else if (Dim3) {

            Color brighter = this.getFillcolor().brighter();
            Color darker = this.getFillcolor().darker();
            g.setColor(raised ? brighter : darker);
            //drawLine(x, y, x, y + height);
            g.fillRect(x, y, 1, height + 1);
            //drawLine(x + 1, y, x + width - 1, y);
            g.fillRect(x + 1, y, width - 1, 1);
            g.setColor(raised ? darker : brighter);
            //drawLine(x + 1, y + height, x + width, y + height);
            g.fillRect(x + 1, y + height, width, 1);
            //drawLine(x + width, y, x + width, y + height - 1);
            g.fillRect(x + width, y, 1, height);

        } else {
            g.drawRect(x, y, width, height);
        }
        if (this.gradientcolor) {
            GradientPaint fill = new GradientPaint(x, y + height, this.getFillcolor().darker(), x, y, this.getFillcolor().brighter());
            g.setPaint(fill);
        } else {
            g.setColor(this.getFillcolor());
        }
        if (rounded) {

            g.fillRoundRect(x, y, width, height, arcDim, arcDim);
        } else if (Dim3) {

            Color brighter = this.getFillcolor().brighter();
            Color darker = this.getFillcolor().darker();

            if (!raised) {
                g.setColor(darker);
            } else if (paint != color) {
                g.setColor(color);
            }
            g.fillRect(x + 1, y + 1, width - 2, height - 2);
            g.setColor(raised ? brighter : darker);
            //drawLine(x, y, x, y + height - 1);
            g.fillRect(x, y, 1, height);
            //drawLine(x + 1, y, x + width - 2, y);
            g.fillRect(x + 1, y, width - 2, 1);
            g.setColor(raised ? darker : brighter);
            //drawLine(x + 1, y + height - 1, x + width - 1, y + height - 1);
            g.fillRect(x + 1, y + height - 1, width - 1, 1);
            //drawLine(x + width - 1, y, x + width - 1, y + height - 2);
            g.fillRect(x + width - 1, y, 1, height - 1);

        } else {

            g.fillRect(x, y, width, height);
        }
//                

        g.setStroke(stroke1);
        g.setColor(color);
        g.setPaint(paint);
        label.draw(g);
    }

    public void settext(String txt) {
        label.settext(txt);
    }
}
