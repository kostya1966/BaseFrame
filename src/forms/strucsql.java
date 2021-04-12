package forms;

import aplclass.Create;
import static aplclass.Create.STR;
import baseclass.Formr;
import baseclass.Gridr;
import baseclass.Reportr;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

/**
 CREATE OR REPLACE VIEW BASIC_COLUMN AS
SELECT a.table_name AS NAMEO,SUBSTR(a.column_name,1,50) as NAME,a.data_type AS TYPE,
CASE WHEN a.data_type='VARCHAR2' or a.data_type='CHAR'  THEN CHAR_LENGTH  ELSE DATA_PRECISION END as LEN
  ,a.data_scale AS DEC,a.data_default AS DEF,SUBSTR(b.comments,1,50) AS DESCR ,'T' AS TYPEO,'ОРАКЛ' AS COL
  ,a.OWNER
  FROM all_tab_columns A LEFT JOIN all_col_comments B  ON b.OWNER=a.OWNER and b.table_name = a.table_name AND b.column_name = a.column_name
  WHERE a.OWNER=sys_context('userenv', 'CURRENT_SCHEMA')
  ORDER BY a.table_name,A.COLUMN_ID	
 *
 * @author Kostya
 */

public class strucsql extends Formr {
 public String SXEMA=""  ;

public strucsql() {  
    super("strucsql","Список полей таблицы "+A.GETVALS("TABLESQL.NAME"),1200,500); //Вызов конструктора от базового класса FormMod
    ENABLE_PRINT=true;
}

    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
     B[0] =P.addobjB(this, "B0", "Класс формы просмотра", "Создание класса для создания формы с гридом для текущей таблицы");        
     B[1] =P.addobjB(this, "B1", "Класс формы корректуры", "Создание класса для создания формы корректуры для текущей таблицы");
     B[3] =P.addobjB(this, "B3", "Дополнительные функции", "Выбор дополнительного набора функций");     
     F[0]=P.addobjF(this,"t1", "", 250, 30); F[0].TYPE=V.TYPE_CHAR;
     F[0].SETREADONLY(false);     
     B[2] =P.addobjB(this, "B2", "Записать", "Записать описание поля");
     G[0] =P.addobjG(this,"strucsql",1,1,880, 500); 
   
    }
    
    public  void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
     GRID.VIEWRECNO=0; //не показывать номер записи в первой колонке -0
     GRID.RESTORECOL=1;//не восстанавливать размеры -0 
     GRID.ALIAS="STRUCSQL";     //алиас данныхдля таблицы   
