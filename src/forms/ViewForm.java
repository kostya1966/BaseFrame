package forms;

import baseclass.Formr;
import baseclass.Gridr;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import javax.swing.ListSelectionModel;
import prg.P;
import prg.V;

public class ViewForm extends Formr {

    String alias, header, ok, esc;
    int width, height, coledit;
    ArrayList<String> arr = new ArrayList<>();
    public String C[], FD[];
    public int S[];

    public ViewForm() {

        super(V.PARAMTO[0], V.PARAMTO[1], 500, 400); //Вызов конструктора от базового класса 

        alias = V.PARAMTO[0];
        header = V.PARAMTO[1];
        width = Integer.parseInt(V.PARAMTO[2]);
        height = Integer.parseInt(V.PARAMTO[3]);
        ok = V.PARAMTO[4];
        esc = V.PARAMTO[5];
        coledit = Integer.parseInt(V.PARAMTO[6]);

        for (int i = 7; i < V.PARAMTO.length; i++) {
            arr.add(V.PARAMTO[i]);
        }
        C = new String[arr.size() / 3];// = {"ФИО", "Дата рождения"};
        FD = new String[arr.size() / 3];// = {"FIO", "BIRTHDAY"};
        S = new int[arr.size() / 3];// = {200, 200};
        int k = 0, w = 0;
        for (int i = 0; i < arr.size() / 3; i++) {
            C[k] = arr.get(i);
            FD[k] = arr.get(i + arr.size() / 3);
            S[k] = Integer.parseInt(arr.get(i + arr.size() / 3 * 2));
            w = w + S[k] + 2;
            k++;
        }
        if (width == 0) {
            width = w+100;
        }
        if (height == 0) {
            height = 500;
        }

        setName(alias);
        setTitle(header);
        setBounds(getX(), getY(), width, height);
    }

    @Override
    public void DESCPROP() {
        SETRESIZABLE(0); //1-Признак фиксированного размера 0- не фиксированный
        SETMODAL(1);    //1-Модальная форма 0-не модальная
//        setClosable(false);

    }

    @Override
    public void KEYPRESS(Component obj, KeyEvent e) {
        super.KEYPRESS(obj, e);
        if (obj.getName().equals("g_view")) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                CLICK_ALL("bok");
            }
        }
    }

    
    @Override
    public void LOAD_OBJ() {
        B[0] = P.addobjB(this, "bok", ok, "Кнопка подтверждения");
        B[1] = P.addobjB(this, "besc", esc, "Кнопка отрицания");
        B[2] = P.addobjB(this, "B2", "+ВСЕ", "Отметить все записи");
        B[3] = P.addobjB(this, "B3", "-ВСЕ", "Снять пометки у всех записей");
        G[0] = P.addobjG(this, "g_view", 1, 1, 300, 300);
        try {
            G[0].setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        } catch (NullPointerException ex) {
        }
        if (coledit>0) {
        G[0].getColumn(coledit).READONLY=false;
        G[0].getColumn(coledit).TYPE=G[0].DATA.COLTYPE[coledit];
        }
        B[2].setVisible(FD[0].length()>=4 && "BIT_".equals(FD[0].substring(0, 4)));
        B[3].setVisible(B[2].isVisible());
        G[0].STAT_DBCLICK=!B[2].isVisible()&& V.ViewForm_STAT_DBCLICK == true; //если нет выборы по  первой колонке то выбор по щелчку если V.ViewForm_STAT_DBCLICK == true
    }

