package com.fimi.kernel.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

@TargetApi(12)

public class VolleyImageCache implements ImageLoader.ImageCache {
    private static LruCache<String, Bitmap> mCache;
    private static VolleyImageCache mImageCache;

    public VolleyImageCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 8;
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            public int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public static VolleyImageCache getInstance() {
        if (mImageCache == null) {
            mImageCache = new VolleyImageCache();
        }
        return mImageCache;
    }

    @Override
    public Bitmap getBitmap(String cacheKey) {
        return mCache.get(cacheKey);
    }

    @Override
    public void putBitmap(String cacheKey, Bitmap bitmap) {
        mCache.put(cacheKey, bitmap);
    }

    public void removeBitmap(String requestUrl, int maxWidth, int maxHeight) {
        mCache.remove(getCacheKey(requestUrl, maxWidth, maxHeight));
    }

    public String getCacheKey(String url, int maxWidth, int maxHeight) {
        return "#W" + maxWidth + "#H" + maxHeight + url;
    }

    public void clearBitmap() {
        mCache.evictAll();
    }
}
