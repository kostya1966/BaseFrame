package baseclass;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import forms.CipherForm;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import prg.A;
import prg.FF;
import prg.P;
import prg.S;
import prg.V;

/**
 * Базовый класс формы
 *
 * @author Kostya
 */
public abstract class Formr extends javax.swing.JInternalFrame {

    public int XX;//начальная ширина
    public int YY;//начальная высота
    public int RESIZABLE = 1; // 1- изменение размеров 0 - без изменения размеров
    public int MODAL = V.MODAL_NOT; // 1- модальная 0 - не модальная
    public int ESC = 1;// 1 - при нажатии ESC - закрытие формы 
    public int EXIT = 0;// 1 - закрытие формы 
    public Formr THISFORM;       //ТЕКУЩАЯ ФОРМА
    public Formr PARENTFORM;     //ВЫЗВАВШАЯ ФОРМА
    public Formr PREVFORM;     //ПРЕДЫДУЩАЯ ФОРМА
    public Buttonr B[]; //резерв кнопки
    public Labelr L[]; //резерв надписи
    public Textr T[]; //резерв текст
    public Editr E[]; //резерв текст
    public Fieldr F[]; //резерв поля
    public Gridr G[]; //резерв гриды таблиц
    public JRadioButtons O[]; //резерв радиокнопок
    public CheckBoxr CH[]; //резерв чекбоксов
    public ComboBoxr CB[]; //резерв комбо
    public ListBoxr LB[]; // резерв листбоксов
    public Treer TR[];
    public Fieldrp PF[];
    public JPanelr J[];
    public static ArrayList<Gridr> GRIDS = new ArrayList<Gridr>();// список загруженных таблиц
    public static ArrayList<Treer> TREES = new ArrayList<>();// список загруженных таблиц
    public boolean SPR = false;//является ли форма вызываемым справочником в поле ввода по умолчанию нет
    public boolean ENABLE_PRINT = false;//ВЫЗОВ МЕТОДА PRINT ИЗ ТУЛБАРА ЗАПРЕЩЕН
    public boolean MAXIMIZED = false;
    public boolean VALIDON = true;//признак проверки при выходе из поля 
    public Buttonr B_EXIT = null; // кнопка выхода 
    public Component LOCOBJR = null; // при расположении последний элемент вправо locl
    public Component LOCOBJD = null; // при расположении последний элемент вниз  locd    
    public boolean READ_OK = false;//если на форме корректуры , вызванной из данной формы была нажата кнопка подтверждения то true иначе и по умолчанию false
    public int READ_VID = 0; //последний вид корреетуры 1- вставка 2- корректрура 3- удаление
    public String SAVE_KEY = "";
    public String HELP = ""; //Название страницы в файле справки, например: index или index.html

    public int INT1;//СЛУЖЕБНЫЙ ДЛЯ ИСПОЛЬЗОВАНИЯ В ДОЧЕРНИХ ФОРМАХ 
    public int INT2;//СЛУЖЕБНЫЙ ДЛЯ ИСПОЛЬЗОВАНИЯ В ДОЧЕРНИХ ФОРМАХ 
    public String STR1;//СЛУЖЕБНЫЙ ДЛЯ ИСПОЛЬЗОВАНИЯ В ДОЧЕРНИХ ФОРМАХ 
    public String STR2;//СЛУЖЕБНЫЙ ДЛЯ ИСПОЛЬЗОВАНИЯ В ДОЧЕРНИХ ФОРМАХ 
    public String STR3;//СЛУЖЕБНЫЙ ДЛЯ ИСПОЛЬЗОВАНИЯ В ДОЧЕРНИХ ФОРМАХ 
    public String STR4;//СЛУЖЕБНЫЙ ДЛЯ ИСПОЛЬЗОВАНИЯ В ДОЧЕРНИХ ФОРМАХ 
    public String STR5;//СЛУЖЕБНЫЙ ДЛЯ ИСПОЛЬЗОВАНИЯ В ДОЧЕРНИХ ФОРМАХ 

    public Timer timer1; //СЛУЖЕБНЫЙ ДЛЯ ИСПОЛЬЗОВАНИЯ В ДОЧЕРНИХ ФОРМАХ 

    public void SETSPR(boolean tf) {
        SPR = tf;
        if (SPR == true) {//ЕСЛИ ФОРМА ВЫЗЫВАЕМЫЙ СПРАВОЧНИК ИЗ ПОЛЯ ВВОДА 
            MODAL = 1;
//         P.STARTMODAL(this);// устанавливаем модальность
        }
    }    // 

    public void LOC_ABOUT() {
        if (G[0] != null) {
            G[0].SCROLL.setBounds(1, 1, this.getWidth() - 20, this.getHeight() - 80);
            if (B[32] != null) {
                locate(B[32], null, V.LOC_CENTR, 0, G[0].SCROLL, V.LOC_DOWN, 0);
            }
        }

    }  //в дочерних формах описывает расположение объектов вызывается при resize

    // для дочерних объектов на click мышки
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
        if ("B32".equals(name)) {//Для кнопки B32 при вызове как справочник для выбора значений
            V.PARAMOT = new String[]{"OK"};
            THISFORM.DESTROY();
        }
    }

    public  void EVENT(String name, String SCAN) {//
        //обработка информации после работы событий во внешних классах-объектах (сом порт)
    }
    public  void EVENT(String name, String SCAN,PrintWriter out) {//
        //обработка информации после работы событий во внешних классах-объектах 
    }

    public void SAVE_TER(CipherForm TER) {//
        //обработка информации после работы терминала
    }

    public void MULTICLICK_ALL(String name) {
    } //для дочерних объектов на множественный клик

    public void CLICKR_ALL(String name) {
    }    // для дочерних объектов на click мышки

    public void DBLCLICK_ALL(String name) {
    }    // для дочерних объектов на двойной click мышки

    public void PRINT() {
    }    // для дочерних объектов на ВЫЗОВ ИЗ ТУЛБАРА

    public void DEPLOY(String name) {
    }    // для дочерних объектов на РАЗВЕРНУТЬ ИЗ ТУЛБАРА ДЛЯ ГРИДА

    public void PREV_UPDATE() {
    }    // ВЫЗЫВАЕТСЯ В ТУЛБАРЕ ПЕРЕД КНОПКАМИ ИЗМЕНЕНИЯ ДАННЫХ

    /**
     * GRID - ссылка на грид ВЫЗЫВАЕТСЯ В РЕЖИМАХ КОРРЕКТУРЫ ПОСЛЕ ЗАКРЫТИЯ
     * ФОРМЫ КОРРЕКТУРЫ (Form_k) СВОЙСТВА ДЛЯ ИСПОЛЬЗОВАНИЯ boolean READ_OK =
     * false;//если на форме корректуры , вызванной из данной формы была нажата
     * кнопка подтверждения то true иначе и по умолчанию false int READ_VID = 0;
     * //последний вид корреетуры 1- вставка 2- корректрура 3- удаление
     */
    public void READ_AFTER(Gridr GRID) {
    }

    /**
     * Используется для инцилизации формы CorrectForm
     *
     * @param form - ссылка на форму с полями корректуры CorrectForm
     */
    public void INIT_COR(Formr form) {
    }

    public void STATUS(String name) {
        if (V.USER_ADMIN < 2 && !V.USER_DIR) {
            P.MESS("Вы не имеете достаточно прав на операцию открытия-закрытия документов   \n Обратитесь к службе сопровождения ПО ");
            return;
        }
        Gridr GRID = GETG(name);
        boolean tf = (boolean) A.GETVAL(GRID.ALIAS + ".BIT_CLOSE");
        String STR = "Вы действительно хотите изменить статус документа на " + P.IIF(tf, "ОТКРЫТ", "ЗАКРЫТ") + " ?";
        int n = P.MESSYESNO(STR);
        if (n == 0) {
            if (V.ARM == 4) {
                V.SELE = "UPDATE " + GRID.ALIAS + " SET BIT_CLOSE=" + P.IIF(tf, "'F'", "'T'") + "  WHERE REL=" + A.GETVALSQL(GRID.ALIAS + ".REL");
            } else {
                V.SELE = "UPDATE " + GRID.ALIAS + " SET BIT_CLOSE=" + P.IIF(tf, "'F'", "'T'") + "  WHERE ID=" + A.GETVALSQL(GRID.ALIAS + ".ID");
            }
            P.SQLUPDATE(V.SELE);
            GRID.FORM_K = null;
            QUERY_REC(GRID, A.RECNO(GRID.ALIAS));
            P.WRITE_INFO("Документ " + P.IIF(tf, "ОТКРЫТ", "ЗАКРЫТ"));
            P.WRITE_INFO("При статусе документа ЗАКРЫТ запрещены операции по изменению данных. ");
        }
        GRID.SETFOCUS();
    }    // для дочерних объектов изменить статус документа

    public void STATUS(String name, String POLE) {
        if (V.USER_ADMIN < 2 && !V.USER_DIR) {
            P.MESS("Вы не имеете достаточно прав на операцию открытия-закрытия документов   \n Обратитесь к службе сопровождения ПО ");
            return;
        }
        Gridr GRID = GETG(name);
        boolean tf = (boolean) A.GETVAL(GRID.ALIAS + ".BIT_CLOSE");
        String STR = "Вы действительно хотите изменить статус документа на " + P.IIF(tf, "ОТКРЫТ", "ЗАКРЫТ") + " ?";
        int n = P.MESSYESNO(STR);
        if (n == 0) {
            V.SELE = "UPDATE " + GRID.ALIAS + " SET BIT_CLOSE=" + P.IIF(tf, "'F'", "'T'") + "  WHERE " + POLE + "=" + A.GETVALSQL(GRID.ALIAS + "." + POLE);
            P.SQLUPDATE(V.SELE);
            GRID.FORM_K = null;
            QUERY_REC(GRID, A.RECNO(GRID.ALIAS));
            P.WRITE_INFO("Документ " + P.IIF(tf, "ОТКРЫТ", "ЗАКРЫТ"));
            P.WRITE_INFO("При статусе документа ЗАКРЫТ запрещены операции по изменению данных. ");
        }
        GRID.SETFOCUS();
    }    // для дочерних объектов изменить статус документа

    public void FILL_TREE(Treer tree) {
    }    // для дочерних объектов на ЗАПОЛНЕНИЕ ДЕРЕВА

    //МЕТОДЫ В КОНСТРУКТОРЕ В ПОРЯДКЕ ВЫЗОВА
    public boolean LOAD() { //первый метод в конструкторе если return false выход
        return true;
    }

    public void DESCPROP() { //Задание свойств формы переоприделяется в дочерней форме 
        SETRESIZABLE(0); //1-Признак фиксированного размера 0- не фиксированный
//        SETMODAL(0);    //1-Модальная форма 0-не модальная
    }
