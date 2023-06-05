package com.fimi.kernel.dataparser.usb;

/* loaded from: classes.dex */
public class UsbLinkPacket {
    int type;
    UsbHeader usbHeader = new UsbHeader();
    UsbPayLoad usbPayLoad = new UsbPayLoad();

    public UsbLinkPacket(int type) {
        this.type = type;
    }

    public byte[] packCmd() {
        int payloadLen = this.usbPayLoad.size();
        int allFrameLen = payloadLen + 5;
        byte[] packs = new byte[allFrameLen];
        this.usbHeader.setLen(allFrameLen);
        this.usbHeader.setType(this.type);
        this.usbHeader.setVer(1);
        byte[] headerContent = this.usbHeader.onPacket();
        System.arraycopy(headerContent, 0, packs, 0, 5);
        System.arraycopy(this.usbPayLoad.getPayloadData(), 0, packs, 5, payloadLen);
        return packs;
    }

    public UsbHeader getUsbHeader() {
        return this.usbHeader;
    }

    public UsbPayLoad getUsbPayLoad() {
        return this.usbPayLoad;
    }
}
