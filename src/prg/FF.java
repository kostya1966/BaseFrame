package prg;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Набор часто используемых функций в основном дублирование из java
 * использование не обязательно для бывших разработчиков на dbase
 *
 * @author Kostya
 */
public class FF {
    //Строковые
/**
 * Подстрока из строки str начиная с позиции posl на количество символов kol
 * @param str
 * @param pos1
 * @param kol
 * @return 
 */
    public static String SUBSTR(String str, int pos1, int kol) {
        if (str.length() == 0 || pos1 <= 0 || kol <= 0) {
            return "";
        }
        if (str.length() < pos1 - 1 + kol) {
            kol = str.length() - pos1 + 1;
            
        }

        return str.substring(pos1 - 1, pos1 - 1 + kol);
    }

    public static String SUBSTR(String str, int pos1) {
        return str.substring(pos1 - 1);
    }
    /**
     * Удаляет в строке str подстроку начинающуюся с подстроки str_h и заканчивающуюся подстрокой str_e и возвращает результат
     * @param str
     * @param str_h
     * @param str_e
     * @return 
     */
    public static String SUBSTR_DEL(String str, String str_h, String str_e) {
         int POS1=0,POS2=0,POS=0;
         String STR1="",STR2="";
         POS1=FF.AT(str_h,str); // начало блока
         if (POS1==0) {
             return str;
         }
         POS2=FF.AT(str_e,str,POS1+str_h.length()); //конец блока поиском с позиции больше начала блока
         STR1=FF.SUBSTR(str,1,POS1-1); // подстрока до блока
         if (POS2>POS1 ) {  //ЕСЛИ ОКОНЧАНИЯ БЛОКА НАЙДЕНО
         STR2=FF.SUBSTR(str,POS2+str_e.length()); // подстрока до блока
         }
        return STR1+STR2;
    }

    /**
     * Выделяет в строке str подстроку между подстрок str_h и  str_e и возвращает результат
     * @param str
     * @param str_h есди null то с 1 позициуй
     * @param str_e есди null то до конца строки
     * @return 
     */
    
    public static String SUBSTR_BETWEEN(String str, String str_h, String str_e) {
         int POS1=0,POS2=0,POS=0;
         String STR1="",STR2="";
         if (str_h==null) {
           POS1=1;  
         } else {
         POS1=FF.AT(str_h,str); // начало блока
         }
         if (POS1==0) {
             return "";
         }
         POS1=POS1+str_h.length();
        if (str_e==null) {
         POS2=str.length()+1; //конец блока поиском с позиции больше начала блока
        } else  {
         POS2=FF.AT(str_e,str,POS1); //конец блока поиском с позиции больше начала блока
        }
         if (POS2<POS1 ) {  //ЕСЛИ ОКОНЧАНИЯ БЛОКА НАЙДЕНО
          return "";
         }
         
         STR1=FF.SUBSTR(str,POS1,POS2-POS1); // подстрока до блока
        return STR1;
    }
 
/**
 * Убрать крайние пробелы
 * @param str строка
 * @return строка без пробелов
 */
    public static String ALLTRIM(String str) {
        return str.trim();
    }
/**
 * Убрать из строки все пробелы (также и внутри)
 * @param str
 * @return 
 */
    public static String ALLTRIM13(String str) {
        if (str==null || str.length()==0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());
        for (int j = 0; j < str.length(); j++) {
            char ch = str.charAt(j);
            if (ch >= ' ') {
                sb.append(ch);
            }
        }
        str = sb.toString();
        return str;
    }
    /**
     * Удаляет пробелы слева
     * @param str
     * @return 
     */
    public static String LTRIM(String str) {
        if (str == null) {
            return "";
        }
        if (str.trim().length() == 0) {
            return "";
        }
        int i = 0;
        char[] strin = str.toCharArray();
        while (str.length() > i-1 && strin[i] == ' ' )  {
            i++;
        }
        if (str.length() == i-1) {
            return "";
        }
        
        return str.substring(i);
    }
    /**
     * Удаляет пробелы справа
     * @param str
     * @return 
     */
    public static String RTRIM(String str) {
        if (str == null) {
            return "";
        }
        if (str.length() == 0) {
            return "";
        }
        int i = str.length() - 1;
        char[] strin = str.toCharArray();
        while (i >= 0 && strin[i] == ' ') {
            i--;
        }
        if (i < 0) {
            return "";
        }
        return str.substring(0, i + 1);
    }
    /**
     * Удаляет из строки все пробелы и управляющие символы а также коментарии с //
     * @param str
     * @return 
     */
    public static String ALLCLEAR(String str) {
        if (str==null || str.length()==0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());
        for (int j = 0; j < str.length(); j++) {
            if (j+1 < str.length() && str.charAt(j)=='/' && str.charAt(j+1)=='/')  { // нашли //
                j++; j++;
                while (j < str.length() && str.charAt(j)!='\n' ) { //пока не конец массива или не конец строки \n
                    j++;
                }
                if (j+1< str.length() && str.charAt(j)=='\n' ) { // если конец строки
                    j++; // пропускаем 
                }
            }
            char ch = str.charAt(j);
            if (ch != ' ' && ch != '\n' && ch != '\t' && ch != '\r') { //пробел и перевод строки
                sb.append(ch);
            }
        }
        str = sb.toString();
        return str;
    }
/**
 * Заменяем во всей строке символ на другой
 * @param str - строка
 * @param s1 - заменяемый
 * @param s2 - новый
 * @return 
 */
       public static String REPLACE(String str,char s1,char s2) {
        if (str==null || str.length()==0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());
        for (int j = 0; j < str.length(); j++) {
            char ch = str.charAt(j);
            if (ch == s1 ) { //если тот который заменяем
                sb.append(s2); //которым заменяем
            } else {
                sb.append(ch); //оставляем
            }
        }
        str = sb.toString();
        return str;
    }
 
/**
 * Удаляет префиксы и суффиксы # $ № ;
 * @param str
 * @return 
 */
    public static String PREF_SUF(String str) {
        if (str == null) {
            return "";
        }
        if (str.length() == 0) {
            return "";
        }
     String E1 = "#";
     String E2 = "$";
     
     String R1 = "№";
     String R2 = ";";    
        str=FF.ALLTRIM(str);
        str=str.replaceAll(E1,"");
        str=str.replaceAll("\\"+E2,"");
        str=str.replaceAll(R1,"");
        str=str.replaceAll(R2,"");

        return str;
    }
    