//    public void RUNPROP(){}; //Обязательно для установки свойств формы после их описания             

    public void LOAD_OBJ() {
        if (V.PARAMTO == null || V.PARAMTO[0] == "") {
            this.SETSPR(false);
        } else {
            this.SETSPR(true); //вызов справочника из поля
        }

        B[32] = P.addobjB(this, "B32", "Выбрать текущее значение", "Занести значение текущей записи в поле ввода"); //добавляем кнопку выбора текущей записи и закрытия формы
        B[32].setVisible(SPR); // если нет параметров занчит не из поля ввода , значит невидимая

    }  //в дочерних формах добавляет объекты

    public void RESTORESIZE() {
        Configs proper = new Configs(V.fileNameFormConf);

        try {
            String name = getName();
            if ("".equals(name)) {
                name = getClass().getName();
            }
            name = SAVE_KEY + name;
            int width = new Integer(proper.getProperty(name + "$" + "WIDTH"));
            int height = new Integer(proper.getProperty(name + "$" + "HEIGHT"));
            boolean full = Boolean.valueOf(proper.getProperty(name + "$" + "FULL"));

            if (V._SCREEN.getWidth() > width & V._SCREEN.getHeight() > height) //если максимальная высота или ширина больше данных
            {
                setSize(width, height);
            } else {
                setSize(V._SCREEN.getWidth() - 20, V._SCREEN.getHeight() - 20);
            }                      // установить широту и высоту
            if (full) {
                setMaximum(true);
                if (V.INFO_TXT_SCROLL != null) {
                    if (V.INFO_TXT_SCROLL.isVisible()) {
                        setBounds(getX(), getY(), V._SCREEN.getWidth() - V._SCREEN.getWidth() + V._SCREEN.getContentPane().getWidth(), V._SCREEN.getHeight() - 115 - V.INFO_TXT_SCROLL.getHeight());
                    } else {
                        setBounds(getX(), getY(), V._SCREEN.getWidth() - V._SCREEN.getWidth() + V._SCREEN.getContentPane().getWidth(), V._SCREEN.getHeight() - 115);
                    }
                } else {
                    setBounds(getX(), getY(), V._SCREEN.getWidth() - V._SCREEN.getWidth() + V._SCREEN.getContentPane().getWidth(), V._SCREEN.getHeight() - 90);
                }
            }
        } catch (Exception e) {
            System.out.println("Не найдены свойства формы");
        }
    }

    /**
     * сохранение при выходе последнего расположения и величины окна
     */
    private void SAVELOCSIZE() {
        Configs proper = new Configs(V.fileNameFormConf);
        String name = getName();
        if ("".equals(name)) {
            name = getClass().getName();
        }
        proper.setProperty(SAVE_KEY + name + "$" + "WIDTH", new Integer(getWidth()).toString());
        proper.setProperty(SAVE_KEY + name + "$" + "HEIGHT", new Integer(getHeight()).toString());
        proper.setProperty(SAVE_KEY + name + "$" + "X_", new Integer(getX()).toString());
        proper.setProperty(SAVE_KEY + name + "$" + "Y_", new Integer(getY()).toString());
        proper.setProperty(SAVE_KEY + name + "$" + "FULL", String.valueOf(this.isMaximum));
        proper.saveProperties("Form configuration");

    }

    /**
     * восстановление последнего расположения и величины окна
     */
    public void RESTORELOC() {
        Configs proper = new Configs(V.fileNameFormConf);
        try {
            String name = getName();
            if ("".equals(name)) {
                name = getClass().getName();
            }
            name = SAVE_KEY + name;
            int x = new Integer(proper.getProperty(name + "$" + "X_"));
            int y = new Integer(proper.getProperty(name + "$" + "Y_"));

            if (x == 0 & y == 0) { ////если координаты равны нулю или больше допустимых
                x = 33;
                y = 33;
            }
            if (x + getWidth() + 10 >= V._SCREEN.getWidth()) {
                x = 5;
            }
            if (y + getHeight() + 10 >= V._SCREEN.getHeight()) {
                y = 5;
            }
            setLocation(x, y);
        } catch (Exception e) {
            System.out.println("Не найдены свойства формы");

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            Dimension frameSize = V._SCREEN.getSize();

            if (frameSize.height > screenSize.height) {

                frameSize.height = screenSize.height;

            }

            if (frameSize.width > screenSize.width) {

                frameSize.width = screenSize.width;

            }

//            V._SCREEN.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            //setSize(screenWidth/2, screenHeight/2);
            // V._SCREEN.setLocationRelativeTo(null);
        }
    }

    public boolean INIT() {// последний метод в конструкторе
        return true;
    }
////////////////////КОНЕЦ МЕТОДОВ ВЫЗЫВАЕМЫХ В КОНСТРУКТОРЕ

