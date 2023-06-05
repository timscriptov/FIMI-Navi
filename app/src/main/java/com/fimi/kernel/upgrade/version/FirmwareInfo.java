package com.fimi.kernel.upgrade.version;

import com.fimi.kernel.upgrade.interfaces.IFirmwareInfo;
import com.fimi.kernel.upgrade.interfaces.IReqFimwareBean;


public class FirmwareInfo implements IFirmwareInfo {
    long logicVersion;
    private String errorInfo;
    private IReqFimwareBean fb;
    private long fileSize;
    private String firmwareName;
    private boolean isForce;
    private String path;
    private byte subTargetId;
    private int sysId;
    private byte targetId;
    private String updateContent;
    private boolean upgradeResult;
    private boolean versionError;

    @Override
    public boolean isVersionError() {
        return this.versionError;
    }

    @Override
    public void setVersionError(boolean versionError) {
        this.versionError = versionError;
    }

    @Override
    public long getLogicVersion() {
        return this.logicVersion;
    }

    @Override
    public void setLogicVersion(long logicVersion) {
        this.logicVersion = logicVersion;
    }

    @Override
    public boolean isForce() {
        return this.isForce;
    }

    @Override
    public void setForce(boolean isForce) {
        this.isForce = isForce;
    }

    @Override
    public String getUpdateContent() {
        return this.updateContent;
    }

    @Override
    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    @Override
    public String getFirmwareName() {
        return this.firmwareName;
    }

    @Override
    public void setFirmwareName(String firmwareName) {
        this.firmwareName = firmwareName;
    }

    @Override
    public byte getSubTargetId() {
        return this.subTargetId;
    }

    @Override
    public void setSubTargetId(byte subTargetId) {
        this.subTargetId = subTargetId;
    }

    @Override
    public boolean isUpgradeResult() {
        return this.upgradeResult;
    }

    @Override
    public void setUpgradeResult(boolean upgradeResult) {
        this.upgradeResult = upgradeResult;
    }

    @Override
    public String getErrorInfo() {
        return this.errorInfo;
    }

    @Override
    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public long getFileSize() {
        return this.fileSize;
    }

    @Override
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public byte getTargetId() {
        return this.targetId;
    }

    @Override
    public void setTargetId(byte targetId) {
        this.targetId = targetId;
    }

    @Override
    public int getSysId() {
        return this.sysId;
    }

    @Override
    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    @Override
    public IReqFimwareBean getFb() {
        return this.fb;
    }

    @Override
    public void setFb(IReqFimwareBean fb) {
        this.fb = fb;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }
}
