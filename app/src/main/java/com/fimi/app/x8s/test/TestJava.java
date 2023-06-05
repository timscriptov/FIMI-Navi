package com.fimi.app.x8s.test;

import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class TestJava {
    public static void main(String[] args) {
        byte[] arr = {51, 51, -64, -64};
        System.out.println(byte2float(arr, 0));
        System.out.println(2.0f);
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        byte[] bytes = {(byte) (intBits & 255), (byte) ((65280 & intBits) >> 8), (byte) ((16711680 & intBits) >> 16), (byte) (((-16777216) & intBits) >> 24)};
        return bytes;
    }

    public static float byteToFloat(byte[] arr, int index) {
        int i = ((-16777216) & (arr[index + 0] << 24)) | (16711680 & (arr[index + 1] << 16)) | (65280 & (arr[index + 2] << 8)) | (arr[index + 3] & 255);
        return Float.intBitsToFloat(i);
    }

    public static float byte2float(byte[] b, int index) {
        int l = b[index + 0];
        return Float.intBitsToFloat((int) ((((int) ((((int) ((l & 255) | (b[index + 1] << 8))) & 65535) | (b[index + 2] << 16))) & ViewCompat.MEASURED_SIZE_MASK) | (b[index + 3] << 24)));
    }
}
