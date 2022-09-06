package prg;

import aplclass.DigitsToTextConverter;
import aplclass.MonitorThread;
import aplclass.PopMenu;
import aplclass.SerialListener;
import baseclass.Buttonr;
import baseclass.CheckBoxr;
import baseclass.ComboBoxr;
import baseclass.Configs;
import baseclass.Crypt;
import baseclass.Cursorr;
import baseclass.Editr;
import baseclass.Fieldr;
import baseclass.Fieldrp;
import baseclass.Formr;
import baseclass.Gridr;
import baseclass.JPanelr;
import baseclass.JRadioButtons;
import baseclass.Labelr;
import baseclass.ListBoxr;
import baseclass.Reportr;
import baseclass.Screen;
import baseclass.Textr;
import baseclass.Treer;
import client.Post;
import com.crccalc.Crc16;
import com.crccalc.CrcCalculator;
import com.onbarcode.barcode.Code128;
import com.onbarcode.barcode.Code93;
import com.onbarcode.barcode.DataMatrix;
import com.onbarcode.barcode.IBarcode;
import com.onbarcode.barcode.QRCode;
import forms.conn;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.PrinterResolution;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuComponent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.print.PrintServiceLookup;

import static prg.V.CONFIGS_DIRECTORY;
import static prg.V.markaNames;

/**
 * Набор глобальных методов по принципу процедурного файла
 *
 * @author Kostya
 */
public class P {

    public static Fieldrp addobjPF(Formr thisform, String name, String title, int w, int h) { //поле пароля
        Fieldrp obj = new Fieldrp(name, title, w, h);//
        obj.THISFORM = thisform;
        thisform.add(obj);
        return obj;
    }

    public static Treer addobjTR(Formr thisform, String name, String root, int width, int height) {
        Treer obj = new Treer(thisform, name, root, width, height);
        obj.THISFORM = thisform;
        thisform.FILL_TREE(obj);
        thisform.TREES.add(obj);//в список деревьев формы добавлеем новое
        return obj;

    }

    public static ListBoxr addobjLB(Formr thisform, String name, String[] str, int count) {
        ListBoxr obj = new ListBoxr(thisform, name, str, count);
        obj.THISFORM = thisform;
        return obj;
    }

    public static ListBoxr addobjLB(Formr thisform, String name, String[] str, int count, int width) {
        ListBoxr obj = new ListBoxr(thisform, name, str, count, width);
        obj.THISFORM = thisform;
        return obj;
    }

    public static ListBoxr addobjLB(Formr thisform, String name, String[] str, int count, boolean flag) {
        ListBoxr obj = new ListBoxr(thisform, name, str, count, flag);
        obj.THISFORM = thisform;
        return obj;
    }

    public static ComboBoxr addobjCB(Formr thisform, String name, String[] str) {
        ComboBoxr obj = new ComboBoxr(thisform, name, str);
        obj.THISFORM = thisform;
        return obj;
    }

    public static ComboBoxr addobjCB(Formr thisform, String name, String[] str, int width, int height) {
        ComboBoxr obj = new ComboBoxr(thisform, name, str, width, height);
        obj.THISFORM = thisform;
        return obj;
    }

    public static CheckBoxr addobjCH(Formr thisform, String name, String str) {
        CheckBoxr obj = new CheckBoxr(thisform, name, str);
        obj.THISFORM = thisform;
        thisform.add(obj);
        return obj;
    }

    public static JRadioButtons addobjO(Formr thisform, String[] str) {
        return addobjO(thisform, str, 1);
    }

    public static JRadioButtons addobjO(Formr thisform, String[] str, int select) {
        JRadioButtons obj = new JRadioButtons(thisform, str, select);
        obj.THISFORM = thisform;
        return obj;
    }

    public static JRadioButtons addobjO(Formr thisform, String[] str, int select, boolean horizontal) {
        JRadioButtons obj = new JRadioButtons(thisform, str, select, horizontal);
        obj.THISFORM = thisform;
        return obj;
    }

    public static Buttonr addobjB(Formr thisform, String name, String title, String desc) {
        return addobjB(thisform, name, title, 0, 0, desc);
    }

    public static Buttonr addobjB(Formr thisform, String name, String title, int w, int h, String desc) {
        Buttonr obj = new Buttonr(name, title, w, h);//
        obj.THISFORM = thisform;
        obj.setToolTipText(desc);
        thisform.add(obj);
        return obj;
    }

    public static Buttonr addobjB(Screen thisform, String name, String title, int w, int h, String desc) {
        Buttonr obj = new Buttonr(name, title, w, h);//
        obj.THISSCREEN = thisform;
        obj.setToolTipText(desc);
        thisform.add(obj);
        return obj;
    }

    public static Labelr addobjL(Formr thisform, String name, String title) {
        return addobjL(thisform, name, title, 0, 0);
    }

    public static Labelr addobjL(Formr thisform, String name, String title, int w, int h) {
        Labelr obj = new Labelr(name, title, w, h);//
        obj.THISFORM = thisform;
        thisform.add(obj);
        return obj;
    }

    public static Textr addobjT(Formr thisform, String name, String title, int w, int h) {
        Textr obj = new Textr(thisform, name, title, w, h);//
        obj.THISFORM = thisform;
//        thisform.add(obj);
        return obj;
    }

    public static Fieldr addobjF(Formr thisform, String name, String title, int w, int h) {
        Fieldr obj = new Fieldr(name, title, w, h);//
        obj.THISFORM = thisform;
        thisform.add(obj);
        return obj;
    }

    public static Editr addobjE(Formr thisform, String name, String title, int w, int h) {
        Editr obj = new Editr(thisform, name, title, w, h);//
        obj.THISFORM = thisform;
//        thisform.add(obj);
        return obj;
    }
    
    public static  JPanelr addobjJ(Formr thisform, String name, int w, int h) {
        JPanelr obj = new JPanelr(thisform, name,w,h);//
        obj.THISFORM = thisform;
  //      thisform.getContentPane().add(obj.SCROLL); //добавляем на панель формы   
        
 //       thisform.add(obj);
        return obj;
    }

    
    public static Gridr addobjG(Formr thisform, String name, int x, int y, int w, int h) {
        Gridr obj = new Gridr(thisform, name, 1, 1, 300, 200);
        S.SETACTIVEGRID(obj);
        obj.THISFORM = thisform;
        if (FF.EMPTY(obj.ALIAS)) {
            obj.ALIAS = name;
        }//присвоим имени алиаса имя грида если пустой
        thisform.TABLEPROP(obj);//свойства грида
        if (!A.USED(obj.ALIAS))//если алиас с данными не существует
        {
            if (thisform.QUERY(obj) == false) {//выполняем запрос на выборку данных
                thisform.EXIT = 1;
                return null;
            }//получаем данные для таблицы
        } else {
            obj.DATA = (Cursorr) V.MAPALIAS.get(obj.ALIAS.toUpperCase()); //получение курсора данных из списка по названию алиаса
            thisform.INITCOLUMN(obj);//формируем шапку и колонки
        }
        thisform.GRIDS.add(obj);//в список таблиц формы добавлеем новую
        obj.GORECNO(A.RECNO(obj.ALIAS));
        obj.STRNO = obj.getSelectedRow();
        if (obj.RECNO > 0) {
            obj.THISFORM.CHANGERECNO(obj, obj.STRNO, obj.RECNO); //метод фрпмы при изменении номера записи     
        }
//        if (obj.RESTORECOL == 1) {
//            obj.RESTORECOL();
//        }

//          thisform.add(obj);
        return obj;
    }

    /**
     * Метод расположения объекта на форме относительно углов формы или другого
     * объекта
     *
     * @param obj объект относительно углов формы
     * @param corner_x направление Vars.LOC_LEFT Vars.LOC_CENTR Vars.LOC_RIGHT
     * относительно углов формы
     * @param corner_y направление Vars.LOC_UP Vars.LOC_CENTR Vars.LOC_DOWN
     */
    public static void LOC(Component obj, int corner_x, int corner_y) {//расположение объекта относительно другого
        Rectangle rect = obj.getBounds();
        Rectangle rectot = obj.getParent().getBounds();
        int H, W; //чистые высота и ширина без заголовка и границ
        int x = 0;
        int y = 0, xx = V.LOC_SPACE, yy = V.LOC_SPACE;
        H = rectot.height - V.LOC_HEADRE - V.LOC_BORDER;
        W = rectot.width - 2 * V.LOC_BORDER;
        //    по x
        if (corner_x >= 0) { //если
            x = corner_x + V.LOC_SPACE;
        }
        if (corner_y >= 0) { //если
            y = corner_y + V.LOC_SPACE;
        }

        if (corner_x < 0) { //если от формы
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

        //    по y
        if (corner_y < 0) {//если от формы
            if (corner_y == V.LOC_CENTR) {
                y = H - rect.height;
                y = y / 2 + yy;
            }

            if (corner_y == V.LOC_UP) {
                y = yy;
            }
            if (corner_y == V.LOC_DOWN) {
                y = H - rect.height - yy;
            }

        }

        if ("Buttonr".equals(obj.getClass().getSimpleName())) {
            Buttonr obj1 = (Buttonr) obj;
            obj1.X1 = x;
            obj1.Y1 = y;
            obj1.X2 = x + obj1.getWidth();
            obj1.Y2 = y + obj1.getHeight();
        }
        if ("Labelr".equals(obj.getClass().getSimpleName())) {
            Labelr obj1 = (Labelr) obj;
            obj1.X1 = x;
            obj1.Y1 = y;
            obj1.X2 = x + obj1.getWidth();
            obj1.Y2 = y + obj1.getHeight();
        }
        if ("Textr".equals(obj.getClass().getSimpleName())) {
            Textr obj1 = (Textr) obj;
            obj1.X1 = x;
            obj1.Y1 = y;
            obj1.X2 = x + obj1.getWidth();
            obj1.Y2 = y + obj1.getHeight();
        }

        obj.setLocation(x, y);
    }

    /**
     * Закрытие если существует формы
     *
     * @param nameform - имя формы
     * @return
     */
    public static boolean CLOSEFORM(String nameform) {
        Formr rform = null; //иницилизация переменной
        if (V.MAPFORMS == null) {
            V.MAPFORMS = new HashMap<>();
        } //если нет массива вызванных форм создаем
        Object ff = V.MAPFORMS.get(nameform); //ищем в массиве вызванных форм текущее имя формы
        if (ff != null) {//если форма уже вызвана и есть в массиве
            rform = (Formr) ff; // ссылка на существующую форму
            rform.DESTROY();
            rform = null;
            return true;
        }

        return false;
    }

    /**
     * Закрытие всех открытых форм
     *
     * @return - число закрытых форм
     */
    public static int CLOSEFORM_ALL() {
        int kol = 0;
        if (V.MAPFORMS == null) {
            V.MAPFORMS = new HashMap<>();
        } //если нет массива вызванных форм создаем
        Set keys = V.MAPFORMS.keySet();
        for (Object key : keys) {
            if (CLOSEFORM((String) key)) { //если закрыли то наращиваем счетчик
                kol++;
            };
        }
        return kol;
    }

    /**
     * Возвращение ссылки на форму по ее имени
     *
     * @param nameform - имя формы
     * @return ссылка Formr
     */
    public static Formr GETFORM(String nameform) {
        Formr rform = null; //иницилизация переменной
        if (V.MAPFORMS == null) {
            V.MAPFORMS = new HashMap<>();
        } //если нет массива вызванных форм создаем
        Object ff = V.MAPFORMS.get(nameform); //ищем в массиве вызванных форм текущее имя формы
        if (ff != null) {//если форма уже вызвана и есть в массиве
            rform = (Formr) ff;
        } // ссылка на существующую форму
        return rform;
    }

    /**
     *
     * Метод вызова формы, если уже вызвана то становиться активной
     *
     * @param nameform строка имени класса формы
     */
    public static Formr DOFORM(String nameform) {
        return DOFORM(nameform, null);
    }

    public static Formr DOFORM(String nameform, String[] param) {
        return DOFORM(nameform, param, 0, -1);
    }

    public static Formr DOFORM(String nameform, int vid) {
        return DOFORM(nameform, null, vid, -1);
    }

    public static Formr DOFORM(String nameform, int vid, int modal) {
        return DOFORM(nameform, null, vid, modal);
    }

    /**
     *
     * @param nameform - имя формы
     * @param param - масив строк параметров
     * @param vid - вид вызова 0 - обычный вызов 1- справочник 3- создаем новый
     * объект если он даже существует
     * @param modal -1- модальная 0 - не модальная
     * @return
     */
    public static Formr DOFORM(String nameform, String[] param, int vid, int modal) {
        String pathform;     //полный путь к классам
        pathform = "forms." + nameform; //определение полного пути к классу формы
        int AT = FF.AT(".", nameform);
        if (AT > 0) {
            nameform = FF.SUBSTR(nameform, AT + 1);
        }

        //локальная вешь
//        if ("Auth".equals(nameform)) { // убрали 21.06.2018
//            nameform = "Auth_BY";
//            pathform = "kassa_by." + nameform;
//        }
        if (!VALID_LIB()) { //ПРОВЕРКА НА ИЗМЕНЕНИЕ ОСНОВНАЫХ БИБЛИОТЕК
            return null;
        }
        //
        if (vid == 1) { // если вызов как справочник то закрываем если была открыта
            P.CLOSEFORM(nameform);
        }
        Formr rform = null; //иницилизация переменной
        if (V.MAPFORMS == null) {
            V.MAPFORMS = new HashMap<>();
        } //если нет массива вызванных форм создаем
        Object ff = V.MAPFORMS.get(nameform); //ищем в массиве вызванных форм текущее имя формы
        if (ff != null && vid != 3) {//если форма уже вызвана и есть в массиве
//            System.out.print("-"+nameform+"\n")       ;
            rform = (Formr) ff; // ссылка на существующую форму
            rform.setVisible(true);
            rform.show();
            rform.SETFOCUS();
        }
        if (ff == null || vid == 3) {// если нет в массиве то создаем новый объект
            Class c = null;  // иницилизация с                     //
            try {
                c = Class.forName(pathform); //загрузка класса по имени
            } catch (ClassNotFoundException ex) {//без этого не работает Class.forName(pathform)
                //System.out.println(ex.toString());
                pathform = "kassa_by." + nameform; //определение полного пути к классу формы для кассы
                try {
                    c = Class.forName(pathform); //загрузка класса по имени
                } catch (ClassNotFoundException ex2) {//без этого не работает Class.forName(pathform)
                    if (V.PATHFORM==null) {
                    P.MESSERR("1.Ошибка загрузки формы: " + nameform+"\n"+ex.toString());
                    return null;
                    }
                    boolean ft=false;  
                    for (int i=0;i<V.PATHFORM.length;i++ ) {
                     pathform = V.PATHFORM[i]+"." + nameform; //определение полного пути к классу формы для кассы
                      try {
                       c = Class.forName(pathform); //загрузка класса по имени
                       ft=true; // найден класс формы
                       break; // выходим  из цикла
                      } catch (ClassNotFoundException ex3) {//без этого не работает Class.forName(pathform)
                          System.out.println(ex3.toString());
                      }
                    }// цикл
                    if (!ft) { // если не был найден класс формы
                    P.MESSERR("2.Ошибка загрузки формы: " + nameform);
                    return null;
                    }
                }
            }
            
            try {
                V.PARAMOT = null;
                if (param != null) // если параметры поступили
                {
                    V.PARAMTO = null; //обнуляем глобальный массив параметров
                    V.PARAMTO = param;
//                    V.PARAMTO = new String[param.size()];  //иницилизируем масиив глобальных параметров до  расмера списка параметров
//                    param.toArray(V.PARAMTO);// копируем парметры в массив глобальных параметров
                }
                Formr PREVFORM = V.LASTFORM;
                rform = (Formr) (Object) c.newInstance();//создаем новый объект формы
                rform.PREVFORM = PREVFORM;
                //
                if (vid == 1) { // если вызов как справочник то закрываем если была открыта
                    rform.SETSPR(true); //установка свойст в форме вызова как справочника
                }
                if (rform.LOAD() == false) {
                    rform.DESTROY(); //при открытии окна формы когда все элементы созданы , если возвращает false форма закрывается
                    rform = null;
                } else {
                    rform.DESCPROP();//Вызов описание свойст формы из дочерней
                    rform.LOAD_OBJ();//в дочерних формах добавляет объекты
                    if (rform == null || rform.EXIT == 1) {
                        return null; //проверка после OPEN
                    }
                    if (rform.RESIZABLE == 0) {//если разрешено изменение размеров формы
                        rform.RESTORESIZE();//Восстановоение размера  с предыдущего закрытия
                    }
                    rform.RESTORELOC();//Восстановоение расположения с предыдущего закрытия
                    rform.LOC_ABOUT(); //Обязательно для начального расположения после создания объектов
                    if (rform.INIT() == false) {
                        rform.DESTROY(); //при открытии окна формы когда все элементы созданы , если возвращает false форма закрывается
                        rform = null;
                    }
                }
                //
                if (rform == null || rform.EXIT == 1) {
                    rform = null;
                    return null; //проверка после LOAD и INIT
                }
                V._SCREEN.add(rform, V.LAYER_FORM, -1);
                V.LASTFORM = rform;

                rform.setVisible(true);
                if (rform == null || rform.EXIT == 1) {
                    return null; //проверка после OPEN
                }
                rform.OPEN(); //МЕТОД КОГДА ФОРМА ЗАГРУЖЕНА
                if (modal == 1) {
                    rform.MODAL = 1;
                }
                if (modal == 0) {
                    rform.MODAL = 0;
                } //если в параметрах определяется модальнгость
                V.MAPFORMS.put(nameform, rform); // записываем в массив вызванных форм класс формы rform по ключу имени nameform
                if (rform.MODAL == 1) // 1- модальная 0 - не модальная
                {
                    P.STARTMODAL(rform);
                } else {
//                    V.MAPFORMS.put(nameform, rform); // записываем в массив вызванных форм класс формы rform по ключу имени nameform
                }
            } catch (InstantiationException | IllegalAccessException ex) {//без этого не работает newInstance()
                P.MESSERR("Ошибка создания формы: " + nameform+"\n"+ex.toString());
                rform = null;              //при ошибке ссылка в null
            }

        }
//      V.ACTIVEFORM=rform;  //данная форма - активная форма
        return rform;
    }

    public static int MESSF(String mess) {
        return MESSF(mess, V.LOC_CENTR, 0, "Внимание !!!", "", "       ОК      ", "");
    }

    public static int MESSF(String mess, int loc) {
        return MESSF(mess, loc, 0, "Внимание !!!", "", "       ОК      ", "");
    }

    public static int MESSF(String mess, int loc, int delay) {
        return MESSF(mess, loc, delay, "Внимание !!!", "", "       ОК      ", "");
    }

    /**
     * Вывод диалогового окна-сообщения
     *
     * @param mess - текст
     * @param loc - расположение окна V.LOC_CENTR - поцентру основного окна
     * V.LOC_COR_RU - правый верхний угол
     * @param delay -сек. ограничение времени жизни , 0 - неограничено , меньше
     * нуля не модальная
     * @param h - заголовок формы
     * @param b0 - текст для кнопки 0 ,если пусто не участвует
     * @param b1 - текст для кнопки 1 ,если пусто не участвует
     * @param b2 - текст для кнопки 2 ,если пусто не участвует
     * @return
     */
    public static int MESSF(String mess, int loc, int delay, String h, String b0, String b1, String b2) {
//        V.PARAMTO = new String[1];
//        V.PARAMTO[0] = mess;
//        P.DOFORM("MESSAGE");
        int shift = 0;
        Formr FP = P.GETFORM("MessForm");
        if (FP != null) {  // если уже создан экземпляр
            shift = FP.INT1 + 20;  // определяем и увеличиваем сдвиг на экране (чтобы были все видны)
        }
        Formr F = P.DOFORM("MessForm", 3); // не перекрывая другой экземпляр
        F.INT1 = shift;
        //Math.abs(n)
        if (delay != 0) { //задан интервал активности формы
            F.INT2 = Math.abs(delay);
//            F.timer1.start();
        }
        F.setTitle(h);
        F.T[0].SETVALUE(mess);
        //кнопки
        if ("".equals(b0)) {
            F.B[0].setVisible(false);
        } else {
            F.B[0].setText(b0);
            F.B[0].RESIZE();
            F.B[0].setVisible(true);
        }
        if ("".equals(b1)) {
            F.B[1].setVisible(false);
        } else {
            F.B[1].setText(b1);
            F.B[1].RESIZE();
            F.B[1].setVisible(true);
        }
        if ("".equals(b2)) {
            F.B[2].setVisible(false);
        } else {
            F.B[2].setText(b2);
            F.B[2].RESIZE();
            F.B[2].setVisible(true);
        }
        //расположение
        int x = 15 + shift, y = 15 + shift; // ЛЕВЫЙ ВЕРХНИЙ УГОЛ
        if (loc == V.LOC_CENTR) { //ПО ЦЕНТРУ ПО УМОЛЧАНИЮ
            x = V._SCREEN.getWidth() / 2 - F.getWidth() / 2 + shift;
            y = V._SCREEN.getHeight() / 2 - F.getHeight() / 2 + shift;
        }
        if (loc == V.LOC_COR_RU) { //ПРАВЫЙ ВЕРХНИЙ УГОЛ
            x = V._SCREEN.getWidth() - F.getWidth() - x - shift;
        }
        if (loc == V.LOC_COR_RD) { //ПРАВЫЙ НИЖНИЙ УГОЛ
            x = V._SCREEN.getWidth() - F.getWidth() - x - shift;
            y = V._SCREEN.getHeight() - F.getHeight() - y - 40 - shift;
        }
        if (loc == V.LOC_COR_LD) { //ЛЕВЫЙ НИЖНИЙ НИЖНИЙ УГОЛ
            y = V._SCREEN.getHeight() - F.getHeight() - y - 40 - shift;
        }

        F.setBounds(x, y, F.getWidth(), F.getHeight());

        //F.SETMODAL(1);
        if (delay >= 0) {
            P.STARTMODAL(F);
        }
        return 0;
    }

    /**
     * Выдача сообщение
     *
     * @param mess -- строка сообщения
     */
    public static void MESS(String mess) {

        if (!V.PRG_CONSOLE) //если приложение не консольное
        {
            P.ALERT("");
//        V.PARAMTO = new String[1];
//        V.PARAMTO[0] = mess;
//        P.DOFORM("MESSAGE");
            JOptionPane.showMessageDialog(V._SCREEN, mess, "Сообщение", JOptionPane.INFORMATION_MESSAGE);
        } else //если консольное
        {
            MESS_CONSOLE(mess);
        }
    }

    /**
     * Выдача сообщение
     *
     * @param mess -- строка сообщения
     * @param caption -- заголовок
     */
    public static void MESS(String mess, String caption) {

        if (!V.PRG_CONSOLE) //если приложение не консольное
        {
            P.ALERT("");
//        V.PARAMTO = new String[2];
//        V.PARAMTO[0] = mess;
//        V.PARAMTO[1] = caption;
//        P.DOFORM("MESSAGE");
            JOptionPane.showMessageDialog(V._SCREEN, mess, caption, JOptionPane.INFORMATION_MESSAGE);
        } else //если консольное
        {
            MESS_CONSOLE(mess);
        }

    }

    /**
     * Сообщение об ошибке
     *
     * @param mess текст сообщения
     * @return
     */
    public static int MESSDIALOG(String mess) {

        int vrem = 0;

        if (!V.PRG_CONSOLE) //если приложение не консольное
        {
            P.ALERT("");
            Object[] options = {"Выход", "Повторить"};
            vrem = JOptionPane.showOptionDialog(V.ACTIVEFORM,
                    mess,
                    "Ошибка",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]);
        } else //если консольное
        {
            MESS_CONSOLE(mess);
            vrem = 1; //по умолчания возвращаем 0 для выхода
        }
        return vrem;
    }

    public static void MESSERR(String mess) {
        if (!V.PRG_CONSOLE) //если не консоль
        {
            P.ALERT("");
//          JOptionPane.showConfirmDialog(null, mess, "ВНИМАНИЕ !!!!", JOptionPane.OK_OPTION);
            JOptionPane.showMessageDialog(V._SCREEN, mess, "Внимание !!!!", JOptionPane.ERROR_MESSAGE);
        } else //консоль
        {
            MESS_CONSOLE(mess);
        }
    }

    public final static int YES = 0;
    public final static int NO = 1;

    public static int MESSYESNO(String mess, int NPP) {
        P.ALERT("");
        Object[] options = {"ДА", "НЕТ"};
        int n = JOptionPane.showOptionDialog(V._SCREEN, mess, "Внимание !", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[NPP]);
        return n;
    }

    public static int MESSSEL(String mess, String[] select, int NPP) {
        //P.ALERT("");
        int n = JOptionPane.showOptionDialog(V._SCREEN, mess, "Внимание !", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, select, select[NPP]);
        return n;
    }

    public static int MESSYESNO(String mess) {
        return MESSYESNO(mess, 0);
    }

    /**
     * вывод сообщения об ошибке в консоль или запись в базу
     *
     * @param mess сообщение
     */
    public static void MESS_CONSOLE(String mess) {

        System.err.println(mess);
        if (!"ALERT".equals(Thread.currentThread().getStackTrace()[2].getMethodName())) {
            P.writeLog("WARN", "BaseFrame.prg.P", Thread.currentThread().getStackTrace()[3].getMethodName(), mess, "");
        }
    }

    /**
     * Сохранение списка символьных данных в текстовом файле
     *
     * @param file
     * @param vars
     */
    public static void SAVETOMEM(String file, String[] vars) {
        int i;

        // Потоки
        FileOutputStream outFile = null;
        DataOutputStream outData = null;
        try {
            outFile = new FileOutputStream(file);  // Открываем исходящий файловый поток из файла
        } catch (FileNotFoundException ex) {
            P.MESSERR("Ошибка открытия файла:" + file);
            return;
        }
        outData = new DataOutputStream(outFile);        // Открываем исходящий поток данных из файлового потока
        try {
            for (String var : vars) //проходим по списку переменных
            {
                byte[] buffer = var.getBytes("Cp1251"); //текущую символьную переменную заносим в побайтный массив
                for (i = 0; i < buffer.length; i++) {
                    if (buffer[i] ==(byte)'\n') {buffer[i]=6;} //заменяем конец строки 13 на 6
                    outData.writeByte(buffer[i]); // Пишем очередной байт в поток
                }
                outData.writeByte(13);        // конец строки

            }
        } catch (IOException ex) {
            P.MESSERR("Ошибка записи в файл: " + file + "\n" + ex.toString());
        }

        try {
            // Закрываем исходящие потоки
            if (outData != null) {
                outData.close();
            }
            if (outFile != null) {
                outFile.close();
            }
        } catch (IOException e) {
            P.MESSERR("Ошибка закрытияы файла: " + file + "\n" + e.toString());
        }

    }

    /**
     * Считывает из файла символьные значения (расположенные построчно) в
     * строковый массив
     *
     * @param file - имя файла
     * @param kol - если массив пустой возврат kol пустых элементов file)
     * @return
     */
    public static String[] RESTOREMEM(String file, int kol) {

        ArrayList<String> LABEL_MEM = null;
        if (FF.FILE(file)) {
            LABEL_MEM = P.RESTOREMEM(file);
        }
        if (LABEL_MEM == null || LABEL_MEM.size() == 0 || LABEL_MEM.size() < kol) {
            if (kol != 0) {
                String[] out = new String[kol];
                Arrays.fill(out, "");
                return out;
            }
            return null;
        }
        String[] out = new String[LABEL_MEM.size()];
        LABEL_MEM.toArray(out);
        return out;
    }

    /**
     * Считывает из файла символьные значения (расположенные построчно)
     *
     * @param file
     * @return возвращает список из строковых данных
     */
    public static ArrayList<String> RESTOREMEM(String file) {
        String str = "", pole = "";
        ArrayList<String> vars = new ArrayList<>();  //создание списка
        str = FF.FILETOSTR(file);
        int AT9 = FF.AT("\n", str);
        while (AT9 > 0) {
            pole = FF.SUBSTR(str, 1, AT9 - 1);
            //byte ent='\n';
            pole=pole.replace((char)6, (char)'\n');
            vars.add(pole);  //добавлям в список строку

            str = FF.SUBSTR(str, AT9 + 1);
            AT9 = FF.AT("\n", str);
        }
        return vars;

        /*        int i = 0;
         int bytesAvailable = 0;
         //String strfile=FF.FILETOSTR(file);

         // Потоки
         FileInputStream inFile = null;
         DataInputStream inData = null;
         try {
         inFile = new FileInputStream(file);  // Открываем исходящий файловый поток из файла
         //    BufferedReader   inFileB = new BufferedReader(new InputStreamReader(inFile, "Cp1251"));
         } catch (FileNotFoundException ex) {
         P.MESSERR("Ошибка поиска файла:" + file + "\n" + ex.toString());
         return vars;
         }

         inData = new DataInputStream(inFile);        // Открываем исходящий поток данных из файлового потока
         try {
         bytesAvailable = inFile.available();  //количество байт в файле

         byte by;
         for (; i < bytesAvailable; i++) {
         by = inData.readByte(); // Читаем очередной байт из потока
         if (by == 13 || i == bytesAvailable - 1) //если байт конца строки или последний
         {
         vars.add(str);  //добавлям в список строку
         str = "";       // иницилизируем строку
         } else {
         str = str + (char) by;
         } //добавляем байт к строке переведя в символ
         }
         } catch (IOException ex) {
         P.MESSERR("Ошибка открытия файла:" + file + "\n" + ex.toString());
         }

         try {
         // Закрываем исходящие потоки
         if (inData != null) {
         inData.close();
         }
         if (inFile != null) {
         inFile.close();
         }
         } catch (IOException e) {
         P.MESSERR("Ошибка закрытияы файла: " + file + "\n" + e.toString());
         }
         return vars;  // возвращаем список
         */
    }

