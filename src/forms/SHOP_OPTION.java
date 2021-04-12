package forms;

import baseclass.Formr;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import prg.A;
import prg.P;
import prg.V;

public class SHOP_OPTION extends Formr {

    JPanel pane = new JPanel();
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(pane);
    JScrollPane scrollPane = new JScrollPane();
    ArrayList<JCheckBox> ch1 = new ArrayList<>();
    ArrayList<JCheckBox> ch2 = new ArrayList<>();
    ArrayList<String> condArr = new ArrayList<>();
    String select = "";

    public SHOP_OPTION() {
        super("SHOP_OPTION", "Создание запроса", 680, 390); //Вызов конструктора от базового класса
        select = V.PARAMTO[0];
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
        for (int i = 0; i < V.OPTION_ARR.size(); i++) {
            L[i] = P.addobjL(this, "l_" + i, V.OPTION_ARR.get(i));
            F[i] = P.addobjF(this, "f_" + i, "", 200, 30);
            F[i].SETTYPE(V.TYPE_CHAR);
            F[i].SETINPUTMASK("");
            F[i].setText("");
            F[i].setEditable(false);
            F[i].setMinimumSize(new Dimension(100, F[i].getHeight()));
            B[i + 2] = P.addobjB(this, "b" + i, "", "Добавить фильтр");
            B[i + 2].setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/view-filter.png")));
            ch1.add(new JCheckBox(""));
            ch2.add(new JCheckBox(""));
            if (V.OPTION_TYPE_ARR.get(i) != null && !V.OPTION_TYPE_ARR.get(i).equals("N")) {
                ch2.get(i).setVisible(false);
            }
            if (V.OPTION_TYPE_ARR.get(i) == null) {
                ch2.get(i).setVisible(false);
            }
//            CH[i] = P.addobjCH(this, "ch_" + i, "");
//            CH[i + V.OPTION_ARR.size()] = P.addobjCH(this, "ch_" + (i + V.OPTION_ARR.size()), "");
//            if (!V.OPTION_TYPE_ARR.get(i).equals("N")) {
//                CH[i + V.OPTION_ARR.size()].setVisible(false);
//            }
//            switch (V.OPTION_TYPE_ARR.get(i)) {
//                case "D":
//                    F[i].SETTYPE(V.TYPE_DATE);
//                    break;
//                case "T":
//                    F[i].SETTYPE(V.TYPE_DATETIME);
//                    break;
////                case "N":
////                    F[i].SETTYPE(V.TYPE_NUMERIC);
////                    F[i].SETINPUTMASK("############################################################");
////                    break;
//            }
        }

        L[V.OPTION_ARR.size() + 1] = P.addobjL(this, "l_gr", "Группировать по:");
        L[V.OPTION_ARR.size() + 2] = P.addobjL(this, "l_sum", "Суммировать по:");
////
        B[0] = P.addobjB(this, "bok", "Подтвердить", "Подтвердить");
        B[1] = P.addobjB(this, "besc", "Отмена", "Отмена");

        pane.setSize(new Dimension(660, 300));
        scrollPane.setSize(new Dimension(660, 300)); // whatever
        scrollPane.setViewportView(pane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        pane.setLayout(layout);
        ParallelGroup pGroupLabel = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        ParallelGroup pGroupField = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        ParallelGroup pGroupCH1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        ParallelGroup pGroupCH2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        ParallelGroup pGroupB = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (int i = 0; i < V.OPTION_ARR.size(); i++) {
            pGroupB.addComponent(B[i + 2]);
            pGroupLabel.addComponent(L[i]);
            pGroupField.addComponent(F[i]);
            if (i == 0) {
                pGroupCH1.addComponent(L[V.OPTION_ARR.size() + 1]);
                pGroupCH2.addComponent(L[V.OPTION_ARR.size() + 2]);
            }
            pGroupCH1.addComponent(ch1.get(i));
            pGroupCH2.addComponent(ch2.get(i));
        }
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(pGroupLabel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
                .addGroup(pGroupField)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
                .addGroup(pGroupB)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
                .addGroup(pGroupCH1).addGap(20)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
                .addGroup(pGroupCH2)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
        );

        SequentialGroup pGroupV = layout.createSequentialGroup();
        pGroupV.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(L[V.OPTION_ARR.size() + 1])
                .addComponent(L[V.OPTION_ARR.size() + 2]));
        for (int i = 0; i < V.OPTION_ARR.size(); i++) {
            pGroupV.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(L[i])
                    .addComponent(F[i])
                    .addComponent(B[i + 2])
                    .addComponent(ch1.get(i))
                    .addComponent(ch2.get(i))
            );
        }
        layout.setVerticalGroup(pGroupV);
    }

