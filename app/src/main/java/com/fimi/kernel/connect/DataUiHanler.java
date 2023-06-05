package com.fimi.kernel.connect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.ILinkMessage;


public class DataUiHanler extends Handler {
    @Override
    public void handleMessage(@NonNull Message msg) {
        Bundle bundle = msg.getData();
        IPersonalDataCallBack callBack = (IPersonalDataCallBack) msg.obj;
        switch (msg.what) {
            case 0:
                ILinkMessage lk = (ILinkMessage) bundle.getSerializable("target");
                callBack.onPersonalDataCallBack(msg.arg1, msg.arg2, lk);
                return;
            case 1:
                BaseCommand bcd = (BaseCommand) bundle.getSerializable("target");
                callBack.onPersonalSendTimeOut(msg.arg1, msg.arg2, bcd);
                return;
            default:
        }
    }
}
