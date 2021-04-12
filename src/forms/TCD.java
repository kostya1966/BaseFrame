package forms;
import aplclass.OracleTypes;
import aplclass.SerialPortRKV;
import baseclass.Formr;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import jssc.SerialPortException;
import prg.FF;
import prg.P;
import prg.V;
//import prg.VM;
    
public class TCD extends Formr {  //    ИМИТАЦИЯ ТЕРМИНАЛА СБОРА    пока сделано на  несколько портов на будущее 
 protected String b31= "4.Настройка";
 protected String b32= "4.Сохранить";
 protected String b01= "1.На считывание      ";
 protected String b02= "1.Выход из считывания";

    public boolean KEY_ON=false; //СЧИТЫВАТЬ В БУФЕР КЛАВИАТУРУ АКТИВЕН (true) ИЛИ НЕТ (false)
    public String KEY_SCAN=""; //БУФЕР ШТРИХКОДА  ИЗ КЛАВИАТУРЫ
    
    public String FILEMEM; //НАЗВАНИЕ ФАЙЛ КОНФИГУРАЦИИ
    public String[] PORT_NOMER; //НОМЕРА ТЕРМИНАЛА
    public String[] PORT_NAME; //ПОРТЫ
    public String NAME_TCD;  //ИМЯ ТЕРМИНАЛА СБОРА ДАННЫХ
    public int KOLPORT;  //КОЛИЧЕСТВО ПОРТОВ
    public String STROP;  //СТРОКА ОПЕРАЦИЙ
    public String VO; //ТЕКУЩАЯ ОПЕРАЦИЯ    
    public String NDOC; //НОМЕР ДОКУМЕНТА
    public static boolean PRINT=true;
   

 public  SerialPortRKV[] port;
// public  String[] COM; //массив имен портов портов
 public  String IP="";
 protected int pass=0;
 public  Formr FORM;
 
  
  public String NVO=""; //НАЗВАНИЕ ОПЕРАЦИИ
  public String NVO_NDOC=""; //СТРОКА НА ВВОД ДОКУМЕНТА
  public String AL1,AL2;
  public String DEL="0"; //ВСТАВКА УДАЛЕНИЕ
  public String SCREEN="1"; //ОПЕРАЦИЯ
 // public String ROWNUM1="1000"; //при просмотре
 // public String ROWNUM2="21"; //при считывании
 // public String ROWNUM=ROWNUM1; //текущее
  public String STAT="STAT2"; //СТАТУС 
  public String MOD="HTM2"; //МОДЕЛЬ СКАНЕРА
  public String STROUT="";
        public    String[] STROP1=  new String[16]; //список операций
        public    String[] STROP2=  new String[16]; //предложение вводв значения документа для операции 
    public String NWIFI="";
    public String STRTO="";  //ПОСЫЛАЕМАЯ СТРОКА
    public String STROT="";  //ПОЛУЧАЕМАЯ СТРОКА
    public String PR_ERR="0";  //ПРИЗНАК ВОЗВРАТА ОШИБКИ

