/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import prg.P;


/**
 *
 * @author Hit
 */
public class ExchangeThread extends Thread {

    private String ID;

    public ExchangeThread(String ID) {
        this.ID = ID;
    }

    @Override
    public void run() {
        System.out.println(ID);
    }

}
