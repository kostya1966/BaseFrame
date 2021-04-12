/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import aplclass.ChekPosition;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

/**
 *
 * @author dima
 */
public class FiscalPrinter2 implements FiscalPrinter {

    ActiveXComponent fr;
    private static final int CHAR_COUNT_IN_CHECK = 34;
    int connect = -1;
    boolean viewError = false; // показывалась ли ошибка на экран
    public String ResultCode = "";
    public String ResultCodeDescription = "";
    private boolean sessionOpen = false;
    public String innQuery = "select distinct nomer,fio,tabel,nvl(b.inn,nvl(a.inn,' ')) inn\n"
            + "from (select coalesce(u.nomer, 100) as nomer, w.FIO, w.TABNO AS TABEL,inn\n"
            + "			from s_seller w\n"
            + "			left join s_shop q on q.shopid = '" + V.SHOP_ID + "'\n"
            + "			left join pos_users u on u.tabel = w.tabno\n"
            + "			where w.orgno = substr(q.shopnum,2) and trunc(sysdate) between trunc(w.DATEB) and trunc(w.DATED)\n"
            + "			and w.tabno not in (select tabno from s_seller_dekret where trunc(sysdate) between trunc(dateb) and trunc(dated))\n"
            + "			and (UPPER(w.STEXT2) like '%ПРОДАВЕЦ%' or UPPER(w.STEXT2) like '%ЗАВ%')\n"
            + "			\n"
            + "			UNION ALL\n"
            + "			\n"
            + "			SELECT NOMER,FIO,TABEL,' '\n"
            + "			FROM POS_USERS\n"
            + "			where (UPPER(DOLJNOST) like '%ПРОДАВЕЦ%' or UPPER(DOLJNOST) like '%ЗАВ%')\n"
            + "			and upper('" + V.SHOP_ID + "') like '%U%') a\n"
            + "left join st_seller_inn b on a.tabel = b.tabno\n"
            + "ORDER BY FIO";

    public FiscalPrinter2() {
        String ofd = "", model = "";
        try {
            fr = new ActiveXComponent("AddIn.DrvFR");
            connect();
            System.out.println("Соединения, код : " + connect);
            writeFRDataToOra(0, "connect", connect);
            if (connect == 0) {
                fr.setProperty("TableNumber", 18);
                fr.setProperty("RowNumber", 1);
                fr.setProperty("FieldNumber", 10);
                fr.invoke("GetFieldStruct");
                fr.invoke("ReadTable");
                ofd = fr.getProperty("ValueOfFieldString").toString();
                fr.invoke("GetDeviceMetrics");
                model = fr.getProperty("UDescription").toString();
                P.SQLUPDATE("call KAA_FP_MODEL_SAVE('" + ofd + "','" + model + "')");
            }
        } catch (Exception e) {
            // если не смогли создать объект принтера
        }
    }

    public String getResultCode() {
        return fr.getProperty("ResultCode").toString();
    }

    public String getResultDescription() {
        return fr.getProperty("ResultCodeDescription").toString();
    }

    //**************************************************************************
    // Соединение с ФР
    //
    //**************************************************************************    
    public boolean connect() {
        viewError = false;
        if (connect != 0) {
            connect = fr.invoke("Connect").getInt();
        }
        viewError = cheсkError();
        if (connect == 0) {
            fr.setProperty("Password", 30);
            return true;
        } else {
            return false;
        }
    }

    public int connected() {
        return connect;
    }

    //**************************************************************************
    // Проверка на ошибку и вывод её на экран
    // 02.05.2014 - убран вывод на экран, запись ошибки в поля 
    //**************************************************************************    
    private boolean cheсkError() {

        if (fr.getProperty("ResultCode").getInt() != 0) {
            if (!viewError) {
                ResultCode = fr.getProperty("ResultCode").toString();
                ResultCodeDescription = fr.getProperty("ResultCodeDescription").toString();
                //P.MESS(fr.getProperty("ResultCode").toString() + ": " + fr.getProperty("ResultCodeDescription").toString(), "Ошибка фискального регистратора");
            }
            return true;
        }
        return false;
    }

    //**************************************************************************
    // Внесение денег
    //
    //**************************************************************************
    public boolean cashIncome(double sum) {
        viewError = false;
        if (!connect()) {
            return false;
        }

        if (!sessionOpen) {
            if (!startSession()) {
                return false;
            }
        }

        Variant sumV = new Variant(sum);
        fr.setProperty("Summ1", sumV);
        writeFRDataToOra(0, "cashIncome", fr.invoke("CashIncome").toInt());

        viewError = cheсkError();
        fr.setProperty("Summ1", 0);
        if (fr.getProperty("ResultCode").toInt() == 0) {
            return true;
        } else {
            return false;
        }
    }

    //**************************************************************************
    // Получить текущее количество денег 
    //
    //**************************************************************************
    public double getCashReg() {
        fr.setProperty("RegisterNumber", 241);
        fr.invoke("GetCashReg");
        return fr.getProperty("ContentsOfCashRegister").toDouble();
    }

    //**************************************************************************
    // Изъятие денег
    //
    //**************************************************************************
    public boolean cashOutcome(double sum) {
        viewError = false;
        if (!connect()) {
            return false;
        }

        if (!sessionOpen) {
            if (!startSession()) {
                return false;
            }
        }

        if (getCashReg() < sum) {
            P.MESSERR("Недостаточно денег на Фискальном регистраторе. Остаток " + getCashReg() + " руб.");
            return false;
        }

        Variant sumV = new Variant(sum);
        fr.setProperty("Summ1", sumV);
        writeFRDataToOra(0, "cashOutcome", fr.invoke("CashOutCome").toInt());

        viewError = cheсkError();
        fr.setProperty("Summ1", 0);

        if (fr.getProperty("ResultCode").toInt() == 0) {
            return true;
        } else {
            return false;
        }
    }

