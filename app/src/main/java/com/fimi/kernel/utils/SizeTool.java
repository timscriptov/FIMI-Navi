package com.fimi.kernel.utils;

import android.content.Context;
import android.util.TypedValue;

/* loaded from: classes.dex */
public class SizeTool {
    public static int pixToDp(float value, Context context) {
        return (int) TypedValue.applyDimension(1, value, context.getResources().getDisplayMetrics());
    }
}
