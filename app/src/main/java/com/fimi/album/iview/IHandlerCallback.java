package com.fimi.album.iview;

import android.os.Handler;
import android.os.Message;


public interface IHandlerCallback extends Handler.Callback {
    @Override
        // android.os.Handler.Callback
    boolean handleMessage(Message message);
}
