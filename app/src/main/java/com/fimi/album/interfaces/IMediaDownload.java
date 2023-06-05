package com.fimi.album.interfaces;

import com.fimi.album.entity.MediaModel;

/* loaded from: classes.dex */
public interface IMediaDownload {
    void addData(MediaModel mediaModel);

    void startDownload();

    void stopDownload();
}
