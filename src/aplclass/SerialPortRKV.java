/**
 * КЛАСС РАБОТЫ С ПОРТАМИ ОТ jssc.SerialPort
 */
package aplclass;

import baseclass.Formr;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import prg.P;

public final class SerialPortRKV extends SerialPort {

    // private static SerialPortRKV port;
    public static String portname;
    public static String SCAN;
    private static Formr form;
    public static boolean print=true;

    public SerialPortRKV(String portname) {
        super(portname);
    }

    public SerialPortRKV(String portname, Formr form) {
        super(portname);
        SerialPortRKV.portname = portname;
        //   this.port=this;
        SerialPortRKV.form = form;
        try {
            if (!openPort()) {
                System.out.println("Ошибка открытия порта");
                return;
            }
            setFlowControlMode(FLOWCONTROL_RTSCTS_IN | FLOWCONTROL_RTSCTS_OUT);
//             System.out.println(port.getInputBufferBytesCount());
            //           System.out.println(port.getOutputBufferBytesCount());

        } catch (SerialPortException ex) {
            System.out.println(ex.toString());
        }
        try {
            addEventListener(new PortReader(this, SerialPortRKV.form), SerialPort.MASK_RXCHAR); //подключение обработчика событий порта
        } catch (SerialPortException ex) {
            System.out.println(ex.toString());
        }
    }

    //device = 0 - default
    //device = 1 - fiscalPrinter
    public SerialPortRKV(String portname, Formr form, int device) {
        super(portname);
        SerialPortRKV.portname = portname;
        //   this.port=this;
        SerialPortRKV.form = form;
        try {
            if (!openPort()) {
                System.out.println("Ошибка открытия порта");
                return;
            }
            setFlowControlMode(FLOWCONTROL_RTSCTS_IN | FLOWCONTROL_RTSCTS_OUT);
//             System.out.println(port.getInputBufferBytesCount());
            //           System.out.println(port.getOutputBufferBytesCount());

        } catch (SerialPortException ex) {
            System.out.println(ex.toString());
        }
        try {
            switch (device) {
                case 0:
                    addEventListener(new PortReader(this, SerialPortRKV.form), SerialPort.MASK_RXCHAR); //подключение обработчика событий порта
                    break;
                case 1:
//                    addEventListener(new PortReaderFP(this, SerialPortRKV.form), SerialPort.MASK_RXCHAR); //подключение обработчика событий порта
                    break;
                default:
                    addEventListener(new PortReader(this, SerialPortRKV.form), SerialPort.MASK_RXCHAR); //подключение обработчика событий порта
                    break;
            }
        } catch (SerialPortException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public boolean isOpened() {
        return super.isOpened(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] readBytes() throws SerialPortException {
        return super.readBytes(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean writeByte(byte singleByte) throws SerialPortException {
        return super.writeByte(singleByte); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean writeInt(int singleInt) throws SerialPortException {
        return super.writeInt(singleInt); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean openPort() {
        try {
            //throws SerialPortException {
            System.out.println("Открытие порт " + portname + " для формы " + form.getName());
            return super.openPort(); //To change body of generated methods, choose Tools | Templates.
        } catch (SerialPortException ex) {
            P.MESS("Ошибка открытия порта" + portname + " \n" + ex.toString());
            return false;
        }
    }

    @Override
    public boolean closePort() {
        try {
            // throws SerialPortException {
            System.out.println("Закрытие порт " + portname + " для формы " + form.getName());
            return super.closePort(); //To change body of generated methods, choose Tools | Templates.
        } catch (SerialPortException ex) {
            return false;
        }
    }

//    @Override
//    public void addEventListener(SerialPortEventListener PortReader) throws SerialPortException {
//        super.addEventListener(PortReader,SerialPort.MASK_RXCHAR); //To change body of generated methods, choose Tools | Templates.
    //   }
    private static class PortReader implements SerialPortEventListener {

        private final SerialPortRKV portd;
        private final Formr formd;

        public PortReader(SerialPortRKV port, Formr form) {
            portd = port;
            formd = form;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            P.WAIT(100);
            int kol = event.getEventValue();
            int kol2;
            try {
                kol2 = portd.getInputBufferBytesCount();
            } catch (SerialPortException ex) {
                kol2 = 0;
            }
            if (event.isRXCHAR() && kol > 0) {
                try {
                    // String  data = port.readString(); 
                    SCAN = portd.readString();
                    if (print) {
                    System.out.println("Штрихкод:" + SCAN + " на порт:" + portd.getPortName()+" для формы:"+formd.getName());
                    }
                    formd.EVENT(portname, SCAN);
                    // System.out.println("из порта "+data+"  "+kol+" "+kol2);                    
                } catch (SerialPortException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
    }
}
