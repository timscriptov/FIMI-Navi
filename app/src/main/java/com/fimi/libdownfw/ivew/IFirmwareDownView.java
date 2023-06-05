package com.fimi.libdownfw.ivew;

import com.fimi.network.DownFwService;

/* loaded from: classes.dex */
public interface IFirmwareDownView {
    void showDownFwProgress(DownFwService.DownState downState, int i, String str);
}
