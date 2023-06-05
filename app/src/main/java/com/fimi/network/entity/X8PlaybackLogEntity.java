package com.fimi.network.entity;

import java.io.Serializable;

/* loaded from: classes.dex */
public class X8PlaybackLogEntity implements Serializable {
    private long createTime;
    private String fimiId;
    private String flieName;
    private long flightTime;
    private int flyDistance;
    private int flyDuration;
    private int id;
    private int logFileSize;
    private String logFileUrl;

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFimiId() {
        return this.fimiId;
    }

    public void setFimiId(String fimiId) {
        this.fimiId = fimiId;
    }

    public long getFlightTime() {
        return this.flightTime;
    }

    public void setFlightTime(long flightTime) {
        this.flightTime = flightTime;
    }

    public int getFlyDistance() {
        return this.flyDistance;
    }

    public void setFlyDistance(int flyDistance) {
        this.flyDistance = flyDistance;
    }

    public int getFlyDuration() {
        return this.flyDuration;
    }

    public void setFlyDuration(int flyDuration) {
        this.flyDuration = flyDuration;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLogFileSize() {
        return this.logFileSize;
    }

    public void setLogFileSize(int logFileSize) {
        this.logFileSize = logFileSize;
    }

    public String getLogFileUrl() {
        return this.logFileUrl;
    }

    public void setLogFileUrl(String logFileUrl) {
        this.logFileUrl = logFileUrl;
    }

    public String getFlieName() {
        return this.flieName;
    }

    public void setFlieName(String flieName) {
        this.flieName = flieName;
    }
}
