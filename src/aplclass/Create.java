package aplclass;

import java.util.ArrayList;
import prg.A;
import prg.FF;
import prg.P;

public class Create {

    public static String STR;

    public static void FORM(String table, String name) {
        STR = "";
        STR = STR + "package forms;" + "\n";
        STR = STR + "import baseclass.Formr;" + "\n";
        STR = STR + "import baseclass.Gridr;" + "\n";
//STR=STR+"import prg.A;"+"\n";
//STR=STR+"import prg.FF;"+"\n";
        STR = STR + "import prg.A;" + "\n";
        STR = STR + "import prg.P;" + "\n";
        STR = STR + "import prg.V;" + "\n";
        STR = STR + "    " + "\n";


        STR = STR + "public class " + table + " extends Formr {" + "\n";
        STR = STR + " private final String AL1=\"" + table + "\";" + "\n";
        STR = STR + "public " + table + "() {  " + "\n";
        STR = STR + "    super(\"" + table + "\",\"" + name + " \",1000,500); " + "\n";
        STR = STR + "}" + "\n";
        STR = STR + "    " + "\n";

//LOAD_OBJ()
        STR = STR + "    @Override" + "\n";
        STR = STR + "    public void LOAD_OBJ() { //вызывается из конструктора" + "\n";
        STR = STR + "    //P.WRITE_INFO(\"Сообщение в инфопанели\");        " + "\n";
        STR = STR + "        B[32] = P.addobjB(this, \"B32\", \"Выбрать текущее значение\", \"Занести значение текущей записи в поле ввода\"); //добавляем кнопку выбора текущей записи и закрытия формы" + "\n";
        STR = STR + "        B[32].setVisible(SPR); // если не справочник-выбора занчит не из поля ввода , значит невидимая" + "\n";

        STR = STR + "     B[0] =P.addobjB(this, \"B0\", \"Кнопка 1\", \"Описание работы кнопки 1\"); " + "\n";
        STR = STR + "     B[1] =P.addobjB(this, \"B1\", \"Кнопка 2\", \"Описание работы кнопки 2\"); " + "\n";
        STR = STR + "     G[0] =P.addobjG(this,\"" + table.toUpperCase() + "\",1,1,880, 500); " + "\n";
        STR = STR + "     B[0].setVisible(false); // при создании кода программно " + "\n";
        STR = STR + "     B[1].setVisible(false); // при создании кода программно" + "\n";

        STR = STR + "    }" + "\n";
        STR = STR + "    " + "\n";
//TABLEPROP(Gridr GRID)
        STR = STR + "   @Override" + "\n";
        STR = STR + "    public  void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG" + "\n";
        STR = STR + "     GRID.VIEWRECNO=0; //не показывать номер записи в первой колонке -0" + "\n";
        STR = STR + "     GRID.RESTORECOL=1;//не восстанавливать размеры -0 " + "\n";
        STR = STR + "     GRID.ALIAS = GRID.getName();     //ИМЯ ГРИДА И АЛИАС СОВПАДАЮТ " + "\n";
        STR = STR + "//   GRID.FREAD=\"" + table + "_K\";//форма корректуры" + "\n";
        STR = STR + "     GRID.ENABLE_DEPLOY = false;  //вызов другой формы спецификации" + "\n";
        
        STR = STR + "    }" + "\n";
//PREV_INITCOLUMN(Gridr GRID)
        STR=STR+FORM_PREV_INITCOLUMN(table, name);
//PREV_QUERY(Gridr GRID)
        STR = STR + "   @Override" + "\n";
        STR = STR + "    public  String PREV_QUERY(Gridr GRID) {//Формирование строки запроса" + "\n";
        STR = STR + "    String SELE=\"SELECT *  FROM " + table + "\";" + "\n";
        STR = STR + "    return SELE;" + "\n";
        STR = STR + "  }" + "\n";
        STR = STR + "    " + "\n";
//PREV_QUERY_REC(Gridr GRID)
        STR = STR + "   @Override" + "\n";
        STR = STR + "    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи" + "\n";
        STR = STR + "        V.SELE = \"SELECT *  FROM " + table + " WHERE ПОЛЕ-КЛЮЧ\" +A.GETVALSQL(AL1+\".ПОЛЕ-КЛЮЧ\");// для вызова при редактировании" + "\n";
        STR = STR + "        return V.SELE;" + "\n";
        STR = STR + "    }" + "\n";
        STR = STR + "    " + "\n";

//LOC_ABOUT()        
        STR = STR + "    @Override" + "\n";
        STR = STR + "    public void LOC_ABOUT() {//Расположение объектов относительно друг друга" + "\n";
        STR = STR + "      if (G[0].SCROLL!=null) {" + "\n";
        STR = STR + "            G[0].SCROLL.setBounds(1, 1, this.getWidth()-20, this.getHeight()-80);" + "\n";
        STR = STR + "        }        " + "\n";
        STR = STR + "     locate(B[0],null,V.LOC_LEFT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  " + "\n";
        STR = STR + "     locate(B[1],B[0],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  " + "\n";
        STR = STR + "     locate(B[32],B[1],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  " + "\n";
        
        STR = STR + "    }" + "\n";
        STR = STR + "    " + "\n";
//OPEN()
        STR = STR + "  @Override    " + "\n";
        STR = STR + "  public void OPEN(){" + "\n";
        STR = STR + "  G[0].SETFOCUS();" + "\n";
        STR = STR + "  }" + "\n";
        STR = STR + "    " + "\n";
//ISEDIT(Gridr GRID, int col)
        STR = STR + "    @Override" + "\n";
        STR = STR + "    public boolean ISEDIT(Gridr GRID, int col) {//Разрешение на корректуру колонки" + "\n";
        STR = STR + "             if (GRID.FIELD[col-1].contains(\"BIT_\")   ) {" + "\n";
        STR = STR + "                return false;" + "\n";
        STR = STR + "            }" + "\n";
        
        STR = STR + "        return super.ISEDIT(GRID, col); //по умолчанию запроещено false" + "\n";
        STR = STR + "    }" + "\n";
//CHANGERECNO(Gridr GRID,int row,int rec) 
        STR = STR + "      @Override" + "\n";
        STR = STR + "     public void CHANGERECNO(Gridr GRID,int row,int rec)   {//выполняется при изменении записи в гриде" + "\n";
        STR = STR + "     }" + "\n";
        STR = STR + "    " + "\n";
//CLICK_ALL(String name)
        STR = STR + "   @Override" + "\n";
        STR = STR + "    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы" + "\n";
        STR = STR + "         if (\"B0\".equals(name)) {//Для кнопки BO" + "\n";
        STR = STR + "           }" + "\n";
        STR = STR + "        if (\"B32\".equals(name)) {//Для кнопки B32 при вызове как справочник для выбора значений" + "\n";
        STR = STR + "            V.PARAMOT = new String[]{\"OK\"};" + "\n";
        STR = STR + "            THISFORM.DESTROY();" + "\n";
        STR = STR + "        }" + "\n";
        
        STR = STR + "    }" + "\n";
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";
//DEPLOY(String name)
        STR = STR + "    @Override" + "\n";
        STR = STR + "    public void DEPLOY(String name) { " + "\n";
        STR = STR + "        String NAMEF=\"" + table + "\"; " + "\n";
        STR = STR + "        P.CLOSEFORM(NAMEF); " + "\n";
        STR = STR + "        A.CLOSE(NAMEF); " + "\n";
        STR = STR + "        Formr form=P.DOFORM(NAMEF); " + "\n";
        STR = STR + "        form.PARENTFORM=this; " + "\n";
        STR = STR + "    }    // для дочерних объектов на РАЗВЕРНУТЬ ИЗ ТУЛБАРА ДЛЯ ГРИДА " + "\n";

/*        
        STR = STR + "    //НЕОБЯЗАТЕЛЬНЫЙ КОД - ДЛЯ ПРИМЕРА ЧТО МОЖНО ПЕРЕКРЫТЬ " + "\n";
        STR = STR + "    public String PREV_KEY(Gridr GRID) {//ПОЛУЧЕНИЕ КЛЮЧЕВОГО ВЫРАЖЕНИЯ В WHERE" + "\n";
        STR = STR + "        return GRID.FORM_K.PREV_KEY(GRID);" + "\n";
        STR = STR + "    }  " + "\n";
        STR = STR + "    public String PREV_UPDATE(Gridr GRID) {// для таблиц получение запроса корректуры" + "\n";
        STR = STR + "          return GRID.FORM_K.PREV_UPDATE(GRID);" + "\n";
        STR = STR + "    }    " + "\n";
        STR = STR + "    public String PREV_INSERT(Gridr GRID) {" + "\n";
        STR = STR + "          return GRID.FORM_K.PREV_INSERT(GRID);" + "\n";
        STR = STR + "    }    // для таблиц получение запроса новой записи" + "\n";
        STR = STR + "    public String PREV_DELETE(Gridr GRID) {" + "\n";
        STR = STR + "          return GRID.FORM_K.PREV_DELETE(GRID);" + "\n";
        STR = STR + "    }    // для таблиц получение запроса удаления записи" + "\n";
*/
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";


        STR = STR + "     }//окнчание класса" + "\n";


        FF._CLIPTEXT(STR);
        P.MESS("Исходный текст класса занесен в буфер обмена (Ctrl+V вставить в файл)");
    }

    
    public static void FORM_K(String table, String name) {
        STR = "";
        STR = STR + "package forms;" + "\n";
        STR = STR + "import baseclass.Formr_k;" + "\n";
        STR = STR + "import baseclass.Formr;" + "\n";
        STR = STR + "import baseclass.Gridr;" + "\n";
        STR = STR + "import prg.FF;" + "\n";
        STR = STR + "import prg.A;" + "\n";
        STR = STR + "import prg.P;" + "\n";
        STR = STR + "import prg.V;" + "\n";

        STR = STR + "import prg.A;" + "\n";
        STR = STR + "import prg.P;" + "\n";
        STR = STR + "import prg.V;" + "\n";
        STR = STR + "public class " + table + "_K extends Formr_k {" + "\n";
        STR = STR + " private final String AL1=\"" + table + "\";" + "\n";
        STR = STR + "// private final String REL=A.GETVALS(AL1+\".REL\"); " + "\n";
        STR = STR + "  public " + table + "_K()  {" + "\n";

        STR = STR + "    super(\"" + table + "_K\",\"Редактирование записи\",500,400); //Вызов конструктора от базового класса " + "\n";
        STR = STR + "}" + "\n";
        STR = STR + "    " + "\n";
// LOAD_OBJ()
        STR = STR + "  @Override" + "\n";
        STR = STR + "  public void LOAD_OBJ(){" + "\n";
        STR = STR + "  //примеры    L[i].SETICON();-картинка книжки  F[i].SETSELECTON(true);-автовыделение текста  F[i].SETSQLUPDATE(false);-не обновляется  " + "\n";
        STR = STR + "  //F[i].SETSPACE_LEFT(false);-без левых пробелов " + "\n";
        STR = STR + "  int i=-1;" + "\n";
        A.SELECT("STRUCSQL");
        int REC = A.RECCOUNT();
        int j = 0;
        for (int i = 1; i <= REC; i++) {
            if ((boolean) A.GETVAL("BIT_ED", i) == false) {
                continue;
            }
            STR = STR + " i++; L[i]=P.addobjL(this,\"L" + A.GETVALS("NAME", i) + "\", \"" + A.GETVALS("DESCR", i) + ":\");" + "\n";
            STR = STR + "      F[i]=P.addobjF(this,\"" + A.GETVALS("NAME", i) + "\", \"\", 150, 30);"  + "\n";
            if ((boolean) A.GETVAL("BIT_KEY", i) == true) {
            STR = STR + "      F[i].SETTHISKEY(true);      //ключевое поле ;" + "\n";
            }
            if ((boolean) A.GETVAL("BIT_READ", i) == true) {
            STR = STR + "      F[i].SETREADONLY(true);      //только чтение ;" + "\n";
            }
            String TYPE=FF.SUBSTR( (String)A.GETVAL("TYPE", i),1,3 )  ;
            if ("NUM".equals(TYPE)) {
            STR = STR + "      F[i].SETTYPE(V.TYPE_NUMERIC);      //числовые данные ;" + "\n";
            STR = STR + "      F[i].SETINPUTMASK(\""+FF.REPLICATE("9", FF.VAL( A.GETVALS("LEN", i))   )+"\");      //маска ввода ;" + "\n";
            }
            if ("VARCH".equals(FF.SUBSTR((String)A.GETVAL("TYPE", i) ,1,4 ) )   ) {
            STR = STR + "      F[i].SETTYPE(V.TYPE_CHAR);      //строковые данные ;" + "\n";
            }
            if ("TIMESTAMP(6)".equals(FF.RTRIM( (String)A.GETVAL("TYPE", i)) )  || "DATETIME".equals(FF.RTRIM( (String)A.GETVAL("TYPE", i)) )  ) {
            STR = STR + "    F[i].SETTYPE(V.TYPE_DATETIME);      //тип датывремя ;" + "\n";
            }
            if ("DATE".equals(FF.RTRIM( (String)A.GETVAL("TYPE", i)) ) ) {
            STR = STR + "    F[i].SETTYPE(V.TYPE_DATE);      //тип даты ;" + "\n";
            }
            
            j++;
        }
        STR = STR + "    " + "\n";

        STR = STR + "if (V.U_I_D>1){//если корректура или удаление" + "\n";
        j = 0;
        String order="ORDERS(";
        for (int i = 1; i <= REC; i++) {
            if ((boolean) A.GETVAL("BIT_ED", i) == false) {
                continue;
            }
            STR = STR + "   GETF(\""+A.GETVALS("NAME", i)+"\").SETVALUE(A.GETVAL(\"" + table.toUpperCase() + "." + A.GETVALS("NAME", i) + "\"));" + "\n";
            order=order+",GETF(\""+A.GETVALS("NAME", i)+"\")";
            j++;
        }
        STR = STR + "}" + "\n";
        STR = STR + "//  " +FF.SUBSTR(order,2)+"); //порядок перехода фокуса между элементами"+ "\n";

        STR = STR + "}" + "\n";
        STR = STR + "    " + "\n";

//LOC_ABOUT()
        STR = STR + "//расположение объектов относительно друг друга выполняется также при изменении размеров формы " + "\n";
        STR = STR + " @Override" + "\n";
        STR = STR + " public void LOC_ABOUT() {" + "\n";
        STR = STR + " LOCOBJR=null; LOCOBJD=null; int xy=10;" + "\n";
        j = 0; int l=1;
        for (int i = 1; i <= REC; i++) {
            if ((boolean) A.GETVAL("BIT_ED", i) == false) {
                continue;
            }
            j++;
            if (j==1) {
             STR = STR + " locd(GETL(\"L"+A.GETVALS("NAME", i)+"\"),xy); " ;
             STR = STR + " locr(GETF(\""+A.GETVALS("NAME", i)+"\"),0) ;          " + "\n";
            } else {
            
            STR = STR + " locd(GETL(\"L"+A.GETVALS("NAME", i)+"\"),xy) ; " ;
            STR = STR + " locr(GETF(\""+A.GETVALS("NAME", i)+"\"),0) ;        " + "\n";
            }
            l=i;
        }
        STR = STR + "}" + "\n";
        STR = STR + "    " + "\n";
//OPEN()        
        STR = STR + "  @Override    " + "\n";
        STR = STR + "  public void OPEN(){" + "\n";
        STR = STR + "   F[0].requestFocus(); //фокус по умолчанию" + "\n";
        STR = STR + "  }" + "\n";
        STR = STR + "    " + "\n";
//CLICK_ALL(String name)
        STR = STR + "  @Override" + "\n";
        STR = STR + "  public void CLICK_ALL(String name) {" + "\n";
        STR = STR + " //     if (\"bok\".equals(name) && V.U_I_D==1) { //выполнить до сохранения если ввод записи " + "\n";
        STR = STR + " //      int REL=A.GETMAXREL(AL1)  ;// НАХОДИМ СЛЕДУЮЩИЙ НОМЕР ОТ МАКСИМАЛЬНОГО          " + "\n";
        STR = STR + " //      if (REL<0) {return;} //ЕСЛИ ОШИБКА ВЫПОЛНЕНИЯ " + "\n";
        STR = STR + " //      GETF(\"REL\").SETVALUE(REL); " + "\n";
        STR = STR + " //     }" + "\n";
        
        STR = STR + "      super.CLICK_ALL(name);" + "\n";
        STR = STR + "          }   " + "\n";

        STR = STR + "           @Override" + "\n";
        STR = STR + "    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи" + "\n";
        STR = STR + "        V.SELE = \"SELECT *  FROM \"+AL1+\" WHERE ID=\" + GETF(\"ID\").getText();// для обновления записи" + "\n";
        STR = STR + "        return V.SELE;" + "\n";
        STR = STR + "    }" + "\n";
    
        STR = STR + "    @Override" + "\n";
        STR = STR + "    public void DBLCLICK_ALL(String name) {" + "\n";
        STR = STR + "      //  VM.DBLCLICK_ALL(THISFORM,name);" + "\n";
        STR = STR + "        }" + "\n";
    
        STR = STR + "    @Override" + "\n";
        STR = STR + "    public boolean VALID_ALL(String name) {" + "\n";
        STR = STR + "       // return VM.VALID_ALL(THISFORM, name);" + "\n";
        STR = STR + "    }" + "\n";

        
/*        
        STR = STR + "    //НЕОБЯЗАТЕЛЬНЫЙ КОД - ДЛЯ ПРИМЕРА ЧТО МОЖНО ПЕРЕКРЫТЬ " + "\n";
        STR = STR + "    public String PREV_KEY(Gridr GRID) {//ПОЛУЧЕНИЕ КЛЮЧЕВОГО ВЫРАЖЕНИЯ В WHERE" + "\n";
        STR = STR + "        return super.PREV_KEY(GRID);" + "\n";
        STR = STR + "    }  " + "\n";
        STR = STR + "    public String PREV_UPDATE(Gridr GRID) {// для таблиц получение запроса корректуры" + "\n";
        STR = STR + "          return super.PREV_UPDATE(GRID);" + "\n";
        STR = STR + "    }    " + "\n";
        STR = STR + "    public String PREV_INSERT(Gridr GRID) {" + "\n";
        STR = STR + "          return super.PREV_INSERT(GRID);" + "\n";
        STR = STR + "    }    // для таблиц получение запроса новой записи" + "\n";
        STR = STR + "    public String PREV_DELETE(Gridr GRID) {" + "\n";
        STR = STR + "          return super.PREV_DELETE(GRID);" + "\n";
        STR = STR + "    }    // для таблиц получение запроса удаления записи" + "\n";
*/
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";

        STR = STR + "     }//окнчание класса" + "\n";
        STR = STR + "    " + "\n";


        FF._CLIPTEXT(STR);
        P.MESS("Исходный текст класса формы редактора занесен в буфер обмена (Ctrl+V вставить в файл)");


    }