    public synchronized static void SQL_HISTORY_SAVE(String time_1, String SELE, String status) {
        if ("2".equals(V.CONN_DRIVER)) { //если sql server не нужно писать
            return;
        }

        long time_2 = 0;
        try {
            time_2 = (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(FF.DATETIMES()).getTime() - new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(time_1).getTime()) / 1000;
            if (time_2 == 0) {
                time_2++;
            }
        } catch (ParseException excep) {
        }
        if (!SELE.contains("HISTORY")) {
            if (SELE.length() > 4000) {
                SELE = SELE.substring(0, 100);
            }
            try {
                int up=0;
           if ("1".equals(V.CONN_DRIVER)) { //если ORACLE нужно писать 
                 up = SQLUPDATE("insert into HISTORY (USER_S,PC_S,DATE_S1,TIME_S,SQL_T,USER_L,BIT_STATUS,PROGRAMM) values ('"
                        + GET_HOST_NAME().replaceAll("'", "''") + "','" + GET_HOST_PC().replaceAll("'", "''") + "',TO_TIMESTAMP('" + time_1 + "','DD.MM.RRRR HH24:MI:SS')," + time_2 + ",'" + SELE.replace("'", "''") + "','" + V.CONN_USER + "','" + status + "',RTRIM(sys_context('USERENV', 'IP_address')) " + ")");
           }
           if ("3".equals(V.CONN_DRIVER)) { //если POSRGRE нужно писать 
                 up = SQLUPDATE("insert into HISTORY (USER_S,PC_S,DATE_S1,TIME_S,SQL_T,USER_L,BIT_STATUS,PROGRAMM) values ('"
                        + GET_HOST_NAME().replaceAll("'", "''") + "','" + GET_HOST_PC().replaceAll("'", "''") + "',TO_TIMESTAMP('" + time_1 + "','DD.MM.RRRR HH24:MI:SS')," + time_2 + ",'" + SELE.replace("'", "''") + "','" + V.CONN_USER + "','" + status + "',inet_client_addr()::varchar " + ")");
           }
           
                if (up <= 0 && V.ARM != 2 && V.ARM != 3 && V.ARM != 1) {
                    P.ALERT("Удаление записей из HISTORY");
                    SQLUPDATE("TRUNCATE TABLE HISTORY");
                    P.ALERT("");
                }
            } catch (Exception exc22) {
            }
        }
    }

    public static void SQL_HISTORY_SAVE(String time_1, String SELE, String status, Connection conn) {
        if ("2".equals(V.CONN_DRIVER)) { //если sql server нужно писать
            return;
        }

        long time_2 = 0;
        try {
            time_2 = (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(FF.DATETIMES()).getTime() - new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(time_1).getTime()) / 1000;
            if (time_2 == 0) {
                time_2++;
            }
        } catch (ParseException excep) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, excep);
            System.err.println("ошибка парсинга в истории SQL_HISTORY_SAVE");
        }
        if (!SELE.contains("HISTORY")) {
            if (SELE.length() > 4000) {
                SELE = SELE.substring(0, 100);
            }
            try {
                int up=0;
           if ("1".equals(V.CONN_DRIVER)) { //если ORACLE нужно писать 
                 up = SQLUPDATE(conn, "insert into HISTORY (USER_S,PC_S,DATE_S1,TIME_S,SQL_T,USER_L,BIT_STATUS,PROGRAMM) values ('"
                        + GET_HOST_NAME() + "','" + GET_HOST_PC() + "',TO_TIMESTAMP('" + time_1 + "','DD.MM.RRRR HH24:MI:SS')," + time_2 + ",'" + SELE.replace("'", "''") + "','" + V.CONN_USER + "','" + status + "',RTRIM(sys_context('USERENV', 'IP_address')) " + ")");
           }
           if ("3".equals(V.CONN_DRIVER)) { //если POSTGRE нужно писать 
                 up = SQLUPDATE(conn, "insert into HISTORY (USER_S,PC_S,DATE_S1,TIME_S,SQL_T,USER_L,BIT_STATUS,PROGRAMM) values ('"
                        + GET_HOST_NAME() + "','" + GET_HOST_PC() + "',TO_TIMESTAMP('" + time_1 + "','DD.MM.RRRR HH24:MI:SS')," + time_2 + ",'" + SELE.replace("'", "''") + "','" + V.CONN_USER + "','" + status + "',inet_client_addr()::varchar " + ")");
           }
           
                if (up <= 0 && V.ARM != 2 && V.ARM != 3 && V.ARM != 1) {
                    P.ALERT("Удаление записей из HISTORY");
                    SQLUPDATE(conn, "TRUNCATE TABLE HISTORY");
                    P.ALERT("");
                }

            } catch (Exception exc22) {
                Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, exc22);
                System.err.println("ошибка записи в историю SQL_HISTORY_SAVE");
            }
        }
    }

    public static boolean SQLEXECUT(String SELE) {
        Connection CONN = V.CONN1;
        return SQLEXECUT(CONN, SELE);
    }

    public static boolean SQLEXECUT(String SELE, int history) {
        Connection CONN = V.CONN1;
        return SQLEXECUT(CONN, SELE, history);
    }

    public static boolean SQLEXECUT(Connection CONN, String SELE) {
        return SQLEXECUT(CONN, SELE, 1);
    }

    /**
     * Выполнение команд (не запросы)
     *
     * @param CONN - соединение
     * @param SELE - команды
     * @param history - признак записи истории 1- писать 0 - нет
     * @return
     */
    public static synchronized boolean SQLEXECUT(Connection CONN, String SELE, int history) {
        boolean RES = false;
        String time_1 = FF.DATETIMES();
        try {
//            if (CONN.isValid(2) == false) {
            if (CONN.isClosed() == true) {
                if (V.CONN_AUTO == false) {
                    P.MESS(" Соединение разорвано... \nНажмите OK для восстановления");
                } else {
                    V.CONN_OUT = false;
                    V.CONN_REP = 0;
                }
                V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                CONN = V.CONN1;
                V.SQL = null;
            }
            SQL_CREATE_STATEMENT(CONN);
        } catch (SQLException | NullPointerException ex) {
            int answer = MESSDIALOG("Ошибка создания потока:" + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString()));
            if (answer == 0) {
                V.FALSETRUE = false;
                P.SYSTEM_EXIT();
                return false;
            } else {
                V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                // SQL_HISTORY_SAVE(time_1, SELE, "F"); не паонятно зачем так было RKV
                return SQLEXECUT(V.CONN1, SELE);
            }
        }
        try {
            RES = V.SQL.execute(SELE);
        } catch (SQLException ex) {
            FF._CLIPTEXT(SELE);
            if (ex.toString().contains("Ошибка ввода/вывода") || ex.toString().contains("Закрытое соединение")) {
                int answer = MESSDIALOG("Ошибка выполнение запроса:" + "\n" + (SELE.length() > 70 ? SELE.substring(0, 67) + "..." : SELE.substring(0, SELE.length())) + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString()));
                if (answer == 0) {
                    V.FALSETRUE = false;
                    P.SYSTEM_EXIT();
                    return false;
                } else {
                    V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                    if (history > 0) {
                        SQL_HISTORY_SAVE(time_1, SELE, "F");
                    }
                    return SQLEXECUT(V.CONN1, SELE);
                }
            } else {
                if (history > 0) {
                    SQL_HISTORY_SAVE(time_1, SELE, "F");
                }
                P.ALERT("");
                P.MESSERR("Ошибка выполнение запроса:" + "\n" + (SELE.length() > 70 ? SELE.substring(0, 67) + "..." : SELE.substring(0, SELE.length())) + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString()));
                return false;
            }
        }
        if (history > 0) {
            SQL_HISTORY_SAVE(time_1, SELE, "T");
        }

        return true;
    }

    //**************************************************************************
    //  Использовать для хранимых процедур, возвращающих имя таблицы
    //  далее данные из таблицы ( TEMPORARY TABLE .. ON COMMIT PRESERVE ROWS) сохраняются в Алиас
    //  возвращает полученный курсор
    //**************************************************************************
    public static Cursorr SQLEXECPROC(String procName, Object[][] param, String ALIAS) { // param - первым указываем тип параметра ии OracleTypes, вторым сам параметы, либо передать null, если параметров нет
        CallableStatement cst;

        String questions = "";
        StringBuilder sb;
        try {
            sb = new StringBuilder(questions);

            for (int paramNo = 0; paramNo < param.length; paramNo++) { // формирование строки параметров
                sb.append("?,");
            }

            sb.append("?"); // последний параметр - возвращаемый результат
            questions = sb.toString();
            cst = V.CONN.prepareCall("BEGIN " + procName + "(" + questions + "); END;");

            for (int i = 0; i < param.length; i++) {
                switch ((Integer) param[i][0]) {
                    case OracleTypes.INTEGER:
                        cst.setInt(i + 1, (Integer) param[i][1]);
                        break;
                    case OracleTypes.VARCHAR:
                        cst.setString(i + 1, param[i][1].toString());
                        break;
                    case OracleTypes.NUMBER:
                        cst.setDouble(i + 1, Double.parseDouble(param[i][1].toString()));
                        break;
                    case OracleTypes.TIMESTAMP:
                        cst.setTimestamp(i + 1, (java.sql.Timestamp) param[i][1]);
                        break;
                    case OracleTypes.DATE:
                        cst.setDate(i + 1, (java.sql.Date) param[i][1]);
                        break;
                }
            }
            cst.registerOutParameter(param.length + 1, OracleTypes.VARCHAR);
            cst.execute();

            Cursorr cur = P.SQLEXECT("SELECT * FROM " + cst.getString(param.length + 1), ALIAS);

            cst.close();

            return cur;

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    //**************************************************************************
    //  Использовать для хранимых процедур, возвращающих имя таблицы
    //  далее данные из таблицы ( TEMPORARY TABLE .. ON COMMIT PRESERVE ROWS) сохраняются в Алиас
    //  возвращает полученный курсор
    //**************************************************************************
    public static void SQLEXECPROC(String procName, Object[][] param) { // param - первым указываем тип параметра ии OracleTypes, вторым сам параметы, либо передать null, если параметров нет
        CallableStatement cst;

        String questions = "";
        StringBuilder sb;
        try {
            sb = new StringBuilder(questions);

            for (int paramNo = 0; paramNo < param.length; paramNo++) { // формирование строки параметров
                if (paramNo == param.length - 1) {
                    sb.append("?");
                } else {
                    sb.append("?,");
                }
            }

            //sb.append("?"); // последний параметр - возвращаемый результат
            questions = sb.toString();
            cst = V.CONN.prepareCall("BEGIN " + procName + "(" + questions + "); END;");

            for (int i = 0; i < param.length; i++) {
                switch ((Integer) param[i][0]) {
                    case OracleTypes.INTEGER:
                        cst.setInt(i + 1, (Integer) param[i][1]);
                        break;
                    case OracleTypes.VARCHAR:
                        cst.setString(i + 1, param[i][1].toString());
                        break;
                    case OracleTypes.NUMBER:
                        cst.setDouble(i + 1, Double.parseDouble(param[i][1].toString()));
                        break;
                    case OracleTypes.TIMESTAMP:
                        cst.setTimestamp(i + 1, (java.sql.Timestamp) param[i][1]);
                        break;
                    case OracleTypes.DATE:
                        cst.setDate(i + 1, (java.sql.Date) param[i][1]);
                        break;
                    case OracleTypes.ARRAY:
                        cst.setArray(i + 1, new ARRAY(ArrayDescriptor.createDescriptor(param[i][2].toString(), V.CONN1), V.CONN1, param[i][1]));//param[i][1]);
                        break;
                }
            }
            //cst.registerOutParameter(param.length + 1, OracleTypes.VARCHAR);
            cst.execute();

            cst.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static Cursorr SET_CURSOR_FROM_RS(ResultSet rs, String ALIAS) {
        Cursorr DATA;
        P.ALERT("Форматирование данных... ");
        DATA = new Cursorr(rs);
        ALIAS = ALIAS.toUpperCase();
        DATA.ALIAS = ALIAS;
        if (V.MAPALIAS == null) {
            V.MAPALIAS = new HashMap<>();
        }  //если первый раз - создаем
        V.MAPALIAS.put(ALIAS.toUpperCase(), DATA); // записываем в массив данных по ключу имени алиаса
        S.SETDATARECNO(DATA, 1);
//              S.SETACTIVEALIAS(ALIAS);
        P.ALERT("");
        return DATA;
    }

    /**
     * Выполнение запросов на insert update delete
     *
     * @param SELE
     * @return число обработанных записей
     */
    public static int SQLUPDATE(String SELE) {
        Connection CONN = V.CONN1;
        return SQLUPDATE(CONN, SELE);
    }

    public static synchronized int SQLUPDATE(Connection CONN, String SELE) {
        int RES = 0;
        String time_1 = FF.DATETIMES();
        try {
//            if (CONN.isValid(2) == false) {
            if (CONN.isClosed() == true) {
                if (V.CONN_AUTO == false) {
                    P.MESS(" Соединение разорвано... \nНажмите OK для восстановления");
                } else {
                    V.CONN_OUT = false;
                    V.CONN_REP = 0;
                }
                V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                CONN = V.CONN1;
                V.SQL = null;
            }
            SQL_CREATE_STATEMENT(CONN);
        } catch (SQLException | NullPointerException ex) {
            int answer = MESSDIALOG("Ошибка создания потока:" + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString()));
            if (answer == 0) {
                V.FALSETRUE = false;
                P.SYSTEM_EXIT();
                return -1;
            } else {
                V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                if (!V.PRG_CONSOLE) //если не консоль
                {
                    SQL_HISTORY_SAVE(time_1, SELE, "F");
                } else {
                    SQL_HISTORY_SAVE(time_1, SELE, "F", CONN);
                }
                SQL_HISTORY_SAVE(time_1, SELE, "F");
                return SQLUPDATE(V.CONN1, SELE);
            }
        }
        try {
            RES = V.SQL.executeUpdate(SELE);
        } catch (SQLException ex) {
            if (ex.toString().contains("Ошибка ввода/вывода") || ex.toString().contains("Закрытое соединение")) {
                FF._CLIPTEXT(SELE);
                int answer = MESSDIALOG("Ошибка выполнение запроса:" + "\n" + (SELE.length() > 70 ? SELE.substring(0, 67) + "..." : SELE.substring(0, SELE.length())) + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString()));
                if (answer == 0) {
                    V.FALSETRUE = false;
                    P.SYSTEM_EXIT();
                    return -1;
                } else {
                    V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                    SQL_HISTORY_SAVE(time_1, SELE, "F");
                    return SQLUPDATE(V.CONN1, SELE);
                }
            } else {
                SQL_HISTORY_SAVE(time_1, SELE, "F");
                P.ALERT("");
                FF._CLIPTEXT(SELE);
                if (!ex.toString().contains("string literal too long")) {
                    P.MESSERR("Ошибка выполнение запроса:" + "\n" + (SELE.length() > 70 ? SELE.substring(0, 67) + "..." : SELE.substring(0, SELE.length())) + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString()));
                }
                return -1;
            }
        }
        SQL_HISTORY_SAVE(time_1, SELE, "T");
        return RES;
    }

    /**
     * Выполнение запросов/вызов процедур с массивами /для конкретной процедуры
     * :(
     *
     * @param charArray стринговый массив данных
     * @param idDel номер рассылки
     * @param filedName имя поля
     * @return удачно или неудачно выполнение
     */
    public static boolean SQLUPDATEARRAY(String[] charArray, int idDel, String filedName) {
        Connection CONN = V.CONN1;
        return SQLUPDATEARRAY(CONN, charArray, idDel, filedName);
    }

    public static boolean SQLUPDATEARRAY(Connection CONN, String[] charArray, int idDel, String filedName) {
        ArrayDescriptor arrayDescC = null;
        ARRAY arrayC = null;
        OracleCallableStatement cst = null;

        try {
            arrayDescC = ArrayDescriptor.createDescriptor("A_ARRAY_CHAR", CONN);
        } catch (SQLException ex) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Не создал ArrayDescriptor");
        }

        try {
            arrayC = new ARRAY(arrayDescC, CONN, charArray);
        } catch (SQLException ex) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Не создал Array");
        }

        try {
            cst = (OracleCallableStatement) CONN.prepareCall(
                    "{call imob.ADD_SHOP_FILTERS(?,?,?)}");
        } catch (SQLException ex) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Не создал OracleCallableStatement");
        }

        try {
            // Определение первого параметра процедуры//массив
            cst.setARRAY(1, arrayC);
            // Определение 2 параметра процедуры //номер рассылки
            cst.setNUMBER(2, new oracle.sql.NUMBER(idDel));
            // Определение 3 параметра процедуры //имя поля
            cst.setString(3, filedName);

        } catch (SQLException ex) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Не создал параметры");
        }

        boolean execute = false;
        try {
            execute = cst.execute(); //выполнение
            String time_1 = FF.DATETIMES();
            SQL_HISTORY_SAVE(time_1, "{call ADD_SHOP_FILTERS(?,?,?)}", "T");
        } catch (SQLException ex) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Не выполнил запрос");
            String time_1 = FF.DATETIMES();
            SQL_HISTORY_SAVE(time_1, "{call ADD_SHOP_FILTERS(?,?,?)}", "F");
        }

        return execute;
    }

    public static boolean SQL_CREATE_STATEMENT(Connection CONN) {    //    throws SQLException {
        try {
            if (V.SQL_KOL > 121) { // при многкратном использовании одного Statement на базе создаются курсоры
                // P.MESS("Закрытие");
                V.SQL.close();  // при закрытии курсоры должны закрываться
                V.SQL_KOL = 0;    ////ЕСЛИ НЕ ЗАКРЫВАТЬ ТО МОЖЕТ ВЫДАТЬ ora-01000 количество открытых курсоров превысило допустимый максимум
                System.out.println("Закрыт Statement на 121 " + FF.DATETIMES());
            }
            if (V.SQL == null || V.SQL.isClosed() || V.SQL.getConnection() != CONN) {
                //    if (V.SQL != null && !V.SQL.isClosed() ) {
                //     V.SQL.close();  //ЕСЛИ НЕ ЗАКРЫВАТЬ ТО МОЖЕТ ВЫДАТЬ ora-01000 количество открытых курсоров превысило допустимый максимум
                // }
//                System.out.println("Сесия "+V.SESSIONID);
                V.SQL = CONN.createStatement();
                System.out.println("Создан Statement " + FF.DATETIMES());
            }
            V.SQL_KOL++;
            return true;

        } catch (SQLException ex) {
            System.out.println(ex.toString());
            P.MESSERR("Ошибка создания канала выполнения \n" + ex.toString());
            return false;
        }
    }

    /**
     * Соединение с базой данных
     *
     * @param SERVER - имя сервера ip:port
     * @param BASA - имя базы данных
     * @param USER - логин пользователя
     * @param PASS - пароль
     * @return - объект соединение
     */
    public static Connection SQLCONNECT(String SERVER, String BASA, String USER, String PASS, String PORT) {   //
        return SQLCONNECT(V.CONN_DRIVER, SERVER, BASA, USER, PASS, PORT, true);
    }

    /**
     * Соединение с базой данных
     *
     * @param CONN_DRIVER - тип сервера 1 oracle 2 sql server
     * @param SERVER - имя сервера ip:port
     * @param BASA - имя базы данных
     * @param USER - логин пользователя
     * @param PASS - пароль
     * @return - объект соединение
     */
    public static Connection SQLCONNECT(String CONN_DRIVER, String SERVER, String BASA, String USER, String PASS, String PORT) {
        return SQLCONNECT(CONN_DRIVER, SERVER, BASA, USER, PASS, PORT, true);
    }

    /**
     * Соединение с базой данных
     *
     * @param CONN_DRIVER - тип сервера 1 oracle 2 sql server
     * @param SERVER - имя сервера ip:port
     * @param BASA - имя базы данных
     * @param USER - логин пользователя
     * @param PASS - пароль
     * @param LOC - локализация соединения false - без сообщения и передачи в
     * V.CONN (по умолчанию true)
     * @return - объект соединение
     */
    public static Connection SQLCONNECT(String CONN_DRIVER, String SERVER, String BASA, String USER, String PASS, String PORT, boolean LOC) {   //ОСНОВНОЙ
//        V.VARCONN();   // НЕ ПОНЯТНО ЗАЧЕМ ИЗ ФАЙЛА ДОСТАЮТСЯ ПАРАМЕТРЫ СОЕДИНЕНИЯ
        Connection CONN = null;
        try {
            if ("1".equals(CONN_DRIVER)) { //для орасле 1
                Class.forName("oracle.jdbc.driver.OracleDriver");
            }
            if ("2".equals(CONN_DRIVER)) {//для sql server 2
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            }
            if ("3".equals(CONN_DRIVER)) {//для postgreSQL
                Class.forName("org.postgresql.Driver");
            }

        } catch (ClassNotFoundException ex) {
            P.MESSERR("Ошибка загрузки драйвера соединения:" + "\n" + ex.toString());
            return null;
        }
        if ("1".equals(CONN_DRIVER)) {
            //     V.CONNECT_URL = "jdbc:oracle:thin:@" + SERVER + ":" + "1521" + ":" + BASA;
            V.CONNECT_URL = "jdbc:oracle:thin:@" + SERVER + ":" + PORT + ":" + BASA;
        }
        if ("2".equals(CONN_DRIVER)) {
            V.CONNECT_URL = "jdbc:sqlserver://" + SERVER + ";databaseName=" + BASA + ";userName=" + USER + ";password=" + PASS + ";";
        }
        //jdbc:postgresql://localhost:5432/MES 
        if ("3".equals(CONN_DRIVER)) {
            V.CONNECT_URL = "jdbc:postgresql://" + SERVER + ":" + PORT + "/" + BASA ;
        //jdbc:postgresql://localhost:5432/MES 
        }
        
        try {
            if ("1".equals(CONN_DRIVER)) {
                Locale.setDefault(Locale.US);//чтобы не было ошибки ORA-12705: Cannot access NLS data files or invalid environment specified.
//               Locale.US.
                CONN = DriverManager.getConnection(V.CONNECT_URL, USER, PASS);
            }
            if ("2".equals(CONN_DRIVER)) {
                java.sql.DriverManager.setLoginTimeout(5);
                CONN = java.sql.DriverManager.getConnection(V.CONNECT_URL);
            }
            if ("3".equals(CONN_DRIVER)) {
                DriverManager.setLoginTimeout(5);
             //   CONN = java.sql.DriverManager.getConnection(V.CONNECT_URL);
                CONN = DriverManager.getConnection(V.CONNECT_URL, USER, PASS);
            }
            
        } catch (SQLException ex) {
            if (V.CONN_AUTO == true && LOC) {  //если автоматические соединение при разрыве
                V.CONN_OUT = false;
                V.CONN_REP++;
                P.ALERT("Восстановление соединения попытка " + V.CONN_REP + "       Для отмены - SOS в буфер обмена");
                if ("SOS".equals(FF._CLIPTEXTOUT())) {
                    P.ALERT("Отмена соединения");
                    return null;
                }
                CONN = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                if (LOC) {
                    V.CONN = CONN;
                }
                return CONN;
            }
            V.ERROR_MES = "Ошибка соединения:" + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString());
            System.out.println(V.ERROR_MES);
            int answer = -1;
            if (LOC) {
                answer = MESSSEL(V.ERROR_MES, new String[]{"Выход из задачи", "Отмена операции", "Повторить"}, 2);
            }
            V.FALSETRUE = false;
            if (answer == 0) {
                P.SYSTEM_EXIT();
                return null;
            }
            if (answer == 1 || answer == -1) {
                return null;
            }
            if (answer == 2) {
                CONN = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
            }
        }
        if (LOC) {
            V.CONN = CONN;
        }
        return CONN;
    }

    /**
     * Выполнение запроса
     *
     * @param CONN - объект соединение
     * @param SELE - строка запроса
     * @param ALIAS имя алиаса
     * @param ft - соощение на получение данных
     * @param err - соощение об ошибке
     * @param history - писать в историю запросов 0- нет 1- да
     * @return - объект набор данных
     */
    public static synchronized Cursorr SQLEXECT(Connection CONN, String SELE, String ALIAS, boolean ft, boolean err, int history) { //ОСНОВНОЙ
        ResultSet CURSOR = null;
        Cursorr DATA;
        String time_1 = FF.DATETIMES();
        try {
            if (CONN.isValid(5) == false) {
          //  if (CONN.isClosed() == true) {
                if (V.CONN_AUTO == false) {
                    P.MESS(FF.DATETIMEMS()+" Соединение разорвано... \nНажмите OK для восстановления");
                } else {
                    V.CONN_OUT = false;
                    V.CONN_REP = 0;
                }
                V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                CONN = V.CONN1;
                V.SQL = null;
            }
            SQL_CREATE_STATEMENT(CONN);
            if (ft) {
                P.ALERT("Получение данных... ");
            }

        } catch (SQLException | NullPointerException ex) {
            int answer = MESSDIALOG("Ошибка создания потока:" + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString()));
            if (answer == 0) {
                P.ALERT("");
                V.FALSETRUE = false;
                P.SYSTEM_EXIT();
                return null;
            } else {
                V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                if (history > 0) {
                    SQL_HISTORY_SAVE(time_1, SELE, "F");
                }
                return SQLEXECT(V.CONN1, SELE, ALIAS, ft);
            }
        }
        try {
            CURSOR = V.SQL.executeQuery(SELE);
        } catch (SQLException ex) {
            if (ex.toString().contains("Ошибка ввода/вывода") || ex.toString().contains("Закрытое соединение")) {
                FF._CLIPTEXT(SELE);
                int answer = MESSDIALOG("Ошибка выполнение запроса:" + "\n" + (SELE.length() > 70 ? SELE.substring(0, 67) + "..." : SELE.substring(0, SELE.length())) + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString()));
                if (answer == 0) {
                    P.ALERT("");
                    V.FALSETRUE = false;
                    P.SYSTEM_EXIT();
                    return null;
                } else {
                    V.CONN1 = SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);
                    if (history > 0) {
                        SQL_HISTORY_SAVE(time_1, SELE, "F");
                    }
                    return SQLEXECT(V.CONN1, SELE, ALIAS, ft);
                }
            } else {
                P.ALERT("");
                if (history > 0) {
                    SQL_HISTORY_SAVE(time_1, SELE, "F");
                }
                FF._CLIPTEXT(SELE);
                V.LAST_ERR = FF.BIG_TEXT_FORMATTER(ex.toString());
                if (err) {
                    P.MESSERR("Ошибка выполнение запроса:" + "\n" + (SELE.length() > 70 ? SELE.substring(0, 67) + "..." : SELE.substring(0, SELE.length())) + "\n" + FF.BIG_TEXT_FORMATTER(ex.toString()));
                }
                return null;
            }
        }
// обработка полученного набора данных , получение объекта с набором и описанием данных
        if (ft) {
            P.ALERT("Форматирование данных... ");
        }
//        if (!"KASSA".equals(V.PROGRAMM)) {  // временно пока касса в эталоне нельзя править
        A.CLEAR(ALIAS);
//        }
        DATA = new Cursorr(CURSOR);
        if (ft) {
            P.ALERT("Данные получены..." + FF.DATETIMES());
        }
        ALIAS = ALIAS.toUpperCase();
        DATA.ALIAS = ALIAS;
        if (V.MAPALIAS == null) {
            V.MAPALIAS = new HashMap<>();
        }  //если первый раз - создаем
        V.MAPALIAS.put(ALIAS.toUpperCase(), DATA); // записываем в массив данных по ключу имени алиаса
        S.SETDATARECNO(DATA, 1);
