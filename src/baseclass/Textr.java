package baseclass;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import prg.FF;
import prg.V;

/**
 * Базовый класс текстового поля
 *
 * @author Kostya
 */
public class Textr extends javax.swing.JTextArea {


    public int X1 = getX(), X2 = getX() + getWidth(), Y1 = getY(), Y2 = getY() + getHeight(); //координаты по углам        public Formr THISFORM   ;
    public String TYPE = V.TYPE_CHAR;
    public boolean READONLY = false;//только чтение
    public boolean SQLUPDATE = true;//обновление по запросу
    public boolean THISKEY = false;//поле ключ для выражения WHERE
    public Formr THISFORM; //ссылка на родительскую форму
    public JScrollPane SCROLL;//

    //СЭТЕРЫ 
    public void SETREADONLY(boolean tf) {
        setOpaque(false);
        READONLY = tf;
        if (READONLY == false) {
            setEditable(true);
            setBackground(V.COLORB_READONLYOFF);

        }
        if (READONLY == true) {
            setEditable(false);
            setBackground(V.COLORB_READONLYON);
        }
    }

    public void SETVALUE(Object val) {
        String str;
        if (val == null) {
            str = "";
        } else {
            str = val.toString();
        }
        setText(str);

        setCaretPosition(0);

    }

    public void WRITE_MESSAGE(String mess) {
        if (getText().equals("")) {
            append(mess);
        } else {
            append("\n" + mess);
        }
        setCaretPosition(getText().length());

    }
//    public JScrollPane SCROLL;//

    public Textr(final Formr THISFORM, String name, String title, int width, int height) {//иницилизация имени поля и заголовка и размеров
        super.setText(title);
        super.setName(name);
        super.setSize(width, height);
        this.setEditable(true);
        setWrapStyleWord(true);
        setLineWrap(true);
        
        SCROLL = new javax.swing.JScrollPane(this); // создаем скролингш для таблицы
        SCROLL.setSize(width, height); // координаты по скролингу а не по полю
        SCROLL.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        SCROLL.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        SCROLL.setAutoscrolls(true);
        SCROLL.setViewportView(this); //не знаю 
        THISFORM.add(SCROLL); //добавляем на панель формы           
    //    SCROLL.setBounds(1, 1, width, height); // координаты по скролингу а не по таблице

//        SCROLL.setLocation(10,200);
//        this.setAlignmentX(LEFT_ALIGNMENT);
//        this.setAlignmentY(LEFT_ALIGNMENT);
//        this.setHorizontalAlignment(JTextField.LEFT);
        setFont(new Font("Arial", 1, 14));
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
//            System.out.print(" key:"+e.getExtendedKeyCode()+"\n"); 
                if (e.getKeyCode() != e.VK_ENTER) {
                    THISFORM.KEYPRESS(e.getComponent(), e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        DBCLICK();
                    } catch (NullPointerException wxcc) {
                    }
                }
            }

        });

    }

    public Textr(String name, String title, int width, int height) {//иницилизация имени поля и заголовка и размеров
        super.setText(title);
        super.setName(name);
        super.setSize(width, height);
        this.setEditable(true);
        setWrapStyleWord(true);
        setLineWrap(true);
        setFont(new Font("Arial", 1, 14));
    }

    @Override
    public void setLocation(int x, int y) {
        if (SCROLL != null && x != 0 && y != -18) {
            SCROLL.setLocation(x, y);
        } else {
            super.setLocation(x, y);
        }
    }

//    public int getWidth() {
//        if (SCROLL != null) {
//            return SCROLL.getWidth();
//        } else {
//            return super.getWidth();
//        }
//    }
//
//    public int getHeight() {
//        if (SCROLL != null) {
//            return SCROLL.getHeight();
//        } else {
//            return super.getHeight();
//        }
//    }
//
    public Rectangle getBounds() {
        if (SCROLL != null) {
            return SCROLL.getBounds();
        } else {
            return super.getBounds();
        }
    }

    public Object GETVALUE() {
//        String str = new String(getPassword());
        String str = getText();
        if (TYPE == V.TYPE_NUMERIC) {
            if (FF.EMPTY(str)) {
                str = "0";
            }
            if (".".equals(str)) {
                str = "0.0";
            }

            if (FF.AT(".", str) > 0) {
                return (Object) Double.parseDouble(str);
            } else {
                return (Object) Integer.parseInt(str);
            }

        }
        if (TYPE == V.TYPE_DATE) {
//                SimpleDateFormat format = new SimpleDateFormat(V.MASK_DATE,Locale.GERMANY);
            try {
                if ("  .  .    ".equals(str)) {
                    str = "01.01.1900";
                }
                Object val = (Object) V.ddmmyyyy.parse(str);
                return val;
            } catch (ParseException ex) {
//                Logger.getLogger(Fieldr.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

        }
        if (TYPE == V.TYPE_DATETIME) {
//                SimpleDateFormat format = new SimpleDateFormat(V.MASK_DATETIME);
            try {
                Object val = (Object) V.ddmmyyyyhhmm.parse(str);
                return val;
            } catch (ParseException ex) {
//                Logger.getLogger(Fieldr.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

        }

        return (Object) str;
    }

    public void DBCLICK() {
        THISFORM.DBLCLICK_ALL(getName());
    }

}
