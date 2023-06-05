package com.fimi.host;

import com.fimi.kernel.dataparser.milink.ByteHexHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes.dex */
public class CmdLogBack {
    private static CmdLogBack hostLogBack = new CmdLogBack();
    Logger logger = LoggerFactory.getLogger("x8s_cmd_log");
    private boolean isLog = true;

    public static CmdLogBack getInstance() {
        return hostLogBack;
    }

    public void writeLog(byte[] bytes, boolean isUp) {
        if (this.isLog) {
            String logStr = ByteHexHelper.bytesToHexString(bytes);
            String logTag = isUp ? "send-->" : "recv-->";
            this.logger.info(logTag + "" + logStr);
        }
    }
}
