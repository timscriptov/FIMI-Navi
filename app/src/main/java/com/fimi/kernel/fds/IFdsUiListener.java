package com.fimi.kernel.fds;

/* loaded from: classes.dex */
public interface IFdsUiListener {
    void onFailure(IFdsFileModel iFdsFileModel);

    void onProgress(IFdsFileModel iFdsFileModel, int i);

    void onStop(IFdsFileModel iFdsFileModel);

    void onSuccess(IFdsFileModel iFdsFileModel);
}
