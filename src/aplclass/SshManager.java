package aplclass;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import java.awt.Label;
import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Администратор
 */
public class SshManager extends Thread{
    
    private static String       HOSTNAME  = "";
    private static String       USERNAME  = "";
    private static String       PASSWORD  = "";
    
    private static final int    SSH_PORT = 22;
    private static final int    CONNECTION_TIMEOUT = 1000000;
    private static final int    BUFFER_SIZE = 200*1024;
    
    private static boolean      out_in_desk = false;
    
    private static String       run_command = "";

    SshManager(){
        HOSTNAME = "8.8.8.8";
        USERNAME = "user";
        PASSWORD = "password";
//        command = "cd /u01/app/oracle/install_param\nls -1\n";
    }
    
    public SshManager(String host, String username, String password){
        HOSTNAME = host;
        USERNAME = username;
        PASSWORD = password;
    }
    
    public void out_on_besk(JTextArea jTextArea){
        out_in_desk = true;
    }
    
    public void SetCommand(String command,JButton jButton,Label label){
        run_command = command;
    }
    
    @Override
    public void run()
    {
        out_in_desk = false;
        connectAndExecuteListCommand(run_command);
        JOptionPane.showMessageDialog(null, "Обработка запроса закончина....", "Внимание", JOptionPane.WARNING_MESSAGE);
    }
    
    public ArrayList<String> connectAndExecuteListCommand(String command) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            
            Session session = initSession(HOSTNAME, USERNAME, PASSWORD);
//            Channel channel = initChannel(command, session);
            Channel channel = session.openChannel("shell");
            InputStream in = channel.getInputStream();
            OutputStream out = channel.getOutputStream();
            ((ChannelShell) channel).setPty(false);
            channel.connect();
            out.write((command).getBytes());
            out.write(("\nexit\n").getBytes());
            out.flush();
            System.out.println("enter command to output stream");
            Thread.sleep(1000);
            
            String dataFromChannel = getDataFromChannel(channel, in);
            lines.addAll(Arrays.asList(dataFromChannel.split("\n")));
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lines;
    }
    
    private Session initSession(String host, String username, String password) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, SSH_PORT);
        session.setPassword(password);
        UserInfo userInfo = new MyUserInfo();
        session.setUserInfo(userInfo);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(CONNECTION_TIMEOUT);
        return session;
    }
    
    private Channel initChannel(String commands, Session session) throws JSchException, IOException, InterruptedException {
        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(false);
        channelExec.setCommand(commands);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        return channel;
    }
    
    private String getDataFromChannel(Channel channel, InputStream in) throws IOException {
        StringBuilder result = new StringBuilder();
        byte[] tmp = new byte[BUFFER_SIZE];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, BUFFER_SIZE);
                if (i < 0) {
                    break;
                }
//                System.out.println((new String(tmp, 0, i)));
                if(out_in_desk)
                    System.out.println(new String(tmp, 0, i));
                else
                    result.append(new String(tmp, 0, i));
            }
            
            if (channel.isClosed()) {
                int exitStatus = channel.getExitStatus();
                System.out.println("exit-status: " + exitStatus);
                break;
            }
            trySleep(1000);
        }
        
        out_in_desk = false;
        
        return result.toString();
    }
    
    private void trySleep(int sleepTimeInMilliseconds) {
        try {
            Thread.sleep(sleepTimeInMilliseconds);
        } catch (Exception e) { }
    }
}
