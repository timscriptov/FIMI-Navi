package com.fimi.x8sdk.dataparser.flightplayback;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.cmdsenum.X8FpvSignalState;
import com.fimi.x8sdk.dataparser.X8BaseMessage;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class AutoRelayHeartPlayback extends X8BaseMessage {
    int channel;
    short status;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.status = packet.getPayLoad4().getShort();
    }

    public int getChannel() {
        return this.channel;
    }

    public int getTxPower() {
        return (this.status >> 11) & 1;
    }

    public int getImageTranmission() {
        return (this.status >> 3) & 127;
    }

    public int getImageTranmissionTwo() {
        return (this.status >> 12) & 3;
    }

    public X8FpvSignalState getX8FpvSignalState() {
        int imageTranmission = getImageTranmissionTwo();
        if (imageTranmission == 2) {
            return X8FpvSignalState.MIDDLE;
        }
        if (imageTranmission == 3) {
            return X8FpvSignalState.STRONG;
        }
        return X8FpvSignalState.LOW;
    }

    public short getStatus() {
        return this.status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AutoRelayHeartPlayback{status=" + ((int) this.status) + CoreConstants.CURLY_RIGHT;
    }
}
