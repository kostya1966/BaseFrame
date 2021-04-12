package baseclass;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class Renderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        setText(value.toString());


        if (isSelected) {
            setBackground(Color.ORANGE);
            setForeground(Color.BLACK);
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }


//        if (value.toString().contains("@")) setValue("<html><a href=\"#\">"+value+"</href></html>");
        

        return this;

    }
}