    public static String FORM_PREV_KEY(String table, String name) {
        STR = "   @Override \n";
        STR = STR + " public String PREV_KEY(Gridr GRID) {//ПОЛУЧЕНИЕ КЛЮЧЕВОГО ВЫРАЖЕНИЯ В WHERE" + "\n";
        A.SELECT("STRUCSQL");
        int REC = A.RECCOUNT();
        int j = 0;
        STR = STR + "  String WHERE = \"\"; " + "\n";
        for (int i = 1; i <= REC; i++) {
            if ((boolean) A.GETVAL("BIT_KEY", i) == true) {
                STR = STR + "    WHERE = WHERE +\" AND " + A.GETVALS("NAME", i) + " =\" + A.GETVALSQL(\"" + table.toUpperCase() + "." + A.GETVALS("NAME", i) + "\", GRID.RECNO); " + "\n";
            }

            j++;
        }
        STR = STR + "        return FF.SUBSTR(WHERE, 5);" + "\n";

        STR = STR + "    " + "\n";

        STR = STR + "    }  " + "\n";
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";
        return STR;
    }
/////////////////////////////

//PREV_INITCOLUMN(Gridr GRID)
    public static String FORM_PREV_INITCOLUMN(String table, String name) {
    
        A.SELECT("STRUCSQL");
        String STR1 = "String C[]={";
        String STR2 = "String F[]={";
        String STR3 = "int    S[]={";

        for (int i = 1; i <= A.RECCOUNT("STRUCSQL"); i++) {
            A.GOTO(i);
            if ((boolean) A.GETVAL("STRUCSQL.BIT_PR")) {
                STR1 = STR1 + "\"" + A.GETVALS("STRUCSQL.DESCR") + "\",";
                STR2 = STR2 + "\"" + A.GETVALS("STRUCSQL.NAME") + "\",";
                STR3 = STR3 + "100,";

            }
        }
        STR1 = FF.SUBSTR(STR1, 1, FF.LEN(STR1) - 1);
        STR1 = STR1 + "};\n";
        STR2 = FF.SUBSTR(STR2, 1, FF.LEN(STR2) - 1);
        STR2 = STR2 + "};\n";
        STR3 = FF.SUBSTR(STR3, 1, FF.LEN(STR3) - 1);
        STR3 = STR3 + "};\n";
        STR =  "   @Override" + "\n";
        STR = STR + "    public  void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы" + "\n";
        STR = STR + STR1;
        STR = STR + STR2;
        STR = STR + STR3;

        STR = STR + "     V.CAPTION=C;" + "\n";
        STR = STR + "     V.FIELD=F;    " + "\n";
        STR = STR + "     V.FSIZE=S;" + "\n";
        STR = STR + "    }" + "\n";
        STR = STR + "    " + "\n";
        return STR;
    }
    
