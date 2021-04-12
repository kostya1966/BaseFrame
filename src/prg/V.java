package prg;

import aplclass.FiscalPrinter;
import aplclass.Tablo;
import baseclass.Cursorr;
import baseclass.Formr;
import baseclass.Gridr;
import baseclass.Screen;
import aplclass.ToolBarr;
import baseclass.Configs;
import baseclass.Crypt;
import baseclass.Textr;
import client.Post;
import java.awt.Color;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import jssc.SerialPort;
import net.sf.jasperreports.engine.JasperPrint;
import org.jdom2.Document;

/**
 * Класс глобальных переменных
 *
 * @author Kostya
 */
public class V {

    public static int ARM = 1; //НОМЕР ПРОЕКТА 1- FIRM_TRADE 2-SHOP 3-KASSA 4- DIST_CENTER
    public static int HELP = 0; //Поддерживается ли проектом справка: 0-Нет, 1-Да
    public static double VER = 2.1; //НОМЕР ВЕРСИИ ПРОГРАММЫ    
    public static double VER_BASE = 1; //НОМЕР ВЕРСИИ ПРОГРАММЫ    
    public static String NVER = " "; //НАЗВАНИЕ ВЕРСИЯ ПРОГРАММЫ
    public static String VER_INFO = ""; //ДОПОЛНИТЕЛЬНАЯ ИНФОРМАЦИЯ ПО ВЕРСИИ
    public static String VER_CHANGE = ""; //ИЗМЕНЕНИЯ ПО ВЕРСИЯМ
    public static String[] PATHFORM;  //ПУТЬ К ДОПОЛНИТЕЛЬНЫМ ПАПКАМ ДЛЯ ВЫЗОВА ФОРМ  пример V.PATHFORM=new String[]{"forms.CONVE", "forms.LITFORM"};

//Расположение объектов    
    public static int X1 = 1;
    public static int X2 = 2;
    public static int Y1 = 3;
    public static int Y2 = 4;
    public static final int LOC_IN = 0;
    public static final int LOC_OUT = 1;
    public static final int LOC_CENTR = -1;
    public static final int LOC_LEFT = -2;
    public static final int LOC_RIGHT = -3;
    public static final int LOC_UP = -4;
    public static final int LOC_DOWN = -5;
    public static final int LOC_COR_LU = -6; //УГОЛ ЛЕВЫЙ ВЕРХНИЙ
    public static final int LOC_COR_RU = -7; //УГОЛ ПРАВЫ ВЕРХНИЙ
    public static final int LOC_COR_LD = -8; //УГОЛ ЛЕВЫЙ НИЖНИЙ
    public static final int LOC_COR_RD = -9; //УГОЛ ПРАВЫ НИЖНИЙ

    public static int LOC_SPACE = 5;
    public static int LOC_HEADRE = 30;
    public static int LOC_BORDER = 5;
//Рабочие ссылки
    public static Screen _SCREEN;  //Основное окно
    public static JDesktopPane DESKTOP;
    public static ToolBarr TOOLBAR;

    public static int MODAL = 1;    //модальная форма
    public static int MODAL_NOT = 0;    //немодальная форма
    public static Formr FBROWSE; //форма данных
    public static Formr FSTRU;   //форма структуры
    public static String SXEMA;   //ИМЯ СХЕМЫ
    /*ВКЛЮЧЕНИЕ ВЫКЛЮЧЕНИЕ БАЗОВЫХ ФУНКЦИЙ*/
    public static boolean ON_TOMCAT = true;
    public static boolean ON_CONN = true;

