package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.session.VideodDataListener;
import com.fimi.x8sdk.common.BasePresenter;

/* loaded from: classes2.dex */
public class VideoPresenter extends BasePresenter {
    public VideoPresenter(VideodDataListener listener) {
        addNoticeListener(listener);
    }

    public void removeVideaListener() {
        removeFpvListener();
    }
}
