/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import baseclass.Buttonr;
import baseclass.Fieldr;
import baseclass.Formr;
import baseclass.Gridr;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
//import org.apache.poi.ss.usermodel.Color;
import prg.A;
import prg.FF;
import prg.P;
import prg.S;
import prg.V;
import support.Help;

/**
 *
 * @author dima
 */
public class ToolBarr extends JToolBar {

    public Formr THISFORM; //ссылка на родительскую форму
    private Gridr GRID = null; //ссылка на текущий grid
    public JButton WIZARD, TOOLS, QUERY, UPDATE, INSERT, DELETE, EXCEL, FILTR, SEARCH, PRINT, DEPLOY, STATUS, RUNLOC, WINDOWS, GRIDFILTER, HELP,PRTSC,REFRESH,REFRESH_RUN;
    public JCheckBox INFO;
    public Fieldr SEARCH_TXT;
    public ToolBarr tb;
    public Formr FHIST;

    public void SETENABLE(Gridr GRID) {
        WIZARD.setEnabled(V.USER_ADMIN == 3);

        QUERY.setEnabled(GRID != null);
        EXCEL.setEnabled(GRID != null);
        SEARCH_TXT.setEnabled(GRID != null);
        FILTR.setEnabled(GRID != null);
        SEARCH.setEnabled(GRID != null);
        GRIDFILTER.setEnabled(GRID != null);
        REFRESH.setEnabled(GRID != null && GRID.ENABLE_REFRESH); //АВТООБНОВЛЕНИЕ ГРИДА
        /*if (GRID != null ) {
        P.prn(GRID.getName());
        }*/

        boolean tf = true;
        if (GRID == null || GRID.REFRESH_RUN==false) {
            tf = false;
        }
           REFRESH.setOpaque(tf);
        
        tf = true;
        if (GRID == null || GRID.FREAD == null || GRID.FREAD == "") {
            tf = false;
        }
        INSERT.setEnabled(tf && GRID.ENABLE_INSERT);
        UPDATE.setEnabled(tf && GRID.ENABLE_READ);
        DELETE.setEnabled(tf && GRID.ENABLE_DELETE);
        tf = true;
        if (GRID == null || GRID.ALIAS == null || GRID.ALIAS == "" || A.RECCOUNT(GRID.ALIAS) == 0) {
            tf = false;
        }
        DEPLOY.setEnabled(tf && GRID.ENABLE_DEPLOY);
        STATUS.setEnabled(tf && GRID.ENABLE_STATUS);
        RUNLOC.setEnabled(tf);

        if (Help.isAvailable()) {
            if (HELP == null) {
                initializeHelp();
                this.add(HELP);
            }
            HELP.setEnabled(true);
        }
    }

    public ToolBarr(String name, int width, int height) {//иницилизация имени  заголовка размеров
        this.tb = this;
        super.setName(name);
        super.setSize(width, height);
        super.setOpaque(true);
        super.setBackground(V.COLORB_TOOLBAR);
        P.prn(getClass().getResource("/icons/Misc-Database-3-icon.png").toString());
        WIZARD = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/Misc-Database-3-icon.png")));
        WIZARD.setToolTipText("Список таблиц базы данных");
        WIZARD.addActionListener(new ActionListener() {//слушатель кнопки
            @Override
            public void actionPerformed(ActionEvent e) {
                BROWSE();
            }
        });

//        TOOLS = new JButton(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Preferences24.gif")));
//     TOOLS=new JButton(new javax.swing.ImageIcon("icons/TOOLS.png"));
        TOOLS = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/Misc-Settings-icon.png")));
        TOOLS.setToolTipText("Инструментарий и текущие настройки интерфейса");
        TOOLS.addActionListener(new ActionListener() {//слушатель кнопки
            @Override
            public void actionPerformed(ActionEvent e) {
                TOOLS(0);
            }
        });
        this.add(new JSeparator(SwingConstants.VERTICAL));
//        QUERY = new JButton(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Refresh24.gif")));
        QUERY = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/view-refresh.png")));
        QUERY.setToolTipText("Обновить выборку данных в таблице");
        QUERY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QUERY(null);
            }
        });