//        S.SETACTIVEALIAS(ALIAS);
        P.ALERT("");
        if (history > 0) {
            SQL_HISTORY_SAVE(time_1, SELE, "T");
        }
        return DATA;
    }

    public static Cursorr SQLEXECT(Connection CONN, String SELE, String ALIAS, boolean ft) {
        //    Cursorr curs = SQLEXECT(CONN, SELE, ALIAS, ft, true, 1);
        Cursorr curs = SQLEXECT(CONN, SELE, ALIAS, ft, true, 0); //19/03/2018 rkv
        return curs;
    }

    /**
     * Выполнение запроса для получения данных
     *
     * @param SELE - строка запроса
     * @return - объект набор данных
     */
    public static Cursorr SQLEXECT(String SELE) {
        Cursorr curs = SQLEXECT(V.CONN1, SELE, "SQL", false);
        return curs;
    }

    public static Cursorr SQLEXECT(String SELE, String ALIAS) {
        Cursorr curs = SQLEXECT(V.CONN1, SELE, ALIAS, true);
        return curs;
    }

    /**
     *
     * @param SELE
     * @param ALIAS
     * @param ft - показ сообщения
     * @return
     */
    public static Cursorr SQLEXECT(String SELE, String ALIAS, boolean ft) {
        Cursorr curs = SQLEXECT(V.CONN1, SELE, ALIAS, ft);
        return curs;
    }

    public static Cursorr SQLEXECT(String SELE, String ALIAS, boolean ft, boolean err) {
        Cursorr curs = SQLEXECT(V.CONN1, SELE, ALIAS, ft, err, 1);
        return curs;
    }

    public static Cursorr SQLEXECT(String SELE, String ALIAS, boolean ft, int history) {
        Cursorr curs = SQLEXECT(V.CONN1, SELE, ALIAS, ft, true, history);
        return curs;
    }

    /**
     * Проверяет на наличие кода доступа к дополнительным правам
     *
     * @param KOD - код доступа
     * @return
     */
    public static boolean VALID_MENUD(String KOD) {
        KOD = FF.ALLTRIM(KOD); //удаляем по краям пробелы
        KOD.replaceAll(",", "");  //убираем запятые
        return FF.AT("," + KOD + ",", V.USER_MENU) > 0;  //ищем код в строке мени
    }

    /**
     * Создание курсора данных из вектора переменных
     *
     * @param DIM - вектор название полей по принципу тип_номер по порядку,
     * например "BIT_0", "CHAR_1","CHAR_2"
     * @param ALIAS - имя алиаса курсора
     * @return
     */
    public static Cursorr SET_CURSOR_FROM_DIM(Vector DIM, String ALIAS) {
        Cursorr DATA = new Cursorr(DIM);
        ALIAS = ALIAS.toUpperCase();
        DATA.ALIAS = ALIAS;
        if (V.MAPALIAS == null) {
            V.MAPALIAS = new HashMap<>();
        }  //если первый раз - создаем
        V.MAPALIAS.put(ALIAS.toUpperCase(), DATA); // записываем в массив данных по ключу имени алиаса
        S.SETDATARECNO(DATA, 1);
        return DATA;
    }

    /**
     * Перевод к строковому виду объекта значения
     *
     * @param pole
     * @return
     */
    public static String TOSTR(Object pole) {
        String str = "";
        if (pole instanceof Boolean) {
            if ((Boolean) pole == true) {
                str = "T";
            } else {
                str = "F";
            }
        } else {
            if (pole instanceof  String) {
                str = pole.getClass().toString();
                str = (String) pole;
                str = FF.RTRIM(str);
            } else {
                if (pole instanceof int[]) {
                    str = pole.getClass().toString();
                    str = (String) pole;
                } else {
                    if (pole instanceof Float || pole instanceof Double) {
                        str = pole.getClass().toString();
                        str = pole.toString();
                    } else {
                        if (pole instanceof Integer) {
                            str = pole.getClass().toString();
                            str = pole.toString();
                        } else {
                            if (pole instanceof Date && !"Timestamp".equals(pole.getClass().getSimpleName())) {
                                str = V.ddmmyyyy.format(pole);
                            } else {
                                if (pole instanceof Date && "Timestamp".equals(pole.getClass().getSimpleName())) {
                                    str = V.ddmmyyyyhhmm.format(pole);
                                } else {
                                }
                            }
                        }
                    }
                }
            }
        }
        return str;
    }

    /**
     * Перевод значение объекта в формат передачи в sql запросе
     *
     * @param pole объект значение
     * @return
     */
    public static String P_SQL(Object pole) {
        String str = "' '";
        if (pole instanceof Boolean) {
            if ((Boolean) pole == true) {
                str = "'T'";
            } else {
                str = "'F'";
            }
        }
        if (pole instanceof String) {
            //   str = pole.getClass().toString();
            str = (String) pole;
            str = str.replaceAll("'", "'||CHR(39)||'");
            str = "'" + FF.RTRIM(str) + "'";
        }
        if (pole instanceof int[]) {
            //  str = pole.getClass().toString();
            str = (String) pole;
        }
        if (pole instanceof Float || pole instanceof Double || pole instanceof Integer || pole instanceof Number || pole instanceof BigDecimal || pole instanceof Long) {
            //str = pole.getClass().toString();
            str = pole.toString();
        }
        if (pole instanceof Date) {
            if (pole.toString().indexOf(":") == -1) {
                str = V.ddmmyyyy.format(pole);
                str = P.STRP("to_date('", str, "','", V.MASK_DATE, "')");
            } else {
                str = V.ddmmyyyyhhmmSSS.format(pole);
                str = P.STRP("to_timestamp('", str, "','", V.MASK_DATETIME_ORACLE, "')");
            }
        }
        if (pole instanceof Timestamp) {
            str = V.ddmmyyyyhhmmSSS.format(pole);
            str = P.STRP("to_timestamp('", str, "','", V.MASK_DATETIME_ORACLE, "')");
        }
        if (pole == null) {
            str = "null";
        }

        if ("''".equals(str)) {
            str = "' '";
        }
        return str;

    }

    /**
     * Перевод чсимаолной строки в определенный формат передачи в sql запросе
     *
     * @param val - строка
     * @param type - тип данных V.TYPE_
     *
     * @return
     */
    public static String P_SQL(String val, String type) {
        String str = "' '";

        if (type.equals(V.TYPE_BIT) || type.equals(V.TYPE_CHAR)) {
            str = "'" + FF.RTRIM(val) + "'";
        }
        if (type.equals(V.TYPE_NUMERIC)) {
            if ("".equals(val.trim())) {
                str = "0";
            } else {
                str = val;
            }
        }
        if (type.equals(V.TYPE_DATE)) {
            str = P.STRP("to_date('", val, "','", V.MASK_DATE, "')");
        } else {
            if (type.equals(V.TYPE_DATETIME)) {
                str = P.STRP("to_date('", val, "','", V.MASK_DATE_ORACLE, "')");
            }
        }
        if ("''".equals(str)) {
            str = "' '";
        }
        return str;
    }

    public static int PING(String adr) {
        try {
            int timeout = 2000;
            InetAddress[] addresses = InetAddress.getAllByName(adr);
            for (InetAddress address : addresses) {
                if (address.isReachable(timeout)) {
                    System.out.printf("%s is reachable%n", address);
                    return 0;
                } else {
                    System.out.printf("%s could not be contacted%n", address);
                    return 1;
                }
            }
        } catch (Exception e) {
            System.out.printf("Unknown host: %s%n", "192.168.2.121");
            return 2;
        }
        return 0;
    }
    /*     public static String str;
     public static void MESSP(String strp) {
     str=strp;
     SwingUtilities.invokeLater(new Runnable() {

     @Override
     public void run() {
     //       ProgressMessage pm = new ProgressMessage("Пожалуйста, подождите! Идет загрузка...");
     }


     });
     }
     * */

    public static void ALERT(String str) {
        if (!V.PRG_CONSOLE) { //если не консоль
            ALERT(str, 0);
        } else { //консоль
            MESS_CONSOLE(str);
        }
    }

    /**
     * Показ сообщения в правом углу экрана
     *
     * @param str - сообщение
     * @param time - миллисекунды показа собщения (0 - до нажатия клавиши или
     * мыши)
     */
    public static void ALERT(String str, int time) {
        if (!V.ALERT_SHOW) {
            return;
        }
        V.ALERT_TEXT = str;
        if ("".equals(str)) {
            //         V._SCREEN.MESS.setText(str);
            V._SCREEN.MESS.REFRESH();
            V._SCREEN.MESS.setVisible(false);
            return;
        }
        V._SCREEN.MESS.setText(str);
        V._SCREEN.MESS.setLocation(V._SCREEN.getWidth() - V._SCREEN.MESS.getWidth() - 20, 10);
        V._SCREEN.MESS.setVisible(true);
        V._SCREEN.MESS.REFRESH();
        if (time > 0) {
            V._SCREEN.MESS.timer.setDelay(time);
            V._SCREEN.MESS.timer.start();
        }
    }

    public static void ALERTF(String str, int location) {
        if (!V.ALERT_SHOW) {
            return;
        }
        if ("".equals(str)) {
            //         V._SCREEN.MESSC.setText(str);
            V._SCREEN.MESSC.REFRESH();
            V._SCREEN.MESSC.setVisible(false);
            return;
        }
        V._SCREEN.MESSC.setText(str.replaceAll("\n", "<br>"));
        if (location == V.LOC_LEFT) {
            V._SCREEN.MESSC.setLocation(10, 10);
        } else if (location == V.LOC_RIGHT) {
            V._SCREEN.MESSC.setLocation(V._SCREEN.getWidth() - V._SCREEN.MESSC.getWidth() - 20, 10);
        } else {
            V._SCREEN.MESSC.setLocation(V._SCREEN.getWidth() / 2 - V._SCREEN.MESSC.getWidth() / 2 - 20, V._SCREEN.getHeight() / 2 - 80);
        }
        V._SCREEN.MESSC.setVisible(true);
        V._SCREEN.MESSC.REFRESH();
    }

    /**
     * Вызов меню
     *
     * @param str - массив пунктов меню
     * @param obj - относительно какого объекта Font new java.awt.Font("Arial",
     * 1, 12)
     * @return - не авбрано -0 иначе по пунктам от 1
     */
    public static int MENU(String[] str, Component obj) {//вызов меню по клику мыши
        return MENU(str, obj, new java.awt.Font("Arial", 1, 12));
    }

    /**
     * Вызов меню
     *
     * @param str - массив пунктов меню
     * @param obj - относительно какого объекта
     * @param font -Font
     * @return - не авбрано -0 иначе по пунктам от 1
     */
    public static int MENU(String[] str, Component obj, java.awt.Font font) {//вызов меню по клику мыши
        PopMenu pop = new PopMenu(str, obj, font);
        STARTMODAL(pop);
        return pop.SelMenu;
//    pop.show(cmp, (int) MouseInfo.getPointerInfo().getLocation().x - V._SCREEN.getX(), (int) MouseInfo.getPointerInfo().getLocation().y - V._SCREEN.getY() - 45);
    }

    //--------------------------------------------------------------------------
/**
 * Текущее значение координат мыши
 * @return Point
 */    
    public static Point MOUSE_XY() {//
        Point loc = MouseInfo.getPointerInfo().getLocation();
        return loc;
    }
/**
 * ПРОИГРИВАНИЕ ЗВУКОВОГО ФАЙЛА В ФОНОВОМ РЕЖИМЕ
 * @param url -имя файла
 */
    public static synchronized void PLAY_SOUND(final String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            @Override
            public void run() {
                try {
                    String soundDir = ""; // Рабочий каталог проекта
                    File jarFile = new File(".");
                    if (jarFile.exists()) {
                        soundDir = jarFile.getAbsolutePath().toString().substring(0, jarFile.getAbsolutePath().toString().length() - 1) + V.SEPARATOR + "Sound" + V.SEPARATOR;
                    }
                    jarFile.delete();
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(soundDir + url).getAbsoluteFile());
                    clip.open(inputStream);
                    clip.start();
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
//exel

    public static void CREATE_CELL_STYLE(CellStyle style, short styleBorderBottom, short styleBorderLeft,
            short styleBorderRight, short styleBorderTop) {//границы
        style.setBorderBottom(styleBorderBottom);
        style.setBorderLeft(styleBorderLeft);
        style.setBorderRight(styleBorderRight);
        style.setBorderTop(styleBorderTop);
    }

    
    /**
     * Читает файл excel и возвращает вектор объектов
     *
     * @param pathfile
     * @return
     */
    public static Vector EXCEL_READ(String pathfile) {
        return EXCEL_READ(pathfile,1);
    }
    /**
     * Читает файл excel и возвращает вектор объектов
     * @param pathfile - путь к файлу
     * @param first - начальная строка для чтения начиная с 1
     * @return 
     */
    public static Vector EXCEL_READ(String pathfile,int first) {
        if (FF.EMPTY(pathfile)) {
          //  P.GETFILE("Выберите файл", "", "xls,xlsx", V.CURRENT_PATH);
            pathfile=P.EXCEL_FILECHOOSE("Выберите файл",V.CURRENT_PATH);
        if (FF.EMPTY(pathfile)) {
            return null;
        }
            
        }
        first--; //нумерация по массиву чтобы с 0
        FileInputStream file = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        Vector row_xls = new Vector(); // хранить записи из результирующего множества выборки
        try {
            file = new FileInputStream(new File(pathfile));
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            int jj = 0, i = 0;
            int all = sheet.getPhysicalNumberOfRows()-first; //за минусом номера начальной строки
            Row row=null;
            while (rowIterator.hasNext()) { // проверка если еще итерации (строки)
                if (i<first) {  //если еще не начальная строка
                  row = rowIterator.next();                   // просто проходим
                  i++;
                  continue; //в начало цикла
                }
                i++;
                jj++;
                if (jj == 100) {
                    P.ALERT("Получение данных из " + pathfile + "   Записей:" + i + " из " + all);
                    jj = 0;
                }

                row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                // for each row all columns
                ArrayList cellList = new ArrayList();//иницилизируем контейнер , хранить данные по каждому столбцу (ячейки)
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Object cellValue = null; //иницилизируем переменную
                    //check cell type
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            cellValue = cell.getNumericCellValue();
                            //cellValue = cell.getDateCellValue();
                            break;
                        case Cell.CELL_TYPE_STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        default:
                            cellValue = cell.getStringCellValue();
                            break;
                    }
                    cellList.add(cellValue); //добавляем объект со значением в контейнер
                }
                Object[] cells = cellList.toArray();//копируем строку (контейнер) в  массив объектов
                row_xls.add(cells); // добавление массива объектов строки в контейнер строк

            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row_xls;
    }

    public static void EXCEL_SAVING(Cursorr grid, String file) {
        String workDir = ""; // Рабочий каталог проекта
        int count = 0;
        File jarFile = new File(".");
        if (jarFile.exists()) {
            workDir = jarFile.getAbsolutePath().toString().substring(0, jarFile.getAbsolutePath().toString().length() - 1) + "\\Excel\\";
        }
        jarFile.delete();
        File myPath = new File(workDir);
        myPath.mkdirs();
        String date = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new GregorianCalendar().getTime());

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        Sheet sheet = null;

        CellStyle valueStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(valueStyle, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_NONE);
        valueStyle.setWrapText(true);
        CellStyle headerStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(headerStyle, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK);
        headerStyle.setWrapText(true);
        FileInputStream fis = null;

        for (int rowInd = 0; rowInd < grid.ROWCOUNT; rowInd++) {
            if (count == 0) {

                sheet = workbook.createSheet();
//                Row row = sheet.createRow(count);
//                CellRangeAddress region = new CellRangeAddress(0, 0, 0, grid.COLCOUNT - 1);
//                sheet.addMergedRegion(region);
//                Cell cell = row.createCell(0);
//                cell.setCellValue("asd");//заголовок
//                CellStyle mainHeaderStyle = workbook.createCellStyle();
//                mainHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
//                Font font = workbook.createFont();
//                font.setFontHeightInPoints((short) 20);
//                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//                mainHeaderStyle.setFont(font);
//                cell.setCellStyle(mainHeaderStyle);
//                count++;

                Row row = sheet.createRow(count);
                for (int colIndHead = 0; colIndHead < grid.COLCOUNT - 1; colIndHead++) {
                    Cell cell = row.createCell(colIndHead);

                    cell.setCellStyle(headerStyle);
                    if (!grid.COLNAMES[colIndHead].contains("<html><center>")) {
                        cell.setCellValue(grid.COLNAMES[colIndHead]);
                    } else {
                        cell.setCellValue(grid.COLNAMES[colIndHead].substring("<html><center>".length()));
                    }
                }
            }
            Row row = sheet.createRow(count + 1);
            for (int colInd = 0; colInd < grid.COLCOUNT - 1; colInd++) {
                Cell cell = row.createCell(colInd);
                cell.setCellStyle(valueStyle);
                if (((Object[]) grid.rowList.get(rowInd))[colInd] == null) {
//                    grid.setValueAt("", rowInd, colInd);
                    cell.setCellValue("");
                } else {
                    String val = ((Object[]) grid.rowList.get(rowInd))[colInd].toString();
                    cell.setCellValue(val.length() > 2 && val.substring(val.length() - 2, val.length()).equals(".0") ? val.substring(0, val.length() - 2) : val);
                }
                sheet.setColumnWidth((short) colInd, (short) 50 * 50);
            }
            count++;
            if (count % 100 == 0) {
                try {
                    ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others

                    // ((SXSSFSheet)sh).flushRows() is a shortcut for ((SXSSFSheet)sh).flushRows(0),
                    // this method flushes all rows
                } catch (IOException ex) {
                    Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        P.ALERT("Сохранение данных в Excel...");
        try {
            if (sheet != null) {
//                String date = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new GregorianCalendar().getTime());
                try (FileOutputStream out = new FileOutputStream(file)) {
                    workbook.write(out);
                    out.close();
                    workbook.dispose();
                }
            }
//            ArrayList<Workbook> arrW = new ArrayList<>();
//            arrW.add(new XSSFWorkbook(new FileInputStream(workDir + "50000" + ".xlsx")));
//            arrW.add(new XSSFWorkbook(new FileInputStream(workDir + "end" + ".xlsx")));
//            try (FileOutputStream out = new FileOutputStream(workDir + "end" + ".xlsx")) {
//                arrW.get(0).write(out);
//                out.close();
//            }
            Runtime.getRuntime().exec("cmd.exe /c \"" + file + "\"");
        } catch (IOException ex) {
            try (FileOutputStream out = new FileOutputStream(workDir + date + ".xlsx")) {
                workbook.write(out);
                out.close();
                workbook.dispose();
                Runtime.getRuntime().exec("cmd.exe /c \"" + workDir + date + ".xlsx\"");
            } catch (IOException wxx) {
                P.MESSERR("Ошибка сохранения!");
            }
        }
        P.ALERT("");
    }

    public static void EXCEL_SAVING(Gridr grid) {
        String workDir = ""; // Рабочий каталог проекта
        int count = 0;
        File jarFile = new File(".");
        if (jarFile.exists()) {
//                workDir = jarFile.getAbsolutePath().toString().substring(0, jarFile.getAbsolutePath().toString().length() - 1) + "\\Excel\\";
            workDir = V.RUN_DIRECTORY + "\\Excel\\";
        }
        jarFile.delete();
        File myPath = new File(workDir);
        myPath.mkdirs();

//        Workbook workbook = new XSSFWorkbook();
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        Sheet sheet = null;

        CellStyle valueStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(valueStyle, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_NONE);
        valueStyle.setWrapText(true);
        CellStyle headerStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(headerStyle, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK);
        headerStyle.setWrapText(true);

        for (int rowInd = 0; rowInd < grid.getRowCount(); rowInd++) {
            if (count == 0) {

                sheet = workbook.createSheet();
                Row row = sheet.createRow(count);
                CellRangeAddress region = new CellRangeAddress(0, 0, 0, grid.getColumnCount() - 1);
                sheet.addMergedRegion(region);
                Cell cell = row.createCell(0);
                cell.setCellValue(grid.THISFORM.getTitle());//заголовок
                CellStyle mainHeaderStyle = workbook.createCellStyle();
                mainHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
                Font font = workbook.createFont();
                font.setFontHeightInPoints((short) 20);
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                mainHeaderStyle.setFont(font);
                cell.setCellStyle(mainHeaderStyle);
                count++;

                row = sheet.createRow(count);
                for (int colIndHead = 0; colIndHead < grid.getColumnCount(); colIndHead++) {
                    cell = row.createCell(colIndHead);
                    headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
                    cell.setCellStyle(headerStyle); //СТИЛЬ ЯЧЕЕК ШАПКИ ТАБЛИЦЫ
                    String cap = grid.getColumnName(colIndHead);
                    if (cap.contains("<html><center>")) {
                        cap = cap.substring("<html><center>".length());
                    }
                    if (cap.contains("<br>")) {
                        cap = cap.replaceAll("<br>", "");
                    }
                    cell.setCellValue(cap);
                    /*
                     if (!grid.getColumnName(colIndHead).contains("<html><center>")) {
                     cell.setCellValue(grid.getColumnName(colIndHead));
                     } else {
                     cell.setCellValue(grid.getColumnName(colIndHead).substring("<html><center>".length()));
                     }
                     */
                }
            }
            Row row = sheet.createRow(count + 1);
            for (int colInd = 0; colInd < grid.getColumnCount(); colInd++) {
                Cell cell = row.createCell(colInd);
                cell.setCellStyle(valueStyle);
                if (grid.getValueAt(rowInd, colInd) == null) {
                    grid.setValueAt("", rowInd, colInd);
                    cell.setCellValue("");
                } else {
                    if ("N".equals(grid.getColumn(colInd).TYPE)) {
                        String str = grid.getValueAt(rowInd, colInd).toString();
                        str = str.replaceAll(" ", "");
                        try {
                            double value = Double.parseDouble(str);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(value);
                        } catch (NumberFormatException escc) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(str);
                        }
                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(grid.getValueAt(rowInd, colInd).toString());
                    }
                }
                sheet.setColumnWidth((short) colInd, (short) grid.getColumnModel().getColumn(colInd).getWidth() * 50);
            }
            count++;
            if (count % 100 == 0) {
                try {
                    ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others

                    // ((SXSSFSheet)sh).flushRows() is a shortcut for ((SXSSFSheet)sh).flushRows(0),
                    // this method flushes all rows
                } catch (IOException ex) {
                    Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        P.ALERT("Сохранение данных в Excel...");
        try {
            if (sheet != null) {
                String date = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new GregorianCalendar().getTime());
                try (FileOutputStream out = new FileOutputStream(workDir + date + ".xlsx")) {
                    workbook.write(out);
                    out.close();
                    workbook.dispose();
                    Runtime.getRuntime().exec("cmd.exe /c \"" + workDir + date + ".xlsx\"");
                }
            }
        } catch (IOException ex) {
        }
        P.ALERT("");
    }

    public static void EXCEL_SAVING_2(Gridr grid) {//без сохранения колонки с номером строки
        String workDir = ""; // Рабочий каталог проекта
        int count = 0;
        File jarFile = new File(".");
        if (jarFile.exists()) {
//            workDir = jarFile.getAbsolutePath().toString().substring(0, jarFile.getAbsolutePath().toString().length() - 1) + "\\Excel\\";
            workDir = V.RUN_DIRECTORY + "\\Excel\\";
        }
        jarFile.delete();
        File myPath = new File(workDir);
        myPath.mkdirs();

//        Workbook workbook = new XSSFWorkbook();
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        Sheet sheet = null;

        CellStyle valueStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(valueStyle, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_NONE);
        valueStyle.setWrapText(true);
        CellStyle headerStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(headerStyle, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK);
        headerStyle.setWrapText(true);

        for (int rowInd = 0; rowInd < grid.getRowCount(); rowInd++) {
            if (count == 0) {

                sheet = workbook.createSheet();
                Row row = sheet.createRow(count);
                CellRangeAddress region = new CellRangeAddress(0, 0, 0, grid.getColumnCount() - 2);
                sheet.addMergedRegion(region);
                Cell cell = row.createCell(0);
                cell.setCellValue(grid.THISFORM.getTitle());//заголовок
                CellStyle mainHeaderStyle = workbook.createCellStyle();
                mainHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
                Font font = workbook.createFont();
                font.setFontHeightInPoints((short) 20);
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                mainHeaderStyle.setFont(font);
                cell.setCellStyle(mainHeaderStyle);
                count++;

                row = sheet.createRow(count);
                for (int colIndHead = 0; colIndHead < grid.getColumnCount() - 1; colIndHead++) {
                    cell = row.createCell(colIndHead);

                    cell.setCellStyle(headerStyle);
                    if (!grid.getColumnName(colIndHead).contains("<html><center>")) {
                        cell.setCellValue(grid.getColumnName(colIndHead + 1));
                    } else {
                        cell.setCellValue(grid.getColumnName(colIndHead + 1).substring("<html><center>".length()));
                    }
                }
            }
            Row row = sheet.createRow(count + 1);
            for (int colInd = 1; colInd < grid.getColumnCount(); colInd++) {
                Cell cell = row.createCell(colInd - 1);
                cell.setCellStyle(valueStyle);
                if (grid.getValueAt(rowInd, colInd) == null) {
                    grid.setValueAt("", rowInd, colInd);
                    cell.setCellValue("");
                } else {
                    if ("N".equals(grid.getColumn(colInd).TYPE)) {
                        String str = grid.getValueAt(rowInd, colInd).toString();
                        str = str.replaceAll(" ", "");
                        try {
                            double value = Double.parseDouble(str);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(value);
                        } catch (NumberFormatException escc) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(str);
                        }
                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(grid.getValueAt(rowInd, colInd).toString());
                    }
                }
                sheet.setColumnWidth((short) colInd - 1, (short) grid.getColumnModel().getColumn(colInd).getWidth() * 50);
            }
            count++;
            if (count % 100 == 0) {
                try {
                    ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others

                    // ((SXSSFSheet)sh).flushRows() is a shortcut for ((SXSSFSheet)sh).flushRows(0),
                    // this method flushes all rows
                } catch (IOException ex) {
                    Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        P.ALERT("Сохранение данных в Excel...");
        try {
            if (sheet != null) {
                String date = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new GregorianCalendar().getTime());
                try (FileOutputStream out = new FileOutputStream(workDir + date + ".xlsx")) {
                    workbook.write(out);
                    out.close();
                    workbook.dispose();
                    Runtime.getRuntime().exec("cmd.exe /c \"" + workDir + date + ".xlsx\"");
                }
            }
        } catch (IOException ex) {
        }
        P.ALERT("");
    }

    public static void EXCEL_SAVING(ArrayList<String> arrP, ArrayList<Gridr> grid) {
        String workDir = ""; // Рабочий каталог проекта
        int count = 0;
        File jarFile = new File(".");
        if (jarFile.exists()) {
            workDir = jarFile.getAbsolutePath().toString().substring(0, jarFile.getAbsolutePath().toString().length() - 1) + "\\Excel\\";
        }
        jarFile.delete();
        File myPath = new File(workDir);
        myPath.mkdirs();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = null;

        CellStyle valueStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(valueStyle, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_NONE);
        valueStyle.setWrapText(true);
        CellStyle headerStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(headerStyle, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK);
        headerStyle.setWrapText(true);

        for (int arrId = 0; arrId < grid.size(); arrId++) {
            for (int rowInd = 0; rowInd < grid.get(arrId).getRowCount(); rowInd++) {
                if (count == 0) {

                    sheet = workbook.createSheet();
                    Row row = sheet.createRow(count);
                    CellRangeAddress region = new CellRangeAddress(0, 0, 0, grid.get(arrId).getColumnCount() - 1);
                    sheet.addMergedRegion(region);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(grid.get(arrId).THISFORM.getTitle());//заголовок
                    CellStyle mainHeaderStyle = workbook.createCellStyle();
                    mainHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
                    Font font = workbook.createFont();
                    font.setFontHeightInPoints((short) 20);
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    mainHeaderStyle.setFont(font);
                    cell.setCellStyle(mainHeaderStyle);
                    count++;

                    row = sheet.createRow(count);
                    for (int colIndHead = 0; colIndHead < grid.get(arrId).getColumnCount() + 1; colIndHead++) {
                        cell = row.createCell(colIndHead);
                        if (colIndHead == grid.get(arrId).getColumnCount()) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellStyle(headerStyle);
                            if (!grid.get(arrId).getColumnName(colIndHead).contains("<html><center>")) {
                                cell.setCellValue(grid.get(arrId).getColumnName(colIndHead));
                            } else {
                                cell.setCellValue(grid.get(arrId).getColumnName(colIndHead).substring("<html><center>".length()));
                            }
                        }

                    }
                }
                Row row = sheet.createRow(count + 1);
                for (int colInd = 0; colInd < grid.get(arrId).getColumnCount() + 1; colInd++) {
                    Cell cell = row.createCell(colInd);
                    cell.setCellStyle(valueStyle);
                    if (colInd == grid.get(arrId).getColumnCount()) {
                        cell.setCellValue(arrP.get(arrId));
                        sheet.setColumnWidth((short) colInd, (short) grid.get(arrId).getColumnModel().getColumn(colInd - 1).getWidth() * 2 * 50);
                    } else {
                        if (grid.get(arrId).getValueAt(rowInd, colInd) == null) {
                            grid.get(arrId).setValueAt("", rowInd, colInd);
                            cell.setCellValue("");
                        } else {
                            if ("N".equals(grid.get(arrId).getColumn(colInd).TYPE)) {
                                String str = grid.get(arrId).getValueAt(rowInd, colInd).toString();
                                str = str.replaceAll(" ", "");
                                try {
                                    double value = Double.parseDouble(str);
                                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                    cell.setCellValue(value);
                                } catch (NumberFormatException escc) {
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    cell.setCellValue(str);
                                }
                            } else {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                cell.setCellValue(grid.get(arrId).getValueAt(rowInd, colInd).toString());
                            }
                        }
                        sheet.setColumnWidth((short) colInd, (short) grid.get(arrId).getColumnModel().getColumn(colInd).getWidth() * 50);
                    }
                }
                count++;
            }
        }
        try {
            if (sheet != null) {
                String date = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new GregorianCalendar().getTime());
                try (FileOutputStream out = new FileOutputStream(workDir + date + ".xlsx")) {
                    workbook.write(out);
                    out.close();
                    Runtime.getRuntime().exec("cmd.exe /c \"" + workDir + date + ".xlsx\"");
                }
            }
        } catch (IOException ex) {
        }
    }

    public static void SAVE_TXT_HISTORY(Component cmp, Fieldrp txt) {
        //пустышка для поля пароля
    }

    public static void SAVE_TXT_HISTORY(Component cmp, Fieldr txt) {

        boolean doubleFlag = false;
        int doubleInd = -1;
        if (V.TXT_HISTORY_COUNT > 0) {

            Configs proper = new Configs(V.fileNameTxtLog);

            ArrayList<String> propArr = new ArrayList<>();
            for (int ind = 0; ind < proper.size(); ind++) {
                if (proper.getProperty(cmp.getClass().getName() + "$" + txt.getName() + ind) != null) {
                    propArr.add(proper.getProperty(cmp.getClass().getName() + "$" + txt.getName() + ind));
                }
            }
            for (int ind = 0; ind < propArr.size(); ind++) {
                if (propArr.get(ind).equals(txt.getText())) {
                    doubleFlag = true;
                    doubleInd = ind;
                }
            }

            if (doubleFlag) {
                if (propArr.size() <= V.TXT_HISTORY_COUNT) {
                    proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + (propArr.size() - 1), propArr.get(doubleInd));
                    for (int ind = doubleInd; ind < propArr.size() - 1; ind++) {
                        proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + ind, propArr.get(ind + 1));
                    }
                } else {
                    for (int ind = 0; ind < propArr.size(); ind++) {
                        proper.remove(cmp.getClass().getName() + "$" + txt.getName() + ind);
                    }
                    for (int ind = 0; ind < V.TXT_HISTORY_COUNT; ind++) {
                        proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + ind, propArr.get(ind + propArr.size() - V.TXT_HISTORY_COUNT));
                    }
                    propArr.clear();
                    doubleFlag = false;
                    for (int ind = 0; ind < proper.size(); ind++) {
                        if (proper.getProperty(cmp.getClass().getName() + "$" + txt.getName() + ind) != null) {
                            propArr.add(proper.getProperty(cmp.getClass().getName() + "$" + txt.getName() + ind));
                        }
                    }
                    for (int ind = 0; ind < proper.size(); ind++) {
                        if (propArr.get(ind).equals(txt.getText())) {
                            doubleFlag = true;
                            doubleInd = ind;
                        }
                    }
                    if (doubleFlag) {
                        proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + (propArr.size() - 1), propArr.get(doubleInd));
                        for (int ind = doubleInd; ind < propArr.size() - 1; ind++) {
                            proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + ind, propArr.get(ind + 1));
                        }
                    }
                }
            } else if (propArr.size() < V.TXT_HISTORY_COUNT) {
                //
                int num = 0;
                if (proper.size() != 0) {
                    for (int ind = 0; ind < proper.size(); ind++) {
                        if (proper.getProperty(cmp.getClass().getName() + "$" + txt.getName() + ind) == null && ind == 0) {
                            num = 0;
                            break;
                        } else if (proper.getProperty(cmp.getClass().getName() + "$" + txt.getName() + ind) == null && ind != 0) {
                            num = ind;
                            break;
                        } else {
                            num = ind + 1;
                        }
                    }
                }
                proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + num, txt.getText());
            } else if (propArr.size() == V.TXT_HISTORY_COUNT) {
                for (int ind = propArr.size() - V.TXT_HISTORY_COUNT + 1; ind < propArr.size(); ind++) {
                    proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + (ind - 1), propArr.get(ind));
                }
                proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + (V.TXT_HISTORY_COUNT - 1), txt.getText());
            } else {
                for (int ind = 0; ind < propArr.size(); ind++) {
                    proper.remove(cmp.getClass().getName() + "$" + txt.getName() + ind);
                }
                for (int ind = 0; ind < V.TXT_HISTORY_COUNT - 1; ind++) {
                    proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + ind, propArr.get(ind + 1 + propArr.size() - V.TXT_HISTORY_COUNT));
                }
                proper.setProperty(cmp.getClass().getName() + "$" + txt.getName() + (V.TXT_HISTORY_COUNT - 1), txt.getText());
            }
            proper.saveProperties("Txt History");
        }
    }

    public static ArrayList<String> SHOW_TXT_HISTORY(Component cmp, Fieldrp txt) {
        ArrayList<String> str = new ArrayList<>();
        return str;
        // ПУСТЫШКА ДЛЯ ПОЛЯ ПАРОЛЯ
    }

    public static ArrayList<String> SHOW_TXT_HISTORY(Component cmp, Fieldr txt) {
        Configs proper = new Configs(V.fileNameTxtLog);
        ArrayList<String> str = new ArrayList<>();
        try {
            for (int ind = 0; ind < proper.size(); ind++) {
                if (proper.getProperty(cmp.getClass().getName() + "$" + txt.getName() + ind) != null) {
                    str.add(proper.getProperty(cmp.getClass().getName() + "$" + txt.getName() + ind));
                }
            }
        } catch (Exception e) {
            System.out.println("Не найдены сохраненные истории");
        }
        return str;
    }

    public static synchronized void STARTMODAL(final Component cmp) {//метод не позволяющий выполнение действий вне компонента
        boolean prF = false;
        boolean drF = false;
       try {
            if (SwingUtilities.isEventDispatchThread() ) { //Возвращает true, если текущий поток является потоком диспетчеризации событий AWT.
                EventQueue theQueue         //EventQueue - это независимый от платформы класс, который ставит в очередь события как из базовых одноранговых классов, так и из классов доверенных приложений.
                        = cmp.getToolkit().getSystemEventQueue();
                while (cmp.isVisible()) { //ЕСЛИ ОБЪЕКТ ВИЗИБЛЕ
                    AWTEvent event = theQueue.getNextEvent(); //Удаляет событие из EventQueue и возвращает его. Этот метод будет блокироваться, пока событие не будет опубликовано другим потоком.
                    Object source = event.getSource(); //ОБЪЕКТ НА КОТОРОМ СОБЫТИЕ ИНИЦИАЛИЗИРОВАННО
                    boolean dispatch = true;
                    if (event instanceof MouseEvent) {
                        MouseEvent e = (MouseEvent) event;
                        MouseEvent m = SwingUtilities.convertMouseEvent((Component) e.getSource(), e, cmp);
                        if (V.TOOLBAR != null) {//существование тулбара
                            MouseEvent t = SwingUtilities.convertMouseEvent((Component) e.getSource(), e, V.TOOLBAR);
                            /////флаги прэса и релиза внутри модальной формы
                            if (cmp.contains(m.getPoint())
                                    && e.getID() != MouseEvent.MOUSE_DRAGGED
                                    && !V.TOOLBAR.contains(t.getPoint())
                                    && !cmp.getClass().getName().equals("aplclass.PopMenu")
                                    && !cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup")
                                    && !cmp.getClass().getName().equals("com.toedter.calendar.JCalendar")) {
                                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                                    prF = true;
                                }
                                if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                                    prF = false;
                                }
                            }
                            ///проверка на смещение мыши за форму при зажатой клавише мыши
                            if (e.getID() == MouseEvent.MOUSE_DRAGGED
                                    && prF
                                    && !V.TOOLBAR.contains(t.getPoint())
                                    && !cmp.getClass().getName().equals("aplclass.PopMenu")
                                    && !cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup")
                                    && !cmp.getClass().getName().equals("com.toedter.calendar.JCalendar")) {
                                drF = true;
                            }
                            ////события мыши вне формы - запрет
                            if (!cmp.contains(m.getPoint())
                                    && !V.TOOLBAR.contains(t.getPoint())
                                    && e.getID() != MouseEvent.MOUSE_DRAGGED
                                    && !cmp.getClass().getName().equals("aplclass.PopMenu")
                                    && !cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup")
                                    && !cmp.getClass().getName().equals("com.toedter.calendar.JCalendar")) {
                                dispatch = false;
                            }
                            if (!cmp.contains(m.getPoint())
                                    && !V.TOOLBAR.contains(t.getPoint())
                                    && e.getID() == MouseEvent.MOUSE_RELEASED
                                    && !cmp.getClass().getName().equals("aplclass.PopMenu")
                                    && !cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup")
                                    && !cmp.getClass().getName().equals("com.toedter.calendar.JCalendar")) {
                                dispatch = false;
                                for (int i = 0; i < ((Formr) cmp).getContentPane().getComponentCount(); i++) {
                                    if (((Formr) cmp).getContentPane().getComponent(i).getClass().getName().equals("baseclass.Fieldr")) {
                                        ((Fieldr) ((Formr) cmp).getContentPane().getComponent(i)).setSelectionStart(0);
                                        ((Fieldr) ((Formr) cmp).getContentPane().getComponent(i)).setSelectionEnd(((Fieldr) ((Formr) cmp).getContentPane().getComponent(i)).getText().length());
                                        if (drF && ((Fieldr) ((Formr) cmp).getContentPane().getComponent(i)).isFocusOwner()) {
                                            dispatch = true;
                                            drF = false;
                                        }
                                    }
                                    if (((Formr) cmp).getContentPane().getComponent(i).getClass().getName().equals("baseclass.Fieldrp")) {
                                        ((Fieldrp) ((Formr) cmp).getContentPane().getComponent(i)).setSelectionStart(0);
                                        ((Fieldrp) ((Formr) cmp).getContentPane().getComponent(i)).setSelectionEnd(((Fieldrp) ((Formr) cmp).getContentPane().getComponent(i)).getText().length());
                                        if (drF && ((Fieldrp) ((Formr) cmp).getContentPane().getComponent(i)).isFocusOwner()) {
                                            dispatch = true;
                                            drF = false;
                                        }
                                    }
                                }
                            }
                            //комбобокс и контекстное меню
                            if (!cmp.contains(m.getPoint())
                                    && !V.TOOLBAR.contains(t.getPoint())
                                    && (cmp.getClass().getName().equals("aplclass.PopMenu")
                                    || cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup"))
                                    && (e.getID() == MouseEvent.MOUSE_PRESSED
                                    || e.getID() == MouseEvent.MOUSE_RELEASED)) {
                                cmp.setVisible(false);
                                dispatch = false;
                            }
                        } else {
                            /////флаги прэса и релиза внутри модальной формы
                            if (cmp.contains(m.getPoint())
                                    && e.getID() != MouseEvent.MOUSE_DRAGGED
                                    && !cmp.getClass().getName().equals("aplclass.PopMenu")
                                    && !cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup")
                                    && !cmp.getClass().getName().equals("com.toedter.calendar.JCalendar")) {
                                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                                    prF = true;
                                }
                                if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                                    prF = false;
                                }
                            }
                            ///проверка на смещение мыши за форму при зажатой клавише мыши
                            if (e.getID() == MouseEvent.MOUSE_DRAGGED
                                    && prF
                                    && !cmp.getClass().getName().equals("aplclass.PopMenu")
                                    && !cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup")
                                    && !cmp.getClass().getName().equals("com.toedter.calendar.JCalendar")) {
                                drF = true;
                            }
                            ////события мыши вне формы - запрет
                            if (!cmp.contains(m.getPoint())
                                    && e.getID() != MouseEvent.MOUSE_DRAGGED
                                    && !cmp.getClass().getName().equals("aplclass.PopMenu")
                                    && !cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup")
                                    && !cmp.getClass().getName().equals("com.toedter.calendar.JCalendar")) {
                                dispatch = false;
                            }
                            ///отпускание мыши за пределами формы
                            if (!cmp.contains(m.getPoint())
                                    && e.getID() == MouseEvent.MOUSE_RELEASED
                                    && !cmp.getClass().getName().equals("aplclass.PopMenu")
                                    && !cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup")
                                    && !cmp.getClass().getName().equals("com.toedter.calendar.JCalendar")) {
                                //выделение текста с релизом мыши вне формы
                                dispatch = false;
                                for (int i = 0; i < ((Formr) cmp).getContentPane().getComponentCount(); i++) {
                                    if (((Formr) cmp).getContentPane().getComponent(i).getClass().getName().equals("baseclass.Fieldr")) {
                                        ((Fieldr) ((Formr) cmp).getContentPane().getComponent(i)).setSelectionStart(0);
                                        ((Fieldr) ((Formr) cmp).getContentPane().getComponent(i)).setSelectionEnd(((Fieldr) ((Formr) cmp).getContentPane().getComponent(i)).getText().length());
                                        if (drF && ((Fieldr) ((Formr) cmp).getContentPane().getComponent(i)).isFocusOwner()) {
                                            dispatch = true;
                                            drF = false;
                                        }
                                    }
                                    if (((Formr) cmp).getContentPane().getComponent(i).getClass().getName().equals("baseclass.Fieldrp")) {
                                        ((Fieldrp) ((Formr) cmp).getContentPane().getComponent(i)).setSelectionStart(0);
                                        ((Fieldrp) ((Formr) cmp).getContentPane().getComponent(i)).setSelectionEnd(((Fieldrp) ((Formr) cmp).getContentPane().getComponent(i)).getText().length());
                                        if (drF && ((Fieldrp) ((Formr) cmp).getContentPane().getComponent(i)).isFocusOwner()) {
                                            dispatch = true;
                                            drF = false;
                                        }
                                    }
                                }
                            }
                            //комбобокс и контекстное меню
                            if (!cmp.contains(m.getPoint())
                                    && (cmp.getClass().getName().equals("aplclass.PopMenu")
                                    || cmp.getClass().getName().equals("javax.swing.plaf.synth.SynthComboPopup"))
                                    && (e.getID() == MouseEvent.MOUSE_PRESSED
                                    || e.getID() == MouseEvent.MOUSE_RELEASED)) {
                                cmp.setVisible(false);
                                dispatch = false;
                            }
                        }
                        //календарь
                        if (cmp.getClass().getName().equals("com.toedter.calendar.JCalendar")) {
                            if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                                if (!cmp.contains(m.getPoint())) {
                                    dispatch = false;
                                    cmp.setVisible(false);
                                    S.SETACTIVEFORM(V.CALENDAR_FORM);
                                    V.CALENDAR_FORM = null;
                                } else if (cmp.contains(m.getPoint())) {
                                    dispatch = true;
                                }
                            }
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
                while (cmp.isVisible()) {
                    P.class.wait();
                }
            }
        } catch (InterruptedException e) {
                        System.out.println("Ошибка модальности /n" + e.toString());

        }
