package com.fimi.x8sdk.common;

import com.fimi.kernel.dataparser.milink.LinkMessage;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import com.fimi.kernel.dataparser.milink.LinkPayload;

/* loaded from: classes2.dex */
public class BaseMessage extends LinkMessage {
    private byte[] content;
    private int seqIndex;

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getSeqIndex() {
        return this.seqIndex;
    }

    public void setSeqIndex(int seqIndex) {
        this.seqIndex = seqIndex;
    }

    @Override // com.fimi.kernel.dataparser.milink.LinkMessage
    public void fillPayload(LinkPacket packet) {
    }

    @Override // com.fimi.kernel.dataparser.milink.LinkMessage
    public void unpack(LinkPayload payload) {
    }

    public void decrypt() throws Exception {
        if (this.content != null) {
            setMsgGroudId(this.content[0]);
            setMsgId(this.content[1]);
            setErrorCode(this.content[2]);
        }
    }
}
