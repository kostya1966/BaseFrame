package baseclass;

//import static baseclass.Gridr.THISFORM;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.Timer;
//import java.util.Timer;
import java.util.TimerTask;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import prg.A;
import prg.FF;
import prg.P;
import prg.S;
import prg.V;

/**
 * Базовый класс для работы с таблицами от JTable
 *
 * @author Kostya
 */
public class Gridr extends JTable {

    public JScrollPane SCROLL;//
    public Cursorr DATA;         //данные из запроса
    public Formr THISFORM;       //текущая родительская форма
    public static Formr WORKFORM;       //текущая родительская форма используется только в конструкторе
    public Modelr MODEL = null;//модель данных 
    public String SELE;  //последний запрос
    public int RECNO;  //номер текущей записи
    public int STRNO;  //номер текущей строки
    public int FOCUS = 0;  //признак 1- в фокусе 0 - вышел из фокуса
    public String CAPTION[];    // список заголвка
    public String FIELD[];      // список полей
    public int FSIZE[];      // список размера колонок по умолчанию 
    public int RESTORECOL = 1;  //признак восстановления размера колонок 1- восстановить 0 - нет

    public int VIEWRECNO = 0;  //признак показа колонки RECNO 1- показать 0 - нет    
    public String ALIAS = "";  //алиас
    public String TABLE = "";  //таблица
    public static String ATMP_RKV;  //
    public String FREAD;      // имя формы корректуры
    public Formr_k FORM_K;      // объект формы корректуры    
    public Gridr GRID;       //текущая таблица
    public boolean ENABLE_REFRESH = false; //true разрешить авто обновление данных
    public boolean REFRESH_RUN = false; //статус авто обновления данных
    public boolean ENABLE_INSERT = true; //РАЗРЕШЕННИЕ НА ВСТАВКУ
    public boolean ENABLE_DELETE = true;    //РАЗРЕШЕННИЕ НА УДАЛЕНИЕ
    public boolean ENABLE_READ = true;    //РАЗРЕШЕННИЕ НА КОРРЕКТУРУ
    public boolean READ_QUERY = false;    //ПЕРЕВЫБОРКА ВСЕХ ДАННЫХ ЗАПРЕЩЕНА ПРИ КОРРЕКТУРЕ если READ_QUERY=false обновление  только одной записи
    public boolean ENABLE_DEPLOY = false;    //ВЫЗОВ ИЗ ТУЛБАРА МЕТОДА РАЗВЕРНУТЬ ДОКУМЕНТ
    public boolean ENABLE_STATUS = false;    //ВЫЗОВ ИЗ ТУЛБАРА МЕТОДА ИЗМЕНИТЬ СТАТУС
    public boolean PREV_QUERY_REC_READ = false;    //ОБНОВЛЕНИЕ ЗАПИСИ ПЕРЕД ВЫЗОВОМ НА КОРРЕКТУРУ
    public boolean QUERY_MESS = true;    //сообщение о запросе данных
    public boolean STAT_DBCLICK = false; // разрешение реакции по двойному щелчку мыши    
    public int HEADERH = V.HEADERH;  //ВЫСОТА ШАПКИ ПО УМОЛЧАНИЮ V.HEADERH
    public int ROWH = V.ROWH;  //ВЫСОТА СТРОКИ ГРИДА
    public String LOCATEFOR = ""; //ЗАПОМИНАЕТ ЗНАЧЕНИЕ ПОЛЯ И НАХОДИТ ПРИ ПЕРЕВЫБОРКЕ ДАННЫХ
    public String LOCATEFOR_NUM = "Nulll";
    public String SAVE_KEY = ""; //КЛЮЧ ДЛЯ СОХРАНЕНИЯ , МОЖНО СОХРАНЯТЬ И ВОССТАНАВЛИВАТЬ  ПОД РАЗНЫЕ КЛЮЧИ НАПРИМЕР ТИП ОПРЕАЦИИ
    public String RESTORESORT = "0";  //признак восстановления сортировки 1- восстановить 0 - нет

    public ArrayList<ArrayList<String>> DYNAMIC_COLOR_CONDITION = new ArrayList<>();
    public ArrayList<ArrayList<Object>> DYNAMIC_COLOR_CONDITION_2 = new ArrayList<>();
    public ArrayList<ArrayList> COLUMN_CONTROL = new ArrayList<>();
    public TableRowSorter<TableModel> SORTER = null;
    public Timer timer=null;
    //для запрета перетаскивания первой колонки
    public int columnValue = -1;
    public int columnNewValue = -1;

    public void DBCLICK() {
        THISFORM.DBLCLICK_ALL(getName());
    }

