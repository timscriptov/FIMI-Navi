package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AutoAiFollowErrorCode extends X8BaseMessage {
    private int aiFollowErrorCode;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.aiFollowErrorCode = packet.getPayLoad4().getByte() & 255;
    }

    public int getAiFollowErrorCode() {
        return this.aiFollowErrorCode;
    }

    public void setAiFollowErrorCode(int aiFollowErrorCode) {
        this.aiFollowErrorCode = aiFollowErrorCode;
    }
}