//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
    @Override
    public void LOC_ABOUT() {
//        locate(L[V.OPTION_ARR.size() + 2], null, V.LOC_RIGHT, 5, null, V.LOC_UP, 5);
//        locate(L[V.OPTION_ARR.size() + 1], L[V.OPTION_ARR.size() + 2], V.LOC_LEFT, 15, null, V.LOC_UP, 5);
//
//        locate(L[0], null, V.LOC_LEFT, 5, null, V.LOC_UP, 75);
//        locate(F[0], null, V.LOC_LEFT, 200, null, V.LOC_UP, 70);
//        locate(CH[0], F[0], V.LOC_RIGHT, 5, L[V.OPTION_ARR.size() + 1], V.LOC_DOWN, 0);
//        locate(CH[V.OPTION_ARR.size()], L[V.OPTION_ARR.size() + 1], V.LOC_RIGHT, 10, L[V.OPTION_ARR.size() + 1], V.LOC_DOWN, 0);
//
//        for (int i = 1; i < V.OPTION_ARR.size(); i++) {
//            locate(L[i], null, V.LOC_LEFT, 5, F[i - 1], V.LOC_DOWN, 10);
//            locate(F[i], null, V.LOC_LEFT, 200, F[i - 1], V.LOC_DOWN, 5);
//            locate(CH[i], F[0], V.LOC_RIGHT, 5, F[i - 1], V.LOC_DOWN, 0);
//            locate(CH[i + V.OPTION_ARR.size()], L[V.OPTION_ARR.size() + 1], V.LOC_RIGHT, 10, F[i - 1], V.LOC_DOWN, 0);
//        }
//
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
            boolean sumFlag = false;
            int groupInc = 0;
            for (int i = 0; i < V.OPTION_ARR.size(); i++) {
                if (ch2.get(i).isSelected()) {
                    sumFlag = true;
                }
                if (ch1.get(i).isSelected()) {
                    groupInc++;
                }
//                if (V.OPTION_TYPE_ARR.get(i).equals("N") && !F[i].getText().equals("")) {
//                    try {
//                        Integer.parseInt(F[i].getText());
//                    } catch (NumberFormatException ex) {
//                        P.MESSERR("Некорректно введено число в поле - " + V.OPTION_ARR.get(i));
//                        return;
//                    }
//                }
            }
            if (sumFlag && groupInc == 0) {
                P.MESSERR("Выбрано поле для суммирования, но не выбрано поле для группировки!");
            } else {
                acceptClick();
            }
        }
        for (int i = 0; i < V.OPTION_ARR.size(); i++) {
            if (("b" + i).equals(name)) {
                V.PARAMTO = new String[3];
                V.PARAMTO[0] = V.OPTION_TYPE_ARR.get(i);
                V.PARAMTO[1] = V.OPTION_ARR.get(i);
                V.PARAMTO[2] = V.OPTION_NAME_ARR.get(i);
                P.DOFORM("SHOP_OPTION_F");
                if (V.PARAMOT != null && V.PARAMOT[0] != null && V.PARAMOT[0].equals("T")) {
                    F[i].setText(F[i].getText() + V.PARAMOT[2] + "; ");
                    condArr.add(V.PARAMOT[1]);
                }
            }
        }
    }

    private void acceptClick() {
        ArrayList<Integer> arr_int = new ArrayList<>();
        ArrayList<String> arr_rname = new ArrayList<>();
        ArrayList<String> arr_ename = new ArrayList<>();
        boolean fl = false, fl_2 = false;
        String buf = "select ", buf_2 = " where ";
        int kol_p = 0, kol_s = 0;
        for (int i = 0; i < V.OPTION_ARR.size(); i++) {
            if (ch1.get(i).isSelected()) {
                fl = true;
                buf += V.OPTION_NAME_ARR.get(i) + ", ";
                kol_p++;
                arr_int.add(i);
            }
        }
        if (fl) {
            buf = buf.substring(0, buf.length() - 2) + " ";
        } else {
            buf += " * ";
            kol_p = V.OPTION_ARR.size();
            for (int j = 0; j < kol_p; j++) {
                arr_int.add(j);
            }
        }

        for (int i = 0; i < V.OPTION_ARR.size(); i++) {
            if (ch2.get(i).isSelected()) {
                buf += ", sum(" + V.OPTION_NAME_ARR.get(i) + ") as \"SUM_" + V.OPTION_NAME_ARR.get(i) + "\" ";
                arr_ename.add(V.OPTION_NAME_ARR.get(i));
                arr_rname.add(V.OPTION_ARR.get(i));
                kol_s++;
            }
        }

        buf += " from (" + select + ") ";

//        for (int i = 0; i < V.OPTION_ARR.size(); i++) {
//            if (!F[i].getText().equals("") && !F[i].getText().equals(V.DATE_TEXT) && !F[i].getText().equals(V.DATETIME_TEXT)) {
//                fl_2 = true;
//                switch (V.OPTION_TYPE_ARR.get(i)) {
//                    case "T":
//                        buf_2 += " upper(" + "to_char(" + V.OPTION_NAME_ARR.get(i) + ",'dd.MM.yyyy HH24:mi:ss')) like upper('%" + F[i].getText() + "%') and ";
//                        break;
//                    case "D":
//                        buf_2 += " upper(" + "to_char(" + V.OPTION_NAME_ARR.get(i) + ",'dd.MM.yyyy')) like upper('%" + F[i].getText() + "%') and ";
//                        break;
//                    case "N":
//                        buf_2 += V.OPTION_NAME_ARR.get(i) + " = " + F[i].getText() + " and ";
//                        break;
//                    default:
//                        buf_2 += " upper(" + V.OPTION_NAME_ARR.get(i) + ") like upper('%" + F[i].getText() + "%') and ";
//                }
//            }
//        }
//        if (fl_2) {
//            buf_2 = buf_2.substring(0, buf_2.length() - 5) + " ";
//            buf += buf_2;
//        }
        if (condArr.size() > 0) {
            buf += buf_2;
            for (int i = 0; i < condArr.size(); i++) {
                if (i == 0 && condArr.get(i).substring(0, 2).equals("an")) {
                    buf += condArr.get(i).substring(4) + " ";
                } else if (i == 0 && condArr.get(i).substring(0, 2).equals("or")) {
                    buf += condArr.get(i).substring(3) + " ";
                } else {
                    buf += condArr.get(i) + " ";
                }
            }
        }

        if (!fl) {
            DESTROY();
            System.out.println(buf);
            P.SQLEXECT(buf, "F_SEL_2");
            Object[] obj = new Object[kol_p * 3 + kol_s * 3];
            for (int i = 0; i < kol_p; i++) {
                obj[i] = V.OPTION_ARR.get(arr_int.get(i));
                obj[i + kol_p + kol_s] = V.OPTION_NAME_ARR.get(arr_int.get(i));
                obj[i + kol_p * 2 + kol_s * 2] = 100;
            }
            for (int i = kol_p; i < kol_s + kol_p; i++) {
                obj[i] = "Сумма_" + arr_rname.get(i - kol_p);
                obj[i + kol_p + kol_s] = "SUM_" + arr_ename.get(i - kol_p);
                obj[i + kol_p * 2 + kol_s * 2] = 100;
            }
            P.DOVIEW("F_SEL_2", "Результирующая раблица", obj);
            A.CLOSE("F_SEL_2");
            return;
        }
        buf += " group by ";

        for (int i = 0; i < V.OPTION_ARR.size(); i++) {
            if (ch1.get(i).isSelected()) {
                buf += V.OPTION_NAME_ARR.get(i) + ", ";
            }
        }
        buf = buf.substring(0, buf.length() - 2) + " ";
        DESTROY();
        System.out.println(buf);
        P.SQLEXECT(buf, "F_SEL_2");
        Object[] obj = new Object[kol_p * 3 + kol_s * 3];
        for (int i = 0; i < kol_p; i++) {
            obj[i] = V.OPTION_ARR.get(arr_int.get(i));
            obj[i + kol_p + kol_s] = V.OPTION_NAME_ARR.get(arr_int.get(i));
            obj[i + kol_p * 2 + kol_s * 2] = 100;
        }
        for (int i = kol_p; i < kol_s + kol_p; i++) {
//                obj[i] = "Сумма_" + (i - kol_p + 1);
//                obj[i + kol_p + kol_s] = "SUM_" + (i - kol_p + 1);
            obj[i] = "Сумма_" + arr_rname.get(i - kol_p);
            obj[i + kol_p + kol_s] = "SUM_" + arr_ename.get(i - kol_p);
            obj[i + kol_p * 2 + kol_s * 2] = 100;
        }
        P.DOVIEW("F_SEL_2", "Результирующая раблица", obj);
        A.CLOSE("F_SEL_2");
    }
}
