/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility.Frame;

import Utility.Frame.Intxfield;

/**
 *
 * @author Istrac
 */
public class Intxsfield extends Intxfield {

    double shift;

    public Intxsfield(String name, int len, double factor, double shift) {
        super(name, len, factor);
        this.shift = shift;
    }

    @Override
    public void setValue(String m) {
        double temp;
        try {
            temp = Double.parseDouble(m);
        } catch (NumberFormatException e) {
            temp = 0;
        }
        long temp2 = (long) ((temp+shift) * factor);
        long rem, div = temp2;
        for (int i = 0; i < size; i++) {
            rem = div % 256;
            div = div / 256;
            value[i] = (byte) rem;
        }
    }

    @Override
    public void setValueWithDecimal(double d) {
        super.setValue((int) ((d+shift) * factor));
    }

    @Override
    public double getValueWithDecimal() {
        return ((getValue() / factor)-shift);
    }

    @Override
    public String getValueAsString() {
        double temp = getValueWithDecimal();
        String s = String.valueOf(temp);
        return s;
    }
}
