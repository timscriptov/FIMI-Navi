package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;



/* loaded from: classes2.dex */
public class AckGetGimbalSensorInfo extends X8BaseMessage {
    private short accelMagnitude;
    private short accelerationX;
    private short accelerationY;
    private short accelerationZ;
    private short gyroVarianceX;
    private short gyroVarianceY;
    private short gyroVarianceZ;
    private short gyroX;
    private short gyroY;
    private short gyroZ;
    private short motor1HallX;
    private short motor1HallY;
    private short motor1IU;
    private short motor1IV;
    private short motor2HallX;
    private short motor2HallY;
    private short motor2IU;
    private short motor2IV;
    private short motor3HallX;
    private short motor3HallY;
    private short motor3IU;
    private short motor3IV;
    private short temp;

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.gyroX = packet.getPayLoad4().getShort();
        this.gyroY = packet.getPayLoad4().getShort();
        this.gyroZ = packet.getPayLoad4().getShort();
        this.gyroVarianceX = packet.getPayLoad4().getShort();
        this.gyroVarianceY = packet.getPayLoad4().getShort();
        this.gyroVarianceZ = packet.getPayLoad4().getShort();
        this.accelerationX = packet.getPayLoad4().getShort();
        this.accelerationY = packet.getPayLoad4().getShort();
        this.accelerationZ = packet.getPayLoad4().getShort();
        this.motor1IU = packet.getPayLoad4().getShort();
        this.motor1IV = packet.getPayLoad4().getShort();
        this.motor2IU = packet.getPayLoad4().getShort();
        this.motor2IV = packet.getPayLoad4().getShort();
        this.motor3IU = packet.getPayLoad4().getShort();
        this.motor3IV = packet.getPayLoad4().getShort();
        this.motor1HallX = packet.getPayLoad4().getShort();
        this.motor1HallY = packet.getPayLoad4().getShort();
        this.motor2HallX = packet.getPayLoad4().getShort();
        this.motor2HallY = packet.getPayLoad4().getShort();
        this.motor3HallX = packet.getPayLoad4().getShort();
        this.motor3HallY = packet.getPayLoad4().getShort();
        this.temp = packet.getPayLoad4().getShort();
        this.accelMagnitude = packet.getPayLoad4().getShort();
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

    public short getAccelerationX() {
        return this.accelerationX;
    }

    public void setAccelerationX(short accelerationX) {
        this.accelerationX = accelerationX;
    }

    public short getAccelerationY() {
        return this.accelerationY;
    }

    public void setAccelerationY(short accelerationY) {
        this.accelerationY = accelerationY;
    }

    public short getAccelerationZ() {
        return this.accelerationZ;
    }

    public void setAccelerationZ(short accelerationZ) {
        this.accelerationZ = accelerationZ;
    }

    public short getGyroVarianceX() {
        return this.gyroVarianceX;
    }

    public void setGyroVarianceX(short gyroVarianceX) {
        this.gyroVarianceX = gyroVarianceX;
    }

    public short getGyroVarianceY() {
        return this.gyroVarianceY;
    }

    public void setGyroVarianceY(short gyroVarianceY) {
        this.gyroVarianceY = gyroVarianceY;
    }

    public short getGyroVarianceZ() {
        return this.gyroVarianceZ;
    }

    public void setGyroVarianceZ(short gyroVarianceZ) {
        this.gyroVarianceZ = gyroVarianceZ;
    }

    public short getMotor1IU() {
        return this.motor1IU;
    }

    public void setMotor1IU(short motor1IU) {
        this.motor1IU = motor1IU;
    }

    public short getMotor1IV() {
        return this.motor1IV;
    }

    public void setMotor1IV(short motor1IV) {
        this.motor1IV = motor1IV;
    }

    public short getMotor2IU() {
        return this.motor2IU;
    }

    public void setMotor2IU(short motor2IU) {
        this.motor2IU = motor2IU;
    }

    public short getMotor2IV() {
        return this.motor2IV;
    }

    public void setMotor2IV(short motor2IV) {
        this.motor2IV = motor2IV;
    }

    public short getMotor3IU() {
        return this.motor3IU;
    }

    public void setMotor3IU(short motor3IU) {
        this.motor3IU = motor3IU;
    }

    public short getMotor3IV() {
        return this.motor3IV;
    }

    public void setMotor3IV(short motor3IV) {
        this.motor3IV = motor3IV;
    }

    public short getMotor1HallX() {
        return this.motor1HallX;
    }

    public void setMotor1HallX(short motor1HallX) {
        this.motor1HallX = motor1HallX;
    }

    public short getMotor1HallY() {
        return this.motor1HallY;
    }

    public void setMotor1HallY(short motor1HallY) {
        this.motor1HallY = motor1HallY;
    }

    public short getMotor2HallX() {
        return this.motor2HallX;
    }

    public void setMotor2HallX(short motor2HallX) {
        this.motor2HallX = motor2HallX;
    }

    public short getMotor2HallY() {
        return this.motor2HallY;
    }

    public void setMotor2HallY(short motor2HallY) {
        this.motor2HallY = motor2HallY;
    }

    public short getMotor3HallX() {
        return this.motor3HallX;
    }

    public void setMotor3HallX(short motor3HallX) {
        this.motor3HallX = motor3HallX;
    }

    public short getMotor3HallY() {
        return this.motor3HallY;
    }

    public void setMotor3HallY(short motor3HallY) {
        this.motor3HallY = motor3HallY;
    }

    public short getTemp() {
        return this.temp;
    }

    public void setTemp(short temp) {
        this.temp = temp;
    }

    public short getAccelMagnitude() {
        return this.accelMagnitude;
    }

    public void setAccelMagnitude(short accelMagnitude) {
        this.accelMagnitude = accelMagnitude;
    }

    @Override // com.fimi.x8sdk.dataparser.X8BaseMessage
    public String toString() {
        return "AckGetGimbalSensorInfo{gyroX=" + ((int) this.gyroX) + ", gyroY=" + ((int) this.gyroY) + ", gyroZ=" + ((int) this.gyroZ) + ", accelerationX=" + ((int) this.accelerationX) + ", accelerationY=" + ((int) this.accelerationY) + ", accelerationZ=" + ((int) this.accelerationZ) + ", gyroVarianceX=" + ((int) this.gyroVarianceX) + ", gyroVarianceY=" + ((int) this.gyroVarianceY) + ", gyroVarianceZ=" + ((int) this.gyroVarianceZ) + ", motor1IU=" + ((int) this.motor1IU) + ", motor1IV=" + ((int) this.motor1IV) + ", motor2IU=" + ((int) this.motor2IU) + ", motor2IV=" + ((int) this.motor2IV) + ", motor3IU=" + ((int) this.motor3IU) + ", motor3IV=" + ((int) this.motor3IV) + ", motor1HallX=" + ((int) this.motor1HallX) + ", motor1HallY=" + ((int) this.motor1HallY) + ", motor2HallX=" + ((int) this.motor2HallX) + ", motor2HallY=" + ((int) this.motor2HallY) + ", motor3HallX=" + ((int) this.motor3HallX) + ", motor3HallY=" + ((int) this.motor3HallY) + ", temp=" + ((int) this.temp) + '}';
    }
}
