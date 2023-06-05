package com.fimi.album.biz;

/* loaded from: classes.dex */
public class X9HandleType {
    public static FragmentType fragmentType = FragmentType.CAMERA;

    public static boolean isCameraView() {
        return fragmentType == FragmentType.CAMERA;
    }

    /* loaded from: classes.dex */
    public enum FragmentType {
        CAMERA,
        LOCAL_MEDIA_LIB
    }
}
