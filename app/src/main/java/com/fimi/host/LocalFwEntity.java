package com.fimi.host;

import java.io.Serializable;



/* loaded from: classes.dex */
public class LocalFwEntity implements Serializable {
    private long logicVersion;
    private int model;
    private int type;
    private String userVersion;

    public LocalFwEntity() {
    }

    public LocalFwEntity(int type, int model, long logicVersion, String userVersion) {
        this.type = type;
        this.model = model;
        this.logicVersion = logicVersion;
        this.userVersion = userVersion;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getModel() {
        return this.model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public long getLogicVersion() {
        return this.logicVersion;
    }

    public void setLogicVersion(long logicVersion) {
        this.logicVersion = logicVersion;
    }

    public String getUserVersion() {
        return this.userVersion;
    }

    public void setUserVersion(String userVersion) {
        this.userVersion = userVersion;
    }

    public String toString() {
        return "LocalFwEntity{type=" + this.type + ", model=" + this.model + ", logicVersion=" + this.logicVersion + ", userVersion='" + this.userVersion + "'" + '}';
    }
}
