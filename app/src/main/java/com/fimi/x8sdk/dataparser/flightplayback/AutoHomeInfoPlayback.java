package com.fimi.x8sdk.dataparser.flightplayback;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.dataparser.X8BaseMessage;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.util.GpsCorrect;



/* loaded from: classes2.dex */
public class AutoHomeInfoPlayback extends X8BaseMessage {
    float height;
    double homeLatitude;
    double homeLongitude;
    int homePointAccuracy;
    int homePointStatus;
    int homePointType;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.homeLongitude = packet.getPayLoad4().getDouble().doubleValue();
        this.homeLatitude = packet.getPayLoad4().getDouble().doubleValue();
        this.height = packet.getPayLoad4().getFloat();
        this.homePointAccuracy = packet.getPayLoad4().getByte();
        this.homePointType = packet.getPayLoad4().getByte();
        this.homePointStatus = packet.getPayLoad4().getByte();
    }

    public FLatLng getFLatLng() {
        return GpsCorrect.Earth_To_Mars(this.homeLatitude, this.homeLongitude);
    }

    public double getHomeLongitude() {
        return this.homeLongitude;
    }

    public void setHomeLongitude(double homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    public double getHomeLatitude() {
        return this.homeLatitude;
    }

    public void setHomeLatitude(double homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getHomePointAccuracy() {
        return this.homePointAccuracy;
    }

    public void setHomePointAccuracy(int homePointAccuracy) {
        this.homePointAccuracy = homePointAccuracy;
    }

    public int getHomePointType() {
        return this.homePointType;
    }

    public void setHomePointType(int homePointType) {
        this.homePointType = homePointType;
    }

    public int getHomePointStatus() {
        return this.homePointStatus;
    }

    public void setHomePointStatus(int homePointStatus) {
        this.homePointStatus = homePointStatus;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AutoHomeInfoPlayback{homeLongitude=" + this.homeLongitude + ", homeLatitude=" + this.homeLatitude + ", height=" + this.height + ", homePointAccuracy=" + this.homePointAccuracy + ", homePointType=" + this.homePointType + ", homePointStatus=" + this.homePointStatus + '}';
    }
}
