package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckUpdateSystemStatus extends X8BaseMessage {
    int status;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.status = packet.getPayLoad4().getByte();
    }

    public int getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "AckUpdateSystemStatus{status=" + this.status + '}';
    }
}
