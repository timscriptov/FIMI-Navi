package com.fimi.kernel.upgrade.version;

import com.fimi.kernel.upgrade.interfaces.IReqFimwareBean;

/* loaded from: classes.dex */
public class FirmwareBean implements IReqFimwareBean {
    private int hardwareVersion;
    private byte[] idA;
    private byte[] idB;
    private byte[] idC;
    private byte[] idD;
    private int model;
    private int softwareVersion;
    private int sysId;
    private int type;
    private String version;

    public FirmwareBean() {
    }

    public FirmwareBean(int type, int model, int hardwareVersion, int softwareVersion, byte[] idA, byte[] idB, byte[] idC, byte[] idD) {
        this.type = type;
        this.model = model;
        this.hardwareVersion = hardwareVersion;
        this.softwareVersion = softwareVersion;
        this.idA = idA;
        this.idB = idB;
        this.idC = idC;
        this.idD = idD == null ? new byte[4] : idD;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public String getVersion() {
        return this.version;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public void setVersion(String version) {
        this.version = version;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public int getSysId() {
        return this.sysId;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public byte getType() {
        return (byte) this.type;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public int getModel() {
        return this.model;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public int getHardwareVersion() {
        return this.hardwareVersion;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public int getSoftwareVersion() {
        return this.softwareVersion;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public byte[] getIdA() {
        return this.idA;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public byte[] getIdB() {
        return this.idB;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public byte[] getIdC() {
        return this.idC;
    }

    @Override // com.fimi.kernel.upgrade.interfaces.IReqFimwareBean
    public byte[] getIdD() {
        return this.idD;
    }

    public void device2SysId() {
    }
}
