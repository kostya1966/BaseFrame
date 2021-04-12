package forms;

import baseclass.Formr;
import java.awt.Component;
import java.awt.event.KeyEvent;
import prg.P;
import prg.V;

public class SHOP_OPTION_F extends Formr {

    String condition_n = "";

    public SHOP_OPTION_F() {
        super("SHOP_OPTION_F", "Добавление фильтра", 680, 140); //Вызов конструктора от базового класса
    }

    @Override
    public void DESCPROP() {
        SETRESIZABLE(1); //1-Признак фиксированного размера 0- не фиксированный
        SETMODAL(1);    //1-Модальная форма 0-не модальная
//        setClosable(false);

    }

    @Override
    public void KEYPRESS(Component obj, KeyEvent e) {
        super.KEYPRESS(obj, e);
//        if (obj.getName().equals("g_view")) {
//            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                CLICK_ALL("bok");
//            }
//        }
    }

    @Override
    public void LOAD_OBJ() {
        L[0] = P.addobjL(this, "l0", V.PARAMTO[1]);
        B[0] = P.addobjB(this, "bok", "Подтвердить", "Подтвердить");
        B[1] = P.addobjB(this, "besc", "Отмена", "Отмена");
        CB[0] = P.addobjCB(this, "cb0", new String[]{"И", "Или"});
        String[] str = null;
        if (V.PARAMTO[0] == null) {
            str = new String[]{"Равно", "Не равно", "Вхождение в строку", "Невхождение в строку"};
        } else if (V.PARAMTO[0].equals("D") || V.PARAMTO[0].equals("T") || V.PARAMTO[0].equals("N")) {
            str = new String[]{"Равно", "Больше или равно", "Меньше или равно", "Больше", "Меньше", "Не равно", "Вхождение в строку", "Невхождение в строку"};
        } else {
            str = new String[]{"Равно", "Не равно", "Вхождение в строку", "Невхождение в строку"};
        }
        CB[1] = P.addobjCB(this, "cb1", str);
        F[0] = P.addobjF(this, "f0", "", 150, 30);
        F[0].SETTYPE(V.TYPE_CHAR);
        F[0].SETINPUTMASK("");
        F[0].setText("");
        if (V.PARAMTO[0] != null) {
            switch (V.PARAMTO[0]) {
                case "D":
                    F[0].SETTYPE(V.TYPE_DATE);
                    break;
                case "T":
                    F[0].SETTYPE(V.TYPE_DATETIME);
                    break;
            }
        }
    }

//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
    @Override
    public void LOC_ABOUT() {
        locate(CB[0], null, V.LOC_LEFT, 5, null, V.LOC_UP, 5);
        locate(L[0], CB[0], V.LOC_RIGHT, 5, null, V.LOC_UP, 15);
        locate(CB[1], L[0], V.LOC_RIGHT, 5, null, V.LOC_UP, 5);
        locate(F[0], CB[1], V.LOC_RIGHT, 5, null, V.LOC_UP, 5);

        locate(B[0], null, V.LOC_LEFT, 5, null, V.LOC_DOWN, 5);
        locate(B[1], null, V.LOC_RIGHT, 5, null, V.LOC_DOWN, 5);
    }

    @Override
    public void DESTROY() {
        super.DESTROY();
    }

    @Override
    public void OPEN() {
        super.OPEN();
    }

    @Override
    public void CLICK_ALL(String name) {
        if ("besc".equals(name)) {
            DESTROY();
        }
        if ("bok".equals(name)) {
            V.PARAMOT = new String[3];
            V.PARAMOT[0] = "F";
            V.PARAMOT[1] = accept();
            V.PARAMOT[2] = condition_n;
            if (!V.PARAMOT[1].equals("")) {
                V.PARAMOT[0] = "T";
            }
        }
    }

