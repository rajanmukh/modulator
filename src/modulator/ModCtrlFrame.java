/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulator;

import Utility.Frame.Intfield;
import Utility.Frame.Intxfield;
import Utility.Frame.Packet;
import Utility.Frame.Status;

/**
 *
 * @author Istrac
 */
public class ModCtrlFrame extends Packet {

    Intfield txID;
    Intfield protID;
    Intfield bytecount;
    Intfield slaveaddr;
    Intfield commandcode;
    Intfield writelocation;
    Intfield noOfRegisters;
    Intfield noOfBytesToWrite;
    Intfield switch1;
    Intfield switch2;
    Intfield spare1;
    Intfield spare2;
    Intxfield filamentVoltage;
    Intxfield cathodeVoltage;
    Intxfield beamOnVoltage;
    Intxfield collectorVoltage;
    Intxfield PRF;
    Intxfield pulsewidth;    
    Intfield warmUpTimer;
    byte[] bytearr;

    public ModCtrlFrame() {
        super(19);
        int index = 0;
        field[index++] = txID = new Intfield("txID", 2);
        field[index++] = protID = new Intfield("protID", 2);
        field[index++] = bytecount = new Intfield("bytecount", 2);
        field[index++] = slaveaddr = new Intfield("slaveaddr", 1);
        field[index++] = commandcode = new Intfield("commandcode", 1);
        field[index++] = writelocation = new Intfield("writelocation", 2);
        field[index++] = noOfRegisters = new Intfield("noOfRegisters", 2);
        field[index++] = noOfBytesToWrite = new Intfield("noOfBytesToWrite", 1);
        field[index++] = switch1 = new Intfield("switch1", 2);
        field[index++] = switch2 = new Intfield("switch2", 2);
        field[index++] = spare1 = new Intfield("spare1", 4);
        field[index++] = filamentVoltage = new Intxfield("filamentVoltage", 2,1000);
        field[index++] = cathodeVoltage = new Intxfield("cathodeVoltage", 2,-100);        
        field[index++] = beamOnVoltage = new Intxfield("beamOnVoltage", 2,-100);
        field[index++] = collectorVoltage = new Intxfield("collectorVoltage", 2,-1000);
        field[index++] = PRF = new Intxfield("PRF", 2,10);
        field[index++] = pulsewidth = new Intxfield("pulsewidth", 2,10);
        field[index++] = warmUpTimer = new Intfield("warmUpTimer", 2);
        field[index++] = spare2 = new Intfield("spare2", 4);
        
        
    }

    public byte[] getPacket() {
        super.getDataBytes(bytearr);
        return bytearr;
    }

    public final void setMains(int stat) {

        if (stat == Status.ON) {
            switch1.setValue(2);
        } else if (stat == Status.OFF) {
            switch1.setValue(1);
        }
    }

    public void setHV(int stat) {

        if (stat == Status.ON) {
            switch1.setBit(2);
        } else if (stat == Status.OFF) {
            switch1.resetBit(2);
        }
    }

    public void setModulator(int stat) {

        if (stat == Status.ON) {
            switch1.setBit(3);
        } else if (stat == Status.OFF) {
            switch1.resetBit(3);
        }
    }

    public void setReset(boolean flag) {
        setHV(Status.OFF);
        setModulator(Status.OFF);
        if (flag) {
            switch1.setBit(4);
        } else {
            switch1.resetBit(4);
        }
    }

    public void setExt() {
        switch1.setBit(5);
    }

    public void setInt() {
        switch1.resetBit(5);
    }

//    public void emergencyStop(boolean flag) {
//        if (flag) {
//            ON_OFF.setBit(6);
//        } else {
//            ON_OFF.resetBit(6);
//        }
//    }

}
