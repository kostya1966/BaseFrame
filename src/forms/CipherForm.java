/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import aplclass.ScanerAdapter;
import baseclass.Cipher;
import baseclass.Formr;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import jssc.SerialPortException;
import jssc.SerialPortList;
import prg.FF;
import prg.P;
import prg.V;

/**
 *
 * @author dima
 */
public class CipherForm extends Formr {

    public Cipher cipher;
    boolean fileFlag;
    String[] portNames;
    public String starton="Получить данные";
    public String startoff="Отменить передачу";
    public Formr RUNFORM=null;    
    public boolean PLAY=false;
    public boolean IKRUN=false;
    
    public CipherForm() {
        super("CipherForm", "Считывание штрихкодов CIPHERLAB", 550, 300); //Вызов конструктора от базового класса FormMod}
        portNames = SerialPortList.getPortNames();
        this.setClosable(false);
        setMaximizable(false); // кнопка максимизации      
        setIconifiable(false);   // кнопка сворачивания    
        this.SETRESIZABLE(1);
        this.setLayer(V.LAYER_MESS);
        
    }

    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
        L[0] = P.addobjL(this, "lb_port", "Порт:");
        L[1] = P.addobjL(this, "lb_speed", "Скорость порта:");
        L[2] = P.addobjL(this, "lb_wait", "Ожидание(с.):"); L[2].setVisible(false);
        L[3] = P.addobjL(this, "lb_folder", "Файл для сохранения:");
        L[6] = P.addobjL(this, "lmarka", "Марка ТСД:");
        
        // CB[0] = P.addobjCB(this, "cb_port", new String[]{"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10", "COM11", "COM12", "COM13", "COM14", "COM15",});
        CB[0] = P.addobjCB(this, "cb_port", portNames);
        if (portNames.length != 0) {
            CB[0].SETVALUE(V.CIPHER_PORT);
        }
       CB[2] = P.addobjCB(this, "marka", V.markaNames);
       CB[2].SETVALUE(V.CIPHER_MARKA);
        
        CB[1] = P.addobjCB(this, "cb_speed", new String[]{"115200", "19200", "38400", "57600"});
        B[0] = P.addobjB(this, "b_start", starton, 160, 30, "Стартуем!");
        B[0].SETALTKYE(1);
        
        B[1] = P.addobjB(this, "b_exit", "Выход", 90, 30, "Выходим!");
        B[2] = P.addobjB(this, "b_folder", "...", 30, 30, "Выбор каталога");
        B[3] = P.addobjB(this, "B3", "...", 30, 30, "Сканирование рабочих портов и определение порта для ТСД");
      //  B[3].setVisible(false);
        F[0] = P.addobjF(this, "f_wait", V.CIPHER_TIME, 80, 30); F[0].setVisible(false);
        F[1] = P.addobjF(this, "f_4", "", 520, 30);
        F[1].setEnabled(false);
        F[2] = P.addobjF(this, "f_folder", V.CIPHER_FILE_PATH, 290, 30);
        L[4] = P.addobjL(this, "L4", "Принято:");
        F[3] = P.addobjF(this, "F3", "0", 40, 25); F[3].SETREADONLY(true);
        L[5] = P.addobjL(this, "L5", "из");
        F[4] = P.addobjF(this, "F4", "0", 40, 25); F[4].SETREADONLY(true);
        B[4] = P.addobjB(this, "B4", "Из файла", 90, 30, "Прием данных из указанного файла содержащего список штрихкодов ");
        B[4].SETALTKYE(5);
        