    public static String FORM_PREV_UPDATE(String table, String name) {
        STR = "   @Override \n";
        STR = STR + " public String PREV_UPDATE(Gridr GRID) {// для таблиц получение запроса корректуры" + "\n";
        A.SELECT("STRUCSQL");
        int REC = A.RECCOUNT();
        int j = 0;
        STR = STR + "  Formr OBJ = GRID.FORM_K;" + "\n";
        STR = STR + "  V.SELE = \"\"; " + "\n";

        for (int i = 1; i <= REC; i++) {
            if ((boolean) A.GETVAL("BIT_ED", i) == true) {
                STR = STR + "    V.SELE = V.SELE +\"," + A.GETVALS("NAME", i) + " =\" + P.P_SQL(OBJ.GETF(\"" + A.GETVALS("NAME", i) + "\").GETVALUE()); " + "\n";
            }

            j++;
        }
        STR = STR + "  V.SELE = \"UPDATE  " + table.toUpperCase() + "  SET \" + FF.SUBSTR(V.SELE, 2);" + "\n";
        STR = STR + "  V.SELE = V.SELE + \" WHERE \" + PREV_KEY( GRID);" + "\n";
        STR = STR + "    return V.SELE;" + "\n";
        STR = STR + "    }  " + "\n";
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";
        return STR;
    }

/////////////////////////////
    public static String FORM_PREV_INSERT(String table, String name) {
        STR = "   @Override \n";
        STR = STR + " public String PREV_INSERT(Gridr GRID) {// для таблиц получение запроса корректуры" + "\n";
        A.SELECT("STRUCSQL");
        int REC = A.RECCOUNT();
        int j = 0;
        STR = STR + "  Formr OBJ = GRID.FORM_K;" + "\n";
        STR = STR + "  V.SELE = \"\"; " + "\n";
        STR = STR + "  String POLE = \"\";" + "\n";
        STR = STR + "  String VALUES = \"\";" + "\n";

        for (int i = 1; i <= REC; i++) {
            if ((boolean) A.GETVAL("BIT_ED", i) == true) {
                STR = STR + " POLE = POLE +\"," + A.GETVALS("NAME", i) + "\";" + "\n";
                STR = STR + " VALUES = VALUES +\",\"  + P.P_SQL(OBJ.GETF(\"" + A.GETVALS("NAME", i) + "\").GETVALUE()); " + "\n";

            }

            j++;
        }
        STR = STR + "        V.SELE = \"INSERT INTO  " + table.toUpperCase() + " (\" + FF.SUBSTR(POLE, 2) + \")\" + \" VALUES (\" + FF.SUBSTR(VALUES, 2) + \")\";" + "\n";
        STR = STR + "    return V.SELE;" + "\n";
        STR = STR + "    }  " + "\n";
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";
        return STR;
    }