///МЕТОДЫ ВЫПОЛНЯЕМЫЕ ПРИ СОБЫТИИ СЛУШАТЕЛЕЙ    
    public void KEYPRESS(Component obj, KeyEvent e) {//метод выполняемый после нажатия клавиши на любом из элементов
        int key = e.getKeyCode();
        if (e.isAltDown()) {
            //P.ALERT(FF.STR(key));
            for (int i = 0; i < B.length; i++) {
                if (B[i] != null && B[i].ALTKEY == key - 48 && B[i].isEnabled()) { // ЕСЛИ КНОПКА ПО ALT+НОМЕР ЦИФРЫ 
                    this.CLICK_ALL(B[i].getName());
                }
            } //for

        }
        KEYPRESS(obj, key);
    }

    public void KEYPRESS(Component obj, int key) {//метод выполняемый после нажатия клавиши на любом из элементов
//            System.out.println("keyPressed "+obj.getName());                            
//              System.out.print(" KEY:"+key+"         "+e.getKeyChar()+"\n");
        P.ALERT(""); // ПОТУШИТЬ СООБЩЕНИЕ ЕСЛИ ВИСЕЛО
//        V.CONN_OUT=true; //если цикл до нажатия клавиши
        if (obj.getClass().getSimpleName().equalsIgnoreCase("Buttonr")) { //проверяем базовый класс объекта 
            Buttonr b = (Buttonr) obj;  //из объекта типа Component формирет ссылку на объект типа Buttonr для доспупа ко всем его свойствам и методам
            if (key == V.KEY_ENTER) {
                b.CLICK();// при нажатии на кнопку Enter выполнить метод click
            }
            if (key == V.KEY_LEFT || key == V.KEY_UP) {
                b.transferFocusBackward();// при нажатии на кнопку влево выполнить фокус на предыдущий элемент
            }
            if (key == V.KEY_RIGTH || key == V.KEY_DOWN) {
                b.transferFocus();// при нажатии на кнопку право выполнить фокус на следующий элемент
            }
        }
        if (obj.getClass().getSimpleName().equalsIgnoreCase("Textr")) {
            Textr t = (Textr) obj;
            if (key == V.KEY_ENTER) {
                t.transferFocus();// при нажатии на кнопку Enter выполнить выход из поля фокус на следующий элемент
            }
        }
        if (obj.getClass().getSimpleName().equalsIgnoreCase("Fieldr")) {
            Fieldr t = (Fieldr) obj;
            if (key == V.KEY_ENTER) {
                t.transferFocus();// при нажатии на кнопку Enter выполнить выход из поля фокус на следующий элемент
            }
            if (key == V.KEY_F1) {
                DBLCLICK_ALL(t.getName());// при нажатии на кнопку F1 выполнить метод двойного щелчка мыши
            }
        }
        if (obj.getClass().getSimpleName().equalsIgnoreCase("Fieldrp")) {
            Fieldrp t = (Fieldrp) obj;
            if (key == V.KEY_ENTER) {
                t.transferFocus();// при нажатии на кнопку Enter выполнить выход из поля фокус на следующий элемент
            }
            if (key == V.KEY_F1) {
                DBLCLICK_ALL(t.getName());// при нажатии на кнопку F1 выполнить метод двойного щелчка мыши
            }

        }
        if (obj.getClass().getSimpleName().equalsIgnoreCase("Gridr") && V.TOOLBAR != null) {
            Gridr t = (Gridr) obj;
            if (key == V.KEY_ENTER && V.TOOLBAR.UPDATE.isEnabled() && !t.THISFORM.SPR) {
                V.TOOLBAR.UPDATE();
            }
            if (key == V.KEY_ENTER && t.THISFORM.SPR) {
                t.THISFORM.CLICK_ALL("B32");
            }

            if (key == V.KEY_INS && V.TOOLBAR.INSERT.isEnabled()) {
                V.TOOLBAR.INSERT();
            }
            if (key == V.KEY_DEL && V.TOOLBAR.DELETE.isEnabled()) {
                V.TOOLBAR.DELETE();
            }
            if (key == V.KEY_F11 && V.TOOLBAR.QUERY.isEnabled()) {
                V.TOOLBAR.QUERY(null);
            }
            if (key == V.KEY_F5 && V.TOOLBAR.DEPLOY.isEnabled()) {
                V.TOOLBAR.DEPLOY(null);
            }
            if (key == V.KEY_F6 && V.TOOLBAR.PRINT.isEnabled()) {
                V.TOOLBAR.PRINT(null);
            }
            if (key == V.KEY_F12 && V.TOOLBAR.STATUS.isEnabled()) {
                V.TOOLBAR.STATUS(null);
            }

        }

        if (THISFORM.ESC == 1 && key == V.KEY_ESC) { //если в форме разрешение выход по esc thisform.Esc==1
            VALIDON = false;
            if (B_EXIT != null) {//если есть кнопка для закрытия формы
                CLICK_ALL(B_EXIT.getName());
            }
            DESTROY();
        }  //закрыть
    } //KEYPRESS

    ;    // выполняется в дочерних формах до открытия, после создания элементов  
    public void OPEN() {
    }    // выполняется в дочерних формах при открытии, после создания элементов  

    public void ACTIVATE() {// выполняется при фокусировании формы
        INF();    // выдача информации при активизации
        S.SETACTIVEFORM(this);
    }

    public void SETFOCUS() {
        try {
            setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Formr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void DEACTIVATE() {// выполняется при потере фокуса формой
        V._SCREEN.INF2.setText("");
    }

    public void DESTROY() {//выполняется при закрытии
        String name = getClass().getSimpleName();
        if (V.MAPFORMS.get(name) != null) {
            V.MAPFORMS.remove(name);
        }//удалить из карты открытых форм
        SAVELOCSIZE();// запомнить координаты и размер окна
        for (Gridr GRID : this.G) {
            if (GRID==null) {
                continue;
            }
            GRID.SAVECOL();
            if (GRID.timer!=null) {
            GRID.timer.stop();
            GRID.timer=null;
            }
            
        }
        for (Treer Tree : TREES) {
            Tree.SAVE();
        }
        EXIT = 1; //признак зарытия
        if (V.TOOLBAR != null) {
            V.TOOLBAR.SETENABLE(null);
        }
        if (this.PREVFORM != null) { //ВЫЗЫВАЕМАЯ ДО ТЕКУЩЕЙ ФОРМА
            this.PREVFORM.SETFOCUS();
        }

        if (this.PARENTFORM != null) { //УСТАНОВОЛЕНА КАК РОДИТЕЛЬСКАЯ ФОРМА
            this.PARENTFORM.SETFOCUS();
        }
        P.ALERT("");
        this.dispose();//закрыть окно 
    }

    public boolean CHECK_TOOLBAR() {//лежит ли форма под тулбаром
        if (this.getY() < 30 && V.TOOLBAR != null) {
            return true;
        }
        return false;
    }

    public boolean CHECK_INFO() {//лежит ли форма под инфопанелью
        if (V.INFO_TXT_SCROLL != null && (this.getY() + this.getHeight()) > (V._SCREEN.getHeight() - V.INFO_TXT_SCROLL.getHeight() - 85)) {
            return true;
        }
        return false;
    }

    public void INF() { //показ текущих координат и размера формы в правом нижнем углу основной
        V._SCREEN.INF2.setText(this.getName() + " X:" + this.getX() + " Y:" + this.getY() + " W:" + this.getWidth() + " H:" + this.getHeight());
    }

    /**
     * событие выполняется при изменении размеров формы перераспологаются
     * элементы формы
     *
     * @param evt
     */
    public void RESIZED() { //изменение размеров формы
        LOC_ABOUT(); //Задание расположения элементов на форме     
    }

    public Formr() { // вызов без параметров
        this("Formr", "Форма", 600, 400);
    }

    /**
     * Конструктор при создании формы
     *
     * @param name имя формы
     * @param title заголовок
     * @param width ширина
     * @param height высота
     */
    public Formr(String name, String title, int width, int height) {
        setClosable(true);  //кнопка закрытия
        setResizable(true); // изменение размеров
        setMaximizable(true); // кнопка максимизации      
        setIconifiable(true);   // кнопка сворачивания    

        THISFORM = this;
        B = new Buttonr[99];
        L = new Labelr[99];
        T = new Textr[9];
        E = new Editr[3];
        F = new Fieldr[99];
        G = new Gridr[9];
        O = new JRadioButtons[9];
        CH = new CheckBoxr[99];
        CB = new ComboBoxr[20];
        LB = new ListBoxr[9];
        TR = new Treer[9];
        PF = new Fieldrp[9];
        J = new JPanelr[9];
        
        setTitle(title); //заголовок окна
        super.setName(name); //присвоение имени объекту                       
        super.setLocation(10, 40);
        super.setSize(width, height);//ширина и высота окна
        setMinimumSize(new Dimension(width, height));//Минимальные размеры формы 
        super.getDesktopIcon().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("press");
            }
        });
        XX = width;
        YY = height; //фиксируем начальные размеры для восстановления
        super.setLayout(null);//Расположение элементов нет
        setHelp();
