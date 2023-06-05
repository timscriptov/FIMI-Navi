package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AckAccurateLandingState extends X8BaseMessage {
    private static int i = 200;
    private static long timeout = 0;
    private int errorCode;
    private int state;

    public static boolean isTimeOut() {
        return System.currentTimeMillis() - timeout > 1000;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.state = packet.getPayLoad4().getByte() & 255;
        this.errorCode = packet.getPayLoad4().getByte();
        timeout = System.currentTimeMillis();
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean getBit2() {
        return (this.state & 4) != 0;
    }
}
