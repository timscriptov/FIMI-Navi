package com.file.zip;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.math.BigInteger;

public final class ZipEightByteInteger {
    public static final ZipEightByteInteger ZERO = new ZipEightByteInteger(0);
    private static final int BYTE_1 = 1;
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private static final int BYTE_2 = 2;
    private static final int BYTE_2_MASK = 16711680;
    private static final int BYTE_2_SHIFT = 16;
    private static final int BYTE_3 = 3;
    private static final long BYTE_3_MASK = 4278190080L;
    private static final int BYTE_3_SHIFT = 24;
    private static final int BYTE_4 = 4;
    private static final long BYTE_4_MASK = 1095216660480L;
    private static final int BYTE_4_SHIFT = 32;
    private static final int BYTE_5 = 5;
    private static final long BYTE_5_MASK = 280375465082880L;
    private static final int BYTE_5_SHIFT = 40;
    private static final int BYTE_6 = 6;
    private static final long BYTE_6_MASK = 71776119061217280L;
    private static final int BYTE_6_SHIFT = 48;
    private static final int BYTE_7 = 7;
    private static final long BYTE_7_MASK = 9151314442816847872L;
    private static final int BYTE_7_SHIFT = 56;
    private static final byte LEFTMOST_BIT = Byte.MIN_VALUE;
    private static final int LEFTMOST_BIT_SHIFT = 63;
    private final BigInteger value;

    public ZipEightByteInteger(long value) {
        this(BigInteger.valueOf(value));
    }

    public ZipEightByteInteger(BigInteger value) {
        this.value = value;
    }

    public ZipEightByteInteger(byte[] bytes) {
        this(bytes, 0);
    }

    public ZipEightByteInteger(byte[] bytes, int offset) {
        this.value = getValue(bytes, offset);
    }

    public static byte[] getBytes(long value) {
        return getBytes(BigInteger.valueOf(value));
    }

    public static byte[] getBytes(@NonNull BigInteger value) {
        long val = value.longValue();
        byte[] result = {(byte) (255 & val), (byte) ((65280 & val) >> 8), (byte) ((16711680 & val) >> 16), (byte) ((BYTE_3_MASK & val) >> 24), (byte) ((BYTE_4_MASK & val) >> 32), (byte) ((BYTE_5_MASK & val) >> 40), (byte) ((BYTE_6_MASK & val) >> 48), (byte) ((BYTE_7_MASK & val) >> 56)};
        if (value.testBit(63)) {
            result[7] = (byte) (result[7] | LEFTMOST_BIT);
        }
        return result;
    }

    public static long getLongValue(byte[] bytes, int offset) {
        return getValue(bytes, offset).longValue();
    }

    public static BigInteger getValue(@NonNull byte[] bytes, int offset) {
        long value = ((long) bytes[offset + 7] << 56) & BYTE_7_MASK;
        BigInteger val = BigInteger.valueOf(value + (((long) bytes[offset + 6] << 48) & BYTE_6_MASK) + (((long) bytes[offset + 5] << 40) & BYTE_5_MASK) + (((long) bytes[offset + 4] << 32) & BYTE_4_MASK) + ((bytes[offset + 3] << 24) & BYTE_3_MASK) + ((bytes[offset + 2] << 16) & 16711680) + ((bytes[offset + 1] << 8) & 65280) + (bytes[offset] & 255));
        if ((bytes[offset + 7] & LEFTMOST_BIT) != -128) {
            return val;
        }
        return val.setBit(63);
    }

    public static long getLongValue(byte[] bytes) {
        return getLongValue(bytes, 0);
    }

    public static BigInteger getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    public byte[] getBytes() {
        return getBytes(this.value);
    }

    public long getLongValue() {
        return this.value.longValue();
    }

    public BigInteger getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ZipEightByteInteger)) {
            return false;
        }
        return this.value.equals(((ZipEightByteInteger) o).getValue());
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    @NonNull
    @Contract(pure = true)
    public String toString() {
        return "ZipEightByteInteger value: " + this.value;
    }
}
