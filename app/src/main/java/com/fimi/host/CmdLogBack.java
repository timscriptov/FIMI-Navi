package com.fimi.host;

import com.fimi.kernel.dataparser.milink.ByteHexHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdLogBack {
    private static final CmdLogBack hostLogBack = new CmdLogBack();
    Logger logger = LoggerFactory.getLogger("x8s_cmd_log");
    private final boolean isLog = true;

    public static CmdLogBack getInstance() {
        return hostLogBack;
    }

    public void writeFpv(byte[] bArr) {
        String a = ByteHexHelper.bytesToHexString(bArr);
        Logger logger = this.logger;
        logger.info("[fpv]" + a);
    }


    public void writeLog(byte[] bytes, boolean isUp) {
        if (this.isLog) {
            String logStr = ByteHexHelper.bytesToHexString(bytes);
            String logTag = isUp ? "send-->" : "recv-->";
            this.logger.info(logTag + "" + logStr);
        }
    }
}
