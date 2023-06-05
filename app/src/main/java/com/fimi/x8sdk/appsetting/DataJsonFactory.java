package com.fimi.x8sdk.appsetting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fimi.host.HostConstants;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.x8sdk.entity.X8AppSettingLog;
import com.fimi.x8sdk.modulestate.TimeStampState;
import com.github.moduth.blockcanary.internal.BlockInfo;
import com.umeng.commonsdk.proguard.g;

/* loaded from: classes2.dex */
public class DataJsonFactory {
    public static String getAllDataJsonString() {
        JSONObject allObjcet = new JSONObject();
        allObjcet.put("flightController", (Object) DeviceVersionFactory.getFc());
        allObjcet.put("camera", (Object) DeviceVersionFactory.getCamera());
        allObjcet.put("gimbal", (Object) DeviceVersionFactory.getGimbal());
        allObjcet.put("remoteController", (Object) DeviceVersionFactory.getRc());
        allObjcet.put(g.W, (Object) DeviceVersionFactory.getBattery());
        allObjcet.put("rcRelay", (Object) DeviceVersionFactory.etRcRelay());
        allObjcet.put("computerVision", (Object) DeviceVersionFactory.getComputerVision());
        allObjcet.put("fcRelay", (Object) DeviceVersionFactory.getFcRelay());
        allObjcet.put("esc", (Object) DeviceVersionFactory.getEsc());
        allObjcet.put("nfz", (Object) DeviceVersionFactory.getNfz());
        allObjcet.put("Ultrasonic", (Object) DeviceVersionFactory.getUltrasonic());
        allObjcet.put("speedLimit", (Object) new ValueFloat(X8AppSettingLog.speedLimit));
        allObjcet.put("distanceLimit", (Object) new ValueFloat(X8AppSettingLog.distanceLimit));
        allObjcet.put("heightLimit", (Object) new ValueFloat(X8AppSettingLog.heightLimit));
        allObjcet.put("returnHeight", (Object) new ValueFloat(X8AppSettingLog.returnHeight));
        allObjcet.put("pilotMode", (Object) new ValueBoolean(X8AppSettingLog.pilotMode));
        allObjcet.put("sportMode", (Object) new ValueBoolean(X8AppSettingLog.sportMode));
        allObjcet.put("lostAction", (Object) new ValueFloat(X8AppSettingLog.lostAction));
        allObjcet.put("accLand", (Object) new ValueBoolean(X8AppSettingLog.accurateLanding));
        allObjcet.put("followRP", (Object) new ValueBoolean(X8AppSettingLog.followRP));
        allObjcet.put("followAB", (Object) new ValueBoolean(X8AppSettingLog.followAB));
        allObjcet.put("lowPower", (Object) new ValueFloat(X8AppSettingLog.lowPower));
        allObjcet.put("feelSensitivity", (Object) new SensityJson(new ValueSensity(X8AppSettingLog.FS_pitch, X8AppSettingLog.FS_roll, X8AppSettingLog.FS_thro, X8AppSettingLog.FS_yaw)));
        allObjcet.put("feelBrake", (Object) new SensityJson(new ValueSensity(X8AppSettingLog.FB_pitch, X8AppSettingLog.FB_roll, X8AppSettingLog.FB_thro, X8AppSettingLog.FB_yaw)));
        allObjcet.put("feelYawTrip", (Object) new SensityJson(new ValueSensity(X8AppSettingLog.FY_pitch, X8AppSettingLog.FY_roll, X8AppSettingLog.FY_thro, X8AppSettingLog.FY_yaw)));
        allObjcet.put("feelExp", (Object) new SensityJson(new ValueSensity(X8AppSettingLog.FE_pitch, X8AppSettingLog.FE_roll, X8AppSettingLog.FE_thro, X8AppSettingLog.FE_yaw)));
        allObjcet.put("cc", (Object) new ValueFloat(X8AppSettingLog.CC));
        allObjcet.put("uvc", (Object) new ValueFloat(X8AppSettingLog.UVC));
        allObjcet.put("totalCapacity", (Object) new ValueFloat(X8AppSettingLog.TOTALCAPACITY));
        allObjcet.put("rcNotUpdateCnt", (Object) new ValueFloat(X8AppSettingLog.RCNOTUPDATECNT));
        allObjcet.put("sysErrorCode", (Object) new ValueFloat(X8AppSettingLog.SYSERRORCODE));
        allObjcet.put("sysState", (Object) new ValueFloat(X8AppSettingLog.SYSSTATE));
        allObjcet.put("gpsTimestamp", (Object) Long.valueOf(TimeStampState.getInstance().getTimeStamp()));
        JSONObject dataObject = new JSONObject();
        dataObject.put("data", (Object) allObjcet);
        dataObject.put("startupTime", (Object) ("" + X8AppSettingLog.STARTUPTIME));
        dataObject.put(BlockInfo.KEY_TIME_COST, (Object) DateUtil.getStringByFormat(System.currentTimeMillis(), HostConstants.FORMATDATE));
        String json = JSON.toJSONString(dataObject, SerializerFeature.PrettyFormat);
        return json;
    }

    public static String onValueChange(String tag, float oldV, float newV) {
        JSONObject dataObject = new JSONObject();
        JSONObject object = new JSONObject();
        object.put(tag, (Object) new ValueFloatChange(oldV, newV));
        dataObject.put("data", (Object) object);
        dataObject.put("startupTime", (Object) ("" + X8AppSettingLog.STARTUPTIME));
        dataObject.put(BlockInfo.KEY_TIME_COST, (Object) DateUtil.getStringByFormat(System.currentTimeMillis(), HostConstants.FORMATDATE));
        String json = JSON.toJSONString(dataObject, SerializerFeature.PrettyFormat);
        return json;
    }

    public static String onValueBooleanChange(String tag, boolean oldV, boolean newV) {
        JSONObject dataObject = new JSONObject();
        JSONObject object = new JSONObject();
        object.put(tag, (Object) new ValueBooleanChange(oldV, newV));
        dataObject.put("data", (Object) object);
        dataObject.put("startupTime", (Object) ("" + X8AppSettingLog.STARTUPTIME));
        dataObject.put(BlockInfo.KEY_TIME_COST, (Object) DateUtil.getStringByFormat(System.currentTimeMillis(), HostConstants.FORMATDATE));
        String json = JSON.toJSONString(dataObject, SerializerFeature.PrettyFormat);
        return json;
    }

    public static String appValueSensityChange(String tag, ValueSensity oldV, ValueSensity newV) {
        JSONObject dataObject = new JSONObject();
        JSONObject object = new JSONObject();
        SensityJsonChange s = new SensityJsonChange(oldV, newV);
        object.put(tag, (Object) s);
        dataObject.put("data", (Object) object);
        dataObject.put("startupTime", (Object) ("" + X8AppSettingLog.STARTUPTIME));
        dataObject.put(BlockInfo.KEY_TIME_COST, (Object) DateUtil.getStringByFormat(System.currentTimeMillis(), HostConstants.FORMATDATE));
        String json = JSON.toJSONString(dataObject, SerializerFeature.PrettyFormat);
        return json;
    }
}