    //**************************************************************************
    // Чек на продажу/возврат
    //
    //**************************************************************************
    public boolean printChek(int check_id, ArrayList<ChekPosition> chekPos, boolean isVozvr, double price_cash,
            double price_card, double price_sert, double price_disc, String dk_inf, String seler, double price_cred, String trigger) {

        System.out.println("Start printCheck");

        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'ONLINE_KASSA_RF'", "ONLINE_KASSA_RF");
        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'ONLINE_KASSA_RF_DISC'", "ONLINE_KASSA_RF_DISC");
        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'ONLINE_KASSA_RF_KASSIR'", "ONLINE_KASSA_RF_KASSIR");

        viewError = false;

        if (!connect()) {
            return false;
        }

        if (!sessionOpen) {
            if (!startSession()) {
                return false;
            }
        }

        System.out.println("Connected printCheck");

        // проверка на состояние ФР (готовность к печати чека)
        int frStatus = getStatus();
        int frAdvStatus = getAdvStatus();
        if ((frStatus != 2 && frStatus != 4) || frAdvStatus != 0) {
            P.MESS("Печать невозможна! \n Режим фискального регистратора: " + getStatusDescription() + " \n Подрежим: " + getAdvStatusDescription());
            return false;
        }

        //ставлю кассира в таблицу кассы равного текущему продавецу
        fr.setProperty("TableNumber", 2);
        fr.setProperty("RowNumber", 30);
        fr.setProperty("FieldNumber", 2);
        fr.setProperty("ValueOfFieldString", seler);
        doWriteFRTable();
        //

        int openCheck = 0;
        int sale = 0;
        int closeCheck = 0;

        fr.setProperty("Summ1", 0);
        fr.setProperty("Summ2", 0);
        fr.setProperty("Summ3", 0);
        fr.setProperty("Summ4", 0);

        if (isVozvr) {
            if (price_cash > getCashReg()) {
                P.MESSERR("Не хватает наличности в кассе для возврата!");
                return false;
            }
            fr.setProperty("CheckType", 2);
        } else {
            fr.setProperty("CheckType", 0);
        }

        openCheck = fr.invoke("OpenCheck").toInt();
        System.out.println("Opened printCheck");
        writeFRDataToOra(check_id, "OpenChek", openCheck);
        viewError = cheсkError();

        P.SQLEXECT("select nvl(max(tabel),' ') tabel from pos_users where fio = '" + seler + "'", "CHECK_SELLER");

        if (!setSellerINN(A.GETVALS("CHECK_SELLER.tabel"))) {
            return false;
        }

        //тег кассира
        if (A.GETVALS("ONLINE_KASSA_RF.SYS_VAL").equals("T") && A.GETVALS("ONLINE_KASSA_RF_KASSIR.SYS_VAL").equals("T")) {
            fr.setProperty("TagNumber", new Variant(1021));
            fr.setProperty("TagType", new Variant(7));
            fr.setProperty("TagValueStr", new Variant(seler));
            fr.setProperty("TagValueLength", new Variant(64));
            fr.invoke("FNSendTag");
        }

//        fr.setProperty("CustomerEmail", "andrey.kuzmenkov@belwest.com");
//        fr.invoke("FNSendCustomerEmail");
        // проставляем свой номер чека:
        fr.setProperty("StringForPrinting", "Номер док. продажи: " + check_id);
        fr.invoke("PrintString");

        int garant = 30;
        if (dk_inf != null && !dk_inf.equals("") && 1 < 0) {
            P.SQLEXECT("select nvl(get_dk_scores_garan('" + dk_inf + "'),30) DAYCOUNT from dual", "GARANT");
            garant = (int) Double.parseDouble(A.GETVALS("GARANT.DAYCOUNT"));
        }
        //некондиция
        P.SQLEXECT("select a.procent,a.scan from d_kassa_cur a"
                + " where a.procent > 0", "nekondChekPr");
        if (A.RECCOUNT("nekondChekPr") != 0) {
            String strbuf = "Гарантия: " + garant + " дней, не распространяется на дефект";
            for (int ni = 0; ni < 2; ni++) {
                if (ni != 1) {
                    fr.setProperty("StringForPrinting", strbuf.substring(CHAR_COUNT_IN_CHECK * ni, CHAR_COUNT_IN_CHECK * (ni + 1)));
                    fr.invoke("PrintString");
                } else {
                    fr.setProperty("StringForPrinting", strbuf.substring(CHAR_COUNT_IN_CHECK * ni));
                    fr.invoke("PrintString");
                }
            }
        } else {
            fr.setProperty("StringForPrinting", "Гарантия: на обувь - " + garant + " дней");
            fr.invoke("PrintString");
            fr.setProperty("StringForPrinting", "          на сумки - 50 дней");
            fr.invoke("PrintString");
        }

        int ink = 0;
        int inkVozvr = 0;
        if (isVozvr) {
            P.SQLEXECT("select nvl(min(id_cur),1) id_cur from d_return_cur where kass_id = '" + P.getUniqueKassNo() + "'", "CUR_RET");
            inkVozvr = (int) Double.parseDouble(A.GETVALS("CUR_RET.ID_CUR"));
        }
        double scoreplus = 0;
        double scoreminus = 0;
        System.out.println("Before ChekPosition printCheck");

        //док возврат
        if (trigger.equals("DRET_RU")) {
            A.GOTOP("G_PRIX2");
        }

        for (ChekPosition cp : chekPos) {
            ink++;
            inkVozvr++;
            String product = cp.productName;
            String productPeace = "";
            boolean findSpace = false;
            while (product.length() != 0) {
                if (product.length() > CHAR_COUNT_IN_CHECK) {
                    for (int charNo = product.substring(0, CHAR_COUNT_IN_CHECK).length() - 2; charNo > CHAR_COUNT_IN_CHECK / 2; charNo--) { // ищу пробел с которого можно сделать перенос
                        if (product.substring(charNo, charNo + 1).equals(" ")) {
                            productPeace = product.substring(0, charNo);
                            product = product.substring(charNo);
                            findSpace = true;
                            break;
                        }
                    }
                    if (!findSpace) {
                        productPeace = product.substring(0, CHAR_COUNT_IN_CHECK);
                        product = product.substring(CHAR_COUNT_IN_CHECK);
                    }
                } else {
                    productPeace = product;
                    product = "";
                }
                fr.setProperty("StringForPrinting", productPeace);
                System.out.println("PrintString: " + fr.invoke("PrintString"));
            }

            fr.setProperty("StringForPrinting", "");
            fr.setProperty("Quantity", cp.quantity);
            if (A.GETVALS("ONLINE_KASSA_RF.SYS_VAL").equals("T") && A.GETVALS("ONLINE_KASSA_RF_DISC.SYS_VAL").equals("F")) {
                fr.setProperty("Price", new Variant(((double) Math.round((cp.price - cp.disc / cp.quantity) * 100000)) / 100000));
            } else {
                fr.setProperty("Price", new Variant(cp.price));
            }

//            System.out.println("* NAME : " + cp.productName);
//            System.out.println("* KOL : " + cp.quantity);
//            System.out.println("* CENA : " + cp.price);
            fr.setProperty("Department", 1);

            //26122017 KAA указание процента и суммы ндс
            P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'NDS_IN_CHECK'", "NDS_CHECK");
            if (A.GETVALS("NDS_CHECK.SYS_VAL").equals("T")) {
                if (!isVozvr) {
                    P.SQLEXECT("select nvl(max(y.nds),0) nds\n"
                            + "from d_kassa_cur x\n"
                            + "left join s_nds y on x.art = y.art and x.asize = y.asize\n"
                            + "where x.kass_id = '" + P.getUniqueKassNo() + "' and x.posnr = " + ink, "NDS_POSITION");
                } else {
                    if (trigger.equals("DRET_RU")) {
                        P.SQLEXECT("select nvl(max(y.nds),0) nds\n"
                                + "from d_prixod2 x\n"
                                + "left join s_nds y on x.art = y.art and x.asize = y.asize\n"
                                + "where x.id = " + A.GETVALS("G_PRIX2.ID") + " and x.id_rec = " + A.GETVALS("G_PRIX2.ID_REC") + "", "NDS_POSITION");
                        A.GOTO("G_PRIX2", ink + 1);
                    } else {
                        P.SQLEXECT("select nvl(max(y.nds),0) nds \n"
                                + "from d_return_cur x\n"
                                + "left join s_nds y on x.art = y.art and x.asize = y.asize\n"
                                + "where x.kass_id = '" + P.getUniqueKassNo() + "'\n"
                                + "and x.id_sale = (select id_sale from d_return_cur where kass_id = '" + P.getUniqueKassNo() + "' and id_cur = " + (inkVozvr - 1) + ")", "NDS_POSITION");
                    }
                }
                if (Double.parseDouble(A.GETVALS("NDS_POSITION.NDS")) == 10) {
                    fr.setProperty("Tax1", 2);
                } else {
                    fr.setProperty("Tax1", 1);
                }
            }
            //

            if (isVozvr) {
                sale = fr.invoke("ReturnSale").toInt();
            } else {
                sale = fr.invoke("Sale").toInt();
            }
            writeFRDataToOra(check_id, "Sale", sale);
            viewError = cheсkError();

            if (A.GETVALS("ONLINE_KASSA_RF.SYS_VAL").equals("T")) {
                if (cp.disc != 0) {
                    fr.setProperty("Summ1", new Variant(cp.disc));
                    fr.setProperty("StringForPrinting", "");
                    fr.invoke("Discount");
                    fr.setProperty("Summ1", 0);
                    viewError = cheсkError();
                }
            }

            //некондиция
            P.SQLEXECT("select a.procent,a.scan from d_kassa_cur a"
                    + " where a.kass_id = '" + P.getUniqueKassNo() + "' and a.posnr = " + ink, "nekondChek");
            if (!A.GETVALS("nekondChek.procent").equals("0") && !A.GETVALS("nekondChek.procent").equals("0.0")) {
                P.SQLEXECT("select 'Код дефекта: '||vid_brak vid_brak, 'Дефект: '||brak_name brak_name, ceil(length('Дефект: '||brak_name)/" + CHAR_COUNT_IN_CHECK + ") leng,'brak' tip \n"
                        + "from pos_brak where scan = '" + A.GETVALS("nekondChek.scan") + "'\n"
                        + "union all\n"
                        + "select 'Код дефекта: '||b.vid_brak vid_brak, 'Дефект: '||b.brak_name brak_name, ceil(length('Дефект: '||b.brak_name)/" + CHAR_COUNT_IN_CHECK + ") leng,'nek' tip\n"
                        + "from s_label_v a\n"
                        + "left join st_brak_uc b on nvl(a.code_def,'0') = b.vid_brak\n"
                        + "where a.scan = '" + A.GETVALS("nekondChek.scan") + "' and a.procent > 0", "POS_BRAK_ALL_CHEK");
                fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.vid_brak"));
                fr.invoke("PrintString");
                if (A.GETVALS("pos_brak_all_chek.brak_name").length() > CHAR_COUNT_IN_CHECK) {
                    for (int ni = 0; ni < (int) Double.parseDouble(A.GETVALS("pos_brak_all_chek.leng")); ni++) {
                        if (ni != (int) Double.parseDouble(A.GETVALS("pos_brak_all_chek.leng")) - 1) {
                            fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.brak_name").substring(CHAR_COUNT_IN_CHECK * ni, CHAR_COUNT_IN_CHECK * (ni + 1)));
                            fr.invoke("PrintString");
                        } else {
                            fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.brak_name").substring(CHAR_COUNT_IN_CHECK * ni));
                            fr.invoke("PrintString");
                        }
                    }
                } else {
                    fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.brak_name"));
                    fr.invoke("PrintString");
                }
                if (A.GETVALS("POS_BRAK_ALL_CHEK.TIP").equals("brak")) {
                    P.SQLEXECT("select nvl(max(d.discount_sum - (cena2p(sysdate,b.art,0,1) - cena2s(sysdate,b.art,0,1))),0) neksum from pos_sale4 d inner join pos_sale2 b on d.id_sale = b.id_sale where b.id_chek = " + check_id + " and d.disc_type_id = 'N000000001'", "sale4neksum");
                    fr.setProperty("StringForPrinting", "Скидка по дефекту: " + Double.parseDouble(A.GETVALS("sale4neksum.neksum")) + "(" + A.GETVALS("nekondChek.procent") + "%" + ")");
                    fr.invoke("PrintString");
//                fr.setProperty("StringForPrinting", "Цена со скидкой по дефекту: " + (((double) Math.round((cp.price * cp.quantity - Double.parseDouble(A.GETVALS("sale4neksum.neksum"))) * 100000)) / 100000));
//                fr.invoke("PrintString");
                    if (cp.disc - Double.parseDouble(A.GETVALS("sale4neksum.neksum")) > 0) {
                        fr.setProperty("StringForPrinting", "Скидка: " + (((double) Math.round((cp.disc - Double.parseDouble(A.GETVALS("sale4neksum.neksum"))) * 100000)) / 100000));
                        fr.invoke("PrintString");
                        fr.setProperty("StringForPrinting", "Цена со скидкой: " + (((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000));
                        fr.invoke("PrintString");
                    }
                } else {
                    if (cp.disc != 0) {
                        fr.setProperty("StringForPrinting", "Скидка: " + cp.disc);
                        fr.invoke("PrintString");
                        fr.setProperty("StringForPrinting", "Цена со скидкой: " + (((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000));
                        fr.invoke("PrintString");
                    }
                }
            } else {
                if (cp.disc != 0) {
                    fr.setProperty("StringForPrinting", "Скидка: " + cp.disc);
                    fr.invoke("PrintString");
                    fr.setProperty("StringForPrinting", "Цена со скидкой: " + (((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000));
                    fr.invoke("PrintString");
                }
            }

            if (dk_inf != null && !dk_inf.equals("")) {
                P.SQLEXECT("select nvl(max(dk_type),' ') dk_type from st_dk where id_dk = '" + dk_inf + "'", "DK_INF");
                if (A.GETVALS("DK_INF.DK_TYPE").equals("B")) {
                    P.SQLEXECT("select * from d_kassa_disc_cur b\n"
                            + "inner join d_kassa_cur a on a.posnr = b.posnr and a.kass_id = b.kass_id\n"
                            + "where b.kass_id = '" + P.getUniqueKassNo() + "' and b.posnr = " + ink + " and discount_type_id = 'A000000001' and b.discount = 0 and b.discount_sum != 0", "DISC_CUR");
                    P.SQLEXECT("select get_dk_scores_sale('" + dk_inf + "'," + (((double) Math.round(cp.price - cp.disc) * 100000) / 100000) + "," + cp.quantity + ") SCPLUS from dual", "GET_DK_SCORES_SALE");
                    if (!isVozvr) {
//                        if (A.RECCOUNT("DISC_CUR") == 0) {
                        scoreplus += Double.parseDouble(A.GETVALS("GET_DK_SCORES_SALE.SCPLUS"));
//                        }
                    } else {
                        P.SQLEXECT("select id_sale from d_return_cur where kass_id = '" + P.getUniqueKassNo() + "' and id_cur = " + (inkVozvr - 1), "VOZVR_SALE_SALE");
                        P.SQLEXECT("select nvl(sum(dk_sum),0) dk_sum from pos_dk_scores where id_sale = '" + A.GETVALS("VOZVR_SALE_SALE.ID_SALE") + "' and dk_sum > 0 and doc_type in ('ck')", "DK4SCORES");
                        scoreminus += Double.parseDouble(A.GETVALS("DK4SCORES.dk_sum"));
                    }
                }
            }
        }
        System.out.println("After ChekPosition printCheck");

        fr.setProperty("StringForPrinting", "Продавец: ");
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", seler);
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", "------------------------------------");
        fr.invoke("PrintString");

        if (dk_inf != null && !dk_inf.equals("")) {
            fr.setProperty("StringForPrinting", "Дисконтная карта: " + dk_inf);
            fr.invoke("PrintString");

            P.SQLEXECT("select fio from st_dk where id_dk = '" + dk_inf + "'", "curDKfio");
            if (A.RECCOUNT("curDKfio") != 0) {
                A.SELECT("curDKfio");
                /*           fr.setProperty("StringForPrinting", "Владелец карты: " + (A.GETVALS("FIO").length()+"Владелец карты: ".length() > CHAR_COUNT_IN_CHECK
                 ? A.GETVALS("FIO").substring(0, CHAR_COUNT_IN_CHECK-"Владелец карты: ".length()) + "\n" 
                 + A.GETVALS("FIO").substring(CHAR_COUNT_IN_CHECK-"Владелец карты: ".length())
                 : A.GETVALS("FIO")));
                 fr.invoke("PrintString");*/
                if (A.GETVALS("FIO").length() + "Владелец карты: ".length() > CHAR_COUNT_IN_CHECK) {
                    fr.setProperty("StringForPrinting", "Владелец карты: " + A.GETVALS("FIO").substring(0, CHAR_COUNT_IN_CHECK - "Владелец карты: ".length()));
                    fr.invoke("PrintString");
                    fr.setProperty("StringForPrinting", A.GETVALS("FIO").substring(CHAR_COUNT_IN_CHECK - "Владелец карты: ".length()));
                    fr.invoke("PrintString");
                } else {
                    fr.setProperty("StringForPrinting", A.GETVALS("FIO"));
                    fr.invoke("PrintString");
                }
                P.SQLEXECT("select nvl(max(dk_type),' ') dk_type from st_dk where id_dk = '" + dk_inf + "'", "DK_INF");
                if (A.GETVALS("DK_INF.DK_TYPE").equals("B")) {
                    if (!isVozvr) {
                        P.SQLEXECT("select nvl(sum(d.discount_sum*b.kol),0) SCMINUS from pos_sale4 d inner join pos_sale2 b on b.id_sale = d.id_sale where d.disc_type_id = 'A000000001' and b.id_chek = " + check_id, "SALE4SCORES");
                        scoreminus = Double.parseDouble(A.GETVALS("SALE4SCORES.SCMINUS"));
                    } else {
                        P.SQLEXECT("select distinct id_chek_vozvr from pos_sale2 where id_chek = " + check_id, "VOZVR_SALE_CHEK");
                        P.SQLEXECT("select nvl(sum(d.discount_sum*b.kol),0) SCMINUS from pos_sale4 d inner join pos_sale2 b on b.id_sale = d.id_sale where d.disc_type_id = 'A000000001' and b.id_chek = " + A.GETVALS("VOZVR_SALE_CHEK.ID_CHEK_VOZVR"), "SALE4SCORES");
                        scoreplus = Double.parseDouble(A.GETVALS("SALE4SCORES.SCMINUS"));
                    }
                    if (scoreminus != 0) {
                        fr.setProperty("StringForPrinting", "Списано баллов: " + scoreminus);
                        fr.invoke("PrintString");
                    }
                    if (scoreplus != 0) {
                        fr.setProperty("StringForPrinting", "Начислено баллов: " + scoreplus);
                        fr.invoke("PrintString");
                    }
                }
            }
        }

        System.out.println("Before Summ1 printCheck");
        if (!A.GETVALS("ONLINE_KASSA_RF.SYS_VAL").equals("T")) {
            if (price_disc != 0) {
                fr.setProperty("Summ1", new Variant(price_disc));
                fr.setProperty("StringForPrinting", "");
                fr.invoke("Discount");
                fr.setProperty("Summ1", 0);
                viewError = cheсkError();
            }
        }

        P.SQLEXECT("select a.* from d_kassa_disc_cur a"
                + " inner join pos_skidki1 b on a.discount_type_id = b.docid"
                + " where b.disc_type_id = 'ZK10'", "curDkType");
        if (A.RECCOUNT("curDkType") != 0) {
            fr.setProperty("StringForPrinting", "АКЦИЯ: СКИДКА НА ВТОРУЮ ПАРУ");
            fr.invoke("PrintString");
        }
        A.CLOSE("curDkType");

        P.SQLEXECT("select a.* from d_kassa_disc_cur a"
                + " inner join pos_skidki1 b on a.discount_type_id = b.docid"
                + " where b.disc_type_id = 'ZK11'", "curDkType");
        if (A.RECCOUNT("curDkType") != 0) {
            fr.setProperty("StringForPrinting", "АКЦИЯ: ТРЕТЬЯ ПАРА В ПОДАРОК");
            fr.invoke("PrintString");
        }
        A.CLOSE("curDkType");

        if (price_cred != 0 && !isVozvr) {
            P.SQLEXECT("select nvl(sum(d.discount_sum),0) disc \n"
                    + "from pos_sale3 c\n"
                    + "inner join pos_sale2 b on c.id_chek = b.id_chek\n"
                    + "inner join pos_sale4 d on d.id_sale = b.id_sale\n"
                    + "where \n"
                    + "c.id_chek = " + check_id + " and \n"
                    + "c.vid_op_id = 4 and c.kred_id in (4) and disc_type_id like 'CR%'", "KRED_PRINT_CHECK");
            if (Double.parseDouble(A.GETVALS("KRED_PRINT_CHECK.DISC")) > 0) {
                fr.setProperty("StringForPrinting", "Сумма переплаты по рассрочке:");
//                fr.setProperty("StringForPrinting", "Скидка по рассрочке:");
                fr.invoke("PrintString");
                fr.setProperty("StringForPrinting", A.GETVALS("KRED_PRINT_CHECK.DISC") + " руб.");
                fr.invoke("PrintString");
                fr.setProperty("StringForPrinting", "------------------------------------");
                fr.invoke("PrintString");
            }
        }

        fr.setProperty("StringForPrinting", "------------------------------------");
        fr.invoke("PrintString");

        String priceType = "Вид оплаты:";

        if (price_cash != 0) {
            fr.setProperty("Summ1", new Variant(price_cash));
            priceType = priceType + " Наличными";
            //System.out.println("Oplata nal: " + price_cash);
        }
        if (price_card != 0) {
            fr.setProperty("Summ2", new Variant(price_card));
            priceType = priceType + " Картой";
            //System.oudt.println("Oplata card: " + price_card);
        }
        if (price_sert != 0) {
            fr.setProperty("Summ3", new Variant(price_sert));
            priceType = priceType + " Сертификат";
            // System.out.println("Oplata sert: " + price_sert);
        }
        if (price_cred != 0) {
            fr.setProperty("Summ4", new Variant(price_cred));
            priceType = priceType + " Потреб.кредит";
            //System.out.println("Oplata cred: " + price_cred);
        }
        fr.setProperty("StringForPrinting", priceType);
        fr.invoke("PrintString");

        fr.setProperty("StringForPrinting", "");
        System.out.println("Before Close printCheck");
        closeCheck = fr.invoke("CloseCheck").toInt();
        System.out.println("After Close printCheck");
        writeFRDataToOra(check_id, "CloseCheck", closeCheck);
        viewError = cheсkError();

        if (closeCheck != 0 && !V.NULL_FISKAL.equals("T")) {
            P.MESSERR("При формировании чека возникла ошибка!\nКод ошибки: " + V.FP.getResultCode() + ".\n" + V.FP.getResultDescription());
        }
        System.out.println("Before CUT printCheck");
        if (V.CUT_PAPER_CHECK) { // если обработка обрыва ленты включена
            fr.invoke("GetShortECRStatus");
            System.out.println("ECR printCheck");
            if ((openCheck != 0 || sale != 0 || closeCheck != 0) && getAdvStatus() != 2) {
                System.out.println("ECR printCheck1");
                return false;
            }

            if (waitPrint()) { // если закончилась бумага, то аннулируем чек и печатаем по новой
                System.out.println("Wait ECR printCheck");
                fr.invoke("GetShortECRStatus");
                while (getAdvStatus() == 3) { // пока не перейдет в другой статус
                    fr.invoke("GetShortECRStatus");
                }
                fr.invoke("CancelCheck");
                while (fr.getProperty("ResultCode").getInt() == 80) { // пока ошибка: Идет печать пред. команды
                    //System.out.println(fr.invoke("GetShortECRStatus"));
                    fr.invoke("CancelCheck");
                };
                waitPrint();
//                return printChek(check_id, chekPos, isVozvr, price_cash, price_card, price_sert, price_disc, dk_inf, seler, price_cred);
                return false;
            }
        } else {
            System.out.println("ECR printCheck2");
            if ((openCheck != 0 || sale != 0 || closeCheck != 0)) {
                return false;
            }
        }
        System.out.println("After CUT printCheck");
        return true;
    }

    // ************************************************************************
    // закрытие соединения с ФР
    //
    //*************************************************************************    
    public void closeConnect() {
        fr.invoke("AdminUnlockPort");
        while (fr.invoke("PortLocked").toBoolean()) {
        }
        fr.invoke("Disconnect");
    }

    // ************************************************************************
    // печать X отчета
    //
    //*************************************************************************
    public boolean getXReport() {
//        if (!sessionOpen) {
//            if (!startSession()) {
//                return false;
//            }
//        }

        viewError = false;
        if (fr.invoke("PrintReportWithoutCleaning").getInt() == 0) {;
            if (V.CUT_PAPER_CHECK) {
                waitPrint();
            }
            return true;
        } else {
            viewError = cheсkError();
            return false;
        }
    }

    //**************************************************************************
    // печать Z отчета
    //
    //**************************************************************************
    public boolean getZReport() {
        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'NDS_IN_CHECK'", "NDS_IN_CHECK");
        if (A.GETVALS("NDS_IN_CHECK.SYS_VAL").equals("T")) {
            if (!getTaxReport()) {
                return false;
            }
        }
        viewError = false;

        if (fr.invoke("FNBeginCloseSession").getInt() != 0) {
            viewError = cheсkError();
            return false;
        }

        if (!setSellerINN("select")) {
            return false;
        }

        if (fr.invoke("PrintReportWithCleaning").getInt() == 0) {
            if (V.CUT_PAPER_CHECK) {
                waitPrint();
            }
            sessionOpen = false;
            return true;
        } else {
            viewError = cheсkError();
            return false;
        }
    }

    //*************************************************************************
    // количество чеков продаж
    //
    //**************************************************************************
    public int getSaleCheckCount() {
//        if (!sessionOpen) {
//            if (!startSession()) {
//                return -1;
//            }
//        }

        try {
            fr.setProperty("RegisterNumber", 148);
            System.out.println(fr.invoke("GetOperationReg"));
            return fr.getProperty("ContentsOfOperationRegister").toInt();
        } catch (Exception ex) {
            System.out.println("Ошибка чтения из регистра ФР. Невозможно прочитать количество чеков.");
        }
        return -1;
    }

    //**************************************************************************
    // количество чеков возвратов
    //
    //**************************************************************************
    public int getReturnCheckCount() {
//        if (!sessionOpen) {
//            if (!startSession()) {
//                return -1;
//            }
//        }

        try {
            fr.setProperty("RegisterNumber", 150);
            System.out.println(fr.invoke("GetOperationReg"));
            return fr.getProperty("ContentsOfOperationRegister").toInt();
        } catch (Exception ex) {
            System.out.println("Ошибка чтения из регистра ФР. Невозможно прочитать количество чеков.");
        }
        return -1;
    }

    //**************************************************************************
    // получить статус ФР
    //
    //**************************************************************************    
    public int getStatus() {
        fr.invoke("GetECRStatus");
        return fr.getProperty("ECRMode").toInt();
    }

    //**************************************************************************
    // получить описание статуса ФР
    //
    //**************************************************************************    
    public String getStatusDescription() {
        fr.invoke("GetECRStatus");
        return fr.getProperty("ECRModeDescription").toString();
    }

    //**************************************************************************
    // получить подрежим ФР
    //
    //**************************************************************************    
    public int getAdvStatus() {
        System.out.println("Before getAdvStatus printCheck");
        fr.invoke("GetECRStatus");
        int result = fr.invoke("ECRAdvancedMode").toInt();
        System.out.println("After getAdvStatus printCheck - " + result);
        return result;
    }

    //**************************************************************************
    // получить описание подрежима ФР
    //
    //**************************************************************************    
    public String getAdvStatusDescription() {
        fr.invoke("GetECRStatus");
        return fr.invoke("ECRAdvancedModeDescription").toString();
    }

    //**************************************************************************
    // продолжение печати чека
    //
    //**************************************************************************    
    public boolean continuePrint() {
        System.out.println("continuePrint printCheck");
        viewError = false;
        if (fr.invoke("ContinuePrint").getInt() == 0) {
            System.out.println("continuePrint true printCheck");
            return true;
        } else {
            System.out.println("continuePrint false printCheck");
            viewError = cheсkError();
            System.out.println("continuePrint false2 printCheck");
            return false;
        }
    }

    //**************************************************************************
    // ожидание завершения печати
    // возвращает true , если была команда продолжения печати
    //**************************************************************************   
    private boolean waitPrint() {
        System.out.println("Before waitPrint printCheck");
        boolean continuePrint = false;
        fr.invoke("GetShortECRStatus");
        System.out.println("After waitPrint printCheck");
        while (getAdvStatus() != 0) {
            System.out.println("After2 waitPrint printCheck");
            if (getAdvStatus() == 2 || getAdvStatus() == 1) { //Активное или пассивное отсутвие бумаги
                P.MESS("Вставьте новый рулон бумаги");
            }
            if (getAdvStatus() == 3) { // после активного отсутcвия бумаги
                System.out.println("After3 waitPrint printCheck");
                continuePrint();
                System.out.println("After4 waitPrint printCheck");
                continuePrint = true;
            }
            System.out.println("After5 waitPrint printCheck");
            fr.invoke("GetShortECRStatus");
            System.out.println("After6 waitPrint printCheck");
        }
        return continuePrint;
    }

    //**************************************************************************
    // записывает результаты выполнения команд ФР в таблицу POS_SALE5
    //
    //**************************************************************************
    private void writeFRDataToOra(int check_id, String command, int value) {
        if (value != 0) {
            P.SQLUPDATE("INSERT INTO POS_SALE5 (ID_CHEK, COMMAND_NAME, COM_VALUE) VALUES (" + check_id + ", '" + command + "', " + value + ")");
        }
    }

    //**************************************************************************
    // записывает в таблицы фр необходимые для работы кассы значения
    //
    //**************************************************************************
    public boolean writeFRTable() {
        boolean error = false;
        fr.setProperty("TableNumber", 5);
        fr.setProperty("RowNumber", 2);
        fr.setProperty("FieldNumber", 1);
        //      fr.setProperty("ValueOfFieldInteger", 2);
        fr.setProperty("ValueOfFieldString", "ПЛАТ.КАРТОЙ");
        error = doWriteFRTable();

        fr.setProperty("TableNumber", 5);
        fr.setProperty("RowNumber", 3);
        fr.setProperty("FieldNumber", 1);
        //       fr.setProperty("ValueOfFieldInteger", 0);
        fr.setProperty("ValueOfFieldString", "СЕРТИФИКАТ");
        error = doWriteFRTable();

        fr.setProperty("TableNumber", 5);
        fr.setProperty("RowNumber", 4);
        fr.setProperty("FieldNumber", 1);
        //       fr.setProperty("ValueOfFieldInteger", 0);

        fr.setProperty("ValueOfFieldString", "ПОТРЕБ.КРЕДИТ.");

        error = doWriteFRTable();

        fr.setProperty("TableNumber", 1);
        fr.setProperty("RowNumber", 1);
        fr.setProperty("FieldNumber", 2);
        fr.setProperty("ValueOfFieldInteger", 1);
        //      fr.setProperty("ValueOfFieldString", "0");
        error = doWriteFRTable();
//        P.MESS(fr.getProperty("ResultCode").toString() + ": " + fr.getProperty("ResultCodeDescription").toString(), "Ошибка фискального регистратора");

        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val, case when to_char(sysdate,'yyyymmdd') >= '20190101' then 'T' else 'F' end dated,\n"
                + "case when to_char(sysdate,'yyyymmdd') >= '20200701' then 'T' else 'F' end dated2\n"
                + "from ri_system where sys_var = 'NDS_IN_CHECK'", "NDS_CHECK");
        
        if (A.GETVALS("NDS_CHECK.DATED2").equals("T")) {
            P.SQLUPDATE("call set_system_val('NDS_IN_CHECK','T','Расчет НДС при продаже')");
        }
        
        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val, case when to_char(sysdate,'yyyymmdd') >= '20190101' then 'T' else 'F' end dated,\n"
                + "case when to_char(sysdate,'yyyymmdd') >= '20200701' then 'T' else 'F' end dated2\n"
                + "from ri_system where sys_var = 'NDS_IN_CHECK'", "NDS_CHECK");
        
        fr.setProperty("TableNumber", 18);
        fr.setProperty("RowNumber", 1);
        fr.setProperty("FieldNumber", 5);
        if (A.GETVALS("NDS_CHECK.DATED2").equals("F") && A.GETVALS("NDS_CHECK.SYS_VAL").equals("F")) {
            fr.setProperty("ValueOfFieldInteger", 8);
        } else {
            fr.setProperty("ValueOfFieldInteger", 1);
        }
        error = doWriteFRTable();

        if (A.GETVALS("NDS_CHECK.SYS_VAL").equals("T")) {

            if (A.GETVALS("NDS_CHECK.DATED").equals("T") && getStatus() == 4) {
                fr.setProperty("TableNumber", 6);
                fr.setProperty("RowNumber", 1);
                fr.setProperty("FieldNumber", 1);
                fr.setProperty("ValueOfFieldInteger", "2000");
                error = doWriteFRTable();

                fr.setProperty("TableNumber", 6);
                fr.setProperty("RowNumber", 1);
                fr.setProperty("FieldNumber", 2);
                fr.setProperty("ValueOfFieldString", "НДС 20%");
                error = doWriteFRTable();
            }
        }

        //Новые реквизиты в чеке
        if (getStatus() == 4) {
            fr.setProperty("TableNumber", 17);
            fr.setProperty("RowNumber", 1);
            fr.setProperty("FieldNumber", 39);
            fr.setProperty("ValueOfFieldInteger", 1);
            error = doWriteFRTable();
        }
        return error;
    }

    private boolean doWriteFRTable() {
        viewError = false;
        fr.invoke("GetFieldStruct");
        fr.invoke("WriteTable");
        return cheсkError();
    }

    //**************************************************************************
    // сменный отчет по кассирам
    //
    //**************************************************************************    
    public boolean cashierReport() {
        return true;
    }

    //**************************************************************************
    // сменный отчет по товарам
    //
    //**************************************************************************    
    public boolean productReport() {
        return true;
    }

    // ************************************************************************
    // печать буфера контрольной ленты
    //
    //*************************************************************************
    public boolean getKReport() {
        return true;
    }

    public String EKLZViewReportByNum() {
        return "";
    }

    public boolean printBankCheck(String checkText) {
//        System.out.println("bankCh-start");
//        System.out.println(checkText);
//        System.out.println("bankCh-end");
        if (!connect()) {
            return false;
        }

//        if (!sessionOpen) {
//            if (!startSession()) {
//                return false;
//            }
//        }
        // проверка на состояние ФР (готовность к печати чека)
        int frStatus = getStatus();
        int frAdvStatus = getAdvStatus();
        if ((frStatus != 2 && frStatus != 4) || frAdvStatus != 0) {
            P.MESS("Печать невозможна! \n Режим фискального регистратора: " + getStatusDescription() + " \n Подрежим: " + getAdvStatusDescription());
            return false;
        }

        ArrayList<String> arrStr = new ArrayList<>(Arrays.asList(checkText.replaceAll("\\r", "").split("\n")));

        for (int i = 0; i < arrStr.size(); i++) {
            if (arrStr.get(i).length() <= CHAR_COUNT_IN_CHECK) {
                fr.setProperty("StringForPrinting", arrStr.get(i));
                fr.invoke("PrintString");
            } else {
                String temp = arrStr.get(i);
                int max = (int) Math.ceil((double) temp.length() / CHAR_COUNT_IN_CHECK);

                for (int j = 0; j < max; j++) {
                    if (j == max - 1) {
                        fr.setProperty("StringForPrinting", temp);
                        fr.invoke("PrintString");
                    } else {
                        fr.setProperty("StringForPrinting", temp.substring(0, CHAR_COUNT_IN_CHECK));
                        fr.invoke("PrintString");

                        temp = temp.substring(CHAR_COUNT_IN_CHECK);
                    }
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            fr.setProperty("StringForPrinting", " ");
            fr.invoke("PrintString");
        }

        fr.setProperty("CutType", new Variant(true));

        fr.invoke("CutCheck");
        fr.invoke("PrintCliche");

        return true;

    }

    ;

    @Override
    public boolean printChekText(int check_id, ArrayList<ChekPosition> chekPos, boolean isVozvr, double price_cash, double price_card, double price_sert, double price_disc, String dk_inf, String seler, double price_cred) {
        viewError = false;

        if (!connect()) {
            return false;
        }

        if (!sessionOpen) {
            if (!startSession()) {
                return false;
            }
        }

        // проверка на состояние ФР (готовность к печати чека)
        int frStatus = getStatus();
        int frAdvStatus = getAdvStatus();
        if ((frStatus != 2 && frStatus != 4 && frStatus != 3) || frAdvStatus != 0) {
            P.MESS("Печать невозможна! \n Режим фискального регистратора: " + getStatusDescription() + " \n Подрежим: " + getAdvStatusDescription());
            return false;
        }

        fr.setProperty("Summ1", 0);
        fr.setProperty("Summ2", 0);
        fr.setProperty("Summ3", 0);
        fr.setProperty("Summ4", 0);

        if (isVozvr) {
            fr.setProperty("CheckType", 2);
        } else {
            fr.setProperty("CheckType", 0);
        }

        fr.setProperty("StringForPrinting", "ИНН " + A.GETVALS("CONFIG.INN"));
        fr.invoke("PrintString");

        fr.setProperty("StringForPrinting", FF.DATETIMES());
        fr.invoke("PrintString");

        if (isVozvr) {
            fr.setProperty("StringForPrinting", "ВОЗВРАТ");
        } else {
            fr.setProperty("StringForPrinting", "ПРОДАЖА");
        }
        fr.invoke("PrintString");

        // проставляем свой номер чека:
        fr.setProperty("StringForPrinting", "Номер док. продажи: " + check_id);
        fr.invoke("PrintString");

        int garant = 30;
        if (dk_inf != null && !dk_inf.equals("") && 1 < 0) {
            P.SQLEXECT("select nvl(get_dk_scores_garan('" + dk_inf + "'),30) DAYCOUNT from dual", "GARANT");
            garant = (int) Double.parseDouble(A.GETVALS("GARANT.DAYCOUNT"));
        }
        //некондиция
        P.SQLEXECT("select a.procent,a.scan from d_kassa_cur a"
                + " where a.procent > 0", "nekondChekPr");
        if (A.RECCOUNT("nekondChekPr") != 0) {
            String strbuf = "Гарантия: " + garant + " дней, не распространяется на дефект";
            for (int ni = 0; ni < 2; ni++) {
                if (ni != 1) {
                    fr.setProperty("StringForPrinting", strbuf.substring(CHAR_COUNT_IN_CHECK * ni, CHAR_COUNT_IN_CHECK * (ni + 1)));
                    fr.invoke("PrintString");
                } else {
                    fr.setProperty("StringForPrinting", strbuf.substring(CHAR_COUNT_IN_CHECK * ni));
                    fr.invoke("PrintString");
                }
            }
        } else {
            fr.setProperty("StringForPrinting", "Гарантия: " + garant + " дней");
            fr.invoke("PrintString");
        }

        int ink = 0;
        int inkVozvr = 0;
        if (isVozvr) {
            P.SQLEXECT("select nvl(min(id_cur),1) id_cur from d_return_cur where kass_id = '" + P.getUniqueKassNo() + "'", "CUR_RET");
            inkVozvr = (int) Double.parseDouble(A.GETVALS("CUR_RET.ID_CUR"));
        }
        double scoreplus = 0;
        double scoreminus = 0;
        double itog = 0;
        fr.setProperty("StringForPrinting", "***");
        fr.invoke("PrintString");
        for (ChekPosition cp : chekPos) {
            itog += ((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000;
            ink++;
            inkVozvr++;
            String product = cp.productName;
            String productPeace = "";
            boolean findSpace = false;
            while (product.length() != 0) {
                if (product.length() > CHAR_COUNT_IN_CHECK) {
                    for (int charNo = product.substring(0, CHAR_COUNT_IN_CHECK).length() - 2; charNo > CHAR_COUNT_IN_CHECK / 2; charNo--) { // ищу пробел с которого можно сделать перенос
                        if (product.substring(charNo, charNo + 1).equals(" ")) {
                            productPeace = product.substring(0, charNo);
                            product = product.substring(charNo);
                            findSpace = true;
                            break;
                        }
                    }
                    if (!findSpace) {
                        productPeace = product.substring(0, CHAR_COUNT_IN_CHECK);
                        product = product.substring(CHAR_COUNT_IN_CHECK);
                    }
                } else {
                    productPeace = product;
                    product = "";
                }
                fr.setProperty("StringForPrinting", productPeace);
                System.out.println("PrintString: " + fr.invoke("PrintString"));
            }

            fr.setProperty("StringForPrinting", cp.quantity + " X " + cp.price + " = " + cp.price * cp.quantity);
            fr.invoke("PrintString");

            //некондиция
            P.SQLEXECT("select a.procent,a.scan from d_kassa_cur a"
                    + " where a.kass_id = '" + P.getUniqueKassNo() + "' and a.posnr = " + ink, "nekondChek");
            if (!A.GETVALS("nekondChek.procent").equals("0") && !A.GETVALS("nekondChek.procent").equals("0.0")) {
                P.SQLEXECT("select 'Код дефекта: '||vid_brak vid_brak, 'Дефект: '||brak_name brak_name, ceil(length('Дефект: '||brak_name)/" + CHAR_COUNT_IN_CHECK + ") leng,'brak' tip \n"
                        + "from pos_brak where scan = '" + A.GETVALS("nekondChek.scan") + "'\n"
                        + "union all\n"
                        + "select 'Код дефекта: '||b.vid_brak vid_brak, 'Дефект: '||b.brak_name brak_name, ceil(length('Дефект: '||b.brak_name)/" + CHAR_COUNT_IN_CHECK + ") leng,'nek' tip\n"
                        + "from s_label_v a\n"
                        + "left join st_brak_uc b on nvl(a.code_def,'0') = b.vid_brak\n"
                        + "where a.scan = '" + A.GETVALS("nekondChek.scan") + "' and a.procent > 0", "POS_BRAK_ALL_CHEK");
                fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.vid_brak"));
                fr.invoke("PrintString");
                if (A.GETVALS("pos_brak_all_chek.brak_name").length() > CHAR_COUNT_IN_CHECK) {
                    for (int ni = 0; ni < (int) Double.parseDouble(A.GETVALS("pos_brak_all_chek.leng")); ni++) {
                        if (ni != (int) Double.parseDouble(A.GETVALS("pos_brak_all_chek.leng")) - 1) {
                            fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.brak_name").substring(CHAR_COUNT_IN_CHECK * ni, CHAR_COUNT_IN_CHECK * (ni + 1)));
                            fr.invoke("PrintString");
                        } else {
                            fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.brak_name").substring(CHAR_COUNT_IN_CHECK * ni));
                            fr.invoke("PrintString");
                        }
                    }
                } else {
                    fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.brak_name"));
                    fr.invoke("PrintString");
                }
                if (A.GETVALS("POS_BRAK_ALL_CHEK.TIP").equals("brak")) {
                    P.SQLEXECT("select nvl(max(d.discount_sum - (cena2p(sysdate,b.art,0,1) - cena2s(sysdate,b.art,0,1))),0) neksum from pos_sale4 d inner join pos_sale2 b on d.id_sale = b.id_sale where b.id_chek = " + check_id + " and d.disc_type_id = 'N000000001'", "sale4neksum");
                    fr.setProperty("StringForPrinting", "Скидка по дефекту: " + Double.parseDouble(A.GETVALS("sale4neksum.neksum")) + "(" + A.GETVALS("nekondChek.procent") + "%" + ")");
                    fr.invoke("PrintString");
//                fr.setProperty("StringForPrinting", "Цена со скидкой по дефекту: " + (((double) Math.round((cp.price * cp.quantity - Double.parseDouble(A.GETVALS("sale4neksum.neksum"))) * 100000)) / 100000));
//                fr.invoke("PrintString");
                    if (cp.disc - Double.parseDouble(A.GETVALS("sale4neksum.neksum")) > 0) {
                        fr.setProperty("StringForPrinting", "Скидка: " + (((double) Math.round((cp.disc - Double.parseDouble(A.GETVALS("sale4neksum.neksum"))) * 100000)) / 100000));
                        fr.invoke("PrintString");
                        fr.setProperty("StringForPrinting", "Цена со скидкой: " + (((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000));
                        fr.invoke("PrintString");
                    }
                } else {
                    if (cp.disc != 0) {
                        fr.setProperty("StringForPrinting", "Скидка: " + cp.disc);
                        fr.invoke("PrintString");
                        fr.setProperty("StringForPrinting", "Цена со скидкой: " + (((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000));
                        fr.invoke("PrintString");
                    }
                }
            } else {
                if (cp.disc != 0) {
                    fr.setProperty("StringForPrinting", "Скидка: " + cp.disc);
                    fr.invoke("PrintString");
                    fr.setProperty("StringForPrinting", "Цена со скидкой: " + (((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000));
                    fr.invoke("PrintString");
                }
            }

            if (dk_inf != null && !dk_inf.equals("")) {
                P.SQLEXECT("select nvl(max(dk_type),' ') dk_type from st_dk where id_dk = '" + dk_inf + "'", "DK_INF");
                if (A.GETVALS("DK_INF.DK_TYPE").equals("B")) {
                    P.SQLEXECT("select * from d_kassa_disc_cur b\n"
                            + "inner join d_kassa_cur a on a.posnr = b.posnr and a.kass_id = b.kass_id\n"
                            + "where b.kass_id = '" + P.getUniqueKassNo() + "' and b.posnr = " + ink + " and discount_type_id = 'A000000001' and b.discount = 0 and b.discount_sum != 0", "DISC_CUR");
                    P.SQLEXECT("select get_dk_scores_sale('" + dk_inf + "'," + (((double) Math.round(cp.price - cp.disc) * 100000) / 100000) + "," + cp.quantity + ") SCPLUS from dual", "GET_DK_SCORES_SALE");
                    if (!isVozvr) {
//                        if (A.RECCOUNT("DISC_CUR") == 0) {
                        scoreplus += Double.parseDouble(A.GETVALS("GET_DK_SCORES_SALE.SCPLUS"));
//                        }
                    } else {
                        P.SQLEXECT("select id_sale from d_return_cur where kass_id = '" + P.getUniqueKassNo() + "' and id_cur = " + (inkVozvr - 1), "VOZVR_SALE_SALE");
                        P.SQLEXECT("select nvl(sum(dk_sum),0) dk_sum from pos_dk_scores where id_sale = '" + A.GETVALS("VOZVR_SALE_SALE.ID_SALE") + "' and dk_sum > 0 and doc_type in ('ck')", "DK4SCORES");
                        scoreminus += Double.parseDouble(A.GETVALS("DK4SCORES.dk_sum"));
                    }
                }
            }
            fr.setProperty("StringForPrinting", "***");
            fr.invoke("PrintString");
        }

        fr.setProperty("StringForPrinting", "Продавец: ");
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", seler);
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", "------------------------------------");
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", "");
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", "Подпись");
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", "");
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", "------------------------------------");
        fr.invoke("PrintString");

        if (dk_inf != null && !dk_inf.equals("")) {
            fr.setProperty("StringForPrinting", "Дисконтная карта: " + dk_inf);
            fr.invoke("PrintString");

            P.SQLEXECT("select fio from st_dk where id_dk = '" + dk_inf + "'", "curDKfio");
            if (A.RECCOUNT("curDKfio") != 0) {
                A.SELECT("curDKfio");
                /*           fr.setProperty("StringForPrinting", "Владелец карты: " + (A.GETVALS("FIO").length()+"Владелец карты: ".length() > CHAR_COUNT_IN_CHECK
                 ? A.GETVALS("FIO").substring(0, CHAR_COUNT_IN_CHECK-"Владелец карты: ".length()) + "\n" 
                 + A.GETVALS("FIO").substring(CHAR_COUNT_IN_CHECK-"Владелец карты: ".length())
                 : A.GETVALS("FIO")));
                 fr.invoke("PrintString");*/
                if (A.GETVALS("FIO").length() + "Владелец карты: ".length() > CHAR_COUNT_IN_CHECK) {
                    fr.setProperty("StringForPrinting", "Владелец карты: " + A.GETVALS("FIO").substring(0, CHAR_COUNT_IN_CHECK - "Владелец карты: ".length()));
                    fr.invoke("PrintString");
                    fr.setProperty("StringForPrinting", A.GETVALS("FIO").substring(CHAR_COUNT_IN_CHECK - "Владелец карты: ".length()));
                    fr.invoke("PrintString");
                } else {
                    fr.setProperty("StringForPrinting", A.GETVALS("FIO"));
                    fr.invoke("PrintString");
                }
                P.SQLEXECT("select nvl(max(dk_type),' ') dk_type from st_dk where id_dk = '" + dk_inf + "'", "DK_INF");
                if (A.GETVALS("DK_INF.DK_TYPE").equals("B")) {
                    if (!isVozvr) {
                        P.SQLEXECT("select nvl(sum(d.discount_sum*b.kol),0) SCMINUS from pos_sale4 d inner join pos_sale2 b on b.id_sale = d.id_sale where d.disc_type_id = 'A000000001' and b.id_chek = " + check_id, "SALE4SCORES");
                        scoreminus = Double.parseDouble(A.GETVALS("SALE4SCORES.SCMINUS"));
                    } else {
                        P.SQLEXECT("select distinct id_chek_vozvr from pos_sale2 where id_chek = " + check_id, "VOZVR_SALE_CHEK");
                        P.SQLEXECT("select nvl(sum(d.discount_sum*b.kol),0) SCMINUS from pos_sale4 d inner join pos_sale2 b on b.id_sale = d.id_sale where d.disc_type_id = 'A000000001' and b.id_chek = " + A.GETVALS("VOZVR_SALE_CHEK.ID_CHEK_VOZVR"), "SALE4SCORES");
                        scoreplus = Double.parseDouble(A.GETVALS("SALE4SCORES.SCMINUS"));
                    }
                    if (scoreminus != 0) {
                        fr.setProperty("StringForPrinting", "Списано баллов: " + scoreminus);
                        fr.invoke("PrintString");
                    }
                    if (scoreplus != 0) {
                        fr.setProperty("StringForPrinting", "Начислено баллов: " + scoreplus);
                        fr.invoke("PrintString");
                    }
                }
            }
        }

        if (price_disc != 0) {
//            fr.setProperty("Summ1", new Variant(price_disc));
            fr.setProperty("StringForPrinting", "СКИДКА = " + price_disc);
            fr.invoke("PrintString");
//            fr.invoke("Discount");
//            fr.setProperty("Summ1", 0);
            viewError = cheсkError();
        }

        P.SQLEXECT("select a.* from d_kassa_disc_cur a"
                + " inner join pos_skidki1 b on a.discount_type_id = b.docid"
                + " where b.disc_type_id = 'ZK10'", "curDkType");
        if (A.RECCOUNT("curDkType") != 0) {
            fr.setProperty("StringForPrinting", "АКЦИЯ: СКИДКА НА ВТОРУЮ ПАРУ");
            fr.invoke("PrintString");
        }
        A.CLOSE("curDkType");

        P.SQLEXECT("select a.* from d_kassa_disc_cur a"
                + " inner join pos_skidki1 b on a.discount_type_id = b.docid"
                + " where b.disc_type_id = 'ZK11'", "curDkType");
        if (A.RECCOUNT("curDkType") != 0) {
            fr.setProperty("StringForPrinting", "АКЦИЯ: ТРЕТЬЯ ПАРА В ПОДАРОК");
            fr.invoke("PrintString");
        }
        A.CLOSE("curDkType");

        fr.setProperty("StringForPrinting", "------------------------------------");
        fr.invoke("PrintString");

//        String priceType = "Вид оплаты:";
        if (price_cash != 0) {
            fr.setProperty("Summ1", new Variant(price_cash));
            fr.setProperty("StringForPrinting", "Наличными: " + price_cash);
            fr.invoke("PrintString");
            //System.out.println("Oplata nal: " + price_cash);
        }
        if (price_card != 0) {
            fr.setProperty("Summ2", new Variant(price_card));
            fr.setProperty("StringForPrinting", "Картой: " + price_card);
            fr.invoke("PrintString");
            //System.oudt.println("Oplata card: " + price_card);
        }
        if (price_sert != 0) {
            fr.setProperty("Summ3", new Variant(price_sert));
            fr.setProperty("StringForPrinting", "Сертификат: " + price_sert);
            fr.invoke("PrintString");
            // System.out.println("Oplata sert: " + price_sert);
        }
        if (price_cred != 0) {
            fr.setProperty("Summ4", new Variant(price_cred));
            fr.setProperty("StringForPrinting", "Потреб.кредит: " + price_cred);
            fr.invoke("PrintString");
            //System.out.println("Oplata cred: " + price_cred);
        }

        fr.setProperty("StringForPrinting", "");
        fr.invoke("PrintString");

        fr.setProperty("StringForPrinting", "ИТОГ = " + itog);
        fr.invoke("PrintString");

        if ((((double) Math.round((price_cash + price_card + price_sert + price_cred) - itog) * 100000) / 100000) > 0) {
            fr.setProperty("StringForPrinting", "Сдача = " + ((double) Math.round((price_cash + price_card + price_sert + price_cred) - itog) * 100000) / 100000);
            fr.invoke("PrintString");
        }

        fr.setProperty("StringForPrinting", "");
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", "");
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", "");
        fr.invoke("PrintString");

        P.SQLEXECT("select substr(shopnum,2) shopnum,cityname,address,nvl(get_system_val('NULL_FISKAL_PHONE'),tel_number) tel_number from s_shop where shopid = '" + V.SHOP_ID + "'", "ZSHOP");
        P.SQLEXECT("select nvl(get_system_val('NULL_FISKAL_CLICHE'),'" + "     ОТД. " + A.GETVALS("ZSHOP.SHOPNUM") + " СООО БЕЛВЕСТ" + "') cliche from dual", "ZCLICHE");
        fr.setProperty("StringForPrinting", A.GETVALS("ZCLICHE.cliche"));
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", A.GETVALS("ZSHOP.SHOPNUM") + " " + A.GETVALS("ZSHOP.cityname"));
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", A.GETVALS("ZSHOP.address"));
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", A.GETVALS("ZSHOP.tel_number"));
        fr.invoke("PrintString");
//        fr.setProperty("StringForPrinting", "        ДОБРО ПОЖАЛОВАТЬ!");
//        fr.invoke("PrintString");

//        fr.invoke("PrintCliche");
        fr.invoke("CutCheck");

        return true;
    }

    public boolean printText(String checkText) {
        if (!connect()) {
            return false;
        }

        // проверка на состояние ФР (готовность к печати чека)
        int frStatus = getStatus();
        int frAdvStatus = getAdvStatus();
        if ((frStatus != 2 && frStatus != 4 && frStatus != 3) || frAdvStatus != 0) {
            P.MESS("Печать невозможна! \n Режим фискального регистратора: " + getStatusDescription() + " \n Подрежим: " + getAdvStatusDescription());
            return false;
        }

        ArrayList<String> arrStr = new ArrayList<>(Arrays.asList(checkText.split("\n")));

        for (int i = 0; i < arrStr.size(); i++) {
            fr.setProperty("StringForPrinting", arrStr.get(i));
            fr.invoke("PrintString");
        }

//        for (int i = 0; i < 5; i++) {
//            fr.setProperty("StringForPrinting", "");
//            fr.invoke("PrintString");
//        }
//        fr.invoke("PrintCliche");
        fr.invoke("CutCheck");

        return true;

    }

    @Override
    public boolean annulCheck(String tip, String id_chek, String fr_id_chek, double chekSum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getTaxReport() {
        viewError = false;
        if (fr.invoke("PrintTaxReport").getInt() == 0) {;
            if (V.CUT_PAPER_CHECK) {
                waitPrint();
            }
            return true;
        } else {
            viewError = cheсkError();
            P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка печати отчета по налогам!");
            return false;
        }
    }

    @Override
    public boolean startSession() {
        int fErr = 0;
        // проверка на состояние ФР
        int frStatus = getStatus();
        if (frStatus == 0) {
            P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка ФР!");
            return false;
        }
        if (frStatus == 4) {//если закрыта
            P.SQLEXECT(innQuery, "UD");
            P.DOVIEW("UD", "Укажите кассира для открытия смены", "Номер", "Ф.И.О. ", "Табельный", "ИНН", "NOMER", "FIO", "TABEL", "INN", 100, 310, 100, 100);
            if (V.PARAMOT != null && V.PARAMOT[0].equals("T")) {
                V.CASHIER_INN = A.GETVALS("UD.INN");

                if (V.CASHIER_INN.equals("") || V.CASHIER_INN.equals(" ")) {
                    P.MESSERR("Для выбранного кассира не найден ИНН в базе!");
                    return false;
                }

                //ставлю кассира в таблицу кассы равного текущему продавецу
                fr.setProperty("TableNumber", 2);
                fr.setProperty("RowNumber", 30);
                fr.setProperty("FieldNumber", 2);
                fr.setProperty("ValueOfFieldString", A.GETVALS("UD.FIO"));
                doWriteFRTable();
                //
            } else {
                P.MESS("Не выбран кассир для открытия смены!");
                return false;
            }

            fErr = fr.invoke("FNBeginOpenSession").toInt();
            if (fErr != 0) {
                viewError = cheсkError();
                P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка открытия смены");
                return false;
            }

            if (!setSellerINN("last")) {
                return false;
            }

            fErr = fr.invoke("OpenSession").toInt();
            if (fErr != 0) {
                viewError = cheсkError();
                P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка открытия смены");
                return false;
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FiscalPrinter2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        sessionOpen = true;
        return true;
    }

    @Override
    public boolean startCheck() {
        return true;
    }

    @Override
    public boolean testCheck() {
        return true;
    }

    @Override
    public boolean printDuplicate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean printCorrectionCheck(int taxType, int correctionType, int calculationSign, double summCheck,
            double summNal, double summElectr, double summNds18, double SummNds10) {
        if (!sessionOpen) {
            if (!startSession()) {
                return false;
            }
        }

        int fErr = 0;
        viewError = false;
        fr.setProperty("TaxType", taxType);
        fr.setProperty("CorrectionType", correctionType);
        fr.setProperty("CalculationSign", calculationSign);
        fr.setProperty("Summ1", new Variant(summCheck));
        fr.setProperty("Summ2", new Variant(summNal));
        fr.setProperty("Summ3", new Variant(summElectr));
        fr.setProperty("Summ7", new Variant(summNds18));
        fr.setProperty("Summ8", new Variant(SummNds10));
        fErr = fr.invoke("FNBeginCorrectionReceipt").toInt();
        if (fErr != 0) {
            viewError = cheсkError();
            P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка фискального регистратора");
            return false;
        }

        if (!setSellerINN("select")) {
            return false;
        }

        fErr = fr.invoke("FNBuildCorrectionReceipt2").toInt();
        if (fErr != 0) {
            viewError = cheсkError();
            P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка фискального регистратора");
            return false;
        }
        return true;
    }

    @Override
    public int getTaxType() {
        int fErr = 0;
        fr.setProperty("TableNumber", 18);
        fr.setProperty("RowNumber", 1);
        fr.setProperty("FieldNumber", 5);
        fr.invoke("GetFieldStruct");
        fr.invoke("ReadTable");
        return fr.getProperty("ValueOfFieldInteger").toInt();
    }

    @Override
    public String getOFDLastDate() {
        int fErr = 0;
        fr.setProperty("TableNumber", 20);
        fr.setProperty("RowNumber", 1);
        fr.setProperty("FieldNumber", 5);
        fr.invoke("GetFieldStruct");
        fr.invoke("ReadTable");
        return fr.getProperty("ValueOfFieldString").toString();
    }

    @Override
    public boolean printChekFN(int check_id, ArrayList<ChekPosition> chekPos, boolean isVozvr,
            double price_cash, double price_card, double price_sert, double price_disc, String dk_inf, String seler, double price_cred, String trigger) {

        System.out.println("Start printCheck");

        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'ONLINE_KASSA_RF'", "ONLINE_KASSA_RF");
        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'ONLINE_KASSA_RF_DISC'", "ONLINE_KASSA_RF_DISC");
        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'ONLINE_KASSA_RF_KASSIR'", "ONLINE_KASSA_RF_KASSIR");
        P.SQLEXECT("select nvl(max(to_number(sys_val)),1) sys_val from ri_system where sys_var = 'ONLINE_KASSA_RF_FN_ITEM'", "ONLINE_KASSA_RF_FN_ITEM");

        viewError = false;

        if (!connect()) {
            return false;
        }

        if (!sessionOpen) {
            if (!startSession()) {
                return false;
            }
        }

        System.out.println("Connected printCheck");

        // проверка на состояние ФР (готовность к печати чека)
        int frStatus = getStatus();
        int frAdvStatus = getAdvStatus();
        if ((frStatus != 2 && frStatus != 4) || frAdvStatus != 0) {
            P.MESS("Печать невозможна! \n Режим фискального регистратора: " + getStatusDescription() + " \n Подрежим: " + getAdvStatusDescription());
            return false;
        }

        System.out.println("Connected printCheck2");

        //ставлю кассира в таблицу кассы равного текущему продавецу
        fr.setProperty("TableNumber", 2);
        fr.setProperty("RowNumber", 30);
        fr.setProperty("FieldNumber", 2);
        fr.setProperty("ValueOfFieldString", seler);
        doWriteFRTable();
        //

        //ставлю систему налогооблажения
        P.SQLEXECT("select nvl(max(sys_val),'F') sys_val, case when to_char(sysdate,'yyyymmdd') >= '20200701' then 'T' else 'F' end dated\n"
                + "from ri_system where sys_var = 'NDS_IN_CHECK'", "NDS_CHECK");
        fr.setProperty("TableNumber", 18);
        fr.setProperty("RowNumber", 1);
        fr.setProperty("FieldNumber", 5);
        if (A.GETVALS("NDS_CHECK.DATED").equals("F") && A.GETVALS("NDS_CHECK.SYS_VAL").equals("F")) {
            fr.setProperty("ValueOfFieldInteger", 8);
        } else {
            fr.setProperty("ValueOfFieldInteger", 1);
        }
        doWriteFRTable();
        //

        int openCheck = 0;
        int sale = 0;
        int closeCheck = 0;

        fr.setProperty("Summ1", 0);
        fr.setProperty("Summ2", 0);
        fr.setProperty("Summ3", 0);
        fr.setProperty("Summ4", 0);

        fr.setProperty("PaymentTypeSign", 4);
        fr.setProperty("PaymentItemSign", (int) Double.parseDouble(A.GETVALS("ONLINE_KASSA_RF_FN_ITEM.SYS_VAL")));

        if (isVozvr) {
            if (price_cash > getCashReg()) {
                P.MESSERR("Не хватает наличности в кассе для возврата!");
                return false;
            }
            fr.setProperty("CheckType", 2);
        } else {
            fr.setProperty("CheckType", 1);
        }

        //тег кассира
        if (A.GETVALS("ONLINE_KASSA_RF.SYS_VAL").equals("T") && A.GETVALS("ONLINE_KASSA_RF_KASSIR.SYS_VAL").equals("T")) {
            fr.setProperty("TagNumber", new Variant(1021));
            fr.setProperty("TagType", new Variant(7));
            fr.setProperty("TagValueStr", new Variant(seler));
            fr.setProperty("TagValueLength", new Variant(64));
            fr.invoke("FNSendTag");
        }

//        fr.setProperty("CustomerEmail", "andrey.kuzmenkov@belwest.com");
//        fr.invoke("FNSendCustomerEmail");
        // проставляем свой номер чека:
        fr.setProperty("StringForPrinting", "Номер док. продажи: " + check_id);
        fr.invoke("PrintString");

        int garant = 30;
        if (dk_inf != null && !dk_inf.equals("") && 1 < 0) {
            P.SQLEXECT("select nvl(get_dk_scores_garan('" + dk_inf + "'),30) DAYCOUNT from dual", "GARANT");
            garant = (int) Double.parseDouble(A.GETVALS("GARANT.DAYCOUNT"));
        }
        //некондиция
        P.SQLEXECT("select a.procent,a.scan from d_kassa_cur a"
                + " where a.procent > 0", "nekondChekPr");
        if (A.RECCOUNT("nekondChekPr") != 0) {
            String strbuf = "Гарантия: " + garant + " дней, не распространяется на дефект";
            for (int ni = 0; ni < 2; ni++) {
                if (ni != 1) {
                    fr.setProperty("StringForPrinting", strbuf.substring(CHAR_COUNT_IN_CHECK * ni, CHAR_COUNT_IN_CHECK * (ni + 1)));
                    fr.invoke("PrintString");
                } else {
                    fr.setProperty("StringForPrinting", strbuf.substring(CHAR_COUNT_IN_CHECK * ni));
                    fr.invoke("PrintString");
                }
            }
        } else {
            fr.setProperty("StringForPrinting", "Гарантия: на обувь - " + garant + " дней");
            fr.invoke("PrintString");
            fr.setProperty("StringForPrinting", "          на сумки - 50 дней");
            fr.invoke("PrintString");
        }

        int ink = 0;
        int inkVozvr = 0;
        if (isVozvr) {
            P.SQLEXECT("select nvl(min(id_cur),1) id_cur from d_return_cur where kass_id = '" + P.getUniqueKassNo() + "'", "CUR_RET");
            inkVozvr = (int) Double.parseDouble(A.GETVALS("CUR_RET.ID_CUR"));
        }
        double scoreplus = 0;
        double scoreminus = 0;
        System.out.println("Before ChekPosition printCheck");

        //док возврат
        if (trigger.equals("DRET_RU")) {
            A.GOTOP("G_PRIX2");
        }

        for (ChekPosition cp : chekPos) {
            ink++;
            inkVozvr++;
            String product = cp.productName;
            String productPeace = "";
            boolean findSpace = false;
            int ind = 0;
            String productCh = "";
            while (product.length() != 0) {
                if (product.length() > CHAR_COUNT_IN_CHECK) {
                    for (int charNo = product.substring(0, CHAR_COUNT_IN_CHECK).length() - 2; charNo > CHAR_COUNT_IN_CHECK / 2; charNo--) { // ищу пробел с которого можно сделать перенос
                        if (product.substring(charNo, charNo + 1).equals(" ")) {
                            productPeace = product.substring(0, charNo);
                            product = product.substring(charNo);
                            findSpace = true;
                            break;
                        }
                    }
                    if (!findSpace) {
                        productPeace = product.substring(0, CHAR_COUNT_IN_CHECK);
                        product = product.substring(CHAR_COUNT_IN_CHECK);
                    }
                } else {
                    productPeace = product;
                    product = "";
                }
                if (ind == 0) {
                    productCh = productPeace;
                }
                fr.setProperty("StringForPrinting", productPeace);
                //System.out.println("PrintString: " + fr.invoke("PrintString"));
                ind++;
            }

            fr.setProperty("StringForPrinting", productCh);
            fr.setProperty("Quantity", cp.quantity);
            if (A.GETVALS("ONLINE_KASSA_RF.SYS_VAL").equals("T") && A.GETVALS("ONLINE_KASSA_RF_DISC.SYS_VAL").equals("F")) {
                fr.setProperty("Price", new Variant(((double) Math.round((cp.price - cp.disc / cp.quantity) * 100000)) / 100000));
            } else {
                fr.setProperty("Price", new Variant(cp.price));
            }

//            System.out.println("* NAME : " + cp.productName);
//            System.out.println("* KOL : " + cp.quantity);
//            System.out.println("* CENA : " + cp.price);
            fr.setProperty("Department", 1);
            fr.setProperty("Summ1Enabled", new Variant(false));
            fr.setProperty("TaxValueEnabled", new Variant(false));

            //26122017 KAA указание процента и суммы ндс
            P.SQLEXECT("select nvl(max(sys_val),'F') sys_val from ri_system where sys_var = 'NDS_IN_CHECK'", "NDS_CHECK");
            if (A.GETVALS("NDS_CHECK.SYS_VAL").equals("T")) {
                if (!isVozvr) {
                    P.SQLEXECT("select nvl(max(y.nds),0) nds\n"
                            + "from d_kassa_cur x\n"
                            + "left join s_nds y on x.art = y.art and x.asize = y.asize\n"
                            + "where x.kass_id = '" + P.getUniqueKassNo() + "' and x.posnr = " + ink, "NDS_POSITION");
                } else {
                    if (trigger.equals("DRET_RU")) {
                        P.SQLEXECT("select nvl(max(y.nds),0) nds\n"
                                + "from d_prixod2 x\n"
                                + "left join s_nds y on x.art = y.art and x.asize = y.asize\n"
                                + "where x.id = " + A.GETVALS("G_PRIX2.ID") + " and x.id_rec = " + A.GETVALS("G_PRIX2.ID_REC") + "", "NDS_POSITION");
                        A.GOTO("G_PRIX2", ink + 1);
                    } else {
                        P.SQLEXECT("select nvl(max(y.nds),0) nds \n"
                                + "from d_return_cur x\n"
                                + "left join s_nds y on x.art = y.art and x.asize = y.asize\n"
                                + "where x.kass_id = '" + P.getUniqueKassNo() + "'\n"
                                + "and x.id_sale = (select id_sale from d_return_cur where kass_id = '" + P.getUniqueKassNo() + "' and id_cur = " + (inkVozvr - 1) + ")", "NDS_POSITION");
                    }
                }
                if (Double.parseDouble(A.GETVALS("NDS_POSITION.NDS")) == 10) {
                    fr.setProperty("Tax1", 2);
                } else {
                    fr.setProperty("Tax1", 1);
                }
            }
            //

            sale = fr.invoke("FNOperation").toInt();
            writeFRDataToOra(check_id, "FNOperation", sale);
            viewError = cheсkError();
            if (sale != 0) {
                P.SQLUPDATE("call WRITE_ERROR_PROC('FNOperation', '" + ResultCode + "', '" + ResultCodeDescription + "', '" + sale + "', ' ', ' ')");

                P.MESSERR("Ошибка фискального регистратора\n" + fr.getProperty("ResultCode").toString() + ": " + fr.getProperty("ResultCodeDescription").toString());

                int fErr = 0;
                //аннулирование
                fErr = fr.invoke("CancelCheck").toInt();
                if (fErr != 0) {
                    viewError = cheсkError();
//                    P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка аннулирования чека");
                }

                fErr = fr.invoke("FNCancelDocument").toInt();
                if (fErr != 0) {
                    viewError = cheсkError();
//                    P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка аннулирования чека");
                }
                return false;
            }

            setProductCode(cp.art, cp.asize);

//            if (A.GETVALS("ONLINE_KASSA_RF.SYS_VAL").equals("T")) {
//                if (cp.disc != 0) {
//                    fr.setProperty("Summ1", new Variant(cp.disc));
//                    fr.setProperty("StringForPrinting", "");
//                    fr.invoke("Discount");
//                    fr.setProperty("Summ1", 0);
//                    viewError = cheсkError();
//                }
//            }
            //некондиция
            P.SQLEXECT("select a.procent,a.scan from d_kassa_cur a"
                    + " where a.kass_id = '" + P.getUniqueKassNo() + "' and a.posnr = " + ink, "nekondChek");
            if (!A.GETVALS("nekondChek.procent").equals("0") && !A.GETVALS("nekondChek.procent").equals("0.0")) {
                P.SQLEXECT("select 'Код дефекта: '||vid_brak vid_brak, 'Дефект: '||brak_name brak_name, ceil(length('Дефект: '||brak_name)/" + CHAR_COUNT_IN_CHECK + ") leng,'brak' tip \n"
                        + "from pos_brak where scan = '" + A.GETVALS("nekondChek.scan") + "'\n"
                        + "union all\n"
                        + "select 'Код дефекта: '||b.vid_brak vid_brak, 'Дефект: '||b.brak_name brak_name, ceil(length('Дефект: '||b.brak_name)/" + CHAR_COUNT_IN_CHECK + ") leng,'nek' tip\n"
                        + "from s_label_v a\n"
                        + "left join st_brak_uc b on nvl(a.code_def,'0') = b.vid_brak\n"
                        + "where a.scan = '" + A.GETVALS("nekondChek.scan") + "' and a.procent > 0", "POS_BRAK_ALL_CHEK");
                fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.vid_brak"));
                fr.invoke("PrintString");
                if (A.GETVALS("pos_brak_all_chek.brak_name").length() > CHAR_COUNT_IN_CHECK) {
                    for (int ni = 0; ni < (int) Double.parseDouble(A.GETVALS("pos_brak_all_chek.leng")); ni++) {
                        if (ni != (int) Double.parseDouble(A.GETVALS("pos_brak_all_chek.leng")) - 1) {
                            fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.brak_name").substring(CHAR_COUNT_IN_CHECK * ni, CHAR_COUNT_IN_CHECK * (ni + 1)));
                            fr.invoke("PrintString");
                        } else {
                            fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.brak_name").substring(CHAR_COUNT_IN_CHECK * ni));
                            fr.invoke("PrintString");
                        }
                    }
                } else {
                    fr.setProperty("StringForPrinting", A.GETVALS("pos_brak_all_chek.brak_name"));
                    fr.invoke("PrintString");
                }
                if (A.GETVALS("POS_BRAK_ALL_CHEK.TIP").equals("brak")) {
                    P.SQLEXECT("select nvl(max(d.discount_sum - (cena2p(sysdate,b.art,0,1) - cena2s(sysdate,b.art,0,1))),0) neksum from pos_sale4 d inner join pos_sale2 b on d.id_sale = b.id_sale where b.id_chek = " + check_id + " and d.disc_type_id = 'N000000001'", "sale4neksum");
                    fr.setProperty("StringForPrinting", "Скидка по дефекту: " + Double.parseDouble(A.GETVALS("sale4neksum.neksum")) + "(" + A.GETVALS("nekondChek.procent") + "%" + ")");
                    fr.invoke("PrintString");
//                fr.setProperty("StringForPrinting", "Цена со скидкой по дефекту: " + (((double) Math.round((cp.price * cp.quantity - Double.parseDouble(A.GETVALS("sale4neksum.neksum"))) * 100000)) / 100000));
//                fr.invoke("PrintString");
                    if (cp.disc - Double.parseDouble(A.GETVALS("sale4neksum.neksum")) > 0) {
                        fr.setProperty("StringForPrinting", "Скидка: " + (((double) Math.round((cp.disc - Double.parseDouble(A.GETVALS("sale4neksum.neksum"))) * 100000)) / 100000));
                        fr.invoke("PrintString");
                        fr.setProperty("StringForPrinting", "Цена со скидкой: " + (((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000));
                        fr.invoke("PrintString");
                    }
                } else {
                    if (cp.disc != 0) {
                        fr.setProperty("StringForPrinting", "Скидка: " + cp.disc);
                        fr.invoke("PrintString");
                        fr.setProperty("StringForPrinting", "Цена со скидкой: " + (((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000));
                        fr.invoke("PrintString");
                    }
                }
            } else {
                if (cp.disc != 0) {
                    fr.setProperty("StringForPrinting", "Скидка: " + cp.disc);
                    fr.invoke("PrintString");
                    fr.setProperty("StringForPrinting", "Цена со скидкой: " + (((double) Math.round((cp.price * cp.quantity - cp.disc) * 100000)) / 100000));
                    fr.invoke("PrintString");
                }
            }

            if (dk_inf != null && !dk_inf.equals("")) {
                P.SQLEXECT("select nvl(max(dk_type),' ') dk_type from st_dk where id_dk = '" + dk_inf + "'", "DK_INF");
                if (A.GETVALS("DK_INF.DK_TYPE").equals("B")) {
                    P.SQLEXECT("select * from d_kassa_disc_cur b\n"
                            + "inner join d_kassa_cur a on a.posnr = b.posnr and a.kass_id = b.kass_id\n"
                            + "where b.kass_id = '" + P.getUniqueKassNo() + "' and b.posnr = " + ink + " and discount_type_id = 'A000000001' and b.discount = 0 and b.discount_sum != 0", "DISC_CUR");
                    P.SQLEXECT("select get_dk_scores_sale('" + dk_inf + "'," + (((double) Math.round(cp.price - cp.disc) * 100000) / 100000) + "," + cp.quantity + ") SCPLUS from dual", "GET_DK_SCORES_SALE");
                    if (!isVozvr) {
//                        if (A.RECCOUNT("DISC_CUR") == 0) {
                        scoreplus += Double.parseDouble(A.GETVALS("GET_DK_SCORES_SALE.SCPLUS"));
//                        }
                    } else {
                        P.SQLEXECT("select id_sale from d_return_cur where kass_id = '" + P.getUniqueKassNo() + "' and id_cur = " + (inkVozvr - 1), "VOZVR_SALE_SALE");
                        P.SQLEXECT("select nvl(sum(dk_sum),0) dk_sum from pos_dk_scores where id_sale = '" + A.GETVALS("VOZVR_SALE_SALE.ID_SALE") + "' and dk_sum > 0 and doc_type in ('ck')", "DK4SCORES");
                        scoreminus += Double.parseDouble(A.GETVALS("DK4SCORES.dk_sum"));
                    }
                }
            }

            fr.setProperty("StringForPrinting", " ");
            fr.invoke("PrintString");
        }
        System.out.println("After ChekPosition printCheck");

        fr.setProperty("StringForPrinting", "Продавец: ");
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", seler);
        fr.invoke("PrintString");
        fr.setProperty("StringForPrinting", "------------------------------------");
        fr.invoke("PrintString");

        if (dk_inf != null && !dk_inf.equals("")) {
            fr.setProperty("StringForPrinting", "Дисконтная карта: " + dk_inf);
            fr.invoke("PrintString");

            P.SQLEXECT("select fio from st_dk where id_dk = '" + dk_inf + "'", "curDKfio");
            if (A.RECCOUNT("curDKfio") != 0) {
                A.SELECT("curDKfio");
                /*           fr.setProperty("StringForPrinting", "Владелец карты: " + (A.GETVALS("FIO").length()+"Владелец карты: ".length() > CHAR_COUNT_IN_CHECK
                 ? A.GETVALS("FIO").substring(0, CHAR_COUNT_IN_CHECK-"Владелец карты: ".length()) + "\n" 
                 + A.GETVALS("FIO").substring(CHAR_COUNT_IN_CHECK-"Владелец карты: ".length())
                 : A.GETVALS("FIO")));
                 fr.invoke("PrintString");*/
                if (A.GETVALS("FIO").length() + "Владелец карты: ".length() > CHAR_COUNT_IN_CHECK) {
                    fr.setProperty("StringForPrinting", "Владелец карты: " + A.GETVALS("FIO").substring(0, CHAR_COUNT_IN_CHECK - "Владелец карты: ".length()));
                    fr.invoke("PrintString");
                    fr.setProperty("StringForPrinting", A.GETVALS("FIO").substring(CHAR_COUNT_IN_CHECK - "Владелец карты: ".length()));
                    fr.invoke("PrintString");
                } else {
                    fr.setProperty("StringForPrinting", A.GETVALS("FIO"));
                    fr.invoke("PrintString");
                }
                P.SQLEXECT("select nvl(max(dk_type),' ') dk_type from st_dk where id_dk = '" + dk_inf + "'", "DK_INF");
                if (A.GETVALS("DK_INF.DK_TYPE").equals("B")) {
                    if (!isVozvr) {
                        P.SQLEXECT("select nvl(sum(d.discount_sum*b.kol),0) SCMINUS from pos_sale4 d inner join pos_sale2 b on b.id_sale = d.id_sale where d.disc_type_id = 'A000000001' and b.id_chek = " + check_id, "SALE4SCORES");
                        scoreminus = Double.parseDouble(A.GETVALS("SALE4SCORES.SCMINUS"));
                    } else {
                        P.SQLEXECT("select distinct id_chek_vozvr from pos_sale2 where id_chek = " + check_id, "VOZVR_SALE_CHEK");
                        P.SQLEXECT("select nvl(sum(d.discount_sum*b.kol),0) SCMINUS from pos_sale4 d inner join pos_sale2 b on b.id_sale = d.id_sale where d.disc_type_id = 'A000000001' and b.id_chek = " + A.GETVALS("VOZVR_SALE_CHEK.ID_CHEK_VOZVR"), "SALE4SCORES");
                        scoreplus = Double.parseDouble(A.GETVALS("SALE4SCORES.SCMINUS"));
                    }
                    if (scoreminus != 0) {
                        fr.setProperty("StringForPrinting", "Списано баллов: " + scoreminus);
                        fr.invoke("PrintString");
                    }
                    if (scoreplus != 0) {
                        fr.setProperty("StringForPrinting", "Начислено баллов: " + scoreplus);
                        fr.invoke("PrintString");
                    }
                }
            }
        }

        System.out.println("Before Summ1 printCheck");
//        if (!A.GETVALS("ONLINE_KASSA_RF.SYS_VAL").equals("T")) {
//            if (price_disc != 0) {
//                fr.setProperty("Summ1", new Variant(price_disc));
//                fr.setProperty("StringForPrinting", "");
//                fr.invoke("Discount");
//                fr.setProperty("Summ1", 0);
//                viewError = cheсkError();
//            }
//        }

        P.SQLEXECT("select a.* from d_kassa_disc_cur a"
                + " inner join pos_skidki1 b on a.discount_type_id = b.docid"
                + " where b.disc_type_id = 'ZK10'", "curDkType");
        if (A.RECCOUNT("curDkType") != 0) {
            fr.setProperty("StringForPrinting", "АКЦИЯ: СКИДКА НА ВТОРУЮ ПАРУ");
            fr.invoke("PrintString");
        }
        A.CLOSE("curDkType");

        P.SQLEXECT("select a.* from d_kassa_disc_cur a"
                + " inner join pos_skidki1 b on a.discount_type_id = b.docid"
                + " where b.disc_type_id = 'ZK11'", "curDkType");
        if (A.RECCOUNT("curDkType") != 0) {
            fr.setProperty("StringForPrinting", "АКЦИЯ: ТРЕТЬЯ ПАРА В ПОДАРОК");
            fr.invoke("PrintString");
        }
        A.CLOSE("curDkType");

        if (price_cred != 0 && !isVozvr) {
            P.SQLEXECT("select nvl(sum(d.discount_sum),0) disc \n"
                    + "from pos_sale3 c\n"
                    + "inner join pos_sale2 b on c.id_chek = b.id_chek\n"
                    + "inner join pos_sale4 d on d.id_sale = b.id_sale\n"
                    + "where \n"
                    + "c.id_chek = " + check_id + " and \n"
                    + "c.vid_op_id = 4 and c.kred_id in (4) and disc_type_id like 'CR%'", "KRED_PRINT_CHECK");
            if (Double.parseDouble(A.GETVALS("KRED_PRINT_CHECK.DISC")) > 0) {
                fr.setProperty("StringForPrinting", "Сумма переплаты по рассрочке:");
//                fr.setProperty("StringForPrinting", "Скидка по рассрочке:");
                fr.invoke("PrintString");
                fr.setProperty("StringForPrinting", A.GETVALS("KRED_PRINT_CHECK.DISC") + " руб.");
                fr.invoke("PrintString");
                fr.setProperty("StringForPrinting", "------------------------------------");
                fr.invoke("PrintString");
            }
        }

        fr.setProperty("StringForPrinting", "------------------------------------");
        fr.invoke("PrintString");

        String priceType = "Вид оплаты:";

        if (price_cash != 0) {
            fr.setProperty("Summ1", new Variant(price_cash));
            priceType = priceType + " Наличными";
            //System.out.println("Oplata nal: " + price_cash);
        }
        if (price_card != 0) {
            fr.setProperty("Summ2", new Variant(price_card));
            priceType = priceType + " Картой";
            //System.oudt.println("Oplata card: " + price_card);
        }
        if (price_sert != 0) {
            fr.setProperty("Summ3", new Variant(price_sert));
            priceType = priceType + " Сертификат";
            // System.out.println("Oplata sert: " + price_sert);
        }
        if (price_cred != 0) {
            fr.setProperty("Summ4", new Variant(price_cred));
            priceType = priceType + " Потреб.кредит";
            //System.out.println("Oplata cred: " + price_cred);
        }
        fr.setProperty("StringForPrinting", priceType);
        fr.invoke("PrintString");

        fr.setProperty("StringForPrinting", "");
        System.out.println("Before Close printCheck");

        fr.setProperty("TaxType", getTaxType());

        P.SQLEXECT("select nvl(max(tabel),' ') tabel from pos_users where fio = '" + seler + "'", "CHECK_SELLER");
        System.out.println("select nvl(max(tabel),' ') tabel from pos_users where fio = '" + seler + "'");

        if (!setSellerINN(A.GETVALS("CHECK_SELLER.tabel"))) {
            return false;
        }

        closeCheck = fr.invoke("FNCloseCheckEx").toInt();
        System.out.println("After Close printCheck");
        writeFRDataToOra(check_id, "FNCloseCheckEx", closeCheck);
        viewError = cheсkError();

        if (closeCheck != 0 && !V.NULL_FISKAL.equals("T")) {
            P.MESSERR("При формировании чека возникла ошибка!\nКод ошибки: " + V.FP.getResultCode() + ".\n" + V.FP.getResultDescription());
        }
        System.out.println("Before CUT printCheck");
        if (V.CUT_PAPER_CHECK) { // если обработка обрыва ленты включена
            fr.invoke("GetShortECRStatus");
            System.out.println("ECR printCheck");
            if ((openCheck != 0 || sale != 0 || closeCheck != 0) && getAdvStatus() != 2) {
                System.out.println("ECR printCheck1");
                return false;
            }

            if (waitPrint()) { // если закончилась бумага, то аннулируем чек и печатаем по новой
                System.out.println("Wait ECR printCheck");
                fr.invoke("GetShortECRStatus");
                while (getAdvStatus() == 3) { // пока не перейдет в другой статус
                    fr.invoke("GetShortECRStatus");
                }
                fr.invoke("CancelCheck");
                while (fr.getProperty("ResultCode").getInt() == 80) { // пока ошибка: Идет печать пред. команды
                    //System.out.println(fr.invoke("GetShortECRStatus"));
                    fr.invoke("CancelCheck");
                };
                waitPrint();
//                return printChek(check_id, chekPos, isVozvr, price_cash, price_card, price_sert, price_disc, dk_inf, seler, price_cred);
                return false;
            }
        } else {
            System.out.println("ECR printCheck2");
            if ((openCheck != 0 || sale != 0 || closeCheck != 0)) {
                return false;
            }
        }
        System.out.println("After CUT printCheck");
        return true;
    }

    @Override
    public boolean setSellerINN(String tabno) {
        System.out.println("Enter setSellerINN");
        int fErr = 0;

        if (tabno.equals("select")) {
            P.SQLEXECT(innQuery, "UD");
            P.DOVIEW("UD", "Укажите кассира", "Номер", "Ф.И.О. ", "Табельный", "ИНН", "NOMER", "FIO", "TABEL", "INN", 100, 310, 100, 100);
            if (V.PARAMOT != null && V.PARAMOT[0].equals("T")) {
                V.CASHIER_INN = A.GETVALS("UD.INN");

                if (V.CASHIER_INN.equals("") || V.CASHIER_INN.equals(" ")) {
                    P.MESSERR("Для выбранного кассира не найден ИНН в базе!");

                    //аннулирование
                    fErr = fr.invoke("CancelCheck").toInt();
                    if (fErr != 0) {
                        viewError = cheсkError();
//                    P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка аннулирования чека");
                    }

                    fErr = fr.invoke("FNCancelDocument").toInt();
                    if (fErr != 0) {
                        viewError = cheсkError();
//                    P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка аннулирования чека");
                    }

                    return false;
                }

                //ставлю кассира в таблицу кассы равного текущему продавецу
                fr.setProperty("TableNumber", 2);
                fr.setProperty("RowNumber", 30);
                fr.setProperty("FieldNumber", 2);
                fr.setProperty("ValueOfFieldString", A.GETVALS("UD.FIO"));
                doWriteFRTable();
                //
            } else {
                P.MESS("Не выбран кассир!");

                //аннулирование
                fErr = fr.invoke("CancelCheck").toInt();
                if (fErr != 0) {
                    viewError = cheсkError();
//                    P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка аннулирования чека");
                }

                fErr = fr.invoke("FNCancelDocument").toInt();
                if (fErr != 0) {
                    viewError = cheсkError();
//                    P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка аннулирования чека");
                }

                return false;
            }
        }

        if (!tabno.equals("last") && !tabno.equals("select")) {
            P.SQLEXECT("select distinct inn\n"
                    + "from (select nvl(max(INN),' ') inn\n"
                    + "			from st_seller_inn\n"
                    + "			where tabno = '" + tabno + "'\n"
                    + "			union all\n"
                    + "			select max(nvl(inn,' ')) inn\n"
                    + "			from s_seller\n"
                    + "			where tabno = '" + tabno + "')\n"
                    + "order by inn desc", "CUR_SELLER");
            V.CASHIER_INN = A.GETVALS("CUR_SELLER.INN");
            System.out.println(tabno);
            System.out.println("select distinct inn\n"
                    + "from (select nvl(max(INN),' ') inn\n"
                    + "			from st_seller_inn\n"
                    + "			where tabno = '" + tabno + "'\n"
                    + "			union all\n"
                    + "			select max(nvl(inn,' ')) inn\n"
                    + "			from s_seller\n"
                    + "			where tabno = '" + tabno + "')\n"
                    + "order by inn desc");
            System.out.println(V.CASHIER_INN);
            System.out.println("end");
            if (V.CASHIER_INN.equals("") || V.CASHIER_INN.equals(" ")) {
                P.MESSERR("Для продавца не найден ИНН в базе!");

                //
                //аннулирование
                fErr = fr.invoke("CancelCheck").toInt();
                if (fErr != 0) {
                    viewError = cheсkError();
//                    P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка аннулирования чека");
                }

                fErr = fr.invoke("FNCancelDocument").toInt();
                if (fErr != 0) {
                    viewError = cheсkError();
//                    P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка аннулирования чека");
                }

                return false;
            }
        }

        //инн кассира
        fr.setProperty("TagNumber", new Variant(1203));
        fr.setProperty("TagType", new Variant(7));
        fr.setProperty("TagValueStr", new Variant(V.CASHIER_INN));
//        fr.setProperty("TagValueLength", new Variant(64));
        fErr = fr.invoke("FNSendTag").toInt();
        if (fErr != 0) {
            viewError = cheсkError();
            P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка установки ИНН кассира в чек!");
            return false;
        }
        return true;
    }

    @Override
    public boolean setProductCode(String art, String asize) {
        int fErr = 0;

        try {
            P.SQLEXECT("select case when substr(a.prodh,1,4) = '0001' then 'T' else 'F' end BIT_SEND,\n"
                    + "a.wstaw,substr('00000000000000',1,14-length(b.ean))||b.ean ean\n"
                    + "from s_art a\n"
                    + "inner join s_ean b on a.art = b.art\n"
                    + "where a.art = '" + art + "' and asize = " + asize, "KTN_CHECK");

            if (A.RECCOUNT("KTN_CHECK") == 0) {
//                P.MESS("Для артикула " + art + ", размера " + asize + " не найдено данных по коду товара!");
                P.SQLUPDATE("insert into history (BIT_STATUS,DATE_S1,PC_S,SQL_T,TIME_S,USER_L,USER_S) values ('T',sysdate,'EXC_IN_KTN','" + art + " - " + asize + " - " + "не найдено данных по коду товара!" + "',1,' ',' ')");
                return false;
            }

            if (A.GETVALS("KTN_CHECK.BIT_SEND").equals("T")) {

                fr.setProperty("MarkingType", new Variant(5408));
                fr.setProperty("GTIN", new Variant(A.GETVALS("KTN_CHECK.EAN")));
                fr.setProperty("SerialNumber", new Variant(A.GETVALS("KTN_CHECK.WSTAW")));
                fErr = fr.invoke("FNSendItemCodeData").toInt();
                if (fErr != 0) {
                    viewError = cheсkError();
//                    P.MESS(ResultCode + ": " + ResultCodeDescription, "Ошибка установки кода товара!");
                    P.SQLUPDATE("insert into history (BIT_STATUS,DATE_S1,PC_S,SQL_T,TIME_S,USER_L,USER_S) values ('T',sysdate,'EXC_IN_KTN','" + art + " - " + asize + " - " + "Ошибка установки кода товара!" + " - " + ResultCodeDescription.replaceAll("'", "''") + "',1,' ',' ')");
                    return false;
                }
                if (!A.GETVALS("KTN_CHECK.WSTAW").equals("") && !A.GETVALS("KTN_CHECK.WSTAW").equals(" ")) {
                    fr.setProperty("StringForPrinting", "Код товара:" + A.GETVALS("KTN_CHECK.WSTAW"));
                    fr.invoke("PrintString");
                }
            }
        } catch (Exception ex) {
            P.SQLUPDATE("insert into history (BIT_STATUS,DATE_S1,PC_S,SQL_T,TIME_S,USER_L,USER_S) values ('T',sysdate,'EXC_IN_KTN','" + art + " - " + asize + " - " + ex.toString().replaceAll("'", "''") + "',1,' ',' ')");
        }

        return true;
    }

    @Override
    public boolean printEan13(String code) {

        System.out.println("Start printEan13");

        viewError = false;

        if (!connect()) {
            return false;
        }

        System.out.println("Connected printEan13");;

        //ставлю кассира в таблицу кассы равного текущему продавецу
        fr.setProperty("BarCode", code);
        int fErr = fr.invoke("PrintBarCode").toInt();
        System.out.println(fErr);
        System.out.println("After printEan13");
        return true;
    }

}
