package com.fimi.x8sdk.entity;


public class X8ErrorCodeInfo {
    private int index;
    private int type;
    private int value;

    public X8ErrorCodeInfo(int type, int index) {
        this.type = type;
        this.index = index;
    }

    public X8ErrorCodeInfo(int type, int value, boolean b) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString() {
        return "X8ErrorCodeInfo{type=" + this.type + ", index=" + this.index + '}';
    }
}
