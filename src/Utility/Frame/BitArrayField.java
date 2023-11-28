/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility.Frame;

/**
 *
 * @author Istrac
 */
public class BitArrayField extends Field {

    byte[] value;

    public BitArrayField(String name, int len) {
        super.name = name;
        size = len;
        value = new byte[size];
    }

    @Override
    public byte[] getValueAsByteArr() {
        return value;
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

    public int[] unpackbits() {
        int[] m = new int[size * 8];
        for (int i = 0; i < m.length; i++) {
            int bytepos = i / 8;
            int bitpos = i % 8;
            m[i] = (value[bytepos] >> bitpos) & (0x1);
        }
        return m;
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

    @Override
    public void setValue(String m) {
        int[] bits=new int[size*8];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = (m.charAt(i)=='1')?1:0;
        }
        packbits(bits);
    }

    @Override
    public void setValue(byte[] m) {
        for (int i = 0; i < size; i++) {
            value[i] = m[size - 1 - i];
        }
    }

    @Override
    public void setValue(byte[] m, int offset) {
        for (int i = offset; i < offset + size; i++) {
            value[i] = m[size - 1 - i];
        }
    }

    @Override
    public String getValueAsString() {
        int[] bits = unpackbits();
        char[] str=new char[bits.length];
        for (int i = 0; i < bits.length; i++) {
            str[i]=(bits[i]==1)?'1':'0';
        }
        return String.valueOf(str);
    }
}
