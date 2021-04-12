/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclass;

import forms.CipherForm;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import prg.FF;
import prg.P;
import prg.V;

/**
 *
 * @author DaN
 */
public class Cipher {

    private static final String DEFFILE = V.HOME_DIRECTORY + V.SEPARATOR + "BARCODES" + V.SEPARATOR; // каталог для сохранения, если файл не выбран
    private static SerialPort serialPort;
    private static int level = 0; // текущий уровень общения с устройством
    private static int level1 = 0; // количество обращений на второй уровень (пропущен первый)
    private static int colBarcode = 0; // количество штрихкодов,уменьшаемое
    private static int colBarcodeChek = 0; // количество штрихкодов 
    javax.swing.Timer timer; // таймер опроса устройства
    javax.swing.Timer timerStart; // таймер ожидания
    public static ArrayList<String> barcodeList = new ArrayList<>(); // сюда пишутся штрихкоды
    public static ArrayList<String> barcodeChekSum = new ArrayList<>(); // сюда пишутся контрольные суммы штрихкодов
    Fieldr newsFild; // для общения с пользователем
    private String fileName = ""; // путь файла для записи
    private static String bufin = ""; // входящие данные
    private boolean fileFlag;
    private boolean d_pl = false;
    public static  CipherForm THISFORM;
    private static int pause = 100; //пауза ожидания приема
    
