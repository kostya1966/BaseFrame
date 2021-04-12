/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import prg.A;
import prg.P;
import prg.V;

/**
 *
 * @author Hit
 */
public class MonitorThread extends Thread {

    private String IP, ID;

    public MonitorThread(String IP, String ID) {
        this.IP = IP;
        this.ID = ID;
    }

    @Override
    public void run() {
        InetAddress remote = null;
        String host = "";
        try {

            host = this.IP;
            remote = InetAddress.getByName(host);

            int port = 1521;

            try {
                Socket s = new Socket(remote, port);
                V.MONITOR_ARR.add(this.ID);
//                P.SQLUPDATE("insert into temp_shopid_m values ('" + this.ID + "')");
//                System.out.println("Server is listening " + this.ID + " on port " + port + " of " + host);
                s.close();
            } catch (IOException ex) {
                // The remote host is not listening on this port
//                System.out.println("Server is not listening " + this.ID + " on port " + port + " of " + host);
            }
        } catch (UnknownHostException ex) {
//            Logger.getLogger(P.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println(this.ID + " - IP ERROR");
        }

    }

}