  public String TITLE="Терминал сбора данных (Эммуляция)"; //СТАТУС 
 
public TCD() {   //ДЛЯ ВЫЗОВА ФОРМЫ  
    super("TDC","",840,660); 
    IP = V.IP ;
    FORM=THISFORM;
    FILEMEM="TCD";
    String[] REST=  P.RESTOREMEM(FILEMEM+".MEM", 5); //  читаем основной файл конфигурации
        NAME_TCD=REST[0]; //имя тсд
        KOLPORT=FF.VAL(REST[1]); //количество портов
        STROP=REST[2];  //СТРОКА ОПЕРАЦИЙ
        VO=REST[3];; //ТЕКУЩАЯ ОПЕРАЦИЯ    
        NDOC=REST[4]; //НОМЕР ДОКУМЕНТА
        if (FF.EMPTY(NAME_TCD)) {NAME_TCD="Терминал сбора данных";}
        if (KOLPORT==0) {KOLPORT=1;}
        PORT_NOMER= new String[50]; // резерв по 50 портов
        PORT_NAME= new String[50];
      IP = V.IP ;
      for (int i=0;i<KOLPORT;i++) { //для всез портов
        REST=  P.RESTOREMEM(FILEMEM+"_"+FF.STR(i)+".MEM", 2); // файл настройки порта
        PORT_NOMER[i]=REST[0]; //НОМЕР ТЕРМИНАЛА
        PORT_NAME[i]=REST[1]; //ПОРТ
        IP=IP+PORT_NAME[i];
      }
    port=new SerialPortRKV[KOLPORT]; // резервируем массив объектов портов    
    
    
}
public  TCD(Formr FORM,String... com) {  //ДЛЯ ВЫЗОВА КАК ОБЪЕКТ КЛАССА
    KOLPORT=com.length    ;
    port=new SerialPortRKV[KOLPORT];    
    PORT_NOMER= new String[KOLPORT]    ;
    PORT_NAME= new String[KOLPORT]    ;
    IP = V.IP ;
    for (int i=0;i<KOLPORT;i++) {
     PORT_NAME[i] = com[i];
     PORT_NOMER[i] = FF.STR(i+1);
     IP=IP+PORT_NAME[i];
    }
    this.FORM=FORM;
}
    
    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
     B[0] =P.addobjB(this, "B0", b01, "Переход в режим считывания");  B[0].SETALTKYE(1);
     B[1] =P.addobjB(this, "B1", "Выбор операции", "Выыбор операции и вводо номера документа "); B[1].SETALTKYE(2);
     B[2] =P.addobjB(this, "B2", "Сервис", "Включение-выключение режима  удаления ");  B[2].SETALTKYE(3);
     B[2].setEnabled(false);     
     B[3] =P.addobjB(this, "B3",b31, "Настройка соединения и порта");  B[3].SETALTKYE(4);
     B[4] =P.addobjB(this, "B4", "Смена пользователя", "Ввод пароля пользователя и загрузка разрешенных видов операций");  B[4].SETALTKYE(5);
     B[5] =P.addobjB(this, "B5", "Очистить буфер", "Очистить историю считанных штрихкодов");  B[5].SETALTKYE(6);
        
        T[0] = P.addobjT(this, "T0", "", 200, 200); //буфер считаных штрихкодов
        T[0].SETREADONLY(true);
        T[0].setFont(new Font("Arial", Font.BOLD, 12));
       // F[0] = P.addobjF(this, "F0", "", 200, 30); //поле имитации считывания
      //  F[0].setVisible("COM0".equals(COM[0].toUpperCase()));
        E[0] = P.addobjE(this, "OUT_SCREEN", "", 300, 200);
        E[0].SETREADONLY(true);
        E[0].setContentType("text/html"); //как браузер html кода
      //  E[0].setFont(new Font("Arial", Font.BOLD, 16));        
        //E[0].setText("<style fontName='Arial' size='23' isBold='true'>"+"1234567"+"</style>");
        //E[0].setText("<html>\n" +"<font face='Verdana' size=5 color='red'>      Этот текст красный, размера 5.      </font><br></html>");
        E[0].setText("");   
        int i=-1;
        i++; //0
        L[i] = P.addobjL(this, "LKOLPORT", "Кол-во портов:");
        F[i] = P.addobjF(this, "KOLPORT", FF.STR(KOLPORT), 40, 30);
        F[i].SETREADONLY(true);
        i++; //1
        L[i] = P.addobjL(this, "LPORT_NOMER", "Номер п.п. порта:");
        F[i] = P.addobjF(this, "PORT_NOMER", PORT_NOMER[0], 40, 30);
        F[i].SETREADONLY(true);
        i++; //2
        L[i] = P.addobjL(this, "LPORT_NAME", "Порт сканера:");
        L[i].SETICON();
        F[i] = P.addobjF(this, "PORT_NAME", PORT_NAME[0], 100, 30);
        F[i].SETREADONLY(true);

