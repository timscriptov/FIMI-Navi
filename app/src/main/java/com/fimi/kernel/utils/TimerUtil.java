package com.fimi.kernel.utils;

import java.util.Formatter;
import java.util.Locale;

/* loaded from: classes.dex */
public class TimerUtil {
    private static volatile TimerUtil timerUtil;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    public static TimerUtil getInstance() {
        if (timerUtil == null) {
            synchronized (TimerUtil.class) {
                if (timerUtil == null) {
                    timerUtil = new TimerUtil();
                }
            }
        }
        return timerUtil;
    }

    public String stringForTime(int timeMs, boolean isMs) {
        int totalSeconds;
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        if (isMs) {
            totalSeconds = timeMs;
        } else {
            totalSeconds = Float.valueOf(Math.round(timeMs / 1000.0f)).intValue();
        }
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        this.mFormatBuilder.setLength(0);
        return hours > 0 ? this.mFormatter.format("%d:%02d:%02d", Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)).toString() : this.mFormatter.format("%02d:%02d", Integer.valueOf(minutes), Integer.valueOf(seconds)).toString();
    }

    public String stringForTime(int totalSeconds) {
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        int totalSeconds2 = totalSeconds / 1000;
        int seconds = totalSeconds2 % 60;
        int minutes = (totalSeconds2 / 60) % 60;
        int hours = totalSeconds2 / 3600;
        this.mFormatBuilder.setLength(0);
        return hours > 0 ? this.mFormatter.format("%d h %2d min", Integer.valueOf(hours), Integer.valueOf(minutes)).toString() : this.mFormatter.format("%2d min %2d s", Integer.valueOf(minutes), Integer.valueOf(seconds)).toString();
    }
}
