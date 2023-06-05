package com.fimi.libperson.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.fimi.libperson.entity.ImageSource;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class BitmapLoadTaskInstance {
    private static final String TAG = "BitmapLoadTaskInstance";
    private static BitmapLoadTaskInstance sBitmapLoadTaskInstance;
    private Executor executor = AsyncTask.THREAD_POOL_EXECUTOR;
    private Bitmap mBitmap;
    private OnLoadListener mOnLoadListener;

    public static BitmapLoadTaskInstance getInstance() {
        if (sBitmapLoadTaskInstance == null) {
            sBitmapLoadTaskInstance = new BitmapLoadTaskInstance();
        }
        return sBitmapLoadTaskInstance;
    }

    public void setImage(ImageSource source, Context context) {
        BitmapLoadTask bitmapLoadTask = new BitmapLoadTask(context, source.getUri(), false);
        execute(bitmapLoadTask);
    }

    private void execute(AsyncTask<Void, Void, ?> asyncTask) {
        asyncTask.executeOnExecutor(this.executor, new Void[0]);
    }

    public synchronized void onImageLoaded(Bitmap bitmap) {
        this.mBitmap = bitmap;
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onComplete();
        }
    }

    public Bitmap decode(Context context, Uri uri) throws Exception {
        Bitmap bitmap;
        Resources res;
        String uriString = uri.toString();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        if (uriString.startsWith("android.resource://")) {
            String packageName = uri.getAuthority();
            if (context.getPackageName().equals(packageName)) {
                res = context.getResources();
            } else {
                PackageManager pm = context.getPackageManager();
                res = pm.getResourcesForApplication(packageName);
            }
            int id = 0;
            List<String> segments = uri.getPathSegments();
            int size = segments.size();
            if (size == 2 && segments.get(0).equals("drawable")) {
                String resName = segments.get(1);
                id = res.getIdentifier(resName, "drawable", packageName);
            } else if (size == 1 && TextUtils.isDigitsOnly(segments.get(0))) {
                try {
                    id = Integer.parseInt(segments.get(0));
                } catch (NumberFormatException e) {
                }
            }
            bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
        } else if (uriString.startsWith("file:///android_asset/")) {
            String assetName = uriString.substring("file:///android_asset/".length());
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(assetName), null, options);
        } else if (uriString.startsWith("file://")) {
            bitmap = BitmapFactory.decodeFile(uriString.substring("file://".length()), options);
        } else {
            InputStream inputStream = null;
            try {
                ContentResolver contentResolver = context.getContentResolver();
                inputStream = contentResolver.openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                    }
                }
            }
        }
        if (bitmap == null) {
            throw new RuntimeException("Skia image region decoder returned null bitmap - image format may not be supported");
        }
        return bitmap;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setRecyle() {
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener = null;
        }
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    /* loaded from: classes.dex */
    public interface OnLoadListener {
        void onComplete();
    }

    /* loaded from: classes.dex */
    public static class BitmapLoadTask extends AsyncTask<Void, Void, Integer> {
        private final WeakReference<Context> contextRef;
        private final boolean preview;
        private final Uri source;
        private Bitmap bitmap;
        private Exception exception;

        public BitmapLoadTask(Context context, Uri source, boolean preview) {
            this.contextRef = new WeakReference<>(context);
            this.source = source;
            this.preview = preview;
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voids) {
            try {
                Context context = this.contextRef.get();
                this.bitmap = BitmapLoadTaskInstance.sBitmapLoadTaskInstance.decode(context, this.source);
                return 1;
            } catch (Exception var6) {
                Log.e(BitmapLoadTaskInstance.TAG, "Failed to load bitmap", var6);
                this.exception = var6;
                return null;
            } catch (OutOfMemoryError var7) {
                Log.e(BitmapLoadTaskInstance.TAG, "Failed to load bitmap - OutOfMemoryError", var7);
                this.exception = new RuntimeException(var7);
                return null;
            }
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer orientation) {
            if (this.bitmap != null) {
                BitmapLoadTaskInstance.sBitmapLoadTaskInstance.onImageLoaded(this.bitmap);
            }
        }
    }
}
