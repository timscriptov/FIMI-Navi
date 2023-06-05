package com.fimi.kernel.base;

/* loaded from: classes.dex */
public abstract class BasePresenter<T> {
    public T mView;

    public void attachVM(T v) {
        this.mView = v;
    }

    public void detachVM() {
        this.mView = null;
    }
}
