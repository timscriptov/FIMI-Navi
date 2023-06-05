package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckSetFcParam extends X8BaseMessage {
    int paraset;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.paraset = packet.getPayLoad4().getByte();
    }

    public int getParaset() {
        return this.paraset;
    }

    public void setParaset(int paraset) {
        this.paraset = paraset;
    }

    @Override
    public String toString() {
        return "AckSetFcParam{paraset=" + this.paraset + '}';
    }
}
