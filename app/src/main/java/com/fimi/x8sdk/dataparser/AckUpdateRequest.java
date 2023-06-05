package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckUpdateRequest extends X8BaseMessage {
    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
    }

    public boolean isResultSucceed() {
        return getMsgRpt() == 0;
    }

    @Override
    public String toString() {
        return "AckUpdateRequest{getMsgRpt():" + getMsgRpt() + '}';
    }
}
