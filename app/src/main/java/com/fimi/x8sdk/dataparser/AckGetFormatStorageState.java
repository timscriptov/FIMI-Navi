package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class AckGetFormatStorageState extends X8BaseMessage {
    private int devid;
    private int process;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.devid = packet.getPayLoad4().getByte();
        this.process = packet.getPayLoad4().getByte();
    }

    public int getDevid() {
        return this.devid;
    }

    public void setDevid(int devid) {
        this.devid = devid;
    }

    public int getProcess() {
        return this.process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckGetFormatStorageState{devid=" + this.devid + ", process=" + this.process + CoreConstants.CURLY_RIGHT;
    }
}
