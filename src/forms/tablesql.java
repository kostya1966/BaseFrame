/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import aplclass.Create;
import static aplclass.Create.STR;
import baseclass.Formr;
import baseclass.Gridr;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

/**
 *
 * @author Kostya
 */
public class tablesql extends Formr {
    public tablesql() {
        super("tablesql", "Список таблиц базы данных", 1100, 500); //Вызов конструктора от базового класса FormMod
        if (FF.EMPTY(V.SXEMA) && V.CONN_DRIVER.equals("1")) {
        V.SXEMA=(String)((Object[])P.SQLEXECT("SELECT sys_context('userenv', 'CURRENT_SCHEMA') FROM DUAL").rowList.get(0))[0]  ;
        }
        if (FF.EMPTY(V.SXEMA) && V.CONN_DRIVER.equals("3")) {
        V.SXEMA="public" ;
        }
    }

    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
        B[0] = P.addobjB(this, "B0", "Просмотр данных", "Просмотр данных в текущей таблицы");
        B[1] = P.addobjB(this, "B1", "Структура таблицы", "Просмотр структуры текущей таблицы");
        B[3] = P.addobjB(this, "B3", "Триггер", "Генерация триггера");
        F[0] = P.addobjF(this, "t1", "", 200, 30);
//     F[0].TYPE=V.TYPE_DATE;F[0].INPUTMASK=V.MASK_DATETIME;
//     F[0].TYPE=V.TYPE_NUMERIC; F[0].INPUTMASK="99999999.99";
        F[0].SETREADONLY(false);
        B[2] = P.addobjB(this, "B2", "Записать", "Записать описание таблицы");
        B[4] = P.addobjB(this, "B4", "Скрипт views", "Создать скрипт всех представлений");
        B[5] = P.addobjB(this, "B5", "Скрипт ТРИГГЕРА-КОПИИ", "Создать скрипт формирования таблицы -копии и триггера заполнения при удалении и корректуре");
        
        G[0] = P.addobjG(this, "tablesql", 1, 1, 750, 500);
        F[1] = P.addobjF(this, "F1", V.SXEMA, 200, 30);
        F[1].SETREADONLY(true);
//        G[0].getColumn(2).SETCOLUMNCONTROL(new JComboBox<>(new String[]{"", "Команды сервиса обработки", "Грппы команд сервиса", "Настройка данных учетных групп"
//        ,"Справочник артикулов", "Справочник операций", "Справочник ЕАН", "Справочник номенклатуры"
//        ,"Справочник должностей", "Справочник магазинов", "Товарные группы", "Справочник товаров", "Виды учета", "Справочник пользователей"}));
//        G[0].getColumn(1).SETCOLUMNCONTROL(new JButton("but1"));
//        G[0].getColumn(2).SETCOLUMNCONTROL(new JButton("but2"));
    }

    public void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
        GRID.VIEWRECNO = 0; //не показывать номер записи в первой колонке -0
        GRID.RESTORECOL = 1;//не восстанавливать размеры -0 