//       INSERT=new JButton(new javax.swing.ImageIcon("icons/DT_INS.png"));        
//        INSERT = new JButton(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/New24.gif")));
//        INSERT = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/document-new.png")));
        INSERT = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/Button-Add-icon.png")));
        INSERT.setToolTipText("Новая запись ");
        INSERT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                INSERT();
            }
        });
//      UPDATE=new JButton(new javax.swing.ImageIcon("icons/DT_EDY.png"));        
//        UPDATE = new JButton(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Edit24.gif")));
        UPDATE = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/Pen-Blue-icon.png")));
        UPDATE.setToolTipText("Редактирование текущей записи ");
        UPDATE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UPDATE();
            }
        });
//        DELETE=new JButton(new javax.swing.ImageIcon("icons/DT_DEL.png"));        
//        DELETE = new JButton(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/Remove24.gif")));
//        DELETE = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/Button-Delete-icon.png")));
        DELETE = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/delete-icon.png")));
        DELETE.setToolTipText("Удаление текущей записи ");
        DELETE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DELETE();
            }
        });

        EXCEL = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/excel3.png")));
        EXCEL.setToolTipText("Экспорт текущей таблицы в EXEL ");
        EXCEL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EXCEL();
            }
        });

//        FILTR = new JButton(new javax.swing.ImageIcon(getClass().getResource("/nuvola/22x22/apps/package_toys.png")));
        FILTR = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/view-filter.png")));
        FILTR.setToolTipText("Фильтр по колонке");
        FILTR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FILTR();
            }
        });

        SEARCH = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/Search-icon.png")));
//        SEARCH = new JButton(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/general/FindAgain24.gif")));
        SEARCH.setToolTipText("Поиск");
        SEARCH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SEARCH();
            }
        });

        SEARCH_TXT = new Fieldr("SEARCH_TXT", "", 100, 27);
        SEARCH_TXT.TYPE = V.TYPE_CHAR_TOOLBAR;
        SEARCH_TXT.setPreferredSize(new Dimension(100, 27));
        SEARCH_TXT.setMaximumSize(new Dimension(100, 27));
        SEARCH_TXT.setToolTipText("Поиск");
        SEARCH_TXT.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    ArrayList<String> arr;
                    arr = P.SHOW_TXT_HISTORY(tb, SEARCH_TXT);
                    String[] historyArr = new String[arr.size()];
                    int ind = 0;
                    for (int i = arr.size() - 1; i >= 0; i--) {
                        historyArr[ind] = arr.get(i);
                        ind++;
                    }
                    try {
                        SEARCH_TXT.setText(historyArr[P.MENU(historyArr, SEARCH_TXT) - 1]);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                    }
                }
            }
        });

        SEARCH_TXT.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SEARCH();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        if (V.INFO_TXT_SCROLL != null) {
            INFO = new JCheckBox("Инфопанель", V.INFO_FLAG_ENV);
            if (V.INFO_FLAG_ENV) {
                INFO.setToolTipText("Убрать инфопанель");
            } else {
                INFO.setToolTipText("Показать инфопанель");
                V.INFO_TXT_SCROLL.setVisible(false);
            }
            INFO.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (INFO.isSelected()) {
                        INFO.setToolTipText("Убрать инфопанель");
                        V.INFO_FLAG_ENV = true;
                        V.INFO_TXT_SCROLL.setVisible(true);
                        if (V.ACTIVEFORM.isMaximum() == true) {
                            V.ACTIVEFORM.setBounds(V.ACTIVEFORM.getX(), 30, V.ACTIVEFORM.getWidth(), V.ACTIVEFORM.getHeight() - V.INFO_TXT_SCROLL.getHeight());
                        }
                        Iterator entries = V.MAPFORMS.entrySet().iterator();
                        while (entries.hasNext()) {
                            Map.Entry thisEntry = (Map.Entry) entries.next();
                            Formr form = (Formr) thisEntry.getValue();
                            if (form.getHeight() >= V._SCREEN.getHeight() - 115 - V.INFO_TXT_SCROLL.getHeight()) {
                                form.setBounds(form.getX(), form.getY(), form.getWidth(), V._SCREEN.getHeight() - 115 - V.INFO_TXT_SCROLL.getHeight());
                            }
                        }
                    } else {
                        V.INFO_FLAG_ENV = false;
                        INFO.setToolTipText("Показать инфопанель");
                        V.INFO_TXT_SCROLL.setVisible(false);
                        if (V.ACTIVEFORM!=null && V.ACTIVEFORM.isMaximum() == true) {
                            V.ACTIVEFORM.setBounds(V.ACTIVEFORM.getX(), 30, V.ACTIVEFORM.getWidth(), V.ACTIVEFORM.getHeight() + V.INFO_TXT_SCROLL.getHeight());
                        }
                    }
                }
            });
        }
        PRINT = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.png")));
        PRINT.setToolTipText("Печать");
        PRINT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PRINT(null);
            }
        });

        DEPLOY = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/Document-icon.png")));
        DEPLOY.setToolTipText("Раскрыть документ");
        DEPLOY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DEPLOY(null);
            }
        });

