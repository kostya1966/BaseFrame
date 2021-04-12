package prg;

import baseclass.Cursorr;
import baseclass.Formr;
import baseclass.Gridr;

/**
 * SET триггер, установка глобальных значений для проведения дополнительных
 * операций
 *
 * @author Kostya
 */
public class S {

    /**
     * установка активного грида для тулбара
     *
     * @param GRID
     */
    public static void SETACTIVEGRID(Gridr GRID) {
        V.ACTIVEGRID = GRID;
        if (GRID == null) {
//    System.out.print(" DEACTIVEGRID:"+"\n");            
        }

        if (GRID != null) {
            if (V.TOOLBAR != null) {
                V.TOOLBAR.SETENABLE(GRID);
            }
            GRID.FOCUS = 1;
//    System.out.print(" ACTIVEGRID:"+V.ACTIVEGRID.getName()+"\n");            
            V.ACTIVEFORM = GRID.THISFORM;
//     SETACTIVEFORM(GRID.THISFORM);
            if (!V.PRG_CONSOLE) { //если приложение не консольное
                INF3(GRID);
            }
        }
    }

    /**
     * Установка активной формы
     *
     * @param THISFORM
     */
    public static void SETACTIVEFORM(Formr THISFORM) {
        V.ACTIVEFORM = THISFORM;
        if (V.ACTIVEFORM != null) {
            V.ACTIVEFORM.SETFOCUS();
            if (V.ACTIVEFORM.isMaximum()) {
                V.ACTIVEFORM.MAXIMIZED = false;
                if (V.TOOLBAR != null) {
                    THISFORM.setBounds(THISFORM.getX(), 30, THISFORM.getWidth(), V._SCREEN.getHeight() - 60);
                }
            }
        }
        if (V.TOOLBAR != null) {
            if (V.ACTIVEFORM != null && V.ACTIVEFORM.ENABLE_PRINT == true) {
                V.TOOLBAR.PRINT.setEnabled(true);
            } else {
                V.TOOLBAR.PRINT.setEnabled(false);
            }
        }
    }

    /**
     * Установка текущего алиаса
     *
     * @param ALIASA
     */
    public static void SETACTIVEALIAS(String ALIAS) {
        V.ACTIVEALIAS = ALIAS;
        if (!V.PRG_CONSOLE) { //если приложение не консольное
            INF4(ALIAS);
        }
    }

    /**
     * Установка текущей записи в курсор данных
     *
     * @param DATA
     * @param REC
     */
    public static void SETDATARECNO(Cursorr DATA, int REC) {
        DATA.RECNO = REC;
        if (!V.PRG_CONSOLE) {//если приложение не консольное
            INF4("");
        }
    }

    public static void INF3(Gridr GRID) {
        if (GRID == null) {
            V._SCREEN.INF3.setText("Грид:  Алиас: ");
        } else {
            V._SCREEN.INF3.setText("Грид: " + GRID.getName() + " Алиас: " + GRID.ALIAS + " Колонок: " + GRID.getColumnCount() + " Записей: " + GRID.getRowCount());
        }
    }

    public static void INF4(String ALIAS) {
        {
            V._SCREEN.INF4.setText("Текущий Алиас: " + A.ALIAS() + " Запись: " + A.RECNO() + " Записей: " + A.RECCOUNT());
        }
    }
}
