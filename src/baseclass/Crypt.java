/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclass;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author DaN
 */
public class Crypt {

    static byte[] keyBytes = "keykeykey3".getBytes(); // ключь шифрования

    public static String decrypt(String str) {
        try {
            javax.crypto.spec.SecretKeySpec key = null;
            try {
                key = new javax.crypto.spec.SecretKeySpec(getRawKey(), "DES");
            } catch (Exception ex) {
                Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
            }
            Cipher decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] utf8 = toByte(str);
            // Descrypt
            byte[] dec = new byte[]{};
            try {
                dec = decryptCipher.doFinal(utf8);
            } catch (IllegalBlockSizeException | BadPaddingException ex) {
                Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
            }
            return new String(dec);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException exc) {
        }
        return str;
    }

    public static String encrypt(String str) {
        try {

            javax.crypto.spec.SecretKeySpec key = null;
            try {
                key = new javax.crypto.spec.SecretKeySpec(getRawKey(), "DES");
            } catch (Exception ex) {
                Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
            }
            Cipher encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] utf8 = null;
            try {
                utf8 = str.getBytes("UTF8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Encrypt
            byte[] enc = null;
            try {
                enc = encryptCipher.doFinal(utf8);
            } catch (IllegalBlockSizeException | BadPaddingException ex) {
                Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
            }
            return toHex(enc);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException exc) {
            System.out.print(exc);
        }
        return str;
    }

    private static byte[] getRawKey() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("DES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(keyBytes);
        kgen.init(56, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    // эти методы используются для конвертации байтов в ASCII символы
    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
