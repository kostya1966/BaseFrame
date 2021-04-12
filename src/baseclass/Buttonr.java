package baseclass;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;

/**
 *Базовый класс кнопки
 * @author Kostya
 */
public  class Buttonr extends JButton {
    public int X1=getX(),X2=getX()+getWidth(),Y1=getY(),Y2=getY()+getHeight(); //координаты по углам
    public  Formr THISFORM   ; //ссылка на родительскую форму
    public  Screen THISSCREEN   ; //ссылка на ЭКРАН
    public boolean AUTOSIZE=false;
    public Buttonr THIS;
    public int width,height;
    public int ALTKEY=0; //НОМЕР КЛАВИШИ С ALT ДЛЯ ИМИТАЦИИ НАЖАТИЯ
    public int STATUS=0; // РАБЮЧЕЕ ПОЛЕ
    public int KOL=0; // РАБЮЧЕЕ ПОЛЕ
    public Color oldcolorB=getBackground();

    Buttonr() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public  void CLICK() {  THISFORM.CLICK_ALL(this.getName()); }   //левая кнопка или enter пробел
    //пока не используются
    public void CLICKR() {THISFORM.CLICKR_ALL(this.getName()); } //правая кнопка
    public void CLICKM() { } // средняя средняя
    public void CLICKDBL() {THISFORM.DBLCLICK_ALL(this.getName()); } // двойной клис
    public void CLICKRDBL() { } //правая кнопка двойной клик
    public void CLICKMDBL() { } // средняя средняя двойной клик
    public void MULTICLICK() {THISFORM.MULTICLICK_ALL(this.getName());} // множественный левый клик
     ////////////////////////////////////////////
    public Buttonr(String name, String title,int width, int height) {//иницилизация имени кнопки заголовка размеры
        super.setText(title);
        super.setName(name);
        this.width=width;
        this.height=height;
        this.THIS=this;
//        this.NAME=name;
//        super.setSize(w,h);        
        if (width==0){
            RESIZE();
            AUTOSIZE=true;             
        } else
        {
            super.setSize(width, height);                
        }
    
        
        super.addKeyListener(new ButtonrActionListener()); //определяет прослушиватель для кнопки описанный в классе ButtonrActionListener
        super.addMouseListener(new ButtonrActionListener());
        super.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
//                   System.out.print(evt.getPropertyName()+"\n");
            }
        });

        super.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent event) {
                //получает фкус
           //        System.out.println("Получил фокус\n");
                oldcolorB=getBackground();
                setBackground(Color.GRAY);
            }
            public void focusLost(FocusEvent event) {
                //теряет фкус
             //      System.out.println("Потерял фокус\n");
                setBackground(oldcolorB);
                
            }
            
});
        
    }

       public class ButtonrActionListener implements ActionListener,KeyListener,MouseListener { //класс для прослушивания событий клавиатуры 

        @Override
        public void actionPerformed(ActionEvent e) {
  //              System.out.print("actionPerformed "+e.getActionCommand());
   
        }
        @Override
        public void keyTyped(KeyEvent e) {//в сочетании shif ctrl ...
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void keyPressed(KeyEvent e) {
//            System.out.println("keyPressed "+e.getComponent().getName());                            
            THISFORM.KEYPRESS(e.getComponent(),e);
//            System.out.print("keyPressed "+e.getComponent().getName());                            
//            JOptionPane.showMessageDialog(null,"click "+e.getKeyCode(), "Внимание!!!",  0); 
            
        }

        @Override
        public void keyReleased(KeyEvent e) {//
//          System.out.print(" keyReleased:"+e.getExtendedKeyCode()+"\n");            
  
        }

        @Override
        public void mouseClicked(MouseEvent e) {//нажали отпустили
//            JOptionPane.showMessageDialog(null,"click "+e.getButton(), "Внимание!!!",  0); 
 //      System.out.print("    mouseClicked:"+e.getButton()+" "+e.getClickCount()+"\n");
//            if (e.getButton() == MouseEvent.BUTTON1 & e.getClickCount()==1)  {CLICK();} 
//            if (e.getButton() == MouseEvent.BUTTON1 & e.getClickCount()==2)  {CLICKDBL();} 
//            if (e.getButton() == MouseEvent.BUTTON2 & e.getClickCount()==1)  {CLICKM();} 
//            if (e.getButton() == MouseEvent.BUTTON2 & e.getClickCount()==2)  {CLICKMDBL();} 
//            if (e.getButton() == MouseEvent.BUTTON3 & e.getClickCount()==1)  {CLICKR();} 
//            if (e.getButton() == MouseEvent.BUTTON3 & e.getClickCount()==2)  {CLICKRDBL();} 
                            
            
        }

        @Override
        public void mousePressed(MouseEvent e) {//нажали
//       System.out.print("    mousePressed:"+e.getButton()+"\n");
        }

        @Override
        public void mouseReleased(MouseEvent e) { //отпустили
//            System.out.print("    mouseReleased:"+e.getButton()+"\n");
//            if   (THISFORM.B_EXIT!= null && THISFORM.B_EXIT.getName().equals(THIS.getName())) 
//            {THISFORM.VALIDON=false;}
            if (!isEnabled()) {return;} //если не доступна
            if (e.getButton() == MouseEvent.BUTTON1 & e.getClickCount()==1)  {CLICK();} 
            if (e.getButton() == MouseEvent.BUTTON1 & e.getClickCount()==2)  {CLICKDBL();} 
            if (e.getButton() == MouseEvent.BUTTON2 & e.getClickCount()==1)  {CLICKM();} 
            if (e.getButton() == MouseEvent.BUTTON2 & e.getClickCount()==2)  {CLICKMDBL();} 
            if (e.getButton() == MouseEvent.BUTTON3 & e.getClickCount()==1)  {CLICKR();} 
            if (e.getButton() == MouseEvent.BUTTON3 & e.getClickCount()==2)  {CLICKRDBL();} 
            if (e.getButton() == MouseEvent.BUTTON1 & e.getClickCount()>2)  {MULTICLICK();} 
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {//на объект
          if   (THISFORM.B_EXIT!= null && THISFORM.B_EXIT.getName().equals(THIS.getName())) 
      //      System.out.print("    mouseEntered:"+THIS.getName()+"\n");
            {THISFORM.VALIDON=false;}

        }

        @Override
        public void mouseExited(MouseEvent e) {//с объекта
//               System.out.print("   mouseExited:"+e.getButton()+"\n");
          if   (THISFORM.B_EXIT!= null && THISFORM.B_EXIT.getName().equals(THIS.getName())) 
            {THISFORM.VALIDON=true;}
            
        }        
        
    }
 public void RESIZE() {
   this.width=this.getFontMetrics(this.getFont()).stringWidth(this.getText())+30;
   this.height=this.getHeight();   
   if (this.height==0){
   this.height=this.getFontMetrics(this.getFont()).getHeight()+20;
   }
   this.setSize(this.width, this.height);                
   
 }   
public void SETALTKYE(int KEY) {
    this.ALTKEY=KEY;
    if (this.getText().indexOf(KEY+".")<0) {
     this.setText(KEY+"."+this.getText());
     this.setSize(this.getWidth()+10, this.getHeight());                
    }
    this.setToolTipText(this.getToolTipText()+" (ALT+"+KEY+")");
//    this.width=this.getFontMetrics(this.getFont()).stringWidth(this.getText()); 

}
  
 
}
