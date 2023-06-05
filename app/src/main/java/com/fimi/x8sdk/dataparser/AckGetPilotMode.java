package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class AckGetPilotMode extends X8BaseMessage {
    int pilotMode;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.pilotMode = packet.getPayLoad4().getByte();
    }

    public int getPilotMode() {
        return this.pilotMode;
    }

    public void setPilotMode(int pilotMode) {
        this.pilotMode = pilotMode;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckGetFcParam{, pilotMode=" + this.pilotMode + CoreConstants.CURLY_RIGHT;
    }
}
