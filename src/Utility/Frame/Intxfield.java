/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility.Frame;

/**
 *
 * @author Istrac
 */
public class Intxfield extends Intfield {
    
    double factor;
    
    public Intxfield(String name, int len, double factor) {
        super(name, len);
        this.factor = factor;
    }
    
    @Override
    public void setValue(String m) {
        double temp;
        try {
            temp = Double.parseDouble(m);
        } catch (NumberFormatException e) {
            temp = 0;
        }
        long temp2 = (long) (temp * factor);
        long rem, div = temp2;
        for (int i = 0; i < size; i++) {
            rem = div % 256;
            div = div / 256;
            value[i] = (byte) rem;
        }
    }

    public void setValueWithDecimal(double d) {
        super.setValue((int) (d * factor));
    }

    public double getValueWithDecimal() {
        return (getValue() / factor);
    }
    
    @Override
    public String getValueAsString() {
        double temp = getValue() / ((double) factor);
        String s = String.valueOf(temp);
        return s;
    }

    @Override
    public int getStatus() {
        double val = getValueWithDecimal();        
        return (val < min || val > max) ? Status.ERROR : Status.FINE;
        
    }

    @Override
    public boolean isChangeSignificant(Field f) {
        double val = ((Intxfield) f).getValueWithDecimal();
        double diff = this.getValueWithDecimal() - val;
        if (diff < 0) {
            return diff < -changethreshold;
        } else {
            return diff > changethreshold;
        }
    }
}