    private String accept() {
        if (!F[0].getText().equals("") && !F[0].getText().equals(V.DATE_TEXT) && !F[0].getText().equals(V.DATETIME_TEXT)) {
            if (V.PARAMTO[0].equals("N") && !F[0].getText().equals("")
                    && !CB[1].GETSELECTEDVALUE().equals("Вхождение в строку") && !CB[1].GETSELECTEDVALUE().equals("Невхождение в строку")) {
                try {
                    Integer.parseInt(F[0].getText());
                } catch (NumberFormatException ex) {
                    P.MESSERR("Некорректно введено число");
                    return "";
                }
            }
            String condition = "";
            String val = "", val2 = "";
            switch (CB[0].GETSELECTEDVALUE()) {
                case "И":
                    condition += "and ";
                    break;
                case "Или":
                    condition += "or ";
                    break;
            }
            condition_n += CB[0].GETSELECTEDVALUE() + " ";
            if (V.PARAMTO[0].equals("D")) {
                val = "to_number(to_char(" + V.PARAMTO[2] + ",'yyyyMMdd')) ";
            } else if (V.PARAMTO[0].equals("T")) {
                val = "to_number(to_char(" + V.PARAMTO[2] + ",'yyyyMMddHH24miss')) ";
            } else {
                val = "upper(" + V.PARAMTO[2] + ") ";
            }
            val2 = "'" + V.PARAMTO[1] + "' ";
            condition += val;
            condition_n += val2;

            switch (CB[1].GETSELECTEDVALUE()) {
                case "Равно":
                    if (!F[0].getText().toUpperCase().equals("NULL")) {
                        condition += "= ";
                    } else {
                        condition += "is null";
                        condition_n += "нулевое значение";
                        DESTROY();
                        return condition;
                    }
                    break;
                case "Больше или равно":
                    condition += ">= ";
                    break;
                case "Меньше или равно":
                    condition += "<= ";
                    break;
                case "Больше":
                    condition += "> ";
                    break;
                case "Меньше":
                    condition += "< ";
                    break;
                case "Не равно":
                    if (!F[0].getText().toUpperCase().equals("NULL")) {
                        condition += "!= ";
                    } else {
                        condition += "is not null";
                        condition_n += "не нулевое значение";
                        DESTROY();
                        return condition;
                    }
                    break;
                case "Вхождение в строку":
                    condition += "like ";
                    if (V.PARAMTO[0].equals("D")) {
                        val = "to_number(to_char(to_date('" + F[0].getText() + "','dd.MM.yyyy'),'yyyyMMdd'))";
                    } else if (V.PARAMTO[0].equals("T")) {
                        val = "to_number(to_char(to_timestamp('" + F[0].getText() + "','dd.MM.yyyy HH24:mi:ss'),'yyyyMMddHH24miss'))";
                    } else {
                        if (F[0].getText().contains("%")) {
                            val = "upper('" + F[0].getText() + "')";
                        } else {
                            val = "upper('%" + F[0].getText() + "%')";
                        }
                    }
                    condition += val;
                    condition_n += "включает в себя подстроку '" + F[0].getText() + "'";
                    DESTROY();
                    return condition;
                case "Невхождение в строку":
                    condition += "not like ";
                    if (V.PARAMTO[0].equals("D")) {
                        val = "to_number(to_char(to_date('" + F[0].getText() + "','dd.MM.yyyy'),'yyyyMMdd'))";
                    } else if (V.PARAMTO[0].equals("T")) {
                        val = "to_number(to_char(to_timestamp('" + F[0].getText() + "','dd.MM.yyyy HH24:mi:ss'),'yyyyMMddHH24miss'))";
                    } else {
                        if (F[0].getText().contains("%")) {
                            val = "upper('" + F[0].getText() + "')";
                        } else {
                            val = "upper('%" + F[0].getText() + "%')";
                        }
                    }
                    condition += val;
                    condition_n += "не включает в себя подстроку '" + F[0].getText() + "'";
                    DESTROY();
                    return condition;
            }
            if (V.PARAMTO[0].equals("D")) {
                val = "to_number(to_char(to_date('" + F[0].getText() + "','dd.MM.yyyy'),'yyyyMMdd'))";
            } else if (V.PARAMTO[0].equals("T")) {
                val = "to_number(to_char(to_timestamp('" + F[0].getText() + "','dd.MM.yyyy HH24:mi:ss'),'yyyyMMddHH24miss'))";
            } else if (V.PARAMTO[0].equals("N")) {
                val = F[0].getText();
            } else {
                val = "upper('" + F[0].getText() + "')";
            }
            condition += val;
            condition_n += CB[1].GETSELECTEDVALUE() + " '" + F[0].getText() + "'";
            DESTROY();
            return condition;
        }
        DESTROY();
        return "";
    }
}