    public void CLICKR_ALL() {
        THISFORM.CLICKR_ALL(getName());
    }

    public void CLICK() {
        THISFORM.CLICK_ALL(getName());
    }

    /**
     * Конструктор таблицы для пустого вызова
     *
     * @param THISFORM - форма корректуры
     * @param alias - алиас
     */
    public Gridr(final Formr_k THISFORM, String alias) {
        this.GRID = this;//для ссылки на себя
        this.FORM_K = THISFORM;//для ссылки на родительскую форму
        this.ALIAS = alias;
        this.RECNO = A.RECNO(this.ALIAS);
        THISFORM.FORMP = THISFORM; 
//        SORTER.setSortsOnUpdates(true);
    }

    /**
     * Конструктор таблицы
     *
     * @param NAME имя таблицы
     * @param THISFORM родительская форма таблицы
     * @param X позиция на форме по горизонтали
     * @param Y позиция на форме по вертикали
     * @param W штрина
     * @param H высота
     */
    public Gridr(final Formr THISFORM, String NAME, int X, int Y, int W, int H) {
//        for (int i=0;i<10;i++) {//для динамического цвета колонок
//            DYNAMIC_COLOR_COLUMN.add(-1);
//        }//
        //setColumnModel(new TableColumnModelr());
        if (V.USER_ADMIN < 2) { // только просмотр 
            ENABLE_INSERT = false; //РАЗРЕШЕННИЕ НА ВСТАВКУ
            ENABLE_DELETE = false;    //РАЗРЕШЕННИЕ НА УДАЛЕНИЕ
            ENABLE_READ = false;    //РАЗРЕШЕННИЕ НА КОРРЕКТУРУ

        }

        this.GRID = this;//для ссылки на себя
        this.THISFORM = THISFORM;//для ссылки на родительскую форму
        setName(NAME);//установка имени
        SCROLL = new javax.swing.JScrollPane(this); // создаем скролингш для таблицы
        SCROLL.setViewportView(this); //не знаю 
        THISFORM.getContentPane().add(SCROLL); //добавляем на панель формы   
        SCROLL.setBounds(X, Y, W, H); // координаты по скролингу а не по таблице
        this.SCROLL.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
//             System.out.print("    22222222:"+"\n");                
                S.SETACTIVEGRID(GRID);//установка активного грида для тулбара

            }
        });

