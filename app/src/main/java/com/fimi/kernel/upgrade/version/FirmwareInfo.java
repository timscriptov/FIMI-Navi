package com.fimi.kernel.upgrade.version;

import com.fimi.kernel.upgrade.interfaces.IFirmwareInfo;
import com.fimi.kernel.upgrade.interfaces.IReqFimwareBean;

/* loaded from: classes.dex */
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

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public boolean isVersionError() {
        return this.versionError;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setVersionError(boolean versionError) {
        this.versionError = versionError;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public long getLogicVersion() {
        return this.logicVersion;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setLogicVersion(long logicVersion) {
        this.logicVersion = logicVersion;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public boolean isForce() {
        return this.isForce;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setForce(boolean isForce) {
        this.isForce = isForce;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public String getUpdateContent() {
        return this.updateContent;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public String getFirmwareName() {
        return this.firmwareName;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setFirmwareName(String firmwareName) {
        this.firmwareName = firmwareName;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public byte getSubTargetId() {
        return this.subTargetId;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setSubTargetId(byte subTargetId) {
        this.subTargetId = subTargetId;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public boolean isUpgradeResult() {
        return this.upgradeResult;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setUpgradeResult(boolean upgradeResult) {
        this.upgradeResult = upgradeResult;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public String getErrorInfo() {
        return this.errorInfo;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public long getFileSize() {
        return this.fileSize;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public byte getTargetId() {
        return this.targetId;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setTargetId(byte targetId) {
        this.targetId = targetId;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public int getSysId() {
        return this.sysId;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public IReqFimwareBean getFb() {
        return this.fb;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setFb(IReqFimwareBean fb) {
        this.fb = fb;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public String getPath() {
        return this.path;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IFirmwareInfo
    public void setPath(String path) {
        this.path = path;
    }
}
