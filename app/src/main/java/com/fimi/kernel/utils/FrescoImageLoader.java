package com.fimi.kernel.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Sets;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/* loaded from: classes.dex */
public class FrescoImageLoader {
    private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";
    private static final int MAX_DISK_CACHE_SIZE = 41943040;
    private static final int MAX_HEAP_SIZE = ((int) Runtime.getRuntime().maxMemory()) / 8;
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE;
    private static final int MAX_POOL_SIZE = 41943040;
    private static final String TAG = "FrescoImageLoader";
    private static ImagePipelineConfig sImagePipelineConfig;

    public static void display(SimpleDraweeView view, String uri) {
        display(view, uri, null);
    }

    public static void display(SimpleDraweeView view, String lowUri, String uri, ControllerListener listener) {
        ImageRequestBuilder imageRequest;
        if (uri != null) {
            int w = view.getLayoutParams().width;
            int h = view.getLayoutParams().height;
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            if (!lowUri.startsWith("file:///")) {
                lowUri = "file://" + lowUri;
            }
            if (lowUri != null && lowUri.length() > 0) {
                controller.setLowResImageRequest(ImageRequest.fromUri(lowUri));
            }
            controller.setOldController(view.getController());
            controller.setAutoPlayAnimations(true);
            boolean isVideo = uri.endsWith(".MP4");
            if (isVideo) {
                imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(lowUri));
            } else {
                imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri));
            }
            if (w > 0 && h > 0) {
                imageRequest.setResizeOptions(new ResizeOptions(w, h));
            }
            controller.setImageRequest(imageRequest.build());
            controller.setControllerListener(listener);
            view.setController(controller.build());
        }
    }

    public static void display(SimpleDraweeView view, String uri, ControllerListener listener) {
        if (uri == null) {
            uri = "";
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).setLocalThumbnailPreviewsEnabled(true).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(view.getController()).setControllerListener(listener).build();
        view.setController(controller);
    }

    public static void display(SimpleDraweeView view, String uri, int width, int height) {
        display(view, uri, width, height, null);
    }

    public static void display(SimpleDraweeView view, String uri, int width, int height, ControllerListener listener) {
        if (uri != null) {
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setOldController(view.getController());
            controller.setAutoPlayAnimations(true);
            ImageRequestBuilder imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).setCacheChoice(ImageRequest.CacheChoice.SMALL).setLocalThumbnailPreviewsEnabled(true);
            imageRequest.setProgressiveRenderingEnabled(true);
            Log.d("Good", uri);
            if (width > 0 && height > 0) {
                imageRequest.setResizeOptions(new ResizeOptions(width, height));
            }
            controller.setImageRequest(imageRequest.build());
            controller.setControllerListener(listener);
            view.setController(controller.build());
        }
    }

    public static void initFresco(Context context) {
        Fresco.initialize(context, getImagePipelineConfig(context));
    }

    public static void shutdown() {
        Fresco.shutDown();
    }

    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context);
            configureCaches(configBuilder, context);
            configureLoggingListeners(configBuilder);
            sImagePipelineConfig = configBuilder.build();
        }
        return sImagePipelineConfig;
    }

    private static void configureCaches(ImagePipelineConfig.Builder configBuilder, Context context) {
        new MemoryCacheParams(MAX_MEMORY_CACHE_SIZE, Integer.MAX_VALUE, MAX_MEMORY_CACHE_SIZE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        MyBitmapMemoryCacheParamsSupplier myBitmapMemoryCacheParamsSupplier = new MyBitmapMemoryCacheParamsSupplier((ActivityManager) context.getSystemService("activity"));
        configBuilder.setDownsampleEnabled(true).setBitmapsConfig(Bitmap.Config.RGB_565).setBitmapMemoryCacheParamsSupplier(myBitmapMemoryCacheParamsSupplier).setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(context.getApplicationContext().getCacheDir()).setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR).setMaxCacheSize(41943040L).build());
    }

    private static void configureLoggingListeners(ImagePipelineConfig.Builder configBuilder) {
        configBuilder.setRequestListeners(Sets.newHashSet(new RequestLoggingListener()));
    }

    public static void clearCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();
    }
}
