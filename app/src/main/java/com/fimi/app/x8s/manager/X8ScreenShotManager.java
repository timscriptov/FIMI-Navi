package com.fimi.app.x8s.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.widget.X8ToastUtil;
import com.twitter.sdk.android.core.internal.scribe.SyndicatedSdkImpressionEvent;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class X8ScreenShotManager {
    public static boolean isBusy;
    boolean isFpvShotSuccess;
    boolean isMapShotSuccess;
    private X8ShotAsyncTask mFpvShotTask;
    private X8ShotAsyncTask mMapShotTask;

    @NonNull
    public static String saveScreenBitmap(Activity activity) {
        Bitmap bitmap = screenShot(activity);
        File file = new File(DirectoryPath.getX8LocalSar() + "/" + DateUtil.getStringByFormat(System.currentTimeMillis(), DateUtil.dateFormatYYMMDDHHMMSS) + ".jpeg");
        try {
            if (!file.exists()) {
                if (file.getParentFile().exists()) {
                    file.createNewFile();
                } else {
                    file.getParentFile().mkdirs();
                }
            }
            boolean ret = save(bitmap, file, Bitmap.CompressFormat.JPEG, true);
            if (ret) {
                X8ToastUtil.showToast(activity, activity.getString(R.string.x8_ai_fly_sar_save_pic_tip), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return false;
        }
        boolean ret = false;
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) {
                src.recycle();
                return ret;
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            return ret;
        }
    }

    public static Bitmap screenShot(@NonNull Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        getStatusBarHeight(activity);
        int width = (int) getDeviceDisplaySize(activity)[0];
        int height = (int) getDeviceDisplaySize(activity)[1];
        Bitmap ret = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return ret;
    }

    public static float[] getDeviceDisplaySize(@NonNull Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return new float[]{width, height};
    }

    public static int getStatusBarHeight(@NonNull Context context) {
        @SuppressLint("InternalInsetResource") int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", SyndicatedSdkImpressionEvent.CLIENT_NAME);
        if (resourceId <= 0) {
            return 0;
        }
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public void starThread(final X8sMainActivity activity) {
        isBusy = true;
        this.mMapShotTask = new X8ShotAsyncTask(activity, btp -> {
            activity.getmMapVideoController().setMapShot(btp);
            X8ScreenShotManager.this.isMapShotSuccess = true;
        }, 0);
        this.mMapShotTask.execute("");
        this.mFpvShotTask = new X8ShotAsyncTask(activity, btp -> {
            activity.getmMapVideoController().setFpvShot(btp);
            X8ScreenShotManager.this.isFpvShotSuccess = true;
            X8ScreenShotManager.this.isShotSave(activity);
        }, 1);
        this.mFpvShotTask.execute("");
    }

    public void isShotSave(X8sMainActivity activity) {
        saveScreenBitmap(activity);
        activity.getmMapVideoController().clearShotBitmap();
        this.mFpvShotTask.recycleBitmap();
        this.mMapShotTask.recycleBitmap();
        isBusy = false;
    }
}
