/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility.Frame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author rajan
 */
public class SH {

    private static int BIG = 0, LITTLE = 1;
    private int endianness = BIG;
    private byte[] arr;
    private final int[] mask = {1, 3, 7, 15, 31, 63, 127, 255};
    private int sizeOfBuffer;
    private final String filePath;
    private Document doc;
    private boolean uninitialized = true;

    public abstract class field {

        String name;
        int byteIndex, bitIndex, size;

        public abstract String getText();

        public abstract int getStatus();

        public String getName() {
            return name;
        }

        public abstract int getValue();

        public int getByteIndex() {
            return byteIndex;
        }

        public int getSize() {
            return size;
        }

        public String getValueAsString() {
            return null;
        }
    }

    public class Discreet1 extends field {

        String aText;
        int aValue;
        int errValue;

        @Override
        public String getText() {
            int temp = arr[byteIndex] >> bitIndex;
            int currValue = temp & 1;
            return (currValue == aValue) ? aText : "NOT " + aText;
        }

        public Discreet1(String name, int byteIndex, int bitIndex, String aText, int aValue, int errValue) {
            this.name = name;
            this.byteIndex = byteIndex;
            this.bitIndex = bitIndex;
            this.aText = aText;
            this.aValue = aValue;
            this.errValue = errValue;
            size = 1;
        }

        @Override
        public int getStatus() {
            int currValue = getValue();
            return (currValue != errValue) ? Status.FINE : Status.ERROR;
        }

        @Override
        public int getValue() {
            int temp = arr[byteIndex] >> bitIndex;
            int currValue = temp & 1;
            return currValue;
        }
    }

    public class Discreet2 extends field {

        final int PROBABLYFINE = -1;
        String[] textlist;
        int[] valuelist;
        int currValue;
        int[] warnVals, errVals;

        public Discreet2(String name, int byteIndex, int bitIndex, int size, String[] textlist, int[] valuelist, int[] errVal, int[] warnVal) {
            this.name = name;
            this.byteIndex = byteIndex;
            this.bitIndex = bitIndex;
            this.size = size;
            this.textlist = textlist;
            this.valuelist = valuelist;
            this.warnVals = warnVal;
            this.errVals = errVal;
        }

        @Override
        public String getText() {
            String text = "UNDEFINED";//if the value is not found in the following search that indicates error
            for (int i = 0; i < valuelist.length; i++) {
                if (currValue == valuelist[i]) {
                    text = textlist[i];
                    break;
                }
            }
            return text;
        }

        @Override
        public int getStatus() {
            try {
                currValue = getValue();
            } catch (Exception ex) {
                System.out.println(name);
            }

            if ((errVals.length == 0) && (warnVals.length == 0)) {
                return Status.DONTCARE;
            } else {
                int status = Status.WARNING;

                for (int i = 0; i < errVals.length; i++) {
                    if (currValue == errVals[i]) {
                        status = Status.ERROR;
                        return status;
                    }
                }

                for (int i = 0; i < warnVals.length; i++) {
                    if (currValue == warnVals[i]) {
                        status = Status.WARNING;
                        return status;
                    }
                }

                for (int i = 0; i < valuelist.length; i++) {
                    if (currValue == valuelist[i]) {
                        status = Status.FINE;
                    }
                }
                return status;
            }

        }

        @Override
        public int getValue() {
            int temp = arr[byteIndex] >> bitIndex;
            currValue = temp & mask[size - 1];
            return currValue;
        }
    }

    public class A2D extends field {
        static final int UNSIGNED=1,SIGNED=2;
        double factor, shift;
        double min, max;
        private String formatStr;
        int range;
        int sign=UNSIGNED;
        public A2D(String name, int byteIndex, int size, double factor, double shift) {
            this.name = name;
            this.byteIndex = byteIndex;
            this.size = size;
            this.factor = factor;
            this.shift = shift;
            double div = Math.abs(factor);
            int decPlaces = 0;
            
            while (div >= 10) {
                div = div / 10;
                decPlaces++;
            }
            formatStr = "%." + decPlaces + "f";
        }

