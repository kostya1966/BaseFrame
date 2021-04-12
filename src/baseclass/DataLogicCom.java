/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclass;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import prg.V;

/**
 *
 * @author DaN
 */
public class DataLogicCom {

    private static SerialPort serialPort;
    public static String data = ""; // данные
    static Robot robot;

    public DataLogicCom() {
        try {
            robot = new Robot();
            serialPort = new SerialPort(V.SCANER_PORT);
            try {
                serialPort.openPort();
            } catch (SerialPortException ex) {
                System.out.println("Ошибка открытия порта!");
                return;
            }
            serialPort.setParams(V.SCANER_SPEED, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            // serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
            serialPort.addEventListener(new EventListener());

            System.out.println("COM проинициализирован! Ошибок не возникло!");
        } catch (SerialPortException | AWTException ex) {
            Logger.getLogger(DataLogicCom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void closePort() {
        try {
            serialPort.closePort();
        } catch (SerialPortException ex) {
            System.out.println("Unable to close port!");
        }
    }

    public static void press(String str) {
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == '\n' || ch[i] == '\r') {
                continue;
            }
            robot.keyPress((int) ch[i]);
        }

    }

    public void sendDataToPort(String data, int wait) { // отправить на порт

        try {
            synchronized (serialPort) {
                serialPort.writeString(data);
                // serialPort.wait(wait);
            }
        } catch (SerialPortException ex) {
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static class EventListener implements SerialPortEventListener { // слушатель порта, обрабатывает все данные с него

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                try {
                    int count = serialPort.getInputBufferBytesCount();
                    if (count == 0) {
                        return;
                    }
                    System.out.println("Кол-во байт:" + count);
                    byte[] buffer = serialPort.readBytes(count);
                    if (buffer == null) {
                        return;
                    }
                    data = new String(buffer);
                    System.out.println(data);
                    press(data);

                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}