//СЛУШАТЕЛИ        
        addComponentListener(new java.awt.event.ComponentAdapter() {//слушатель на изменение разммеров
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {// при изменении размеров
                if (THISFORM.isMaximum && !MAXIMIZED) {//максимизизация формы
//                    THISFORM.setSize(THISFORM.getWidth(), THISFORM.getHeight() - 30);
                    if (V.INFO_TXT_SCROLL != null) {
                        if (V.INFO_TXT_SCROLL.isVisible()) {//если инфопанель включена
                            THISFORM.setBounds(THISFORM.getX(), 30, THISFORM.getWidth(), THISFORM.getHeight() - 55 - V.INFO_TXT_SCROLL.getHeight());
                        } else {
                            THISFORM.setBounds(THISFORM.getX(), 30, THISFORM.getWidth(), THISFORM.getHeight() - 55);
                        }
                    }
                    MAXIMIZED = true;
                }
                if (!THISFORM.isMaximum) {
                    MAXIMIZED = false;
                }
                //09.11.2016 убрал проверку для тестов
//                if (V.INFO_TXT_SCROLL != null && V.INFO_TXT_SCROLL.isVisible()) {//проверка высоты. чтобы не превышала расстояние между тулбаром и нижними панелями
//                    if (THISFORM.getHeight() > V._SCREEN.getHeight() - 115 - V.INFO_TXT_SCROLL.getHeight()) {
//                        THISFORM.setBounds(THISFORM.getX(), THISFORM.getY(), THISFORM.getWidth(), V._SCREEN.getHeight() - 118 - V.INFO_TXT_SCROLL.getHeight());
//                    }
//                } else if (V.INFO_TXT_SCROLL != null && !V.INFO_TXT_SCROLL.isVisible()) {
//                    if (THISFORM.getHeight() > V._SCREEN.getHeight() - 115) {
//                        THISFORM.setBounds(THISFORM.getX(), THISFORM.getY(), THISFORM.getWidth(), V._SCREEN.getHeight() - 118);
//                    }
//                }
                INF();//информация по положению и размерам формы
                RESIZED();
            }

            public void componentMoved(java.awt.event.ComponentEvent evt) { //при изменении координат

                if (CHECK_TOOLBAR()) {//проверка попытики перетащить форму под тулбар
//                    THISFORM.setLocation(THISFORM.getX(), 30);
                    THISFORM.setBounds(THISFORM.getX(), 30, THISFORM.getWidth(), THISFORM.getHeight());
                }
                //09.11.2016 убрал проверку для тестов
//                if (CHECK_INFO() && V.INFO_TXT_SCROLL.isVisible()) {//проверка попытки перетащить форму под инфопанель
////                    THISFORM.setLocation(THISFORM.getX(), V._SCREEN.getHeight() - V.INFO_TXT_SCROLL.getHeight() - THISFORM.getHeight() - 35);
//                    if (THISFORM.getHeight() > V._SCREEN.getHeight() - V.INFO_TXT_SCROLL.getHeight() - 85) {
//                        THISFORM.setBounds(THISFORM.getX(), 30, THISFORM.getWidth(), THISFORM.getHeight());
//                    } else {
//                        THISFORM.setBounds(THISFORM.getX(), V._SCREEN.getHeight() - V.INFO_TXT_SCROLL.getHeight() - THISFORM.getHeight() - 85, THISFORM.getWidth(), THISFORM.getHeight());
//                    }
//                }
//                if (V.INFO_TXT_SCROLL != null && !V.INFO_TXT_SCROLL.isVisible()) {//проверка попытки перетащить форму под панель со свернутыми формами, если выключена инфопанель
//                    if (THISFORM.getHeight() > V._SCREEN.getHeight() - 85) {
//                        THISFORM.setBounds(THISFORM.getX(), 30, THISFORM.getWidth(), THISFORM.getHeight());
//                    } else {
//                        if ((THISFORM.getY() + THISFORM.getHeight()) > (V._SCREEN.getHeight() - 85)) {
//                            THISFORM.setBounds(THISFORM.getX(), V._SCREEN.getHeight() - THISFORM.getHeight() - 87, THISFORM.getWidth(), THISFORM.getHeight());
//                        }
//                    }
//                }
                INF();//информация по положению и размерам формы
            }
        });

        addContainerListener(new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {//добавление компонента
            }

            @Override
            public void componentRemoved(ContainerEvent e) {//удаление компонента
//             System.out.print(" ----:"+e.getComponent().getClass().getSimpleName() +"\n");
            }
        });

        addInternalFrameListener(new InternalFrameListener() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {//открытие формы
//                OPEN();
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {//закрытие формы
//               if (JOptionPane.showConfirmDialog(null, "Вы уверены, что хотите выйти?" ) == JOptionPane.YES_OPTION) 
                DESTROY();
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {//закрыта
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) { //уходит в низ
                MAXIMIZED = false;
//                if (V.INFO_TXT_SCROLL.isVisible()) {
//                    getDesktopIcon().setBounds(getDesktopIcon().getX(), V._SCREEN.getHeight() - V.INFO_TXT_SCROLL.getHeight() - 90, getDesktopIcon().getWidth(), getDesktopIcon().getHeight());
//                } else {
//                    getDesktopIcon().setBounds(getDesktopIcon().getX(), V._SCREEN.getHeight() - 90, getDesktopIcon().getWidth(), getDesktopIcon().getHeight());
//                }
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {//возвращается на экран
                MAXIMIZED = false;
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {//активизация
                S.SETACTIVEFORM(THISFORM);
                ACTIVATE();
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {//деактивная
                if (MODAL != 1) {//только если форма не модальная
                    S.SETACTIVEGRID(null);//установка активного грида для тулбара
                    DEACTIVATE();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//нажатие клавиш
                KEYPRESS(e.getComponent(), e);
            }
        });
    } //КОНЕЦ КОНСТРУКТОРА

    public synchronized void STARTMODAL() {//метод не позволяющий выполнение действий вне формы

        try {
            if (SwingUtilities.isEventDispatchThread()) {
                EventQueue theQueue = getToolkit().getSystemEventQueue();
                while (isVisible()) {
                    AWTEvent event = theQueue.getNextEvent();
                    Object source = event.getSource();
                    boolean dispatch = true;

                    if (event instanceof MouseEvent) {
                        MouseEvent e = (MouseEvent) event;
                        MouseEvent m = SwingUtilities.convertMouseEvent((Component) e.getSource(), e, this);
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

    //МЕТОДЫ ДЛЯ РАБОТЫ С ТАБЛИЦАМИ GRID
    public String PREV_QUERY(Gridr GRID) {// для таблиц получение запроса данных
        return null;
    }

    public String PREV_QUERY_REC(Gridr GRID) {// для таблиц получение запроса для одиночной записи
        String WHERE = PREV_KEY_REC(GRID);
        if (FF.EMPTY(WHERE)) {
            return "";
        }
        V.SELE = PREV_QUERY(GRID) + " WHERE " + WHERE;// для вставки записи

        return V.SELE;
    }

    public String PREV_INSERT(Gridr GRID) {// для таблиц получение запроса новой записи
        if (FF.EMPTY(GRID.TABLE)) { //ЕСЛИ ИМЯ ТАБЛИЦЫ РАВНО АЛИАСУ
            GRID.TABLE = GRID.ALIAS;
        }
        return PREV_INSERT(GRID.FORM_K, GRID.TABLE);
    }

    public String PREV_INSERT(Formr OBJ, String TABLE) {// для таблиц получение запроса новой записи
//        Formr OBJ = GRID.FORM_K;
        V.SELE = "";
        String POLE = "";
        String VALUES = "";

        for (int i = 0; i < OBJ.F.length; i++) {
            if (OBJ.F[i] != null && OBJ.F[i].SQLUPDATE) {
                POLE = POLE + "," + OBJ.F[i].getName();
                VALUES = VALUES + "," + P.P_SQL(OBJ.F[i].GETVALUE());
            }
        } //for

        for (int i = 0; i < OBJ.T.length; i++) {
            if (OBJ.T[i] != null && OBJ.T[i].SQLUPDATE) {
                POLE = POLE + "," + OBJ.T[i].getName();
                VALUES = VALUES + "," + P.P_SQL(OBJ.T[i].GETVALUE());
            }
        } //for

        for (int i = 0; i < OBJ.LB.length; i++) { //для поля LB
            if (OBJ.LB[i] != null && OBJ.LB[i].SQLUPDATE) {
                POLE = POLE + "," + OBJ.LB[i].GETNAME();
                VALUES = VALUES + "," + P.P_SQL(OBJ.LB[i].GETINDEX());
            }
        } //for

        for (int i = 0; i < OBJ.O.length; i++) { //для поля LB
            if (OBJ.O[i] != null && OBJ.O[i].SQLUPDATE) {
                POLE = POLE + "," + OBJ.O[i].GETNAME();
                VALUES = VALUES + "," + P.P_SQL(OBJ.O[i].GETVALUE());
            }
        } //for

        for (int i = 0; i < OBJ.CH.length; i++) { //для поля CH
            if (OBJ.CH[i] != null && OBJ.CH[i].SQLUPDATE) {
                POLE = POLE + "," + OBJ.CH[i].GETNAME();
                VALUES = VALUES + "," + P.P_SQL(OBJ.CH[i].GETVALUE());
            }
        } //for
        for (int i = 0; i < OBJ.CB.length; i++) { //для поля CH
            if (OBJ.CB[i] != null && OBJ.CB[i].SQLUPDATE) {
                POLE = POLE + "," + OBJ.CB[i].GETNAME();
                VALUES = VALUES + "," + P.P_SQL(OBJ.CB[i].GETSELECTEDVALUE());
            }
        } //for

        V.SELE = "INSERT INTO " + TABLE + " (" + FF.SUBSTR(POLE, 2) + ")" + " VALUES (" + FF.SUBSTR(VALUES, 2) + ")";
        return V.SELE;
    }

    public String PREV_KEY(Gridr GRID) {//ПОЛУЧЕНИЕ КЛЮЧЕВОГО ВЫРАЖЕНИЯ В WHERE
        Formr OBJ = GRID.FORM_K;
        String WHERE = "";
        for (int i = 0; i < OBJ.F.length; i++) {
            if (OBJ.F[i] != null && OBJ.F[i].THISKEY) {
                WHERE = WHERE + " AND " + OBJ.F[i].getName() + "=" + A.GETVALSQL(GRID.ALIAS + "." + OBJ.F[i].getName(), GRID.RECNO);
            }
        } //for
        return FF.SUBSTR(WHERE, 5);
    }

    public String PREV_KEY_REC(Gridr GRID) {//ПОЛУЧЕНИЕ КЛЮЧЕВОГО ВЫРАЖЕНИЯ В WHERE ПРИ ВСТАВКЕ
        Formr OBJ = GRID.FORM_K;
        if (OBJ == null) {
            return "";
        }
        String WHERE = "";
        for (int i = 0; i < OBJ.F.length; i++) {
            if (OBJ.F[i] != null && OBJ.F[i].THISKEY) {
                WHERE = WHERE + " AND " + OBJ.F[i].getName() + "=" + P.P_SQL(OBJ.F[i].GETVALUE());
            }
        } //for
        return FF.SUBSTR(WHERE, 5);
    }

    public String PREV_UPDATE(Gridr GRID) {// для таблиц получение запроса корректуры
        Formr OBJ = GRID.FORM_K;
        V.SELE = "";
        String WHERE = "";
        for (int i = 0; i < OBJ.F.length; i++) { //для поля F
            if (OBJ.F[i] != null && OBJ.F[i].SQLUPDATE) {
                V.SELE = V.SELE + "," + OBJ.F[i].getName() + "=" + P.P_SQL(OBJ.F[i].GETVALUE());
                if (OBJ.F[i].THISKEY) {
                    WHERE = WHERE + " AND " + OBJ.F[i].getName() + "=" + A.GETVALSQL(GRID.ALIAS + "." + OBJ.F[i].getName(), GRID.RECNO);
                }
            }
        } //for
        for (int i = 0; i < OBJ.T.length; i++) { //для поля T
            if (OBJ.T[i] != null && OBJ.T[i].SQLUPDATE) {
                V.SELE = V.SELE + "," + OBJ.T[i].getName() + "=" + P.P_SQL(OBJ.T[i].GETVALUE());
                if (OBJ.T[i].THISKEY) {
                    WHERE = WHERE + " AND " + OBJ.T[i].getName() + "=" + A.GETVALSQL(GRID.ALIAS + "." + OBJ.T[i].getName(), GRID.RECNO);
                }
            }
        } //for

        for (int i = 0; i < OBJ.LB.length; i++) { //для поля LB
            if (OBJ.LB[i] != null && OBJ.LB[i].SQLUPDATE) {
                V.SELE = V.SELE + "," + OBJ.LB[i].GETNAME() + "=" + P.P_SQL(OBJ.LB[i].GETINDEX());
                if (OBJ.LB[i].THISKEY) {
                    WHERE = WHERE + " AND " + OBJ.LB[i].GETNAME() + "=" + A.GETVALSQL(GRID.ALIAS + "." + OBJ.LB[i].GETNAME(), GRID.RECNO);
                }
            }
        } //for
        for (int i = 0; i < OBJ.CH.length; i++) { //для поля LH
            if (OBJ.CH[i] != null && OBJ.CH[i].SQLUPDATE) {
                V.SELE = V.SELE + "," + OBJ.CH[i].GETNAME() + "=" + P.P_SQL(OBJ.CH[i].GETVALUE());
                if (OBJ.CH[i].THISKEY) {
                    WHERE = WHERE + " AND " + OBJ.CH[i].GETNAME() + "=" + A.GETVALSQL(GRID.ALIAS + "." + OBJ.CH[i].GETNAME(), GRID.RECNO);
                }
            }
        } //for
        for (int i = 0; i < OBJ.CB.length; i++) { //для поля LH
            if (OBJ.CB[i] != null && OBJ.CB[i].SQLUPDATE) {
                V.SELE = V.SELE + "," + OBJ.CB[i].GETNAME() + "=" + P.P_SQL(OBJ.CB[i].GETSELECTEDVALUE());
                if (OBJ.CB[i].THISKEY) {
                    WHERE = WHERE + " AND " + OBJ.CB[i].GETNAME() + "=" + A.GETVALSQL(GRID.ALIAS + "." + OBJ.CB[i].GETSELECTEDVALUE(), GRID.RECNO);
                }
            }
        } //for
        for (int i = 0; i < OBJ.O.length; i++) { //для поля O
            if (OBJ.O[i] != null && OBJ.O[i].SQLUPDATE) {
                V.SELE = V.SELE + "," + OBJ.O[i].GETNAME() + "=" + P.P_SQL(OBJ.O[i].GETVALUE());
                if (OBJ.O[i].THISKEY) {
                    WHERE = WHERE + " AND " + OBJ.O[i].GETNAME() + "=" + A.GETVALSQL(GRID.ALIAS + "." + OBJ.O[i].GETNAME(), GRID.RECNO);
                }
            }
        } //for

        if (FF.EMPTY(GRID.TABLE)) {
            GRID.TABLE = GRID.ALIAS;
        }
        V.SELE = "UPDATE " + GRID.TABLE + " SET " + FF.SUBSTR(V.SELE, 2);
        if (WHERE == "") {
            V.SELE = V.SELE + " WHERE " + PREV_KEY(GRID);
        } else {
            V.SELE = V.SELE + " WHERE " + FF.SUBSTR(WHERE, 5);
        }
        return V.SELE;
    }

    public String PREV_DELETE(Gridr GRID) { // для таблиц получение запроса удаления записи
        Formr OBJ = GRID.FORM_K;
        V.SELE = "";
        String WHERE = "";
        for (int i = 0; i < OBJ.F.length; i++) {
            if (OBJ.F[i] != null) {
                if (OBJ.F[i].THISKEY) {
                    WHERE = WHERE + " AND " + OBJ.F[i].getName() + "=" + P.P_SQL(OBJ.F[i].GETVALUE());
                }
            }
        } //for
        if (FF.EMPTY(GRID.TABLE)) {
            GRID.TABLE = GRID.ALIAS;
        }
        V.SELE = "DELETE FROM  " + GRID.TABLE;
        if (WHERE == "") {
            V.SELE = V.SELE + " WHERE " + PREV_KEY(GRID);
        } else {
            V.SELE = V.SELE + " WHERE " + FF.SUBSTR(WHERE, 5);
        }

        return V.SELE;
    }

    public void PREV_INITCOLUMN(Gridr GRID) {
    }    // для таблиц заголовки и данные

    public boolean UPDATE(Gridr GRID) {//выполнение корректуры
        int rec = 0;
        V.SELE = PREV_UPDATE(GRID);
        rec = P.SQLUPDATE(V.SELE);
        if (rec == -1) {
            return false;
        }
//           QUERY(GRID);             
        return true;
    }

    public boolean INSERT(Gridr GRID) {//выполнение корректуры
        int rec = 0;
        V.SELE = PREV_INSERT(GRID);
        rec = P.SQLUPDATE(V.SELE);
        if (rec == -1) {
            return false;
        }
        return true;
    }

    public boolean DELETE(Gridr GRID) {//выполнение удаления
        int rec = 0;
        V.SELE = PREV_DELETE(GRID);
        rec = P.SQLUPDATE(V.SELE);
        if (rec == -1) {
            return false;
        }
        if (getName().equals("UnitsHierarchy")) {//для формы иерархии подразделений при удалении обновить все гриды и дерево
            CLICK_ALL("cb_roznNet");
        }
        return true;
    }

    public void CHANGERECNO(Gridr GRID) {
        CHANGERECNO(GRID, GRID.RECNO, GRID.STRNO);
    }    // для таблиц изменение записи

    public void CHANGERECNO(Gridr GRID, int rowlast, int reclast) {
    }    // для таблиц изменение записи

    public void TABLEPROP(Gridr GRID) {
    }    // для таблиц задание свойств

    /**
     * Получение данных для таблицы
     *
     * @return
     */
    public boolean QUERY(Gridr GRID) {
        if (GRID == null) {
            return false;
        }

        String SELE = THISFORM.PREV_QUERY(GRID); //получаем строку запроса , метод из родительской формы
        if (FF.EMPTY(SELE)) {
            return false;
        }
        int REC = A.RECNO(GRID.ALIAS);
        Cursorr DATA = P.SQLEXECT(SELE, GRID.ALIAS, GRID.QUERY_MESS); //выполнение запроса и получение данных
        if (DATA == null) {
            return false;
        }

        GRID.SELE = SELE;

        GRID.DATA = DATA;
        GRID.SELE = SELE;
        GRID.DATA.SELE = SELE;
        if (A.RECCOUNT(GRID.ALIAS) > 0) {
//         GRID.GORECNO(1);
//            GRID.RECNO = 1;
        }
//        try {
//            if (GRID.getColumn(1).getWidth() != 75) {//сбрасывается ширина колонок, после вызова INITCOLUMN(GRID) и потом снова вызывается GRID.SAVECOL(). то есть размеры сохраняются не правильные!!!
//                GRID.SAVECOL();
//            }
//        } catch (ArrayIndexOutOfBoundsException ex) {
//            GRID.SAVECOL();
//        }
        boolean tf = INITCOLUMN(GRID);
        if (REC > 0 && A.RECCOUNT(GRID.ALIAS) >= REC) {
            GRID.GORECNO(REC);
        }
        GRID.RECNO = A.RECNO(GRID.ALIAS);
        CHANGERECNO(GRID, GRID.RECNO, GRID.STRNO);
        if (GRID.QUERY_MESS) {
            GRID.SETFOCUS();
        }
        String[] typeArr = new String[GRID.FIELD.length + 1];
        typeArr[0] = "N";
        for (int j = 0; j < GRID.FIELD.length; j++) {
            for (int k = 0; k < GRID.DATA.COLNAMES.length; k++) {
                if (GRID.FIELD[j].equals(GRID.DATA.COLNAMES[k])) {
                    typeArr[j + 1] = GRID.DATA.COLTYPE[k];
                    break;
                }
            }
        }
        for (int i = 0; i < GRID.FIELD.length + 1; i++) {
            if (typeArr[i] != null) {
                GRID.SORTER.setComparator(i, comparator(i, typeArr[i], GRID));
            }
        }

        return tf;
    }

    /**
     * ПЕРЕЗАПРОС ТЕКУЩЕЙ ЗАПИСИ И ОБНОВЛЕНИЕ ЕЕ В ГРИДЕ
     *
     * @param GRID -ГРИД
     * @return
     */
    public boolean QUERY_REC(Gridr GRID) { //
        return QUERY_REC(GRID, GRID.RECNO);

    }

    /**
     * ПЕРЕЗАПРОС УКАЗАННОЙ ЗАПИСИ И ОБНОВЛЕНИЕ ЕЕ В ГРИДЕ
     *
     * @param GRID -ГРИД
     * @param REC - НОМЕР ЗАПИСИ В ГРИДЕ
     * @return
     */
    public boolean QUERY_REC(Gridr GRID, int REC) { //
        int type = REC;
        if (REC >= 0) {//ВСТАВКА ИЛИ КОРРЕКТУРА
            GRID.GORECNO(REC);
            if (GRID.FORM_K != null) {
                V.SELE = GRID.FORM_K.PREV_QUERY_REC(GRID); //получаем строку запроса для текущей записи , метод из  формы корректуры
                GRID.FORM_K = null;
            } else {
                V.SELE = THISFORM.PREV_QUERY_REC(GRID); //получаем строку запроса для текущей записи , метод из родительской формы
            }
            if (FF.EMPTY(V.SELE)) {
                return false;
            }
            Cursorr DATA = P.SQLEXECT(V.SELE, "UD_REC"); //выполнение запроса и получение данных
            if (DATA == null) {
                return false;
            }
            int kol = DATA.COLCOUNT;//количество полей
            if (REC == 0) {//ЕСЛИ ВСТАВКА ЗАПИСИ ВСТАВЛЯЕМ В КУРСОР
                //формирование пустой записи
                int rowCount = GRID.DATA.ROWCOUNT + 1; //количество записей плюс еще одна
                REC = 1; // вставка в начало таблицы
                Object[] cells = new Object[kol]; // массив объектов с количеством полей
                cells[kol - 1] = new Integer(REC); // последнее поле это номер запсии , туда его и записываем
                for (int i = 0; i < GRID.DATA.rowList.size(); i++) { // сдвиг записей
                    Integer rowid = Integer.class.cast(Object[].class.cast(GRID.DATA.rowList.get(i))[kol - 1]);
                    Object[].class.cast(GRID.DATA.rowList.get(i))[kol - 1] = ++rowid;
                }
                GRID.DATA.rowList.add(REC - 1, cells); // добавление массива объектов строки в контейнер строк (в курсор пустую строку с номером записи)
                GRID.DATA.ROWCOUNT = rowCount; //количество записей в объекте курсор уже вместе с вставленой
            }
            // заполняем данными 
            Object val = null; // инициализация объекта-ячейки
            String pole = ""; // инициализация переменной с именем поля
            for (int i = 0; i < kol - 1; i++) { // по всем полям
                pole = DATA.COLNAMES[i];  // имя поля в курсоре
                val = A.GETVAL("UD_REC." + pole); //значение в курсоре в который выбрали введенную запись
                A.SETVAL(val, GRID.ALIAS, pole, REC); // ЗАПОЛНЕНИЕ ТЕКУЩЕГО КУРСОРА из полученного из базы
            }
        }
        if (REC < 0) {//УДАЛЕНИЕ   
            REC = -REC;
            GRID.DATA.rowList.remove(REC - 1);
            GRID.DATA.ROWCOUNT--;
        }
        THISFORM.INITCOLUMN(GRID);
        GRID.GORECNO(REC);
        THISFORM.CHANGERECNO(GRID, GRID.RECNO, GRID.STRNO);
        return true;
    }

    /**
     * Инициализация модели таблицы по списку полей и заголовков полученнных из
     * метода public String[][] INITCOLUMN(String name) родительской формы
     *
     * @return true - нормальное завершение
     */
    public boolean INITCOLUMN(Gridr GRID) {
        if (GRID.getModel() != null) {
            GRID.SAVECOL();
        }
        THISFORM.PREV_INITCOLUMN(GRID);//получаем двумерный массив названий столбцов и имена полей , метод из родительской формы

        ///////////////// ПОРЯДОК КОЛОНОК
        if (V.CAPTION != null) { //если не просмотр данных

            GRID.CAPTION = new String[V.CAPTION.length];
            GRID.FIELD = new String[V.FIELD.length];
            GRID.FSIZE = new int[V.FSIZE.length];//из глобальных (транспортных) в локальные массивы

            System.arraycopy(V.CAPTION, 0, GRID.CAPTION, 0, V.CAPTION.length);
            System.arraycopy(V.FIELD, 0, GRID.FIELD, 0, V.FIELD.length);
            System.arraycopy(V.FSIZE, 0, GRID.FSIZE, 0, V.FSIZE.length);

            Configs proper = new Configs(V.fileNameGridconf);
            int colcount = new Integer(proper.getProperty("COLCOUNT_" + THISFORM.getName() + GRID.getName(), "0"));
            if (GRID.RESTORECOL == 1 && colcount == V.FIELD.length) {

                for (int i = 0; i < V.FIELD.length; i++) {  //ПОРЯДОК КОЛОНОК
                    int npp = new Integer(proper.getProperty(GRID.SAVE_KEY + "NPP_" + THISFORM.getName() + GRID.getName() + "_" + V.FIELD[i], "-1"));
                    if (npp != -1 && npp <= V.FIELD.length) {
                        GRID.FIELD[npp - 1] = V.FIELD[i];
                        GRID.CAPTION[npp - 1] = V.CAPTION[i];
                        GRID.FSIZE[npp - 1] = V.FSIZE[i];

                    } else {  // если нестыковка по количеству или не найдено поле
                        System.arraycopy(V.CAPTION, 0, GRID.CAPTION, 0, V.CAPTION.length);
                        System.arraycopy(V.FIELD, 0, GRID.FIELD, 0, V.FIELD.length);
                        System.arraycopy(V.FSIZE, 0, GRID.FSIZE, 0, V.FSIZE.length);

                        break;
                    }

                }

            }
        }
        ////////////////

        V.CAPTION = null;
        V.FIELD = null;
        V.FSIZE = null;//иницилизация для следующего использования
        if (GRID.CAPTION == null) { //если получен null берем весь курсор данных
            GRID.CAPTION = GRID.DATA.COLNAMES;//полный список названий полей
            GRID.FIELD = GRID.DATA.COLNAMES;//полный список названий полей
        }
        if (GRID.FSIZE == null) {
            GRID.FSIZE = new int[GRID.CAPTION.length];
        }
        if (GRID.CAPTION.length != GRID.FIELD.length || GRID.FIELD.length != GRID.FSIZE.length) {
            P.MESSERR("Не равное количество элементов \n" + " Заголовок:" + GRID.CAPTION.length + "\n" + " Поля:" + GRID.FIELD.length + "\n" + " Размеры:" + GRID.FSIZE.length);
            return false;
        }
        try {
            Modelr MODEL = null;
            MODEL = new Modelr(GRID, GRID.CAPTION, GRID.FIELD); // формируем из массива данных и массива имен колонок в шапке модель для таблицы    
//        MODEL = (Modelr) GRID.getModel();
            GRID.setLocale(Locale.US);
            GRID.setModel(MODEL); //модель к таблице
            GRID.MODEL = MODEL;
            for (int i = 1; i < MODEL.getColumnCount(); i++) {//установка текстовых полей в грид
                if (!"BIT_".equals(FF.SUBSTR(GRID.DATA.COLNAMES[MODEL.COLINDEX[i]], 1, 4)) //  если не boolean       
                        ) {
//                    if (GRID.DATA.COLTYPE[MODEL.COLINDEX[i]] != V.TYPE_DATETIME
//                            && GRID.DATA.COLTYPE[MODEL.COLINDEX[i]] != V.TYPE_DATE) {
//                        GRID.getColumn(i).SETCOLUMNCONTROL(new JTextField());
//                    }
                    
                    //22032018 KAA Добавил проверку на текущий объект в ячейке таблицы, чтобы не перетирало постоянно на JTextField
                    int count = GRID.COLUMN_CONTROL.size();
                    boolean cellControl = false;
                    Object objControl = null;
                    for (int j = 0; j < count; j++) {
                        if (i == (int) GRID.COLUMN_CONTROL.get(j).get(0)) {
                            cellControl = true;
                            objControl = GRID.COLUMN_CONTROL.get(j).get(1);
                            GRID.COLUMN_CONTROL.remove(j);
                            break;
                        }
                    }
                    if (!cellControl) {
                        GRID.getColumn(i).SETCOLUMNCONTROL(new JTextField(),GRID);
                    } else {
                        if (objControl.getClass().getName().equals("javax.swing.JComboBox")) {
                            JComboBox cbe = (JComboBox) objControl;
                            GRID.getColumn(i).SETCOLUMNCONTROL(cbe, GRID);
                        }
                        if (objControl.getClass().getName().equals("javax.swing.JButton")) {
                            JButton but = (JButton) objControl;
                            GRID.getColumn(i).SETCOLUMNCONTROL(but, GRID);
                        }
                        if (objControl.getClass().getName().equals("baseclass.Buttonr")) {
                            Buttonr but = (Buttonr) objControl;
                            GRID.getColumn(i).SETCOLUMNCONTROL(but, GRID);
                        }
                    }
                    //22032018 KAA
                    
                    if (GRID.DATA.COLTYPE[MODEL.COLINDEX[i]] == V.TYPE_NUMERIC) {
                        GRID.getColumn(i).TYPE = V.TYPE_NUMERIC;
                    }

                    if (GRID.DATA.COLTYPE[MODEL.COLINDEX[i]] == V.TYPE_DATETIME) {
//                     GRID.getColumn(i).setCellRenderer(new RendererDate(V.ddmmyyyyhhmm));
                    }
                    if (GRID.DATA.COLTYPE[MODEL.COLINDEX[i]] == V.TYPE_DATE) {
//                     GRID.getColumn(i).setCellRenderer(new RendererDate(V.ddmmyyyy));
                    }

                }

            }

            if (GRID.VIEWRECNO == 0)//если номер записи в таблице не нужно показывать
            {
                GRID.getColumnModel().getColumn(0).setMaxWidth(0);       //служебный не видимый  поле RECNO - начальный индекс в массиве
                GRID.getColumnModel().getColumn(0).setMinWidth(0);       //служебный не видимый              
                GRID.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
                GRID.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
            } else {
                GRID.getColumnModel().getColumn(0).setPreferredWidth(100);
            }
            int x = 0;
            for (int i = 1; i < GRID.getColumnModel().getColumnCount(); i++) {
                if (GRID.FSIZE[i - 1] == 0) {
                    x = V.COLWIDTH;
                } else {
                    x = GRID.FSIZE[i - 1];
                }
                GRID.getColumnModel().getColumn(i).setPreferredWidth(x);
            }

            //включаем сортировку по щелчку мыши в шапке
            GRID.SORTER = new TableRowSorter<TableModel>(MODEL);
            GRID.setRowSorter(GRID.SORTER);

            GRID.setAutoResizeMode(Gridr.AUTO_RESIZE_OFF);//отключаем автоширину
//        GRID.getTableHeader().setPreferredSize(new Dimension(21000, GRID.HEADERH));
            GRID.getTableHeader().setResizingAllowed(true);//разрешить изменение размеров заголовка
            GRID.getTableHeader().setReorderingAllowed(true);//разрешить перетаскивание
            GRID.setShowHorizontalLines(true); //линии сетки в таблице горизонтальные
            GRID.setShowVerticalLines(true);//вертикальные
            GRID.setSelectionMode(1);//не знаю 

            for (int i = 0; i < GRID.getRowCount(); i++) {//высота строк грида
                GRID.setRowHeight(i, GRID.ROWH);
            }
            //ВОССТАНОВЛЕНИЕ ШИРИНЫ 
            if (GRID.RESTORECOL == 1) {
                GRID.RESTORECOL();
            }
            GRID.SETHEADERH(GRID.HEADERH);
            //Изменение цвета колонки от условия
            if (!GRID.DYNAMIC_COLOR_CONDITION_2.isEmpty()) {
                for (int i = 0; i < GRID.DYNAMIC_COLOR_CONDITION.size(); i++) {
                    GRID.getColumn(Integer.parseInt(GRID.DYNAMIC_COLOR_CONDITION.get(i).get(0))).DinamycBackColor = GRID.DYNAMIC_COLOR_CONDITION.get(i).get(1);
                    GRID.getColumn(Integer.parseInt(GRID.DYNAMIC_COLOR_CONDITION.get(i).get(0))).objArr = (Object[]) GRID.DYNAMIC_COLOR_CONDITION_2.get(i).get(1);
                    GRID.getColumn(Integer.parseInt(GRID.DYNAMIC_COLOR_CONDITION.get(i).get(0))).c1 = GRID.DYNAMIC_COLOR_CONDITION_2.get(i).get(2);
                    GRID.getColumn(Integer.parseInt(GRID.DYNAMIC_COLOR_CONDITION.get(i).get(0))).c2 = GRID.DYNAMIC_COLOR_CONDITION_2.get(i).get(3);
                }
            } else {

                if (!GRID.DYNAMIC_COLOR_CONDITION.isEmpty()) {
                    for (int i = 0; i < GRID.DYNAMIC_COLOR_CONDITION.size(); i++) {
                        GRID.getColumn(Integer.parseInt(GRID.DYNAMIC_COLOR_CONDITION.get(i).get(0))).DinamycBackColor = GRID.DYNAMIC_COLOR_CONDITION.get(i).get(1);
                    }
                }
            }
            //   
        } catch (Throwable t) {
            System.out.println(" Общая ошибка:" + t.toString());
        }

        //Поиск позиции в конфиге
//        System.out.println(GRID.LOCATEFOR);
//        System.out.println(GRID.LOCATEFOR_NUM);  
        
        //KAA 09052018 закоментил. летит эксепшн, надо разбираться
//        if (GRID.LOCATEFOR_NUM != null && !GRID.LOCATEFOR.equals("")) {
//          GRID.GORECNO(A.LOCATE(GRID.ALIAS+"."+GRID.LOCATEFOR, GRID.LOCATEFOR_NUM)); //на позицию которая была активна при выходе 
//        } else 
        if (!GRID.LOCATEFOR.equals("")) {
            int col = -1;
            for (int i = 0; i < GRID.FIELD.length; i++) {
                if (GRID.FIELD[i].equals(GRID.LOCATEFOR)) { // НАХОДИМ ИМЯ ПОЛЯ
                    col = i;
                }//999
            }
            if (col != -1) {
                for (int i = 0; i < GRID.getRowCount(); i++) {
                    if (GRID.getValueAt(i, col + 1) != null && GRID.getValueAt(i, col + 1).toString().equals(GRID.LOCATEFOR_NUM)) {
                        GRID.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            }
        }

        return true;
    }

    public Object getValueAt(Gridr GRID, int row, int col, int[] COLINDEX) {//вызывается в модели грида можно переопределить для получения выражения
//             return   GRID.DATA.DATA[row][COLINDEX[col]];
        if (GRID.DATA.rowList.size() == 0) {
            return null;
        }
        if (COLINDEX.length <= col) {
            return null;
        }
        Object val;
        try {
            Object[] RECORD = (Object[]) GRID.DATA.rowList.get(row);
            if (RECORD.length <= COLINDEX[col]) {
                return null;
            }

            val = RECORD[COLINDEX[col]];
            if (GRID.DATA.COLTYPE[COLINDEX[col]] == V.TYPE_DATE) {
                if (val != null) {
                    String dat = "";
                    try {
                        dat = V.ddmmyyyy.format(val);
                    } catch (IllegalArgumentException ex2) {
                        try {
                            dat = V.ddmmyyyy.format(V.ddmmyyyy.parse(val.toString()));
                        } catch (ParseException ex) {
                            Logger.getLogger(Formr.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (Exception ex) {
                    }
                    val = (Object) dat;
                }
            }
            if (GRID.DATA.COLTYPE[COLINDEX[col]] == V.TYPE_DATETIME) {
                if (val != null) {
                    String dat = "";
                    try {
//                    ddmmyyyyhhmm.
//                    dat = V.ddmmyyyyhhmm.format(val);
                        dat = V.ddmmyyyyhhmm.format(val);
//                    dat=val.toString();
                    } catch (IllegalArgumentException ex2) {
                        try {
                            dat = V.ddmmyyyyhhmm.format(V.ddmmyyyyhhmm.parse(val.toString()));
                        } catch (ParseException ex) {
                            Logger.getLogger(Formr.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (Exception ex) {
                    }
                    val = (Object) dat;
                }
            }
            if (GRID.DATA.COLTYPE[COLINDEX[col]] == V.TYPE_NUMERIC) {

                if (val != null && val.getClass() == Double.class) {

                    V.DecFormatP.setDecimalSeparator('.');
                    V.DecFormatP.setGroupingSeparator(' ');
                    V.DecFormat.setDecimalFormatSymbols(V.DecFormatP);
                    val = V.DecFormat.format(val);
                }
            }

        } catch (Exception ex) {
            System.out.println(" Общая ошибка из класса Formr:" + ex.toString());
            return null;

        }
        return val;

    }

    /**
     * Доступ в колонки грида (не разрешает корректуру , только выделение и
     * копирование)
     *
     * @param GRID грид
     * @param col номер колонки начиная с 1
     * @return
     */
    public boolean ISEDIT(Gridr GRID, int col) {
        if (GRID.STAT_DBCLICK) { // если форма выбора и нужно обработать двойной клик
            return false;
        }
        return true; //по умолчанию разрешить чтобы был вход в поля для листинга и копирования ,читать только нужно делать переопределением метода 
    }
    //СЭТЕРЫ     

    /**
     * РАЗРЕШЕНИЕ НА ИЗМЕНЕНИЕ РАЗМЕРА
     *
     * @param TF
     */
    public void SETRESIZABLE(int TF) {
        RESIZABLE = TF;
//Фиксированный размер
        if (RESIZABLE == 0) {
            setResizable(true);
            V.LOC_BORDER = 10;
        }
        if (RESIZABLE == 1) {
            setResizable(false);
            V.LOC_BORDER = 5;
        }

    }

    public void SETMODAL(int TF) {
        if (TF == 1) {
            setMaximizable(false); // кнопка максимизации      
            setIconifiable(false);   // кнопка сворачивания    
        } else {
            setMaximizable(true); // кнопка максимизации      
            setIconifiable(true);   // кнопка сворачивания    
        }
        MODAL = TF;
    }

    /**
     * Проверка введенных значений полей формы
     *
     * @param name
     * @return
     */
    public boolean VALID_ALL(String name) {
        /*   if (obj.getClass().getName().equals("baseclass.Fieldr")) {
         Fieldr txt = (Fieldr) obj;
         if (txt.INPUTMASK.equals(V.MASK_DATE) && !txt.DATECHECK(false, txt.GETVALUE().toString())) {
         return false;
         } else if (txt.INPUTMASK.equals(V.MASK_DATETIME) && !txt.DATECHECK(true, txt.GETVALUE().toString())) {
         return false;
         } else if (V.TYPE_NUMERIC.equals(txt.TYPE) && !txt.NUMCHECK(txt.GETVALUE().toString())) {
         return false;
         }
         } */
        return true;
    }    // для дочерних объектов на проверку валидности

    /**
     * Проверка введенных значение в гриде формы вызывается от Modelr.setValueAt
     *
     * @param GRID
     * @param aValue
     * @param COLNAME
     * @return
     */
    public boolean VALID_GRID(Gridr GRID, String COLNAME, Object aValue) {
        return true;
    }

//ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ФУНКЦИИ
    /**
     * Располжение объекта
     *
     * @param objr - объект
     * @param ii - смещение от
     */
    public void locr(Component objr, int ii) {//расположение объекта относительно другого внизу
        locr(LOCOBJR, objr, ii, 0);
    }

    public void locr(Component objl, Component objr, int ii) {//расположение объекта относительно другого внизу
        locr(objl, objr, ii, 0);
    }

    public void locr(Component objl, Component objr, int xx, int yy) {//расположение объекта относительно другого справа
        int x = 0;
        int y = 0;
        if (objl == null) { //если от формы
            x = x + xx;
            y = y + yy + 10;
        } else {
            Rectangle rectl = objl.getBounds();
            Rectangle rectr = objr.getBounds();

            x = xx + rectl.x + rectl.width;
            y = rectl.height - rectr.height;
            y = rectl.y + y / 2 + yy;
        }
        LOCOBJR = objr;
        objr.setLocation(x, y);
    }

    public void locd(Component objr, int ii) {//расположение объекта относительно другого внизу
        locd(LOCOBJD, objr, ii, 0);
    }

    public void locd(Component objl, Component objr, int ii) {//расположение объекта относительно другого внизу
        locd(objl, objr, ii, 0);
    }

    /**
     * Расположение объекта относительно другого внизу
     *
     * @param objl - объект относительно которого
     * @param objr - перемещаемый объект
     * @param yy - смещение по вертикали от нижней границы относительного
     * объекта
     * @param xx - смещение по горизонтали от левой границы относительного
     * объекта
     */
    public void locd(Component objl, Component objr, int yy, int xx) {//расположение объекта относительно другого внизу
        int x = 2;
        int y = 0;
        if (objl == null) { //если от формы
            x = x + xx;
            y = y + yy + 10;
        } else {
            Rectangle rectl = objl.getBounds();
            Rectangle rectr = objr.getBounds();

            y = yy + rectl.y + rectl.height + 2;
//          x = rectl.width - rectr.width;
//          x = rectl.x+x / 2 + xx;
            x = xx + rectl.x;
        }
        LOCOBJD = objr;
        LOCOBJR = objr;
        objr.setLocation(x, y);
    }

    public void locu(Component objr, int ii) {//расположение объекта относительно другого внизу
        locu(LOCOBJD, objr, ii, 0);
    }

    public void locu(Component objl, Component objr, int ii) {//расположение объекта относительно другого внизу
        locu(objl, objr, ii, 0);
    }

    /**
     * Расположение объекта относительно другого сверху
     *
     * @param objl - объект относительно которого
     * @param objr - перемещаемый объект
     * @param yy - смещение по вертикали вверх от нижней границы относительного
     * объекта
     * @param xx - смещение по горизонтали от левой границы относительного
     * объекта
     */
    public void locu(Component objl, Component objr, int yy, int xx) {//расположение объекта относительно другого внизу
        int x = 2;
        int y = this.getHeight();
        if (objl == null) { //если от формы
            x = x + xx;
            y = y - yy - 10;
        } else {
            Rectangle rectl = objl.getBounds(); //относительно объекта
            Rectangle rectr = objr.getBounds();

            y = rectl.y - rectl.height - yy - 2;
            x = xx + rectl.x;
        }
        LOCOBJD = objr;
        LOCOBJR = objr;
        objr.setLocation(x, y);
    }

    /**
     * Метод расположения объекта на форме относительно углов формы или другого
     * объекта
     *
     * @param obj объект
     * @param objx по горизонтали относительно объекта objx ,если null -
     * относительно углов формы
     * @param corner_x направление Vars.LOC_LEFT Vars.LOC_CENTR Vars.LOC_RIGHT
     * @param xx смещение по x
     * @param objy по вертикали относительно объекта objy ,если null -
     * относительно углов формы
     * @param corner_y направление Vars.LOC_UP Vars.LOC_CENTR Vars.LOC_DOWN
     * @param yy смещение по y
     */
    public void locate(Component obj, Component objx, int corner_x, int xx, Component objy, int corner_y, int yy) {//расположение объекта относительно другого
        Rectangle rect = obj.getBounds();
        Rectangle rectot = THISFORM.getBounds();

        int H, W; //чистые высота и ширина без заголовка и границ
        int x = 0;
        int y = 0;
        H = rectot.height - V.LOC_HEADRE - V.LOC_BORDER;
        W = rectot.width - 2 * V.LOC_BORDER;
        //    по x        
        if (objx == null) { //если от формы
            if (corner_x == V.LOC_CENTR) {
                x = W - rect.width + xx;
                x = x / 2;
            }
            if (corner_x == V.LOC_LEFT) {
                x = xx;
            }
            if (corner_x == V.LOC_RIGHT) {//
                x = W - rect.width - xx;
            }
        }
        if (objx != null) { //если от элемента на форме
            Rectangle rectx = objx.getBounds();
            H = rectx.height;
            W = rectx.width;
            if (corner_x == V.LOC_CENTR) {
                x = rectx.x + W / 2 - rect.width / 2 + xx;
            }

            if (corner_x == V.LOC_LEFT) {
                //x = rectx.x - W - xx;
                x = rectx.x - obj.getWidth() - xx;
            }
            if (corner_x == V.LOC_RIGHT) {
                x = (rectx.x + W) + xx;
            }

        }

        H = rectot.height - V.LOC_HEADRE - V.LOC_BORDER;
        W = rectot.width - 2 * V.LOC_BORDER;
        //    по y        
        if (objy == null) {//если от формы

            if (corner_y == V.LOC_CENTR) {
                y = H - rect.height;
                y = y / 2 + yy;
            }

            if (corner_y == V.LOC_UP) {
                y = yy;
            }
            if (corner_y == V.LOC_DOWN) {
                y = H - rect.height - yy;
                //y=rectot.height-H-yy;
            }

        }
        if (objy != null) {//если от формы

            Rectangle recty = objy.getBounds();
            H = recty.height;
            W = recty.width;

            if (corner_y == V.LOC_CENTR) {
                y = recty.y + H / 2 - rect.height / 2 + yy;
            }

            if (corner_y == V.LOC_UP) {
                y = recty.y - rect.height - yy;
            }

            if (corner_y == V.LOC_DOWN) {
                y = recty.y + H + yy;
            }
        }
        obj.setLocation(x, y);
    }
    
/** 28.11.2018 RKV
 * Поиск объекта формы по его наименованию и массиву элементов
 * @param <T> - обобщенный
 * @param name - имя объекта
 * @param obj - массив объектов (класс пример F-поля или B- кнопки)
 * @return номер индекса , если не найден -1
 */
    public <T> int GET(String name,T... obj)    {    
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null && ((Component)obj[i]).getName().toUpperCase().equals(name.toUpperCase())) {
                return i;
            }
        } //for
        return -1;
    }
    
    /**
     * Получение объекта "кнопка" по его имени
     *
     * @param name
     * @return
     */
    public Buttonr GETB(String name) {
        for (int i = 0; i < B.length; i++) {
            if (B[i] != null && B[i].getName().toUpperCase().equals(name.toUpperCase())) {
                return B[i];
            }
        } //for
        P.MESSERR("Не найден объект-кнопка " + name);
        return null;

    }

    /**
     * Получение объекта "поле ввода" по его имени
     *
     * @param name
     * @return
     */
    public Fieldr GETF(String name) {
        for (int i = 0; i < F.length; i++) {
            if (F[i] != null && F[i].getName().toUpperCase().equals(name.toUpperCase())) {
                return F[i];
            }
        } //for
        P.MESSERR("Не найден объект-поле " + name);
        return null;

    }

    
    /**
     * Получение объекта CheckBoxr по его имени
     *
     * @param name
     * @return
     */
    public CheckBoxr GETCH(String name) {
        for (int i = 0; i < CH.length; i++) {
            if (CH[i] != null && CH[i].getName().toUpperCase().equals(name.toUpperCase())) {
                return CH[i];
            }
        } //for
        P.MESSERR("Не найден объект-CheckBoxr " + name);
        return null;

    }
    /**
     * Получение объекта ComboBoxr по его имени
     *
     * @param name
     * @return
     */
    public ComboBoxr GETCB(String name) {
        for (int i = 0; i < CB.length; i++) {
            if (CB[i] != null && CB[i].GETNAME().toUpperCase().equals(name.toUpperCase())) {
                return CB[i];
            }
        } //for
        P.MESSERR("Не найден объект-ComboBoxr " + name);
        return null;

    }

    /**
     * Получение объекта "поле ввода" по его имени
     *
     * @param name
     * @return
     */
    public Textr GETT(String name) {
        for (int i = 0; i < T.length; i++) {
            if (T[i] != null && T[i].getName().toUpperCase().equals(name.toUpperCase())) {
                return T[i];
            }
        } //for
        P.MESSERR("Не найден объект-поле " + name);
        return null;

    }

        /**
     * Получение объекта "поле ввода" по его имени
     *
     * @param name
     * @return
     */
    public JPanelr GETJ(String name) {
        for (int i = 0; i < J.length; i++) {
            if (J[i] != null && J[i].getName().toUpperCase().equals(name.toUpperCase())) {
                return J[i];
            }
        } //for
        P.MESSERR("Не найден объект-поле " + name);
        return null;

    }

    /**
     * Получение объекта "поле ввода" по его имени
     *
     * @param name
     * @return
     */
    public Editr GETE(String name) {
        for (int i = 0; i < E.length; i++) {
            if (E[i] != null && E[i].getName().toUpperCase().equals(name.toUpperCase())) {
                return E[i];
            }
        } //for
        P.MESSERR("Не найден объект-поле " + name);
        return null;

    }
    
    /**
     * Получение объекта метка по его имени
     *
     * @param name
     * @return
     */
    public Labelr GETL(String name) {
        for (int i = 0; i < L.length; i++) {
            if (L[i] != null && L[i].getName().toUpperCase().equals(name.toUpperCase())) {
                return L[i];
            }
        } //for
        P.MESSERR("Не найден объект-метка " + name);
        return null;

    }

    /**
     * Получение объекта метка по его имени
     *
     * @param name
     * @return
     */
    public JRadioButtons GETO(String name) {
        for (int i = 0; i < O.length; i++) {
            if (O[i] != null && O[i].getName().toUpperCase().equals(name.toUpperCase())) {
                return O[i];
            }
        } //for
        P.MESSERR("Не найден объект-метка " + name);
        return null;

    }

    /**
     * Получение объекта "грид" по его имени
     *
     * @param name
     * @return
     */
    public Gridr GETG(String name) {
        for (int i = 0; i < G.length; i++) {
            if (G[i] != null && G[i].getName().toUpperCase().equals(name.toUpperCase())) {
                return G[i];
            }
        } //for
        P.MESSERR("Не найден объект-ГРИД " + name);
        return null;

    }

    private Comparator<?> comparator(int col, String type, Gridr GRID) { //сортировка
        if (type.equals("N") && !GRID.getColumnClass(col).toString().equals("class java.lang.Integer")) {
            return new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    o1 = o1.replaceAll(" ", "");
                    o2 = o2.replaceAll(" ", "");
                    if (!o1.contains(".")) {
                        o1 = o1 + ".0";
                    }
                    if (!o2.contains(".")) {
                        o2 = o2 + ".0";
                    }
                    if (Double.parseDouble(o1) == Double.parseDouble(o2)) {
                        return 0;
                    } else if (Double.parseDouble(o1) > Double.parseDouble(o2)) {
                        return 1;
                    } else {
                        return -1;
                    }
//                    o1 = o1.replaceAll(" ", "");
//                    o2 = o2.replaceAll(" ", "");
//                    if (o1 == null || o2 == null || o2.equals("") || o1.equals("")) {
//                        System.out.println("ERROR - " + o1 + " - " + o2);
//                    }
//                    try {
//                        Integer.parseInt(o1);
//                        Integer.parseInt(o2);
//                    } catch (NumberFormatException exc) {
////                        return (int) (Double.parseDouble(o1) - Double.parseDouble(o2));
//                        if ((int) (Double.parseDouble(o1) - Double.parseDouble(o2)) < 0) {
//                            return -1;
//                        } else if ((int) (Double.parseDouble(o1) - Double.parseDouble(o2)) == 0) {
//                            return 0;
//                        } else {
//                            return 1;
//                        }
//                    }
////                    return Integer.parseInt(o1) - Integer.parseInt(o2);
//                    if (Integer.parseInt(o1) - Integer.parseInt(o2) < 0) {
//                        return -1;
//                    } else if (Integer.parseInt(o1) - Integer.parseInt(o2) == 0) {
//                        return 0;
//                    } else {
//                        return 1;
//                    }
                }
            };
        }
        if (type.equals("D")) {
            return new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    Date d1 = null, d2 = null;
                    try {
                        d1 = sdf.parse(o1);
                        d2 = sdf.parse(o2);
                    } catch (ParseException ex) {
                    }
                    return d1.compareTo(d2);
                }
            };
        }
        if (type.equals("T")) {
            return new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    Date d1 = null, d2 = null;
                    try {
                        d1 = sdf.parse(o1);
                        d2 = sdf.parse(o2);
                    } catch (ParseException ex) {
                    }
                    return d1.compareTo(d2);
                }
            };
        }
        return GRID.SORTER.getComparator(col);
    }

    /**
     * Устанавливает ссылку, для файла CHM
     */
    private void setHelp() {
        if (V.CONN1 == null) {
            return;
        }
        String result = null;
        String className = this.getClass().getName();
        try (Statement st = V.CONN1.createStatement();
                ResultSet rs = st.executeQuery("SELECT LINK FROM ST_FAQ WHERE CLASS = '" + className.substring(className.lastIndexOf('.') + 1) + "'")) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            //  бля когда хоть какие  коомменты будут ?
        }
        HELP = result;
    }

    /**
     * Порядок перехода(получения фокуса) по объектам формы
     *
     * @param <T> - обобщенный тип
     * @param obj - упорядоченный список элементов неопределенного типа
     */
    public <T> void ORDERS(T... obj) {  //http://www.sbp-program.ru/java/sbp-focustraversalpolicy.htm  - переделать по новому можно так
        for (int i = 0; i < obj.length - 1; i++) {
            ((JComponent) obj[i]).setNextFocusableComponent((JComponent) obj[i + 1]);
        }
    }

} //конец класса
