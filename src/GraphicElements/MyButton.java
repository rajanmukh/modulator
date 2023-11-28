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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

/**
 *
 * @author rajan
 */
public class MyButton extends JButton{

    
    
    public Text label;
    Point loc =new Point(0,0);
    int fontSize=9;
    public MyButton(String str){
        label=new Text(str,fontSize);
        
       
    }
    public void setFontSize(int fontSize){
       this.fontSize =fontSize;
       label.setfontSize(fontSize);
    }

   

    
    
    
    public void setforegroundColor(Color fg) {
         //To change body of generated methods, choose Tools | Templates.
        label.setColor(fg);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        Dimension d = this.getSize();
        int w = d.width;
        int h = d.height;

        int len = 0;
        int noOfLines = label.text.length;
        for (int i = 0; i < noOfLines; i++) {
            int temp = label.text[i].length();
            if (len < temp) {
                len = temp;
            }
        }
        loc.x = (int) (0 + (w / 2.0) - (len * 0.38 * fontSize));
        loc.y =  (int) (h * 0.6);
        label.setLoc(loc);
        label.update(1, 1);            
        label.draw((Graphics2D)g);
    }
    
}
