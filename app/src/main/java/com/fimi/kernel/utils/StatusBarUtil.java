package com.fimi.kernel.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.drawerlayout.widget.DrawerLayout;

import com.fimi.android.app.R;
import com.fimi.kernel.FimiAppContext;
import com.fimi.kernel.widget.statusbar.StatusBarView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.twitter.sdk.android.core.internal.scribe.SyndicatedSdkImpressionEvent;

import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class StatusBarUtil {
    public static final int DEFAULT_STATUS_BAR_ALPHA = 0;
    public static final int FLAG_NOTCH_SUPPORT = 65536;
    public static final int NOTCH_IN_SCREEN_VOIO = 32;
    public static final int ROUNDED_IN_SCREEN_VOIO = 8;

    public static void setColor(Activity activity, @ColorInt int color) {
        setColor(activity, color, 0);
    }

    public static void setColor(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count > 0 && (decorView.getChildAt(count - 1) instanceof StatusBarView)) {
                decorView.getChildAt(count - 1).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            } else {
                StatusBarView statusView = createStatusBarView(activity, color, statusBarAlpha);
                decorView.addView(statusView);
            }
            setRootView(activity);
        }
    }

    public static void setColorForSwipeBack(Activity activity, int color) {
        setColorForSwipeBack(activity, color, 0);
    }

    public static void setColorForSwipeBack(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= 19) {
            ViewGroup contentView = (ViewGroup) activity.findViewById(16908290);
            contentView.setPadding(0, getStatusBarHeight(activity), 0, 0);
            contentView.setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            setTransparentForWindow(activity);
        }
    }

    public static void setColorNoTranslucent(Activity activity, @ColorInt int color) {
        setColor(activity, color, 0);
    }

    @Deprecated
    public static void setColorDiff(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= 19) {
            transparentStatusBar(activity);
            ViewGroup contentView = (ViewGroup) activity.findViewById(16908290);
            if (contentView.getChildCount() > 1) {
                contentView.getChildAt(1).setBackgroundColor(color);
            } else {
                contentView.addView(createStatusBarView(activity, color));
            }
            setRootView(activity);
        }
    }

    public static void setTranslucent(Activity activity) {
        setTranslucent(activity, 0);
    }

    public static void setTranslucent(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= 19) {
            setTransparent(activity);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTranslucentForCoordinatorLayout(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= 19) {
            transparentStatusBar(activity);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            transparentStatusBar(activity);
            setRootView(activity);
        }
    }

    @Deprecated
    public static void setTranslucentDiff(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            setRootView(activity);
        }
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    public static void setColorNoTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= 21) {
                activity.getWindow().addFlags(Integer.MIN_VALUE);
                activity.getWindow().clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
                activity.getWindow().setStatusBarColor(0);
            } else {
                activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            }
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (contentLayout.getChildCount() > 0 && (contentLayout.getChildAt(0) instanceof StatusBarView)) {
                contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            } else {
                StatusBarView statusBarView = createStatusBarView(activity, color);
                contentLayout.addView(statusBarView, 0);
            }
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity) + contentLayout.getPaddingTop(), contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
            }
            setDrawerLayoutProperty(drawerLayout, contentLayout);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout, ViewGroup drawerLayoutContentLayout) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    @Deprecated
    public static void setColorForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (contentLayout.getChildCount() > 0 && (contentLayout.getChildAt(0) instanceof StatusBarView)) {
                contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, 0));
            } else {
                StatusBarView statusBarView = createStatusBarView(activity, color);
                contentLayout.addView(statusBarView, 0);
            }
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            setDrawerLayoutProperty(drawerLayout, contentLayout);
        }
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        setTranslucentForDrawerLayout(activity, drawerLayout, 0);
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= 19) {
            setTransparentForDrawerLayout(activity, drawerLayout);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTransparentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= 21) {
                activity.getWindow().addFlags(Integer.MIN_VALUE);
                activity.getWindow().clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
                activity.getWindow().setStatusBarColor(0);
            } else {
                activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            }
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            setDrawerLayoutProperty(drawerLayout, contentLayout);
        }
    }

    @Deprecated
    public static void setTranslucentForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            contentLayout.setFitsSystemWindows(true);
            contentLayout.setClipToPadding(true);
            ViewGroup vg = (ViewGroup) drawerLayout.getChildAt(1);
            vg.setFitsSystemWindows(false);
            drawerLayout.setFitsSystemWindows(false);
        }
    }

    public static void setTransparentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, int statusBarAlpha, View needOffsetView) {
        if (Build.VERSION.SDK_INT >= 19) {
            setTransparentForWindow(activity);
            addTranslucentView(activity, statusBarAlpha);
            if (needOffsetView != null) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
                layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0);
            }
        }
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    public static void setTransparentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, int statusBarAlpha, View needOffsetView) {
        setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            clearPreviousSetting(activity);
        }
    }

    @TargetApi(19)
    private static void clearPreviousSetting(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        if (count > 0 && (decorView.getChildAt(count - 1) instanceof StatusBarView)) {
            decorView.removeViewAt(count - 1);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(16908290)).getChildAt(0);
            rootView.setPadding(0, 0, 0, 0);
        }
    }

    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(16908290);
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    private static StatusBarView createStatusBarView(Activity activity, @ColorInt int color) {
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private static StatusBarView createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    private static void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(16908290);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    private static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(0);
            activity.getWindow().getDecorView().setSystemUiVisibility(FimiAppContext.UI_HEIGHT);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(NTLMConstants.FLAG_UNIDENTIFIED_9, NTLMConstants.FLAG_UNIDENTIFIED_9);
        }
    }

    @TargetApi(19)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_10);
            activity.getWindow().setStatusBarColor(0);
            return;
        }
        activity.getWindow().addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
    }

    private static StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", SyndicatedSdkImpressionEvent.CLIENT_NAME);
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        float a = 1.0f - (alpha / 255.0f);
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;
        return (-16777216) | (((int) ((red * a) + 0.5d)) << 16) | (((int) ((green * a) + 0.5d)) << 8) | ((int) ((blue * a) + 0.5d));
    }

    @TargetApi(19)
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            window.getDecorView().setSystemUiVisibility(FimiAppContext.UI_HEIGHT);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(NTLMConstants.FLAG_UNIDENTIFIED_9, NTLMConstants.FLAG_UNIDENTIFIED_9);
        }
    }

    public static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            window.getDecorView().setSystemUiVisibility(FimiAppContext.UI_HEIGHT);
        } else if (Build.VERSION.SDK_INT >= 19) {
            transparencyBar(activity);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);
        }
    }

    public static int StatusBarLightMode(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            boolean isMiui9 = false;
            String systemProperty = getSystemProperty("ro.miui.ui.version.name");
            if (systemProperty != null && !"".equals(systemProperty)) {
                try {
                    String codeStr = systemProperty.substring(1);
                    try {
                        int i = Integer.parseInt(codeStr);
                        if (i >= 9) {
                            isMiui9 = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (isMiui9 && Build.VERSION.SDK_INT >= 23) {
                transparencyBar(activity);
                activity.getWindow().getDecorView().setSystemUiVisibility(9216);
            } else if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                transparencyBar(activity);
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                transparencyBar(activity);
            } else if (Build.VERSION.SDK_INT >= 23) {
                transparencyBar(activity);
                activity.getWindow().getDecorView().setSystemUiVisibility(9216);
            } else {
                setStatusBarColor(activity, R.color.kernal_status_status);
            }
        }
        return 0;
    }

    public static void StatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(9216);
        }
    }

    public static void StatusBarDarkMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(0);
        }
    }

    public static String getSystemProperty(String propName) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            BufferedReader input2 = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            try {
                line = input2.readLine();
                input2.close();
                if (input2 != null) {
                    try {
                        input2.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e2) {
                input = input2;
                line = null;
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e3) {
                    }
                }
                return line;
            } catch (Throwable th) {
                th = th;
                input = input2;
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
        }
        return line;
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        int value;
        if (window == null) {
            return false;
        }
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value2 = meizuFlags.getInt(lp);
            if (dark) {
                value = value2 | bit;
            } else {
                value = value2 & (bit ^ (-1));
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        if (window == null) {
            return false;
        }
        Class clazz = window.getClass();
        try {
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
            if (dark) {
                extraFlagField.invoke(window, Integer.valueOf(darkModeFlag), Integer.valueOf(darkModeFlag));
            } else {
                extraFlagField.invoke(window, 0, Integer.valueOf(darkModeFlag));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNotch(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        String name = Build.BRAND;
        if ("Xiaomi".equals(name)) {
            boolean ret = hasNotchInScreenAtMi(context);
            return ret;
        }
        boolean ret2 = hasNotchInScreen(context);
        return ret2;
    }

    public static Integer getInt(Context context, String key, int def) throws IllegalArgumentException {
        Integer.valueOf(def);
        try {
            ClassLoader cl = context.getClassLoader();
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = {String.class, Integer.TYPE};
            Method getInt = SystemProperties.getMethod("getInt", paramTypes);
            Object[] params = {new String(key), new Integer(def)};
            Integer ret = (Integer) getInt.invoke(SystemProperties, params);
            return ret;
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            Integer ret2 = Integer.valueOf(def);
            return ret2;
        }
    }

    public static boolean hasNotchInScreenAtMi(Context context) {
        int ret = getInt(context, "ro.miui.notch", 0).intValue();
        return ret == 1;
    }

    public static boolean hasNotchInScreen(Context context) {
        boolean z;
        boolean ret = false;
        try {
            try {
                try {
                    try {
                        ClassLoader cl = context.getClassLoader();
                        Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
                        Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen", new Class[0]);
                        ret = ((Boolean) get.invoke(HwNotchSizeUtil, new Object[0])).booleanValue();
                        z = ret;
                    } catch (ClassNotFoundException e) {
                        Log.e("test", "hasNotchInScreen ClassNotFoundException");
                        z = false;
                    }
                } catch (NoSuchMethodException e2) {
                    Log.e("test", "hasNotchInScreen NoSuchMethodException");
                    z = false;
                }
            } catch (Exception e3) {
                Log.e("test", "hasNotchInScreen Exception");
                z = false;
            }
            return z;
        } catch (Throwable th) {
            return ret;
        }
    }

    public static int[] getNotchSize(Context context) {
        int[] ret;
        int[] ret2 = {0, 0};
        try {
            try {
                ClassLoader cl = context.getClassLoader();
                Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
                Method get = HwNotchSizeUtil.getMethod("getNotchSize", new Class[0]);
                ret2 = (int[]) get.invoke(HwNotchSizeUtil, new Object[0]);
                ret = ret2;
            } catch (ClassNotFoundException e) {
                Log.e("test", "getNotchSize ClassNotFoundException");
                ret = ret2;
            } catch (Exception e2) {
                Log.e("test", "getNotchSize Exception");
                ret = ret2;
            }
            return ret;
        } catch (Throwable th) {
            return ret2;
        }
    }

    @TargetApi(19)
    public static void setFullScreenWindowLayoutInDisplayCutout(Window window) {
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            try {
                Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
                Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
                Object layoutParamsExObj = con.newInstance(layoutParams);
                Method method = layoutParamsExCls.getMethod("addHwFlags", Integer.TYPE);
                method.invoke(layoutParamsExObj, 65536);
            } catch (ClassNotFoundException e) {
                Log.e("test", "hw notch screen flag api error");
            } catch (IllegalAccessException e2) {
                Log.e("test", "hw notch screen flag api error");
            } catch (InstantiationException e3) {
                Log.e("test", "hw notch screen flag api error");
            } catch (NoSuchMethodException e4) {
                Log.e("test", "hw notch screen flag api error");
            } catch (InvocationTargetException e5) {
                Log.e("test", "hw notch screen flag api error");
            } catch (Exception e6) {
                Log.e("test", "other Exception");
            }
        }
    }

    public static boolean hasNotchInScreenAtOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static boolean hasNotchInScreenAtVoio(Context context) {
        boolean z;
        boolean ret = false;
        try {
            try {
                ClassLoader cl = context.getClassLoader();
                Class FtFeature = cl.loadClass("com.util.FtFeature");
                Method get = FtFeature.getMethod("isFeatureSupport", Integer.TYPE);
                ret = ((Boolean) get.invoke(FtFeature, 32)).booleanValue();
                z = ret;
            } catch (Exception e) {
                Log.e("test", "hasNotchInScreen Exception");
                z = false;
            }
            return z;
        } catch (Throwable th) {
            return ret;
        }
    }
}
