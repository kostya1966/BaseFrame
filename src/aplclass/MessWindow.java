package aplclass;

import baseclass.Labelr;
//import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener; // ошибка в некоторых компах что нет библиотеки
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import prg.V;

public class MessWindow extends Thread {
 public Labelr    LMESS;//,LBORD;
 public Timer timer;
    public MessWindow(JFrame obj){
       LMESS = new Labelr("LMESS", "");//
//       LBORD = new Labelr("LBORD", "");//03112017 KAA убрали LBORD так как из-за него некорректна шла отрисовка программы при использовании таймеров
       
       LMESS.setSize(LMESS.getWidth(), 50);  ;
       LMESS.setOpaque(true);
       LMESS.setBackground(Color.WHITE);
       LMESS.setForeground(Color.BLACK);
       LMESS.setHorizontalAlignment(SwingConstants.CENTER)       ;
       LMESS.setBorder(new LineBorder(Color.BLACK, 2, false));
//       LBORD.setSize(LMESS.getWidth(), 50);  ;
//       LBORD.setOpaque(true);
//       LBORD.setBackground(Color.GRAY);
      obj.add(LMESS, V.LAYER_INF);       
//      obj.add(LBORD, V.LAYER_INF);               
      timer= new Timer( 3000 , new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
               setVisible(false);
     //          V.CONN_OUT=true; //если цикл до нажатия клавиши
               
               //System.out.println( "WOW!" );               
           }
       } );
      timer.setRepeats(false);
      
              LMESS.addMouseListener(new MouseListener() {

           @Override
           public void mouseClicked(MouseEvent e) {
               setVisible(false);
               V.CONN_OUT=true; //если цикл до нажатия клавиши               
           }

           @Override
           public void mousePressed(MouseEvent e) {
               setVisible(false);
               V.CONN_OUT=true; //если цикл до нажатия клавиши               
           }

           @Override
           public void mouseReleased(MouseEvent e) {
               setVisible(false);
               V.CONN_OUT=true; //если цикл до нажатия клавиши               
           }

           @Override
           public void mouseEntered(MouseEvent e) {
               setVisible(false);
               V.CONN_OUT=true; //если цикл до нажатия клавиши               
           }

           @Override
           public void mouseExited(MouseEvent e) {
               setVisible(false);
               V.CONN_OUT=true; //если цикл до нажатия клавиши               
           }

            } ) ;


        
    }

public int getWidth() {
    return this.LMESS.getWidth()+10;
}

public void setLocation(int x,int y) {
    this.LMESS.setLocation(x, y);
//    this.LBORD.setLocation(x+5, y+5);
    
}

public void setVisible(boolean yn){
    this.LMESS.setVisible(yn);
//    this.LBORD.setVisible(yn);
    
}

public void setText(String str){
//    str="<html></b> <li><font size = +2>"+str;
    this.LMESS.setText(str);
//    this.LBORD.setSize(this.LMESS.getWidth(), this.LMESS.getHeight());
}

public void REFRESH(){
 this.LMESS.paintImmediately(new Rectangle(this.LMESS.getSize()));           
// this.LBORD.paintImmediately(new Rectangle(this.LBORD.getSize()));           
//V._SCREEN.getContentPane().P
 
}




}
