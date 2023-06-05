package com.fimi.x8sdk.dataparser;

import androidx.annotation.NonNull;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AckGetAiLinePointsAction extends X8BaseMessage implements Comparable<AckGetAiLinePointsAction> {
    public int cmd0;
    public int cmd1;
    public int count;
    public int para0;
    public int para1;
    public int pos;
    public int time;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.pos = packet.getPayLoad4().getByte();
        this.count = packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        packet.getPayLoad4().getByte();
        this.cmd0 = packet.getPayLoad4().getByte();
        this.cmd1 = packet.getPayLoad4().getByte();
        for (int i = 0; i < 14; i++) {
            packet.getPayLoad4().getByte();
        }
        this.time = packet.getPayLoad4().getByte();
        this.para0 = packet.getPayLoad4().getByte();
        this.para1 = packet.getPayLoad4().getByte();
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull AckGetAiLinePointsAction o) {
        return this.pos - o.pos;
    }

    public int getAction() {
        if (this.cmd0 == 0) {
            return 0;
        }
        if (this.cmd0 == 1 && this.cmd1 == 0) {
            return 1;
        }
        if (this.cmd0 == 3 && this.cmd1 == 0) {
            return 2;
        }
        if (this.cmd0 == 2 && this.cmd1 == 0 && this.para0 == 1) {
            return 4;
        }
        if (this.cmd0 == 1 && this.cmd1 == 2) {
            return 5;
        }
        if (this.cmd0 != 2 || this.cmd1 != 0 || this.para0 != 3) {
            return 0;
        }
        return 6;
    }
}