    public Cipher(String port,String speed, int time, CipherForm form, boolean fileFlag) throws SerialPortException {  // инициализируем и открываем порт с начальными параметрами + timer
        this.fileFlag = fileFlag;
        this.THISFORM=form;
        newsFild = form.F[1];
        serialPort = new SerialPort(port); //иницилизация класса работы с портом
        if (serialPort.isOpened()) {
            serialPort.closePort();
        }
        if (!serialPort.openPort() ) {
            P.MESS("Ошибка открытия порта "+port+"/n"+"Возможно порт занят другим приложением.");
            return;
        }
        if (!serialPort.setParams(SerialPort.BAUDRATE_38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE) ) {
            P.MESS("Ошибка задания параметров "+SerialPort.BAUDRATE_38400+","+SerialPort.DATABITS_8+","+SerialPort.STOPBITS_1+","+SerialPort.PARITY_NONE  +" для порта "+port);
            return;
        }
        if (V.markaNames[0].equals(form.CB[2].GETSELECTEDVALUE()) ){  //для 8000 есть инфокрасный порт на подставке
        
            if (!setIrdaSpeed(speed)){  //включить инфокрасный порт на подставке
               String mes="Ошибка включения инфокрасного порта подставки для начала передачи  "+SerialPort.BAUDRATE_38400+","+SerialPort.DATABITS_8+","+SerialPort.STOPBITS_1+","+SerialPort.PARITY_NONE  +" для порта "+port;
                mes=mes+"\n"+"Проверьте правильность подключения кабеля между РС и подставкой ТСД, а также включено ли питание подставки .  ";
                P.MESS(mes);
                THISFORM.IKRUN=false;
                return;
              }
        } else {
            serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            level=1;
        }
         THISFORM.IKRUN=true;
         newsFild.setText("Устройсво готово к принятию данных. Ожидание  передачи...");
        
      

        timer = new javax.swing.Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (level==1){
                        sendDataToPort("ACK1" + (char) 13, pause); // 1-ый запрос
//                        timer.stop();
                        level1++;
                        if (level1>25) {
                            level1=0;
                            THISFORM.CLICK_ALL("b_start");
                            THISFORM.F[1].setText("Установите ТСД в подставку и нажмите <"+THISFORM.starton+">..." );
                            THISFORM.F[1].setOpaque(true);
                            THISFORM.B[0].setBackground(Color.red);
                            //THISFORM.F[5].requestFocus(); //фокус по умолчанию
                            THISFORM.CLICK_ALL("B5"); //готовновсть для ручного сканера

                            
                        }
                        return;
                } 
                if (level>1){
//                  timer.stop();                    
                }
                
                if (level==4){
                        check();
                        return;
                }
                
                
            }
        });
        
         getDataStart(THISFORM.F[2].getText()); //включение режима передачи
        
    }

    public Cipher() throws SerialPortException {  // инициализируем и открываем порт без параметров + timer
        this(V.CIPHER_PORT,"115200",5000,null, true);
        d_pl = true;
    }

    private  void check() { // после получения данных проверяем, все ли считались
//        if (colBarcodeChek <= barcodeList.size() && checkSum() == true) {
        if (colBarcodeChek <= barcodeList.size()) {
            timer.stop();
            if (d_pl == true) {
                save();
            } else {
                saveToFile();
            }
            
            clear();
            if (THISFORM.RUNFORM!=null) {  //если есть метод в форме вызвавшую терминальную программу для обработки принятых штрихкодов
             THISFORM.RUNFORM.SAVE_TER(THISFORM);
            }
        } else {
            P.MESSERR("Ошибка с данными, количество штрихкодов не совпало!");
//            sendDataToPort("NO" + (char) 13, 50);
            timer.stop();
            clear();
        }
    }

    private String Sum(String scan) { //для проверки контрольной суммы
            String ui = FF.ALLTRIM13(scan);
            int sum = 0;
            int len = ui.length();
            for (int j = 0; j < len; j++) {
                sum = sum + ui.codePointAt(j);
            }
            String s_sum = String.valueOf(sum);
            return s_sum;
        
    }
    private boolean checkSum() {
        barcodeChekSum.clear(); // чистим 
        for (int i = 0; i < barcodeList.size(); i++) {
            String ui = FF.ALLTRIM13(barcodeList.get(i));
            int sum = 0;
            int len = ui.length() - 2;
            for (int j = 0; j < len; j++) {
                sum = sum + ui.codePointAt(j);
            }
            String s_sum = String.valueOf(sum);
            barcodeChekSum.add(s_sum);
            if (!ui.substring(len).equals(s_sum.substring(s_sum.length() - 2))) {
                return false;
            }
        }
        return true;
    }

    public void save() {
        V.FALSETRUE = true;
    }

    public String saveToFile() { // сохраняем в файл
        FileWriter fw = null;
        StringBuilder sb;
        try {
            if (fileFlag == true) {
                fw = new FileWriter(fileName);
            } else {
                fw = new FileWriter(fileName, true);
            }
//            fw.write("*********Штрихкоды*********\r\n");

            System.out.println("************А вот и список:*********");
            for (int i = 0; i < barcodeList.size(); i++) {
                sb = new StringBuilder();
                sb.append(barcodeList.get(i).substring(0, barcodeList.get(i).length() - 2));
                sb.append("\r\n");
                fw.write(sb.toString()); //записывем собранную строку в файл
                System.out.println(barcodeList.get(i)+" - "+barcodeChekSum.get(i));
            }
            fw.close();
            newsFild.setText("Данные получены и записаны в файл!");
            V.FALSETRUE = true;
        } catch (IOException ex) {
            saveDefFile();
            newsFild.setText("Файл не выбран!Записано в (C:\\Users\\Имя_пользователя\\BARCODES\\BARCODES.txt)");
        }
        //clear();
        return "Сохранено!";
    }

    public void saveDefFile() { // сохраняем в дефолтный файл, если пользователь забыл

        FileWriter fw = null;
        StringBuilder sb;


        File myPath = new File(DEFFILE);
        myPath.mkdirs();

        try {
            fw = new FileWriter(DEFFILE + V.SEPARATOR + "BARCODES.txt");

            fw.write("*********Штрихкоды*********\r\n");

            for (int i = 0; i < barcodeList.size(); i++) {
                sb = new StringBuilder();
                sb.append(barcodeList.get(i));
                sb.append("\r\n");
                fw.write(sb.toString()); //записывем собранную строку в файл
            }
            fw.close();
            V.FALSETRUE = true;
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public boolean setIrdaSpeed(String speed) { //установка скорости порта (1 шаг)
        try {
            synchronized (serialPort) {
                serialPort.setRTS(false);
                serialPort.wait(500);

                serialPort.writeInt((char) 7);
                serialPort.wait(100);

                switch (speed) {

                    case "19200":
                        serialPort.writeInt((char) 51);
                        serialPort.wait(100);
                        break;
                    case "38400":
                        serialPort.writeInt((char) 52);
                        serialPort.wait(100);
                        break;
                    case "57600":
                        serialPort.writeInt((char) 53);
                        serialPort.wait(100);
                        break;
                    case "115200":
                        serialPort.writeInt((char) 54);
                        serialPort.wait(100);
                        break;
                }
                serialPort.writeInt((char) 81);
                serialPort.wait(100);

                serialPort.closePort();
                serialPort.openPort();
                switch (speed) {

                    case "19200":
                        serialPort.setParams(SerialPort.BAUDRATE_19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        break;
                    case "38400":
                        serialPort.setParams(SerialPort.BAUDRATE_38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        break;
                    case "57600":
                        serialPort.setParams(SerialPort.BAUDRATE_57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        break;
                    case "115200":
                        serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        break;
                }
                serialPort.setRTS(false);
                serialPort.writeInt((char) 15);
                serialPort.wait(100);
                byte[] buffer = serialPort.readBytes();
                if (buffer[0] == (char) 15) {
                    level = 1;
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
            clear();
            return false;
        }
    }

    public String getDataStart(String fileName) { // если скорость установлена, запускаем считываение даныых
        this.fileName = fileName;
        return getDataStart();
    }

    public String getDataStart() { // если скорость установлена, запускаем считываение даныых
        if (level == 0) {
            P.MESSERR("Ошибка!Скорость порта не установлена!");
            return "Ошибка!Скорость порта не установлена!";
        }
        if (level == 1) {
            try {
                serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
                serialPort.addEventListener(new EventListener());
                sendDataToPort((char) 7, 500);
                serialPort.setRTS(true);
//                timerStart.start();
                timer.start();
            } catch (SerialPortException ex) {
                System.out.println(ex);
                return ex.toString();
            }
        } else {
            P.MESSERR("Ошибка получения данных");
            return "Ошибка получения данных";
        }
//        P.MESS("Для начала передачи,поставьте устройство на док-станцию,ожидание " + V.CIPHER_TIME + " секунд");
     //   P.MESS("После окончания передачи и обнуления данных на терминале нажмите ОК");
       return "Для начала передачи,поставьте устройство на док-станцию...";
    }

    private void sendDataToPort(int data, int wait) { // отправить на порт

        try {
            synchronized (serialPort) {
                serialPort.writeInt(data);
                serialPort.wait(wait);
            }
        } catch (SerialPortException | InterruptedException ex) {
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void sendDataToPort(String data, int wait) { // отправить на порт

        try {
            synchronized (serialPort) {
                serialPort.writeString(data);
                serialPort.wait(wait);
            }
        } catch (SerialPortException | InterruptedException ex) {
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public  void clear() { // очистка статик переменных, данных и закрытие порта, если открыт
        try {
            if (serialPort.isOpened()) {
                serialPort.closePort();
            }
        } catch (SerialPortException ex) {
            Logger.getLogger(Cipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        level = 0;
        colBarcode = 0;
        colBarcodeChek = 0;
        //      barcodeList.clear(); //СТИРАНИЕ СПИСКА ПОЛУЧЕНЫХ КОДОВ
    }

    private  class EventListener implements SerialPortEventListener { // слушатель порта, обрабатывает все данные с него в зависимости от уровня работы с устройством

        @Override
        public  void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                String tmp = "";
                try {
                    int count = serialPort.getInputBufferBytesCount();
                    if (count == 0) {
                        return;
                    }
//                    System.out.println("Кол-во байт:" + count);
                    byte[] buffer = serialPort.readBytes(count);
                    if (buffer == null) {
                        return;
                    }
                    String buf = new String(buffer);
//                    System.out.println("Буфер "+buf);
                    if (FF.EMPTY(buf)) {
                        return;
                    }

                    int pos = FF.AT(13, buf);
                    if (pos != 0) {
                        buf = FF.SUBSTR(buf, 1, pos - 1);
                        bufin = bufin + buf;
                        tmp = bufin;
                        bufin = "";
                    }
                    if (pos == 0) {
                        bufin = bufin + buf;
                        return;
                    }
                    if (level == 1 && FF.AT("CIPHER", tmp) > 0) {  //если пришло CIPHER
                       sendDataToPort("ACK2" + (char) 13, pause+900); // запрос количества
//                     System.out.println("На уровень 2");
                       level = 2;
                        return;
                    }
                    if (level == 2) {                //приходит количество штрихкодов
                        colBarcodeChek = new Integer(tmp); //количество штрихкодов 
                        colBarcode = 0;
                        barcodeList.clear();
                        THISFORM.F[4].SETVALUE(colBarcodeChek);
                        THISFORM.F[1].SETVALUE("Прием данных...");
                        
                       sendDataToPort("ACK3" + (char) 13, pause); // запрос штрихкода
//                    System.out.println("На уровень 3");                        
                        level = 3;
                    
                        return;
                    }
                    if (level == 3) {  //должны получить первый штрихкод
                        barcodeList.add(tmp);
                        colBarcode++; // добавляем штрихкод
                        THISFORM.F[3].SETVALUE(colBarcode);
                        sendDataToPort("ACK" + (char) 13, pause); // запрос штрихкода
//                       System.out.println("Штрихкод "+tmp);
                    }
                    if (colBarcode==colBarcodeChek) { //если все приняты 
                        if (!checkSum()) { // проверяем на контрольные разряды
                         sendDataToPort("NO" + (char) 13, 50);
                            THISFORM.CLICK_ALL("b_start");
                            THISFORM.F[1].setText("Ошибка при пердаче данных . Повторите передачу" );
                            THISFORM.F[1].setOpaque(true);
                            THISFORM.B[0].setBackground(Color.red);
                            return;
                        }
                     sendDataToPort("OK" + (char) 13, 50);
//                     System.out.println("OK");
                     level = 4;
                     
                                return;
                    }
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}
