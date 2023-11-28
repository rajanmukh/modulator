/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility.Frame;

/**
 *
 * @author Istrac
 */
public class Intfield extends Field {
    static final int UNSIGNED=1,SIGNED=2;
    byte[] value;
    double min, max;
    double changethreshold;
    int sign=UNSIGNED;
    int range;
    public Intfield() {
    }

    public Intfield(String name, int len) {
        super.name = name;
        size = len;
        value = new byte[size];
    }
    public void setSign(int sign){
        this.sign=sign;
        range=(int)Math.pow(256, size);
    }
    @Override
    public byte[] getValueAsByteArr() {        
        return value;
    }

    @Override
    public String getValueAsString() {
        return String.valueOf(getValue());
    }

    @Override
    public byte[] getAsCharArray() {
        byte[] arr = new byte[2 * size];
        for (int i = size - 1; i >= 0; i--) {
            int val = value[i];
            if (val < 0) {
                val += 256;
            }
            int uppernibble = val / 16;
            int lowernibble = val % 16;
            int j = size - 1 - i;
            arr[2 * j] = getAsciibyte(uppernibble);
            arr[2 * j + 1] = getAsciibyte(lowernibble);

        }
        return arr;
    }

    public int[] unpackbits() {
        int[] m = new int[size * 8];
        for (int i = 0; i < m.length; i++) {
            int bytepos = i / 8;
            int bitpos = i % 8;
            m[i] = (value[bytepos] >> bitpos) & (0x1);
        }
        return m;
    }

    @Override
    public void setValue(String m) {
        try {
            setValue(Integer.parseInt(m));
        } catch (NumberFormatException e) {
            setValue(0);
        }
    }

    @Override
    public void setValue(byte[] m) {
        System.arraycopy(m, 0, value, 0, size);
    }

    @Override
    public void setValue(byte[] m, int offset) {
        System.arraycopy(m, offset, value, offset, size);
    }

    public boolean isChangeSignificant(Field f) {
        double val = ((Intfield) f).getValue();
        double diff = getValue() - val;
        if (diff < 0) {
            return diff < -changethreshold;
        } else {
            return diff > changethreshold;
        }
    }

    public int getValue() {
        int retval = 0;
        for (int i = size - 1; i >= 0; i--) {
            retval = 256 * retval + ((value[i] < 0) ? value[i] + 256 : value[i]);
        }
        if(sign==SIGNED){
            int signbit=value[size - 1]&128;
            if(signbit==1){
                retval=retval-range;
            }
        }
        return retval;
    }

    public void setValueAsBCD(int m) {
        int rem = 0;
        int div = m;
        int[] temp = new int[size * 2];
        for (int i = 0; i < size * 2; i++) {
            rem = div % 10;
            div = div / 10;
            temp[i] = rem;
        }
        for (int i = size - 1; i >= 0; i--) {
            value[i] = (byte) (16 * temp[2 * i + 1] + temp[2 * i]);
        }
    }

    @Override
    public void setValue(int m) {
        int rem;
        int div = m;
        for (int i = 0; i < size; i++) {
            rem = div % 256;
            div = div / 256;
            value[i] = (byte) rem;
        }
    }

    public void packbits(int[] m) {
        for (int i = 0; i < size; i++) {
            value[i] = 0;
        }
        for (int i = m.length - 1; i >= 0; i--) {
            int bytepos = i / 8;
            int bitpos = i % 8;
            value[bytepos] = (byte) (value[bytepos] | (m[i] << bitpos));
        }
    }

    public void setBit(int index) {
        int[] bits = unpackbits();
        bits[index] = 1;
        packbits(bits);
    }

    public void resetBit(int index) {
        int[] bits = unpackbits();
        bits[index] = 0;
        packbits(bits);
    }

    private byte getAsciibyte(int nibble) {
        byte b;
        if (nibble < 10) {
            b = (byte) (nibble + 48);
        } else {
            b = (byte) (nibble + 55);
        }
        return b;
    }

    @Override
    public void setLimit(double min, double max) {
        this.min = min;
        this.max = max;
        changethreshold = 0.1 * (max - min);
    }

    @Override
    public int getStatus() {
        int val = getValue();
        return (val < min || val > max) ? Status.ERROR : Status.FINE;
    }
}
