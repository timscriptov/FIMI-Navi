package com.fimi.network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fimi.host.HostConstants;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.network.entity.NetModel;

import java.util.HashMap;
import java.util.Iterator;

import ch.qos.logback.core.joran.action.Action;

public class ApkVersionManager extends BaseManager {
    public final String OPEN_SIXPOINT_CALIBRATE = "open_sixpoint_calibrate";
    public final String OPEN_FLY_LOG = "open_fly_log";
    public final String OPEN_FORMATTED_MEMORY = "open_Formatted_memory";
    public final String OPEN_STATE = "on";
    public HashMap<String, String> appSettingHashMap = new HashMap<>();

    public void getOnlineNewApkFileInfo(String packageName, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = getRequestParams();
        requestParams.put("pkgName", packageName);
        String url = HostConstants.NEW_APK_URL + "getApkDetail2";
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }

    public void getAppSetting(final AppSettingListener appSettingListener) {
        String url = HostConstants.GET_APP_SETTING + "getAppSetting";
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, getRequestParams()), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                NetModel netModel = JSON.parseObject(responseObj.toString(), NetModel.class);
                if (netModel.getData() != null) {
                    appSettingHashMap.clear();
                    try {
                        JSONArray jsonArray = JSON.parseArray(netModel.getData().toString());
                        Iterator<Object> iterator = jsonArray.iterator();
                        while (iterator.hasNext()) {
                            JSONObject jsonObjectData = (JSONObject) iterator.next();
                            String key = jsonObjectData.getString(Action.KEY_ATTRIBUTE);
                            String value = jsonObjectData.getString("value");
                            appSettingHashMap.put(key, value);
                        }
                        appSettingListener.onAppSettingListener();
                    } catch (Exception e) {
                        HostLogBack.getInstance().writeLog("Alanqiu  ==============getAppSetting:" + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        }));
    }

    public boolean isOpen(String key) {
        String value;
        return this.appSettingHashMap != null && (value = this.appSettingHashMap.get(key)) != null && !value.equalsIgnoreCase(OPEN_STATE);
    }

    public interface AppSettingListener {
        void onAppSettingListener();
    }
}