    /////////////////////////////
    public static String FORM_PREV_DELETE(String table, String name) {
        STR = "   @Override \n";
        STR = STR + " public String PREV_DELETE(Gridr GRID) {// для таблиц получение запроса корректуры" + "\n";
        STR = STR + "  V.SELE = \"\"; " + "\n";

        STR = STR + "  V.SELE = \"DELETE FROM  " + table.toUpperCase() + " \";" + "\n";
        STR = STR + "  V.SELE = V.SELE + \" WHERE \" + PREV_KEY( GRID);" + "\n";
        STR = STR + "    return V.SELE;" + "\n";
        STR = STR + "    }  " + "\n";
        STR = STR + "    " + "\n";
        STR = STR + "    " + "\n";
        return STR;
    }

    public static void FORM_PREV(String table, String name, int NOMER) {
        String STR = "";
        if (NOMER == 0) {
            STR = STR + FORM_PREV_KEY(table, name);
            STR = STR + FORM_PREV_UPDATE(table, name);
            STR = STR + FORM_PREV_INSERT(table, name);
            STR = STR + FORM_PREV_DELETE(table, name);
        }

        if (NOMER == 1) {
            STR = FORM_PREV_KEY(table, name);
        }
        if (NOMER == 2) {
            STR = FORM_PREV_UPDATE(table, name);
        }
        if (NOMER == 3) {
            STR = FORM_PREV_INSERT(table, name);
        }
        if (NOMER == 4) {
            STR = FORM_PREV_DELETE(table, name);
        }

        FF._CLIPTEXT(STR);
        P.MESS("Исходный текст методов PREV_* занесен в буфер обмена (Ctrl+V вставить в файл)");

    }

