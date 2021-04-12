package baseclass;

import aplclass.InfoPanelAdapter;
import aplclass.MessCenterWindow;
import aplclass.MessWindow;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UnsupportedLookAndFeelException;
import prg.P;
import prg.START;
import prg.V;
import support.Help;

public class Screen extends JFrame {

    public START PRG = null;
    private Dimension SIZE; //Размеры формы
    private Point LOCATION;// Расположение формы
    public int XX;//начальная ширина
    public int YY;//начальная высота
    public Labelr INF, INF1, INF2, INF3, INF4, INF5;
    public Textr txtArea;
    public JScrollPane txtScrollPane = new JScrollPane();
    boolean infoflag = true;
    public MessWindow MESS;
    public MessCenterWindow MESSC;
    JPanel iconPan;
    public Buttonr B[]; //резерв кнопки
//    public ToolBarr toolBar;

    public void DESTROY() {//при закрытии
        if (PRG != null) {
            PRG.DESTROY();
        }
        SAVELOCSIZE();// запомнить координаты и размер окна
        P.SAVETOSYS();

        P.SAVE_ENV_PROP(V.CIPHER_FILE_PATH, V.FULLSCREEN_FLAG, V.CIPHER_PORT, V.CIPHER_TIME, V.INFO_FLAG_ENV, V.CIPHER_MARKA);
        //if (V.FALSETRUE && V.ARM != 5 && V.ARM != 99) {
        if (V.FALSETRUE  && V.ARM != 99) { //ИСПРАВИЛ RKV 07/08/2019
            P.SESSION_CLOSE();
        }
    }