    /*РАБОЧИЕ ПОЛЯ ДЛЯ ПЕРЕНОСА ЗНАЧЕНИЙ */
    public static Formr CALENDAR_FORM; //форма, с которой последний раз вызывался календарь
    public static String SHOP_ID_HIER;   //ид магазина для добавления в иерархию розничной сети
    public static String REGION_RETAIL = "RU";//для добавления в иерархию розничной сети
    public static String TITLE = "";    //заголовок задачи
    public static String PARAM_FMOD;    //параметры для модальной формы
    public static boolean FALSETRUE;    //Глобальная булевая переменная
    public static String CAPTION[];    // список заголвка
    public static String FIELD[];      // список полей
    public static int FSIZE[];      // список размера колонок по умолчанию 
    public static ArrayList<String> MONITOR_ARR = new ArrayList<String>();
    public static final int CLASS_BUTTOMR = 1;
    public static final int CLASS_LABELR = 2;
    public static final int CLASS_TEXTR = 3;
//Клавиатура
    public static final int KEY_ENTER = 10;
    public static final int KEY_LEFT = 37;
    public static final int KEY_RIGTH = 39;
    public static final int KEY_UP = 38;
    public static final int KEY_DOWN = 40;
    public static final int KEY_ESC = 27;
    public static final int KEY_F1 = 112;
    public static final int KEY_F2 = 113;
    public static final int KEY_F3 = 114;
    public static final int KEY_F4 = 115;
    public static final int KEY_F5 = 116;
    public static final int KEY_F6 = 117;
    public static final int KEY_F7 = 118;
    public static final int KEY_F8 = 119;
    public static final int KEY_F9 = 120;
    public static final int KEY_F10 = 121;
    public static final int KEY_F11 = 122;
    public static final int KEY_F12 = 123;
    public static final int KEY_DEL = 127;
    public static final int KEY_INS = 155;
//Типы данных
    public static final String TYPE_CHAR = "C";
    public static final String TYPE_NUMERIC = "N";
    public static final String TYPE_BIG_NUM = "BN";
    public static final String TYPE_DATE = "D";
    public static final String TYPE_DATETIME = "T";
    public static final String TYPE_BIT = "B";
    public static final String TYPE_BLOD = "BL";
    public static final String TYPE_CLOD = "CL";

    public static final boolean FALSE = false;
    public static final boolean TRUE = true;
//Данные и формы
    public static Map MAPFORMS = null; //карта колекция загруженных форм
    public static Map MAPALIAS = null; //карта колекция текущих курсоров
    public static Formr ACTIVEFORM = null;    //текущая активная форма
    public static Formr LASTFORM = null;    //последняя закруженая форма
    public static Gridr ACTIVEGRID = null;    //текущий активный грид
    public static String ACTIVEALIAS = null; //текущий курсор (алиас)
//соединение SqlServer
    public static String CONN_USER = "SHOP";
    public static String CONN_PASS = "";
    public static String CONN_SERVER = "192.168.1.9";
    public static String CONN_BASA = "xe";
    public static String CONN_DRIVER = "1";
    public static String CONN_PORT = "1521";
    public static String CONN_NAME = "DBCONNECT";
    public static int CONN_RECCOUNT = 1000000;
    public static boolean CONN_AUTO = false;
    public static boolean CONN_OUT = true;
    public static int CONN_REP = 0;
    public static String LAST_ERR = "";  //ПОСЛЕДНЯЯ ОШИБКА НА ЗАПРОСЕ

//системные переменный ПОЛЗОВАТЕЛЬ
    public static String USER_NAME = ""; //пароль
    public static String USER_FIO = "НЕ ОПРЕДЕЛЕН";  // название
    public static String USER_PODR = "НЕ ОПРЕДЕЛЕН";//  подразделение
    public static String USER_KPODR = " ";// код  подразделения
    public static String USER_MENU = ""; // меню допуск
    public static Vector USER_MENUD = new Vector();  //меню допуск дополнительный
    public static String USER_KWIFI = ""; // код входа для доп.оборудования (сканера, терминалы)
    public static String USER_NWIFI = ""; // название пользователя
    public static String USER_MWIFI = ""; // меню допуск
    public static String USER_DWIFI = ""; // доп.названия пользователя

    public static int USER_NOMER = 0;// номер пользователя в системе
    public static int USER_ADMIN = 0;    // уровень доступа
    public static boolean USER_DIR = false;    //пользователь - директор филиала
    public static int USER_DOP = 0;    //дополнительный  уровень доступа
    public static int DAYW = 0;    // дней работы системы
    public static int DAY_ADMIN = 0;    // дней работы системы

