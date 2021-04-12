package prg;

import baseclass.Cursorr;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import static prg.A.ALIAS;
import static prg.A.GETVAL;
import static prg.A.GOTO;
import static prg.A.RECCOUNT;
import static prg.A.RECNO;

/**
 * Класс статистических методов работы с данными
 * @author Kostya
 */
public class A {
    /**
     * Проверяет существует ли заданный алиас
     * @param ALIAS алиас
     * @return     false - нет  true- да
     */
    public static Boolean USED(String ALIAS) {
        ALIAS=ALIAS.toUpperCase();        
        if (V.MAPALIAS == null) {
            V.MAPALIAS = new HashMap<>();}
        
        if (V.MAPALIAS.containsKey(ALIAS)==false){            return false;}
//        S.ACTIVEALIAS(ALIAS);
        return true;
    }
    
    /**
     * 
     * @return текущий алиас
     */
    public static String ALIAS() { 
        return V.ACTIVEALIAS;
    }
    /**
     * Делает текущим заданный алиас
     * @param ALIAS алиас
     * @return     0 - ошибка нет алиаса 1- ок
     */
    public static int SELECT(String ALIAS) {
        ALIAS=ALIAS.toUpperCase();        
        if (V.MAPALIAS.containsKey(ALIAS)==false){            return 0;}
        S.SETACTIVEALIAS(ALIAS);        return 1;
    }
/**
 * Чистка памяти по алиасу данных
 * @param ALIAS алиас
 * @return     0 - ошибка нет алиаса 1- ок
 */
    public static void CLEAR(String ALIAS) {
        ALIAS=ALIAS.toUpperCase();       
      //  if (V.ACTIVEALIAS!=null && V.ACTIVEALIAS.equals(ALIAS))
      //  {S.SETACTIVEALIAS(null) ;}  
        if (V.MAPALIAS== null) return;
        Cursorr  D=(Cursorr) V.MAPALIAS.get(ALIAS); //получение курсора данных из списка по названию алиаса
        if (D!=null) {
        D.rowList.clear(); //удаление колекции данных для освобождения памяти 
        D=null;
//        Runtime.getRuntime().gc();        
        }
        
    }
    
/**
 * закрытие алиаса данных
 * @param ALIAS алиас
 * @return     0 - ошибка нет алиаса 1- ок
 */
    public static int CLOSE(String ALIAS) {
        ALIAS=ALIAS.toUpperCase();       
        if (V.ACTIVEALIAS!=null && V.ACTIVEALIAS.equals(ALIAS))
        {S.SETACTIVEALIAS(null) ;}        
        CLEAR(ALIAS);
        if (V.MAPALIAS.remove(ALIAS)==null)
        {            return 0;}
        return 1;
    }
    public static int CLOSE_ALL() {
        int kol=V.MAPALIAS.size();
        S.SETACTIVEALIAS(null) ;        
        V.MAPALIAS.clear();
        return kol;
    }
    
    /**
     * 
     * @param ALIAS алиас
     * @param REC   номер записи
     * @return     0- ошибка иниче номерзаписи
     */
    public static int GOTO(String ALIAS,int REC) {
       ALIAS=ALIAS.toUpperCase();        
      Cursorr  DATA=(Cursorr) V.MAPALIAS.get(ALIAS);
        if (DATA==null){            return 0;}
        S.SETDATARECNO(DATA,REC);
        return DATA.RECNO;
    }
    public static int GOTO(int REC) {
        if (V.ACTIVEALIAS==null) {return 0 ;}        
        return GOTO(V.ACTIVEALIAS,REC);
    }
/**
 * На первую запись
 * @param ALIAS алиас
 * @return номер записи
 */    
public static int GOTOP(String ALIAS) {
        ALIAS=ALIAS.toUpperCase();       
        return GOTO(ALIAS,1);
    }    
public static int GOTOP() {
        return GOTO(ALIAS(),1);
    }    
/**
 * Определить количество записей
 * @param ALIAS алиас
 * @return количество
 */    
public static int RECCOUNT(String ALIAS) {
      if (ALIAS==null){  return 0;}        
      ALIAS=ALIAS.toUpperCase();   
      Cursorr  DATA=(Cursorr) V.MAPALIAS.get(ALIAS);
        if (DATA==null){ return 0;}
        return DATA.ROWCOUNT;
    }    
public static int RECCOUNT() {
    return RECCOUNT(ALIAS());
}    
/**
 * На последнюю запись
 * @param ALIAS алиас
 * @return номер записи
 */    
public static int GOBOTTOM(String ALIAS) {
        return GOTO(ALIAS,RECCOUNT(ALIAS));
}
public static int GOBOTTOM() {
        return GOTO(ALIAS(),RECCOUNT(ALIAS()));
}

