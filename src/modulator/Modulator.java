/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modulator;

import GraphicElements.MyButton;
import GraphicElements.PSpace;
import Utility.Frame.Status;
import Utility.Time;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author Istrac
 */
public class Modulator extends javax.swing.JFrame {

    private byte[] buf;
    private ServerSocket listener;
    private Socket socket;
    private DataInputStream input;
    private final JTextField[] disp;
    private DataOutputStream out;
    private final JTextField[] rdisp;
    private final ModCtrlFrame ctrlFrame;
    private final ModStatFrame statframe;
    private final byte[] sendbuf = new byte[50];
    private final JCheckBox[] boxes;
    private final MyButton[] btns;
    private final JTextField[] cptexts1;
    private final JTextField[] cptexts2;
    private Timer timer;
    private final Timer readyTimer;
    private volatile boolean connected;
    private boolean interlickSet;
    private boolean MAINS_ON;

    /**
     * Creates new form Modulator
     */
    public Modulator() {
        ctrlFrame = new ModCtrlFrame();
        statframe = new ModStatFrame();
        readyTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statframe.status.setBit(0);
            }
        });
        readyTimer.setRepeats(false);
        initComponents();
        buf = new byte[1024];

        new Thread() {
            @Override
            public void run() {
                try {
                    listener = new ServerSocket(20000, 5);
                } catch (IOException ex) {
                    Logger.getLogger(Modulator.class.getName()).log(Level.SEVERE, null, ex);
                }

                while (true) {//loop for listennnning and accepting connection at the first connection and later restablishing connection
                    try {
                        System.out.println("Listening for incomming connection");
                        socket = listener.accept();
                        System.out.println("connection accepted");
                        connected = true;
                        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                        out = new DataOutputStream(socket.getOutputStream());
                        while (true) {//receive infinite loop
                            if (connected) {
                                while (true) {// match txID byte0 & 1 and protocol ID byte 2&3
                                    byte byte0 = input.readByte();
                                    if (byte0 != 1) {
                                        continue;
                                    }
                                    byte byte1 = input.readByte();
                                    if (byte1 != 10) {
                                        continue;
                                    }
                                    byte byte2 = input.readByte();
                                    if (byte2 != 0) {
                                        continue;
                                    }
                                    byte byte3 = input.readByte();
                                    if (byte3 == 0) {
                                        break;
                                    }
                                }
                                input.readFully(buf, 4, 2);
                                int length = buf[5];
                                input.readFully(buf, 6, length);
                                int code = buf[7];
                                if (code == 3) { //status req packet
                                    sendPacket(code);
                                    System.out.println(Time.getPreciseTime()+": status req packet received");
                                } else if (code == 16) {//control packet
                                    ctrlFrame.setFromArray(buf);
//                                    System.out.println(Time.getPreciseTime()+": control packet received");
                                    setDisplay();
                                }

                            } else {
                                break;
                            }
                        }
                        //int i = 1;
                    } catch (IOException ex) {
                        Logger.getLogger(Modulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }.start();

        JPanel rp;
        {
            rp = new JPanel();
            rp.setPreferredSize(new Dimension(0, 0));
            rp.setLayout(new GridBagLayout());
            JLabel heading = new JLabel("Received");
            heading.setHorizontalAlignment(SwingConstants.CENTER);
            heading.setPreferredSize(new Dimension(0, 0));
            JLabel[] labels = new JLabel[18];
            PSpace[] spaces = new PSpace[18];
            rdisp = new JTextField[18];
            for (int i = 0; i < labels.length; i++) {
                String labeltext = getlabeltext(i + 1);
                labels[i] = new JLabel(labeltext);
                labels[i].setPreferredSize(new Dimension(0, 0));
                rdisp[i] = new JTextField("");
                spaces[i] = new PSpace(rdisp[i], 0.1, 0.1, 0.1, 0.1);
            }

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.weighty = 1.0 / 13;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            rp.add(heading, gridBagConstraints);
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.weightx = 0.5;
            for (int i = 0; i < labels.length; i++) {
                gridBagConstraints.gridy = i + 1;
                gridBagConstraints.gridx = 0;
                rp.add(labels[i], gridBagConstraints);
                gridBagConstraints.gridx = 1;
                rp.add(spaces[i], gridBagConstraints);
            }
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 11;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.weightx = 2.0 / 13;
            JLabel dummy = new JLabel();
            dummy.setPreferredSize(new Dimension(0, 0));
            rp.add(dummy, gridBagConstraints);
//            
        }
        JPanel cp;
        {
            cp = new JPanel();
            cp.setPreferredSize(new Dimension(0, 0));
            cp.setLayout(new GridBagLayout());
            JLabel heading = new JLabel("status panel");
            heading.setHorizontalAlignment(SwingConstants.CENTER);
            heading.setPreferredSize(new Dimension(0, 0));
            JPanel p1;
            {
                p1 = new JPanel();
                p1.setPreferredSize(new Dimension(0, 0));
                p1.setLayout(new GridBagLayout());
                boxes = new JCheckBox[5];
                String[] names = new String[]{"MAins", "HV", "Modulator", "IntExt", "Reset"};
                for (int i = 0; i < boxes.length; i++) {
                    boxes[i] = new JCheckBox();
                    boxes[i].setText(names[i]);
                }
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.weightx = 1.0 / 5;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                for (int i = 0; i < boxes.length; i++) {
                    gridBagConstraints.gridx = i;
                    p1.add(boxes[i], gridBagConstraints);
                }
            }
            JPanel p2;
            {
                p2 = new JPanel();
                p2.setPreferredSize(new Dimension(0, 0));
                p2.setLayout(new GridBagLayout());
                btns = new MyButton[5];
                PSpace[] spaces = new PSpace[5];
                String[] names = new String[]{"Mains", "HV", "Modulator", "IntExt", "Reset"};
                for (int i = 0; i < btns.length; i++) {
                    btns[i] = new MyButton(names[i]);
                    btns[i].setPreferredSize(new Dimension(0, 0));
                    btns[i].setBackground(Color.red);
                    spaces[i] = new PSpace(btns[i], 0.1, 0.1, 0.1, 0.1);
                }
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.weightx = 1.0 / 5;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                for (int i = 0; i < spaces.length; i++) {
                    gridBagConstraints.gridx = i;
                    p2.add(spaces[i], gridBagConstraints);
                }
            }
            JPanel p3;
            {
                p3 = new JPanel();
                p3.setPreferredSize(new Dimension(0, 0));
                p3.setLayout(new GridBagLayout());
                JLabel[] labels = new JLabel[4];
                cptexts1 = new JTextField[4];
                PSpace[] spaces = new PSpace[4];
                String[] names = new String[]{"PRF", "PW", "set Cathode Voltage", "set Grid Voltage"};
                for (int i = 0; i < labels.length; i++) {
                    labels[i] = new JLabel(names[i]);
                    labels[i].setPreferredSize(new Dimension(0, 0));
                    cptexts1[i] = new JTextField("");
                    spaces[i] = new PSpace(cptexts1[i], 0.1, 0.1, 0.1, 0.1);
                }

                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.weightx = 0.5;
                gridBagConstraints.weighty = 1.0 / 3;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                for (int i = 0; i < labels.length; i++) {
                    gridBagConstraints.gridy = i;
                    gridBagConstraints.gridx = 0;
                    p3.add(labels[i], gridBagConstraints);
                    gridBagConstraints.gridx = 1;
                    p3.add(spaces[i], gridBagConstraints);
                }
            }
            JPanel p4;
            {
                p4 = new JPanel();
                p4.setPreferredSize(new Dimension(0, 0));
                p4.setLayout(new GridBagLayout());
                JLabel[] labels = new JLabel[6];
                cptexts2 = new JTextField[6];
                PSpace[] spaces = new PSpace[6];
                String[] names = new String[]{"Filament voltage", "Cathode Voltage", "Beam On Voltage", "Filament current", "Helix Current", "Cathode Current",};
                for (int i = 0; i < labels.length; i++) {
                    labels[i] = new JLabel(names[i]);
                    labels[i].setPreferredSize(new Dimension(0, 0));
                    cptexts2[i] = new JTextField("");
                    spaces[i] = new PSpace(cptexts2[i], 0.1, 0.1, 0.1, 0.1);
                }

                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.weightx = 0.5;
                gridBagConstraints.weighty = 1.0 / 6;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                for (int i = 0; i < labels.length; i++) {
                    gridBagConstraints.gridy = i;
                    gridBagConstraints.gridx = 0;
                    p4.add(labels[i], gridBagConstraints);
                    gridBagConstraints.gridx = 1;
                    p4.add(spaces[i], gridBagConstraints);
                }
            }
            JPanel p5;
            {
                p5 = new JPanel();
                p5.setPreferredSize(new Dimension(0, 0));
                p5.setLayout(new GridBagLayout());
                JCheckBox[] tickbox = new JCheckBox[5];
                JLabel[] labels = new JLabel[tickbox.length];
                String[] names = new String[]{"Duty", "Body Current", "Temperature", "Cathode Cuttent", "VSWR"};
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.weightx = 1.0 / tickbox.length;
                gridBagConstraints.weighty = 1.0 / 2;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                ActionListener listener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        interlickSet = true;
                        statframe.status.resetBit(13);
                    }
                };

                for (int i = 0; i < tickbox.length; i++) {
                    tickbox[i] = new JCheckBox();
                    tickbox[i].addActionListener(listener);
                    gridBagConstraints.gridy = 0;
                    gridBagConstraints.gridx = i;
                    p5.add(tickbox[i], gridBagConstraints);
                    gridBagConstraints.gridy = 1;
                    labels[i] = new JLabel(names[i]);
                    labels[i].setPreferredSize(new Dimension(0, 0));
                    p5.add(labels[i], gridBagConstraints);
                }
                tickbox[0].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean selected = ((JCheckBox) (e.getSource())).isSelected();
                        if (selected) {
                            statframe.status.setBit(3);//excess duty
                            statframe.faultStatus.setValue(17);
                        } else {
                            statframe.status.resetBit(3);
                            statframe.faultStatus.setValue(0);
                        }
                    }
                });
                tickbox[1].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean selected = ((JCheckBox) (e.getSource())).isSelected();
                        if (selected) {
                            statframe.status.setBit(1);
                            statframe.faultStatus.setValue(14);
                        } else {
                            statframe.status.resetBit(1);
                            statframe.faultStatus.setValue(0);
                        }
                    }
                });
                tickbox[2].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean selected = ((JCheckBox) (e.getSource())).isSelected();
                        if (selected) {
                            statframe.status.setBit(6);//K OT
                            statframe.faultStatus.setValue(12);
                        } else {
                            statframe.status.resetBit(6);
                            statframe.faultStatus.setValue(0);
                        }
                    }
                });
                tickbox[3].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean selected = ((JCheckBox) (e.getSource())).isSelected();
                        if (selected) {
                            statframe.status.setBit(2);
                            statframe.faultStatus.setValue(10);
                        } else {
                            statframe.status.resetBit(2);
                            statframe.faultStatus.setValue(0);
                        }
                    }
                });
                tickbox[4].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean selected = ((JCheckBox) (e.getSource())).isSelected();
                        if (selected) {
                            statframe.status.setBit(4);
                            statframe.faultStatus.setValue(18);
                        } else {
                            statframe.status.resetBit(4);
                            statframe.faultStatus.setValue(0);
                        }
                    }
                });
            }

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.weighty = 1.0 / 10;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            p1.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            cp.add(new PSpace(p1, 0.05, 0.1, 0.05, 0.1), gridBagConstraints);
            gridBagConstraints.gridy = 1;
            p2.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            cp.add(new PSpace(p2, 0.05, 0.1, 0.05, 0.1), gridBagConstraints);
            gridBagConstraints.gridy = 2;
            gridBagConstraints.weighty = 1.0 / 4;
            p3.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            cp.add(new PSpace(p3, 0.05, 0.1, 0.05, 0.1), gridBagConstraints);
            gridBagConstraints.gridy = 3;
            gridBagConstraints.weighty = 1.0 / 3;
            p4.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            cp.add(new PSpace(p4, 0.05, 0.1, 0.05, 0.1), gridBagConstraints);
            gridBagConstraints.gridy = 4;
            gridBagConstraints.weighty = 1.0 / 10;
            p5.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            cp.add(new PSpace(p5, 0.05, 0.1, 0.05, 0.1), gridBagConstraints);
        }
        JPanel sp;
        {
            sp = new JPanel();
            sp.setPreferredSize(new Dimension(0, 0));
            sp.setLayout(new GridBagLayout());
            JLabel heading = new JLabel("send Packet");
            heading.setHorizontalAlignment(SwingConstants.CENTER);
            heading.setPreferredSize(new Dimension(0, 0));
            JLabel[] labels = new JLabel[15];
            PSpace[] spaces = new PSpace[15];
            disp = new JTextField[15];
            for (int i = 0; i < labels.length; i++) {
                String labeltext = getlabeltext(i + 1);
                labels[i] = new JLabel(labeltext);
                labels[i].setPreferredSize(new Dimension(0, 0));
                disp[i] = new JTextField("0000");
                spaces[i] = new PSpace(disp[i], 0.1, 0.1, 0.1, 0.1);
            }
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.weighty = 1.0 / 17;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            sp.add(heading, gridBagConstraints);
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.weightx = 0.5;
            for (int i = 0; i < labels.length; i++) {
                gridBagConstraints.gridy = i + 1;
                gridBagConstraints.gridx = 0;
                sp.add(labels[i], gridBagConstraints);
                gridBagConstraints.gridx = 1;
                sp.add(spaces[i], gridBagConstraints);
            }
            JButton send = new JButton("reset");
            send.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reset();
                }
            });
            PSpace bsp = new PSpace(send, 0.4, 0.1, 0.4, 0.1);
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.gridy = 17;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.weightx = 1;
            sp.add(bsp, gridBagConstraints);
        }
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        rp.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        add(new PSpace(rp, 0.05, 0.1, 0.05, 0.1), gridBagConstraints);
        gridBagConstraints.gridx = 1;
        cp.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        add(new PSpace(cp, 0.05, 0.1, 0.05, 0.1), gridBagConstraints);
        gridBagConstraints.gridx = 2;
        sp.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        add(new PSpace(sp, 0.05, 0.1, 0.05, 0.1), gridBagConstraints);
        setSize(1100, 700);
        cptexts2[0].setText("-1");
        cptexts2[1].setText("-15");
        cptexts2[2].setText("-1");
        cptexts2[3].setText("0");
        cptexts2[4].setText("1");
        cptexts2[5].setText("1");

        interlickSet = true;
        statframe.status.resetBit(13);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Modulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Modulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Modulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Modulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Modulator().setVisible(true);
            }
        });
    }

    private String getlabeltext(int i) {
        String str = "";
        switch (i) {
            case 1:
                str = "  " + i + "st word";
                break;
            case 2:
                str = "  " + i + "nd word";
                break;
            case 3:
                str = "  " + i + "rd word";
                break;
            default:
                str = "  " + i + "th word";
        }
        return str;
    }

    private void sendPacket(int code) {
//        statframe.PRF.setValue(ctrlframe.PRF.getValueAsString());
//        statframe.pw.setValue(ctrlframe.pw.getValueAsString());
//        statframe.gridOnVoltage.setValue(ctrlframe.gridOnVoltage.getValueAsString());
//        statframe.setcathodeVoltage.setValue(ctrlframe.cathodeVoltage.getValueAsString());
        int length = 0;

        double offset[] = new double[]{-2, -25, -4, 1.01, 2, 3};
        switch (code) {
            case 3://stat packet request
//                for (int i = 0; i < 6; i++) {
//                    double random = 0 * (Math.random()) + offset[i];
////                    cptexts2[i].setText(String.format("%.2f", random));
//                        cptexts2[i].setText(cptexts2[i].getText());
//
//                }
                statframe.filamentVoltage.setValue(cptexts2[0].getText());
                statframe.cathodeVoltage.setValue(cptexts2[1].getText());
                statframe.beamOnVoltage.setValue(cptexts2[2].getText());
                statframe.filamentCurrent.setValue(cptexts2[3].getText());
                statframe.helixBodyCurrent.setValue(cptexts2[4].getText());
                statframe.cathodeCurrent.setValue(cptexts2[5].getText());
                statframe.beamOFFVoltage.setValueWithDecimal(-3.1);
                statframe.getDataBytes(sendbuf);

                length = 49;
                break;
            case 16:
                //response after receiving control packet
                sendbuf[0] = 1;
                sendbuf[1] = 10;
                sendbuf[2] = 0;
                sendbuf[3] = 0;
                sendbuf[4] = 0;
                sendbuf[5] = 33;
                sendbuf[6] = 1;
                sendbuf[7] = 16;
                sendbuf[8] = 0;
                sendbuf[9] = 0;
                sendbuf[10] = 0;
                sendbuf[11] = 13;
                sendbuf[12] = 26;

                length = 13;
        }
        if (code == 3) {
            for (int i = 0; i < 12; i++) {
                String text = getString(sendbuf, 2 * i) + " " + getString(sendbuf, 2 * i + 1);
                disp[i].setText(text);
            }
        }

        try {
            out.write(sendbuf, 0, length);
            out.flush();
        } catch (IOException ex) {
            connected = false;
            Logger.getLogger(Modulator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setDisplay() {
        for (int i = 0; i < 13; i++) {
            String text = getString(buf, 2 * i + 13) + " " + getString(buf, 2 * i + 1 + 13);
            rdisp[i].setText(text);
        }
        
        statframe.switch1.setValue(ctrlFrame.switch1.getValue());
        
        statframe.switch2.setValue(ctrlFrame.switch2.getValue());
        cptexts1[0].setText(ctrlFrame.PRF.getValueAsString());
        cptexts1[1].setText(ctrlFrame.pulsewidth.getValueAsString());
        cptexts1[2].setText(ctrlFrame.cathodeVoltage.getValueAsString());
        cptexts1[3].setText(ctrlFrame.beamOnVoltage.getValueAsString());
        statframe.PRF.setValueWithDecimal(ctrlFrame.PRF.getValueWithDecimal());
        statframe.PW.setValueWithDecimal(ctrlFrame.pulsewidth.getValueWithDecimal());
        int delay = ctrlFrame.warmUpTimer.getValue() * 1000;
        readyTimer.setInitialDelay(delay);
        readyTimer.setDelay(delay);
        int[] bits = ctrlFrame.switch1.unpackbits();
        if (bits[0] == 1) {
            boxes[0].setSelected(false);// MAINS OFF
        } else if (bits[1] == 1) {
            boxes[0].setSelected(true);// MAINS ON
        }
        boxes[1].setSelected(bits[2] == 1);
        boxes[2].setSelected(bits[3] == 1);
        boxes[4].setSelected(bits[4] == 1);

//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Modulator.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        bits = ctrlFrame.switch1.unpackbits();
//        for(int k=0;k<bits.length;k++){
//            System.out.print(""+bits[k]);
//        }
//        System.out.println("");
        if (bits[0] == 1) {
            btns[0].setBackground(Color.RED);// MAINS OFF
        } else if (bits[1] == 1) {
            if (statframe.getMainsStatus() != Status.ON) {
                System.out.println("ready timer restarted");
                btns[0].setBackground(Color.GREEN);// MAINS ON
                readyTimer.restart();
            }

        }
        if (bits[2] == 1) {
            btns[1].setBackground(Color.GREEN);// HV ON
        } else {
            btns[1].setBackground(Color.RED);// HV OFF
        }
        if (bits[3] == 1) {
            btns[2].setBackground(Color.GREEN);// Mod ON
        } else {
            btns[2].setBackground(Color.RED);// Mod OFF
        }
        if (bits[0] == 1) {
            statframe.status.setBit(9);// MAINS OFF
            statframe.status.resetBit(10);
            MAINS_ON = false;
            statframe.status.resetBit(0);
        } else if (bits[1] == 1) {
            statframe.status.resetBit(9);
            statframe.status.setBit(10);// MAINS ON
            if (MAINS_ON == false) {
                MAINS_ON = true;
                statframe.status.resetBit(0);
            }
        }
        if (bits[2] == 1) {
            statframe.status.setBit(11);// HV ON
        } else {
            statframe.status.resetBit(11);// HV OFF
        }
        if (bits[3] == 1) {
            statframe.status.setBit(12);// Mod ON
        } else {
            statframe.status.resetBit(12);// Mod OFF
        }

//        if (bits[6] == 0) { //ext
//            statframe.status.resetBit(5);
//        } else {//int
//            statframe.status.setBit(5);
//        }
        boxes[3].setSelected(bits[6] == 0);
        
            System.out.println(Time.getPreciseTime()+": control packet received");
        
        if (bits[4] == 1) { //reset
            statframe.status.setBit(13);
            interlickSet = false;
        } else {
            statframe.status.resetBit(13);
        }
        if (bits[11] == 1) {
            statframe.status.setBit(14);
        }else{
            statframe.status.resetBit(14);
        }
//        statframe.switch1.resetBit(6);
//        if (ctrlframe.getMains() == Status.ON) {
//            timer.start();
//        } else {
//            timer.stop();
//        }
//        new Thread(){
//
//            @Override
//            public void run() {
//                double d=ctrlframe.cathodeVoltage.getValueWithDecimal();   
//                for(int i=0;i<5;i++){
//                    try {
//                        Thread.sleep(1000);                             
//                        cptexts2[1].setText(""+d);
//                        d=d*1.02;
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(Modulator.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//        }.start();
    }

    private String getString(byte[] buf, int index) {
        int val = buf[index];
        if (val < 0) {
            val += 256;
        }
        int uppernibble = val / 16;
        int lowernibble = val % 16;

        return (nibble2String(uppernibble) + nibble2String(lowernibble));
    }

    private String nibble2String(int nibble) {
        String str = "";
        switch (nibble) {
            case 15:
                str = "F";
                break;
            case 14:
                str = "E";
                break;
            case 13:
                str = "D";
                break;
            case 12:
                str = "C";
                break;
            case 11:
                str = "B";
                break;
            case 10:
                str = "A";
                break;
            default:
                str = String.valueOf(nibble);

        }
        return str;
    }

    private void reset() {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].setSelected(false);
        }
        for (int i = 0; i < btns.length; i++) {
            btns[i].setBackground(Color.red);
        }
        for (int i = 0; i < cptexts1.length; i++) {
            cptexts1[i].setText("");
        }
        for (int i = 0; i < cptexts2.length; i++) {
            cptexts2[i].setText("");
        }
        for (int i = 0; i < rdisp.length; i++) {
            rdisp[i].setText("");
        }
        for (int i = 0; i < disp.length; i++) {
            disp[i].setText("");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
