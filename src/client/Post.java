/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

/**
 *
 * @author DaN
 */
public class Post {

    private static Post Post;
    private String query = "";//KAA 14052018 убрал static, так как при одновременном запуске нескольких потоков не создавалась новая переменная
    ServletsConnect sconn = new ServletsConnect();
    InetAddress addr;
    public String nzap = "";

    private Post() {
    }

    public static Post getInstance() {
        //   if (Post == null) {
        V.CONN_COUNT = 0;
        Post = new Post();
        //  }
        return Post;
    }

    public void setParam(String nameParam, String value) {
        nzap = value;
        StringBuilder sq = new StringBuilder(query);
        sq.append('&');
        try {
            sq.append(URLEncoder.encode(nameParam, "utf-8"));
            sq.append('=');
            sq.append(URLEncoder.encode(value, "utf-8")); 
            // sq.append(value); // ОТМЕНИЛ КОДИРОВКУ RKV 15/05/2019
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Post.class.getName()).log(Level.SEVERE, null, ex);
        }
        query = sq.toString();
    }

    public boolean SendData() {
        try {
            addr = InetAddress.getLocalHost();
            String myShopID = V.SHOP_ID;
            if (FF.EMPTY(V.SHOP_ID)) {
                P.SQLEXECT("SELECT SHOPID from CONFIG", "ShopID");
                if (A.RECCOUNT("ShopID") == 0) {
                    P.MESSERR("Нет данных в таблице конфигурации CONFIG!");
                    return false;
                }
                A.SELECT("ShopID");
                myShopID = A.GETVALS("SHOPID");//addr.getHostAddress();
            }
            P.ALERT("Выполнение запроса данных №" + nzap);
            URLConnection con = sconn.getServletConnection("MainPostal");
            OutputStream outstream = con.getOutputStream();
            //  String outStr = "Query=" + URLEncoder.encode(mess, "utf-8") + "&IP_Address=" + URLEncoder.encode(myLanIP, "utf-8");
//            String outStr = query + "&ShopId=" + URLEncoder.encode(myLanIP, "utf-8");
            String outStr = query + "&ShopId=" + URLEncoder.encode(myShopID, "utf-8");
            outstream.write(outStr.getBytes("utf-8"));
            outstream.flush();
            query = "";

            String result;
            InputStream instr = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(instr, "utf-8");
            BufferedReader buff = new BufferedReader(isr);
            StringBuilder strBuff = new StringBuilder();
            String s;
            while ((s = buff.readLine()) != null) {
                strBuff.append(s);
            }
            result = strBuff.toString();

            P.ALERT("");
            System.out.println(result);
            P.ALERT("");
            JOptionPane.showMessageDialog(null, result, "Сообщение", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            P.MESSERR("Ошибка сервера! Проверьте подключение!");
            System.out.println(ex);
            sconn = null;
            return false;
        }
        return true;
//        P.MESS("");
    }

    public String SendDataWithResult() {
        return SendDataWithResult("Выполнение запроса данных №" + nzap);
    }

    public String SendDataWithResult(String SEND) {
        return SendDataWithResult(SEND, 0);
    }

    public String SendDataWithResult(int MESS) {
        return SendDataWithResult("Выполнение запроса данных №" + nzap, MESS);
    }

    public String SendDataWithResult(String SEND, int MESS) {
        return SendDataWithResult(SEND, MESS, V.SHOP_ID, V.CONN_SERVLET);
    }

    public String SendDataWithResult(String SEND, String SERVET) {
        return SendDataWithResult(SEND, 0, "0", SERVET);
    }

    public String SendDataWithResult(String SEND, int MESS, String SERVET) {
        return SendDataWithResult(SEND, MESS, "0", SERVET);
    }

    /**
     * Запос с возвращаемым сообщением
     *
     * @param SEND сообщение на экран при выполнении
     * @param MESS сообщение при ошибке 0
     * @param myShopID код подразделения для запроса Query. "0" - любой запрос
     * @return
     */
    public String SendDataWithResult(String SEND, int MESS, String myShopID, String SERVLET) {

        String outStr;
        try {
            V.CONN_COUNT++;
            addr = InetAddress.getLocalHost();

            if (!"0".equals(myShopID)) { //если требуется код подразделения
                if (FF.EMPTY(myShopID)) {
                    P.SQLEXECT("SELECT SHOPID from CONFIG", "ShopID");
                    if (A.RECCOUNT("ShopID") == 0) {
                        P.MESSERR("Нет данных в таблице конфигурации CONFIG!");
                        return "";
                    }
                    A.SELECT("ShopID");
                    myShopID = A.GETVALS("SHOPID");//addr.getHostAddress();
                }
                outStr = query + "&ShopId=" + URLEncoder.encode(myShopID, "utf-8");
            } else { // если запрос без кода подразделения
                outStr = query;
            }
            if (!FF.EMPTY(SEND)) {
                P.ALERT(SEND);
            }
           // System.out.println(URLEncoder.encode(query, "cp1251"));
            URLConnection con = sconn.getServletConnection(SERVLET); // соединение с сервисом
            System.out.println(URLDecoder.decode(outStr, "UTF-8"));
            OutputStream outstream = con.getOutputStream(); //выходной подок
            outstream.write(outStr.getBytes("utf-8")); // пишем строку 
            outstream.flush();
            query = "";

            String result;
            InputStream instr = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(instr, "utf-8");
            BufferedReader buff = new BufferedReader(isr);
            StringBuilder strBuff = new StringBuilder();
            String s;
            while ((s = buff.readLine()) != null) {
                strBuff.append(s);
            }
            result = strBuff.toString();

            if (result.contains("<")) {
                System.out.println(result);
            }
            if (!FF.EMPTY(SEND)) {
                P.ALERT("");
            }
            return result;
        } catch (Exception ex) {
            /*            
             if (V.CONN_COUNT < 2) {
             if (V.CONN_SERV_IP.equals("192.168.2.121")) {
             V.CONN_SERV_IP = "pos.vit.belwest.com";
             } else {
             V.CONN_SERV_IP = "192.168.2.121";
             }
             P.SAVECONNPROP(V.CONN_SERV_IP, V.CONN_SERV_PORT, V.CONN_SERV_NAME, V.CONN_PROX_NAME, V.CONN_PROX_PASS, V.CONN_PROX_IP, V.CONN_PROX_PORT, false,V.CONN_SERVLET);
             sconn = new ServletsConnect();
             return SendDataWithResult(SEND, MESS);
             }
             */
            
            //18102018 KAA
            if (V.CONN_COUNT < 2 && (V.ARM == 2 || V.ARM == 3 || V.ARM == 1)) {
                V.CONN_SERV_IP = "service-bw.tomcat.vit.belwest.com";
                V.CONN_SERV_PORT = "80";
                V.CONN_SERV_NAME = "";
                P.SAVECONNPROP(V.CONN_SERV_IP, V.CONN_SERV_PORT, V.CONN_SERV_NAME, V.CONN_PROX_NAME, V.CONN_PROX_PASS, V.CONN_PROX_IP, V.CONN_PROX_PORT, false, V.CONN_SERVLET);
                sconn = new ServletsConnect();
                return SendDataWithResult(SEND, MESS);
            }
            
            if (MESS == 0) {
                P.MESSERR("Ошибка сервера! Проверьте подключение!");
            }
            System.out.println(ex);
            sconn = null;
            return "#ERR# Ошибка:" + ex.getMessage();
        }
    }

    /**
     * Запрос на сервлет общий без магазинов
     *
     * @param SEND сообщение пи запросе
     * @param StrMasQuery массив параметров
     * @return
     */
    public String SendDataWithResult_2(String SEND, String[][] StrMasQuery) {
        try {
            addr = InetAddress.getLocalHost();

            P.ALERT(SEND);
            URLConnection con = sconn.getServletConnection("MainPostal");
            OutputStream outstream = con.getOutputStream();
            //  String outStr = "Query=" + URLEncoder.encode(mess, "utf-8") + "&IP_Address=" + URLEncoder.encode(myLanIP, "utf-8");
//            String outStr = query + "&ShopId=" + URLEncoder.encode(myLanIP, "utf-8");
            String outStr = "";
            for (String str[] : StrMasQuery) {
                outStr += str[0] + "=" + URLEncoder.encode(str[1], "utf-8") + "&";
            }
            outStr = outStr.substring(0, outStr.length() - 1);
            outstream.write(outStr.getBytes("utf-8"));
            outstream.flush();
            query = "";

            String result;
            InputStream instr = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(instr, "utf-8");
            BufferedReader buff = new BufferedReader(isr);
            StringBuilder strBuff = new StringBuilder();
            String s;
            while ((s = buff.readLine()) != null) {
                strBuff.append(s);
            }
            result = strBuff.toString();

            P.ALERT("");
            return result;

        } catch (Exception ex) {
            P.MESSERR("Ошибка сервера! Проверьте подключение!");
            System.out.println(ex);
            sconn = null;
            return "";
        }
    }
}