        i++; //3
        L[i] = P.addobjL(this, "LNVO", "Текущая операция:");
        F[i] = P.addobjF(this, "NVO", NVO, 250, 30);
        F[i].SETREADONLY(true);
        i++; //4
        L[i] = P.addobjL(this, "LNDOC", NVO_NDOC);
        F[i] = P.addobjF(this, "NDOC", NDOC, 80, 30);
        F[i].SETREADONLY(true);
        i++; //5
        F[i] = P.addobjF(this, "STRIN", "", 280, 30);
        F[i].SETREADONLY(true);
        CH[0] = P.addobjCH(this, "CH0", "Режим удаления");     

        
       GETSTROP(STROP);
       SETTITLE();
    }
    
    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга
      if (T[0].SCROLL!=null) { //буфер штрихкодов
            T[0].SCROLL.setBounds(1,1, this.getWidth()/10*2, this.getHeight()-280);
        }        
      if (E[0].SCROLL!=null) {
            E[0].SCROLL.setBounds(T[0].SCROLL.getWidth()+10, 1, this.getWidth()/10*8-10,this.getHeight()-210);
        }        
     locate(B[5],null,V.LOC_LEFT,0,T[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(CH[0],null,V.LOC_LEFT,0,B[5],V.LOC_DOWN,0) ;  
     locate(B[0],null,V.LOC_LEFT,0,CH[0],V.LOC_DOWN,0) ;  
     locate(B[1],null,V.LOC_LEFT,0,B[0],V.LOC_DOWN,0) ;  
     locate(B[2],null,V.LOC_LEFT,0,B[1],V.LOC_DOWN,0) ;  
     locate(B[3],null,V.LOC_LEFT,0,B[2],V.LOC_DOWN,0) ;  
     locate(B[4],null,V.LOC_LEFT,0,B[3],V.LOC_DOWN,0) ;  
     
     B[0].setSize(T[0].SCROLL.getWidth(),B[0].getHeight() );
     B[1].setSize(T[0].SCROLL.getWidth(),B[1].getHeight() );
     B[2].setSize(T[0].SCROLL.getWidth(),B[2].getHeight() );
     B[3].setSize(T[0].SCROLL.getWidth(),B[3].getHeight() );
     B[4].setSize(T[0].SCROLL.getWidth(),B[4].getHeight() );
     
 LOCOBJR=B[3]; LOCOBJD=B[2]; int xy=10;
 locr(GETL("LKOLPORT"),xy);  locr(GETF("KOLPORT"),0) ;  locr(GETL("LPORT_NOMER"),xy);  locr(GETF("PORT_NOMER"),0) ; locr(GETL("LPORT_NAME"),xy);  locr(GETF("PORT_NAME"),0) ;
 LOCOBJR=B[1]; LOCOBJD=B[0];  xy=10;
 locr(GETL("LNVO"),xy);  locr(GETF("NVO"),0) ;  locr(GETL("LNDOC"),xy);  locr(GETF("NDOC"),0) ;          

LOCOBJR=B[0]; LOCOBJD=B[0];  xy=10;
  locr(GETF("STRIN"),0) ; 
   F[4].setSize(E[0].SCROLL.getWidth(),F[4].getHeight() );     
    }
    
  @Override    
  public void OPEN(){
  }
/**
 * Проверка на открыт или нет порт ерез com
 * http://javadox.com/org.scream3r/jssc/2.8.0/javadoc/jssc/SerialNativeInterface.html#getLinesStatus(long) описание методов
 * @return 
 */    
  public boolean PORTVALID(){
      for (int i=0;i<KOLPORT;i++) {
          if (PORT_NAME[i]!=null && !"".equals(PORT_NAME[i]) && !"COM0".equals(PORT_NAME[i].toUpperCase()) && !"KEY".equals(PORT_NAME[i].toUpperCase()) ) {
              if (port[i]==null) {
                  return false;
              }
               if  ( !port[i].isOpened()) { //если порт не удалось открыть 
                 PORTCLOSE(); 
                 PORTOPEN();
              }
          }
          
      }
      return true;
  }

  
  public boolean PORTOPEN(){
      for (int i=0;i<KOLPORT;i++) {
          if (PORT_NAME[i]!=null && !"".equals(PORT_NAME[i]) && !"COM0".equals(PORT_NAME[i].toUpperCase()) && !"KEY".equals(PORT_NAME[i].toUpperCase()) ) {
              port[i]= new SerialPortRKV(PORT_NAME[i],FORM);          
              try {              
                  port[i].setEventsMask (SerialPortRKV.MASK_RXCHAR); /*Устанавливаем маску или список события на которые будет происходить реакция. В данном случае это приход данных в буффер порта*/
                  port[i].print=PRINT;
              } catch (SerialPortException ex) {
                      System.out.println("Маска порта "+PORT_NAME[i]+" не установлена ");
              }
              if (!port[i].isOpened()) { //если порт не удалось открыть выход
                  port[i]=null;
                  return false;
              }
          }
          if (PORT_NAME[i]!=null && "KEY".equals(PORT_NAME[i].toUpperCase()) ) {//ЕСЛИ СКАНЕР В РАЗРЫВ КЛАВИАТУРЫ
              KEY_ON =true;
          }
          
      }
      return true;
  }
  public boolean PORTCLOSE(){
      for (int i=0;i<KOLPORT;i++) {
     try {
          
          if (PORT_NAME[i]!=null && !"".equals(PORT_NAME[i]) && !"COM0".equals(PORT_NAME[i].toUpperCase()) && !"KEY".equals(PORT_NAME[i].toUpperCase()) ) {
              if (port[i]==null  )
              {              } else {
                  if (port[i].isOpened()) {
                      //если порт не удалось открыть выход
                      port[i].closePort();
                      port[i]=null;
                      System.out.println("Закрыт порт "+PORT_NAME[i]+" для формы "+THISFORM.getName());
                  }
               }
      }
     }   catch (Exception e) {
        System.out.println("Ошибка закрытия порта "+PORT_NAME[i]+" для формы "+THISFORM.getName());
         return false;
     }
          if (PORT_NAME[i]!=null && "KEY".equals(PORT_NAME[i].toUpperCase()) ) {//ЕСЛИ СКАНЕР В РАЗРЫВ КЛАВИАТУРЫ
              KEY_ON =false;
          }
     
     }
      return true;
  }
  public boolean PREV_CLICK_ALL() {//ВЫПОЛНЯЕТСЯ ПЕРЕД НАЖАТИЕМ КНОПОК
      return true;
  }    
   @Override
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
         if (!PREV_CLICK_ALL()) {
             return;
         }
         if (GETF("STRIN").equals(name)) {//ПРОСМОТР РЕЗУЛЬТАТА
             FF._CLIPTEXT(STROT);
             P.MESS(STROT);
         }
         
         if ("B0".equals(name)) {//ВОЙТИ В РЕЖИМ СЧИТЫВАНИЯ
           if (b01.equals(GETB("B0").getText())) {
               //открываем порты
               GETB("B1").setEnabled(false); //выбор операции не доступно
               GETB("B3").setEnabled(false); // не доступно
               GETB("B4").setEnabled(false); // не доступно
               CH[0].setEnabled(false);
               //https://habrahabr.ru/post/133766/
              if (!PORTOPEN()){
                  return;
              }
//           System.out.println("Открыт порт "+COM+" для формы "+THISFORM.getName());
           GETB("B0").setText(b02); //МЕНЯЕТ ТЕКСТ НА КНОПКЕ 
           if (!CH[0].GETVALUE()) { // режим
            DEL="0"; //ВСТАВКА
           }  else {
            DEL="1"; //УДАЛЕНИЕ
           }
           STRTO="STAT1"+MOD+VO+DEL+NDOC+";;1;"+V.USER_KWIFI+";"+V.MAC+";" ;
           TERM_IMIT(IP,STRTO); // ЗАПРОС НА БАЗУ И РЕЗУЛЬТАТ НА T1
         // ROWNUM=ROWNUM2;
           STAT="STAT2";
           } else { //закрываем порты
              if (!PORTCLOSE()){
                  return;
              }
           GETB("B0").setText(b01);
           GETB("B1").setEnabled(true);
           GETB("B3").setEnabled(true);
           GETB("B4").setEnabled(true);
           CH[0].setEnabled(true);
           
           STRTO="STATQ"+MOD+VO+DEL+"0;" ;   
           TERM_IMIT(IP,STRTO); //  окончание считывания
           CH[0].SETVALUE(false); // в режим вставки
          // ROWNUM=ROWNUM1;
         }
                   QUERY(G[0]);
        }
         
         if ("B1".equals(name)) {//выбор операции
            int BAR1 = P.MENU(STROP1, B[1], new java.awt.Font("Arial", 1, 16));
            if (BAR1 == 0) {
                return;
            }
            VO=FF.STR(BAR1);
            NDOC="0";
            if (!STROP2[BAR1-1].equals("0")) { //ЕСДИ НУЖНО ВВЕСТИ НОМЕР ДОКУМЕНТА
            P.DOCORRECT("Введите данные:",new Object[]{STROP2[BAR1],V.TYPE_CHAR,"",NDOC});
            if (V.PARAMOT == null || V.PARAMOT[0].equals("F")) {
                return;
                        };
                NDOC=V.PARAMOT[1];
            }
            SETTITLE();
            SAVE();

         }
         
         if ("B3".equals(name)) {//Настройка
           if (b31.equals(GETB("B3").getText())) {
               GETF("KOLPORT").SETREADONLY(false);
               GETF("PORT_NOMER").SETREADONLY(false);
               GETF("PORT_NAME").SETREADONLY(false);
               GETB("B3").setText(b32);
               F[0].requestFocus();
           } else  {//сохранить
               GETF("KOLPORT").SETREADONLY(!false);
               GETF("PORT_NOMER").SETREADONLY(!false);
               GETF("PORT_NAME").SETREADONLY(!false);
               SAVE();
               GETB("B3").setText(b31);
               SETTITLE();  
           }
         }
        
             
         if ("B5".equals(name)) {//очистить буфер штрихкодов
           GETT("T0").setText("");
         }
         if ("B4".equals(name)) {//Для кнопки B4
            P.DOCORRECT("Введите код пользователя",new Object[]{"Пароль:",V.TYPE_NUMERIC,"999",pass});
            if (V.PARAMOT == null || V.PARAMOT[0].equals("F")) {
                return;
                        };
             pass=FF.VAL(V.PARAMOT[1]) ;
             STRTO="STAT0"+MOD+pass+";"+V.VER+";" ;
             TERM_IMIT(IP,STRTO); //  окончание считывания              
             if (STAT.equals("STAT9")) {
               P.PLAY_SOUND("errbax.wav");       // звук пулемета
               E[0].setText("Ошибка идентификации пользователя с кодом "+pass); 
             }
          STROP=STROUT;   
          GETSTROP(STROP);
          SAVE();
          P.PLAY_SOUND("alarm.wav");       // выполнено
         }

         
    }
    public void SAVE() {
                   P.SAVETOMEM(FILEMEM+".MEM",new String[]{NAME_TCD,FF.STR(KOLPORT),STROP,VO,NDOC}); 
      for (int i=0;i<KOLPORT;i++) {
                   P.SAVETOMEM(FILEMEM+"_"+FF.STR(i)+".MEM",new String[]{PORT_NOMER[i],PORT_NAME[i]}) ; 
      }
    }
