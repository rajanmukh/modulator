/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author Istrac
 */
public class MyInputVerifier extends InputVerifier {

    VerifyListener listener;
    double min, max;
    Border border;

    public MyInputVerifier(double min, double max) {
        this.min = min;
        this.max = max;
        border = (new JTextField()).getBorder();
    }

    @Override
    public boolean verify(JComponent input) {

        boolean retvalue = false;
        JTextField t = (JTextField) input;
        String text = t.getText();
        double val;
        try {
            val = Double.parseDouble(text);

            if ((val > max) || (val < min)) {
                t.setForeground(Color.red);
                t.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED));                
                JOptionPane.showMessageDialog(null, "value outside valid range from " + (int) min + " to " + (int) max);
            } else {
                t.setForeground(Color.BLACK);
                t.setBorder(border);
                retvalue = true;
                if (listener != null) {
                    listener.actionToDo();
                }
            }

        } catch (NumberFormatException ex) {
            t.setForeground(Color.red);
            t.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED));
        }
        
        return retvalue;
    }

    public void setMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public void addListener(VerifyListener l) {
        this.listener = l;
    }

}
