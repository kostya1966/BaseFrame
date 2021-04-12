package baseclass;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

public class JRadioButtons extends Component{
public  String CAPTION;
public  Formr THISFORM   ; //ссылка на родительскую форму
public Box BOX;
public int VALUE;
public int COUNT;
public int width,height,size;
public ButtonGroup BG;
public JRadioButton[] RB;
public boolean SQLUPDATE = true;//обновление по запросу
public boolean THISKEY = false;//ключевое 
public int SEL=0;
    public void CLICK() {
        THISFORM.CLICK_ALL(getName());
    }

    public JRadioButtons(Formr thisform,String[] str) {
        this(thisform,str,1,"", false);
    }
    public JRadioButtons(Formr thisform,String[] str,int select) {
        this(thisform,str,1,"", false);
    }
    public JRadioButtons(Formr thisform,String[] str,int select, boolean horizontal) {
        this(thisform,str,1,"", horizontal);
    }
        
    
    public JRadioButtons(Formr thisform,String[] str,int select,String caption, boolean horizontal) {
        this.CAPTION=caption;
        BOX = Box.createVerticalBox();  //создаем панель
        if (horizontal)
            BOX = Box.createHorizontalBox();  //создаем панель
            
       BG = new ButtonGroup(); // создаем группу взаимного исключения       
        COUNT=str.length;
        RB= new JRadioButton[COUNT];
        width=0; height=0;
        for(int i=0;i<COUNT;i++){
            RB[i] =new JRadioButton(str[i]) ;
            
            if (horizontal) //если горизонтальное, то ширина - сумма ширин всех кнопок, высота - высота самой высокой...
            {
                height = RB[i].getFontMetrics(RB[i].getFont()).getHeight() + 20;
                
                width = width + RB[i].getFontMetrics(RB[i].getFont()).stringWidth(RB[i].getText()) + 35;
                
            } else { //если вертикальная, то ширина - длина самой длинной кнопки, высота - сумма высоты всех
                size = RB[i].getFontMetrics(RB[i].getFont()).stringWidth(RB[i].getText()) + 50;
                if (size > width) {
                    width = size;
                }
                height = height + RB[i].getFontMetrics(RB[i].getFont()).getHeight() + 15;
            }
            
         RB[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CLICK();
            }
        });
                 RB[i].addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
            //      System.out.println(" keyTyped");            
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (KeyEvent.VK_ENTER == e.getKeyCode() ) { //ПО ENTER переход к следующему элементу формы
                      //System.out.println("2");            
                   //    BOX.requestFocus();
                      //  BOX.transferFocus();
                      RB[COUNT-1].transferFocus();
                          
                    }
                    if (KeyEvent.VK_RIGHT == e.getKeyCode() ) {
                      System.out.println("R");            
                      SEL=ISSELECT();
                      if (SEL+1<COUNT) {  //ЕСЛИ НЕ ПОСЛЕДНИЙ
                      RB[SEL+1].requestFocus();
                      } 
                         }
                    if (KeyEvent.VK_LEFT == e.getKeyCode() ) {
                      System.out.println("L");            
                      SEL=ISSELECT();
                      if (SEL>0) {  //ЕСЛИ НЕ ПЕРВЫЙ
                      RB[SEL-1].requestFocus();
                      } 
                    }
                    
                }

                @Override
                public void keyReleased(KeyEvent e) {
              //        System.out.println("3");            
                }

                
            }  );
          
                      BG.add(RB[i]); // сделали радиокнопки взаимоисключающими            
                      BOX.add(RB[i]); //добавляем в список
        }
           SETVALUE(select);
           BOX.setSize(width, height);
           BOX.setOpaque(true);
           BOX.setBorder(new TitledBorder(CAPTION));                
           thisform.add(BOX);
           BOX.setVisible(true);
           
    }

    public void setLocation(int x, int y) {
        BOX.setLocation(x, y);
    }

    public int getWidth() {
        return this.BOX.getWidth();
    }

    public int getHeight() {
        return this.BOX.getHeight();
    }

    public Rectangle getBounds() {
        return this.BOX.getBounds();
    }

    public void SETVALUE(int val) {
        if (val<1 || val>COUNT) {
            val=1;
        }
        RB[val - 1].setSelected(true);
        VALUE = val;
    }
    /**
     * Определяет номер активной опции от 0
     * @return 
     */
    public int ISSELECT() {
        for (int i = 0; i < COUNT; i++) {
            if (RB[i].isSelected()) {
                return i ;
            }
        }
        return 0;
    }

    
    /**
     *узнать номер выбранного элемента (начинается с 1)
     * @return номер выбранного элемента
     */
    public int GETVALUE() {
        for (int i = 0; i < COUNT; i++) {
            if (RB[i].isSelected()) {
                return i + 1;
            }
        }
        return 0;
    }
/**
 * Возвращает название активной опции
 * @return 
 */    
    public String GETVALUEO() {
        return RB[GETVALUE()-1].getText();
    }
    
    public String GETNAME()
    {
        return getName();
    }

     public void SETSQLUPDATE(boolean tf) {
        SQLUPDATE = tf;
    }
   
}
