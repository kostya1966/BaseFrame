package forms;

import baseclass.Formr;
import baseclass.Gridr;
import java.text.SimpleDateFormat;
import prg.A;
import prg.P;
import prg.V;

public class HISTORY_SESSION extends Formr {

    public HISTORY_SESSION() {
        super("HISTORY_SESSION", "История сессий", 1000, 650); //Вызов конструктора от базового класса FormMod}
    }

    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
        F[0] = P.addobjF(this, "f_date_s", "", 100, 30);
        F[0].SETTYPE(V.TYPE_DATE);
        F[1] = P.addobjF(this, "f_date_e", "", 100, 30);
        F[1].SETTYPE(V.TYPE_DATE);
        L[0] = P.addobjL(this, "l_show_1", "сессии с");
        L[1] = P.addobjL(this, "l_show_2", "по");
        G[0] = P.addobjG(this, "g_history", 1, 1, 750, 500);
        B[0] = P.addobjB(this, "b_show", "Показать", "Показать сессии");
        B[1] = P.addobjB(this, "b_close", "Выйти", "Закрыть окно");
        B[2] = P.addobjB(this, "b_clear", "Очистка", "Очистка истории сессий");
    }

    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга
        if (G[0].SCROLL != null) {
            G[0].SCROLL.setBounds(1, 1, this.getWidth() - 20, this.getHeight() - 80);
        }
        locd(G[0].SCROLL,L[0],10); locr(F[0],0); locr(L[1],10); locr(F[1],0); locr(B[0],5); locr(B[1],30); locr(B[2],10);

    }

    @Override
    public void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
        String C[] = {"Номер", "IP адрес", "Компьютер", "Пользователь", "Начало сессии", "Окончание", "Время(мин)", "ФИО", "Программа", "Версия"};
        String FD[] = {"SESSIONID", "IP", "HOST", "OS_USER", "DATE_S", "DATE_E", "SESSION_TIME", "USER_FIO", "PROGRAMM", "VER"};
        int S[]={75,104,100,100,139,133,69,98,100,176};
        V.CAPTION = C;
        V.FIELD = FD;
        V.FSIZE = S;

    }

    @Override
    public String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
        //String SELE = "select * from (select REL,USER_S, PC_S, DATE_S1, TIME_S, SUBSTR(SQL_T, 1, 30) SQL_T, PROGRAMM, USER_L, BIT_STATUS from HISTORY order by date_s1 desc) where ROWNUM <= " + F[0].getText();
        String SELE = "select * from history_session where "
                + ((F[0].getText().equals(V.DATE_TEXT) && F[1].getText().equals(V.DATE_TEXT)) ? "to_char(date_s, 'dd.mm.rrrr') = to_char(sysdate, 'dd.mm.rrrr')"
                : ((F[0].getText().equals(V.DATE_TEXT) && !F[1].getText().equals(V.DATE_TEXT)) ? "date_s <= to_date('" + F[1].getText() + "', 'dd.mm.rrrr')+1"
                : ((!F[0].getText().equals(V.DATE_TEXT) && F[1].getText().equals(V.DATE_TEXT)) ? "date_s >= to_date('" + F[0].getText() + "', 'dd.mm.rrrr')"
                : "date_s between to_date('" + F[0].getText() + "', 'dd.mm.rrrr') and to_date('" + F[1].getText() + "', 'dd.mm.rrrr')+1")));
         SELE=SELE+" ORDER BY DATE_S DESC" ;
        return SELE;
    }

    public void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
        GRID.VIEWRECNO = 0; //не показывать номер записи в первой колонке -0
        GRID.RESTORECOL = 1;//не восстанавливать размеры -0 
//     GRID.FREAD="users_k";//форма корректуры
        GRID.ALIAS = "HISTORYSESSION";     //алиас данныхдля таблицы   
    }
    public Formr FBROWSE;

    @Override
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
        if ("b_clear".equals(name)) {
            P.SQLUPDATE("delete from HISTORY_SESSION");
            V.TOOLBAR.QUERY(null);
        }
        if ("b_show".equals(name)) {
            V.TOOLBAR.QUERY(null);
        }
        if ("b_close".equals(name)) {
            DESTROY();
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
    }
}