//   GRID.FREAD="users_k";//форма корректуры
    }
    
   @Override
    public  void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
     String C[]={"ПРОСМОТР","РЕДАКТОР","КЛЮЧ","ЧТЕНИЕ","Название поля","Тип","Длина","Десятичные","По умолчанию","Описание"};
     String F[]={"BIT_PR","BIT_ED","BIT_KEY","BIT_READ", "NAME", "TYPE", "LEN", "DEC", "DEF", "DESCR"}     ;
     int S[]={76,70,44,57,84,89,44,78,99,265};
     V.CAPTION=C;
     V.FIELD=F;     
     V.FSIZE=S;
      
  }
   @Override
    public  String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
    String SELE="SELECT 'F' AS BIT_PR,'F' AS BIT_ED,'F' AS BIT_KEY,'F' AS BIT_READ,A.*  FROM "+V.SXEMA+".BASIC_COLUMN A WHERE A.OWNER='"+V.SXEMA+"' AND A.NAMEO='"+V.PARAMTO[0]+"'";
    return SELE;
  }

   
 
    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга
      if (G[0].SCROLL!=null) {
            G[0].SCROLL.setBounds(1, 1, this.getWidth()-20, this.getHeight()-80);
        }        
     locate(B[3],null,V.LOC_LEFT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(B[0],B[3],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;       
     locate(B[1],B[0],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(F[0],B[1],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,5) ;               
     locate(B[2],F[0],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;               
      
    }
   public Formr FSTRU;
    @Override
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
         if ("B0".equals(name)) {//Создание класса для создания формы с гридом для текущей таблицы
           Create.FORM(A.GETVALS("TABLESQL.NAME"),A.GETVALS("TABLESQL.DESCR"));
           }
         if ("B1".equals(name)) {//Создание класса для создания формы корректуры для текущей таблицы
           Create.FORM_K(A.GETVALS("TABLESQL.NAME"),A.GETVALS("TABLESQL.DESCR"));
           }
         
         if ("B2".equals(name)) {//
             V.SELE="COMMENT ON COLUMN "+V.SXEMA+"."+A.GETVALS("TABLESQL.NAME")+"."+A.GETVALS("STRUCSQL.NAME")+" IS '"+F[0].GETVALUE()+"'" ;
             P.SQLUPDATE(V.SELE);
             System.out.println(V.SELE);
             QUERY(G[0]);  
             G[0].SETFOCUS();
           }

         if ("B3".equals(name)) {//Дополнительные функции
        String [] strm ={"Выбрать все для просмотра","Выбрать все для редактирования","Создать все методы PREV_*","Создать PREV_KEY","Создать PREV_UPDATE","Создать PREV_INSERT","Создать PREV_DELETE","Список полей в буфер обмена","Список полей (метод PREV_INITCOLUMN)","Описание от другой таблицы-представления","Текущее описание поля для всех таблицы-представления","Файл скриптов для создания коментариев таблиц-представлений","Файл скриптов для создания коментариев полей "};
        int n=P.MENU(strm,B[3]);
        if (n==0){return;}
        if (n==1){//Выбрать все для просмотра
            for(int i=0;i<G[0].getRowCount();i++){
            G[0].setValueAt(true, i, 1);
            }
            G[0].SETFOCUS();
            }
        
        if (n==2){//Выбрать все для редактирования
            for(int i=0;i<G[0].getRowCount();i++){
            G[0].setValueAt(true, i, 2);
            }
            G[0].SETFOCUS();
            }
        if (n==3){//Создать методы PREV_*
             Create.FORM_PREV(A.GETVALS("TABLESQL.NAME"),A.GETVALS("TABLESQL.DESCR"),0);
            }
        if (n==4){//Создать методы PREV_*
             Create.FORM_PREV(A.GETVALS("TABLESQL.NAME"),A.GETVALS("TABLESQL.DESCR"),1);
            }
        if (n==5){//Создать методы PREV_*
             Create.FORM_PREV(A.GETVALS("TABLESQL.NAME"),A.GETVALS("TABLESQL.DESCR"),2);
            }
        if (n==6){//Создать методы PREV_*
             Create.FORM_PREV(A.GETVALS("TABLESQL.NAME"),A.GETVALS("TABLESQL.DESCR"),3);
            }
        if (n==7){//Создать методы PREV_*
             Create.FORM_PREV(A.GETVALS("TABLESQL.NAME"),A.GETVALS("TABLESQL.DESCR"),4);
            }
        if (n==8){//Список полей
            String STR="";
            for (int i = 1; i <= A.RECCOUNT("STRUCSQL"); i++) {
            A.GOTO("STRUCSQL",i);
            STR = STR + A.GETVALS("STRUCSQL.NAME") + ",";
            }
           STR = FF.SUBSTR(STR, 1, FF.LEN(STR) - 1);
        FF._CLIPTEXT(STR);
        P.MESS(STR+" занесен в буфер обмена (Ctrl+V вставить)");
            
        }

        if (n==9){//PREV_INITCOLUMN
             STR=Create.FORM_PREV_INITCOLUMN(A.GETVALS("TABLESQL.NAME"),A.GETVALS("TABLESQL.DESCR"));
             FF._CLIPTEXT(STR);
             P.MESS("Исходный текст метода PREV_INITCOLUMN занесен в буфер обмена (Ctrl+V вставить в файл)");
             
            }
        if (n==10){//Описание полей из другой таблицы 
             V.SELE="SELECT NAME||' '||DESCR AS TEXT,NAME FROM "+V.SXEMA+".BASIC_TABLE WHERE NAME!="+ A.GETVALSQL("TABLESQL.NAME")+" ORDER BY NAME  \n";
            int nn=P.MENU(P.FILL_ARRAY_FROM_QUERY(V.SELE), G[0]);
            if (nn==0) {return;}
             nn--; //P.FILL_ARRAY_FROM_QUERY ВЫДАЕТ С ОДНОЙ ВЕРХНЕЙ ЗАПИСЬЮ
            String NAMETO=A.GETVALSQL("TABLESQL.NAME");
            String NAMEOT=A.GETVALSQL("TMP.NAME", nn);
            if (P.MESSYESNO("Записать коментарии к столбцам из "+NAMEOT+" для "+ NAMETO) != 0) {
                return;
            }
            V.SELE = "";
            V.SELE = V.SELE + "BEGIN \n";
            V.SELE = V.SELE + ""+V.SXEMA+".COMMENT_IMPORT(" + NAMEOT +","+NAMETO+ ");\n";//ВЫПОЛНЕНИЕ ХРАНИМОЙ ПРОЦЕДУРЫ 
            V.SELE = V.SELE + "END; \n";
            P.SQLEXECUT(V.SELE);
            V.TOOLBAR.QUERY(G[0]);
        }     
        if (n==11){//комментарий для всех
            String POLE=A.GETVALSQL("STRUCSQL.NAME");
            String COMM=A.GETVALSQL("STRUCSQL.DESCR");
            if (P.MESSYESNO("Записать коментарии: "+COMM+ " к столбцу: "+POLE) != 0) {
                return;
            }
            V.SELE = "";
            V.SELE = V.SELE + "BEGIN \n";
            V.SELE = V.SELE + ""+V.SXEMA+".COMMENT_POLE(" + POLE +","+COMM+ ");\n";//ВЫПОЛНЕНИЕ ХРАНИМОЙ ПРОЦЕДУРЫ 
            V.SELE = V.SELE + "END; \n";
            P.SQLEXECUT(V.SELE);
            V.TOOLBAR.QUERY(G[0]);
        }   
        if (n==12){//комментарии для таблиц в файл
            String SELE = "SELECT * FROM "+V.SXEMA+".BASIC_TABLE ORDER BY NAME";
            P.SQLEXECT(SELE, "UD");
            String STR="";
            for (int i = 1; i <= A.RECCOUNT("UD"); i++) {
            A.GOTO("UD",i);
            if (FF.EMPTY(A.GETVALS("UD.DESCR"))==false) {
            STR = STR + "COMMENT ON TABLE "+V.SXEMA+"." + A.GETVALS("UD.NAME") + " IS '" +  A.GETVALS("UD.DESCR") + "';"+"\n";
            }
            }
             FF._CLIPTEXT(STR);
            if ( P.MESSYESNO("Скрипт занесен в буфер обмена (Ctrl+V вставить)\n"+"Зписать в файл ?")==0  ) {
                FF.STRTOFILE(STR, "");
            }
                     
        }     
        if (n==13){//комментарии для полей в файл
            String SELE = "SELECT * FROM BASIC_COLUMN ORDER BY NAMEO";
            P.SQLEXECT(SELE, "UD");
            String STR="";
            for (int i = 1; i <= A.RECCOUNT("UD"); i++) {
            A.GOTO("UD",i);
            if (FF.EMPTY(A.GETVALS("UD.DESCR"))==false) {
            STR = STR + "COMMENT ON COLUMN "+V.SXEMA+"."+A.GETVALS("UD.NAMEO")+"."+A.GETVALS("UD.NAME")+" IS '"+A.GETVALS("UD.DESCR")+"';"+"\n";
            }
            }
             FF._CLIPTEXT(STR);
//             P.MESS("Скрипт занесен в буфер обмена (Ctrl+V вставить)");
            if ( P.MESSYESNO("Скрипт занесен в буфер обмена (Ctrl+V вставить)\n"+"Зписать в файл ?")==0  ) {
                FF.STRTOFILE(STR, "");
            }
                     
        }     
        
         
    }
}
    
  @Override    
  public void OPEN(){
  G[0].SETFOCUS();
  }

    @Override
    public boolean ISEDIT(Gridr GRID, int col) {//Разрешение на корректуру колонки
        if (col<=4)
        {return true;}  // разрешить
        else {
     return super.ISEDIT(GRID, col);        
    }
    }
      @Override
     public void CHANGERECNO(Gridr GRID,int row,int rec)   {
               F[0].SETVALUE(A.GETVALS("STRUCSQL.DESCR"));  
//                        F[0].SETVALUE(A.GETVAL("STRUCSQL.BIT_PR"));  
     }

    @Override
    public void PRINT() {
            Reportr.INIT(" SELECT NAMEO,NAME,TYPE,DEC,LEN,DESCR FROM BASIC_COLUMN WHERE NAMEO="+A.GETVALSQL("TABLESQL.NAME")  ); //инициализация параметров
            V.PRINTMAP.put("ReportTitle", V.PARAMTO[0].toString()); //добавление параметров
            Reportr.RUN("struct"); //запуск отчета
        }
    

    
             
   





}



