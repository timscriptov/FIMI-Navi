package com.fimi.album.biz;

import android.graphics.drawable.Animatable;

import com.facebook.drawee.controller.ControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;


public class FrescoControllerListener implements ControllerListener<ImageInfo> {
    @Override
    public void onSubmit(String id, Object callerContext) {
    }

    @Override
    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
    }

    @Override
    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
    }

    @Override
    public void onIntermediateImageFailed(String id, Throwable throwable) {
    }

    @Override
    public void onFailure(String id, Throwable throwable) {
    }

    @Override
    public void onRelease(String id) {
    }
}
