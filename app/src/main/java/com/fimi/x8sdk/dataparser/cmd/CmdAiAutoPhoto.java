package com.fimi.x8sdk.dataparser.cmd;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class CmdAiAutoPhoto {
    public int angle;
    public int config;
    public int mode;
    public int routeLength;
    public int speed;

    public String toString() {
        return "{angle=" + this.angle + ", routeLength=" + this.routeLength + ", speed=" + this.speed + ", config=" + this.config + ", mode=" + this.mode + CoreConstants.CURLY_RIGHT;
    }
}
