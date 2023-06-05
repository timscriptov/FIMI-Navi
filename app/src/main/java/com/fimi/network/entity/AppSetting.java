package com.fimi.network.entity;


public class AppSetting extends BaseModel {
    String key;
    String value;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "AppSetting{key='" + this.key + "'" + ", value='" + this.value + "'" + '}';
    }
}
