package com.fimi.x8sdk.connect.datatype;

import com.fimi.kernel.connect.session.NoticeManager;

/* loaded from: classes2.dex */
public class VideoDataChanel implements IDataChanel {
    @Override // com.fimi.x8sdk.connect.datatype.IDataChanel
    public void forwardData(byte[] data) {
        NoticeManager.getInstance().onRawDataCallBack(data);
    }
}
