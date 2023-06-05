package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.command.X8VisionCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.ivew.IVcAction;

/* loaded from: classes2.dex */
public class X8VcPresenter extends BasePresenter implements IVcAction {
    public X8VcPresenter() {
        addNoticeListener();
    }

    @Override // com.fimi.x8sdk.ivew.IVcAction
    public void setVcRectF(UiCallBackListener listener, int x, int y, int w, int h, int classfier) {
        sendCmd(new X8VisionCollection(this, listener).setVcRectF(x, y, w, h, classfier));
    }

    @Override // com.fimi.x8sdk.ivew.IVcAction
    public void setVcFpvMode(UiCallBackListener listener, int mode) {
        sendCmd(new X8VisionCollection(this, listener).setVcFpvMode(mode));
    }

    @Override // com.fimi.x8sdk.ivew.IVcAction
    public void setVcFpvLostSeq(UiCallBackListener listener, int seq) {
        sendCmd(new X8VisionCollection(this, listener).setVcFpvLostSeq(seq));
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
        if (groupId == 15) {
            switch (msgId) {
                case 3:
                    onNormalResponse(isAck, packet, bcd);
                    return;
                default:
                    onNormalResponseWithParam(isAck, packet, bcd);
                    return;
            }
        }
    }
}