        if (V.USER_ADMIN<3)      {
            B[4].setEnabled(false);
        }
        B[5] = P.addobjB(this, "B5", "Ручной сканер", 150, 30, "Прием данных считыванием ручного сканера-имитатора вводв с клавиатуры");
        B[5].SETALTKYE(6);
        B[5].setVisible(false);
        F[5] = P.addobjF(this, "F5", "", 21, 25); 
        F[5].setVisible(false);
        
    }


    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга
        locate(L[0], null, V.LOC_LEFT, 10, null, V.LOC_UP, 15);
        locate(CB[0], L[0], V.LOC_RIGHT, 0, null, V.LOC_UP, 10);
        
        locate(B[3], CB[0], V.LOC_RIGHT, 10, null, V.LOC_UP, 10);
        locate(L[6], B[3], V.LOC_RIGHT, 10, null, V.LOC_UP, 15);
        locate(CB[2], L[6], V.LOC_RIGHT, 0, null, V.LOC_UP, 10);
        
        locate(L[1], CB[2], V.LOC_RIGHT, 10, null, V.LOC_UP, 15);
        
        locate(CB[1], L[1], V.LOC_RIGHT, 0, null, V.LOC_UP, 10);
        locate(L[2], CB[1], V.LOC_RIGHT, 10, null, V.LOC_UP, 15);
        locate(F[0], L[2], V.LOC_RIGHT, 0, null, V.LOC_UP, 10);

        locate(F[1], null, V.LOC_LEFT, 10, L[0], V.LOC_DOWN, 10);

        locate(B[0], null, V.LOC_LEFT, 10, F[1], V.LOC_DOWN, 0);
        locate(L[4], B[0], V.LOC_RIGHT, 30, F[1], V.LOC_DOWN, 0);
        locate(F[3], L[4], V.LOC_RIGHT, 10, F[1], V.LOC_DOWN, 0);
        locate(L[5], F[3], V.LOC_RIGHT, 10, F[1], V.LOC_DOWN, 0);
        locate(F[4], L[5], V.LOC_RIGHT, 10, F[1], V.LOC_DOWN, 0);

        locate(B[1], null, V.LOC_RIGHT, 0, F[1], V.LOC_DOWN, 0);

        locate(L[3], null, V.LOC_LEFT, 10, B[0], V.LOC_DOWN, 15);
        locate(B[2], L[3], V.LOC_RIGHT, 0, B[0], V.LOC_DOWN, 10);
        locate(F[2], B[2], V.LOC_RIGHT, 0, B[0], V.LOC_DOWN, 10);
        locate(B[4], F[2], V.LOC_RIGHT, 0, B[0], V.LOC_DOWN, 10);
        locate(B[5], null, V.LOC_LEFT, 0, B[4], V.LOC_DOWN, 10);
        locate(F[5], B[5], V.LOC_RIGHT, 0, B[4], V.LOC_DOWN, 10);
        
    }
 
    @Override
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
        if ("B5".equals(name)) { //ручной сканер
         F[5].setBackground(Color.GREEN);
         F[5].requestFocus(); //фокус по умолчанию
        }
        
        if ("b_start".equals(name)) {
            if (B[0].getText().equals(startoff) ){  // если прервать передачу
                B[0].setText(starton);
                //блок отката по передаче
                V.FALSETRUE = false;
                cipher.clear();
                F[1].setText("Порт закрыт. ");  // если нет ожибки
                F[3].setText("0");  //
                F[4].setText("0");  //
                
                return;
            }
            if (F[2].getText().equals("")) {
                P.MESSERR("Не выбран файл для сохранения штрихкодов!" + "\n" + "Пожалуйста, выберите файл!");
            } else {

                File file = new File(F[2].getText());

                if (file.exists()) {
                    int answ=0;
                    if (PLAY==false) {  //если не автозапуск проверка на существование файла
                     answ = P.MESSSEL("Резервный файл для штрихкодов уже создан!" + "\n" + "Перезаписать или добавить данные?", new String[]{"Перезаписать", "Добавить"}, 0);
                                      }
                    switch (answ) {
                        case 0:
                            fileFlag = true; // перезаписать
                            break;
                        case 1:
                            fileFlag = false;
                            break;
                        default:
                            return;
                    }

                } else {
                    fileFlag = false;
                }
                try {
                    cipher = new Cipher(CB[0].GETSELECTEDVALUE(),CB[1].GETSELECTEDVALUE(), Integer.parseInt(F[0].getText()), this, fileFlag);
                                         // номер порта                         время ожидания    сообщение  файл перезаписать или нет
                    if (IKRUN) {
                    B[0].setText(startoff);
                    } else {
                    F[1].setText("Ошибка включения подставки ТСД для передачи...");  // если нет ожибки
                    B[0].setText(starton);
                        
                    }
                    
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                    F[1].setText("Ошибка открытия порта...");  // если ошибка
                }

//                F[1].setText(cipher.setIrdaSpeed(CB[1].GETSELECTEDVALUE()));
//                F[1].setText(cipher.getDataStart(F[2].getText()));
            }
        }
        if ("b_exit".equals(name)) {
            if (cipher != null) {
                cipher.clear();
            }
            this.DESTROY();
        }
        if ("b_folder".equals(name)) {
//            F[2].setText(P.FILECHOOSE("Выбор файла для занесения считанных данных из терминала"));
            F[2].setText(P.GETFILE("Выбор файла для занесения считанных данных из терминала","","",V.CURRENT_PATH));
            
            if (F[2].getText().equals("")) {
                F[2].setText(V.CIPHER_FILE_PATH);
            }
        }
        if ("B4".equals(name)) {
                DBLCLICK_ALL("f_folder");
            }
        
    }
   
  @Override
    public boolean VALID_ALL(String name) {
     //KAA 27082018 переделал на работу с классом ScanerAdapter
     String E1 = "#";
     String E2 = "$";
     
     String R1 = "№";
     String R2 = ";";    
    
        if (name.equals("F5")) { //ВВОД ШТРИХКОДА
        F[5].setBackground(Color.red);
        String SCAN=(String)GETF("F5").GETVALUE();
        SCAN=FF.ALLTRIM(SCAN);
        if (FF.LEN(SCAN)==0) {
            return true;
        }
        GETF("F5").SETVALUE("");
        //KAA 22082018 - добавил условие из-за махинаций на инвентаризациях
//        if (!SCAN.contains(E1) && !SCAN.contains(E2) && !SCAN.contains(R1) && !SCAN.contains(R2)) {
//            P.MESSERR("Возможен ввод только с ручного сканера!");
//            return true;
//        }
        SCAN=SCAN.replaceAll(E1,"");
        SCAN=SCAN.replaceAll("\\"+E2,"");
        SCAN=SCAN.replaceAll(R1,"");
        SCAN=SCAN.replaceAll(R2,"");
            
        if (SCAN.length()!=20 && SCAN.length()!=22 && SCAN.length()!=13) {
         P.MESS(SCAN+" ДЛИНА:"+SCAN.length()+"\n"+"   Допустима длина 13-20-22  ");
         return true;
        }
        cipher.barcodeList.clear();  //чистим 
        cipher.barcodeList.add(SCAN+"  "); //строку в список штрихкодов        
        V.FALSETRUE = true;
        if (cipher.THISFORM.RUNFORM!=null) {  //если есть метод в форме вызвавшую терминальную программу для обработки принятых штрихкодов
          cipher.THISFORM.RUNFORM.SAVE_TER(cipher.THISFORM);
         }
        
        }
        return true;
        
    }
   public void DBLCLICK_ALL(String name) {
    
    
        if ("f_folder".equals(name)) { //считывание из файла
        String file=""    ;
        String str=""    ;
        V.FALSETRUE = false;
        file=P.GETFILE("Выбор файла для имуляции сброса штрихкодов с терминала","","",F[2].getText());
        if (FF.EMPTY(file)){
            return;
        }
        cipher.barcodeList.clear();  //чистим 
        BufferedReader reader=null;
            try {
                reader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CipherForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                while ((str = reader.readLine()) != null) { // читаем строку пока не кончатся
                    cipher.barcodeList.add(str+"  "); //строку в список штрихкодов
                            
                }   } catch (IOException ex) {
                Logger.getLogger(CipherForm.class.getName()).log(Level.SEVERE, null, ex);
            }
                    V.FALSETRUE = true;

                        if (cipher.THISFORM.RUNFORM!=null) {  //если есть метод в форме вызвавшую терминальную программу для обработки принятых штрихкодов
                             cipher.THISFORM.RUNFORM.SAVE_TER(cipher.THISFORM);
                         }

        }  //считывание из файла
    
    }    // для дочерних объектов на двойной click мышки

    @Override
    public void OPEN() {
        ScanerAdapter adapter = new ScanerAdapter(this);

        for (int i = 0; i < getContentPane().getComponentCount(); i++) {
            if (!getContentPane().getComponent(i).getClass().getName().equals("javax.swing.JScrollPane")) {
                getContentPane().getComponent(i).addKeyListener(adapter);
            } else {
                for (int j = 0; j < ((JScrollPane) getContentPane().getComponent(i)).getComponentCount(); j++) {
                    if (((JScrollPane) getContentPane().getComponent(i)).getComponent(j).getClass().getName().equals("javax.swing.JViewport")) {
                        ((JViewport) ((JScrollPane) getContentPane().getComponent(i)).getComponent(j)).getView().addKeyListener(adapter);
                    }
                }
            }
        }
        super.OPEN(); 
    }
   
   
    
}
