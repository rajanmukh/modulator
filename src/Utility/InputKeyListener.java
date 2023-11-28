/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author Istrac
 */
public class InputKeyListener extends KeyAdapter {

    String temp;
    boolean stopEntry = false;
 Border border;
    public InputKeyListener() {
        super();
        border = (new JTextField()).getBorder();
    }
    

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.                    
        JTextField t = (JTextField) (e.getSource());
        String temp1 = null;
        try {
            temp1 = t.getText();
            if (temp1 != "") {
                double value = Double.parseDouble(temp1);
            }
            stopEntry = false;
            temp = temp1;
            t.setForeground(Color.BLACK);
            t.setBorder(border);

        } catch (NumberFormatException ex) {
            if ("".equals(temp1)) {
                stopEntry = false;
                t.setForeground(Color.black);
                t.setBorder(border);
            } else {
                if ((!stopEntry)) {
                    stopEntry = true;
                    temp = temp1;
                    t.setForeground(Color.red);
                    t.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED));
                }
                t.setText(temp);
            }
        }
    }
}
