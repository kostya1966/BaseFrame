package forms;
//http://javatutor.net/books/tiej/socket
//import aplclass.ServerS;
import baseclass.Formr;
import baseclass.Gridr;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;
import prg.FF;
import prg.P;
import static prg.P.SQLCONNECT;
import static prg.P.SQL_CREATE_STATEMENT;
import prg.V;
    
public class WEBS extends Formr {
    static final int PORT = 5552;
    //static final int PORT = 8080;
    static final int timeout = 5000;
    
    public boolean START = false; //ПРИЗНАК ОПРОСА ПОРТА 
    public Thread POTOK=null;
    public ServerSocket SS=null    ;
    public Map MAPIN_OUT = null; //карта колекция ПОТОКОВ загруженных соединений
    public String IPS="";
    public String TIP="1"; // ТИП ТЕРМИНАЛА 1- CHIPHER  0 - РУЧНОЙ ВВОД
    public String PROC="pwifi";
    
public WEBS() {  
     super("WEBS","WEB Сервер  по адресу:"+V.IP+":"+PORT+"        Версия N.1",900,600); 
     V.CONN_AUTO=true;
//   InetAddress addr=null;         
//   addr = InetAddress.getLocalHost();
//   IPS = addr.getHostAddress();  
   
   
}
    
    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
    //P.WRITE_INFO("Сообщение в инфопанели");        
        B[32] = P.addobjB(this, "B32", "Выбрать текущее значение", "Занести значение текущей записи в поле ввода"); //добавляем кнопку выбора текущей записи и закрытия формы
        B[32].setVisible(SPR); // если не справочник-выбора занчит не из поля ввода , значит невидимая
     B[0] =P.addobjB(this, "B0", "Запустить", "Начало опроса порта для соединения термилов по wifi"); 
     B[1] =P.addobjB(this, "B1", "Остановить", "Остановка сервера и закрытие соединений"); 
     B[2] =P.addobjB(this, "B2", "Настройка", "Настройки сервера терминалов"); 
     B[3] =P.addobjB(this, "B3", "Сервис", "Дополнительный функционал "); 
     
   //  G[0] =P.addobjG(this,"WIFI",1,1,880, 500); 
     T[0] = P.addobjT(this, "T0", "", 250, 80);
     T[0].SETREADONLY(true);
     T[1] = P.addobjT(this, "T1", "", 250, 80);
     T[1].SETREADONLY(true);
     T[2] = P.addobjT(this, "T2", "", 250, 80);
     T[2].SETREADONLY(true);

     
     F[0] = P.addobjF(this, "F0", "", 250, 40);
     F[0].SETREADONLY(true);
     
    }
    
    public  void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
     GRID.VIEWRECNO=0; //не показывать номер записи в первой колонке -0
     GRID.RESTORECOL=1;//не восстанавливать размеры -0 
     GRID.ALIAS = GRID.getName();     //ИМЯ ГРИДА И АЛИАС СОВПАДАЮТ 
//   GRID.FREAD="WIFI_K";//форма корректуры
     GRID.ENABLE_DEPLOY = false;  //вызов другой формы спецификации
    }
   @Override
    public  void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
