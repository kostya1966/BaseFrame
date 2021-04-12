package baseclass;

import java.awt.Component;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import prg.V;

public class RendererDate extends JLabel implements TableCellRenderer {
  SimpleDateFormat mask;
    public RendererDate(SimpleDateFormat mask) {
        this.mask=mask;
    }
 
    
  @Override
  public Component getTableCellRendererComponent(JTable  table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
      if (isSelected){
            setBackground(V.COLORB_GRID_REC_FOCUSON);
            setForeground(V.COLORF_GRID_REC_FOCUSON);
      }   
//            setText("Table element is not a java.util.Date!");
//            setIcon(null);

        if ((value instanceof Date)) {
           Date date = (Date) value;
           setText(mask.format(date));
            return this;
        }
        if ((value instanceof Timestamp)) {
           Timestamp date = (Timestamp) value;
           setText(mask.format(date));
            return this;
        }
        
        return this;
    }

}