package baseclass;

import prg.A;
import prg.P;
import prg.V;

/**
 * Базовый класс формы с фильтрами
 *
 * @author stankevichpa
 */
public abstract class FormrWithFilters extends Formr {

    public FormrWithFilters() {
        super();
    }

    public FormrWithFilters(String name, String title, int width, int height) {
        super(name, title, width, height);
    }

    /**
     * Устанавливает фильтр для отчета по магазинам
     * @return 
     */
    public String shopFilter() {
        return this.shopFilter(null);
    }

    /**
     * Устанавливает фильтр для отчета по магазинам
     *
     * @param shopid - Примеры: 00%; 00F1.
     * @return
     */
    public String shopFilter(String shopid) {
        String result = null;
        StringBuilder select = new StringBuilder("SELECT 'F' AS BIT_SELE, SHOPNAME, SHOPID\n FROM st_shop\n WHERE org_kod ='FIL'\n");
        if (shopid != null && !shopid.isEmpty() && !"".equals(shopid)) {
            select.append("  AND shopid LIKE '").append(shopid).append("'\n");
        }
        select.append("ORDER BY shopid");
        P.SQLEXECT(select.toString(), "FIL_SEL");
        P.DOVIEW("FIL_SEL", "Выберите филиалы", "Выбор", "Наименование", "Код",
                "BIT_SELE", "SHOPNAME", "SHOPID", 100, 250, 100);
        if (V.PARAMOT != null && "T".equals(V.PARAMOT[0])) {
            A.SELECT("FIL_SEL");
            A.GOTOP("FIL_SEL");
            String buf = "";
            for (int i = 0; i < A.RECCOUNT("FIL_SEL"); i++) {
                if (A.GETVALS("BIT_SELE").equals("T")) {
                    buf += "'" + A.GETVALS("SHOPID") + "',";
                }
                A.GOTO("FIL_SEL", i + 2);
            }
            A.CLOSE("FIL_SEL");
            if (!"".equals(buf)) {
                buf = buf.substring(0, buf.length() - 1);
                String sele = "SELECT 'F' AS BIT_SELE,\n"
                        + "  a.shopname,\n"
                        + "  a.shopid\n"
                        + "FROM st_shop a\n"
                        + "INNER JOIN st_retail_hierarchy b ON a.shopid = b.shopid\n"
                        + "WHERE b.shopparent IN (" + buf + ")\n"
                        + "ORDER BY shopid";
                P.SQLEXECT(sele, "SHOP_SEL");
                P.DOVIEW("SHOP_SEL", "Выберите магазины",
                        "Выбор", "Наименование", "Код",
                        "BIT_SELE", "shopname", "shopid",
                        100, 250, 100);
                if (V.PARAMOT != null && "T".equals(V.PARAMOT[0])) {
                    A.SELECT("SHOP_SEL");
                    A.GOTOP("SHOP_SEL");
                    buf = "";
                    for (int i = 0; i < A.RECCOUNT("SHOP_SEL"); i++) {
                        if ("T".equals(A.GETVALS("BIT_SELE"))) {
                            buf += "'" + A.GETVALS("SHOPID") + "',";
                        }
                        A.GOTO("SHOP_SEL", i + 2);
                    }
                    A.CLOSE("SHOP_SEL");
                    if (!buf.equals("")) {
                        buf = buf.substring(0, buf.length() - 1);
                        result = buf;
                    }
                    A.CLOSE("SHOP_SEL");
                }
            }
            A.CLOSE("FIL_SEL");
        }
        return (result == null) ? "" : result;
    }
}