//     GRID.FREAD="users_k";//форма корректуры
        GRID.ALIAS = "TABLESQL";     //алиас данныхдля таблицы   
        GRID.LOCATEFOR="NAME";
    }

    @Override
    public void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
        String C[] = {"Название таблицы-представления", "Описание","Тип"};
        String F[] = {"NAME", "DESCR","TYPE"};
        int S[]={150,415,126};
        V.CAPTION = C;
        V.FIELD = F;
        V.FSIZE = S;

    }

    @Override
    public void CLICK_ALL(String name) {
        if ("B0".equals(name)) {//Д
            V.PARAMTO = new String[2];
            String str = (String) A.GETVAL("TABLESQL.NAME");
            V.PARAMTO[0] = str;
            V.PARAMTO[1] = V.TABLE;
            if (V.FBROWSE != null) {
                V.FBROWSE.DESTROY();
                V.FBROWSE = null;
            }
            V.FBROWSE = P.DOFORM("browse");
        }
        if ("B1".equals(name)) {//Д
            V.PARAMTO = new String[2];
            String str = (String) A.GETVAL("TABLESQL.NAME");
            V.PARAMTO[0] = str;
            V.PARAMTO[1] = V.TABLE;
            if (V.FSTRU != null) {
                V.FSTRU.DESTROY();
                V.FSTRU = null;
            }
            A.CLOSE("STRUCSQL");
            V.FSTRU = P.DOFORM("strucsql");
        }

        if ("B2".equals(name)) {//  комментарий на таблицу
            if ("1".equals(V.CONN_DRIVER) || ("3".equals(V.CONN_DRIVER) && A.GETVALN("TABLESQL.ID_TYPE")==1)) { //для орасле ИЛИ  POSTGRE И ТАБЛИЦА
            P.SQLUPDATE("COMMENT ON TABLE "+V.SXEMA+"." + A.GETVALS("TABLESQL.NAME") + " IS '" + F[0].GETVALUE() + "'");
            }
            if ( ("3".equals(V.CONN_DRIVER) && A.GETVALN("TABLESQL.ID_TYPE")==2)) { //для   POSTGRE И ВЬЮ
            P.SQLUPDATE("COMMENT ON VIEW "+V.SXEMA+"." + A.GETVALS("TABLESQL.NAME") + " IS '" + F[0].GETVALUE() + "'");
            }
            
            if ("2".equals(V.CONN_DRIVER)) {//для sql server
                V.SELE="";
                V.SELE=V.SELE+"IF EXISTS(SELECT * FROM "+V.SXEMA+".discr_rkv WHERE NAMEO=" + A.GETVALSQL("TABLESQL.NAME") + ") "+"\n";
                V.SELE=V.SELE+" UPDATE "+V.SXEMA+".discr_rkv SET VALUE1='" + F[0].GETVALUE() + "' WHERE NAMEO=" + A.GETVALSQL("TABLESQL.NAME") + " "+"\n";
                V.SELE=V.SELE+"ELSE "+"\n";
                V.SELE=V.SELE+" INSERT INTO "+V.SXEMA+".discr_rkv (NAMEO,VALUE1,NAMEP) VALUES (" + A.GETVALSQL("TABLESQL.NAME") + ",'" + F[0].GETVALUE() + "','') "+"\n";
                P.SQLUPDATE(V.SELE);
                
            }
            QUERY_REC(G[0]);
            G[0].SETFOCUS();
        }
        
        if ("B3".equals(name)) {//
            V.PARAMTO = new String[2];
            String str = (String) A.GETVAL("TABLESQL.NAME");
            V.PARAMTO[0] = str;
            V.PARAMTO[1] = V.TABLE;
            if (V.FSTRU != null) {
                V.FSTRU.DESTROY();
                V.FSTRU = null;
            }
            A.CLOSE("TRIGGERGEN");
            V.FSTRU = P.DOFORM("TriggerGen");
        }
        if ("B4".equals(name)) {//скрипт VIEW
          P.ALERT("Формирование таблицы исходных текстов");
            V.SELE = "";
            V.SELE = V.SELE + "BEGIN \n";
            V.SELE = V.SELE + " DELETE FROM "+V.SXEMA+".RI_VIEW_SCRIPT;\n";
            V.SELE = V.SELE + " "+V.SXEMA+".SET_VIEW_SCRIPT();\n";
            V.SELE = V.SELE + "END; \n";
           if (P.SQLUPDATE(V.SELE)<=0){
            return ;
           }
             STR=Create.VIEW();
             FF._CLIPTEXT(STR);
             if ( P.MESSYESNO("Скрипт занесен в буфер обмена (Ctrl+V вставить)\n"+"Зписать в файл ?")==0  ) {
                FF.STRTOFILE(STR, "");
             }
           
        }
        if ("B5".equals(name)) {//скрипт TRRIGER
          P.ALERT("Формирование исходных текстов триггера");
             STR=Create.TR_SCRIPT_COPY(A.GETVALS("TABLESQL.NAME"),A.GETVALS("TABLESQL.DESCR"));
             FF._CLIPTEXT(STR);
             if ( P.MESSYESNO("Скрипт занесен в буфер обмена (Ctrl+V вставить)\n"+"Зписать в файл ?")==0  ) {
                FF.STRTOFILE(STR, "");
             }
           
        }
        
        
    }

    @Override
    public void DBLCLICK_ALL(String name) {
        if ("F1".equals(name)) {//СМЕНА СХЕМЫ
                   P.SQLEXECT("SELECT USERNAME FROM all_users ORDER BY USERNAME ", "UD_SXEMA");
                    Boolean tf= P.DOVIEW("UD_SXEMA","Выберите схему для работы", "Схема","USERNAME" ,350);
              if (!tf)  {
                 return;
              }
            V.SXEMA=A.GETVALS("UD_SXEMA.USERNAME");
            F[1].SETVALUE(V.SXEMA);
            QUERY(G[0]);
            G[0].SETFOCUS();
        }
        
    }
 

    
    @Override
    public String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
        String SELE = "SELECT * FROM "+V.SXEMA+".BASIC_TABLE  WHERE  OWNER='"+V.SXEMA+"' ORDER BY NAME";
        return SELE;
    }
   @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        V.SELE = "SELECT *  FROM "+V.SXEMA+".BASIC_TABLE WHERE  OWNER='"+V.SXEMA+"' AND NAME=" + A.GETVALSQL("TABLESQL.NAME");// для обновления записи
        return V.SELE;
    }

    @Override
    public void LOC_ABOUT() {
        if (G[0].SCROLL != null) {
            G[0].SCROLL.setBounds(1, 1, this.getWidth() - 250, this.getHeight() - 80);
        }
        locate(F[1], G[0].SCROLL, V.LOC_RIGHT, 10, null, V.LOC_UP, 0);
        locate(B[0], null, V.LOC_LEFT, 0, G[0].SCROLL, V.LOC_DOWN, 0);
        locate(B[1], B[0], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 0);
        locate(F[0], B[3], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 0);
        locate(B[2], F[0], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 0);
        locate(B[3], B[1], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 0);
        locate(B[4], B[2], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 0);
        locate(B[5], B[4], V.LOC_RIGHT, 0, G[0].SCROLL, V.LOC_DOWN, 0);

    }

    @Override
    public void OPEN() {
        G[0].SETFOCUS();
//     System.out.print("3"+TABLE.getParent().getName()  );  
    }


    @Override
    public void CHANGERECNO(Gridr GRID, int row, int rec) {
        F[0].SETVALUE(A.GETVALS("TABLESQL.DESCR"));

    }
}
