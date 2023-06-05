package com.fimi.x8sdk.controller;

import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.network.BaseManager;


public class X8FlightPlayBackManager extends BaseManager {
    private static volatile X8FlightPlayBackManager x8FlightPlayBackManager = null;

    public static X8FlightPlayBackManager getX8FlightPlayBackManagerInstans() {
        if (x8FlightPlayBackManager == null) {
            synchronized (X8FlightPlayBackManager.class) {
                if (x8FlightPlayBackManager == null) {
                    x8FlightPlayBackManager = new X8FlightPlayBackManager();
                }
            }
        }
        return x8FlightPlayBackManager;
    }

    public void getX8FlightPlaybackLog(String startTime, String endTime, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = getRequestParams();
        requestParams.put("start", "0");
        requestParams.put("pageSize", "10000");
        requestParams.put("startTime", startTime);
        requestParams.put("endTime", endTime);
        String url = HostConstants.SAVE_FDS_URL_2_FIMI_URL_PLAYBACK + "record/fetchFlyRecordByPage";
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, requestParams), disposeDataHandle);
    }
}
