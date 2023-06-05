package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AckSetCloudParams extends X8BaseMessage {
    private int data0;
    private double param1;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.data0 = packet.getPayLoad4().getByte();
        this.param1 = packet.getPayLoad4().getFloat();
    }

    public int getData0() {
        return this.data0;
    }

    public double getParam1() {
        return this.param1;
    }

    public void setParam1(double param1) {
        this.param1 = param1;
    }
}
