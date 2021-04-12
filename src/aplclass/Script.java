/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import baseclass.Cursorr;
import client.Post;
import prg.P;

/**
 *
 * @author Hit
 */
public class Script {

    public static void CREATE_TABLES() {

//        P.SQLUPDATE("create or replace TRIGGER \"D_RETURN_CUR_ID\" BEFORE INSERT ON D_RETURN_CUR FOR EACH ROW BEGIN SELECT D_RETURN_CUR_SEQ.NEXTVAL INTO :NEW.ID_CUR FROM DUAL; END;");
        CREATE_HISTORY();
        CREATE_VIEWS();

        Object[] record = (Object[]) P.SQLEXECT("SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME='RI_SCRIPT_STORE'").rowList.get(0);
        if ((double) record[0] == 0) {
            P.SQLUPDATE("CREATE TABLE RI_SCRIPT_STORE (NAME VARCHAR2(50 BYTE) NOT NULL ENABLE, SCRIPT VARCHAR2(4000 BYTE) NOT NULL ENABLE, COM_ID VARCHAR2(10 BYTE))");

            Post.getInstance().setParam("Query", "006");
            Post.getInstance().SendData();

        } else {
            record = (Object[]) P.SQLEXECT("SELECT COUNT(*) FROM RI_SCRIPT_STORE").rowList.get(0);
            if ((double) record[0] == 0) {

                Post.getInstance().setParam("Query", "006");
                Post.getInstance().SendData();

            }
        }

        Cursorr cursor = P.SQLEXECT("SELECT distinct RI_SCRIPT_STORE.NAME, RI_SCRIPT_STORE.COM_ID, RI_SCRIPT_STORE.SCRIPT FROM RI_SCRIPT_STORE left outer join ALL_TABLES on RI_SCRIPT_STORE.NAME = ALL_TABLES.TABLE_NAME where ALL_TABLES.TABLE_NAME is null or RI_SCRIPT_STORE.NAME = 'CONFIG'");
        Object[] table;

        int d_sap_odgruz_count = 0;

        for (int i = 0; i < cursor.ROWCOUNT; i++) {
            table = (Object[]) cursor.rowList.get(i);
            String tableName = (String) table[0];
            String command = (String) table[1];
            String script = (String) table[2];
//            record = (Object[]) P.SQLEXECT("SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME='" + tableName + "'").rowList.get(0);
            if (tableName.equals("CONFIG")) {
                record = (Object[]) P.SQLEXECT("SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME='" + tableName + "'").rowList.get(0);
                if ((double) record[0] == 0) {
                    P.SQLUPDATE(script);
                    P.DOFORM("ConfigImport");
                } else {
                    record = (Object[]) P.SQLEXECT("SELECT COUNT(*) FROM CONFIG").rowList.get(0);
                    if ((double) record[0] == 0) {
                        P.DOFORM("ConfigImport");
                    }
                }
            } else if (tableName.equals("USERS")) {
                P.SQLUPDATE(script);
            } else if (tableName.equals("POS_USERS")) {
                P.SQLUPDATE(script);
            } else if (tableName.equals("ST_DK")) {
                P.SQLUPDATE(script);
            } else if (tableName.equals("D_SAP_ODGRUZ1") || (tableName.equals("D_SAP_ODGRUZ2"))) {
                P.SQLUPDATE(script);
                d_sap_odgruz_count++;
            } else {
//                if ((int) record[0] == 0) {
                P.SQLUPDATE(script);
                Post.getInstance().setParam("Query", command);
                Post.getInstance().SendData();
//                }
            }
        }
        if (d_sap_odgruz_count == 2) {
            Post.getInstance().setParam("Query", "023");
            Post.getInstance().SendData();
        }
    }

    private static void CREATE_HISTORY() {
        Object[] record = (Object[]) P.SQLEXECT("SELECT COUNT(*) FROM ALL_TABLES WHERE TABLE_NAME='HISTORY'").rowList.get(0);
        if ((double) record[0] == 0) {
            P.SQLUPDATE("CREATE TABLE HISTORY (REL NUMBER(18,0) NOT NULL ENABLE, USER_S VARCHAR2(50 BYTE) NOT NULL ENABLE, PC_S VARCHAR2(50 BYTE) NOT NULL ENABLE, DATE_S1 TIMESTAMP (6) DEFAULT systimestamp NOT NULL ENABLE, TIME_S NUMBER(18,0) DEFAULT NULL NOT NULL ENABLE, SQL_T VARCHAR2(4000 BYTE) NOT NULL ENABLE, PROGRAMM VARCHAR2(250 BYTE), USER_L VARCHAR2(50 BYTE) NOT NULL ENABLE, BIT_STATUS VARCHAR2(1 BYTE) NOT NULL ENABLE, CONSTRAINT HISTORY_PK PRIMARY KEY (REL))");
            P.SQLUPDATE("COMMENT ON TABLE HISTORY  IS 'История запросов (служебный)'");
            P.SQLUPDATE("CREATE SEQUENCE  HISTORY_SEQ  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  ORDER  NOCYCLE");
            P.SQLUPDATE("CREATE OR REPLACE TRIGGER HISTORY_ID BEFORE INSERT ON HISTORY FOR EACH ROW BEGIN SELECT HISTORY_SEQ.NEXTVAL INTO :NEW.REL FROM DUAL; END;");
        }
    }

    private static void CREATE_VIEWS() {
        Object[] record = (Object[]) P.SQLEXECT("select count(object_id) FROM user_objects WHERE object_name = 'BASIC_COLUMN'").rowList.get(0);
        if ((double) record[0] == 0) {
            P.SQLUPDATE("CREATE OR REPLACE FORCE VIEW \"BASIC_COLUMN\" (\"NAMEO\", \"NAME\", \"TYPE\", \"LEN\", \"DEC\", \"DEF\", \"DESCR\", \"TYPEO\", \"COL\") AS SELECT a.table_name AS NAMEO,SUBSTR(a.column_name,1,50) as NAME,a.data_type AS TYPE, CASE WHEN DATA_PRECISION>0 THEN DATA_PRECISION ELSE DATA_LENGTH END as LEN,a.data_scale AS DEC,a.data_default AS DEF,SUBSTR(b.comments,1,50) AS DESCR ,'T' AS TYPEO,'ОРАКЛ' AS COL  FROM user_tab_columns A LEFT JOIN user_col_comments B  ON b.table_name = a.table_name AND b.column_name = a.column_name");
        }

        record = (Object[]) P.SQLEXECT("select count(object_id) FROM user_objects WHERE object_name = 'BASIC_TABLE'").rowList.get(0);
        if ((double) record[0] == 0) {
            P.SQLUPDATE("CREATE OR REPLACE FORCE VIEW \"BASIC_TABLE\" (\"NAME\", \"DESCR\") AS SELECT a.table_name as NAME,b.comments AS DESCR FROM user_tables A LEFT JOIN user_tab_comments B ON b.table_name = a.table_name");
        }
    }
}
