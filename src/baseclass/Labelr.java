/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclass;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Базовый класс метки
 * 
 * @author Kostya
 */
public class Labelr extends JLabel {

    private static final long serialVersionUID = 1L;

    public int X1 = getX(), X2 = getX() + getWidth(), Y1 = getY(), Y2 = getY() + getHeight(); // координаты по углам
                                                                                              // public Formr THISFORM;
    public int width, height;
    public Formr THISFORM; // ссылка на родительскую форму
    public boolean AUTOSIZE = false;

    public Labelr(String name, String title) {// иницилизация имени заголовка размеров
        this(name, title, 0, 0);
    }

    public Labelr(String name, String title, int width, int height) {// иницилизация имени заголовка размеров
        this.width = width;
        this.height = height;
        super.setText(title);
        super.setName(name);
        if (width == 0) {
            RESIZE();
            AUTOSIZE = true;
        } else {
            super.setSize(width, height);
            X1 = getX(); X2 = getX() + getWidth(); Y1 = getY(); Y2 = getY() + getHeight(); // координаты по углам
        }
    }

    public void SETIMAGE(Image image) {
        BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        image = bimage.getScaledInstance(
                bimage.getWidth() > bimage.getHeight() ? this.getWidth() : (int) (bimage.getWidth() * (this.getHeight() / (double) bimage.getHeight())),
                bimage.getHeight() > bimage.getWidth() ? this.getHeight() : (int) (bimage.getHeight() * (this.getWidth() / (double) bimage.getWidth())),
                Image.SCALE_SMOOTH);
        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bGr = bimage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        this.setIcon(new ImageIcon(bimage));

    }

    public void RESIZE() {
        this.width = this.getFontMetrics(this.getFont()).stringWidth(this.getText() + 10);
        this.height = this.getHeight();
        if (this.height == 0) {
            this.height = this.getFontMetrics(this.getFont()).getHeight();
        }
        this.setSize(this.width, this.height);
        X1 = getX(); X2 = getX() + getWidth(); Y1 = getY(); Y2 = getY() + getHeight(); // координаты по углам

    }

    @Override
    public void setText(String text) {
        super.setText(text);
        if (AUTOSIZE == true) {
            RESIZE();
        }

    }

    public void SETICON() {
        setHorizontalAlignment(LEFT);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/book.png"));
        setIcon(icon);
        setSize(getWidth() + 20, getHeight());

    }

    @Override
    public void setLocation(Point p) {
        X1 = getX(); X2 = getX() + getWidth(); Y1 = getY(); Y2 = getY() + getHeight(); // координаты по углам
        super.setLocation(p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLocation(int x, int y) {
        X1 = getX(); X2 = getX() + getWidth(); Y1 = getY(); Y2 = getY() + getHeight(); // координаты по углам
        super.setLocation(x, y); //To change body of generated methods, choose Tools | Templates.
    }

    
}
