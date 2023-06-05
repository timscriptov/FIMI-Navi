package com.fimi.x8sdk.command;

import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.ByteUtil;


public class X8GimbalCollection extends X8BaseCmd {
    public static final int MSG_GET_GIMBAL_SENSOR = 96;
    public static final byte MSG_GROUP_FC_GIMBAL = 9;
    public static final int MSG_ID_GET_GC_PARAM = 106;
    public static final int MSG_ID_GET_GC_PARAM_NEW = 29;
    public static final byte MSG_ID_GET_GIMBAL_GAIN = 31;
    public static final int MSG_ID_GET_HORIZONTAL_ADJUST = 43;
    public static final int MSG_ID_GET_PITCH_SPEED = 41;
    public static final int MSG_ID_POSRATE = 6;
    public static final int MSG_ID_REST_GC_PARAM = 47;
    public static final int MSG_ID_SET_GC_PARAM = 105;
    public static final int MSG_ID_SET_GC_PARAM_NEW = 28;
    public static final byte MSG_ID_SET_GIMBAL_GAIN = 30;
    public static final int MSG_ID_SET_HORIZONTAL_ADJUST = 42;
    public static final int MSG_ID_SET_PITCH_SPEED = 40;
    public static final int STATE = 1;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBack;

    public X8GimbalCollection() {
    }

    public X8GimbalCollection(IPersonalDataCallBack callBack, UiCallBackListener uiCallBack) {
        this.personalDataCallBack = callBack;
        this.uiCallBack = uiCallBack;
    }

    public X8SendCmd getGimbalSensorInfoCmd() {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[4];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_SET_FOLLOW_OPEN;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    private X8SendCmd getBase(byte moduleName) {
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

    public X8SendCmd setAiAutoPhotoPitchAngle(int aiAutoPhotoPitchAngle) {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[17];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = 6;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 10;
        int i6 = i5 + 1;
        payload[i5] = 0;
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = 0;
        int i9 = i8 + 1;
        payload[i8] = 0;
        int i10 = i9 + 1;
        payload[i9] = 0;
        int i11 = i10 + 1;
        payload[i10] = 0;
        int i12 = i11 + 1;
        payload[i11] = 0;
        int i13 = i12 + 1;
        payload[i12] = 0;
        int aiAutoPhotoPitchAngle2 = aiAutoPhotoPitchAngle * 100;
        int i14 = i13 + 1;
        payload[i13] = (byte) (aiAutoPhotoPitchAngle2 >> 0);
        int i15 = i14 + 1;
        payload[i14] = (byte) (aiAutoPhotoPitchAngle2 >> 8);
        int i16 = i15 + 1;
        payload[i15] = 0;
        int i17 = i16 + 1;
        payload[i16] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setHorizontalAdjust(float angle) {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[8];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_SET_DISABLE_TRIPOD;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        byte[] temp = ByteUtil.float2byte(angle);
        int i5 = i4 + 1;
        payload[i4] = temp[0];
        int i6 = i5 + 1;
        payload[i5] = temp[1];
        int i7 = i6 + 1;
        payload[i6] = temp[2];
        int i8 = i7 + 1;
        payload[i7] = temp[3];
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd getHorizontalAdjust() {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[4];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_SET_ENABLE_AERAILSHOT;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd setPitchSpeed(int speed) {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[5];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_GET_AUTO_HOME;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) speed;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getPitchSpeed() {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[4];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_SET_ENABLE_TRIPOD;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd restGcParams() {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[4];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_SET_ENABLE_FIXWING;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getGcParams() {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[5];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_AUTOSEND_PANORAMA_PHOTOGRAPH;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGcParams(int model, float param) {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[9];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_ID_SET_PANORAMA_PHOTOGRAPH;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) model;
        byte[] temp = ByteUtil.float2byte(param);
        int i6 = i5 + 1;
        payload[i5] = temp[0];
        int i7 = i6 + 1;
        payload[i6] = temp[1];
        int i8 = i7 + 1;
        payload[i7] = temp[2];
        int i9 = i8 + 1;
        payload[i8] = temp[3];
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getGcParamsNew() {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[4];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_SET_FC_RETURE_HOME_EXITE;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGcParamsNew(int model, float param1, float param2, float param3) {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[8];
        int i = 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = FcCollection.MSG_SET_FC_RETURE_HOME_RESUME;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) model;
        int i6 = i5 + 1;
        payload[i5] = (byte) (param1 * 10.0f);
        int i7 = i6 + 1;
        payload[i6] = (byte) (param2 * 10.0f);
        int i8 = i7 + 1;
        payload[i7] = (byte) (param3 * 10.0f);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGcGain(int data) {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = {9, MSG_ID_SET_GIMBAL_GAIN, 0, 0, (byte) data};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd fetchGcGain() {
        X8SendCmd sendCmd = getBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = {9, MSG_ID_GET_GIMBAL_GAIN};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        HostLogBack.getInstance().writeLog("获取===");
        return sendCmd;
    }
}