    /**
     * Получение текущей записи
     * @param ALIAS алиас
     * @return  номер записи
     */
    public static int RECNO(String ALIAS) {
        if (ALIAS==null){  return 0;}        
       ALIAS=ALIAS.toUpperCase();        
      Cursorr  DATA=(Cursorr) V.MAPALIAS.get(ALIAS);
        if (DATA==null){            return 0;}
        return DATA.RECNO;
    }
    public static int RECNO() {
     return RECNO(ALIAS());
    }
    /**
     * Получение данных по алиасу имени поля и номера записи
     * @param ALIAS
     * @param POLE
     * @param REC
     * @return объект значение
     */
    public static Object GETVAL(String ALIAS,String POLE,int REC) {
//       try { 
       Object val=null; //иницилизация объкта данных
       if (REC<0) return null; //если текущий номер записи меньше нулм возвращаем null
       ALIAS=ALIAS.toUpperCase(); //алиас в верхнем регистре
      Cursorr  DATA=(Cursorr) V.MAPALIAS.get(ALIAS); //получение курсора данных из списка по названию алиаса
        if (DATA==null){ 
            P.MESS("Не найден курсор:"+ALIAS);
            return null;} //если такого нет возвращаем нул
        for (int j=0;j<DATA.COLNAMES.length;j++){ //по массиву списка полей курсора
            if (POLE.equalsIgnoreCase(DATA.COLNAMES[j])) { //если имена полей совпали не учитывая верхний и нижний регистр символов
//              val=DATA.DATA[REC-1][j];// получаем данные по индексу номера записи минус 1 и номера столбца
              if (A.RECCOUNT(ALIAS)==0) { //если нет записей
              if (DATA.COLTYPE[j]==V.TYPE_NUMERIC) {
              val = new Double(0); 
              return val; }
              if (DATA.COLTYPE[j]==V.TYPE_BIT) {
              val = new Boolean(false); 
              return val; }
              
              return null;
          }
              Object[] RECORD = null;
              try {
                RECORD= (Object[])DATA.rowList.get(REC-1);
              } catch (ArrayIndexOutOfBoundsException exc) {
                  System.out.println("GETVAL EXC - " + exc.toString());
                  return null;
              }
                val= RECORD[j];
              
              return val;                        //возвращаем значение
            }
        }
            P.MESS("Не найдено поле:"+POLE +" в курсоре:"+ALIAS);
                return null;
//       }
//       finally {
//           P.MESSERR("Ошибка получения значения:"+ALIAS+"."+POLE+" "+FF.STR(REC));
//           return null;
//       }
    }
    public static Object GETVAL(String POLE,int REC) {//по текущему алиасу или алиасу заданному через точку с именем поля
       int pos=POLE.indexOf(".");//если разделение точкой то алиас.название поля
       if (pos>-1) //
       {
           String ALIAS=POLE.substring(0,pos);//выделяем до точки алиас
           POLE=POLE.substring(pos+1);//выделяем после точки имя поля
           return GETVAL(ALIAS,POLE,REC);//выполняем по трем параметрам
       }
       return GETVAL(ALIAS(),POLE,REC);
    }
    public static Object GETVAL(String POLE) {
       int pos=POLE.indexOf(".");//если разделение точкой то алиас.название поля
       if (pos>-1) //
       {
           String ALIAS=POLE.substring(0,pos);//выделяем до точки алиас
           POLE=POLE.substring(pos+1);//выделяем после точки имя поля
           return GETVAL(ALIAS,POLE,RECNO(ALIAS));//выполняем по трем параметрам
       }
        
       return GETVAL(ALIAS(),POLE,RECNO());
    }
    public static int GETVALN(String POLE) {
        return  FF.VAL(A.GETVAL(POLE).toString())   ;
    }
    public static double GETVALD(String POLE) {
        return  (double)(A.GETVAL(POLE))   ;
    }
    
