package com.fimi.kernel.connect.ble.scanner;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public interface SimpleScanCallback {
    void onBleScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr);

    void onBleScanFailed(BleScanState bleScanState);
}
