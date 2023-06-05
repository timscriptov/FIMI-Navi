package com.fimi.x8sdk.modulestate;

import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.dataparser.AutoAiSurroundState;
import com.fimi.x8sdk.dataparser.AutoFcBattery;
import com.fimi.x8sdk.dataparser.AutoFcSignalState;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.dataparser.AutoFixedwingState;
import com.fimi.x8sdk.dataparser.AutoHomeInfo;
import com.fimi.x8sdk.dataparser.AutoNavigationState;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcHeartPlayback;
import com.fimi.x8sdk.entity.X8AppSettingLog;


public class DroneStateFlightPlayback extends BaseState {
    public static final int CAMP = 5000;
    public static final int DEFAULT = 1500;
    public static long taskMadeChangeTime;
    AutoFcHeartPlayback autoFcHeart;
    AutoFcBattery fcBatterState;
    AutoHomeInfo homeInfo;
    AutoFcSportState sportState;
    private int accurateLanding;
    private AutoAiSurroundState autoAiSurroundState;
    private AutoFixedwingState autoFixedwingState;
    private boolean autoLandingCheckObj;
    private float deviceAngle;
    private AutoFcSignalState fcSingal;
    private float flyDistance;
    private float flyHeight;
    private float gpsSpeed;
    private float height;
    private boolean isSportMode;
    private long lastFcHeartTime;
    private double latitude;
    private double longitude;
    private int lowPowerValue;
    private int pilotMode;
    private float returnHomeHight;
    private int outTime = 1500;
    private int ctrlMode = 1;
    private int ctrlType = 1;
    private int fcBrakeSenssity = 10;
    private int fcYAWSenssity = 10;
    private int followReturn = 0;
    private int taskMode = 0;
    private int naviTaskSta = -1;
    private int apStatus = -1;
    private int wpNUM = -1;

    public boolean isAutoLandingCheckObj() {
        return this.autoLandingCheckObj;
    }

    public int getCtrlType() {
        return this.ctrlType;
    }

    public void setCtrlType(int ctlType) {
        this.ctrlType = ctlType;
    }

    public float getGpsSpeed() {
        return this.gpsSpeed;
    }

    public void setGpsSpeed(float gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
        X8AppSettingLog.setSpeedLimit(gpsSpeed);
    }

    public int getFollowReturn() {
        return this.followReturn;
    }

    public void setFollowReturn(int followReturn) {
        this.followReturn = followReturn;
        X8AppSettingLog.setFollowAB(followReturn == 1);
    }

    public int getLowPowerValue() {
        return this.lowPowerValue;
    }

    public void setLowPowerValue(int lowPowerValue) {
        this.lowPowerValue = lowPowerValue;
        X8AppSettingLog.setLowPower(lowPowerValue);
    }

    public void updateLastFcHeartTime() {
        this.lastFcHeartTime = System.currentTimeMillis();
    }

    public boolean isFcTimeOut() {
        return System.currentTimeMillis() - this.lastFcHeartTime >= ((long) this.outTime);
    }

    public float getDeviceAngle() {
        return this.deviceAngle;
    }

    public int getTaskMode() {
        return this.taskMode;
    }

    public int getWpNUM() {
        return this.wpNUM;
    }

    public int getCtrlMode() {
        return this.ctrlMode;
    }

    public void setCtrlMode(int ctrlMode) {
        taskMadeChangeTime = System.currentTimeMillis();
        this.ctrlMode = ctrlMode;
    }

    public int getFcBrakeSenssity() {
        return this.fcBrakeSenssity;
    }

    public void setFcBrakeSenssity(int fcBrakeSenssity) {
        this.fcBrakeSenssity = fcBrakeSenssity;
    }

    public int getFcYAWSenssity() {
        return this.fcYAWSenssity;
    }

    public void setFcYAWSenssity(int fcYAWSenssity) {
        this.fcYAWSenssity = fcYAWSenssity;
    }

    public boolean isOnGround() {
        return getAutoFcHeart().getFlightPhase() == 0 || getAutoFcHeart().getFlightPhase() == 1 || getAutoFcHeart().getFlightPhase() == 5;
    }

    public boolean isInSky() {
        return getAutoFcHeart().getFlightPhase() == 3 || getAutoFcHeart().getFlightPhase() == 2 || getAutoFcHeart().getFlightPhase() == 4;
    }

    public boolean isTakeOffing() {
        return getAutoFcHeart().getFlightPhase() == 2;
    }

    public boolean isLanding() {
        return getAutoFcHeart().getFlightPhase() == 4;
    }

    public boolean isConnect() {
        AckVersion version = StateManager.getInstance().getVersionState().getModuleFcAckVersion();
        return version != null && version.getSoftVersion() > 0;
    }

    public boolean isCanFly() {
        return getAutoFcHeart().getTakeOffCap() == 0 || getAutoFcHeart().getAutoTakeOffCap() == 0;
    }

