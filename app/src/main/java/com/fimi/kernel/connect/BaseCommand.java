package com.fimi.kernel.connect;

import androidx.annotation.NonNull;

import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.connect.usb.LinkMsgType;
import com.fimi.kernel.dataparser.milink.LinkMessage;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;


public abstract class BaseCommand extends LinkMessage {
    public String camKey;
    public int fileOffset;
    LinkMsgType linkMsgType = LinkMsgType.FmLink4;
    private byte[] cmdData;
    private long createTime;
    private byte[] encryptData;
    private boolean isTimerCmd;
    private JsonUiCallBackListener jsonUiCallBackListener;
    private long lastSendTime;
    private LinkPacket linkPacket;
    private int msgSeq;
    private IPersonalDataCallBack personalDataCallBack;
    private byte[] rawCmdData;
    private UiCallBackListener uiCallBack;
    private byte[] unEncryptData;
    private int usbCmdKey;
    private int outTime = 500;
    private int reSendNum = 5;
    private boolean isAddToSendQueue = true;
    private int currentSendNum = 0;

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isAddToSendQueue() {
        return this.isAddToSendQueue;
    }

    public void setAddToSendQueue(boolean addToSendQueue) {
        this.isAddToSendQueue = addToSendQueue;
    }

    public int getCurrentSendNum() {
        return this.currentSendNum;
    }

    public void setCurrentSendNum(int currentSendNum) {
        this.currentSendNum = currentSendNum;
    }

    public boolean isTimerCmd() {
        return this.isTimerCmd;
    }

    public void setTimerCmd(boolean timerCmd) {
        this.isTimerCmd = timerCmd;
    }

    public byte[] getUnEncryptData() {
        return this.unEncryptData;
    }

    public void setUnEncryptData(byte[] unEncryptData) {
        this.unEncryptData = unEncryptData;
    }

    public void fillUnEncryptData(@NonNull LinkPacket packet) {
        if (packet.payload != null) {
            packet.payload.putBytes(this.unEncryptData);
        }
    }

    public byte[] getCmdData() {
        return this.cmdData;
    }

    public void setCmdData(byte[] cmdData) {
        this.cmdData = cmdData;
    }

    public int getOutTime() {
        return this.outTime;
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public int getMsgSeq() {
        return this.msgSeq;
    }

    public void setMsgSeq(int msgSeq) {
        this.msgSeq = msgSeq;
    }

    public int getReSendNum() {
        return this.reSendNum;
    }

    public void setReSendNum(int reSendNum) {
        this.reSendNum = reSendNum;
    }

    public LinkPacket getLinkPacket() {
        return this.linkPacket;
    }

    public void setLinkPacket(LinkPacket linkPacket) {
        this.linkPacket = linkPacket;
    }

    public long getLastSendTime() {
        return this.lastSendTime;
    }

    public void setLastSendTime(long lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    @Override
    public void fillPayload(LinkPacket packet) {
        if (this.encryptData != null && packet.payload != null) {
            packet.payload.putBytes(this.encryptData);
        }
    }

    public byte[] getEncryptData() {
        return this.encryptData;
    }

    public void setEncryptData(byte[] encryptData) {
        this.encryptData = encryptData;
    }

    public byte[] getRawCmdData() {
        return this.rawCmdData;
    }

    public void setRawCmdData(byte[] rawCmdData) {
        this.rawCmdData = rawCmdData;
    }

    public IPersonalDataCallBack getPersonalDataCallBack() {
        return this.personalDataCallBack;
    }

    public void setPersonalDataCallBack(IPersonalDataCallBack personalDataCallBack) {
        this.personalDataCallBack = personalDataCallBack;
    }

    @Override
    public UiCallBackListener getUiCallBack() {
        return this.uiCallBack;
    }

    @Override
    public void setUiCallBack(UiCallBackListener uiCallBack) {
        this.uiCallBack = uiCallBack;
    }

    public String getCamKey() {
        return this.camKey;
    }

    public void setCamKey(String camKey) {
        this.camKey = camKey;
    }

    public JsonUiCallBackListener getJsonUiCallBackListener() {
        return this.jsonUiCallBackListener;
    }

    public void setJsonUiCallBackListener(JsonUiCallBackListener jsonUiCallBackListener) {
        this.jsonUiCallBackListener = jsonUiCallBackListener;
    }

    public int getFileOffset() {
        return this.fileOffset;
    }

    public void setFileOffset(int fileOffset) {
        this.fileOffset = fileOffset;
    }

    public int getUsbCmdKey() {
        return this.usbCmdKey;
    }

    public void setUsbCmdKey(int usbCmdKey) {
        this.usbCmdKey = usbCmdKey;
    }

    public LinkMsgType getLinkMsgType() {
        return this.linkMsgType;
    }

    public void setLinkMsgType(LinkMsgType linkMsgType) {
        this.linkMsgType = linkMsgType;
    }
}
