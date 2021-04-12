package forms;

import baseclass.Formr;
import baseclass.Gridr;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

public class History extends Formr {

    public History() {
        super("History", "История запросов", 1000, 650); //Вызов конструктора от базового класса FormMod}
        V.SELE = "SELECT MIN(DATE_S1) AS DATE1 from HISTORY ";
        P.SQLEXECT(V.SELE, "UD");
        setTitle("История запросов пользователей от "+A.GETVALS("UD.DATE1"));
    }

    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
        T[0] = P.addobjT(this, "f_query", "", 500, 120);
        B[2] = P.addobjB(this, "b_show", "Показать", "Обновить данные");
        F[0] = P.addobjF(this, "f_count", "100", 50, 30);
        F[0].SETTYPE(V.TYPE_NUMERIC);
        F[0].SETINPUTMASK("99999");
        F[1] = P.addobjF(this, "F1", "", 200, 30);
        F[2] = P.addobjF(this, "F2", "", 200, 30);
        F[3] = P.addobjF(this, "F3", "", 200, 30);
        
        L[0] = P.addobjL(this, "l_show_1", "последние");
        L[1] = P.addobjL(this, "l_show_2", "записей");
        L[2] = P.addobjL(this, "L2", "или ключевыя фразы запроса");
        
        G[0] = P.addobjG(this, "historyGrid", 1, 1, 750, 500);
//        G[0].SETDYNAMICCOLOR(6, "#SQL_T#.contains(\"select\")" + V.COLOR_CONDITION);
//        G[0].getColumn(6).DinamycBackColor = "#SQL_T#.contains(\"select\")" + V.COLOR_CONDITION;
//        G[0].getColumn(6).DinamycBackColor = "#PC_S#.contains(#USER_S#)" + V.COLOR_CONDITION;
        B[0] = P.addobjB(this, "b_query", "Весь запрос", "Весь запрос");
        B[1] = P.addobjB(this, "b_clear", "Очистить историю", "Очистка истории");
        B[3] = P.addobjB(this, "b_exec", "Выполнить", "Выполнить запрос");
    }

    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга
        if (G[0].SCROLL != null) {
            G[0].SCROLL.setBounds(1, 1, this.getWidth() - 20, this.getHeight() - 260);
        }
        if (T[0].SCROLL != null) {
            T[0].SCROLL.setBounds(1, this.getHeight() - 210, this.getWidth() - 20, T[0].SCROLL.getHeight());
        }
        locate(B[0], null, V.LOC_LEFT, 1, T[0].SCROLL, V.LOC_DOWN, 10);
        locate(B[1], B[0], V.LOC_RIGHT, 10, T[0].SCROLL, V.LOC_DOWN, 10);
        locate(B[2], null, V.LOC_LEFT, 1, G[0].SCROLL, V.LOC_DOWN, 10);
        locate(L[0], null, V.LOC_LEFT, 10, G[0].SCROLL, V.LOC_DOWN, 15);
        locate(F[0], L[0], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 10);
        locate(L[2], F[0], V.LOC_RIGHT, 80, G[0].SCROLL, V.LOC_DOWN, 15);
        locate(F[1], L[2], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 10);
        locate(F[2], F[1], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 10);
        locate(F[3], F[2], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 10);
        locate(B[2], F[3], V.LOC_RIGHT, 1, G[0].SCROLL, V.LOC_DOWN, 10);
        locate(L[1], F[0], V.LOC_RIGHT, 10, G[0].SCROLL, V.LOC_DOWN, 15);
        locate(B[3], null, V.LOC_RIGHT, 10, T[0].SCROLL, V.LOC_DOWN, 10);
    }

    @Override
    public void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
        String C[] = {"Номер", "Пользователь", "Компьютер", "Дата выполнения", "Время выполнения(сек.)", "Запрос", "Программа", "Логин", "Статус выполнения"};
        String FD[] = {"REL", "USER_S", "PC_S", "DATE_S1", "TIME_S", "SQL_T", "PROGRAMM", "USER_L", "BIT_STATUS"};
        int S[] = {63, 90, 79, 161, 91, 298, 87, 61, 90};
        V.CAPTION = C;
        V.FIELD = FD;
        V.FSIZE = S;

    }

    @Override
    public String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
        String WHERE="";
        if (!FF.EMPTY(F[1].getText())) {
            WHERE=WHERE+" AND  UPPER(SQL_T) LIKE'%"+F[1].getText().toUpperCase()+"%'";
        }
        if (!FF.EMPTY(F[2].getText())){
            WHERE=WHERE+" AND  UPPER(SQL_T) LIKE'%"+F[2].getText().toUpperCase()+"%'";
        }
        if (!FF.EMPTY(F[3].getText())){
            WHERE=WHERE+" AND  UPPER(SQL_T) LIKE'%"+F[3].getText().toUpperCase()+"%'";
        }
        String WHERE2=" WHERE ";
        WHERE2=WHERE2+" ROWNUM <= " + F[0].getText();
        
        if (!FF.EMPTY(WHERE) ) {
            WHERE=" WHERE "+FF.SUBSTR(WHERE,5); 
        }
        String SELE = "SELECT * FROM (SELECT REL,USER_S, PC_S, DATE_S1, TIME_S, SUBSTR(SQL_T, 1, 60) SQL_T, PROGRAMM, USER_L, BIT_STATUS from HISTORY "+WHERE+" ORDER BY REL DESC) ";
        SELE =SELE+ WHERE2;
