package com.fimi.x8sdk.command;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.dataparser.milink.LinkPayload;

import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;


public class X8DownLoadCmd extends BaseCommand {
    private X8MediaCmd createBaseComand() {
        X8MediaCmd x8MediaCmd = new X8MediaCmd();
        return x8MediaCmd;
    }

    public X8MediaCmd getMediaXmlFile(String xmlFileName) {
        X8MediaCmd x8MediaCmd = createBaseComand();
        short len = (short) xmlFileName.getBytes().length;
        byte[] nameBytes = xmlFileName.getBytes();
        byte[] payload = new byte[len + 7];
        byte[] lenBytes = ByteHexHelper.intToByteArray(payload.length);
        payload[0] = 0;
        System.arraycopy(lenBytes, 0, payload, 1, lenBytes.length);
        int i = lenBytes.length + 1;
        int i2 = i + 1;
        payload[i] = (byte) (len & 255);
        payload[i2] = (byte) ((65280 & len) >> 8);
        System.arraycopy(nameBytes, 0, payload, i2 + 1, nameBytes.length);
        x8MediaCmd.packCmd(payload);
        x8MediaCmd.setUsbCmdKey(payload[0]);
        return x8MediaCmd;
    }

    public X8MediaCmd downMediaFile(int offset, short maxLen, String downFileName, boolean isStop) {
        int i;
        X8MediaCmd x8MediaCmd = createBaseComand();
        short len = (short) downFileName.getBytes().length;
        byte[] nameBytes = downFileName.getBytes();
        byte[] payload = new byte[len + 13];
        byte[] lenBytes = ByteHexHelper.intToByteArray(payload.length);
        if (isStop) {
            int i2 = 1;
            payload[0] = 2;
            i = i2;
        } else {
            int i3 = 1;
            payload[0] = 1;
            i = i3;
        }
        System.arraycopy(lenBytes, 0, payload, i, lenBytes.length);
        int i4 = lenBytes.length + 1;
        byte[] offeBytes = ByteHexHelper.intToByteArray(offset);
        System.arraycopy(offeBytes, 0, payload, i4, offeBytes.length);
        int i5 = i4 + offeBytes.length;
        if (maxLen >= 1024) {
            maxLen = NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE;
        }
        int i6 = i5 + 1;
        payload[i5] = (byte) (maxLen & 255);
        int i7 = i6 + 1;
        payload[i6] = (byte) ((65280 & maxLen) >> 8);
        int i8 = i7 + 1;
        payload[i7] = (byte) (len & 255);
        payload[i8] = (byte) ((65280 & len) >> 8);
        System.arraycopy(nameBytes, 0, payload, i8 + 1, nameBytes.length);
        x8MediaCmd.setUsbCmdKey(offset);
        x8MediaCmd.setFileOffset(offset);
        x8MediaCmd.setReSendNum(5);
        x8MediaCmd.setOutTime(1000);
        x8MediaCmd.packCmd(payload);
        return x8MediaCmd;
    }

    public X8MediaCmd downMediaFile2(int offset, short maxLen, String downFileName) {
        X8MediaCmd x8MediaCmd = createBaseComand();
        short len = (short) downFileName.getBytes().length;
        byte[] nameBytes = downFileName.getBytes();
        byte[] payload = new byte[len + 13];
        int i = 1;
        payload[0] = 1;
        int payLoadLen = payload.length;
        int i2 = i + 1;
        payload[i] = (byte) (payLoadLen & 255);
        int i3 = i2 + 1;
        payload[i2] = (byte) ((payLoadLen >> 8) & 255);
        int i4 = i3 + 1;
        payload[i3] = (byte) ((payLoadLen >> 16) & 255);
        int i5 = i4 + 1;
        payload[i4] = (byte) ((payLoadLen >> 24) & 255);
        int i6 = i5 + 1;
        payload[i5] = (byte) (offset & 255);
        int i7 = i6 + 1;
        payload[i6] = (byte) ((offset >> 8) & 255);
        int i8 = i7 + 1;
        payload[i7] = (byte) ((offset >> 16) & 255);
        int i9 = i8 + 1;
        payload[i8] = (byte) ((offset >> 24) & 255);
        if (maxLen >= 1024) {
            maxLen = 1024;
        }
        int i10 = i9 + 1;
        payload[i9] = (byte) (maxLen & 255);
        int i11 = i10 + 1;
        payload[i10] = (byte) ((maxLen & 65280) >> 8);
        int i12 = i11 + 1;
        payload[i11] = (byte) (len & 255);
        payload[i12] = (byte) ((len & 65280) >> 8);
        System.arraycopy(nameBytes, 0, payload, i12 + 1, nameBytes.length);
        x8MediaCmd.setReSendNum(5);
        x8MediaCmd.setOutTime(1000);
        x8MediaCmd.packCmd(payload);
        return x8MediaCmd;
    }

    @Override
    public void unpack(LinkPayload payload) {
    }
}
