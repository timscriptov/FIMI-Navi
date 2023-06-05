package com.fimi.kernel.utils;

import java.text.DecimalFormat;


public class NumberUtil {
    public static String decimalPointStr(double decimal, int number) {
        String str;
        DecimalFormat df;
        try {
            try {
                if (number == 1) {
                    df = new DecimalFormat("0.0");
                    str = df.format(decimal).replace(",", ".");
                } else if (number == 2) {
                    df = new DecimalFormat("0.00");
                    str = df.format(decimal).replace(",", ".");
                } else if (number == 4) {
                    df = new DecimalFormat("0.0000");
                    str = df.format(decimal).replace(",", ".");
                } else if (number == 7) {
                    df = new DecimalFormat("0.0000000");
                    str = df.format(decimal).replace(",", ".");
                } else {
                    str = decimal + "";
                    return str;
                }
                return str;
            } catch (Exception e) {
                e = e;
                e.printStackTrace();
                return "0";
            }
        } catch (Exception e2) {
            return "0";
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 255;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
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

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static float convertToFloat(String value, int defaultValue) {
        if (value == null || "".equals(value.trim())) {
            return defaultValue;
        }
        try {
            return Float.valueOf(value).floatValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Double convertToDouble(String value, Double defaultValue) {
        if (value != null && !"".equals(value.trim())) {
            try {
                return Double.valueOf(value);
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
