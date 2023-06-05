package com.fimi.x8sdk.command;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class AoaTestColletion {
    public X8SendCmd getTestContent(String content) {
        return null;
    }

    public X8SendCmd getTestContent(byte[] content) {
        X8SendCmd x8SendCmd = new X8SendCmd(new LinkPacket4());
        x8SendCmd.setCmdData(content);
        return x8SendCmd;
    }
}