    public static String TOMCAT_DOMAIN_NAME = "tomcat.vit.belwest.com";

//Соединение сервера (Service_BW)
    public static String CONN_SERV_IP = "";
    public static String CONN_SERV_PORT = "";
    public static String CONN_SERV_NAME = "";
    public static String CONN_SERVLET = "MainPostal";
    public static String CONN_PROX_NAME = "";
    public static String CONN_PROX_PASS = "";
    public static String CONN_PROX_IP = "";
    public static String CONN_PROX_PORT = "";
    public static boolean CONN_PROX_FLAG = false;
    public static java.sql.Connection CONNECT_BUF = null;
    public static int CONN_COUNT = 0;
    // public static URLConnection CONN_SERV=null; //соединение к web сервису
    public static String ENET_SERV_IP = "192.168.130.172";//адрес прод-сервера по интернет-заказам
//Каталоги для сохранения конфигурационных файлов
    public static boolean FLAG_SHOP = false; // используется для определения подключения из SHOP, чтобы задать нужный путь для файлов конфига. При подключении из FIRM - false
    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String HOME_DIRECTORY = System.getProperty("user.home");
    public static String RUN_DIRECTORY = new File("").getAbsolutePath()+SEPARATOR; //каталог запуска программы;
   // public static String RUN_DIRECTORY = System.getProperty("java.class.path")+SEPARATOR; //каталог запуска программы;
    public static String FILES_DIRECTORY = RUN_DIRECTORY + "FILES" + SEPARATOR;
    public static String LOGS_DIRECTORY = RUN_DIRECTORY + "Logs" + SEPARATOR;
    public static String CONFIGS_DIRECTORY = "";
    public static String CONFIGS_DIRECTORY_FIRM = "";
    public static String DIRECTORY_REPORT = RUN_DIRECTORY + "Reports" + SEPARATOR;
    public static final String DIRECTORY_SCRIPT = DIRECTORY_REPORT + "SCRIPT" + SEPARATOR;
    public static String CONFIGS_DIRECTORY_Shop = HOME_DIRECTORY + SEPARATOR + "Shop_Propertis" + SEPARATOR;
    public static String CONFIGS_DIRECTORY_VCMPU = HOME_DIRECTORY + SEPARATOR + "VCMPU_Propertis" + SEPARATOR;
    public static String CONFIGS_DIRECTORY_APPT = HOME_DIRECTORY + SEPARATOR + "APPT_Propertis" + SEPARATOR;
    public static String CONFIGS_DIRECTORY_IMOBIS = HOME_DIRECTORY + SEPARATOR + "Imobis_Propertis" + SEPARATOR;
    public static String CONFIGS_DIRECTORY_LUNCH = HOME_DIRECTORY + SEPARATOR + "Lunch_Propertis" + SEPARATOR;
    public static String CONFIGS_DIRECTORY_IMOBIS_SERV = HOME_DIRECTORY + SEPARATOR + "Imobis_Propertis_Serv" + SEPARATOR;
    public static String CONFIGS_DIRECTORY_ALL = HOME_DIRECTORY + SEPARATOR + "ALL_Propertis" + SEPARATOR;
    public static final String CONFIGS_DIRECTORY_Kassa = HOME_DIRECTORY + SEPARATOR + "Kassa_Propertis" + SEPARATOR;
    public static final String CONFIGS_DIRECTORY_DIST_CENTER = HOME_DIRECTORY + SEPARATOR + "DIST_CENTER_Propertis" + SEPARATOR;
    public static String PASS_IN =RUN_DIRECTORY+"RKV_IN.TXT"; //ИМЯ ФАЙЛА С ПАРОЛЕМ
    public static String fileNameTxtLog = CONFIGS_DIRECTORY + "txtHistory.config";  // путь для сохранеия файла конфигурации 
    public static String fileNameConServ = CONFIGS_DIRECTORY + "connect.config";  // путь для сохранеия файла конфигурации
    public static String fileNameGridconf = CONFIGS_DIRECTORY + "grid_save.config";  // путь для сохранеия файла конфигурации
    public static String fileNameTreeconf = CONFIGS_DIRECTORY + "tree.config";  // путь для сохранеия файла конфигурации
    public static String fileNameScreenconf = CONFIGS_DIRECTORY + "screen.config";  // путь для сохранеия файла конфигурации
    public static String fileNameConnMem = CONFIGS_DIRECTORY + "conn.mem";  // путь для сохранеия файла конфигурации
    public static String fileNameFormConf = CONFIGS_DIRECTORY + "form.config";  // путь для сохранеия файла конфигурации
    public static String fileNameEnvConf = CONFIGS_DIRECTORY + "envParam.config";  // путь для сохранеия файла конфигурации
    public static String fileNameKassaSettingsConf = CONFIGS_DIRECTORY + "KassaSett.config";  // путь для сохранеия файла конфигурации
    public static String fileConnPropConf = CONFIGS_DIRECTORY + "ConnProp.config";  // путь для сохранеия файла конфигурации
    public static String fileFirmEnetPropConf = CONFIGS_DIRECTORY + "FirmEnetProp.config";  // путь для сохранения файла настроек отображения интернет-заказов из программы FIRM
    public static String fileSYS = "sys.mem";  // путь для сохранеия файла переменных окружения
    public static String PICT_DIR = RUN_DIRECTORY + "Reports" + SEPARATOR + "PICT" + SEPARATOR;  // путь для сохранеия файла картинок артикула
    public static String fileXmlLog;  // путь к текущему файлу логов 

