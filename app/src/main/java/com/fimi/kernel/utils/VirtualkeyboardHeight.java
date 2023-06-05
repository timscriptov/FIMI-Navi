package com.fimi.kernel.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class VirtualkeyboardHeight {
    public static int getScreenDPI(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            int dpi = displayMetrics.widthPixels;
            return dpi;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getScreenHeightDPI(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            int dpi = displayMetrics.heightPixels;
            return dpi;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getBottomStatusHeight(Context context) {
        int totlaHeight = getScreenDPI(context);
        int contentHeight = getScreenHeight(context);
        return totlaHeight - contentHeight;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService("window");
        DisplayMetrics out = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(out);
        return out.widthPixels;
    }

    public static boolean isStandardSize(Context context) {
        float width = getScreenDPI(context);
        float heigth = getScreenHeightDPI(context);
        float ratio = width / heigth;
        return ratio > 1.7777778f + 0.1f || ratio <= 1.7777778f - 0.1f;
    }
}
