package com.fimi.x8sdk.dataparser;

import com.fimi.android.app.R;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.x8sdk.entity.X8AppSettingLog;


public class AutoFcHeart extends X8BaseMessage {
    public static final int VEHICLE_CTRL_TYPE_ACRO = 5;
    public static final int VEHICLE_CTRL_TYPE_ASSITED = 4;
    public static final int VEHICLE_CTRL_TYPE_ATTI = 1;
    public static final int VEHICLE_CTRL_TYPE_GLOBAL_AUTO = 2;
    public static final int VEHICLE_CTRL_TYPE_LOCAL_AUTO = 3;
    public static final int VEHICLE_PHASE_AERIAL = 3;
    public static final int VEHICLE_PHASE_LANDED = 5;
    public static final int VEHICLE_PHASE_LANDING = 4;
    public static final int VEHICLE_PHASE_NULL = 0;
    public static final int VEHICLE_PHASE_PRE_FLIGHT = 1;
    public static final int VEHICLE_PHASE_TAKE_OFF = 2;
    int autoTakeOffCap;
    int candidateCtrlType;
    int ctrlModel;
    int ctrlType;
    int disarmCount;
    int flightPhase;
    int flightTime;
    int powerConRate;
    int startUpTime;
    int systenPhase;
    int takeOffCap;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.flightTime = packet.getPayLoad4().getShort();
        this.startUpTime = packet.getPayLoad4().getShort();
        this.ctrlType = packet.getPayLoad4().getByte();
        this.candidateCtrlType = packet.getPayLoad4().getByte();
        this.flightPhase = packet.getPayLoad4().getByte();
        this.ctrlModel = packet.getPayLoad4().getByte();
        this.systenPhase = packet.getPayLoad4().getByte();
        this.disarmCount = packet.getPayLoad4().getByte();
        this.powerConRate = packet.getPayLoad4().getByte();
        this.takeOffCap = packet.getPayLoad4().getByte();
        this.autoTakeOffCap = packet.getPayLoad4().getByte();
        X8AppSettingLog.setStartUpTime(this.startUpTime);
    }

    public int getFcMode() {
        int temp = R.string.x8_ctrl_type_na;
        switch (this.ctrlType) {
            case 1:
                int temp2 = R.string.x8_ctrl_type_atti;
                return temp2;
            case 2:
                int temp3 = R.string.x8_ctrl_type_global_auto;
                return temp3;
            case 3:
                int temp4 = R.string.x8_ctrl_type_local_auto;
                return temp4;
            case 4:
                int temp5 = R.string.x8_ctrl_type_assited;
                return temp5;
            case 5:
                int temp6 = R.string.x8_ctrl_type_acro;
                return temp6;
            default:
                return temp;
        }
    }

    public int getFlightTime() {
        return this.flightTime;
    }

    public void setFlightTime(int flightTime) {
        this.flightTime = flightTime;
    }

    public int getStartUpTime() {
        return this.startUpTime;
    }

    public void setStartUpTime(int startUpTime) {
        this.startUpTime = startUpTime;
    }

    public int getCtrlType() {
        return this.ctrlType;
    }

    public void setCtrlType(int ctrlType) {
        this.ctrlType = ctrlType;
    }

    public int getCandidateCtrlType() {
        return this.candidateCtrlType;
    }

    public void setCandidateCtrlType(int candidateCtrlType) {
        this.candidateCtrlType = candidateCtrlType;
    }

    public int getFlightPhase() {
        return this.flightPhase;
    }

    public void setFlightPhase(int flightPhase) {
        this.flightPhase = flightPhase;
    }

    public int getCtrlModel() {
        return this.ctrlModel;
    }

    public void setCtrlModel(int ctrlModel) {
        this.ctrlModel = ctrlModel;
    }

    public int getSystenPhase() {
        return this.systenPhase;
    }

    public void setSystenPhase(int systenPhase) {
        this.systenPhase = systenPhase;
    }

    @Override
    public String toString() {
        return "AutoFcHeart{flightTime=" + this.flightTime + ", startUpTime=" + this.startUpTime + ", ctrlType=" + this.ctrlType + ", candidateCtrlType=" + this.candidateCtrlType + ", flightPhase=" + this.flightPhase + ", ctrlModel=" + this.ctrlModel + ", systenPhase=" + this.systenPhase + ", disarmCount=" + this.disarmCount + ", powerConRate=" + this.powerConRate + ", takeOffCap=" + this.takeOffCap + ", autoTakeOffCap=" + this.autoTakeOffCap + '}';
    }

    public int getDisarmCount() {
        return this.disarmCount;
    }

    public void setDisarmCount(int disarmCount) {
        this.disarmCount = disarmCount;
    }

    public int getPowerConRate() {
        return this.powerConRate;
    }

    public void setPowerConRate(int powerConRate) {
        this.powerConRate = powerConRate;
    }

    public int getTakeOffCap() {
        return this.takeOffCap;
    }

    public void setTakeOffCap(int takeOffCap) {
        this.takeOffCap = takeOffCap;
    }

    public int getAutoTakeOffCap() {
        return this.autoTakeOffCap;
    }

    public void setAutoTakeOffCap(int autoTakeOffCap) {
        this.autoTakeOffCap = autoTakeOffCap;
    }
}