    public static int LEN(String str) {
        return str.length();
    }
   /**
    * Дополняет слева строку заданным количеством повторения заданной строки S
    * @param STR
    * @param n
    * @param S
    * @return 
    */ 
    public static String LPAD(String STR,int n,String S) {
        int len =STR.length();
        STR=FF.REPLICATE(S, n-len)+ STR;
        return STR;
    }
   /**
    * Дополняет слева строку заданным количеством пробелов
    * @param STR
    * @param n
    * @return 
    */ 
    public static String LPAD(String STR,int n) {
        return LPAD(STR,n," ");
    }

   /**
    * Дополняет справо строку заданным количеством повторения заданной строки S
    * @param STR
    * @param n
    * @param S
    * @return 
    */ 
    public static String RPAD(String STR,int n,String S) {
        int len =STR.length();
        STR=STR+FF.REPLICATE(S, n-len);
        return STR;
    }
   /**
    * Дополняет справо строку заданным количеством пробелов
    * @param STR
    * @param n
    * @return 
    */ 
    public static String RPAD(String STR,int n) {
        return RPAD(STR,n," ");
    }
/**
 * Читает буфкр обмена
 * @return 
 */
    public static String _CLIPTEXTOUT() {
                                  Clipboard clip = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
                          Transferable content = clip.getContents(null);
                          String STR="";
                    try {
                        STR = (String) content.getTransferData(DataFlavor.stringFlavor);
                    } catch (UnsupportedFlavorException ex) {
                        return "";
                    } catch (IOException ex) {
                        return "";
                    }
                    return STR;
    }
/**
 * Картинку в буфер обмена
 * @param image 
 * https://qarchive.ru/1377384_ustanovka_izobrazhenii__v_bufer_obmena___java
 */
    public static void _CLIPTEXT(final BufferedImage  image) {
        Clipboard clip = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable data = new Transferable( ) {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                 return new DataFlavor[] { DataFlavor.imageFlavor };
            }
            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                 return flavor == DataFlavor.imageFlavor;
            }
            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return image;
            }
        };        
        clip.setContents(data, null);
    }
    
