package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;



/* loaded from: classes2.dex */
public class AckTFCarddCap extends X8BaseMessage {
    private String tfCardCap;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        byte[] payLoad = packet.getPayLoad4().getPayloadData();
        int length = payLoad.length;
        byte[] tfcap = new byte[length - 4];
        System.arraycopy(payLoad, 4, tfcap, 0, tfcap.length);
        this.tfCardCap = new String(tfcap);
    }

    public String getTfCardCap() {
        return this.tfCardCap;
    }

    public void setTfCardCap(String tfCardCap) {
        this.tfCardCap = tfCardCap;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckTFCarddCap{tfCardCap='" + this.tfCardCap + "'" + '}';
    }
}
