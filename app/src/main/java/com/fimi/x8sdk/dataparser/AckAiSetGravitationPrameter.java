package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class AckAiSetGravitationPrameter extends X8BaseMessage {
    private int autoVideo;
    private int eccentricWheel;
    private int ellipseInclinal;
    private int horizontalDistance;
    private int riseHeight;
    private int rotateDirecetion;
    private int rotateSpeed;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.rotateDirecetion = packet.getPayLoad4().getByte();
        this.rotateSpeed = packet.getPayLoad4().getByte();
        this.horizontalDistance = packet.getPayLoad4().getByte();
        this.riseHeight = packet.getPayLoad4().getByte();
        this.ellipseInclinal = packet.getPayLoad4().getByte();
        this.eccentricWheel = packet.getPayLoad4().getByte();
        this.autoVideo = packet.getPayLoad4().getByte();
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

    public int getAutoVideo() {
        return this.autoVideo;
    }

    public void setAutoVideo(int autoVideo) {
        this.autoVideo = autoVideo;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckAiSetGravitationPrameter{rotateDirecetion=" + this.rotateDirecetion + ", rotateSpeed=" + this.rotateSpeed + ", horizontalDistance=" + this.horizontalDistance + ", riseHeight=" + this.riseHeight + ", ellipseInclinal=" + this.ellipseInclinal + ", eccentricWheel=" + this.eccentricWheel + ", autoVideo=" + this.autoVideo + CoreConstants.CURLY_RIGHT;
    }
}
