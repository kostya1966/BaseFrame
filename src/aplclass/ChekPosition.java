/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

/**
 * позиция чека
 * @author dima
 */
public class ChekPosition {
  
    public String art = ""; //артикул
    public String productName = ""; // наименование товара
    public double price = 0; // цена товара
    public int quantity = 0; // количество товара
    public double disc = 0; // скидка в позиции
    public String asize = "0";  //размер
    
    public ChekPosition(String art, String asize, String productName, double price, int quantity, double disc){
        this.art = art;
        this.asize = asize;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.disc = disc;
    }
}
