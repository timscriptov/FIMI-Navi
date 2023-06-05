package com.fimi.x8sdk.modulestate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class X8CameraSettings {
    public static boolean hasGetFocusSetting;
    public static boolean hasGetFoucusSettingValues;
    private static String focusSetting = "2.0";
    private static List<String> focusSettingList = new ArrayList();

    public static void setFocusSetting(String s) {
        focusSetting = s;
    }

    public static void getSettings(CameraManager cameraManager) {
        if (!hasGetFocusSetting) {
            cameraManager.getCameraFocuse(new JsonUiCallBackListener() { // from class: com.fimi.x8sdk.modulestate.X8CameraSettings.1
                @Override // com.fimi.kernel.dataparser.usb.JsonUiCallBackListener
                public void onComplete(JSONObject rt, Object o) {
                    CameraParamsJson paramsJson;
                    String param;
                    if (rt != null && (paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class)) != null && (param = paramsJson.getParam()) != null && !param.equals("")) {
                        String unused = X8CameraSettings.focusSetting = paramsJson.getParam();
                        X8CameraSettings.hasGetFocusSetting = true;
                    }
                }
            });
        }
        if (!hasGetFoucusSettingValues) {
            cameraManager.getCameraFocuseValues(new JsonUiCallBackListener() { // from class: com.fimi.x8sdk.modulestate.X8CameraSettings.2
                @Override // com.fimi.kernel.dataparser.usb.JsonUiCallBackListener
                public void onComplete(JSONObject rt, Object o) {
                    CameraParamsJson paramsJson;
                    List<String> param;
                    if (rt != null && (paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class)) != null && (param = paramsJson.getOptions()) != null && param.size() > 0) {
                        List unused = X8CameraSettings.focusSettingList = paramsJson.getOptions();
                        X8CameraSettings.hasGetFoucusSettingValues = true;
                    }
                }
            });
        }
    }

    public static List<String> getFocusSettingList() {
        if (focusSettingList.size() <= 0) {
            for (int i = 10; i <= 30; i++) {
                focusSettingList.add("" + (i / 10.0f));
            }
        }
        return focusSettingList;
    }

    public static void reset() {
        hasGetFocusSetting = false;
        hasGetFoucusSettingValues = false;
        focusSettingList.clear();
    }

    public static int getFocuse() {
        int x = (int) (NumberUtil.convertToFloat(focusSetting, 0) * 10.0f);
        return x;
    }

    public static int[] getMinMaxFocuse() {
        getFocusSettingList();
        if (focusSettingList.size() > 2) {
            int min = (int) (NumberUtil.convertToFloat(focusSettingList.get(0), 0) * 10.0f);
            int max = (int) (NumberUtil.convertToFloat(focusSettingList.get(focusSettingList.size() - 1), 0) * 10.0f);
            return new int[]{min, max};
        }
        return new int[]{10, 30};
    }
}
