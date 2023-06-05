package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AutoFixedwingState extends X8BaseMessage {
    private int fixedwingErroCode;
    private int fixedwingPhase;

    public int getFixedwingphase() {
        return this.fixedwingPhase;
    }

    public void setFixedwingphase(int fixedwingphase) {
        this.fixedwingPhase = fixedwingphase;
    }

    public int getFixedwingErroCode() {
        return this.fixedwingErroCode;
    }

    public void setFixedwingErroCode(int fixedwingErroCode) {
        this.fixedwingErroCode = fixedwingErroCode;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.fixedwingPhase = packet.getPayLoad4().getByte();
        this.fixedwingErroCode = packet.getPayLoad4().getByte();
    }
}
