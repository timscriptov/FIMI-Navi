package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckRcMatchCodeState extends X8BaseMessage {
    private int progress;

    public int getProgress() {
        return this.progress;
    }

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.progress = packet.getPayLoad4().getByte();
    }

    @Override
    public String toString() {
        return "AckCloudCaliState{progress=" + this.progress + '}';
    }
}