/**
 * Записать в буфер  строку str
 * @param str 
 */    
    public static void _CLIPTEXT(String str) {
        Clipboard clip = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        //копируем текст в буфер обмена
//   try {
        Transferable data = new StringSelection(str);
        clip.setContents(data, null);
//        } finally {
//            System.err.println("Ошибка _CLIPTEXT:"+str);
//        }
    }

    public static boolean EMPTY(String val) {
        if (val == null) {
            return true;
        }
        val = FF.RTRIM(val);
        if ("".equals(val)) {
            return true;
        }
        return false;
    }

    public static boolean EMPTY(int val) {
        if (val == 0) {
            return true;
        }
        return false;
    }

    public static boolean EMPTY(float val) {
        if (val == 0) {
            return true;
        }
        return false;
    }

    public static boolean EMPTY(double val) {
        if (val == 0) {
            return true;
        }
        return false;
    }

    public static boolean EMPTY(boolean val) {
        if (val == false) {
            return true;
        }
        return false;
    }
/**
 * Поиск подстроки sstr в строке str
 * @param sstr - подстрока
 * @param str - строка
 * @param ind - позиции с которой стартует поиск 1- первая позиция
 * @return начальная позиция входждения начиная с 1
 */
    public static int AT(String sstr, String str,int ind) {
        if (FF.EMPTY(str) || FF.EMPTY(sstr)) {
            return 0;
        }
        int n = str.indexOf(sstr,ind-1);
        return n + 1;
    }
/**
 * Поиск подстроки sstr в строке str
 * @param sstr - подстрока
 * @param str - строка
 * @return начальная позиция входждения начиная с 1
 */
    public static int AT(String sstr, String str) {
        return AT( sstr, str,1);
    }
    
/**
 * Поиск символа с номером sstr в строке str
 * @param sstr - номер символа
 * @param str - строка
 * @return начальная позиция входждения начиная с 1
 */
    public static int AT(int sstr, String str) {
        if (FF.EMPTY(str) || sstr == 0) {
            return 0;
        }
        int n = str.indexOf(sstr);
        return n + 1;
    }

    public static int RAT(String sstr, String str) {
        if (FF.EMPTY(str) || FF.EMPTY(sstr)) {
            return 0;
        }
        int n = str.lastIndexOf(sstr);
        return n + 1;
    }

    public static int RAT(int sstr, String str) {
        if (FF.EMPTY(str) || sstr == 0) {
            return 0;
        }
        int n = str.lastIndexOf(sstr);
        return n + 1;
    }

    public static Integer VAL(String str) {
        if (FF.EMPTY(str)) {
            return 0;
        }
        if (FF.AT(".", str) > 0) {
            str = FF.SUBSTR(str, 1, FF.AT(".", str) - 1);
        }
        int n;
        try {
            n = (int) Integer.parseInt(str, 10);
        } catch (Exception ex) {
            n = 0;
        }
        return n;
    }
/**
 * Число в строку
 * @param n
 * @return 
 */
    public static String STR(double n) {
        return String.valueOf(n);
    }
    
    public static String STR(double n,int r1,int r2) {
      String pattern="";
      /*123456.789  ###,###.###  123,456.789
      123456.789  ###.##  123456.79
      123.78  000000.000  000123.780
      12345.67  $###,###.###  $12,345.67 */
      pattern=FF.REPLICATE("#", r1);
      if (r2>0) {
       pattern=pattern+"."+FF.REPLICATE("#", r2);   
      } 
      DecimalFormat myFormatter = new DecimalFormat(pattern);
      String output = myFormatter.format(n);        
       return output;
    }

/**
 * Число в строку
 * @param n
 * @return 
 */
    public static String STR(float n) {
        return String.valueOf(n);
    }
/**
 * Число в строку
 * @param n
 * @return 
 */
    public static String STR(int n) {
        return Integer.toString(n);
    }


    /**
     * Возвращает количество моментов (раз), когда одно символное выражение
     * "встречается" (входит) в другом символьном выражении.
     *
     * @param sstr Определяет первое символьное выражение, которое ищется
     * функцией OCCURS( ) в другом символьном выражении
     * @param str Определяет символьное выражение, в котором выполняется поиск
     * функцией OCCURS( ) первого символьного выражения
     * @return
     */
    public static int OCCURS(String sstr, String str) {
        if (FF.EMPTY(str) || FF.EMPTY(sstr)) {
            return 0;
        }
        String s = str.replace(sstr, "");
        int n = str.length() - s.length();
        int count = n / sstr.length();
        return count;
    }

    public static long FILE_TIME_OLD(String pfile) {
        if ("\\".equals(FF.SUBSTR(pfile, 1,1))) { // ЕСЛИ ЗАДАН ОТНОСИТЕЛЬНЫЙ ПУТЬ
         File UDFile = new File(".");
         if (UDFile.exists() && !V.ARGS2) {
             pfile = UDFile.getAbsolutePath().toString().substring(0, UDFile.getAbsolutePath().toString().length() - 1) + pfile;
          }
          UDFile.delete();
          }
        pfile = pfile.replaceAll("Cashier","Shop");
        File file = new File(pfile);
        return file.lastModified();
    }
