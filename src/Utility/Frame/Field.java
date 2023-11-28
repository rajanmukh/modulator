/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility.Frame;

/**
 *
 * @author Istrac
 */
public class Field {

    protected String name;
    protected int size;
    public int pos = 0;

    public byte[] getValueAsByteArr() {
        return null;
    }

    public String getValueAsString() {
        return null;
    }

    public void setValue(byte[] m) {
    }

    public void setValue(byte[] m, int offset) {
    }   

    public void setValue(int m) {
    }

    public void setValue(String m) {
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public byte[] getAsCharArray() {
        return null;
    }

    public void setLimit(double a, double b) {
    }

    public int getStatus() {
        return 0;
    }
}
