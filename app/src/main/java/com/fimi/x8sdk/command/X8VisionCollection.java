package com.fimi.x8sdk.command;

import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;


public class X8VisionCollection extends X8BaseCmd {
    public static final byte MSG_GROUP_VISION_0F = 15;
    public static final byte MSG_SET_FPV_LOST_SEQ = 17;
    public static final byte MSG_SET_FPV_MODE = 16;
    public static final byte MSG_SET_RECTF = 3;
    public static final byte MSG_TRACKING_RECTF = 4;
    private IPersonalDataCallBack personalDataCallBack;
    private UiCallBackListener uiCallBackListener;

    public X8VisionCollection() {
    }

    public X8VisionCollection(IPersonalDataCallBack callBack, UiCallBackListener uiCallBackListener) {
        this.personalDataCallBack = callBack;
        this.uiCallBackListener = uiCallBackListener;
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
        x8SendCmd.setUiCallBack(this.uiCallBackListener);
        return x8SendCmd;
    }

    public X8SendCmd setVcRectF(int x, int y, int w, int h, int classfier) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_CV.ordinal());
        byte[] payload = new byte[14];
        int i = 1;
        payload[0] = 15;
        int i2 = i + 1;
        payload[i] = 3;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) (x & 255);
        int i6 = i5 + 1;
        payload[i5] = (byte) ((x >> 8) & 255);
        int i7 = i6 + 1;
        payload[i6] = (byte) (y & 255);
        int i8 = i7 + 1;
        payload[i7] = (byte) ((y >> 8) & 255);
        int i9 = i8 + 1;
        payload[i8] = (byte) (w & 255);
        int i10 = i9 + 1;
        payload[i9] = (byte) ((w >> 8) & 255);
        int i11 = i10 + 1;
        payload[i10] = (byte) (h & 255);
        int i12 = i11 + 1;
        payload[i11] = (byte) ((h >> 8) & 255);
        int i13 = i12 + 1;
        payload[i12] = (byte) (classfier & 255);
        int i14 = i13 + 1;
        payload[i13] = (byte) ((classfier >> 8) & 255);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setVcFpvMode(int vcFpvMode) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_CV.ordinal());
        byte[] payload = new byte[5];
        int i = 1;
        payload[0] = 15;
        int i2 = i + 1;
        payload[i] = 16;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) vcFpvMode;
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }

    public X8SendCmd setVcFpvLostSeq(int seq) {
        X8SendCmd sendCmd = getFCBase((byte) X8BaseCmd.X8S_Module.MODULE_CV.ordinal());
        byte[] payload = new byte[8];
        int i = 1;
        payload[0] = 15;
        int i2 = i + 1;
        payload[i] = 17;
        int i3 = i2 + 1;
        payload[i2] = 0;
        int i4 = i3 + 1;
        payload[i3] = 0;
        int i5 = i4 + 1;
        payload[i4] = (byte) (seq & 255);
        int i6 = i5 + 1;
        payload[i5] = (byte) ((seq >> 8) & 255);
        int i7 = i6 + 1;
        payload[i6] = (byte) ((seq >> 16) & 255);
        int i8 = i7 + 1;
        payload[i7] = (byte) ((seq >> 24) & 255);
        sendCmd.setAddToSendQueue(false);
        sendCmd.setPayLoad(payload);
        sendCmd.packSendCmd();
        return sendCmd;
    }
}
