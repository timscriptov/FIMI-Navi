package com.fimi.x8sdk.connect.usb;


public interface UsbStatusListener {
    void notifyUsbStatusChanged(USBConnStatusManager.UsbStatus usbStatus);
}