    // для дочерних объектов на click мышки
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
    }

    public void MULTICLICK_ALL(String name) {
    } //для дочерних объектов на множественный клик

    public void CLICKR_ALL(String name) {
    }    // для дочерних объектов на click мышки

    public void DBLCLICK_ALL(String name) {
    }    // для дочерних объектов на двойной click мышки

    public Screen() {
    }

    ;
    public Screen(String name, String title, int width, int height) {
        super(title); //заголовок окна
//        ArrayList<Image> iCont = new ArrayList<Image>();
        //      iCont.add(new javax.swing.ImageIcon(getClass().getResource("/icons/POS-21.jpg")).getImage());
        //setIconImages(iCont);
        SETICON("/icons/POS-21.jpg");
        //Установка стиля окна 
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) { //  Metal Nimbus CDE/Motif Windows Windows Classic  com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
            if (V.FORMVID.equals(info.getName())) {
                try {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Formr.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }

        super.setName(name); //присвоение имени объекту                       
        super.setSize(width, height);//ширина и высота окна
        setMinimumSize(new Dimension(width, height));//Минимальные размеры формы            
        XX = width;
        YY = height;
        super.setLocationRelativeTo(null); //по центру основного окна
        super.setLayout(null);
        V.DESKTOP = new JDesktopPane();//мультидокументальная панель
        this.setContentPane(V.DESKTOP);//установка понели на главном окне (только в конструкторе)    
        B = new Buttonr[99];
        INF = new Labelr("INF1", "Информация по версии программы", 1200, 30);//        
        INF1 = new Labelr("INF1", "Информация по активному экрану", 400, 30);//        
        INF2 = new Labelr("INF2", "Информация по активной форме", 400, 30);//
        INF3 = new Labelr("INF2", "Информация по активному гриду", 400, 30);//
        INF4 = new Labelr("INF4", "Информация по активному алиасу", 400, 30);//
        INF5 = new Labelr("INF5", "Информация", 1200, 100);//
        INF1.setVisible(false);
        INF2.setVisible(false);
        INF3.setVisible(false);
        INF4.setVisible(false);
        this.add(INF, V.LAYER_FORM);
        this.add(INF1, V.LAYER_INF);
        this.add(INF2, V.LAYER_INF);
        this.add(INF3, V.LAYER_INF);
        this.add(INF4, V.LAYER_INF);
        this.add(INF5, V.LAYER_FORM);
        INF.setFont(new Font("Arial", 2, 16));
        INF.setForeground(Color.WHITE);
        INF.setText(V.NVER);
        INF5.setFont(new Font("Arial", 2, 30));
        INF5.setForeground(Color.WHITE);
        INF5.setText("");
        //V.calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());        
        V.calendar.setTime(new java.util.Date());
        if (V.calendar.get(java.util.Calendar.MONTH) == 2 && V.calendar.get(java.util.Calendar.DAY_OF_MONTH) <= 8 && V.calendar.get(java.util.Calendar.DAY_OF_MONTH) > 5) {
            INF.setText(INF.getText() + "    Дорогие женщины ! Поздравляем вас с праздником 8 Марта ! ");
        }

        //информационная панель
        //if (!V.FLAG_KASSA && V.ARM != 5 && V.ARM != 99) {
        if (!V.FLAG_KASSA && V.ARM != 99) {
            txtArea = new Textr("info_txt", "", getWidth() - 16, 82);
            V.INFO_TXT = txtArea;

            txtScrollPane.setSize(getWidth() - 16, 82);
            txtScrollPane.setOpaque(true);
            txtScrollPane.setViewportView(txtArea);
            txtScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            InfoPanelAdapter adapter = new InfoPanelAdapter(txtScrollPane, this);
            txtScrollPane.addMouseListener(adapter);
            txtScrollPane.addMouseMotionListener(adapter);
            txtScrollPane.addComponentListener(adapter);

            V.INFO_TXT_SCROLL = txtScrollPane;
//            add(txtScrollPane, V.LAYER_INF);
            add(txtScrollPane, V.LAYER_STAT);
            V.INFO_FLAG = true;

            //панель для свернутых форм
            iconPan = new JPanel();
            iconPan.setSize(this.getWidth(), 100);
            iconPan.setLocation(0, this.getHeight() - 28);
            this.add(iconPan, V.LAYER_STAT);
            //
        }
        //

        MESS = new MessWindow(this);
        MESS.setVisible(false);

        MESSC = new MessCenterWindow(this);
        MESSC.setVisible(false);

        RESTORELOCSIZE();//Восстановоение размера и расположения с предыдущего закрытия
        LOC_ABOUT();

        this.addWindowListener(new WindowAdapter() {//слушатель окна на закрытие
            @Override
            public void windowClosing(WindowEvent e) { // событие закрытие окна
                //21112017 КАА запрет на закрытие программы, если открыта форма с обменом
                V._SCREEN.setDefaultCloseOperation(3);
                if (!P.exchangeCloseCheck()) {
                    return;
                } else {
                    //
                    DESTROY();
                }
            } //при закрытии
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                RESIZED(evt);
                V._SCREEN.INF1.setText(V._SCREEN.getName() + " X:" + V._SCREEN.getX() + " Y:" + V._SCREEN.getY() + " W:" + V._SCREEN.getWidth() + " H:" + V._SCREEN.getHeight());
            }

            public void componentMoved(java.awt.event.ComponentEvent evt) {
                V._SCREEN.INF1.setText(V._SCREEN.getName() + " X:" + V._SCREEN.getX() + " Y:" + V._SCREEN.getY() + " W:" + V._SCREEN.getWidth() + " H:" + V._SCREEN.getHeight());
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//нажатие клавиш
                    AWTKeyStroke ak = AWTKeyStroke.getAWTKeyStrokeForEvent(e);
/*                   if(ak.equals(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_MASK)))  //CTRL+P
                   {
                       P._SCREEN_TO_FILE(1); //СНИМОК ЭКРАНА
                   }
  */                  
                   if(ak.equals(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_F4,InputEvent.ALT_MASK)))  //ALT+F4
                   {
                //21112017 КАА запрет на закрытие программы, если открыта форма с обменом
                V._SCREEN.setDefaultCloseOperation(3);
                if (!P.exchangeCloseCheck()) {
                    return;
                } else {
                    //
                    DESTROY();
                }
                   }                
                P.ALERT("");
                KEYPRESS(e.getComponent(), e);
            }
        });
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {

            private KeyEvent last;

            private KeyEvent now;

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1 && Help.isProjectHasHelp()) {
                    now = e;
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            if (last == null || now.getWhen() - last.getWhen() > 500) {
                                last = now;
                                Help.execute(V.ACTIVEFORM == null ? null : V.ACTIVEFORM.HELP);
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                P.ALERT("");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                P.ALERT("");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public void LOC_ABOUT() {
        INF.setLocation(20, 30);
        INF1.setLocation(20, this.getHeight() - INF1.getHeight() - 60);
        INF2.setLocation(this.getWidth() - INF2.getWidth() - 20, this.getHeight() - INF2.getHeight() - 60);
        INF3.setLocation(20, INF1.getY() - 20);
        INF4.setLocation(this.getWidth() - INF4.getWidth() - 20, INF3.getY());
        INF5.setLocation(20, 100);
        MESS.setLocation(this.getWidth() - MESS.getWidth() - 20, 10);
        MESSC.setLocation(this.getWidth() / 2 - MESSC.getWidth() / 2 - 20, this.getHeight() / 2 - 80);
        if (V.ACTIVEFORM != null) {
            V.ACTIVEFORM.MAXIMIZED = false;
        }
        if (V.TOOLBAR != null) {
            V.TOOLBAR.setSize(this.getWidth(), V.TOOLBAR.getHeight());
        }
        if (V.INFO_TXT_SCROLL != null) {
            iconPan.setLocation(0, this.getHeight() - 88);
            iconPan.setSize(this.getWidth(), 100);
            if (V.INFO_FLAG) {
                txtScrollPane.setLocation(0, this.getHeight() - txtScrollPane.getHeight() - 60);
            } else {
                txtScrollPane.setLocation(0, this.getHeight() - txtScrollPane.getHeight() - 85);
            }
            if ((double) this.getHeight() / txtScrollPane.getHeight() < 3) {
                txtScrollPane.setLocation(0, this.getHeight() - txtScrollPane.getHeight() - 85 + txtScrollPane.getHeight() - this.getHeight() / 3);
                txtScrollPane.setSize(this.getWidth() - this.getWidth() + this.getContentPane().getWidth(), this.getHeight() / 3);
            } else {
                txtScrollPane.setSize(this.getWidth() - this.getWidth() + this.getContentPane().getWidth(), txtScrollPane.getHeight());
            }
//            txtScrollPane.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), txtScrollPane.getHeight());

        }

        //изменение размеров всех открытых форм при изменении размера главного окна, если нет привязки к инфопанели. если форма уходит за пределы окна - выравниваем ее размер.
        if (V.MAPFORMS != null) {
            Iterator entries = V.MAPFORMS.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) entries.next();
                Formr form = (Formr) thisEntry.getValue();
                if (V.INFO_TXT_SCROLL == null) {
                    if (form.getHeight() > this.getContentPane().getHeight()) {
                        form.setBounds(form.getX(), 0, form.getWidth(), this.getContentPane().getHeight());
                    }
                    if (form.getWidth() > this.getContentPane().getWidth()) {
                        form.setBounds(0, form.getY(), this.getContentPane().getWidth(), form.getHeight());
                    }
                } else {
                    if (V.INFO_TXT_SCROLL.isVisible()) {
                        if (form.getHeight() > this.getHeight() - 115 - V.INFO_TXT_SCROLL.getHeight()) {
                            form.setBounds(form.getX(), 30, form.getWidth(), this.getHeight() - 118 - V.INFO_TXT_SCROLL.getHeight());
                        }
                        if (form.getWidth() > this.getContentPane().getWidth()) {
                            form.setBounds(0, form.getY(), this.getContentPane().getWidth(), form.getHeight());
                        }
                    } else {
                        if (form.getHeight() > this.getHeight() - 115) {
                            form.setBounds(form.getX(), 30, form.getWidth(), this.getHeight() - 118);
                        }
                        if (form.getWidth() > this.getContentPane().getWidth()) {
                            form.setBounds(0, form.getY(), this.getContentPane().getWidth(), form.getHeight());
                        }
                    }
                }
            }
        }
        //

//    MESS.setLocation(100,100);        
    }

    public void RESIZED(java.awt.event.ComponentEvent evt) { //изменение размеров формы
        LOC_ABOUT();
    }

    //восстановление последнего расположения и величины окна
    private void RESTORELOCSIZE() {
        try {
            //Dimension maxxy = Toolkit.getDefaultToolkit().getScreenSize(); // старое определение размера экрана только для одного
Configs proper = new Configs(V.fileNameScreenconf);
int width=0,height=0;            
           width = new Integer(proper.getProperty(getName() + "$" + "WIDTH"));
           height = new Integer(proper.getProperty(getName() + "$" + "HEIGHT"));
int x=0,y=0;            
           x = new Integer(proper.getProperty(getName() + "$" + "X_"));
           y = new Integer(proper.getProperty(getName() + "$" + "Y_"));
            
int swidth=0;            
int screenWidth;
DisplayMode maxxy=null;
//определяем номер экрана 
GraphicsEnvironment environment =   GraphicsEnvironment.getLocalGraphicsEnvironment();
GraphicsDevice[] devices = environment.getScreenDevices(); // количество экранов
    for (int i=0; i<devices.length; i++) {
         maxxy = devices[i].getDisplayMode();  // экран
         screenWidth = maxxy.getWidth();       // ширина экрана  
         swidth=swidth+screenWidth;           // сумма ширины экранов
        if (x+10<swidth) {                   // если позиция попадает на экран 
          break;                            // то делаем экран текущим
        } else {
          maxxy=null;                      // если вообще не попало то экран null
        }
    }
 
            if (maxxy == null) {
                setSize(width, height);
            } else {
                if (maxxy.getHeight() > height & maxxy.getWidth() > width) //если максимальная высота или ширина больше данных
                {
                    setSize(width, height);
                } // установить широту и высоту
                else {
                    setExtendedState(MAXIMIZED_BOTH);
                    // setSize((int) maxxy.getWidth() - 20, (int) maxxy.getHeight() - 40);
                }                      // установить широту и высоту
            }
//            if (x + this.getWidth() + 10 >= maxxy.getWidth()) {
            if (x + this.getWidth() + 10 >= swidth) {
                
                x = 5;
            }
            if (y + this.getHeight() + 10 >= maxxy.getHeight()) {
                y = 5;
            }

            if (x == 0 & y == 0) { ////если координаты равны нулю или больше допустимых
                super.setLocationRelativeTo(null); //по центру основного окна
            } else {
                setLocation(x, y);
            }//иначе установить координаты
        } catch (Exception e) {
            System.out.println("Не найдены свойства окна");
        }

//            Preferences prefs = Preferences.userNodeForPackage(getClass()); //класс для сохранения значений переменнных 
////        Rectangle  maxxy = this.getMaximizedBounds();//определение максимальных границ формы
////          Dimension  maxxy = this.getMaximumSize();//определение максимальных границ формы                
//            Dimension maxxy = Toolkit.getDefaultToolkit().getScreenSize();
//            int width = prefs.getInt("WIDTH_" + getName(), (int) maxxy.getWidth() - 20);
//            int height = prefs.getInt("HEIGHT_" + getName(), (int) maxxy.getHeight() - 40);
//            if (maxxy == null) {
//                setSize(width, height);
//            } else {
//                if (maxxy.getHeight() > height & maxxy.getWidth() > width) //если максимальная высота или ширина больше данных
//                {
//                    setSize(width, height);
//                } // установить широту и высоту
//                else {
//                    setSize((int) maxxy.getWidth() - 20, (int) maxxy.getHeight() - 40);
//                }                      // установить широту и высоту
//
//            }
//            int x = prefs.getInt("X_" + getName(), 0);
//            int y = prefs.getInt("Y_" + getName(), 0);
//            if (x + this.getWidth() + 10 >= maxxy.getWidth()) {
//                x = 5;
//            }
//            if (y + this.getHeight() + 10 >= maxxy.getHeight()) {
//                y = 5;
//            }
//
//            if (x == 0 & y == 0) { ////если координаты равны нулю или больше допустимых
//                super.setLocationRelativeTo(null); //по центру основного окна
//            } else {
//                setLocation(x, y);
//            }//иначе установить координаты
    }
    //сохранение при выходе последнего расположения и величины окна

    private void SAVELOCSIZE() {
        Configs proper = new Configs(V.fileNameScreenconf);
        proper.setProperty(getName() + "$" + "WIDTH", new Integer(getWidth()).toString());
        proper.setProperty(getName() + "$" + "HEIGHT", new Integer(getHeight()).toString());
        proper.setProperty(getName() + "$" + "X_", new Integer(getX()).toString());
        proper.setProperty(getName() + "$" + "Y_", new Integer(getY()).toString());
        proper.saveProperties("Screen Congigyration");
//        SIZE = getSize();
//        LOCATION = getLocation();
//        Preferences prefs = Preferences.userNodeForPackage(getClass());
//        prefs.putInt("WIDTH_" + getName(), SIZE.width);
//        prefs.putInt("HEIGHT_" + getName(), SIZE.height);
//        prefs.putInt("X_" + getName(), LOCATION.x);
//        prefs.putInt("Y_" + getName(), LOCATION.y);

    }

