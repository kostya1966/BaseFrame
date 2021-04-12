/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import java.util.ArrayList;

/**
 *
 * @author dima
 */
public interface FiscalPrinter {

    public String getResultCode();

    public String getResultDescription();

    //**************************************************************************
    // Внесение денег
    //
    //**************************************************************************    
    public boolean cashIncome(double sum);

    //**************************************************************************
    // Получить текущее количество денег 
    //
    //**************************************************************************     
    public double getCashReg();

    //**************************************************************************
    // Изъятие денег
    //
    //**************************************************************************
    public boolean cashOutcome(double sum);

    //**************************************************************************
    // Чек на продажу/возврат
    //
    //**************************************************************************
    public boolean printChek(int check_id, ArrayList<ChekPosition> chekPos, boolean isVozvr, double price_cash,
            double price_card, double price_sert, double price_disc, String dk_inf, String seler, double price_cred, String trigger);

    // ************************************************************************
    // закрытие соединения с ФР
    //
    //*************************************************************************    
    public void closeConnect();

    // ************************************************************************
    // печать X отчета
    //
    //*************************************************************************
    public boolean getXReport();

    //**************************************************************************
    // печать Z отчета
    //
    //**************************************************************************
    public boolean getZReport();

    // ************************************************************************
    // печать буфера контрольной ленты
    //
    //*************************************************************************
    public boolean getKReport();

    //*************************************************************************
    // количество чеков продаж
    //
    //**************************************************************************
    public int getSaleCheckCount();

    //**************************************************************************
    // количество чеков возвратов
    //
    //**************************************************************************
    public int getReturnCheckCount();

    //**************************************************************************
    // получить статус ФР
    //
    //**************************************************************************    
    public int getStatus();

    //**************************************************************************
    // получить описание статуса ФР
    //
    //**************************************************************************    
    public String getStatusDescription();

    //**************************************************************************
    // получить подрежим ФР
    //
    //**************************************************************************    
    public int getAdvStatus();

    //**************************************************************************
    // получить описание подрежима ФР
    //
    //**************************************************************************    
    public String getAdvStatusDescription();

    //**************************************************************************
    // продолжение печати чека
    //
    //**************************************************************************    
    public boolean continuePrint();

    //**************************************************************************
    // записывает в таблицы фр необходимые для работы кассы значения
    //
    //**************************************************************************
    public boolean writeFRTable();

    //**************************************************************************
    // сменный отчет по кассирам
    //
    //**************************************************************************    
    public boolean cashierReport();

    //**************************************************************************
    // сменный отчет по товарам
    //
    //**************************************************************************    
    public boolean productReport();

    //**************************************************************************
    // вывод на экран буфера контрольной ленты
    //
    //**************************************************************************    
    public String EKLZViewReportByNum();

    //**************************************************************************
    // печать чека по банковскому терминалу
    //
    //**************************************************************************    
    public boolean printBankCheck(String checkText);

    //**************************************************************************
    // проверка соединения
    //
    //**************************************************************************    
    public int connected();

    //**************************************************************************
    // Чек на продажу/возврат (текстовый документ)
    //
    //**************************************************************************
    public boolean printChekText(int check_id, ArrayList<ChekPosition> chekPos, boolean isVozvr, double price_cash,
            double price_card, double price_sert, double price_disc, String dk_inf, String seler, double price_cred);

    //**************************************************************************
    // (текстовый документ)
    //
    //**************************************************************************
    public boolean printText(String checkText);

    //**************************************************************************
    // аннулирование
    //
    //**************************************************************************
    public boolean annulCheck(String tip, String id_chek, String fr_id_chek, double chekSum);

    //**************************************************************************
    // отчет по налогам
    //
    //**************************************************************************
    public boolean getTaxReport();

    //**************************************************************************
    // открытие смены
    //
    //**************************************************************************
    public boolean startSession();

    //**************************************************************************
    // проверка на статус открытой смены
    //
    //**************************************************************************
    public boolean startCheck();

    //**************************************************************************
    // печать тест чека
    //
    //**************************************************************************
    public boolean testCheck();

    //**************************************************************************
    // соединение с принтером
    //
    //**************************************************************************
    public boolean connect();

    //**************************************************************************
    // Печать дубликата последнего чека
    //
    //**************************************************************************
    public boolean printDuplicate();

    //**************************************************************************
    // Печать чека коррекции
    //
    //**************************************************************************
    public boolean printCorrectionCheck(int taxType, int correctionType, int calculationSign, double summCheck,
            double summNal, double summElectr, double summNds18, double SummNds10);

    //**************************************************************************
    // Считываение системы налогообложения
    //
    //**************************************************************************
    public int getTaxType();

    //**************************************************************************
    // Считываение даты последнего обмен с ОФД
    //
    //**************************************************************************
    public String getOFDLastDate();

    //**************************************************************************
    // Печать чека через фискальный накопитель
    //
    //**************************************************************************
    public boolean printChekFN(int check_id, ArrayList<ChekPosition> chekPos, boolean isVozvr, double price_cash,
            double price_card, double price_sert, double price_disc, String dk_inf, String seler, double price_cred, String trigger);

    /**
     * Установка тега ИНН кассира
     */
    public boolean setSellerINN(String tabno);

    /**
     * Установка тега кода товара
     */
    public boolean setProductCode(String art, String asize);

    /**
     * Печать EAN13
     */
    public boolean printEan13(String code);
}