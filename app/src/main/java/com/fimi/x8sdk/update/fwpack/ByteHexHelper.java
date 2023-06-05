package com.fimi.x8sdk.update.fwpack;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class ByteHexHelper {
    private static boolean D = false;

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (byte b : src) {
            int v = b & 255;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String byteToHexString(byte src) {
        StringBuilder stringBuilder = new StringBuilder("");
        int v = src & 255;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString();
    }

    public static int byteToInt(byte src) {
        return src & 255;
    }

    public static byte[] intToHexBytes(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 2) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        return hexStringToBytes(hexString);
    }

    public static byte[] intToTwoHexBytes(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 4) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        return hexStringToBytes(hexString);
    }

    public static byte[] intToFourHexBytes(int n) {
        byte[] b = {(byte) (n & 255), (byte) ((n >> 8) & 255), (byte) ((n >> 16) & 255), (byte) ((n >> 24) & 255)};
        return b;
    }

    public static byte[] intToFourHexBytesTwo(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        if (len < 2) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        while (len < 8) {
            hexString = hexString + "0";
            len = hexString.length();
        }
        return hexStringToBytes(hexString);
    }

    public static byte intToHexByte(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 2) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        return hexStringToByte(hexString);
    }

    public static byte[] hexStringToBytes2(String hexString) {
        if (hexString == null || hexString.equals("")) {
            byte[] bytes = new byte[0];
            return bytes;
        }
        String hexString2 = hexString.toUpperCase();
        int length = hexString2.length() / 2;
        char[] hexChars = hexString2.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte hexStringToByte(String hexString) {
        String hexString2 = hexString.toUpperCase();
        int length = hexString2.length() / 2;
        char[] hexChars = hexString2.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d[0];
    }

    public static byte[] hexStringToBytes(String str) {
        int c;
        int i;
        String str1 = str.replace(" ", "");
        byte[] d = new byte[str1.length() / 2];
        int i2 = 0;
        while (i2 < str1.length()) {
            int tmp = str1.substring(i2, i2 + 1).getBytes()[0];
            if (tmp > 96) {
                c = ((tmp - 97) + 10) * 16;
            } else if (tmp > 64) {
                c = ((tmp - 65) + 10) * 16;
            } else {
                c = (tmp - 48) * 16;
            }
            int i3 = i2 + 1;
            int tmp2 = str1.substring(i3, i3 + 1).getBytes()[0];
            if (tmp2 > 96) {
                i = (tmp2 - 97) + 10;
            } else if (tmp2 > 64) {
                i = (tmp2 - 65) + 10;
            } else {
                i = tmp2 - 48;
            }
            d[i3 / 2] = (byte) (c + i);
            i2 = i3 + 1;
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String XOR(String hex) {
        byte bytes = 0;
        if (hex.length() > 0) {
            for (int i = 0; i < hex.length() / 2; i++) {
                bytes = (byte) (hexStringToByte(hex.substring(i * 2, (i * 2) + 2)) ^ bytes);
            }
        }
        byte[] bbb = {bytes};
        return bytesToHexString(bbb);
    }

    public static String currentData() {
        StringBuffer stringBuffer = new StringBuffer();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        String year = decimalFormat.format(calendar.get(1));
        String month = decimalFormat.format(calendar.get(2) + 1);
        String day = decimalFormat.format(calendar.get(5));
        String hour = decimalFormat.format(calendar.get(11));
        String minute = decimalFormat.format(calendar.get(12));
        String second = decimalFormat.format(calendar.get(13));
        String week = decimalFormat.format(calendar.get(7) - 1);
        stringBuffer.append(year.substring(2, year.length())).append(month).append(day).append(hour).append(minute).append(second).append(week);
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }

    public static String RandomMethod() {
        int random = (int) (Math.random() * 100.0d);
        String hexString = Integer.toHexString(random);
        int len = hexString.length();
        while (len < 2) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        return hexString;
    }

    public static String packLength(String str) {
        String hexLength = Integer.toHexString(str.length() / 2);
        int len = hexLength.length();
        while (len < 4) {
            hexLength = "0" + hexLength;
            len = hexLength.length();
        }
        return hexLength;
    }

    public static String checkedSite(int site) {
        String hexLength = Integer.toHexString(site);
        int len = hexLength.length();
        while (len < 2) {
            hexLength = "0" + hexLength;
            len = hexLength.length();
        }
        return hexLength;
    }

    public static String packLength(int dataLen) {
        String hexLength = Integer.toHexString(dataLen);
        int len = hexLength.length();
        while (len < 4) {
            hexLength = "0" + hexLength;
            len = hexLength.length();
        }
        return hexLength;
    }

    public static int intPackLength(String str) {
        int intLength = Integer.valueOf(str, 16).intValue();
        return intLength;
    }

    public static int intPackLength(byte[] str) {
        String byteStr = bytesToHexString(str);
        int intLength = Integer.valueOf(byteStr, 16).intValue();
        return intLength;
    }

    public static String packVerify(String target, String source, String packLengths, String counter, String commandWord, String dataArea) {
        String verify = XOR(target + source + packLengths + counter + commandWord + dataArea);
        return verify;
    }

    public static String dpuString(String str) {
        if (str == null || str.length() <= 0) {
            return "";
        }
        byte[] src = (str + "\u0000").getBytes();
        String result = bytesToHexString(src);
        String resultLength = packLength(result);
        String buffer = resultLength + result;
        System.out.println("resultLength==" + buffer);
        return buffer;
    }

    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("")) {
            return "";
        }
        if (bString.length() % 8 != 0) {
            int addLen = 8 - (bString.length() % 8);
            for (int i = 0; i < addLen; i++) {
                bString = bString + "0";
            }
            System.out.println("choiceItem = " + bString);
        }
        StringBuffer tmp = new StringBuffer();
        for (int i2 = 0; i2 < bString.length(); i2 += 4) {
            int iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i2 + j, (i2 + j) + 1)) << ((4 - j) - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        System.out.println("tmp.toString() = " + tmp.toString());
        return tmp.toString();
    }

    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            return null;
        }
        String bString = "";
        for (int i = 0; i < hexString.length(); i++) {
            String tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString = bString + tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest.trim();
    }

    public static ArrayList<String> toStringArray(byte[] data) {
        int total_bytes;
        if (data != null && (total_bytes = data.length) >= 3) {
            int walkthrough = 0;
            ArrayList<String> result_strings = new ArrayList<>();
            while (walkthrough < total_bytes - 1) {
                int temp_len = (data[walkthrough] << 8) | data[walkthrough + 1];
                byte[] str_bytes = new byte[temp_len - 1];
                System.arraycopy(data, walkthrough + 2, str_bytes, 0, temp_len - 1);
                result_strings.add(new String(str_bytes));
                walkthrough += temp_len + 2;
            }
            return result_strings;
        }
        return null;
    }

    public static byte[] appendByteArray(byte[] src, byte[] data) {
        if (src.length > 0 && data.length > 0) {
            byte[] ret = new byte[src.length + data.length];
            System.arraycopy(src, 0, ret, 0, src.length);
            System.arraycopy(data, 0, ret, src.length, data.length);
            return ret;
        }
        throw new IllegalArgumentException("字节数组参数错误");
    }

    public static String calculateSingleFileMD5sum(File file) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);
        byte[] buff = new byte[256];
        while (true) {
            int readLen = fis.read(buff);
            if (readLen == -1) {
                break;
            }
            md5.update(buff, 0, readLen);
        }
        fis.close();
        StringBuilder sb = new StringBuilder();
        byte[] data = md5.digest();
        for (byte b : data) {
            sb.append(new Formatter().format("%02x", Byte.valueOf(b)));
        }
        return sb.toString();
    }

    public static String parseAscii(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        for (byte b : bs) {
            sb.append(toHex(b));
        }
        return sb.toString();
    }

    public static String toHex(int n) {
        StringBuilder sb = new StringBuilder();
        if (n / 16 == 0) {
            return toHexUtil(n);
        }
        String t = toHex(n / 16);
        int nn = n % 16;
        sb.append(t).append(toHexUtil(nn));
        return sb.toString();
    }

    private static String toHexUtil(int n) {
        switch (n) {
            case 10:
                String rt = "A";
                return rt;
            case 11:
                String rt2 = "B";
                return rt2;
            case 12:
                String rt3 = "C";
                return rt3;
            case 13:
                String rt4 = "D";
                return rt4;
            case 14:
                String rt5 = "E";
                return rt5;
            case 15:
                String rt6 = "F";
                return rt6;
            default:
                String rt7 = "" + n;
                return rt7;
        }
    }

    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    public static String byteToBit(byte b) {
        return "" + ((int) ((byte) ((b >> 7) & 1))) + ((int) ((byte) ((b >> 6) & 1))) + ((int) ((byte) ((b >> 5) & 1))) + ((int) ((byte) ((b >> 4) & 1))) + ((int) ((byte) ((b >> 3) & 1))) + ((int) ((byte) ((b >> 2) & 1))) + ((int) ((byte) ((b >> 1) & 1))) + ((int) ((byte) ((b >> 0) & 1)));
    }

    public static byte[] shortToBytes(short sNum) {
        byte[] bytesRet = {(byte) (sNum & 255), (byte) ((sNum >> 8) & 255)};
        return bytesRet;
    }
}
