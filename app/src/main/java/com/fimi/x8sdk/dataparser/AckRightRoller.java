package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AckRightRoller extends X8BaseMessage {
    private int moveDireciton;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.moveDireciton = packet.getPayLoad4().getByte();
    }

    public int getMoveDireciton() {
        return this.moveDireciton;
    }

    public void setMoveDireciton(int moveDireciton) {
        this.moveDireciton = moveDireciton;
    }

    public boolean isMoveUp() {
        return this.moveDireciton == 1;
    }

    public boolean isMoveDown() {
        return this.moveDireciton == 2;
    }
}