//        Renderer cellRenderer = new Renderer();
//        this.setDefaultRenderer(Object.class, cellRenderer);     
        //листнер нажатия на шапку грида
        this.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON1) {
                    V.SELECTED_COLUMN = columnAtPoint(e.getPoint());
                    //SORTER.toggleSortOrder(V.SELECTED_COLUMN);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {  //правая кнопка для подсчета сумм
                    double kolSum = 0, kolSumS = 0;
                    int[] Srow = GRID.getSelectedRows(); // номера выделенных строк- записей
                    for (int i = 0; i < GRID.getRowCount(); i++) {
                        try {
                            try {
                                kolSum += Double.parseDouble(GRID.getValueAt(i, columnAtPoint(e.getPoint())).toString().replaceAll(" ", ""));
                                if (GRID.isRowSelected(i)) {
                                    kolSumS += Double.parseDouble(GRID.getValueAt(i, columnAtPoint(e.getPoint())).toString().replaceAll(" ", ""));
                                }
                            } catch (ClassCastException exc) {
                                try {
                                    kolSum += Integer.parseInt(GRID.getValueAt(i, columnAtPoint(e.getPoint())).toString().replaceAll(" ", ""));
                                    if (GRID.isRowSelected(i)) {
                                        kolSumS += Integer.parseInt(GRID.getValueAt(i, columnAtPoint(e.getPoint())).toString().replaceAll(" ", ""));
                                    }

                                } catch (ClassCastException exce) {
                                    return;
                                }
                            }
                        } catch (NumberFormatException ex) {
                            P.MESS("Всего записей: " + GRID.getRowCount() + "\n Выделенных: " + Srow.length);
                            return;
                        } catch (NullPointerException exNull) {
                            return;
                        }
                    }

                    //P.MESS("Всего записей: " + GRID.getRowCount() + "\nСумма: " + (new DecimalFormat(V.DESFORMAT_TEXT)).format(kolSum)
                    //+"\n\n Выделенных: "+Srow.length+ "\nСумма выделенных: " + (new DecimalFormat(V.DESFORMAT_TEXT)).format(kolSumS));
                    P.MESS("Всего записей: " + GRID.getRowCount() + "\nСумма: " + P.TRANSFORM(kolSum, V.PATTERN_SUM, " ", ".")
                            + "\n\n Выделенных: " + Srow.length + "\nСумма выделенных: " + P.TRANSFORM(kolSumS, V.PATTERN_SUM, " ", "."));
                    if (kolSumS != 0) {
                        FF._CLIPTEXT(FF.STR(kolSumS));
                    }
                }
            }

            //если при перетаскивании колонок задета нулевая, возвращаем все на место КАА
            @Override
            public void mouseReleased(MouseEvent e) {
                if (columnValue != -1 && (columnValue == 0 || columnNewValue == 0)) {
                    GRID.moveColumn(columnNewValue, columnValue);
                }

                columnValue = -1;
                columnNewValue = -1;
            }

        });

        //КАА после перетаскивания колонок, сохраняю новые индексы
        GRID.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

            @Override
            public void columnAdded(TableColumnModelEvent e) {
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
                if (columnValue == -1) {
                    columnValue = e.getFromIndex();
                }

                columnNewValue = e.getToIndex();
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        });

        ListSelectionModel selModel = this.getSelectionModel();//класс выбора строки
        selModel.addListSelectionListener(new ListSelectionListener() {     //слушатль по выбору строки          
            public void valueChanged(ListSelectionEvent e) {
                CHANGERECNO(); //проверка на изменение номера записи                                     }               
//               System.out.print("valueChanged"+GRID.RECNO+"\n");            

            }
        });

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {//фокус получили
                S.SETACTIVEGRID(GRID);//установка активного грида для тулбара
                GRID.setSelectionBackground(V.COLORB_GRID_REC_FOCUSON);  //при переходе на запись все ячейки строки определяются Selection и к ним устанавливается цвет
                GRID.setSelectionForeground(V.COLORF_GRID_REC_FOCUSON);  //

            }

            @Override
            public void focusLost(FocusEvent e) {//фокус потеряли
                FOCUS = 0;
                GRID.setSelectionBackground(V.COLORB_GRID_REC_FOCUSOFF);
                GRID.setSelectionForeground(V.COLORF_GRID_REC_FOCUSOFF);
//       System.out.print("  focusLost:"+GRID.getName()+"\n");
                GRID.LOSTFOCUS();
            }
        });

        addKeyListener(new KeyListener() { //слушатель на клавиатуру 
            @Override
            public void keyPressed(KeyEvent e) {
//               System.out.print("keyPressed recno:"+GRID.RECNO+"\n");            
//                if (e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_TAB && e.getKeyCode() != KeyEvent.VK_CONTROL && e.getKeyCode() != KeyEvent.VK_C) {
//                    e.consume();
//                }
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
                THISFORM.KEYPRESS(e.getComponent(), e);//обработчик общий метод из формы

            }

            @Override
            public void keyReleased(KeyEvent e) {
                e.consume();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                e.consume();
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                //    System.out.print("    11111111111:"+"\n");
                P.ALERT("");
                if (STAT_DBCLICK && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {  // если статус разрешения двойного щелчка и одинарный щелчок - вызов общего метода формы
                    try {
                        CLICK();
                    } catch (NullPointerException wxcc) {
                    }
                }

                if (e.getButton() == MouseEvent.BUTTON3) { //правая кнопка 
                    int column = GRID.columnAtPoint(e.getPoint());
                    int row = GRID.rowAtPoint(e.getPoint());
                    GRID.setColumnSelectionInterval(column, column);
                    GRID.setRowSelectionInterval(row, row);
                    if (V.TOOLBAR != null) {
                        gridTools(0);
                    }
                    CLICKR_ALL();
                }
                if (e.getButton() == MouseEvent.BUTTON2) { //средняя
                    int column = GRID.columnAtPoint(e.getPoint());
                    int row = GRID.rowAtPoint(e.getPoint());
                    GRID.setColumnSelectionInterval(column, column);
                    GRID.setRowSelectionInterval(row, row);
                }

                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) { // двойной щелчок
                    int column = GRID.columnAtPoint(e.getPoint());
                    int row = GRID.rowAtPoint(e.getPoint());
                    GRID.setColumnSelectionInterval(column, column);
                    GRID.setRowSelectionInterval(row, row);
                    DBCLICK();
                }
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) { // левая
                    int column = GRID.columnAtPoint(e.getPoint());
                    int row = GRID.rowAtPoint(e.getPoint());
//                    GRID.setColumnSelectionInterval(column, column);
//                    GRID.setRowSelectionInterval(row, row);
                    int col = GRID.getColumnModel().getColumn(column).getModelIndex();
                    Object obj = GRID.MODEL.getValue(row, GRID.MODEL.COLINDEX[col]);
                    // ВРЕМЕННО НА ДЕНОМИНАЦИЮ
                    /*int NUMCONF=0;
                     int KOP_CONF=0;
                     if (!"U".equals(A.TYPE("CONFIG.NUMCONF"))) {
                     NUMCONF=(int)A.GETVAL("CONFIG.NUMCONF");
                     }
                            
                     if (NUMCONF==2) { //ДЛЯ РБ
                            
                     if (!"U".equals(A.TYPE("CONFIG.KOP_CONF"))) {
                     KOP_CONF=(int)A.GETVAL("CONFIG.KOP_CONF");
                     }
                            

                     if (obj instanceof Float || obj instanceof Double) { //временно для диноминации
                     double d=(double)obj;
                     if (KOP_CONF==0) {
                     P.ALERT(new DecimalFormat("#,##0").format(d)+"     "+new DecimalFormat("#,##0.00").format(d/10000));
                     } else {
                     P.ALERT(new DecimalFormat("#,##0.00").format(d)+"     "+new DecimalFormat("#,##0.00").format(d*10000));
                     }
                     } else {
                     P.ALERT("");
                     }
                    
                     }
                     */
                }

            }
        });

        // Установка своего обработчика нажатия клавиши Enter. По умолчанию - переход на следующую запись . При корректуре нет возврата на текущую.
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key-enter");
        getActionMap().put("key-enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                THISFORM.KEYPRESS(GRID,KeyEvent.VK_ENTER);   //что бы ничего не происходило
            }
        });
    } // конец конструктора
    //Перекрываем методы оаределения и установки размеров по SCROLL
