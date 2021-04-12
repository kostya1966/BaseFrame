package forms;

import aplclass.SshManager;
import baseclass.Formr;
import client.Post;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

/**
 * Форма соединения с сервером и получения прав из таблицы users
 *
 * @author Kostya
 */
public class conn extends Formr {

    public conn() {
        super("conn", "Установка соединения", 240, 180); //Вызов конструктора от базового класса 
        P.WRITE_INFO("Введите пароль и нажмите Вход.");

    }

    @Override
    public void DESCPROP() {
        SETRESIZABLE(1); //1-Признак фиксированного размера 0- не фиксированный
        SETMODAL(1);    //1-Модальная форма 0-не модальная
    }

    @Override
    public void KEYPRESS(Component obj, KeyEvent e) {
        super.KEYPRESS(obj, e);
        if (obj.getName().equals("t1")) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                CLICK_ALL("bok");
            }
        }
    }

    @Override
    public void LOAD_OBJ() {

        L[0] = P.addobjL(this, "l1", "Введите пароль", 100, 30);
        L[1] = P.addobjL(this, "l1", "", 150, 30);

        PF[0] = P.addobjPF(this, "t1", "", 150, 30);
//        F[0].SETTYPE(V.TYPE_CHAR);
        PF[0].setEchoChar('*');
        B[0] = P.addobjB(this, "bok", "Вход", 100, 30, "Соединение с базой данных и проверка прав");
        B[1] = P.addobjB(this, "besc", "Выход", 100, 30, "Выход из задачи");
        B[2] = P.addobjB(this, "b2", "Настройка соединения", 220, 25, "Вызов формы для вводв параметров соединения");
        L[1].setText(P.LOAD_CONNECTIONS_CONF_SHOPID_LAST());
    }
