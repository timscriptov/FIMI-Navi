package com.fimi.kernel.connect.interfaces.ble;

/* loaded from: classes.dex */
public interface IBleScanner {
    boolean isOpenBluetooth();

    boolean openBluetooth();

    void setBleScanResult(IBleScanResult iBleScanResult);

    void setLastBleDevice(String str);

    void startBleScan();

    void stopBleScan();
}
