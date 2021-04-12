/* ДОБАВЛЕНО ПОЛЕ Имя сервлета 22.08.2017
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import baseclass.Formr;
import prg.P;
import prg.V;

/**
 *
 * @author DaN
 */
public class ServCon extends Formr {

    public ServCon() {
        super("ServCon", "Параметры Service_BW", 335, 350); //Вызов конструктора от базового класса FormMod  
    }

    @Override
    public void DESCPROP() {
        SETRESIZABLE(1); //1-Признак фиксированного размера 0- не фиксированный
        SETMODAL(1);    //1-Модальная форма 0-не модальная
    }

    @Override
    public void LOAD_OBJ() {
        L[0] = P.addobjL(this, "l1", "Адрес сервера:", 100, 30);
        F[0] = P.addobjF(this, "t1", "", 150, 30);
        F[0].TYPE = V.TYPE_CHAR;
        L[1] = P.addobjL(this, "l2", "Порт сервера:", 100, 30);
        F[1] = P.addobjF(this, "t2", "", 150, 30);
        F[1].TYPE = V.TYPE_CHAR;
        L[2] = P.addobjL(this, "l3", "Имя сервиса:", 100, 30);
        F[2] = P.addobjF(this, "t3", "", 150, 30);
        F[2].TYPE = V.TYPE_CHAR;
        L[7] = P.addobjL(this, "l7", "Имя сервлета:", 100, 30);
        F[7] = P.addobjF(this, "t7", "", 150, 30);
        F[7].TYPE = V.TYPE_CHAR;
        
        L[3] = P.addobjL(this, "l4", "Прокси сервер:", 100, 30);
        F[3] = P.addobjF(this, "t4", "", 150, 30);
        F[3].TYPE = V.TYPE_CHAR;
        L[4] = P.addobjL(this, "l5", "Прокси порт:", 100, 30);
        F[4] = P.addobjF(this, "t5", "", 150, 30);
        F[4].TYPE = V.TYPE_CHAR;
        L[5] = P.addobjL(this, "l6", "Имя пользователя:", 120, 30);
        F[5] = P.addobjF(this, "t6", "", 150, 30);
        F[5].TYPE = V.TYPE_CHAR;
        L[6] = P.addobjL(this, "l7", "Пароль пользователя:", 130, 30);
        F[6] = P.addobjF(this, "t7", "", 150, 30);
        F[6].TYPE = V.TYPE_CHAR;

        CH[0] = P.addobjCH(this, "ch_serv_con", "Использовать прокси");

        B[0] = P.addobjB(this, "bok", "Сохранить", 100, 30, "Записать параметры для последующего использования");
        B[1] = P.addobjB(this, "besc", "Отмена", 100, 30, "Не сохранять изменения");

        V.VARCONNSERV();
        F[0].setText(V.CONN_SERV_IP);
        F[1].setText(V.CONN_SERV_PORT);
        F[2].setText(V.CONN_SERV_NAME);
        F[3].setText(V.CONN_PROX_IP);
        F[4].setText(V.CONN_PROX_PORT);
        F[5].setText(V.CONN_PROX_NAME);
        F[6].setText(V.CONN_PROX_PASS);
        F[7].setText(V.CONN_SERVLET);

        if (V.CONN_PROX_FLAG == true) {
            CH[0].SETVALUE(true);
        } else {
            CH[0].SETVALUE(false);
            F[3].setEnabled(false);
            F[4].setEnabled(false);
            F[5].setEnabled(false);
            F[6].setEnabled(false);
        }


    }

    //расположение объектов относительно друг друга выполняется также при изменении размеров формы 
    @Override
    public void LOC_ABOUT() {

        locate(L[0], null, V.LOC_LEFT, 10, null, V.LOC_UP, 10);
        locate(F[0], null, V.LOC_LEFT, 155, null, V.LOC_UP, 10);
        locate(L[1], null, V.LOC_LEFT, 10, L[0], V.LOC_DOWN, 0);
        locate(F[1], null, V.LOC_LEFT, 155, L[0], V.LOC_DOWN, 0);
        locate(L[2], null, V.LOC_LEFT, 10, L[1], V.LOC_DOWN, 0);
        locate(F[2], null, V.LOC_LEFT, 155, L[1], V.LOC_DOWN, 0);
        locate(L[7], null, V.LOC_LEFT, 10, L[2], V.LOC_DOWN, 0);
        locate(F[7], null, V.LOC_LEFT, 155, L[2], V.LOC_DOWN, 0);
         
        locate(CH[0], null, V.LOC_LEFT, V.LOC_SPACE, F[7], V.LOC_DOWN, V.LOC_SPACE);
        locate(L[3], null, V.LOC_LEFT, 10, CH[0], V.LOC_DOWN, 0);
        locate(F[3], null, V.LOC_LEFT, 155, CH[0], V.LOC_DOWN, 0);
        locate(L[4], null, V.LOC_LEFT, 10, L[3], V.LOC_DOWN, 0);
        locate(F[4], null, V.LOC_LEFT, 155, L[3], V.LOC_DOWN, 0);
        locate(L[5], null, V.LOC_LEFT, 10, L[4], V.LOC_DOWN, 0);
        locate(F[5], null, V.LOC_LEFT, 155, L[4], V.LOC_DOWN, 0);
        locate(L[6], null, V.LOC_LEFT, 10, L[5], V.LOC_DOWN, 0);
        locate(F[6], null, V.LOC_LEFT, 155, L[5], V.LOC_DOWN, 0);

        locate(B[0], null, V.LOC_LEFT, V.LOC_SPACE, null, V.LOC_DOWN, V.LOC_SPACE);
        locate(B[1], null, V.LOC_RIGHT, V.LOC_SPACE, null, V.LOC_DOWN, V.LOC_SPACE);


    }

    @Override
    public void OPEN() {
        F[0].requestFocusInWindow();
    }

    @Override
    public void CLICK_ALL(String name) {
        if ("ch_serv_con".equals(name)) {
            if (CH[0].GETVALUE() == false) {
                F[3].setEnabled(false);
                F[4].setEnabled(false);
                F[5].setEnabled(false);
                F[6].setEnabled(false);
            } else {
                F[3].setEnabled(true);
                F[4].setEnabled(true);
                F[5].setEnabled(true);
                F[6].setEnabled(true);
            }
        }
        if ("lb_serv_con".equals(name)){
            System.out.println(LB[0].GETVALUE());
        }
        if ("bok".equals(name)) {
            //сохранение в файле
            P.SAVECONNPROP(F[0].getText(), F[1].getText(), F[2].getText(), F[5].getText(), F[6].getText(), F[3].getText(), F[4].getText(), CH[0].GETVALUE(), F[7].getText());
            this.DESTROY();
        }
        if ("besc".equals(name)) {
            this.DESTROY();
        }


    }
}