    public static String GETVALS(String POLE) {
        return  P.TOSTR( A.GETVAL(POLE))        ;
    }
    public static String GETVALS(String POLE,int REC) {
        return  P.TOSTR(A.GETVAL(POLE,REC))        ;
    }
    public static String GETVALSQL(String POLE) {
        return  P.P_SQL(A.GETVAL(POLE))        ;
    }
    public static String GETVALSQL(String POLE,int REC) {
        return  P.P_SQL(A.GETVAL(POLE,REC))        ;
    }
    public static String GETVALSQL(String POLE,String TYPE) {
        return  P.P_SQL(A.GETVALS(POLE),TYPE)        ;
    }

    /**
     * Занесение данных по алиасу имени поля и номера записи
     * @param VAL
     * @param ALIAS
     * @param POLE
     * @param REC
     * @return объект значение
     */
    public static boolean SETVAL(Object VAL,String ALIAS,String POLE,int REC) {
       if (REC<=0) return false; //если текущий номер записи меньше нулм возвращаем null
       ALIAS=ALIAS.toUpperCase(); //алиас в верхнем регистре
      Cursorr  DATA=(Cursorr) V.MAPALIAS.get(ALIAS); //получение курсора данных из списка по названию алиаса
        if (DATA==null){ return false;} //если такого нет возвращаем false
        for (int j=0;j<DATA.COLNAMES.length;j++){ //по массиву списка полей курсора
         if (POLE.equalsIgnoreCase(DATA.COLNAMES[j])) { //если имена полей совпали не учитывая верхний и нижний регистр символов
//              DATA.DATA[REC-1][j]=VAL;// получаем данные по индексу номера записи минус 1 и номера столбца
         Object[] RECORD= (Object[])DATA.rowList.get(REC-1);
         RECORD[j] = VAL;
         DATA.rowList.set(REC-1,RECORD);
              
              return true;                        //возвращаем значение
            }
        }
                return false;
    }

