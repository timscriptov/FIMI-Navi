package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.X8VisionCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.ivew.IVcAction;


public class X8VcPresenter extends BasePresenter implements IVcAction {
    public X8VcPresenter() {
        addNoticeListener();
    }

    @Override
    public void setVcRectF(UiCallBackListener listener, int x, int y, int w, int h, int classfier) {
        sendCmd(new X8VisionCollection(this, listener).setVcRectF(x, y, w, h, classfier));
    }

    @Override
    public void setVcFpvMode(UiCallBackListener listener, int mode) {
        sendCmd(new X8VisionCollection(this, listener).setVcFpvMode(mode));
    }

    @Override
    public void setVcFpvLostSeq(UiCallBackListener listener, int seq) {
        sendCmd(new X8VisionCollection(this, listener).setVcFpvLostSeq(seq));
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
        if (groupId == 15) {
            if (msgId == 3) {
                onNormalResponse(isAck, packet, bcd);
                return;
            } else {
                onNormalResponseWithParam(isAck, packet, bcd);
            }
        }
    }
}
