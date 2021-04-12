/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import client.Post;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import java.util.ArrayList;
import java.util.Arrays;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

/**
 *
 * @author dima
 */
public class FiscalPrinterSpark implements FiscalPrinter {

    private ActiveXComponent fr;
    private static final int CHAR_COUNT_IN_CHECK = 34;
    private int connect = -1;
    private boolean sessionOpen = false;
    private boolean viewError = false; // показывалась ли ошибка на экран
    public String ResultCode = "";
    public String ResultCodeDescription = "";
    private String shopNo = "";
    private String shopAdress = "";
    private String shopUNP = "";
    public static String id_dk = "";

    public FiscalPrinterSpark() {
        try {
            fr = new ActiveXComponent("AddIn.Spark617TF");
            connect();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public FiscalPrinterSpark(String shopNo, String shopAdress, String shopUNP) {
        try {
            P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'ONLINE_KASSA_RB'", "ONLINE_RB");
            V.ONLINE_KASSA_RB = A.GETVALS("ONLINE_RB.SYS_VAL").equals("T");
            System.out.println("RB1");
            fr = new ActiveXComponent("AddIn.Spark617TF");
            System.out.println("RB2");
            this.shopNo = shopNo;
            this.shopAdress = shopAdress;
            this.shopUNP = shopUNP;
            connect();

        } catch (Exception e) {
            System.out.println("RB3");
            System.err.println(e.getMessage());
        }
    }

    public String getResultCode() {
        return ResultCode; //fr.invoke("GetErrorCode2").toString();
    }

    public String getResultDescription() {
        return ResultCodeDescription; //fr.invoke("GetErrorComment2", fr.invoke("GetErrorCode2")).toString();
    }

//**************************************************************************
// Соединение с ФР
//
//**************************************************************************    
    public boolean connect() {

        int fInitDev;
        fInitDev = fr.invoke("GetDeviceInfo", 101).toInt();
        if (fInitDev != 0 && fInitDev != 1) { // если невозможно считать информацию
            cheсkError(fInitDev);
            return false;
        }

        if (fInitDev == 1) { // если устройство уже инициализировано
            return true;
        }

        if (fInitDev == 0) { // если нет
            disconnect();
        }

        int fError;
        Variant[] varComNum = {new Variant(1), new Variant(V.FP_PORT_NUM)}; // номер компорта
        fError = fr.invoke("SetDeviceOpt", varComNum).toInt();
        if (fError != 0) {
            cheсkError(fError);
            return false;
        }

        int comSpeed = 6;
        switch (V.FP_SPEED) {
            case "4800":
                comSpeed = 0;
                break;
            case "9600":
                comSpeed = 1;
                break;
            case "19200":
                comSpeed = 2;
                break;
            case "38400":
                comSpeed = 3;
                break;
            case "57600":
                comSpeed = 4;
                break;
            case "76800":
                comSpeed = 5;
                break;
            case "115200":
                comSpeed = 6;
                break;
        }

        Variant[] varComSpeed = {new Variant(101), new Variant(comSpeed)}; // скорость компорта
        fr.invoke("SetDeviceOpt", varComSpeed);

        Variant[] varTender = {new Variant(2), new Variant(0)}; // включить кэширование данных об оплате
        fr.invoke("SetDeviceOpt", varTender);

        Variant[] varNal1 = {new Variant(31), new Variant(0)}; // отключить печать нулевых налогов в сменных отчетах
        fr.invoke("SetDeviceOpt", varNal1);

        Variant[] varNal2 = {new Variant(32), new Variant(0)}; // отключить печать нулевых налогов в сменных отчетах
        fr.invoke("SetDeviceOpt", varNal2);

        Variant[] varNal3 = {new Variant(33), new Variant(0)}; // отключить печать нулевых налогов в сменных отчетах
        fr.invoke("SetDeviceOpt", varNal3);

        Variant[] varNal4 = {new Variant(34), new Variant(0)}; // отключить печать нулевых налогов в сменных отчетах
        fr.invoke("SetDeviceOpt", varNal4);

        fr.invoke("SetAccessKey", V.FP_PASS); // пароль доступа 
//        fr.invoke("SetAccessKey", "111111"); // пароль доступа для БЕЛГИС
        fr.invoke("SetClerk", 1);

        System.out.println("init do " + fr.invoke("GetDeviceInfo", 101).toString());
        connect = fr.invoke("InitDevice").getInt(); // СОЕДИНЕНИЕ С ФР
        System.out.println("init posle " + fr.invoke("GetDeviceInfo", 101).toString());
        if (connect == 0) {
            if (V.ONLINE_KASSA_RB) {
                Variant[] var7 = {new Variant(24), new Variant(0)};
                fError = fr.invoke("SetDeviceOpt", var7).toInt();
                if (fError != 0) {
                    cheсkError(fError);
                }

                Variant[] var8 = {new Variant(22), new Variant(0)};
                fError = fr.invoke("SetDriverOpt", var8).toInt();
                if (fError != 0) {
                    cheсkError(fError);
                }
            }
        } else {
            cheсkError(connect);
            return false;
        }

        if (fr.invoke("GetDeviceInfo", 14).toInt() == 1) {
            P.MESS("24 часа закончены! Требуется снять Z-отчет!", "Внимание");
        };
        return true;

    }

    public void disconnect() {
        connect = fr.invoke("DeinitDevice").getInt();
        System.out.println("disconnect: " + connect);
        connect = -1;
    }

    public int connected() {
        int fError = fr.invoke("GetDeviceInfo", 101).toInt();
        if (fError != 0 && fError != 1) { // если невозможно считать информацию
            cheсkError(fError);
            return -1;
        }
        return connect;
    }

// ************************************************************************
// печать X отчета
//
//*************************************************************************
    public boolean getXReport() {
        int fError;
        if (!sessionOpen) {
            if (!startSession()) {
                return false;
            }
        }

        fError = fr.invoke("PrintReport", 1).getInt();
        if (fError == 0) {
            return true;
        } else {
            cheсkError(fError);
            return false;
        }
    }

// ************************************************************************
// печать Z отчета
//
//*************************************************************************
    public boolean getZReport() {
        int fError;
//        if (!sessionOpen) {
//            startSession();
//        }
        //отмена печати контрольной ленты
        if (V.ONLINE_KASSA_RB) {
            Variant[] var7 = {new Variant(24), new Variant(0)};
            fError = fr.invoke("SetDeviceOpt", var7).toInt();
            if (fError != 0) {
                cheсkError(fError);
            }

            Variant[] var8 = {new Variant(22), new Variant(0)};
            fError = fr.invoke("SetDriverOpt", var8).toInt();
            if (fError != 0) {
                cheсkError(fError);
            }
        }
        return endSession();
//        fError = fr.invoke("PrintReport", 3).getInt();
//        if (fError == 0) {
//            return true;
//        } else {
//            cheсkError(fError);
//            return false;
//        }
    }

// ************************************************************************
// печать буфера контрольной ленты
//
//*************************************************************************
    public boolean getKReport() {
        int fError;

        fError = fr.invoke("PrintReport", 12).getInt();
        if (fError == 0) {
            return true;
        } else {
            cheсkError(fError);
            return false;
        }
    }

// ************************************************************************
// открытие сессии 
// из инстр-ии:
// Для выполнения операций регистрации продаж, аннулирований продаж, возвратов товара, внесений в кассу,
// выплат из кассы, печати промежуточных отчетов, текстовых документов и прочих действий, 
// сопровождающих процесс розничной торговли, необходимо предварительно открыть устройство с помощью функции StartSession.
//*************************************************************************
    public boolean startSession() {
        int fError;

        //if (connect!=0){ //если нет соединения, пробуем подключится
//        if (!connect()) {
//            return false;
//        }
        //}
//        if (fr.invoke("GetDeviceInfo", new Variant(4)).toInt() == 1) {
//            sessionOpen = true;
//            return true;
//        }
        if (V.ONLINE_KASSA_RB) {//проверка на соответствие даты фискальника с реальной датой
            if (fr.invoke("GetDeviceInfo", new Variant(4)).toInt() != 1) {
                if (!timeCheck()) {
                    return false;
                }
            }
        }

        Variant[] var = {new Variant(V.KASSIR_FIO), new Variant(V.KASSA_ID)};

        fError = fr.invoke("StartSession", var).getInt();
        if (fError == 0) {
            sessionOpen = true;
            return true;
        } else {
            cheсkError(fError, "StartSession");
            return false;
        }
    }

// ************************************************************************
// закрытие сессии 
// из инстр-ии:
// Завершает отчетную сессию функция EndSession, которая переводит 
// устройство в неактивное состояние со снятием сменного Z-отчета
//*************************************************************************
    public boolean endSession() {
        int fError;

        fError = fr.invoke("EndSession").getInt();
        if (fError == 0) {
            sessionOpen = false;
//            disconnect();
            return true;
        } else {
            cheсkError(fError, "EndSession");
            return false;
        }

    }

// ************************************************************************
// старт документа
//
//*************************************************************************
    private boolean startDocument(int dType, String sClerkName) {
        int fError;
        if (!sessionOpen) {
            if (!startSession()) {
                return false;
            }
        }
        Variant[] var = {new Variant(dType), new Variant(1), new Variant(1), new Variant(sClerkName)};

        fError = fr.invoke("StartDocument", var).getInt();
        if (fError == 0) {
            return true;
        } else {
            cheсkError(fError, "StartDocument");
            return false;
        }
    }

// ************************************************************************
// старт документа
//
//*************************************************************************
    private boolean startDocument(int dType, String order, String sClerkName) {
        int fError;
        if (!sessionOpen) {
            if (!startSession()) {
                return false;
            }
        }

        Variant[] varO = {new Variant(0), new Variant(0), new Variant(0), new Variant("")};
        fr.invoke("SetExtraDocData", varO);
        
        if (!"".equals(id_dk) && dType == 1) {
            P.SQLEXECT("select case when to_number(to_char(to_date((select nvl(max(sys_val),'31.12.9999') sys_val from ri_system where sys_var = 'DK_START_SCORES_DATE'),'dd.MM.yyyy'),'yyyyMMdd')) <= to_number(to_char(sysdate,'yyyyMMdd')) \n"
                    + "then 'T' else 'F' end START_SCORES from dual ", "DK_START");
            if (A.GETVALS("DK_START.START_SCORES").equals("T")) {
                int garant = 0;
                P.SQLEXECT("select nvl(get_dk_scores_garan('" + id_dk + "'),30) - 30 DAYCOUNT from dual", "GARANT");
                garant = (int) Double.parseDouble(A.GETVALS("GARANT.DAYCOUNT"));
                if (garant > 0) {
                    Variant[] varO2 = {new Variant(0), new Variant(0), new Variant(0), new Variant(garant + " дней (Обувь)")};
                    fr.invoke("SetExtraDocData", varO2);
                }
            }
        }

//        P.SQLEXECT("select a.scan,b.procent uc,b.vid_brak/*||' '||b.brak_name*/ brak_name\n"
//                + "from d_kassa_cur c\n"
//                + "inner join s_label a on a.scan = nvl(c.scan,' ')\n"
//                + "inner join pos_brak b on a.scan = b.scan and b.procent > 0\n"
//                + "where c.kass_id = '" + P.getUniqueKassNo() + "'", "RB_BRAK_CHEK");
//        if (A.RECCOUNT("RB_BRAK_CHEK") > 0) {
//            Variant[] varO3 = {new Variant(0), new Variant(0), new Variant(0), new Variant(A.GETVALS("RB_BRAK_CHEK.brak_name"))};
//            fr.invoke("SetExtraDocData", varO3);
//        }

        Variant[] var = {new Variant(dType), new Variant(1), new Variant(order), new Variant(sClerkName)};

        fError = fr.invoke("StartDocument", var).getInt();
        if (fError == 0) {
            return true;
        } else {
            cheсkError(fError, "StartDocument");
            return false;
        }
    }

// ************************************************************************
// закрыть документ
//
//*************************************************************************
    private boolean endDocument() {
        int fError;

        fError = fr.invoke("EndDocument").getInt();
        if (fError == 0) {
            return true;
        } else {
            cheсkError(fError, "EndDocument");
            return false;
        }
    }

// ************************************************************************
// внесение денег
//
//*************************************************************************
    public boolean cashIncome(double sum) {
        int fError;

        if (!startDocument(4, V.KASSIR_FIO)) {
            return false;
        }

        Variant[] var = {new Variant(sum), new Variant(8), new Variant(0), new Variant(0)};
        fr.invoke("Tender2", var);

        endDocument();

        return true;
    }

// ************************************************************************
// изъятие денег 
//
//*************************************************************************
    public boolean cashOutcome(double sum) {
        int fError;

        if (getCashReg() < sum) {
            P.MESSERR("Недостаточно денег на Фискальном регистраторе. Остаток " + getCashReg() + " руб.");
            return false;
        }

        if (!startDocument(5, V.KASSIR_FIO)) {
            return false;
        }

        Variant[] var = {new Variant(sum), new Variant(8), new Variant(0), new Variant(0)};
        fr.invoke("Tender2", var);

        endDocument();

        return true;
    }

//**************************************************************************
// Проверка на ошибку 
// 
//**************************************************************************    
    private void cheсkError(int errCode) {
        ResultCode = String.valueOf(errCode);
        ResultCodeDescription = fr.invoke("GetErrorComment", errCode).toString();
        System.err.println(errCode + " : " + fr.invoke("GetErrorComment", errCode));
        P.MESSERR(errCode + " : " + fr.invoke("GetErrorComment", errCode));
    }

//**************************************************************************
// Проверка на ошибку 
// 
//**************************************************************************    
    private void cheсkError(int errCode, String func) {
        ResultCode = String.valueOf(errCode);
        ResultCodeDescription = fr.invoke("GetErrorComment", errCode).toString();
        System.err.println(func + " " + errCode + " : " + fr.invoke("GetErrorComment", errCode));
        P.MESSERR(errCode + " : " + fr.invoke("GetErrorComment", errCode));
    }

//**************************************************************************
// Чек на продажу/возврат
//
//**************************************************************************
    public boolean printChek(int check_id, ArrayList<ChekPosition> chekPos, boolean isVozvr, double price_cash,
            double price_card, double price_sert, double price_disc, String dk_inf, String seler, double price_cred, String trigger) {

        int fError;
        double discSumm = 0;

//        String addr = "";
//        if (shopAdress.length() <= 35) {
//            while (addr.length() != (18 - shopAdress.length() / 2)) {
//                addr += " ";
//            }
//            addr += shopAdress;
//        } else {
//            addr = shopAdress;
//        }
        id_dk = "";
        if (dk_inf != null && !dk_inf.equals("")) {
            id_dk = dk_inf;
        }

        if (isVozvr) {
//            if (price_cash > getCashReg()) {
//                P.MESSERR("Не хватает наличности в кассе для возврата!");
//                return false;
//            }
            if (!startDocument(2, String.valueOf(check_id), V.KASSIR_FIO)) {
                return false;
            }
        } else {
            if (!startDocument(1, String.valueOf(check_id), V.KASSIR_FIO)) {
                return false;
            }
        }

//        fr.invoke("TextLine", "            СООО 'БЕЛВЕСТ'");
//        fr.invoke("TextLine", "           Магазин №" + shopNo);
//        fr.invoke("TextLine", addr);
        // установка номера ДК, если есть
        if (dk_inf != null && !dk_inf.equals("")) {
            fr.invoke("AddExtraString", "Дисконтная карта: " + dk_inf);
        }

        // позиции
        for (ChekPosition cp : chekPos) {
            Variant[] var = {new Variant(cp.quantity * 1000), new Variant(cp.price), new Variant(cp.productName), new Variant(0)};  //cp.price
            fr.invoke("Item", var);
            discSumm = cp.disc;
            if (cp.disc != 0) {  // если скидка

                Variant[] varO = {new Variant(36), new Variant(0)}; // отключить тонкую линию
                fr.invoke("SetDeviceOpt", varO);

                Variant[] varCorr = {new Variant(discSumm * -1), new Variant(""),};
                fError = fr.invoke("AbsoluteCorrectionText", varCorr).toInt();
                if (fError != 0) {
                    cheсkError(fError, "AbsoluteCorrectionText");
                    fr.invoke("CancelDocument");
                    return false;
                }

                Variant[] varF = {new Variant(36), new Variant(1)}; // включить тонкую линию
                fr.invoke("SetDeviceOpt", varF);

            }
        }

//        if (discSumm != 0 ) {  // если скидка
//            Variant[] varCorr = {new Variant(discSumm*-1), new Variant(""), };  
//           fError = fr.invoke("AbsoluteCorrectionText", varCorr).toInt();
//           if (fError != 0) {
//              cheсkError(fError, "AbsoluteCorrectionText");
//              fr.invoke("CancelDocument");
//              return false;
//          }                
//       }       
        // платёж
        if (price_cash != 0) {
            System.out.println("Oplata nal: " + price_cash);
            Variant[] varTend = {new Variant(price_cash), new Variant(8), new Variant(""), new Variant("")};  //price_cash
            fError = fr.invoke("tender2", varTend).toInt();
            if (fError != 0) {
                cheсkError(fError, "price_cash");
                fr.invoke("CancelDocument");
                return false;
            }
        }
        if (price_card != 0) {
            System.out.println("Oplata card: " + price_card);
            Variant[] varTend = {new Variant(price_card), new Variant(1), new Variant(""), new Variant("")};
            fError = fr.invoke("tender2", varTend).toInt();
            if (fError != 0) {
                cheсkError(fError, "price_card");
                fr.invoke("CancelDocument");
                return false;
            }
        }
        if (price_sert != 0) {
            System.out.println("Oplata sert: " + price_sert);
            Variant[] varTend = {new Variant(price_sert), new Variant(2), new Variant(""), new Variant("")};
            fError = fr.invoke("tender2", varTend).toInt();
            if (fError != 0) {
                cheсkError(fError, "price_sert");
                fr.invoke("CancelDocument");
                return false;
            }
        }
        if (price_cred != 0) {
            System.out.println("Oplata cred: " + price_cred);
            Variant[] varTend = {new Variant(price_cred), new Variant(7), new Variant(""), new Variant("")};
            fError = fr.invoke("tender2", varTend).toInt();
            if (fError != 0) {
                cheсkError(fError, "price_cred");
                fr.invoke("CancelDocument");
                return false;
            }
        }

//        fr.invoke("TextLine", "         СПАСИБО ЗА ПОКУПКУ!");
        if (!endDocument()) {
            fError = fr.invoke("CancelDocument").toInt();
            if (fError != 0) {
                cheсkError(fError, "CancelDocument");
            }
            return false;
        }

        return true;
    }

    //**************************************************************************
    // записывает в таблицы фр необходимые для работы кассы значения
    //
    //**************************************************************************
    public boolean writeFRTable() {
        int fError;

        if (connect != 0) { // если нет соединения, то выходим
            return true;
        }

        if (fr.invoke("GetDeviceInfo", new Variant(4)).toInt() == 1) {
            return true; // если смена открыта - выходим
        }
//        Variant[] var1 = {new Variant(1), new Variant(""), new Variant(0)};
//        Variant[] var1 = {new Variant(1), new Variant("СООО 'БЕЛВЕСТ'"), new Variant(0)};
//        fError = fr.invoke("SetOrderHeader", var1).toInt();
//        if (fError != 0) {
//            cheсkError(fError);
//        }
//
////        Variant[] var2 = {new Variant(2), new Variant(""), new Variant(0)};
//        P.SQLEXECT("select cityname,tel_number from s_shop a inner join config b on a.shopid = b.shopid", "SHOPCHEKC");
//        Variant[] var2 = {new Variant(2), new Variant("Магазин №" + shopNo + ", " + A.GETVALS("SHOPCHEKC.CITYNAME")+ ", "+A.GETVALS("SHOPCHEKC.tel_number")), new Variant(0)};
//        fError = fr.invoke("SetOrderHeader", var2).toInt();
//        if (fError != 0) {
//            cheсkError(fError);
//        }
//
////        Variant[] var3 = {new Variant(3), new Variant(""), new Variant(0)};
//        Variant[] var3 = {new Variant(3), new Variant(shopAdress), new Variant(0)};
//        fError = fr.invoke("SetOrderHeader", var3).toInt();
//        if (fError != 0) {
//            cheсkError(fError);
//        }
//
////        Variant[] var4 = {new Variant(4), new Variant(""), new Variant(0)};
//        Variant[] var4 = {new Variant(4), new Variant("Гарантия на обувь 30, на сумки 50 дней"), new Variant(0)};
//        fError = fr.invoke("SetOrderHeader", var4).toInt();
//        if (fError != 0) {
//            cheсkError(fError);
//        }

        Variant[] var5 = {new Variant(42), new Variant("Чек №")};
        fError = fr.invoke("SetDescriptorText", var5).toInt();
        if (fError != 0) {
            cheсkError(fError);
        }

        Variant[] var6 = {new Variant(80), new Variant("Доп гарантия")};
        fError = fr.invoke("SetDescriptorText", var6).toInt();
        if (fError != 0) {
            cheсkError(fError);
        }

        return false;
    }

    @Override
    public double getCashReg() {
        return fr.invoke("GetDoubleDeviceInfo", new Variant(308)).toDouble();
    }

    @Override
    public void closeConnect() {
        disconnect();
    }

    @Override
    public int getSaleCheckCount() {
        if (fr.invoke("GetDeviceInfo", 4).toInt() == 1) {
            return fr.invoke("GetDeviceInfo", new Variant(26)).toInt();
        } else {
            startSession();
            return fr.invoke("GetDeviceInfo", new Variant(26)).toInt();
        }
    }

    @Override
    public int getReturnCheckCount() {
        if (fr.invoke("GetDeviceInfo", 4).toInt() == 1) {
            return fr.invoke("GetDeviceInfo", new Variant(26)).toInt();
        } else {
            startSession();
            return fr.invoke("GetDeviceInfo", new Variant(26)).toInt();
        }
    }

    @Override
    public int getStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getStatusDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getAdvStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAdvStatusDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean continuePrint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //**************************************************************************
    // печать буфера контрольной ленты
    //
    //**************************************************************************    
    private void EKLZPrintReportByNum() {
        fr.invoke("EKLZPrintReportByNum", new Variant(102));

    }

    //**************************************************************************
    // вывод на экран буфера контрольной ленты
    //
    //**************************************************************************    
    public String EKLZViewReportByNum() {
        ResultCodeDescription = "";
        if (fr != null && fr.invoke("GetDeviceInfo", new Variant(4)).toInt() != 1) {
            return "Смена не открыта";
        }

        //КоличествоЧековЗаСменуНеФискальных = GetDeviceInfo(25); 
        //КоличествоЧековЗаСменуФискальных = GetDeviceInfo(24);
        //ВсегоЧеков = GetDeviceInfo(26);          
        //Prm1 = ВсегоЧеков – (КоличествоЧековЗаСменуНеФискальных+ КоличествоЧековЗаСменуФискальных);                           
        int ckeckCountNF = fr.invoke("GetDeviceInfo", new Variant(25)).toInt();
        int chekCountF = fr.invoke("GetDeviceInfo", new Variant(24)).toInt();
        int chekCount = fr.invoke("GetDeviceInfo", new Variant(26)).toInt();
        int prm1 = chekCount - (ckeckCountNF + chekCountF);
        int n = 1;
        String str = "";
        String result = "";
        System.out.println("1  " + fr.invoke("GetDeviceInfo", new Variant(25)));
        System.out.println("2   " + fr.invoke("GetDeviceInfo", new Variant(24)));
        System.out.println("3   " + fr.invoke("GetDeviceInfo", new Variant(26)));
        System.out.println("4   " + prm1);
        //return "вот оно";
        while (prm1 <= chekCount || chekCount == -1) {
            Variant[] var = {new Variant(113), new Variant(prm1), new Variant(0), new Variant(0)};
            int fError = fr.invoke("EKLZPrintReportByNum", var).toInt();

            if (fError != 0) {
                //cheсkError(fError, "EKLZPrintReportByNum");
                //return ResultCodeDescription;
                prm1++;
                if (chekCount == -1) {
                    cheсkError(fError, "EKLZPrintReportByNum");
                    return ResultCodeDescription;
                }
                continue;
            }

            n = 1;
            str = fr.invoke("GetTextDeviceInfo", new Variant(-n)).toString();
            while (!str.trim().equals("")) {
                str = fr.invoke("GetTextDeviceInfo", new Variant(-n)).toString();
                result = result + str + "\n";
                System.out.println(str);
                n++;
            }
            prm1++;
        }

        if (result.equals("")) {
            return ResultCodeDescription;
        }

        return result;
    }

    //**************************************************************************
    // сменный отчет по кассирам
    //
    //**************************************************************************    
    public boolean cashierReport() {
        P.SQLEXECT("Select * from cashier_report where kassa_id = '" + V.KASSA_ID + "' order by kassir_fio, type", "cashierReport");
        A.SELECT("cashierReport");
        if (A.RECCOUNT("cashierReport") == 0) {
            P.MESS("Отсутсвуют данные");
            return true;
        }

        if (!startDocument(3, V.KASSIR_FIO)) {
            return false;
        }

        fr.invoke("TextLine", "Отчет по кассирам по кассе №" + V.KASSA_ID);
        fr.invoke("TextLine", "За дату: " + FF.DATES());

        String kassir_fio = "";
        for (int i = 1; i <= A.RECCOUNT("cashierReport"); i++) {
            A.GOTO(i);
            if (!kassir_fio.equals(A.GETVALS("kassir_fio"))) {
                kassir_fio = A.GETVALS("kassir_fio");

                int len = 0;
                while (true) {
                    if (("Кассир: " + kassir_fio).length() <= len + 39) {
                        fr.invoke("TextLine", " " + ("Кассир: " + kassir_fio).substring(len));
                        break;
                    } else {
                        fr.invoke("TextLine", " " + ("Кассир: " + kassir_fio).substring(len, len + 39));
                    }
                    len = len + 39;
                }

                //fr.invoke("TextLine", "Кассир: "+ kassir_fio);        
            }

            switch (Integer.valueOf(A.GETVALS("type"))) {
                case 1:
                    fr.invoke("TextLine", "  Продажа количество: " + A.GETVALS("kol"));
                    fr.invoke("TextLine", "  Продажа сумма: " + A.GETVALS("sum"));
                    break;
                case 2:
                    fr.invoke("TextLine", "  Возврат количество: " + A.GETVALS("kol"));
                    fr.invoke("TextLine", "  Возврат сумма: " + A.GETVALS("sum"));
                    break;
                case 3:
                    fr.invoke("TextLine", "  Внесение количество: " + A.GETVALS("kol"));
                    fr.invoke("TextLine", "  Внесение сумма: " + A.GETVALS("sum"));
                    break;
                case 4:
                    fr.invoke("TextLine", "  Изъятие количество: " + A.GETVALS("kol"));
                    fr.invoke("TextLine", "  Изъятие сумма: " + A.GETVALS("sum"));
                    break;

            }
        }

        if (!endDocument()) {
            return false;
        }

        A.CLOSE("cashierReport");

        return true;
    }

    //**************************************************************************
    // сменный отчет по товарам
    //
    //**************************************************************************    
    public boolean productReport() {
        P.SQLEXECT("Select * from product_report where kassa_id = '" + V.KASSA_ID + "' order by  type, art", "productReport");
        A.SELECT("productReport");
        if (A.RECCOUNT("productReport") == 0) {
            P.MESS("Отсутсвуют данные");
            return true;
        }

        if (!startDocument(3, V.KASSIR_FIO)) {
            return false;
        }

        fr.invoke("TextLine", "Отчет по товарам по кассе №" + V.KASSA_ID);
        fr.invoke("TextLine", "За дату: " + FF.DATES());

        String typeDoc = "";
        for (int i = 1; i <= A.RECCOUNT("productReport"); i++) {
            A.GOTO(i);
            if (!typeDoc.equals(A.GETVALS("type"))) {
                typeDoc = A.GETVALS("type");
                if (typeDoc.equals("1")) {
                    fr.invoke("TextLine", " ");
                    fr.invoke("TextLine", "Продажа: ");
                }
                if (typeDoc.equals("2")) {
                    fr.invoke("TextLine", " ");
                    fr.invoke("TextLine", "Возврат: ");
                }
            }

            //if (A.GETVALS("art").length()>40){
            int len = 0;
            while (true) {
                if (A.GETVALS("art").length() <= len + 39) {
                    fr.invoke("TextLine", " " + A.GETVALS("art").substring(len));
                    break;
                } else {
                    fr.invoke("TextLine", " " + A.GETVALS("art").substring(len, len + 39));
                }
                len = len + 39;
            }

            //}
            //fr.invoke("TextLine", " "+A.GETVALS("art").substring(0,39));
            fr.invoke("TextLine", " Кол. " + A.GETVALS("kol") + "  Cумма " + A.GETVALS("cena3"));
        }
        if (!endDocument()) {
            return false;
        }

        A.CLOSE("productReport");
        return true;
    }

    //**************************************************************************
    // печать чека по банковскому терминалу
    //
    //**************************************************************************    
    public boolean printBankCheck(String checkText) {
//        int fError;
//        ArrayList<String> arrS = new ArrayList<>();
//        String str[] = new String[]{"СООО 'БЕЛВЕСТ'", "Магазин №" + shopNo, shopAdress};
//        Variant[] var = new Variant[3];
//
//        for (int i = 0; i < 3; i++) {
//            var[0] = new Variant(i + 1);
//            var[1] = new Variant(str[i]);
//            var[2] = new Variant(0);
//            fError = fr.invoke("SetOrderHeader", var).toInt();
//            if (fError != 0) {
//                cheсkError(fError, "SetOrderHeader");
//            }
//            arrS.add(String.valueOf(fr.invoke("GetTextDeviceInfo", i + 31)));
//        }

        ArrayList<String> arrStr = new ArrayList<>(Arrays.asList(checkText.split("\n")));

        if (!startDocument(3, V.KASSIR_FIO)) {
            return false;
        }

        for (int i = 0; i < arrStr.size(); i++) {
            int len = 0;
            while (true) {
                if (arrStr.get(i).length() <= len + 39) {
                    fr.invoke("TextLine", " " + arrStr.get(i).substring(len));
                    break;
                } else {
                    fr.invoke("TextLine", " " + arrStr.get(i).substring(len, len + 39));
                }
                len = len + 39;
            }
        }

        return endDocument();
    }

    @Override
    public boolean printChekText(int check_id, ArrayList<ChekPosition> chekPos, boolean isVozvr, double price_cash, double price_card, double price_sert, double price_disc, String dk_inf, String seler, double price_cred) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean printText(String checkText) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean annulCheck(String tip, String id_chek, String fr_id_chek, double chekSum) {

        int fError;

        if (!startDocument(6, fr_id_chek, V.KASSIR_FIO)) {
            return false;
        }

        Variant[] var = {new Variant(fr_id_chek), new Variant(100 * chekSum), new Variant("item1"), new Variant(0), new Variant(-1)};  //cp.price
        fr.invoke("ItemEx", var);

        if (tip.equals("S")) {
            P.SQLEXECT("select * from pos_sale3 where sum > 0 and id_chek = " + id_chek, "ANNUL_CHEK");
        } else {
            P.SQLEXECT("select * from d_rasxod3 where sum > 0 and id = " + id_chek, "ANNUL_CHEK");
        }
        int cash = 0;
        for (int i = 0; i < A.RECCOUNT("ANNUL_CHEK"); i++) {
            switch (A.GETVALS("ANNUL_CHEK.VID_OP_ID")) {
                case "1":
                    cash = 8;
                    break;
                case "2":
                    cash = 1;
                    break;
                case "3":
                    cash = 2;
                    break;
                case "4":
                    cash = 7;
                    break;
            }
            Variant[] varTend = {new Variant(100 * Double.parseDouble(A.GETVALS("ANNUL_CHEK.SUM"))), new Variant(cash), new Variant(""), new Variant("")};  //price_cash
            fError = fr.invoke("tender2", varTend).toInt();
            if (fError != 0) {
                cheсkError(fError, "price_cash_annul");
                fr.invoke("CancelDocument");
                return false;
            }
            A.GOTO("ANNUL_CHEK", i + 2);
        }

//        fr.invoke("TextLine", "         СПАСИБО ЗА ПОКУПКУ!");
        if (!endDocument()) {
            fError = fr.invoke("CancelDocument").toInt();
            if (fError != 0) {
                cheсkError(fError, "CancelDocument");
            }
            return false;
        }

        return true;
    }

    private boolean timeCheck() {
        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'CANCEL_DATE_CHECK'", "DATE_CHECK");
        if (A.GETVALS("DATE_CHECK.sys_val").equals("T")) {
            return true;
        }

        int fError;
        P.ALERT("Выполняется запрос на сервер для проверки соответствия даты ...  ");
        Post post = Post.getInstance();
        post.setParam("Query", "035"); // общий вызов сервисной команды
        String PARAM = "A09~";
        post.setParam("P1", PARAM);
        String res = post.SendDataWithResult();
        if (res.contains("OK")) {
            P.SQLEXECT("select to_char(to_date(serv_date,'dd.MM.yyyy HH24:mi:ss'),'ddMMyy HH24miss') serv_date,to_char(to_date(serv_date,'dd.MM.yyyy HH24:mi:ss'),'dd.MM.yy HH24:mi:ss') serv_date_s from config", "CONFSDATE");
        } else {
//            P.SQLEXECT("select to_char(sysdate,'ddMMyy HH24miss') serv_date from dual", "CONFSDATE");
            P.MESSERR("Не удалось получить время с сервера!");
            return false;
        }
        String time = String.valueOf(fr.invoke("GetDeviceInfo", new Variant(30)).toInt());
        String date = String.valueOf(fr.invoke("GetDeviceInfo", new Variant(31)).toInt());
        if (time.equals("-1") || date.equals("-1")) {
            time = "000000";
            date = "000000";
        } else {
            time = ("000000" + time).substring(time.length());
            date = ("000000" + date).substring(date.length());
        }
        if (!A.GETVALS("CONFSDATE.serv_date").equals(date + " " + time)) {
            if (P.MESSYESNO("Дата и время в фискальном регистраторе отличаются от даты и времени на сервере!\n"
                    + "Дата и время ФР -           " + (date.substring(0, 2) + "." + date.substring(2, 4) + "." + date.substring(4, 6) + " " + time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6))
                    + "\nДата и время сервера - " + A.GETVALS("CONFSDATE.serv_date_s")
                    + "\nЗаписать в настройки ФР дату и время с сервера?") != 0) {
                return false;
            }
            date = A.GETVALS("CONFSDATE.serv_date").substring(0, 6);
            time = A.GETVALS("CONFSDATE.serv_date").substring(7);
            Variant[] var1 = {new Variant(Integer.parseInt(date.substring(0, 2))), new Variant(Integer.parseInt(date.substring(2, 4))), new Variant(Integer.parseInt("20" + date.substring(4)))};//010117
            fError = fr.invoke("SetDate", var1).getInt();
            if (fError == 0) {
            } else {
                cheсkError(fError, "SetDate");
                P.MESSERR("Не удалось установить дату!");
                return false;
            }

            Variant[] var2 = {new Variant(Integer.parseInt(time.substring(0, 2))), new Variant(Integer.parseInt(time.substring(2, 4))), new Variant(Integer.parseInt(time.substring(4)))};//010117
            fError = fr.invoke("SetTime", var2).getInt();
            if (fError == 0) {
            } else {
                cheсkError(fError, "SetTime");
                P.MESSERR("Не удалось установить время!");
                return false;
            }

//            time = String.valueOf(fr.invoke("GetDeviceInfo", new Variant(30)).toInt());
//            date = String.valueOf(fr.invoke("GetDeviceInfo", new Variant(31)).toInt());
        }
        return true;
    }

    @Override
    public boolean getTaxReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean startCheck() {
        if (fr != null && fr.invoke("GetDeviceInfo", new Variant(4)).toInt() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean testCheck() {
        int fError;
        if (fr != null && fr.invoke("GetDeviceInfo", new Variant(4)).toInt() == 1) {
            fError = fr.invoke("TestPrinter", new Variant(3)).toInt();
            if (fError == 0) {
            } else {
                cheсkError(fError, "TestPrinter");
                P.MESSERR("Не удалось напечатать тестовый чек!");
                return false;
            }
            return true;
        }
        P.MESSERR("Не удалось напечатать тестовый чек!");
        return false;
    }

    @Override
    public boolean printDuplicate() {
        int fError;
        if (fr != null && fr.invoke("GetDeviceInfo", new Variant(4)).toInt() == 1) {
            fError = fr.invoke("PrintDuplicate").toInt();
            if (fError == 0) {
            } else {
                cheсkError(fError);
                return false;
            }
            return true;
        }
        P.MESSERR("Не удалось напечатать дубликат чека!");
        return false;
    }

    @Override
    public boolean printCorrectionCheck(int taxType, int correctionType, int calculationSign, double summCheck,
            double summNal, double summElectr, double summNds18, double SummNds10) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTaxType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getOFDLastDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean printChekFN(int check_id, ArrayList<ChekPosition> chekPos, boolean isVozvr, double price_cash, double price_card, double price_sert, double price_disc, String dk_inf, String seler, double price_cred, String trigger) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setSellerINN(String tabno) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setProductCode(String art, String asize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean printEan13(String code) {
        return false;
    }

}
