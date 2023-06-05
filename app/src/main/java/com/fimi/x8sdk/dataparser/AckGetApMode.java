package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckGetApMode extends X8BaseMessage {
    int apMode;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.apMode = packet.getPayLoad4().getByte();
    }

    public int getApMode() {
        return this.apMode;
    }

    public void setApMode(int apMode) {
        this.apMode = apMode;
    }

    @Override
    public String toString() {
        return "AckGetApMode{apMode=" + this.apMode + '}';
    }
}
