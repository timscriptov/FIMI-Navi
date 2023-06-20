package com.fimi.host;

import com.fimi.kernel.dataparser.milink.ByteHexHelper;

public class CmdLogBack {
    private static final CmdLogBack hostLogBack = new CmdLogBack();
    private final boolean isLog = true;

    public static CmdLogBack getInstance() {
        return hostLogBack;
    }

    public void writeFpv(byte[] bArr) {
        String a = ByteHexHelper.bytesToHexString(bArr);
        System.out.println("[fpv]" + a);
    }


    public void writeLog(byte[] bytes, boolean isUp) {
        if (this.isLog) {
            String logStr = ByteHexHelper.bytesToHexString(bytes);
            String logTag = isUp ? "send-->" : "recv-->";
            System.out.println(logTag + "" + logStr);
        }
    }
}
