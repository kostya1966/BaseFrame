/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclass;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import prg.FF;
import prg.V;

/**
 * Класс формирующий колонку грида
 *
 * @author Kostya
 */
public final class TableColumnr extends javax.swing.table.TableColumn {

    Object obj;
    int gRow, gCol;
    boolean flag;
    public String DinamycBackColor = "";
    Binding binding = new Binding();
    Object o;
    public Object[] objArr = null;
    public Object c1, c2;
    /**
     * Признак корректуры по умолчанию только читать
     */
    public boolean READONLY = true;
    /**
     * Тип данных
     */
    public String TYPE = V.TYPE_CHAR;

    public TableColumnr() {
        super();
    }

    public TableColumnr(int modelIndex) {
        super(modelIndex);
    }

    public TableColumnr(int modelIndex, int width) {
        super(modelIndex, width);
    }

    public TableColumnr(int modelIndex, int width,
            TableCellRenderer cellRenderer,
            TableCellEditor cellEditor) {
        super(modelIndex, width, cellRenderer, cellEditor);
    }

    public void SETCOLUMNCONTROL(Object obj) {
        SETCOLUMNCONTROL(obj, V.ACTIVEGRID);
    }

    public void SETCOLUMNCONTROL(Object obj, Gridr grid) {
        this.obj = obj;
//        System.out.println(this.getCellEditor(). + "||" + gRow + "||" + obj.getClass().getName());
        if (!obj.getClass().getName().equals("javax.swing.JTextField")) {
            grid.COLUMN_CONTROL.add(new ArrayList<>(Arrays.asList(getModelIndex(), obj)));
        }
        setCellRenderer(new Renderer()); //отображатель по obj
        if (obj.getClass().getName().equals("javax.swing.JComboBox")) {
            JComboBox cbe = (JComboBox) obj;
            setCellEditor(new DefaultCellEditor(cbe));
        }
        if (obj.getClass().getName().equals("javax.swing.JButton")) {
            setCellEditor(new ButtonEditor(grid));
        }
        if (obj.getClass().getName().equals("baseclass.Buttonr")) {
            setCellEditor(new ButtonEditorR(grid));
        }

        if (obj.getClass().getName().equals("javax.swing.JTextField")) {
            final JTextField txt = (JTextField) obj;
            txt.setBorder(javax.swing.BorderFactory.createEmptyBorder()); //удалить границы
            txt.setBackground(new Color(210, 210, 210));  //установить серый цвет
            //txt.setToolTipText(TYPE);

            // setCellEditor(new DefaultCellEditor(txt)); //по умолчанию txt
            txt.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if ("0".equals(txt.getText()) && !READONLY && TYPE.equals(V.TYPE_NUMERIC)) {
                        txt.setText("");
                        txt.setCaretPosition(0);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if ("".equals(txt.getText().trim()) && !READONLY && TYPE.equals(V.TYPE_NUMERIC)) {
                        txt.setText("0");
                    }

                }
            }
            );

            txt.addKeyListener(new KeyListener() {  //слушатель клавиатуры на символы
                @Override
                public void keyTyped(KeyEvent e) {
                    if (READONLY && (e.getKeyCode() != KeyEvent.VK_LEFT && e.getKeyCode() != KeyEvent.VK_RIGHT)) //если только читать и не вправо или влево                       
                    {
                        e.consume();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) { //слушатель клавиатуры на символы клавиши

                    if (!READONLY) {
                        return;
                    }
                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                        e.consume();
                    }
                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) {
                        e.consume();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                        e.consume();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    e.consume();
                }
            });
            setCellEditor(new DefaultCellEditor(txt));
        }

    }
