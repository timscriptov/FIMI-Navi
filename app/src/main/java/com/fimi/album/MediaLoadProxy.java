package com.fimi.album;

import com.fimi.album.download.interfaces.IMediaFileLoad;

/* loaded from: classes.dex */
public class MediaLoadProxy implements IMediaFileLoad {
    IMediaFileLoad load;

    public void setMediaLoad(IMediaFileLoad load) {
        this.load = load;
    }

    @Override // com.fimi.album.download.interfaces.IMediaFileLoad
    public void startLoad() {
        this.load.startLoad();
    }

    @Override // com.fimi.album.download.interfaces.IMediaFileLoad
    public void stopLoad() {
        this.load.stopLoad();
    }
}
