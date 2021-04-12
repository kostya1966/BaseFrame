package baseclass;
import prg.A;
import prg.P;
import prg.V;


/**Базовый класс формы для корректуры записи
 * 
 * @author Kostya
 */
public class Formr_k extends Formr {
public Buttonr  bok;// КНОПКА СОХРАНИТЬ
public Buttonr  besc;//КНОПКА ОТМЕНИТЬ
public String ALIASP ;//АЛИАС КОРРЕКТУРЫ
public Gridr GRIDP ;//ГРИД КОРРЕКТУРЫ
public Formr FORMP ;//ФОРМА КОРРЕКТУРЫ
public int REC ;//номер записи 
    
public Formr_k(String name, String title, int width, int height) {
    super(name,title,width,height); //Вызов конструктора от базового класса 
     if (V.U_I_D==1) { //вставка записи
        this.setTitle(title+" (НОВАЯ)");
     }
    GRIDP=V.ACTIVEGRID; 
    GRIDP.FORM_K=this;
//    ALIASP=GRIDP.ALIAS;
    FORMP=V.ACTIVEFORM;
    PARENTFORM=FORMP;
    REC=A.RECNO(GRIDP.ALIAS);                
    bok = P.addobjB(this,"bok", "Сохранить", 100, 30,"Записать параметры для последующего использования"); 
    besc = P.addobjB(this,"besc", "Отмена", 100, 30,"Не сохранять изменения");
    this.B_EXIT=besc;
    this.setClosable(false);
    SETRESIZABLE(0); //1-Признак фиксированного размера 0- не фиксированный
    SETMODAL(1);    //1-Модальная форма 0-не модальная
    locate(bok,null,V.LOC_LEFT,V.LOC_SPACE,null,V.LOC_DOWN,V.LOC_SPACE) ;  
    locate(besc,null,V.LOC_RIGHT,0,null,V.LOC_DOWN,V.LOC_SPACE) ;  
    
   P.WRITE_INFO("Для полей c иконкой 'Раскрытая книга' - по двойному щелчку мыши вызов справочной информации для выбора ");   
   OPEN_K();
}
public boolean INIT(){

      if (V.U_I_D==3 )    { //УДАЛЕНИЕ
      if (P.MESSYESNO("Хотите удалить текущую запись?", 1)==1)    {//если НЕТ
            return false;
      }
          
      if (FORMP.DELETE(this.GRIDP)==true  );{ //ВЫЗЫВАЕТСЯ МЕТОД ИЗ РОДИТЕЛЬСКОЙ ФОРМЫ КЛАССА Formr UPDATE С ПАРАМЕТРОМ ТЕКУЩЕГО ГРИДА
          //ОН ВЫЗЫВАЕТ МЕТОД PREV_UPDATE(GRID)ОПИСАННОМ В КЛАССЕ ФОРМЫ ЗАТЕМ SQL ЗАПРОС ПО ПОЛУЧЕННОМУ СКРИПТУ
          if (GRIDP.READ_QUERY=false) {
           FORMP.QUERY_REC(this.GRIDP,-REC);          //если GRIDP.READ_QUERY=false обновление  только одной записи
          } 
          else
                  {
           FORMP.QUERY(this.GRIDP);                 //выполняется полная перевыборка данных
          }
      this.DESTROY(); // ЕСЛИ ВЫПОЛНЕНО ЗАКРЫВАЕМ ФОРМУ
      }
    return false;
      } //УДАЛЕНИЕ
      
    return true;
    
}

public Formr_k(){  
    this("Formr_k","Редактирование записи",400,300); //Вызов конструктора от базового класса 
    
}
@Override
  public void DESCPROP(){
    
  }
  /**
   * ВЫЗЫВАЕТСЯ ПОСЛЕ КОНСТРУКТОРА
   */
   public void OPEN_K(){
  }
   
     
//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
   @Override
    public void RESIZED() {
    super.RESIZED();
    
    locate(bok,null,V.LOC_LEFT,V.LOC_SPACE,null,V.LOC_DOWN,V.LOC_SPACE) ;  
    locate(besc,null,V.LOC_RIGHT,0,null,V.LOC_DOWN,V.LOC_SPACE) ;  
   

    }
  @Override    
  public void OPEN(){
  }

  @Override
  public void CLICK_ALL(String name) {
      if ("bok".equals(name)) 
      {
      if (V.U_I_D==1)    {//вставка записи
      if (FORMP.INSERT(this.GRIDP)==true){ //ВЫЗЫВАЕТСЯ МЕТОД ИЗ РОДИТЕЛЬСКОЙ ФОРМЫ КЛАССА Formr INSERT С ПАРАМЕТРОМ ТЕКУЩЕГО ГРИДА
          //ОН ВЫЗЫВАЕТ МЕТОД PREV_INSERT(GRID)ОПИСАННОМ В КЛАССЕ ФОРМЫ ЗАТЕМ SQL ЗАПРОС ПО ПОЛУЧЕННОМУ СКРИПТУ
          if (GRIDP.READ_QUERY==false) {
          FORMP.QUERY_REC(this.GRIDP,0);          //если GRIDP.READ_QUERY=false обновление  только одной записи
          } 
          else
                  {
           FORMP.QUERY(this.GRIDP);                 //выполняется полная перевыборка данных
          }
      
      this.DESTROY(); // ЕСЛИ ВЫПОЛНЕНО ЗАКРЫВАЕМ ФОРМУ
      }
      }
          
      if (V.U_I_D==2)    {//корректура
       int REC=A.RECNO(GRIDP.ALIAS);                
      if (FORMP.UPDATE(this.GRIDP)==true){ //ВЫЗЫВАЕТСЯ МЕТОД ИЗ РОДИТЕЛЬСКОЙ ФОРМЫ КЛАССА Formr UPDATE С ПАРАМЕТРОМ ТЕКУЩЕГО ГРИДА
//          FORMP.QUERY_REC(this.GRIDP,this.GRIDP.RECNO);
//                FORMP.QUERY(this.GRIDP);          
          if (GRIDP.READ_QUERY==false) {
          FORMP.QUERY_REC(this.GRIDP,REC);          //если GRIDP.READ_QUERY=false обновление  только одной записи
          } 
          else
                  {
           FORMP.QUERY(this.GRIDP);                 //выполняется полная перевыборка данных
          }
                this.DESTROY(); // ЕСЛИ ВЫПОЛНЕНО ЗАКРЫВАЕМ ФОРМУ
      }
//       System.out.print("    2:"+REC+"\n");                
      
      }
      if (V.U_I_D==3)    {//смотри конструктор удаление INIT()
      }
      FORMP.READ_OK=true;          //передача в родительскую форму признака подтверждения корректуры
      }
      if ("besc".equals(name))  {
      FORMP.READ_OK=false;          
      GRIDP.SETFOCUS();          
      this.DESTROY();}
      
      
    }

    //ЕСЛИ ПЕРЕКРЫТ В ФОРМЕ КОРРЕКТУРЫ ТО ВЫПОЛНЯЕТСЯ В ФОРМЕ КОРРЕКТУРЫ , ЕСЛИ ПЕРЕКРЫТ В ВЫЗЫВАЮЩЕЙ КОРРЕКТУРУ ФОРМЕ
  // ТО ВЫПОЛНЯЕТСЯ ТАМ , ИНАЧЕ В БАЗОВОМ КЛАССЕ ФОРМЫ
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

    @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
          return FORMP.PREV_QUERY_REC(GRID);
    }
  
  
  
  
    } //конец класса
   
  


