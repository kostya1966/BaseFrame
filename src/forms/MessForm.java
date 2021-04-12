package forms;

import baseclass.Formr;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import prg.FF;
import prg.P;
import prg.V;
//ФОРМА ДИАЛОГОВОГО ОКНА ВЫЗЫВАЕМАЯ В P.MESSF()
public class MessForm extends Formr {
   public Timer timer;
   public int sek=0; // текущее время жизни сообщение
       public MessForm() {

        super("NAME_FORMR_MESS", "ЗАГОЛОВОК", 300, 250); //Вызов конструктора от базового класса 
        setMaximizable(false);
        setIconifiable(false);
        INT1=0;   //СДВИГ В КООРДИНАТАХ
        INT2=0;
        timer1= new Timer( 1000 , new ActionListener() {
        @Override
           public void actionPerformed(ActionEvent e) {
        //     System.out.println( "MessForm " +FF.STR(sek));                              
               sek++;
               L[0].setText(FF.STR(sek));
               L[1].setText(FF.STR(INT2));
               //L[1].setText("12345");
               if (INT2>0 && INT2<=sek) { // если задан инвервал жизни формы и прошло больше или равно секунд
               DESTROY();  // закрываем окно
               }
           }
       } );
        
      timer1.start();
    }
    
    @Override
    public void DESCPROP() {
        SETRESIZABLE(0); //1-Признак фиксированного размера 0- не фиксированный
 //       SETMODAL(1);    //1-Модальная форма 0-не модальная
//        setClosable(false);

    }

    @Override
    public void KEYPRESS(Component obj, KeyEvent e) {
        super.KEYPRESS(obj, e);
        if (obj.getName().equals("T0")) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                CLICK_ALL("bok");
            }
        }
    }

    @Override
    public void LOAD_OBJ() {
        B[0] = P.addobjB(this, "B0", "B0     ", ""); //B[0].setSize(B[0].getHeight()-10,B[0].getWidth() );
        B[1] = P.addobjB(this, "B1", "B1     ", ""); //B[1].setSize(B[1].getHeight()-10,B[1].getWidth() );
        B[2] = P.addobjB(this, "B2", "B2     ", ""); //B[2].setSize(B[2].getHeight()-10,B[2].getWidth() );
        T[0] = P.addobjT(this, "T0", "", 250, 80);
        T[0].SETREADONLY(true);
        T[0].setBackground(Color.yellow);
        L[0]=P.addobjL(this,"L0", "  0");      
        L[1]=P.addobjL(this,"L1", "  0");      
        

    }

//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
    @Override
    public void LOC_ABOUT() {
       T[0].SCROLL.setBounds(1, 1, this.getWidth()-15, this.getHeight()-90);             
        locate(B[0], null, V.LOC_LEFT, 3, null, V.LOC_DOWN, 10);
        locate(B[1], null, V.LOC_CENTR, 3, null, V.LOC_DOWN, 10);
        locate(B[2], null, V.LOC_RIGHT, 3, null, V.LOC_DOWN, 10);
        locate(L[0], B[1], V.LOC_LEFT, 20, null, V.LOC_DOWN, 10);
        locate(L[1], B[1], V.LOC_RIGHT, 50, null, V.LOC_DOWN, 10);

    }

    
    @Override
    public void DESTROY() {
        timer1.stop(); // останавливаем таймер 
        super.DESTROY();
        V.PARAMOT = new String[1];
        V.PARAMOT[0] = "F";
    }

    @Override
    public void OPEN() {
     B[1].requestFocus() ;      

    }

    @Override
    public void CLICK_ALL(String name) {
        if ("B0".equals(name)) {
            DESTROY();
        }
        if ("B1".equals(name)) {
            DESTROY();
        }
        if ("B2".equals(name)) {
            DESTROY();
        }

    }
    
}
