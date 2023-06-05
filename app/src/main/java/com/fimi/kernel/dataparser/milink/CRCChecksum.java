package com.fimi.kernel.dataparser.milink;


public class CRCChecksum {
    public static final int X25_INIT_CRC = 65535;

    public static int crc16_calculate(byte[] pBuffer, int length) {
        int crcTmp = 65535;
        for (int i = 0; i < length; i++) {
            crcTmp = crc16_accumulate(pBuffer[i] & 255, 65535 & crcTmp);
        }
        return crcTmp;
    }

    static int crc16_accumulate(int data, int crcAccum) {
        int a = crcAccum & 255;
        int tmp = data ^ a;
        int tmp2 = (tmp ^ (tmp << 4)) & 255;
        return (((crcAccum >> 8) ^ (tmp2 << 8)) ^ (tmp2 << 3)) ^ (tmp2 >> 4);
    }

    public static void main(String[] args) {
        byte[] x = {-2, 3, 32, 0, 3, 0, 8, 1, 0, 0, 0, 0, 0, 0};
        crc16_calculate(x, 14);
    }
}