    public static String posOpenShopConfig = HOME_DIRECTORY + SEPARATOR + "open_pos_conn.conf"; // путь к файлу настроек программы POS_OPEN_SHOP

    public static Boolean ARGS2 = false;

    //--------------параметры окружения ----------------------------
    public static String CURRENT_PATH = ".";
    //--------------параметры окружения порта Shop----------------------------
    public static String CIPHER_FILE_PATH = "";
    public static String CIPHER_PORT = "";
    public static String BANK_PORT = "";
    public static String[] markaNames = {"8000", "8230", "8230WiFi"};
    public static String CIPHER_MARKA = markaNames[0];
    public static String CIPHER_TIME = "";
    public static String CIPHER_SPEED = "115200";
    public static boolean FULLSCREEN_FLAG = false;
    public static String SCANER_PORT = "COM2";
    public static int SCANER_SPEED = 38400;
    public static String CIPHER_FILE_1 = "SynLoad.exe"; //ВНЕШНЯЯ УТИЛИТА ЗАГРУЗКИ ПО
    public static String CIPHER_FILE_3 = "CIPHER.VER"; //ФАЙЛ С ВЕРСИЕЙ ПРОГРАММЫ

    //--------------типы сканкодов ---------------------------------------------
    public static final int scanEO = 0;  // единица обработки
    public static final int scanRKV = 1; // код ркв
    public static final int scanEAN = 2;  // еан код
    public static final int scanDK = 3;  // дисконтная карта
    public static final int scanPS = 4; // подарочный сертификат 
    public static final int scanKupon = 5;  //купон
    public static final int scanUnknown = -1; // неизвестный     
//печать
    public static JasperPrint JP = null;
    public static String[] PRINTERS;
    public static String PRINTER;
    public static String FILE_REPORT;
    public static int JP_SIZE = 0;

