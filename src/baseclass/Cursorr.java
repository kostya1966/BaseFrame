/*
 * 27/12/2017 введен BLOD  RKV
 * 
 */
package baseclass;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import prg.FF;
import prg.P;
import prg.V;

/**
 * Курсор с данными их типом и функциями обработки
 * 
 * @author Kostya
 */
public class Cursorr {

    public String ALIAS = "SqlCursor";
    public ResultSet CURSOR;
    public ResultSetMetaData STRUC;
    public ArrayList colNames = new ArrayList();// список имен столбцов
    public ArrayList colTypes = new ArrayList();// список номеров типов столбцов по драйверу
    public ArrayList colTypesName = new ArrayList();// список  названия типов столбцов по драйверу
    public ArrayList colLen = new ArrayList();// длинна столбцов
    public ArrayList colDec = new ArrayList();// длинна столбцов
    public ArrayList colClass = new ArrayList();// соответствие типа столбцов к классу java 
//    public Vector rowList = new Vector(); // хранить записи из результирующего множества выборки  УСТАРЕЛ  Vector()
    public ArrayList rowList = new ArrayList(); // хранить записи из результирующего множества выборки     18.08.2017 переход на   ArrayList() 21/06/2018
    public String[] COLNAMES; // хранит имена столбцов
    public Class[] COLCLASS; // хранит типы-классы столбцов
    public String[] COLTYPE; // хранит общие типы данных используемые в задаче 
    public String[] COLMASK; // хранит маски ввода по типу полей
    public int COLCOUNT = 0;//количество столбцов
    public int ROWCOUNT = 0;//количество записей
    public int RECNO = 0;//текущая запись
    public String SELE = "";
    public boolean CLOSE = true;// закрывать курсор
    
    /**
     * Формирование курсора из вектора
     * @param rs - вектор (двумерный массив объектов Vector)
     */
    public Cursorr(Vector rs) { 
        Object[] RECORD = (Object[]) rs.get(0);
        ROWCOUNT=rs.size();
        COLCOUNT=RECORD.length+1;
        COLTYPE=new String[COLCOUNT];
        Object obj;
            for (int i = 0; i < RECORD.length; i++) //по всем полям таблицы
            {
                obj=RECORD[i];
                if (obj instanceof Boolean) {
                    COLTYPE[i]=V.TYPE_BIT;
                    colNames.add("BIT_"+i); //имя столбца
                }
                if (obj instanceof String) {
                    COLTYPE[i]=V.TYPE_CHAR;
                    colNames.add("CHAR_"+i); //имя столбца
                    
                }
                if ((obj instanceof  int[]) || (obj instanceof  Float) || (obj instanceof  Double) || (obj instanceof  Integer) ) {
                    COLTYPE[i]=V.TYPE_NUMERIC;
                    colNames.add("NUMERIC_"+i); //имя столбца
                    
                }
                if (obj instanceof Date && !"Timestamp".equals(obj.getClass().getSimpleName())) {
                    COLTYPE[i]=V.TYPE_DATE;
                    colNames.add("DATE_"+i); //имя столбца
                    
                }
                if (obj instanceof Date && "Timestamp".equals(obj.getClass().getSimpleName())) {
                    COLTYPE[i]=V.TYPE_DATETIME;
                    colNames.add("DATETIME_"+i); //имя столбца
                }
                
            }
                colNames.add("RECNO"); //имя последнего столбца
                COLTYPE[RECORD.length]=V.TYPE_NUMERIC;                  
            for (int i = 0; i < rs.size(); i++) {
                RECORD = (Object[]) rs.get(i);
                ArrayList cellList=new ArrayList<Object>(Arrays.asList(RECORD));
                Object cellValue = null; //иницилизируем переменную             
                cellValue = new Integer(i+1);//номер записи
                cellList.add(cellValue); //добавляем объект со значением в контейнер
                Object[] cells = cellList.toArray();//копируем строку (контейнер) в  массив объектов 
                rowList.add(cells); // добавление массива объектов строки в контейнер строк 
            }  
            COLNAMES = new String[colNames.size()]; //формирование массива названия столбцов
            colNames.toArray(COLNAMES); //заполнение массива названия столбцов из контейнера colNames
    
    }

