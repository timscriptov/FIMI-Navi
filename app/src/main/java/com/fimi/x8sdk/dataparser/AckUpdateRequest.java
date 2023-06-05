package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;



/* loaded from: classes2.dex */
public class AckUpdateRequest extends X8BaseMessage {
    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
    }

    public boolean isResultSucceed() {
        return getMsgRpt() == 0;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckUpdateRequest{getMsgRpt():" + getMsgRpt() + '}';
    }
}
