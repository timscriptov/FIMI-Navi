package com.fimi.album.biz;


public class LocalHandleType {
    public static FragmentType fragmentType = FragmentType.VIDEO;

    public static boolean isPhoto() {
        return fragmentType == FragmentType.PHOTO;
    }


    public enum FragmentType {
        PHOTO,
        VIDEO
    }
}
