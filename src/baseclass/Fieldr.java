package baseclass;

import prg.FF;
import prg.P;
import prg.V;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Базовый класс текстового поля
 *
 * @author Kostya
 */
//public class Fieldr extends javax.swing.JPasswordField {
public class Fieldr extends javax.swing.JTextField {

    public int X1 = getX(), X2 = getX() + getWidth(), Y1 = getY(), Y2 = getY() + getHeight(); //координаты по углам        public Formr THISFORM   ;
    public String TYPE = V.TYPE_CHAR;
    public String INPUTMASK = "";
    public boolean SELECTON = false;

    public char echoChar;
    public boolean echoCharSet;
    public boolean HISTORY = true;
    public boolean calendar = false;
    public boolean calFlag = false;
    String text = "";
    public Fieldr textFr = this;
    public boolean READONLY = false;//только чтение
    public boolean SQLUPDATE = true;//обновление по запросу
    public boolean THISKEY = false;//поле ключ для выражения WHERE
    public boolean PR_ESC = false;//если true то разрешено выход из поля даже при valid .
    public Formr THISFORM; //ссылка на родительскую форму
    public boolean SPACE_LEFT = true; //допустимость пробелов слева
    public boolean PREF_SUF = true; //допустимость префиксов и суффиксов 

    public String VAL_OLD = ""; //РАБОЧЕЕ ПОЛЕ ДЛЯ СОХРАНЕНИЯ СТАРОГО ЗНАЧЕНИЯ ПРИ ВХОДЕ В ПОЛЕ
    public String VAL = null; //РАБОЧЕЕ ПОЛЕ ДЛЯ СОХРАНЕНИЯ  ЗНАЧЕНИЯ 

    /**
     * Записывает теку4щее значения во внутреннее поле
     */
    public void SET_VAL() {
        VAL = getText();
    }

    /**
     * Возвращает значение записанное во внутреннию переменную
     *
     * @return
     */
    public String GET_VAL() {
        return VAL;
    }

    /**
     * Проверяет на изменение текущего значения с записанным по SET_VAL true
     * -при изменении
     *
     * @return
     */
    public boolean GET_VAL_CH() {
        if (VAL == null) {
            return false;
        }
        return (!getText().equals(VAL));
    }

    public void DBCLICK() {
        THISFORM.DBLCLICK_ALL(getName());
    }

    public void CLICK() {
        THISFORM.CLICK_ALL(getName());
    }

    public boolean VALID() {
        return THISFORM.VALID_ALL(this.getName());
    }

    //СЭТЕРЫ 

    /**
     * Вызоа календаря в поле дата по двойному щелчку
     *
     * @param flag - true - вызов календаря
     */
    public void SETCALENDAR(boolean flag) {
        calendar = flag;
    }

    /**
     * При true отсекает левые пробелы при выходе из текстового поля
     *
     * @param flag
     */
    public void SETSPACE_LEFT(boolean flag) {
        SPACE_LEFT = flag;
    }

    /**
     * Автоматическое выделение текста на фокус
     *
     * @param flag
     */
    public void SETSELECTON(boolean flag) {
        SELECTON = flag;
    }

    public void setEchoChar(char c) {
        echoChar = c;
        echoCharSet = true;
        repaint();
        revalidate();
    }

    public void SETREADONLY(boolean tf) {
        READONLY = tf;
        if (READONLY == false) {
            setEditable(true);
            setBackground(V.COLORB_READONLYOFF);
        }
        if (READONLY == true) {
            setEditable(false);
            setBackground(V.COLORB_READONLYON);
        }
    }

    public void SETTYPE(String type) {
        TYPE = type;
        if (type.equals(V.TYPE_DATE)) {
            SETINPUTMASK(V.MASK_DATE);
            calendar = true;
            setText(V.DATE_TEXT);
            setCaretPosition(0);
        }
        if (type.equals(V.TYPE_DATETIME)) {
            SETINPUTMASK(V.MASK_DATETIME);
            calendar = true;
            setText(V.DATETIME_TEXT);
            setCaretPosition(0);
        }
        if (type.equals(V.TYPE_NUMERIC)) {
            SETINPUTMASK("99999999");
        }
        if (type.equals(V.TYPE_BIG_NUM)) {
            TYPE = V.TYPE_NUMERIC;
            SETINPUTMASK("9999999999999999999999999999");
        }
    }

    public void SETINPUTMASK(String mask) {
        if ((TYPE.equals(V.TYPE_NUMERIC) || TYPE.equals(V.TYPE_BIG_NUM)) && mask.contains(".")) {
            setText(".");
            setCaretPosition(0);
        }
        INPUTMASK = mask;
    }

    public void SETSQLUPDATE(boolean tf) {
        SQLUPDATE = tf;
    }

    public void SETTHISKEY(boolean tf) {
        THISKEY = tf;
    }

    public void SETVALUE(Object val) {
        String str;
        if (val == null) {
            str = "";
        } else {
            if (val instanceof int[]) {
                TYPE = V.TYPE_NUMERIC;
            }
            if (val instanceof String) {
                TYPE = V.TYPE_CHAR;
            }
            if (val != null && val.getClass() == Double.class) {
                String DEC = ".00";
                int POS = FF.AT(".", INPUTMASK);
                if (!FF.EMPTY(INPUTMASK) && POS > 0) { //ЕСЛИ В МАСКЕ ЕСТЬ ТОЧККА 
                    DEC = "." + FF.REPLICATE("0", FF.SUBSTR(INPUTMASK, POS + 1).length()); //КОЛИЧЕСТВО ЗНАКОВ ПОСЛЕ ТОЧКИ
                }
                val = (new DecimalFormat("#0" + DEC)).format(val);

            }

            if (TYPE == V.TYPE_DATE) {
                str = V.ddmmyyyy.format(val);
            } else {
                if (TYPE == V.TYPE_DATETIME) {
                    str = V.ddmmyyyyhhmm.format(val);
                } else {

                    str = val.toString();
                }
            }
        }
        setText(str);

        setCaretPosition(0);

    }

