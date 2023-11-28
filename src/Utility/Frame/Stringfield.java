/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility.Frame;

import java.util.Arrays;

/**
 *
 * @author Istrac
 */
public class Stringfield extends Field {

    String value = "NA";

    public Stringfield(String name, int n) {
        super.name = name;
        super.size = n;
    }

    @Override
    public byte[] getValueAsByteArr() {
        byte[] m = new byte[size];
        byte[] temp = value.getBytes();
        System.arraycopy(temp, 0, m, 0, temp.length);
        return m;
    }

    @Override
    public String getValueAsString() {
        return value;
    }

    @Override
    public void setValue(String m) {
        if (m != null) {
            value = m;
        }
    }

    @Override
    public void setValue(byte[] m) {
        int len=m.length;        
        int i = 0;
        while (m[i] != 0) {
            i++;
            if(i==len){
                break;
            }
        }
        value = new String(Arrays.copyOf(m, i));
    }

    @Override
    public void setValue(byte[] m, int offset) {
        int i = offset;
        while (m[i] != 0) {
            i++;
        }
        value = new String(Arrays.copyOf(m, i));
    }
}