    public static HashMap<String, Object> PRINTMAP = null;
//
    public static Post POST = null;
    public static String PARAM = "";
    public static String RES = "";
    public static String RUB = "руб.";
    public static String KOP = "kop.";
    public static String ERROR_MES = ""; //СООБЩЕНИЕ ОБ ОШИБКЕ
    public static String DO_FORM = ""; //ЗАГРУЗКА ФОРМЫ ПРИ ВХОДЕ

//---------------------------------------------------------------------------------------------------------------    
    public static boolean VARCONN() {
        if (!FF.FILE(fileNameConnMem)) {
            String[] vars = {"", Crypt.encrypt(""), CONN_SERVER, CONN_BASA, CONN_DRIVER, FF.STR(CONN_RECCOUNT), CONN_PORT};
            P.SAVETOMEM(fileNameConnMem, vars);
            P.MESSERR("Не настроено соединение с данными. Выполните настройку соединения");
            return false;
        }
        ArrayList<String> vars = P.RESTOREMEM(fileNameConnMem);//параметры соединения из файла
        if (vars.size() > 0) {
            CONN_USER = vars.get(0);
        }
        if (vars.size() > 1) {
            String cryptStr = vars.get(1);
            CONN_PASS = Crypt.decrypt(cryptStr);
        }
        if (vars.size() > 2) {
            CONN_SERVER = vars.get(2);
        }
        if (vars.size() > 3) {
            CONN_BASA = vars.get(3);
        }
        if (vars.size() > 4) {
            CONN_DRIVER = vars.get(4);
        }
        if (vars.size() > 5) {
            CONN_RECCOUNT = FF.VAL(vars.get(5));
        }
        if (vars.size() > 6) {
            CONN_PORT = vars.get(6);
        }

        return true;

    }

    public static boolean VARCONNSERV() {  // параметры подключения к Service_BW из конфига в корне по
        Configs proper = new Configs(fileNameConServ);
        if (proper.size() != 0) {  // если файла нет то количество пропертис 0, следовательно нужно его создать
            try {
                CONN_SERV_IP = proper.getProperty("$" + "IP", "service-bw.tomcat.vit.belwest.com");
                CONN_SERV_PORT = proper.getProperty("$" + "PORT", "80");
                CONN_SERV_NAME = proper.getProperty("$" + "SERVICE", "");
                CONN_SERVLET = proper.getProperty("$" + "SERVLET", "MainPostal");
                CONN_PROX_FLAG = Boolean.valueOf(proper.getProperty("$" + "PROXYFLAG"));
                CONN_PROX_IP = proper.getProperty("$" + "PROXYIP");
                CONN_PROX_PORT = proper.getProperty("$" + "PROXYPORT");
                CONN_PROX_NAME = proper.getProperty("$" + "PROXYNAME");
                String cryptStr = proper.getProperty("$" + "PROXYPASS");
                CONN_PROX_PASS = Crypt.decrypt(cryptStr);
                //20122017 KAA
                if (CONN_SERV_IP.equals("vit.belwest.com")) {
                    CONN_SERV_IP = "pos.vit.belwest.com";
                    P.SAVECONNPROP(CONN_SERV_IP, CONN_SERV_PORT, CONN_SERV_NAME, CONN_PROX_NAME, CONN_PROX_PASS, CONN_PROX_IP, CONN_PROX_PORT, false, CONN_SERVLET);
                }
                if (CONN_SERV_IP.equals("pos.vit.belwest.com") || CONN_SERV_IP.equals("192.168.2.121")) {
                    CONN_SERV_IP = "service-bw.tomcat.vit.belwest.com";
                    CONN_SERV_PORT = "80";
                    CONN_SERV_NAME = "";
                    P.SAVECONNPROP(CONN_SERV_IP, CONN_SERV_PORT, CONN_SERV_NAME, CONN_PROX_NAME, CONN_PROX_PASS, CONN_PROX_IP, CONN_PROX_PORT, false, CONN_SERVLET);
                }
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Не найдены свойства подключения к серверу!");
                return false;
            }
        } else {
            P.FIRSTSAVECONNPROP(); // создаем файл с деф параметрами
//            P.MESSERR("Файл конфигурации создан впервые и содержит параметры по умолчанию");
        }
        return true;
    }
    public static boolean TF = false;  // общий
    public static boolean TF_PR = true; //для формы PRINT
    public static String CONNECT_URL = "";
    public static Connection CONN, CONN1, CONN2, CONN3, CONN4, CONN5;
    public static String SQLPORT = "1522";
    public static Statement SQL = null;
    public static int SQL_KOL = 0;

