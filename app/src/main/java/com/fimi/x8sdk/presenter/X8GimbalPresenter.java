package com.fimi.x8sdk.presenter;

import com.fimi.android.app.R;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.X8GimbalCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckGetHorizontalAdjust;
import com.fimi.x8sdk.dataparser.AckGetPitchSpeed;
import com.fimi.x8sdk.ivew.IGbAciton;


public class X8GimbalPresenter extends BasePresenter implements IGbAciton {
    public X8GimbalPresenter() {
        addNoticeListener();
    }

    @Override
    public void getGimbalSensorInfo(UiCallBackListener callBackListener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, callBackListener);
        BaseCommand cmd = gc.getGimbalSensorInfoCmd();
        sendCmd(cmd);
    }

    @Override
    public void setAiAutoPhotoPitchAngle(int angle, UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.setAiAutoPhotoPitchAngle(angle);
        sendCmd(cmd);
    }

    @Override
    public void setHorizontalAdjust(float angle, UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.setHorizontalAdjust(angle);
        sendCmd(cmd);
    }

    @Override
    public void getHorizontalAdjust(UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.getHorizontalAdjust();
        sendCmd(cmd);
    }

    @Override
    public void setPitchSpeed(int speed, UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.setPitchSpeed(speed);
        sendCmd(cmd);
    }

    @Override
    public void getPitchSpeed(UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.getPitchSpeed();
        sendCmd(cmd);
    }

    @Override
    public void restGcSystemParams(UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.restGcParams();
        sendCmd(cmd);
    }

    @Override
    public void getGcParams(UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.getGcParams();
        sendCmd(cmd);
    }

    @Override
    public void setGcParams(int value, float params, UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.setGcParams(value, params);
        sendCmd(cmd);
    }

    @Override
    public void getGcParamsNew(UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.getGcParamsNew();
        sendCmd(cmd);
    }

    @Override
    public void setGcParamsNew(int model, float param1, float param2, float param3, UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.setGcParamsNew(model, param1, param2, param3);
        sendCmd(cmd);
    }

    @Override
    public void setGcGain(int data0, UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.setGcGain(data0);
        sendCmd(cmd);
    }

    @Override
    public void getGcGain(UiCallBackListener listener) {
        X8GimbalCollection gc = new X8GimbalCollection(this, listener);
        BaseCommand cmd = gc.fetchGcGain();
        sendCmd(cmd);
    }

    @Override
    public void onPersonalDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    @Override
    public void onPersonalSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        reponseCmd(false, groupId, msgId, null, bcd);
    }

    @Override
    public void reponseCmd(boolean isAck, int groupId, int msgId, ILinkMessage packet, BaseCommand bcd) {
        if (groupId == 9) {
            switch (msgId) {
                case 6:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 28:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 29:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 30:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 31:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 40:
                    onErrorResponseWithParam(isAck, packet, bcd);
                    return;
                case 41:
                    if (isAck && isNotNull(packet.getUiCallBack())) {
                        AckGetPitchSpeed pitchSpeed = (AckGetPitchSpeed) packet;
                        if (pitchSpeed.getMsgRpt() == 0) {
                            packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), pitchSpeed);
                            return;
                        } else {
                            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail), null);
                            return;
                        }
                    }
                    return;
                case 42:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 43:
                    if (isAck && isNotNull(packet.getUiCallBack())) {
                        AckGetHorizontalAdjust horizontalAdjust = (AckGetHorizontalAdjust) packet;
                        if (horizontalAdjust.getMsgRpt() == 0) {
                            packet.getUiCallBack().onComplete(new CmdResult(true, R.string.cmd_success), Float.valueOf(horizontalAdjust.getAngle()));
                            return;
                        } else {
                            packet.getUiCallBack().onComplete(new CmdResult(false, R.string.cmd_fail), null);
                            return;
                        }
                    }
                    return;
                case 47:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 96:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 105:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                case 106:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
                default:
            }
        }
    }
}
