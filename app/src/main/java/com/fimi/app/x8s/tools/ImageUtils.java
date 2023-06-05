package com.fimi.app.x8s.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class ImageUtils {
    private static Map<String, SoftReference<Bitmap>> imageCache = new HashMap();

    public static SoftReference<Bitmap> addBitmapToCache(Context context, int res) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        SoftReference<Bitmap> softBitmap = new SoftReference<>(bitmap);
        imageCache.put("" + res, softBitmap);
        return softBitmap;
    }

    public static Bitmap getBitmapByPath(Context context, int res) {
        SoftReference<Bitmap> softBitmap = imageCache.get("" + res);
        if (softBitmap == null) {
            return addBitmapToCache(context, res).get();
        }
        Bitmap bitmap = softBitmap.get();
        return bitmap == null ? addBitmapToCache(context, res).get() : bitmap;
    }
}
