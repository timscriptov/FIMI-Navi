package com.fimi.x8sdk.controller;

import android.content.Context;

import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.AckAiSetGravitationPrameter;
import com.fimi.x8sdk.dataparser.cmd.CmdAiAutoPhoto;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePoints;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction;
import com.fimi.x8sdk.entity.GpsInfoCmd;
import com.fimi.x8sdk.ivew.IFcAction;
import com.fimi.x8sdk.presenter.FcPresenter;


public class FcManager implements IFcAction {
    IFcAction fcAction = new FcPresenter();

    public void setContext(Context context) {
        ((FcPresenter) this.fcAction).setContext(context);
    }

    @Override
    public void takeOff(UiCallBackListener listener) {
        this.fcAction.takeOff(listener);
    }

    @Override
    public void takeOffExit(UiCallBackListener listener) {
        this.fcAction.takeOffExit(listener);
    }

    @Override
    public void land(UiCallBackListener listener) {
        this.fcAction.land(listener);
    }

    @Override
    public void landExit(UiCallBackListener listener) {
        this.fcAction.landExit(listener);
    }

    @Override
    public void setFollowStandby(UiCallBackListener listener) {
        this.fcAction.setFollowStandby(listener);
    }

    @Override
    public void setFollowExcute(UiCallBackListener listener) {
        this.fcAction.setFollowExcute(listener);
    }

    @Override
    public void setFollowExit(UiCallBackListener listener) {
        this.fcAction.setFollowExit(listener);
    }

    @Override
    public void getFwVersion(byte moduleName, byte type, UiCallBackListener listener) {
        this.fcAction.getFwVersion(moduleName, type, listener);
    }

    @Override
    public void setAiFollowPoint2Point(double longitude, double latitude, int altitude, int speed, UiCallBackListener listener) {
        this.fcAction.setAiFollowPoint2Point(longitude, latitude, altitude, speed, listener);
    }

    @Override
    public void setAiFollowPoint2PointExcute(UiCallBackListener listener) {
        this.fcAction.setAiFollowPoint2PointExcute(listener);
    }

    @Override
    public void getAiFollowPoint(UiCallBackListener listener) {
        this.fcAction.getAiFollowPoint(listener);
    }

    @Override
    public void setAiFollowPoint2PointExite(UiCallBackListener listener) {
        this.fcAction.setAiFollowPoint2PointExite(listener);
    }

    @Override
    public void setAiSurroundExcute(UiCallBackListener listener) {
        this.fcAction.setAiSurroundExcute(listener);
    }

    @Override
    public void setAiSurroundExite(UiCallBackListener listener) {
        this.fcAction.setAiSurroundExite(listener);
    }

    @Override
    public void setAiSurroundCiclePoint(double longitude, double latitude, float altitude, double longitudeTakeoff, double latitudeTakeoff, float altitudeTakeoff, int type, UiCallBackListener listener) {
        this.fcAction.setAiSurroundCiclePoint(longitude, latitude, altitude, longitudeTakeoff, latitudeTakeoff, altitudeTakeoff, type, listener);
    }

    @Override
    public void getAiSurroundCiclePoint(UiCallBackListener listener) {
        this.fcAction.getAiSurroundCiclePoint(listener);
    }

    @Override
    public void setAiSurroundSpeed(int value, UiCallBackListener listener) {
        this.fcAction.setAiSurroundSpeed(value, listener);
    }

    @Override
    public void setAiSurroundOrientation(int value, UiCallBackListener listener) {
        this.fcAction.setAiSurroundOrientation(value, listener);
    }

    @Override
    public void getAiSurroundSpeed(UiCallBackListener listener) {
        this.fcAction.getAiSurroundSpeed(listener);
    }

    @Override
    public void getAiSurroundOrientation(UiCallBackListener listener) {
        this.fcAction.getAiSurroundOrientation(listener);
    }

    @Override
    public void setAiRetureHome(UiCallBackListener listener) {
        this.fcAction.setAiRetureHome(listener);
    }

    @Override
    public void setAiRetureHomeExite(UiCallBackListener listener) {
        this.fcAction.setAiRetureHomeExite(listener);
    }

    @Override
    public void setAiLinePoints(CmdAiLinePoints points, UiCallBackListener listener) {
        this.fcAction.setAiLinePoints(points, listener);
    }

    @Override
    public void setAiLinePointsAction(CmdAiLinePointsAction action, UiCallBackListener listener) {
        this.fcAction.setAiLinePointsAction(action, listener);
    }

    @Override
    public void setAiLineExcute(UiCallBackListener listener) {
        this.fcAction.setAiLineExcute(listener);
    }

