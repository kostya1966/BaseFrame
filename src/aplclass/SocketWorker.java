/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplclass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Администратор
 */
public class SocketWorker {
    private int Port;
    private String AdressIP;
    private char endFromLine;
    private String lastError;
    
    private DataInputStream inputLine;
    private DataOutputStream outputLine;
    private Socket socket;
    
    private String charsetSendBytes;
    
    
    public SocketWorker(String AdressIP,int Port) {
        constructor(AdressIP,Port,' ');
    }
    
    public SocketWorker(String AdressIP,int Port, char endFromLine) {
        constructor(AdressIP,Port,endFromLine);
    }
    
    private void constructor(String AdressIP,int Port, char endFromLine) {
        this.AdressIP = AdressIP;
        this.Port = Port;
        this.endFromLine = endFromLine;
        this.lastError = null;
        this.charsetSendBytes = "UTF-8";
        this.socket = null;
        this.inputLine = null;
        this.outputLine = null;
    }
    
    public void setCharsetSendBytes(String charset) {
        this.charsetSendBytes = charset;
    }
    
    public String GetLastException(){
        return lastError;
    }
    
    private void writeException(Exception ex) {
        lastError = ex.getMessage()+"\r\n";
        
        for(StackTraceElement stackTraceElement : ex.getStackTrace())
            lastError +=stackTraceElement.toString() + "\r\n";
    }
    
    public boolean connection() {
        try {
            socket = new Socket(InetAddress.getByName(AdressIP),Port);
            
            inputLine = new DataInputStream(socket.getInputStream());
            outputLine = new DataOutputStream(socket.getOutputStream());
            
            return true;
        } catch (IOException ex) {
            writeException(ex);
            return false;
        }
    }
    
    public boolean SendLine(String sendLine) {
        try {
            outputLine.write((sendLine).getBytes(charsetSendBytes), 0, sendLine.length());
            return true;
        } catch (IOException ex) {
            writeException(ex);
            return false;
        }
    }
    
    public String GetLine() {
        String answer = "";
        int BufferLen = 512;
        byte Buffer[] = new byte[BufferLen];
        int getNumByte = 0;
        
        try {
            if(endFromLine != ' ') {
                
                while((char)(Buffer[getNumByte] = inputLine.readByte()) != endFromLine ) {
                    getNumByte ++;
                    if(getNumByte == BufferLen) {
                        answer += new String(Buffer,"UTF-8");
                        getNumByte = 0;
                        Buffer = new byte[BufferLen];
                    }
                }
                
                answer += new String(Buffer,"UTF-8");
                
                return answer;
            } else {
                answer = inputLine.readUTF();
                return answer;
            }
        } catch (IOException ex) {
            writeException(ex);
            return null;
        }
    }
    
    public String SendAndGetAnswer(String sendLine) {
        if(SendLine(sendLine)) {
            return GetLine();
        } else {
            return null;
        }
    } 
    
    public boolean CloseConnection() {
        try {
            socket.close();
            return true;
        } catch (IOException ex) {
            writeException(ex);
            return false;
        }
    }
    
}
