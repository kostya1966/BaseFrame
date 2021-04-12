/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import baseclass.Crypt;
import baseclass.Formr;
import prg.FF;
import prg.P;
import prg.V;

/**
 * Форма настройки соединения с сервером
 *
 * @author Kostya
 */
public class conn1 extends Formr {

    public conn1() {
        super("conn1", "Параметры соединения", 300, 500); //Вызов конструктора от базового класса FormMod

    }

    @Override
    public void DESCPROP() {
        SETRESIZABLE(1); //1-Признак фиксированного размера 0- не фиксированный
        SETMODAL(1);    //1-Модальная форма 0-не модальная
    }

    @Override
    public void LOAD_OBJ() {
        L[0] = P.addobjL(this, "l1", "Логин:", 100, 30);
        F[0] = P.addobjF(this, "t1", "", 150, 30);
        F[0].TYPE = V.TYPE_CHAR;
        F[0].setEnabled(false);
        L[1] = P.addobjL(this, "l2", "Пароль:", 100, 30);
        PF[1] = P.addobjPF(this, "t2", "", 150, 30);
        PF[1].setEchoChar('*');
        PF[1].setEnabled(false);
        L[2] = P.addobjL(this, "l3", "Сервер:", 100, 30);
        F[2] = P.addobjF(this, "t3", "", 150, 30);
        F[2].setEnabled(false);
        L[3] = P.addobjL(this, "lport", "Порт:", 100, 30);
        F[3] = P.addobjF(this, "port", "", 50, 30);
        F[3].setEnabled(false);


        L[4] = P.addobjL(this, "l4", "Имя базы:", 100, 30);
        F[4] = P.addobjF(this, "t4", "", 150, 30);
        F[4].setEnabled(false);

        L[5] = P.addobjL(this, "l5", "Записей при выборке", 250, 30);
        F[5] = P.addobjF(this, "t5", "", 150, 30);
        F[5].TYPE = V.TYPE_NUMERIC;
        F[5].INPUTMASK = "99999999";
        L[6] = P.addobjL(this, "l6", "Название соединения", 250, 30);
        F[6] = P.addobjF(this, "f6", "", 270, 30);
        F[6].setEnabled(false);
        O[0] = P.addobjO(this, new String[]{"База данных ORACLE", "База данных SQL Server", "База данных PostgreSQL"});

        B[0] = P.addobjB(this, "bok", "Сохранить", 100, 30, "Записать параметры для последующего использования");
        B[1] = P.addobjB(this, "besc", "Отмена", 100, 30, "Не сохранять изменения");


        V.VARCONN();
        F[0].setText(V.CONN_USER);
        PF[1].setText(V.CONN_PASS);
        F[2].setText(V.CONN_SERVER);
        F[3].setText(V.CONN_PORT);
        F[4].setText(V.CONN_BASA);
        F[5].setText(String.valueOf(V.CONN_RECCOUNT));
        F[6].setText(V.CONN_NAME);
        O[0].SETVALUE(FF.VAL(V.CONN_DRIVER));

//        CB[0] = P.addobjCB(this, "cb_conf", P.LOAD_CONNECTIONS_CONF_SHOPID(), 270, 30);
        B[2] = P.addobjB(this, "b_connList", "Выбор соединения", 280, 30, "Перейти к выбору соединения");

    }

//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
    @Override
    public void LOC_ABOUT() {
//        locate(CB[0], null, V.LOC_LEFT, 10, null, V.LOC_UP, 10);
        locate(B[2], null, V.LOC_LEFT, 5, null, V.LOC_UP, 10);
        locate(L[0], null, V.LOC_LEFT, 10, B[2], V.LOC_DOWN, 10);
        locate(F[0], null, V.LOC_LEFT, 100, B[2], V.LOC_DOWN, 10);
        locate(L[1], null, V.LOC_LEFT, 10, L[0], V.LOC_DOWN, 0);
        locate(PF[1], null, V.LOC_LEFT, 100, L[0], V.LOC_DOWN, 0);
        locate(L[2], null, V.LOC_LEFT, 10, L[1], V.LOC_DOWN, 0);
        locate(F[2], null, V.LOC_LEFT, 100, L[1], V.LOC_DOWN, 0);
        locate(L[3], null, V.LOC_LEFT, 10, L[2], V.LOC_DOWN, 0);
        locate(F[3], null, V.LOC_LEFT, 100, L[2], V.LOC_DOWN, 0);
        locate(L[4], null, V.LOC_LEFT, 10, L[3], V.LOC_DOWN, 0);
        locate(F[4], null, V.LOC_LEFT, 100, L[3], V.LOC_DOWN, 0);
        locate(O[0], null, V.LOC_LEFT, 10, F[4], V.LOC_DOWN, V.LOC_SPACE);
        locate(L[5], null, V.LOC_LEFT, 10, O[0], V.LOC_DOWN, 0);
        locate(F[5], null, V.LOC_LEFT, 10, L[5], V.LOC_DOWN, 0);
        locate(L[6], null, V.LOC_LEFT, 10, F[5], V.LOC_DOWN, 0);
        locate(F[6], null, V.LOC_LEFT, 10, L[6], V.LOC_DOWN, 0);

        locate(B[0], null, V.LOC_LEFT, 10, F[6], V.LOC_DOWN, V.LOC_SPACE);
        locate(B[1], null, V.LOC_RIGHT, 0, F[6], V.LOC_DOWN, V.LOC_SPACE);


    }

    @Override
    public void OPEN() {
        F[0].requestFocusInWindow();
        if (!P.LOAD_CONNECTIONS_CONF_SHOPID_LAST().equals("")) {
            String[] arr = P.LOAD_CONNECTIONS_CONF_DATA(P.LOAD_CONNECTIONS_CONF_SHOPID_LAST());
            F[0].setText(arr[1]);
            PF[1].setText(arr[2]);
            F[2].setText(arr[0]);
            F[3].setText(arr[3]);
            F[4].setText(arr[4]);
            F[6].setText(arr[5]);
        }
    }

    @Override
    public void CLICK_ALL(String name) {
        if ("bok".equals(name)) {
            if (!F[6].getText().equals("")) {
//                V.CONN_NAME = F[6].getText();
                //сохранение в файле
                String[] vars = {F[0].getText(), Crypt.encrypt(PF[1].getText()), F[2].getText(), F[4].getText(), String.valueOf(O[0].GETVALUE()), F[5].getText(), F[3].getText()};
                P.SAVETOMEM(V.fileNameConnMem, vars);
                V.VARCONN();
//                P.SETTINGS_CHECK(); //проверка настройки соединения
                this.DESTROY();
  //              P.DOFORM("conn");
            } else {
                P.MESSERR("Введите название соединения!");
            }
        }
        if ("besc".equals(name)) {
            this.DESTROY();
        }

        if ("b_connList".equals(name)) {
            P.DOFORM("conn2");
            try {
                if (V.PARAMOT[0].equals("true")) {
                    String[] arr = P.LOAD_CONNECTIONS_CONF_DATA(V.PARAMOT[1]);
                    F[0].setText(arr[1]);
                    PF[1].setText(arr[2]);
                    F[2].setText(arr[0]);
                    F[3].setText(arr[3]);
                    F[4].setText(arr[4]);
                    F[6].setText(arr[5]);
                }
            } catch (NullPointerException ex) {
            }
        }

    }
} //конец класса