       /**
     * Формирование массива данных и его описания по результирующим данным
     * запроса
     *
     * @param rs результирующие данные после выполнения запроса
     */
 
    public Cursorr(ResultSet rs) {
                    int j = 0;
        try {
            STRUC = rs.getMetaData();// метаданные (описание полей)
            COLTYPE = new String[STRUC.getColumnCount() + 1];
            COLTYPE[STRUC.getColumnCount()] = V.TYPE_NUMERIC;
            COLMASK = new String[STRUC.getColumnCount() + 1];
            COLMASK[STRUC.getColumnCount()] = "9999999999";

            int i;
            for (i = 1; i <= STRUC.getColumnCount(); i++) //по всем полям таблицы 
            {
                colNames.add(STRUC.getColumnName(i).toUpperCase()); //имя столбца
                colTypes.add(STRUC.getColumnType(i));   // номер типа столбца
                colTypesName.add(STRUC.getColumnTypeName(i));//название типа 
                colLen.add(STRUC.getPrecision(i));            //длина поля столбца
                colDec.add(STRUC.getScale(i));            // кол-во десятичных знаков

                // выбрать нужный класс соответствующий типу данных

                switch (STRUC.getColumnType(i)) {
                    case Types.BLOB:
                        colClass.add(java.sql.Blob.class);
                        COLTYPE[i - 1] = V.TYPE_BLOD;
                        break;
                    case Types.CLOB:
                        colClass.add(java.sql.Clob.class);
                        COLTYPE[i - 1] = V.TYPE_CLOD;
                        break;
                    
                    case Types.INTEGER:
                        colClass.add(Integer.class);
                        COLTYPE[i - 1] = V.TYPE_NUMERIC;
                        break;
                    case Types.FLOAT:
                        colClass.add(Float.class);
                        COLTYPE[i - 1] = V.TYPE_NUMERIC;
                        break;
                    case Types.DOUBLE:
                    case Types.REAL:
                    case Types.DECIMAL:
                    case Types.NUMERIC:
                        colClass.add(Double.class);
                        COLTYPE[i - 1] = V.TYPE_NUMERIC;
                        break;
                    case Types.DATE:
                        colClass.add(java.sql.Date.class);
                        COLTYPE[i - 1] = V.TYPE_DATE;
                        break;
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        colClass.add(java.sql.Date.class);
                        if (STRUC.getColumnDisplaySize(i) <= 7) {
                            COLTYPE[i - 1] = V.TYPE_DATE;
                        } else {
                            COLTYPE[i - 1] = V.TYPE_DATETIME;
                        }
                        break;
                    case Types.BIT:
                        colClass.add(Boolean.class);
                        COLTYPE[i - 1] = V.TYPE_BIT;
                        break;
                    case -101:
                        colClass.add(java.sql.Date.class);
                        if (STRUC.getColumnDisplaySize(i) <= 7) {
                            COLTYPE[i - 1] = V.TYPE_DATE;
                        } else {
                            COLTYPE[i - 1] = V.TYPE_DATETIME;
                        }
                        break;
                    default:
                        if ("BIT_".equals(FF.SUBSTR(rs.getMetaData().getColumnName(i), 1, 4).toUpperCase())) {
                            COLTYPE[i - 1] = V.TYPE_BIT;
                        } else {
                            COLTYPE[i - 1] = V.TYPE_CHAR;
                        }
                        colClass.add(String.class);
                        break;
                }
                //         Определение маски ввода
                if (COLTYPE[i - 1] == V.TYPE_NUMERIC) {
                    if (STRUC.getScale(i) == 0) {
                        COLMASK[i - 1] = FF.REPLICATE("9", STRUC.getPrecision(i));
                    } else {
                        COLMASK[i - 1] = FF.REPLICATE("9", STRUC.getPrecision(i) - STRUC.getScale(i)) + "." + FF.REPLICATE("9", STRUC.getScale(i));
                    }
                }
                if (COLTYPE[i - 1] == V.TYPE_DATE) {
                    COLMASK[i - 1] = V.MASK_DATE;
                }
                if (COLTYPE[i - 1] == V.TYPE_DATETIME) {
                    COLMASK[i - 1] = V.MASK_DATETIME;
                }


            } //конец for

            // имена столбцов из списка сохранить в отдельный массив COLNAMES для упрощенного доступа
            //добавочный столбец номера записи
            colNames.add("RECNO"); //имя столбца
            colTypes.add(Types.INTEGER);   // номер типа столбца
            colTypesName.add("INTEGER");//название типа 
            colLen.add(7);            //длина поля столбца
            colDec.add(0);            // кол-во десятичных знаков
            COLCOUNT = colNames.size(); //количество столбцов        
            COLNAMES = new String[colNames.size()]; //формирование массива названия столбцов
            colNames.toArray(COLNAMES); //заполнение массива названия столбцов из контейнера colNames

            // классы как типы столбцов сохранить в отдельный массив COLCLASS
            COLCLASS = new Class[colClass.size()];
            colClass.toArray(COLCLASS);
            // цикл по всем записям таблицы
            String d1 = FF.DATETIMES();
            int jj = 0;
            long memf=0;
            long memt=0;
            long memm=0;
                     //   P.MESSERR("Размер записи:"+rs.getFetchSize());
                    memm=Runtime.getRuntime().maxMemory()/1024;    // максимально возможная для выделения
            while (rs.next() && V.CONN_RECCOUNT >= j) {
                jj++;
                if (jj == 500) {
                    memf=Runtime.getRuntime().freeMemory()/1024; //свободная память
                    memt=Runtime.getRuntime().totalMemory()/1024; //выделенная для работы                    

//                    P.ALERT("Получение данных...Начало:" + d1 + " Записей "+rs.getRow()+  " Память:" + memt+  " max:" + memm+" Остаток:"+memf);
                    P.ALERT("Получение данных...Начало:" + d1 + " Записей "+rs.getRow());                    
                    jj = 0;
                                //long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();            
                    if (memt==memm && memf<256) { //если выбрана максимальная память  и свободной осталось 256
                        P.MESSERR("  Переполнение памяти "+"\n Записей:"+rs.getRow()+"\n  Всего памяти " +memt+"\n Остаток:"+memf);
                     //   Runtime.getRuntime().gc();
           //             rs.getStatement().close();
                        CLOSE=false;
                        break;
                    }
                }

                ArrayList cellList = new ArrayList();//иницилизируем контейнер , хранить данные по каждому столбцу (ячейки)
                for (i = 0; i < COLCLASS.length; i++) {  //по всем столбцам

                    Object cellValue = null; //иницилизируем переменную 
                    //если char(1) BIT_* то как BIT 
                    if (COLCLASS[i] == String.class) {
                        if ("BIT_".equals(FF.SUBSTR(rs.getMetaData().getColumnName(i + 1), 1, 4).toUpperCase())) {
                            if ("T".equals(rs.getString(i + 1))) {
                                cellValue = V.TRUE;
                            } else {
                                cellValue = V.FALSE;
                            }
                        } else {
                            cellValue = rs.getString(i + 1);
                        }
                    }
                    if (COLCLASS[i] == java.sql.Blob.class) { //бинарные данные (файлы)
                        cellValue = rs.getBlob(i + 1);
                    }
                    if (COLCLASS[i] == java.sql.Clob.class) { //строковые данные (файлы)
                        cellValue = rs.getClob(i + 1);
                    }
                    
                    if (COLCLASS[i] == Integer.class) {
//                        cellValue = new Integer(rs.getInt(i + 1));
                        cellValue = rs.getInt(i + 1);
                        
                    }
                    if ((COLCLASS[i] == Float.class || COLCLASS[i] == Double.class) && rs.getMetaData().getScale(i + 1) > 0) { //если есть дробные то float
                        //cellValue = new Double(rs.getDouble(i + 1));
                         cellValue = rs.getDouble(i + 1);
                    }
                    if ((COLCLASS[i] == Float.class || COLCLASS[i] == Double.class) && rs.getMetaData().getScale(i + 1) == 0 && rs.getMetaData().getPrecision(i + 1) == 0) { //если все 0
                        //cellValue = new Double(rs.getDouble(i + 1));
                        cellValue = rs.getDouble(i + 1);                        
                    }

                    if ((COLCLASS[i] == Float.class || COLCLASS[i] == Double.class) && rs.getMetaData().getScale(i + 1) == 0 && rs.getMetaData().getPrecision(i + 1) > 0) { //если нет дробных int
                        //cellValue = new Integer(rs.getInt(i + 1));
                        cellValue = rs.getInt(i + 1);
                    }
                    if ((COLCLASS[i] == Float.class || COLCLASS[i] == Double.class) && rs.getMetaData().getPrecision(i + 1) > 100) { //если нет дробных и очень большое bIGint
                        //cellValue = new Integer(rs.getInt(i + 1));
                        cellValue = rs.getInt(i + 1);
                    }
                    if ((COLCLASS[i] == Float.class || COLCLASS[i] == Double.class) && rs.getMetaData().getScale(i + 1) < 0 && rs.getMetaData().getPrecision(i + 1) == 0) { //если  дробных<0 int
                        //cellValue = new Integer(rs.getInt(i + 1));
                        cellValue = rs.getInt(i + 1);
                    }

                    if (COLCLASS[i] == Double.class && cellValue==null) {
                      //  cellValue = new Double(rs.getDouble(i + 1));
                       cellValue = rs.getDouble(i + 1);                        
                    }
                    if (COLCLASS[i] == java.sql.Date.class) {
                        if (COLTYPE[i] == V.TYPE_DATE) {
                            cellValue = rs.getDate(i + 1);//Дата
                        } else {
//                          cellValue = rs.getDate(i + 1);//Дата STRUC.getColumnTypeName(i+1)= TIMESTAMP WITH TIME ZONE"                          
                           if (rs.getTimestamp(i + 1)!=null && "TIMESTAMP WITH TIME ZONE".equals(STRUC.getColumnTypeName(i+1)) && !"00:00:00".equals(V.time.format(rs.getTimestamp(i + 1)))) {
                            cellValue = new Timestamp(rs.getTimestamp(i + 1).getTime()+V.DIFF_DATE);//Дата+время
                            
                            if ("DATED".equals(COLNAMES[i])||"DATE_S".equals(COLNAMES[i])||"SALE_DATE".equals(COLNAMES[i]) ) {
                            int jjjjjj=0; //для отладки
                            }
                            
                            } else {
                                cellValue = rs.getTimestamp(i + 1);//
                            }
                        }
                            
                    }
                    if (COLCLASS[i] == Boolean.class) {
                        cellValue = rs.getBoolean(i + 1);
                    }

                    cellList.add(cellValue); //добавляем объект со значением в контейнер

                }// for запись (строка ) записана

                //добавляем последнюю колонку для recno
                Object cellValue = null; //иницилизируем переменную             
                cellValue = new Integer(j + 1);//номер записи
                cellList.add(cellValue); //добавляем объект со значением в контейнер
                j++;
                //Object[] cells = cellList.toArray();//копируем строку (контейнер) в  массив объектов 
                //rowList.add(cells); // добавление массива объектов строки в контейнер строк 
                 rowList.add(cellList.toArray());
            } // while
            if (CLOSE && rs != null && !rs.isClosed()) {
             rs.close();
            }
            
      //      if (rs != null && !rs.isClosed()) {
      //          rs.close();
       //     }
        } 
        catch (SQLException ex) {
            P.MESSERR("Ошибка заполнения курсора:" + "\n" + ex.toString().replace(".", ".\n"));
        }
        catch (OutOfMemoryError ex) {
          //  rowList.clear();
            //rowList.remove(j-1);
 //           rs.close();
            Runtime.getRuntime().gc();
            P.MESSERR("Ошибка памяти:" + "\n" + ex.toString().replace(".", ".\n"));
        }

            ROWCOUNT = rowList.size(); //количество записей (строк)



    }//конец конструктора

    /**
     * Получения объекта-значения из курсора по координатам
     *
     * @param row строка с 0
     * @param col колонка с 0
     * @return
     */
    public Object GETROWCOL(int row, int col) {
        if (rowList.size() == 0) {
            return null;
        }

        Object[] RECORD = (Object[]) rowList.get(row);
        return RECORD[col];

    }
}
