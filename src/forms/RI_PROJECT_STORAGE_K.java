package forms;
import baseclass.Formr_k;
import baseclass.Gridr;
import prg.A;
import prg.P;
import prg.V;
public class RI_PROJECT_STORAGE_K extends Formr_k {
public RI_PROJECT_STORAGE_K()  {
    super("RI_PROJECT_STORAGE_K","Редактирование записи",500,400); //Вызов конструктора от базового класса 
}
    
  @Override
  public void LOAD_OBJ(){
  int i=-1;
 i++; L[i]=P.addobjL(this,"LDATEINS", "ВРЕМЯ ВСТАВКИ:");
      F[i]=P.addobjF(this,"DATEINS", "", 150, 30);
      F[i].SETREADONLY(true);      //только чтение ;
    F[i].SETTYPE(V.TYPE_DATETIME);      //тип датывремя ;
    F[i].SETSQLUPDATE(false);
 i++; L[i]=P.addobjL(this,"LPROG_DISCR", "ОПИСАНИЕ ВЕРСИИ:");
      F[i]=P.addobjF(this,"PROG_DISCR", "", 350, 30);
 i++; L[i]=P.addobjL(this,"LFILEID", "НОМЕР ОБНОВЛЕНИЯ:");
      F[i]=P.addobjF(this,"FILEID", "", 50, 30);
      F[i].SETTHISKEY(true);      //ключевое поле ;
      F[i].SETTYPE(V.TYPE_NUMERIC);      //числовые данные ;
      F[i].SETINPUTMASK("999999");      //маска ввода ;
 i++; L[i]=P.addobjL(this,"LPROGID", "НОМЕР ВИДА ПРОГРАММЫ:");
      F[i]=P.addobjF(this,"PROGID", "", 50, 30);
 i++; L[i]=P.addobjL(this,"LARM", "НОМЕР АРМА:");
      F[i]=P.addobjF(this,"ARM", "", 50, 30);
      F[i].SETTYPE(V.TYPE_NUMERIC);      //числовые данные ;
      F[i].SETINPUTMASK("999999");      //маска ввода ;

  GETF("PROG_DISCR").SETVALUE(A.GETVAL("RI_PROJECT_STORAGE.PROG_DISCR"));
if (V.U_I_D>1){//если корректура или удаление
   GETF("DATEINS").SETVALUE(A.GETVAL("RI_PROJECT_STORAGE.DATEINS"));
   GETF("FILEID").SETVALUE(A.GETVAL("RI_PROJECT_STORAGE.FILEID"));
   GETF("PROGID").SETVALUE(A.GETVAL("RI_PROJECT_STORAGE.PROGID"));
   GETF("ARM").SETVALUE(A.GETVAL("RI_PROJECT_STORAGE.ARM"));
}  
if (V.U_I_D==1){//если ввод
   GETF("FILEID").SETVALUE(A.GETVALMAXS("RI_PROJECT_STORAGE","FILEID")); // НАХОДИМ СЛЕДУЮЩИЙ НОМЕР ОТ МАКСИМАЛЬНОГО
   GETF("ARM").SETVALUE(V.ARM);

}


}
    
//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
 @Override
 public void LOC_ABOUT() {
 LOCOBJR=null; LOCOBJD=null; int xy=10;
 locd(GETL("LDATEINS"),xy);  locr(GETF("DATEINS"),0) ;          
 locd(GETL("LPROG_DISCR"),xy) ;  locr(GETF("PROG_DISCR"),0) ;        
 locd(GETL("LFILEID"),xy) ;  locr(GETF("FILEID"),0) ;        
 locd(GETL("LPROGID"),xy) ;  locr(GETF("PROGID"),0) ;        
 locd(GETL("LARM"),xy) ;  locr(GETF("ARM"),0) ;        
}
    
  @Override    
  public void OPEN(){
   F[1].requestFocus(); //фокус по умолчанию
  }
    
  @Override
  public void CLICK_ALL(String name) {
//      if ("bok".equals(name)) 
      super.CLICK_ALL(name);
          }   
   @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        V.SELE = "SELECT DATEINS,PROG_DISCR,FILEID,PROGID,ARM,LEN  FROM RI_PROJECT_STORAGE WHERE FILEID="  + P.P_SQL(GETF("FILEID").GETVALUE());// для вставки записи
        return V.SELE;
    }
    
    
    
     }//окнчание класса
    
