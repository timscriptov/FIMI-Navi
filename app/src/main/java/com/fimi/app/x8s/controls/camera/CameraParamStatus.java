package com.fimi.app.x8s.controls.camera;

/* loaded from: classes.dex */
public class CameraParamStatus {
    public static CameraModelStatus modelStatus = CameraModelStatus.ideal;

    /* loaded from: classes.dex */
    public enum CameraModelStatus {
        ideal,
        takePhoto,
        record,
        recording
    }
}