    @Override
    public void setAiLineExite(UiCallBackListener listener) {
        this.fcAction.setAiLineExite(listener);
    }

    @Override
    public void getAiLinePoint(int number, UiCallBackListener listener) {
        this.fcAction.getAiLinePoint(number, listener);
    }

    @Override
    public void getAiLinePointValue(int number, UiCallBackListener listener) {
        this.fcAction.getAiLinePointValue(number, listener);
    }

    @Override
    public void setAiAutoPhotoValue(CmdAiAutoPhoto cmd, UiCallBackListener listener) {
        this.fcAction.setAiAutoPhotoValue(cmd, listener);
    }

    @Override
    public void setAiAutoPhotoExcute(UiCallBackListener listener) {
        this.fcAction.setAiAutoPhotoExcute(listener);
    }

    @Override
    public void setAiAutoPhotoExit(UiCallBackListener listener) {
        this.fcAction.setAiAutoPhotoExit(listener);
    }

    @Override
    public void syncTime(UiCallBackListener callBackListener) {
        this.fcAction.syncTime(callBackListener);
    }

    @Override
    public void setAiVcOpen(UiCallBackListener listener) {
        this.fcAction.setAiVcOpen(listener);
    }

    @Override
    public void setAiVcClose(UiCallBackListener listener) {
        this.fcAction.setAiVcClose(listener);
    }

    @Override
    public void setAiVcNotityFc(UiCallBackListener listener) {
        this.fcAction.setAiVcNotityFc(listener);
    }

    @Override
    public void setAiFollowModle(int type, UiCallBackListener listener) {
        this.fcAction.setAiFollowModle(type, listener);
    }

    @Override
    public void getAiFollowModle(UiCallBackListener listener) {
        this.fcAction.getAiFollowModle(listener);
    }

    @Override
    public void setAiFollowSpeed(int value, UiCallBackListener listener) {
        this.fcAction.setAiFollowSpeed(value, listener);
    }

    @Override
    public void getAiFollowSpeed(UiCallBackListener listener) {
        this.fcAction.getAiFollowSpeed(listener);
    }

    @Override
    public void setHomePoint(float h, double lat, double lng, int mode, float accuracy, UiCallBackListener listener) {
        this.fcAction.setHomePoint(h, lat, lng, mode, accuracy, listener);
    }

    @Override
    public void getGravitationPrameter(UiCallBackListener listener) {
        this.fcAction.getGravitationPrameter(listener);
    }

    @Override
    public void setGravitationPrameter(AckAiSetGravitationPrameter prameter, UiCallBackListener listener) {
        this.fcAction.setGravitationPrameter(prameter, listener);
    }

    @Override
    public void setGravitationStart(UiCallBackListener listener) {
        this.fcAction.setGravitationStart(listener);
    }

    @Override
    public void setGravitationPause(UiCallBackListener listener) {
        this.fcAction.setGravitationPause(listener);
    }

    @Override
    public void setGravitationResume(UiCallBackListener listener) {
        this.fcAction.setGravitationResume(listener);
    }

    @Override
    public void setGravitationExite(UiCallBackListener listener) {
        this.fcAction.setGravitationExite(listener);
    }

    @Override
    public void setScrewPrameter(AckAiScrewPrameter prameter, UiCallBackListener listener) {
        this.fcAction.setScrewPrameter(prameter, listener);
    }

    @Override
    public void getScrewPrameter(UiCallBackListener listener) {
        this.fcAction.getScrewPrameter(listener);
    }

    @Override
    public void setScrewStart(UiCallBackListener listener) {
        this.fcAction.setScrewStart(listener);
    }

    @Override
    public void setScrewPause(UiCallBackListener listener) {
        this.fcAction.setScrewPause(listener);
    }

    @Override
    public void setScrewResume(UiCallBackListener listener) {
        this.fcAction.setScrewResume(listener);
    }

    @Override
    public void setScrewExite(UiCallBackListener listener) {
        this.fcAction.setScrewExite(listener);
    }

    @Override
    public void sysCtrlMode2AiVc(UiCallBackListener listener, int ctrlMode) {
        this.fcAction.sysCtrlMode2AiVc(listener, ctrlMode);
    }

    @Override
    public void setPressureInfo(float alt, float hPa) {
        this.fcAction.setPressureInfo(alt, hPa);
    }

    @Override
    public void setGpsInfo(GpsInfoCmd gps) {
        this.fcAction.setGpsInfo(gps);
    }

    @Override
    public void getNoFlyNormal(UiCallBackListener listener) {
        this.fcAction.getNoFlyNormal(listener);
    }
}
