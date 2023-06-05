package com.fimi.kernel.connect.model;

import java.io.Serializable;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes.dex */
public class UpdateDateMessage implements Serializable {
    byte faultMessage;
    int fileOffset;
    int msgLen;

    public int getMsgLen() {
        return this.msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }

    public byte getFaultMessage() {
        return this.faultMessage;
    }

    public void setFaultMessage(byte faultMessage) {
        this.faultMessage = faultMessage;
    }

    public int getFileOffset() {
        return this.fileOffset;
    }

    public void setFileOffset(int fileOffset) {
        this.fileOffset = fileOffset;
    }

    public String toString() {
        return "UpdateDateMessage{msgLen=" + this.msgLen + ", faultMessage=" + ((int) this.faultMessage) + ", fileOffset=" + this.fileOffset + CoreConstants.CURLY_RIGHT;
    }
}
