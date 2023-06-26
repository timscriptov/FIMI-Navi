package com.fimi.kernel.dataparser.fmlink4;

import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;


public class LinkPacket4 {
    private final Header4 header4 = new Header4();
    private final LinkPayLoad4 payLoad4 = new LinkPayLoad4();
    byte[] sendCmd;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBack;

    public Header4 getHeader4() {
        return this.header4;
    }

    public LinkPayLoad4 getPayLoad4() {
        return this.payLoad4;
    }

    public byte[] packCmd() {
        int payloadLen = this.payLoad4.size();
        int len = payloadLen + 16;
        this.header4.setPayloadLen(payloadLen);
        this.sendCmd = new byte[len];
        this.header4.setLen(len);
        this.header4.setCrcFrame(this.payLoad4.getIntCRC(this.sendCmd, 16));
        byte[] headBytes = this.header4.onPacket();
        System.arraycopy(headBytes, 0, this.sendCmd, 0, 16);
        return this.sendCmd;
    }

    public String showPayload() {
        byte[] d = this.header4.onPacket();
        String hex = ByteHexHelper.bytesToHexString(d);
        int len = this.payLoad4.size();
        byte[] bytes = new byte[len];
        System.arraycopy(this.payLoad4.payload.array(), 0, bytes, 0, len);
        return hex + " " + ByteHexHelper.bytesToHexString(bytes);
    }

    public IPersonalDataCallBack getPersonalDataCallBack() {
        return this.personalDataCallBack;
    }

    public void setPersonalDataCallBack(IPersonalDataCallBack personalDataCallBack) {
        this.personalDataCallBack = personalDataCallBack;
    }

    public UiCallBackListener getUiCallBack() {
        return this.uiCallBack;
    }

    public void setUiCallBack(UiCallBackListener uiCallBack) {
        this.uiCallBack = uiCallBack;
    }

    public byte[] getPacketData() {
        byte[] header = this.header4.onPacket();
        int len = this.payLoad4.size();
        int count = header.length + len;
        byte[] bytes = new byte[count];
        System.arraycopy(header, 0, bytes, 0, header.length);
        System.arraycopy(this.payLoad4.payload.array(), 0, bytes, header.length, len);
        return bytes;
    }
}
