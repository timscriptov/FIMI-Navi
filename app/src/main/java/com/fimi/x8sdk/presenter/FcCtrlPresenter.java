package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckGetFcParam;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;
import com.fimi.x8sdk.ivew.IFcCtrlAction;


public class FcCtrlPresenter extends BasePresenter implements IFcCtrlAction {
    public FcCtrlPresenter() {
        addNoticeListener();
    }

    @Override
    public void setReturnHeight(UiCallBackListener retHomeListener, float height) {
        sendCmd(new FcCollection(this, retHomeListener).setReturnHeight(height));
    }

    @Override
    public void reqReturnHeight(UiCallBackListener<AckGetRetHeight> reqRetHome) {
        sendCmd(new FcCollection(this, reqRetHome).getReturnHeight());
    }

    @Override
    public void setLostAction(UiCallBackListener listener, int action) {
        sendCmd(new FcCollection(this, listener).setLostAction((byte) action));
    }

    @Override
    public void getLostAction(UiCallBackListener callBackListenerWithParam) {
        sendCmd(new FcCollection(this, callBackListenerWithParam).getLostAction());
    }

    @Override
    public void setPilotMode(UiCallBackListener listener, byte mode) {
        sendCmd(new FcCollection(this, listener).setPilotMode(mode));
    }

