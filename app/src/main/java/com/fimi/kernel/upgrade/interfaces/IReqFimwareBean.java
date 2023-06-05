package com.fimi.kernel.upgrade.interfaces;

/* loaded from: classes.dex */
public interface IReqFimwareBean {
    int getHardwareVersion();

    byte[] getIdA();

    byte[] getIdB();

    byte[] getIdC();

    byte[] getIdD();

    int getModel();

    int getSoftwareVersion();

    int getSysId();

    void setSysId(int i);

    byte getType();

    String getVersion();

    void setVersion(String str);
}
