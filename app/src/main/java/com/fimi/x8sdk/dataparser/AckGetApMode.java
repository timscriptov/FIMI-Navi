package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;



/* loaded from: classes2.dex */
public class AckGetApMode extends X8BaseMessage {
    int apMode;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.apMode = packet.getPayLoad4().getByte();
    }

    public int getApMode() {
        return this.apMode;
    }

    public void setApMode(int apMode) {
        this.apMode = apMode;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckGetApMode{apMode=" + this.apMode + '}';
    }
}
