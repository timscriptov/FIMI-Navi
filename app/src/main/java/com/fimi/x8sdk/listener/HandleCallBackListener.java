package com.fimi.x8sdk.listener;

import com.fimi.kernel.dataparser.usb.CmdResult;

/* loaded from: classes2.dex */
public interface HandleCallBackListener {

    /* loaded from: classes2.dex */
    public interface CallBack {
        void onComplete(CmdResult cmdResult);
    }

    /* loaded from: classes2.dex */
    public interface CallBackWithParam<T> {
        void onError(CmdResult cmdResult);

        void onResult(T t);
    }
}
