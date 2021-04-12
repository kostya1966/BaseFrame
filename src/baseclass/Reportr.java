/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclass;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;
import prg.A;
import prg.FF;
import prg.P;
import prg.V;

/**
 *
 * @author DonchenkoUA
 */
public class Reportr {
// boolean TF=true;
    public static  String SQL_QUERY_TABLEINFO = "";
    //HashMap<String, Object> map = new HashMap<>();
    public static JasperPrint jp = null;
    public static boolean INIT(String SQL) { // ИНИЦИАЛИЗАЦИЯ ПЕРЕМЕННЫХ
        
            V.PRINTMAP = new HashMap<>();
            V.PRINTMAP.put("USER",V.USER_FIO);
            V.PRINTMAP.put("DATE", FF.DATETIMES());
            V.PRINTMAP.put("PODR",V.USER_PODR);
            V.PRINTMAP.put("KPODR",V.USER_KPODR);
                V.PRINTMAP.put("RUB", V.RUB );//
                V.PRINTMAP.put("KOP", V.KOP );//
            
            if (V.TYPE_CHAR.equals(A.TYPE("CONFIG.PATTERN_SUM"))) {
             V.PRINTMAP.put("PATTERN_SUM",V.PATTERN_SUM); //маска суммы на печать
            } 
            if (!FF.EMPTY(SQL)) {
            V.PRINTMAP.put("SELE", SQL );
            FF._CLIPTEXT(SQL);
            }
            
        return true;
    }
    public static boolean RUN(String REPORT) { // ВЫПОЛНЕНИЕ ОТЧЕТА
        return RUN(REPORT,"",false,"В отчете отсутствуют данные.");
    }
    public static boolean RUN(String REPORT,String PRINTER) { // ВЫПОЛНЕНИЕ ОТЧЕТА
        return RUN(REPORT,PRINTER,false,"В отчете отсутствуют данные.");
    }
    public static boolean RUN(String REPORT,String PRINTER,boolean OK) { // ВЫПОЛНЕНИЕ ОТЧЕТА
        return RUN(REPORT,PRINTER,OK,"В отчете отсутствуют данные.");
    }
    /**
     * 
     * @param REPORT - ИМЯ ОТЧЕТА
     * @param PRINTER - ИМЯ ПРИНТЕРА
     * @param OK - true БЕЗ  ФОРМЫ ПРЕДВАРИТЕЛЬНОГО ПРОСМОТРА (ДАЖЕ  ЕСЛИ  ЕСТЬ PRINTER)
     * @param zeroMess - сообщение при пустом отчете
     * @return 
     */
    public static boolean RUN(String REPORT,String PRINTER,boolean OK,String zeroMess) { // ВЫПОЛНЕНИЕ ОТЧЕТА
        P.WRITE_INFO("Выполнение отчета "+REPORT);
        V.PRINTMAP.put("REPORT_NAME",REPORT);
//        V.TF_PR=true;
        V.TF_PR=genRep(REPORT,V.PRINTMAP,PRINTER,OK,zeroMess);
        
        return V.TF_PR;
    }

