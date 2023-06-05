package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AutoRCMatchRt extends X8BaseMessage {
    private int progress;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.progress = packet.getPayLoad4().getByte();
    }

    public int getProgress() {
        return this.progress;
    }
}
