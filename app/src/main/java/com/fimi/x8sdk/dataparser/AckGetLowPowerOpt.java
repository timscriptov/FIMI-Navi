package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;



/* loaded from: classes2.dex */
public class AckGetLowPowerOpt extends X8BaseMessage {
    int lowPowerOpt;
    int lowPowerValue;
    int seriousLowPowerOpt;
    int seriousLowPowerValue;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.lowPowerValue = packet.getPayLoad4().getByte();
        this.seriousLowPowerValue = packet.getPayLoad4().getByte();
        this.lowPowerOpt = packet.getPayLoad4().getByte();
        this.seriousLowPowerOpt = packet.getPayLoad4().getByte();
    }

    public int getLowPowerValue() {
        return this.lowPowerValue;
    }

    public void setLowPowerValue(int lowPowerValue) {
        this.lowPowerValue = lowPowerValue;
    }

    public int getSeriousLowPowerValue() {
        return this.seriousLowPowerValue;
    }

    public void setSeriousLowPowerValue(int seriousLowPowerValue) {
        this.seriousLowPowerValue = seriousLowPowerValue;
    }

    public int getLowPowerOpt() {
        return this.lowPowerOpt;
    }

    public void setLowPowerOpt(int lowPowerOpt) {
        this.lowPowerOpt = lowPowerOpt;
    }

    public int getSeriousLowPowerOpt() {
        return this.seriousLowPowerOpt;
    }

    public void setSeriousLowPowerOpt(int seriousLowPowerOpt) {
        this.seriousLowPowerOpt = seriousLowPowerOpt;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckGetLowPowerOpt{lowPowerValue=" + this.lowPowerValue + ", seriousLowPowerValue=" + this.seriousLowPowerValue + ", lowPowerOpt=" + this.lowPowerOpt + ", seriousLowPowerOpt=" + this.seriousLowPowerOpt + '}';
    }
}
