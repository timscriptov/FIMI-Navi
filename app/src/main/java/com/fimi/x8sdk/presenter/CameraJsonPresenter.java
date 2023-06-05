package com.fimi.x8sdk.presenter;

import com.alibaba.fastjson.JSONObject;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.session.JsonListener;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.connect.JsonNoticeManager;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.ivew.ICamJsonAction;
import com.fimi.x8sdk.jsonResult.CurParamsJson;


public class CameraJsonPresenter extends BasePresenter implements ICamJsonAction, JsonListener {
    public CameraJsonPresenter() {
        addNoticeListener(this);
    }

    @Override
    public void startSession() {
        sendCmd(new CameraJsonCollection().startSession());
    }

    @Override
    public void setCameraSys(String param) {
        sendCmd(new CameraJsonCollection().setCamera(param));
    }

    @Override
    public void setVideoResolution(String param) {
        sendCmd(new CameraJsonCollection().setVideoResolution(param));
    }

    @Override
    public void setPhotoSize(String param) {
        sendCmd(new CameraJsonCollection().setPhotoSize(param));
    }

    @Override
    public void setPhotoFormat(String param) {
        sendCmd(new CameraJsonCollection().setPhotoFormat(param));
    }

    @Override
    public void formatTFCard(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().formatTFCard(callBackListener));
    }

    @Override
    public void defaltSystem(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().defaultSystem(callBackListener));
    }

    @Override
    public void getCameraEV() {
        sendCmd(new CameraJsonCollection().getCameraEV());
    }

    @Override
    public void getCameraShutter() {
        sendCmd(new CameraJsonCollection().getCameraShutter());
    }

    @Override
    public void setCameraDeControl(String param, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().setCameraDeControl(param, callBackListener));
    }

    @Override
    public void getCameraISO() {
        sendCmd(new CameraJsonCollection().getCameraISO());
    }

    @Override
    public void getCameraIsoOptions() {
        sendCmd(new CameraJsonCollection().getCameraIsoOptions());
    }

    @Override
    public void getCameraAwb() {
        sendCmd(new CameraJsonCollection().getCameraAWB());
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IDataCallBack
    public void onSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        super.onSendTimeOut(groupId, msgId, bcd);
        JsonNoticeManager.getNoticeManager().sendOutTime();
    }

    @Override
    public void onProcess(int msgId, JSONObject respJson) {
        JsonNoticeManager.getNoticeManager().onProcess(msgId, respJson);
    }

    public void removeLisnters() {
        removeNoticeListener();
        NoticeManager.getInstance().removeJsonListener(this);
    }

    @Override
    public void setCameraIso(String param) {
        CurParamsJson curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        if (curParamsJson != null && !curParamsJson.getIso().trim().equalsIgnoreCase(param.trim())) {
            sendCmd(new CameraJsonCollection().setCameraIso(param));
        }
    }

    @Override
    public void setCameraShutterTime(String shutterTime) {
        CurParamsJson curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        if (!curParamsJson.getShutter_time().trim().equalsIgnoreCase(shutterTime.trim())) {
            sendCmd(new CameraJsonCollection().setCameraShutterTime(shutterTime));
        }
    }

    @Override
    public void getCameraShutterOptions() {
        sendCmd(new CameraJsonCollection().getCameraShutterOptions());
    }

    @Override
    public void getCurAllParams(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCameraCurParams(callBackListener));
    }

    @Override
    public void setCameraEV(String param) {
        CurParamsJson curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        if (param != null && curParamsJson != null && !curParamsJson.getAe_bias().trim().equalsIgnoreCase(param.trim())) {
            sendCmd(new CameraJsonCollection().setCameraEV(param));
        }
    }

    @Override
    public void getCameraKeyOptions(String param) {
        sendCmd(new CameraJsonCollection().getCameraKeyOptions(param));
    }

    @Override
    public void getCameraKeyOptions(String param, JsonUiCallBackListener listener) {
        sendCmd(new CameraJsonCollection().getCameraKeyOptions(param, listener));
    }

    @Override
    public void setCameraKeyParams(String paramValue, String paramKey, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().setCameraKeyParam(paramValue, paramKey, callBackListener));
    }

    @Override
    public void getCurCameraParams(String paramKey, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCurCameraParams(paramKey, callBackListener));
    }

    @Override
    public void setCameraFocuse(String param, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().setCameraFocuse(param, callBackListener));
    }

    @Override
    public void getCameraFocuse(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCameraFocuse(callBackListener));
    }

    @Override
    public void getCameraFocuseValues(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCameraFocuseValues(callBackListener));
    }

    @Override
    public void deleteOnlineFile(String path, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().deleteFile(path, callBackListener));
    }

    @Override
    public void setInterestMetering(String param) {
        sendCmd(new CameraJsonCollection().setInterestMetering(param));
    }
}
