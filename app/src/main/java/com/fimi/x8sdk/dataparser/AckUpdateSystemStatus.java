package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;



/* loaded from: classes2.dex */
public class AckUpdateSystemStatus extends X8BaseMessage {
    int status;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.status = packet.getPayLoad4().getByte();
    }

    public int getStatus() {
        return this.status;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckUpdateSystemStatus{status=" + this.status + '}';
    }
}