        public static boolean SETVAL(Object VAL,String POLE,int REC) {//по текущему алиасу или алиасу заданному через точку с именем поля
       int pos=POLE.indexOf(".");//если разделение точкой то алиас.название поля
       if (pos>-1) //
       {
           String ALIAS=POLE.substring(0,pos);//выделяем до точки алиас
           POLE=POLE.substring(pos+1);//выделяем после точки имя поля
           return SETVAL(VAL,ALIAS,POLE,REC);//выполняем по трем параметрам
       }
       return SETVAL(VAL,ALIAS(),POLE,REC);
    }
    public static boolean SETVAL(Object VAL,String POLE) {
       int pos=POLE.indexOf(".");//если разделение точкой то алиас.название поля
       if (pos>-1) //
       {
           String ALIAS=POLE.substring(0,pos);//выделяем до точки алиас
           POLE=POLE.substring(pos+1);//выделяем после точки имя поля
           return SETVAL(VAL,ALIAS,POLE,RECNO(ALIAS));//выполняем по трем параметрам
       }
        
       return SETVAL(VAL,ALIAS(),POLE,RECNO());
    }
/**
 * Возвращает тип данного поля курсора
 * @param ALIAS - имя курсора
 * @param POLE  - имя поля
 * @return - если возвращается "U" - поля не существует
 */
    public static String TYPE(String ALIAS,String POLE) {
  //String val=V.TYPE_CHAR; //иницилизация объкта данных
        String val="U"; //иницилизация объкта данных
       ALIAS=ALIAS.toUpperCase(); //алиас в верхнем регистре
      Cursorr  DATA=(Cursorr) V.MAPALIAS.get(ALIAS); //получение курсора данных из списка по названию алиаса
        //if (DATA==null){ return V.TYPE_CHAR;} //если такого нет возвращаем нул
      if (DATA==null){ return val;} //если такого нет возвращаем U
        for (int j=0;j<DATA.COLNAMES.length;j++){ //по массиву списка полей курсора
            if (POLE.equalsIgnoreCase(DATA.COLNAMES[j])) { //если имена полей совпали не учитывая верхний и нижний регистр символов
              val=DATA.COLTYPE[j];// получаем данные по индексу номера записи минус 1 и номера столбца
              return val;                        //возвращаем значение
            }
        }
                return val;
        
    }
/**
 * Возвращает тип данного поля курсора
 * @param POLE  - имя курсора + имя поля
 * @return - если возвращается "U" - поля не существует
 */   
    public static String TYPE(String POLE) {
       int pos=POLE.indexOf(".");//если разделение точкой то алиас.название поля
       if (pos>-1) //
       {
           String ALIAS=POLE.substring(0,pos);//выделяем до точки алиас
           POLE=POLE.substring(pos+1);//выделяем после точки имя поля
           return TYPE(ALIAS,POLE);//выполняем по двум параметрам
       }
           return TYPE("M",POLE);//выполняем по двум параметрам
        
    }
/**
 * Получение уникального номера записи 
 * @param table - имя таблицы
 * @return 
 */    
    public static int GETMAXREL(String table) {
        CallableStatement run;
        try {
            run = V.CONN1.prepareCall("{call GET_REL(?, ?)}"); //ВЫЗОВ ХРАНИМОЙ ПОЛУЧЕНИЯ УНИКАЛЬНОГО НОМЕРА REL ДЛЯ table
            run.setString(1, table);
            run.registerOutParameter(2,java.sql.Types.INTEGER);
            run.execute();
            return run.getInt(2);
        } catch (SQLException ex) {
            P.MESSERR("Ошибка выполнение запроса:" + "\n" +  ex.toString().replace(".", ".\n"));
            return -1;
        }
    }
/**
 * Запрос на максимальное значение
 * @param table - таблица
 * @param pole - поле
 * @return 
 */    
    public static Object GETVALMAXS(String table,String pole) {
        return GETVALMAXS(table,pole,"");
    }
/**
 * Запрос на максимальное значение
 * @param table - таблица
 * @param pole - поле
 * @param where - условие в WHERE если есть
 * @return 
 */
    public static Object GETVALMAXS(String table,String pole,String where) {
        if (!FF.EMPTY(where)) {
            where=" WHERE "+where;
        }
                    if ("1".equals(V.CONN_DRIVER)) { //для орасле
                      P.SQLEXECT("SELECT CAST(nvl(max("+pole+"),0)+1 AS NUMBER(9,0) ) AS VAL FROM "+table+where, "UD");
                    } 
                    if ("2".equals(V.CONN_DRIVER)) { //для sql server
                      P.SQLEXECT("SELECT CAST(ISNULL(max("+pole+"),0)+1 AS NUMERIC(9,0) ) AS VAL FROM "+table+where, "UD");
                    } 
                    if ("3".equals(V.CONN_DRIVER)) { //для postgre
                      P.SQLEXECT(" select CAST(case when VAL is null then 1 else VAL end AS NUMERIC(9,0)) from (SELECT  cast( max("+pole+")+1 AS NUMERIC(9,0) )  AS VAL FROM "+table+where+ ") A ","UD");                      
                    } 
                    
        return A.GETVAL("UD.VAL");
    }
    
    /**
     * Добавление пустой записи в курсор
     * @param ALIAS
     * @return  номер вставленной записи если 0 - ошибка вставки
     */
    public static int  APPEND(String ALIAS) {
         ALIAS=ALIAS.toUpperCase(); //алиас в верхнем регистре
         Cursorr  DATA=(Cursorr) V.MAPALIAS.get(ALIAS); //получение курсора данных из списка по названию алиаса
        if (DATA==null){ return 0;} //если такого нет возвращаем нул
        int kol = DATA.COLCOUNT;//количество полей
                //формирование пустой записи
              int  REC = DATA.ROWCOUNT + 1; //количество записей плюс еще одна
                Object[] cells = new Object[kol]; // массив объектов с количеством полей
                cells[kol - 1] = new Integer(REC); // последнее поле это номер запсии , туда его и записываем
                DATA.rowList.add(cells); // добавление массива объектов строки в контейнер строк (в курсор пустую строку с номером записи)
                DATA.ROWCOUNT = REC; //количество записей в объекте курсор уже вместе с вставленой
             return REC;
    }
    public static int  APPEND() {
        return APPEND(A.ALIAS());
    }
/**
 * Удаление заданной записи в курсоре данных
 * @param ALIAS
 * @param REC
* @return  номер удаленной записи если 0 - ошибка удаления
 */    
    public static int  DELETE(String ALIAS,int REC) {
         ALIAS=ALIAS.toUpperCase(); //алиас в верхнем регистре
         Cursorr  DATA=(Cursorr) V.MAPALIAS.get(ALIAS); //получение курсора данных из списка по названию алиаса
         if (DATA==null){ return 0;} //если такого нет возвращаем нул         
         DATA.rowList.remove(REC - 1);
         DATA.ROWCOUNT--;
         return DATA.ROWCOUNT;
    }

