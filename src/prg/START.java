/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import aplclass.ToolBarr;
import baseclass.Configs;
import baseclass.Formr;
import baseclass.Screen;
import client.Post;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Основная программа запуска
 *
 * @author Kostya
 */
public class START {

    /**
     * @param args the command line arguments
     */
    public static String result = "";
    public static JDesktopPane DESKTOP;

    public START() {
        V._SCREEN = new Screen("screen", V.TITLE, 1024, 768);
        P.WRITE_INFO(V.CONFIGS_DIRECTORY);
        V._SCREEN.setDefaultCloseOperation(Screen.EXIT_ON_CLOSE);//закрыть приложение при закрытии формы                        
        V._SCREEN.setVisible(true); //открываем
        V.TOOLBAR = new ToolBarr("mainToolBar", V._SCREEN.getWidth(), 30);
        V.FALSETRUE = false;
        V.POS_OPEN_SHOP_USERNAME = P.loadPosOpenShopUser();
        

        if (V.ON_TOMCAT) {
            P.SETTINGS_CHECK(); //проверка настройки соединения c сервисом
        }
        P.WRITE_INFO("Конфигурация соединения:" + V.fileNameConnMem);
        P.WRITE_INFO("Конфигурация грида:" + V.fileNameGridconf);
        Formr form = null;
        P.macAddress();//ПОЛУЧЕНИЕ ОСНОВНОГО IP И MAC АДРЕСА
        if (V.ON_CONN) {

            if (V.VARCONN() && "rkv".equals(V.CONN_USER) && FF.EMPTY(V.CONN_PASS)) {  //если в настройках соединения имя пользователя rkv и пароль пустой
                //  то вход по макадресу
                if ("rkv".equals(V.CONN_USER) && FF.EMPTY(V.CONN_PASS)) {//вход по мак адресу
                    if (FF.EMPTY(V.MAC)) {
                        P.MESSERR("Ошибка определения МАС адреса. Нужно изменить параметры соединения.");
                        V.FALSETRUE = false;
                        return;
                    }
                    P.ALERT("Выполняется запрос на сервер для получения прав доступа");
                    V.SHOP_ID = "37W1";
                    // V.SHOP_ID="9999";
                    V.POST = Post.getInstance();
                    V.POST.setParam("Query", "035");  //V.ART=2- МАГАЗИН    4- РЦ
                    V.PARAM = "012~" + V.MAC + "~" + FF.STR(V.ARM);
//                FF._CLIPTEXT(PARAM);
                    V.POST.setParam("P1", V.PARAM);
                    V.RES = V.POST.SendDataWithResult();

                    if (!"OK".equals(FF.SUBSTR(V.RES, 1, 2))) {//если не 
                        if ("NO".equals(FF.SUBSTR(V.RES, 1, 2))) {
                            V.RES = FF.SUBSTR(V.RES, 3);
                        }
                        FF._CLIPTEXT(V.MAC);
                        P.MESS(V.RES);
                        //    V.FALSETRUE = false;
                        //   return;
                    } else {
                        V.RES = FF.SUBSTR(V.RES, 3);
                        int pos = 0;
                        pos = FF.AT("~", V.RES);
                        V.CONN_SERVER = FF.SUBSTR(V.RES, 1, pos - 1);
                        V.RES = FF.SUBSTR(V.RES, pos + 1);
                        pos = FF.AT("~", V.RES);
                        V.CONN_BASA = FF.SUBSTR(V.RES, 1, pos - 1);
                        V.RES = FF.SUBSTR(V.RES, pos + 1);
                        pos = FF.AT("~", V.RES);
                        V.CONN_PORT = FF.SUBSTR(V.RES, 1, pos - 1);
                        V.RES = FF.SUBSTR(V.RES, pos + 1);
                        pos = FF.AT("~", V.RES);
                        V.CONN_USER = FF.SUBSTR(V.RES, 1, pos - 1);
                        V.RES = FF.SUBSTR(V.RES, pos + 1);
                        pos = FF.AT("~", V.RES);
                        V.CONN_PASS = FF.SUBSTR(V.RES, 1, pos - 1);
                        V.RES = FF.SUBSTR(V.RES, pos + 1);
                        V.RES = FF.ALLTRIM(V.RES);
                        if (!FF.EMPTY(V.RES)) {
                            V.USER_NAME = FF.ALLTRIM(V.RES);
                        }
                    }
                }
            }

            if (!FF.EMPTY(V.USER_NAME)) {
                form = P.DOFORM("conn", 0, 0);
                form.PF[0].SETVALUE(V.USER_NAME);
                form.CLICK_ALL("bok");
            } else {
                P.DOFORM("conn");
            }
            sessionInsert();
            //if (V.ARM != 4) { // если  не DIST_CENTER
            //    updateCheck(); //перенесено в shop
            //}
            if (V.ARM == 2 && V.USER_ADMIN == 2) { //Проверить сообщения если shop и не админ
//                noticeCheck();
            }

            if (!V.INFO_FLAG_ENV && V.TOOLBAR.INFO != null) {
                V.TOOLBAR.INFO.setSelected(false);
            }
//V.FALSETRUE=true;   P.DOFORM("users");

            if (!V.FALSETRUE) {
                P.SYSTEM_EXIT();
            }
        } else {                    // if (V.ON_CONN) {
            V.USER_ADMIN = 1;
        }
    }