//        } catch (NullPointerException ex) {
//        }

    }

    public static void SAVETOSYS() {

        Configs proper = new Configs(V.CONFIGS_DIRECTORY + V.fileSYS);

        proper.setProperty("$" + "CURRENT_PATH", V.CURRENT_PATH);
        proper.saveProperties("Переменные окружения");

    }

    public static void RESTOREFROMSYS() {
        Configs proper = new Configs(V.CONFIGS_DIRECTORY + V.fileSYS);
        if (!proper.fConfig.exists()) {
            return;
        }
        V.CURRENT_PATH = proper.getProperty("$" + "CURRENT_PATH", V.CURRENT_PATH);
    }

    public static void SAVECONNPROP(String serv_ip, String serv_port, String serv_name, String prox_name,
            String prox_pass, String prox_ip, String prox_port, boolean prox_flag, String serv_let) {

        Configs proper = new Configs(V.fileNameConServ);

        proper.setProperty("$" + "IP", serv_ip);
        proper.setProperty("$" + "PORT", serv_port);
        proper.setProperty("$" + "SERVICE", serv_name);
        proper.setProperty("$" + "SERVLET", serv_let);
        proper.setProperty("$" + "PROXYFLAG", String.valueOf(prox_flag));
        proper.setProperty("$" + "PROXYIP", prox_ip);
        proper.setProperty("$" + "PROXYPORT", prox_port);
        proper.setProperty("$" + "PROXYNAME", prox_name);
        proper.setProperty("$" + "PROXYPASS", Crypt.encrypt(prox_pass));
        proper.saveProperties("Connect Configuration");

    }

    public static void FIRSTSAVECONNPROP() { // сохранение параметров подключения к сервису, если файла еще не существует (деф)

        Configs proper = new Configs(V.fileNameConServ);

        proper.setProperty("$" + "IP", "service-bw.tomcat.vit.belwest.com");
        proper.setProperty("$" + "PORT", "80");
        proper.setProperty("$" + "SERVICE", "");
        proper.setProperty("$" + "SERVLET", "MainPostal");
        proper.setProperty("$" + "PROXYFLAG", "false");
        proper.setProperty("$" + "PROXYIP", "192.168.0.118");
        proper.setProperty("$" + "PROXYPORT", "8080");
        proper.setProperty("$" + "PROXYNAME", "sssss");
        proper.setProperty("$" + "PROXYPASS", Crypt.encrypt("ddddd"));
        proper.saveProperties("Connect Configuration");
        V.VARCONNSERV(); // ПРИСВОЕНИЕ ГЛОБАЛЬНЫМ ПЕРЕМЕННЫМ
    }

    public static void SAVE_ENV_PROP(String cipherFile, boolean fullScreen, String cipherPort, String cipherTime, boolean infoShow, String cipherMarka) {
//V.CIPHER_FILE_PATH,V.FULLSCREEN_FLAG,V.CIPHER_PORT,V.CIPHER_TIME,V.INFO_FLAG_ENV,V.CIPHER_MARKA
        Configs proper = new Configs(V.fileNameEnvConf);
        proper.setProperty("$" + "FULL_SCREEN", String.valueOf(fullScreen));
        proper.setProperty("$" + "CIPHER_DIR", cipherFile == null ? "DATA.TXT" : cipherFile);
        proper.setProperty("$" + "CIPHER_PORT", cipherPort == null ? "COM1" : cipherPort);
        proper.setProperty("$" + "CIPHER_TIME", cipherTime == null ? "" : cipherTime);
        proper.setProperty("$" + "INFO_SHOW", String.valueOf(infoShow));
        proper.setProperty("$" + "CIPHER_MARKA", cipherMarka);

        V.CIPHER_FILE_PATH = cipherFile;
        V.FULLSCREEN_FLAG = fullScreen;
        V.CIPHER_PORT = cipherPort;
        V.CIPHER_TIME = cipherTime;
        V.CIPHER_MARKA = cipherMarka;
        V.INFO_FLAG_ENV = infoShow;
        proper.saveProperties("Environment Configuration");

    }

    public static void FIRST_SAVE_ENV_PROP(Configs proper) {
        //Configs proper = new Configs(V.fileNameEnvConf);
        proper.setProperty("$" + "FULL_SCREEN", "true");
        proper.setProperty("$" + "CIPHER_DIR", "DATA.TXT");
        proper.setProperty("$" + "CIPHER_PORT", "COM1");
        proper.setProperty("$" + "CIPHER_TIME", "10");
        proper.setProperty("$" + "INFO_SHOW", "true");
        proper.setProperty("$" + "CIPHER_MARKA", markaNames[0]);
        proper.saveProperties("Environment Configuration");
    }

    public static void RESTORE_ENV_PROP() {
        Configs proper = new Configs(V.fileNameEnvConf);
        if (!proper.fConfig.exists()) {
            FIRST_SAVE_ENV_PROP(proper);
        }
        V.FULLSCREEN_FLAG = Boolean.valueOf(proper.getProperty("$" + "FULL_SCREEN"));
        V.CIPHER_FILE_PATH = proper.getProperty("$" + "CIPHER_DIR");
        V.CIPHER_PORT = proper.getProperty("$" + "CIPHER_PORT");
        V.CIPHER_TIME = proper.getProperty("$" + "CIPHER_TIME");
        V.INFO_FLAG_ENV = Boolean.valueOf(proper.getProperty("$" + "INFO_SHOW"));
        V.CIPHER_MARKA = proper.getProperty("$" + "CIPHER_MARKA", markaNames[0]);

    }

    public static void SET_CONFIGS_DIRECTORY(boolean flag) { // устанавливает нужный путь для конфигов проверя флан проектов, либо SHOP либо FIEM, используется в main проектов
        if (V.FLAG_KASSA) {
            V.CONFIGS_DIRECTORY = V.CONFIGS_DIRECTORY_Kassa;
            SET_CONFIGS_FILES();
            return;
        }

        if (V.ARM == 5) { //ВЦМПУ
            V.CONFIGS_DIRECTORY = V.CONFIGS_DIRECTORY_VCMPU;
            SET_CONFIGS_FILES();
            return;
        }

        if (V.ARM == 99) { //APPT
            V.CONFIGS_DIRECTORY = V.CONFIGS_DIRECTORY_APPT;
            SET_CONFIGS_FILES();
            return;
        }

        if (V.ARM == 1) { //ФИРМЕННАЯ ТОРГОВЛЯ
            V.CONFIGS_DIRECTORY = V.CONFIGS_DIRECTORY_FIRM;
            SET_CONFIGS_FILES();
        }
        if (V.ARM == 2 || V.FLAG_SHOP) { //ФИРМЕННЫЙ МАГАЗИН
            V.CONFIGS_DIRECTORY = V.CONFIGS_DIRECTORY_Shop;
            SET_CONFIGS_FILES();
        }

        if (V.ARM == 66) { //IMOBIS
            V.CONFIGS_DIRECTORY = V.CONFIGS_DIRECTORY_IMOBIS;
            SET_CONFIGS_FILES();
            return;
        }
        if (V.ARM == 67) { //IMOBIS
            V.CONFIGS_DIRECTORY = V.CONFIGS_DIRECTORY_IMOBIS_SERV;
            SET_CONFIGS_FILES();
            return;
        }
        if (V.ARM == 77) { //LUNCH
            V.CONFIGS_DIRECTORY = V.CONFIGS_DIRECTORY_LUNCH;
            SET_CONFIGS_FILES();
            return;
        }
    }

    public static void SET_CONFIGS_FILES() { //устанавливает(обновляет) имена каждого файла в соосветсвии с новой директорией
        V.fileNameTxtLog = CONFIGS_DIRECTORY + "txtHistory.config";  // путь для сохранеия файла конфигурации
        V.fileNameConServ = CONFIGS_DIRECTORY + "connect.config";  // путь для сохранеия файла конфигурации
        V.fileNameGridconf = CONFIGS_DIRECTORY + "grid_save.config";  // путь для сохранеия файла конфигурации
        V.fileNameScreenconf = CONFIGS_DIRECTORY + "screen.config";  // путь для сохранеия файла конфигурации
        V.fileNameConnMem = CONFIGS_DIRECTORY + "conn.mem";  // путь для сохранеия файла конфигурации
        V.fileNameFormConf = CONFIGS_DIRECTORY + "form.config";  // путь для сохранеия файла конфигурации
        V.fileNameEnvConf = CONFIGS_DIRECTORY + "envParam.config";
        V.fileNameTreeconf = CONFIGS_DIRECTORY + "tree.config";
        V.fileNameKassaSettingsConf = CONFIGS_DIRECTORY + "KassaSett.config";
        V.fileConnPropConf = CONFIGS_DIRECTORY + "ConnProp.config";
        V.fileFirmEnetPropConf = CONFIGS_DIRECTORY + "FirmEnetProp.config";
    }

    public static void WRITE_INFO(String mess) {
        if (V.INFO_TXT != null) {
            String time = FF.DATETIMES();
            V.INFO_TXT.WRITE_MESSAGE(time.substring(time.length() - 8, time.length()) + " - " + mess);
        }
    }

    public static String[] GET_COM_PORTS() {
        return SerialPortList.getPortNames();
    }

    /**
     * Формирование массива символьных переменных из запроса в таблице по
     * первому полю Данные начинаются с 1 элемента , массив плюс 1 элемент , 0 -
     * ой элемент пустой
     *
     * @param query -sql запрос
     * @return
     */
    public static String[] FILL_ARRAY_FROM_QUERY(String query) {
        Cursorr cursor = P.SQLEXECT(query, "TMP",false,0);
        if (cursor == null) {
            String[] arr = new String[1];
            arr[0] = "Неполучены данные";
            return arr;
        }
        String[] arr = new String[cursor.ROWCOUNT + 1];

        arr[0] = "";
        for (int i = 1; i < cursor.ROWCOUNT + 1; i++) {
            Object[] RECORD = (Object[]) cursor.rowList.get(i - 1);
            try {
                arr[i] = (String) RECORD[0];
            } catch (ClassCastException ex) {
                arr[i] = String.valueOf((int) RECORD[0]);
            }
        }
        return arr;
    }

    /**
     * Формирование массива символьных переменных из запроса в таблице по
     * первому полю
     *
     * @param query -sql запрос
     * @param fl - boolean , любое значение (чтобы колчество элементов было
     * равно количеству записей
     * @return
     */
    public static String[] FILL_ARRAY_FROM_QUERY(String query, boolean fl) {
        Cursorr cursor = P.SQLEXECT(query, "TMP");
        if (cursor == null) {
            String[] arr = new String[1];
            arr[0] = "Неполучены данные";
            return null;
        }
        String[] arr = new String[cursor.ROWCOUNT];

        for (int i = 0; i < cursor.ROWCOUNT; i++) {
            Object[] RECORD = (Object[]) cursor.rowList.get(i);
            try {
                arr[i] = (String) RECORD[0];
            } catch (ClassCastException ex) {
                try {
                    arr[i] = String.valueOf((int) RECORD[0]);
                } catch (ClassCastException exc) {
                    arr[i] = String.valueOf((double) RECORD[0]);
                }
            }
        }
        return arr;
    }
/**
 * Выбор файла  excel
 * @param title
 * @param fileName
 * @return 
 */
    public static String EXCEL_FILECHOOSE(String title, String fileName) {
        File selectedfile = null;
        boolean flag = false;

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);// выбрать тип диалога Open или Save
        //chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(title);
        chooser.setApproveButtonText("Сохранить");//выбрать название для кнопки согласия
        FileFilter filter3 = new FileNameExtensionFilter("Документ Excel", "xlsx","xls");
        //chooser.setFileFilter(filter);
        chooser.addChoosableFileFilter(filter3);
        chooser.setFileFilter(filter3);
        chooser.setCurrentDirectory(new File(V.CURRENT_PATH));
        chooser.setSelectedFile(new File(fileName));
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedfile = chooser.getSelectedFile();
            if (!selectedfile.exists()) {
                flag = true;
                V.CURRENT_PATH = chooser.getCurrentDirectory().toString();
            }
        } else {
            return "";
        }
        if (flag == true) {
            if (chooser.getFileFilter() == filter3) {
                return selectedfile + ".xlsx";
            }
        } else {
            V.CURRENT_PATH = chooser.getCurrentDirectory().toString();
            return selectedfile.toString();
        }
        return "";
    }

    /**
     * Выбор файла из списка
     *
     * @param title - заголовок списка
     * @param fname - файл по умолчанию
     * @param fexp - маска расширений
     * @param path - путь по умолчанию
     * @return
     */
    public static String GETFILE(String title, String fname, String fexp, String path) {

        JFileChooser fileopen = new JFileChooser();
        if (FF.EMPTY(path)) {
            path = V.CURRENT_PATH;
        }
        int x=V._SCREEN.getX()+100;
        int y=V._SCREEN.getY()+100;
        
        fileopen.setLocation(x, y);// не работает можно в showDialog задать формув которой вызывается, whenever you show the chooser, the internal createDialog method will set the location to center of parent.
        fileopen.setCurrentDirectory(new File(path));
        fileopen.setSelectedFile(new File(fname));
        fileopen.setDialogTitle(title);
        //if (!"".equals(fname) && !"".equals(fexp)) {
        if ( !"".equals(fexp)) {
            FileFilter filter = new FileNameExtensionFilter("File", fexp);    //НАЗВАНИЕ ТИПА, РАСШИРЕНИЕ
            fileopen.setFileFilter(filter);
        }
        fileopen.setApproveButtonText("Выбрать");
        int ret = fileopen.showDialog(null, "Выбрать");
        if (ret != JFileChooser.APPROVE_OPTION) {
            return "";
        }
        File file = fileopen.getSelectedFile();
        V.CURRENT_PATH = fileopen.getCurrentDirectory().toString();
        return file.getPath();
    }

    public static String FILECHOOSE(String title) {
        File selectedfile = null;
        boolean flag = false;

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);// выбрать тип диалога Open или Save
        //chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(title);
        chooser.setApproveButtonText("Сохранить");//выбрать название для кнопки согласия
        FileFilter filter1 = new FileNameExtensionFilter("Текстовый файл", "txt");
        FileFilter filter2 = new FileNameExtensionFilter("Документ Word", "doc");
        FileFilter filter3 = new FileNameExtensionFilter("Документ Excel 2007 xlsx", "xlsx");
        FileFilter filter4 = new FileNameExtensionFilter("Документ Excel 2003 xls", "xls");
        //chooser.setFileFilter(filter);
        chooser.addChoosableFileFilter(filter1);
        chooser.addChoosableFileFilter(filter2);
        chooser.addChoosableFileFilter(filter3);
        chooser.addChoosableFileFilter(filter4);
        chooser.setFileFilter(filter1);
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedfile = chooser.getSelectedFile();
            if (!selectedfile.exists()) {
                flag = true;
                V.CURRENT_PATH = chooser.getCurrentDirectory().toString();
            }
        } else {
            return "";
        }
        if (flag == true) {
            if (chooser.getFileFilter() == filter1) {
                return selectedfile + ".txt";
            }
            if (chooser.getFileFilter() == filter2) {
                return selectedfile + ".doc";
            }
            if (chooser.getFileFilter() == filter3) {
                return selectedfile + ".xlsx";
            }
            if (chooser.getFileFilter() == filter4) {
                return selectedfile + ".xls";
            }
        } else {
            return selectedfile.toString();
        }
        return "";
    }

    /**
     * Диалог выбора директории
     *
     * @param PATH путь по умолчанию если пустой "" - то текущий V.CURRENT_PATH
     * @return возвращает NULL если отменили
     */
    public static String GETDIR(String PATH) {
        return GETDIR(PATH, null);
    }

    /**
     * Диалог выбора директории
     *
     * @param PATH путь по умолчанию если пустой "" - то текущий V.CURRENT_PATH
     * @param title заголовок окна выбора, по умолчанию "Выбрать"
     * @return возвращает NULL если отменили
     */
    public static String GETDIR(String PATH, String title) {
        if (FF.EMPTY(PATH)) {
            PATH = V.CURRENT_PATH;
        }
        JFileChooser fileopen = new JFileChooser();
        fileopen.setCurrentDirectory(new File(PATH));
        fileopen.setDialogTitle(title == null || title.isEmpty() ? "Выбрать" : title);
        fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileopen.setAcceptAllFileFilterUsed(false);
        fileopen.setApproveButtonText("Выбрать");
        int ret = fileopen.showDialog(null, "Выбрать");
        if (ret != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File file = fileopen.getSelectedFile();
        V.CURRENT_PATH = file.getPath();
        return V.CURRENT_PATH;
    }

    public static String DIRCHOOSE(String title) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);// выбрать тип диалога Open или Save
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        //chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(title);
        chooser.setApproveButtonText("Выбор");//выбрать название для кнопки согласия
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile.isDirectory()) {
                V.CURRENT_PATH = chooser.getCurrentDirectory().toString();
                return selectedFile.getAbsolutePath();

            }
        }
        return "";
    }

    public static String GET_HOST_PC() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            return "";
        }
    }

    public static String GET_HOST_NAME() {
        return System.getProperty("user.name");
    }

    public static void SETTINGS_CHECK() {
        if (!V.VARCONN()) {
            P.DOFORM("conn1");
//        } else if (V.CONN_USER.equals("") || V.CONN_PASS.equals("") || V.CONN_SERVER.equals("") || V.CONN_BASA.equals("")) {
        } else if (V.CONN_USER.equals("")) {

            P.MESSERR("Не настроено соединение с данными. Выполните настройку соединения");
            P.DOFORM("conn1");
        }
        if (V.VARCONNSERV()
                && (V.CONN_SERV_IP.equals("") || V.CONN_SERV_PORT.equals("")
                || (V.CONN_PROX_FLAG
                && (V.CONN_PROX_IP.equals("") || V.CONN_PROX_PORT.equals("") || V.CONN_PROX_NAME.equals("") || V.CONN_PROX_PASS.equals(""))))) {
            P.MESSERR("Не настроено соединение с сервисом. Выполните настройку соединения");
            P.DOFORM("ServCon");
        }
    }

    /**
     * Сцепление двух и более строк
     *
     * @param STR
     * @return
     */
    public static String STRP(String... STR) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < STR.length; i++) {
            sb.append(STR[i]);
        }
        return sb.toString();
    }

    /**
     * Выдача пераой или второй строки взависимости от выполнения условия
     *
     * @param tf
     * @param STR1
     * @param STR2
     * @return
     */
    public static String IIF(boolean tf, String STR1, String STR2) {
        if (tf) {
            return STR1;
        } else {
            return STR2;
        }

    }
