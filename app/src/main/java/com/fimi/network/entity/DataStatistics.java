package com.fimi.network.entity;


public class DataStatistics {
    private String appType;
    private String createTime;
    private String flyDistance;
    private String flyHeight;
    private String flyTime;
    private String gimbalVersion;
    private String handleVersion;
    private String latitude;
    private String longitude;
    private String mcuVersion;
    private String productModel;
    private String sysVersion;
    private String useTime;
    private String userId;

    public String getAppType() {
        return this.appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHandleVersion() {
        return this.handleVersion;
    }

    public void setHandleVersion(String handleVersion) {
        this.handleVersion = handleVersion;
    }

    public String getGimbalVersion() {
        return this.gimbalVersion;
    }

    public void setGimbalVersion(String gimbalVersion) {
        this.gimbalVersion = gimbalVersion;
    }

    public String getUseTime() {
        return this.useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getProductModel() {
        return this.productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getSysVersion() {
        return this.sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getMcuVersion() {
        return this.mcuVersion;
    }

    public void setMcuVersion(String mcuVersion) {
        this.mcuVersion = mcuVersion;
    }

    public String getFlyTime() {
        return this.flyTime;
    }

    public void setFlyTime(String flyTime) {
        this.flyTime = flyTime;
    }

    public String getFlyDistance() {
        return this.flyDistance;
    }

    public void setFlyDistance(String flyDistance) {
        this.flyDistance = flyDistance;
    }

    public String getFlyHeight() {
        return this.flyHeight;
    }

    public void setFlyHeight(String flyHeight) {
        this.flyHeight = flyHeight;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "DataStatistics{appType='" + this.appType + "'" + ", userId='" + this.userId + "'" + ", handleVersion='" + this.handleVersion + "'" + ", gimbalVersion='" + this.gimbalVersion + "'" + ", useTime='" + this.useTime + "'" + ", longitude='" + this.longitude + "'" + ", latitude='" + this.latitude + "'" + ", productModel='" + this.productModel + "'" + ", sysVersion='" + this.sysVersion + "'" + ", mcuVersion='" + this.mcuVersion + "'" + ", flyTime='" + this.flyTime + "'" + ", flyDistance='" + this.flyDistance + "'" + ", flyHeight='" + this.flyHeight + "'" + ", createTime='" + this.createTime + "'" + '}';
    }
}
