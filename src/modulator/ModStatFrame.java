/*
 * To change this template, choose Tools | Templates
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
public class ModStatFrame extends Packet {

    public Intfield txID;
    public Intfield protID;
    public Intfield bytecount;
    public Intfield slaveaddr;
    public Intfield commandcode;
    public Intfield noOfDataBytes;
    public Intfield status;
    public Intfield spare1;
    public Intfield faultStatus;
    public Intfield switch1;
    public Intfield switch2;
    public Intxfield filamentVoltage;
    public Intxfield filamentCurrent;
    public Intxfield beamOnVoltage;
    public Intxfield beamOFFVoltage;
    public Intxfield collectorVoltage;
    public Intxfield helixBodyCurrent;
    public Intxfield cathodeVoltage;
    public Intxfield cathodeCurrent;
    public Intxfield PRF;
    public Intxfield PW;
    public Intfield spare2;

    public ModStatFrame() {
        super(22);
        int index = 0;
        field[index++] = txID = new Intfield("txID", 2);
        field[index++] = protID = new Intfield("protID", 2);
        field[index++] = bytecount = new Intfield("bytecount", 2);
        field[index++] = slaveaddr = new Intfield("slaveaddr", 1);
        field[index++] = commandcode = new Intfield("commandcode", 1);
        field[index++] = noOfDataBytes = new Intfield("noOfDataBytes", 1);
        field[index++] = status = new Intfield("status", 2);
        field[index++] = spare1 = new Intfield("spare1", 2);
        field[index++] = faultStatus = new Intfield("faultStatus", 2);
        field[index++] = switch1 = new Intfield("switch1", 2);
        field[index++] = switch2 = new Intfield("switch2", 2);
        field[index++] = filamentVoltage = new Intxfield("filamentVoltage", 2, -1000);
        field[index++] = filamentCurrent = new Intxfield("filamentCurrent", 2, 1000);
        field[index++] = beamOnVoltage = new Intxfield("beamOnVoltage", 2, -100);
        field[index++] = beamOFFVoltage = new Intxfield("beamOFFVoltage", 2, -1000);
        field[index++] = collectorVoltage = new Intxfield("beamOFFVoltage", 2, -1000);
        field[index++] = helixBodyCurrent = new Intxfield("helixBodyCurrent", 2, 100);
        field[index++] = cathodeVoltage = new Intxfield("cathodeVoltage", 2, -100);
        field[index++] = cathodeCurrent = new Intxfield("cathodeCurrent", 2, 1000);
        field[index++] = PRF = new Intxfield("PRF", 2, 10);
        field[index++] = PW = new Intxfield("PW", 2, 10);
        field[index++] = spare2 = new Intfield("spare2", 10);
        txID.setValue(new byte[]{10, 1});
        protID.setValue(0);
        bytecount.setValue(43);
        slaveaddr.setValue(1);
        commandcode.setValue(3);
        noOfDataBytes.setValue(40);
    }

    public int getMainsStatus() {
        int value = status.getValue()&0x0400;
        if (value == 0x0400) {
            return Status.ON;
        } else {
            return Status.OFF;
        }
    }
}
