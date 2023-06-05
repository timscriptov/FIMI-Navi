package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.command.FwUpdateCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.AckAiSetGravitationPrameter;
import com.fimi.x8sdk.dataparser.cmd.CmdAiAutoPhoto;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePoints;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction;
import com.fimi.x8sdk.entity.GpsInfoCmd;
import com.fimi.x8sdk.ivew.IFcAction;


public class FcPresenter extends BasePresenter implements IFcAction {
    public FcPresenter() {
        addNoticeListener();
    }

    @Override
    public void takeOff(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).takeOff((byte) 16));
    }

    @Override
    public void takeOffExit(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).takeOff((byte) 19));
    }

    @Override
    public void land(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).land((byte) 21));
    }

    @Override
    public void landExit(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).land((byte) 24));
    }

    @Override
    public void setFollowStandby(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowCmd(84));
    }

    @Override
    public void setFollowExcute(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowCmd(80));
    }

    @Override
    public void setFollowExit(UiCallBackListener listener) {
        sendCmd(new FcCollection(this, listener).setAiFollowCmd(83));
    }

    @Override
    public void getFwVersion(byte moduleName, byte type, UiCallBackListener listener) {
        sendCmd(new FwUpdateCollection(this, listener).getVersion(moduleName, type));
    }

    @Override
    public void setAiFollowPoint2Point(double longitude, double latitude, int altitude, int speed, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowPoint2Point(longitude, latitude, altitude, speed);
        sendCmd(cmd);
    }

    @Override
    public void setAiFollowPoint2PointExcute(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowPoint2PointExcute((byte) 48);
        sendCmd(cmd);
    }

    @Override
    public void getAiFollowPoint(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowPoint2PointExcute((byte) 53);
        sendCmd(cmd);
    }

    @Override
    public void setAiFollowPoint2PointExite(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowPoint2PointExcute((byte) 51);
        sendCmd(cmd);
    }

    @Override
    public void setAiSurroundExcute(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiSurroundExcute(FcCollection.MSG_SET_SURROUND_EXCUTE);
        sendCmd(cmd);
    }

    @Override
    public void setAiSurroundExite(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiSurroundExcute(FcCollection.MSG_SET_SURROUND_EXIT);
        sendCmd(cmd);
    }

    @Override
    public void setAiSurroundCiclePoint(double longitude, double latitude, float altitude, double longitudeTakeoff, double latitudeTakeoff, float altitudeTakeoff, int type, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiSurroundPoint(68, longitude, latitude, altitude, longitudeTakeoff, latitudeTakeoff, altitudeTakeoff, type);
        sendCmd(cmd);
    }

    @Override
    public void getAiSurroundCiclePoint(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAiSurroundPoint();
        sendCmd(cmd);
    }

    @Override
    public void setAiSurroundSpeed(int value, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiSurroundSpeed(FcCollection.MSG_SET_SURROUND_SPEED, value);
        sendCmd(cmd);
    }

    @Override
    public void setAiSurroundOrientation(int value, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiSurroundOrientation(FcCollection.MSG_SET_SURROUND_DEVICE_ORIENTATION, value);
        sendCmd(cmd);
    }

    @Override
    public void getAiSurroundSpeed(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAiSurroundSpeed();
        sendCmd(cmd);
    }

    @Override
    public void getAiSurroundOrientation(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAiSurroundOrientation();
        sendCmd(cmd);
    }

    @Override
    public void setAiRetureHome(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiRetureHome(26);
        sendCmd(cmd);
    }

    @Override
    public void setAiRetureHomeExite(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiRetureHome(29);
        sendCmd(cmd);
    }

    @Override
    public void setAiLinePoints(CmdAiLinePoints points, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiLinePoints(points);
        sendCmd(cmd);
    }

    @Override
    public void setAiLinePointsAction(CmdAiLinePointsAction action, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiLinePointsAction(action);
        sendCmd(cmd);
    }

    @Override
    public void setAiLineExcute(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiLineExcute();
        sendCmd(cmd);
    }

    @Override
    public void setAiLineExite(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiLineExite();
        sendCmd(cmd);
    }

    @Override
    public void getAiLinePoint(int number, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAiLinePoint(number);
        sendCmd(cmd);
    }

    @Override
    public void setAiAutoPhotoValue(CmdAiAutoPhoto v, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiAutoPhotoValue(v);
        sendCmd(cmd);
    }

    @Override
    public void getAiLinePointValue(int number, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAiLinePointsAction(number);
        sendCmd(cmd);
    }

    @Override
    public void setAiAutoPhotoExcute(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiAutoPhotoExcute();
        sendCmd(cmd);
    }

    @Override
    public void setAiAutoPhotoExit(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiAutoPhotoExit();
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

    @Override
    public void reponseCmd(boolean isAck, int groupId, int msgId, ILinkMessage packet, BaseCommand bcd) {
        if (groupId == 3) {
            if (msgId == 103) {
                onNormalResponseWithParamForNav2Screw(isAck, packet, bcd);
            } else if (msgId == 36) {
                onNormalResponseWithParamForNav(isAck, packet, bcd);
            } else {
                onNormalResponseWithParamForNav(isAck, packet, bcd);
            }
        } else if (groupId == 16 && 177 == msgId) {
            onNormalResponseWithParam(isAck, packet, bcd);
        }
    }

    @Override
    public void syncTime(UiCallBackListener callBackListener) {
        FcCollection fc = new FcCollection(this, callBackListener);
        BaseCommand cmd = fc.setSyncTimeCmd();
        sendCmd(cmd);
    }

    @Override
    public void setAiVcOpen(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowVcEnable(96);
        sendCmd(cmd);
    }

    @Override
    public void setAiVcClose(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowVcEnable(97);
        sendCmd(cmd);
    }

    @Override
    public void setAiVcNotityFc(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowVcEnable(98);
        sendCmd(cmd);
    }

    @Override
    public void setAiFollowModle(int type, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowModle(type);
        sendCmd(cmd);
    }

    @Override
    public void getAiFollowModle(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAiFollowModle();
        sendCmd(cmd);
    }

    @Override
    public void setAiFollowSpeed(int value, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setAiFollowSpeed(value);
        sendCmd(cmd);
    }

    @Override
    public void getAiFollowSpeed(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getAiFollowSpeed();
        sendCmd(cmd);
    }

    @Override
    public void setHomePoint(float h, double lat, double lng, int mode, float accuracy, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setHomePoint(h, lat, lng, mode, accuracy);
        sendCmd(cmd);
    }

    @Override
    public void getGravitationPrameter(UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand cmd = fcCollection.getGravitationPrameter();
        sendCmd(cmd);
    }

    @Override
    public void setGravitationStart(UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand cmd = fcCollection.setGravitationStart();
        sendCmd(cmd);
    }

    @Override
    public void setGravitationPause(UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand cmd = fcCollection.setGravitationPause();
        sendCmd(cmd);
    }

    @Override
    public void setGravitationResume(UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand cmd = fcCollection.setGravitationResume();
        sendCmd(cmd);
    }

    @Override
    public void setGravitationExite(UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand cmd = fcCollection.setGravitationExite();
        sendCmd(cmd);
    }

    @Override
    public void setGravitationPrameter(AckAiSetGravitationPrameter prameter, UiCallBackListener listener) {
        FcCollection fcCollection = new FcCollection(this, listener);
        BaseCommand cmd = fcCollection.setGravitationPrameter(prameter);
        sendCmd(cmd);
    }

    @Override
    public void setScrewPrameter(AckAiScrewPrameter prameter, UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setScrewPrameter(prameter);
        sendCmd(cmd);
    }

    @Override
    public void getScrewPrameter(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getScrewPrameter();
        sendCmd(cmd);
    }

    @Override
    public void setScrewStart(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setScrewStart();
        sendCmd(cmd);
    }

    @Override
    public void setScrewPause(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setScrewPause();
        sendCmd(cmd);
    }

    @Override
    public void setScrewResume(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setScrewResume();
        sendCmd(cmd);
    }

    @Override
    public void setScrewExite(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.setScrewExite();
        sendCmd(cmd);
    }

    @Override
    public void sysCtrlMode2AiVc(UiCallBackListener listener, int ctrlMode) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.sysCtrlMode2AiVc(ctrlMode);
        sendCmd(cmd);
    }

    @Override
    public void setPressureInfo(float alt, float hPa) {
        FcCollection fc = new FcCollection(this, null);
        BaseCommand cmd = fc.setPressureInfo(alt, hPa);
        sendCmd(cmd);
    }

    @Override
    public void setGpsInfo(GpsInfoCmd gps) {
        FcCollection fc = new FcCollection(this, null);
        BaseCommand cmd = fc.setGpsInfo(gps);
        sendCmd(cmd);
    }

    @Override
    public void getNoFlyNormal(UiCallBackListener listener) {
        FcCollection fc = new FcCollection(this, listener);
        BaseCommand cmd = fc.getNoFlyNormal();
        sendCmd(cmd);
    }
}
