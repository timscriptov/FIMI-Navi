package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AutoCameraStateADV extends X8BaseMessage {
    public static final byte CAMERA_STATE_DELAY_PHOTOGRAPHY_START = 11;
    public static final byte CAMERA_STATE_DELAY_PHOTOGRAPHY_SUCCEED = 12;
    public static final byte CAMERA_STATE_EXCEPTION = 6;
    public static final byte CAMERA_STATE_NORMAL = 0;
    public static final byte CAMERA_STATE_POWEROFF_FINLISH = 10;
    public static final byte CAMERA_STATE_POWEROFF_RUNNING = 7;
    public static final byte CAMERA_STATE_READY = 1;
    public static final byte CAMERA_STATE_REBOOT = 8;
    public static final byte CAMERA_STATE_RECORDING = 2;
    public static final byte CAMERA_STATE_SNAPSHOT = 9;
    public static final byte CAMERA_STATE_SNAPSHOT_FAILED = 16;
    public static final byte CAMERA_STATE_TAKING_PHOTO = 4;
    public static final byte CAMERA_STOP_RECORDING = 3;
    public static final byte CAMERA_STOP_TAKING_PHOTO = 5;
    public static final byte CARD_STATE_FULLED = 5;
    public static final byte CARD_STATE_FULLING = 4;
    public static final byte CARD_STATE_LOWSPEED = 1;
    public static final byte CARD_STATE_NORMAL = 0;
    public static final byte CARD_STATE_NO_EXIST = 3;
    public static final byte CARD_STATE_PARAMETERS_ERR = 2;
    public static final byte CARD_STATE_SYTEM_ERR = 6;
    public static final byte RECORDING_MODE_CONTINOU = 33;
    public static final byte RECORDING_MODE_NORMAL = 32;
    public static final byte RECORDING_MODE_SLOWMOTION = 34;
    public static final byte RECORDING_MODE_TAKEPHOTO = 36;
    public static final byte TAKEPHOTO_MODE_CONTINOU = 17;
    public static final byte TAKEPHOTO_MODE_FULLVIEW = 20;
    public static final byte TAKEPHOTO_MODE_NORMAL = 16;
    public static final byte TAKEPHOTO_MODE_TIMELAPSE = 19;
    private int freeSpace;
    private int info;
    private int mode;
    private int recHour;
    private int recMinute;
    private int recSecond;
    private short recTime;
    private int state;
    private int totalSpace;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.state = packet.getPayLoad4().getByte();
        this.mode = packet.getPayLoad4().getByte();
        this.info = packet.getPayLoad4().getByte() & 255;
        this.recTime = packet.getPayLoad4().getShort();
        this.recSecond = this.recTime & 63;
        this.recMinute = (this.recTime >> 6) & 63;
        this.recHour = (this.recTime >> 12) & 63;
        this.freeSpace = packet.getPayLoad4().getInt();
        this.totalSpace = packet.getPayLoad4().getInt();
    }

    public boolean isCardInfo() {
        return this.info == 254;
    }

    public boolean isNoTFCard() {
        return this.info == 3;
    }

    public int getState() {
        return this.state;
    }

    public int getMode() {
        return this.mode;
    }

    public int getInfo() {
        return this.info;
    }

    public short getRecTime() {
        return this.recTime;
    }

    public int getRecSecond() {
        return this.recSecond;
    }

    public int getRecMinute() {
        return this.recMinute;
    }

    public int getRecHour() {
        return this.recHour;
    }

    public int getFreeSpace() {
        return this.freeSpace;
    }

    public void setFreeSpace(int freeSpace) {
        this.freeSpace = freeSpace;
    }

    public int getTotalSpace() {
        return this.totalSpace;
    }

    public void setTotalSpace(int totalSpace) {
        this.totalSpace = totalSpace;
    }

    public boolean isDelayedPhotography() {
        return this.state == 11 || this.state == 12;
    }

    @Override
    public String toString() {
        return "AutoCameraStateADV{state=" + this.state + ", mode=" + this.mode + ", info=" + this.info + ", recTime=" + ((int) this.recTime) + ", recSecond=" + this.recSecond + ", recMinute=" + this.recMinute + ", recHour=" + this.recHour + ", freeSpace=" + this.freeSpace + ", totalSpace=" + this.totalSpace + '}';
    }
}
