package com.fimi.x8sdk.connect;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;

/* loaded from: classes2.dex */
public class ConnectStatusManager {
    Handler handler = new Handler(Looper.getMainLooper()) { // from class: com.fimi.x8sdk.connect.ConnectStatusManager.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public void onDataRecieved(LinkPacket4 packet4) {
    }
}