        public void setlimit(double min, double max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public String getText() {
            String value;
            if (factor == 1) {
                value = String.valueOf(getValue());
            } else {
                value = String.valueOf(getValueWithDecimal());
            }
            return value;
        }

        @Override
        public int getStatus() {
            return Status.DONTCARE;
        }

        @Override
        public int getValue() {
            int byteval, temp = 0, value;
            int signbit=UNSIGNED;
            if (endianness == LITTLE) {
                for (int i = size - 1; i >= 0; i--) {
                    byteval = (arr[byteIndex + i] < 0) ? arr[byteIndex + i] + 256 : arr[byteIndex + i];
                    temp = 256 * temp + byteval;
                }
                if(sign==SIGNED){
                    signbit=(arr[byteIndex+size-1]>>7)&1;;
                }
            } else {
                for (int i = 0; i < size; i++) {
                    byteval = (arr[byteIndex + i] < 0) ? arr[byteIndex + i] + 256 : arr[byteIndex + i];
                    temp = 256 * temp + byteval;
                }
                if(sign==SIGNED){
                    signbit=(arr[byteIndex]>>7)&1;
                }
            }
            value = temp;
            if(sign==SIGNED){
            
            if(signbit==1){
                value=value-range;
            }
        }
            return value;
        }
        public void setSign(int sign){
            this.sign=sign;
            range=(int)Math.pow(256, size);
        }
        public double getValueWithDecimal() {
            double m;
            int value = getValue();
            if (factor == 1) {
                m = value - shift;
            } else {
                m = (((double) value) / factor) - shift;
            }
            return m;
        }

        @Override
        public String getValueAsString() {
            return String.format(formatStr, getValueWithDecimal());
        }

        public void setPrecision(int no) {
            formatStr = "%." + no + "f";
        }
    }
    public ArrayList<field> fields;

