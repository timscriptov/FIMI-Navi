package com.fimi.kernel.widget.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fimi.kernel.FimiAppContext;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

@TargetApi(21)

public class StatusBarCompatLollipop {
    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        window.clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(statusColor);
        window.getDecorView().setSystemUiVisibility(0);
        ViewGroup mContentView = window.findViewById(16908290);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    return insets;
                }
            });
            ViewCompat.setFitsSystemWindows(mChildView, true);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        window.addFlags(Integer.MIN_VALUE);
        if (hideStatusBarBackground) {
            window.clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            window.setStatusBarColor(0);
            window.getDecorView().setSystemUiVisibility(FimiAppContext.UI_HEIGHT);
        } else {
            window.addFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
            window.getDecorView().setSystemUiVisibility(0);
        }
        ViewGroup mContentView = window.findViewById(16908290);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    return insets;
                }
            });
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static void setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusColor) {
        Window window = activity.getWindow();
        window.clearFlags(NTLMConstants.FLAG_UNIDENTIFIED_9);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(0);
        window.getDecorView().setSystemUiVisibility(0);
        ViewGroup mContentView = window.findViewById(16908290);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mChildView, new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    return insets;
                }
            });
            ViewCompat.setFitsSystemWindows(mChildView, true);
            ViewCompat.requestApplyInsets(mChildView);
        }
        ((View) appBarLayout.getParent()).setFitsSystemWindows(true);
        appBarLayout.setFitsSystemWindows(true);
        collapsingToolbarLayout.setFitsSystemWindows(true);
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(true);
        toolbar.setFitsSystemWindows(false);
        collapsingToolbarLayout.setStatusBarScrimColor(statusColor);
    }
}