//    

    //**************************************************************************
    //  уникальный номер кассы
    //
    //**************************************************************************
    public static String getUniqueKassNo() {
        InetAddress addr;
        String hostname = "";
        try {
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
        }
        return V.KASSA_ID + "#" + hostname;
    }

    //**************************************************************************
    //  возвращает наименование месяца по номеру в нужном падеже
    //
    //**************************************************************************
    public static String getMonthName(int monthNo) {
        return getMonthName(monthNo, V.PAD_I);
    }

    public static String getMonthName(int monthNo, int pad) {
        switch (monthNo) {
            case 1:
                switch (pad) {
                    case V.PAD_I:
                        return "январь";
                    case V.PAD_R:
                        return "января";
                    case V.PAD_D:
                        return "январю";
                }
                break;
            case 2:
                switch (pad) {
                    case V.PAD_I:
                        return "февраль";
                    case V.PAD_R:
                        return "февраля";
                    case V.PAD_D:
                        return "февралю";
                }
                break;
            case 3:
                switch (pad) {
                    case V.PAD_I:
                        return "март";
                    case V.PAD_R:
                        return "марта";
                    case V.PAD_D:
                        return "марту";
                }
                break;
            case 4:
                switch (pad) {
                    case V.PAD_I:
                        return "апрель";
                    case V.PAD_R:
                        return "апреля";
                    case V.PAD_D:
                        return "апрелю";
                }
                break;
            case 5:
                switch (pad) {
                    case V.PAD_I:
                        return "май";
                    case V.PAD_R:
                        return "мая";
                    case V.PAD_D:
                        return "маю";
                }
                break;
            case 6:
                switch (pad) {
                    case V.PAD_I:
                        return "июнь";
                    case V.PAD_R:
                        return "июня";
                    case V.PAD_D:
                        return "июню";
                }
                break;
            case 7:
                switch (pad) {
                    case V.PAD_I:
                        return "июль";
                    case V.PAD_R:
                        return "июля";
                    case V.PAD_D:
                        return "июлю";
                }
                break;
            case 8:
                switch (pad) {
                    case V.PAD_I:
                        return "август";
                    case V.PAD_R:
                        return "августа";
                    case V.PAD_D:
                        return "августу";
                }
                break;
            case 9:
                switch (pad) {
                    case V.PAD_I:
                        return "сентябрь";
                    case V.PAD_R:
                        return "сентября";
                    case V.PAD_D:
                        return "сентябрю";
                }
                break;
            case 10:
                switch (pad) {
                    case V.PAD_I:
                        return "октябрь";
                    case V.PAD_R:
                        return "октября";
                    case V.PAD_D:
                        return "октабрю";
                }
                break;
            case 11:
                switch (pad) {
                    case V.PAD_I:
                        return "ноябрь";
                    case V.PAD_R:
                        return "ноября";
                    case V.PAD_D:
                        return "ноябрю";
                }
                break;
            case 12:
                switch (pad) {
                    case V.PAD_I:
                        return "декабрь";
                    case V.PAD_R:
                        return "декабря";
                    case V.PAD_D:
                        return "декабрю";
                }
                break;
        }

        return "неизвестен";
    }

    //**************************************************************************
    //  округляет число (value) до указанного числа  (roundValue)
    // если нужно округлять дробные числа , то только до разрядности.
    // параметр округления minMaxRound:  1  в большую сторону, -1 в меньшую, 0  по правилам округления
    // например ROUND(117.123456, 10, 0) - результат: 120
    // например ROUND(117.123456, 3, 0) - результат: 117
    // например ROUND(117.123456, 0.001, 0) - результат: 117.123
    // например ROUND(117.123456, 50, 0) - результат: 100
    // например ROUND(117.123456, 50, 1) - результат: 150
    //**************************************************************************    
    public static double ROUND_DIG(double value, double roundValue, int minMaxRound) {

        if (roundValue == 1) {
            return 0;
        }

        double division = value / roundValue;
        int intP = (int) Math.floor(division);
        double modP = division - intP;

        if (roundValue < 1) { // если округляем до дробного
            //System.out.println(new BigDecimal(value).setScale(2, RoundingMode.DOWN).doubleValue());
            int len = String.valueOf(roundValue).substring(String.valueOf(roundValue).indexOf(".") + 1).length();
            if (minMaxRound == 1) {
                return new BigDecimal(value).setScale(len, RoundingMode.UP).doubleValue();
            }
            if (minMaxRound == -1) {
                return new BigDecimal(value).setScale(len, RoundingMode.DOWN).doubleValue();
            }
            if (minMaxRound == 0) {
                return new BigDecimal(value).setScale(len, RoundingMode.HALF_UP).doubleValue();
            }
        }

        // округление до целых
        modP = new BigDecimal(modP).setScale(1, RoundingMode.DOWN).doubleValue();
        modP = Integer.parseInt(String.valueOf(modP).substring(String.valueOf(modP).indexOf(".") + 1));

        if (value < roundValue && minMaxRound == 1) {
            return roundValue;
        }

        if (minMaxRound == -1 || modP == 0) {
            return intP * roundValue;
        }

        if (minMaxRound == 1) {
            return intP * roundValue + roundValue;
        }

        if (minMaxRound == 0 && modP >= 5) {
            return intP * roundValue + roundValue;
        }

        if (minMaxRound == 0 && modP < 5) {
            return intP * roundValue;
        }

        return 0;
    }

    public static String SUM_PROP(double chislo) {
        return SUM_PROP(chislo, 0);
    }

    /**
     * Число прописью
     *
     * @param chislo - число
     * @param vid - вид вывода 0 - Сто пять руб. 01 коп. 1- Сто пять 2 - 01
     * @return
     */
    public static String SUM_PROP(double chislo, int vid) {
        String str = "", rub = "", kop = "", m = "";
        if (chislo >= 1E+15 || chislo < 0) {
            return "ошибка числа";
        }
        String RUB = V.RUB;
        String KOP = V.KOP;
        if (vid != 0) {
            RUB = "";
        }

        if (chislo == 0 && vid == 0) {
            return "0 " + RUB + " 00 " + KOP;
        }
        String sot[] = {"", "сто ", "двести ", "триста ", "четыреста ", "пятьсот ", "шестьсот ", "семьсот ", "восемьсот ", "девятьсот "};
        String nadc[] = {"", "один ", "два ", "три ", "четыре ", "пять ", "шесть ", "семь ", "восемь ", "девять ", "десять ", "одиннадцать ", "двенадцать ", "тринадцать ", "четырнадцать ", "пятнадцать ", "шестнадцать ", "семнадцать ", "восемнадцать ", "девятнадцать "};
        String nadcw[] = {"", "одна ", "две "};
        String des[] = {"", "", "двадцать ", "тридцать ", "сорок ", "пятьдесят ", "шестьдесят ", "семьдесят ", "восемьдесят ", "девяносто "};
//        String razr1[] = {"рубль ","тясяча ", "миллион ", "миллиард ","триллион "};
        //      String razr2[] = {"рубля ","тясячи ", "миллиона ", "миллиарда ","триллиона "};
        //    String razr5[] = {"рублей ","тясяч ", "миллионов ", "миллиардов ","триллионов "};

        String razr1[] = {"руб.", "тясяча ", "миллион ", "миллиард ", "триллион "};
        String razr2[] = {"руб.", "тясячи ", "миллиона ", "миллиарда ", "триллиона "};
        String razr5[] = {"руб.", "тясяч ", "миллионов ", "миллиардов ", "триллионов "};

        str = FF.STR(chislo, 18, 2); //число в строку
        str = str.replaceAll(",", ".");
        if (FF.AT(".", str) > 0) {
            rub = FF.SUBSTR(str, 1, FF.AT(".", str) - 1); //целое 
            kop = FF.SUBSTR(str, FF.AT(".", str) + 1); // дробное
            if (kop.length() == 1) {
                kop = kop + "0";
            }
            str = rub;
        } else {
            rub = str;
            kop = "";
        }
        if ((int) chislo == 0) {
            m = "ноль ";
        } //если целое ноль

        int len = FF.LEN(str);  //длина числа
        if (len % 3 > 0) { //если есть остток от деления т.е. не полные три разряда
            str = FF.LPAD(str, (len / 3 + 1) * 3).replaceAll(" ", "0"); //добавить пробелы до кратности трем и заменить нулями
            len = FF.LEN(str);//новая длина
        }
        String s = "", sum1 = "", sum2 = "", sum = "", name = "";
        int d = 0, j = 0;
        while (!FF.EMPTY(str)) {
            s = "";
            sum1 = "";
            sum2 = "";
            name = "";
            s = FF.SUBSTR(str, len - 2, 3); // выделяем последнюю тройку
            d = FF.VAL(FF.SUBSTR(s, 1, 1));
            if (d > 0) {
                sum1 = sot[d];
            }
            d = FF.VAL(FF.SUBSTR(s, 2));
            if (d > 0 && d < 20) {
                sum2 = (j == 1 && d <= 2 ? nadcw[d] : nadc[d]);

            }
            if (d > 0 && d >= 20) {
                sum2 = des[d / 10] + (j == 1 && d % 10 <= 2 ? nadcw[d % 10] : nadc[d % 10]);
            }
            name = razr5[j];
            if (d % 10 == 1) {
                name = razr1[j];
            }
            if (d % 10 > 1 && d % 10 < 5) {
                name = razr2[j];
            }

            sum = sum1 + sum2 + name + sum;

            len = len - 3; // отрезаем три последних
            str = FF.SUBSTR(str, 1, len);
            j++;
        }

        sum = FF.SUBSTR(sum, 1, 1).toUpperCase() + FF.SUBSTR(sum, 2);
        //int dec =(int)(chislo-(int)chislo)*FF.LEN(kop);
        //int dec =(int)((chislo-(int)chislo)*Math.pow(10, FF.LEN(kop)));    
        //int dec =(int)Math.round((chislo-(int)chislo)*100);
        if (FF.VAL(kop) == 0) {
            sum = sum + " 00 " + KOP;
        }
        if (FF.VAL(kop) > 0 && vid == 0) {
            sum = sum + " " + kop + " " + KOP;
        }

        return sum;
    }

    /**
     * Сумма прописью
     *
     * @param sum
     * @return
     */
    public static String SM_PROP(double sum) {
        return SM_PROP(sum, 0);
    }

    public static String SM_PROP(double sum, int pr) {
        String RUB = V.RUB;
        String KOP = V.KOP;

        if (sum == 0 && pr == 0) {
            return "0 " + RUB + " 00 " + KOP;
        }
        if (sum == 0 && pr == 1) {
            return "Ноль ";
        }
        double mld = 0;
        String smld = "";
        if (sum > 999999999) {
            mld = sum - 999999999;
            sum = sum - 1000000000 * mld;
            smld = SM_PROP(mld, 1) + " миллиард. ";
        }
        String str = FF.STR(sum);
        String sum_prop = FF.STR(sum);
        sum_prop = DigitsToTextConverter.digits2text(str);

        if (pr == 1) { //не деньги
            RUB = "";
            KOP = "";
        }
        sum_prop = sum_prop.replaceAll("рублей", RUB);
        sum_prop = sum_prop.replaceAll("рубля", RUB);
        sum_prop = sum_prop.replaceAll("рубль", RUB);
        sum_prop = sum_prop.replaceAll("рублей", RUB);
        sum_prop = sum_prop.replaceAll("Рубль", RUB);
        sum_prop = sum_prop.replaceAll("коп.", KOP);
        if ("".equals(KOP)) {
            sum_prop = sum_prop.replaceAll(" 00 ", KOP);
        }

        if (!FF.EMPTY(smld)) {
            sum_prop = smld + sum_prop.toLowerCase();
        }
        return sum_prop;
    }

    //**************************************************************************
    // По артикулу возвращает относится ли он к сопутсвующей группе товаров
    //
    //**************************************************************************    
    public static boolean isAttendProduct(String art) {
        P.SQLEXECT("select mtart from s_art where mtart = 'ZHW3' and art = '" + art + "'", "curTmpIsAP");
        if (A.RECCOUNT("curTmpIsAP") == 0) {
            A.CLOSE("curTmpIsAP");
            return false;
        }
        A.CLOSE("curTmpIsAP");
        return true;
    }

    public static boolean isBagProduct(String art) {
        String curAlias = A.ALIAS();
        P.SQLEXECT("select BAGGINS('" + art + "') FLAG from dual", "baggins");
        A.SELECT("baggins");
        if (A.GETVALS("FLAG").equals("F")) {
            A.CLOSE("baggins");
            if (!curAlias.equals("") && curAlias != null) {
                A.SELECT(curAlias);
            }
            return false;
        }
        A.CLOSE("baggins");
        if (!curAlias.equals("") && curAlias != null) {
            A.SELECT(curAlias);
        }
        return true;
    }

    public static void SESSION_CLOSE() {
        if ("2".equals(V.CONN_DRIVER)) { //если sql server нужно писать 
            return;
        }

        if (V.FLAG_KASSA) {
            Iterator entries = V.MAPFORMS.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) entries.next();
                Formr form = (Formr) thisEntry.getValue();
                if (form.getName().equals("SaleForm") || form.getName().equals("SaleForm_BY")) {
                    P.SQLUPDATE("delete from D_KASSA_CUR where KASS_ID = '" + P.getUniqueKassNo() + "'");
                    P.SQLUPDATE("delete from D_KASSA_DISC_CUR where KASS_ID = '" + P.getUniqueKassNo() + "'");
                    P.SQLUPDATE("delete from D_RETURN_CUR where KASS_ID = '" + P.getUniqueKassNo() + "'");

                    V.TABLO.writeMessage("Добро пожаловать!");
                }
            }
        }
        try {
            Timestamp date_s = (Timestamp) ((Object[]) P.SQLEXECT("select date_s from history_session where sessionid = " + V.SESSIONID).rowList.get(0))[0];
            String date_e=" ";
            Integer time=0;
           if ("1".equals(V.CONN_DRIVER)) { //если ORACLE нужно писать 
             date_e = (String) ((Object[]) P.SQLEXECT("select to_char(systimestamp, 'dd.mm.yyyy HH24:mi:ss') from dual").rowList.get(0))[0];
             time = (Integer) ((Object[]) P.SQLEXECT("select ceil((to_date('" + date_e + "', 'dd.mm.yyyy HH24:MI:SS') - to_date('" + V.ddmmyyyyhhmm.format(date_s) + "', 'dd.mm.yyyy HH24:MI:SS'))*24*60) from dual").rowList.get(0))[0];
           }
           if ("3".equals(V.CONN_DRIVER)) { //postgre
             date_e = (String) ((Object[]) P.SQLEXECT("select to_char(CURRENT_TIMESTAMP, 'dd.mm.yyyy HH24:mi:ss') ").rowList.get(0))[0];
             time = (Integer) ((Object[]) P.SQLEXECT("select cast (extract(epoch from ((to_TIMESTAMP('" + date_e + "', 'dd.mm.yyyy HH24:MI:SS')-to_TIMESTAMP('" + V.ddmmyyyyhhmm.format(date_s) + "', 'dd.mm.yyyy HH24:MI:SS')) ) ) as integer)").rowList.get(0))[0];             
           }
           
            P.SQLUPDATE("update history_session set "
                    + "date_e = to_timestamp('" + date_e + "','dd.mm.rrrr HH24:mi:ss'), "
                    + "session_time = " + (time <= 0 ? 1 : time) + " where sessionid = " + V.SESSIONID);
        } catch (IndexOutOfBoundsException | NullPointerException ex) {
        }
        if (V.CUR_PASS_OFFLINE.equals("offline") || V.CUR_PASS_OFFLINE.equals("offline_admin")) {
            Object[][] param = new Object[1][2];
            param[0][0] = OracleTypes.VARCHAR;
            param[0][1] = V.OFFLINE_OUT_RESULT;
            P.ALERT("Закрытие базы оффлайн...");
            P.SYSTEM_FIRM_CONNECT(param, "LKA_DROP_DATABASE");
            P.ALERT("");
        }
        if (V.CUR_PASS_BYR_BASE.contains("byr")) {
            Post post = Post.getInstance();
//            post.setParam("Query", "040&ShopId="+V.CUR_BYR_SHOPID+"&param=" + V.CUR_BYR_PARAM);
            post.SendDataWithResult_2("Обработка запроса...", new String[][]{{"Query", "040"}, {"ShopId", V.CUR_BYR_SHOPID}, {"param", V.CUR_BYR_PARAM}});
        }
    }

    public static void SYSTEM_EXIT() {
        //21112017 КАА запрет на закрытие программы, если открыта форма с обменом
       try {
        if (!exchangeCloseCheck()) {
            V._SCREEN.setDefaultCloseOperation(3);
            P.ALERT("Идет обмен.....");
            System.out.println("Идет обмен ...." );
            return;
        }
            } catch (Exception ex) {
                System.out.println("Ошибка на exchangeCloseCheck() "+ ex.toString());
            }
       try {
        //if (V.FALSETRUE && V.ARM != 5 && V.ARM != 99) {
        if (V.FALSETRUE  && V.ARM != 99) {
            P.SESSION_CLOSE();
        }
        V._SCREEN.DESTROY();
            } catch (Exception ex) {
                System.out.println("Ошибка на SESSION_CLOSE() DESTROY() "+ ex.toString() );
            }
       try {
       System.exit(0);
            } catch (Exception ex) {
                System.out.println("Ошибка на System.exit(0) "+ ex.toString() );
                Runtime.getRuntime().exit(0)    ;    
            }
       
    }

    public static void SYSTEM_FIRM_CONNECT(Object[][] param, String PROC) {
        V.CONN1 = P.SQLCONNECT("pos.vit.belwest.com", "orcl", "system", "60p46K136", "1521");

        CallableStatement cst;

        String questions = "";
        StringBuilder sb;
        try {
            sb = new StringBuilder(questions);

            for (int paramNo = 0; paramNo < param.length; paramNo++) { // формирование строки параметров
                sb.append("?,");
            }

            questions = sb.toString().substring(0, sb.toString().length() - 1);
            cst = V.CONN1.prepareCall("BEGIN " + PROC + "(" + questions + "); END;");

            for (int i = 0; i < param.length; i++) {
                switch ((Integer) param[i][0]) {
                    case OracleTypes.INTEGER:
                        cst.setInt(i + 1, (Integer) param[i][1]);
                        break;
                    case OracleTypes.VARCHAR:
                        cst.setString(i + 1, param[i][1].toString());
                        break;
                    case OracleTypes.NUMBER:
                        cst.setDouble(i + 1, Double.parseDouble(param[i][1].toString()));
                        break;
                    case OracleTypes.TIMESTAMP:
                        cst.setTimestamp(i + 1, (java.sql.Timestamp) param[i][1]);
                        break;
                    case OracleTypes.DATE:
                        cst.setDate(i + 1, (java.sql.Date) param[i][1]);
                        break;
                }
            }
            cst.execute();

            cst.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        try {
            V.CONN1.close();
            V.SQL = null;
        } catch (SQLException ex) {
            Logger.getLogger(conn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //сохранение в конфиг параметров удачного подключения к базе оракла
    public static void UPDATE_CONNECTIONS_CONF(String ip, String login, String pass, String port, String dbName, String connName) {
        Configs proper = new Configs(V.fileConnPropConf);
        int num = 0, count = 0;

        for (int ind = 0; ind < proper.size() / 7; ind++) {
            if (proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind) != null) {
                if (proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind).equals(connName)) {
                    num = ind;
                    count++;
                }
            }
            if (proper.getProperty("CONN" + "$" + "LASTCONN" + "$" + ind) != null) {
                proper.setProperty("CONN" + "$" + "LASTCONN" + "$" + ind, "false");
            }
        }

        if (count == 0) {
            num = proper.size() / 7;
        }

        if (proper.size() == 0) {
            num = 0;
        }

        proper.setProperty("CONN" + "$" + "IP" + "$" + num, ip);
        proper.setProperty("CONN" + "$" + "LOGIN" + "$" + num, login);
        proper.setProperty("CONN" + "$" + "PASS" + "$" + num, pass);
        proper.setProperty("CONN" + "$" + "PORT" + "$" + num, port);
        proper.setProperty("CONN" + "$" + "DBNAME" + "$" + num, dbName);
        proper.setProperty("CONN" + "$" + "LASTCONN" + "$" + num, "true");
        proper.setProperty("CONN" + "$" + "SHOPID" + "$" + num, connName);
        proper.saveProperties("ConnProp Configuration");
    }

    //удаление из конфига коннекта
    public static void DELETE_CONNECTION(String shopid) {
        Configs proper = new Configs(V.fileConnPropConf);

        int k = 0;
        int size = proper.size() / 7;
        for (int ind = 0; ind < size; ind++) {
            if (proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind) != null) {
                if (proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind).equals(shopid)) {
                    k = ind;
                    break;
                }
            }
        }
        for (int ind = k; ind < size; ind++) {
            if (ind == size - 1) {
                proper.remove("CONN" + "$" + "SHOPID" + "$" + ind);
                proper.remove("CONN" + "$" + "IP" + "$" + ind);
                proper.remove("CONN" + "$" + "LOGIN" + "$" + ind);
                proper.remove("CONN" + "$" + "PASS" + "$" + ind);
                proper.remove("CONN" + "$" + "PORT" + "$" + ind);
                proper.remove("CONN" + "$" + "DBNAME" + "$" + ind);
                proper.remove("CONN" + "$" + "LASTCONN" + "$" + ind);
            } else {
                int ind_2 = ind + 1;
                proper.setProperty("CONN" + "$" + "IP" + "$" + ind, proper.getProperty("CONN" + "$" + "IP" + "$" + ind_2));
                proper.setProperty("CONN" + "$" + "LOGIN" + "$" + ind, proper.getProperty("CONN" + "$" + "LOGIN" + "$" + ind_2));
                proper.setProperty("CONN" + "$" + "PASS" + "$" + ind, proper.getProperty("CONN" + "$" + "PASS" + "$" + ind_2));
                proper.setProperty("CONN" + "$" + "PORT" + "$" + ind, proper.getProperty("CONN" + "$" + "PORT" + "$" + ind_2));
                proper.setProperty("CONN" + "$" + "DBNAME" + "$" + ind, proper.getProperty("CONN" + "$" + "DBNAME" + "$" + ind_2));
                proper.setProperty("CONN" + "$" + "LASTCONN" + "$" + ind, proper.getProperty("CONN" + "$" + "LASTCONN" + "$" + ind_2));
                proper.setProperty("CONN" + "$" + "SHOPID" + "$" + ind, proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind_2));
            }
        }

        proper.saveProperties("ConnProp Configuration");
    }

    //сохранение в конфиг коннектов выбранного SHOPID
    public static void UPDATE_CONNECTIONS_LASTCONN(String shopid) {
        Configs proper = new Configs(V.fileConnPropConf);

        int k = 0;

        for (int ind = 0; ind < proper.size() / 7; ind++) {
            if (proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind) != null) {
                if (proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind).equals(shopid)) {
                    proper.setProperty("CONN" + "$" + "LASTCONN" + "$" + ind, "true");
                } else {
                    proper.setProperty("CONN" + "$" + "LASTCONN" + "$" + ind, "false");
                }
            }
        }

        proper.saveProperties("ConnProp Configuration");
    }

    //загрузка из кофига коннектов списка SHOPID
    public static String[] LOAD_CONNECTIONS_CONF_SHOPID() {
        Configs proper = new Configs(V.fileConnPropConf);
        String[] arr = new String[proper.size() / 7];

        for (int ind = 0; ind < proper.size() / 7; ind++) {
            if (proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind) != null) {
                arr[ind] = proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind);
            }
        }

        return arr;
    }

    //загрузка из кофига коннектов последнего удачного SHOPID
    public static String LOAD_CONNECTIONS_CONF_SHOPID_LAST() {
        Configs proper = new Configs(V.fileConnPropConf);
        String shopidLast = "";

        for (int ind = 0; ind < proper.size() / 7; ind++) {
            if (proper.getProperty("CONN" + "$" + "LASTCONN" + "$" + ind) != null) {
                shopidLast = proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind);
                if (proper.getProperty("CONN" + "$" + "LASTCONN" + "$" + ind).equals("true")) {
                    break;
                }
            }
        }

        return shopidLast;
    }

    //загрузка из конфига коннектов данных по SHOPID
    public static String[] LOAD_CONNECTIONS_CONF_DATA(String shopid) {
        Configs proper = new Configs(V.fileConnPropConf);
        String[] arr = new String[6];

        for (int ind = 0; ind < proper.size() / 7; ind++) {
            if (proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind) != null) {
                if (proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind).equals(shopid)) {
                    arr[0] = proper.getProperty("CONN" + "$" + "IP" + "$" + ind);
                    arr[1] = proper.getProperty("CONN" + "$" + "LOGIN" + "$" + ind);
                    arr[2] = proper.getProperty("CONN" + "$" + "PASS" + "$" + ind);
                    arr[3] = proper.getProperty("CONN" + "$" + "PORT" + "$" + ind);
                    arr[4] = proper.getProperty("CONN" + "$" + "DBNAME" + "$" + ind);
                    arr[5] = proper.getProperty("CONN" + "$" + "SHOPID" + "$" + ind);
                    break;
                }
            }
        }

        return arr;
    }

    /**
     * Получает из файла настроек програмы POS_OPEN_SHOP имя пользователя
     *
     * @return имя пользователя программы POS_OPEN_SHOP
     */
    public static String loadPosOpenShopUser() {
        Configs props = new Configs(V.posOpenShopConfig);
        return (String) props.get("$ORP$UserName");
    }

    //меню со всеми активными формами
    public static void ACTIVEFROMS_MENU() {
        int n = 0;

        Iterator entries = V.MAPFORMS.entrySet().iterator();
        ArrayList<String> arr = new ArrayList<>();
        ArrayList<Formr> arrF = new ArrayList<>();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            Formr form = (Formr) thisEntry.getValue();
            arr.add(form.getTitle());
            arrF.add(form);
        }

        String[] strm = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            strm[i] = arr.get(i);
        }
        n = P.MENU(strm, V.TOOLBAR.PRINT);
        if (n == 0) {
            return;
        }
        for (int i = 1; i < arr.size() + 1; i++) {
            if (n == i) {
                S.SETACTIVEFORM(arrF.get(i - 1));
            }
        }
    }

    public static boolean DOVIEW(String alias, String header, Object... columns) {
        return DOVIEW(0, "Выбрать", "Отмена", alias, header, 0, 0, columns);
    }

    public static boolean DOVIEW(String alias, String header, Integer width, Integer height, Object... columns) {
        return DOVIEW(0, "Выбрать", "Отмена", alias, header, width, height, columns);
    }

    /**
     *
     * Универсальная форма просиотра и выбора строки данных с подтверждением или
     * отменой
     *
     * @param coledit номер колонки для корректуры
     *
     * @param ok - название кнопки подтверждения
     * @param esc - название кнопки отмены
     * @param alias - имя алиаса данных
     * @param header - заголовок формы
     * @param width ширина формы если 0 то по сумме ширины полей
     * @param height высота формы если 0 то по умалчанию
     * @param columns список полей название,имя,размер
     * @return
     */
    public static boolean DOVIEW(int coledit, String ok, String esc, String alias, String header, Integer width, Integer height, Object... columns) {
        //public static boolean DOVIEW(String alias, String header, Integer width, Integer height,  Object... columns) {    
        V.PARAMTO = new String[columns.length + 7];
        V.PARAMTO[0] = alias; //АЛИАС
        V.PARAMTO[1] = header; //ЗАГОЛОВОК ФОРМЫ
        V.PARAMTO[2] = String.valueOf(width);//ШИРИНА
        V.PARAMTO[3] = String.valueOf(height);//ВЫСОТА
        V.PARAMTO[4] = ok; //кнопка ok
        V.PARAMTO[5] = esc; //кнопка esc
        V.PARAMTO[6] = String.valueOf(coledit);//НОМЕР КОЛОНКИ

        for (int i = 0; i < columns.length; i++) { // НАЗВАНИЯ ПОЛЕЙ ,ИМЕНА ПОЛЕЙ, ШИРИНА КОЛОНОК
            V.PARAMTO[i + 7] = String.valueOf(columns[i]);
        }

        Formr form = P.DOFORM("ViewForm", 3);
        V.ViewForm_STAT_DBCLICK = true;
        form = null;

        if (V.PARAMOT[0].equals("T")) {
            return true;
        }
        return false;
    }

    /**
     * Универсальная форма корректуры полей
     *
     * @param formName имя создаваемой формы
     * @param field массив полей
     * @return
     */
    public static boolean DOCORRECT(String formName, Object... field) {
        return DOCORRECT(V.ACTIVEFORM, V.ACTIVEFORM.getName() + "_CORR", formName, field);
    }

    /**
     * Универсальная форма корректуры полей
     *
     * @param form - вызывающая форма
     * @param name - имя создаваемой формы
     * @param formCaption - шапка создаваемой формы
     * @param field - массив полей
     * @return
     */
    public static boolean DOCORRECT(Formr form, String name, String formCaption, Object... field) {
        V.PARAMTO = new String[field.length + 1];
        for (int i = 0; i < field.length; i++) {
            V.PARAMTO[i] = String.valueOf(field[i]);
        }
        V.FormCorrName = name;
        V.FormParent = form;
        V.PARAMTO[field.length] = formCaption;
        P.DOFORM("CorrectForm");
        V.FormCorrName = ""; // 25.09.17 rkv 
//        V.FormCorr.SETMODAL(1);    //1-Модальная форма 0-не модальная
        if (V.PARAMOT[0].equals("T")) {
            return true;
        }
        return false;
    }

  
    /**
     * Универсальная форма корректуры полей
     *
     * @param form - вызывающая форма
     * @param name - имя создаваемой формы
     * @param formCaption - шапка создаваемой формы
     * @param field - массив полей
     * @return
     */
    public static boolean DOCORRECTPF(Formr form, String name, String formCaption, Object... field) {
        V.PARAMTO = new String[field.length + 1];
        for (int i = 0; i < field.length; i++) {
            V.PARAMTO[i] = String.valueOf(field[i]);
        }
        V.FormCorrName = name;
        V.FormParent = form;
        V.PARAMTO[field.length] = formCaption;
        P.DOFORM("CorrectFormPF");
        V.FormCorrName = ""; // 25.09.17 rkv 
//        V.FormCorr.SETMODAL(1);    //1-Модальная форма 0-не модальная
        if (V.PARAMOT[0].equals("T")) {
            return true;
        }
        return false;
    }
  /**
     * Универсальная форма корректуры полей
     *
     * @param formName имя создаваемой формы
     * @param field массив полей
     * @return
     */
    public static boolean DOCORRECTPF(String formName, Object... field) {
        return DOCORRECTPF(V.ACTIVEFORM, V.ACTIVEFORM.getName() + "_CORR", formName, field);
    }

    public static ArrayList<ArrayList<String>> EXCEL_LOADING(String filePath, String vid) {
        if (filePath == null || filePath.equals("")) {
            return null;
        }
        ArrayList<ArrayList<String>> arr = new ArrayList<>();
        InputStream in = null;
        try {
            in = new FileInputStream(filePath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
        }
        Workbook wb = new XSSFWorkbook();
        try {
            if (filePath.substring(filePath.length() - 4, filePath.length()).equals("xlsx")) {
                wb = new XSSFWorkbook(in);
            } else if (filePath.substring(filePath.length() - 4, filePath.length()).equals(".xls")) {
                wb = new HSSFWorkbook(in);
            } else {
                P.MESSERR("Выберите Excel файл!");
                return null;
            }
        } catch (IOException ex) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);

            Iterator<Row> it = sheet.iterator();
            while (it.hasNext()) {
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                ArrayList<String> arr_2 = new ArrayList<>();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    int cellType = cell.getCellType();
                    String s;
                    switch (cellType) {
                        case Cell.CELL_TYPE_STRING:
                            arr_2.add(cell.getStringCellValue().substring(cell.getStringCellValue().length() - 2, cell.getStringCellValue().length()).equals(".0")
                                    ? cell.getStringCellValue().substring(0, cell.getStringCellValue().length() - 2)
                                    : cell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            s = (new DecimalFormat("#.0")).format(cell.getNumericCellValue());
                            arr_2.add(s.substring(s.length() - 2, s.length()).equals(".0")
                                    ? s.substring(0, s.length() - 2)
                                    : s);
                            break;

                        case Cell.CELL_TYPE_FORMULA:
                            s = (new DecimalFormat("#.0")).format(cell.getNumericCellValue());
                            arr_2.add(s.substring(s.length() - 2, s.length()).equals(".0")
                                    ? s.substring(0, s.length() - 2)
                                    : s);
                            break;
                        default:
                            break;
                    }
                    if (vid.equals("ZK01") || vid.equals("ZK21")) {
                        arr_2.add(wb.getSheetName(i));
                        break;
                    }
                }
                arr.add(arr_2);
            }
        }
        return arr;
    }

    public static void PRICE_LIST_PRINT(String sele, String repName) {
        PRICE_LIST_PRINT(sele, repName, FF.DATES());
    }

    public static void PRICE_LIST_PRINT(String sele, String repName, String DATEB) {
        String buf = "";
        if (P.MESSYESNO("Если хотите выбрать конкретные ценники для печати, нажмие \"Да\". \nЕсли все - нажмите \"Нет\"") == 0) {
            sele = sele.replaceFirst("SELECT distinct", "SELECT");
            sele = sele.replaceFirst("SELECT ", "SELECT distinct 'F' AS BIT_FLAG, ");
            P.SQLEXECT(sele, "PRICE_LIST_PR");
            P.DOVIEW("PRICE_LIST_PR", "Ценники", "Выбор", "Артикул", "Размерный ряд", "Цена", "BIT_FLAG", "ART", "STRR", "CENA2", 100, 100, 100, 100);
            if (V.PARAMOT != null && V.PARAMOT[0].equals("T")) {
                A.SELECT("PRICE_LIST_PR");
                A.GOTOP("PRICE_LIST_PR");
                for (int i = 0; i < A.RECCOUNT("PRICE_LIST_PR"); i++) {
                    if (A.GETVALS("BIT_FLAG").equals("T")) {
                        buf += "'" + A.GETVALS("ART").replaceAll("\\\\","\\\\\\\\") + "',";
                    }
                    A.GOTO("PRICE_LIST_PR", i + 2);
                }
                if (!buf.equals("")) {
                    buf = buf.substring(0, buf.length() - 1);
                    sele = sele.replaceFirst("A WHERE ", "A WHERE UPPER(A.ART) IN (" + buf.toUpperCase() + ") and ");
                    sele = sele.replaceFirst("A WHERE left join (select art,max(ean) ean from s_ean group by art) E on A.ART = E.ART ", "A left join (select art,max(ean) ean from s_ean group by art) E on A.ART = E.ART WHERE UPPER(A.ART) IN (" + buf.toUpperCase() + ") and ");
                    sele = sele.replaceFirst("A left join s_price_mif c on c.art = a.art WHERE ", "A left join s_price_mif c on c.art = a.art WHERE UPPER(A.ART) IN (" + buf.toUpperCase() + ") and ");
//                    sele = sele.replaceFirst("WHERE e.art is not null and", "WHERE UPPER(C.ART) IN (" + buf.toUpperCase() + ") and e.art is not null and");
//                    sele = sele.replaceFirst("WHERE c.art is not null and", "WHERE UPPER(A.ART) IN (" + buf.toUpperCase() + ") and c.art is not null and");
                    sele = sele.replaceFirst("C WHERE ", "C WHERE UPPER(C.ART) IN (" + buf.toUpperCase() + ") and ");
                    sele = sele.replaceFirst("C WHERE left join (select art,max(ean) ean from s_ean group by art) E on C.ART = E.ART ", "C left join (select art,max(ean) ean from s_ean group by art) E on C.ART = E.ART WHERE UPPER(C.ART) IN (" + buf.toUpperCase() + ") and ");
                    sele = sele.replaceFirst("C left join s_price_mif e on e.art = c.art WHERE ", "C left join s_price_mif e on e.art = c.art WHERE UPPER(C.ART) IN (" + buf.toUpperCase() + ") and ");
                }
                A.CLOSE("PRICE_LIST_PR");
            }
        }
        Reportr.INIT(sele); //инициализация параметров
        V.PRINTMAP.put("DATEB", DATEB); //ДАТА ЦЕНЫ
        V.PRINTMAP.put("RR", "р-ры:");
        Reportr.RUN(repName); //запуск отчета
    }

    /**
     * Определение разницы во времени
     *
     * @return
     */
    public static boolean TEST_TIME() { // ВЫГРУЗКА ДАННЫХ ПО СИСТЕМНЫМ НАСТРОЙКАМ
        if ("2".equals(V.CONN_DRIVER)) { //если sql server нужно писать 
            return true;
        }
        Date DD, DS = null;
        DD = FF.DATETIME();
        V.SELE = "SELECT TO_CHAR(systimestamp,'DD.MM.YYYY HH24:MI:SS,ff3') AS DATES,systimestamp  AS DATED,TO_CHAR(systimestamp,'ff3') AS DATESSS FROM DUAL";
        P.SQLEXECT(V.SELE, "TEST_TIME");
        //FF._CLIPTEXT(V.SELE);
        //DD = (Date) A.GETVAL("TEST_TIME.DATED"); //ЛОКАЛЬНОЕ ОТРАЖЕНИЕ
        int MS = (int) FF.VAL(A.GETVALS("TEST_TIME.DATESSS")); //ЛОКАЛЬНОЕ ОТРАЖЕНИЕ

        try {
            String str = (String) A.GETVAL("TEST_TIME.DATES");
            DS = V.ddmmyyyyhhmm.parse(str);  //СЕРВЕРНОЕ ОТРАЖЕНИЕ
        } catch (ParseException ex) {

        }
        V.DIFF_DATE = DS.getTime() - (DD.getTime() - MS);
        V.DIFF_HOURS = (int) (V.DIFF_DATE / 3600000);

        return true;
    }

    /**
     * Проверка на наличие объекта таблица-представление в базе если нет то
     * выполняются скрипты
     *
     * @param STRU - вид проверки 0- таблица 1- поле в таблице
     * @param TABLE - список таблиц-поелй
     * @return
     */
    public static boolean VALID_STRU_TABLE(int STRU, String... TABLE) { // ПРОВЕРКА СТРУКТУРЫ ДАННЫХ ПРИ ОБНОВЛЕНИЯХ
        return VALID_STRU_TABLE("", STRU, TABLE);
    }

    /**
     * Проверка на наличие объекта таблица-представление в базе если нет то
     * выполняются скрипты
     *
     * @param doppath - дополнительный подкаталог к \\Reports\\SCRIPT\\
     * @param STRU - вид проверки 0- таблица 1- поле в таблице
     * @param TABLE - список таблиц-поелй
     * @return
     */
    public static boolean VALID_STRU_TABLE(String doppath, int STRU, String... TABLE) { // ПРОВЕРКА СТРУКТУРЫ ДАННЫХ ПРИ ОБНОВЛЕНИЯХ
        String SELE_HOME = "";
        if ("1".equals(V.CONN_DRIVER)) { //для орасле
            SELE_HOME = "SELECT  SYSDATE AS DATEV";
        }
        if ("2".equals(V.CONN_DRIVER)) {//для sql server
            SELE_HOME = "SELECT  GETDATE() AS DATEV";
        }

        V.SELE = "";
        String file = "";
        //String path="\\Reports\\SCRIPT\\";
        String path = V.DIRECTORY_SCRIPT;
        if (!"".equals(doppath)) {
            path = path + doppath + V.SEPARATOR;
        }
        String SELE = "", WHERE = "", ALIAS = "", POLE = "";
        int pos = 0;
        int j = 0;
        switch (STRU) {
            case 0: // ЕСЛИ ПОИСК ТАБЛИЦЫ

                SELE = SELE_HOME + "\n";
                WHERE = "FROM BASIC_TABLE  WHERE NAME IN (' '";
                for (int i = 0; i < TABLE.length; i++) {
                    SELE = SELE + ",CAST(NVL(SUM(CASE WHEN NAME='" + TABLE[i] + "' THEN 1 ELSE 0 END ),0) AS NUMBER(1,0) ) AS P" + i + " " + "\n";
                    WHERE = WHERE + ",'" + TABLE[i] + "' ";
                }
                V.SELE = V.SELE + SELE + WHERE + ")" + "\n";
                break;

            case 1: // ЕСЛИ ПОИСК ПОЛЯ В ТАБЛИЦЕ

                SELE = SELE_HOME + "\n";
                WHERE = "FROM BASIC_COLUMN  WHERE 1=0 ";
                for (int i = 0; i < TABLE.length; i++) {
                    POLE = TABLE[i];
                    pos = POLE.indexOf(".");//если разделение точкой то алиас.название поля
                    if (pos > -1) //
                    {
                        ALIAS = POLE.substring(0, pos);//выделяем до точки алиас
                        POLE = POLE.substring(pos + 1);//выделяем после точки имя поля
                    } else {
                        return false;
                    }

                    SELE = SELE + ",CAST(NVL(SUM(CASE WHEN NAMEO='" + ALIAS + "' AND NAME='" + POLE + "'  THEN 1 ELSE 0 END ),0) AS NUMBER(1,0) ) AS P" + i + " " + "\n";
                    WHERE = WHERE + " OR (NAMEO='" + ALIAS + "' AND NAME='" + POLE + "')";
                }
                V.SELE = V.SELE + SELE + WHERE + " " + "\n";
                break;
            case 2: // ЕСЛИ ПОИСК ИНДЕКСА

                SELE = SELE_HOME + "\n";
                WHERE = "FROM BASIC_INDEX  WHERE NAME IN (' '";
                for (int i = 0; i < TABLE.length; i++) {
                    SELE = SELE + ",CAST(NVL(SUM(CASE WHEN NAME='" + TABLE[i] + "' THEN 1 ELSE 0 END ),0) AS NUMBER(1,0) ) AS P" + i + " " + "\n";
                    WHERE = WHERE + ",'" + TABLE[i] + "' ";
                }
                V.SELE = V.SELE + SELE + WHERE + ")" + "\n";
                break;

        }
        if ("2".equals(V.CONN_DRIVER)) {//для sql server
            V.SELE = V.SELE.replaceAll("NVL", "ISNULL");
            V.SELE = V.SELE.replaceAll("NUMBER", "NUMERIC");
        }

        P.SQLEXECT(V.SELE, "UD_TABLE", false);

        for (int i = 0; i < TABLE.length; i++) {
            j = 1;
            file = path + TABLE[i] + "_" + j + ".SQL";
            while (FF.FILE(file)) {
                if ("0".equals(A.GETVALS("UD_TABLE.P" + i))) {
                    SELE = "";
                    SELE = SELE + FF.FILETOSTR(file,"UTF-8");

                    if (FF.EMPTY(SELE)) {
                        return false;
                    }
                    //для многократного выполнения через; вначале ***
                    if ("***".equals(FF.SUBSTR(SELE, 1, 3)) || "!!!".equals(FF.SUBSTR(SELE, 1, 3))) {
                        boolean ERR = "!!!".equals(FF.SUBSTR(SELE, 1, 3)); // ОШИБКИ ИГНОРИРУЮТСЯ
                        SELE = FF.SUBSTR(SELE, 4);
                        int AT9 = FF.AT(";", SELE);
                        while (AT9 > 0) {
                            V.SELE = FF.SUBSTR(SELE, 1, AT9 - 1);
                            if (P.SQLEXECUT(V.SELE) == false && !ERR) {  // ЕСЛИ ОШИБКА И НЕ ИГНОРИРУЕТСЯ
                                P.MESS("Исполняемый Файл:" + file);
                                return false;
                            }
                            SELE = FF.SUBSTR(SELE, AT9 + 1);
                            AT9 = FF.AT(";", SELE);
                        }
                    } else {  //если одиночная команда
                        if (P.SQLEXECUT(SELE) == false) {
                            P.MESS("Исполняемый Файл:" + file);
                            return false;
                        }
                    }
                }
                j++;
                file = path + TABLE[i] + "_" + j + ".SQL";
            }

        }
        return true;
    }

    /**
     * Создание индекса (проверка существования на имя таблицы и списка полей в
     * BASIC_INDEX)
     *
     * @param VID - по умолчанию 0 - общий индекс 1- primary key
     * @param TABLE - имя таблицы
     * @param INDEX - имя индекса
     * @param FIELDS - список полей
     * @return
     */
    public static boolean CREATE_INDEX(int VID, String TABLE, String INDEX, String FIELDS) { // ФОРМИРОВАНИЕ ИНДЕКСА , ЕСЛИ ТАКОГО НЕТ
        TABLE = TABLE.toUpperCase();
        INDEX = INDEX.toUpperCase();
        FIELDS = FIELDS.toUpperCase();

        V.SELE = "";
        V.SELE = V.SELE + "SELECT * FROM BASIC_INDEX WHERE TABLE_NAME=" + P.P_SQL(TABLE) + " AND (FIELDS=" + P.P_SQL(FIELDS) + " OR  SUBSTR(FIELDS,1,4)='SYS_')" + "\n";
        P.SQLEXECT(V.SELE, "UD_INDEX", false);
        if (A.RECCOUNT("UD_INDEX") > 0) { //если индекс с таким порядком полей существует
            return true;
        }
        if (VID == 0) {  //primary key
            V.SELE = "create index " + INDEX + " on " + TABLE + "(" + FIELDS + ")";
        }
        if (VID == 1) {
            V.SELE = "alter table " + TABLE + " add constraint " + INDEX + " primary key (" + FIELDS + ")";
        }
        if (P.SQLEXECUT(V.SELE) == false) {
            return false;
        }
        return true;
    }

    public static String TRANSFORM(BigDecimal VAL, String MASK) { // 

        double VALD = VAL.doubleValue();
        return TRANSFORM(VALD, MASK, " ", ".");
    }

    public static String TRANSFORM(BigDecimal VAL, String MASK, String GSep, String DSep) { // 
        double VALD = VAL.doubleValue();
        return TRANSFORM(VALD, MASK, GSep, DSep);
    }

    public static String TRANSFORM(double VAL, String MASK) { // 
        return TRANSFORM(VAL, MASK, " ", ".");
    }

    /**
     * Преобразование числа в форматированную строку по заданной маске
     * prg.P.TRANSFORM($F{SUM2},$P{PATTERN_SUM}," " ,",")
     *
     * @param VAL - ЗНАЧЕНИЕ double
     * @param MASK - МАСКА ВЫВОДА
     * @param GSep - РАЗДЕЛИТЕЛЬ ГРУПП
     * @param DSep - РАЗДЕЛИТЕЛЬ ДЕСЯТИЧНЫХ
     * @return
     */
    public static String TRANSFORM(double VAL, String MASK, String GSep, String DSep) { // 
        String DESFORMAT_TEXT = MASK; //  "###,###.###"; //МАСКА ВЫВОДА ЧИСЕЛ
        DecimalFormatSymbols DecFormatP = new DecimalFormatSymbols();// КЛАСС ОПРЕДЕЛЕНИЯ РАЗДЕЛИТЕЛЕЙ
        DecimalFormat DecFormat = new DecimalFormat(DESFORMAT_TEXT);    //КЛАСС ФОРМАТИРОВАНИЯ
        if (FF.EMPTY(DSep)) {
            DSep = String.valueOf(V.DecFormatP.getDecimalSeparator());
        }
        if (FF.EMPTY(GSep)) {
            GSep = String.valueOf(V.DecFormatP.getGroupingSeparator());
        }

        V.DecFormatP.setGroupingSeparator(GSep.charAt(0));
        V.DecFormatP.setDecimalSeparator(DSep.charAt(0));
        V.DecFormat.setDecimalFormatSymbols(V.DecFormatP);
        String str = V.DecFormat.format(VAL);
        int pointm = FF.AT(".0", MASK);
        int pointv = FF.AT(DSep, str);

        if (pointm == 0) {
            return str;
        }
        int decm = FF.LEN(FF.SUBSTR(MASK, pointm)) - 1;
        if (pointv == 0) {
            str = str + DSep + FF.REPLICATE("0", decm);
        } else {
            int decv = FF.LEN(FF.SUBSTR(str, pointv)) - 1;

            if (decm > decv) {
                str = str + FF.REPLICATE("0", decm - decv);
            }
        }
        return str;

    }

    public static JTable SQL_SERVER_QUERY(String ip, String port, String base, String name, String pass, String query) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            V.CONNECT_BUF = java.sql.DriverManager.getConnection("jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + base + ";userName=" + name + ";" + (pass.equals("") ? "password=" + pass + ";" : ""));
            PreparedStatement ps = V.CONNECT_BUF.prepareStatement(query);
            DefaultTableModel model = new DefaultTableModel(0, 0);
            JTable table = new JTable();
            table.setModel(model);
            ResultSet rs = ps.executeQuery();
            int ColumnCount;
            ColumnCount = rs.getMetaData().getColumnCount();
            model.setColumnCount(0);
            for (int colInd = 1; colInd < ColumnCount + 1; colInd++) {
                model.addColumn(rs.getMetaData().getColumnName(colInd).toString());
            }
            int rowInd = 0, rsInd;
            while (rs.next()) {
                rsInd = 1;
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{});
                for (int colInd = 0; colInd < ColumnCount; colInd++) {
                    table.setValueAt(rs.getString(rsInd), rowInd, colInd);
                    rsInd++;
                }
                rowInd++;
            }
            V.CONNECT_BUF.close();
            V.CONNECT_BUF = null;
            ps.close();
            rs.close();
            return table;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static JTable ORACLE_SERVER_QUERY(String query) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            V.CONNECT_BUF = P.SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);//V.CONN1 глобальная переменная
            PreparedStatement ps = V.CONNECT_BUF.prepareStatement(query);
            DefaultTableModel model = new DefaultTableModel(0, 0);
            JTable table = new JTable();
            table.setModel(model);
            ResultSet rs = ps.executeQuery();
            int ColumnCount;
            ColumnCount = rs.getMetaData().getColumnCount();
            model.setColumnCount(0);
            for (int colInd = 1; colInd < ColumnCount + 1; colInd++) {
                model.addColumn(rs.getMetaData().getColumnName(colInd).toString());
            }
            int rowInd = 0, rsInd;
            while (rs.next()) {
                rsInd = 1;
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{});
                for (int colInd = 0; colInd < ColumnCount; colInd++) {
                    table.setValueAt(rs.getString(rsInd), rowInd, colInd);
                    rsInd++;

                }
                rowInd++;