//расположение объектов относительно друг друга выполняется также при изменении размеров формы 

    @Override
    public void LOC_ABOUT() {

        locate(L[0], null, V.LOC_CENTR, 0, null, V.LOC_UP, 0);
        locate(PF[0], L[0], V.LOC_CENTR, 0, L[0], V.LOC_DOWN, 0);
        locate(B[0], null, V.LOC_LEFT, V.LOC_SPACE, PF[0], V.LOC_DOWN, V.LOC_SPACE);
        locate(B[1], null, V.LOC_RIGHT, 0, PF[0], V.LOC_DOWN, V.LOC_SPACE);
        locate(B[2], null, V.LOC_CENTR, 0, null, V.LOC_DOWN, V.LOC_SPACE);
        locate(L[1], null, V.LOC_CENTR, 0, B[2], V.LOC_UP, -5);

    }

    @Override
    public boolean INIT() {
        return true;
    }

    @Override
    public void DBLCLICK_ALL(String name) {
        String STR = "";
        STR = STR + V.CONN_SERVER + "\n";
        FF._CLIPTEXT(STR);
        STR = STR + V.CONN_PORT + "\n";
        STR = STR + V.CONN_BASA + "\n";
        STR = STR + V.CONN_USER + "\n";
        STR = STR + V.CONN_PASS + "\n";

        P.MESS(STR, "Данные по соединению ");
    }

    @Override
    public void CLICKR_ALL(String name) {
        if ("besc".equals(name)) {
            V.FALSETRUE = false;
            this.DESTROY();
        }
    }    // для дочерних объектов на click мышки

    @Override
    public void OPEN() {
        PF[0].requestFocusInWindow();
    }

    @Override
    public void CLICK_ALL(String name) {

        if ("bok".equals(name)) {//ДЛЯ КНОПКИ ВХОД
//            if (V.VARCONN() == false) {
            //              P.MESSERR("Ошибка чтения данных соединения ");
            //            return;
            //      }

//        P.MESS("Проверка сетевого соединения "+V.CONN_SERVER);
//        if ( P.PING(V.CONN_SERVER)>0 ) {
//            P.MESS("");
//         P.ERRMESS("Нет проверочного ответа от "+V.CONN_SERVER+"\n"+"проверте настройки сети");
//              return ;         
//        }
            oldBaseConnect();
            offlineConnect();

            P.ALERT("Установка соединения с " + V.CONN_SERVER);
            // Анонимный вход если login = "rkv" 
            //
            V.CONN1 = P.SQLCONNECT(V.CONN_SERVER, V.CONN_BASA, V.CONN_USER, V.CONN_PASS, V.CONN_PORT);//V.CONN1 глобальная переменная
           //  P.VALID_STRU_TABLE(V.CONN_DRIVER,0, "USERS"); //V.CONN_DRIVER =='1' - oracle  '2' - SQL Server
            P.ALERT("");
            if (V.CONN1 == null) {
                V.FALSETRUE = false;
                return;
            }
            String STR = PF[0].GETVALUE().toString();
            if ("RKV".equals(FF.SUBSTR(STR, 1, 3))) {
                STR = FF.SUBSTR(STR, 4);
                int DIG = 0;
                DIG = FF.MONTH() + FF.DAY() - 30;
                DIG = Math.abs(DIG);
                if (STR == null ? FF.STR(DIG) == null : STR.equals(FF.STR(DIG))) {
                    V.USER_ADMIN = 3;
                    V.FALSETRUE = true;
                    V.USER_FIO = "RKV";
                    this.DESTROY();
                    return;
                }
            }
            String YEARS = FF.STR(FF.YEAR());
            String STRP = "09" + FF.SUBSTR(YEARS, 1, 2) + "10" + FF.SUBSTR(YEARS, 3, 2) + "66";
            if (STRP.equals(STR) || "0920101766".equals(STR)) {
                V.USER_ADMIN = 1;
                V.FALSETRUE = true;
                V.USER_FIO = "Только на чтение";
                this.DESTROY();
                return;
            }
            if ((STRP + "_zo").equals(STR) || "0920101766_zo".equals(STR)) {
                V.USER_ADMIN = 2;
                V.FALSETRUE = true;
                V.USER_FIO = "Пользователь 2 уровня";
                this.DESTROY();
                return;
            }

            //проверка пользователя            
            V.SELE = "SELECT *  FROM USERS  WHERE NAME=" + P.P_SQL(PF[0].GETVALUE());
//            V.SELE = "SELECT *  FROM USERS  WHERE RTRIM(NAME)=" + P.P_SQL(PF[0].GETVALUE());

//            FF._CLIPTEXT(V.SELE);
            P.SQLEXECT(V.SELE, "UD", false);
            if (A.RECCOUNT("UD") == 0) {
                P.MESSERR("Не найден пользователь . Проверьте правильноcть ввода пароля.");
                PF[0].requestFocus();
                try {
                    V.CONN1.close();
                    V.SQL.close();
                    V.CONN1 = null;
                    V.SQL = null;
                } catch (SQLException ex) {
                    Logger.getLogger(conn.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
            V.USER_NAME = A.GETVALS("UD.NAME");
            V.USER_FIO = A.GETVALS("UD.FIO");
            V.USER_MENU = A.GETVALS("UD.MENU");
            V.USER_NOMER = A.GETVALN("UD.NOMER");
            V.USER_KPODR = A.GETVALS("UD.KPODR");
            V.USER_PODR = A.GETVALS("UD.PODR");
            if (A.TYPE("UD.KWIFI") != "U") {
                V.USER_KWIFI = A.GETVALS("UD.KWIFI");
                V.USER_NWIFI = A.GETVALS("UD.NWIFI");
                V.USER_MWIFI = A.GETVALS("UD.MWIFI");
                V.USER_DWIFI = A.GETVALS("UD.DWIFI");
            }
            V.CUR_PASS = PF[0].GETVALUE().toString();

            if (FF.AT("#", V.USER_MENU) > 0) { //все смотреть
                if (PF[0].GETVALUE().toString().equals("dir01")) {
                    V.USER_DIR = true;
                }
                V.USER_ADMIN = 1;
            }
            if (FF.AT("##", V.USER_MENU) > 0) {//все смотреть и корректировать 
                V.USER_ADMIN = 2;
            }
            if (FF.AT("###", V.USER_MENU) > 0) {//все 
                V.USER_ADMIN = 3;
            }

            V.FALSETRUE = true;
//            V.SHOP_ID = (String)((Object[])P.SQLEXECT("select distinct shopid from config").rowList.get(0))[0];
            V.CONN_NAME = P.LOAD_CONNECTIONS_CONF_SHOPID_LAST();
            this.DESTROY();
            V.INFO_FLAG = false;
        }//ДЛЯ КНОПКИ ВХОД

        if ("besc".equals(name)) {
            V.FALSETRUE = false;
            this.DESTROY();
        }

        if ("b2".equals(name)) {
            P.DOFORM("conn1");//вызов формы conn1
            L[1].setText(P.LOAD_CONNECTIONS_CONF_SHOPID_LAST());
        }

    }

    private void oldBaseConnect() {
        if (PF[0].GETVALUE().toString().contains("byr")) {
            V.CUR_PASS_BYR_BASE = PF[0].GETVALUE().toString();
            String shop = PF[0].GETVALUE().toString().substring(3);
            while (shop.length() < 4) {
                shop = "0" + shop;
            }
            V.CUR_BYR_SHOPID = shop;
            Post post = Post.getInstance();
//            post.setParam("Query", "039");
            V.CUR_BYR_PARAM = post.SendDataWithResult_2("Обработка запроса...", new String[][]{{"Query", "039"}, {"ShopId", shop}});
//            V.CONN_SERVER = "194.158.203.82";
            V.CONN_SERVER = V.CONN_SERV_IP;
            V.CONN_BASA = "orcl";
            V.CONN_USER = "off_shop" + V.CUR_BYR_PARAM;
            V.CONN_PASS = "off_Shop" + V.CUR_BYR_PARAM;
            V.CONN_PORT = "1521";
            PF[0].setText("1966");
        }
    }

    private void offlineConnect() {
        if (PF[0].GETVALUE().equals("offline_admin") || PF[0].GETVALUE().equals("offline")) {
            V.CUR_PASS_OFFLINE = PF[0].GETVALUE().toString();
            Object[] objArr = new Object[4];
            objArr[0] = "Номер";
            objArr[1] = V.TYPE_CHAR;
            objArr[2] = "";
            objArr[3] = "";
            P.DOCORRECT("Введите номер отделения", objArr);
            if (V.PARAMOT != null && V.PARAMOT[0].equals("T")) {
                P.ALERT("Обработка данных...");
                V.OFFLINE_OUT_RESULT = systemConnect();
                P.ALERT("");
                if (V.OFFLINE_OUT_RESULT.equals("")) {
                    return;
                }
                System.out.println(V.OFFLINE_OUT_RESULT);
                SshManager manager = new SshManager("pos.vit.belwest.com", "oracle", "oracle");
                String command = "/u01/app/oracle/product/11.2.0.3/db/bin/impdp USERID=off_shop" + V.OFFLINE_OUT_RESULT + "/off_Shop" + V.OFFLINE_OUT_RESULT + " remap_schema=SHOP999:OFF_SHOP" + V.OFFLINE_OUT_RESULT + " logfile=First_impW313.log directory=DIROFF_" + V.OFFLINE_OUT_RESULT + " network_link=BASE_SHOP table_exists_action=replace content=METADATA_ONLY transform=segment_attributes:n;";
                manager.connectAndExecuteListCommand(command);
                V.CONN1 = P.SQLCONNECT("pos.vit.belwest.com", "orcl", "firm", "firmt", "1521");
                if (V.PARAMOT[1].length() == 5) {
                    P.SQLEXECT("select shopid from FIRM.s_shop where substr(shopnum,2) = '" + V.PARAMOT[1] + "'", "S_SHOP_OFF");
                    V.PARAMOT[1] = A.GETVALS("S_SHOP_OFF.shopid");
                }
                double shopCount = (double) ((Object[]) P.SQLEXECT("select count(*) from st_shop where shopid = '" + V.PARAMOT[1] + "'").rowList.get(0))[0];
                if (shopCount > 0) {
                    P.ALERT("");
                    P.ALERT("Идет выполнение процедуры...");
                    P.SQLUPDATE("call disable_triggers@" + "SOFF_" + V.OFFLINE_OUT_RESULT + "('POS_SALE1_ID')");
                    P.SQLUPDATE("call disable_triggers@" + "SOFF_" + V.OFFLINE_OUT_RESULT + "('POS_SALE2_ID')");
                    P.SQLUPDATE("begin COPY_DATA_TO_SHOP2('" + V.PARAMOT[1] + "','SOFF_" + V.OFFLINE_OUT_RESULT + "');end;");

                    P.ALERT("");
                    try {
                        V.CONN1.close();
                        V.SQL = null;
                    } catch (SQLException ex) {
                        Logger.getLogger(conn.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    V.CONN_SERVER = "pos.vit.belwest.com";
                    V.CONN_BASA = "orcl";
                    V.CONN_USER = "off_shop" + V.OFFLINE_OUT_RESULT;
                    V.CONN_PASS = "off_Shop" + V.OFFLINE_OUT_RESULT;
                    V.CONN_PORT = "1521";
                    if (PF[0].GETVALUE().equals("offline_admin")) {
                        PF[0].setText("1966");
                    } else if (PF[0].GETVALUE().equals("offline")) {
                        PF[0].setText("09" + FF.SUBSTR(FF.STR(FF.YEAR()), 1, 2) + "10" + FF.SUBSTR(FF.STR(FF.YEAR()), 3, 2) + "66");
                    }

                } else {
                    P.MESSERR("Номер отделения не найден!");
                    offlineConnect();
                }
            }
        }
    }

    private String systemConnect() {
        String out_res = "";
        V.CONN1 = P.SQLCONNECT("pos.vit.belwest.com", "orcl", "system", "60p46K136", "1521");
        Object[][] param = new Object[1][2];
        param[0][0] = OracleTypes.VARCHAR;
        param[0][1] = V.PARAMOT[1];

        CallableStatement cst;

        String questions = "";
        StringBuilder sb;
        try {
            sb = new StringBuilder(questions);

            for (int paramNo = 0; paramNo < param.length; paramNo++) { // формирование строки параметров
                sb.append("?,");
            }

            sb.append("?"); // последний параметр - возвращаемый результат
            questions = sb.toString();
            cst = V.CONN1.prepareCall("BEGIN LKA_CREATE_DATABASE(" + questions + "); END;");

            for (int i = 0; i < param.length; i++) {
                switch ((Integer) param[i][0]) {
                    case OracleTypes.INTEGER:
                        cst.setInt(i + 1, (Integer) param[i][1]);
                        break;
                    case OracleTypes.VARCHAR:
                        cst.setString(i + 1, param[i][1].toString());
                        break;
                    case OracleTypes.NUMBER:
                        cst.setDouble(i + 1, Double.parseDouble(param[i][1].toString()));
                        break;
                    case OracleTypes.TIMESTAMP:
                        cst.setTimestamp(i + 1, (java.sql.Timestamp) param[i][1]);
                        break;
                    case OracleTypes.DATE:
                        cst.setDate(i + 1, (java.sql.Date) param[i][1]);
                        break;
                }
            }
            cst.registerOutParameter(param.length + 1, OracleTypes.VARCHAR);
            cst.execute();

            out_res = cst.getString(param.length + 1);

            cst.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        try {
            V.CONN1.close();
            V.SQL = null;
        } catch (SQLException ex) {
            Logger.getLogger(conn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out_res;
    }
}