    @Override
    public void getPilotMode(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getPilotMode());
    }

    @Override
    public void setFcParam(UiCallBackListener callBackListener, int paramIndex, float paramData) {
        sendCmd(new FcCollection(this, callBackListener).setFcParam((byte) paramIndex, paramData));
    }

    @Override
    public void getFcParam(UiCallBackListener<Float> listenerWithParam, int paramIndex) {
        sendCmd(new FcCollection(this, listenerWithParam).getFcParam((byte) paramIndex));
    }

    @Override
    public void setGpsSpeedParam(UiCallBackListener callBackListener, float paramData) {
        sendCmd(new FcCollection(this, callBackListener).setFcParam((byte) 3, paramData));
    }

    @Override
    public void getGpsSpeedParam(UiCallBackListener<AckGetFcParam> listenerWithParam) {
        sendCmd(new FcCollection(this, listenerWithParam).getFcParam((byte) 3));
    }

    @Override
    public void setFlyDistanceParam(UiCallBackListener callBackListener, float paramData) {
        sendCmd(new FcCollection(this, callBackListener).setFcParam((byte) 7, paramData));
    }

    @Override
    public void getFlyDistanceParam(UiCallBackListener<AckGetFcParam> listenerWithParam) {
        sendCmd(new FcCollection(this, listenerWithParam).getFcParam((byte) 7));
    }

    @Override
    public void setCalibrationStart(int type, int cmd, int mode, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand c = fc.setCalibrationStart(type, cmd, mode);
        sendCmd(c);
    }

    @Override
    public void getCalibrationState(int sensorType, int type, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getCalibrationState(sensorType, type);
        sendCmd(cmd);
    }

    @Override
    public void getAircrftCalibrationState(int sensorType, int type, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAircrftCalibrationState(sensorType, type);
        sendCmd(cmd);
    }

    @Override
    public void setAircrftCalibrationStart(int type, int cmd, int mode, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand c = fc.setAircrftCalibrationStart(type, cmd, mode);
        sendCmd(c);
    }

    @Override
    public void setFormatStorage(int opcode, int devid, UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand command = fcCollection.setFormatStorage(opcode, devid);
        sendCmd(command);
    }

    @Override
    public void getFormatStorage(int devid, UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand command = fcCollection.getFormatStorage(devid);
        sendCmd(command);
    }

    @Override
    public void setApMode(byte mode, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setApMode(mode);
        sendCmd(cmd);
    }

    @Override
    public void setApModeRestart(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setApModeRestart();
        sendCmd(cmd);
    }

    @Override
    public void getApMode(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getApMode();
        sendCmd(cmd);
    }

    @Override
    public void onSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
    }

    @Override
    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
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

    @Override
    public void getIUMInfo(int imuType, UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.getIMUInfoCmd(imuType);
        sendCmd(cmd);
    }

    @Override
    public void cloudCalibration(int status, UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.setCloudCalibrationCmd(status);
        sendCmd(cmd);
    }

    @Override
    public void checkCloudCalibrationProgress(UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.checkCloudCalibrationCmd();
        sendCmd(cmd);
    }

    @Override
    public void setLowPowerOperation(UiCallBackListener listener, int lowPowerValue, int seriousLowPowerValue, int lowPowerOpt, int seriousLowPowerOpt) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setLowPowerOperation(lowPowerValue, seriousLowPowerValue, lowPowerOpt, seriousLowPowerOpt);
        sendCmd(cmd);
    }

    @Override
    public void getLowPowerOperation(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getLowPowerOperation();
        sendCmd(cmd);
    }

    @Override
    public void rcMatchCodeOrNot(int codeType) {
        FcCollection fc = new FcCollection(this, null);
        BaseCommand cmd = fc.RCMatchOrCancelCode(codeType);
        sendCmd(cmd);
    }

    @Override
    public void setOpticFlow(UiCallBackListener listener, boolean isOpen) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setOpticFlow(isOpen);
        sendCmd(cmd);
    }

    @Override
    public void getOpticFlow(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getOpticFlow();
        sendCmd(cmd);
    }

    @Override
    public void setAttitudeSensitivity(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAttitudeSensitivity(rollPercent, pitchPercent);
        sendCmd(cmd);
    }

    @Override
    public void setYawSensitivity(UiCallBackListener listener, int yawPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setYawSensitivity(yawPercent);
        sendCmd(cmd);
    }

    @Override
    public void getSensitivity(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getSensitivity();
        sendCmd(cmd);
    }

    @Override
    public void setBrakeSens(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setBrakeSens(rollPercent, pitchPercent);
        sendCmd(cmd);
    }

    @Override
    public void getBrakeSens(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getBrakeSens();
        sendCmd(cmd);
    }

    @Override
    public void setYawTrip(UiCallBackListener listener, int yawValue) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setYawTrip(yawValue);
        sendCmd(cmd);
    }

    @Override
    public void getYawTrip(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getYawTrip();
        sendCmd(cmd);
    }

    @Override
    public void setUpDownRockerExp(UiCallBackListener listener, int throttlePercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setUpDownRockerExp(throttlePercent);
        sendCmd(cmd);
    }

    @Override
    public void setLeftRightRockerExp(UiCallBackListener listener, int yawPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setLeftRightRockerExp(yawPercent);
        sendCmd(cmd);
    }

    @Override
    public void setGoBackRockerExp(UiCallBackListener listener, int rollPercent, int pitchPercent) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setGoBackRockerExp(rollPercent, pitchPercent);
        sendCmd(cmd);
    }

    @Override
    public void getRockerExp(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getRockerExp();
        sendCmd(cmd);
    }

    @Override
    public void setRcCtrlMode(UiCallBackListener listener, byte mode) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setCtrlMode(mode);
        sendCmd(cmd);
    }

    @Override
    public void getRcCtrlMode(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getCtrlMode();
        sendCmd(cmd);
    }

    @Override
    public void checkMatchCodeProgress(UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.checkMatchCodeProgress();
        sendCmd(cmd);
    }

    @Override
    public void rcCalibration(int cmdTyp, UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.rcCalibration(cmdTyp);
        sendCmd(cmd);
    }

    @Override
    public void checkRcCilabration(UiCallBackListener uiCallBackListener) {
        FcCollection fc = new FcCollection(this, uiCallBackListener);
        BaseCommand cmd = fc.checkRCCalibration();
        sendCmd(cmd);
    }

    @Override
    public void checkIMUException(int sensorType, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).checkIMUException(sensorType));
    }

    @Override
    public void restSystemParams(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).restSystemParams());
    }

    @Override
    public void setSportMode(UiCallBackListener uiCallBackListener, int enable) {
        sendCmd(new FcCollection(this, uiCallBackListener).setSportMode(enable));
    }

    @Override
    public void getSportMode(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).getSportMode());
    }

    @Override
    public void setAutoHomePoint(int enable, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).setAutoHomePoint(enable));
    }

    @Override
    public void getAutoHomePoint(UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).getAutoHomePoint());
    }

    @Override
    public void setEnableTripod(int enable, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).setEnableTripod(enable));
    }

    @Override
    public void setEnableAerailShot(int enable, UiCallBackListener uiCallBackListener) {
        sendCmd(new FcCollection(this, uiCallBackListener).setEnableAerailShot(enable));
    }

    @Override
    public void setEnableFixwing(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setEnableFixwing();
        sendCmd(cmd);
    }

    @Override
    public void setDisenableFixwing(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setDisenableFixwing();
        sendCmd(cmd);
    }

    @Override
    public void setEnableHeadingFree(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setEnableHeadingFree();
        sendCmd(cmd);
    }

    @Override
    public void setDisenableHeadingFree(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setDisenableHeadingFree();
        sendCmd(cmd);
    }

    @Override
    public void setAiFollowEnableBack(int value, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowEnableBack(value);
        sendCmd(cmd);
    }

    @Override
    public void getAiFollowEnableBack(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAiFollowEnableBack();
        sendCmd(cmd);
    }

    @Override
    public void setFlyHeight(UiCallBackListener listener, float paramData) {
        sendCmd(new FcCollection(this, listener).setFcParam((byte) 5, paramData));
    }

    @Override
    public void getFlyHeight(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).getFcParam((byte) 5));
    }

    @Override
    public void openCheckIMU(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.checkIMUInfoCmd();
        sendCmd(cmd);
    }

    @Override
    public void openAccurateLanding(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAccurateLanding(1);
        sendCmd(cmd);
    }

    @Override
    public void closeAccurateLanding(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAccurateLanding(0);
        sendCmd(cmd);
    }

    @Override
    public void getAccurateLanding(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAccurateLanding();
        sendCmd(cmd);
    }

    @Override
    public void setUpdateHeadingFree(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setUpdateHeadingFree();
        sendCmd(cmd);
    }

    @Override
    public void setPanoramaPhotographType(UiCallBackListener listener, int panoramaPhotographType) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setPanoramaPhotographType(panoramaPhotographType);
        sendCmd(cmd);
    }

    @Override
    public void setPanoramaPhotographState(UiCallBackListener listener, byte panoramaPhotographState) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setPanoramaPhotographState(panoramaPhotographState);
        sendCmd(cmd);
    }
}
