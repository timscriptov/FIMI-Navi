package com.fimi.app.x8s.ui.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.x8sdk.common.BasePresenter;


public class X8CameraParameterPresenter extends BasePresenter {
    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IPersonalDataCallBack
    public void onPersonalDataCallBack(int groupId, int cmdId, ILinkMessage packet) {
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IPersonalDataCallBack
    public void onPersonalSendTimeOut(int groupId, int cmdId, BaseCommand bcd) {
    }

    public void deleteSdcard() {
    }

    public void setPreviewVideo(int resolution) {
    }

    public void setVideoParamters() {
    }

    public void setTakePhotoParamter() {
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IDataCallBack
    public void onDataCallBack(int groupId, int cmdId, ILinkMessage packet) {
    }
}
