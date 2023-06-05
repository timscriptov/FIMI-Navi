package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckGetPilotMode extends X8BaseMessage {
    int pilotMode;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.pilotMode = packet.getPayLoad4().getByte();
    }

    public int getPilotMode() {
        return this.pilotMode;
    }

    public void setPilotMode(int pilotMode) {
        this.pilotMode = pilotMode;
    }

    @Override
    public String toString() {
        return "AckGetFcParam{, pilotMode=" + this.pilotMode + '}';
    }
}
