package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckGetFcParam;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;
import com.fimi.x8sdk.ivew.IFcCtrlAction;

/* loaded from: classes2.dex */
public class FcCtrlPresenter extends BasePresenter implements IFcCtrlAction {
    public FcCtrlPresenter() {
        addNoticeListener();
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setReturnHeight(UiCallBackListener retHomeListener, float height) {
        sendCmd(new FcCollection(this, retHomeListener).setReturnHeight(height));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void reqReturnHeight(UiCallBackListener<AckGetRetHeight> reqRetHome) {
        sendCmd(new FcCollection(this, reqRetHome).getReturnHeight());
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setLostAction(UiCallBackListener listener, int action) {
        sendCmd(new FcCollection(this, listener).setLostAction((byte) action));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getLostAction(UiCallBackListener callBackListenerWithParam) {
        sendCmd(new FcCollection(this, callBackListenerWithParam).getLostAction());
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setPilotMode(UiCallBackListener listener, byte mode) {
        sendCmd(new FcCollection(this, listener).setPilotMode(mode));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getPilotMode(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getPilotMode());
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setFcParam(UiCallBackListener callBackListener, int paramIndex, float paramData) {
        sendCmd(new FcCollection(this, callBackListener).setFcParam((byte) paramIndex, paramData));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getFcParam(UiCallBackListener<Float> listenerWithParam, int paramIndex) {
        sendCmd(new FcCollection(this, listenerWithParam).getFcParam((byte) paramIndex));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setGpsSpeedParam(UiCallBackListener callBackListener, float paramData) {
        sendCmd(new FcCollection(this, callBackListener).setFcParam((byte) 3, paramData));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getGpsSpeedParam(UiCallBackListener<AckGetFcParam> listenerWithParam) {
        sendCmd(new FcCollection(this, listenerWithParam).getFcParam((byte) 3));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setFlyDistanceParam(UiCallBackListener callBackListener, float paramData) {
        sendCmd(new FcCollection(this, callBackListener).setFcParam((byte) 7, paramData));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getFlyDistanceParam(UiCallBackListener<AckGetFcParam> listenerWithParam) {
        sendCmd(new FcCollection(this, listenerWithParam).getFcParam((byte) 7));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setCalibrationStart(int type, int cmd, int mode, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand c = fc.setCalibrationStart(type, cmd, mode);
        sendCmd(c);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getCalibrationState(int sensorType, int type, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getCalibrationState(sensorType, type);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getAircrftCalibrationState(int sensorType, int type, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAircrftCalibrationState(sensorType, type);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setAircrftCalibrationStart(int type, int cmd, int mode, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand c = fc.setAircrftCalibrationStart(type, cmd, mode);
        sendCmd(c);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setFormatStorage(int opcode, int devid, UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand command = fcCollection.setFormatStorage(opcode, devid);
        sendCmd(command);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getFormatStorage(int devid, UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand command = fcCollection.getFormatStorage(devid);
        sendCmd(command);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setApMode(byte mode, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setApMode(mode);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setApModeRestart(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setApModeRestart();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getApMode(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getApMode();
        sendCmd(cmd);
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IDataCallBack
    public void onSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IDataCallBack
    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IPersonalDataCallBack
    public void onPersonalDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IPersonalDataCallBack
    public void onPersonalSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        reponseCmd(false, groupId, msgId, null, bcd);
    }

    @Override // com.fimi.x8sdk.common.BasePresenter
    public void reponseCmd(boolean isAck, int groupId, int msgId, ILinkMessage packet, BaseCommand bcd) {
        if (groupId == 4) {
            onNormalResponseWithParamForFCCTRL(isAck, packet, bcd);
        } else if (groupId == 13) {
            onNormalResponseWithParamForMaintenance(isAck, packet, bcd);
        } else if (groupId == 12) {
            onNormalResponseWithParamForTelemetry(isAck, packet, bcd);
        } else if (groupId == 9) {
            onNormalResponseWithParamForGimbal(isAck, packet, bcd);
        } else if (groupId == 11) {
            onNormalResponseWithParamWithRcCTRL(isAck, packet, bcd);
        } else if (groupId == 3) {
            onNormalResponse(isAck, packet, bcd);
        } else if (groupId == 14) {
            onNormalResponse(isAck, packet, bcd);
        } else if (groupId == 1) {
            if (msgId == 12) {
                onNormalResponse(isAck, packet, bcd);
            }
            if (msgId == 21) {
                onNormalFormatResonse(isAck, packet, bcd);
            }
        }
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getIUMInfo(int imuType, UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.getIMUInfoCmd(imuType);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void cloudCalibration(int status, UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.setCloudCalibrationCmd(status);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void checkCloudCalibrationProgress(UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.checkCloudCalibrationCmd();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setLowPowerOperation(UiCallBackListener listener, int lowPowerValue, int seriousLowPowerValue, int lowPowerOpt, int seriousLowPowerOpt) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setLowPowerOperation(lowPowerValue, seriousLowPowerValue, lowPowerOpt, seriousLowPowerOpt);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getLowPowerOperation(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getLowPowerOperation();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void rcMatchCodeOrNot(int codeType) {
        FcCollection fc = new FcCollection(this, null);
        BaseCommand cmd = fc.RCMatchOrCancelCode(codeType);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setOpticFlow(UiCallBackListener listener, boolean isOpen) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setOpticFlow(isOpen);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getOpticFlow(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getOpticFlow();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setAttitudeSensitivity(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAttitudeSensitivity(rollPercent, pitchPercent);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setYawSensitivity(UiCallBackListener listener, int yawPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setYawSensitivity(yawPercent);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getSensitivity(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getSensitivity();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setBrakeSens(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setBrakeSens(rollPercent, pitchPercent);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getBrakeSens(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getBrakeSens();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setYawTrip(UiCallBackListener listener, int yawValue) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setYawTrip(yawValue);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getYawTrip(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getYawTrip();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setUpDownRockerExp(UiCallBackListener listener, int throttlePercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setUpDownRockerExp(throttlePercent);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setLeftRightRockerExp(UiCallBackListener listener, int yawPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setLeftRightRockerExp(yawPercent);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setGoBackRockerExp(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setGoBackRockerExp(rollPercent, pitchPercent);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getRockerExp(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getRockerExp();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setRcCtrlMode(UiCallBackListener listener, byte mode) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setCtrlMode(mode);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getRcCtrlMode(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getCtrlMode();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void checkMatchCodeProgress(UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.checkMatchCodeProgress();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void rcCalibration(int cmdTyp, UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.rcCalibration(cmdTyp);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void checkRcCilabration(UiCallBackListener uiCallBackListener) {
        FcCollection fc = new FcCollection(this, uiCallBackListener);
        BaseCommand cmd = fc.checkRCCalibration();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void checkIMUException(int sensorType, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).checkIMUException(sensorType));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void restSystemParams(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).restSystemParams());
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setSportMode(UiCallBackListener uiCallBackListener, int enable) {
        sendCmd(new FcCollection(this, uiCallBackListener).setSportMode(enable));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getSportMode(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).getSportMode());
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setAutoHomePoint(int enable, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).setAutoHomePoint(enable));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getAutoHomePoint(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).getAutoHomePoint());
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setEnableTripod(int enable, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).setEnableTripod(enable));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setEnableAerailShot(int enable, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).setEnableAerailShot(enable));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setEnableFixwing(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setEnableFixwing();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setDisenableFixwing(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setDisenableFixwing();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setEnableHeadingFree(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setEnableHeadingFree();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setDisenableHeadingFree(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setDisenableHeadingFree();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setAiFollowEnableBack(int value, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowEnableBack(value);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getAiFollowEnableBack(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAiFollowEnableBack();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setFlyHeight(UiCallBackListener listener, float paramData) {
        sendCmd(new FcCollection(this, listener).setFcParam((byte) 5, paramData));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getFlyHeight(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getFcParam((byte) 5));
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void openCheckIMU(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.checkIMUInfoCmd();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void openAccurateLanding(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAccurateLanding(1);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void closeAccurateLanding(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAccurateLanding(0);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void getAccurateLanding(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAccurateLanding();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setUpdateHeadingFree(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setUpdateHeadingFree();
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setPanoramaPhotographType(UiCallBackListener listener, int panoramaPhotographType) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setPanoramaPhotographType(panoramaPhotographType);
        sendCmd(cmd);
    }

    @Override // com.fimi.x8sdk.ivew.IFcCtrlAction
    public void setPanoramaPhotographState(UiCallBackListener listener, byte panoramaPhotographState) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setPanoramaPhotographState(panoramaPhotographState);
        sendCmd(cmd);
    }
}
