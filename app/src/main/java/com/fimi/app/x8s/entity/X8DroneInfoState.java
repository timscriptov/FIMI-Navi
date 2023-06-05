package com.fimi.app.x8s.entity;

import com.fimi.app.x8s.controls.fcsettting.X8DroneInfoStateController;

/* loaded from: classes.dex */
public class X8DroneInfoState {
    private int errorEvent;
    private String info;
    private X8DroneInfoStateController.Mode mode;
    private String name;
    private X8DroneInfoStateController.State state;

    public X8DroneInfoStateController.State getState() {
        return this.state;
    }

    public void setState(X8DroneInfoStateController.State state) {
        this.state = state;
    }

    public X8DroneInfoStateController.Mode getMode() {
        return this.mode;
    }

    public void setMode(X8DroneInfoStateController.Mode mode) {
        this.mode = mode;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getErrorEvent() {
        return this.errorEvent;
    }

    public void setErrorEvent(int errorEvent) {
        this.errorEvent = errorEvent;
    }
}