    public static ResultSet CURSOR = null;
    public static String SELE = "";//для запроса
    public static String SELE_LAST_REPORT = "";//ПОЛСЛЕДНИЙ ЗАПРОС ОТЧЕТА
    public static String[] PARAMTO; //массив параметров для передачи в форму через P.DOFORM
    public static String[] PARAMOT; //массив параметров для передачи из формы 
    public static int U_I_D = 1;//ОПЕРАЦИЯ ДЛЯ АЛИАСА 1-INSERT  2- UPDATE 3- DELETE
    public static ArrayList<String> LIST_SWORK=new ArrayList(Arrays.asList(""));  // РАБОЧИЙ СИМВОЛЬНЫЙ МАССИВ КОНТЕЙНЕР
//Значения по умолчанию
    public static String SHOP_ID;
    public static final String FORMVIDS[] = {"Metal", "Nimbus", "CDE/Motif", "Windows", "Windows Classic","Systems"};//стили форм
    public static final String FORMVID = FORMVIDS[1]; //выбранный стиль
    public static final Integer LAYERS[] = {0, 2, 4, 6};//слои панели
    public static final Integer LAYER_STAT = LAYERS[0];// для статистических фоновых данных последняя
    public static final Integer LAYER_FORM = LAYERS[1];// для форм предпоследняя
    public static final Integer LAYER_INF = LAYERS[2];// для информационных объектов
    public static final Integer LAYER_MESS = LAYERS[3];// для экстренных сообщений на переднем плане
    public static final String SELECT = "SELECT";
    public static final String TABLE = "TABLE";
    public static final String ALIAS = "ALIAS";
    public static int FONT_MENU = 20; // размер пунктов меню
    public static final int COLWIDTH = 150; // ширина колонок по умолчанию
    public static final int HEADERH = 50; // высота шапки грида поумолчанию
    public static final int ROWH = 21;// высота строк нрида по умолчанию
    public static String SESSIONID;
    public static String PROGRAMM = " ";
    public static String CUR_PASS = "";
    public static String CUR_PASS_OFFLINE = "";
    public static String CUR_PASS_BYR_BASE = "";
    public static String CUR_BYR_SHOPID = "";
    public static String CUR_BYR_PARAM = "";
    public static String OFFLINE_OUT_RESULT = "";
    public static String COUNTRY = "";
//Головное меню
    public static ArrayList<String> MENU_ITEMS = new ArrayList<>();
    public static ArrayList<String> MENU_ITEMS1 = new ArrayList<>();
    public static ArrayList<String> MENU_ITEMS2 = new ArrayList<>();
    public static Map MENU_ITEMS3 = null; //меню верхней линейки
    public static Map MENU_ITEMS4 = null; //меню вызываемое
    public static Cursorr QUERY_DATA;
//Цвета  "?new Color(255, 255, 0):new Color(255, 255, 255)"
    public static String COLOR_CONDITION_TRUE = "new Color(255, 255, 0)"; //желтый 
    public static String COLOR_CONDITION_FALSE = "new Color(255, 255, 255)"; //  белый
    public static String COLOR_CONDITION = "?" + V.COLOR_CONDITION_TRUE + ":" + V.COLOR_CONDITION_FALSE; //строка с условием изменения цвета
    public static String COLOR_CONDITION_RED = "?new Color(255, 0, 0):" + V.COLOR_CONDITION_FALSE; //строка с условием изменения цвета

