package com.fimi.kernel.utils;

import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.command.FwUpdateCollection;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/* loaded from: classes.dex */
public class DesEncryptUtil {
    private static volatile DesEncryptUtil desEncryptUtil = null;
    public final byte[] ENCRYPT_KEY_BYTES = {-77, -92, -10, -30, 52, 33, FcCollection.MSG_ID_SET_ENABLE_TRIPOD, 17};
    public final byte[] ENCRYPT_KEY_BYTES_TWO = {FwUpdateCollection.MSG_ID_GET_VERSION, -87, -58, 50, 52, -111, FcCollection.MSG_GET_SURROUND_DEVICE_ORIENTATION, FcCollection.MSG_SET_FOLLOW_ERROR_CODE};
    private final String HEX = "0123456789ABCDEF";

    public static DesEncryptUtil getInstans() {
        if (desEncryptUtil == null) {
            synchronized (DesEncryptUtil.class) {
                if (desEncryptUtil == null) {
                    desEncryptUtil = new DesEncryptUtil();
                }
            }
        }
        return desEncryptUtil;
    }

    public static void main(String[] str) {
        double freeCapacity = Double.valueOf("").doubleValue();
        System.out.print(freeCapacity);
    }

    public static String toHexString1(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (byte b2 : b) {
            buffer.append(toHexString1(b2));
        }
        return buffer.toString();
    }

    public static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 255);
        if (s.length() == 1) {
            return "0" + s;
        }
        return s;
    }

    public String desCode(String hex, String password) {
        try {
            return new String(decrypt(toByte(hex), password));
        } catch (Exception e) {
            e.printStackTrace();
            return hex;
        }
    }

    public String enCode(String str, String password) {
        try {
            byte[] by = desCrypto(str.getBytes("utf-8"), password);
            String hex = toHex(by);
            return hex;
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    private byte[] desCrypto(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes("utf-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, random);
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] decrypt(byte[] src, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes("utf-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, random);
        return cipher.doFinal(src);
    }

    private byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(i * 2, (i * 2) + 2), 16).byteValue();
        }
        return result;
    }

    private String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(buf.length * 2);
        for (byte b : buf) {
            appendHex(result, b);
        }
        return result.toString();
    }

    private void appendHex(StringBuffer sb, byte b) {
        sb.append("0123456789ABCDEF".charAt((b >> 4) & 15)).append("0123456789ABCDEF".charAt(b & 15));
    }

    public byte[] desCbcEncrypt(byte[] content, byte[] keyBytes) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, random);
            return cipher.doFinal(content);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] desCbcDecrypt(byte[] content, byte[] keyBytes) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, securekey, random);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