    public AckVersion getVersion() {
        AckVersion version = StateManager.getInstance().getVersionState().getModuleFcAckVersion();
        return version;
    }

    @Override
    public boolean isAvailable() {
        AckVersion version = StateManager.getInstance().getVersionState().getModuleFcAckVersion();
        return version != null && version.getSoftVersion() >= 0;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void clearState() {
        this.taskMode = 0;
        this.ctrlMode = 1;
        this.naviTaskSta = -1;
        this.apStatus = -1;
        this.wpNUM = -1;
        this.longitude = 0.0d;
        this.latitude = 0.0d;
        this.height = 0.0f;
        this.deviceAngle = 0.0f;
        this.autoFixedwingState = null;
        this.autoLandingCheckObj = false;
    }

    public void setSportStateValue(double longitude, double latitude, float height, float deviceAngle) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.height = height;
        this.deviceAngle = deviceAngle;
        if (deviceAngle < 0.0f) {
            float f = deviceAngle + 360.0f;
        }
    }

    public float getReturnHomeHight() {
        return this.returnHomeHight;
    }

    public void setReturnHomeHight(float returnHomeHight) {
        this.returnHomeHight = returnHomeHight;
        X8AppSettingLog.setReturnHeight(returnHomeHight);
    }

    public float getFlyHeight() {
        return this.flyHeight;
    }

    public void setFlyHeight(float flyHeight) {
        this.flyHeight = flyHeight;
        X8AppSettingLog.setHeightLimit(flyHeight);
    }

    public void setANavigationState(AutoNavigationState mNavigationState) {
        this.taskMode = mNavigationState.getTaskMode();
        this.naviTaskSta = mNavigationState.getNaviTaskSta();
        this.apStatus = mNavigationState.getApStatus();
        this.wpNUM = mNavigationState.getWpNUM();
    }

    public void resetCtrlMode() {
        this.ctrlMode = 1;
        this.taskMode = 0;
    }

    public void setFcHeart(AutoFcHeartPlayback fcHeart) {
        this.autoFcHeart = fcHeart;
    }

    public AutoFcHeartPlayback getAutoFcHeart() {
        if (this.autoFcHeart == null) {
            this.autoFcHeart = new AutoFcHeartPlayback();
        }
        return this.autoFcHeart;
    }

    public boolean isGPSMode() {
        return this.autoFcHeart != null && this.autoFcHeart.getCtrlType() == 2;
    }

    public AutoFcBattery getFcBatterState() {
        return this.fcBatterState;
    }

    public void setFcBatterState(AutoFcBattery fcBatterState) {
        this.fcBatterState = fcBatterState;
    }

    public AutoHomeInfo getHomeInfo() {
        return this.homeInfo;
    }

    public void setHomeInfo(AutoHomeInfo homeInfo) {
        this.homeInfo = homeInfo;
    }

    public AutoFcSportState getFcSportState() {
        return this.sportState;
    }

    public void setFcSportState(AutoFcSportState sportState) {
        this.sportState = sportState;
    }

    public AutoFcSignalState getFcSingal() {
        return this.fcSingal;
    }

    public void setFcSingal(AutoFcSignalState fcSingal) {
        this.fcSingal = fcSingal;
    }

    public void setPilotMode(int pilotMode) {
        this.pilotMode = pilotMode;
        X8AppSettingLog.setPilotMode(pilotMode == 0);
    }

    public boolean isPilotModePrimary() {
        return this.pilotMode == 0;
    }

    public boolean isSportMode() {
        return this.isSportMode;
    }

    public void setSportMode(boolean sportMode) {
        this.isSportMode = sportMode;
        X8AppSettingLog.setSportMode(this.isSportMode);
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public AutoFixedwingState getAutoFixedwingState() {
        return this.autoFixedwingState;
    }

    public void setAutoFixedwingState(AutoFixedwingState autoFixedwingState) {
        this.autoFixedwingState = autoFixedwingState;
    }

    public float getFlyDistance() {
        return this.flyDistance;
    }

    public void setFlyDistance(float flyDistance) {
        this.flyDistance = flyDistance;
        X8AppSettingLog.setDistanceLimit(flyDistance);
    }

    public void setAutoAiSurroundState(AutoAiSurroundState autoAiSurroundState) {
        this.autoAiSurroundState = autoAiSurroundState;
    }

    public AutoAiSurroundState getAiSurroundState() {
        return this.autoAiSurroundState;
    }

    public void setAccurateLanding(int accurateLanding) {
        this.accurateLanding = accurateLanding;
        X8AppSettingLog.onChangeAccurateLanding(accurateLanding == 1);
    }

    public boolean isOpenAccurateLanding() {
        return this.accurateLanding == 1;
    }

    public void setAccurateLandingCheckObj(boolean autoLandingCheckObj) {
        this.autoLandingCheckObj = autoLandingCheckObj;
    }
}