/**
 * Показывает элементы в таблице 
 */
    public class Renderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            try {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Gridr grid = (Gridr) table;
//            System.out.println(row + "||" + column + "||" + value + "||" + obj.getClass().getName());

                if (obj.getClass().getName().equals("javax.swing.JComboBox")) {
                    JComboBox cb = (JComboBox) obj;
                    String[] str = new String[cb.getItemCount()];
                    for (int j = 0; j < cb.getItemCount(); j++) {
                        str[j] = cb.getItemAt(j).toString();
                    }
                    cb = new JComboBox(str);
                    cb.setSelectedIndex(0);
                    for (int k = 0; k < cb.getItemCount(); k++) {
                        if (grid.getValueAt(row, column) != null
                                && grid.getValueAt(row, column).equals(cb.getItemAt(k))) {
                            cb.setSelectedIndex(k);
                            break;
                        }
                    }
                    grid.setValueAt(cb.getItemAt(cb.getSelectedIndex()), row, column);
                    return cb;
                }
                if (obj.getClass().getName().equals("javax.swing.JButton")) {
                    JButton button;
                    if (((JButton) obj).getText().equals("icon")) {
                        button = new JButton(((JButton) obj).getIcon());
                    } else {//RKV 08.04.2019 не понятно почему в имя в заголовок, заменил на значение поля таблицы если имя начинается с data
                    if (((JButton) obj).getName().substring(0, 4).equals("data")) {
                         button = new JButton(value.toString());
                         button.setFont(((JButton) obj).getFont());
                    } else {
                        button = new JButton(((JButton) obj).getName());
                        button.setFont(((JButton) obj).getFont());
                    }}
//                button = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/view-refresh.png")));
                    button.setVisible(true);
                    return button;
                }
                if (obj.getClass().getName().equals("baseclass.Buttonr")) {
                    ((Buttonr) obj).setVisible(true);
                    return ((Buttonr) obj);
                }
                //динамическое изменение цвета
                if (!DinamycBackColor.equals("")) {
                    String dbc = DinamycBackColor;
                    String name = "";
                    ArrayList<ArrayList<String>> arr = new ArrayList<>();
                    if (dbc.indexOf("#") != -1 && FF.OCCURS("#", dbc) > 1 && FF.OCCURS("#", dbc) / 2 == (int) FF.OCCURS("#", dbc) / 2) {
                        while (dbc.contains("#")) {
                            name = dbc.substring(dbc.indexOf("#") + 1, dbc.substring(dbc.indexOf("#") + 1).indexOf("#") + dbc.indexOf("#") + 1);
                            arr.add(new ArrayList<>(Arrays.asList("-1", name)));
                            dbc = dbc.substring(dbc.substring(dbc.indexOf("#") + 1).indexOf("#") + dbc.indexOf("#") + 2, dbc.length());
                        }

                        int cnt = 0;
                        for (int i = 0; i < grid.FIELD.length; i++) {
                            for (int j = 0; j < arr.size(); j++) {
                                if (grid.FIELD[i].equals(arr.get(j).get(1))) {
//                                arr.get(j).set(0, String.valueOf(i));
                                    arr.get(j).set(0, String.valueOf(grid.COL_REAL(i + 1) - 1));  //при перемещении находит старый индекс grid.COL_REAL(i+1)-1
                                    cnt++;
                                }
                            }
                        }

                        if (cnt == arr.size()) {
                            String DinamycBackColor_e = DinamycBackColor;
                            int k = 0;
                            for (int i = 0; i < arr.size(); i++) {
                                String val = "\"" + (grid.getValueAt(row, Integer.parseInt(arr.get(i).get(0)) + 1) != null ? grid.getValueAt(row, Integer.parseInt(arr.get(i).get(0)) + 1).toString() : "") + "\"";
                                String gridVal = (grid.getValueAt(row, Integer.parseInt(arr.get(i).get(0)) + 1) != null ? grid.getValueAt(row, Integer.parseInt(arr.get(i).get(0)) + 1).toString() : "");
                                if (!gridVal.contains(".")) {
                                    try {
                                        Integer.parseInt(gridVal);
                                        val = val.replaceAll("\"", "");
                                    } catch (Exception ex) {
                                    }
                                } else {
                                    try {
                                        Double.parseDouble(gridVal);
                                        val = val.replaceAll("\"", "");
                                    } catch (Exception ex) {
                                    }
                                }
                                val = val.replaceAll("\\n", " ");
                                DinamycBackColor_e = DinamycBackColor_e.replaceAll("#" + arr.get(i).get(1) + "#", val);
                                if (objArr != null) {
                                    objArr[k] = val.replaceAll("\"", "");
                                    k += 4;
                                }
                            }
                            if (objArr != null) {
                                int fl = 0;
//                            int condKol = (objArr.length - 1) / 3;
                                for (int i = 0; i < objArr.length; i += 4) {
                                    if (objArr[i + 1].equals("equals")) {
                                        if (objArr[i].equals(objArr[i + 2])) {
                                            fl++;
                                        }
                                    }
                                }
                                if (fl == (objArr.length - 1) / 3) {
                                    o = c1;
                                } else {
                                    o = c2;
                                }
                            } else {
                                GroovyShell shell = new GroovyShell(binding);
                                o = shell.evaluate("import java.awt.Color;" + DinamycBackColor_e);
                            }
                        }
//                    Object o2 = shell.evaluate(DinamycBackColor_e.substring(0, DinamycBackColor_e.indexOf("?")));

//                    if ((boolean) o2) {
                        if (isSelected) {
                            cell.setBackground(((Color) o).darker());
                        } else {
                            cell.setBackground((Color) o);
                        }
//                    } else {
//                        if (isSelected) {
//                            cell.setBackground(Color.black);
//                        } else {
//                            cell.setBackground(Color.green);
//                        }
//
//                    }

//                    cell.setBackground((Color) o);
                    }
                }

                try {

                    setToolTipText(value.toString());
                } catch (NullPointerException ex) {
                }
            } catch (Exception exc) {
                System.out.println("EXCCCCC_REND - " + exc.toString());
            }

            return this;

        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        JTable table;
        JButton button;
        int clickCountToStart = 1;
        public ButtonEditor(JTable table) {
           
            if (((JButton) obj).getText().equals("icon")) {
                button = new JButton(((JButton) obj).getIcon());
            } else {  //RKV 08.04.2019 не понятно почему в имя в заголовок убрал
             if (((JButton) obj).getName().substring(0, 4).equals("data")) {
                  button = new JButton(((JButton) obj).getText());
                  button.setFont(((JButton) obj).getFont());
             } else {
                button = new JButton(((JButton) obj).getName());
            }}
//            button = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/view-refresh.png")));
            this.table = table;
            for (int i = 0; i < ((JButton) obj).getActionListeners().length; i++) {
                button.addActionListener(((JButton) obj).getActionListeners()[i]);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {//действие кнопки в ячейке грида
            JButton but = (JButton) obj;
            if (but.getText().equals("but1")) {
                System.out.println("qweqweqweqew");
            }
            if (but.getText().equals("but2")) {
                System.out.println("but2");
            }

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
            }
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }

        @Override
        public void cancelCellEditing() {
            super.cancelCellEditing();
        }
    }

    class ButtonEditorR extends AbstractCellEditor implements TableCellEditor, ActionListener {

        JTable table;
        Buttonr button = new Buttonr(((Buttonr) obj).getName(), ((Buttonr) obj).getText(), 50, 30);
        int clickCountToStart = 1;

        public ButtonEditorR(JTable table) {
            this.table = table;
            button.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {//действие кнопки в ячейке грида
            if (obj.getClass().getName().equals("baseclass.Buttonr")) {
                Buttonr but = (Buttonr) obj;
                if (but.getText().equals("B0")) {
                    System.out.println("qweqweqweqew");
                }
            }

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
            }
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }

        @Override
        public void cancelCellEditing() {
            super.cancelCellEditing();
        }
    }

    public class cellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            Gridr grid = (Gridr) table;
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            try {

                if (row == gRow && flag) {
                    if (isSelected) {
                        cell.setBackground(Color.yellow);
                    } else {
                        cell.setBackground(Color.red);
                    }
                } else {
                    if (isSelected) {
                        cell.setBackground(Color.black);
                    } else {
                        cell.setBackground(Color.green);
                    }

                }
                try {
                    setToolTipText("9999" + value.toString());
                } catch (NullPointerException ex) {
                }
            } catch (Exception exc) {
                System.out.println("EXCCCCC - " + exc.toString());
            }
            return cell;

        }
    }

    public void SETCELLRENDERER(int row, boolean fl) {
        this.gRow = row;
        this.flag = fl;
        setCellRenderer(new cellRenderer());
    }
    
    
    
    
    
}
