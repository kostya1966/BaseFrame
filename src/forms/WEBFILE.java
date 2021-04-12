package forms;
import baseclass.Formr;
import baseclass.Gridr;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;
    
public class WEBFILE extends Formr {
 private final String AL1="TEMP_WEBFILE";
// private final String TABLE="FIRM.TEMP_WEBFILE";
 private final String TABLE="TEMP_WEBFILE";
 //private final String AUTH="Basic "+P.encodeBase64("dataapp:Aif8ahneichu"); 
 private final String AUTH="Basic ZGF0YWFwcDpBaWY4YWhuZWljaHU="; 
 private final String WEB_LOAD_ALL="https://datarep.vit.belwest.com/file-manager/"; //ЗАГРУЗКА СПИСКА ВСЕХ ФАЙЛОВ
 private final String WEB_DELETE="https://datarep.vit.belwest.com/file-manager"; //УДАЛЕНИЕ ФАЙЛА "https://datarep.vit.belwest.com/file-manager/RKV/TEST8.TXT"
 private final String WEB_INSERT="https://datarep.vit.belwest.com/file-manager/file"; //ЗАПИСЬ ФАЙЛА НА WEB СКРВЕР
// private final String WEB_LOAD_FILE="https://dataapp:Aif8ahneichu@datarep.vit.belwest.com/file-manager-data"; //ЗАГРУЗКА  ФАЙЛА https://datarep.vit.belwest.com/file-manager-data/RKV/TEST7.TXT
 private final String WEB_LOAD_FILE="https://datarep.vit.belwest.com/file-manager-data"; //ЗАГРУЗКА  ФАЙЛА https://datarep.vit.belwest.com/file-manager-data/RKV/TEST7.TXT
  String[] file; //массив файлов  сервера
public WEBFILE() {  
    super("WEBFILE","Менеджер файловой системы WEB сервера",1100,500); 
}
    
    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
    //P.WRITE_INFO("Сообщение в инфопанели");        
        B[32] = P.addobjB(this, "B32", "Выбрать текущее значение", "Занести значение текущей записи в поле ввода"); //добавляем кнопку выбора текущей записи и закрытия формы
        B[32].setVisible(SPR); // если не справочник-выбора занчит не из поля ввода , значит невидимая
     B[0] =P.addobjB(this, "B0", "Список ", "Обновить спписок файлов web сервера"); 
     B[1] =P.addobjB(this, "B1", "Удалить", "Удадление текущего файла из web сервера"); 
     B[2] =P.addobjB(this, "B2", "Записать", "Записать выбранный файл на web сервер"); 
     B[3] =P.addobjB(this, "B3", "Выгрузить", "Выгрузить текущий файл из web сервера"); 
     
