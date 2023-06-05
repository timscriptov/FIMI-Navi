package com.fimi.network.entity;



/* loaded from: classes.dex */
public class Download {
    private String fileEncode;
    private String forceSign;
    private String modelID;
    private String newVersion;
    private String pushFireType;
    private String status;
    private String sysid;
    private String sysname;
    private String updcontents;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileEncode() {
        return this.fileEncode;
    }

    public void setFileEncode(String fileEncode) {
        this.fileEncode = fileEncode;
    }

    public String getForceSign() {
        return this.forceSign;
    }

    public void setForceSign(String forceSign) {
        this.forceSign = forceSign;
    }

    public String getSysid() {
        return this.sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getNewVersion() {
        return this.newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getPushFireType() {
        return this.pushFireType;
    }

    public void setPushFireType(String pushFireType) {
        this.pushFireType = pushFireType;
    }

    public String getSysname() {
        return this.sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getUpdcontents() {
        return this.updcontents;
    }

    public void setUpdcontents(String updcontents) {
        this.updcontents = updcontents;
    }

    public String getModelID() {
        return this.modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "Download{fileEncode='" + this.fileEncode + "'" + ", forceSign='" + this.forceSign + "'" + ", sysid='" + this.sysid + "'" + ", newVersion='" + this.newVersion + "'" + ", pushFireType='" + this.pushFireType + "'" + ", sysname='" + this.sysname + "'" + ", updcontents='" + this.updcontents + "'" + ", modelID='" + this.modelID + "'" + ", status='" + this.status + "'" + ", url='" + this.url + "'" + '}';
    }
}
