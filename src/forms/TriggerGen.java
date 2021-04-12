/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import aplclass.Create;
import baseclass.Formr;
import java.util.ArrayList;
import java.util.Arrays;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

/**
 *
 * @author dima
 */
public class TriggerGen extends Formr {
    
    ArrayList<ArrayList> rel = new ArrayList<>();
    ArrayList table = new ArrayList<>();
    ArrayList field = new ArrayList<>();
    
    public TriggerGen() {
        super("TriggerGen", "Создание триггера для таблицы " + A.GETVALS("TABLESQL.NAME"), 900, 560); //Вызов конструктора от базового класса FormMod}
    }
    
    @Override
    public void LOAD_OBJ() { //вызывается из конструктора
        L[0] = P.addobjL(this, "lb_parent_fields", "Ключевое поле родительской таблицы");
        CB[0] = P.addobjCB(this, "cb_parent_fields", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + V.PARAMTO[0] + "'"));
        L[1] = P.addobjL(this, "lb_child_tables", "Дочерние таблицы");
        CB[1] = P.addobjCB(this, "cb_child_tables", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct NAME FROM BASIC_TABLE WHERE name != '" + V.PARAMTO[0] + "' ORDER BY NAME"));
        L[2] = P.addobjL(this, "lb_child_fields", "Ключевые поля дочерних таблиц");
        CB[2] = P.addobjCB(this, "cb_child_fields", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[1].GETSELECTEDVALUE() + "'"));
        L[3] = P.addobjL(this, "lb_trigger_type", "Тип триггера");
        CB[3] = P.addobjCB(this, "cb_trigger_type", new String[]{"Удаление", "Изменение"});
        L[4] = P.addobjL(this, "lb_trigger_vid", "Вид триггера");
        CB[4] = P.addobjCB(this, "cb_trigger_vid", new String[]{"Запретить", "Разрешить"});
        B[0] = P.addobjB(this, "b_trigger", "Создать", "Скопировать код триггера в буфер обмена");
        B[1] = P.addobjB(this, "b_relation", "Создать соответствие", "Создать соответствие полей между таблицами");
        B[1].setEnabled(false);
        B[2] = P.addobjB(this, "b_relation_cancel", "Отменить соответствия", "Отменить соответствия полей между таблицами");
        B[2].setEnabled(false);
        CB[5] = P.addobjCB(this, "cb_child_tables_2", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct NAME FROM BASIC_TABLE WHERE name != '" + V.PARAMTO[0] + "' ORDER BY NAME"));
        CB[5].setEnabled(false);
        CB[6] = P.addobjCB(this, "cb_child_tables_3", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct NAME FROM BASIC_TABLE WHERE name != '" + V.PARAMTO[0] + "' ORDER BY NAME"));
        CB[6].setEnabled(false);
        CB[7] = P.addobjCB(this, "cb_child_fields_2", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[5].GETSELECTEDVALUE() + "'"));
        CB[7].setEnabled(false);
        CB[8] = P.addobjCB(this, "cb_child_fields_3", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[6].GETSELECTEDVALUE() + "'"));
        CB[8].setEnabled(false);
        CB[9] = P.addobjCB(this, "cb_relation_fields_1", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + V.PARAMTO[0] + "'"));
        CB[9].setEnabled(false);
        CB[10] = P.addobjCB(this, "cb_relation_fields_2", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[1].GETSELECTEDVALUE() + "'"));
        CB[10].setEnabled(false);
        CB[11] = P.addobjCB(this, "cb_relation_fields_3", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[5].GETSELECTEDVALUE() + "'"));
        CB[11].setEnabled(false);
        CB[12] = P.addobjCB(this, "cb_relation_fields_4", P.FILL_ARRAY_FROM_QUERY(
                "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[6].GETSELECTEDVALUE() + "'"));
        CB[12].setEnabled(false);
        
        L[5] = P.addobjL(this, "lb_relation", "Поля соответствия");
        
        T[0] = P.addobjT(this, "ta_relation", "", 900, 160);
        T[0].setEnabled(false);
        T[0].setEditable(false);
        
    }
    
    @Override
    public void LOC_ABOUT() {//Расположение объектов относительно друг друга

        //тип
        locate(L[3], null, V.LOC_LEFT, 10, null, V.LOC_UP, 15);
        locate(CB[3], L[3], V.LOC_RIGHT, 10, null, V.LOC_UP, 10);
        //вид
        locate(L[4], null, V.LOC_LEFT, 10, CB[3], V.LOC_DOWN, 15);
        locate(CB[4], L[3], V.LOC_RIGHT, 10, CB[3], V.LOC_DOWN, 10);
        //родитель
        locate(L[0], null, V.LOC_LEFT, 10, CB[1], V.LOC_DOWN, 10);
        locate(CB[0], null, V.LOC_LEFT, 10, L[0], V.LOC_DOWN, 0);
        locate(L[5], null, V.LOC_LEFT, 10, CB[0], V.LOC_DOWN, 15);
        locate(CB[9], null, V.LOC_LEFT, 10, L[5], V.LOC_DOWN, 0);
        //дите_1
        locate(L[1], L[0], V.LOC_RIGHT, 20, CB[4], V.LOC_DOWN, 10);
        locate(CB[1], L[0], V.LOC_RIGHT, 20, L[1], V.LOC_DOWN, 0);
        locate(L[2], L[0], V.LOC_RIGHT, 20, CB[1], V.LOC_DOWN, 10);
        locate(CB[2], L[0], V.LOC_RIGHT, 20, L[2], V.LOC_DOWN, 0);
        locate(CB[10], L[0], V.LOC_RIGHT, 20, L[5], V.LOC_DOWN, 0);
        //дите_2
        locate(CB[5], L[2], V.LOC_RIGHT, 20, L[1], V.LOC_DOWN, 0);
        locate(CB[7], L[2], V.LOC_RIGHT, 20, L[2], V.LOC_DOWN, 0);
        locate(CB[11], L[2], V.LOC_RIGHT, 20, L[5], V.LOC_DOWN, 0);
        //дите_3
        locate(CB[6], L[2], V.LOC_RIGHT, 230, L[1], V.LOC_DOWN, 0);
        locate(CB[8], L[2], V.LOC_RIGHT, 230, L[2], V.LOC_DOWN, 0);
        locate(CB[12], L[2], V.LOC_RIGHT, 230, L[5], V.LOC_DOWN, 0);
        //соответствие
        locate(B[1], null, V.LOC_LEFT, 6, CB[9], V.LOC_DOWN, 5);
        locate(B[2], B[1], V.LOC_RIGHT, 6, CB[9], V.LOC_DOWN, 5);
        locate(T[0], null, V.LOC_LEFT, 6, B[1], V.LOC_DOWN, 15);
        //создать
        locate(B[0], null, V.LOC_LEFT, 6, T[0].SCROLL, V.LOC_DOWN, 10);
        
    }
    
    @Override
    public void CLICK_ALL(String name) {//нажатие мышки на объекты формы
        
        if ("cb_trigger_vid".equals(name)){
            rel.clear();
            T[0].setText("");
            if (CB[3].GETSELECTEDVALUE().equals("Изменение") && CB[4].GETSELECTEDVALUE().equals("Разрешить")) {
                CB[9].setEnabled(true);
                CB[10].setEnabled(true);
                if (CB[5].isEnabled()) {
                    CB[11].setEnabled(true);
                }
                if (CB[6].isEnabled()) {
                    CB[12].setEnabled(true);
                }
                B[1].setEnabled(true);
                B[2].setEnabled(true);
                T[0].setEnabled(true);
            } else {
                CB[9].setEnabled(false);
                CB[9].SETVALUE("");
                CB[10].setEnabled(false);
                CB[10].SETVALUE("");
                if (CB[5].isEnabled()) {
                    CB[11].setEnabled(false);
                    CB[11].SETVALUE("");
                }
                if (CB[6].isEnabled()) {
                    CB[12].setEnabled(false);
                    CB[12].SETVALUE("");
                }
                B[1].setEnabled(false);
                B[2].setEnabled(false);
                T[0].setEnabled(false);
            }
        }
        
        if ("cb_trigger_type".equals(name)) {
            rel.clear();
            T[0].setText("");
            if (CB[3].GETSELECTEDVALUE().equals("Изменение") && CB[4].GETSELECTEDVALUE().equals("Разрешить")) {
                CB[9].setEnabled(true);
                CB[10].setEnabled(true);
                if (CB[5].isEnabled()) {
                    CB[11].setEnabled(true);
                }
                if (CB[6].isEnabled()) {
                    CB[12].setEnabled(true);
                }
                B[1].setEnabled(true);
                B[2].setEnabled(true);
                T[0].setEnabled(true);
            } else {
                CB[9].setEnabled(false);
                CB[9].SETVALUE("");
                CB[10].setEnabled(false);
                CB[10].SETVALUE("");
                if (CB[5].isEnabled()) {
                    CB[11].setEnabled(false);
                    CB[11].SETVALUE("");
                }
                if (CB[6].isEnabled()) {
                    CB[12].setEnabled(false);
                    CB[12].SETVALUE("");
                }
                B[1].setEnabled(false);
                B[2].setEnabled(false);
                T[0].setEnabled(false);
            }
        }
        if ("cb_child_tables".equals(name)) {
            rel.clear();
            T[0].setText("");
            CB[2].SETVALUES(P.FILL_ARRAY_FROM_QUERY(
                    "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[1].GETSELECTEDVALUE() + "'"));
            CB[10].SETVALUES(P.FILL_ARRAY_FROM_QUERY(
                    "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[1].GETSELECTEDVALUE() + "'"));
            if (!CB[1].GETSELECTEDVALUE().equals("")) {
                CB[5].setEnabled(true);
                CB[7].setEnabled(true);
                if (CB[3].GETSELECTEDVALUE().equals("Изменение") && CB[4].GETSELECTEDVALUE().equals("Разрешить")) {
                    CB[11].setEnabled(true);
                }
            } else {
                CB[5].setEnabled(false);
                CB[5].SETVALUE("");
                CB[7].setEnabled(false);
                CB[7].SETVALUE("");
                if (CB[3].GETSELECTEDVALUE().equals("Изменение") && CB[4].GETSELECTEDVALUE().equals("Разрешить")) {
                    CB[11].setEnabled(false);
                    CB[11].SETVALUE("");
                }
            }
        }
        if ("cb_child_tables_2".equals(name)) {
            rel.clear();
            T[0].setText("");
            CB[11].SETVALUES(P.FILL_ARRAY_FROM_QUERY(
                    "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[5].GETSELECTEDVALUE() + "'"));
            CB[7].SETVALUES(P.FILL_ARRAY_FROM_QUERY(
                    "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[5].GETSELECTEDVALUE() + "'"));
            if (!CB[5].GETSELECTEDVALUE().equals("")) {
                CB[6].setEnabled(true);
                CB[8].setEnabled(true);
                if (CB[3].GETSELECTEDVALUE().equals("Изменение") && CB[4].GETSELECTEDVALUE().equals("Разрешить")) {
                    CB[12].setEnabled(true);
                }
            } else {
                CB[6].setEnabled(false);
                CB[6].SETVALUE("");
                CB[8].setEnabled(false);
                CB[8].SETVALUE("");
                if (CB[3].GETSELECTEDVALUE().equals("Изменение") && CB[4].GETSELECTEDVALUE().equals("Разрешить")) {
                    CB[12].setEnabled(false);
                    CB[12].SETVALUE("");
                }
            }
        }
        if ("cb_child_tables_3".equals(name)) {
            rel.clear();
            T[0].setText("");
            CB[8].SETVALUES(P.FILL_ARRAY_FROM_QUERY(
                    "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[6].GETSELECTEDVALUE() + "'"));
            CB[12].SETVALUES(P.FILL_ARRAY_FROM_QUERY(
                    "SELECT distinct A.name FROM BASIC_COLUMN A WHERE A.NAMEO='" + CB[6].GETSELECTEDVALUE() + "'"));
        }
        if ("b_trigger".equals(name)) {
            table.clear();
            field.clear();
            if ((CB[1].GETSELECTEDVALUE().equals(CB[5].GETSELECTEDVALUE()) && !CB[1].GETSELECTEDVALUE().equals(""))
                    || (CB[1].GETSELECTEDVALUE().equals(CB[6].GETSELECTEDVALUE()) && !CB[1].GETSELECTEDVALUE().equals(""))
                    || (CB[5].GETSELECTEDVALUE().equals(CB[6].GETSELECTEDVALUE()) && !CB[5].GETSELECTEDVALUE().equals(""))) {
                P.MESSERR("Одинаковые дочерние таблицы.");
            } else if (CB[1].GETSELECTEDVALUE().equals(CB[5].GETSELECTEDVALUE())
                    && CB[1].GETSELECTEDVALUE().equals(CB[6].GETSELECTEDVALUE())
                    && CB[5].GETSELECTEDVALUE().equals(CB[6].GETSELECTEDVALUE())
                    && CB[1].GETSELECTEDVALUE().equals("")) {
                P.MESSERR("Не выбрана ни одна дочерняя таблица.");
            } else {
                if (CB[0].GETSELECTEDVALUE().equals("")
                        || CB[2].GETSELECTEDVALUE().equals("")
                        || (CB[7].GETSELECTEDVALUE().equals("") && !CB[5].GETSELECTEDVALUE().equals(""))
                        || (CB[8].GETSELECTEDVALUE().equals("") && !CB[6].GETSELECTEDVALUE().equals(""))) {
                    P.MESSERR("Не выбраны ключевые поля для таблиц.");
                } else {
                    table.add(CB[1].GETSELECTEDVALUE());
                    field.add(CB[2].GETSELECTEDVALUE());
                    if (!CB[7].GETSELECTEDVALUE().equals("")) {
                        table.add(CB[5].GETSELECTEDVALUE());
                        field.add(CB[7].GETSELECTEDVALUE());
                    }
                    if (!CB[8].GETSELECTEDVALUE().equals("")) {
                        table.add(CB[6].GETSELECTEDVALUE());
                        field.add(CB[8].GETSELECTEDVALUE());
                    }
                    FF._CLIPTEXT(Create.TRIGGER(V.PARAMTO[0], CB[0].GETSELECTEDVALUE(), table, field, CB[3].GETSELECTEDVALUE(), CB[4].GETSELECTEDVALUE(), rel));
//                    P.WRITE_INFO("Код триггера для таблицы " + V.PARAMTO[0] + " скопирован в буфер обмена.");
                    P.MESS("Код триггера для таблицы " + V.PARAMTO[0] + " скопирован в буфер обмена.");
                }
            }
        }
        if ("b_relation_cancel".equals(name) && B[2].isEnabled()) {
            rel.clear();
            T[0].setText("");
        }
        if ("b_relation".equals(name) && B[1].isEnabled()) {
            if ((CB[1].GETSELECTEDVALUE().equals(CB[5].GETSELECTEDVALUE()) && !CB[1].GETSELECTEDVALUE().equals(""))
                    || (CB[1].GETSELECTEDVALUE().equals(CB[6].GETSELECTEDVALUE()) && !CB[1].GETSELECTEDVALUE().equals(""))
                    || (CB[5].GETSELECTEDVALUE().equals(CB[6].GETSELECTEDVALUE()) && !CB[5].GETSELECTEDVALUE().equals(""))) {
                P.MESSERR("Одинаковые дочерние таблицы.");
            } else if (CB[1].GETSELECTEDVALUE().equals(CB[5].GETSELECTEDVALUE())
                    && CB[1].GETSELECTEDVALUE().equals(CB[6].GETSELECTEDVALUE())
                    && CB[5].GETSELECTEDVALUE().equals(CB[6].GETSELECTEDVALUE())
                    && CB[1].GETSELECTEDVALUE().equals("")) {
                P.MESSERR("Не выбрана ни одна дочерняя таблица.");
            } else {
                if (CB[9].GETSELECTEDVALUE().equals("")) {
                    P.MESSERR("Не выбрано поле соответствия для родительской таблицы.");
                } else {
                    if (!CB[10].GETSELECTEDVALUE().equals("")
                            || (!CB[11].GETSELECTEDVALUE().equals("") && !CB[5].GETSELECTEDVALUE().equals(""))
                            || (!CB[12].GETSELECTEDVALUE().equals("") && !CB[6].GETSELECTEDVALUE().equals(""))) {
                        String str = V.PARAMTO[0] + "." + CB[9].GETSELECTEDVALUE();
                        if (!CB[10].GETSELECTEDVALUE().equals("")) {
                            str += "<-->";
                            str += CB[1].GETSELECTEDVALUE() + "." + CB[10].GETSELECTEDVALUE();
                        }
                        if (!CB[11].GETSELECTEDVALUE().equals("")) {
                            str += "<-->";
                            str += CB[5].GETSELECTEDVALUE() + "." + CB[11].GETSELECTEDVALUE();
                        }
                        if (!CB[12].GETSELECTEDVALUE().equals("")) {
                            str += "<-->";
                            str += CB[6].GETSELECTEDVALUE() + "." + CB[12].GETSELECTEDVALUE();
                        }
                        rel.add(new ArrayList<>(Arrays.asList(CB[9].GETSELECTEDVALUE(), CB[10].GETSELECTEDVALUE(), CB[11].GETSELECTEDVALUE(), CB[12].GETSELECTEDVALUE())));
                        T[0].WRITE_MESSAGE(str);
                        CB[9].SETVALUE("");
                        CB[10].SETVALUE("");
                        CB[11].SETVALUE("");
                        CB[12].SETVALUE("");
                    } else {
                        P.MESSERR("Не выбраны поля соответствия для дочерних таблиц.");
                    }
                }
            }
        }
    }
}
