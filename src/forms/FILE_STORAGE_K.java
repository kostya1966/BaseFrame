package forms;
import baseclass.Formr_k;
import baseclass.Gridr;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;
public class FILE_STORAGE_K extends Formr_k {
    String AL1="F_ZAYVKA1";
    int REL=A.GETVALN("F_ZAYVKA1.REL");
    String FILE_ST="";    
public FILE_STORAGE_K()  {
    super("FILE_STORAGE_K","Редактирование записи",500,400); //Вызов конструктора от базового класса 
}
    
  @Override
  public void LOAD_OBJ(){
  int i=-1;
 i++; L[i]=P.addobjL(this,"LFILEID", "Номер записи:");
      F[i]=P.addobjF(this,"FILEID", "", 60, 30);
      F[i].SETTYPE(V.TYPE_NUMERIC);      //числовые данные ;
      F[i].SETTHISKEY(true);      //ключевое поле ;
      F[i].SETREADONLY(true);      //только чтение ;
      F[i].SETINPUTMASK("9999999999");      //маска ввода ;
 i++; L[i]=P.addobjL(this,"LNAIM", "Имя файла:"); L[i].SETICON();
      F[i]=P.addobjF(this,"NAIM", "", 250, 30);
      F[i].SETREADONLY(true);      //только чтение ;
 i++; L[i]=P.addobjL(this,"LTEXT", "Примечание:");
      F[i]=P.addobjF(this,"TEXT", "", 450, 30);
 i++; L[i]=P.addobjL(this,"LDATE_S", "Время загрузки:");
      F[i]=P.addobjF(this,"DATE_S", "", 150, 30);
      F[i].SETREADONLY(true);      //только чтение ;
      F[i].SETSQLUPDATE(false);
 i++; L[i]=P.addobjL(this,"LLEN", "Длина файла байт:");
      F[i]=P.addobjF(this,"LEN", "", 100, 30);
      F[i].SETTYPE(V.TYPE_NUMERIC);      //числовые данные ;
      F[i].SETINPUTMASK("");      //маска ввода ;
      F[i].SETREADONLY(true);      //только чтение ;
 i++; 
      F[i]=P.addobjF(this,"REL", "", 150, 30); F[i].SETREADONLY(true);
      F[i].SETTYPE(V.TYPE_NUMERIC);      //числовые данные ;
      F[i].SETINPUTMASK("999999999");      //маска ввода ;
      F[i].setVisible(false);
if (V.U_I_D>1){//если корректура или удаление
   GETF("FILEID").SETVALUE(A.GETVAL("FILE_STORAGE.FILEID"));
   GETF("NAIM").SETVALUE(A.GETVAL("FILE_STORAGE.NAIM"));
   GETF("TEXT").SETVALUE(A.GETVAL("FILE_STORAGE.TEXT"));
   GETF("DATE_S").SETVALUE(A.GETVAL("FILE_STORAGE.DATE_S"));
   GETF("LEN").SETVALUE(A.GETVAL("FILE_STORAGE.LEN"));
}
   GETF("REL").SETVALUE(REL);
    
}
    
//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
 @Override
 public void LOC_ABOUT() {
 LOCOBJR=null; LOCOBJD=null; int xy=10;
 locd(GETL("LFILEID"),xy);  locr(GETF("FILEID"),0) ;          
 locd(GETL("LNAIM"),xy) ;  locr(GETF("NAIM"),0) ;        
 locd(GETL("LTEXT"),xy) ;  locr(GETF("TEXT"),0) ;        
 locd(GETL("LDATE_S"),xy) ;  locr(GETF("DATE_S"),0) ;        
 locd(GETL("LLEN"),xy) ;  locr(GETF("LEN"),0) ;        
}
    
  @Override    
  public void OPEN(){
   F[1].requestFocus(); //фокус по умолчанию
  }
    
  @Override
  public void CLICK_ALL(String name) {
      if ("bok".equals(name) && V.U_I_D==1) { //выполнить до сохранения если ввод записи
       int FILEID=A.GETMAXREL("FILIAL.FILE_STORAGE")  ;// НАХОДИМ СЛЕДУЮЩИЙ НОМЕР ОТ МАКСИМАЛЬНОГО          
       if (FILEID<0) {return;} //ЕСЛИ ОШИБКА ВЫПОЛНЕНИЯ 
       GETF("FILEID").SETVALUE(FILEID); 
      }
      super.CLICK_ALL(name);
      if ("bok".equals(name) && V.U_I_D<3 && !FF.EMPTY(FILE_ST)) { //ВВОД ИЛИ КОРРЕКТУРА ЗАПИСЫВАЕМ ФАЙЛ
          try {          
             File file = new File(FILE_ST);
             int len=(int)file.length();
             
             PreparedStatement st = V.CONN1.prepareStatement("update FILIAL.FILE_STORAGE set FILE_ST = ?,LEN= ?,DATE_S=SYSDATE,PATH = ? where FILEID="+P.P_SQL(GETF("FILEID").GETVALUE()));
             st.setBinaryStream(1, new FileInputStream(file), len);
             st.setInt(2,len);
             st.setString(3, file.getPath());             
             st.execute();             
             } catch (SQLException | FileNotFoundException ex) {
                 P.MESSERR("Не удалось записать файл "+FILE_ST+"\n"+ex.getMessage());
             }
      }

      }   

     @Override
    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
       V.SELE="SELECT FILEID,NAIM,TEXT,LEN,DATE_S,PATH  FROM FILIAL.FILE_STORAGE ";
        V.SELE=V.SELE+"    WHERE FILEID="+ P.P_SQL(GETF("FILEID").GETVALUE());        
        return V.SELE;
    }
  public void DBLCLICK_ALL(String name) {
      
        if (name.equals("NAIM")  ) { //
            VALIDON=false;   
             FILE_ST= P.GETFILE("Выберите файл","","",V.CURRENT_PATH);
            VALIDON=true;
             
             if ("".equals(FILE_ST)) 
             {
                 return;
             }
                 
             File file = new File(FILE_ST);
             V.CURRENT_PATH=file.getPath();
             int len=(int)file.length();
             GETF("NAIM").SETVALUE(file.getName());     
             GETF("LEN").SETVALUE(0);             
        }
        }  


     }//окнчание класса
    
