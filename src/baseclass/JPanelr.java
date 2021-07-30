package baseclass;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import prg.V;

/**
* Базовый класс панели
*
* @author Kostya
*/
public class JPanelr extends javax.swing.JPanel {


public int X1 = getX(), X2 = getX() + getWidth(), Y1 = getY(), Y2 = getY() + getHeight(); //координаты по углам public Formr THISFORM ;
public String TYPE = V.TYPE_CHAR;
public boolean READONLY = false;//только чтение
public boolean SQLUPDATE = true;//обновление по запросу
public boolean THISKEY = false;//поле ключ для выражения WHERE
public Formr THISFORM; //ссылка на родительскую форму
public JScrollPane SCROLL;//



public JPanelr(final Formr THISFORM, String name, int width, int height) {//иницилизация имени поля и заголовка и размеров
super.setName(name);
super.setSize(width, height);
super.setBackground(Color.white);
SCROLL = new javax.swing.JScrollPane(this); // создаем скролингш
SCROLL.setSize(width, height); // координаты по скролингу
SCROLL.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
SCROLL.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
 SCROLL.setAutoscrolls(true);
SCROLL.setViewportView(this); //не знаю
 THISFORM.add(SCROLL); //добавляем на панель формы
// SCROLL.setBounds(1, 1, width, height); // координаты по скролингу а не по таблице

// SCROLL.setLocation(10,200);
// this.setAlignmentX(LEFT_ALIGNMENT);
// this.setAlignmentY(LEFT_ALIGNMENT);
// this.setHorizontalAlignment(JTextField.LEFT);

/*
setFont(new Font("Arial", 1, 14));
addKeyListener(new KeyListener() {
@Override
public void keyTyped(KeyEvent e) {
}

@Override
public void keyPressed(KeyEvent e) {
// System.out.print(" key:"+e.getExtendedKeyCode()+"\n");
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


@Override
public void setLocation(int x, int y) {
if (SCROLL != null && x != 0 && y != -18) {
SCROLL.setLocation(x, y);
} else {
super.setLocation(x, y);
}
}

public Rectangle getBounds() {
if (SCROLL != null) {
return SCROLL.getBounds();
} else {
return super.getBounds();
}
}


public void DBCLICK() {
THISFORM.DBLCLICK_ALL(getName());
}
*/
}
}