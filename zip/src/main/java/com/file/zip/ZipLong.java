package com.file.zip;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public final class ZipLong implements Cloneable {
    public static final ZipLong CFH_SIG = new ZipLong(33639248);
    public static final ZipLong LFH_SIG = new ZipLong(67324752);
    public static final ZipLong DD_SIG = new ZipLong(134695760);
    static final ZipLong ZIP64_MAGIC = new ZipLong(4294967295L);
    private static final int BYTE_1 = 1;
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private static final int BYTE_2 = 2;
    private static final int BYTE_2_MASK = 16711680;
    private static final int BYTE_2_SHIFT = 16;
    private static final int BYTE_3 = 3;
    private static final long BYTE_3_MASK = 4278190080L;
    private static final int BYTE_3_SHIFT = 24;
    private final long value;

    public ZipLong(long value) {
        this.value = value;
    }

    public ZipLong(byte[] bytes) {
        this(bytes, 0);
    }

    public ZipLong(byte[] bytes, int offset) {
        this.value = getValue(bytes, offset);
    }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static byte[] getBytes(long value) {
        return new byte[]{(byte) (255 & value), (byte) ((65280 & value) >> 8), (byte) ((16711680 & value) >> 16), (byte) ((BYTE_3_MASK & value) >> 24)};
    }

    @Contract(pure = true)
    public static long getValue(@NonNull byte[] bytes, int offset) {
        long value = (bytes[offset + 3] << 24) & BYTE_3_MASK;
        return value + ((bytes[offset + 2] << 16) & BYTE_2_MASK) + ((bytes[offset + 1] << 8) & 65280) + (bytes[offset] & 255);
    }

    public static long getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public byte[] getBytes() {
        return getBytes(this.value);
    }

    public long getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        return o != null && (o instanceof ZipLong) && this.value == ((ZipLong) o).getValue();
    }

    public int hashCode() {
        return (int) this.value;
    }

    @NonNull
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }

    @NonNull
    public String toString() {
        return "ZipLong value: " + this.value;
    }
}
