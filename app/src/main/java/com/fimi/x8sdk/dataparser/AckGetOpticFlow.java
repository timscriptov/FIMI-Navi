package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckGetOpticFlow extends X8BaseMessage {
    boolean isOpen;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.isOpen = packet.getPayLoad4().getByte() == 1;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    @Override
    public String toString() {
        return "AckGetOpticFlow{isOpen=" + this.isOpen + '}';
    }
}
