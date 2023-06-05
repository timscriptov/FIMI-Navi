package com.fimi.libdownfw.ivew;

import com.fimi.network.DownFwService;


public interface IFirmwareDownView {
    void showDownFwProgress(DownFwService.DownState downState, int i, String str);
}