    public static String TRIGGER(String parentTable, String parentField, ArrayList childTable, ArrayList childField, String type, String vid, ArrayList<ArrayList> rel) {
        if (type.equals("Удаление")) {
            STR = "create or replace TRIGGER DELETE_" + parentTable +" --"+FF.DATES()+ "\n";
            STR = STR + "BEFORE DELETE ON " + parentTable + " for each row" + "\n";
        } else if (type.equals("Изменение")) {
            STR = "create or replace TRIGGER UPDATE_" + parentTable + "\n";
            STR = STR + "BEFORE UPDATE ON " + parentTable + " for each row" + "\n";
        }
        STR = STR + "DECLARE" + "\n";
        for (int i = 0; i < childTable.size(); i++) {
            STR = STR + "col_" + (i + 1) + " NUMBER;\n";
        }
        STR = STR + "BEGIN" + "\n";
        for (int i = 0; i < childTable.size(); i++) {
            STR = STR + "SELECT COUNT(*) INTO col_" + (i + 1) + " FROM " + childTable.get(i).toString() + " WHERE :old." + parentField + " = " + childTable.get(i).toString() + "." + childField.get(i).toString() + ";" + "\n";
        }
        if (vid.equals("Запретить")) {
            STR = STR + "if ";
            for (int i = 0; i < childTable.size(); i++) {
                STR = STR + "col_" + (i + 1) + ">0 or ";
            }
            STR = STR.substring(0, STR.length() - 4);
            STR = STR + " then" + "\n";
            STR = STR + "begin" + "\n";
            if (type.equals("Удаление")) {
                STR = STR + "raise_application_error(-20000,'Нельзя удалить запись . Содержит дочерние данные (не пустой документ)";
            } else if (type.equals("Изменение")) {
                STR = STR + "raise_application_error(-20000,'No UPDATE operation allowed on this table because there is an entry ";
            }
            for (int i = 0; i < childTable.size(); i++) {
                STR = STR + " в таблице " + childTable.get(i).toString() + " или ";
            }
            STR = STR.substring(0, STR.length() - 4);
            STR = STR + "!');" + "\n";;
            STR = STR + "rollback;" + "\n";
            STR = STR + "end;" + "\n";
            STR = STR + "end if;" + "\n";
        } else if (vid.equals("Разрешить")) {
            if (type.equals("Удаление")) {
                for (int i = 0; i < childTable.size(); i++) {
                    STR = STR + "if col_" + (i + 1) + ">0 then" + "\n";
                    STR = STR + "begin" + "\n";
                    STR = STR + "delete from " + childTable.get(i).toString() + " where " + childField.get(i).toString() + " = :old." + parentField + ";" + "\n";
                    STR = STR + "end;" + "\n";
                    STR = STR + "end if;" + "\n";
                }
            } else if (type.equals("Изменение")) {
                for (int i = 0; i < childTable.size(); i++) {
                    int count = 0;
                    for (int j = 0; j < rel.size(); j++) {
                        if (rel.get(j).get(i + 1).toString().equals("")) {
                            count++;
                        }
                    }
                    if (count != rel.size()) {
                        STR = STR + "if col_" + (i + 1) + ">0 then" + "\n";
                        STR = STR + "begin" + "\n";
                        STR = STR + "update " + childTable.get(i).toString() + " set ";
                        for (int j = 0; j < rel.size(); j++) {
                            if (!rel.get(j).get(i + 1).toString().equals("")) {
                                STR = STR + childTable.get(i).toString() + "." + rel.get(j).get(i + 1).toString() + " = " + ":new." + rel.get(j).get(0).toString() + ", ";
                            }
                        }
                        STR = STR.substring(0, STR.length() - 2);
                        STR = STR + " where " + childField.get(i).toString() + " = :old." + parentField + ";" + "\n";
                        STR = STR + "end;" + "\n";
                        STR = STR + "end if;" + "\n";
                    }
                }
            }
        }
        STR = STR + "END;";
        return STR;
    }
    
