/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphicElements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author rajan
 */
public class MultiLineText extends javax.swing.JPanel {
    
    private int fontsize = 9;
    static final int MIDDLE = 4;
    private int textanchor = MIDDLE;
    Point loc;
    
    private boolean outlined=true;

    public void setOutlined(boolean outlined) {
        this.outlined = outlined;
    }
    public Rectangle rectangle;
    
    private Color fgcolor;
    
    public void setTextanchor(int textanchor) {
        this.textanchor = textanchor;
    }

    public void setFontsize(int fontsize) {
        this.fontsize = fontsize;
        rectangle.label.setfontSize(fontsize);
    }

    public void setFonttype(int fonttype) {
        
        rectangle.label.setFonttype(fonttype);
    }

    /**
     * Creates new form MultiLineText
     *
     * @param str
     * @param x
     * @param y
     */
    public MultiLineText(String str) {
        
        this.setPreferredSize(new Dimension(0, 0));
        setLayout(null);
        rectangle = new Rectangle(str);
        rectangle.setGradientcolor(true);
        loc = new Point(0, 0);
        fgcolor = Color.BLACK;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //setBackground(colors[BACKGROUND]);
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = this.getSize();
//        double xfactor = d.width / 20.0;
//        double yfactor = d.height / 10.0;
        int w = d.width;
        int h = d.height;

        //int size = (d.width < 2 * d.height) ? d.width : 2 * d.height;
        //rectLabelSize = (int) (size * (13 / 870.0));
        int len = 0;
        int noOfLines = rectangle.label.text.length;
        for (int i = 0; i < noOfLines; i++) {
            int temp = rectangle.label.text[i].length();
            if (len < temp) {
                len = temp;
            }
        }
        if (textanchor == MIDDLE) {
            loc.x = (int) (0 + (w / 2.0) - (len * 0.35 * fontsize));
        } else {
            loc.x = (int) (0 + (w / 10.0));
        }
        loc.y =  (int) (h * 0.6);
        if (outlined) {
            rectangle.defineLimites(2, 2, w - 4, h - 4);
            rectangle.label.setLoc(loc);
            //outline.label.setSpbnln((0.95*h) / noOfLines);
            rectangle.label.update(1, 1);            
            rectangle.draw(g2d);
        } else {
            rectangle.label.setLoc(loc);
            //outline.label.setSpbnln((0.95*h) / noOfLines);
            rectangle.label.update(1, 1);            
            rectangle.label.draw(g2d);
        }

//        g2d.setStroke(new BasicStroke(1, 0, 2, 10, null, 0));
//        for (int i = 0; i < 2; i++) {
//            connections[i].update();
//            connections[i].draw(g2d);
//        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
