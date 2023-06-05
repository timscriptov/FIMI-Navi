package com.fimi.kernel.utils;

import android.content.Context;

import java.util.HashMap;


public class MediaSDK {
    private static MediaSDK mediaSDK = null;
    private static final HashMap mapDur = new HashMap();

    public static MediaSDK getInstance() {
        if (mediaSDK == null) {
            mediaSDK = new MediaSDK();
        }
        return mediaSDK;
    }

    public static HashMap<String, String> getMapDur() {
        return mapDur;
    }

    public void init(Context context) {
        FrescoImageLoader.initFresco(context);
    }

    public void shutdown() {
        FrescoImageLoader.shutdown();
    }
}
