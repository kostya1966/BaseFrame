/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import baseclass.Formr;
import baseclass.Screen;
import java.awt.Cursor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JComponent;
import prg.V;

public class InfoPanelAdapter extends ComponentAdapter implements MouseListener, MouseMotionListener {

    JComponent componentResize;
    Screen owner;
    int freeSize;

    public InfoPanelAdapter(JComponent _componentResize, Screen _owner) {
        componentResize = _componentResize;
        owner = _owner;
    }
    private boolean startResize = false;
    private int resizeYstart = 0;
    private int resizeYend = 0;
    private int curHeight = 0;
    private static int HEIGH_OF_BORDER = 5;

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (componentResize.getCursor().getType() == Cursor.N_RESIZE_CURSOR) {
            curHeight = componentResize.getHeight();
            startResize = true;
            resizeYstart = e.getY();
            resizeYend = e.getY();
        }
    }

    public void mouseReleased(MouseEvent e) {
        startResize = false;
        resizeYstart = 0;
        resizeYend = 0;
        freeSize = 2 * owner.getHeight() / 3;

        if (componentResize.getHeight() < 60) {
            componentResize.setLocation(0, owner.getHeight() - 60 - 85);
            componentResize.setSize(componentResize.getWidth(), 60);
        }
        if (componentResize.getHeight() > owner.getHeight() - freeSize) {
            componentResize.setLocation(0, freeSize - 85);
            componentResize.setSize(componentResize.getWidth(), owner.getHeight() - freeSize);
        }

        Iterator entries = V.MAPFORMS.entrySet().iterator();
        while (entries.hasNext()) {
            Entry thisEntry = (Entry) entries.next();
            Formr form = (Formr) thisEntry.getValue();
            if (form.getHeight() >= V._SCREEN.getHeight() - 115 - V.INFO_TXT_SCROLL.getHeight()) {
                form.setBounds(form.getX(), form.getY(), form.getWidth(), V._SCREEN.getHeight() - 118 - V.INFO_TXT_SCROLL.getHeight());
            }
        }
        
        if (V.ACTIVEFORM.isMaximum()){
            V.ACTIVEFORM.setBounds(V.ACTIVEFORM.getX(), 30, V.ACTIVEFORM.getWidth(), V._SCREEN.getHeight() - 115 - V.INFO_TXT_SCROLL.getHeight());
        }

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        owner.validate();
    }

    public void mouseDragged(MouseEvent e) {
        freeSize = 2 * owner.getHeight() / 3;
        if (startResize && componentResize.getHeight() > 60 && (owner.getHeight() - componentResize.getHeight()) > freeSize) {
            resizeYend = e.getY();
            curHeight = componentResize.getHeight();
            componentResize.setLocation(0, owner.getHeight() - curHeight - 85 + (resizeYend - resizeYstart));
            componentResize.setSize(componentResize.getWidth(), curHeight - (resizeYend - resizeYstart));
        } else {
            if ((componentResize.getHeight() <= 60 && resizeYend > e.getY())
                    || ((owner.getHeight() - componentResize.getHeight()) <= freeSize && resizeYend < e.getY())) {
                resizeYend = e.getY();
                curHeight = componentResize.getHeight();
                componentResize.setLocation(0, owner.getHeight() - curHeight - 85 + (resizeYend - resizeYstart));
                componentResize.setSize(componentResize.getWidth(), curHeight - (resizeYend - resizeYstart));
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (startResize || e.getY() < HEIGH_OF_BORDER && e.getY() >= 0) {
            componentResize.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        } else {
            componentResize.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}