///МЕТОДЫ ВЫПОЛНЯЕМЫЕ ПРИ СОБЫТИИ СЛУШАТЕЛЕЙ    
    public void KEYPRESS(Component obj, KeyEvent e) {//метод выполняемый после нажатия клавиши на любом из элементов
        int key = e.getKeyCode();
        if (e.isAltDown()) {
            //P.ALERT(FF.STR(key));
            for (int i = 0; i < B.length; i++) {
                if (B[i] != null && B[i].ALTKEY == key - 48) { // ЕСЛИ КНОПКА ПО ALT+НОМЕР ЦИФРЫ
                    this.CLICK_ALL(B[i].getName());
                }
            } //for

        }
        KEYPRESS(obj, key);
    }

    public void KEYPRESS(Component obj, int key) {//метод выполняемый после нажатия клавиши на любом из элементов

//              System.out.print(" KEY:"+key+"         "+e.getKeyChar()+"\n");
        if (obj.getClass().getSimpleName().equalsIgnoreCase("Buttonr")) //проверяем базовый класс объекта 
        {
            Buttonr b = (Buttonr) obj;  //из объекта типа Component формирет ссылку на объект типа Buttonr для доспупа ко всем его свойствам и методам
            if (key == V.KEY_ENTER) {
                b.CLICK();// при нажатии на кнопку Enter выполнить метод click
            }
            if (key == V.KEY_LEFT) {
                b.transferFocusBackward();// при нажатии на кнопку влево выполнить фокус на предыдущий элемент
            }
            if (key == V.KEY_RIGTH) {
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
    }//KEYPRESS

    public void SETICON(String file) {
        ArrayList<Image> iCont = new ArrayList<Image>();
        iCont.add(new javax.swing.ImageIcon(getClass().getResource(file)).getImage()); //"/icons/POS-21.jpg"
        //iCont.add(new ImageIcon(getClass().getResource(file)).getImage()); //"/icons/POS-21.jpg"
        
        setIconImages(iCont);

    }

}