    public static String COLOR_CONDITION(String color_t, String color_f) {
        return "?" + color_t + ":" + color_f;
    }
    public static Color COLORB_GRID_REC_FOCUSON = new Color(35, 208, 233);//фон записи активного грида
    public static Color COLORF_GRID_REC_FOCUSON = new Color(0, 0, 0);     //fore записи активного грида
    public static Color COLORB_GRID_REC_FOCUSOFF = new Color(255, 255, 0);//фон записи неактивного грида
    public static Color COLORF_GRID_REC_FOCUSOFF = new Color(0, 0, 0);//fore записи неактивного грида
    public static Color COLORB_TOOLBAR = new Color(216, 240, 254);//фон тулбара
    public static Color COLORB_MENU = new Color(240, 240, 240);//фон меню
    public static Color COLORB_READONLYON = new Color(220, 220, 220);//фон  текста читать только 
    public static Color COLORB_READONLYOFF = new Color(255, 255, 255);//фон  текста корректуры
    public static SimpleDateFormat ddmmyyyy = new SimpleDateFormat(V.MASK_DATE, Locale.GERMAN);
    public static SimpleDateFormat ddmmyyyyhhmm = new SimpleDateFormat(V.MASK_DATETIME, Locale.GERMAN);
    public static SimpleDateFormat ddmmyyyyhhmmSSS = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS", Locale.GERMAN);
    public static SimpleDateFormat timezone = new SimpleDateFormat("Z", Locale.GERMAN);
    public static SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss", Locale.GERMAN);

    //Макс/Мин годы
    public static final int YEARMIN = 1900;
    public static final int YEARMAX = 2050;
//Маски ввода
//    public static final Locale LOCRUS=new java.util.Locale("ru", "", "");
    public static java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
    ;
    public static long DIFF_DATE = 0;
    public static int DIFF_HOURS = 0;
    public static final Locale LOCRUS = new Locale.Builder().setLanguage("ru").setScript("Cyrl").build();
    public static final String MASK_DATE = "dd.MM.yyyy";
    public static final String MASK_DATETIME = "dd.MM.yyyy HH:mm:ss";
    public static final String MASK_DATETIME_ORACLE = "DD.MM.YYYY HH24:MI:SS,ff3";
    public static final String MASK_DATE_ORACLE = "DD.MM.YYYY HH24:MI:SS";

    public static final String DATE_TEXT = "  .  .    ";
    public static final String DATETIME_TEXT = "  .  .       :  :  ";
    public static final String DESFORMAT_TEXT = "###,###.####"; //МАСКА ВЫВОДА ЧИСЕЛ
    public static String PATTERN_SUM = "#,##0.00"; //ФОРМАТ ВЫВОДА СУММ В ОТЧЕТАХ
    public static DecimalFormatSymbols DecFormatP = new DecimalFormatSymbols();// КЛАСС ОПРЕДЕЛЕНИЯ РАЗДЕЛИТЕЛЕЙ
    public static DecimalFormat DecFormat = new DecimalFormat(DESFORMAT_TEXT);    //КЛАСС ФОРМАТИРОВАНИЯ
    public static int SELECTED_COLUMN = -1;  //выделенная в гриде колонка
    public static int TXT_HISTORY_COUNT = 10;// История в текстовом поле
    public static String TYPE_CHAR_TOOLBAR = "C_TB";
    public static Textr INFO_TXT;
    public static JScrollPane INFO_TXT_SCROLL;
    public static boolean INFO_FLAG;
    public static boolean INFO_FLAG_ENV = true;
    //Для кассы
    public static String FMODEL = "ШТРИХ";
    public static String KASSA_ID = "1";
    public static Tablo TABLO;
    public static String TABLO_TEXT;
    public static boolean TABLO_SOLD = false;
    public static String TABLO_PORT = "COM4";
    public static String TABLO_SPEED = "9600";
    public static String FP_PORT = "COM3";
    public static int FP_PORT_NUM = 6;    // номер компорта для фискального принтера
    public static String FP_SPEED = "115200"; // скорость порта
    public static String BT_MODEL = "Р2100";
    public static String BT_IP = "192.168.0.99";
    public static int BT_PORT = 5552;
    public static boolean BT_INTEGR = false;
    public static int BT_KVAL = 643; //КОД ВАЛЮТЫ ДЛЯ БАНКОВСКОГО ТЕРМИНАЛА
    public static String BT_ID = ""; //ИДЕНТИФИКАТОР ТЕРМИНАЛА

