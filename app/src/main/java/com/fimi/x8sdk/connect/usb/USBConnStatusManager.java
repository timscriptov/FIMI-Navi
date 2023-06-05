package com.fimi.x8sdk.connect.usb;


public class USBConnStatusManager {
    private static final USBConnStatusManager statusManager = new USBConnStatusManager();
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


    public enum UsbStatus {
        UnConnect,
        Attach,
        Dettached
    }
}
