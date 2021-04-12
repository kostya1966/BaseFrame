package support;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import prg.P;
import prg.V;

/**
 * Работа со справкой
 *
 * @author stankevichpa
 */
public class Help {

    private static String version;
    private static String doc = "chm" + File.separatorChar + "doc.chm";
    private static final String ver = "chm" + File.separatorChar + "ver";

    private Help() {
    }

    /**
     * Проверяет на наличие файла справки
     *
     * @return
     */
    public static boolean isFileFound() {
        return isFileFound(doc);
    }

    /**
     * Проверяет на наличие файла
     *
     * @return
     */
    private static boolean isFileFound(String fileName) {
        return new File(fileName).isFile();
    }

    /**
     * Проверяет проект, доступна ли в нем справка
     *
     * @return
     */
    public static boolean isProjectHasHelp() {
        return V.HELP == 1;
    }

    /**
     * Проверяет, поддерживается ли данная ОС
     *
     * @return
     */
    public static boolean isSystemSuported() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") != -1;
    }

    /**
     * Делает проверку проекта на возможность запуска
     *
     * @return
     */
    public static boolean isAvailable() {
        return isFileFound() && isProjectHasHelp();
    }

    /**
     * Делает проверку проекта на возможность запуска на данной ОС
     *
     * @return
     */
    public static boolean isAvailableOnSystem() {
        return isAvailable() && isSystemSuported();
    }

    /**
     * Загружает в БД новую версию справки
     *
     * @param file
     *            файл, который будет загружен как новая версия
     * @throws FileNotFoundException
     *             указанный файл не найден
     * @throws IOException
     *             не получен доступ к файловой системе
     * @throws NullPointerException
     *             если пользователь не вошел в систему
     */
    public static void loadHelp(File file, String description)
            throws FileNotFoundException, IOException, NullPointerException {
        long len = file.length();
        try (PreparedStatement st = V.CONN1
                .prepareStatement("INSERT INTO RI_FAQ(FAQ_FILE, DESCRIPTION) VALUES (?, ?)")) {
            st.setBinaryStream(1, new FileInputStream(file), len);
            st.setString(2, description);
            st.execute();
        } catch (SQLException ex) {
            System.err.println("Help.loadHelp - Невозможно загрузить файл справки в БД: \r\n" + ex);
        }
    }

    /**
     * Скачивает версию по умолчанию
     *
     * @throws NullPointerException
     *             если пользователь не вошел в систему
     */
    private static void downloadHelp() throws NullPointerException {
        downloadHelp(version == null || version.isEmpty() ? "1" : version);
    }

    /**
     * Скачивает указанную версию
     *
     * @param version
     * @throws NullPointerException
     *             если пользователь не вошел в систему
     */
    public static void downloadHelp(String path, String version) throws NullPointerException {
        String temp = doc;
        doc = path + "";
        downloadHelp(version);
        doc = temp;
    }

    /**
     * Скачивает указанную версию
     *
     * @param version
     * @throws NullPointerException
     *             если пользователь не вошел в систему
     */
    private static void downloadHelp(String version) throws NullPointerException {
        createDirs(doc);
        try (Statement st = V.CONN1.createStatement();
                ResultSet rs = st.executeQuery("SELECT LENGTH(FAQ_FILE), FAQ_FILE FROM RI_FAQ WHERE ID=" + version)) {
            if (rs.next()) {
                int length = rs.getInt(1);
                Blob blob = rs.getBlob(2);
                try (BufferedInputStream is = new BufferedInputStream(blob.getBinaryStream());
                        FileOutputStream fos = new FileOutputStream(doc);) {
                    byte[] buffer = new byte[length];
                    int r = 0;
                    while ((r = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, r);
                    }
                    fos.flush();
                } catch (IOException ex) {
                    System.err.println("Help.downloadHelp - Невозможно сохранить файл справки: \r\n" + ex);
                }
                blob.free();
            }
        } catch (SQLException ex) {
            System.err.println("Help.downloadHelp - Невозможно скачать файл справки: \r\n" + ex);
        }
    }

    /**
     * Получает текущую версию справки установленную на ПК
     *
     * @return
     */
    private static String getVersionOnPC() {
        if (version == null) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ver))) {
                return ois.readUTF();
            } catch (IOException ex) {
                System.err.println("Файл версии не найден.");
            }
        }
        return version;
    }

    /**
     * Получает из БД актуальную версию справки
     *
     * @return послежний номер версии из БД
     */
    public static String getActualVersion() {
        String result = null;
        try (Statement st = V.CONN1.createStatement();
                ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(ID), 0) FROM RI_FAQ")) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            System.err.println(
                    "Help.getActualVersion - При  получении актуальной версии справки возникла ошибка: \r\n" + ex);
        }
        return result;
    }

    /**
     * Создает файл с указанием текущей версии на ПК
     *
     * @param version
     *            текущая версия
     */
    private static void setVersionOcPC(String version) {
        if (version == null || version.isEmpty()) {
            return;
        }
        createDirs(ver);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ver))) {
            oos.writeUTF(version);
            oos.flush();
            Help.version = version;
        } catch (IOException ex) {
            System.err.println("Help.setVersionOcPC - Невозможно установить версию справки: \r\n" + ex);
        }
    }

    /**
     * Обновляет если необходимо файл справки
     *
     * @throws NullPointerException
     *             если пользователь не вошел в систему
     */
    private static void updateHelp() throws NullPointerException {
        version = getVersionOnPC();
        String actualVersion = getActualVersion();
        if ((version == null || !version.equals(actualVersion) || !isFileFound(doc)) && actualVersion != null
                && !actualVersion.isEmpty()) {
            setVersionOcPC(actualVersion);
            try {
                downloadHelp();
            } catch (NullPointerException ex) {
                System.err
                        .println("Help.updateHelp - Невозможно скачать файл справки. Пользователь не вошел в систему.");
            }
        }
    }

    /**
     * Запускает файл справки
     */
    public static void execute() {
        execute(null);
    }

    /**
     * Запускает файл справки
     *
     * @param str
     *            файл в справке, пример: str="index"; //тогда в документе
     *            справки, будет открыто index.html.
     *            <p>
     *            Если параметр str пуст или null, то вызовется справка, с
     *            установленной страницей по умолчанию
     */
    public static void execute(String str) {
        if (isSystemSuported()) {
            try {
                updateHelp();
            } catch (NullPointerException ex) {
                System.err.println("Help.execute - Невозможно обновить файл справки. Пользователь не вошел в систему.");
            }
            try {
                String comand = "cmd /c hh.exe " + doc;
                if (str != null && !str.isEmpty()) {
                    if (str.lastIndexOf(".html") == -1 || str.lastIndexOf(".html") != str.length() - 5) {
                        str += ".html";
                    }
                    comand += "::" + str;
                }
                Runtime.getRuntime().exec(comand);
            } catch (IOException e) {
                P.MESSERR(e.getMessage());
            }
        } else {
            P.MESSERR("Справка не поддерживается на данной ОС.");
        }
    }

    /**
     * Создает папки до указанного пути
     * <p>
     * <b>Обязательно! После всех директорий необходимо ставить сепаратор<b>
     * </p>
     * <p>
     * Пример: "folder" - неправильно, "folder\" - правильно
     *
     * @param dir
     */
    private static void createDirs(String dir) {
        int lastSeparator = dir.lastIndexOf(File.separatorChar);
        File folder = new File(dir.substring(0, lastSeparator == -1 ? dir.length() : lastSeparator));
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
    }
}
