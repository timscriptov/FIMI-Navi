package com.fimi.x8sdk.command;

import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;


public class CameraCollection extends X8BaseCmd {
    public static final byte MSGID_CAMERA_INTEREST_METERING = 12;
    public static final byte MSGID_CAMERA_STATE = 21;
    public static final byte MSGID_CAMERA_TF_CAP = 8;
    public static final byte MSGID_CLOSE_CAMERA = 1;
    public static final byte MSGID_START_RECORD = 2;
    public static final byte MSGID_STOP_RECORD = 3;
    public static final byte MSGID_STOP_TAKE_PHOTO = 5;
    public static final byte MSGID_TAKE_PHOTO = 4;
    public static final byte MSG_CAMERA_PHOTO_MODE = 10;
    public static final byte MSG_CAMERA_VIDEO_MODE = 11;
    public static final byte MSG_GROUP_CAMERA = 2;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBack;

    public CameraCollection() {
    }

    public CameraCollection(IPersonalDataCallBack callBack, UiCallBackListener uiCallBackListener) {
        this.personalDataCallBack = callBack;
        this.uiCallBack = uiCallBackListener;
    }

    private X8SendCmd getCameraBase(short seqIndex) {
        LinkPacket4 packet4 = new LinkPacket4();
        packet4.getHeader4().setType((byte) 1);
        packet4.getHeader4().setEncryptType((byte) 0);
        packet4.getHeader4().setSrcId((byte) X8BaseCmd.X8S_Module.MODULE_GCS.ordinal());
        packet4.getHeader4().setDestId((byte) X8BaseCmd.X8S_Module.MODULE_CAMERA.ordinal());
        packet4.getHeader4().setSeq(seqIndex);
        X8SendCmd x8SendCmd = new X8SendCmd(packet4);
        x8SendCmd.setPersonalDataCallBack(this.personalDataCallBack);
        x8SendCmd.setUiCallBack(this.uiCallBack);
        return x8SendCmd;
    }

    public X8SendCmd closeCamera() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = {2, 1};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd startRecord() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = {2, 2};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd stopRecord() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = {2, 3};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd takePhoto() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = {2, 4};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd stopTakePhoto() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payload = {2, 5};
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd getTFCardCAP() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payLoad = {8, 5};
        sendCmd.setPayLoad(payLoad);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd switchVideoMode() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payLoad = {2, 11};
        sendCmd.setPayLoad(payLoad);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setInterestMetering(byte meteringIndex) {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payLoad = {2, 12, meteringIndex};
        sendCmd.setPayLoad(payLoad);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd switchPhotoMode() {
        X8SendCmd sendCmd = getCameraBase(this.seqIndex);
        byte[] payLoad = {2, 10};
        sendCmd.setPayLoad(payLoad);
        sendCmd.packSendCmd();
        return sendCmd;
    }
}
