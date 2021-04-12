/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import static aplclass.Create.STR;
import baseclass.Formr;
import baseclass.Gridr;
import java.util.Vector;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

/**
 *
 * @author Kostya
 */
public class users extends Formr {

    public users() {
        super("users", "Настройка пользователей", 760, 480); //Вызов конструктора от базового класса FormMod

    }

    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
        L[0] = P.addobjL(this, "l0", "Доступ к пунктам меню и специальным функциям", 350, 30);
        T[0] = P.addobjT(this, "t1", "", 350, 50);
        T[0].TYPE = V.TYPE_CHAR;
        T[0].SETREADONLY(true);
        G[0] = P.addobjG(this, "users", 1, 1, 450, 300);
        B[0] = P.addobjB(this, "b_history", "История сессий", "Показать историю сессий");
        B[1] = P.addobjB(this, "B1", "Доп. права", "Допуск к дополнительным функциям");

    }

    public void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
        GRID.VIEWRECNO = 0; //не показывать номер записи в первой колонке -0
        GRID.RESTORECOL = 1;//не восстанавливать размеры-0
        GRID.FREAD = "users_k";//форма корректуры
        GRID.ALIAS = "users";     //алиас данныхдля таблицы   
    }

    @Override
    public void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
        String C[] = {"Номер пользователя", "Пароль", "Ф.И.О.", "Доступ к меню","Код подразделение","Название подразделения"};
        String F[] = {"NOMER", "NAME", "FIO", "MENU","KPODR","PODR"};
        int S[] = {90, 80, 327, 698,70,200};
        V.CAPTION = C;
        V.FIELD = F;
        V.FSIZE = S;

    }

    @Override
    public void LOC_ABOUT() {
        if (G[0].SCROLL != null) {
            G[0].SCROLL.setBounds(1, 1, this.getWidth() - 20, this.getHeight() - 150);
        }
        T[0].SCROLL.setSize(this.getWidth() - 20, T[0].getHeight());
        locate(L[0], G[0].SCROLL, V.LOC_CENTR, 0, G[0].SCROLL, V.LOC_DOWN, 0);
        locate(T[0].SCROLL, L[0], V.LOC_CENTR, 0, L[0], V.LOC_DOWN, 0);
        locate(B[0], null, V.LOC_LEFT, 0, null, V.LOC_DOWN, 0);
        locate(B[1],B[0] , V.LOC_RIGHT, 30, T[0].SCROLL, V.LOC_DOWN, 0);
        
    }

    @Override
    public void OPEN() {
        G[0].SETFOCUS();
//     System.out.print("3"+TABLE.getParent().getName()  );  
    }

    @Override
    public String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
        String SELE = "SELECT * FROM users ORDER BY DATE_S";
        return SELE;
    }

    @Override //НУЖЕН ЕСЛИ ОБЩИЙ SELECT C ДОБАВКАМИ
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        V.SELE = "SELECT * FROM users " + " WHERE " + PREV_KEY_REC(GRID);// для вставки записи
        return V.SELE;
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
        T[0].SETVALUE(A.GETVAL(GRID.ALIAS, "MENU", GRID.RECNO));
    }

    @Override
    public void CLICK_ALL(String name) {
        if ("b_history".equals(name)) {
            P.DOFORM("HISTORY_SESSION");
            G[0].requestFocus();
        }
        if ("B1".equals(name)) {
            Vector MENUD = new Vector();
            String menu=A.GETVALS("USERS.MENU");
            for (int i=0;i<V.USER_MENUD.size();i++) {
                        Object[] RECORD = (Object[]) V.USER_MENUD.get(i);
                        Object[] RECORDNEW=new Object[3];                        
                        if (FF.AT(","+FF.ALLTRIM((String)RECORD[0])+",", menu)>0){
                        RECORDNEW[0]=new Boolean(true);       
                        } else {
                        RECORDNEW[0]=new Boolean(false);       
                        }
                        RECORDNEW[1]=RECORD[0];                             
                        RECORDNEW[2]=RECORD[1];                             
                        MENUD.add(RECORDNEW);
                     }
                  P.SET_CURSOR_FROM_DIM(MENUD,"MENUD");      
                  Boolean tf= P.DOVIEW("MENUD","Дополнительные разрешения", "Выбор", "Код", "Описание", "BIT_0", "CHAR_1","CHAR_2",100,150,300); 
                  if (!tf) {return;}
        A.SELECT("MENUD");
        int REC = A.RECCOUNT();
        for (int i = 1; i <= REC; i++) {
            if ((boolean) A.GETVAL("BIT_0", i) == true &&  (FF.AT(","+FF.ALLTRIM((String)A.GETVAL("CHAR_1", i))+",", menu)==0)) {  //если выбран и нет в меню
                menu=menu+","+FF.ALLTRIM((String)A.GETVAL("CHAR_1", i))+",";
             }
           if ((boolean) A.GETVAL("BIT_0", i) == false &&  (FF.AT(","+FF.ALLTRIM((String)A.GETVAL("CHAR_1", i))+",", menu)>0)) {  //если не выбран и есть в меню
                menu=menu.replaceAll( ","+FF.ALLTRIM((String)A.GETVAL("CHAR_1", i))+",","");
             }

            }
        //НУЖНО ЗАМЕНИТЬ MENU
           V.SELE = "";
           V.SELE = "UPDATE USERS SET MENU="+P.P_SQL(menu)+"  WHERE NOMER=" +  A.GETVALSQL("USERS.NOMER");
           if (P.SQLUPDATE(V.SELE)>0){
            V.TOOLBAR.QUERY(G[0]);
            G[0].SETFOCUS();            

        }
        
    
    }
    }



}


