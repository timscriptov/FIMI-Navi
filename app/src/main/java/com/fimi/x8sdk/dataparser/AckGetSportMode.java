package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AckGetSportMode extends X8BaseMessage {
    int vehicleControlType;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.vehicleControlType = packet.getPayLoad4().getInt();
    }

    public int getVehicleControlType() {
        return (this.vehicleControlType >> 7) & 31;
    }

    public void setVehicleControlType(byte vehicleControlType) {
        this.vehicleControlType = vehicleControlType;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckGetFcParam{}";
    }
}
