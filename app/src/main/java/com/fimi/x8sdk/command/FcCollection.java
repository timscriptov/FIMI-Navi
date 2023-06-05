package com.fimi.x8sdk.command;

import android.util.Log;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.AckAiSetGravitationPrameter;
import com.fimi.x8sdk.dataparser.cmd.CmdAiAutoPhoto;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePoints;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction;
import com.fimi.x8sdk.entity.GpsInfoCmd;

import java.util.Calendar;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class FcCollection extends X8BaseCmd {
    public static final byte MSG_ATUO_BATTERRY = 5;
    public static final byte MSG_BLACK_BOX_30 = 48;
    public static final byte MSG_BLACK_BOX_31 = 49;
    public static final byte MSG_BLACK_BOX_32 = 50;
    public static final byte MSG_CHECK_IMU = 8;
    public static final byte MSG_CLOUD_CALIBRATION = 44;
    public static final byte MSG_CLOUD_CALIBRATION_CHECK = 45;
    public static final byte MSG_FC_AUTO_LAND = 21;
    public static final byte MSG_FC_AUTO_LAND_EXIT = 24;
    public static final byte MSG_FC_AUTO_NAVIGATION_STATE = 1;
    public static final byte MSG_FC_AUTO_TAKE_OFF = 16;
    public static final byte MSG_FC_AUTO_TAKE_OFF_EXIT = 19;
    public static final byte MSG_FC_ERRCODE = 4;
    public static final byte MSG_FC_HEART = 1;
    public static final byte MSG_FC_POINT_2_POINT_EXCUTE = 48;
    public static final byte MSG_FC_POINT_2_POINT_EXITE = 51;
    public static final byte MSG_FC_POINT_2_POINT_PAUSE = 49;
    public static final byte MSG_FC_POINT_2_POINT_RESUME = 50;
    public static final byte MSG_FC_SIGNAL_STATE = 3;
    public static final byte MSG_FC_SPORT_STATE = 2;
    public static final byte MSG_GET_CALI = 6;
    public static final byte MSG_GET_FC_AI_FOLLOW_ENABLE_BACK = 11;
    public static final byte MSG_GET_FC_PARAM = 6;
    public static final byte MSG_GET_FC_POINT_2_POINT = 53;
    public static final byte MSG_GET_FOLLOW_MODLE = 86;
    public static final byte MSG_GET_FOLLOW_SPEED = 89;
    public static final byte MSG_GET_GRAVITATION_PRAMETER = 117;
    public static final byte MSG_GET_IMU = 7;
    public static final byte MSG_GET_LINE_SET_POINTS = 38;
    public static final byte MSG_GET_LINE_SET_POINTSACTION = 39;
    public static final byte MSG_GET_LOSTACTION = 13;
    public static final byte MSG_GET_RETURN_HEIGHT = 9;
    public static final byte MSG_GET_SCREW_PRAMETER = 104;
    public static final byte MSG_GET_SPORT_MODE = 4;
    public static final byte MSG_GET_SURROUND_CIRCLE_DOT = 69;
    public static final byte MSG_GET_SURROUND_DEVICE_ORIENTATION = 73;
    public static final byte MSG_GET_SURROUND_SPEED = 71;
    public static final byte MSG_GROUP_CAMERA = 2;
    public static final byte MSG_GROUP_FC_BLACK_BOX = 10;
    public static final byte MSG_GROUP_FC_CTR = 4;
    public static final byte MSG_GROUP_FC_GIMBAL = 9;
    public static final byte MSG_GROUP_FC_MAINTENANCE = 13;
    public static final byte MSG_GROUP_FC_NAVI = 3;
    public static final byte MSG_GROUP_FC_NOFLY = 17;
    public static final byte MSG_GROUP_FC_TELEMETRY = 12;
    public static final byte MSG_GROUP_FC_TIME = 8;
    public static final byte MSG_GROUP_FORMAT = 1;
    public static final byte MSG_GROUP_RC = 14;
    public static final byte MSG_GROUP_RC_CALI = 11;
    public static final byte MSG_GROUP_RC_CTRL = 11;
    public static final byte MSG_HOME_INFO = 6;
    public static final byte MSG_ID_AUTOSEND_PANORAMA_PHOTOGRAPH = 106;
    public static final byte MSG_ID_GET_AP_MODE = 21;
    public static final byte MSG_ID_GET_AUTO_HOME = 40;
    public static final byte MSG_ID_GET_BRAKE_SENS = 36;
    public static final byte MSG_ID_GET_FORMAT_STORAGE = 21;
    public static final byte MSG_ID_GET_LOW_POWER_OPERATION = 23;
    public static final byte MSG_ID_GET_OPTIC_FLOW = 15;
    public static final byte MSG_ID_GET_PILOT_MODE = 2;
    public static final byte MSG_ID_GET_RC_CTRL_MODE = 18;
    public static final byte MSG_ID_GET_RC_ChANGE_DIRECTION = 19;
    public static final byte MSG_ID_GET_ROCKER_EXP = 26;
    public static final byte MSG_ID_GET_SENSITIVITY = 38;
    public static final byte MSG_ID_GET_YAW_TRIP = 34;
    public static final byte MSG_ID_ROCKER_INFO = 2;
    public static final byte MSG_ID_SET_AP_MODE = 10;
    public static final byte MSG_ID_SET_AP_MODE_RESTART = 15;
    public static final byte MSG_ID_SET_AUTO_HOME = 39;
    public static final byte MSG_ID_SET_BRAKE_SENS = 35;
    public static final byte MSG_ID_SET_DISABLE_TRIPOD = 42;
    public static final byte MSG_ID_SET_DISENABLE_AERAILSHOT = 44;
    public static final byte MSG_ID_SET_ENABLE_AERAILSHOT = 43;
    public static final byte MSG_ID_SET_ENABLE_TRIPOD = 41;
    public static final byte MSG_ID_SET_FORMAT_STORAGE = 12;
    public static final byte MSG_ID_SET_LOW_POWER_OPERATION = 24;
    public static final byte MSG_ID_SET_OPTIC_FLOW = 14;
    public static final byte MSG_ID_SET_PANORAMA_PHOTOGRAPH = 105;
    public static final byte MSG_ID_SET_PANORAMA_PHOTOGRAPH_STOP = 111;
    public static final byte MSG_ID_SET_PILOT_MODE = 1;
    public static final byte MSG_ID_SET_RC_CTRL_MODE = 17;
    public static final byte MSG_ID_SET_RC_FIVE_KEY = 16;
    public static final byte MSG_ID_SET_ROCKER_EXP = 25;
    public static final byte MSG_ID_SET_SENSITIVITY = 37;
    public static final byte MSG_ID_SET_YAW_TRIP = 33;
    public static final byte MSG_LOCK_MOTOR_STATE = 54;
    public static final byte MSG_NFZ_STATE = 3;
    public static final byte MSG_PILOT_MODE_FACTORY = 3;
    public static final byte MSG_PILOT_MODE_PRIMARY = 0;
    public static final byte MSG_PILOT_MODE_SEMI_SENIOR = 1;
    public static final byte MSG_PILOT_MODE_SENIOR = 2;
    public static final byte MSG_RC_CHECK_CMD = 15;
    public static final byte MSG_RC_SET_CMD = 14;
    public static final byte MSG_RC_STATE_CMD = 4;
    public static final byte MSG_REFUSE_NO_FLY = 1;
    public static final byte MSG_REQ_NO_FLY_NORMAL = 2;
    public static final byte MSG_REST_SYSTEM_PARAMS = -119;
    public static final byte MSG_SET_ACCURATE_LANDING = 108;
    public static final byte MSG_SET_AUTO_PHOTO_EXCUTE = 56;
    public static final byte MSG_SET_AUTO_PHOTO_EXIT = 59;
    public static final byte MSG_SET_AUTO_PHOTO_PAUSE = 57;
    public static final byte MSG_SET_AUTO_PHOTO_RESUME = 58;
    public static final byte MSG_SET_AUTO_PHOTO_VALUE = 60;
    public static final byte MSG_SET_CALI = 5;
    public static final byte MSG_SET_DISENABLE_ACCURATE = 52;
    public static final byte MSG_SET_DISENABLE_FIXWING = 48;
    public static final byte MSG_SET_DISENABLE_HEADING_FREE = 46;
    public static final byte MSG_SET_ENABLE_ACCURATE = 51;
    public static final byte MSG_SET_ENABLE_FIXWING = 47;
    public static final byte MSG_SET_ENABLE_FIXWING_STATE = 49;
    public static final byte MSG_SET_ENDABLE_HEADING_FREE = 45;
    public static final byte MSG_SET_FC_AI_FOLLOW_ENABLE_BACK = 10;
    public static final byte MSG_SET_FC_PARAM = 5;
    public static final byte MSG_SET_FC_POINT_2_POINT = 52;
    public static final byte MSG_SET_FC_RETURE_HOME_EXCUTE = 26;
    public static final byte MSG_SET_FC_RETURE_HOME_EXITE = 29;
    public static final byte MSG_SET_FC_RETURE_HOME_PAUSE = 27;
    public static final byte MSG_SET_FC_RETURE_HOME_RESUME = 28;
    public static final byte MSG_SET_FOLLOW_CLOSE = 97;
    public static final byte MSG_SET_FOLLOW_ERROR_CODE = 87;
    public static final byte MSG_SET_FOLLOW_EXCUTE = 80;
    public static final byte MSG_SET_FOLLOW_EXIT = 83;
    public static final byte MSG_SET_FOLLOW_MODLE = 85;
    public static final byte MSG_SET_FOLLOW_NOTITY_FC = 98;
    public static final byte MSG_SET_FOLLOW_OPEN = 96;
    public static final byte MSG_SET_FOLLOW_PAUSE = 81;
    public static final byte MSG_SET_FOLLOW_RESUME = 82;
    public static final byte MSG_SET_FOLLOW_SPEED = 88;
    public static final byte MSG_SET_FOLLOW_STANDBY = 84;
    public static final byte MSG_SET_GET_ACCURATE = 53;
    public static final byte MSG_SET_GRAVITATION_EXITE = 115;
    public static final byte MSG_SET_GRAVITATION_PAUSE = 113;
    public static final byte MSG_SET_GRAVITATION_PRAMETER = 116;
    public static final byte MSG_SET_GRAVITATION_RESUME = 114;
    public static final byte MSG_SET_GRAVITATION_START = 112;
    public static final byte MSG_SET_HEADING_FREE_UPDATE = 50;
    public static final byte MSG_SET_HOME_POINT = 90;
    public static final byte MSG_SET_LINE_EXCUTE = 32;
    public static final byte MSG_SET_LINE_EXIT = 35;
    public static final byte MSG_SET_LINE_PAUSE = 33;
    public static final byte MSG_SET_LINE_RESUME = 34;
    public static final byte MSG_SET_LINE_SET_POINTS = 36;
    public static final byte MSG_SET_LINE_SET_POINTSACTION = 37;
    public static final byte MSG_SET_LOSTACTION = 12;
    public static final byte MSG_SET_RETURN_HEIGHT = 8;
    public static final byte MSG_SET_SCREW_EXITE = 102;
    public static final byte MSG_SET_SCREW_PAUSE = 100;
    public static final byte MSG_SET_SCREW_PRAMETER = 103;
    public static final byte MSG_SET_SCREW_RESUME = 101;
    public static final byte MSG_SET_SCREW_START = 99;
    public static final byte MSG_SET_SPORT_MODE = 3;
    public static final byte MSG_SET_SURROUND_CIRCLE_DOT = 68;
    public static final byte MSG_SET_SURROUND_DEVICE_ORIENTATION = 72;
    public static final byte MSG_SET_SURROUND_EXCUTE = 64;
    public static final byte MSG_SET_SURROUND_EXIT = 67;
    public static final byte MSG_SET_SURROUND_PAUSE = 65;
    public static final byte MSG_SET_SURROUND_RESUME = 66;
    public static final byte MSG_SET_SURROUND_SPEED = 70;
    public static final byte MSG_SYNC_FC_GPS = 5;
    public static final byte MSG_SYNC_FC_PRESSURE = 6;
    public static final byte MSG_SYNC_FC_TIME = 4;
    public static final int MSG_SYS_CTRL_MODE = 107;
    public static final byte MSG_UPDATE_SURROUND_STATE = 74;
    public static final byte RC_MODE_AMERICAN = 1;
    public static final byte RC_MODE_CHINESE = 3;
    public static final byte RC_MODE_JAPANESE = 2;
    public static final int SENSORTYPE_IMUM = 1;
    public static final int SENSORTYPE_IMUS = 2;
    public static final byte X8_MSG_RC_CANCEL_CODE = 3;
    public static final byte X8_MSG_RC_MATCH_CODE = 1;
    public static final byte X8_MSG_RC_MATCH_RT = 2;
    public static byte X8_FC_SET_AP_MODE_CE = 0;
    public static byte X8_FC_SET_AP_MODE_FCC = 1;
    private BaseCommand aiLinePoint;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBack;

    public FcCollection() {
    }

    public FcCollection(IPersonalDataCallBack callBack, UiCallBackListener uiCallBack) {
        this.personalDataCallBack = callBack;
        this.uiCallBack = uiCallBack;
    }

    private X8SendCmd getFCBase(byte moduleName) {
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

    private X8SendCmd getFCBase(byte moduleName, int type) {
        LinkPacket4 packet4 = new LinkPacket4();
        packet4.getHeader4().setType((byte) type);
        packet4.getHeader4().setEncryptType((byte) 0);
        packet4.getHeader4().setSrcId((byte) X8BaseCmd.X8S_Module.MODULE_GCS.ordinal());
        packet4.getHeader4().setDestId(moduleName);
        packet4.getHeader4().setSeq(this.seqIndex);
        X8SendCmd x8SendCmd = new X8SendCmd(packet4);
        x8SendCmd.setPersonalDataCallBack(this.personalDataCallBack);
        x8SendCmd.setUiCallBack(this.uiCallBack);
        x8SendCmd.setReSendNum(0);
        return x8SendCmd;
    }

    public X8SendCmd takeOff(byte cmdID) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, cmdID, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd land(byte cmdID) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, cmdID, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd reponseNoFlyNormal() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_NFZ.ordinal());
        byte[] payload = {17, 1, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getNoFlyNormal() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_NFZ.ordinal());
        byte[] payload = {17, 2, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setPilotMode(byte mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 1, 0, 0, mode};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getPilotMode() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 2, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setReturnHeight(float height) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 8, 0, 0};
        byte[] h = ByteUtil.float2byte(height);
        System.arraycopy(h, 0, payload, 4, h.length);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getReturnHeight() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 9, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setLostAction(byte action) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 12, 0, 0, action};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getLostAction() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 13, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setFcParam(byte paramIndex, float paramData) {
        Log.i("moweiru", "paramData:" + paramData);
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 5, 0, 0, paramIndex};
        byte[] params = ByteUtil.float2byte(paramData);
        System.arraycopy(params, 0, payload, 5, params.length);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getFcParam(byte parmIndex) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 6, 0, 0, parmIndex};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowCmd(int msgCmd) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, (byte) msgCmd};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowVcEnable(int msgCmd) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, (byte) msgCmd};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowModle(int type) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_FOLLOW_MODLE, 0, 0, (byte) type};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand getAiFollowModle() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_GET_FOLLOW_MODLE, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowSpeed(int value) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[6];
        int i = 0 + 1;
        payload[0] = 3;
        int i2 = i + 1;
        payload[i] = MSG_SET_FOLLOW_SPEED;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) (value >> 0);
        int i6 = i5 + 1;
        payload[i5] = (byte) (value >> 8);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiFollowSpeed() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 3;
        int i2 = i + 1;
        payload[i] = MSG_GET_FOLLOW_SPEED;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiFollowEnableBack(int flag) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 10, 0, 0, (byte) flag};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiFollowEnableBack() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 11, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setAiFollowPoint2Point(double longitude, double latitude, int altitude, int speed) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[25];
        int i = 0 + 1;
        payload[0] = 3;
        payload[i] = 52;
        int i2 = i + 1 + 2;
        byte[] buffer = ByteHexHelper.getDoubleBytes(longitude);
        System.arraycopy(buffer, 0, payload, i2, 8);
        int i3 = i2 + 8;
        byte[] buffer2 = ByteHexHelper.getDoubleBytes(latitude);
        System.arraycopy(buffer2, 0, payload, i3, 8);
        int i4 = i3 + 8;
        int i5 = i4 + 1;
        payload[i4] = (byte) (altitude >> 0);
        int i6 = i5 + 1;
        payload[i5] = (byte) (altitude >> 8);
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = 0;
        int i9 = i8 + 1;
        payload[i8] = (byte) speed;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd setAiFollowPoint2PointExcute(byte msgId) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, msgId};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiSurroundExcute(byte msgId) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, msgId};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setAiSurroundPoint(int msgId, double longitude, double latitude, float altitude, double longitudeTakeoff, double latitudeTakeoff, float altitudeTakeoff, int type) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[44];
        int i = 0 + 1;
        payload[0] = 3;
        payload[i] = (byte) msgId;
        int i2 = i + 1 + 2;
        byte[] buffer = ByteHexHelper.getDoubleBytes(longitude);
        System.arraycopy(buffer, 0, payload, i2, 8);
        int i3 = i2 + 8;
        byte[] buffer2 = ByteHexHelper.getDoubleBytes(latitude);
        System.arraycopy(buffer2, 0, payload, i3, 8);
        int i4 = i3 + 8;
        int alt = (int) (10.0f * altitude);
        int i5 = i4 + 1;
        payload[i4] = (byte) (alt >> 0);
        int i6 = i5 + 1;
        payload[i5] = (byte) (alt >> 8);
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = 0;
        byte[] buffer3 = ByteHexHelper.getDoubleBytes(longitudeTakeoff);
        System.arraycopy(buffer3, 0, payload, i8, 8);
        int i9 = i8 + 8;
        byte[] buffer4 = ByteHexHelper.getDoubleBytes(latitudeTakeoff);
        System.arraycopy(buffer4, 0, payload, i9, 8);
        int i10 = i9 + 8;
        int alt2 = (int) (10.0f * altitudeTakeoff);
        int i11 = i10 + 1;
        payload[i10] = (byte) (alt2 >> 0);
        int i12 = i11 + 1;
        payload[i11] = (byte) (alt2 >> 8);
        int i13 = i12 + 1;
        payload[i12] = (byte) type;
        int i14 = i13 + 1;
        payload[i13] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        ByteHexHelper.bytesToHexString(payload);
        return sendCmd;
    }

    public X8SendCmd setAiSurroundSpeed(byte msgId, int value) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[8];
        int i = 0 + 1;
        payload[0] = 3;
        int i2 = i + 1;
        payload[i] = msgId;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) (value >> 0);
        int i6 = i5 + 1;
        payload[i5] = (byte) (value >> 8);
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiSurroundOrientation(byte msgId, int value) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[8];
        int i = 0 + 1;
        payload[0] = 3;
        int i2 = i + 1;
        payload[i] = msgId;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) value;
        int i6 = i5 + 1;
        payload[i5] = 0;
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiSurroundSpeed() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_GET_SURROUND_SPEED};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiSurroundOrientation() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_GET_SURROUND_DEVICE_ORIENTATION};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAiSurroundPoint() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_GET_SURROUND_CIRCLE_DOT};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiRetureHome(int msgId) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, (byte) msgId};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiLinePoints(CmdAiLinePoints points) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[58];
        int i = 0 + 1;
        payload[0] = 3;
        int i2 = i + 1;
        payload[i] = 36;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) points.nPos;
        int i6 = i5 + 1;
        payload[i5] = (byte) points.count;
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = 0;
        byte[] buffer = ByteHexHelper.getDoubleBytes(points.longitude);
        System.arraycopy(buffer, 0, payload, i8, 8);
        int i9 = i8 + 8;
        byte[] buffer2 = ByteHexHelper.getDoubleBytes(points.latitude);
        System.arraycopy(buffer2, 0, payload, i9, 8);
        int i10 = i9 + 8;
        int i11 = i10 + 1;
        payload[i10] = (byte) (points.altitude >> 0);
        int i12 = i11 + 1;
        payload[i11] = (byte) (points.altitude >> 8);
        int angle1 = ((int) points.angle) * 100;
        int i13 = i12 + 1;
        payload[i12] = (byte) (angle1 >> 0);
        int i14 = i13 + 1;
        payload[i13] = (byte) (angle1 >> 8);
        int i15 = i14 + 1;
        payload[i14] = (byte) (points.gimbalPitch >> 0);
        int i16 = i15 + 1;
        payload[i15] = (byte) (points.gimbalPitch >> 8);
        int i17 = i16 + 1;
        payload[i16] = (byte) points.speed;
        int i18 = i17 + 1;
        payload[i17] = 0;
        int i19 = i18 + 1;
        payload[i18] = 0;
        int i20 = i19 + 1;
        payload[i19] = 0;
        int i21 = i20 + 1;
        payload[i20] = (byte) ((points.autoRecord << 4) | (points.coordinatedTurnOff << 2) | points.pioEnbale | 2);
        int i22 = i21 + 1;
        payload[i21] = (byte) (points.orientation | (points.rotation << 4));
        int i23 = i22 + 1;
        payload[i22] = (byte) (points.orientation != 1 ? 0 : 1);
        int i24 = i23 + 1;
        payload[i23] = 0;
        int i25 = i24 + 1;
        payload[i24] = (byte) points.compeletEvent;
        int i26 = i25 + 1;
        payload[i25] = (byte) points.disconnectEvent;
        byte[] buffer3 = ByteHexHelper.getDoubleBytes(points.longitudePIO);
        System.arraycopy(buffer3, 0, payload, i26, 8);
        int i27 = i26 + 8;
        byte[] buffer4 = ByteHexHelper.getDoubleBytes(points.latitudePIO);
        System.arraycopy(buffer4, 0, payload, i27, 8);
        int i28 = i27 + 8;
        int i29 = i28 + 1;
        payload[i28] = (byte) (points.altitudePIO >> 0);
        int i30 = i29 + 1;
        payload[i29] = (byte) (points.altitudePIO >> 8);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiLinePointsAction(CmdAiLinePointsAction action) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[56];
        int i = 0 + 1;
        payload[0] = 3;
        int i2 = i + 1;
        payload[i] = 37;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) action.pos;
        int i6 = i5 + 1;
        payload[i5] = (byte) action.count;
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = 0;
        int i9 = i8 + 1;
        payload[i8] = (byte) action.cmd0;
        payload[i9] = (byte) action.cmd1;
        int i10 = i9 + 1 + 14;
        int i11 = i10 + 1;
        payload[i10] = (byte) action.time;
        int i12 = i11 + 1;
        payload[i11] = (byte) action.para0;
        int i13 = i12 + 1;
        payload[i12] = (byte) action.para1;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand getAiLinePointsAction(int number) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, 39, 0, 0, (byte) number};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiLineExcute() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, 32};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiLineExite() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, 35};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand getAiLinePoint(int number) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, 38, 0, 0, (byte) number};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setAiAutoPhotoValue(CmdAiAutoPhoto aiAutoPhotoValue) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[11];
        int i = 0 + 1;
        payload[0] = 3;
        int i2 = i + 1;
        payload[i] = MSG_SET_AUTO_PHOTO_VALUE;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) (aiAutoPhotoValue.angle >> 0);
        int i6 = i5 + 1;
        payload[i5] = (byte) (aiAutoPhotoValue.angle >> 8);
        int i7 = i6 + 1;
        payload[i6] = (byte) (aiAutoPhotoValue.routeLength >> 0);
        int i8 = i7 + 1;
        payload[i7] = (byte) (aiAutoPhotoValue.routeLength >> 8);
        int i9 = i8 + 1;
        payload[i8] = (byte) aiAutoPhotoValue.speed;
        int i10 = i9 + 1;
        payload[i9] = (byte) aiAutoPhotoValue.config;
        int i11 = i10 + 1;
        payload[i10] = (byte) aiAutoPhotoValue.mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiAutoPhotoExcute() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_AUTO_PHOTO_EXCUTE};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAiAutoPhotoExit() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_AUTO_PHOTO_EXIT};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setCalibrationStart(int type, int cmd, int mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[7];
        int i = 0 + 1;
        payload[0] = 13;
        int i2 = i + 1;
        payload[i] = 5;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) type;
        int i6 = i5 + 1;
        payload[i5] = (byte) cmd;
        int i7 = i6 + 1;
        payload[i6] = (byte) mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setAircrftCalibrationStart(int type, int cmd, int mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[7];
        int i = 0 + 1;
        payload[0] = 13;
        int i2 = i + 1;
        payload[i] = 5;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) type;
        int i6 = i5 + 1;
        payload[i5] = (byte) cmd;
        int i7 = i6 + 1;
        payload[i6] = (byte) mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        sendCmd.setOutTime(5000);
        return sendCmd;
    }

    public BaseCommand getAircrftCalibrationState(int sensorType, int type) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal(), 0);
        byte[] payload = new byte[6];
        int i = 0 + 1;
        payload[0] = 13;
        int i2 = i + 1;
        payload[i] = 6;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) sensorType;
        int i6 = i5 + 1;
        payload[i5] = (byte) type;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        sendCmd.setOutTime((int) CoreConstants.MILLIS_IN_ONE_MINUTE);
        sendCmd.setReSendNum(10);
        return sendCmd;
    }

    public BaseCommand getCalibrationState(int sensorType, int type) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[6];
        int i = 0 + 1;
        payload[0] = 13;
        int i2 = i + 1;
        payload[i] = 6;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) sensorType;
        int i6 = i5 + 1;
        payload[i5] = (byte) type;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setFormatStorage(int opcode, int devid) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[6];
        int i = 0 + 1;
        payload[0] = 1;
        int i2 = i + 1;
        payload[i] = 12;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) opcode;
        int i6 = i5 + 1;
        payload[i5] = (byte) devid;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand getFormatStorage(int devid) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[3];
        int i = 0 + 1;
        payload[0] = 1;
        int i2 = i + 1;
        payload[i] = 21;
        int i3 = i2 + 1;
        payload[i2] = (byte) devid;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setSyncTimeCmd() {
        Calendar calendar = Calendar.getInstance();
        short year = (short) calendar.get(1);
        byte month = (byte) (calendar.get(2) + 1);
        byte day = (byte) calendar.get(5);
        byte hour = (byte) calendar.get(11);
        byte min = (byte) calendar.get(12);
        byte second = (byte) calendar.get(13);
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal(), 0);
        byte[] payload = new byte[11];
        int i = 0 + 1;
        payload[0] = 8;
        int i2 = i + 1;
        payload[i] = 4;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) (year & 255);
        int i6 = i5 + 1;
        payload[i5] = (byte) ((65280 & year) >> 8);
        int i7 = i6 + 1;
        payload[i6] = month;
        int i8 = i7 + 1;
        payload[i7] = day;
        int i9 = i8 + 1;
        payload[i8] = hour;
        int i10 = i9 + 1;
        payload[i9] = min;
        int i11 = i10 + 1;
        payload[i10] = second;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setApMode(byte mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_REPEATER_RC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 14;
        int i2 = i + 1;
        payload[i] = 10;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setApModeRestart() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_REPEATER_RC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 14;
        int i2 = i + 1;
        payload[i] = 15;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 1;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getApMode() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 21;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setLowPowerOperation(int lowPowerValue, int seriousLowPowerValue, int lowPowerOpt, int seriousLowPowerOpt) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[8];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 24;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) lowPowerValue;
        int i6 = i5 + 1;
        payload[i5] = (byte) seriousLowPowerValue;
        int i7 = i6 + 1;
        payload[i6] = (byte) lowPowerOpt;
        int i8 = i7 + 1;
        payload[i7] = (byte) seriousLowPowerOpt;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getLowPowerOperation() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = MSG_ID_GET_LOW_POWER_OPERATION;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getIMUInfoCmd(int imuType) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 12;
        int i2 = i + 1;
        payload[i] = 7;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) imuType;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkIMUInfoCmd() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 13;
        int i2 = i + 1;
        payload[i] = 7;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkIMUException(int sensorType) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 13;
        int i2 = i + 1;
        payload[i] = 8;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) sensorType;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getCheckIMUStatus(int sensorType) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {13, 8, 0, 0, (byte) sensorType};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setCloudCalibrationCmd(int status) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = 44;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) status;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkCloudCalibrationCmd() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 9;
        int i2 = i + 1;
        payload[i] = 45;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd RCMatchOrCancelCode(int doneType) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_RC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 14;
        int i2 = i + 1;
        payload[i] = (byte) doneType;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkMatchCodeProgress() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_RC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 14;
        int i2 = i + 1;
        payload[i] = 2;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd rcCalibration(int cmdType) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_RC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 11;
        int i2 = i + 1;
        payload[i] = 14;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) cmdType;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd checkRCCalibration() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_RC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 11;
        int i2 = i + 1;
        payload[i] = 15;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setOpticFlow(boolean isOpen) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 14;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) (isOpen ? 1 : 0);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getOpticFlow() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 15;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAttitudeSensitivity(int rollPercent, int pitchPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 37;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 3;
        int i6 = i5 + 1;
        payload[i5] = (byte) rollPercent;
        int i7 = i6 + 1;
        payload[i6] = (byte) pitchPercent;
        int i8 = i7 + 1;
        payload[i7] = 0;
        int i9 = i8 + 1;
        payload[i8] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setYawSensitivity(int yawPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 37;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 4;
        int i6 = i5 + 1;
        payload[i5] = 0;
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = (byte) yawPercent;
        int i9 = i8 + 1;
        payload[i8] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getSensitivity() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 38;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setBrakeSens(int rollPercent, int pitchPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 35;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 3;
        int i6 = i5 + 1;
        payload[i5] = (byte) rollPercent;
        int i7 = i6 + 1;
        payload[i6] = (byte) pitchPercent;
        int i8 = i7 + 1;
        payload[i7] = 0;
        int i9 = i8 + 1;
        payload[i8] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getBrakeSens() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 36;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setYawTrip(int yawValue) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 33;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 4;
        int i6 = i5 + 1;
        payload[i5] = 0;
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = (byte) yawValue;
        int i9 = i8 + 1;
        payload[i8] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getYawTrip() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 34;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setUpDownRockerExp(int throttlePercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = MSG_ID_SET_ROCKER_EXP;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 8;
        int i6 = i5 + 1;
        payload[i5] = 0;
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = 0;
        int i9 = i8 + 1;
        payload[i8] = (byte) throttlePercent;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd restSystemParams() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = MSG_REST_SYSTEM_PARAMS;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 1;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setLockMotor(int lock) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = MSG_LOCK_MOTOR_STATE;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) lock;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setSportMode(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[6];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 3;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 7;
        int i6 = i5 + 1;
        payload[i5] = (byte) enable;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getSportMode() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 4;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setLeftRightRockerExp(int yawPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = MSG_ID_SET_ROCKER_EXP;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 4;
        int i6 = i5 + 1;
        payload[i5] = 0;
        int i7 = i6 + 1;
        payload[i6] = 0;
        int i8 = i7 + 1;
        payload[i7] = (byte) yawPercent;
        int i9 = i8 + 1;
        payload[i8] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGoBackRockerExp(int rollPercent, int pitchPercent) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[9];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = MSG_ID_SET_ROCKER_EXP;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = 3;
        int i6 = i5 + 1;
        payload[i5] = (byte) rollPercent;
        int i7 = i6 + 1;
        payload[i6] = (byte) pitchPercent;
        int i8 = i7 + 1;
        payload[i7] = 0;
        int i9 = i8 + 1;
        payload[i8] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getRockerExp() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 4;
        int i2 = i + 1;
        payload[i] = 26;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setHomePoint(float height, double lat, double lng, int mode, float accuracy) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[32];
        payload[0] = 3;
        payload[1] = 90;
        payload[2] = 0;
        payload[3] = 0;
        int pos = 4 + 1;
        payload[4] = (byte) mode;
        int pos2 = pos + 1;
        payload[pos] = 0;
        int pos3 = pos2 + 1;
        payload[pos2] = 0;
        int pos4 = pos3 + 1;
        payload[pos3] = 0;
        byte[] buffer = ByteUtil.float2byte(accuracy);
        System.arraycopy(buffer, 0, payload, pos4, 4);
        int pos5 = pos4 + 4;
        byte[] buffer2 = ByteUtil.float2byte(height);
        System.arraycopy(buffer2, 0, payload, pos5, 4);
        int pos6 = pos5 + 4;
        byte[] buffer3 = ByteHexHelper.getDoubleBytes(lng);
        System.arraycopy(buffer3, 0, payload, pos6, 8);
        int pos7 = pos6 + 8;
        byte[] buffer4 = ByteHexHelper.getDoubleBytes(lat);
        System.arraycopy(buffer4, 0, payload, pos7, 8);
        int i = pos7 + 8;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setCtrlMode(byte mode) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_RC.ordinal());
        byte[] payload = new byte[5];
        int i = 0 + 1;
        payload[0] = 11;
        int i2 = i + 1;
        payload[i] = 17;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = mode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getCtrlMode() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_RC.ordinal());
        byte[] payload = new byte[4];
        int i = 0 + 1;
        payload[0] = 11;
        int i2 = i + 1;
        payload[i] = 18;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAutoHomePoint(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 39, 0, 0, (byte) enable};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAutoHomePoint() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, MSG_ID_GET_AUTO_HOME, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setEnableFixwing() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, MSG_SET_ENABLE_FIXWING, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setDisenableFixwing() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 48, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setEnableHeadingFree() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 45, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setUpdateHeadingFree() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 50, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setDisenableHeadingFree() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, MSG_SET_DISENABLE_HEADING_FREE, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setEnableTripod(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = 4;
        if (enable == 1) {
            payload[1] = MSG_ID_SET_ENABLE_TRIPOD;
        } else if (enable == 0) {
            payload[1] = MSG_ID_SET_DISABLE_TRIPOD;
        }
        payload[2] = 0;
        payload[3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public BaseCommand setEnableAerailShot(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[5];
        payload[0] = 4;
        if (enable == 1) {
            payload[1] = MSG_ID_SET_ENABLE_AERAILSHOT;
        } else if (enable == 0) {
            payload[1] = 44;
        }
        payload[2] = 0;
        payload[3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getGravitationPrameter() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_GET_GRAVITATION_PRAMETER, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGravitationStart() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_GRAVITATION_START, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGravitationPause() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_GRAVITATION_PAUSE, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGravitationResume() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_GRAVITATION_RESUME, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGravitationExite() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_GRAVITATION_EXITE, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGravitationPrameter(AckAiSetGravitationPrameter prameter) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[11];
        int i = 0 + 1;
        payload[0] = 3;
        int i2 = i + 1;
        payload[i] = MSG_SET_GRAVITATION_PRAMETER;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) prameter.getRotateDirecetion();
        int i6 = i5 + 1;
        payload[i5] = (byte) prameter.getRotateSpeed();
        int i7 = i6 + 1;
        payload[i6] = (byte) prameter.getHorizontalDistance();
        int i8 = i7 + 1;
        payload[i7] = (byte) prameter.getRiseHeight();
        int i9 = i8 + 1;
        payload[i8] = (byte) prameter.getEllipseInclinal();
        int i10 = i9 + 1;
        payload[i9] = (byte) prameter.getEccentricWheel();
        int i11 = i10 + 1;
        payload[i10] = (byte) prameter.getAutoVideo();
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewPrameter(AckAiScrewPrameter prameter) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[12];
        int i = 0 + 1;
        payload[0] = 3;
        int i2 = i + 1;
        payload[i] = MSG_SET_SCREW_PRAMETER;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) (prameter.getDistance() >> 0);
        int i6 = i5 + 1;
        payload[i5] = (byte) (prameter.getDistance() >> 8);
        int i7 = i6 + 1;
        payload[i6] = (byte) prameter.getCiclePeriod();
        int i8 = i7 + 1;
        payload[i7] = (byte) prameter.getVertSpeed();
        int i9 = i8 + 1;
        payload[i8] = (byte) prameter.getRTHTostart();
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getScrewPrameter() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_GET_SCREW_PRAMETER, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewStart() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_SCREW_START, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewPause() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_SCREW_PAUSE, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewResume() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_SCREW_RESUME, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setScrewExite() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_SET_SCREW_EXITE, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setAccurateLanding(int enable) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = new byte[4];
        payload[0] = 4;
        if (enable == 1) {
            payload[1] = 51;
        } else {
            payload[1] = 52;
        }
        payload[2] = 0;
        payload[3] = 0;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getAccurateLanding() {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {4, 53, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd sysCtrlMode2AiVc(int ctrlMode) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, 107, 0, 0, (byte) ctrlMode};
        sendCmd.setAddToSendQueue(false);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setPanoramaPhotographType(int panoramaPhotographType) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, MSG_ID_SET_PANORAMA_PHOTOGRAPH, 0, 0, (byte) panoramaPhotographType};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setPanoramaPhotographState(byte state) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal());
        byte[] payload = {3, state, 0, 0};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setPressureInfo(float alt, float hPa) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal(), 0);
        byte[] payload = new byte[12];
        payload[0] = 8;
        payload[1] = 6;
        payload[2] = 0;
        payload[3] = 0;
        byte[] buffer = ByteUtil.float2byte(alt);
        System.arraycopy(buffer, 0, payload, 4, 4);
        int i = 4 + 4;
        byte[] buffer2 = ByteUtil.float2byte(hPa);
        System.arraycopy(buffer2, 0, payload, i, 4);
        int i2 = i + 4;
        sendCmd.setAddToSendQueue(false);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setGpsInfo(GpsInfoCmd gps) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal(), 0);
        byte[] payload = new byte[32];
        payload[0] = 8;
        payload[1] = 5;
        payload[2] = 0;
        payload[3] = 0;
        byte[] buffer = ByteHexHelper.getDoubleBytes(gps.mLongitude);
        System.arraycopy(buffer, 0, payload, 4, 8);
        int i = 4 + 8;
        byte[] buffer2 = ByteHexHelper.getDoubleBytes(gps.mLatitude);
        System.arraycopy(buffer2, 0, payload, i, 8);
        int i2 = i + 8;
        byte[] buffer3 = ByteUtil.float2byte(gps.mAltitude);
        System.arraycopy(buffer3, 0, payload, i2, 4);
        int i3 = i2 + 4;
        int i4 = i3 + 1;
        payload[i3] = (byte) gps.mHorizontalAccuracyMeters;
        int i5 = i4 + 1;
        payload[i4] = (byte) gps.mVerticalAccuracyMeters;
        byte[] buffer4 = ByteUtil.float2byte(gps.mSpeed);
        System.arraycopy(buffer4, 0, payload, i5, 4);
        int i6 = i5 + 4;
        int i7 = i6 + 1;
        payload[i6] = (byte) (gps.mBearing >> 0);
        int i8 = i7 + 1;
        payload[i7] = (byte) (gps.mBearing >> 8);
        sendCmd.setAddToSendQueue(false);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }
}
