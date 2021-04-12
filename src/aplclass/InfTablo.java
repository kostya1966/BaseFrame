/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import prg.V;

/**
 *
 * @author dima
 */
public class InfTablo {

    private ActiveXComponent tablo;

    public InfTablo() {
        try {
            tablo = new ActiveXComponent("AddIn.Line45");
            //tablo.invoke("DeviceEnabled", new Variant(true));
            tablo.setProperty("DeviceEnabled", new Variant(true));
            Variant[] var = {new Variant("Hello МИР"), new Variant(0)}; 
            tablo.invoke("DisplayText",var);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