public static long FILE_TIME(String pfile) {
    if ("\\".equals(FF.SUBSTR(pfile, 1, 1))) { // ЕСЛИ ЗАДАН ОТНОСИТЕЛЬНЫЙ ПУТЬ
        File UDFile = new File(".");
        if (UDFile.exists() && !V.ARGS2) {
            pfile = UDFile.getAbsolutePath().substring(0, UDFile.getAbsolutePath().length() - 1) + pfile;
        }
        UDFile.delete();
    }
    pfile = pfile.replaceAll("Cashier", "Shop");
    File file = new File(pfile);
    try {
        BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        return attrs.lastModifiedTime().toMillis();
    } catch (IOException e) {
        return 0L;
        //throw new RuntimeException(String.format("getting last modify time of file '%s' exception", file), e);
    }
}    
    /**
     * Проверка на существование файла
     * @param pfile - путь к файлу
     * @return 
     */
    public static boolean FILE(String pfile) {
        if ("\\".equals(FF.SUBSTR(pfile, 1,1))) { // ЕСЛИ ЗАДАН ОТНОСИТЕЛЬНЫЙ ПУТЬ
         File UDFile = new File(".");
         if (UDFile.exists() && !V.ARGS2) {
             pfile = UDFile.getAbsolutePath().toString().substring(0, UDFile.getAbsolutePath().toString().length() - 1) + pfile;
          }
          UDFile.delete();
          }
        pfile = pfile.replaceAll("Cashier","Shop");
        File file = new File(pfile);
        file.lastModified();
        return file.exists();
    }
    
    /**
     * Создание каталога если его не существует, вместе с подкаталогами
     * @param pdir - путь каталога
     * @return 
     */
    public static boolean CREATE_DIR(String pdir) {
        if (FILE(pdir)) { //ЕСЛИ СУЩЕСТВУЕТ
        return true;
        }
    File dir2 = new File(pdir);
      return dir2.mkdirs(); //создаем каталог
    }
    
    /**
     * Текущая дата-время в символьном виде
     *
     * @return
     */
    public static String DATETIMES() {
        String datetime = V.ddmmyyyyhhmm.format(new GregorianCalendar().getTime());
        return datetime;
    }
    /**
     * Текущая дата-время В милисекундах в символьном виде
     *
     * @return
     */
    public static String DATETIMEMS() {
        String datetime = V.ddmmyyyyhhmmSSS.format(new GregorianCalendar().getTime());
        return datetime;
    }

    /**
     * Текущая дата-время
     *
     * @return
     */
    public static Date DATETIME() {
        String dates = V.ddmmyyyyhhmm.format(new GregorianCalendar().getTime());
        try {
            return V.ddmmyyyyhhmm.parse(dates);
        } catch (ParseException ex) {
            Logger.getLogger(FF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }        
 public static int YEAR(Date DD) {
 V.calendar.setTime(DD);
  return  V.calendar.get(java.util.Calendar.YEAR);
 }
 public static int YEAR() {
  return  YEAR(new java.util.Date());
 }
 public static int MONTH(Date DD) {
 V.calendar.setTime(DD);
  return  V.calendar.get(java.util.Calendar.MONTH)+1;
 }
 public static int MONTH() {
  return  MONTH(new java.util.Date());
 }

  public static int DAY(Date DD) {
 V.calendar.setTime(DD);
  return  V.calendar.get(java.util.Calendar.DAY_OF_MONTH);
 }
 public static int DAY() {
  return  DAY(new java.util.Date());
 }
 
 public static int HOUR(Date DD) {
 V.calendar.setTime(DD);
  return  V.calendar.get(java.util.Calendar.HOUR_OF_DAY);
 }
 public static int HOUR() {
  return  HOUR(new java.util.Date());
 }
 public static int MINUTE(Date DD) {
 V.calendar.setTime(DD);
  return  V.calendar.get(java.util.Calendar.MINUTE);
 }
 public static int MINUTE() {
  return  MINUTE(new java.util.Date());
 }
/**
 * Секунд в заданной дате-времени
 * @param DD
 * @return 
 */ 
public static int SECOND(Date DD) {
 V.calendar.setTime(DD);
  return  V.calendar.get(java.util.Calendar.SECOND);
 }
/**Миллисекунд в заданной дате
 * @param DD
 * @return 
 */
public static int MILLISECOND(Date DD) {
 V.calendar.setTime(DD);
  return  V.calendar.get(java.util.Calendar.MILLISECOND);
 }

/**
 * Секунд в текущем времени
 * @return 
 */
 public static int SECOND() {
  return  SECOND(new java.util.Date());
 }
/**
 * МилиСекунд в текущем времени
 * @return 
 */
 public static int MILLISECOND() {
  return  MILLISECOND(new java.util.Date());
 }

 public static int MAX_DAY_MONTH() {
   Calendar c = new GregorianCalendar();
   int maximum = c.getMaximum(V.calendar.DAY_OF_MONTH); 
   return  maximum;
 }
 
 /**
  * Символьная строка  текущего времени до секунд без маски
  * @return 
  */
    public static String DATETIMESS() {
        return DATETIMESS(0);
    }
    /**
     * Символьная строка текущего времени без маски до заданной длинны 1- до милисекунд
     * @param tip
     * @return 
     */
    public static String DATETIMESS(int tip) {
        String STR="";
        STR=STR+FF.STR(YEAR())+LPAD(STR(MONTH()),2,"0")+LPAD(STR(DAY()),2,"0")+LPAD(STR(HOUR()),2,"0")+LPAD(STR(MINUTE()),2,"0");
        if (tip==1) {
            STR=STR+LPAD(STR(MILLISECOND()),4,"0");
        }
        return STR;
    }
   
    
    /**
     * Заданная дата в символьном виде
     *
     * @return строка
     */
    public static String DTOC(Date d) {
        String date = V.ddmmyyyy.format(d);
        return date;
    }

    /**
     * Текущая дата в символьном виде
     *
     * @return
     */
    public static String DATES() {
        String date = V.ddmmyyyy.format(new GregorianCalendar().getTime());
        return date;
    }

    /**
     * Текущая дата
     *
     * @return
     */
    public static Date DATE() {
        try {
            return V.ddmmyyyy.parse(DATES());
        } catch (ParseException ex) {
            Logger.getLogger(FF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static Date DATE(String date) {
        try {
            return V.ddmmyyyy.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(FF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static boolean isDATE(String date) {
        try {
             V.ddmmyyyy.parse(date);
             return true;
        } catch (ParseException ex) {
            return false;
        }
    }
    
/**
 * Дата на дней от заданной даты
 * @param D - дата
 * @param DAY - количество дней смещения
 * @return 
 */
 public static Date DATE_PLUS_DAY(Date D ,int DAY) {
  //Calendar instance = Calendar.getInstance();
  V.calendar.setTime(D); //устанавливаем дату, с которой будет производить операции
  V.calendar.add(V.calendar.DAY_OF_MONTH, DAY);// прибавляем 3 дня к установленной дате
  return V.calendar.getTime(); // получаем измененную дату         
  }
    
    public static String TIMES() {
        String datetime = V.ddmmyyyyhhmm.format(new GregorianCalendar().getTime());
        return datetime;
    }
/**
 * Создание строки из строки str повторенной  kol раз
 * @param str
 * @param kol
 * @return 
 */
    public static String REPLICATE(String str, int kol) {
        if ( kol<=0 ) {return "";}
        String strout = "";
        for (int i = 1; i <= kol; i++) {
            strout = strout + str;
        }
        return strout;
    }
/**
 * Проверка все ли символы строки - цифры
 * @param NUM
 * @return 
 */
   public static boolean NUM_ALL(String NUM) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(NUM);
        return m.matches();
       
   }
    /**
     * Строку в файл
     * @param str - символьная строка
     * @param file - имя файла
     * @return 
     */
    public static String STRTOFILE(String str, String file) {
        
        PrintWriter txtWriter = null;
        try {
            txtWriter = new PrintWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            try {
                file=P.FILECHOOSE("Выбор файла для сохранения строки");
                txtWriter = new PrintWriter(new FileOutputStream(file));
            } catch (FileNotFoundException ex) {
                P.MESSERR("Неизвестная ошибка!");
                return "";
            }
        }
        if (txtWriter != null) {
            txtWriter.print(str);
            txtWriter.close();
        }
        return file;
    }
/**
 * Считывает текстовый файл и возвращает строку
 * @param fileName
 * @return
 * @throws UnsupportedEncodingException 
 */
public static String FILETOSTR(String fileName)  {
    return FILETOSTR(fileName,"Cp1251");
}
/**
 * Считывает текстовый файл и возвращает строку
 * @param fileName - путь к файлу
 * @param Cpage - кодовая страница
 * @return 
 */    
public static String FILETOSTR(String fileName,String Cpage)  {
        if ("\\".equals(FF.SUBSTR(fileName, 1, 1))) { // ЕСЛИ ЗАДАН ОТНОСИТЕЛЬНЫЙ ПУТЬ
            File UDFile = new File(".");
            if (UDFile.exists()) {
                fileName = UDFile.getAbsolutePath().toString().substring(0, UDFile.getAbsolutePath().toString().length() - 1) + fileName;
            }
            UDFile.delete();
        }
        File inf = new File(fileName);
        BufferedReader in = null;
        try {
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),Cpage));
            } catch (UnsupportedEncodingException ex1) {
                Logger.getLogger(FF.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (FileNotFoundException ex) {
            try {
                inf = new File(P.FILECHOOSE("Выбор файла для загрузки строки"));
                try {
                    in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), Cpage));
                } catch (UnsupportedEncodingException ex1) {
                    Logger.getLogger(FF.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (FileNotFoundException ex1) {
                P.MESSERR("Неизвестная ошибка! \n"+ex1.toString());
                return "";
            }
        }
        if (in != null) {
            StringBuilder sb = new StringBuilder(inf.length() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) inf.length());
            try {
                for (String s = in.readLine(); s != null; s = in.readLine()) {
                    sb.append(s).append("\n");
                }
                in.close();
            } catch (IOException ex) {
            }
            return sb.toString();
        }
        return "";
    }

    //приводит текст к читаемому виду. например для sql ошибок. KAA
    //charCount - максимальное кол-во символов в строке
    public static String BIG_TEXT_FORMATTER(String txt) {
        return BIG_TEXT_FORMATTER(txt, 70);
    }

    public static String BIG_TEXT_FORMATTER(String txt, int charCount) {
//        txt = txt.replace(".", ".\n");
        String[] arr = txt.split("\n");
        txt = "\n";
        for (int i=0;i<arr.length;i++) {
            if (arr[i].length() <= charCount) {
                txt += arr[i];
            } else {
                while (arr[i].length() > 0) {
                    if (arr[i].length() <= charCount) {
                        txt += arr[i];
                        break;
                    } else {
                        int ind = arr[i].substring(0, charCount).lastIndexOf(" ");
                        if (ind == -1) {
                            txt += arr[i].substring(0, charCount) + "\n";
                            arr[i] = arr[i].substring(charCount, arr[i].length());
                        } else {
                            txt += arr[i].substring(0, ind + 1) + "\n";
                            arr[i] = arr[i].substring(ind + 1, arr[i].length());
                        }
                    }   
                }        
            }
            txt += "\n";
        }
        return txt;
    }
    
    /**
     * Имя выполняемого программного модуля
     * @param cl - класс
     * @return
     */
    public static String getNameProgramm(Class cl) {
        return getNameProgramm(cl,1);
    }
    
    /**
     * Имя выполняемого программного модуля
     * @param cl - класс
     * @param type - тип вывода 1- файл 2- путь
     * @return
     */
    public static String getNameProgramm(Class cl,int type) {
        File currentJar = null;
        try {
            currentJar = new File(cl.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
         //   System.err.println(e.getMessage());
        }
        String str="";
        if (type==1) { //Файл 
         str= currentJar.getName();
        }
        if (type==2) { //Путь к каталогу
         str=currentJar.getParent();
         //str= currentJar.getAbsolutePath();
        }
        if (type==3) { //Расширение 
         if (currentJar.getName().endsWith(".jar")) {
             str="jar";
         } else {
             str="class";
         }
        }
        return str;
    }
    
    
}
