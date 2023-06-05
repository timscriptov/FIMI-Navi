package com.fimi.kernel.dataparser.usb;

import com.alibaba.fastjson.JSONObject;

/* loaded from: classes.dex */
public interface JsonUiCallBackListener<T> {
    void onComplete(JSONObject jSONObject, T t);
}