    public static String FP_PASS = "000000";
    public static boolean CUT_PAPER_CHECK = true;
    public static boolean AUTOCHANGE_KASSIR = true;
    public static boolean EXCHANGE_DATA_Z = true;
    public static boolean FLAG_KASSA = false;//запущена ли программа кассы
    public static boolean RETURN_FLAG = false;// возврат или покупка
    public static boolean ALLOW_NULL_OSTATOK = true; // показывать в ручном поиске товары, которых нет в остатках
    public static boolean SHOW_PROCENT = true; // показывать чеке некондицию
    public static String USER_EDIT;//пользователь, выбранный для редактирования
    public static String KASSIR_ID = "";
    public static String KASSIR_FIO = "";
    public static double ROUND_DIG = 10; // порядок округления при продаже
    public static int ROUND_MM = 0; //тип округления 1-в большую, -1 -в меньшую, 0 - по правилам округления
    public static double KRED_SUMM = 0;
    public static ArrayList<Double> KRED_ARR = new ArrayList<>();
    public static double END_PRICE = 0;
    public static boolean EXCHANGE_SCAN = false;
    public static ArrayList<String> OPTION_ARR = new ArrayList<>();
    public static ArrayList<String> OPTION_NAME_ARR = new ArrayList<>();
    public static ArrayList<String> OPTION_TYPE_ARR = new ArrayList<>();
    public static boolean GET_CENA3_ERROR = false;
    public static boolean MIF_PRICE = false;
    public static String NULL_FISKAL = "F";
    public static String NULL_FISKAL_PRINT = "F";
    public static String ONLINE_KASSA_RF_FN_PRINT = "F";
    public static String CASHIER_INN = " ";
    public static String SCAN_EDIT = "T";
    public static String S_DK_MENU_IND = "";
    public static HttpsURLConnection httpsConnection = null;
    public static boolean ONLINE_KASSA_RB = false; //ФЛАГ УСТАНОВКИ СКНО в РБ
    public static SerialPort RADIO_PORT = null;//порт для радиометок

    public static String POS_OPEN_SHOP_USERNAME;

    // Падежи
    public static final int PAD_I = 1;
    public static final int PAD_R = 2;
    public static final int PAD_D = 3;
    public static final int PAD_V = 4;
    public static final int PAD_T = 5;
    public static final int PAD_P = 6;
    // Фискальный принтер
    public static FiscalPrinter FP;
    public static String KODIROVKA = "Cp866";

    // DOCORRECT глобальны е переменные для формы общей корректуры
    public static String FormCorrName = "";
    public static Formr FormCorr;
    public static Formr FormParent;

    // Сетевые 
    public static String Host = "";
    public static String IP = "";
    public static String MAC = "";

    //КЛАССЫ
    public static String F_BaseFrame = "LIB\\FIRM_Shop.jar";
    public static String F_FIRM_Shop = "LIB\\FIRM_Shop.jar";

    public static long T_BaseFrame = FF.FILE_TIME(F_BaseFrame);  //время последней модификации
    public static long T_FIRM_Shop = FF.FILE_TIME(F_FIRM_Shop);
    ; //время последней модификации    
    
    public static boolean PRG_CONSOLE = false; //переменная для определения консольного приложения
    public static Document XML_FILE; //для записи логов в xml файл

    public static boolean ALERT_SHOW = true; //отображение панели по выполняющимся запросам
    public static String ALERT_TEXT = ""; //текущий текст оповещения

    public static int ENET_REGION_VIEW = 0;

    //типы штрихкодов
    public static final String BAR_CODE93 = "Code93";
    public static final String BAR_CODE128 = "Code128";
    public static final String BAR_EAN13 = "EAN13";
    public static final String BAR_QRCode = "QRCode";
    // промежуточные на выполнение форме ViewForm
    public static boolean ViewForm_STAT_DBCLICK = true; // реакция грида на двойной щелчек если true то в метод CLICK_ALL
    //ОТЛАДКА
    public static boolean DEVELOP = false; //запуск из среды разработки
}
