package com.fimi.album.interfaces;

import com.fimi.album.entity.MediaModel;

/* loaded from: classes.dex */
public interface OnDownloadUiListener {
    void onFailure(MediaModel mediaModel);

    void onProgress(MediaModel mediaModel, int i);

    void onStop(MediaModel mediaModel);

    void onSuccess(MediaModel mediaModel);
}
