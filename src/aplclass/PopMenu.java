package aplclass;

import baseclass.Formr;
import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.MenuComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import prg.FF;
import prg.P;
import prg.V;

public class PopMenu extends JPopupMenu {

    JMenuItem item[];
    int countItem = 0;
    JMenuItem it;
    int CountMenu = 0;
    public int SelMenu = 0;
    public Formr THISFORM;

    public PopMenu(String str[], Component comp,Font font) {
        countItem = str.length;
        if (countItem != 0) {
            for (int i = 0; i < str.length; i++) {
                it = null;
                boolean enable=true;
                if ("<>".equals(FF.SUBSTR(str[i],1,2))   ) //если пункт меню нгачинается с <> то не доступен
                {enable=false;
                str[i]=FF.SUBSTR(str[i],3);
                }
                it = new JMenuItem(str[i]);
                it.setEnabled(enable);
                it.setBorderPainted(true);
                it.setName(FF.STR(i + 1));  
                it.setFont(font);;
                //it.setName(Integer.toHexString(i + 1));
                //final String y=Integer.toHexString(i+1);
                it.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ONPAD(e.getSource());
                        ;

                    }
                });
                this.add(it);
                this.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
            this.setBorder(new LineBorder(Color.BLACK, 2, true));
            this.setSelected(this.getComponent(1));
            if (comp==null ) {
              this.show(V._SCREEN, (int) (V._SCREEN.getWidth()/2-this.getWidth()/2 ), (int) (V._SCREEN.getHeight()/2-this.getHeight()/2));
             return;   
            }

            if (comp!=null && !comp.getClass().getName().equals("baseclass.Gridr")) {
                this.show(comp, comp.getWidth() / 2, comp.getHeight());
            } else {
                this.show(V.ACTIVEFORM, (int) (P.MOUSE_XY().getX() - V._SCREEN.getX() - V.ACTIVEFORM.getX() - 10), (int) (P.MOUSE_XY().getY() - V._SCREEN.getY() - V.ACTIVEFORM.getY() - 55));
            }

        }
    }

    public void ONPAD(Object obj) {
        JMenuItem Pad = (JMenuItem) obj;
        String str = Pad.getName();
        SelMenu = FF.VAL(str);
//     this.setVisible(false);
    }

    public synchronized void STARTMODAL() {//метод не позволяющий выполнение действий вне формы

        try {
            if (SwingUtilities.isEventDispatchThread()) {
                EventQueue theQueue =
                        getToolkit().getSystemEventQueue();
                while (isVisible()) {
                    AWTEvent event = theQueue.getNextEvent();
                    Object source = event.getSource();
                    boolean dispatch = true;

                    if (event instanceof MouseEvent) {
                        MouseEvent e = (MouseEvent) event;
                        MouseEvent m =
                                SwingUtilities.convertMouseEvent((Component) e.getSource(), e, this);
                        if (!this.contains(m.getPoint())
                                && e.getID() != MouseEvent.MOUSE_DRAGGED) {
                            dispatch = false;
                        }
                    }

                    if (dispatch) {
                        if (event instanceof ActiveEvent) {
                            ((ActiveEvent) event).dispatch();
                        } else if (source instanceof Component) {
                            ((Component) source).dispatchEvent(
                                    event);
                        } else if (source instanceof MenuComponent) {
                            ((MenuComponent) source).dispatchEvent(
                                    event);
                        } else {
                            System.err.println(
                                    "Unable to dispatch: " + event);
                        }
                    }
                }
            } else {
                while (isVisible()) {
                    wait();
                }
            }
        } catch (InterruptedException ignored) {
        }

    }

    private synchronized void STOPMODAL() {
        notifyAll();
    }
}
