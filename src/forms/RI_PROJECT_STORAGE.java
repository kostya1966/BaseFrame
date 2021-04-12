package forms;
import baseclass.Formr;
import baseclass.Gridr;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import prg.A;
import prg.P;
import prg.V;
    
public class RI_PROJECT_STORAGE extends Formr {
public RI_PROJECT_STORAGE() {  
    super("RI_PROJECT_STORAGE","Загрузка обновлений ПО ",1100,500); 
}
    
    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
    //P.WRITE_INFO("Сообщение в инфопанели");        
        B[32] = P.addobjB(this, "B32", "Выбрать текущее значение", "Занести значение текущей записи в поле ввода"); //добавляем кнопку выбора текущей записи и закрытия формы
        B[32].setVisible(SPR); // если не справочник-выбора занчит не из поля ввода , значит невидимая
     B[0] =P.addobjB(this, "B0", "Загрузить обновление", "Записать DIST.ZIP в таблицу обновлений для текущей записи"); 
     B[1] =P.addobjB(this, "B1", "Кнопка 2", "Описание работы кнопки 2"); 
     G[0] =P.addobjG(this,"RI_PROJECT_STORAGE",1,1,980, 500); 
//     B[0].setVisible(false); // при создании кода программно 
     B[1].setVisible(false); // при создании кода программно
    }
    
    public  void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
     GRID.VIEWRECNO=0; //не показывать номер записи в первой колонке -0
     GRID.RESTORECOL=1;//не восстанавливать размеры -0 
     GRID.ALIAS = GRID.getName();     //ИМЯ ГРИДА И АЛИАС СОВПАДАЮТ 
     GRID.FREAD="RI_PROJECT_STORAGE_K";//форма корректуры
     GRID.ENABLE_DEPLOY = false;  //вызов другой формы спецификации
     GRID.SETLOCATEFOR("PROGID");     
    }
   @Override
    public  void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
         String C[]={"НОМЕР ОБНОВЛЕНИЯ","ВРЕМЯ ВСТАВКИ","ОПИСАНИЕ ВЕРСИИ","НОМЕР ВИДА ПРОГРАММЫ","НОМЕР АРМА","РАЗМЕР ФАЙЛА"};
         String F[]={"FILEID","DATEINS","PROG_DISCR","PROGID","ARM","LEN"};
         int S[]={100,134,476,100,100,100};
     V.CAPTION=C;
     V.FIELD=F;    
     V.FSIZE=S;
    }
    
   @Override
    public  String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
    String SELE="SELECT DATEINS,PROG_DISCR,FILEID,PROGID,ARM,LEN  FROM RI_PROJECT_STORAGE ORDER BY FILEID ";
    return SELE;
  }
    
   @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        V.SELE = "SELECT DATEINS,PROG_DISCR,FILEID,PROGID,ARM,LEN  FROM RI_PROJECT_STORAGE WHERE FILEID=" +  A.GETVALSQL(GRID.ALIAS + ".FILEID");// для вставки записи
        return V.SELE;
    }
    
    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга
      if (G[0].SCROLL!=null) {
            G[0].SCROLL.setBounds(1, 1, this.getWidth()-20, this.getHeight()-80);
        }        
     locate(B[0],null,V.LOC_LEFT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(B[1],B[0],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  
     locate(B[32],B[1],V.LOC_RIGHT,0,G[0].SCROLL,V.LOC_DOWN,0) ;  
    }
    
  @Override    
  public void OPEN(){
  G[0].SETFOCUS();
  }
    
    @Override
    public boolean ISEDIT(Gridr GRID, int col) {//Разрешение на корректуру колонки
        return super.ISEDIT(GRID, col); //по умолчанию запроещено false
    }
      @Override
     public void CHANGERECNO(Gridr GRID,int row,int rec)   {//выполняется при изменении записи в гриде
     }
    
   @Override
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
         if ("B0".equals(name)) {try {
             //Для кнопки BO
             String FILE= P.GETFILE("Выберите архив","DIST.ZIP","ZIP",V.CURRENT_PATH);
             File file = new File(FILE);
             V.CURRENT_PATH=file.getPath();
             int len=(int)file.length();
             PreparedStatement st = V.CONN1.prepareStatement("update RI_PROJECT_STORAGE set PROJECTFILE = ?,LEN=?  where FILEID="+A.GETVALSQL("RI_PROJECT_STORAGE.FILEID"));
             st.setBinaryStream(1, new FileInputStream(file), len);
             st.setInt(2,len);
             st.execute();             
             } catch (SQLException ex) {
                 Logger.getLogger(RI_PROJECT_STORAGE.class.getName()).log(Level.SEVERE, null, ex);
             } catch (FileNotFoundException ex) {
                 Logger.getLogger(RI_PROJECT_STORAGE.class.getName()).log(Level.SEVERE, null, ex);
             }
                       //V.TOOLBAR.QUERY(G[0]);
                       QUERY_REC(G[0]);
           }
        if ("B32".equals(name)) {//Для кнопки B32 при вызове как справочник для выбора значений
            V.PARAMOT = new String[]{"OK"};
            THISFORM.DESTROY();
        }
    }
    
    
    public void DEPLOY(String name) { 
        String NAMEF="RI_PROJECT_STORAGE"; 
        P.CLOSEFORM(NAMEF); 
        A.CLOSE(NAMEF); 
        Formr F=P.DOFORM(NAMEF); 
        F.PARENTFORM=this; 
    }    // для дочерних объектов на РАЗВЕРНУТЬ ИЗ ТУЛБАРА ДЛЯ ГРИДА 
    //НЕОБЯЗАТЕЛЬНЫЙ КОД - ДЛЯ ПРИМЕРА ЧТО МОЖНО ПЕРЕКРЫТЬ 
    public String PREV_KEY(Gridr GRID) {//ПОЛУЧЕНИЕ КЛЮЧЕВОГО ВЫРАЖЕНИЯ В WHERE
        return GRID.FORM_K.PREV_KEY(GRID);
    }  
    public String PREV_UPDATE(Gridr GRID) {// для таблиц получение запроса корректуры
          return GRID.FORM_K.PREV_UPDATE(GRID);
    }    
    public String PREV_INSERT(Gridr GRID) {
          return GRID.FORM_K.PREV_INSERT(GRID);
    }    // для таблиц получение запроса новой записи
    public String PREV_DELETE(Gridr GRID) {
          return GRID.FORM_K.PREV_DELETE(GRID);
    }    // для таблиц получение запроса удаления записи
    
    
    
    
     }//окнчание класса
