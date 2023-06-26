package com.fimi.app.x8s.tools;

import android.content.Context;

import androidx.annotation.NonNull;


public class ConvertSize {
    public static int dipToPx(@NonNull Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    public static int pxToDip(@NonNull Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((pxValue / scale) + 0.5f);
    }
}
