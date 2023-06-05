package com.fimi.kernel.dataparser.milink;

import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;

import java.io.Serializable;

/* loaded from: classes.dex */
public class LinkPacket implements Serializable {
    public LinkPayload payload = new LinkPayload();
    byte[] msg;
    private int MsgId;
    private int msgGroupId;
    private IPersonalDataCallBack personalDataCallBack;
    private Header mHeader = new Header();

    public IPersonalDataCallBack getPersonalDataCallBack() {
        return this.personalDataCallBack;
    }

    public void setPersonalDataCallBack(IPersonalDataCallBack personalDataCallBack) {
        this.personalDataCallBack = personalDataCallBack;
    }

    public int getMsgGroupId() {
        return this.msgGroupId;
    }

    public void setMsgGroupId(int msgGroupId) {
        this.msgGroupId = msgGroupId;
    }

    public int getMsgId() {
        return this.MsgId;
    }

    public void setMsgId(int msgId) {
        this.MsgId = msgId;
    }

    public Header getHeader() {
        return this.mHeader;
    }

    public byte[] encodePacket() {
        this.mHeader.setPayloadLen(this.payload.size());
        this.msg = new byte[this.payload.size() + 20];
        this.mHeader.setCrcFrame(this.payload.getIntCRC(this.msg, 20));
        byte[] headBytes = this.mHeader.onPaket();
        System.arraycopy(headBytes, 0, this.msg, 0, 20);
        setMsgGroupId(this.msg[20] & 255);
        setMsgId(this.msg[21] & 255);
        return this.msg;
    }

    public int getSeq() {
        return this.mHeader.getSeq();
    }

    public void setSeq(int seq) {
        this.mHeader.setSeq(seq);
    }
}