    public START(int programm) {
        if (programm == 1) {
            V.ARM = 3;
            V._SCREEN = new Screen("screen", V.TITLE, 1024, 768);
            V._SCREEN.setDefaultCloseOperation(Screen.EXIT_ON_CLOSE);//закрыть приложение при закрытии формы                        
            V._SCREEN.setVisible(true); //открываем
            V._SCREEN.setExtendedState(V._SCREEN.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            V.FALSETRUE = false;
            P.SETTINGS_CHECK();
            Formr form = null;
            if (!FF.EMPTY(V.USER_NAME)) {
                form = P.DOFORM("conn", 0, 0);
                form.PF[0].SETVALUE(V.USER_NAME);
                form.CLICK_ALL("bok");
            } else {
                P.DOFORM("conn");
            }
            sessionInsert();
//            if (V.ARM != 4) { // если  не DIST_CENTER
            //              updateCheck();        //ПРОВЕРКА НАЛИЧИЯ ОБНОВЛЕНИЙ НА ЦЕНТРАЛЬНОЙ БАЗЕ
            //  
            System.out.println("перед  -" + V.USER_ADMIN + "-" + V.ARM);
            if (V.ARM == 3 && V.USER_ADMIN >= 0) {  // ЕСЛИ ПРОГРАММА KASSA
                System.out.println("после  -" + V.USER_ADMIN + "-" + V.ARM);

                if (V.CONN1 != null && P.programmUpdateCheck()) {
                    updateCheck();
                    updatePRG();// проверка обновлений на локальной базе
                }
            }
            if (V.ARM == 2 && V.USER_ADMIN >= 2) {  // ЕСЛИ ПРОГРАММА SHOP 
//                noticeCheck();   // ПРОВЕРКА НАЛИЧИЯ СООБЩЕНИЙ
            }
        } else if (programm == 5) {
            V._SCREEN = new Screen("screen", V.TITLE, 1024, 768);
            V._SCREEN.setDefaultCloseOperation(Screen.EXIT_ON_CLOSE);//закрыть приложение при закрытии формы                        
            V._SCREEN.setVisible(true); //открываем
            V._SCREEN.setExtendedState(V._SCREEN.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            V.FALSETRUE = false;
            P.SETTINGS_CHECK();
            Formr form = null;
            if (!FF.EMPTY(V.USER_NAME)) {
                form = P.DOFORM("conn", 0, 0);
                form.PF[0].SETVALUE(V.USER_NAME);
                form.CLICK_ALL("bok");
            } else {
                P.DOFORM("conn");
            }
//            V.CONN1 = P.SQLCONNECT("192.168.2.121", "ORCL", "belwpr", "belwprt", "1521");
        } else if (programm == 99) {
            V._SCREEN = new Screen("screen", V.TITLE, 1024, 768);
            V._SCREEN.setDefaultCloseOperation(Screen.EXIT_ON_CLOSE);//закрыть приложение при закрытии формы                        
            V._SCREEN.setVisible(true); //открываем
            V._SCREEN.setExtendedState(V._SCREEN.getExtendedState() | JFrame.MAXIMIZED_BOTH);
//            V.TOOLBAR = new ToolBarr("mainToolBar", V._SCREEN.getWidth(), 30);
            V.FALSETRUE = false;
            P.SETTINGS_CHECK();
            Formr form = null;
            if (!FF.EMPTY(V.USER_NAME)) {
                form = P.DOFORM("conn", 0, 0);
                form.PF[0].SETVALUE(V.USER_NAME);
                form.CLICK_ALL("bok");
            } else {
                P.DOFORM("conn");
            }
        } else if (programm == 66) {
            V._SCREEN = new Screen("screen", V.TITLE, 1024, 768);
            V._SCREEN.setDefaultCloseOperation(Screen.EXIT_ON_CLOSE);//закрыть приложение при закрытии формы                        
            V._SCREEN.setVisible(true); //открываем
            V._SCREEN.setExtendedState(V._SCREEN.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            V.TOOLBAR = new ToolBarr("mainToolBar", V._SCREEN.getWidth(), 30);
            V.FALSETRUE = false;
            P.SETTINGS_CHECK();
            Formr form = null;
            if (!FF.EMPTY(V.USER_NAME)) {
                form = P.DOFORM("conn", 0, 0);
                form.PF[0].SETVALUE(V.USER_NAME);
                form.CLICK_ALL("bok");
            } else {
                P.DOFORM("conn");
            }
            sessionInsert();
        } else if (programm == 77) {
            V._SCREEN = new Screen("screen", V.TITLE, 1024, 768);
            V._SCREEN.setDefaultCloseOperation(Screen.EXIT_ON_CLOSE);
            V._SCREEN.setLocationRelativeTo(null);
            V._SCREEN.setVisible(true);
            V.TOOLBAR = new ToolBarr("mainToolBar", V._SCREEN.getWidth(), 30);
            V.FALSETRUE = false;
            P.SETTINGS_CHECK();
            P.DOFORM("MainLoginForm");
            sessionInsert();
        }
//V.FALSETRUE=true;   P.DOFORM("users");

        if (!V.FALSETRUE) {
            P.SYSTEM_EXIT();
        }
    }

    public static void updateCheck() {
        String file = "UPDATER_" + V.ARM + ".config";
        Configs proper = new Configs(file);  //файл где хранится версия обновлений
        if (!proper.fConfig.exists()) {  //если такого нет то и не обновляемая
            file = "UPDATER.config";
            proper = new Configs(file);  //файл где хранится версия обновлений
            if (!proper.fConfig.exists()) {  //если такого нет то и не обновляемая
                return;
            }
        }
        //P.ALERT("Проверка на наличие и получение обновлений ПО ...  ");
        V.POST = Post.getInstance();
        V.POST.setParam("Query", "035");  //V.ARM=2- МАГАЗИН    4- РЦ
        
        try {
            V.Host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            V.Host = " ";
        }
        
        V.PARAM = "019~" + V.ARM + "~" + V.Host;
//                FF._CLIPTEXT(PARAM);
        V.POST.setParam("P1", V.PARAM);
        V.RES = V.POST.SendDataWithResult("Проверка на наличие и получение обновлений ПО ...  ");
        if (!"OK".equals(FF.SUBSTR(V.RES, 1, 2))) {//ЕСЛИ ВСЕ ОК
            P.MESS(V.RES);
            return;
        }

    }

    public static void updatePRG() {
        P.ALERT("Проверка обновлений...");
        String file = V.RUN_DIRECTORY + "UPDATER_" + V.ARM + ".config";
        Configs proper = new Configs(file);  //файл где хранится версия обновлений
        if (!proper.fConfig.exists()) {  //если такого нет то и не обновляемая
            file = V.RUN_DIRECTORY +"UPDATER.config";
            proper = new Configs(file);  //файл где хранится версия обновлений
            if (!proper.fConfig.exists()) {  //если такого нет то и не обновляемая
                P.ALERT("");
                return;
            }
        }

        // если через центр то вставить запрос на сервис для вставки обновления в локальный RI_PROJECT_STORAGE
        //*************
        String PROGID = proper.getProperty("$" + "PROGID", "0");    // считываем номер версии                
        if (!FF.NUM_ALL(PROGID)) { //ПРОВЕРКА НА ВСЕ ЦИФРЫ 
            PROGID = "0";
        }

        //V.SELE = "SELECT DATEINS,PROG_DISCR,FILEID,PROGID FROM RI_PROJECT_STORAGE WHERE FILEID=(SELECT NVL(MAX(FILEID),0) FROM RI_PROJECT_STORAGE WHERE ARM=" + P.P_SQL(V.ARM) + ") AND FILEID>" + P.P_SQL(PROGID) + " ";
        V.SELE = "SELECT DATEINS,PROG_DISCR,FILEID,PROGID FROM RI_PROJECT_STORAGE WHERE FILEID=(SELECT MAX(FILEID) FROM RI_PROJECT_STORAGE WHERE ARM=" + P.P_SQL(V.ARM) + ") AND FILEID>" + P.P_SQL(PROGID) + " ";
        P.SQLEXECT(V.SELE, "UD_UPDATE");  // ПРОВЕРКА НА НАЛИЧИЕ ОБНОВЛЕНИЙ C НОМЕРОМ FILEID БОЛЬШИМ ЧЕМ У НАС В UPDATER.config
        if (A.RECCOUNT("UD_UPDATE") == 0) {  //
            P.ALERT("Нет обновлений");  //ЕСЛИ НЕТ ТО СООБЩИТЬ И НА ВЫХОД
            return;
        }

        if ((V.ARM == 3 || V.ARM == 2) && V.USER_ADMIN < 3) {
            P.MESS("Обновление :" + A.GETVALS("UD_UPDATE.PROG_DISCR"));
        } else if (P.MESSYESNO("Обновление :" + A.GETVALS("UD_UPDATE.PROG_DISCR") + "\n    Обновить текущую программу ?") == 1) {
            return;
        }//ОБНОВЛЕНИЯ ЕСТЬ И ВОПРОС НА ОБНОВИТЬ

        P.ALERT("Получение обновлений ПО");
        V.SELE = "SELECT PROJECTFILE FROM RI_PROJECT_STORAGE WHERE FILEID=" + A.GETVALS("UD_UPDATE.FILEID");
        if (!P.BLOB_TO_FILE(V.SELE, V.RUN_DIRECTORY+ "DIST.ZIP")) { //ДОСТАЕМ БИНАРНОЕ ПОЛЕ С АРХИВОМ И ПЕРЕВОДИМ ЕГО В ФАЙЛ DIST.ZIP
            P.MESS("Ошибка получения файла ");
            return;
        }
        P.ALERT("Распаковка архива с ПО");
        if (!P.UNZIP(V.RUN_DIRECTORY + "DIST.ZIP", V.RUN_DIRECTORY,null) ){  // РАСПАКОВЫВАЕМ DIST.ZIP В ТЕКУЩИЙ КАТАЛОГ ЗАМЕНЯЯ ВСЕ ФАЙЛЫ В КАТАЛОГЕ ЗАПУСКА И ИСПОЛНЯЕМЫЕ ФАЙЛЫ 
//              P.MESS("Ошибка распаковки файла ");
            P.ALERT("");
            return;
        }
        proper.setProperty("$" + "PROGID", A.GETVALS("UD_UPDATE.FILEID")); // ЗАПИСЫВАЕМ НОВУЮ ВЕРСИЮ В UPDATER.config
        proper.saveProperties("Update Configuration");  // НАЗВАНИЕ ФАЙЛА UPDATER.config
        P.ALERT("Удаление предыдущих версий");
        V.SELE = "DELETE FROM RI_PROJECT_STORAGE WHERE ARM = " + P.P_SQL(V.ARM) + " and FILEID<" + A.GETVALS("UD_UPDATE.FILEID");
        P.SQLEXECUT(V.SELE);  //Удаление предыдущих версий
        P.MESS("Для завершения обновления запустите задачу заново.... ");
        System.exit(0);
        return;
        //       }

    }

    public static void noticeCheck() {
        if (V.CUR_PASS_OFFLINE.equals("offline_admin") || V.CUR_PASS_OFFLINE.equals("offline")) {
            return;
        }
//        P.MESS("THIS IS SPARTAAAAA!!!!!!111");
//        double count = (double) ((Object[]) P.SQLEXECT("select count(*) from ri_project_storage").rowList.get(0))[0];
//        if (count > 0) {
//            if (P.MESSYESNO("Доступно обновление. Установить?") == 0) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

//                Post.getInstance().setParam("PROGID", proper.getProperty("$" + "PROGID"));
//                        String result = Post.getInstance().SendDataWithResult();
                Thread myThready = new Thread(new Runnable() {
                    @Override
                    public void run() //Этот метод будет выполняться в побочном потоке
                    {
                        Post post = Post.getInstance();
                        post.setParam("Query", "035");
                        String PARAM = "A01";
                        post.setParam("P1", PARAM);
                        result = post.SendDataWithResult("Проверка на наличие сообщений....");
                    }
                });
                myThready.start();

                do {
                    try {
                        myThready.join(15000);    //Подождать окончания мысли четверть секунды.
                    } catch (InterruptedException e) {
                    }
                    myThready.stop();
                } while (myThready.isAlive()); //Пока brain думает...

                if (result.equals("YES")) {
                }

                int count = (int) ((double) ((Object[]) P.SQLEXECT("select count(*) from ri_notice3 where dateread is null").rowList.get(0))[0]);
                if (count > 0) {
                    P.DOFORM("RI_NOTICE1_SHOP");
                }
                P.ALERT("");

//                    P.SYSTEM_EXIT();
            }
        });
//            }
//        }
    }

