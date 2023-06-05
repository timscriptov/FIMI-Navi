package com.fimi.network.oauth2;

/* loaded from: classes.dex */
public abstract class CallBackListner {
    public abstract void onSuccess(Object obj);

    public void onFailed(String errMsg) {
    }
}