/**
 * Разложение на строки строку полученную как описание операций
 * @param STR - строка операций
 */
    public void GETSTROP(String STR) {
        if (FF.EMPTY(STR)) {
            NWIFI="";
        }
          NWIFI  =FF.SUBSTR(STR,1,FF.AT(";",STR)-1);
          STR=FF.SUBSTR(STR,FF.AT(";",STR)+1);
          for (int i=0;i<16;i++) {
              STROP1[i]=FF.SUBSTR(STR,1,FF.AT(";",STR)-1);
              STR=FF.SUBSTR(STR,FF.AT(";",STR)+1);
          }
          for (int i=0;i<16;i++) {
              STROP2[i]=FF.SUBSTR(STR,1,FF.AT(";",STR)-1);
              STR=FF.SUBSTR(STR,FF.AT(";",STR)+1);
          }
        SETTITLE();
    }
    
    @Override
    public void DESTROY() {
           PORTCLOSE(); //ЗАКРЫТИЕ ПОРТОВ
           SAVE()           ;
        super.DESTROY(); //To change body of generated methods, choose Tools | Templates.
    }
/**
 * ВЫПОЛНЯЕТСЯ ПРИ СЧИТЫВАНИИ 
 * @param name - ИМЯ ПОРТА
 * @param SCAN - ШТРИХКОД
 */               
      @Override
    public void EVENT(String name,String SCAN) {// вызывается при получении штрихкода
 //   System.out.println("Событие на порт "+name+" для формы "+THISFORM.getName()+" "+SCAN);
    SCAN=FF.ALLTRIM13(SCAN);
    if ("".equals(SCAN)) {
        SCAN="ПУСТО";
    }
    // УБИРАЕМ ДОПОЛНИТЕЛЬНЫЙ СИМВОЛЫ
     String E1 = "#";
     String E2 = "$";
     String R1 = "№";
     String R2 = ";";    
        SCAN=SCAN.replaceAll(E1,"");
        SCAN=SCAN.replaceAll("\\"+E2,"");
        SCAN=SCAN.replaceAll(R1,"");
        SCAN=SCAN.replaceAll(R2,"");
        
    T[0].setText(T[0].getText()+SCAN+"\n"  );
    T[0].setCaretPosition(T[0].getText().length());
    //STAT282002011000000000047;80;   
     TERM_IMIT(IP,STAT+MOD+VO+DEL+SCREEN+SCAN); // ЗАПРОС НА БАЗУ И РЕЗУЛЬТАТ НА T1 В РЕЖИМЕ СЧИТЫВАНИЯ
    }
    /**
     * ВЫПОЛНЕНИЕ ПРОЦЕДУРЫ НА СЕРВЕРЕ И  ПОЛУЧЕНИЕ РЕЗУЛЬТАТА
     * @param IP - IP УСТРОЙСТВА (УНИКАЛЬНЫЙ КЛЮЧ)
     * @param STRIN - СТРОКА ПОСЫЛАЕМАЯ 
     * @return 
     */
      public boolean TERM_IMIT(String IP,String STRIN) {
         try {
      if (GET("STRIN",F)!=-1) {      //ЕСЛИ ОБЪЕКТ СУЩЕСТВЕТ
          GETF("STRIN").setText(STRIN);
      }
      CallableStatement cstmt = V.CONN.prepareCall("{call pwifi(?, ?, ?)}");
      cstmt.setString(1, IP);
      cstmt.setString(2, STRIN);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.execute();
      STROUT=cstmt.getString(3);
      STROT=STROUT;
      cstmt.close();
   }
   catch (Exception e) {
//      e.printStackTrace();
    System.out.println("Ошибка запроса: ");
    System.out.println(e.toString());
    P.MESSERR("       Ошибка запроса: "+IP+" "+STRIN+"\n"+e.toString());
    STROUT="";
    return false;
     }                
    STAT=FF.SUBSTR(STROUT, 1, 5); //ПОСЫЛАЕМЫЙ СТАТУС
    STROUT=FF.SUBSTR(STROUT, 6);
    PR_ERR=FF.SUBSTR(STROUT, 1, 1); //0- НОРМАЛЬНОЕ ЗАВЕРШЕНИЕ 1- ПРИЗНАК ОШИБКИ
    STROUT=FF.SUBSTR(STROUT, 2);
     if ("1".equals(PR_ERR)){ //ЕСЛИ ПЕРЕДАЛО ОШИБКУ
       P.PLAY_SOUND("errbax.wav");       // звук пулемета
     } else {
       P.PLAY_SOUND("alarm.wav");       // выполнено
     }
     if (GET("OUT_SCREEN",E)!=-1) { 
      E[0].setText(STROUT); 
     }
     //FF._CLIPTEXT(STROUT);
     return true;
    }

    

    public void SETTITLE() {
        this.setTitle(TITLE+" "+IP+" "+NWIFI);        
        if (!FF.EMPTY(VO)) {
            NVO=STROP1[FF.VAL(VO)-1];
            NVO_NDOC=STROP2[FF.VAL(VO)-1];
            GETF("NVO").setText(NVO);
            //F[2].RESIZE();
            GETL("LNDOC").setText(NVO_NDOC);
            GETL("LNDOC").RESIZE();
            GETF("NDOC").setText(NDOC);
              GETL("LNDOC").setVisible(!NVO_NDOC.equals("0"));
              GETF("NDOC").setVisible(!NVO_NDOC.equals("0"));
            LOC_ABOUT();
        }
                
        return ;
    }
 
    @Override
    public void DBLCLICK_ALL(String name) {
        if (FF.SUBSTR(name,1,4).equals("PORT") )  { //порты
          String[] portNames;
          portNames = P.GET_COM_PORTS();
          int  n = P.MENU(portNames, GETF(name),new java.awt.Font("Arial", 1, 16));
            if (n == 0) {
                return;
            }
          GETF(name).SETVALUE(portNames[n-1]); // пишем в поле
         int i=FF.VAL(GETF("PORT_NOMER").getText()); // номер порта
         PORT_NAME[i-1]=GETF("PORT_NAME").getText() ;// пишем в массив по номеру порта его наименование
          
        }
    
        }    

    @Override
    public boolean VALID_ALL(String name) {
     if (name.equals("FSCAN") && !"".equals(GETF("FSCAN").getText().trim())   ) {
      if   (  VALIDON==false) {return true;}        
        EVENT(name,GETF("FSCAN").getText()) ;
     }
        
     if (name.equals("KOLPORT")   ) {
         int KOL=FF.VAL(GETF("KOLPORT").getText());
         if (KOL<1) {
             P.MESSERR("Номер больше общего количества портов");
             return false;
         }
         KOLPORT=KOL;
     }
        
     if (name.equals("PORT_NOMER")   ) {
         int kol=FF.VAL(GETF("KOLPORT").getText());         
         int i=FF.VAL(GETF("PORT_NOMER").getText());
         if (kol<i) {
             P.MESSERR("Номер больше общего количества портов");
             return false;
         }
         
         PORT_NOMER[i-1]=FF.STR(i);
         GETF("PORT_NAME").setText(PORT_NAME[i-1]) ;
     }
     if (name.equals("PORT_NAME")   ) {
         int i=FF.VAL(GETF("PORT_NOMER").getText());
         PORT_NAME[i-1]=GETF("PORT_NAME").getText();
     }
        
        
        return true;
    }

