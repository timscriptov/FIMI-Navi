package com.fimi.kernel.upgrade.version;

import com.fimi.kernel.upgrade.interfaces.IReqFimwareBean;


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

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
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
    public byte getType() {
        return (byte) this.type;
    }

    @Override
    public int getModel() {
        return this.model;
    }

    @Override
    public int getHardwareVersion() {
        return this.hardwareVersion;
    }

    @Override
    public int getSoftwareVersion() {
        return this.softwareVersion;
    }

    @Override
    public byte[] getIdA() {
        return this.idA;
    }

    @Override
    public byte[] getIdB() {
        return this.idB;
    }

    @Override
    public byte[] getIdC() {
        return this.idC;
    }

    @Override
    public byte[] getIdD() {
        return this.idD;
    }

    public void device2SysId() {
    }
}