     G[0] =P.addobjG(this,AL1,1,1,880, 500); 
    }
    
   @Override
    public  void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
     GRID.VIEWRECNO=0; //не показывать номер записи в первой колонке -0
     GRID.RESTORECOL=1;//не восстанавливать размеры -0 
     GRID.ALIAS = GRID.getName();     //ИМЯ ГРИДА И АЛИАС СОВПАДАЮТ 
   //  GRID.FREAD="RI_MAC_K";//форма корректуры
     GRID.ENABLE_DEPLOY = false;  //вызов другой формы спецификации
    }
   @Override
    public  void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
         String C[]={"Файл","Подкаталоги","Имя","Время получения данных","ПК получения данных"};
         String F[]={"FILEFULL","FILEPATH","FILENAME","DATE_S","PC_S"};
         int S[]={486,226,217,135,300};
     V.CAPTION=C;
     V.FIELD=F;    
     V.FSIZE=S;
    }

    @Override
    public boolean QUERY(Gridr GRID) {
        return super.QUERY(GRID); //To change body of generated methods, choose Tools | Templates.
    }

    
   @Override
    public  String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
        
    V.SELE="SELECT * FROM "+TABLE+" ORDER BY FILEFULL";
    return V.SELE;
  }
    
   @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        V.SELE = "SELECT *  FROM "+TABLE+" WHERE FILEFULL=" +A.GETVALSQL(AL1+".FILEFULL");// для вызова при редактировании
        return V.SELE;
    }
    
    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга
      if (G[0].SCROLL!=null) {
            G[0].SCROLL.setBounds(1, 1, this.getWidth()-20, this.getHeight()-80);
        }        
     locate(B[0],null,V.LOC_LEFT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(B[1],B[0],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(B[2],B[1],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(B[3],B[2],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  
    }
    
  @Override    
  public void OPEN(){
  G[0].SETFOCUS();
  }
    
    @Override
    public boolean ISEDIT(Gridr GRID, int col) {//Разрешение на корректуру колонки
             if (GRID.FIELD[col-1].contains("BIT_")   ) {
                return false;
            }
        return super.ISEDIT(GRID, col); //по умолчанию запроещено false
    }
      @Override
     public void CHANGERECNO(Gridr GRID,int row,int rec)   {//выполняется при изменении записи в гриде
     }
    
   @Override
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
             String file="000",filepath="",filename="" ;
        
         if ("B0".equals(name)) {//список файлов
             P.ALERT("Выполнение запроса и записи данных...");
             String out=P.WEB_GET(WEB_LOAD_ALL, "",AUTH);
             if (out.equals("@ERR"))  { 
                 return ; 
             }
            V.SELE = "" ;
            V.SELE=V.SELE+"BEGIN "+"\n";
            V.SELE=V.SELE+"DELETE FROM "+TABLE+"  ;"+"\n";
            V.SELE=V.SELE+"END; ";
            if (P.SQLEXECUT(V.SELE)!=true){
                return ;
            }

            while (true) {
             file = FF.SUBSTR_BETWEEN(out, "\"", "\"") ; //ВЫДЕЛЯЕМ ИМЯ ФАЙЛА "/358240051111110_1_128.jpg"
             if (FF.EMPTY(file)) { //ЕСЛИ СППИСОК ЗАКОНЧЕН
                 break;
             }
             out = FF.SUBSTR_DEL(out, "\"", "\""); // удаляем ИЗ ОБЩЕЙ СТРОКИ ФАЙЛОВ ТЕККУЩИЙ
             filepath=FF.SUBSTR_BETWEEN(file, "/", "/");
             filename=FF.SUBSTR_BETWEEN(file, "/",null);
             
             V.SELE="INSERT INTO  "+TABLE+" (FILEFULL,FILEPATH,FILENAME) VALUES ("+P.P_SQL(file)+","+P.P_SQL(filepath)+","+P.P_SQL(filename)+")"+"\n";
            if (P.SQLEXECUT(V.SELE)!=true){ return ;}
             }  
             P.ALERT("");
            QUERY(G[0]);             
            G[0].SETFOCUS();            
           }
         
        if ("B1".equals(name)) {//удаление
            /////////////////////
             filepath=A.GETVALS(AL1+".FILEPATH");
                 try {
                     filename=URLEncoder.encode(A.GETVALS(AL1+".FILENAME"), "utf-8");
                 } catch (UnsupportedEncodingException ex) {
                     P.MESSERR("Ршибка кодировки "+filename+"/n"+ex.toString());
                     return;
                 }
             //String url=WEB_DELETE+"/"+filepath+"/"+filename;
             ///////////////////////////////////
                 
             String url=WEB_DELETE+A.GETVALS(AL1+".FILEFULL");
             String out=P.WEB_FILE("DELETE", url,null,AUTH,"","");        
            if (out.equals("@ERR"))  { 
                 return ; 
             }
            P.MESS(out);
            G[0].SETFOCUS();            
        }
        if ("B2".equals(name)) {//записать
                   String  FILE_ST= P.GETFILE("Выберите файл","","",V.CURRENT_PATH);
             if ("".equals(FILE_ST))      {         return;             }
             File FILE = new File(FILE_ST);
             V.CURRENT_PATH=FILE.getPath();
             filepath=A.GETVALS(AL1+".FILEPATH");
             filename=FILE.getName();
            P.DOCORRECT("Введите ",new Object[]{"Каталог:",V.TYPE_CHAR,"",filepath,"Имя:",V.TYPE_CHAR,"",filename});
            if (V.PARAMOT == null || V.PARAMOT[0].equals("F")) {
                return;
                        };
             filepath=V.PARAMOT[1] ;
             filename=V.PARAMOT[2] ;
             String out=P.WEB_FILE("POST", WEB_INSERT, FILE,AUTH,filepath,filename);
            P.MESS(out);
            G[0].SETFOCUS();            
             
             
        }
        if ("B3".equals(name)) {//ВЫГРУЗИТЬ
             filename= P.GETFILE("Выберите файл",A.GETVALS(AL1+".FILEFULL"),"",V.CURRENT_PATH);
             if ("".equals(filename))      {         return;             }
             String nurl=WEB_LOAD_FILE+A.GETVALS(AL1+".FILEFULL");
                     P.WEB_GET_FILE(nurl,filename,AUTH);
                     
        }
        
        
        
    }
    
    
    
     }//окнчание класса
