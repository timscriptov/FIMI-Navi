package com.fimi.app.x8s.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;

import org.jetbrains.annotations.Contract;


public class X8sNavigationBarUtils {
    public static final float STANDARD_SCREEN_SCALE = 0.5625f;

    @SuppressLint({"ResourceAsColor"})
    public static void hideBottomUIMenu(Activity context) {
        if (Build.VERSION.SDK_INT >= 21) {
            context.getWindow().setNavigationBarColor(Color.parseColor("#00000000"));
        }
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = context.getWindow().getDecorView();
            v.setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = context.getWindow().getDecorView();
            decorView.setBackgroundColor(R.color.black);
            decorView.setSystemUiVisibility(4614);
        }
    }

    @Contract(pure = true)
    public static boolean isHWProportion(@NonNull DisplayMetrics dm) {
        if (dm.widthPixels == 0) {
            return true;
        }
        float scale = dm.heightPixels / (dm.widthPixels * 1.0f);
        return scale <= STANDARD_SCREEN_SCALE;
    }

    @NonNull
    @Contract(pure = true)
    public static float[] getRootHeightAndWidth(DisplayMetrics dm) {
        float[] hw = new float[2];
        if (isHWProportion(dm)) {
            hw[0] = dm.heightPixels;
            hw[1] = dm.heightPixels / STANDARD_SCREEN_SCALE;
        } else {
            hw[0] = dm.widthPixels * STANDARD_SCREEN_SCALE;
            hw[1] = dm.widthPixels;
        }
        return hw;
    }

    public static boolean isPad(@NonNull Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }
}
