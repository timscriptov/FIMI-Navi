package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AckCloudParams extends X8BaseMessage {
    private int data0;
    private double param1;
    private double param2;
    private double param3;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.data0 = packet.getPayLoad4().getByte();
        this.param1 = packet.getPayLoad4().getFloat();
        this.param2 = packet.getPayLoad4().getFloat();
        this.param3 = packet.getPayLoad4().getFloat();
    }

    public double getParam1() {
        return this.param1;
    }

    public void setParam1(double param1) {
        this.param1 = param1;
    }

    public double getParam2() {
        return this.param2;
    }

    public void setParam2(double param2) {
        this.param2 = param2;
    }

    public double getParam3() {
        return this.param3;
    }

    public void setParam3(double param3) {
        this.param3 = param3;
    }
}
