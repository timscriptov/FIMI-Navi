package com.fimi.kernel.dataparser.medialink;

import java.math.BigInteger;
import java.nio.ByteBuffer;


public class MediaPayLoad {
    public static final int MAX_PAYLOAD_SIZE = 1024;
    public int index;
    public ByteBuffer payload = ByteBuffer.allocate(1024);
    private int cmdType;

    public ByteBuffer getData() {
        return this.payload;
    }

    public int size() {
        return this.payload.position();
    }

    public void add(byte c) {
        this.payload.put(c);
    }

    public void resetIndex() {
        this.index = 0;
    }

    public byte getByte() {
        byte result = (byte) ((this.payload.get(this.index) & 255) | 0);
        this.index++;
        return result;
    }

    public short getShort() {
        short result = (short) (((this.payload.get(this.index + 1) & 255) << 8) | 0);
        short result2 = (short) ((this.payload.get(this.index) & 255) | result);
        this.index += 2;
        return result2;
    }

    public int getInt() {
        int result = 0 | ((this.payload.get(this.index + 3) & 255) << 24);
        int result2 = result | ((this.payload.get(this.index + 2) & 255) << 16) | ((this.payload.get(this.index + 1) & 255) << 8) | (this.payload.get(this.index) & 255);
        this.index += 4;
        return result2;
    }

    public long getLong() {
        long result = 0 | ((long) (this.payload.get(this.index + 7) & 255) << 56);
        long result2 = result | ((long) (this.payload.get(this.index + 6) & 255) << 48) | ((long) (this.payload.get(this.index + 5) & 255) << 40) | ((long) (this.payload.get(this.index + 4) & 255) << 32) | ((long) (this.payload.get(this.index + 3) & 255) << 24) | ((this.payload.get(this.index + 2) & 255) << 16) | ((this.payload.get(this.index + 1) & 255) << 8) | (this.payload.get(this.index) & 255);
        this.index += 8;
        return result2;
    }

    public long getLongReverse() {
        long result = 0 | ((long) (this.payload.get(this.index) & 255) << 56);
        long result2 = result | ((long) (this.payload.get(this.index + 1) & 255) << 48) | ((long) (this.payload.get(this.index + 2) & 255) << 40) | ((long) (this.payload.get(this.index + 3) & 255) << 32) | ((long) (this.payload.get(this.index + 4) & 255) << 24) | ((this.payload.get(this.index + 5) & 255) << 16) | ((this.payload.get(this.index + 6) & 255) << 8) | (this.payload.get(this.index + 7) & 255);
        this.index += 8;
        return result2;
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public float getThreeFloat() {
        byte result = (byte) ((this.payload.get(this.index + 2) & 255) | 0);
        byte result2 = (byte) ((this.payload.get(this.index + 1) & 255) | 0);
        byte result3 = (byte) ((this.payload.get(this.index) & 255) | 0);
        byte[] value = {result, result2, result3};
        BigInteger bigInteger = new BigInteger(1, value);
        this.index += 3;
        return Float.parseFloat(bigInteger.toString());
    }

    public Double getDouble() {
        return Double.valueOf(Double.longBitsToDouble(getLong()));
    }

    public void putByte(byte data) {
        add(data);
    }

    public void putShort(short data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
    }

    public void putChar(char data) {
        add((byte) (data >> 0));
        add((byte) (data >> '\b'));
    }

    public char getChar() {
        char result = (char) (((this.payload.get(this.index + 1) & 255) << 8) | 0);
        char result2 = (char) ((this.payload.get(this.index) & 255) | result);
        this.index += 2;
        return result2;
    }

    public void putuInt32(long data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
        add((byte) (data >> 24));
    }

    public long getuInt32() {
        long result = 0 | ((long) (this.payload.get(this.index + 3) & 255) << 24);
        long result2 = result | ((this.payload.get(this.index + 2) & 255) << 16) | ((this.payload.get(this.index + 1) & 255) << 8) | (this.payload.get(this.index) & 255);
        this.index += 4;
        return result2;
    }

    public void putThreeByte(int data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
    }

    public void putInt(int data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
        add((byte) (data >> 24));
    }

    public void putLong(long data) {
        add((byte) (data >> 0));
        add((byte) (data >> 8));
        add((byte) (data >> 16));
        add((byte) (data >> 24));
        add((byte) (data >> 32));
        add((byte) (data >> 40));
        add((byte) (data >> 48));
        add((byte) (data >> 56));
    }

    public void putFloat(float data) {
        putInt(Float.floatToIntBits(data));
    }

    public void putDouble(double data) {
        putLong(Double.doubleToLongBits(data));
    }

    public void setIndex(int x) {
        this.index = x;
    }

    public void putBytes(byte[] data) {
        this.payload.put(data);
    }

    public byte[] getPayloadData() {
        int len = size();
        byte[] bytes = new byte[len];
        System.arraycopy(this.payload.array(), 0, bytes, 0, len);
        return bytes;
    }

    public int getCmdType() {
        return this.cmdType;
    }

    public void setCmdType(int cmdType) {
        this.cmdType = cmdType;
    }
}
