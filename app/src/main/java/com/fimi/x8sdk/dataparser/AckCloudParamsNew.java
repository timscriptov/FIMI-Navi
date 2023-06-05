package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckCloudParamsNew extends X8BaseMessage {
    private double param1;
    private double param2;
    private double param3;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.param1 = packet.getPayLoad4().getByte();
        this.param2 = packet.getPayLoad4().getByte();
        this.param3 = packet.getPayLoad4().getByte();
    }

    public double getParam1() {
        return this.param1 / 10.0d;
    }

    public void setParam1(double param1) {
        this.param1 = param1;
    }

    public double getParam2() {
        return this.param2 / 10.0d;
    }

    public void setParam2(double param2) {
        this.param2 = param2;
    }

    public double getParam3() {
        return this.param3 / 10.0d;
    }

    public void setParam3(double param3) {
        this.param3 = param3;
    }
}
