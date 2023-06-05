package com.fimi.album.biz;

/* loaded from: classes.dex */
public class LocalHandleType {
    public static FragmentType fragmentType = FragmentType.VIDEO;

    public static boolean isPhoto() {
        return fragmentType == FragmentType.PHOTO;
    }

    /* loaded from: classes.dex */
    public enum FragmentType {
        PHOTO,
        VIDEO
    }
}
