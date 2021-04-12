/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import prg.V;

/**
 *
 * @author DaN
 */
public class Configs extends Properties {

    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String HOME_DIRECTORY = System.getProperty("user.home");
    public static final String CONFIGS_DIRECTORY = HOME_DIRECTORY + SEPARATOR + "ProjectPropertis" + SEPARATOR;
    public File fConfig = null;

    public Configs(String fileName) {
        fConfig = new File(fileName);
        fillPropertiesFromConfigFile(fConfig);
    }

    public void saveProperties(String nameConfiguration) {
        try {
            try (FileOutputStream fos = new FileOutputStream(fConfig, false)) {
                this.store(fos, nameConfiguration);
                fos.close();
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void loadProperties() {
    }

    private void fillPropertiesFromConfigFile(File fConfig) {
        if (fConfig.exists()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(fConfig);
                try {
                    this.load(fis);
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException exc) {
                    //KAA 23072018 - если побился конфиг, удаляю файл и требую перезапуск
                    try {
                        fis.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Configs.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(V._SCREEN, "Ошибка файла настроек " + fConfig + "!\nПерезапустите программу!", "Внимание !!!!", JOptionPane.ERROR_MESSAGE);
                    fConfig.delete();
                    System.exit(0);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
