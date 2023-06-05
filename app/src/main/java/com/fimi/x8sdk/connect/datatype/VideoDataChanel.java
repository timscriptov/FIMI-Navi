package com.fimi.x8sdk.connect.datatype;

import com.fimi.kernel.connect.session.NoticeManager;


public class VideoDataChanel implements IDataChanel {
    @Override
    public void forwardData(byte[] data) {
        NoticeManager.getInstance().onRawDataCallBack(data);
    }
}
