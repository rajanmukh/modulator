/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility.Frame;

import Utility.XMLReadWrite;
import java.io.File;

/**
 *
 * @author Istrac
 */
public class Packet {

    public final static int BIG = 1, LITTLE = 2;
    public Field[] field;
    private final int noOfFields;
    protected int Endianness = BIG;

    public Packet(int num) {
        noOfFields = num;
        field = new Field[num];
    }

    public void getDataBytes(byte[] arr) {

        byte[] temp;
        int startIndex = 0;
        for (int i = 0; i < noOfFields; i++) {
            field[i].pos = startIndex;
            int len = field[i].getSize();
            temp = field[i].getValueAsByteArr();
            if (Endianness == BIG) {
                for (int j = 0; j < len; j++) {
                    arr[startIndex + j] = temp[len - 1 - j];
                }
            } else {//LITTLE
                System.arraycopy(temp, 0, arr, startIndex, len);
            }
            startIndex += len;
        }
    }

    public void getchararray(byte[] arr) {

        byte[] temp;
        int startIndex = 0;
        for (int i = 0; i < noOfFields; i++) {
            field[i].pos = startIndex;
            int len = 2 * field[i].getSize();
            temp = field[i].getAsCharArray();
            System.arraycopy(temp, 0, arr, startIndex, len);
            startIndex += len;
        }
    }

    public void setFromArray(byte[] m) {
        setFromArray(m,0);
    }

    public void setFromArray(byte[] m, int offset) {
        byte[] temp;
        int startIndex = offset;
        for (int i = 0; i < noOfFields; i++) {
            int len = field[i].getSize();
            temp = new byte[len];if (Endianness == BIG) {
                for (int j = 0; j < len; j++) {
                    temp[len - 1 - j]=m[startIndex+j];
                }
            } else {//LITTLE
                System.arraycopy(m, startIndex, temp,0, len);
            }
            field[i].setValue(temp);
            startIndex += len;
        }
    }
    public void loadFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            XMLReadWrite rwagent = new XMLReadWrite(filePath);
            String str;
            for (int i = 0; i < noOfFields; i++) {
                str = rwagent.getTextByTag(field[i].getName());
                if (str != null) {
                    field[i].setValue(str);
                }
            }
        }
    }
    public void printToFile(String filename) {
        XMLReadWrite rwagent = new XMLReadWrite(filename);

        for (int i = 0; i < noOfFields; i++) {
            String str1 = field[i].getName();
            String str2 = field[i].getValueAsString();
            rwagent.Write(str1, str2);
        }
        rwagent.printToFile();
    }
}
