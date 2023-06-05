package com.fimi.x8sdk.update.fwpack;

/* loaded from: classes2.dex */
public interface IBuildPackInfo {
    void createUpdatePkg();

    byte[] getFwInfo();

    byte[] getOneFwInfo(FwInfo fwInfo);

    byte[] getPackCRC(String str);

    byte[] getfwPackInfo();

    void mergFwDataFile(String str);
}
