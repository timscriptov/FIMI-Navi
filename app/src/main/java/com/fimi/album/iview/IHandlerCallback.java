package com.fimi.album.iview;

import android.os.Handler;
import android.os.Message;

/* loaded from: classes.dex */
public interface IHandlerCallback extends Handler.Callback {
    @Override
        // android.os.Handler.Callback
    boolean handleMessage(Message message);
}