//                System.out.println(rowInd);
            }
            V.CONNECT_BUF.close();
            V.CONNECT_BUF = null;
            ps.close();
            rs.close();
            return table;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void EXCEL_SAVING(JTable grid, String file, ArrayList<String> arrr) {
        String workDir = ""; // Рабочий каталог проекта
        int count = 0;
        File jarFile = new File(".");
        if (jarFile.exists()) {
            workDir = jarFile.getAbsolutePath().toString().substring(0, jarFile.getAbsolutePath().toString().length() - 1) + "\\Excel\\";
        }
        jarFile.delete();
        File myPath = new File(workDir);
        myPath.mkdirs();
        String date = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new GregorianCalendar().getTime());

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        Sheet sheet = null;

        CellStyle valueStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(valueStyle, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_NONE);
        valueStyle.setWrapText(true);
        CellStyle headerStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(headerStyle, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK);
        headerStyle.setWrapText(true);
        FileInputStream fis = null;

        for (int rowInd = 0; rowInd < grid.getRowCount(); rowInd++) {
            if (count == 0) {

                sheet = workbook.createSheet();
//                Row row = sheet.createRow(count);
//                CellRangeAddress region = new CellRangeAddress(0, 0, 0, grid.COLCOUNT - 1);
//                sheet.addMergedRegion(region);
//                Cell cell = row.createCell(0);
//                cell.setCellValue("asd");//заголовок
//                CellStyle mainHeaderStyle = workbook.createCellStyle();
//                mainHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
//                Font font = workbook.createFont();
//                font.setFontHeightInPoints((short) 20);
//                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//                mainHeaderStyle.setFont(font);
//                cell.setCellStyle(mainHeaderStyle);
//                count++;

                Row row = sheet.createRow(count);
                for (int colIndHead = 0; colIndHead < grid.getColumnCount(); colIndHead++) {
                    Cell cell = row.createCell(colIndHead);

                    cell.setCellStyle(headerStyle);
                    cell.setCellValue(arrr.get(colIndHead));
                }
            }
            Row row = sheet.createRow(count + 1);
            for (int colInd = 0; colInd < grid.getColumnCount(); colInd++) {
                Cell cell = row.createCell(colInd);
                cell.setCellStyle(valueStyle);
                if (grid.getValueAt(rowInd, colInd) == null) {
//                    grid.setValueAt("", rowInd, colInd);
                    cell.setCellValue("");
                } else {
                    String val = grid.getValueAt(rowInd, colInd).toString();
                    cell.setCellValue(val.length() > 2 && val.substring(val.length() - 2, val.length()).equals(".0") ? val.substring(0, val.length() - 2) : val);
                }
                sheet.setColumnWidth((short) colInd, (short) 50 * 50);
            }
            count++;
            if (count % 100 == 0) {
                try {
                    ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others

                    // ((SXSSFSheet)sh).flushRows() is a shortcut for ((SXSSFSheet)sh).flushRows(0),
                    // this method flushes all rows
                } catch (IOException ex) {
                    Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        P.ALERT("Сохранение данных в Excel...");
        try {
            if (sheet != null) {
//                String date = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new GregorianCalendar().getTime());
                try (FileOutputStream out = new FileOutputStream(file)) {
                    workbook.write(out);
                    out.close();
                    workbook.dispose();
                }
            }
//            ArrayList<Workbook> arrW = new ArrayList<>();
//            arrW.add(new XSSFWorkbook(new FileInputStream(workDir + "50000" + ".xlsx")));
//            arrW.add(new XSSFWorkbook(new FileInputStream(workDir + "end" + ".xlsx")));
//            try (FileOutputStream out = new FileOutputStream(workDir + "end" + ".xlsx")) {
//                arrW.get(0).write(out);
//                out.close();
//            }
            Runtime.getRuntime().exec("cmd.exe /c \"" + file + "\"");
        } catch (IOException ex) {
            try (FileOutputStream out = new FileOutputStream(workDir + date + ".xlsx")) {
                workbook.write(out);
                out.close();
                workbook.dispose();
                Runtime.getRuntime().exec("cmd.exe /c \"" + workDir + date + ".xlsx\"");
            } catch (IOException wxx) {
                P.MESSERR("Ошибка сохранения!");
            }
        }
        P.ALERT("");
    }

    //проверка строки регулярным выражением
    public static boolean regExpCheck(String str, String condition) {
        Pattern p = Pattern.compile(condition);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static void gridFilter(Gridr grid) {
        String buf = "";
        String[] typeArr = new String[grid.FIELD.length];
        for (int j = 0; j < grid.FIELD.length; j++) {
            for (int k = 0; k < grid.DATA.COLNAMES.length; k++) {
                if (grid.FIELD[j].equals(grid.DATA.COLNAMES[k])) {
                    typeArr[j] = grid.DATA.COLTYPE[k];
                    break;
                }
            }
        }
        for (int i = 0; i < grid.FIELD.length; i++) {
//            System.out.println(G[0].FIELD[i] + " " + G[0].CAPTION[i]);
            buf = buf + "select 'F' as BIT_SELE, '" + grid.CAPTION[i] + "' as F1 from dual " + "union all ";
        }
        buf = buf.substring(0, buf.length() - 10);
        P.SQLEXECT(buf, "F_SEL");
        P.DOVIEW("F_SEL", "Выберите поля", "Выбор", "Наименование", "BIT_SELE", "F1", 100, 250);
        if (V.PARAMOT != null && V.PARAMOT[0].equals("T")) {
            V.OPTION_ARR.clear();
            V.OPTION_NAME_ARR.clear();
            V.OPTION_TYPE_ARR.clear();
            A.SELECT("F_SEL");
            A.GOTOP("F_SEL");

            for (int i = 0; i < A.RECCOUNT("F_SEL"); i++) {
                if (A.GETVALS("BIT_SELE").equals("T")) {
                    V.OPTION_ARR.add(A.GETVALS("F1"));
                    for (int m = 0; m < grid.CAPTION.length; m++) {
                        if (A.GETVALS("F1").equals(grid.CAPTION[m])) {
                            V.OPTION_NAME_ARR.add(grid.FIELD[m]);
                            V.OPTION_TYPE_ARR.add(typeArr[m]);
                            break;
                        }
                    }
                }
                A.GOTO("F_SEL", i + 2);
            }
            A.CLOSE("F_SEL");
            V.PARAMTO = new String[1];
            V.PARAMTO[0] = grid.DATA.SELE;
            if (!V.OPTION_ARR.isEmpty()) {
                Formr form_d = P.DOFORM("SHOP_OPTION");
                form_d.PARENTFORM = grid.THISFORM;
                if (form_d.PARENTFORM != null) {
                    form_d.PARENTFORM.SETFOCUS();
                }
            }
        }
    }

    /**
     * Извлечение поля типа BLOD в бинарный файл
     *
     * @param SELE - запрос для поля BLOD
     * @param FILE - файл результат
     */
    public static boolean BLOB_TO_FILE(String SELE, String FILE) {
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = V.CONN1.prepareStatement(SELE);
            rs = ps.executeQuery();
            while (rs.next()) {
                File file = new File(FILE);
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                InputStream is = rs.getBinaryStream(1);
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
//                while (is.read(buffer) > 0) {
//                    fos.write(buffer);
//                }
                fos.close();
            }
            ps.close();
            rs.close();
        } catch (SQLException | IOException e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Конвертация типа BLOD в бинарный файл
     *
     * @param blob - поле BLOD
     * @param blobFile-файл результат
     * @return
     */
    public static boolean BLOB_TO_FILE(Blob blob, String blobFile) {
        try {
            FileOutputStream outStream = new FileOutputStream(blobFile);
            InputStream inStream = blob.getBinaryStream();

            int length = -1;
            int size = (int) blob.length();
            byte[] buffer = new byte[size];

            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }

            inStream.close();
            outStream.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Распаковка архива zip
     *
     * @param zipName - имя архива
     * @param dirName - каталог распаковки
     * @param area - область сообщений
     * @return
     */
    public static boolean UNZIP(String zipName, String dirName, JTextArea area) {
        Enumeration entries = null;
        ZipFile zip;

        if (zipName.equals("")) {
            // JOptionPane.showMessageDialog(null, "Указанного файла zip, не существует! Укажите существующий файл zip.");
            System.out.println("Указанного файла zip, не существует! Укажите существующий файл zip.");
            if (area != null) {
                area.append("Указанного файла zip, не существует! Укажите существующий файл zip.\n");
                area.paintImmediately(area.getBounds());
            }
            return false;
        }

        // Проверим наличие папки в которую распаковываем архив
        // при необходимости создадим ее
        createDir(dirName);

        try {
            zip = new ZipFile(zipName);
            System.out.println(zipName);

            entries = zip.entries();
            ZipEntry entry;
            while (entries.hasMoreElements()) {
                entry = (ZipEntry) entries.nextElement();
                System.out.println("Распаковка: " + entry.getName());
                if (area != null) {
                    area.append("Распаковка: " + entry.getName() + "\n");
                    area.paintImmediately(area.getBounds());
                }
                String zipPath = entry.getName();
                // проверим в пути наличие папок и если они есть, то создадим их, и обратно вернем только имя файла
                if (!ZIPwrite(dirName, zipPath, zip.getInputStream(entry))) {
                    P.MESSERR("Ошибка записи файла: " + zipPath);
                    return false;
                }
            }
            zip.close();
            return true;
        } catch (NoSuchElementException e) {
            System.out.println(e);
            P.MESSERR("Ошибка чтения из архива \n" + e.getMessage());
            return false;

        } catch (java.lang.IllegalArgumentException e) {
            System.out.println(e);
            P.MESSERR("Ошибка чтения из архива \n" + e.getMessage());
            return false;

        } catch (IOException e) {
            System.out.println(e);
            P.MESSERR("Ошибка распаковки архива");
            return false;
        }
    }

    private static boolean ZIPwrite(String dirName, String FilePath, InputStream in) throws IOException {
        // сначала создадим папки которые указаны в пути к файлу
        if (dirName == "") {
            dirName = ".";
        }
        System.out.println(dirName);
        String mFilePath = FilePath;
        String newFilePath = dirName;
        boolean kol = true;
        while (kol) {
            if (mFilePath.indexOf("/") < 0) {
                // если папок больше не осталось, то прекратим цикл и создадим сам файл
                kol = false;
            } else {
                Integer resName = mFilePath.indexOf("/"); // найдем разделитель папок
                String mDirName = mFilePath.substring(0, resName); // получим имя папки
                mFilePath = mFilePath.substring(resName + 1); // сократим путь на одну папку
                createDir(newFilePath + System.getProperty("file.separator") + mDirName);
                newFilePath = newFilePath + System.getProperty("file.separator") + mDirName;
                if (mFilePath.equals("")) {
                    return true;
                }
            }
        }

        OutputStream out = new BufferedOutputStream(new FileOutputStream(dirName + System.getProperty("file.separator") + FilePath));

        // а теперь создадим сам файл
        //
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
        return true;
    }

    public static void createDir(String dir) {
        if (dir != null) {
            File myPath = new File(dir);
            if (!myPath.exists()) {
                myPath.mkdirs();
            }
        }
    }
    /**
     * Определение директории запуска программы
     *
     * @return
     */
    public static String programmDir() {
        URL pathURL = V.class.getProtectionDomain().getCodeSource().getLocation();
        String path = String.valueOf(pathURL);
        path = path.substring(6, path.length() - 1);
        path = path.substring(0, path.lastIndexOf("/") + 1).replace("/", System.getProperty("file.separator"));
        return path;
    }

    public static String macAddress() {
        InetAddress ip;
        NetworkInterface network = null;
        byte[] mac = null;
        try {

            V.Host = InetAddress.getLocalHost().getHostName();
            InetAddress[] mip = InetAddress.getAllByName(V.Host);
            int KOLIP = mip.length;                       //InetAddress.getAllByName(HOST).length;
            try {
                for (int j = 0; j < KOLIP; j++) {
                    mac = NetworkInterface.getByInetAddress(mip[j]).getHardwareAddress();
                    if (mip[j] != null && mac != null && mip[j].isSiteLocalAddress() && mac.length > 0) {
                        V.IP = mip[j].getHostAddress();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mac.length; i++) {
                            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                        }
                        V.MAC = sb.toString();
                        //  mac=NetworkInterface.getByInetAddress(mip[j]).getInetAddresses().nextElement().getAddress();
                    }
                }
            } catch (SocketException | NullPointerException ex) {
                //          Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
//                P.MESSERR("Ошибка получения адреса оборудования\n"+ex.getMessage());
                return "";
            }

            System.out.println("Current IP address : " + V.IP);

        } catch (UnknownHostException ex) {
//            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
            P.MESSERR("Ошибка получения MAC адреса \n" + ex.getMessage());
            return "";

        }
        System.out.println("Current MAC address : " + V.MAC);
        return V.MAC;
    }

    
    /**
     * Экпорт запроса в excel таблицу
     *
     * @param SELE - запрос
     * @param HEADER - заголовок
     * @param COL - название колонок
     */
    public static void SELETOEXCEL(String SELE, String HEADER, String... COL) {
        P.SQLEXECT(SELE, "EXCELS");
        A.SELECT("EXCELS");
        String fileName = EXCEL_FILECHOOSE("Выберите файл", "");
        int count = 0;
//        File jarFile = new File(".");
//        if (jarFile.exists()) {
//            workDir = jarFile.getAbsolutePath().toString().substring(0, jarFile.getAbsolutePath().toString().length() - 1) + "\\Excel\\";
//        }
//        jarFile.delete();
        File myPath = new File(fileName);
//        myPath.mkdirs();

//        Workbook workbook = new XSSFWorkbook();
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        Sheet sheet = null;

        CellStyle valueStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(valueStyle, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_NONE);
        valueStyle.setWrapText(true);
        CellStyle headerStyle = workbook.createCellStyle();
        CREATE_CELL_STYLE(headerStyle, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK, CellStyle.BORDER_THICK);
        headerStyle.setWrapText(true);
        String[] typeArr = new String[COL.length];
        for (int j = 0; j < COL.length; j++) {
            typeArr[j] = "C";
        }
        for (int rowInd = 0; rowInd < A.RECCOUNT(); rowInd++) {
            A.GOTO(rowInd + 1);
            if (count == 0) {

                sheet = workbook.createSheet();
                Row row = sheet.createRow(count);
                row.setHeight((short) (row.getHeight() * 4));
                CellRangeAddress region = new CellRangeAddress(0, 0, 0, COL.length - 1);
                sheet.addMergedRegion(region);
                Cell cell = row.createCell(0);
                cell.setCellValue(HEADER);//заголовок
                CellStyle mainHeaderStyle = workbook.createCellStyle();
                mainHeaderStyle.setAlignment(CellStyle.ALIGN_CENTER);
                Font font = workbook.createFont();
                font.setFontHeightInPoints((short) 20);
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                mainHeaderStyle.setFont(font);
                cell.setCellStyle(mainHeaderStyle);
                count++;

                row = sheet.createRow(count);
                for (int colIndHead = 0; colIndHead < COL.length; colIndHead++) {
                    cell = row.createCell(colIndHead);

                    cell.setCellStyle(headerStyle);
                    if (!COL[colIndHead].contains("<html><center>")) {
                        cell.setCellValue(COL[colIndHead]);
                    } else {
                        cell.setCellValue(COL[colIndHead].substring("<html><center>".length()));
                    }
                }
            }
            Row row = sheet.createRow(count + 1);
//            A.GOTOP();
            Cursorr DATA = (Cursorr) V.MAPALIAS.get("EXCELS");
            for (int colInd = 0; colInd < COL.length; colInd++) {
                String name = DATA.COLNAMES[colInd];
                Cell cell = row.createCell(colInd);
                cell.setCellStyle(valueStyle);
                if (A.GETVALS("EXCELS." + name) == null) {
                    cell.setCellValue("");
                } else {
                    if (typeArr[colInd].equals("N")) {
                        String str = A.GETVALS("EXCELS." + name);
                        str = str.replaceAll(" ", "");
                        try {
                            cell.setCellValue(Double.parseDouble(str));
                        } catch (NumberFormatException escc) {
                            cell.setCellValue(str);
                        }
                    } else {
                        cell.setCellValue(A.GETVALS("EXCELS." + name));
                    }
                }
                sheet.setColumnWidth((short) colInd, (short) COL[colInd].length() * 380);
                //             A.GOTO(colInd + 2);
            }
            count++;
            if (count % 100 == 0) {
                try {
                    ((SXSSFSheet) sheet).flushRows(100); // retain 100 last rows and flush all others

                    // ((SXSSFSheet)sh).flushRows() is a shortcut for ((SXSSFSheet)sh).flushRows(0),
                    // this method flushes all rows
                } catch (IOException ex) {
                    Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            if (sheet != null) {
                try (FileOutputStream out = new FileOutputStream(fileName)) {
                    workbook.write(out);
                    out.close();
                    Runtime.getRuntime().exec("cmd.exe /c \"" + fileName);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    public static void SCAN_ORA_PORT() {
        A.CLOSE("IPADD");
        P.SQLEXECT("select a.IP,a.shopid from st_shop a where REGEXP_LIKE(a.shopid,'^\\d{4}$') \n"
                + "and substr(a.shopid,1,1) != '9'\n"
                + "and a.ORG_KOD ='SHP' \n"
                + "and a.DB_LINK_NAME is not null\n"
                + "and a.IP != ' '\n"
                + "and a.SHOPID != '2009' and a.SHOPID != '2000' and a.shopid != '0031' and a.BIT_OPEN != 'F' order by a.shopid", "IPADD");

        V.MONITOR_ARR.clear();

        ArrayList<Thread> thrArr = new ArrayList<>();

        for (int i = 1; i < A.RECCOUNT("IPADD") + 1; i++) {
            thrArr.add(new MonitorThread(A.GETVALS("IPADD.IP"), A.GETVALS("IPADD.SHOPID")));
            A.GOTO("IPADD", i + 1);
        }

        for (int i = 0; i < A.RECCOUNT("IPADD"); i++) {
            thrArr.get(i).start();
        }

        while (true) {
            try {
                for (int i = 0; i < A.RECCOUNT("IPADD"); i++) {
                    thrArr.get(i).join();
                }
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void scanport() {
        A.CLOSE("IPADD");
        P.SQLEXECT("select a.IP,a.shopid from st_shop a where REGEXP_LIKE(a.shopid,'^\\d{4}$') \n"
                + "and substr(a.shopid,1,1) != '9'\n"
                + "and a.ORG_KOD ='SHP' \n"
                + "and a.DB_LINK_NAME is not null\n"
                + "and a.IP != ' '\n"
                + "and a.SHOPID != '2009' and a.SHOPID != '2000' and a.shopid != '0031' and a.BIT_OPEN != 'F' order by a.shopid", "IPADD");

        P.SQLUPDATE("delete from temp_shopid_m");
        V.MONITOR_ARR.clear();

        ArrayList<Thread> thrArr = new ArrayList<>();

        for (int i = 1; i < A.RECCOUNT("IPADD") + 1; i++) {
            thrArr.add(new MonitorThread(A.GETVALS("IPADD.IP"), A.GETVALS("IPADD.SHOPID")));
            A.GOTO("IPADD", i + 1);
        }

        for (int i = 0; i < A.RECCOUNT("IPADD"); i++) {
            thrArr.get(i).start();
        }

        while (true) {
            try {
                for (int i = 0; i < A.RECCOUNT("IPADD"); i++) {
                    thrArr.get(i).join();
                }
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < V.MONITOR_ARR.size(); i++) {
            if (V.MONITOR_ARR.get(i) != null) {
                P.SQLUPDATE("insert into temp_shopid_m values ('" + V.MONITOR_ARR.get(i) + "')");
            }
        }
    }

    /**
     * Пауза во время выполнения программы
     *
     * @param ms - миллисекудды
     * @return если 0 - ошибка иначе ms
     */
    public static int WAIT(int ms) {
        try {
            Thread.sleep(ms);  // пауза 
        } catch (InterruptedException e) {
            return 0;

        }
        return ms;

    }

    /**
     * кодирует переданный текст для передачи в url указанное количество раз
     *
     * @see P#urlEncode(String, String)
     */
    public static String urlEncode(String text, String enc, int count) throws UnsupportedEncodingException {
        String encoded = text;
        for (int i = 0; i < count; i++) {
            encoded = urlEncode(encoded, enc);
        }
        return encoded;
    }

    /**
     * кодирует переданный текст для передачи в url
     *
     * @see URLEncoder#encode(String, String)
     */
    public static String urlEncode(String text, String enc) throws UnsupportedEncodingException {
        return URLEncoder.encode(text, enc);
    }

    /**
     *  декодирует переданный текст из представлоения для передачи по url в человекочитаемый вид
     *  указанное количество раз (если переданный текст кодировался несколько раз)
     *
     * @see URLDecoder#decode(String, String)
     * */
    public static String urlDecoder(String encodedText, String enc, int count) throws UnsupportedEncodingException {
        String decoded = encodedText;
        for (int i = 0; i < count; i++) {
            decoded = urlDecoder(decoded, enc);
        }
        return decoded;
    }

    /**
     * декодирует переданный текст из представлоения для передачи по url в человекочитаемый вид
     *
     * @see URLDecoder#decode(String, String)
     * */
    public static String urlDecoder(String encodedText, String enc) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedText, enc);
    }


    // calculate the checksum:
    char XOR(String STR) {
        char check = 0;
        // iterate over the string, XOR each byte with the total sum:
        for (int i = 0; i < STR.length(); i++) {
            check = (char) (check ^ STR.charAt(i));
        }
        return check;
    }

    /**
     * Формирование массива имен принтеров в V.PRINTERS
     *
     * @return количество принтеров в системе
     */
    public static int PRINTERSET() {
        int kol = 0;
        PrintService[] services = PrinterJob.lookupPrintServices();
        V.PRINTERS = new String[services.length];
        kol = services.length;
        for (int index = 0; index < services.length; index++) {
            V.PRINTERS[index] = services[index].getName();
        }
        return kol;

    }

    /**
     * Возвращает количество заданий в заданном принтере
     *
     * @param name - имя принтера
     * @return
     */
    public static String PRINT_Q(String name) {
        String kol = "";
        PrintService[] services = PrinterJob.lookupPrintServices();
        PrintService printer = null;
        V.PRINTERS = new String[services.length];
        //kol = services.length;
        for (int index = 0; index < services.length; index++) {
            if (name.equals(services[index].getName())) {
                printer = services[index];
                break;
            }
        }
        if (printer == null) {
            return kol;
        }
        PrintServiceAttributeSet attr = printer.getAttributes();
        Attribute[] at = attr.toArray();
        for (int index = 0; index < at.length; index++) {
            if ("queued-job-count".equals(at[index].getName())) {
                return at[index].toString();
            }
        }

        //QueuedJobCount         queued-job-count
        return "НЕ ОПР.";

    }

    /**
     * Выбор принтера
     *
     * @param OBJ компонент-полжение  к которому привязывается меню выбора
     * @return принтер
     */
    public static String PRINTERGET(Component OBJ) {
        if (PRINTERSET() == 0) {
            return "";
        }
        int BAR = P.MENU(V.PRINTERS, OBJ, new java.awt.Font("Arial", 1, 16));
        if (BAR != 0) {
            return V.PRINTERS[BAR - 1];
        }
        return "";
    }

    /**
     * Выбор принтера
     *
     * @return имя принтера
     */
    public static String PRINTERGET() {
        return PRINTERGET(V.TOOLBAR.PRINT);
    }
/**
 * Имя принтера по умолчанию
 * @return 
 */
    public static String PRINTERDEF() {
       PrintService service = PrintServiceLookup.lookupDefaultPrintService();         
        return service.getName();
    }
    
    /**
     * Проверка на существование принтера в списке принтеров системы
     *
     * @param prn -Имя принтера для поиска
     * @return
     */
    public static int PRINTERLOC(String prn) {
        if (PRINTERSET() == 0) {
            return -1;
        }
        for (int i = 0; i < V.PRINTERS.length; i++) {
            if (V.PRINTERS[i].equals(prn)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Копия Image в файл jpeg
     *
     * @param image
     * @param file
     * @return
     */
    public static boolean IMAGETOFILE(Image image, String file) {
        BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        File f = new File(file + ".JPEG");
        boolean tf = true;
        try {
            tf = ImageIO.write(bimage, "JPEG", f);
        } catch (IOException ex) {
            tf = false;
        }
        return tf;
    }

    /**
     * Проверка на изменение библиотек во время работы программы
     *
     * @return
     */
    public static boolean VALID_LIB() {
        return VALID_LIB("Изменены базовые элементы программы (обновление) \n  Требуется перезапуск приложения (Выход-вход в задачу).  ");
    }

    /**
     * Проверка на изменение библиотек во время работы программы
     *
     * @param mess - выдаваемое сообщение
     * @return
     */
    public static boolean VALID_LIB(String mess) {
        boolean tf = true;
        if (V.T_BaseFrame != 0 && V.T_BaseFrame != FF.FILE_TIME(V.F_BaseFrame)) { // ЕСЛИ ЕСТЬ НАЧАЛЬНОЕ ЗНАЧЕНИЕ И НЕ РАВНО ТЕКУЩЕМУ
            tf = false;
        }
        if (V.T_FIRM_Shop != 0 && V.T_FIRM_Shop != FF.FILE_TIME(V.F_FIRM_Shop)) { // ЕСЛИ ЕСТЬ НАЧАЛЬНОЕ ЗНАЧЕНИЕ И НЕ РАВНО ТЕКУЩЕМУ
            tf = false;
        }
        if (!tf) {
            P.MESSERR(mess);
            System.exit(0);
        }
        return tf;
    }

    /**
     * запись информации в лог
     *
     * @param type тип сообщения
     * @param clas класс, откуда записываем
     * @param method метод
     * @param message сообщение либо название ошибки
     * @param stack стек (при ошибке)
     */
    public synchronized static void writeLog(String type, String clas, String method, String message, String stack) {
        java.text.DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String thread = Thread.currentThread().getName();
        V.fileXmlLog = getLogFilePath();
        Element root = null;
        if (V.XML_FILE != null) { //если xml файл открыт

        } else {
            //если xml файл не открыт в проге
            File xmlFile = new File(V.fileXmlLog);
            if (xmlFile.exists()) { //если такой файл существует
                try {
                    FileInputStream fis;
                    // try to load document from xml file if it exist
                    // create a file input stream
                    fis = new FileInputStream(xmlFile);
                    // create a sax builder to parse the document
                    SAXBuilder sb = new SAXBuilder();
                    // parse the xml content provided by the file input stream and create a Document object                      
                    V.XML_FILE = sb.build(fis);
                    fis.close();
                } catch (Exception ex) {
                    //Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Неподходящая структура файла " + V.fileXmlLog);
                    createXmlFile();
                }

            } else { //если файла не существует
                // if it does not exist create a new document and new root
                createXmlFile();
            }
        }//конец if else на открытый файл в проге
        // get the root element of the document

        root = V.XML_FILE.getRootElement();
        Element child = new Element("log");
        child.addContent(new Element("datetime").setText(dateFormat.format(new Date())));
        child.addContent(new Element("type").setText(type));
        child.addContent(new Element("thread").setText(thread));
        child.addContent(new Element("class").setText(clas));
        child.addContent(new Element("method").setText(method));
        child.addContent(new Element("message").setText(message));
        child.addContent(new Element("stack").setText(stack));
        root.addContent(child);

        for (int i = 0; i < child.getContentSize(); i++) {
            System.out.print(child.getChildren().get(i).getName() + ":" + child.getChildren().get(i).getText() + "|");
        }
        System.out.println("");

        try (java.io.FileWriter writer = new java.io.FileWriter(V.fileXmlLog)) {
            org.jdom2.output.XMLOutputter outputter = new org.jdom2.output.XMLOutputter();
            outputter.setFormat(org.jdom2.output.Format.getPrettyFormat());;
            outputter.output(V.XML_FILE, writer);
            //V.XML_FILE =null;
        } catch (IOException e) {
            V.XML_FILE = null;
            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, e);
        }
    } //метод

    /**
     * сбор информации об исключении с дальнейшей записью в лог
     *
     * @param exception обрабатываемое исключение
     * @param className класс-источник
     * @param methodName метод-источник
     */
    public static void exceptionToLog(Exception exception, String className, String methodName) {
        String stackText = "";
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            stackText = stackText + stackTraceElement.toString() + "|";
        }
        String message = exception.getMessage();
        String type = exception.getClass().getSimpleName();
        writeLog(type, className, methodName, message, stackText);
    }

    /**
     * возвращает путь к файлу логов. вернет новейший файл из каталога логов.
     * если файлы отстутствуют, вернет сгенерированный
     *
     * @return выбранный путь
     */
    public static String getLogFilePath() {
        File folder = new File(V.LOGS_DIRECTORY.substring(0, V.LOGS_DIRECTORY.length() - 1));
        if (!folder.exists()) {
            folder.mkdir();
        }
        java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String filePath = "";
        Date dateFile = null;
        if (folder.listFiles() != null) {
            for (final File fileEntry : folder.listFiles()) {
                String dateString = fileEntry.getName().replace("LOG_", "");
                try {
                    Date date = dateFormat.parse(dateString);
                    if (dateFile == null || date.after(dateFile)) {
                        dateFile = date;
                        filePath = fileEntry.getPath();
                    }
                } catch (ParseException ex) {
                    //Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(fileEntry.getName() + " не соответствует формату");
                }
            }
            return filePath;
        } else {
            return generateNameLogFile();
        }
    }

    /**
     * возвращает сгенерированный путь к файлу логов. имя файла генерируется по
     * текущей дате.
     *
     * @return полученный путь
     */
    public static String generateNameLogFile() {
        java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        return V.LOGS_DIRECTORY + "LOG_" + dateFormat.format(new Date()) + ".xml";
    }

    /**
     * задает новый XML_FILE и сгенерированный путь fileXmlLog .
     */
    public static void createXmlFile() {
        V.XML_FILE = new Document();
        Element root = new Element("logList");
        V.XML_FILE.setRootElement(root);
        V.fileXmlLog = generateNameLogFile();
    }

    /**
     * Возвращает html формат текста подбирая размер шрифта на ширину
     * максимальную или минимальную
     *
     * @param text - текст
     * @param font - фонт
     * @param bold - жирный 1(да) 0 (нет)
     * @param width - ширина поля
     * @param max - максимальный размер шрифта
     * @param min - минимальный размер шрифта если не влазит при максимальном в
     * with
     * @return
     */
    public static String FONT_STRIKE(String text, String font, int bold, int width, int max, int min) {
        JLabel l = new JLabel();
        l.setFont(new java.awt.Font(font, bold, max));
        if ((l.getFontMetrics(l.getFont()).stringWidth(text)) + 14 > width) {
            return "<style fontName='" + font + "' size='" + min + "' isBold='" + (bold == 1 ? "true" : "false") + "'>" + text + "</style>";
        }
        return "<style fontName='" + font + "' size='" + max + "' isBold='" + (bold == 1 ? "true" : "false") + "'>" + text + "</style>";
        /*
         "<style fontName='DejaVu Sans' size='26' isBold='true'>"+ ($F{CENA2P}).substring(0,($F{CENA2P}).indexOf("."))
         +"</style><style fontName='DejaVu Sans' size='19' isBold='false'>" + ($F{CENA2P}).substring(($F{CENA2P}).indexOf(".")) + "</style>"     
         <style fontName='DejaVu Sans' size='12' isBold='true'>1234567890</style>
         prg.P.FONT_STRIKE($V{art},"DejaVu Sans",1 ,90,12,5)  
         1637087/1      60 1637087
         */
    }

    /**
     * Возвращает html формат текста подбирая максимальный размер шрифта на
     * заданную ширину
     *
     * @param text - текст
     * @param font - фонт
     * @param bold - жирный 1(да) 0 (нет)
     * @param width - ширина поля
     * @param max - максимальный размер шрифта, от него уменьшает пока не влезет
     * @return
     */
    public static String FONT_STRIKE_FLOAT(String text, String font, int bold, int width, int max) {
        JLabel l = new JLabel();
        int size = max;
        while (size > 0) {
            l.setFont(new java.awt.Font(font, bold, size));
            int w = l.getFontMetrics(l.getFont()).stringWidth(text);
            double wp = w * 1.3;
            if (wp >= width) {
                size = size - 1;
            } else {
                break;
            }
        }
        return "<style fontName='" + font + "' size='" + size + "' isBold='" + (bold == 1 ? "true" : "false") + "'>" + text + "</style>";
        //prg.P.FONT_STRIKE_FLOAT($V{art},"DejaVu Sans",1,90,12)
    }
/**
 * Определение максимального размера фонта для текста объекта для поподания на ширину объекта
 * @param text - текст
 * @param font - название фонта например Arial
 * @param bold - жирный 1
 * @param width - ширина
 * @param max - начальный размер
 * @return 
 */
    public static int FONT_SIZE(String text, String font, int bold, int width, int max) {
        JLabel l = new JLabel(); //НА ПРИМЕРЕ МЕТКИ
        int size = max; //НАЧАЛЬНАЯ МАКСИМАЛЬНАЯ ШИРИНА
        while (size > 0) { //
            l.setFont(new java.awt.Font(font, bold, size));//УСТАНАВЛИВАЕМ ФОНТ ДЛЯ МЕТКИ
            int w = l.getFontMetrics(l.getFont()).stringWidth(text);//ОПРЕДЕЛЯЕМ ШИРИНУ ТЕКСТА В МЕТКИ ПО УСТАНОВЛЕННОМУ ФОНТУ
            double wp = w * 1.3; //КОЭФФИЦИЭНТ ШИРИНЫ ТЕКСТА 
            if (wp >= width) {//ЕСЛИ ШИРИНА ТЕКСТА БОЛЬШЕ ШИРИНЫ ОБЪЕКТА
                size = size - 1;//ДЕЛАЕМ РАЗМЕР ФОНТА МЕНЬШЕ
            } else {
                break; // ВСЕ НОРМАЛЬНО РАЗМЕР ФОНТА ПОПОДАЕТ В ШИРИНУ ОБЪЕКТА
            }
        }
        return size;
    }

    /**
     * Возвращает ширину текста в пикселях по заданному шрифту
     *
     * @param text
     * @param font
     * @param bold
     * @param size
     * @return
     */
    public static int FONT_WIDTH(String text, String font, int bold, int size) {
        JLabel l = new JLabel();
        l.setFont(new java.awt.Font(font, bold, size));
        return (l.getFontMetrics(l.getFont()).stringWidth(text) + 16);
    }

    public static String BARCODETOFILE(String SCAN, String TYPE, String FILE) {
        return BARCODETOFILE(SCAN, TYPE, 0f, 0f, 0f, 0f, FILE);
    }

    /**
     * Формирование файла-картинки штрихкода
     *
     * @param SCAN значение штрихкода
     * @param TYPE вид штрихкода
     * @param X
     * @param H высота см
     * @param Y
     * @param W ширина см
     * @param FILE директория
     * @return если создает файл то возвращает путь к файлу
     */
    public static String BARCODETOFILE(String SCAN, String TYPE, float X, float W, float Y, float H, String FILE) {
        if (TYPE.equals(V.BAR_CODE128)) {
            Code128 barcode = new Code128();
            barcode.setData(SCAN);  // данные в штрихкод
            barcode.setUom(IBarcode.UOM_PIXEL); //единица измерения
//	barcode.setY(H); //высота
//	barcode.setX(W); //ширина
//        float h =barcode.getBarcodeHeight();
//        float w =barcode.getBarcodeWidth();
//	barcode.setBarcodeHeight(H); //высота
//	barcode.setBarcodeWidth(W); //ширина
            if (X != 0) {
                barcode.setX(X);
            }
            if (W != 0) {
                barcode.setBarcodeWidth(W);
            }      //ШИРИНА
            if (Y != 0) {
                barcode.setY(Y);
            }//
            if (H != 0) {
                barcode.setBarcodeHeight(H);
            } //высота  
            // barcode image margins
            barcode.setLeftMargin(0f);
            barcode.setRightMargin(0f);
            barcode.setTopMargin(0f);
            barcode.setBottomMargin(0f);
            barcode.setResolution(200);// разрешение
            barcode.setShowText(true); // ПОКАЗЫВАТЬ ЗНАЧЕНИЕ кода
            barcode.setTextFont(new java.awt.Font("Arial", 0, 12)); // фонт текста
            //barcode.setTextMargin(6); //расстояние между штрихкодом и текстом 
            barcode.setRotate(IBarcode.ROTATE_0); //разворот
            try {
                if (barcode.drawBarcode(FILE) == false) {
                    P.MESS("Ошибка создания штрихкода");
                    return null;
                }
            } catch (Exception ex) {
                return null;
            }
        }

        if (TYPE.equals(V.BAR_CODE93)) {
            Code93 barcode = new Code93();
            barcode.setData(SCAN);  // данные в штрихкод
            barcode.setUom(IBarcode.UOM_PIXEL); //единица измерения
//	barcode.setY(H); //высота
//	barcode.setX(W); //ширина
//        float h =barcode.getBarcodeHeight();
//        float w =barcode.getBarcodeWidth();
//	barcode.setBarcodeHeight(H); //высота
//	barcode.setBarcodeWidth(W); //ширина
            if (X != 0) {
                barcode.setX(X);
            }
            if (W != 0) {
                barcode.setBarcodeWidth(W);
            }      //ШИРИНА
            if (Y != 0) {
                barcode.setY(Y);
            }//
            if (H != 0) {
                barcode.setBarcodeHeight(H);
            } //высота  
            // barcode image margins
            barcode.setLeftMargin(0f);
            barcode.setRightMargin(0f);
            barcode.setTopMargin(0f);
            barcode.setBottomMargin(0f);
            barcode.setResolution(200);// разрешение
            barcode.setShowText(true); // ПОКАЗЫВАТЬ ЗНАЧЕНИЕ кода
            barcode.setTextFont(new java.awt.Font("Arial", 0, 12)); // фонт текста
            //barcode.setTextMargin(6); //расстояние между штрихкодом и текстом 
            barcode.setRotate(IBarcode.ROTATE_0); //разворот
            try {
                if (barcode.drawBarcode(FILE) == false) {
                    P.MESS("Ошибка создания штрихкода");
                    return null;
                }
            } catch (Exception ex) {
                return null;
            }
        }
////////////////////////////////    //QRCode     
        if (TYPE.equals(V.BAR_QRCode)) {
            QRCode barcode = new QRCode();
            barcode.setData(SCAN);  // данные в штрихкод
            barcode.setUom(IBarcode.UOM_PIXEL); //единица измерения
//	barcode.setY(H); //высота
//	barcode.setX(W); //ширина
//        float h =barcode.getBarcodeHeight();
//        float w =barcode.getBarcodeWidth();
//	barcode.setBarcodeHeight(H); //высота
//	barcode.setBarcodeWidth(W); //ширина
            if (X != 0) {
                barcode.setX(X);
            }
            if (W != 0) {
                barcode.setBarcodeWidth(W);
            }      //ШИРИНА
            if (H != 0) {
                barcode.setBarcodeHeight(H);
            } //высота  
            // barcode image margins
            barcode.setLeftMargin(0f);
            barcode.setRightMargin(0f);
            barcode.setTopMargin(0f);
            barcode.setBottomMargin(0f);
            barcode.setResolution(200);// разрешение
            barcode.setRotate(IBarcode.ROTATE_0); //разворот

            try {
                if (barcode.drawBarcode(FILE) == false) {
                    P.MESS("Ошибка создания штрихкода");
                    return null;
                }
            } catch (Exception ex) {
                return null;
            }
        }

        //QRCode
        return FILE;
//            Code128 barcode128 = new Code128(); 
        //           EAN13 barcode13 = new EAN13(); 
//http://www.onbarcode.com/java/code-93-generator.html                          

    }

    /**
     * Формирование класса Image содержащий в картинке штрихкод QRCODE
     *
     * @param SCAN - значение штрихкода
     * @return
     */
    public static Image QRCODE(String SCAN) {
        QRCode barcode = new QRCode();
        barcode.setData(SCAN);  // данные в штрихкод
        barcode.setResolution(200);
        try {
            return barcode.drawBarcode();
        } catch (Exception ex) {
            return null;
        }
    }
//DataMatrix http://www.onbarcode.com/products/java_barcode/barcodes/data_matrix.html
    /**
     * Формирование класса Image содержащий в картинке штрихкод DataMatrix
     *
     * @param SCAN - значение штрихкода
     * @param FNC1 - если 1 то первый символ ACSSI232 для encode GS1 compatible
     * @return
     */
    public static Image DataMatrix(String SCAN,int FNC1,int DPI) {
      //for (int i = 0; i < 256; i++) {
      //  System.out.println(i + " -> " + (char) i);
    //}        
        DataMatrix barcode = new DataMatrix();
        barcode.setDataMode(1); //ASCII
        if (FNC1==1) {
         String FNC= Character.toString((char)232);
         SCAN=FNC+SCAN;
        }
     //   FF._CLIPTEXT(SCAN);
        barcode.setData(SCAN);  // данные в штрихкод
        barcode.setResolution(DPI);
    //    barcode.setFnc1Mode(IBarcode.FNC1_ENABLE);
        Image IM=null;
        try {
          //barcode.drawBarcode("d:/barcode.jpg");             
            IM= barcode.drawBarcode();
            return IM;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    
    /**
     * Формирование класса BufferedImage содержащий в картинке штрихкод QRCODE
     *
     * @param SCAN - значение штрихкода
     * @param w - ширина штрихкода
     * @param h - высота штрихкода
     * @return
     */
    public static BufferedImage QRCODE(String SCAN, float w, float h) {
        QRCode barcode = new QRCode();
        barcode.setResolution(200);
        barcode.setData(SCAN);  // данные в штрихкод
        barcode.setBarcodeWidth(w);
        barcode.setBarcodeHeight(h);
        barcode.setAutoResize(true);
        try {
            return barcode.drawBarcode();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Конвертация типа blob в Image
     *
     * @param blob
     * @return
     */
    public static Image BlobToImage(Blob blob) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(blob.getBytes(1L, (int) blob.length()));
            return icon.getImage();

        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     *
     * Отчет в файлы картинок по странично
     *
     * @param i - номер страницы
     * @param FILE - имя файла
     * @param koef - коэфициент увеличения картинки
     * @return
     */
    public static boolean ReportToImage(int i, String format, String FILE, float koef) {
        /*        
         PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
         attrs.add(new PrinterResolution(203, 203, ResolutionSyntax.DPI));
         JRPrintServiceExporter exporter = new JRPrintServiceExporter(); 
         exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, attrs); 
         exporter.setParameter(JRExporterParameter.JASPER_PRINT, V.JP); 
         exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, pageImage.getGraphics());
         exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
         exporter.exportReport();
         ImageIO.write(pageImage, "jpeg", out);
         */
        BufferedImage image = new BufferedImage((int) (V.JP.getPageWidth() * koef), (int) (V.JP.getPageHeight() * koef), BufferedImage.TYPE_INT_RGB); //буфер для картинки
        JRGraphics2DExporter exporter = null;
        try {
            exporter = new JRGraphics2DExporter();
        } catch (JRException ex) {
            P.MESSERR("Ошибка 1 \n" + ex.getMessage());
            return false;
        }

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, V.JP); //отчет привязан
        exporter.setParameter(JRExporterParameter.PAGE_INDEX, i); //номер страницы
        exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, (Graphics2D) image.getGraphics()); //приваязка буфера картинки
        exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, koef); //коэфициент увеличения
        PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
        attrs.add(new PrinterResolution(300, 300, ResolutionSyntax.DPI)); // разрешение не работает
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, attrs);

        try {
            exporter.exportReport();
        } catch (JRException ex) {
            P.MESSERR("Ошибка 2 \n" + ex.getMessage());
            return false;
        }
        try {
            ImageIO.write(image, format, new File(FILE));        //буфер картинки в файл
//                        Image capt= JasperPrintManager.printPageToImage(V.JP,i, 1);
//                        ImageIO.write((RenderedImage)capt, format,new File(FILE));                        
//            } catch (JRException ex) {
//            P.MESSERR("Ошибка формирования образа \n"+ex.getMessage());
//                return false;
        } catch (JRRuntimeException ex) {
            P.MESSERR("Ошибка формирования образа \n" + ex.getMessage());
            return false;
        } catch (Exception ex) {
            P.MESSERR("Ошибка формирования рисунка отчета \n" + ex.getMessage());
            return false;
        }
        return true;
        /**
         * ПРИМЕР ВЫЗОВА Reportr.INIT(V.SELE); //запрос в отчете
         * Reportr.RUN("ST_LITFORM"); //запуск отчета for (int
         * i=0;i<V.JP_SIZE;i++) { A.GOTO("UD", i+1); if
         * (!P.ReportToImage(i,"jpg",DIR+A.GETVALS("UD.SCAN")+".jpg",Float.valueOf(2.9f))
         * ) { return ; } }
         *
         */
    }

    public static void serialListen(String portNum) {
        V.RADIO_PORT = new SerialPort(portNum);
        try {
            V.RADIO_PORT.openPort();
            V.RADIO_PORT.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//            V.RADIO_PORT.setEventsMask(SerialPort.MASK_RXCHAR);
            V.RADIO_PORT.addEventListener(new SerialListener());
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    //перевыборка грида из другой формы
    public static void updateDistGrid(String fName, int gNum) {
        Formr f = P.GETFORM(fName);
        f.QUERY(f.G[gNum]);
    }

    /**
     * RKV 27/10/2017 Импрот данных из полсистемы с соединением CONN в
     * действующую по умолчанию
     *
     * @param CONN -соединение
     * @param SELE - запрос на выборку
     * @param DEL - запрос на удаление в результате
     * @param TABLETO - таблица результата
     * @param pr_mess - показывать сообщения процесса true или нет false
     * @return
     */
    public static boolean IMPORT_EXEC(Connection CONN, String SELE, String DEL, String TABLETO, boolean pr_mess) {
        int kolp = 0, jj = 0, ii = 0;
        Object obj;
        String INS1 = "", INS2 = "", INS = "";
        String d1 = FF.DATETIMES();
        ResultSet rs;

        ////
        try {
            if (pr_mess) {//если выдача сообщений
                P.ALERT("Запрос к данным...");
            }
//           if (P.SQL_CREATE_STATEMENT(CONN)==false) { return false ;} // создание канала с сервером
            Statement SQL = CONN.createStatement();
            rs = SQL.executeQuery(SELE); //выполнение запроса
            kolp = rs.getMetaData().getColumnCount();
            if (pr_mess) {//если выдача сообщений
                P.ALERT("Удаление данных в принимаемой таблице...");
            }
            V.SELE = "";
            V.SELE = V.SELE + "BEGIN " + "\n";
            V.SELE = V.SELE + DEL + " ;" + "\n";
            V.SELE = V.SELE + "END; ";
            if (P.SQLEXECUT(V.SELE) != true) {
                return false;
            } //УДАЛЕНИЕ В ПРИНИМАЕМОЙ ТАБЛИЦЕ

            SELE = "";
            if (pr_mess) {//если выдача сообщений
                P.ALERT("Выборка из источника и передача в таблицу результата...");
            }
            while (rs.next()) {
                ii++;
                INS1 = "";
                INS2 = "";
                for (int i = 1; i <= kolp; i++) { //по всем полям таблицы 
                    //  type= rs.getMetaData().getColumnType(i);
                    INS1 = INS1 + "," + rs.getMetaData().getColumnName(i);
                    obj = rs.getObject(i);
                    INS2 = INS2 + "," + P.P_SQL(obj);
                } //for
                INS = "INSERT INTO " + TABLETO + " (" + FF.SUBSTR(INS1, 2) + ") VALUES (" + FF.SUBSTR(INS2, 2) + ");";
                SELE = SELE + INS + "\n";
                jj++;
                if (jj == 500) {
                    if (pr_mess) {//если выдача сообщений
                        P.ALERT("Заполнение данных...Начало:" + d1 + " Записей " + ii);
                    }
                    jj = 0;
                    V.SELE = "";
                    V.SELE = V.SELE + "BEGIN " + "\n";
                    V.SELE = V.SELE + SELE + " " + "\n";
                    V.SELE = V.SELE + "END; ";
                    if (P.SQLEXECUT(V.SELE) != true) {
                        return false;
                    } //
                    SELE = "";
                }
            } //while
            rs.close();
            SQL.close();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            P.MESSERR("Ошибка выполнения операции \n" + ex.toString());
            return false;
        }
        if (jj > 0) { //ЕСЛИ ЕСТЬ ДАННЫЕ НА ВСТАВКУ В SELE
            V.SELE = "";
            V.SELE = V.SELE + "BEGIN " + "\n";
            V.SELE = V.SELE + SELE + " " + "\n";
            V.SELE = V.SELE + "END; ";
            if (P.SQLEXECUT(V.SELE) != true) {
                return false;
            } //
        }
        P.ALERT("");

        return true;
    }

    //KAA 21/11/2017 Проверка на существование открытой формы обмена данными
    public static boolean exchangeCloseCheck() {
        if (V.ARM == 3 || V.ARM == 2) {
            Iterator entries = V.MAPFORMS.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) entries.next();
                Formr form = (Formr) thisEntry.getValue();
                if (form.getName().equals("RI_JOB")) {
                    P.MESSERR("Выполняется обмен данными! \nПожалуйста, дождитесь его окончания!");
                    V._SCREEN.setDefaultCloseOperation(0);
                    return false;
                }
            }
        }
        return true;
    }
/**
 * Выполнение метода из заданного класса
 * @param Sclass - класс
 * @param Smethod - метод
 * @param params  - параметры передаваемые в метод через ~  пример 7~2
 * @return 
 */
    public static boolean EXEC(String Sclass, String Smethod, String params) {
        V.ERROR_MES = "";
        boolean Bexec = false;
        Object[] param = null;
        Object result = null;
        String type_out = "";
        Class cls;
        try {
            cls = Class.forName(Sclass);

            //cls2 = cls1.getClass()                        ;
        } catch (ClassNotFoundException ex) {//без этого не работает Class.forName(pathform)
            V.ERROR_MES = "Ошибка загрузки класса: " + Sclass + "   " + ex.toString();
            System.out.println(ex.toString());
            return false;
        }
        Method[] methods = cls.getDeclaredMethods(); // возвращаем все методы класса не зависимо от типа доступа
        for (Method method : methods) {
            if (method.getName().equals(Smethod)) {  // сравниваем имя метода
                System.out.println("Имя: " + method.getName());
                System.out.println("Возвращаемый тип: " + method.getReturnType().getName());

                Class[] paramTypes = method.getParameterTypes(); // берем типы параметров метода
                param = new Object[paramTypes.length]; // массив параметров который заполним из строки params
                System.out.println("Типы параметров: ");
                Class paramType = null;
                String str = null;
                int ind = 0;
                for (int i = 0; i < paramTypes.length; i++) {
                    paramType = paramTypes[i];
                    type_out = paramType.getName();
                    System.out.println(" " + paramType.getName()); // выводим имя типа
                    //выделяем символьное значение параметра
                    ind = params.indexOf("~", ind);
                    if (ind > 0) {
                        str = params.substring(0, ind);
                        params = params.substring(ind + 1);
                    } else {
                        str = params;
                    }
                    //Приводим к нужному типу
                    if (paramType.getName().indexOf("String") >= 0) {
                        param[i] = str;
                    }
                }

                System.out.println();
                System.out.println("------------------------");
                try {
                    Bexec = true; //что то выполнялось                       
                    result = method.invoke(cls.newInstance(), param);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    V.ERROR_MES = "Ошибка выполнения метода: " + Smethod + "   " + ex.toString();
                    System.out.println(ex.toString());
                    return false;
                }
                break;
            }
        }
        //cls.getMethod(Smethod);
        if (!Bexec) {
            V.ERROR_MES = "Нет запуска на выполнение метода: " + Smethod + " Возможно неправильно описан ";
            System.out.println(V.ERROR_MES);
        }
        if (result != null) { // если метод возвращает результат
            if (type_out.indexOf("Boolean") >= 0) { // если результат булев
                if ((boolean) result == false) { // если результат false
                    V.ERROR_MES = "Метод вернуд false.  " + V.ERROR_MES;
                    System.out.println(V.ERROR_MES);
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * Проверка на наличие параметра в ri_system по обновлению программы
     *
     * @return
     */
    public static boolean programmUpdateCheck() {
        SQLEXECT("SELECT * FROM dba_tables where table_name = 'RI_SYSTEM'", "RI_SYST_COUNT");
        if (A.RECCOUNT("RI_SYST_COUNT") == 0) {
            return true;
        }
        SQLEXECT("select nvl(max(sys_val),'T') sys_val from ri_system where sys_var = 'PROGRAMM_UPDATE'", "SYST_PRG_UPD");
        if (A.GETVALS("SYST_PRG_UPD.SYS_VAL").equals("F")) {
            SQLUPDATE("update ri_system set sys_val = 'T' where sys_var = 'PROGRAMM_UPDATE'");
            return false;
        }
        return true;
    }
/**
 * Запись системных свойств в файл 
 * @param file - имя файла
 * @param inf - дополнительная информация
 * @return 
 */
    public static boolean System_getProperties_to_file(String file,String inf)  {
        Properties prop= System.getProperties();
        try { 
            FileWriter writer = new FileWriter(file);
             writer.write("Конфигурация при запуске "+FF.DATETIMES()+"\n"); //пишем время создания
         //   for (int i=0;i<prop.size();i++) {
             String str=System.getProperties().toString(); // все свойства в строку
             str=str.replaceAll(", ", "\n"); // заменить на перевод строки
             writer.write(str+"\n"); 
             writer.write(inf); 
             writer.close();
          //  }
            
        } catch (IOException ex) {
                         System.out.println("Ошибка создания файла системных свойств"+ex.toString()); 
                         return false;
        }
        return true;
}
   /**
    * Проверка правильности пути и возвращение правильного с \ в конце
    * @param path - путь
    * @return 
    */ 
    public static String DirValid(String path) {
        String SEP=System.getProperty("file.separator");
        path = path.replace("/", SEP); // заменить разделители каталогов на системный
        if (path.substring(path.length()-1) != null && SEP != null && !path.substring(path.length()-1).equals(SEP)) {
            path=path+SEP;
        }
        return path;
    }
    
    

/**
 * Расчет контрольной суммы  
 * @param bytes - байтовая последовательность
 * @return  два байта 
 */
    public static byte[] CRC16BUYPASS(byte[] bytes) { 
      CrcCalculator calculator = new CrcCalculator(Crc16.CRC_16_BUYPASS);
      long crc=calculator.calc(bytes);
          //    System.out.println(calculator.calc(bytes));
      byte [] crcb  = ByteBuffer.allocate(2).putShort((short)crc).array() ; //длинна значения поля 2 байта 
      return crcb;
    }        
    /*
        byte [] b={0x02,0x05,0x00,0x19,0x02,0x00,0x32,0x31 }; //020501902003231
        P.CRC16(b);
        P.CRC16_1(b);
        P.CRC16BUYPASS(b);

    */
    
/**
 * Приобразование байтовой последовательности в строкку 16-го формата
 * @param a - байтовый массив
 * @return 
 */ 
public static String byteArrayToHex(byte[] a) {
   StringBuilder sb = new StringBuilder(a.length * 2);
   for(byte b: a)
      sb.append(String.format("%02x", b)+" ");
   return (sb.toString()).toUpperCase();
}   
/**
 * GET запрос на WEB сервис 
 * @param URL_STR   URL запроса  http://pos.vit.belwest.com:8084//Service_BW//MainPostal
 * @param Param     строка парметров  Query=035&ShopId=999&P1=MPU001~ПРОВЕРКА~12-34-56-78-90-12
 * @param AUTH строка аунтификации  "Basic "+P.encodeBase64("dataapp:Aif8ahneichu")
 */
public static String WEB_GET(String URL_STR,String Param,String AUTH) {
             
             URLConnection CONN_SERV=null;
             URL url;
             //String URL_STR="http://" + V.CONN_SERV_IP + ":" + V.CONN_SERV_PORT + "//" + V.CONN_SERV_NAME + "//" +NameSrv ;  // строка соединения с сервисом
             //http://pos.vit.belwest.com:8084//Service_BW//MainPostal
             try {
             System.out.println(URL_STR);
             url = new URL(URL_STR); //создание URL 192.168.2.121:8084/Service_BW/SendToSap
             } catch (MalformedURLException ex) {
                 P.MESSERR("Ошибка создания URL /n"+URL_STR+"/n"+ex.toString());
                 return "@ERR";
             }
             try {
                 CONN_SERV = url.openConnection();
             } catch (IOException ex) {
                 P.MESSERR("Ошибка соединения с сервисом /n"+URL_STR+"/n"+ex.toString());
                 return "@ERR";
             }
             if (AUTH!=null && !AUTH.equals("")) {
              CONN_SERV.setRequestProperty("Authorization", AUTH);
             }
             CONN_SERV.setConnectTimeout(4000);
             CONN_SERV.setDoInput(true); //разрешено принимать
             CONN_SERV.setDoOutput(true); // разрешено посылать 
             CONN_SERV.setUseCaches(false); // не используем кэш
     //      con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
             CONN_SERV.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // свойсва
       //      } //V.CONN_SERV==null
             
             //посылаем
             String outStr=Param;     //&Query=035&ShopId=999&P1=MPU001~ПРОВЕРКА~12-34-56-78-90-12" ;
             //&Query=035 - сервоет по данному ключу вызывает хранимую RKV_ALL  &ShopId=999 - первый параметр этой процедуры (не нужен в нашей задаче но должне быть)
             // &P1="+"MPU001~ПРОВЕРКА~"+ V.MAC - второй парметр  MPU001 - код операции в case ~ПРОВЕРКА~...... ПАРАМЕТРЫ РАЗДЕЛЕННЫЕ ~
             //ЧЕРЕЗ БРАУЗЕР http://pos.vit.belwest.com:8084//Service_BW//MainPostal?&Query=035&ShopId=999&P1=MPU001~ПРОВЕРКА~12-34-56-78-90-12
             System.out.println(outStr);
            OutputStream outstream=null; 
            if (!Param.equals("")) {
             try {
                 outstream = CONN_SERV.getOutputStream(); //выходной подок
             } catch (IOException ex) {
                 P.MESSERR("Ошибка создание выходного потока /n"+ex.toString());
                 CONN_SERV=null;
                 return "@ERR";
             }
             try {
                 outstream.write(outStr.getBytes("utf-8")); // пишем строку
                 outstream.flush(); 
                 outstream.close();
             } catch (IOException ex) {
                 P.MESSERR("Ошибка записи в выходной поток /n"+ex.toString());
                 CONN_SERV=null;
                 return "@ERR";
             }
            }
            //читаем
            String result;
            InputStream instr=null;
             try {
                 instr = CONN_SERV.getInputStream();
             } catch (IOException ex) {
                 P.MESSERR("Ошибка создание входного потока /n"+ex.toString());
                 CONN_SERV=null;
                 return "@ERR";
             }
            InputStreamReader isr=null;
             try {
                 isr = new InputStreamReader(instr, "utf-8");
             } catch (UnsupportedEncodingException ex) {
                 P.MESSERR("Ошибка задания чтения выходного потока /n"+ex.toString());
                 CONN_SERV=null;
                 return "@ERR";
             }
            BufferedReader buff = new BufferedReader(isr);
            StringBuilder strBuff = new StringBuilder(); //для добавление в строку
            String s;
             try {
                 while ((s = buff.readLine()) != null) { //если в буффере есть символы
                     strBuff.append(s); //добавляем к строке
                 }} catch (IOException ex) {
                 P.MESSERR("Ошибка  чтения выходного потока /n"+ex.toString());
                 CONN_SERV=null;
                 return "@ERR";
             }
//String respCode = CONN_SERV.getResponseCode(); //возвращение кода выполнения
//String mess=CONN_SERV.getResponseMessage(); //возвращение сообщение  выполнения
             
             try {
                 buff.close();
                 isr.close();
             } catch (IOException ex) {
                 P.MESSERR("Ошибка закрытия выходного потока /n"+ex.toString());
                 CONN_SERV=null;
                 return "@ERR";
             }
            return strBuff.toString();
}

public static boolean WEB_GET_FILE(String FILE_URL,String FILE_NAME,String AUTH) {
             
             URLConnection CONN_SERV=null;
             URL url;
             try {
             //System.out.println(URL_STR);
             url = new URL(FILE_URL); //создание URL 192.168.2.121:8084/Service_BW/SendToSap
             } catch (MalformedURLException ex) {
                 P.MESSERR("Ошибка создания URL /n"+FILE_URL+"/n"+ex.toString());
                 return false;
             }
             try {
                 CONN_SERV = url.openConnection();
             } catch (IOException ex) {
                 P.MESSERR("Ошибка соединения с сервисом /n"+FILE_URL+"/n"+ex.toString());
                 return false;
             }
             if (AUTH!=null && !AUTH.equals("")) {
              CONN_SERV.setRequestProperty("Authorization", AUTH);
             }
             CONN_SERV.setConnectTimeout(4000);
             CONN_SERV.setDoInput(true); //разрешено принимать
             CONN_SERV.setDoOutput(true); // разрешено посылать 
             CONN_SERV.setUseCaches(false); // не используем кэш
             BufferedInputStream in=null;             
             FileOutputStream fileOutputStream=null;             
try {
       in = new BufferedInputStream(CONN_SERV.getInputStream());
       fileOutputStream = new FileOutputStream(FILE_NAME) ;
    byte dataBuffer[] = new byte[1024];
    int bytesRead;
    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        fileOutputStream.write(dataBuffer, 0, bytesRead);
    }
       fileOutputStream.close();
       in.close();
} catch (IOException ex) {
                 P.MESSERR("Ошибка потока "+"/n"+ex.toString());
                 return false;
}             

            return true;
}

    public static String encodeBase64(String sourceString) {
        //return new String(Base64.encodeBase64(sourceString.getBytes()));
        return new String(Base64.encodeBase64(sourceString.getBytes()));
    }

/**
     * Запись или удаление бинарного файла на web server
 * @param METOD - метод запроса (POST DELETE) 
 * @param URLS - путь (https://datarep.vit.belwest.com/file-manager/file)
 * @param file - объект файл   (File file = new File(FILE)  )
 * @param AUTH - строка аунтификацмм ("Basic "+P.encodeBase64("dataapp:Aif8ahneichu") )
 * @param path - каталог на сервере ("fold1/fold2/fold3")
 * @param filename - имя файла на  сервере ("test.txt")
 * @return 
 */    
 public static String WEB_FILE(String  METOD, String URLS,File file,String AUTH,String path,String filename)  {
//     File file = new File(FILE);
     URL url=null;
        try {
            url = new URL(URLS); //из строки пути url объект
        } catch (MalformedURLException ex) {
                 System.out.println("Ошибка URL "+URLS+"\n"+ex.toString());
                 return "@ERR";
        }
    HttpURLConnection connection=null;
        try {
            connection = (HttpURLConnection) url.openConnection(); // соединение
        } catch (IOException ex) {
                 System.out.println("Ошибка соединения "+ex.toString());
                 return "@ERR";
        }
connection.setRequestProperty("Authorization", AUTH);
String boundary = UUID.randomUUID().toString(); // случайный набор
connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary); //способ передачи  данных с началом и концом boundary
        try {
            connection.setRequestMethod(METOD); // метод "POST" "DELETE"
        } catch (ProtocolException ex) {
                 System.out.println("Ошибка setRequestMethod "+METOD+" "+ex.toString());
                 return "@ERR";
        }
DataOutputStream request=null;
connection.setDoOutput(true);
        try {
            request = new DataOutputStream(connection.getOutputStream()); // выходной поток
            //               outstream.write(outStr.getBytes("utf-8")); // пишем строку
        } catch (IOException ex) {
                 System.out.println("Ошибка открытия потока "+ex.toString());
                 return "@ERR";
        }
int respCode=0; String mess="";
        try {
String str="";            
if (METOD.equals("POST")) { // ЕСЛИ МЕТОД POST ТО ПОСЫЛАЕМ ДАННЫЕ НА  СОЗДАНИЕ ФАЙЛА
if (path!=null && !path.equals("")) {  // если задан путь передаем тело с переменной path и ее значением
request.writeBytes("--" + boundary + "\r\n"); //начало тела
str="Content-Disposition: form-data; name=\"path\"" + "\r\n\r\n" ; // объявление path
request.writeBytes(str);
request.writeBytes(path); //передача значения
request.writeBytes("\r\n");
}
request.writeBytes("--" + boundary + "\r\n");
str="Content-Disposition: form-data; name=\"file\";  filename=\"" +filename + "\"\r\n\r\n" ;
request.writeBytes(str);
// НУЖНО КИНУТЬ БАЙТОВУЮ ПОСЛЕДОВАТЕЛЬНОСТЬ ФАЙЛА
byte[] fbyte = Files.readAllBytes(file.toPath());
request.write(fbyte);
request.writeBytes("\r\n");
request.writeBytes("--" + boundary + "--\r\n");
}
request.flush();

respCode = connection.getResponseCode(); //возвращение кода выполнения
mess=connection.getResponseMessage(); //возвращение сообщение  выполнения
connection.disconnect();
        } catch (IOException ex) {
                 System.out.println("Ошибка записи данных в поток или получения результата \n "+ex.toString());
                 return "@ERR";
        }

                 return FF.STR(respCode)+" "+mess;
/* Пример вызова
            String  FILE_ST= P.GETFILE("Выберите файл","","",V.CURRENT_PATH);
             if ("".equals(FILE_ST)) 
             {                 return;             }
             File file = new File(FILE_ST);
             String URLS="https://datarep.vit.belwest.com/file-manager/file";
             String AUTH="Basic "+P.encodeBase64("dataapp:Aif8ahneichu"); 
             String out=P.WEB_FILE("POST", URLS, file,AUTH,"RKV",file.getName());
//           дляудаления  String out=P.WEB_FILE("DELETE", "https://datarep.vit.belwest.com/file-manager/RKV/TEST8.TXT", file,AUTH,"RKV",file.getName());
           P.MESS(out);
*/     
 }
/**
 * Снимок окна экрана или приложения в буфер обмена и файл screen.png
 * @param tip - 0 - экран 1 - окно приложения
 * @return 
 */ 
public static String _SCREEN_TO_FILE(int tip) {
    //https://habr.com/ru/sandbox/67456/
    //http://qaru.site/questions/52587/is-there-a-way-to-take-a-screenshot-using-java-and-save-it-to-some-sort-of-image
    Dimension dime=null;
    Rectangle rect=null;    
   if (tip==0) {
    dime=  Toolkit.getDefaultToolkit().getScreenSize()  ;
    rect= new Rectangle(dime);
   }
   if (tip==1) {
    rect=  V._SCREEN.getBounds()  ;
   }
   
   BufferedImage  image=null;
        try {
              image= new Robot().createScreenCapture(rect);
        } catch (AWTException ex) {
                 System.out.println("Ошибка снимка экрана \n "+ex.toString());
                return null;
        }
       File file= new File("screen.png");
        try {
            ImageIO.write(image,"png",file )  ;
        } catch (IOException ex) {
                 System.out.println("Ошибка записи файла снимка экрана \n "+ex.toString());
                 return null;
        }
     FF._CLIPTEXT(image);
    return file.getAbsolutePath();
}
   
public static void prn(String str) {
  System.out.println(str);
}

} //КОНЕЦ P


    

