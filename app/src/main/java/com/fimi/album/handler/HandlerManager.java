package com.fimi.album.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/* loaded from: classes.dex */
public class HandlerManager {
    public static final String TAG = "otherThreadHandler";
    private static HandlerManager mHandlerManager;

    private HandlerManager() {
    }

    public static HandlerManager obtain() {
        if (mHandlerManager == null) {
            synchronized (HandlerManager.class) {
                if (mHandlerManager == null) {
                    mHandlerManager = new HandlerManager();
                }
            }
        }
        return mHandlerManager;
    }

    public Handler getHandlerInOtherThread(Handler.Callback callback) {
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        Handler otherThreadHandler = new Handler(handlerThread.getLooper(), callback);
        return otherThreadHandler;
    }

    public Handler getHandlerInMainThread(Handler.Callback callback) {
        return new Handler(Looper.getMainLooper(), callback);
    }

    public Handler getHandlerInMainThread() {
        return new Handler(Looper.getMainLooper());
    }
}
