package forms;

import baseclass.Buttonr;
import baseclass.Formr;
import java.awt.Color;
import java.awt.Image;
import java.util.List;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import prg.FF;
import prg.P;
import prg.V;

public class PRINT extends Formr {
public Buttonr  bok;// КНОПКА СОХРАНИТЬ
public Buttonr  besc;//КНОПКА ОТМЕНИТЬ
public Buttonr  r;//КНОПКА следующая страница
public Buttonr  l;//КНОПКА предыдущая страница

    String art = "";
    int wp = V.JP.getPageWidth();
    int hp = V.JP.getPageHeight();
    int npp=1;
    float koef=1;
    int page =V.JP.getPages().size();
    //String PRINT =V.PRINTER+"  Очередь:"+P.PRINT_Q(V.PRINTER) ; // количество заданий в принтере
    public PRINT() {
        super("PRINT", V.PRINTER, 300, 550);
        setTitle(V.PRINTER+"  Очередь:"+P.PRINT_Q(V.PRINTER));
    }

    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
    B[3] =P.addobjB(this, "B3", "Принтер", "Изменить принтер печати"); 
    L[0] = P.addobjL(this, "L0", "Образ");
    L[0].setOpaque(true);
    L[0].setBackground(Color.BLACK);
    F[0]=P.addobjF(this,"F0","1", 50, 30);
     F[0].SETREADONLY(true);      //только чтение ; 
    L[1] = P.addobjL(this, "L1", "из");
     
    F[1]=P.addobjF(this,"F1",FF.STR(page), 50, 30);
     F[1].SETREADONLY(true);      //только чтение ; 
    
//     F[3] = P.addobjF(this, "F1", V.PRINTER, 250, 40); //принтер 
 //    F[3].SETREADONLY(true);
   
    bok = P.addobjB(this,"bok", "Печать", 100, 30,"Отправить на принтер задание"); 
    besc = P.addobjB(this,"besc", "Отмена", 100, 30,"Выход и отмена печати");
    r = P.addobjB(this,"r", ">", 40, 30,"Следующая страница"); 
    l = P.addobjB(this,"l", "<", 40, 30,"Предыдущая страница"); 
    r.setEnabled(page>1);
    l.setEnabled(false);
    
    this.B_EXIT=besc;
    this.setClosable(false);
    SETRESIZABLE(0); //1-Признак фиксированного размера 0- не фиксированный
    SETMODAL(1);    //1-Модальная форма 0-не модальная
//    bok.requestFocus();
    }

    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга
LOCOBJR=null; LOCOBJD=null; int xy=5;
 locd(GETL("L0"),xy); // locr(GETF("REL"),0) ;          
