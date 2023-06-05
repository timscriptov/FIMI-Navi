package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.utils.ByteUtil;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class AckGetHorizontalAdjust extends X8BaseMessage {
    float angle;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        byte byte0 = packet.getPayLoad4().getByte();
        byte byte1 = packet.getPayLoad4().getByte();
        byte byte2 = packet.getPayLoad4().getByte();
        byte byte3 = packet.getPayLoad4().getByte();
        this.angle = ByteUtil.byte2float(new byte[]{byte0, byte1, byte2, byte3}, 0);
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckGetHorizontalAdjust{angle=" + this.angle + CoreConstants.CURLY_RIGHT;
    }
}
