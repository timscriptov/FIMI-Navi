package com.fimi.network;

import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.request.CommonRequest;


public class FwManager extends BaseManager {
    private final String V2 = "v2";
    private final String V3 = "v3";

    public void getX9FwNetDetail(DisposeDataHandle disposeDataHandle) {
        String url = HostConstants.HostURL + "v3/firmware/getFirmwareDetail";
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, getRequestParams()), disposeDataHandle);
    }

    public void getX9FwNetDetailV3(DisposeDataHandle disposeDataHandle) {
        String url = HostConstants.HostURL + "v3/firmware/getFirmwareDetail";
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, getRequestParams()), disposeDataHandle);
    }
}