int w=this.getWidth()-5;
int h=this.getHeight()-150;
if (w/wp>koef && h/hp>koef ){
    koef=(float)w/wp;
    this.OPEN();
}
if ((wp*koef>w || hp*koef>h) && koef>0){
    koef=(float)w/wp;
    this.OPEN();
}
if (koef==0 )
{ koef=1 ; }
        
 if (L[0] != null) {           L[0].setBounds(L[0].getX()+5,L[0].getY() ,w, h  );        }
    locate(bok,null,V.LOC_LEFT,V.LOC_SPACE,null,V.LOC_DOWN,V.LOC_SPACE) ;  
    locate(besc,null,V.LOC_RIGHT,0,null,V.LOC_DOWN,V.LOC_SPACE) ;  
    locate(L[1],null,V.LOC_CENTR,V.LOC_SPACE,L[0],V.LOC_DOWN,V.LOC_SPACE) ;  
    locate(F[0],L[1],V.LOC_LEFT,V.LOC_SPACE,L[0],V.LOC_DOWN,V.LOC_SPACE) ;  
    locate(F[1],L[1],V.LOC_RIGHT,V.LOC_SPACE,L[0],V.LOC_DOWN,V.LOC_SPACE) ;  
    locate(r,F[1],V.LOC_RIGHT,V.LOC_SPACE,L[0],V.LOC_DOWN,V.LOC_SPACE) ;  
    locate(l,F[0],V.LOC_LEFT,V.LOC_SPACE,L[0],V.LOC_DOWN,V.LOC_SPACE) ;  

    //locate(F[3],null,V.LOC_CENTR,V.LOC_SPACE+50,null,V.LOC_DOWN,V.LOC_SPACE) ;  
    locate(B[3],null,V.LOC_CENTR,V.LOC_SPACE,null,V.LOC_DOWN,V.LOC_SPACE) ;  
    
     }

    @Override
    public void OPEN() {
        if (L[0] != null) {
            try {            
              List<JRPrintPage> ps= V.JP.getPages();
              //ps.get(0).
                Image capt= JasperPrintManager.printPageToImage(V.JP,npp-1, koef);
             //   ImageIO.write(capt, "jpg",new File("C:\\out.jpg"));
                ImageIcon icon = new ImageIcon(capt);                 
                L[0].setIcon(icon);
            } catch (JRException ex) {
            P.MESSERR("Ошибка формирования образа \n"+ex.getMessage());
                return;
            } catch (JRRuntimeException ex) {
            P.MESSERR("Ошибка формирования образа \n"+ex.getMessage());
                return;
            } catch (Exception ex) {
            P.MESSERR("Ошибка формирования рисунка отчета \n"+ex.getMessage());
                return;
                
            }
            
                }
        bok.requestFocus();
    }

  @Override
  public void CLICK_ALL(String name) {
      if ("B3".equals(name)) {
          String print= P.PRINTERGET(B[3]); //выбор принтера
          if (FF.EMPTY(print)){
              return;
          }
          V.PRINTER=print; //
//String WorkOffline = getWMIValue("Select * from Win32_Printer where Name='printer_name'", "WorkOffline");          
          setTitle(V.PRINTER+"  Очередь:"+P.PRINT_Q(V.PRINTER));
          
      }
      
      if ("bok".equals(name)) {
 JRPrintServiceExporter exporter = new JRPrintServiceExporter(); 
 PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet(); 
 printServiceAttributeSet.add(new PrinterName(V.PRINTER, null)); 
 exporter.setParameter(JRExporterParameter.JASPER_PRINT, V.JP); 
 exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet); 
 exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE); 
 exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE); 
 //if (exporter.getPrintStatus()) { //СТАТУС ПРИНТЕРА
     
 //}
 //JRPropertiesUtil R =exporter.getPropertiesUtil();
 
          try {            
              exporter.exportReport();
          } catch (JRException ex) {
            P.MESSERR("Ошибка формирования отчета "+V.FILE_REPORT+"\n"+ex.getMessage());
            System.out.print(ex.getMessage());
            V.TF_PR=false;
            return ;
          }
       V.TF_PR=true;          
      this.DESTROY(); // ЕСЛИ ВЫПОЛНЕНО ЗАКРЫВАЕМ ФОРМУ
      }
      if ("besc".equals(name))  {
          V.TF_PR=false;
          this.DESTROY();}
      
      if ("r".equals(name)) {
          npp=npp+1;
          OPEN();
          if (npp>=page) {
            r.setEnabled(false);
          } else {
            r.setEnabled(true);
          }
          if (npp==1) {
            l.setEnabled(false);
          } else {
            l.setEnabled(true);
          }
          
          F[0].setText(FF.STR(npp));
      }
      if ("l".equals(name)) {
          npp=npp-1;
          OPEN();
          if (npp==1) {
            l.setEnabled(false);
          } else {
            l.setEnabled(true);
          }
          if (npp>=page) {
            r.setEnabled(false);
          } else {
            r.setEnabled(true);
          }
          F[0].setText(FF.STR(npp));
      }
    }
    
}//окнчание класса
