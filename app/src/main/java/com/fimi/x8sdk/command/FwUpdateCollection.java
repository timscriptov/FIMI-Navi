package com.fimi.x8sdk.command;

import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.connect.usb.LinkMsgType;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;

/* loaded from: classes2.dex */
public class FwUpdateCollection extends X8BaseCmd {
    public static final byte MSG_GROUP_FW_UPDATE = 16;
    public static final byte MSG_ID_CHECK_CUR_STATUS = 5;
    public static final byte MSG_ID_GET_START_UPDATE = 2;
    public static final byte MSG_ID_GET_VERSION = -79;
    public static final byte MSG_ID_NOTIFY_UPGRADE = 4;
    public static final byte MSG_ID_PUT_FILE = 3;
    public static final byte MSG_ID_UPDATE_STATUS = 6;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBack;

    public FwUpdateCollection() {
    }

    public FwUpdateCollection(IPersonalDataCallBack callBack, UiCallBackListener listener) {
        this.personalDataCallBack = callBack;
        this.uiCallBack = listener;
    }

    public X8SendCmd getVersion(byte moduleName, byte type) {
        X8SendCmd sendCmd = getUpdateBase(moduleName);
        byte[] payload = {16, MSG_ID_GET_VERSION, 0, 0, type, 0, 0, 0, 1, -1, 2, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    private X8SendCmd getUpdateBase(byte moduleName) {
        LinkPacket4 packet4 = new LinkPacket4();
        packet4.getHeader4().setType((byte) 1);
        packet4.getHeader4().setEncryptType((byte) 0);
        packet4.getHeader4().setSrcId((byte) X8BaseCmd.X8S_Module.MODULE_GCS.ordinal());
        packet4.getHeader4().setDestId(moduleName);
        packet4.getHeader4().setSeq(this.seqIndex);
        X8SendCmd x8SendCmd = new X8SendCmd(packet4);
        x8SendCmd.setPersonalDataCallBack(this.personalDataCallBack);
        x8SendCmd.setUiCallBack(this.uiCallBack);
        return x8SendCmd;
    }

    public X8SendCmd getCameraVer() {
        X8SendCmd sendCmd = getUpdateBase((byte) X8BaseCmd.X8S_Module.MODULE_CAMERA.ordinal());
        byte[] payload = {16, MSG_ID_GET_VERSION, 4};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd requestStartUpdate() {
        X8SendCmd sendCmd = getUpdateBase((byte) X8BaseCmd.X8S_Module.MODULE_CAMERA.ordinal());
        byte[] payload = {16, 2, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.setReSendNum(0);
        sendCmd.packSendCmd(5, LinkMsgType.FmLink4);
        return sendCmd;
    }

    public X8SendCmd requestUploadFile(byte[] fileSize, byte[] crc) {
        X8SendCmd sendCmd = getUpdateBase((byte) X8BaseCmd.X8S_Module.MODULE_CAMERA.ordinal());
        byte[] payload = new byte[12];
        payload[0] = 16;
        payload[1] = 3;
        payload[2] = 0;
        payload[3] = 0;
        System.arraycopy(fileSize, 0, payload, 4, fileSize.length);
        System.arraycopy(crc, 0, payload, 8, crc.length);
        sendCmd.setPayLoad(payload);
        sendCmd.setReSendNum(10);
        sendCmd.setOutTime(PathInterpolatorCompat.MAX_NUM_POINTS);
        sendCmd.packSendCmd(5, LinkMsgType.FmLink4);
        HostLogBack.getInstance().writeLog("Alanqiu  ==========" + sendCmd.toString());
        return sendCmd;
    }

    public X8SendCmd sendFwFileContent(int fileOffset, byte[] payLoad) {
        int msgLen = payLoad.length + 12;
        short payloadLen = (short) payLoad.length;
        byte[] content = new byte[msgLen];
        int checksum = 0;
        int i = 0 + 1;
        content[0] = (byte) (msgLen >> 0);
        int i2 = i + 1;
        content[i] = (byte) (msgLen >> 8);
        int i3 = i2 + 1;
        content[i2] = (byte) (msgLen >> 16);
        int i4 = i3 + 1;
        content[i3] = (byte) (msgLen >> 24);
        int i5 = i4 + 1;
        content[i4] = (byte) (fileOffset >> 0);
        int i6 = i5 + 1;
        content[i5] = (byte) (fileOffset >> 8);
        int i7 = i6 + 1;
        content[i6] = (byte) (fileOffset >> 16);
        int i8 = i7 + 1;
        content[i7] = (byte) (fileOffset >> 24);
        int i9 = i8 + 1;
        content[i8] = (byte) (payloadLen >> 0);
        int i10 = i9 + 1;
        content[i9] = (byte) (payloadLen >> 8);
        for (byte b : payLoad) {
            checksum += b & 255;
        }
        int i11 = i10 + 1;
        content[i10] = (byte) (checksum >> 0);
        content[i11] = (byte) (checksum >> 8);
        System.arraycopy(payLoad, 0, content, i11 + 1, payloadLen);
        X8SendCmd x8SendCmd = new X8SendCmd();
        x8SendCmd.setFileOffset(fileOffset + payloadLen);
        x8SendCmd.setCmdData(x8SendCmd.addUSBHeader(content, 6));
        x8SendCmd.setLinkMsgType(LinkMsgType.FwUploadData);
        return x8SendCmd;
    }

    public X8SendCmd queryCurSystemStatus() {
        X8SendCmd sendCmd = getUpdateBase((byte) X8BaseCmd.X8S_Module.MODULE_CAMERA.ordinal());
        byte[] payload = {16, 5, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd(5, LinkMsgType.FmLink4);
        return sendCmd;
    }

    public X8SendCmd queryCurUpdateStatus() {
        X8SendCmd sendCmd = getUpdateBase((byte) X8BaseCmd.X8S_Module.MODULE_CAMERA.ordinal());
        byte[] payload = {16, 6, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.setReSendNum(0);
        sendCmd.packSendCmd(5, LinkMsgType.FmLink4);
        return sendCmd;
    }
}
