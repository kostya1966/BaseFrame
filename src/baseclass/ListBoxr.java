package baseclass;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListBoxr extends Component {
    
    public Formr THISFORM; //ссылка на родительскую форму
    public int width, height, size;
    public JList list;
    public JScrollPane scrollPane;
    public boolean SQLUPDATE = true;//обновление по запросу
    public boolean THISKEY = false;//ключевое 

    public void CLICK() {
        THISFORM.CLICK_ALL(scrollPane.getName());
    }
    
    public void PRESS(int key) {
        THISFORM.KEYPRESS(list, key);
    }
    
    public ListBoxr(Formr thisform, String name, String[] str, int count, int width) {
        scrollPane = new JScrollPane();
        height = 0;
        size = 0;
        list = new JList(str);
        for (int i = 0; i < str.length; i++) {
            if (size < list.getFontMetrics(list.getFont()).stringWidth(list.getModel().getElementAt(i).toString())) {
                size = list.getFontMetrics(list.getFont()).stringWidth(list.getModel().getElementAt(i).toString());
            }
        }
        size += 50;
        height = height + (list.getFontMetrics(list.getFont()).getHeight()) * count + 20;
        scrollPane.setSize(width, height);
        scrollPane.setName(name);
        list.setSize(width, height);
        list.setName(name);
        list.setSelectionBackground(Color.gray);
        list.setFixedCellHeight(50);
        list.setFont(new Font("Arial", Font.BOLD, 45));
        scrollPane.setViewportView(list);
        thisform.add(scrollPane);
        
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                CLICK();
            }
        });
        
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                CLICK();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    
    public ListBoxr(Formr thisform, String name, String[] str, int count) {
        scrollPane = new JScrollPane();
        width = 0;
        height = 0;
        size = 0;
        list = new JList(str);
        for (int i = 0; i < str.length; i++) {
            if (size < list.getFontMetrics(list.getFont()).stringWidth(list.getModel().getElementAt(i).toString())) {
                size = list.getFontMetrics(list.getFont()).stringWidth(list.getModel().getElementAt(i).toString());
            }
        }
        size += 50;
        if (size > width) {
            width = size;
        }
        height = height + (list.getFontMetrics(list.getFont()).getHeight()) * count + 20;
        scrollPane.setSize(width, height);
        scrollPane.setName(name);
        list.setSize(width, height);
        list.setName(name);
        list.setSelectionBackground(Color.green);
        scrollPane.setViewportView(list);
        thisform.add(scrollPane);
        
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                CLICK();
            }
        });
        
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                CLICK();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    
    public ListBoxr(Formr thisform, String name, String[] str, int count, boolean flag) {
        scrollPane = new JScrollPane();
        width = 0;
        height = 0;
        size = 0;
        list = new JList(str);
        for (int i = 0; i < str.length; i++) {
            if (size < list.getFontMetrics(list.getFont()).stringWidth(list.getModel().getElementAt(i).toString())) {
                size = list.getFontMetrics(list.getFont()).stringWidth(list.getModel().getElementAt(i).toString());
            }
        }
        size += 50;
        if (size > width) {
            width = size;
        }
        height = height + (list.getFontMetrics(list.getFont()).getHeight()) * count + 20;
        scrollPane.setSize(width, height);
        scrollPane.setName(name);
        list.setSize(width, height);
        list.setName(name);
        list.setSelectionBackground(Color.green);
        scrollPane.setViewportView(list);
        thisform.add(scrollPane);
        
        scrollPane.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                PRESS(e.getKeyCode());
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
//                PRESS(e.getKeyCode());
            }
        });
        
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CLICK();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    
    public void setLocation(int x, int y) {
        scrollPane.setLocation(x, y);
    }
    
    public int getWidth() {
        return this.scrollPane.getWidth();
    }
    
    public int getHeight() {
        return this.scrollPane.getHeight();
    }
    
    public Rectangle getBounds() {
        return this.scrollPane.getBounds();
    }
    
    public void SET_INDEX(int ind) {
        list.setSelectedIndex(ind);
        list.ensureIndexIsVisible(ind);
    }

    public void SETTHISKEY(boolean tf) {
        THISKEY = tf;
    }
    
    public int SETVALUE(String str) {
        for (int i = 0; i < list.getModel().getSize(); i++) {
            if (list.getModel().getElementAt(i).equals(str)) {
                list.setSelectedIndex(i);
                list.ensureIndexIsVisible(i);
                return i;
            }
        }
        list.ensureIndexIsVisible(0);
        list.setSelectedIndex(0);
        return 1;
    }
    
    public int GETINDEX() {
        return list.getSelectedIndex();
    }

    public String GETNAME() {
        return list.getName();
    }
    
    public String GETVALUE() {
        return (String) list.getModel().getElementAt(list.getSelectedIndex());
//        return list.getSelectedIndex();
    }
    
    public String GETVALUE_BY_INDEX(int ind) {
        return (String) list.getModel().getElementAt(ind);
//        return list.getSelectedIndex();
    }
    
    public int GETCOUNT() {
        return list.getModel().getSize();
    }
    
    public void SET_LIST_DATA(String[] str) {
        list.setListData(str);
    }
}