    public static boolean genRep_OLD(String name, HashMap<String, Object> map,String PRINTER,boolean OK, String zeroMess) { // загружаем и выводим отчет
//        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        String workDir = "";
        String file = "";
        
        char sep = (char) 92; //если в имени указан путь к подкаталогу как windows \  то переписать на текущий V.SEPARATOR
        char[] sepa=V.SEPARATOR.toCharArray();
        char sep2=sepa[0];
        name=name.replace(sep, sep2);

        try {
//            file=workDir + name + ".jasper";
            file=V.DIRECTORY_REPORT + name + ".jasper";
            if (FF.FILE(file)==false) {
            P.MESSERR("Не найден файл отчета "+"\n"+file);
            }
            P.ALERT("Формирование отчета...");
            jp = JasperFillManager.fillReport(file, map, V.CONN1);
            P.ALERT("");
            V.JP_SIZE = jp.getPages().size();
            if (jp.getPages().size()==0){
//                P.MESS("В отчете отсутствуют данные.");
                P.MESS(zeroMess);
                return false;
            }

V.JP=jp; 
V.PRINTER=PRINTER;     
V.FILE_REPORT=file;
            
 if (OK)            { //БЕЗ ФОРМЫ ПРЕДВАРИТЕЛЬНОГО ПРОСМОТРА
 JRPrintServiceExporter exporter = new JRPrintServiceExporter(); 
 PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet(); 
 printServiceAttributeSet.add(new PrinterName(V.PRINTER, null)); 
 exporter.setParameter(JRExporterParameter.JASPER_PRINT, V.JP); 
 exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet); 
 exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE); 
 exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE); 
          try {            
              exporter.exportReport();
          } catch (JRException ex) {
            P.MESSERR("Ошибка формирования отчета "+V.FILE_REPORT+"\n"+ex.getMessage());
            System.out.print(ex.getMessage());
            V.TF_PR=false;
            return false ;
          }
       V.TF_PR=true;   
       return V.TF_PR;       
     
 }
 if (FF.EMPTY(PRINTER)) { //если не задан притер то предосмотр 
     JasperViewer.viewReport(jp, false,V.LOCRUS);
     
 } else { // на принтер без предосмотра
    //P.PRINTERSTAT(PRINTER); 
    //Image capt= JasperPrintManager.printPageToImage(jp, 1, 1);
    //P.IMAGETOFILE(capt, name);
  P.DOFORM("PRINT");

 }
           
        } catch (JRException ex) {
            P.MESSERR("Ошибка формирования отчета "+file+"\n"+ex.getMessage());
            System.out.print(ex.getMessage());
            return false;
        } catch (Exception ex) {
            P.MESSERR("Ошибка выполнения отчета "+file+"\n"+ex.getMessage());
            System.out.print(ex.getMessage());
            return false ;
         }
        
    return V.TF_PR;
    }

  public static boolean genRep(String name, HashMap<String, Object> map, String PRINTER, boolean OK, String zeroMess) { // загружаем и выводим отчет
//        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        String file = "";
        //если в имени указан путь к подкаталогу как windows '\' то переписать на сепаратор системы
        name = name.replace("\\", File.separator);
        try {
            file = V.DIRECTORY_REPORT + name + ".jasper"; //
//            if (!FF.FILE(file)) {
//                FormP.MESSERR("Не найден файл отчета " + "\n" + file);
//            }
            // создаем временный файл отчета
//            String fileName = name;
//            if (name.contains(File.separator)) {
//                fileName = name
//                        .substring(name.lastIndexOf(File.separator))
//                        .replace(File.separator, "");
//            }
            final Path report = Paths.get(file).toAbsolutePath();
//            Path tmpReport = Files.createTempFile(report.getParent(), fileName + "-", ".jasper").toAbsolutePath();
//            tmpReport.toFile().deleteOnExit();
//
//            Files.copy(report, tmpReport, StandardCopyOption.REPLACE_EXISTING);
            //
            P.WRITE_INFO(report.toString());
            P.ALERT("Формирование отчета...");
            jp = JasperFillManager.fillReport(report.toString(), map, V.CONN1);
            P.ALERT("");
            V.JP_SIZE = jp.getPages().size();
            if (jp.getPages().size() == 0) {
//                FormP.MESS("В отчете отсутствуют данные.");
                P.MESS(zeroMess);
                return false;
            }
            V.JP = jp;
            V.PRINTER = PRINTER;
            V.FILE_REPORT = file;
            if (OK) { //БЕЗ ФОРМЫ ПРЕДВАРИТЕЛЬНОГО ПРОСМОТРА
                JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
                printServiceAttributeSet.add(new PrinterName(V.PRINTER, null));
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, V.JP);
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
                try {
                    exporter.exportReport();
                } catch (JRException ex) {
                    P.MESSERR("Ошибка формирования отчета " + V.FILE_REPORT + "\n" + ex.getMessage());
                    System.out.print(ex.getMessage());
                    V.TF_PR = false;
                    return false;
                }
                V.TF_PR = true;
                return V.TF_PR;
            }
            if (FF.EMPTY(PRINTER)) { //если не задан притер то предосмотр
                JasperViewer.viewReport(jp, false, V.LOCRUS);
            } else { // на принтер без предосмотра
                //P.PRINTERSTAT(PRINTER);
                //Image capt= JasperPrintManager.printPageToImage(jp, 1, 1);
                //P.IMAGETOFILE(capt, name);
                P.DOFORM("PRINT");
            }
        } catch (JRException ex) {
            P.MESSERR("Ошибка формирования отчета " + file + "\n" + ex.getMessage());
            System.out.print(ex.getMessage());
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            P.MESSERR("Ошибка выполнения отчета " + file + "\n" + ex.getMessage());
            System.out.print(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return V.TF_PR;
    }  
    public static void showRep(String nameRep) {
        // JasperViewer.viewReport(genR , false);
        //JRViewer jv = new JRViewer(jp); // компонент просмотра отчета
    }
}
