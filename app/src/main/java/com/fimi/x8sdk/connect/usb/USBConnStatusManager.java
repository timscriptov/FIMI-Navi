package com.fimi.x8sdk.connect.usb;

/* loaded from: classes2.dex */
public class USBConnStatusManager {
    private static USBConnStatusManager statusManager = new USBConnStatusManager();
    UsbStatus status = UsbStatus.UnConnect;

    private USBConnStatusManager() {
    }

    public static USBConnStatusManager getInstance() {
        return statusManager;
    }

    public UsbStatus getUsbStatus() {
        return this.status;
    }

    public void setStatus(UsbStatus status) {
        this.status = status;
    }

    /* loaded from: classes2.dex */
    public enum UsbStatus {
        UnConnect,
        Attach,
        Dettached
    }
}
