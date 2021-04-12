/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import baseclass.Crypt;
import baseclass.Formr;
import baseclass.JRadioButtons;
import java.beans.PropertyVetoException;
import prg.FF;
import prg.P;
import prg.V;

/**
 * Форма настройки соединения с сервером
 *
 * @author Kostya
 */
public class conn2 extends Formr {

    public conn2() {
        super("conn2", "Выбор соединения", 200, 300); //Вызов конструктора от базового класса FormMod

    }

    @Override
    public void DESCPROP() {
        SETRESIZABLE(1); //1-Признак фиксированного размера 0- не фиксированный
        SETMODAL(1);    //1-Модальная форма 0-не модальная
    }

    @Override
    public void LOAD_OBJ() {
        LB[0] = P.addobjLB(this, "lb1", P.LOAD_CONNECTIONS_CONF_SHOPID(), 13);
        B[0] = P.addobjB(this, "bok", "Выбор", 100, 30, "Выбрать соединение");
        L[0] = P.addobjL(this, "l1", "Логин:", 100, 30);
        F[0] = P.addobjF(this, "t1", "", 180, 30);
        F[0].TYPE = V.TYPE_CHAR;
        L[1] = P.addobjL(this, "l2", "Пароль:", 100, 30);
        PF[1] = P.addobjPF(this, "t2", "", 180, 30);
        PF[1].setEchoChar('*');
        L[2] = P.addobjL(this, "l3", "Сервер:", 100, 30);
        F[2] = P.addobjF(this, "t3", "", 180, 30);
        L[3] = P.addobjL(this, "lport", "Порт:", 100, 30);
        F[3] = P.addobjF(this, "port", "", 50, 30);


        L[4] = P.addobjL(this, "l4", "Имя базы:", 100, 30);
        F[4] = P.addobjF(this, "t4", "", 180, 30);

        L[5] = P.addobjL(this, "l5", "Название соединения", 250, 30);
        F[5] = P.addobjF(this, "f5", "", 180, 30);

        B[1] = P.addobjB(this, "besc", "Отмена", 100, 30, "Отмена");

        B[2] = P.addobjB(this, "b2", "Сохранить", 100, 30, "Сохранить соединение");
        B[3] = P.addobjB(this, "b3", "Удалить", 100, 30, "Удалить соединение");

        V.VARCONN();
        F[0].setText(V.CONN_USER);
        PF[1].setText(V.CONN_PASS);
        F[2].setText(V.CONN_SERVER);
        F[3].setText(V.CONN_PORT);
        F[4].setText(V.CONN_BASA);
        F[5].setText(V.CONN_NAME);

    }

//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
    @Override
    public void LOC_ABOUT() {
        locate(LB[0], null, V.LOC_LEFT, 10, null, V.LOC_UP, 5);
        locate(B[0], null, V.LOC_LEFT, 10, LB[0], V.LOC_DOWN, 5);
        locate(L[0], LB[0], V.LOC_RIGHT, 10, null, V.LOC_UP, 5);
        locate(F[0], LB[0], V.LOC_RIGHT, 140, null, V.LOC_UP, 5);
        locate(L[1], LB[0], V.LOC_RIGHT, 10, L[0], V.LOC_DOWN, 0);
        locate(PF[1], LB[0], V.LOC_RIGHT, 140, L[0], V.LOC_DOWN, 0);
        locate(L[2], LB[0], V.LOC_RIGHT, 10, L[1], V.LOC_DOWN, 0);
        locate(F[2], LB[0], V.LOC_RIGHT, 140, L[1], V.LOC_DOWN, 0);
        locate(L[3], LB[0], V.LOC_RIGHT, 10, L[2], V.LOC_DOWN, 0);
        locate(F[3], LB[0], V.LOC_RIGHT, 140, L[2], V.LOC_DOWN, 0);
        locate(L[4], LB[0], V.LOC_RIGHT, 10, L[3], V.LOC_DOWN, 0);
        locate(F[4], LB[0], V.LOC_RIGHT, 140, L[3], V.LOC_DOWN, 0);
        locate(L[5], LB[0], V.LOC_RIGHT, 10, L[4], V.LOC_DOWN, 0);
        locate(F[5], LB[0], V.LOC_RIGHT, 140, L[4], V.LOC_DOWN, 0);
        locate(B[3], null, V.LOC_RIGHT, 8, L[5], V.LOC_DOWN, 18);
        locate(B[2], B[3], V.LOC_LEFT, 5, L[5], V.LOC_DOWN, 18);

        locate(B[1], null, V.LOC_RIGHT, 8, LB[0], V.LOC_DOWN, 5);


    }

    @Override
    public void OPEN() {
        setBounds(getX(), getY(), LB[0].getWidth() + 140 + F[5].getWidth() + 30, getHeight());
        LB[0].SETVALUE(P.LOAD_CONNECTIONS_CONF_SHOPID_LAST());
    }

    @Override
    public void CLICK_ALL(String name) {
        if ("bok".equals(name)) {
            V.PARAMOT = new String[2];
            V.PARAMOT[0] = "true";
            V.PARAMOT[1] = LB[0].GETVALUE();
            V.CONN_NAME = LB[0].GETVALUE();
            P.UPDATE_CONNECTIONS_LASTCONN(LB[0].GETVALUE());
            this.DESTROY();
        }
        if ("besc".equals(name)) {
            V.PARAMOT = new String[1];
            V.PARAMOT[0] = "false";
            this.DESTROY();
        }
        if ("lb1".equals(name)) {
            try {
                String[] arr = P.LOAD_CONNECTIONS_CONF_DATA(LB[0].GETVALUE());
                F[0].setText(arr[1]);
                PF[1].setText(arr[2]);
                F[2].setText(arr[0]);
                F[3].setText(arr[3]);
                F[4].setText(arr[4]);
                F[5].setText(arr[5]);
            } catch (ArrayIndexOutOfBoundsException ex) {
            }
        }

        if ("b2".equals(name)) {
            if (F[5].getText().equals("")) {
                P.MESSERR("Введите название соединения!");
                return;
            }
            boolean exFlag = false;
            for (int i = 0; i < LB[0].GETCOUNT(); i++) {
                if (LB[0].GETVALUE_BY_INDEX(i).equals(F[5].getText())) {
                    exFlag = true;
                }
            }
            if (!exFlag) {
                P.UPDATE_CONNECTIONS_CONF(F[2].getText(), F[0].getText(), PF[1].getText(), F[3].getText(), F[4].getText(), F[5].getText());
                LB[0].SET_LIST_DATA(P.LOAD_CONNECTIONS_CONF_SHOPID());
                LB[0].SETVALUE(F[5].getText());
            } else {
                if (P.MESSYESNO("Перезаписать данные по соединению \"" + F[5].getText() + "\"?") == 0) {
                    P.UPDATE_CONNECTIONS_CONF(F[2].getText(), F[0].getText(), PF[1].getText(), F[3].getText(), F[4].getText(), F[5].getText());
                }
            }
        }
        if ("b3".equals(name)) {
            if (F[5].getText().equals("")) {
                P.MESSERR("Введите название соединения!");
                return;
            }
            if (P.MESSYESNO("Удалить соединение \"" + F[5].getText() + "\"?") == 0) {
                P.DELETE_CONNECTION(F[5].getText());
                LB[0].SET_LIST_DATA(P.LOAD_CONNECTIONS_CONF_SHOPID());
                if (LB[0].GETCOUNT() != 0) {
                    LB[0].SET_INDEX(0);
                }
            }
        }

    }
} //конец класса