      public static String VIEW() {
       P.SQLEXECT("SELECT * FROM  RI_VIEW_SCRIPT", "UD");
        A.SELECT("UD");
        int REC = A.RECCOUNT();
        STR="";
        for (int i = 1; i <= REC; i++) {
         STR=STR+"CREATE OR REPLACE FORCE VIEW "+A.GETVALS("NAME", i)+"  AS \n";
         STR=STR+A.GETVALS("SELE", i)+";\n";
         STR=STR+"---------------------------------------\n";
        }
        A.CLOSE("UD");
        return STR;  
    }

    public static String TR_SCRIPT_COPY(String table, String name) {
        STR = "  DROP TABLE "+table+"_COPY; \n";
        STR = STR + " CREATE TABLE "+table+"_COPY AS  \n";
        STR = STR + " SELECT * FROM  "+table+" WHERE 1=2;  \n";
        STR = STR + "    " + "\n";
        STR = STR + " ALTER TABLE  "+table+"_COPY  \n";
        STR = STR + " ADD (VID999 NUMERIC(1,0) );  \n";
        STR = STR + "    " + "\n";
        STR = STR + " ALTER TABLE  "+table+"_COPY  \n";
        STR = STR + " ADD (DATE_INS999 TIMESTAMP DEFAULT systimestamp );  \n";
        STR = STR + "    " + "\n";
        STR = STR + " ALTER TABLE  "+table+"_COPY  \n";
        STR = STR + " ADD (SESSIONID999 VARCHAR2(99 CHAR) ); \n";
        STR = STR + "    " + "\n";
        STR = STR + " ALTER TABLE  "+table+"_COPY  \n";
        STR = STR + " ADD (IP999 VARCHAR2(33 CHAR) );  \n";
        STR = STR + "    " + "\n";
        STR = STR + " ALTER TABLE  "+table+"_COPY  \n";
        STR = STR + " ADD (HOST999 VARCHAR2(150 CHAR) ); \n";
        STR = STR + "    " + "\n";
        STR = STR + " ALTER TABLE  "+table+"_COPY  \n";
        STR = STR + " ADD (OS_USER999 VARCHAR2(150 CHAR) );  \n";
        STR = STR + "    " + "\n";

       STR = STR + " ------СОЗДАНИЕ ТРИГГЕРА  \n";
       STR = STR + " CREATE OR REPLACE TRIGGER "+table+"_COPY --"+FF.DATES()+"  \n";
       STR = STR + " AFTER DELETE OR UPDATE ON "+table+"  FOR EACH ROW \n";
       STR = STR + " DECLARE \n";
       STR = STR + " V_VID NUMBER(1,0); \n";
       STR = STR + " V_DATE_INS TIMESTAMP; \n";
       STR = STR + " BEGIN \n";
       STR = STR + " IF UPDATING THEN  \n";
       STR = STR + " V_VID:=1; \n";
       STR = STR + " END IF; \n";
       STR = STR + " IF DELETING THEN  \n";
       STR = STR + " V_VID:=3; \n";
       STR = STR + " END IF; \n";
       STR = STR + " V_DATE_INS:=SYSTIMESTAMP; \n";

       STR = STR + " INSERT INTO "+table+"_COPY (VID999,DATE_INS999,SESSIONID999,IP999,HOST999,OS_USER999";
                          P.SQLEXECT("SELECT * FROM BASIC_COLUMN A WHERE A.NAMEO='"+table+"'", "UD_STRUC");
        A.SELECT("UD_STRUC");
        int REC = A.RECCOUNT();
        for (int i = 1; i <= REC; i++) {
                STR = STR + ","+ A.GETVALS("NAME", i) ;
        }
        STR = STR + "  )" + "\n";
        STR = STR + " VALUES (V_VID,V_DATE_INS ,USERENV('sessionid'),RTRIM(sys_context('USERENV', 'IP_address')),sys_context('USERENV', 'host'),sys_context('USERENV', 'os_user') " ;
        for (int i = 1; i <= REC; i++) {
                STR = STR + ",:OLD."+ A.GETVALS("NAME", i) ;
        }
        STR = STR + " ) ; " + "\n";
        STR = STR + "    " + "\n";
        
        STR = STR + " IF UPDATING THEN  " + "\n";
       STR = STR + " INSERT INTO "+table+"_COPY (VID999,DATE_INS999,SESSIONID999,IP999,HOST999,OS_USER999";
        for (int i = 1; i <= REC; i++) {
                STR = STR + ","+ A.GETVALS("NAME", i) ;
        }
        STR = STR + "  )" + "\n";
        STR = STR + " VALUES (V_VID+1,V_DATE_INS ,USERENV('sessionid'),RTRIM(sys_context('USERENV', 'IP_address')),sys_context('USERENV', 'host'),sys_context('USERENV', 'os_user') " ;
        for (int i = 1; i <= REC; i++) {
                STR = STR + ",:NEW."+ A.GETVALS("NAME", i) ;
        }
        STR = STR + " ) ; " + "\n";
        STR = STR + " END IF;    " + "\n";
        STR = STR + "     " + "\n";
        STR = STR + " END ;    " + "\n";
        
        return STR;
    }

      
}
