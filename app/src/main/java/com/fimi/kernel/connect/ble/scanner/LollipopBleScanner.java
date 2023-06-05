package com.fimi.kernel.connect.ble.scanner;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import com.fimi.kernel.utils.LogUtil;

import java.util.List;

@TargetApi(21)
/* loaded from: classes.dex */
public class LollipopBleScanner extends BaseBleScanner {
    private static final String TAG = LollipopBleScanner.class.getName();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothScanner;
    private SimpleScanCallback mScanCallback;
    private ScanCallback scanCallback = new ScanCallback() { // from class: com.fimi.kernel.connect.ble.scanner.LollipopBleScanner.1
        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            LogUtil.i(LollipopBleScanner.TAG, "onScanResult: " + callbackType + " ScanResult:" + result);
            if (result.getScanRecord() != null) {
                LollipopBleScanner.this.mScanCallback.onBleScan(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
            }
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            LogUtil.i(LollipopBleScanner.TAG, "onBatchScanResults()");
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanFailed(int errorCode) {
            if (errorCode != 3 && errorCode != 1) {
                LogUtil.i(LollipopBleScanner.TAG, "onScanFailed: " + errorCode);
                LollipopBleScanner.this.mScanCallback.onBleScanFailed(BleScanState.newInstance(errorCode));
            }
        }
    };

    public LollipopBleScanner(SimpleScanCallback callback) {
        this.mBluetoothScanner = null;
        this.mBluetoothAdapter = null;
        this.mScanCallback = null;
        this.mScanCallback = callback;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter != null) {
            this.mBluetoothScanner = this.mBluetoothAdapter.getBluetoothLeScanner();
        }
    }

    @Override // com.fimi.kernel.connect.ble.scanner.BaseBleScanner
    public void onStartBleScan(long timeoutMillis) {
        long delay = timeoutMillis == 0 ? 10000L : timeoutMillis;
        if (this.mBluetoothScanner != null && this.mBluetoothAdapter != null && this.mBluetoothAdapter.isEnabled()) {
            try {
                this.mBluetoothScanner.startScan(this.scanCallback);
                this.isScanning = true;
            } catch (Exception e) {
                this.isScanning = false;
                LogUtil.e(TAG, e.toString());
            }
            this.timeoutHandler.postDelayed(this.timeoutRunnable, delay);
        } else {
            this.mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
        }
        LogUtil.i(TAG, "mBluetoothScanner.startScan()");
    }

    @Override // com.fimi.kernel.connect.ble.scanner.BaseBleScanner
    public void onStartBleScan() {
        if (this.mBluetoothScanner != null && this.mBluetoothAdapter != null && this.mBluetoothAdapter.isEnabled()) {
            try {
                this.mBluetoothScanner.startScan(this.scanCallback);
                this.isScanning = true;
            } catch (Exception e) {
                this.isScanning = false;
                this.mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
                LogUtil.e(TAG, e.toString());
            }
        } else {
            this.mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
        }
        LogUtil.i(TAG, "mBluetoothScanner.startScan()");
    }

    @Override // com.fimi.kernel.connect.ble.scanner.BaseBleScanner
    public void onStopBleScan() {
        this.isScanning = false;
        if (this.mBluetoothScanner != null && this.mBluetoothAdapter != null && this.mBluetoothAdapter.isEnabled()) {
            try {
                this.mBluetoothScanner.stopScan(this.scanCallback);
            } catch (Exception e) {
                LogUtil.e(TAG, e.toString());
            }
        }
    }

    @Override // com.fimi.kernel.connect.ble.scanner.BaseBleScanner
    public void onBleScanFailed(BleScanState scanState) {
        this.mScanCallback.onBleScanFailed(scanState);
    }
}
