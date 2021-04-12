package forms;
import baseclass.Formr;
import baseclass.Gridr;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import prg.A;
import prg.P;
import prg.V;
    
public class FILE_STORAGE extends Formr {
    //String AL1="F_ZAYVKA1";
    //String AL2="F_ZAYVKA2";
        int REL=A.GETVALN("F_ZAYVKA1.REL");
public FILE_STORAGE() {  
    super("FILE_STORAGE"," ",1100,500); 
    setTitle("Список сохраненных документов по заявке №"+REL);
}
    
    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
    //P.WRITE_INFO("Сообщение в инфопанели");        
        B[32] = P.addobjB(this, "B32", "Выбрать текущее значение", "Занести значение текущей записи в поле ввода"); //добавляем кнопку выбора текущей записи и закрытия формы
        B[32].setVisible(SPR); // если не справочник-выбора занчит не из поля ввода , значит невидимая
     B[0] =P.addobjB(this, "B0", "Перезаписать файл", "Выбрать и записать  файл для текущей записи"); 
     B[1] =P.addobjB(this, "B1", "Выгрузить и просмотреть файл", "Выгрузка из таблицы и просмотр файла"); 
     G[0] =P.addobjG(this,"FILE_STORAGE",1,1,980, 500); 
     B[0].setVisible(false);
    }
    
    public  void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
     GRID.VIEWRECNO=0; //не показывать номер записи в первой колонке -0
     GRID.RESTORECOL=1;//не восстанавливать размеры -0 
     GRID.ALIAS = GRID.getName();     //ИМЯ ГРИДА И АЛИАС СОВПАДАЮТ 
     GRID.TABLE = "FILIAL."+GRID.getName();     //ИМЯ ТАБЛИЦЫ     
     if (P.VALID_MENUD("102") || V.USER_ADMIN>=3){
      GRID.FREAD="FILE_STORAGE_K";//форма корректуры
      GRID.ENABLE_DELETE=true;
      GRID.ENABLE_INSERT=true;
      GRID.ENABLE_READ=true;
     } else {
      GRID.FREAD="";//форма корректуры
     }
     GRID.ENABLE_DEPLOY = false;  //вызов другой формы спецификации
     GRID.HEADERH=50; //ПО УМОЛЧАНИЮ 50
     //GRID.SETLOCATEFOR("PROGID");     
    }
    
     public void READ_AFTER(Gridr GRID) {
         if (READ_OK) { // если на форме корректуры  нажата сохранить
           QUERY_REC(G[0]);
         }
    }
    
   @Override
    public  void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
         String C[]={"Номер П.П.","Имя файла","Описание","Размер файла","Время записи","Каталог при загрузке"};
         String F[]={"FILEID","NAIM","TEXT","LEN","DATE_S","PATH"};
         int S[]={60,120,400,100,150,250};
     V.CAPTION=C;
     V.FIELD=F;    
     V.FSIZE=S;
    }
    
   @Override
    public  String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
    V.SELE="SELECT FILEID,NAIM,TEXT,LEN,DATE_S,PATH  FROM FILIAL.FILE_STORAGE ";
    V.SELE=V.SELE+"  WHERE REL="+ REL+" ORDER BY DATE_S DESC ";        
    
    return V.SELE;
  }
    
   @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        V.SELE = "SELECT FILEID,NAIM,TEXT,LEN,DATE_S,PATH  FROM FILIAL.FILE_STORAGE WHERE FILEID=" +  A.GETVALSQL(GRID.ALIAS + ".FILEID");// для вставки записи
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
         if ("B1".equals(name)) {
             String NAIM=A.GETVALS("FILE_STORAGE.NAIM");
        P.ALERT("Получение фала:"+NAIM);
        V.SELE = "SELECT FILE_ST FROM FILIAL.FILE_STORAGE WHERE FILEID=" + A.GETVALS("FILE_STORAGE.FILEID");
        if (!P.BLOB_TO_FILE(V.SELE, V.FILES_DIRECTORY  + NAIM)) { //ДОСТАЕМ БИНАРНОЕ ПОЛЕ  И ПЕРЕВОДИМ ЕГО В ФАЙЛ 
            P.MESS("Ошибка получения файла "+NAIM);
            return;
        }
          P.ALERT("");     
          if (P.MESSYESNO("Открыть файл "+V.FILES_DIRECTORY  + NAIM+" ?")!=0) {
              return ;
          }
          Desktop desktop = null;
          if (Desktop.isDesktopSupported()) {
          desktop = Desktop.getDesktop();
          }
          try {
            desktop.open(new File(V.FILES_DIRECTORY  + NAIM)); // открытие документа через проводник 
          } catch (IOException ioe) {
            //ioe.printStackTrace();
              P.MESSERR("Ошибка открытия "+V.FILES_DIRECTORY  + NAIM+"\n"+ioe.getMessage());
           }                
         }
        
        if ("B32".equals(name)) {//Для кнопки B32 при вызове как справочник для выбора значений
            V.PARAMOT = new String[]{"OK"};
            THISFORM.DESTROY();
        }
    }
    
    
    
    
    
     }//окнчание класса
