package com.fimi.x8sdk.listener;

import com.fimi.x8sdk.entity.ErrCodeEntity;

import java.util.List;

/* loaded from: classes2.dex */
public interface ErrcodeListener {
    void showErrCode(List<ErrCodeEntity> list);

    void showVcErrCode(int i);
}