//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
    @Override
    public void LOC_ABOUT() {
        try {
            G[0].SCROLL.setBounds(5, 5, this.getWidth() - 20, this.getHeight() - 80);
        } catch (NullPointerException ex) {
        }
        locate(B[0], null, V.LOC_LEFT, 5, G[0].SCROLL, V.LOC_DOWN, 8);
        locate(B[2], B[0], V.LOC_RIGHT, 5, G[0].SCROLL, V.LOC_DOWN, 8);

        locate(B[1], null, V.LOC_RIGHT, 5, G[0].SCROLL, V.LOC_DOWN, 8);
        locate(B[3], B[1], V.LOC_LEFT, 5, G[0].SCROLL, V.LOC_DOWN, 8);

    }

    @Override
    public void TABLEPROP(Gridr GRID) {//Свойства грида выполняется в P.addobjG
        GRID.VIEWRECNO = 0; //не показывать номер записи в первой колонке -0
        GRID.RESTORECOL = 0;//не восстанавливать размеры -0 
        String name = GRID.getName();
        if ("g_view".equals(name)) {//Для
            GRID.ALIAS = alias;     //алиас данных для таблицы
        }

    }

    @Override
    public void PREV_INITCOLUMN(Gridr GRID) {//Формирование массива для таблицы шапки и колонок , если несколько то по имени таблицы

        if ("g_view".equals(GRID.getName())) {//Для
            V.CAPTION = C;
            V.FIELD = FD;
            V.FSIZE = S;

        }
    }

    @Override
    public String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
        String SELE = "";
        return SELE;
    }

    @Override
    public void CHANGERECNO(Gridr GRID, int row, int rec) {//выполняется при изменении записи в гриде
    }
    
    @Override
    public void DESTROY() {
        super.DESTROY();
        V.PARAMOT = new String[1];
        V.PARAMOT[0] = "F";
    }

    @Override
    public void OPEN() {
        G[0].DATA.SELE = V.SELE;
        QUERY(G[0]);
        G[0].GORECNO(1);
        
        String[] typeArr = new String[G[0].FIELD.length + 1];
        typeArr[0] = "N";
        for (int j = 0; j < G[0].FIELD.length; j++) {
            for (int k = 0; k < G[0].DATA.COLNAMES.length; k++) {
                if (G[0].FIELD[j].equals(G[0].DATA.COLNAMES[k])) {
                    typeArr[j + 1] = G[0].DATA.COLTYPE[k];
                    break;
                }
            }
        }
        for (int i = 0; i < G[0].FIELD.length + 1; i++) {
            if (typeArr[i] != null) {
                G[0].SORTER.setComparator(i, comparator(i, typeArr[i], G[0]));
            }
        }
        
        super.OPEN();

    }

    @Override
    public void CLICK_ALL(String name) {
        if ("besc".equals(name)) {
            DESTROY();
        }
        if ("bok".equals(name) || G[0].getName().equals(name)) { //или двойной клик по гриду
            DESTROY();
            V.PARAMOT[0] = "T";
        }
        if ("B2".equals(name)) {
            for (int i = 0; i < G[0].getRowCount(); i++) {
                G[0].setValueAt(true, i, 1);
            }
            G[0].SETFOCUS();

        }
        if ("B3".equals(name)) {
            for (int i = 0; i < G[0].getRowCount(); i++) {
                G[0].setValueAt(false, i, 1);
            }
            G[0].SETFOCUS();

        }

    }
    
    private Comparator<?> comparator(int col, String type, Gridr GRID) {
        if (type.equals("N") && !GRID.getColumnClass(col).toString().equals("class java.lang.Integer")) {
            return new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    o1 = o1.replaceAll(" ", "");
                    o2 = o2.replaceAll(" ", "");
                    try {
                        Integer.parseInt(o1);
                        Integer.parseInt(o2);
                    } catch (NumberFormatException exc) {
                        return (int) (Double.parseDouble(o1) - Double.parseDouble(o2));
                    }
                    return Integer.parseInt(o1) - Integer.parseInt(o2);
                }
            };
        }
        if (type.equals("D")) {
            return new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    Date d1 = null, d2 = null;
                    try {
                        d1 = sdf.parse(o1);
                        d2 = sdf.parse(o2);
                    } catch (ParseException ex) {
                    }
                    return d1.compareTo(d2);
                }
            };
        }
        if (type.equals("T")) {
            return new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    Date d1 = null, d2 = null;
                    try {
                        d1 = sdf.parse(o1);
                        d2 = sdf.parse(o2);
                    } catch (ParseException ex) {
                    }
                    return d1.compareTo(d2);
                }
            };
        }
        return GRID.SORTER.getComparator(col);
    }
}
