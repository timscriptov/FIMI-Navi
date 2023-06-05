package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckGetFormatStorageState extends X8BaseMessage {
    private int devid;
    private int process;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.devid = packet.getPayLoad4().getByte();
        this.process = packet.getPayLoad4().getByte();
    }

    public int getDevid() {
        return this.devid;
    }

    public void setDevid(int devid) {
        this.devid = devid;
    }

    public int getProcess() {
        return this.process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "AckGetFormatStorageState{devid=" + this.devid + ", process=" + this.process + '}';
    }
}