    public SH(String filepath) {
        fields = new ArrayList<>();
        filePath = filepath;

        try {
            File f = new File(filepath);
            DocumentBuilderFactory d = DocumentBuilderFactory.newInstance();
            DocumentBuilder a = d.newDocumentBuilder();
            doc = a.parse(f);
            Element root = doc.getDocumentElement();
            NodeList n = root.getElementsByTagName("field");
            int count = n.getLength();
            int size;
            int bitpos = 0, byteIndex, bitIndex;
            for (int i = 0; i < count; i++) {

                Element ele = (Element) n.item(i);
                String nameStr = null;
                Node nameNode = ele.getElementsByTagName("name").item(0);
                if (nameNode != null) {
                    nameStr = nameNode.getTextContent();
                }
                Node sizeNode = ele.getElementsByTagName("size").item(0);
                Node typeNode = ele.getElementsByTagName("type").item(0);
                String typeStr = "";
                if (typeNode != null) {
                    typeStr = typeNode.getTextContent();
                }

                NodeList t;
                if (sizeNode == null) {
                    size = 1;
                } else {
                    size = Integer.parseInt(sizeNode.getTextContent());
                }

                byteIndex = bitpos / 8;
                bitIndex = bitpos % 8;
                if (!typeStr.equalsIgnoreCase("spare")) {
                    if (size < 8) {//
                        t = ele.getElementsByTagName("text");
                        int len = t.getLength();
                        String[] texts = new String[len];
                        int[] values = new int[len];
                        for (int j = 0; j < len; j++) {
                            Element h = (Element) t.item(j);
                            texts[j] = h.getTextContent();
                            values[j] = Integer.parseInt(h.getAttribute("x"));
                        }
                        int[] errVals = null, warnVals = null;
                        t = ele.getElementsByTagName("error");
                        if (t != null) {
                            errVals = new int[t.getLength()];
                            for (int j = 0; j < t.getLength(); j++) {
                                Element h = (Element) t.item(j);
                                errVals[j] = Integer.parseInt(h.getTextContent());
                            }
                        }

                        t = ele.getElementsByTagName("warning");
                        if (t != null) {
                            warnVals = new int[t.getLength()];
                            for (int j = 0; j < t.getLength(); j++) {
                                Element h = (Element) t.item(j);
                                warnVals[j] = Integer.parseInt(h.getTextContent());
                            }
                        }
                        if ((size == 1) && (texts.length == 1)) {
                            fields.add(new Discreet1(nameStr, byteIndex, bitIndex, texts[0], values[0], errVals[0]));

                        } else {
                            fields.add(new Discreet2(nameStr, byteIndex, bitIndex, size, texts, values, errVals, warnVals));

                        }
                    } else {

                        NodeList list = ele.getElementsByTagName("factor");
                        Node factorNode = list.item(0);
                        double factor = 1;
                        if (factorNode != null) {
                            String factorStr = factorNode.getTextContent();
                            factor = Double.parseDouble(factorStr);
                        }
                        list = ele.getElementsByTagName("shift");
                        Node shiftNode = list.item(0);
                        double shift = 0;
                        if (shiftNode != null) {
                            String shiftStr = shiftNode.getTextContent();
                            shift = Double.parseDouble(shiftStr);
                        }
                        list = ele.getElementsByTagName("min");
                        Node minNode = list.item(0);
                        double min = 0;
                        if (minNode != null) {
                            String minStr = minNode.getTextContent();
                            min = Double.parseDouble(minStr);
                        }
                        list = ele.getElementsByTagName("max");
                        Node maxNode = list.item(0);
                        double max = 0;
                        if (maxNode != null) {
                            String maxStr = maxNode.getTextContent();
                            max = Double.parseDouble(maxStr);
                        }
                        list = ele.getElementsByTagName("precision");
                        Node precisionNode = list.item(0);
                        int precision = 0;
                        if (precisionNode != null) {
                            String precisionStr = precisionNode.getTextContent();
                            precision = Integer.parseInt(precisionStr);
                        }
                        list = ele.getElementsByTagName("sign");
                        Node signNode = list.item(0);
                        String signStr = null;
                        if (signNode != null) {
                            signStr = signNode.getTextContent();
                        }
                        A2D a2d = new A2D(nameStr, byteIndex, size / 8, factor, shift);
                        if (min != 0 || max != 0) {
                            a2d.setlimit(min, max);
                        }
                        if (precision != 0) {
                            a2d.setPrecision(precision);
                        }
                        if (signStr != null) {
                            if (signStr.equalsIgnoreCase("signed")) {
                                a2d.setSign(A2D.SIGNED);
                            }
                        }
                        fields.add(a2d);
                    }
                }
                bitpos += size;
            }
            sizeOfBuffer = bitpos / 8;
            if ((bitpos % 8) != 0) {
                sizeOfBuffer++;
            }
            arr = new byte[sizeOfBuffer];

        } catch (SAXException ex) {
        } catch (IOException ex) {
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SH.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setArr(byte[] arr) {

        System.arraycopy(arr, 0, this.arr, 0, sizeOfBuffer);
        uninitialized = false;
    }

    public int getStatus() {
        int status = Status.FINE;
        int noOfFields = fields.size();
        for (int i = 0; i < noOfFields; i++) {
            field f = fields.get(i);
            if (f.getSize() != 0) {
                int elementStatus = f.getStatus();
                if (elementStatus == Status.ERROR) {
                    status = Status.ERROR;
                    break;
                } else if (elementStatus == Status.WARNING) {
                    status = Status.WARNING;
                }
            }
        }
        return status;
    }

    public byte[] getArr() {
        return arr;
    }

    public static void main(String[] arg) throws InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SAXException, IOException {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        String filepath = "data/StatDefs/Servo.xml";
        SH a = new SH(filepath);
    }
}
