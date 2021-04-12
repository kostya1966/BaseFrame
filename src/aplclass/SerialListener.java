/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import prg.P;
import prg.V;

public class SerialListener implements SerialPortEventListener {

    @Override
    public void serialEvent(SerialPortEvent event) {
        String inputStream;
        P.MESS("SERIAL - " + event.getPortName());
        if (event.isRXCHAR()) {
            try {
                inputStream = V.RADIO_PORT.readString();
                P.MESS(inputStream);
//                V.RADIO_PORT.closePort();
            } catch (SerialPortException ex) {
                System.out.println(ex);
            }
        }
    }

}
