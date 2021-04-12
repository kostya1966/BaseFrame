package forms;
import baseclass.Formr;
import baseclass.Formr_k;
import baseclass.Gridr;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;
public class RI_MAC_K extends Formr_k {
 private final String AL1="RI_MAC";
// private final String REL=A.GETVALS(AL1+".REL"); 
  public RI_MAC_K()  {
    super("RI_MAC_K","Редактирование записи",500,400); //Вызов конструктора от базового класса 
}
    
  @Override
  public void LOAD_OBJ(){
  //примеры    L[i].SETICON();-картинка книжки  F[i].SETSELECTON(true);-автовыделение текста  F[i].SETSQLUPDATE(false);-не обновляется  
  //F[i].SETSPACE_LEFT(false);-без левых пробелов 
  int i=-1;
 i++; L[i]=P.addobjL(this,"LMAC", "Мак адрес:");
      F[i]=P.addobjF(this,"MAC", "", 150, 30); F[i].SETTHISKEY(true);
      F[i].SETREADONLY(true);      //только чтение ;
 i++; L[i]=P.addobjL(this,"LTABNO", "Табельный номер:"); //L[i].SETICON();
      F[i]=P.addobjF(this,"TABNO", "", 100, 30);
 i++; L[i]=P.addobjL(this,"LFIO", "ФИО:");
      F[i]=P.addobjF(this,"FIO", "", 250, 30);
      F[i].SETREADONLY(true);      //только чтение ;      
 i++; L[i]=P.addobjL(this,"LNOMER", "Номер пользователя-роли:");
      F[i]=P.addobjF(this,"NOMER", "", 50, 30);
      F[i].SETTYPE(V.TYPE_NUMERIC);      //числовые данные ;
      F[i].SETINPUTMASK("999999");      //маска ввода ;
 i++; L[i]=P.addobjL(this,"LFIO_USER", "Название роли:");
      F[i]=P.addobjF(this,"FIO_USER", "", 250, 30);
      F[i].SETREADONLY(true);      //только чтение ;      
 i++; L[i]=P.addobjL(this,"LTEXT", "Примечание:");
      F[i]=P.addobjF(this,"TEXT", "", 350, 30);
 i++; L[i]=P.addobjL(this,"LSTOP", "Остановка разрешения (0- остановить):");
      F[i]=P.addobjF(this,"STOP", "", 150, 30);
      F[i].SETTYPE(V.TYPE_NUMERIC);      //числовые данные ;
      F[i].SETINPUTMASK("9");      //маска ввода ;
 i++; L[i]=P.addobjL(this,"LARM", "Номер программы:");
      F[i]=P.addobjF(this,"ARM", "", 50, 30);
      F[i].SETREADONLY(true);      //только чтение ;
 i++; L[i]=P.addobjL(this,"LLOG", "Писать лог -1 :");
      F[i]=P.addobjF(this,"LOG", "", 50, 30);
 i++; L[i]=P.addobjL(this,"LKPODR", "Код подр.:"); //L[i].SETICON();
      F[i]=P.addobjF(this,"KPODR", "", 60, 30);
 i++; L[i]=P.addobjL(this,"LPODR", "Название подразделения:");
      F[i]=P.addobjF(this,"PODR", "", 250, 30);
      //F[i].SETREADONLY(true);      //только чтение ;      

      
if (V.U_I_D>1){//если корректура или удаление
   GETF("MAC").SETVALUE(A.GETVAL("RI_MAC.MAC"));
   GETF("TABNO").SETVALUE(A.GETVAL("RI_MAC.TABNO"));
   GETF("FIO").SETVALUE(A.GETVAL("RI_MAC.FIO"));
   GETF("TEXT").SETVALUE(A.GETVAL("RI_MAC.TEXT"));
   GETF("STOP").SETVALUE(A.GETVAL("RI_MAC.STOP"));
   GETF("ARM").SETVALUE(A.GETVAL("RI_MAC.ARM"));
   GETF("NOMER").SETVALUE(A.GETVAL("RI_MAC.NOMER"));
   GETF("FIO_USER").SETVALUE(A.GETVAL("RI_MAC.FIO_USER"));
   GETF("LOG").SETVALUE(A.GETVAL("RI_MAC.LOG"));
   GETF("KPODR").SETVALUE(A.GETVAL("RI_MAC.KPODR"));
   GETF("PODR").SETVALUE(A.GETVAL("RI_MAC.PODR"));
   
}
//  RDERS(,GETF("MAC"),GETF("TABNO"),GETF("FIO"),GETF("TEXT"),GETF("STOP"),GETF("ARM"),GETF("NOMER")); //порядок перехода фокуса между элементами
}
    
//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
 @Override
 public void LOC_ABOUT() {
 LOCOBJR=null; LOCOBJD=null; int xy=10;
 locd(GETL("LMAC"),xy);  locr(GETF("MAC"),0) ;          
 locd(GETL("LTABNO"),xy) ;  locr(GETF("TABNO"),0) ;        
 locd(GETL("LFIO"),xy) ;  locr(GETF("FIO"),0) ;        
 locd(GETL("LNOMER"),xy) ;  locr(GETF("NOMER"),0) ;        
 locd(GETL("LFIO_USER"),xy) ;  locr(GETF("FIO_USER"),0) ;        
 locd(GETL("LTEXT"),xy) ;  locr(GETF("TEXT"),0) ;        
 locd(GETL("LSTOP"),xy) ;  locr(GETF("STOP"),0) ;        
 locd(GETL("LARM"),xy) ;  locr(GETF("ARM"),0) ;        
 locd(GETL("LLOG"),xy) ;  locr(GETF("LOG"),0) ;        
 locd(GETL("LKPODR"),xy) ;  locr(GETF("KPODR"),0) ;        
 locd(GETL("LPODR"),xy) ;  locr(GETF("PODR"),0) ;        
 
}
    
