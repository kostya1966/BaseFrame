package aplclass;

import baseclass.Labelr;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import prg.V;

public class MessCenterWindow {

    public Labelr LMESS, LBORD;

    public MessCenterWindow(JFrame obj) {
        LMESS = new Labelr("LMESS", "");//
        LBORD = new Labelr("LBORD", "");//

        LMESS.setFont(new Font(LMESS.getFont().getName(), Font.PLAIN, 30));
        LMESS.setSize(LMESS.getWidth(), 200);;
        LMESS.setOpaque(true);
        LMESS.setBackground(Color.WHITE);
        LMESS.setForeground(Color.BLACK);
        LMESS.setHorizontalAlignment(SwingConstants.CENTER);
        LMESS.setBorder(new LineBorder(Color.BLACK, 2, false));
        LBORD.setSize(LMESS.getWidth(), 200);
        LBORD.setOpaque(true);
        LBORD.setBackground(Color.GRAY);
        obj.add(LMESS, V.LAYER_INF);
        obj.add(LBORD, V.LAYER_INF);

    }

    public int getWidth() {
        return this.LMESS.getWidth() + 10;
    }

    public void setLocation(int x, int y) {
        this.LMESS.setLocation(x, y);
        this.LBORD.setLocation(x + 5, y + 5);

    }

    public void setVisible(boolean yn) {
        this.LMESS.setVisible(yn);
        this.LBORD.setVisible(yn);

    }

    public void setText(String str) {
//    str="<html></b> <li><font size = +2>"+str;
        this.LMESS.setText("<html><center>" + str + "</center></html>");
        ArrayList<String> arrS = new ArrayList<>(Arrays.asList(str.split("<br>")));
        int len = 0;
        for (int i = 0; i < arrS.size(); i++) {
            if (len < arrS.get(i).length()) {
                len = arrS.get(i).length();
            }
        }

//        this.LBORD.setSize(this.LMESS.getWidth(), this.LMESS.getHeight());
        this.LMESS.setSize(11 * len, this.LMESS.getHeight());
        this.LBORD.setSize(11 * len, this.LMESS.getHeight());
        LMESS.setForeground(new Color(0, 0, 0, V.LOC_BORDER));
    }

    public void REFRESH() {
        this.LMESS.paintImmediately(new Rectangle(this.LMESS.getSize()));
        this.LBORD.paintImmediately(new Rectangle(this.LBORD.getSize()));
//V._SCREEN.getContentPane().P

    }

}
