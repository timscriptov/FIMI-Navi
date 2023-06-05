package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;



/* loaded from: classes2.dex */
public class AckAiGetGravitationPrameter extends X8BaseMessage {
    private int eccentricWheel;
    private int ellipseInclinal;
    private int horizontalDistance;
    private int riseHeight;
    private int rotateDirecetion;
    private int rotateSpeed;
    private float startHeight;
    private double startLat;
    private double startLng;
    private float startNosePoint;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.rotateDirecetion = packet.getPayLoad4().getByte();
        this.rotateSpeed = packet.getPayLoad4().getByte();
        this.horizontalDistance = packet.getPayLoad4().getByte();
        this.riseHeight = packet.getPayLoad4().getByte();
        this.ellipseInclinal = packet.getPayLoad4().getByte();
        this.eccentricWheel = packet.getPayLoad4().getByte();
        this.startLng = packet.getPayLoad4().getDouble().doubleValue();
        this.startLat = packet.getPayLoad4().getDouble().doubleValue();
        this.startHeight = packet.getPayLoad4().getShort();
        this.startNosePoint = packet.getPayLoad4().getShort();
    }

    public int getRotateDirecetion() {
        return this.rotateDirecetion;
    }

    public void setRotateDirecetion(int rotateDirecetion) {
        this.rotateDirecetion = rotateDirecetion;
    }

    public int getRotateSpeed() {
        return this.rotateSpeed;
    }

    public void setRotateSpeed(int rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    public int getHorizontalDistance() {
        return this.horizontalDistance;
    }

    public void setHorizontalDistance(int horizontalDistance) {
        this.horizontalDistance = horizontalDistance;
    }

    public int getRiseHeight() {
        return this.riseHeight;
    }

    public void setRiseHeight(int riseHeight) {
        this.riseHeight = riseHeight;
    }

    public int getEllipseInclinal() {
        return this.ellipseInclinal;
    }

    public void setEllipseInclinal(int ellipseInclinal) {
        this.ellipseInclinal = ellipseInclinal;
    }

    public int getEccentricWheel() {
        return this.eccentricWheel;
    }

    public void setEccentricWheel(int eccentricWheel) {
        this.eccentricWheel = eccentricWheel;
    }

    public double getStartLng() {
        return this.startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getStartLat() {
        return this.startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public float getStartHeight() {
        return this.startHeight;
    }

    public void setStartHeight(short startHeight) {
        this.startHeight = startHeight;
    }

    public float getStartNosePoint() {
        return this.startNosePoint;
    }

    public void setStartNosePoint(short startNosePoint) {
        this.startNosePoint = startNosePoint;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckAiGetGravitationPrameter{rotateDirecetion=" + this.rotateDirecetion + ", rotateSpeed=" + this.rotateSpeed + ", horizontalDistance=" + this.horizontalDistance + ", riseHeight=" + this.riseHeight + ", ellipseInclinal=" + this.ellipseInclinal + ", eccentricWheel=" + this.eccentricWheel + ", startLng=" + this.startLng + ", startLat=" + this.startLat + ", startHeight=" + this.startHeight + ", startNosePoint=" + this.startNosePoint + '}';
    }
}
