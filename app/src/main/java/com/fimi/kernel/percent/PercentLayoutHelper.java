package com.fimi.kernel.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;

import com.fimi.android.app.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class PercentLayoutHelper {
    private static final String REGEX_PERCENT = "^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)%([wh]?)$";
    private static final String TAG = "PercentLayout";
    private final ViewGroup mHost;

    public PercentLayoutHelper(ViewGroup host) {
        this.mHost = host;
    }

    public static void fetchWidthAndHeight(@NonNull ViewGroup.LayoutParams params, @NonNull TypedArray array, int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    public static PercentLayoutInfo getPercentLayoutInfo(@NonNull Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
        PercentLayoutInfo info = setPaddingRelatedVal(array, setMinMaxWidthHeightRelatedVal(array, setTextSizeSupportVal(array, setMarginRelatedVal(array, setWidthAndHeightVal(array, null)))));
        Log.d(TAG, "constructed: " + info);
        array.recycle();
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "constructed: " + info);
        }
        return info;
    }

    private static PercentLayoutInfo setWidthAndHeightVal(TypedArray array, PercentLayoutInfo info) {
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_widthPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent width: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.widthPercent = percentVal;
        }
        PercentLayoutInfo.PercentVal percentVal2 = getPercentVal(array, R.styleable.PercentLayout_layout_heightPercent, false);
        if (percentVal2 != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent height: " + percentVal2.percent);
            }
            PercentLayoutInfo info2 = checkForInfoExists(info);
            info2.heightPercent = percentVal2;
            return info2;
        }
        return info;
    }

    private static PercentLayoutInfo setTextSizeSupportVal(TypedArray array, PercentLayoutInfo info) {
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_textSizePercent, false);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent text size: " + percentVal.percent);
            }
            PercentLayoutInfo info2 = checkForInfoExists(info);
            info2.textSizePercent = percentVal;
            return info2;
        }
        return info;
    }

    private static PercentLayoutInfo setMinMaxWidthHeightRelatedVal(TypedArray array, PercentLayoutInfo info) {
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_maxWidthPercent, true);
        if (percentVal != null) {
            checkForInfoExists(info);
            info.maxWidthPercent = percentVal;
        }
        PercentLayoutInfo.PercentVal percentVal2 = getPercentVal(array, R.styleable.PercentLayout_layout_maxHeightPercent, false);
        if (percentVal2 != null) {
            checkForInfoExists(info);
            info.maxHeightPercent = percentVal2;
        }
        PercentLayoutInfo.PercentVal percentVal3 = getPercentVal(array, R.styleable.PercentLayout_layout_minWidthPercent, true);
        if (percentVal3 != null) {
            checkForInfoExists(info);
            info.minWidthPercent = percentVal3;
        }
        PercentLayoutInfo.PercentVal percentVal4 = getPercentVal(array, R.styleable.PercentLayout_layout_minHeightPercent, false);
        if (percentVal4 != null) {
            checkForInfoExists(info);
            info.minHeightPercent = percentVal4;
        }
        return info;
    }

    private static PercentLayoutInfo setMarginRelatedVal(TypedArray array, PercentLayoutInfo info) {
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_marginPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.leftMarginPercent = percentVal;
            info.topMarginPercent = percentVal;
            info.rightMarginPercent = percentVal;
            info.bottomMarginPercent = percentVal;
        }
        PercentLayoutInfo.PercentVal percentVal2 = getPercentVal(array, R.styleable.PercentLayout_layout_marginLeftPercent, true);
        if (percentVal2 != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent left margin: " + percentVal2.percent);
            }
            info = checkForInfoExists(info);
            info.leftMarginPercent = percentVal2;
        }
        PercentLayoutInfo.PercentVal percentVal3 = getPercentVal(array, R.styleable.PercentLayout_layout_marginTopPercent, false);
        if (percentVal3 != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent top margin: " + percentVal3.percent);
            }
            info = checkForInfoExists(info);
            info.topMarginPercent = percentVal3;
        }
        PercentLayoutInfo.PercentVal percentVal4 = getPercentVal(array, R.styleable.PercentLayout_layout_marginRightPercent, true);
        if (percentVal4 != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent right margin: " + percentVal4.percent);
            }
            info = checkForInfoExists(info);
            info.rightMarginPercent = percentVal4;
        }
        PercentLayoutInfo.PercentVal percentVal5 = getPercentVal(array, R.styleable.PercentLayout_layout_marginBottomPercent, false);
        if (percentVal5 != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent bottom margin: " + percentVal5.percent);
            }
            info = checkForInfoExists(info);
            info.bottomMarginPercent = percentVal5;
        }
        PercentLayoutInfo.PercentVal percentVal6 = getPercentVal(array, R.styleable.PercentLayout_layout_marginStartPercent, true);
        if (percentVal6 != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent start margin: " + percentVal6.percent);
            }
            info = checkForInfoExists(info);
            info.startMarginPercent = percentVal6;
        }
        PercentLayoutInfo.PercentVal percentVal7 = getPercentVal(array, R.styleable.PercentLayout_layout_marginEndPercent, true);
        if (percentVal7 != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent end margin: " + percentVal7.percent);
            }
            PercentLayoutInfo info2 = checkForInfoExists(info);
            info2.endMarginPercent = percentVal7;
            return info2;
        }
        return info;
    }

    private static PercentLayoutInfo setPaddingRelatedVal(TypedArray array, PercentLayoutInfo info) {
        PercentLayoutInfo.PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_layout_paddingPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingLeftPercent = percentVal;
            info.paddingRightPercent = percentVal;
            info.paddingBottomPercent = percentVal;
            info.paddingTopPercent = percentVal;
        }
        PercentLayoutInfo.PercentVal percentVal2 = getPercentVal(array, R.styleable.PercentLayout_layout_paddingLeftPercent, true);
        if (percentVal2 != null) {
            info = checkForInfoExists(info);
            info.paddingLeftPercent = percentVal2;
        }
        PercentLayoutInfo.PercentVal percentVal3 = getPercentVal(array, R.styleable.PercentLayout_layout_paddingRightPercent, true);
        if (percentVal3 != null) {
            info = checkForInfoExists(info);
            info.paddingRightPercent = percentVal3;
        }
        PercentLayoutInfo.PercentVal percentVal4 = getPercentVal(array, R.styleable.PercentLayout_layout_paddingTopPercent, true);
        if (percentVal4 != null) {
            info = checkForInfoExists(info);
            info.paddingTopPercent = percentVal4;
        }
        PercentLayoutInfo.PercentVal percentVal5 = getPercentVal(array, R.styleable.PercentLayout_layout_paddingBottomPercent, true);
        if (percentVal5 != null) {
            PercentLayoutInfo info2 = checkForInfoExists(info);
            info2.paddingBottomPercent = percentVal5;
            return info2;
        }
        return info;
    }

    private static PercentLayoutInfo.PercentVal getPercentVal(@NonNull TypedArray array, int index, boolean baseWidth) {
        String sizeStr = array.getString(index);
        return getPercentVal(sizeStr, baseWidth);
    }

    @NonNull
    private static PercentLayoutInfo checkForInfoExists(PercentLayoutInfo info) {
        if (info == null) {
            return new PercentLayoutInfo();
        }
        return info;
    }

    @Nullable
    private static PercentLayoutInfo.PercentVal getPercentVal(String percentStr, boolean isOnWidth) {
        boolean isBasedWidth = true;
        if (percentStr == null) {
            return null;
        }
        Pattern p = Pattern.compile(REGEX_PERCENT);
        Matcher matcher = p.matcher(percentStr);
        if (!matcher.matches()) {
            throw new RuntimeException("the value of layout_xxxPercent invalid! ==>" + percentStr);
        }
        int len = percentStr.length();
        String floatVal = matcher.group(1);
        String lastAlpha = percentStr.substring(len - 1);
        float percent = Float.parseFloat(floatVal) / 100.0f;
        if ((!isOnWidth || lastAlpha.equals("h")) && !lastAlpha.equals("w")) {
            isBasedWidth = false;
        }
        return new PercentLayoutInfo.PercentVal(percent, isBasedWidth);
    }

    private static boolean shouldHandleMeasuredWidthTooSmall(View view, PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredWidthAndState(view) & ViewCompat.MEASURED_STATE_MASK;
        return info != null && info.widthPercent != null && info.mPreservedParams != null && state == 16777216 && info.widthPercent.percent >= 0.0f && info.mPreservedParams.width == -2;
    }

    private static boolean shouldHandleMeasuredHeightTooSmall(View view, PercentLayoutInfo info) {
        if (info == null || info.heightPercent == null || info.mPreservedParams == null) {
            return false;
        }
        int state = ViewCompat.getMeasuredHeightAndState(view) & ViewCompat.MEASURED_STATE_MASK;
        return state == 16777216 && info.heightPercent.percent >= 0.0f && info.mPreservedParams.height == -2;
    }

    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "adjustChildren: " + this.mHost + " widthMeasureSpec: " + View.MeasureSpec.toString(widthMeasureSpec) + " heightMeasureSpec: " + View.MeasureSpec.toString(heightMeasureSpec));
        }
        int widthHint = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = View.MeasureSpec.getSize(heightMeasureSpec);
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "widthHint = " + widthHint + " , heightHint = " + heightHint);
        }
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "should adjust " + view + " " + params);
            }
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "using " + info);
                }
                if (info != null) {
                    supportTextSize(widthHint, heightHint, view, info);
                    supportPadding(widthHint, heightHint, view, info);
                    supportMinOrMaxDimesion(widthHint, heightHint, view, info);
                    if (params instanceof ViewGroup.MarginLayoutParams) {
                        info.fillMarginLayoutParams((ViewGroup.MarginLayoutParams) params, widthHint, heightHint);
                    } else {
                        info.fillLayoutParams(params, widthHint, heightHint);
                    }
                }
            }
        }
    }

    private void supportPadding(int widthHint, int heightHint, View view, PercentLayoutInfo info) {
        int left = view.getPaddingLeft();
        int right = view.getPaddingRight();
        int top = view.getPaddingTop();
        int bottom = view.getPaddingBottom();
        PercentLayoutInfo.PercentVal percentVal = info.paddingLeftPercent;
        if (percentVal != null) {
            int base = percentVal.isBaseWidth ? widthHint : heightHint;
            left = (int) (base * percentVal.percent);
        }
        PercentLayoutInfo.PercentVal percentVal2 = info.paddingRightPercent;
        if (percentVal2 != null) {
            int base2 = percentVal2.isBaseWidth ? widthHint : heightHint;
            right = (int) (base2 * percentVal2.percent);
        }
        PercentLayoutInfo.PercentVal percentVal3 = info.paddingTopPercent;
        if (percentVal3 != null) {
            int base3 = percentVal3.isBaseWidth ? widthHint : heightHint;
            top = (int) (base3 * percentVal3.percent);
        }
        PercentLayoutInfo.PercentVal percentVal4 = info.paddingBottomPercent;
        if (percentVal4 != null) {
            int base4 = percentVal4.isBaseWidth ? widthHint : heightHint;
            bottom = (int) (base4 * percentVal4.percent);
        }
        view.setPadding(left, top, right, bottom);
    }

    private void supportMinOrMaxDimesion(int widthHint, int heightHint, View view, PercentLayoutInfo info) {
        try {
            Class clazz = view.getClass();
            invokeMethod("setMaxWidth", widthHint, heightHint, view, clazz, info.maxWidthPercent);
            invokeMethod("setMaxHeight", widthHint, heightHint, view, clazz, info.maxHeightPercent);
            invokeMethod("setMinWidth", widthHint, heightHint, view, clazz, info.minWidthPercent);
            invokeMethod("setMinHeight", widthHint, heightHint, view, clazz, info.minHeightPercent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
    }

    private void invokeMethod(String methodName, int widthHint, int heightHint, View view, Class clazz, PercentLayoutInfo.PercentVal percentVal) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (percentVal != null) {
            Method setMaxWidthMethod = clazz.getMethod(methodName, Integer.TYPE);
            setMaxWidthMethod.setAccessible(true);
            int base = percentVal.isBaseWidth ? widthHint : heightHint;
            setMaxWidthMethod.invoke(view, Integer.valueOf((int) (base * percentVal.percent)));
        }
    }

    private void supportTextSize(int widthHint, int heightHint, View view, PercentLayoutInfo info) {
        PercentLayoutInfo.PercentVal textSizePercent = info.textSizePercent;
        if (textSizePercent != null) {
            int base = textSizePercent.isBaseWidth ? widthHint : heightHint;
            float textSize = (int) (base * textSizePercent.percent);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(0, textSize);
            }
        }
    }

    public void restoreOriginalParams() {
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "should restore " + view + " " + params);
            }
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "using " + info);
                }
                if (info != null) {
                    if (params instanceof ViewGroup.MarginLayoutParams) {
                        info.restoreMarginLayoutParams((ViewGroup.MarginLayoutParams) params);
                    } else {
                        info.restoreLayoutParams(params);
                    }
                }
            }
        }
    }

    public boolean handleMeasuredStateTooSmall() {
        PercentLayoutInfo info;
        boolean needsSecondMeasure = false;
        int N = this.mHost.getChildCount();
        for (int i = 0; i < N; i++) {
            View view = this.mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if ((params instanceof PercentLayoutParams) && (info = ((PercentLayoutParams) params).getPercentLayoutInfo()) != null) {
                if (shouldHandleMeasuredWidthTooSmall(view, info)) {
                    needsSecondMeasure = true;
                    params.width = -2;
                }
                if (shouldHandleMeasuredHeightTooSmall(view, info)) {
                    needsSecondMeasure = true;
                    params.height = -2;
                }
            }
        }
        return needsSecondMeasure;
    }

    public interface PercentLayoutParams {
        PercentLayoutInfo getPercentLayoutInfo();
    }

    /* loaded from: classes.dex */
    public static class PercentLayoutInfo {
        final ViewGroup.MarginLayoutParams mPreservedParams = new ViewGroup.MarginLayoutParams(0, 0);
        public PercentVal bottomMarginPercent;
        public PercentVal endMarginPercent;
        public PercentVal heightPercent;
        public PercentVal leftMarginPercent;
        public PercentVal maxHeightPercent;
        public PercentVal maxWidthPercent;
        public PercentVal minHeightPercent;
        public PercentVal minWidthPercent;
        public PercentVal paddingBottomPercent;
        public PercentVal paddingLeftPercent;
        public PercentVal paddingRightPercent;
        public PercentVal paddingTopPercent;
        public PercentVal rightMarginPercent;
        public PercentVal startMarginPercent;
        public PercentVal textSizePercent;
        public PercentVal topMarginPercent;
        public PercentVal widthPercent;

        public void fillLayoutParams(ViewGroup.LayoutParams params, int widthHint, int heightHint) {
            this.mPreservedParams.width = params.width;
            this.mPreservedParams.height = params.height;
            if (this.widthPercent != null) {
                int base = this.widthPercent.isBaseWidth ? widthHint : heightHint;
                params.width = (int) (base * this.widthPercent.percent);
            }
            if (this.heightPercent != null) {
                int base2 = this.heightPercent.isBaseWidth ? widthHint : heightHint;
                params.height = (int) (base2 * this.heightPercent.percent);
            }
        }

        public void fillMarginLayoutParams(ViewGroup.MarginLayoutParams params, int widthHint, int heightHint) {
            fillLayoutParams(params, widthHint, heightHint);
            this.mPreservedParams.leftMargin = params.leftMargin;
            this.mPreservedParams.topMargin = params.topMargin;
            this.mPreservedParams.rightMargin = params.rightMargin;
            this.mPreservedParams.bottomMargin = params.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(this.mPreservedParams, MarginLayoutParamsCompat.getMarginStart(params));
            MarginLayoutParamsCompat.setMarginEnd(this.mPreservedParams, MarginLayoutParamsCompat.getMarginEnd(params));
            if (this.leftMarginPercent != null) {
                int base = this.leftMarginPercent.isBaseWidth ? widthHint : heightHint;
                params.leftMargin = (int) (base * this.leftMarginPercent.percent);
            }
            if (this.topMarginPercent != null) {
                int base2 = this.topMarginPercent.isBaseWidth ? widthHint : heightHint;
                params.topMargin = (int) (base2 * this.topMarginPercent.percent);
            }
            if (this.rightMarginPercent != null) {
                int base3 = this.rightMarginPercent.isBaseWidth ? widthHint : heightHint;
                params.rightMargin = (int) (base3 * this.rightMarginPercent.percent);
            }
            if (this.bottomMarginPercent != null) {
                int base4 = this.bottomMarginPercent.isBaseWidth ? widthHint : heightHint;
                params.bottomMargin = (int) (base4 * this.bottomMarginPercent.percent);
            }
            if (this.startMarginPercent != null) {
                int base5 = this.startMarginPercent.isBaseWidth ? widthHint : heightHint;
                MarginLayoutParamsCompat.setMarginStart(params, (int) (base5 * this.startMarginPercent.percent));
            }
            if (this.endMarginPercent != null) {
                int base6 = this.endMarginPercent.isBaseWidth ? widthHint : heightHint;
                MarginLayoutParamsCompat.setMarginEnd(params, (int) (base6 * this.endMarginPercent.percent));
            }
            if (Log.isLoggable(PercentLayoutHelper.TAG, Log.DEBUG)) {
                Log.d(PercentLayoutHelper.TAG, "after fillMarginLayoutParams: (" + params.width + ", " + params.height + ")");
            }
        }

        public String toString() {
            return "PercentLayoutInfo{widthPercent=" + this.widthPercent + ", heightPercent=" + this.heightPercent + ", leftMarginPercent=" + this.leftMarginPercent + ", topMarginPercent=" + this.topMarginPercent + ", rightMarginPercent=" + this.rightMarginPercent + ", bottomMarginPercent=" + this.bottomMarginPercent + ", startMarginPercent=" + this.startMarginPercent + ", endMarginPercent=" + this.endMarginPercent + ", textSizePercent=" + this.textSizePercent + ", maxWidthPercent=" + this.maxWidthPercent + ", maxHeightPercent=" + this.maxHeightPercent + ", minWidthPercent=" + this.minWidthPercent + ", minHeightPercent=" + this.minHeightPercent + ", paddingLeftPercent=" + this.paddingLeftPercent + ", paddingRightPercent=" + this.paddingRightPercent + ", paddingTopPercent=" + this.paddingTopPercent + ", paddingBottomPercent=" + this.paddingBottomPercent + ", mPreservedParams=" + this.mPreservedParams + '}';
        }

        public void restoreMarginLayoutParams(ViewGroup.MarginLayoutParams params) {
            restoreLayoutParams(params);
            params.leftMargin = this.mPreservedParams.leftMargin;
            params.topMargin = this.mPreservedParams.topMargin;
            params.rightMargin = this.mPreservedParams.rightMargin;
            params.bottomMargin = this.mPreservedParams.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(params, MarginLayoutParamsCompat.getMarginStart(this.mPreservedParams));
            MarginLayoutParamsCompat.setMarginEnd(params, MarginLayoutParamsCompat.getMarginEnd(this.mPreservedParams));
        }

        public void restoreLayoutParams(ViewGroup.LayoutParams params) {
            params.width = this.mPreservedParams.width;
            params.height = this.mPreservedParams.height;
        }

        /* loaded from: classes.dex */
        public static class PercentVal {
            public boolean isBaseWidth;
            public float percent;

            public PercentVal() {
                this.percent = -1.0f;
            }

            public PercentVal(float percent, boolean isBaseWidth) {
                this.percent = -1.0f;
                this.percent = percent;
                this.isBaseWidth = isBaseWidth;
            }

            public String toString() {
                return "PercentVal{percent=" + this.percent + ", isBaseWidth=" + this.isBaseWidth + '}';
            }
        }
    }
}
