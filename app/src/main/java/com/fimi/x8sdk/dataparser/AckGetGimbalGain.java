package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckGetGimbalGain extends X8BaseMessage {
    int data;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.data = packet.getPayLoad4().getByte() & 255;
    }

    public int getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "AckGetGimbalGain{data=" + this.data + '}';
    }
}
