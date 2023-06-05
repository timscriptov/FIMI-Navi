package com.fimi.app.x8s.manager;

import com.fimi.app.x8s.controls.X8MainErrorCodePowerPitchAngleController;

/* loaded from: classes.dex */
public class X8DroneInfoStatemManager {
    private static final String BAT = "BAT";
    private static final String COMPASS = "Compass";
    private static final String GC = "GC";
    private static final String GPS = "GPS";
    private static final String IMU = "IMU";
    private static X8MainErrorCodePowerPitchAngleController errorCodeHandler;

    public static void setErrorCodeHolder(X8MainErrorCodePowerPitchAngleController holder) {
        errorCodeHandler = holder;
    }

    public static boolean isGpsError() {
        if (errorCodeHandler.getmX8ErrorCodeController() == null) {
            return false;
        }
        boolean ret = errorCodeHandler.getmX8ErrorCodeController().isDroneStateErrorByLable(GPS);
        return ret;
    }

    public static boolean isCompassError() {
        if (errorCodeHandler.getmX8ErrorCodeController() == null) {
            return false;
        }
        boolean ret = errorCodeHandler.getmX8ErrorCodeController().isDroneStateErrorByLable(COMPASS);
        return ret;
    }

    public static boolean isImuError() {
        if (errorCodeHandler.getmX8ErrorCodeController() == null) {
            return false;
        }
        boolean ret = errorCodeHandler.getmX8ErrorCodeController().isDroneStateErrorByLable(IMU);
        return ret;
    }

    public static boolean isBatteryError() {
        if (errorCodeHandler.getmX8ErrorCodeController() == null) {
            return false;
        }
        boolean ret = errorCodeHandler.getmX8ErrorCodeController().isDroneStateErrorByLable(BAT);
        return ret;
    }

    public static boolean isGcError() {
        if (errorCodeHandler.getmX8ErrorCodeController() == null) {
            return false;
        }
        boolean ret = errorCodeHandler.getmX8ErrorCodeController().isDroneStateErrorByLable(GC);
        return ret;
    }
}
