package com.fimi.kernel.utils;

import com.fimi.x8sdk.command.FcCollection;

import java.util.ArrayList;
import java.util.List;


public class CRCUtil {
    public static final int X25_INIT_CRC = 65535;

    public static long calcCRC32(int[] ptr, int len) {
        long crc = -1;
        int k = 0;
        while (true) {
            int k2 = k;
            int len2 = len;
            len = len2 - 1;
            if (len2 > 0) {
                long xbit = 2147483648L;
                k = k2 + 1;
                long data = ptr[k2];
                for (long bits = 0; bits < 32; bits++) {
                    if ((2147483648L & crc) == 2147483648L) {
                        crc = ((crc << 1) & 4294967295L) ^ 79764919;
                    } else {
                        crc = (crc << 1) & 4294967295L;
                    }
                    if ((data & xbit) == xbit) {
                        crc ^= 79764919;
                    }
                    xbit >>= 1;
                }
            } else {
                return crc;
            }
        }
    }

    public static int[] bytesToInts(byte[] bylength) {
        int length = bylength.length;
        List<byte[]> listbyte = new ArrayList<>();
        byte[] item = new byte[4];
        for (int y = 0; y < bylength.length; y += 4) {
            if (bylength.length - y < 4 && bylength.length % 4 == 1) {
                item[0] = bylength[y];
                item[1] = 0;
                item[2] = 0;
                item[3] = 0;
            } else if (bylength.length - y < 4 && bylength.length % 4 == 2) {
                item[0] = bylength[y];
                item[1] = bylength[y + 1];
                item[2] = 0;
                item[3] = 0;
            } else if (bylength.length - y < 4 && bylength.length % 4 == 3) {
                item[0] = bylength[y];
                item[1] = bylength[y + 1];
                item[2] = bylength[y + 2];
                item[3] = 0;
            } else {
                item[0] = bylength[y];
                item[1] = bylength[y + 1];
                item[2] = bylength[y + 2];
                item[3] = bylength[y + 3];
            }
            listbyte.add(trans(item));
            item = new byte[4];
        }
        int[] arrayitem = new int[listbyte.size()];
        for (int x = 0; x < listbyte.size(); x++) {
            arrayitem[x] = byteArrayToInt(listbyte.get(x));
        }
        return arrayitem;
    }

    public static byte[] trans(byte[] by) {
        byte[] bx = new byte[4];
        for (int x = 0; x < by.length; x++) {
            bx[x] = by[(by.length - x) - 1];
        }
        return bx;
    }

    public static int byteArrayToInt(byte[] b) {
        return (b[3] & 255) | ((b[2] & 255) << 8) | ((b[1] & 255) << 16) | ((b[0] & 255) << 24);
    }

    public static int crc16Calculate(byte[] pBuffer, int length) {
        int crcTmp = 65535;
        for (int i = 0; i < length; i++) {
            crcTmp = crc16Accumulate(pBuffer[i] & 255, 65535 & crcTmp);
        }
        return crcTmp;
    }

    private static int crc16Accumulate(int data, int crcAccum) {
        int a = crcAccum & 255;
        int tmp = data ^ a;
        int tmp2 = (tmp ^ (tmp << 4)) & 255;
        return (((crcAccum >> 8) ^ (tmp2 << 8)) ^ (tmp2 << 3)) ^ (tmp2 >> 4);
    }

    public static void main(String[] args) {
        byte[] cmd = {9, 32, -44, 1, FcCollection.MSG_SET_SCREW_RESUME, 13};
        int[] crcInts = bytesToInts(cmd);
        long crcL = calcCRC32(crcInts, crcInts.length);
        byte[] crc32s = ByteUtil.longToBytes(crcL);
        System.out.println(ByteUtil.bytesToHexString(crc32s) + "================");
    }
}
