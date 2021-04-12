package baseclass;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JCheckBox;
import prg.V;

public class CheckBoxr extends JCheckBox {

    public Formr THISFORM; //ссылка на родительскую форму
    public int width, height, size;
    public boolean SQLUPDATE = true;//обновление по запросу
    public boolean THISKEY = false;//ключевое 
    public boolean READONLY = false;//только чтение
    public  void CLICK() {THISFORM.CLICK_ALL(this.getName()); } 

    public CheckBoxr(Formr thisform, String name, String title) {
        super.setText(title);
        width = 0;
        height = 0;
        size = this.getFontMetrics(this.getFont()).stringWidth(this.getText()) + 50;
        if (size > width) {
            width = size;
        }
        height = height + this.getFontMetrics(this.getFont()).getHeight() + 15;  //RKV изменил 25 на 15
        this.setSize(width, height);
        this.setName(name);
//        thisform.add(CH);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                    THISFORM.KEYPRESS(e.getComponent(), e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        if (READONLY == true) {
                        setSelected(!isSelected());
        }
                CLICK();
            }
            
        });
    }
    @Override
    public void setFont(Font f) {
        super.setFont(f); //To change body of generated methods, choose Tools | Templates.
        this.setSize(this.getFontMetrics(this.getFont()).stringWidth(this.getText()) + 50, this.getFontMetrics(this.getFont()).getHeight() + 15)        ;
    }
 
    public void SETREADONLY(boolean tf) {
        READONLY = tf;
        if (READONLY == false) {
          //  setEditable(true);
            setBackground(V.COLORB_READONLYOFF);
        }
        if (READONLY == true) {
           // setEditable(false);
            setBackground(V.COLORB_READONLYON);
        }
    }
    
/*    public void setLocation(int x, int y) {
        super.setLocation(x, y);
    }

    public int getWidth() {
        return this.getWidth();
    }

    @Override
    public int getHeight() {
        return this.getHeight();
    }

 //   @Override
  //  public Rectangle getBounds() {
  //      return this.getBounds();
  //  }

  */
   public void SETVALUE(boolean select) {
        this.setSelected(select);
    }

    public boolean GETVALUE() {
        if (this.isSelected()) {
            return true;
        }
        return false;
    }

    public String GETNAME()
    {
        return getName();
    }
    
    public void SETTHISKEY(boolean tf) {
        THISKEY = tf;
    }
    
}