    public Object GETVALUE() {
//        String str = new String(getPassword());
        String str = getText();
        if (TYPE == V.TYPE_NUMERIC) {
            if (FF.EMPTY(str)) {
                str = "0";
            }
            if (".".equals(str)) {
                str = "0.0";
            }

            if (FF.AT(".", str) > 0) {
                return (Object) Double.parseDouble(str);
            } else {
                return (Object) Integer.parseInt(str);
            }

        }
        if (TYPE == V.TYPE_DATE) {
//                SimpleDateFormat format = new SimpleDateFormat(V.MASK_DATE,Locale.GERMANY);
            try {
                if (V.DATE_TEXT.equals(str)) {
                    str = "01.01.1900";
                }
                Object val = (Object) V.ddmmyyyy.parse(str);
                return val;
            } catch (ParseException ex) {
//                Logger.getLogger(Fieldr.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

        }
        if (TYPE == V.TYPE_DATETIME) {
//                SimpleDateFormat format = new SimpleDateFormat(V.MASK_DATETIME);
            try {
                Object val = (Object) V.ddmmyyyyhhmm.parse(str);
                return val;
            } catch (ParseException ex) {
//                Logger.getLogger(Fieldr.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

        }

        return (Object) str;
    }

    public boolean NUMCHECK(String text) {
        try {
            if (text.length() == 0) {
                setText("0");
                return true;
            }
            if (text.substring(0, 1).equals(".")) {
                text = "0" + text;
            }
            if (text.substring(text.length() - 1).equals(".")) {
                text = text + "0";
            }
            setText(text);

            Double.parseDouble(text);
        } catch (NumberFormatException | StringIndexOutOfBoundsException exc) {
            P.WRITE_INFO(text + "Некорректный формат ввода числа по маске " + INPUTMASK);
            return false;
        }
        return true;
    }

    public boolean DATECHECK(boolean timeFlag, String text) {
        if (timeFlag) {
            try {
                if (text.substring(11, 12).equals(" ") && text.substring(12, 13).equals(" ")) {
                    P.WRITE_INFO(text + " Некорректно введены часы.");
                    return false;
                } else if (text.substring(11, 12).equals(" ")) {
                    text = text.substring(0, 11) + "0" + text.substring(12, text.length());
                } else if (text.substring(12, 13).equals(" ")) {
                    text = text.substring(0, 11) + "0" + text.substring(11, 12) + text.substring(13, text.length());
                }
                if (Integer.parseInt(text.substring(11, 13)) > 23) {
                    P.WRITE_INFO(text + " Часы больше 23.");
                    return false;
                }
            } catch (StringIndexOutOfBoundsException ex) {
                P.WRITE_INFO(text + " Некорректно введено время.");
                return false;
            }
            setText(text);
            if (text.substring(14, 15).equals(" ") && text.substring(15, 16).equals(" ")) {
                P.WRITE_INFO(text + " Некорректно введены минуты.");
                return false;
            } else if (text.substring(14, 15).equals(" ")) {
                text = text.substring(0, 14) + "0" + text.substring(15, text.length());
            } else if (text.substring(15, 16).equals(" ")) {
                text = text.substring(0, 14) + "0" + text.substring(14, 15);
            }
            if (Integer.parseInt(text.substring(14, 16)) > 59) {
                P.WRITE_INFO(text + " Минута больше 59.");
                return false;
            }
            setText(text);
            if (text.substring(17, 18).equals(" ") && text.substring(18, 19).equals(" ")) {
                P.WRITE_INFO(text + " Некорректно введены секунды.");
                return false;
            } else if (text.substring(17, 18).equals(" ")) {
                text = text.substring(0, 17) + "0" + text.substring(18, text.length());
            } else if (text.substring(18, 19).equals(" ")) {
                text = text.substring(0, 17) + "0" + text.substring(17, 18);
            }
            if (Integer.parseInt(text.substring(17, 19)) > 59) {
                P.WRITE_INFO(text + " Секунда больше 59.");
                return false;
            }
            setText(text);
        }
        if (text.substring(0, 1).equals(" ") && text.substring(1, 2).equals(" ")) {
            P.WRITE_INFO(text + " Некорректно введено число.");
            return false;
        } else if (text.substring(0, 1).equals(" ")) {
            text = "0" + text.substring(1, text.length());
        } else if (text.substring(1, 2).equals(" ")) {
            text = "0" + text.substring(0, 1) + text.substring(2, text.length());
        }
        setText(text);
        if (text.substring(3, 4).equals(" ") && text.substring(4, 5).equals(" ")) {
            P.WRITE_INFO(text + " Некорректно введен месяц.");
            return false;
        } else if (text.substring(3, 4).equals(" ")) {
            text = text.substring(0, 3) + "0" + text.substring(4, text.length());
        } else if (text.substring(4, 5).equals(" ")) {
            text = text.substring(0, 3) + "0" + text.substring(3, 4) + text.substring(5, text.length());
        }
        setText(text);
        if (Integer.parseInt(text.substring(0, 2)) == 0) {
            P.WRITE_INFO(text + " Такого числа не существует.");
            return false;
        }
        if (Integer.parseInt(text.substring(3, 5)) > 12 || Integer.parseInt(text.substring(3, 5)) == 0) {
            P.WRITE_INFO(text + " Такого месяца не существует.");
            return false;
        }
        try {
            if (Integer.parseInt(text.substring(6, 10)) < V.YEARMIN || Integer.parseInt(text.substring(6, 10)) > V.YEARMAX) {
                P.WRITE_INFO(text + " Год не входит в диапазон.");
                return false;
            }
        } catch (NumberFormatException exc) {
            P.WRITE_INFO(text + " Некорректно введен год.");
            return false;
        }
        switch (text.substring(3, 5)) {
            case "01":
                if (Integer.parseInt(text.substring(0, 2)) > 31) {
                    P.WRITE_INFO(text + " В январе 31 день.");
                } else {
                    return true;
                }
                break;
            case "02":
                GregorianCalendar cal = new GregorianCalendar(V.LOCRUS);
                if (cal.isLeapYear(Integer.parseInt(text.substring(6, 10)))) {
                    if (Integer.parseInt(text.substring(0, 2)) > 29) {
                        P.WRITE_INFO(text + " Високосный год. В феврале 29 дней.");
                    } else {
                        return true;
                    }
                } else {
                    if (Integer.parseInt(text.substring(0, 2)) > 28) {
                        P.WRITE_INFO(text + " В феврале 28 дней.");
                    } else {
                        return true;
                    }
                }
                break;
            case "03":
                if (Integer.parseInt(text.substring(0, 2)) > 31) {
                    P.WRITE_INFO(text + " В марте 31 день.");
                } else {
                    return true;
                }
                break;
            case "04":
                if (Integer.parseInt(text.substring(0, 2)) > 30) {
                    P.WRITE_INFO(text + " В апреле 30 дней.");
                } else {
                    return true;
                }
                break;
            case "05":
                if (Integer.parseInt(text.substring(0, 2)) > 31) {
                    P.WRITE_INFO(text + " В мае 31 день.");
                } else {
                    return true;
                }
                break;
            case "06":
                if (Integer.parseInt(text.substring(0, 2)) > 30) {
                    P.WRITE_INFO(text + " В июне 30 дней.");
                } else {
                    return true;
                }
                break;
            case "07":
                if (Integer.parseInt(text.substring(0, 2)) > 31) {
                    P.WRITE_INFO(text + " В июле 31 день.");
                } else {
                    return true;
                }
                break;
            case "08":
                if (Integer.parseInt(text.substring(0, 2)) > 31) {
                    P.WRITE_INFO(text + " В августе 31 день.");
                } else {
                    return true;
                }
                break;
            case "09":
                if (Integer.parseInt(text.substring(0, 2)) > 30) {
                    P.WRITE_INFO(text + " В сентябре 30 дней.");
                } else {
                    return true;
                }
                break;
            case "10":
                if (Integer.parseInt(text.substring(0, 2)) > 31) {
                    P.WRITE_INFO(text + " В октябре 31 день.");
                } else {
                    return true;
                }
                break;
            case "11":
                if (Integer.parseInt(text.substring(0, 2)) > 30) {
                    P.WRITE_INFO(text + " В ноябре 30 дней.");
                } else {
                    return true;
                }
                break;
            case "12":
                if (Integer.parseInt(text.substring(0, 2)) > 31) {
                    P.WRITE_INFO(text + " В декабре 31 день.");
                } else {
                    return true;
                }
                break;
        }
        return false;
    }

    private void datetimeKeyTyping(int caretPos, char key, boolean timeFlag) {
        if (caretPos == 0) {
            setText(key + text.substring(1, 2) + text.substring(2));
            setCaretPosition(caretPos + 1);
        } else if (caretPos == 1) {
            setText(text.substring(0, 1) + key + text.substring(2));
            setCaretPosition(caretPos + 2);
        } else if (caretPos == 2) {
            setText(text.substring(0, 3) + key + text.substring(4));
            setCaretPosition(caretPos + 2);
        } else if (caretPos == 3) {
            setText(text.substring(0, 3) + key + text.substring(4));
            setCaretPosition(caretPos + 1);
        } else if (caretPos == 4) {
            setText(text.substring(0, 4) + key + text.substring(5));
            setCaretPosition(caretPos + 2);
        } else if (caretPos == 5) {
            setText(text.substring(0, 6) + key + text.substring(7));
            setCaretPosition(caretPos + 2);
        } else if (caretPos == 6) {
            setText(text.substring(0, 6) + key + text.substring(7));
            setCaretPosition(caretPos + 1);
        } else if (caretPos == 7) {
            setText(text.substring(0, 7) + key + text.substring(8));
            setCaretPosition(caretPos + 1);
        } else if (caretPos == 8) {
            setText(text.substring(0, 8) + key + text.substring(9));
            setCaretPosition(caretPos + 1);
        } else if (caretPos == 9) {
            if (!timeFlag) {
                setText(text.substring(0, 9) + key);
                setCaretPosition(caretPos + 1);
            } else {
                setText(text.substring(0, 9) + key + text.substring(10));
                setCaretPosition(caretPos + 2);
            }
        }
        if (timeFlag) {
            if (caretPos == 10) {
                setText(text.substring(0, 11) + key + text.substring(12));
                setCaretPosition(caretPos + 2);
            }
            if (caretPos == 11) {
                setText(text.substring(0, 11) + key + text.substring(12));
                setCaretPosition(caretPos + 1);
            }
            if (caretPos == 12) {
                setText(text.substring(0, 12) + key + text.substring(13));
                setCaretPosition(caretPos + 2);
            }
            if (caretPos == 13) {
                setText(text.substring(0, 14) + key + text.substring(15));
                setCaretPosition(caretPos + 2);
            }
            if (caretPos == 14) {
                setText(text.substring(0, 14) + key + text.substring(15));
                setCaretPosition(caretPos + 1);
            }
            if (caretPos == 15) {
                setText(text.substring(0, 15) + key + text.substring(16));
                setCaretPosition(caretPos + 2);
            }
            if (caretPos == 16) {
                setText(text.substring(0, 17) + key + text.substring(18));
                setCaretPosition(caretPos + 2);
            }
            if (caretPos == 17) {
                setText(text.substring(0, 17) + key + text.substring(18));
                setCaretPosition(caretPos + 1);
            }
            if (caretPos == 18) {
                setText(text.substring(0, 18) + key);
                setCaretPosition(caretPos + 1);
            }
        }
    }

    private void timeKeyTyping(int caretPos, char key, boolean timeFlag) {
        if (caretPos == 0) {
            setText(key + text.substring(1, 2) + text.substring(2));
            setCaretPosition(caretPos + 1);
        } else if (caretPos == 1) {
            setText(text.substring(0, 1) + key + text.substring(2));
            setCaretPosition(caretPos + 2);
        } else if (caretPos == 2) {
            setText(text.substring(0, 3) + key + text.substring(4));
            setCaretPosition(caretPos + 2);
        } else if (caretPos == 3) {
            setText(text.substring(0, 3) + key + text.substring(4));
            setCaretPosition(caretPos + 1);
        } else if (caretPos == 4) {
            setText(text.substring(0, 4) + key + text.substring(5));
            setCaretPosition(caretPos + 2);
        }

    }

    private void historyUpdate() {
        if (!getText().equals("") && HISTORY) {
            try {
                P.SAVE_TXT_HISTORY(THISFORM, textFr);
            } catch (IndexOutOfBoundsException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * Изменение размера шрифта
     *
     * @param size
     */
    public void SETFONTSIZE(int size) {
        Font font = getFont();
        font.getFontName();
        setFont(new Font(getFont().getFontName(), getFont().getStyle(), size));

    }

    public Fieldr(String name, String title, int width, int height) {//иницилизация имени поля и заголовка и размеров
        this.textFr = this;
        super.setText(title);
        super.setName(name);
        super.setSize(width, height);
        this.setEchoChar((char) 0);
        this.setEditable(true);
//        this.setTransferHandler(null);//запрет на вставку из буфера со скопированными данными
//        this.setAlignmentX(LEFT_ALIGNMENT);
//        this.setAlignmentY(LEFT_ALIGNMENT);
        this.setHorizontalAlignment(JTextField.LEFT);
        setFont(new Font("Arial", 1, 14));
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { // ПОЛУЧЕНИЕ ФОКУСА
                // setSelectionEnd(PROPERTIES)
//                isEditable

                calFlag = false;
                VAL_OLD = getText(); //СОХРАНИТЬ ЗНАЧЕНИЕ ПРИ ВХОДЕ
                if (!SELECTON && !READONLY && TYPE.equals(V.TYPE_NUMERIC) && "0".equals(getText().trim())) { // ЕСЛИ НОЛЬ ТО С ПУСТОГО
                    setText("");
                }
                if (!SELECTON && !READONLY && TYPE.equals(V.TYPE_NUMERIC) && ("0.00".equals(getText().trim()) || "0.00".equals(getText().trim()) || "0.000".equals(getText().trim()) || "0.0".equals(getText().trim()))) { // ЕСЛИ НОЛЬ ТО С ПУСТОГО
                    setText(".");
                }

                setCaretPosition(0);
                if (SELECTON && isEditable()) {
                    selectAll();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {  // потеря фокуса
                if ("".equals(getText().trim()) && !READONLY && TYPE.equals(V.TYPE_NUMERIC)) { //ЕСЛИ ЧИСЛО И КОРРЕКТУРА И ПУСТО - ПОСТАВИТЬ 0
                    setText(VAL_OLD);
                }
                if (READONLY == true) {
                    return;
                } //если только чтение контроль не проводится
                if (V.TYPE_CHAR.equals(TYPE) && SPACE_LEFT == false) { //УБРАТЬ ПРОБЕЛЫ СЛЕВА
                    SETVALUE(FF.LTRIM(GETVALUE().toString()));
                }
                if (V.TYPE_CHAR.equals(TYPE) && PREF_SUF == false) { // E,HFNM CУФФЕКСЫ И ПРЕФИКСЫ
                    SETVALUE(FF.PREF_SUF(GETVALUE().toString()));
                }

                if (!TYPE.equals(V.TYPE_CHAR_TOOLBAR) && !calFlag) {
                    if (INPUTMASK.equals(V.MASK_DATE)) {
//                        if (!DATECHECK(false, GETVALUE().toString()) && !getText().equals("  .  .    ")) {
//                        if (!DATECHECK(false, V.ddmmyyyy.format(GETVALUE()) ) && !getText().equals("  .  .    ")) {                        
                        if (!DATECHECK(false, getText()) && !getText().equals("  .  .    ")) {

                            setForeground(Color.red);
                            // P.PLAY_SOUND("beep-3.wav");
                            requestFocus();
                        } else {
                            historyUpdate();
                            setForeground(Color.black);
                        }
                    } else if (INPUTMASK.equals(V.MASK_DATETIME)) {
//                        if (!DATECHECK(true, GETVALUE().toString()) && !getText().equals("  .  .       :  :  ")) {
//                        if (!DATECHECK(true,  V.ddmmyyyyhhmm.format(GETVALUE())) && !getText().equals("  .  .       :  :  ")) {                        
                        if (!DATECHECK(true, getText()) && !getText().equals("  .  .       :  :  ")) {
                            setForeground(Color.red);
                            // P.PLAY_SOUND("beep-3.wav");
                            requestFocus();
                        } else {
                            historyUpdate();
                            setForeground(Color.black);
                        }
                    } else if (V.TYPE_NUMERIC.equals(TYPE)) {
                        if (!NUMCHECK(getText()) && (!getText().equals("") || !getText().equals("."))) {
                            setForeground(Color.red);
                            // P.PLAY_SOUND("beep-3.wav");
                            requestFocus();
                        } else {
                            historyUpdate();
                            setForeground(Color.black);
                        }
                    } else {
                        historyUpdate();
                    }

                    if (VALID() == false) {
                        //P.PLAY_SOUND("beep-3.wav");
                        requestFocus();
                    }
                }
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {  //ввод символа
                boolean ctrl = e.isControlDown();
                char key = e.getKeyChar();
                int caretPos = getCaretPosition();
                text = getText();
                if (READONLY && KeyEvent.VK_ENTER != e.getKeyCode()) { //если запрет на корректуру  ничего не предпринимать RKV 21/02/2017
                    THISFORM.KEYPRESS(e.getComponent(), e); // RKV 12/02/2019
                    e.consume();
                    return;
                }
                switch (TYPE) {
                    case V.TYPE_NUMERIC:

                        if (KeyEvent.VK_ENTER == e.getKeyCode() && getSelectedText() != null) { //если enter и выделен текст ничего не предпринимать
                            e.consume();
                        }
                        if (getSelectedText() != null && getSelectedText().contains(".") && Character.isDigit(key)) { // если выделен текст и есть точка и нажата цифра
                            int selPos = getSelectionStart();
                            if (getSelectedText().length() == 1) {
                                setCaretPosition(selPos + 1);
                            } else if (getSelectedText().substring(0, 1).equals(".")) {
                                setText(getText().substring(0, getSelectionStart()) + "." + key + getText().substring(getSelectionEnd(), getText().length()));
                                setCaretPosition(selPos + 2);
                            } else {
                                setText(getText().substring(0, getSelectionStart()) + key + "." + getText().substring(getSelectionEnd(), getText().length()));
                                setCaretPosition(selPos + 1);                 // получаем " . "
                            }
                            e.consume();
                            break;
                        } else if (getSelectedText() != null && !getSelectedText().contains(".") && Character.isDigit(key)) { //// если выделен текст и нет точки и нажата цифра
                            int selPos = getSelectionStart();
                            setText(getText().substring(0, getSelectionStart()) + key + getText().substring(getSelectionEnd(), getText().length()));
                            setCaretPosition(selPos + 1);  // получаем "  "
                            e.consume();
                            break;
                        }
                        if (text.length() == INPUTMASK.length() && caretPos == INPUTMASK.length()) { //если последняя позиция по маске
                            e.consume();
                            break;
                        }

                        if (getSelectedText() != null && getSelectedText().contains(".") && (key == '.' || key == ',')) { // если выделен текст и есть точка и нажата точка или запятая
                            setText(getText().substring(0, getSelectionStart()) + "." + getText().substring(getSelectionEnd(), getText().length()));
                            //setText("0.");                            
                            //setCaretPosition(text.indexOf(".")+1);
                        } else {
                            if (getSelectedText() == null && text.contains(".") && (key == '.' || key == ',')) { // если не выделен текст и есть точка и нажата точка или запятая
                                //setText(getText().substring(0, getSelectionStart()) + "." +  getText().substring(getSelectionEnd(), getText().length()));                            
                                setCaretPosition(text.indexOf(".") + 1);
                            }
                        }

                        if (!Character.isDigit(key) && key != '.' && key != ',') {
                            e.consume();
                        } else {
                            if (INPUTMASK.contains(".")) {
                                if (Character.isDigit(key) && INPUTMASK.length() - INPUTMASK.indexOf(".") == text.length() - text.indexOf(".") && caretPos > text.indexOf(".")) {
                                    if (caretPos == text.indexOf(".") + INPUTMASK.length() - INPUTMASK.indexOf(".")) {
                                        e.consume();
                                    } else {
                                        if (caretPos + 1 <= getText().length()) {
                                            setText(getText().substring(0, caretPos) + key + getText().substring(caretPos + 1, getText().length()));
                                        } else {
                                            setText(getText().substring(0, caretPos) + key);
                                        }
                                        setCaretPosition(caretPos + 1);
                                        e.consume();
                                    }
                                }
                                if (Character.isDigit(key) && text.indexOf(".") == caretPos && INPUTMASK.indexOf(".") == text.indexOf(".")) {
                                    caretPos++;
                                    if (caretPos + 1 <= getText().length()) {
                                        setText(getText().substring(0, caretPos) + key + getText().substring(caretPos + 1, getText().length()));
                                    } else {
                                        setText(getText().substring(0, caretPos) + key);
                                    }
                                    setCaretPosition(caretPos + 1);
                                    e.consume();
                                }
                                if (text.indexOf(".") == caretPos && (key == '.' || key == ',')) {
                                    setText(getText().substring(0, caretPos) + '.' + getText().substring(caretPos + 1, getText().length()));
                                    setCaretPosition(caretPos + 1);
                                    e.consume();
                                }
                                if (Character.isDigit(key) && caretPos < INPUTMASK.indexOf(".") && text.indexOf(".") == INPUTMASK.indexOf(".")) {
                                    if (caretPos + 1 <= getText().length()) {
                                        setText(getText().substring(0, caretPos) + key + getText().substring(caretPos + 1, getText().length()));
                                    } else {
                                        setText(getText().substring(0, caretPos) + key);
                                    }
                                    setCaretPosition(caretPos + 1);
                                    e.consume();
                                }
                                if (key == '.' || key == ',') {
                                    e.consume();
                                }
                            } else {
                                if (Character.isDigit(key)) {
                                    if (caretPos + 1 <= getText().length()) {
                                        setText(getText().substring(0, caretPos) + key + getText().substring(caretPos + 1, getText().length()));
                                    } else {
                                        setText(getText().substring(0, caretPos) + key);
                                    }
                                    setCaretPosition(caretPos + 1);
                                }
                                e.consume();
                            }
                        }
                        break;
                    case V.TYPE_CHAR:
                        if (!INPUTMASK.equals("")) {
                            if (getText().length() == INPUTMASK.length()) {
                                e.consume();
                            }
                            if (!Character.isDigit(key)) {
                                e.consume();
                            }
                        }
                        break;
                    case V.TYPE_DATE:
                        if (Character.isDigit(key)) {
                            if (getSelectedText() != null) {
                                if (getSelectedText().substring(0, 1).equals(".")) {
                                    if (getSelectedText().length() == 1) {
                                        setCaretPosition(caretPos + 1);
                                    } else {
                                        int selPos = getSelectionStart();
                                        setText(getText().substring(0, getSelectionStart()) + "." + key + V.DATE_TEXT.substring(getSelectionStart() + 2, getSelectionEnd()) + getText().substring(getSelectionEnd(), getText().length()));
                                        setCaretPosition(selPos + 2);
                                        e.consume();
                                    }
                                } else {
                                    int selPos = getSelectionStart();
                                    setText(getText().substring(0, getSelectionStart()) + key + V.DATE_TEXT.substring(getSelectionStart() + 1, getSelectionEnd()) + getText().substring(getSelectionEnd(), getText().length()));
                                    setCaretPosition(selPos + 1);
                                    e.consume();
                                }
                            } else {
                                datetimeKeyTyping(caretPos, key, false);
                            }
                        }
                        e.consume();
                        break;
                    case V.TYPE_DATETIME:
                        if (Character.isDigit(key)) {
                            if (getSelectedText() != null) {
                                if (getSelectedText().substring(0, 1).equals(" ") && (caretPos == 10 || caretPos == 11)) {
                                    if (getSelectedText().length() == 1) {
                                        setCaretPosition(caretPos + 1);
                                    } else {
                                        int selPos = getSelectionStart();
                                        setText(getText().substring(0, getSelectionStart()) + " " + key + V.DATETIME_TEXT.substring(getSelectionStart() + 2, getSelectionEnd()) + getText().substring(getSelectionEnd(), getText().length()));
                                        setCaretPosition(selPos + 2);
                                        e.consume();
                                    }
                                } else if (getSelectedText().substring(0, 1).equals(":")) {
                                    if (getSelectedText().length() == 1) {
                                        setCaretPosition(caretPos + 1);
                                    } else {
                                        int selPos = getSelectionStart();
                                        setText(getText().substring(0, getSelectionStart()) + ":" + key + V.DATETIME_TEXT.substring(getSelectionStart() + 2, getSelectionEnd()) + getText().substring(getSelectionEnd(), getText().length()));
                                        setCaretPosition(selPos + 2);
                                        e.consume();
                                    }
                                } else if (getSelectedText().substring(0, 1).equals(".")) {
                                    if (getSelectedText().length() == 1) {
                                        setCaretPosition(caretPos + 1);
                                    } else {
                                        int selPos = getSelectionStart();
                                        setText(getText().substring(0, getSelectionStart()) + "." + key + V.DATETIME_TEXT.substring(getSelectionStart() + 2, getSelectionEnd()) + getText().substring(getSelectionEnd(), getText().length()));
                                        setCaretPosition(selPos + 2);
                                        e.consume();
                                    }
                                } else {
                                    int selPos = getSelectionStart();
                                    setText(getText().substring(0, getSelectionStart()) + key + V.DATETIME_TEXT.substring(getSelectionStart() + 1, getSelectionEnd()) + getText().substring(getSelectionEnd(), getText().length()));
                                    setCaretPosition(selPos + 1);
                                    e.consume();
                                }
                            } else {
                                datetimeKeyTyping(caretPos, key, true);
                            }
                        }
                        e.consume();
                        break;
                }
                if (!TYPE.equals(V.TYPE_CHAR_TOOLBAR)) {
                    THISFORM.KEYPRESS(e.getComponent(), e);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {  // нажатие любой клавиши
                int caretPos = getCaretPosition();
//                int code = e.getKeyCode();
                if (READONLY && KeyEvent.VK_ENTER != e.getKeyCode()) { //если запрет на корректуру  ничего не предпринимать RKV 21/02/2017
                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C && getSelectedText() != null) { //если Ctrl-C
                        FF._CLIPTEXT(getSelectedText());
                    }

                    e.consume();
                    return;
                }

                if (INPUTMASK.equals(V.MASK_DATETIME) || INPUTMASK.equals(V.MASK_DATE)) {

                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                        String clip = FF._CLIPTEXTOUT().replaceAll("-", ".").replaceAll("/", ".").replaceAll(",", ".");
                        FF._CLIPTEXT(clip);
                        if (P.regExpCheck(clip, "^[0-9]{2}[.]{1}[0-9]{2}[.]{1}[0-9]{4}")) {//маска date
                            if (INPUTMASK.equals(V.MASK_DATE)) {
                                setText("");
                            } else {
                                e.consume();
                                return;
                            }
                        } else if (P.regExpCheck(clip, "^[0-9]{2}[.]{1}[0-9]{2}[.]{1}[0-9]{4}[ ]{1}[0-9]{2}[:]{1}[0-9]{2}")) {//маска datetime
                            if (INPUTMASK.equals(V.MASK_DATETIME)) {
                                setText("");
                            } else {
                                e.consume();
                                return;
                            }
                        } else {
                            e.consume();
                            return;
                        }
                    }

                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X && getSelectedText() != null) {
                        FF._CLIPTEXT(getSelectedText());
                        int selPos = getSelectionStart();
                        setText(getText().substring(0, getSelectionStart()) + V.DATETIME_TEXT.substring(getSelectionStart(), getSelectionEnd()) + getText().substring(getSelectionEnd(), getText().length()));
                        setCaretPosition(selPos);
                        e.consume();
                    }

                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        if (getSelectedText() != null) {
                            int selPos = getSelectionStart();
                            setText(getText().substring(0, getSelectionStart()) + V.DATETIME_TEXT.substring(getSelectionStart(), getSelectionEnd()) + getText().substring(getSelectionEnd(), getText().length()));
                            setCaretPosition(selPos);
                            e.consume();
                        } else if (caretPos != 0
                                && (caretPos == 11
                                || (getText().toString().substring(caretPos - 1, caretPos).equals(".")
                                || getText().toString().substring(caretPos - 1, caretPos).equals(":")))) {
                            setCaretPosition(caretPos - 1);
                            e.consume();
                        } else if (caretPos != 0) {
                            if (caretPos == 1) {
                                setText(" " + getText().toString().substring(1));
                                setCaretPosition(0);
                            } else if (caretPos == getText().toString().length()) {
                                setText(getText().toString().substring(0, getText().toString().length() - 1) + " ");
                                setCaretPosition(getText().toString().length() - 1);
                            } else {
                                setText(getText().toString().substring(0, caretPos - 1) + " " + getText().toString().substring(caretPos));
                                setCaretPosition(caretPos - 1);
                            }
                            e.consume();
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                        if (getSelectedText() != null) {
                            int selPos = getSelectionStart();
                            setText(getText().substring(0, getSelectionStart()) + V.DATETIME_TEXT.substring(getSelectionStart(), getSelectionEnd()) + getText().substring(getSelectionEnd(), getText().length()));
                            setCaretPosition(selPos);
                            e.consume();
                        } else if (caretPos != getText().toString().length()
                                && (caretPos == 10
                                || (getText().toString().substring(caretPos, caretPos + 1).equals(".")
                                || getText().toString().substring(caretPos, caretPos + 1).equals(":")))) {
                            setCaretPosition(caretPos + 1);
                            e.consume();
                        } else if (caretPos != getText().toString().length()) {
                            if (caretPos == 0) {
                                setText(" " + getText().toString().substring(1));
                                setCaretPosition(1);
                            } else if (caretPos == getText().toString().length() - 1) {
                                setText(getText().toString().substring(0, getText().toString().length() - 1) + " ");
                                setCaretPosition(getText().toString().length());
                            } else {
                                setText(getText().toString().substring(0, caretPos) + " " + getText().toString().substring(caretPos + 1));
                                setCaretPosition(caretPos + 1);
                            }
                            e.consume();
                        }
                    }
//            System.out.print(" key:"+e.getExtendedKeyCode()+"\n"); 
                } else if (TYPE.equals(V.TYPE_NUMERIC)) {//обработка нажатия клавиш в числовых полях

                    if (KeyEvent.VK_ENTER == e.getKeyCode() && getSelectedText() != null) {
                        System.out.println("num+enter");
                        e.consume();
                    }

                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {//ctrl V
                        String clip = FF._CLIPTEXTOUT().replaceAll(" ", "").replaceAll(",", ".");
                        if (clip.contains(".0") || clip.contains(".00")) {
                            clip = clip.substring(0, clip.indexOf("."));
                        }
                        FF._CLIPTEXT(clip);
                        if (!P.regExpCheck(clip, "^(?=.)([+-]?([0-9]*)(\\.([0-9]+))?)$")) {//маска integer || double
                            e.consume();
                            return;
                        }
                        boolean dotM = INPUTMASK.contains(".");
                        int digBeforeM = dotM ? INPUTMASK.substring(0, INPUTMASK.indexOf(".")).length() : INPUTMASK.length();
                        int digAfterM = dotM ? INPUTMASK.substring(INPUTMASK.indexOf(".") + 1).length() : -1;
                        boolean dotC = clip.contains(".");
                        int digBeforeC = dotC ? clip.substring(0, clip.indexOf(".")).length() : clip.length();
                        int digAfterC = dotC ? clip.substring(clip.indexOf(".") + 1).length() : -1;
                        if (digBeforeM < digBeforeC || digAfterM < digAfterC) {
                            e.consume();
                            return;
                        }
                        if (dotM && dotC) {
                            setText("");
                        } else if (dotM && !dotC) {
                            setText("");
                            FF._CLIPTEXT(clip + ".0");
                        } else if (!dotM && dotC) {
                            e.consume();
                        } else {
                            setText("");
                        }
                    }

                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X && getSelectedText() != null && getSelectedText().contains(".")) {
                        FF._CLIPTEXT(getSelectedText());
                        int selPos = getSelectionStart();
                        setText(getText().substring(0, getSelectionStart()) + "." + getText().substring(getSelectionEnd(), getText().length()));
                        setCaretPosition(selPos);
                        e.consume();
                    }
                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C && getSelectedText() != null) {
                        FF._CLIPTEXT(getSelectedText());
                        setText(getText().substring(0, getSelectionStart()) + getSelectedText() + getText().substring(getSelectionEnd(), getText().length()));
                        setCaretPosition(caretPos);
                        e.consume();
                    }

                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                            && caretPos != 0
                            && getText().substring(caretPos - 1, caretPos).equals(".")) {
                        setCaretPosition(caretPos - 1);
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                            && getSelectedText() != null
                            && getSelectedText().contains(".")) {
                        int selPos = getSelectionStart();
                        setText(getText().substring(0, getSelectionStart()) + "." + getText().substring(getSelectionEnd(), getText().length()));
                        setCaretPosition(selPos);
                        e.consume();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DELETE
                            && caretPos != getText().length()
                            && getText().substring(caretPos, caretPos + 1).equals(".")) {
                        setCaretPosition(caretPos + 1);
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_DELETE
                            && getSelectedText() != null
                            && getSelectedText().contains(".")) {
                        int selPos = getSelectionStart();
                        setText(getText().substring(0, getSelectionStart()) + "." + getText().substring(getSelectionEnd(), getText().length()));
                        setCaretPosition(selPos);
                        e.consume();
                    }
                } else if (TYPE.equals(V.TYPE_CHAR) && !INPUTMASK.equals("")) {
                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                        String clip = FF._CLIPTEXTOUT();
                        int digM = INPUTMASK.length();
                        int digC = clip.length();
                        if (digM < digC) {
                            e.consume();
                            return;
                        }
                        setText("");
                    }
                }
                if (!TYPE.equals(V.TYPE_CHAR_TOOLBAR)) {
                    THISFORM.KEYPRESS(e.getComponent(), e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
//            System.out.print(" keyReleased:"+e.getExtendedKeyCode()+"\n");            
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {  // 
                    try {
                        CLICK();
                    } catch (NullPointerException wxcc) {
                    }
                }
                if (!calendar && e.getClickCount() == 2) {  // если нет статуса вызова календаря и двойной щелчок - вызов общего метода формы
                    try {
                        DBCLICK();
                    } catch (NullPointerException wxcc) {
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && (TYPE.equals(V.TYPE_DATE) || TYPE.equals(V.TYPE_DATETIME)) && e.getClickCount() == 2 && calendar && isEditable()) {
                    V.CALENDAR_FORM = THISFORM;
                    calFlag = true;
                    new DateChooser(Fieldr.this);
                }
                if (e.getButton() == MouseEvent.BUTTON3 && !TYPE.equals(V.TYPE_CHAR_TOOLBAR) && HISTORY) { //правая кнопка и вызов истории введенных
                    textFr.requestFocus();
                    ArrayList<String> arr;
                    boolean valid = THISFORM.VALIDON;
                    THISFORM.VALIDON = false;
                    arr = P.SHOW_TXT_HISTORY(THISFORM, textFr);
                    String[] historyArr = new String[arr.size() + 1];
                    historyArr[0] = "Вставить из Clipboard (пусто)";
                    String STR = FF._CLIPTEXTOUT();
                    if (FF.EMPTY(STR)) {
                        historyArr[0] = "<>" + historyArr[0];
                    } else {
                        historyArr[0] = "<" + FF.SUBSTR(STR, 1, 33) + ">";
                    }
                    int ind = 1;
                    for (int i = arr.size() - 1; i >= 0; i--) {
                        historyArr[ind] = arr.get(i);
                        ind++;
                    }
                    try {
                        textFr.PR_ESC = true;
                        int n = P.MENU(historyArr, textFr);
                        if (n == 0) {
                            return;
                        }

                        if (n > 1) {
                            setText(historyArr[n - 1]);
                        } else {
                            setText(STR);
                        }
                        textFr.PR_ESC = false;
                        THISFORM.VALIDON = valid;

                    } catch (ArrayIndexOutOfBoundsException ex) {
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON2) {  //средняя кнопка
                    int n = 0;
                    String[] strm = {"Копировать все", "Вставить (Заменить)", "Добавить в конец", "Добавить в начало", "Очистить поле", "Очистить Clipboard"};
                    n = P.MENU(strm, textFr);
                    if (n == 0) {
                        return;
                    }
                    if (n == 1) {
                        FF._CLIPTEXT(getText());
                    }
                    if (n >= 2 && n <= 4) {
                        String STR = FF._CLIPTEXTOUT();
                        if (FF.EMPTY(STR)) {
                            return;
                        }
                        if (n == 3) { //в конец
                            STR = getText() + STR;
                        }
                        if (n == 4) { //в начало
                            STR = STR + getText();
                        }
                        setText(STR);
                    }
                    if (n == 5) {
                        setText("");
                    }
                    if (n == 6) {
                        FF._CLIPTEXT("");
                    }

                }

            }
        });

    }
}
