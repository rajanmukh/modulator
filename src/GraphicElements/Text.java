/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphicElements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author rajan
 */
public class Text {

    String[] text;
    private Point normloc, currLoc;
    int fontsize;
    double spBtnLn;
    int fonttype = Font.BOLD;

    public void setFonttype(int fonttype) {
        this.fonttype = fonttype;
    }

    public void setSpbnln(double spbnln) {
        this.spBtnLn = spbnln;
    }

    public void setfontSize(int size) {
        this.fontsize = size;
    }

    Color color = Color.BLACK;
    double xfactor, yfactor;
    private double factor;

    public void setColor(Color color) {
        this.color = color;
    }

    public Text(String text, int size, Point p) {
        parseIntoLines(text);
        this.fontsize = size;
        normloc = p;
        currLoc = new Point(0, 0);
    }

    public Text(String text, int size) {
        parseIntoLines(text);
        this.fontsize = size;
        currLoc = new Point(0, 0);
    }

    public void update(double xf, double yf) {
        xfactor = xf;
        yfactor = yf;
        currLoc.x = (int) (xfactor * normloc.x);
        currLoc.y = (int) (yfactor * normloc.y);
        spBtnLn = 1.6*fontsize;
    }

    public void draw(Graphics2D g) {
        Color prevcolor = g.getColor();
        g.setColor(color);

        g.setFont(new Font("DejaVu Sans", fonttype, fontsize));
        int noOfLines = text.length;

        for (int i = 0; i < noOfLines; i++) {
            g.drawString(text[i], currLoc.x, currLoc.y + (int) ((i - ((noOfLines - 1) / 2.0)) * spBtnLn));
        }
        g.setColor(prevcolor);
    }

    public void setLoc(Point loc) {
        normloc = loc;
    }

    private void parseIntoLines(String txt) {
        text = txt.split(":");
    }

    public void settext(String txt) {
        parseIntoLines(txt);
    }
}
