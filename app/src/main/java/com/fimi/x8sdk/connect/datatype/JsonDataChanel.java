package com.fimi.x8sdk.connect.datatype;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IRetransmissionJsonHandle;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.dataparser.JsonMessage;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;

/* loaded from: classes2.dex */
public class JsonDataChanel implements IDataChanel {
    public static String testString = "";
    IRetransmissionJsonHandle retransmissionHandle;

    @Override // com.fimi.x8sdk.connect.datatype.IDataChanel
    public void forwardData(byte[] data) {
        CameraParamsJson paramsJson;
        String param;
        BaseCommand jsonCmdData;
        try {
            String json = new String(data);
            JSONObject rtJson = JSONObject.parseObject(json);
            int msg_id = rtJson.getInteger("msg_id").intValue();
            String camKey = rtJson.getString("type");
            if (this.retransmissionHandle != null && (jsonCmdData = this.retransmissionHandle.removeFromListByCmdID(msg_id, camKey)) != null) {
                JsonMessage jsonMsg = new JsonMessage(msg_id, rtJson, jsonCmdData.getJsonUiCallBackListener());
                notifyCamJsonData(jsonMsg);
            }
            if (msg_id == 7 && camKey != null && camKey.equals("temp")) {
                JsonMessage jsonMsg2 = new JsonMessage(msg_id, rtJson, null);
                JSONObject rt = jsonMsg2.getJsonRt();
                if (rt != null && (paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class)) != null && (param = paramsJson.getParam()) != null && !param.equals("")) {
                    String s = paramsJson.getParam();
                    testString = s;
                }
            }
            if (!TextUtils.isEmpty(json)) {
                notifyCamJsonData(msg_id, rtJson);
            }
        } catch (Exception e) {
        }
    }

    private void notifyCamJsonData(int msgId, JSONObject respJson) {
        NoticeManager.getInstance().onJsonDataCallBack(msgId, respJson);
    }

    private void notifyCamJsonData(JsonMessage message) {
        NoticeManager.getInstance().onJsonDataUICallBack(message);
    }

    public void setRetransmissionHandle(IRetransmissionJsonHandle retransmissionHandle) {
        this.retransmissionHandle = retransmissionHandle;
    }
}
