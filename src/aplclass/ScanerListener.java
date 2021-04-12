/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import prg.V;

/**
 *
 * @author dima Класс работы кассового сканера 14.12.2013
 *
 */
public abstract class ScanerListener implements KeyListener {

    private final static char beginCharEn = '#';
    private final static char beginCharRu = '№';
    private final static char endCharEn = '$';
    private final static char endCharRu = ';';
    private static boolean scanFlag = false; // признак того что идет считывание со сканера данных
    private static String scanString = ""; // строка данных со сканера

    //--------------типы сканкодов ---------------------------------------------
    public static final int scanEO = 0;  // единица обработки
    public static final int scanRKV = 1; // код ркв
    public static final int scanEAN = 2;  // еан код
    public static final int scanDK = 3;  // дисконтная карта
    public static final int scanPS = 4; // подарочный сертификат 
    public static final int scanKupon = 5; // купон
    public static final int scanUnknown = -1; // неизвестный     

    //**************************************************************************
    //  является ли символ началом сканирования
    // 
    //**************************************************************************    
    private boolean isBeginScan(char c) {
        if (c == beginCharEn || c == beginCharRu) {
            return true;
        }
        return false;
    }

    //**************************************************************************
    //  является ли символ концом сканирования
    // 
    //**************************************************************************    
    private boolean isEndScan(char c) {
        if (c == endCharEn || c == endCharRu) {
            return true;
        }
        return false;
    }

    //**************************************************************************
    //  идет ли сканирование в данный момент
    // 
    //**************************************************************************    
    private boolean isScanning() {
        return scanFlag;
    }

    //**************************************************************************
    //  запись символов в сканкод
    // 
    //**************************************************************************    
    private void writeScan(char c) {
        scanString = scanString + c;
    }

    //**************************************************************************
    // обработка сканкода
    // определяет вид сканкода
    // Параметр: scan - Сканкод
    //**************************************************************************
    public static int getScanCodeType(String scan) {

        if (scan.length() == 20) { // стандтартный код ЕО    
            System.out.println("стандтартный код ЕО ");
            return scanEO;
        }

        if (scan.length() == 21 || scan.length() == 22) { // код РКВ
            System.out.println("код РКВ");
            return scanRKV;
        }

        if (scan.length() == 16) { // Карта Учащегося
            System.out.println("Карта Учащегося");
            return scanDK;
        }

        if (scan.length() == 13 && (scan.startsWith("777") || scan.startsWith("775") || scan.startsWith("776")
                || scan.startsWith("220") || scan.startsWith("215") || scan.startsWith("222") || scan.startsWith("555"))) {
            System.out.println("Купон");
            return scanKupon;
        }

        if (scan.length() == 13 && !scan.substring(0, 3).equals("100")
                && !scan.substring(0, 3).equals("200") && !scan.substring(0, 5).equals("50000")
                && !scan.substring(0, 3).equals("000") && !scan.substring(0, 5).equals("99680") && !scan.substring(0, 7).equals("1911016")
                && !scan.substring(0, 2).equals("05")) { // код EAN    
            System.out.println("код EAN ");
            return scanEAN;
        }

        if (scan.length() == 13 && (scan.substring(0, 3).equals("000") || scan.substring(0, 7).equals("1911016") || scan.substring(0, 5).equals("99680")
                || scan.substring(0, 2).equals("05"))) { // Дисконтная карта
            System.out.println("Дисконтная карта");
            return scanDK;
        }

        if (scan.length() == 13 && (scan.substring(0, 3).equals("100")
                || scan.substring(0, 3).equals("200") || scan.substring(0, 5).equals("50000"))) { // Подарочный сертификат    
            System.out.println("Подарочный сертификат");
            return scanPS;
        }

        return scanUnknown; // сканкод не определен
    }

    //**************************************************************************
    //  получить отсканированную строку
    // 
    //**************************************************************************    
    public String getScanString() {
        return scanString;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (isScanning()) {
            e.consume();
            return;
        }

        keyTypedS(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();

        // System.out.println(c);
        // System.out.println(scanString);
        if (isBeginScan(c)) {
            scanString = "";
            scanFlag = true;
            return;
        }

        if (isEndScan(c)) {
            return;
        }

        if (e.getKeyCode() != 16 && isScanning()) {
            writeScan(c);
            return;
        }

        keyPressedS(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();

        if (isEndScan(c)) {
            scanFlag = false;

            if (scanString.length() == 22) {
                scanString = scanString.substring(0, 21);
            }

            if (scanString.length() == 12) {
                scanString = "0" + scanString;
            }

            scanComplete(scanString, getScanCodeType(scanString));
            return;
        }

        if (isScanning()) {
            return;
        }

        keyReleasedS(e);
    }

    public abstract void scanComplete(String scan, int scanType);

    public abstract void keyTypedS(KeyEvent e);

    public abstract void keyPressedS(KeyEvent e);

    public abstract void keyReleasedS(KeyEvent e);
}