/*  
     @Override
     public void setBounds(int x, int y, int width, int height){
     this.SCROLL.setBounds( x,  y,  width, height);
     }

     @Override
     public Rectangle getBounds() {
     return this.SCROLL.getBounds(); //
     }
  
     */

    public TableColumnr getColumn(int index) {
        TableColumnModel cm = getColumnModel();
        return (TableColumnr) getColumnModel().getColumn(index);
    }

    @Override
    public void createDefaultColumnsFromModel() {  // создание column на основании rkfccf TableColumnr
        TableModel m = getModel();
        if (m != null) {
            // Remove any current columns
            TableColumnModel cm = getColumnModel();
            while (cm.getColumnCount() > 0) {
                cm.removeColumn(cm.getColumn(0));
            }

            // Create new columns from the data model info
            for (int i = 0; i < m.getColumnCount(); i++) {
                TableColumnr newColumn = new TableColumnr(i);
                addColumn(newColumn);
            }
        }
    }

    public void addColumn(TableColumnr aColumn) {
        if (aColumn.getHeaderValue() == null) {
            int modelColumn = aColumn.getModelIndex();
            String columnName = getModel().getColumnName(modelColumn);
            aColumn.setHeaderValue(columnName);
        }
        getColumnModel().addColumn(aColumn);
    }

    public void gridTools(int NOMER) {//ВЫБОР ОПЕРАЦИИ ДЛЯ ГРИДА
        int n = 0;
        if (NOMER == 0) {
            String[] strm = {"Обновить выборку данных в таблице", "Новая запись записи", "Редактирование текущей записи", "Удаление текущей записи", "Экспорт текущей таблицы в EXEL", "Печать", "Раскрыть документ", "Изменить статус документа"};
            if (!V.TOOLBAR.QUERY.isEnabled()) {
                strm[0] = "<>" + strm[0];
            }
            if (!V.TOOLBAR.INSERT.isEnabled()) {
                strm[1] = "<>" + strm[1];
            }
            if (!V.TOOLBAR.UPDATE.isEnabled()) {
                strm[2] = "<>" + strm[2];
            }
            if (!V.TOOLBAR.DELETE.isEnabled()) {
                strm[3] = "<>" + strm[3];
            }
            if (!V.TOOLBAR.PRINT.isEnabled()) {
                strm[5] = "<>" + strm[5];
            }
            if (!V.TOOLBAR.DEPLOY.isEnabled()) {
                strm[6] = "<>" + strm[6];
            }
            if (!V.TOOLBAR.STATUS.isEnabled()) {
                strm[7] = "<>" + strm[7];
            }

            n = P.MENU(strm, GRID);
            if (n == 0) {
                return;
            }
        } else {
            n = NOMER;
        }
//         JOptionPane.showMessageDialog(null,n, "Внимание!!!",  0); 
        if (n == 1) {
            V.TOOLBAR.QUERY(null);
        }
        if (n == 2) {
            V.TOOLBAR.INSERT();
        }
        if (n == 3) {
            V.TOOLBAR.UPDATE();
        }
        if (n == 4) {
            V.TOOLBAR.DELETE();
        }
        if (n == 5) {
            V.TOOLBAR.EXCEL();
        }
        if (n == 6) {
            V.TOOLBAR.PRINT(THISFORM);
        }
        if (n == 7) {
            V.TOOLBAR.DEPLOY(GRID);
        }
        if (n == 8) {
            V.TOOLBAR.STATUS(GRID);
        }

    }

    public void SETHEADERH(int HEADERH) {
        GRID.HEADERH = HEADERH;
        GRID.getTableHeader().setResizingAllowed(true);//разрешить изменение размеров заголовка
        GRID.getTableHeader().setPreferredSize(new Dimension(21000, GRID.HEADERH));
    }

    public void SETLOCATEFOR(String loc) {
        GRID.LOCATEFOR = loc;
    }

    public void SETREFRESH_RUN()  {
        if (!REFRESH_RUN) {
            P.DOCORRECT("Подтвердите операцию:",new Object[]{"Период в сек.:",V.TYPE_NUMERIC,"99",5});
            if (V.PARAMOT == null || V.PARAMOT[0].equals("F")) {
                return;
                        };
           int delay=FF.VAL(V.PARAMOT[1])*1000;
/*           
		timer = new Timer();
		timer.schedule(
				new TimerTask() {
					@Override
					public void run() {
            //System.out.println("timer:"+THISFORM.getName());
            THISFORM.QUERY(GRID);             
					}
				}, delay, delay
		);
  */         
        timer= new Timer( delay , new ActionListener() {
        @Override
           public void actionPerformed(ActionEvent e) {
            //System.out.println("timer:"+THISFORM.getName());
            THISFORM.QUERY(GRID);             
               }
       } );
        
        timer.start();
         REFRESH_RUN=true;    
        } else {
            if (timer!=null) {
            timer.stop();
            timer=null;
            }
            REFRESH_RUN=false;    
            
        }
    }
    
    /**
     * Выдает новый номер колонки от указанного в FIELD после его перемещения
     *
     * @param col
     * @return
     */
    public int COL_REAL(int col) {
        for (int i = 0; i < getModel().getColumnCount(); i++) {   // найти новый позицию колонки     
            if (GRID.getColumn(i).getModelIndex() == col) {
                col = i;
                break;
            }
        }
        return col;
    }

    public void SETDYNAMICCOLOR(int col, String cond) {
        col = COL_REAL(col);
        GRID.getColumn(col).DinamycBackColor = cond;
        GRID.DYNAMIC_COLOR_CONDITION.add(new ArrayList<>(Arrays.asList(String.valueOf(col), cond)));
    }

    public void SETDYNAMICCOLOR(int col, String cond, Object c1, Object c2, Object... obj) {
        GRID.getColumn(col).DinamycBackColor = cond;
        GRID.DYNAMIC_COLOR_CONDITION.add(new ArrayList<>(Arrays.asList(String.valueOf(col), cond)));
        GRID.getColumn(col).objArr = obj;
        GRID.getColumn(col).c1 = c1;
        GRID.getColumn(col).c2 = c2;
        GRID.DYNAMIC_COLOR_CONDITION_2.add(new ArrayList<>(Arrays.asList((Object) String.valueOf(col), obj, c1, c2)));
    }

    public void SETFOCUS() {
        GRID.setFocusable(true);
        GRID.requestFocusInWindow();;
        GRID.GORECNO(RECNO);
        return;
    }

    public void LOSTFOCUS() {
    }

    /**
     * проверка на изменение номера записи вызывается из слушателя
     * ListSelectionModel selModel
     */
    public int CHANGERECNO() {
        //проверка на измекнение текущей записи и вызов метода обработки
        int reclast = GRID.RECNO;
        int rowlast = GRID.STRNO;
        int rec = 0;
        int row = GRID.getSelectedRow();  // определяем какая строка выбранна
        if (row < 0) {
            return row;
        }
        try {
            rec = (int) GRID.getValueAt(row, 0);
        } catch (Exception e) {
            System.out.println("Значение в строке " + row + " для 0 -колонки " + GRID.getValueAt(row, 0));
        }
        if (rec < 0) {
            return rec;
        }

        if (rec != GRID.RECNO) //если изменилась текущая запись
        {
            GRID.RECNO = rec;
            GRID.STRNO = row;
            S.SETDATARECNO(DATA, rec);
            if (rec > 0) {
                GRID.THISFORM.CHANGERECNO(GRID, row, rec);
            }
        }
//                               GRID.THISFORM.CHANGERECNO(GRID,rowlast,reclast);                           
        return reclast;
    }

    public int GETSELROW() {
        return getSelectedRow() + 1;
    }

    public int GETSELCOL() {
        return getSelectedColumn();
    }

    //установка требуемой записи в гриде   
    /**
     * Соответствие записи в гриде и записи в курсоре
     *
     * @param RECNO - переход в курсоре и гриде на указаннйю запись
     * @return
     */
    public int GORECNO(int RECNO) {
        if (RECNO <= 0) {
            return 0;
        }
        A.GOTO(ALIAS, RECNO);
        for (int i = 0; i < getRowCount(); i++) {
            //     if (getValueAt(i, 0)!=null && RECNO == (int) getValueAt(i, 0)) {
            try {
                if (RECNO == (int) getValueAt(i, 0)) {
                    changeSelection(i, getSelectedColumn(), false, false);
                    S.SETDATARECNO(DATA, RECNO);
                    return i;
                }
            } catch (NullPointerException exc) {
                return 0;
            }
        }
        return 0;
    }

    public int GORECNO() {
        return GORECNO(A.RECNO(ALIAS));
    }

    /**
     * Восстановление ширины и порядок колонок в таблице
     */
    public void RESTORECOL() {//восстановление ширины колонок
        Configs proper = new Configs(V.fileNameGridconf);
        int ind = 0;
        try {
            int colcount = new Integer(proper.getProperty("COLCOUNT_" + THISFORM.getName() + getName(), "0"));
            if (GRID.FIELD.length == colcount || colcount == 0) { //если количество колонок не поменялось

                for (int i = 1; i <= GRID.FIELD.length; i++) { //ШИРИНА КОЛОНОК
                    ind = GRID.getColumnModel().getColumn(i).getModelIndex() - 1;
                    if (!"".equals(proper.getProperty(SAVE_KEY + "WIDTH_" + THISFORM.getName() + getName() + "_" + GRID.FIELD[ind], ""))) {
                        int x = new Integer(proper.getProperty(SAVE_KEY + "WIDTH_" + THISFORM.getName() + getName() + "_" + GRID.FIELD[ind], "0"));
                        if (x == 75) {
                            x = 150;
                        }
                        if (x > 0) {
//                    V.FSIZE[i]=x;
                            this.getColumnModel().getColumn(i).setPreferredWidth(x);
                        }
                    }
                }
                //
/*        for (int i = 1; i <= GRID.FIELD.length; i++) {  //ПОРЯДОК КОЛОНОК
                 ind = GRID.getColumnModel().getColumn(i).getModelIndex() - 1  ;          
                 int npp = new Integer(proper.getProperty(SAVE_KEY+"NPP_" + THISFORM.getName() + getName() + "_"+GRID.FIELD[ind], "-1"));
                 if (npp!=-1 && npp<=GRID.FIELD.length ) {
                 GRID.getColumnModel().moveColumn(i, npp);
                 }
                 }
                 */

            }

            if (!GRID.LOCATEFOR.equals("")) {
                GRID.LOCATEFOR_NUM = proper.getProperty("LOCATEFORNUM$" + THISFORM.getName() + getName());
            }
            String sort = proper.getProperty(SAVE_KEY + "SORT_" + THISFORM.getName() + getName(), "");
            RESTORESORT = proper.getProperty(SAVE_KEY + "RESTORESORT_" + THISFORM.getName() + getName(), "0");
            if ("0".equals(RESTORESORT)) { //если не сохранять сортировку
                sort = "";
            }

            if (!FF.EMPTY(sort)) {
                int col = FF.VAL(FF.SUBSTR(sort, 3));
                ArrayList<SortKey> keys = new ArrayList<SortKey>(); // создаем коллецию ключей сортировки            
                keys.add(new SortKey(col, FF.SUBSTR(sort, 1, 2) == "-2" ? SortOrder.DESCENDING : SortOrder.ASCENDING));  //Записываем ключ сортировки
                SORTER.setSortKeys(keys);
                SORTER.toggleSortOrder(col);
            }
            HEADERH = FF.VAL(proper.getProperty(SAVE_KEY + "HEADERH_" + THISFORM.getName() + getName(), FF.STR(HEADERH)));

        } catch (Exception e) {
            System.out.println("Не найдены свойства грида");
            return;
        }

    }

    public void SAVECOL() {//сохранение ширины колонок
        SAVECOL(0);
    }

    /**
     * Сохранение ширины и порядок колонок в таблице
     */
    public void SAVECOL(int LEVEL) {//сохранение ширины колонок
        Configs proper = new Configs(V.fileNameGridconf);
        boolean tf = true;
        int ind = 0;
        if (LEVEL == 0 || LEVEL == 1) { //ДЛЯ ШИРИНЫ И ПОСЛЕДОВАТЕЛЬНОСТИ КОЛОНОК
            for (int i = 1; i < this.getColumnModel().getColumnCount(); i++) {
                tf = tf && this.getColumnModel().getColumn(i).getWidth() == 75;
            }
            if (tf) {
                return;
            } //если все 75 не сохранять
            for (int i = 1; i <= GRID.FIELD.length; i++) {
                ind = GRID.getColumnModel().getColumn(i).getModelIndex() - 1;    //начальный номер колонки      
                proper.setProperty(SAVE_KEY + "WIDTH_" + THISFORM.getName() + getName() + "_" + GRID.FIELD[ind], new Integer(this.getColumnModel().getColumn(i).getWidth()).toString());
                proper.setProperty(SAVE_KEY + "NPP_" + THISFORM.getName() + getName() + "_" + GRID.FIELD[ind], new Integer(i).toString());
            }
            proper.setProperty("COLCOUNT_" + THISFORM.getName() + getName(), String.valueOf(GRID.FIELD.length)); //колмчество колонок

        } //ДЛЯ ШИРИНЫ И ПОСЛЕДОВАТЕЛЬНОСТИ КОЛОНОК
        if (LEVEL == 0 || LEVEL == 2) { //ДЛЯ ПОИСКА ПО УМОЛЧАНИЮ
          /*
             int col = -1;
             for (int i = 0; i < GRID.FIELD.length; i++) {
             if (GRID.FIELD[i].equals(GRID.LOCATEFOR)) {
             col = i;
             }
             }
             */
            int col = A.RECNO(GRID.ALIAS);
            try {
                if (!GRID.LOCATEFOR.equals("") && col > 0 && A.RECCOUNT(GRID.ALIAS) > 0) { //СОХРАНИТЬ ПОЗИЦИЮ В ТАБЛИЦЕ ПО ПОСЛЕДНЕМУ ЗНАЧЕНИЮ УКАЗАННОГО ПОЛЯ В GRID.LOCATEFOR
                    //    proper.setProperty("LOCATEFORNUM$" + THISFORM.getName() + getName(), String.valueOf(GRID.getValueAt(GRID.RECNO - 1, col + 1)));
                    proper.setProperty("LOCATEFORNUM$" + THISFORM.getName() + getName(), A.GETVALS(GRID.ALIAS + "." + GRID.LOCATEFOR));
                } else {
                    proper.setProperty("LOCATEFORNUM$" + THISFORM.getName() + getName(), String.valueOf("Nulll"));
                }
            } catch (Exception ex) {
                proper.setProperty("LOCATEFORNUM$" + THISFORM.getName() + getName(), String.valueOf("Nulll"));
            }
            proper.setProperty("LOCATEFOR$" + THISFORM.getName() + getName(), GRID.LOCATEFOR);
        } //ДЛЯ ПОИСКА ПО УМОЛЧАНИЮ

        if ((LEVEL == 0 || LEVEL == 3) && !"2".equals(RESTORESORT)) { //ДЛЯ СОРТИРОВКИ RESTORESORT==0 ТО СОРХАРАНЯЕМ ПУСТУЮ СОРТИРОВКУ RESTORESORT==1 СОХРАНЯЕМ ТЕКУЩУЮ RESTORESORT==2 НИЧЕГО НЕ ДЕЛАЕМ
            List keysort = SORTER.getSortKeys();
            String sort = "";
            if ("1".equals(RESTORESORT) && keysort.size() != 0 && keysort.get(0) != null) {
                SortKey sk = (SortKey) keysort.get(0);
                if (sk.getSortOrder() == SortOrder.ASCENDING) {
                    sort = "-1";
                } else {
                    sort = "-2";
                }
                sort = sort + sk.getColumn();
            }
            proper.setProperty(SAVE_KEY + "SORT_" + THISFORM.getName() + getName(), sort); // сохраняем сортировку
        }//ДЛЯ СОРТИРОВКИ 
        if (LEVEL == 0 || LEVEL == 4) { //ДЛЯ  ВИДА СОРТИРОВКИ 
            proper.setProperty(SAVE_KEY + "RESTORESORT_" + THISFORM.getName() + getName(), RESTORESORT); // сохраняем сортировку
        }
        if (LEVEL == 0 || LEVEL == 5) { //ДЛЯ  ВЫСОТЫ ШАПКИ
            proper.setProperty(SAVE_KEY + "HEADERH_" + THISFORM.getName() + getName(), FF.STR(HEADERH)); // сохраняем сортировку
        }
        proper.saveProperties("Grid Configuration");

    }

    public void SAVECOL_old() {//сохранение ширины колонок
        boolean tf = true;
        for (int i = 1; i < this.getColumnModel().getColumnCount(); i++) {
            tf = tf && this.getColumnModel().getColumn(i).getWidth() == 75;
        }
        if (tf) {
            return;
        } //если все 75 не сохранять
        Configs proper = new Configs(V.fileNameGridconf);
        for (int i = 0; i < this.getColumnModel().getColumnCount(); i++) {
            proper.setProperty(SAVE_KEY + "WIDTH_" + THISFORM.getName() + getName() + i, new Integer(this.getColumnModel().getColumn(i).getWidth()).toString());
            proper.setProperty(SAVE_KEY + "NPP_" + THISFORM.getName() + getName() + i, new Integer(this.getColumnModel().getColumn(i).getModelIndex()).toString());
        }
        proper.setProperty(SAVE_KEY + "COL_" + THISFORM.getName() + getName(), new Integer(GRID.getSelectedColumn()).toString());
        proper.setProperty(SAVE_KEY + "ROW_" + THISFORM.getName() + getName(), new Integer(GRID.getSelectedRow()).toString());
        int col = -1;
        for (int i = 0; i < GRID.FIELD.length; i++) {
            if (GRID.FIELD[i].equals(GRID.LOCATEFOR)) {
                col = i;
            }
        }
        try {
            if (!GRID.LOCATEFOR.equals("") && col != -1 && A.RECCOUNT(GRID.ALIAS) > 0) {
                proper.setProperty("LOCATEFORNUM$" + THISFORM.getName() + getName(), String.valueOf(GRID.getValueAt(GRID.RECNO - 1, col + 1)));
            } else {
                proper.setProperty("LOCATEFORNUM$" + THISFORM.getName() + getName(), String.valueOf("Nulll"));
            }
        } catch (Exception ex) {
            proper.setProperty("LOCATEFORNUM$" + THISFORM.getName() + getName(), String.valueOf("Nulll"));
        }
        proper.setProperty("LOCATEFOR$" + THISFORM.getName() + getName(), GRID.LOCATEFOR);
        proper.saveProperties("Grid Configuration");

    }

    public void RESTORECOL_old() {//восстановление ширины колонок

        Configs proper = new Configs(V.fileNameGridconf);

        try {
            for (int i = 1; i < this.getColumnModel().getColumnCount(); i++) {
                if ("".equals(proper.getProperty(SAVE_KEY + "WIDTH_" + THISFORM.getName() + getName() + i, ""))) {
                    return;//ЕСЛИ ЕСТЬ ПУСТОЕ ЗНАЧЕНИЕ (ДОБАВИЛИ ПОЛЕ , ТО ВОССТАНОВЛЕНИЕ РАЗМЕРОВ НЕ ДЕЛАЕМ
                }

            }
            for (int i = 1; i < this.getColumnModel().getColumnCount(); i++) {
                int x = new Integer(proper.getProperty(SAVE_KEY + "WIDTH_" + THISFORM.getName() + getName() + i, "0"));
                if (x == 75) {
                    x = 150;
                }
                if (x > 0) {
                    this.getColumnModel().getColumn(i).setPreferredWidth(x);
                }

            }

            String moveCol = "";
            for (int i = 1; i < this.getColumnModel().getColumnCount(); i++) {
                int npp = new Integer(proper.getProperty(SAVE_KEY + "NPP_" + THISFORM.getName() + getName() + i, "-1"));
                if (npp != -1 && FF.AT("," + i + "-" + npp, moveCol) == 0) {
                    //    this.getColumnModel().getColumn(i).setModelIndex(npp);

                    this.getColumnModel().moveColumn(i, npp);
                    moveCol = moveCol + "," + npp + "-" + i;
                }

            }

            int x = new Integer(proper.getProperty(SAVE_KEY + "ROW_" + THISFORM.getName() + getName()));
            int y = new Integer(proper.getProperty(SAVE_KEY + "COL_" + THISFORM.getName() + getName()));
            if (!GRID.LOCATEFOR.equals("")) {
                GRID.LOCATEFOR_NUM = proper.getProperty("LOCATEFORNUM$" + THISFORM.getName() + getName());
            }
//                this.changeSelection(x, y, false, false);
        } catch (Exception e) {
            System.out.println("Не найдены свойства грида");
        }

    }

    @Override
    public boolean isCellEditable(int row, int column) {
//        try {
        //      if (FIELD[column-1].contains("BIT_") && !getName().equals("g_view")  ) {
        //        return false;
        //   }
        //  } catch(Exception ex){}
        return super.isCellEditable(row, column);
    }

}//

