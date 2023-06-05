package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.X8FcLogManager;


public class AutoBlackBox31 extends X8BaseMessage {
    private boolean isFc;

    @Override
    public void unPacket(LinkPacket4 packet) {
    }

    public void unPacketIsFc(LinkPacket4 packet, boolean isFc) {
        this.isFc = isFc;
        X8FcLogManager.getInstance().flightPlaybackLogWrite(packet.getPacketData(), isFc);
    }
}
