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

/* loaded from: classes2.dex */
public class CameraJsonPresenter extends BasePresenter implements ICamJsonAction, JsonListener {
    public CameraJsonPresenter() {
        addNoticeListener(this);
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void startSession() {
        sendCmd(new CameraJsonCollection().startSession());
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setCameraSys(String param) {
        sendCmd(new CameraJsonCollection().setCamera(param));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setVideoResolution(String param) {
        sendCmd(new CameraJsonCollection().setVideoResolution(param));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setPhotoSize(String param) {
        sendCmd(new CameraJsonCollection().setPhotoSize(param));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setPhotoFormat(String param) {
        sendCmd(new CameraJsonCollection().setPhotoFormat(param));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void formatTFCard(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().formatTFCard(callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void defaltSystem(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().defaultSystem(callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraEV() {
        sendCmd(new CameraJsonCollection().getCameraEV());
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraShutter() {
        sendCmd(new CameraJsonCollection().getCameraShutter());
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setCameraDeControl(String param, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().setCameraDeControl(param, callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraISO() {
        sendCmd(new CameraJsonCollection().getCameraISO());
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraIsoOptions() {
        sendCmd(new CameraJsonCollection().getCameraIsoOptions());
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraAwb() {
        sendCmd(new CameraJsonCollection().getCameraAWB());
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IDataCallBack
    public void onSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        super.onSendTimeOut(groupId, msgId, bcd);
        JsonNoticeManager.getNoticeManager().sendOutTime();
    }

    @Override // com.fimi.kernel.connect.session.JsonListener
    public void onProcess(int msgId, JSONObject respJson) {
        JsonNoticeManager.getNoticeManager().onProcess(msgId, respJson);
    }

    public void removeLisnters() {
        removeNoticeListener();
        NoticeManager.getInstance().removeJsonListener(this);
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setCameraIso(String param) {
        CurParamsJson curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        if (curParamsJson != null && !curParamsJson.getIso().trim().equalsIgnoreCase(param.trim())) {
            sendCmd(new CameraJsonCollection().setCameraIso(param));
        }
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setCameraShutterTime(String shutterTime) {
        CurParamsJson curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        if (!curParamsJson.getShutter_time().trim().equalsIgnoreCase(shutterTime.trim())) {
            sendCmd(new CameraJsonCollection().setCameraShutterTime(shutterTime));
        }
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraShutterOptions() {
        sendCmd(new CameraJsonCollection().getCameraShutterOptions());
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCurAllParams(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCameraCurParams(callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setCameraEV(String param) {
        CurParamsJson curParamsJson = X8CameraParamsValue.getInstance().getCurParamsJson();
        if (param != null && curParamsJson != null && !curParamsJson.getAe_bias().trim().equalsIgnoreCase(param.trim())) {
            sendCmd(new CameraJsonCollection().setCameraEV(param));
        }
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraKeyOptions(String param) {
        sendCmd(new CameraJsonCollection().getCameraKeyOptions(param));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraKeyOptions(String param, JsonUiCallBackListener listener) {
        sendCmd(new CameraJsonCollection().getCameraKeyOptions(param, listener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setCameraKeyParams(String paramValue, String paramKey, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().setCameraKeyParam(paramValue, paramKey, callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCurCameraParams(String paramKey, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCurCameraParams(paramKey, callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setCameraFocuse(String param, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().setCameraFocuse(param, callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraFocuse(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCameraFocuse(callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void getCameraFocuseValues(JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().getCameraFocuseValues(callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void deleteOnlineFile(String path, JsonUiCallBackListener callBackListener) {
        sendCmd(new CameraJsonCollection().deleteFile(path, callBackListener));
    }

    @Override // com.fimi.x8sdk.ivew.ICamJsonAction
    public void setInterestMetering(String param) {
        sendCmd(new CameraJsonCollection().setInterestMetering(param));
    }
}
