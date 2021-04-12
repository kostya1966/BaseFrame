package aplclass;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import jssc.SerialPort;
import jssc.SerialPortException;
import prg.P;
import prg.V;

public class Tablo {

    private ActiveXComponent tablo;
    Timer timer;
    TimerTask task;
    public static SerialPort serialPort;

    public Tablo() {
    }

    public void tabloConnection() {
        try {
            System.out.println("TBin1");
            tablo = new ActiveXComponent("AddIn.Line45");   
            System.out.println("TBin2");
            tablo.setProperty("DeviceEnabled", new Variant(false));
            System.out.println("TBin3");
            tablo.setProperty("BaudRate", new Variant(9600));
            System.out.println("TBin4");
            tablo.setProperty("Parity", new Variant(0));
            System.out.println("TBin5");
            tablo.setProperty("DataBits", new Variant(8));
            System.out.println("TBin6");
            tablo.setProperty("StopBits", new Variant(1));
            System.out.println("TBin7");
            tablo.setProperty("Model", new Variant(1));
            System.out.println("TBin8");
            tablo.setProperty("CharacterSet", new Variant(866));
            System.out.println("TBin9");
            tablo.setProperty("DeviceEnabled", new Variant(true));
            System.out.println("TBin10");
//            String[] arrP = P.GET_COM_PORTS();
//            for (int i = 0; i < arrP.length; i++) {
//                tablo.setProperty("DeviceEnabled", new Variant(false));
//                tablo.setProperty("PortNumber", new Variant(Integer.parseInt(arrP[i].substring(3))));
//                tablo.setProperty("DeviceEnabled", new Variant(true));
//                if (!tablo.getProperty("ResultCode").toString().equals("0")) {
//                    System.out.println("COM" + i + " - " + tablo.getProperty("ResultDescription").toString());
//                    tablo.setProperty("DeviceEnabled", new Variant(false));
//                    continue;
//                } else {
//                    tablo.setProperty("DeviceEnabled", new Variant(false));
//                    serialPort = new SerialPort(arrP[i]);
//                }
//                break;
//            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
//        serialPort = new SerialPort(V.TABLO_PORT);
//        try {
//            serialPort.openPort();
//            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//        } catch (SerialPortException ex) {
//        }
    }

    public void tabloClose() {
//        try {
//            serialPort.closePort();
//        } catch (SerialPortException ex) {
//        }
        tablo.setProperty("DeviceEnabled", new Variant(false));
    }

    public void writeMessage(String all) {
        try {
//            tablo.setProperty("DeviceEnabled", new Variant(false));
//            tablo.setProperty("DeviceEnabled", new Variant(true));
            tablo.invoke("Clear");
            while (all.length() != 40) {
                if (all.length() > 40) {
                    all = all.substring(0, 40);
                    break;
                }
                all = P.STRP(all, " ");
            }
            Variant[] var = {new Variant(all), new Variant(0)};
            Variant[] var2 = {new Variant(0), new Variant(0), new Variant(2), new Variant(20), new Variant(2), new Variant(20)};
            tablo.invoke("CreateWindow", var2);
            tablo.invoke("DisplayText", var);

            V.TABLO_TEXT = all;
        } catch (Exception exc) {
        }
//        try {
//
//            V.TABLO_TEXT = all;
////            serialPort.purgePort(1000);
////            serialPort.writeBytes("                                        ".getBytes());
//            while (all.length() != 40) {
//                if (all.length() > 40) {
//                    all = all.substring(0, 40);
//                    break;
//                }
//                all = P.STRP(all, " ");
//            }
//            
//            try {
//                serialPort.writeBytes(all.toUpperCase().getBytes(V.KODIROVKA));
//            } catch (UnsupportedEncodingException ex) {
//            }
//        } catch (SerialPortException ex) {
//        }
    }

    public void stop() {
        try {
            task.cancel();
        } catch (NullPointerException ex) {
        }
    }

    public void writeTimeMessage(final String all, int time) {
        task = new TimerTask() {
            @Override
            public void run() {
                writeMessage(all);
            }
        };
        timer = new Timer();
        timer.schedule(task, time);
    }
}
