package com.fimi.x8sdk.dataparser.cmd;


public class CmdAiAutoPhoto {
    public int angle;
    public int config;
    public int mode;
    public int routeLength;
    public int speed;

    public String toString() {
        return "{angle=" + this.angle + ", routeLength=" + this.routeLength + ", speed=" + this.speed + ", config=" + this.config + ", mode=" + this.mode + '}';
    }
}
