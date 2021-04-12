package aplclass;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jcraft.jsch.UserInfo;

/**
 *
 * @author Администратор
 */
public class MyUserInfo implements UserInfo {

    private String password;

    public void showMessage(String message) {
        System.out.println(message);
    }

    public boolean promptYesNo(String message) {
        System.out.println(message);
        return true;
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean promptPassphrase(String arg0) {
        System.out.println(arg0);
        return true;
    }

    @Override
    public boolean promptPassword(String arg0) {
        System.out.println(arg0);
        this.password = arg0;
        return true;
    }
}
