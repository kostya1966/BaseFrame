package forms;
import baseclass.Formr_k;
import baseclass.Formr;
import baseclass.Gridr;
import prg.FF;
import prg.A;
import prg.P;
import prg.V;
import prg.A;
import prg.P;
import prg.V;
public class users_k extends Formr_k {
public users_k()  {
    super("USERS_K","Редактирование записи",500,400); //Вызов конструктора от базового класса 
}
    
  @Override
  public void LOAD_OBJ(){
  int i=-1;
 i++; L[i]=P.addobjL(this,"LNOMER", "Номер пользователя:");
      F[i]=P.addobjF(this,"NOMER", "", 50, 30);
      F[i].SETTHISKEY(true);      //ключевое поле ;
      F[i].SETTYPE(V.TYPE_NUMERIC);      //числовые данные ;
      F[i].SETINPUTMASK("999999");      //маска ввода ;
 i++; L[i]=P.addobjL(this,"LNAME", "Пароль:");
      F[i]=P.addobjF(this,"NAME", "", 150, 30);
      F[i].SETTYPE(V.TYPE_CHAR);      //строковые данные ;
 i++; L[i]=P.addobjL(this,"LFIO", "Ф.И.О. название:");
      F[i]=P.addobjF(this,"FIO", "", 250, 30);
      F[i].SETTYPE(V.TYPE_CHAR);      //строковые данные ;
 i++; L[i]=P.addobjL(this,"LMENU", "Доступ к пунктам меню:");
      F[i]=P.addobjF(this,"MENU", "", 350, 30);
      F[i].SETTYPE(V.TYPE_CHAR);      //строковые данные ;
 i++; L[i]=P.addobjL(this,"LKPODR", "Код подразделения:");
      F[i]=P.addobjF(this,"KPODR", "", 50, 30);
      F[i].SETTYPE(V.TYPE_CHAR);      //строковые данные ;
 i++; L[i]=P.addobjL(this,"LPODR", "Название подразделения:");
      F[i]=P.addobjF(this,"PODR", "", 250, 30);
      F[i].SETTYPE(V.TYPE_CHAR);      //строковые данные ;
 
      
if (V.U_I_D>1){//если корректура или удаление
   GETF("NOMER").SETVALUE(A.GETVAL("USERS.NOMER"));
   GETF("NAME").SETVALUE(A.GETVAL("USERS.NAME"));
   GETF("FIO").SETVALUE(A.GETVAL("USERS.FIO"));
   GETF("KPODR").SETVALUE(A.GETVAL("USERS.KPODR"));
   GETF("PODR").SETVALUE(A.GETVAL("USERS.PODR"));
   GETF("MENU").SETVALUE(A.GETVAL("USERS.MENU"));
}
    
}
    
//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
 @Override
 public void LOC_ABOUT() {
 LOCOBJR=null; LOCOBJD=null; int xy=10;
 locd(GETL("LNOMER"),xy);  locr(GETF("NOMER"),0) ;          
 locd(GETL("LNAME"),xy) ;  locr(GETF("NAME"),0) ;        
 locd(GETL("LFIO"),xy) ;  locr(GETF("FIO"),0) ;        
 locd(GETL("LMENU"),xy) ;  locr(GETF("MENU"),0) ;        
 locd(GETL("LKPODR"),xy) ;  locr(GETF("KPODR"),0) ;        
 locd(GETL("LPODR"),xy) ;  locr(GETF("PODR"),0) ;        
  
}
    
  @Override    
  public void OPEN(){
   F[0].requestFocus(); //фокус по умолчанию
  }
    
  @Override
  public void CLICK_ALL(String name) {
//      if ("bok".equals(name)) 
      super.CLICK_ALL(name);
          }   
    
    
    @Override
    public boolean VALID_ALL(String name) {
     if (V.ARM!=4) {return true;}
      if   (  VALIDON==false) {return true;}
        if (name.equals("KPODR")) {
            V.SELE = "SELECT *  FROM CONFIG_DC_V  WHERE DCID=" + P.P_SQL(GETF("KPODR").GETVALUE());
            P.SQLEXECT(V.SELE, "UD");
            if (A.RECCOUNT("UD") == 0) {
                P.MESSERR("Значение не найдено !");
                return true;
            }
        }
        return true;
    }

    public void DBLCLICK_ALL(String name) {
     if (V.ARM!=4) {return ;}
        
        if (name.equals("KPODR")) {
            VALIDON=false;
            Formr form = P.DOFORM("CONFIG_DC_V",1);
            VALIDON=true;
            if (V.PARAMOT!=null) {
            GETF("KPODR").SETVALUE(A.GETVAL("CONFIG_DC.DCID"));            
            }        
        }
        

    }
    
    
     }//окнчание класса
    