//        STATUS = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/File-Open-icon.png")));
        STATUS = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/Button-Delete-icon.png")));
        STATUS.setToolTipText("Изменить статус документа");
        STATUS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                STATUS(null);
            }
        });

        RUNLOC = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/format-list-ordered.png")));
        RUNLOC.setToolTipText("Показ данных по текущему ТМЦ");
        RUNLOC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //ARTINF(null, 0);
                //boolean RUN = V.Loc1.RUN();

                //tf = LOC.ARTINF(GRID,NOMER,RUNLOC );                
                Class c = null;  // иницилизация с                     //
                try {
                    c = Class.forName("prg.LOC"); //загрузка класса по имени 
                    c.newInstance();//создаем новый объект 
                } catch (ClassNotFoundException ex) {//без этого не работает Class.forName(pathform)
                    P.MESSERR("Ошибка загрузки программы: " + "<prg.LOC>");
                    return;
                } catch (InstantiationException ex) {
                    P.MESSERR("Ошибка иницилизации программы: " + "<prg.LOC>");
                    return;
//                P.MESSERR("Ошибка выполнения программы: " + "<prg.LOC>");
                    //              return ;

                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ToolBarr.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
//document-multiple
        WINDOWS = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/document-multiple.png")));
        WINDOWS.setToolTipText("Выбор из списка активных окон");
        WINDOWS.addActionListener(new ActionListener() {//слушатель кнопки
            @Override
            public void actionPerformed(ActionEvent e) {
                WINDOWS();
            }
        });
        
        GRIDFILTER = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/table-add-icon.png")));
        GRIDFILTER.setToolTipText("Форма выбора критерия для таблицы");
        GRIDFILTER.addActionListener(new ActionListener() {//слушатель кнопки
            @Override
            public void actionPerformed(ActionEvent e) {
                GRIDFILTER(null);
            }
        });
        //СНИМОК ЭКРАНА
        if (getClass().getResource("/icons/Screenshot_icon-icons.png")==null) {
         PRTSC = new JButton("PrtSc");
        } else {
         PRTSC = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/Screenshot_icon-icons.png")));
        }
        PRTSC.setToolTipText("Снимок окна программы в буфер обмена и файл screen.png");
        PRTSC.addActionListener(new ActionListener() {//слушатель кнопки
            @Override
            public void actionPerformed(ActionEvent e) {
               P._SCREEN_TO_FILE(1); //СНИМОК ЭКРАНА
            }
        });
        //автообновление данных в таблице
        if (getClass().getResource("/icons/icons8-refresh-24.png")==null) {
         REFRESH = new JButton("RefResh");
        } else {
         REFRESH = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-refresh-24.png")));
        }
         REFRESH.setBackground(Color.GREEN);

        REFRESH.setToolTipText("Автообновление данных текущей таблицы");
        REFRESH.addActionListener(new ActionListener() {//слушатель кнопки
            @Override
            public void actionPerformed(ActionEvent e) {
            if (VALIDGRID() == false) {
              return;
            }
              GRID.SETREFRESH_RUN(); //автообновление данных в таблице
            }
        });
        /*
         REFRESH_RUN = new JButton("123");
         REFRESH_RUN.setOpaque(true);
         REFRESH_RUN.setBackground(Color.red);
         */
        if (Help.isAvailable()) {
            initializeHelp();
        }

        this.add(WIZARD);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(TOOLS);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(QUERY);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(INSERT);
        this.add(UPDATE);
        this.add(DELETE);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(EXCEL);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(SEARCH_TXT);
        this.add(SEARCH);
        this.add(FILTR);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        if (INFO != null) {
            this.add(INFO);
        }
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(PRINT);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(DEPLOY);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(STATUS);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(RUNLOC);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(WINDOWS);
        this.add(new JSeparator(SwingConstants.VERTICAL));
        this.add(GRIDFILTER);
        if (HELP != null) {
            this.add(new JSeparator(SwingConstants.VERTICAL));
            this.add(HELP);
        }
        this.add(PRTSC);
        this.add(REFRESH);
        //this.add(REFRESH_RUN);

        SETENABLE(null);
        // THISFORM.getContentPane().add(this,BorderLayout.NORTH);
        //getContentPane().add(this, BorderLayout.NORTH);
        this.setFloatable(false);//возможность перетаскивания
        V._SCREEN.add(this, V.LAYER_INF);

    }

    public void TOOLS(int NOMER) {//список ширины колонок
        int n = 0;
        if (NOMER == 0) {
            String[] strm = {"Программный код описания колонок", "Вернуть исходные размеры формы", "Вернуть исходные параметры грида", "Информация по текущему объекту", "История запросов", "Просмотр данных текущего грида", "Данные по соединению", "Сохранять последнюю сортировку для текущей таблицы", "Установить текущую сортировку по умолчанию", "Отменить сохранение сортировки в текущей таблице", "Изменить высоту шапки таблицы", "Скрипт последнего запроса в ClipBoard"};
            if (V.USER_ADMIN != 3) { //если не админ
                strm[0] = "<>" + strm[0];
                strm[3] = "<>" + strm[3];
                strm[4] = "<>" + strm[4];
                strm[5] = "<>" + strm[5];
                strm[6] = "<>" + strm[6];
                strm[11] = "<>" + strm[11];

            }
            GRID = V.ACTIVEGRID;
            if (GRID == null || GRID.SORTER == null || GRID.SORTER.getSortKeys().size() == 0) { // если нет сортировки
                strm[7] = "<>" + strm[7];
                strm[8] = "<>" + strm[8];
            }
            if (GRID == null || GRID.THISFORM != V.ACTIVEFORM) { // высоту шапки
                strm[1] = "<>" + strm[1];
                strm[2] = "<>" + strm[2];
                strm[10] = "<>" + strm[10];

            } else {
                strm[1] = strm[1] + " " + GRID.THISFORM.SAVE_KEY;
                strm[2] = strm[2] + " " + GRID.SAVE_KEY;
                strm[10] = strm[10] + " тек." + GRID.HEADERH + " pix";
            }

            n = P.MENU(strm, WIZARD);
            if (n == 0) {
                return;
            }
        } else {
            n = NOMER;
        }
//         JOptionPane.showMessageDialog(null,n, "Внимание!!!",  0); 
        if (n == 1) { //ширина колонок
            GRID = V.ACTIVEGRID;
            if (GRID == null || GRID.THISFORM != V.ACTIVEFORM) {
                return;
            }
            String str1 = "         String C[]={";
            String str2 = "         String F[]={";
            String str3 = "         int S[]={";
            int ind = 0;
            for (int i = 1; i < GRID.getColumnModel().getColumnCount(); i++) {
                ind = GRID.getColumnModel().getColumn(i).getModelIndex() - 1;
                str1 = str1 + "\"" + GRID.CAPTION[ind] + "\",";
                str2 = str2 + "\"" + GRID.FIELD[ind] + "\",";
                str3 = str3 + new Integer(GRID.getColumnModel().getColumn(i).getWidth()).toString();
                str3 = str3 + ',';
            }
            str1 = FF.SUBSTR(str1, 1, FF.LEN(str1) - 1);
            str1 = str1 + "};" + "\n";
            str2 = FF.SUBSTR(str2, 1, FF.LEN(str2) - 1);
            str2 = str2 + "};" + "\n";

            str3 = FF.SUBSTR(str3, 1, FF.LEN(str3) - 1);
            str3 = str3 + "};" + "\n";
            FF._CLIPTEXT(str1 + str2 + str3);
            JOptionPane.showMessageDialog(null, str1 + str2 + str3, "Внимание!!!", 0);
        }
        if (n == 2) { //начальные размеры формы
            GRID = V.ACTIVEGRID;
            if (GRID == null || GRID.THISFORM != V.ACTIVEFORM) {
                return;
            }
            int xx = GRID.THISFORM.XX;
            int yy = GRID.THISFORM.YY;
            GRID.THISFORM.setSize(xx, yy);
        }
        if (n == 3) { //начальные размеры колонок грида
            GRID = V.ACTIVEGRID;
            if (GRID == null || GRID.THISFORM != V.ACTIVEFORM) {
                return;
            }
            int x = 0;
            for (int i = 1; i < GRID.getColumnModel().getColumnCount(); i++) {
                if (GRID.FSIZE[i - 1] == 0) {
                    x = V.COLWIDTH;
                } else {
                    x = GRID.FSIZE[i - 1];
                }
                GRID.getColumnModel().getColumn(i).setPreferredWidth(x);
                GRID.getColumnModel().getColumn(i).setModelIndex(i);
            }
        }
        if (n == 4) { //информация по текущему объекту
            String str = "";
            str = str + "Активная форма заголовок: " + V.ACTIVEFORM.getTitle() + "\n";
            str = str + "Название файла класса формы: " + V.ACTIVEFORM.getClass().getName() + "\n";
            str = str + "Имя переменной формы: " + V.ACTIVEFORM.getName() + "\n" + "\n";
            str = str + "Информация по активному экрану: " + V._SCREEN.INF1.getText() + "\n";
            str = str + "Информация по активной форме  : " + V._SCREEN.INF2.getText() + "\n";
            str = str + "Информация по активному гриду : " + V._SCREEN.INF3.getText() + "\n";
            str = str + "Информация по активному алиасу: " + V._SCREEN.INF4.getText() + "\n";

            P.MESS(str);
        }
        if (n == 5) {
            if (FHIST != null) {
                FHIST.DESTROY();
                FHIST = null;
            }
            A.CLOSE("HISTORY");
            FHIST = P.DOFORM("History");
        }
        if (n == 6) {
            if (VALIDGRID() == false) {
                return;
            }

            V.PARAMTO = new String[2];
            V.PARAMTO[0] = GRID.ALIAS;
            V.PARAMTO[1] = V.ALIAS;
            if (V.FBROWSE != null) {
                V.FBROWSE.DESTROY();
                V.FBROWSE = null;
            }
            V.FBROWSE = P.DOFORM("browse");
        }

        if (n == 7) { //
            String STR = "";
            STR = STR + V.CONN_SERVER + "\n";
            FF._CLIPTEXT(STR);
            STR = STR + V.CONN_PORT + "\n";
            STR = STR + V.CONN_BASA + "\n";
            STR = STR + V.CONN_USER + "\n";
//            STR = STR +V.CONN_PASS + "\n";
            STR = STR + P.macAddress() + "\n";

            P.MESS(STR, "Данные по соединению ");

        }

        if (n == 8) { //СОХРАНЯТЬ ПОСЛЕДНЮЮ СОРТИРОВКУ
            GRID.RESTORESORT = "1";
            GRID.SAVECOL();
        }
        if (n == 9) { //УСТАНОВИТЬ ТЕКУЩУЮ СОРТИРОВКУ ПО УМОЛЧАНИЮ 
            GRID.RESTORESORT = "1";
            GRID.SAVECOL(3); //СОХРАНИЛИ ТОЛЬКО СОРТИРОВКУ
            GRID.RESTORESORT = "2";
            GRID.SAVECOL(4); //СОХРАНИЛИ ВИД СОРТИРОВКИ RESTORESORT="2" (НЕ БУДЕТ СОХРАНЯТЬ СОРТИРОВКУ)

        }

        if (n == 10) { //ОТМЕНИТЬ СОРТИРОВКУ
            //  GRID.SORTER = new TableRowSorter<TableModel>(GRID.MODEL);
            //  GRID.setRowSorter(GRID.SORTER);
            GRID.RESTORESORT = "0";
            GRID.SAVECOL();
            //  GRID.THISFORM.QUERY(GRID);
        }
        if (n == 11) { //ИЗМЕНИТЬ ВЫСОТУ ШАПКИ
            Object[] objArr = new Object[4];
            objArr[0] = "Высота шапки таблицы";
            objArr[1] = V.TYPE_NUMERIC;
            objArr[2] = "999";
            objArr[3] = GRID.HEADERH;

            P.DOCORRECT("Введите данные", objArr);
            if (V.PARAMOT == null || !V.PARAMOT[0].equals("T")) {
                return;
            }
            int h = FF.VAL(V.PARAMOT[1]);
            if (h < 10 || h > 300) {
                P.MESSERR("Значение может быть только в пределах 10-300");
                return;
            }
            GRID.SETHEADERH(h);
            GRID.SAVECOL();
            GRID.THISFORM.INITCOLUMN(GRID);
        }
        if (n == 12) { //запрос в clipboard
            FF._CLIPTEXT(GRID.SELE);
            P.MESS(GRID.SELE, "Запрос для " + GRID.getName());
        }

    }

    public void QUERY(Gridr G) {//обновление данных в таблице
        if (G == null) {
            if (VALIDGRID() == false) {
                return;
            }
        } else {
            GRID = G;
        }
        //  int REC = A.RECNO(GRID.ALIAS);
        GRID.THISFORM.QUERY(GRID);
        //обновление комбобоксов/кнопок в гриде, если есть---
        int count = GRID.COLUMN_CONTROL.size();
        for (int i = 0; i < count; i++) {
            Object obj = GRID.COLUMN_CONTROL.get(i).get(1);
            if (obj.getClass().getName().equals("javax.swing.JComboBox")) {
                JComboBox cbe = (JComboBox) obj;
                GRID.getColumn((int) GRID.COLUMN_CONTROL.get(i).get(0)).SETCOLUMNCONTROL(cbe, GRID);
            }
            if (obj.getClass().getName().equals("javax.swing.JButton")) {
                JButton but = (JButton) obj;
                GRID.getColumn((int) GRID.COLUMN_CONTROL.get(i).get(0)).SETCOLUMNCONTROL(but, GRID);
            }
            if (obj.getClass().getName().equals("baseclass.Buttonr")) {
                Buttonr but = (Buttonr) obj;
                GRID.getColumn((int) GRID.COLUMN_CONTROL.get(i).get(0)).SETCOLUMNCONTROL(but, GRID);
            }
        }
        while (GRID.COLUMN_CONTROL.size() != count) {
            GRID.COLUMN_CONTROL.remove(GRID.COLUMN_CONTROL.size() - 1);
        }
        //---------------------------------------------------
        //GRID.GORECNO(REC);
        //GRID.SETFOCUS();

    }

    public void INSERT() {
        INSERT(null);
    }

    public void INSERT(Gridr G) {
        if (G == null) {
            if (VALIDGRID() == false) {
                return;
            }
        } else {
            GRID = G;
            S.SETACTIVEGRID(GRID);
        }

        if (GRID.FREAD == null || GRID.FREAD == "") {
            JOptionPane.showMessageDialog(null, "Не задана форма корректуры ", "Внимание!!!", 0);
            return;
        }
        V.U_I_D = 1;
        GRID.THISFORM.READ_VID = 1; //ВОЗВРАЩАЕМ В РОДИТЕЛЬСКУЮ ФОРМУ ВИД КОРРЕКТУРЫ
        GRID.THISFORM.PREV_UPDATE();
        if (!this.INSERT.isEnabled() && G == null) {
            return;
        }
        P.DOFORM(GRID.FREAD);
//        GRID.GORECNO(GRID.DATA.ROWCOUNT);
        //   P.SQLEXECT("select '" + GRID.getName() + "' gridname,'" + THISFORM.getName() + "' thisname from dual");
        GRID.THISFORM.READ_AFTER(GRID); // ВЫЗЫВАЕТСЯ МЕТОД ИЗ РОДИТЕЛЬСКОЙ ФОРМЫ
        GRID.SETFOCUS();
    }

    public void UPDATE() {
        UPDATE(null);
    }

    public void UPDATE(Gridr G) {
        if (G == null) {
            if (VALIDGRID() == false) {
                return;
            }
        } else {
            GRID = G;
            S.SETACTIVEGRID(GRID);
        }
        if (GRID.FREAD == null || GRID.FREAD == "") {
            JOptionPane.showMessageDialog(null, "Не задана форма корректуры ", "Внимание!!!", 0);
            return;
        }

        V.U_I_D = 2;
        GRID.THISFORM.READ_VID = 2; //ВОЗВРАЩАЕМ В РОДИТЕЛЬСКУЮ ФОРМУ ВИД КОРРЕКТУРЫ
        GRID.THISFORM.PREV_UPDATE();
        if (!this.UPDATE.isEnabled() && G == null) {
            return;
        }
        int REC = A.RECNO(GRID.ALIAS);
        if (GRID.PREV_QUERY_REC_READ == true) { //если обновление записи перед корректурой 
            GRID.FORM_K = null;
            GRID.THISFORM.QUERY_REC(GRID, REC);          // обновление  записи
        }
        P.DOFORM(GRID.FREAD);
        GRID.GORECNO(REC);
        GRID.SETFOCUS();
        GRID.THISFORM.READ_AFTER(GRID); // ВЫЗЫВАЕТСЯ МЕТОД ИЗ РОДИТЕЛЬСКОЙ ФОРМЫ
    }

    public void DELETE() {
        DELETE(null);
    }

    public void DELETE(Gridr G) {
        if (G == null) {
            if (VALIDGRID() == false) {
                return;
            }
        } else {
            GRID = G;
            S.SETACTIVEGRID(GRID);
        }
        if (GRID.FREAD == null || GRID.FREAD == "") {
            JOptionPane.showMessageDialog(null, "Не задана форма корректуры ", "Внимание!!!", 0);
            return;
        }

        V.U_I_D = 3;
        GRID.THISFORM.READ_VID = 3; //ВОЗВРАЩАЕМ В РОДИТЕЛЬСКУЮ ФОРМУ ВИД КОРРЕКТУРЫ
        GRID.THISFORM.PREV_UPDATE();
        if (!this.DELETE.isEnabled() && G == null) {
            return;
        }
        int REC = A.RECNO(GRID.ALIAS);
        P.DOFORM(GRID.FREAD);
        GRID.SETFOCUS();
        GRID.GORECNO(REC);
        GRID.THISFORM.READ_AFTER(GRID); // ВЫЗЫВАЕТСЯ МЕТОД ИЗ РОДИТЕЛЬСКОЙ ФОРМЫ
        GRID.FORM_K = null;
    }

    public void EXCEL() {
        if (VALIDGRID() == false) {
            return;
        }
        P.EXCEL_SAVING(GRID);
    }

    public void FILTR() {
        if (VALIDGRID() == false) {
            return;
        }
        for (int i = 0; i < V.ACTIVEGRID.getRowCount(); i++) {//высота строк грида
            V.ACTIVEGRID.setRowHeight(i, 22);
        }
        try {
            if (V.SELECTED_COLUMN != -1) {
                V.ACTIVEGRID.SORTER.setRowFilter(RowFilter.regexFilter("(?iu)" + SEARCH_TXT.getText(), V.ACTIVEGRID.getColumnModel().getColumnIndex(V.ACTIVEGRID.getColumnName(V.SELECTED_COLUMN))));
            } else {
                V.ACTIVEGRID.SORTER.setRowFilter(RowFilter.regexFilter("(?iu)" + SEARCH_TXT.getText()));
            }
        } catch (NullPointerException ex) {
        } catch (ArrayIndexOutOfBoundsException exc) {
            V.SELECTED_COLUMN = -1;
            V.ACTIVEGRID.SORTER.setRowFilter(RowFilter.regexFilter("(?iu)" + SEARCH_TXT.getText()));
        }
        SEARCH_TXT.setText("");
    }

    public void SEARCH() {
        if (VALIDGRID() == false) {
            return;
        }
        if (!SEARCH_TXT.getText().equals("")) {
            P.SAVE_TXT_HISTORY(this, SEARCH_TXT);
        }
        boolean searchFlag = false;
        int selRow = V.ACTIVEGRID.getSelectedRow();
        if (selRow + 1 == V.ACTIVEGRID.getRowCount()) {
            V.ACTIVEGRID.getSelectionModel().clearSelection();
            selRow = -1;
        }
        for (int rowInd = 0; rowInd < V.ACTIVEGRID.getRowCount(); rowInd++) {
            for (int colInd = 1; colInd < V.ACTIVEGRID.getColumnCount(); colInd++) {
                if (V.ACTIVEGRID.getValueAt(rowInd, colInd) != null
                        && V.ACTIVEGRID.getValueAt(rowInd, colInd).toString().toUpperCase().contains(SEARCH_TXT.getText().toUpperCase())) {
                    searchFlag = true;
                    break;
                }
            }
        }
        for (int rowInd = selRow + 1; rowInd < V.ACTIVEGRID.getRowCount(); rowInd++) {
            for (int colInd = 1; colInd < V.ACTIVEGRID.getColumnCount(); colInd++) {
                if (V.ACTIVEGRID.getValueAt(rowInd, colInd) != null
                        && V.ACTIVEGRID.getValueAt(rowInd, colInd).toString().toUpperCase().contains(SEARCH_TXT.getText().toUpperCase())) {
                    V.ACTIVEGRID.changeSelection(rowInd, colInd, true, true);
                    V.ACTIVEGRID.setRowSelectionInterval(rowInd, rowInd);
                    return;
                }
            }
            if (rowInd == V.ACTIVEGRID.getRowCount() - 1) {
                V.ACTIVEGRID.getSelectionModel().clearSelection();
                if (searchFlag) {
                    SEARCH();
                }
            }
        }
    }

    public void BROWSE() {//

        P.DOFORM("tablesql");

    }

    public void PRINT(Formr F) {
        if (F == null && V.ACTIVEFORM!=null) {
            V.ACTIVEFORM.PRINT();
        } else {
            F.PRINT();
        }
    }

    public void DEPLOY(Gridr G) {
        if (G == null) {
            if (VALIDGRID() == false) {
                return;
            }
        } else {
            GRID = G;
        }
        V.ACTIVEFORM.DEPLOY(GRID.getName());
    }

    public void STATUS(Gridr G) {
        if (G == null) {
            if (VALIDGRID() == false) {
                return;
            }
        } else {
            GRID = G;
        }

        V.ACTIVEFORM.STATUS(GRID.getName());

    }

    public void ARTINF(Gridr G, int NOMER) {//список ширины колонок
        if (G == null) {
            if (VALIDGRID() == false) {
                return;
            }
        } else {
            GRID = G;
        }
        try {
            boolean tf;
//            tf = LOC.ARTINF(GRID,NOMER,RUNLOC );
        } finally {
        }

    }

    public void WINDOWS() {
                    if ("1".equals(V.CONN_DRIVER)) { //если oracle нужно писать 
                     System.out.println(String.valueOf((int) ((double) ((Object[]) P.SQLEXECT("select USERENV ('sessionid') from dual").rowList.get(0))[0])));
                    }
                    if ("3".equals(V.CONN_DRIVER)) { //если postgreSQL нужно писать 
                     System.out.println(String.valueOf((int) ((double) ((Object[]) P.SQLEXECT("SELECT pg_backend_pid()").rowList.get(0))[0])));
                    }

        P.ACTIVEFROMS_MENU();
    }

    public void GRIDFILTER(Gridr G) {
        if (G == null) {
            if (VALIDGRID() == false) {
                return;
            }
        } else {
            GRID = G;
        }
        P.gridFilter(GRID);
    }

    private boolean VALIDGRID() {//
        GRID = V.ACTIVEGRID;
        if (GRID == null) {
            JOptionPane.showMessageDialog(null, "Выберите текущую таблицу ", "Внимание!!!", 0);
            return false;
        }
        THISFORM = GRID.THISFORM;
        if (THISFORM != V.ACTIVEFORM) {
            JOptionPane.showMessageDialog(null, "Форма текущей таблицы не активна ", "Внимание!!!", 0);
            return false;
        }

        return true;
    }

    private void initializeHelp() {
        HELP = new JButton(new javax.swing.ImageIcon(getClass().getResource("/icons/question.png")));
        HELP.setToolTipText("Помощь(F1)");
        HELP.addActionListener(new ActionListener() {//слушатель кнопки
            @Override
            public void actionPerformed(ActionEvent e) {
                Help.execute(V.ACTIVEFORM == null ? null : V.ACTIVEFORM.HELP);
            }
        });
    }
}