/**
 * Слушатель сканирования  через буфер кдавиатуры
 * @param obj - объект
 * @param e  - событие клавиши
 */  
    public void KEYPRESS(Component obj, KeyEvent e) {//метод выполняемый после нажатия клавиши на любом из элементов
//        String str=FF._CLIPTEXTOUT();
        boolean keyno=false;
        int key = e.getKeyCode();
        int SEC=0;
        if (!KEY_ON) { keyno=true;} //
//        System.out.println("имя:"+obj.getName());
//        System.out.println("имя:"+obj.getClass().getSimpleName());
//        System.out.println("код "+e.getKeyChar());
        String namec=obj.getClass().getSimpleName();
        
        if (!keyno && "Fieldr".equals(namec) && !(FORM.GETF(obj.getName())).READONLY) { //поле ввода 
            keyno=true;
        }
        if (!keyno && "Editr".equals(namec) && !(FORM.GETE(obj.getName())).READONLY) { //поле ввода 
            keyno=true;
        }
        if (!keyno && "Textr".equals(namec) && !(FORM.GETT(obj.getName())).READONLY) { //поле ввода 
            keyno=true;
        }
        
        if (!keyno &&  e.isAltDown() || e.isControlDown() || e.isShiftDown()) {
            keyno=true;
        }
        if (!keyno && key == V.KEY_ENTER && "".equals(KEY_SCAN)) {//если enter но последовательность пуста
            keyno=true;
        }
        if (!keyno && (e.isActionKey() && key != V.KEY_ENTER)) { //клавиша действие но не enter
            keyno=true;
        }
        
        if (keyno) { //если нет события считывания в буфер клавиатуры
//        System.out.println("имя:"+obj.getName());
        super.KEYPRESS(obj, e);
        KEY_SCAN="";
        return;
        }
        if (!keyno && key == V.KEY_ENTER && !"".equals(KEY_SCAN)) {//если enter и  последовательность есть  то считан штрихкод
            FORM.EVENT("KEY",KEY_SCAN);
            KEY_SCAN="";
            return;
        }
        KEY_SCAN=KEY_SCAN+e.getKeyChar();
      //  SEC=FF.SECOND();
      //  System.out.println("сек:"+SEC);

        
    }

    
    
    
    
    
    
     }//окнчание класса