//        FF._CLIPTEXT(SELE);
        return SELE;
    }

    public void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
        GRID.VIEWRECNO = 0; //не показывать номер записи в первой колонке -0
        GRID.RESTORECOL = 1;//не восстанавливать размеры -0 
//     GRID.FREAD="users_k";//форма корректуры
        GRID.ALIAS = "HISTORY";     //алиас данныхдля таблицы   
        GRID.SETLOCATEFOR("DATE_S1");
    }
    public Formr FBROWSE;

    @Override
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
        if ("b_clear".equals(name)) {
            P.SQLUPDATE("delete from HISTORY");
            V.TOOLBAR.QUERY(G[0]);
            G[0].SETFOCUS();            
        }
        if ("b_query".equals(name)) {
            try {
                V.SELE = "select distinct SQL_T from HISTORY where REL = " + A.GETVALSQL("HISTORY.REL");
                Object[] RECORD = (Object[]) P.SQLEXECT(V.SELE).rowList.get(0);
                T[0].SETVALUE((String) RECORD[0]);
            } catch (IndexOutOfBoundsException ex) {
                P.WRITE_INFO("Не выделена строка.");
                T[0].SETVALUE("");
            }
        }
        if ("b_show".equals(name)) {
            V.TOOLBAR.QUERY(G[0]);
            G[0].SETFOCUS();            
        }
        if ("b_exec".equals(name) && !T[0].getText().equals("")) {
            V.PARAMTO = new String[2];
            V.PARAMTO[0] = T[0].getText();
            V.PARAMTO[1] = V.SELECT;
            if (FBROWSE != null) {
                FBROWSE.DESTROY();
                FBROWSE = null;
            }
            FBROWSE = P.DOFORM("browse");
        }
    }

    @Override
    public void OPEN() {
        G[0].SETFOCUS();

//     System.out.print("3"+TABLE.getParent().getName()  );  
    }

    //НЕОБЯЗАТЕЛЬНЫЙ КОД - ДЛЯ ПРИМЕРА ЧТО МОЖНО ПЕРЕКРЫТЬ 
    public String PREV_KEY(Gridr GRID) {//ПОЛУЧЕНИЕ КЛЮЧЕВОГО ВЫРАЖЕНИЯ В WHERE
        return super.PREV_KEY(GRID);
    }

    public String PREV_UPDATE(Gridr GRID) {// для таблиц получение запроса корректуры
        return super.PREV_UPDATE(GRID);
    }

    public String PREV_INSERT(Gridr GRID) {
        return super.PREV_INSERT(GRID);
    }    // для таблиц получение запроса новой записи

    public String PREV_DELETE(Gridr GRID) {
        return super.PREV_DELETE(GRID);
    }    // для таблиц получение запроса удаления записи
/////////////////////////////  

    @Override
    public void CHANGERECNO(Gridr GRID, int row, int rec) {
        T[0].SETVALUE("");
    }
}
