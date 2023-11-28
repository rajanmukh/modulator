/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphicElements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Stroke;
import javax.swing.JPanel;

/**
 *
 * @author rajan
 */
public class Border extends javax.swing.JPanel {

    class Frametop extends JPanel {
        int[] xarr=new int[4];
        int[] yarr=new int[4];
        private Color fillcolor = Color.WHITE;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
            Dimension size = getSize();
            int w = size.width;
            int h = size.height;
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = g2d.getColor();
            Stroke stroke = g2d.getStroke();
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawLine(0, 4, w - h + 4, 4);
            g2d.drawLine(w - h + 4, 4, w, h);
            g2d.setStroke(stroke);
            g2d.setColor(fillcolor);
            xarr[0]=0;
            yarr[0]=4+1;
            xarr[3]=0;
            yarr[3]=h;
            xarr[2]=w-1;
            yarr[2]=h;
            xarr[1]=w-h+3;
            yarr[1]=4+1;
            g2d.fillPolygon(xarr, yarr, 4);
            g2d.setColor(color1);
        }

    }

    class Framebottom extends JPanel {

        private Color fillcolor = Color.WHITE;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
            Dimension size = getSize();
            int w = size.width;
            int h = size.height;
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = g2d.getColor();
            Stroke stroke = g2d.getStroke();
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawLine(0, h - 4, w, h - 4);
            g2d.setStroke(stroke);
            g2d.setColor(fillcolor);
            g2d.fillRect(0,0,w,h-5);
            g2d.setColor(color1);
        }
    }

    class Frameleft extends JPanel {

        private Color fillcolor = Color.WHITE;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
            Dimension size = getSize();
            int w = size.width;
            int h = size.height;
            int toph = top.getSize().height;
            int bottomh = bottom.getSize().height;
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = g2d.getColor();
            Stroke stroke = g2d.getStroke();
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawLine(4, w + 4, 4, h - 4);
            //g2d.drawLine(4, toph/2, w, toph/2);
            g2d.drawLine(4, h - 4, w, h - 4);
            g2d.drawArc(4, 4, 2 * w, 2 * w, 90, 90);
            g2d.setStroke(stroke);
            g2d.setColor(fillcolor);
            g2d.fillRect(4+1, w + 1, w - 4, h - w - 6);
            g2d.fillArc(4+1, 4+1, 2 * w-1, 2 * w-1, 90, 90);
            g2d.setColor(color1);

        }
    }

    class Frameright extends JPanel {
        int[] xarr=new int[4];
        int[] yarr=new int[4];
        private Color fillcolor = Color.WHITE;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
            Dimension size = getSize();
            int w = size.width;
            int h = size.height;
            int toph = top.getSize().height;
            int bottomh = bottom.getSize().height;
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = g2d.getColor();
            Stroke stroke = g2d.getStroke();
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawLine(0, toph, w - 4, toph + w - 4);
            g2d.drawLine(w - 4, toph + w - 4, w - 4, h - 4);
            g2d.drawLine(0, h - 4, w - 4, h - 4);
            g2d.setStroke(stroke);
            g2d.setColor(fillcolor);
            xarr[0]=0;
            yarr[0]=toph+1;
            xarr[3]=0;
            yarr[3]=h-4-1;
            xarr[2]=w-4-1;
            yarr[2]=h-4-1;
            xarr[1]=w-4-1;
            yarr[1]=toph + w-4+1;
            g2d.fillPolygon(xarr, yarr, 4);
            g2d.setColor(color1);
        }
    }

    /**
     * Creates new form Border
     *
     * @param panel
     */
    public Border(JPanel panel) {
        this.center = panel;
        initcomponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initcomponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.85;
        gridBagConstraints.weighty = 0.1;

        //gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        //gridBagConstraints.insets = new java.awt.Insets(100, 150, 100, 150);
        top = new Frametop();
        top.setPreferredSize(new Dimension(0, 0));
        add(top, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.85;
        gridBagConstraints.weighty = 0.05;

        //gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        //gridBagConstraints.insets = new java.awt.Insets(100, 150, 100, 150);
        bottom = new Framebottom();
        bottom.setPreferredSize(new Dimension(0, 0));
        add(bottom, gridBagConstraints);
        //////
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1;

        //gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        //gridBagConstraints.insets = new java.awt.Insets(100, 150, 100, 150);
        left = new Frameleft();
        left.setPreferredSize(new Dimension(0, 0));
        add(left, gridBagConstraints);
        /////
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.05;
        gridBagConstraints.weighty = 1;

        //gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        //gridBagConstraints.insets = new java.awt.Insets(100, 150, 100, 150);
        right = new Frameright();
        right.setPreferredSize(new Dimension(0, 0));
        add(right, gridBagConstraints);
        /////
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.85;

        //gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        //gridBagConstraints.insets = new java.awt.Insets(100, 150, 100, 150);
        center.setPreferredSize(new Dimension(0, 0));
        add(center, gridBagConstraints);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.

        //validate();
    }

    public void setOuterfillColor(Color bg) {
        left.setBackground(bg);
        right.setBackground(bg);
        top.setBackground(bg);
        bottom.setBackground(bg);
    }

    public void setInnerfillColor(Color bg) {
        left.fillcolor = bg;
        right.fillcolor = bg;
        top.fillcolor = bg;
        bottom.fillcolor = bg;
        center.setBackground(bg);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
private Frametop top;
    private Framebottom bottom;
    private Frameleft left;
    private Frameright right;
    private javax.swing.JPanel center;
    private float thickness = 4.0f;
    private Color color = Color.BLUE;

    public void setborderColor(Color color) {
        this.color = color;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }
}
