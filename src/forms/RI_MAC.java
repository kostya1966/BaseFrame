package forms;
import baseclass.Formr;
import baseclass.Gridr;
import prg.A;
import prg.P;
import prg.V;
    
public class RI_MAC extends Formr {
 private final String AL1="RI_MAC";
public RI_MAC() {  
    super("RI_MAC","Настройка доступа гаджитов в системе",1100,500); 
}
    
    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
    //P.WRITE_INFO("Сообщение в инфопанели");        
        B[32] = P.addobjB(this, "B32", "Выбрать текущее значение", "Занести значение текущей записи в поле ввода"); //добавляем кнопку выбора текущей записи и закрытия формы
        B[32].setVisible(SPR); // если не справочник-выбора занчит не из поля ввода , значит невидимая
     B[0] =P.addobjB(this, "B0", "Кнопка 1", "Описание работы кнопки 1"); 
     B[1] =P.addobjB(this, "B1", "Кнопка 2", "Описание работы кнопки 2"); 
     G[0] =P.addobjG(this,"RI_MAC",1,1,880, 500); 
     B[0].setVisible(false); // при создании кода программно 
     B[1].setVisible(false); // при создании кода программно
    }
    
   @Override
    public  void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
     GRID.VIEWRECNO=0; //не показывать номер записи в первой колонке -0
     GRID.RESTORECOL=1;//не восстанавливать размеры -0 
     GRID.ALIAS = GRID.getName();     //ИМЯ ГРИДА И АЛИАС СОВПАДАЮТ 
     GRID.FREAD="RI_MAC_K";//форма корректуры
     GRID.ENABLE_DEPLOY = false;  //вызов другой формы спецификации
    }
   @Override
    public  void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы
         String C[]={"Мак адрес","Табельный номер","ФИО","Номер пользователя","Название роли","Примечание","Остановка разрешения (0- остановить)","Время ввода","ПК ввода","Номер программы","Время последнего входа","Писать лог 1","Версия","Код подр.","Название подразделения","Модель устройства","Версия андроид"};
         String F[]={"MAC","TABNO","FIO","NOMER","FIO_USER","TEXT","STOP","DATE_S","USER_PC","ARM","DATE_IN","LOG","VER","KPODR","PODR","MODEL","AND_VER"};
         int S[]={148,75,216,90,147,143,100,129,211,80,100,50,50,60,200,150,80};
     V.CAPTION=C;
     V.FIELD=F;    
     V.FSIZE=S;
    }
    
   @Override
    public  String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
    String SELE="SELECT *  FROM RI_MAC WHERE ARM>=6 ORDER BY DATE_S DESC";
    return SELE;
  }
    
   @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        V.SELE = "SELECT *  FROM RI_MAC WHERE MAC=" +A.GETVALSQL(AL1+".MAC");// для вызова при редактировании
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
         if ("B0".equals(name)) {//Для кнопки BO
           }
        if ("B32".equals(name)) {//Для кнопки B32 при вызове как справочник для выбора значений
            V.PARAMOT = new String[]{"OK"};
            THISFORM.DESTROY();
        }
    }
    
    
    @Override
    public void DEPLOY(String name) { 
        String NAMEF="RI_MAC"; 
        P.CLOSEFORM(NAMEF); 
        A.CLOSE(NAMEF); 
        Formr form=P.DOFORM(NAMEF); 
        form.PARENTFORM=this; 
    }    // для дочерних объектов на РАЗВЕРНУТЬ ИЗ ТУЛБАРА ДЛЯ ГРИДА 
    
    
    
    
     }//окнчание класса
