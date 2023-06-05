package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class AckRcPlaybackState extends X8BaseMessage {
    private int buttonState;
    private short rc0;
    private short rc1;
    private short rc2;
    private short rc3;
    private short rc4;
    private short rc5;

    public int getButtonState() {
        return this.buttonState;
    }

    public short getRc0() {
        return this.rc0;
    }

    public short getRc1() {
        return this.rc1;
    }

    public short getRc2() {
        return this.rc2;
    }

    public short getRc3() {
        return this.rc3;
    }

    public short getRc4() {
        return this.rc4;
    }

    public short getRc5() {
        return this.rc5;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.rc0 = packet.getPayLoad4().getShort();
        this.rc1 = packet.getPayLoad4().getShort();
        this.rc2 = packet.getPayLoad4().getShort();
        this.rc3 = packet.getPayLoad4().getShort();
        this.rc4 = packet.getPayLoad4().getShort();
        this.rc5 = packet.getPayLoad4().getShort();
        this.buttonState = packet.getPayLoad4().getShort();
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckRcCilbrationState{, status=" + this.buttonState + ", rc0=" + ((int) this.rc0) + ", rc1=" + ((int) this.rc1) + ", rc2=" + ((int) this.rc2) + ", rc3=" + ((int) this.rc3) + ", rc4=" + ((int) this.rc4) + ", rc5=" + ((int) this.rc5) + CoreConstants.CURLY_RIGHT;
    }
}
