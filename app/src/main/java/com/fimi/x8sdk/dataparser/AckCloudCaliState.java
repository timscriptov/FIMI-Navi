package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.utils.BitUtil;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class AckCloudCaliState extends X8BaseMessage {
    private int caliErrorCode;
    private int progress;
    private int status;

    public int getProgress() {
        return this.progress;
    }

    public int getStatus() {
        return this.status;
    }

    public int getCaliErrorCode() {
        return this.caliErrorCode;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.progress = packet.getPayLoad4().getByte();
        this.status = packet.getPayLoad4().getByte();
        this.caliErrorCode = packet.getPayLoad4().getInt();
    }

    public boolean isTempeOverErr() {
        return BitUtil.getBitByByte(this.caliErrorCode, 0) > 0;
    }

    public boolean isNeedLeveling123() {
        return BitUtil.getBitByByte(this.caliErrorCode, 1) > 0 || BitUtil.getBitByByte(this.caliErrorCode, 2) > 0 || BitUtil.getBitByByte(this.caliErrorCode, 3) > 0;
    }

    public boolean isNeedLeveling4() {
        return BitUtil.getBitByByte(this.caliErrorCode, 4) > 0;
    }

    public boolean isNeedLeveling5() {
        return BitUtil.getBitByByte(this.caliErrorCode, 5) > 0;
    }

    public boolean isNeedLeveling6() {
        return BitUtil.getBitByByte(this.caliErrorCode, 6) > 0;
    }

    public boolean isNeedLeveling7() {
        return BitUtil.getBitByByte(this.caliErrorCode, 7) > 0;
    }

    public boolean isNeedLeveling8() {
        return BitUtil.getBitByByte(this.caliErrorCode, 8) > 0;
    }

    public boolean isNeedLeveling9() {
        return BitUtil.getBitByByte(this.caliErrorCode, 9) > 0;
    }

    public boolean isNeedLeveling10() {
        return BitUtil.getBitByByte(this.caliErrorCode, 10) > 0;
    }

    public boolean isNeedLeveling11() {
        return BitUtil.getBitByByte(this.caliErrorCode, 11) > 0;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckCloudCaliState{progress=" + this.progress + ", status=" + this.status + ", caliErrorCode=" + this.caliErrorCode + CoreConstants.CURLY_RIGHT;
    }
}