String C[]={"IP адрес терминала","Ф.И.О.","Название пользователя","Серийный номер терминала","Номер операции","Номер документа","Время входа на считывание"};
String F[]={"IP","FIO","NWIFI","SNWIFI","VO","NDOC","IPLAST"};
int S[]={92,100,100,100,65,67,128};
     V.CAPTION=C;
     V.FIELD=F;    
     V.FSIZE=S;
    }
    
   @Override
    public  String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
    String SELE="SELECT *  FROM wifi ORDER BY IPLAST DESC";
    return SELE;
  }
    
   @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        V.SELE = "SELECT *  FROM wifi WHERE " + PREV_KEY_REC(GRID);// для вставки записи
        return V.SELE;
    }
    
    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга
  //    if (G[0].SCROLL!=null) {           G[0].SCROLL.setBounds(1, 1, this.getWidth()-20, this.getHeight()/2);        }
     F[0].setSize(this.getWidth()-B[0].getWidth()-B[1].getWidth()-30-B[2].getWidth()-30, F[0].getHeight()     );
     T[0].SCROLL.setBounds(1, this.getHeight()/2, this.getWidth()/3-60, this.getHeight()/2-80);     
     //T[1].SCROLL.setBounds(T[0].SCROLL.getWidth(), 40, this.getWidth()/3*2-10, this.getHeight()-40);     
     T[1].SCROLL.setBounds(1, 1, this.getWidth()-20, this.getHeight()/2);        
     T[2].SCROLL.setBounds(T[0].SCROLL.getWidth(), T[1].SCROLL.getHeight(), this.getWidth()/3*2-10, this.getHeight()/2-80);     
     
     locate(B[0],null,V.LOC_LEFT,0,T[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(B[1],B[0],V.LOC_RIGHT,0,T[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(F[0],B[1],V.LOC_RIGHT,0,T[0].SCROLL,V.LOC_DOWN,0);
     locate(B[2],F[0],V.LOC_RIGHT,0,T[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(B[3],B[2],V.LOC_RIGHT,0,T[0].SCROLL,V.LOC_DOWN,0) ;  
     
    }
    
  @Override    
  public void OPEN(){
  CLICK_ALL("B0");      
  //G[0].SETFOCUS();
  }
    
    @Override
    public boolean ISEDIT(Gridr GRID, int col) {//Разрешение на корректуру колонки
        return super.ISEDIT(GRID, col); //по умолчанию запроещено false
    }
      @Override
     public void CHANGERECNO(Gridr GRID,int row,int rec)   {//выполняется при изменении записи в гриде
     }
    
   @Override
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
         if ("B0".equals(name)) {
             POTOK= new LOC_SOCKET();
             START = true;
             POTOK.start();
        }             
         if ("B1".equals(name)) {
             try {
                 if (SS!=null) SS.close();
             } catch (IOException ex) {
                 Logger.getLogger(WEBS.class.getName()).log(Level.SEVERE, null, ex);
             }
             SS=null;
             POTOK=null;
             START = false;
             MAPIN_OUT =null;             
             System.out.println("Server STOP");
             F[0].setText("Server STOP инициирован пользователем");
             
        }             
             
           
        if ("B2".equals(name)) {//настройка
             Object[] objArr=null;
             objArr = new Object[4];
                objArr[0] = "Имя хранимой процедуры"; // заголовок
                objArr[1] = V.TYPE_CHAR; // тип данных
                objArr[2] = ""; //маска ввода
                objArr[3] = PROC ; // по умолчанию
            P.DOCORRECT("Введите данные ",objArr);
            if (V.PARAMOT == null || V.PARAMOT[0].equals("F")) {
                return;
                        };
              PROC=V.PARAMOT[1];
        }
        if ("B3".equals(name)) {//СЕРВИС
            String  FILE_ST= P.GETFILE("Выберите файл","","",V.CURRENT_PATH);
             if ("".equals(FILE_ST)) 
             {
                 return;
             }
             File file = new File(FILE_ST);
             V.CURRENT_PATH=file.getPath();
             int len=(int)file.length();
             String URLS="https://datarep.vit.belwest.com/file-manager/file";
           //  String URLS="https://192.168.9.100:5552/" ;
             String AUTH="Basic "+P.encodeBase64("dataapp:Aif8ahneichu"); 
             //String out=P.WEB_FILE("POST", URLS, file,AUTH,"RKV",file.getName());
             String out=P.WEB_FILE("DELETE", "https://datarep.vit.belwest.com/file-manager/RKV/TEST8.TXT", file,AUTH,"RKV",file.getName());
//public static String WEB_FILE(String  METOD, String URLS,File file,String AUTH,String path,String filename)  {             
             P.MESS(out);
        }
        
        
    }

    @Override
    public void DESTROY() {
        CLICK_ALL("B1");
        super.DESTROY(); //To change body of generated methods, choose Tools | Templates.
    }

    
 /**
  * Прослушивание порта и соединение с клиентом
  */
public class LOC_SOCKET extends Thread {
 
    @Override
    public void run() {
         MAPIN_OUT = new HashMap<>();                     
        try {
            ///////////////
         SS = new ServerSocket(PORT);
        } catch (IOException ex) {
            //Logger.getLogger(SWiFi.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString())            ;
            F[0].setText("Ошибка запуска сервера "+ex.toString());
            return ;
        }
      // THISFORM.setTitle("Сервер терминалов WiFi "+IPS);
       MAPIN_OUT = new HashMap<>();                     
       Socket socket=null;
       System.out.println("WEB Server START");
       F[0].setText("WEB Сервер START");
          while (START) {
             // Блокируется до возникновения нового соединения:
           try {
               socket = SS.accept(); // ждем соединения на этой команде 
               // соединение прошло 
               if (MAPIN_OUT!=null) { // если не отключили сервер                 
                String  IP=FF.SUBSTR(socket.getInetAddress().toString(),2); // адрес клиента
                IN_OUT obj=(IN_OUT)MAPIN_OUT.get(IP); // проверим если такой ip уже в соединении 
                if (obj!=null) { // если есть то закрываем 
                   System.out.println(IP +" удалить "+socket.toString()+obj.toString()); // сообщение
                    obj.RUN="0"; //устанавливаем признак на самоудаление
//                    obj.socket.close();
                } 
                MAPIN_OUT.put(IP, new IN_OUT(socket,THISFORM)) ;  //ОТКРЫТИЕ НОВОГО ПОТОКА С  СОЕДИНЕНИЕМ И ЗАПИСЬ В МАССИВ ПОТОКОВ
                
                F[0].setText("WEB SERVER Соединений: "+MAPIN_OUT.size()+"  "+FF.DATETIMES());
               }    
           } catch (SocketException ex) { // не дождались
               F[0].setText("Нет соединения.......");
           } catch (IOException ex) {
               Logger.getLogger(WEBS.class.getName()).log(Level.SEVERE, null, ex);
           }
  
//          SS.close();
  
//          SS.close();

       }
            
    
   }}
/**
 * Клиент 
 * Работа с соединением,получение , обработка и отправка данных
 */
 class IN_OUT extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String RECNO="";    
    private String IP="";    
    private String RUN="1";
    private String ERR="";
    
    public IN_OUT(Socket s,Formr form) throws IOException {
//       System.out.println("Соединение с "+socket.getInetAddress().toString());
       socket = s;
       IP=FF.SUBSTR(socket.getInetAddress().toString(),2);
       String str=form.T[0].getText()+"Канал с "+IP+"\n Открыт в "+FF.DATETIMES()+"\n";
       form.T[0].setText(str);
       start(); // вызываем run()
    }

    //    IN_OUT(SocketChannel channel, Formr THISFORM) {
      //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //}
    
    public void run() {
        
       try {
       in = new BufferedReader(new InputStreamReader(socket.getInputStream()) );  //поток получения данных
//       in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"Cp1251") );  
       // Включаем автоматическое выталкивание:
       //out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"Cp866")), true);
       out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"Cp1251")), true);
       // Если любой из вышеприведенных вызовов приведет к
       // возникновению исключения, то вызывающий отвечает за
       // закрытие сокета. В противном случае, нить
       // закроет его.
       Object[][] param = new Object[2][2];       //параметры для вызова ораклавской процедуры
         socket.setSoTimeout(timeout);
           
          while (START && RUN=="1" && socket.isConnected() && !socket.isClosed() && !socket.isInputShutdown()) {
             String STRIN="";
             String STROUT="";
             
         try {
                //System.out.println(FF.DATETIMEMS()+" : "+STRIN);          
//////                 STRIN = in.readLine(); //ЖДЕТ timeout ПОТОМ НА catch (SocketTimeoutException e)
             //
               int  kol = socket.getInputStream().available(); // ПРОВЕРЯЕМ ЕСТЬ ЛИ  ЧТО В ПОТОКЕ
               byte[] IN=new byte[0];
                if (kol > 3) { //ЕСЛИ  ЕСТЬ КОЛ-ВО БАЙТ 2 байта контрольной суммы и еще хотя  бы один  байт
                   IN = new byte[kol]; //СОЗДАЕМ МАССИВ НА ЭТО КОЛ-ВО БАЙТ
                    for (int j = 0; j < kol; j++) {  // ЗАМЕНИЛИ   input.read(IN);
                        IN[j] = (byte) socket.getInputStream().read(); // БЕРЕМ ПОБАЙТОВА
                        }
                    }
              STRIN=new String(IN,"UTF-8"); // байты в строку UTF-8
                 if (FF.EMPTY(STRIN)) { //если пусто
                     continue;
                 }
              STRIN = URLDecoder.decode(STRIN, "UTF-8"); // раскодировка от htttp в нормальгный вид
              String path=FF.SUBSTR_BETWEEN(STRIN,"GET ","?");
              String ShopId=FF.SUBSTR_BETWEEN(STRIN,"ShopId=","&");                      
              String Query=FF.SUBSTR_BETWEEN(STRIN,"Query=","&");                      
              String P1=FF.SUBSTR_BETWEEN(STRIN,"P1="," HTTP/");                      
              String in=STRIN+"\n" ; // +"path:"+path+"\n"+"ShopId:"+ShopId+"\n"+"Query:"+Query+"\n"+"P1:"+P1+"\n";
              String old=THISFORM.T[1].getText();
              THISFORM.T[1].setText(old+in);                              
              
              /*
           
http://192.168.9.100:5552/Service_BW/MainPostal?ShopId=AND1&Query=045&P1="APP":"1.03008","ARM":"7","MAC":"868225043811219_1","STEP":"1","TASK":"MPU100","TEXT":""
(192.168.9.106  24.10.2019 13:53:48.800  размер:319 )
GET /Service_BW/MainPostal?ShopId=AND99&Query=045&P1={"APP":"1.03008","ARM":"7","MAC":"868225043811219_1","STEP":"1","TASK":"MPU100","TEXT":""} HTTP/1.1
User-Agent: Dalvik/2.1.0 (Linux; U; Android 8.1.0; MI PAD 4 PLUS MIUI/V10.3.2.0.ODJCNXM)
Host: 192.168.9.102:5551
Connection: Keep-Alive
Accept-Encoding: gzip

HTTP/1.1 200 OK
Date: Mon, 27 Jul 2009 12:28:53 GMT
Server: Apache/2.2.14 (Win32)
Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT
Content-Length: 88
Content-Type: text/html
Connection: Closed
<html>
<body>
<h1>Hello, World!</h1>
</body>
</html>



              
              */
             //  
            //  FF._CLIPTEXT(STRIN);
                          
             //RECNO=FF.SUBSTR(STRIN,1,3);
             //STRIN=FF.SUBSTR(STRIN,4);
//             System.out.println(IP + " "+RUN+" "+socket.toString()+this.toString());
         if ("1".equals(V.CONN_DRIVER)) { //для орасле
         try {
            if (V.CONN.isValid(2) == false) {
                THISFORM.T[1].setText(" Соединение разорвано... Восстановление");
                    V.CONN_OUT=false;
                    V.CONN_REP=0;
                V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                V.CONN = V.CONN1;
                V.SQL = null;
               SQL_CREATE_STATEMENT(V.CONN);
            }
          
      CallableStatement cstmt = V.CONN.prepareCall("{call "+PROC+"(?, ?, ?)}");
      //CallableStatement cstmt = V.CONN.prepareCall("{call RKV_ALL(?, ?, ?)}");            
      cstmt.setString(1, ShopId); //"192.168.9.110" - ip терминала
      cstmt.setString(2, P1); //
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.execute();
      STROUT=cstmt.getString(3);  //
         
String response = "HTTP/1.1 200 OK\n";
response += "Last-Modified: " + FF.DATETIMES() + "\n";
response += "Content-Length: " + STROUT.length() + "\n";
response += "Content-Type: text/html"  + "\n";
response +="Connection: close\n";
response += "link rel=\"shortcut icon\" href=\"data:image/x-icon;,\" type=\"image/x-icon\""+ "\n";  
response += "Server: Server\n\n";            
response +=STROUT;
out.println(response);  //      
System.out.println(response);          
              old=THISFORM.T[1].getText();
              THISFORM.T[1].setText(old+response+"\n");                              

RUN="0"; //устанавливаем признак на самоудаление
   }
   catch (Exception e) {
    System.out.println("Ошибка запроса: " + e.toString());
    STROUT="";
    ERR=e.toString();
    
    
     }                
                
                
            }
             

                    }
                  catch (SocketTimeoutException e) {
                  System.out.println("Время ожидания приема истекло "+e.toString()+"\n "+socket.toString()+this.toString());
                  }
         
             catch (Exception e) {
             ERR=e.toString();
             THISFORM.T[2].setText("("+IP+"  "+FF.DATETIMEMS()+" Ошибка :"+STROUT.length()+" " +")\n"+ERR);                              
                   }
             
          } //ЦИКЛ ОПРОСА на входящие данные
               System.out.println(IP + " "+RUN+" "+socket.toString());          
               socket.close();
       }
       catch (IOException e) {
          System.err.println("Окончание соединения "+e.toString());
       }
    }
 }
 
 
 
 
 
 
    }//окнчание класса



