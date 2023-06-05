package com.fimi.kernel.upgrade.interfaces;


public interface IFirmwareInfo {
    String getErrorInfo();

    void setErrorInfo(String str);

    IReqFimwareBean getFb();

    void setFb(IReqFimwareBean iReqFimwareBean);

    long getFileSize();

    void setFileSize(long j);

    String getFirmwareName();

    void setFirmwareName(String str);

    long getLogicVersion();

    void setLogicVersion(long j);

    String getPath();

    void setPath(String str);

    byte getSubTargetId();

    void setSubTargetId(byte b);

    int getSysId();

    void setSysId(int i);

    byte getTargetId();

    void setTargetId(byte b);

    String getUpdateContent();

    void setUpdateContent(String str);

    boolean isForce();

    void setForce(boolean z);

    boolean isUpgradeResult();

    void setUpgradeResult(boolean z);

    boolean isVersionError();

    void setVersionError(boolean z);
}
