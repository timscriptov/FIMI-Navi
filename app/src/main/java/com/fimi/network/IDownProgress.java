package com.fimi.network;


public interface IDownProgress {
    void onProgress(DownFwService.DownState downState, int i, String str);
}