  @Override    
  public void OPEN(){
   F[1].requestFocus(); //фокус по умолчанию
  }
    
  @Override
  public void CLICK_ALL(String name) {
 //     if ("bok".equals(name) && V.U_I_D==1) { //выполнить до сохранения если ввод записи 
 //      int REL=A.GETMAXREL(AL1)  ;// НАХОДИМ СЛЕДУЮЩИЙ НОМЕР ОТ МАКСИМАЛЬНОГО          
 //      if (REL<0) {return;} //ЕСЛИ ОШИБКА ВЫПОЛНЕНИЯ 
 //      GETF("REL").SETVALUE(REL); 
 //     }
      super.CLICK_ALL(name);
          }  
  
           @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        V.SELE = "SELECT *  FROM "+AL1+" WHERE MAC=" + P.P_SQL(GETF("MAC").getText());// для обновления записи
        return V.SELE;
    }
    @Override
    public void DBLCLICK_ALL(String name) {
      // VM.DBLCLICK_ALL(THISFORM,name);
        }
    @Override
    public boolean VALID_ALL(String name) {
      //  return VM.VALID_ALL(THISFORM, name);
        Formr form=THISFORM;
           String AL="UD";
      if   (  form.VALIDON==false) {return true;}   
      
        if (name.equals("NOMER") && "RI_MAC_K".equals(form.getName())) {
            V.SELE = "SELECT FIO  FROM USERS  WHERE NOMER=" + P.P_SQL(form.GETF(name).GETVALUE())+ " ";
            P.SQLEXECT(V.SELE, "UD");
            if (A.RECCOUNT("UD") == 0) {
                P.MESSERR("Значение не найдено !");
                return false;
            }
        form.GETF("FIO_USER").SETVALUE(A.GETVAL("UD.FIO"));
          
        }
      

        if (name.equals("TABNOV") || name.equals("TABNO")) {
            if ((name.equals("TABNOV") || form.getName().equals("RI_MAC_K")) && FF.EMPTY(form.GETF(name).getText() ) ) { //если пустой табельный 
                if (form.getName().equals("RI_MAC_K")) {
                    form.GETF("FIO").SETVALUE("");
                }
                return true;
            }
            if (V.ARM==1) { //если из FIRM
            V.SELE = "SELECT *  FROM S_SELLER_V  WHERE   TABNO=" + P.P_SQL(form.GETF(name).GETVALUE())+" OR ID="+P.P_SQL(form.GETF(name).getText(),V.TYPE_NUMERIC);
            }  else {
            V.SELE = "SELECT *  FROM ST_TAB_V  WHERE ID="+P.P_SQL(form.GETF(name).getText(),V.TYPE_NUMERIC)+" OR  TABNO=" + P.P_SQL(form.GETF(name).GETVALUE());
            }
            P.SQLEXECT(V.SELE, "UD");
            if (A.RECCOUNT("UD") == 0) {
                P.MESSERR("Значение не найдено !");
                return false;
            }
        if (name.equals("TABNO"))  {  
        if (V.FormCorrName != "TABNO") {
          form.GETF("FIO").SETVALUE(A.GETVAL("UD.FIO"));            
        } else {
            V.FormCorr.GETF("F1").SETVALUE(A.GETVAL("UD.FIO"));
            V.FormCorr.GETF("F2").SETVALUE(A.GETVAL("UD.KCEH"));
        }
          form.GETF("TABNO").SETVALUE(A.GETVAL("UD.TABNO"));            
         if (form.GET("DOL",form.F)!=-1) {      
          form.GETF("DOL").SETVALUE(A.GETVAL("UD.DOL"));            
         }
        }
        if (name.equals("TABNOV"))  {            
          form.GETF("FIOV").SETVALUE(A.GETVAL("UD.FIO"));            
        }
        
        
        }
        if (name.equals("ART")) {
            V.SELE = "SELECT *  FROM S_ART  WHERE ART=" + P.P_SQL(form.GETF(name).GETVALUE());
            P.SQLEXECT(V.SELE, "UD");
            if (A.RECCOUNT("UD") == 0) {
                P.MESSERR("Значение не найдено !");
                return false;
            }
          //form.GETF("NDET").SETVALUE(A.GETVAL("UD.NDET"));            
        }
        
        
        return true;
    }
        
   
    
    
    
    
     }//окнчание класса
    
