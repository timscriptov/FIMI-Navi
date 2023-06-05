package com.fimi.x8sdk.presenter;

import com.fimi.android.app.R;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.CameraCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckCameraInterestMetering;
import com.fimi.x8sdk.dataparser.AckCameraPhotoMode;
import com.fimi.x8sdk.dataparser.AckCameraVideoMode;
import com.fimi.x8sdk.dataparser.AckStartRecord;
import com.fimi.x8sdk.dataparser.AckStopRecord;
import com.fimi.x8sdk.dataparser.AckTFCarddCap;
import com.fimi.x8sdk.dataparser.AckTakePhoto;
import com.fimi.x8sdk.ivew.ICamAction;
import com.fimi.x8sdk.listener.CallBackParamListener;

/* loaded from: classes2.dex */
public class CameraPresenter extends BasePresenter implements ICamAction {
    private CallBackParamListener tfCardStateListener;

    public CameraPresenter() {
        addNoticeListener();
    }

    @Override // com.fimi.x8sdk.ivew.ICamAction
    public void takePhoto(UiCallBackListener takePhotoListener) {
        sendCmd(new CameraCollection(this, takePhotoListener).takePhoto());
    }

    @Override // com.fimi.x8sdk.ivew.ICamAction
    public void startRecord(UiCallBackListener startRecordListener) {
        sendCmd(new CameraCollection(this, startRecordListener).startRecord());
    }

    @Override // com.fimi.x8sdk.ivew.ICamAction
    public void endRecord(UiCallBackListener endRecordListener) {
        sendCmd(new CameraCollection(this, endRecordListener).stopRecord());
    }

    @Override // com.fimi.x8sdk.ivew.ICamAction
    public void getTFCardState(CallBackParamListener tfStateListener) {
        this.tfCardStateListener = tfStateListener;
    }

    @Override // com.fimi.x8sdk.ivew.ICamAction
    public void swithVideoMode(UiCallBackListener swithVideoModeListener) {
        sendCmd(new CameraCollection(this, swithVideoModeListener).switchVideoMode());
    }

    @Override // com.fimi.x8sdk.ivew.ICamAction
    public void switchPhotoMode(UiCallBackListener switchPhotoModeListener) {
        sendCmd(new CameraCollection(this, switchPhotoModeListener).switchPhotoMode());
    }

    @Override // com.fimi.x8sdk.ivew.ICamAction
    public void setInterestMetering(byte meteringIndex, UiCallBackListener setInterestMeteringListener) {
        sendCmd(new CameraCollection(this, setInterestMeteringListener).setInterestMetering(meteringIndex));
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IDataCallBack
    public void onSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        super.onSendTimeOut(groupId, msgId, bcd);
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IDataCallBack
    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        super.onDataCallBack(groupId, msgId, packet);
        reponseCmd(true, groupId, msgId, packet, null);
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IPersonalDataCallBack
    public void onPersonalDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IPersonalDataCallBack
    public void onPersonalSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        onCameraTimeOut(groupId, msgId, bcd);
    }

    @Override // com.fimi.x8sdk.common.BasePresenter
    public void reponseCmd(boolean isAck, int groupId, int msgId, ILinkMessage packet, BaseCommand bcd) {
        if (groupId == 2) {
            if (msgId == 4) {
                AckTakePhoto takePhoto = (AckTakePhoto) packet;
                if (isNotNull(packet.getUiCallBack())) {
                    if (takePhoto.getMsgRpt() == 0) {
                        packet.getUiCallBack().onComplete(new CmdResult(true, 0), null);
                    } else {
                        packet.getUiCallBack().onComplete(new CmdResult(false, 0, takePhoto.getMsgRpt()), null);
                    }
                }
            }
            if (msgId == 2) {
                AckStartRecord ackStartRecord = (AckStartRecord) packet;
                if (isNotNull(packet.getUiCallBack())) {
                    if (ackStartRecord.getMsgRpt() == 0) {
                        packet.getUiCallBack().onComplete(new CmdResult(true, 0), null);
                    } else {
                        packet.getUiCallBack().onComplete(new CmdResult(false, 0, ackStartRecord.getMsgRpt()), null);
                    }
                }
            }
            if (msgId == 3) {
                AckStopRecord ackStopRecord = (AckStopRecord) packet;
                if (isNotNull(packet.getUiCallBack())) {
                    if (ackStopRecord.getMsgRpt() == 0) {
                        packet.getUiCallBack().onComplete(new CmdResult(true, 0), null);
                    } else {
                        packet.getUiCallBack().onComplete(new CmdResult(false, 0, ackStopRecord.getMsgRpt()), null);
                    }
                }
            }
            if (msgId == 11) {
                AckCameraVideoMode ackCameraVideoMode = (AckCameraVideoMode) packet;
                if (isNotNull(packet.getUiCallBack())) {
                    if (ackCameraVideoMode.getMsgRpt() == 0) {
                        packet.getUiCallBack().onComplete(new CmdResult(true, 0), null);
                    } else {
                        packet.getUiCallBack().onComplete(new CmdResult(false, 0, ackCameraVideoMode.getMsgRpt()), null);
                    }
                }
            }
            if (msgId == 12) {
                AckCameraInterestMetering ackCameraInterestMetering = (AckCameraInterestMetering) packet;
                if (isNotNull(packet.getUiCallBack())) {
                    if (ackCameraInterestMetering.getMsgRpt() == 0) {
                        packet.getUiCallBack().onComplete(new CmdResult(true, 0), null);
                    } else {
                        packet.getUiCallBack().onComplete(new CmdResult(false, 0, ackCameraInterestMetering.getMsgRpt()), null);
                    }
                }
            }
            if (msgId == 10) {
                AckCameraPhotoMode ackCameraPhotoMode = (AckCameraPhotoMode) packet;
                if (isNotNull(packet.getUiCallBack())) {
                    if (ackCameraPhotoMode.getMsgRpt() == 0) {
                        packet.getUiCallBack().onComplete(new CmdResult(true, 0), null);
                    } else {
                        packet.getUiCallBack().onComplete(new CmdResult(false, 0, ackCameraPhotoMode.getMsgRpt()), null);
                    }
                }
            }
            if (msgId == 8) {
                AckTFCarddCap tfCarddCap = (AckTFCarddCap) packet;
                if (isNotNull(this.tfCardStateListener)) {
                    if (tfCarddCap.getMsgRpt() == 0) {
                        this.tfCardStateListener.callbackResult(true, tfCarddCap.getTfCardCap());
                    } else {
                        this.tfCardStateListener.callbackResult(false, tfCarddCap.getTfCardCap());
                    }
                }
            }
        }
    }

    @Override // com.fimi.x8sdk.common.BasePresenter
    public void onNormalResponse(boolean isAck, ILinkMessage packet, BaseCommand bcd) {
        if (isAck) {
            if (isNotNull(packet.getUiCallBack())) {
                packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), null);
                return;
            }
            return;
        }
        bcd.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_timeout), null);
    }

    public void onCameraTimeOut(int groupId, int msgId, BaseCommand bcd) {
        if (groupId == 2) {
            if (msgId == 4 && isNotNull(bcd.getUiCallBack())) {
                bcd.getUiCallBack().onComplete(new CmdResult(false, 0), null);
            }
            if (msgId == 3 && isNotNull(bcd.getUiCallBack())) {
                bcd.getUiCallBack().onComplete(new CmdResult(false, 0), null);
            }
            if (msgId == 2 && isNotNull(bcd.getUiCallBack())) {
                bcd.getUiCallBack().onComplete(new CmdResult(false, 0), null);
            }
        }
    }
}
