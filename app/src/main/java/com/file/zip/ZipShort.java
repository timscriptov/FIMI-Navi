package com.file.zip;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

/* loaded from: classes.dex */
public final class ZipShort implements Cloneable {
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private final int value;

    public ZipShort(int value) {
        this.value = value;
    }

    public ZipShort(byte[] bytes) {
        this(bytes, 0);
    }

    public ZipShort(byte[] bytes, int offset) {
        this.value = getValue(bytes, offset);
    }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static byte[] getBytes(int value) {
        return new byte[]{(byte) (value & 255), (byte) ((65280 & value) >> 8)};
    }

    @Contract(pure = true)
    public static int getValue(@NonNull byte[] bytes, int offset) {
        int value = (bytes[offset + 1] << 8) & 65280;
        return value + (bytes[offset] & 255);
    }

    public static int getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public byte[] getBytes() {
        return new byte[]{(byte) (this.value & 255), (byte) ((this.value & 65280) >> 8)};
    }

    public int getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        return o != null && (o instanceof ZipShort) && this.value == ((ZipShort) o).getValue();
    }

    public int hashCode() {
        return this.value;
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
    @Contract(pure = true)
    public String toString() {
        return "ZipShort value: " + this.value;
    }
}
