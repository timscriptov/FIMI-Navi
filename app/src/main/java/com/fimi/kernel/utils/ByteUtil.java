package com.fimi.kernel.utils;

import android.annotation.SuppressLint;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.core.view.ViewCompat;

import com.fimi.kernel.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint({"UseValueOf", "DefaultLocale"})
/* loaded from: classes.dex */
public class ByteUtil {
    private static ByteBuffer buffer = ByteBuffer.allocate(8);

    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 255).byteValue();
            temp >>= 8;
        }
        return b;
    }

    public static short byteToShort(byte[] b, int index) {
        short s0 = (short) (b[index] & 255);
        short s1 = (short) (b[index + 1] & 255);
        short s = (short) (s0 | ((short) (s1 << 8)));
        return s;
    }

    public static char byteToChar(byte[] b, int index) {
        char c = (char) (((b[index] & 255) << 8) | (b[index + 1] & 255));
        return c;
    }

    public static String byteToString(byte[] data) {
        ByteBuffer bbuf = ByteBuffer.allocate(data.length);
        for (int i = 0; i < data.length; i++) {
            bbuf.put(data[i + 0]);
        }
        return new String(bbuf.array());
    }

    public static byte[] getByteArray(byte[] cmd, int start, int length) {
        byte[] data = new byte[length];
        if (length > 0) {
            try {
                System.arraycopy(cmd, start, data, 0, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

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
        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString2(byte[] src) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < src.length; i++) {
            sb.append(Character.forDigit((src[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(src[i] & 15, 16));
        }
        return sb.toString();
    }

    @SuppressLint({"DefaultLocale"})
    public static byte[] hexStringToBytes(String hexString) {
        byte[] d;
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        String hexString2 = hexString.toUpperCase();
        int length = hexString2.length() / 2;
        char[] hexChars = hexString2.toCharArray();
        if (length == 1) {
            d = new byte[2];
        } else {
            d = new byte[length];
        }
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        if (d.length == 1) {
            d[1] = d[0];
            d[0] = 0;
            return d;
        }
        return d;
    }

    public static int getUnsignedByte(byte cmd) {
        if (cmd >= 0) {
            return cmd;
        }
        int c = cmd + 256;
        return c;
    }

    public static int get2ByteToInt(byte low, byte high) {
        int b = (high << 8) | getUnsignedByte(low);
        return b;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    @SuppressLint({"DefaultLocale"})
    public static byte hexStringToByte(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return (byte) 0;
        }
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        char[] hexChars = hexString.toUpperCase().toCharArray();
        return (byte) ((charToByte(hexChars[0]) << 4) | charToByte(hexChars[1]));
    }

    public static byte getCalibration(byte[] cmd) {
        byte total = 0;
        for (int i = 0; i < cmd.length - 1; i++) {
            total = (byte) (cmd[i] + total);
        }
        return total;
    }

    public static byte getCalibrationAll(byte[] cmd) {
        byte total = 0;
        for (byte b : cmd) {
            total = (byte) (b + total);
        }
        return total;
    }

    public static byte[] float2byte(float f) {
        int fbit = Float.floatToIntBits(f);
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - (i * 8)));
        }
        int len = b.length;
        byte[] dest = new byte[len];
        System.arraycopy(b, 0, dest, 0, len);
        for (int i2 = 0; i2 < len / 2; i2++) {
            byte temp = dest[i2];
            dest[i2] = dest[(len - i2) - 1];
            dest[(len - i2) - 1] = temp;
        }
        return dest;
    }

    public static float byte2float(byte[] b, int index) {
        int l = b[index + 0];
        return Float.intBitsToFloat((int) ((((int) ((((int) ((l & 255) | (b[index + 1] << 8))) & 65535) | (b[index + 2] << 16))) & ViewCompat.MEASURED_SIZE_MASK) | (b[index + 3] << 24)));
    }

    public static long longFrom8Bytes(byte[] input, int offset, boolean littleEndian) {
        long value = 0;
        for (int count = 0; count < 8; count++) {
            int shift = (littleEndian ? count : 7 - count) << 3;
            value |= (255 << shift) & (input[offset + count] << shift);
        }
        return value;
    }

    public static byte[] intToByte(int intValue) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) ((intValue >> (i * 8)) & 255);
        }
        return b;
    }

    public static byte[] intToByteArray(int i) {
        byte[] result = {(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
        return result;
    }

    public static int byteToInt(byte[] b) {
        int n = 0;
        for (int i = 0; i < 4; i++) {
            int temp = b[i] & 255;
            n = (n << 8) | temp;
        }
        return n;
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value = (src[offset] & 255) | ((src[offset + 1] & 255) << 8) | ((src[offset + 2] & 255) << 16) | ((src[offset + 3] & 255) << 24);
        return value;
    }

    public static byte[] shortToByteArray(short s) {
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = ((shortBuf.length - 1) - i) * 8;
            shortBuf[i] = (byte) ((s >>> offset) & 255);
        }
        return shortBuf;
    }

    public static int get2UnsignedByteToInt(byte[] cmd, int start) {
        int a = getUnsignedByte(cmd[start + 1]);
        int b = getUnsignedByte(cmd[start]);
        int c = (a << 8) | b;
        return c;
    }

    public static String byteToBitString(byte b) {
        return "" + ((int) ((byte) ((b >> 7) & 1))) + ((int) ((byte) ((b >> 6) & 1))) + ((int) ((byte) ((b >> 5) & 1))) + ((int) ((byte) ((b >> 4) & 1))) + ((int) ((byte) ((b >> 3) & 1))) + ((int) ((byte) ((b >> 2) & 1))) + ((int) ((byte) ((b >> 1) & 1))) + ((int) ((byte) ((b >> 0) & 1)));
    }

    public static byte[] toLH(int n) {
        byte[] b = {(byte) (n & 255), (byte) ((n >> 8) & 255), (byte) ((n >> 16) & 255), (byte) ((n >> 24) & 255)};
        return b;
    }

    public static byte[] toHL(int n) {
        byte[] b = {(byte) ((n >> 24) & 255), (byte) ((n >> 16) & 255), (byte) ((n >> 8) & 255), (byte) (n & 255)};
        return b;
    }

    public static byte[] toHL(long n) {
        byte[] b = {(byte) ((n >> 56) & 255), (byte) ((n >> 48) & 255), (byte) ((n >> 40) & 255), (byte) ((n >> 32) & 255), (byte) ((n >> 24) & 255), (byte) ((n >> 16) & 255), (byte) ((n >> 8) & 255), (byte) (n & 255)};
        return b;
    }

    public static byte[] longToBytes(long x) {
        buffer.clear();
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ix++) {
            num = (num << 8) | (byteNum[ix] & 255);
        }
        return num;
    }

    public static int getLHByteToInt(byte[] cmd, int start) {
        int b = (cmd[start + 3] << 24) | (getUnsignedByte(cmd[start + 2]) << 16) | (getUnsignedByte(cmd[start + 1]) << 8) | getUnsignedByte(cmd[start]);
        return b;
    }

    public static int get4ByteToInt(byte[] cmd, int start) {
        int b = (cmd[start + 3] << 24) | (getUnsignedByte(cmd[start + 2]) << 16) | (getUnsignedByte(cmd[start + 1]) << 8) | getUnsignedByte(cmd[start]);
        return b;
    }

    public static final int crc16CheckSum(byte[] data) {
        int crc = 65535;
        for (int i = 0; i < data.length - 2; i++) {
            int tmp = data[i] ^ (crc & 255);
            int tmp2 = tmp ^ ((tmp & 255) << 4);
            crc = (((crc >> 8) ^ ((tmp2 & 255) << 8)) ^ ((tmp2 & 255) << 3)) ^ ((tmp2 & 255) >> 4);
        }
        return crc;
    }

    public static final int crcAddCheckSum(byte[] data) {
        if (data.length < 3) {
            return 0;
        }
        int crc = 0;
        for (byte b : getByteArray(data, 1, data.length - 3)) {
            crc += getUnsignedByte(b);
        }
        return crc;
    }

    public static boolean isCheckSumCorrect(byte[] cmd) {
        byte[] cmdCheckSum = intToByte(CRCUtil.crc16Calculate(cmd, Constants.CRC16_LENGTH));
        byte[] originalCheckSum = getByteArray(cmd, 14, 2);
        return cmdCheckSum[0] == originalCheckSum[0] && cmdCheckSum[1] == originalCheckSum[1];
    }

    public static boolean isCheckSumCorrectPayload(byte[] cmd) {
        int[] crcInts = CRCUtil.bytesToInts(Arrays.copyOfRange(cmd, 20, cmd.length));
        int crcL = (int) CRCUtil.calcCRC32(crcInts, crcInts.length);
        byte[] crc32s = longToBytes(crcL);
        byte[] originalCheckSum = Arrays.copyOfRange(cmd, 16, 20);
        return crc32s[0] == originalCheckSum[0] && crc32s[1] == originalCheckSum[1] && crc32s[2] == originalCheckSum[2] && crc32s[3] == originalCheckSum[3];
    }

    public static String toHex(byte b) {
        String result = Integer.toHexString(b & 255);
        if (result.length() == 1) {
            return '0' + result;
        }
        return result;
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static Object[] splitAry(byte[] ary, int subSize) {
        int count = ary.length % subSize == 0 ? ary.length / subSize : (ary.length / subSize) + 1;
        List<List<Byte>> subAryList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int index = i * subSize;
            List<Byte> list = new ArrayList<>();
            int j = 0;
            for (int index2 = index; j < subSize && index2 < ary.length; index2++) {
                list.add(Byte.valueOf(ary[index2]));
                j++;
            }
            subAryList.add(list);
        }
        Object[] subAry = new Object[subAryList.size()];
        for (int i2 = 0; i2 < subAryList.size(); i2++) {
            List<Byte> subList = subAryList.get(i2);
            byte[] subAryItem = new byte[subList.size()];
            for (int j2 = 0; j2 < subList.size(); j2++) {
                subAryItem[j2] = subList.get(j2).byteValue();
            }
            subAry[i2] = subAryItem;
        }
        return subAry;
    }

    public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if (bytes == null || bytes.length == 0 || offset < 0 || dateLen <= 0 || offset >= bytes.length || bytes.length - offset < dateLen) {
            return null;
        }
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {
            String asciiStr = new String(data, "ISO8859-1");
            return asciiStr;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static int getHeight4(byte data) {
        int height = (data & 240) >> 4;
        return height;
    }

    public static int getLow4(byte data) {
        int low = data & 15;
        return low;
    }

    public static int getByte6(byte data) {
        int height = (data & 240) >> 4;
        return height;
    }

    public static int getByte8(byte data) {
        int height = (data & 240) >> 8;
        return height;
    }

    public static String deletTailChar0(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int ascii : data) {
            if (ascii == 0) {
                break;
            }
            char ch2 = (char) ascii;
            sb.append(ch2);
        }
        return sb.toString();
    }

    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1073741824) {
            double i = size / 1.073741824E9d;
            bytes.append(format.format(i)).append("GB");
        } else if (size >= PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            double i2 = size / 1048576.0d;
            bytes.append(format.format(i2)).append("MB");
        } else if (size >= 1024) {
            double i3 = size / 1024.0d;
            bytes.append(format.format(i3)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    public static String int2HexString(int originalValue) {
        byte[] bytes = intToByteArray(originalValue);
        String hexStr = bytesToHexString(bytes);
        return hexStr;
    }

    public static byte[] readFileToByte(File file) {
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[0];
        try {
            try {
                FileInputStream fileInputStream2 = new FileInputStream(file);
                try {
                    bytes = new byte[fileInputStream2.available()];
                    fileInputStream2.read(bytes);
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e2) {
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    return bytes;
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
            }
        } catch (Exception e5) {
        }
        return bytes;
    }

    public int bytesToInt2(byte[] src, int offset) {
        int value = ((src[offset] & 255) << 24) | ((src[offset + 1] & 255) << 16) | ((src[offset + 2] & 255) << 8) | (src[offset + 3] & 255);
        return value;
    }
}
