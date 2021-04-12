package baseclass;

import prg.P;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ComboBoxr extends Component {

    public Formr THISFORM; //ссылка на родительскую форму
    public int width, height, size;
    public JComboBox<String> CB;
    public boolean SQLUPDATE = true;//обновление по запросу
    public boolean THISKEY = false;//ключевое 

    public void CLICK() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                THISFORM.CLICK_ALL(CB.getName());
                return null;
            }
        }.execute();
    }

    public ComboBoxr(Formr thisform, String name, String[] str) {
        width = 0;
        height = 0;
        size = 0;
        CB = new JComboBox<>(str);
        for (int i = 0; i < str.length; i++) {
            if (size < CB.getFontMetrics(CB.getFont()).stringWidth(CB.getItemAt(i))) {
                size = CB.getFontMetrics(CB.getFont()).stringWidth(CB.getItemAt(i));
            }
        }
        size += 50;
        if (size > width) {
            width = size;
        }
        height = height + CB.getFontMetrics(CB.getFont()).getHeight() + 15;
        CB.setSize(width, height);
        CB.setName(name);
        thisform.add(CB);

        CB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CLICK();
            }
        });

        CB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BasicComboPopup popup = (BasicComboPopup) CB.getAccessibleContext().getAccessibleChild(0);
                P.STARTMODAL(popup);
            }
        });
    }

    public ComboBoxr(Formr thisform, String name, String[] str, int width, int height) {
        CB = new JComboBox<>(str);
        CB.setSize(width, height);
        CB.setName(name);
        thisform.add(CB);

        CB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CLICK();
            }
        });

        CB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BasicComboPopup popup = (BasicComboPopup) CB.getAccessibleContext().getAccessibleChild(0);
                P.STARTMODAL(popup);
            }
        });


    }

    public void setLocation(int x, int y) {
        CB.setLocation(x, y);
    }

    public int getWidth() {
        return this.CB.getWidth();
    }

    public int getHeight() {
        return this.CB.getHeight();
    }

    public Rectangle getBounds() {
        return this.CB.getBounds();
    }

    public void CLEAR() {
        CB.removeAllItems();
    }

    public void REMOVE_ITEM_AT(int index) {
        CB.removeItemAt(index);
    }

    public void SETVALUES(String[] str) {//заполнение с размерами самой длинной строки
        CLEAR();
        width = 0;
        height = 0;
        size = 0;
        for (int i = 0; i < str.length; i++) {
            CB.addItem(str[i]);
            if (size < CB.getFontMetrics(CB.getFont()).stringWidth(CB.getItemAt(i))) {
                size = CB.getFontMetrics(CB.getFont()).stringWidth(CB.getItemAt(i));
            }
        }
        size += 50;
        if (size > width) {
            width = size;
        }
        height = height + CB.getFontMetrics(CB.getFont()).getHeight() + 15;
        CB.setSize(width, height);
    }

    public void SETVALUES(String[] str, boolean sizeFlag) {//заполнение с переданными размерами
        CLEAR();
        for (int i = 0; i < str.length; i++) {
            CB.addItem(str[i]);
        }
    }

    public int SETVALUE(String str) {
        for (int i = 0; i < CB.getItemCount(); i++) {
            if (CB.getItemAt(i).equals(str)) {
                CB.setSelectedIndex(i);
                return i;
            }
        }
        CB.setSelectedIndex(0);
        return 1;
    }

    public String GETSELECTEDVALUE() {
        return CB.getItemAt(CB.getSelectedIndex());
    }

    public String GETVALUE(int num) {
        return CB.getItemAt(num);
    }

    public int GETSELECTEDINDEX() {
        return CB.getSelectedIndex();
    }

    public String GETNAME() {
        return CB.getName();
    }

    @Override
    public void setEnabled(boolean b) {
        CB.setEnabled(b);
    }

    @Override
    public boolean isEnabled() {
        return CB.isEnabled();
    }

    @Override
    public void setVisible(boolean b) {
        CB.setVisible(b);
    }
}
