package com.fimi.kernel.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


public class FrescoUtils {
    private static boolean isInit = false;

    public static void displayPhoto(SimpleDraweeView mSimpleDraweeView, String path, int width, int height) {
        if (!TextUtils.isEmpty(path)) {
            PipelineDraweeControllerBuilder mPipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
            ImageRequestBuilder mImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(path));
            mImageRequestBuilder.setResizeOptions(new ResizeOptions(width, height));
            mPipelineDraweeControllerBuilder.setOldController(mSimpleDraweeView.getController());
            mPipelineDraweeControllerBuilder.setImageRequest(mImageRequestBuilder.build());
            mSimpleDraweeView.setController(mPipelineDraweeControllerBuilder.build());
        }
    }

    public static void displayPhoto(SimpleDraweeView mSimpleDraweeView, String path, int width, int height, ControllerListener mControllerListener) {
        if (!TextUtils.isEmpty(path)) {
            PipelineDraweeControllerBuilder mPipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
            ImageRequestBuilder mImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(path));
            mImageRequestBuilder.setResizeOptions(new ResizeOptions(width, height));
            mPipelineDraweeControllerBuilder.setOldController(mSimpleDraweeView.getController());
            mPipelineDraweeControllerBuilder.setImageRequest(mImageRequestBuilder.build());
            mPipelineDraweeControllerBuilder.setControllerListener(mControllerListener);
            mSimpleDraweeView.setController(mPipelineDraweeControllerBuilder.build());
        }
    }

    public static void showThumb(SimpleDraweeView draweeView, String url, int resizeWidthDp, int resizeHeightDp) {
        if (url != null && !"".equals(url) && draweeView != null) {
            initialize(draweeView.getContext());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setResizeOptions(new ResizeOptions(resizeWidthDp, resizeHeightDp)).build();
            DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request).setControllerListener(new BaseControllerListener()).build();
            draweeView.setController(controller);
        }
    }

    public static void initialize(Context context) {
        if (!isInit) {
            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context).setDownsampleEnabled(true).build();
            Fresco.initialize(context, config);
            isInit = true;
        }
    }
}
