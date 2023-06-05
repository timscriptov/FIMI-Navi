package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class AckGetIMUInfo extends X8BaseMessage {
    private short accelX;
    private short accelY;
    private short accelZ;
    private short gyroX;
    private short gyroY;
    private short gyroZ;
    private short iMUTempe;
    private short imuType;
    private short magX;
    private short magY;
    private short magZ;

    public short getImuType() {
        return this.imuType;
    }

    public void setImuType(short imuType) {
        this.imuType = imuType;
    }

    public short getiMUTempe() {
        return this.iMUTempe;
    }

    public void setiMUTempe(short iMUTempe) {
        this.iMUTempe = iMUTempe;
    }

    public short getGyroX() {
        return this.gyroX;
    }

    public void setGyroX(short gyroX) {
        this.gyroX = gyroX;
    }

    public short getGyroY() {
        return this.gyroY;
    }

    public void setGyroY(short gyroY) {
        this.gyroY = gyroY;
    }

    public short getGyroZ() {
        return this.gyroZ;
    }

    public void setGyroZ(short gyroZ) {
        this.gyroZ = gyroZ;
    }

    public short getAccelX() {
        return this.accelX;
    }

    public void setAccelX(short accelX) {
        this.accelX = accelX;
    }

    public short getAccelY() {
        return this.accelY;
    }

    public void setAccelY(short accelY) {
        this.accelY = accelY;
    }

    public short getAccelZ() {
        return this.accelZ;
    }

    public void setAccelZ(short accelZ) {
        this.accelZ = accelZ;
    }

    public short getMagX() {
        return this.magX;
    }

    public void setMagX(short magX) {
        this.magX = magX;
    }

    public short getMagY() {
        return this.magY;
    }

    public void setMagY(short magY) {
        this.magY = magY;
    }

    public short getMagZ() {
        return this.magZ;
    }

    public void setMagZ(short magZ) {
        this.magZ = magZ;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.imuType = packet.getPayLoad4().getByte();
        this.iMUTempe = packet.getPayLoad4().getByte();
        this.gyroX = packet.getPayLoad4().getShort();
        this.gyroY = packet.getPayLoad4().getShort();
        this.gyroZ = packet.getPayLoad4().getShort();
        this.accelX = packet.getPayLoad4().getShort();
        this.accelY = packet.getPayLoad4().getShort();
        this.accelZ = packet.getPayLoad4().getShort();
        this.magX = packet.getPayLoad4().getShort();
        this.magY = packet.getPayLoad4().getShort();
        this.magZ = packet.getPayLoad4().getShort();
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckGetIMUInfo{imuType=" + ((int) this.imuType) + ", iMUTempe=" + ((int) this.iMUTempe) + ", gyroX=" + ((int) this.gyroX) + ", gyroY=" + ((int) this.gyroY) + ", gyroZ=" + ((int) this.gyroZ) + ", accelX=" + ((int) this.accelX) + ", accelY=" + ((int) this.accelY) + ", accelZ=" + ((int) this.accelZ) + ", magX=" + ((int) this.magX) + ", magY=" + ((int) this.magY) + ", magZ=" + ((int) this.magZ) + CoreConstants.CURLY_RIGHT;
    }
}