    private void sessionInsert() {
        if (V.FALSETRUE) {
            if ("1".equals(V.CONN_DRIVER)) { //если sql server нужно писать 
            P.SQLUPDATE("INSERT INTO HISTORY_SESSION (USER_FIO, PROGRAMM, VER) VALUES (" + P.P_SQL(V.USER_FIO) + ", " + P.P_SQL(V.PROGRAMM) + ", " + P.P_SQL(V.NVER) + ")");
            V.SESSIONID = String.valueOf((int) ((double) ((Object[]) P.SQLEXECT("select USERENV ('sessionid') from dual").rowList.get(0)) [0]));
            }
            if ("2".equals(V.CONN_DRIVER)) { //если sql server нужно писать 
                P.SQLUPDATE("INSERT INTO HISTORY_SESSION (USER_FIO, PROGRAMM, VER) VALUES (" + P.P_SQL(V.USER_FIO) + ", " + P.P_SQL(V.PROGRAMM) + ", " + P.P_SQL(V.NVER) + ")");
                return;
            }
            if ("3".equals(V.CONN_DRIVER)) { //если postgreSQL нужно писать 
            P.SQLUPDATE("INSERT INTO HISTORY_SESSION (USER_FIO, PROGRAMM, VER) VALUES (" + P.P_SQL(V.USER_FIO) + ", " + P.P_SQL(V.PROGRAMM) + ", " + P.P_SQL(V.NVER) + ")");
            V.SESSIONID = String.valueOf((int) ( ((Object[]) P.SQLEXECT("SELECT pg_backend_pid()").rowList.get(0)) [0]));
            }
            
        }
        
    }

    public void DESTROY() {//при закрытии SCREEN
    }

}
