package baseclass;

import javax.swing.table.*;
import prg.V;

public class Modelr extends AbstractTableModel  {

    public int[] COLINDEX;// соответствие заданных колонок и реальных
    private Cursorr DATA;
    private String[] CAPTION;
    private String[] FIELD;
    private Gridr GRID;
    private Formr THISFORM;

    public Modelr(Gridr GRID, String[] CAP, String[] FIELDS) {
        super();
        this.GRID = GRID;
        this.DATA = GRID.DATA;
        this.THISFORM = GRID.THISFORM;
        this.CAPTION = new String[CAP.length + 1];
        this.FIELD = new String[FIELDS.length + 1];
        this.CAPTION[0] = "N.п.п.";
        this.FIELD[0] = "RECNO";

        for (int i = 1; i < this.FIELD.length; i++) {
            this.CAPTION[i] = CAP[i - 1];
            this.FIELD[i] = FIELDS[i - 1];

        }

        COLINDEX = new int[this.FIELD.length];  // массив соответствия интекса указанных колонок ко всем колонкам
        for (int i = 0; i < this.FIELD.length; i++) {
            for (int j = 0; j < this.DATA.COLNAMES.length; j++) {
                if (this.FIELD[i].equalsIgnoreCase(this.DATA.COLNAMES[j])) {
                    COLINDEX[i] = j;
                }
            }

        }

    }

    @Override
    public int getRowCount() {
//        if (DATA.ROWCOUNT==0){
//        return 1;}

        return DATA.ROWCOUNT;
    }

    @Override
    public int getColumnCount() {
//        if (DATA.DATA.length == 0) {
//            return 0;
//        } else {
        return CAPTION.length;
//        }

    }

    public Object getValue(int row, int col)  {//ИЗ КУРСОРА ПО ЗАПИСИ И КОЛОНКЕ
//        return   DATA.DATA[row][col];
        Object[] RECORD = (Object[]) DATA.rowList.get(row);
        return RECORD[col];
    }

    @Override
    public Object getValueAt(int row, int column) { // ДЛЯ ГРИДА ПО РЯДУ И КОЛОНКЕ ГРИДА ИЗ КУРСОРА
        if (row < 0) {
            return null;
        }
        if (column < 0) {
            return null;
        }
        if (row >= DATA.ROWCOUNT) {
            return null;
        }
        if (column >= COLINDEX.length) {
            return null;
        }
        return THISFORM.getValueAt(GRID, row, column, COLINDEX);

    }

    @Override
    public void setValueAt(Object aValue, int row, int col) { //изменение значения в курсоре по записи и колонке с 0
        try {
        if (DATA.COLTYPE[COLINDEX[col]].equals(V.TYPE_NUMERIC) && "".equals((String)aValue)) {
          aValue="0";  
        }
        
        } catch (Exception e) {
                System.out.println(e.toString());
                }
            finally         {
                              //  System.out.println("setValueAt for:"+aValue.getClass().toString());
        }
        if (THISFORM.VALID_GRID(GRID, DATA.COLNAMES[COLINDEX[col]], aValue)) { //проверка введенного значения в методе формы VALID_GRID
        Object[] RECORD = (Object[]) DATA.rowList.get(row);
        RECORD[COLINDEX[col]] = aValue;
        DATA.rowList.set(row, RECORD);
        fireTableCellUpdated(row, col);
        } 
    }

    @Override
    public Class getColumnClass(int col) {
        
        
        if (DATA.ROWCOUNT == 0 || DATA.rowList.size() == 0) {
            return String.class;
        }
        Object value = getValueAt(0, col);
/*        if (col!=0 && COLINDEX[col]!=0  )
        {
            
           Class c=  (Class)DATA.colClass.get(COLINDEX[col]);
           if (c==Double.class) {
               return c;
           }
        }
  */      
        return (value == null) ? Object.class : value.getClass();
    }

    @Override
    public String getColumnName(int col) {
 //       return CAPTION[col];
     //   return "<html><center>"+CAPTION[col];
//        return "<html><div align=center>"+CAPTION[col]+"</html>";        
        return "<html><center>"+CAPTION[col]+"<br><br>";                
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        }
        return THISFORM.ISEDIT(GRID, columnIndex);
//        return true;//ПОМЕНЯЛ!!!!!

    }

    public void addRow(int rowS, int rowE) {
        this.fireTableRowsInserted(rowS, rowE);
    }
}
