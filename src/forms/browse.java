package forms;

import baseclass.Cursorr;
import baseclass.Formr;
import baseclass.Gridr;
import baseclass.Reportr;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.swing.JTextField;
import prg.A;
import prg.P;
import prg.V;

public class browse extends Formr {

    String SELE = "";
    HashMap<String, Object> map = null;

    public browse() {
        super("browse", "Просмотр данных в таблице", 240, 180); //Вызов конструктора от базового класса 
    }

    public browse(Cursorr DATA) {
        super();
    }

    public browse(String TEXT, String SELETABLE) {
        super("browse", "Просмотр таблицы", 400, 200); //Вызов конструктора от базового класса 
    }

    @Override
    public void LOAD_OBJ() {
        if (V.PARAMTO.length == 2) {//если два параметра
            if (V.PARAMTO[1] == V.SELECT) { //если второй параметр показывает что первый это select ЗАПРОС
                SELE = V.PARAMTO[0];
            }
            if (V.PARAMTO[1] == V.TABLE) {//если задана таблица
                SELE = "SELECT * FROM " + V.PARAMTO[0];
            }
            if (V.PARAMTO[1] != V.ALIAS) { //если второй параметр не показывает что первый это готовый алиас
                
            A.CLOSE("BROWSE");
            P.ALERT("Загрузка данных для "+V.PARAMTO[0]);
            Cursorr DATA = P.SQLEXECT(SELE,"BROWSE");
            P.ALERT("");
            }
        }
        G[0] = P.addobjG(this, "table", 1, 1, 400, 200);
        B[0] = P.addobjB(this, "B0", "В Excel", 100, 30, "Формирование отчета");
     F[0]=P.addobjF(this,"t1", "", 350, 30); F[0].TYPE=V.TYPE_CHAR;
     F[0].setHorizontalAlignment(JTextField.LEFT);
     F[0].SETREADONLY(true);
     F[0].SETVALUE(SELE);  
    }

    public void TABLEPROP(Gridr G) {
        G.RESTORECOL = 0;
            if (V.PARAMTO[1] == V.ALIAS) { //если второй параметр показывает что первый это готовый алиас
                G.ALIAS=V.PARAMTO[0]; 
            } else
            {
            G.ALIAS="BROWSE";                
            }
        
    }

    @Override
    public void LOC_ABOUT() {
        if (G[0].SCROLL != null) {
            G[0].SCROLL.setBounds(1, 1, this.getWidth() - 20, this.getHeight() - 80);
        }
        locate(B[0], null, V.LOC_RIGHT, 0, null, V.LOC_DOWN, V.LOC_SPACE);
        locate(F[0],null,V.LOC_LEFT,0,null,V.LOC_DOWN,0) ;  
        F[0].setSize(this.getWidth()-120, F[0].getHeight());        
        
    }

    @Override
    public void OPEN() {
        G[0].requestFocus();
    }

    public void PREV_INITCOLUMN(Gridr GRID) {
    }

    @Override
    public String PREV_QUERY(Gridr GRID) {//Формирование строки запроса
        return SELE;
    }

    @Override
    public void CLICK_ALL(String name) {
        if ("B0".equals(name)) {//ДЛЯ КНОПКИ excel
        }

        if ("browseButt".equals(name)) {//ДЛЯ КНОПКИ ОТЧЕТ
          Reportr.SQL_QUERY_TABLEINFO=" SELECT * FROM BASIC_COLUMN WHERE NAMEO="+ "'" + V.PARAMTO[0] + "'";
            map = new HashMap<>();
            map.put("Author", "Dan");
            map.put("ReportTitle", V.PARAMTO[0].toString());
            map.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new GregorianCalendar().getTime()));
            map.put("Query", Reportr.SQL_QUERY_TABLEINFO );
            Reportr.genRep("report4", map,"",false,"В отчете отсутствуют данные.");
        }

    }
}
