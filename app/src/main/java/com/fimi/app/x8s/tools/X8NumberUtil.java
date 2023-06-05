package com.fimi.app.x8s.tools;

import android.content.Context;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.util.UnityUtil;


public class X8NumberUtil {
    private static String prexDistance;
    private static String prexSpeed;

    public static void resetPrexString(Context context) {
        if (GlobalConfig.getInstance().isShowmMtric()) {
            prexDistance = context.getResources().getString(R.string.x8_unit_distance);
            prexSpeed = context.getResources().getString(R.string.x8_unit_speed);
            return;
        }
        prexDistance = context.getResources().getString(R.string.x8_unit_distance_en);
        prexSpeed = context.getResources().getString(R.string.x8_unit_speed_en);
    }

    public static String getDistanceNumberString(float decimal, int number, boolean toUpperCase) {
        if (GlobalConfig.getInstance().isShowmMtric()) {
            if (toUpperCase) {
                return NumberUtil.decimalPointStr(decimal, number) + prexDistance.toUpperCase();
            }
            return NumberUtil.decimalPointStr(decimal, number) + prexDistance;
        } else if (toUpperCase) {
            return NumberUtil.decimalPointStr(UnityUtil.meterToFoot(decimal), 1) + prexDistance.toUpperCase();
        } else {
            return NumberUtil.decimalPointStr(UnityUtil.meterToFoot(decimal), 1) + prexDistance;
        }
    }

    public static String getSpeedNumberString(float decimal, int number, boolean toUpperCase) {
        if (GlobalConfig.getInstance().isShowmMtric()) {
            if (toUpperCase) {
                return NumberUtil.decimalPointStr(decimal, number) + prexSpeed.toUpperCase();
            }
            return NumberUtil.decimalPointStr(decimal, number) + prexSpeed;
        } else if (toUpperCase) {
            return NumberUtil.decimalPointStr(UnityUtil.msToMph(decimal), 1) + prexSpeed.toUpperCase();
        } else {
            return NumberUtil.decimalPointStr(UnityUtil.msToMph(decimal), 1) + prexSpeed;
        }
    }

    public static String getDistanceNumberNoPrexString(float decimal, int number) {
        return GlobalConfig.getInstance().isShowmMtric() ? NumberUtil.decimalPointStr(decimal, number) + "" : NumberUtil.decimalPointStr(UnityUtil.meterToFoot(decimal), 1) + "";
    }

    public static String getSpeedNumberNoPrexString(float decimal, int number) {
        return GlobalConfig.getInstance().isShowmMtric() ? NumberUtil.decimalPointStr(decimal, number) + "" : NumberUtil.decimalPointStr(UnityUtil.msToMph(decimal), 1) + "";
    }

    public static String getPrexDistance() {
        return prexDistance;
    }

    public static String getPrexSpeed() {
        return prexSpeed;
    }

    public static String getPrexDistanceUppercase() {
        return prexDistance.toUpperCase();
    }

    public static String getPrexSpeedUppercase() {
        return prexSpeed.toUpperCase();
    }
}