    public static int  DELETE(String ALIAS) {
        return DELETE(ALIAS,A.RECNO(ALIAS));
    }

    public static int  DELETE() {
        return DELETE(A.ALIAS(),A.RECNO());
    }
//ПОИСК В КУРСОРЕ ПО ЗНАЧЕНИЯМ int REC=A.LOCATE("D_OST2.ART",ART,"D_OST2.ASIZE",ASIZE,"D_OST2.PROCENT",PROCENT,"D_OST2.SCAN",SCAN )
    // int REC=A.LOCATE(ПОЛЕ1,ЗНАЧЕНИЕ1.....);
    public static int LOCATE(Object... POLES) {
       int pos=POLES[0].toString().indexOf(".");//если разделение точкой то алиас.название поля
       String ALIAS=A.ALIAS();
      if (pos>-1) //
       {
           ALIAS=POLES[0].toString().substring(0,pos);//выделяем до точки алиас
       }
        A.SELECT(ALIAS);
        int REC = A.RECCOUNT();
        int j = POLES.length/2;
        boolean tf=false;
        for (int i = 1; i <= REC; i++) {// по всем записям
            tf=true;
         for (int ii = 0; ii < j*2; ii=ii+2) { //по всем связкам полей и значение
             if (A.TYPE(POLES[ii].toString())=="N") {
                 double dd1,dd2;
                 dd1 = Double.parseDouble(POLES[ii+1].toString());                 
                 Object  OO=A.GETVAL(POLES[ii].toString(), i);
                 dd2 = Double.parseDouble(OO.toString());                 
                 tf=tf&&dd1==dd2 ; // должны все совпасть                 
             } else {
            tf=tf&&POLES[ii+1].equals(A.GETVAL(POLES[ii].toString(), i) ) ; // должны все совпасть
             }
         }   
            if (tf){  //если все совпали
                A.GOTO(i);
                return i;
            }
         }
        
   return 0;   
}
/**
 * Подсчет суммы double по полю алиаса
 * @param POLE (ALIAS.POLE)
 * @return 
 */
     public static double SUMPOLE(String POLE) {
       String ALIAS="";         
       double SUM=0;
       int pos=POLE.indexOf(".");//если разделение точкой то алиас.название поля
       if (pos>-1) //
       {
            ALIAS=POLE.substring(0,pos);//выделяем до точки алиас
//           POLE=POLE.substring(pos+1);//выделяем после точки имя поля
       }         
       int REC=A.RECNO(ALIAS);
            for (int i = 1; i <= A.RECCOUNT(ALIAS); i++) {
            A.GOTO(ALIAS,i);
            SUM=SUM+(double)A.GETVAL(POLE)  ;
            }
      A.GOTO(REC);
 
        return  SUM ;
    }
   
/**
 * Подсчет суммы int по полю алиаса
 * @param POLE (ALIAS.POLE)
 * @return 
 */
     public static int SUMPOLEN(String POLE) {
       String ALIAS="";         
       int SUM=0;
       int pos=POLE.indexOf(".");//если разделение точкой то алиас.название поля
       if (pos>-1) //
       {
            ALIAS=POLE.substring(0,pos);//выделяем до точки алиас
//           POLE=POLE.substring(pos+1);//выделяем после точки имя поля
       }         
       int REC=A.RECNO(ALIAS);
            for (int i = 1; i <= A.RECCOUNT(ALIAS); i++) {
            A.GOTO(ALIAS,i);
            SUM=SUM+(int)A.GETVAL(POLE)  ;
            }
      A.GOTO(REC);
 
        return  SUM ;
    }
    
    
    
}
