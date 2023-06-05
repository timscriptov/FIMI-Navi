package com.fimi.kernel.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

/* loaded from: classes.dex */
public class AbViewUtil {
    public static final int INVALID = Integer.MIN_VALUE;
    private static final int xMax = 6;
    private static final int yMax = 4;
    private static int[] xBoxs = null;
    private static int[] yBoxs = null;

    public static void setAbsListViewHeight(AbsListView absListView, int lineNumber, int verticalSpace) {
        int totalHeight = getAbsListViewHeight(absListView, lineNumber, verticalSpace);
        ViewGroup.LayoutParams params = absListView.getLayoutParams();
        params.height = totalHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        absListView.setLayoutParams(params);
    }

    public static int getAbsListViewHeight(AbsListView absListView, int lineNumber, int verticalSpace) {
        int totalHeight = 0;
        int w = View.MeasureSpec.makeMeasureSpec(0, 0);
        int h = View.MeasureSpec.makeMeasureSpec(0, 0);
        absListView.measure(w, h);
        ListAdapter mListAdapter = (ListAdapter) absListView.getAdapter();
        if (mListAdapter == null) {
            return 0;
        }
        int count = mListAdapter.getCount();
        if (absListView instanceof ListView) {
            for (int i = 0; i < count; i++) {
                View listItem = mListAdapter.getView(i, null, absListView);
                listItem.measure(w, h);
                totalHeight += listItem.getMeasuredHeight();
            }
            if (count == 0) {
                totalHeight = verticalSpace;
            } else {
                totalHeight += ((ListView) absListView).getDividerHeight() * (count - 1);
            }
        } else if (absListView instanceof GridView) {
            int remain = count % lineNumber;
            if (remain > 0) {
                remain = 1;
            }
            if (mListAdapter.getCount() == 0) {
                totalHeight = verticalSpace;
            } else {
                View listItem2 = mListAdapter.getView(0, null, absListView);
                listItem2.measure(w, h);
                int line = (count / lineNumber) + remain;
                totalHeight = (listItem2.getMeasuredHeight() * line) + ((line - 1) * verticalSpace);
            }
        }
        return totalHeight;
    }

    public static void measureView(View view) {
        int childHeightSpec;
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(-1, -2);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        view.measure(childWidthSpec, childHeightSpec);
    }

    public static int getViewWidth(View view) {
        measureView(view);
        return view.getMeasuredWidth();
    }

    public static int getViewHeight(View view) {
        measureView(view);
        return view.getMeasuredHeight();
    }

    public static void removeSelfFromParent(View v) {
        ViewParent parent = v.getParent();
        if (parent != null && (parent instanceof ViewGroup)) {
            ((ViewGroup) parent).removeView(v);
        }
    }

    public static float dip2px(Context context, float dipValue) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        return applyDimension(1, dipValue, mDisplayMetrics);
    }

    public static float px2dip(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.density;
    }

    public static float sp2px(Context context, float spValue) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        return applyDimension(2, spValue, mDisplayMetrics);
    }

    public static float px2sp(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.scaledDensity;
    }

    public static int scaleValue(Context context, float value) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        if (mDisplayMetrics.scaledDensity > 2.0f) {
            if (mDisplayMetrics.widthPixels > 720) {
                value *= 1.3f - (1.0f / mDisplayMetrics.scaledDensity);
            } else if (mDisplayMetrics.widthPixels < 720) {
                value *= 1.0f - (1.0f / mDisplayMetrics.scaledDensity);
            }
        }
        return scale(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, value);
    }

    public static int scaleTextValue(Context context, float value) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        if (mDisplayMetrics.scaledDensity > 2.0f) {
        }
        return scale(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, value);
    }

    public static int scale(int displayWidth, int displayHeight, float pxValue) {
        if (pxValue == 0.0f) {
            return 0;
        }
        float scale = 1.0f;
        float scaleWidth = displayWidth / 720.0f;
        float scaleHeight = displayHeight / 1280.0f;
        try {
            scale = Math.min(scaleWidth, scaleHeight);
        } catch (Exception e) {
        }
        return Math.round((pxValue * scale) + 0.5f);
    }

    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case 0:
                return value;
            case 1:
                return value * metrics.density;
            case 2:
                return value * metrics.scaledDensity;
            case 3:
                return metrics.xdpi * value * 0.013888889f;
            case 4:
                return value * metrics.xdpi;
            case 5:
                return metrics.xdpi * value * 0.03937008f;
            default:
                return 0.0f;
        }
    }

    public static void scaleContentView(View parent, int id) {
        View view = parent.findViewById(id);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
        }
    }

    public static void scaleContentView(Context context, int id) {
        View view = ((Activity) context).findViewById(id);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
        }
    }

    @SuppressLint({"NewApi"})
    public static void scaleView(View view) {
        ViewGroup.MarginLayoutParams mMarginLayoutParams;
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            setTextSize(textView, textView.getTextSize());
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            int width = Integer.MIN_VALUE;
            int height = Integer.MIN_VALUE;
            if (params.width != -2 && params.width != -1) {
                width = params.width;
            }
            if (params.height != -2 && params.height != -1) {
                height = params.height;
            }
            setViewSize(view, width, height);
            setPadding(view, view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }
        if ((view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) && (mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams()) != null) {
            setMargin(view, mMarginLayoutParams.leftMargin, mMarginLayoutParams.topMargin, mMarginLayoutParams.rightMargin, mMarginLayoutParams.bottomMargin);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            int minWidth = scaleValue(view.getContext(), view.getMinimumWidth());
            int minHeight = scaleValue(view.getContext(), view.getMinimumHeight());
            view.setMinimumWidth(minWidth);
            view.setMinimumHeight(minHeight);
        }
    }

    public static void setSPTextSize(TextView textView, float size) {
        float scaledSize = scaleTextValue(textView.getContext(), size);
        textView.setTextSize(scaledSize);
    }

    public static void setTextSize(TextView textView, float sizePixels) {
        float scaledSize = scaleTextValue(textView.getContext(), sizePixels);
        textView.setTextSize(0, scaledSize);
    }

    public static void setTextSize(Context context, TextPaint textPaint, float sizePixels) {
        float scaledSize = scaleTextValue(context, sizePixels);
        textPaint.setTextSize(scaledSize);
    }

    public static void setTextSize(Context context, Paint paint, float sizePixels) {
        float scaledSize = scaleTextValue(context, sizePixels);
        paint.setTextSize(scaledSize);
    }

    public static void setViewSize(View view, int widthPixels, int heightPixels) {
        int scaledWidth = scaleValue(view.getContext(), widthPixels);
        int scaledHeight = scaleValue(view.getContext(), heightPixels);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            if (widthPixels != Integer.MIN_VALUE) {
                params.width = scaledWidth;
            }
            if (heightPixels != Integer.MIN_VALUE) {
                params.height = scaledHeight;
            }
            view.setLayoutParams(params);
        }
    }

    public static void setPadding(View view, int left, int top, int right, int bottom) {
        int scaledLeft = scaleValue(view.getContext(), left);
        int scaledTop = scaleValue(view.getContext(), top);
        int scaledRight = scaleValue(view.getContext(), right);
        int scaledBottom = scaleValue(view.getContext(), bottom);
        view.setPadding(scaledLeft, scaledTop, scaledRight, scaledBottom);
    }

    public static void setMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams mMarginLayoutParams;
        int scaledLeft = scaleValue(view.getContext(), left);
        int scaledTop = scaleValue(view.getContext(), top);
        int scaledRight = scaleValue(view.getContext(), right);
        int scaledBottom = scaleValue(view.getContext(), bottom);
        if ((view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) && (mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams()) != null) {
            if (left != Integer.MIN_VALUE) {
                mMarginLayoutParams.leftMargin = scaledLeft;
            }
            if (right != Integer.MIN_VALUE) {
                mMarginLayoutParams.rightMargin = scaledRight;
            }
            if (top != Integer.MIN_VALUE) {
                mMarginLayoutParams.topMargin = scaledTop;
            }
            if (bottom != Integer.MIN_VALUE) {
                mMarginLayoutParams.bottomMargin = scaledBottom;
            }
            view.setLayoutParams(mMarginLayoutParams);
        }
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (((rotation == 0 || rotation == 2) && height > width) || ((rotation == 1 || rotation == 3) && width > height)) {
            switch (rotation) {
                case 0:
                    return 1;
                case 1:
                    return 0;
                case 2:
                    return 9;
                case 3:
                    return 8;
                default:
                    return 1;
            }
        }
        switch (rotation) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 8;
            case 3:
                return 9;
            default:
                return 0;
        }
    }

    public static int xyToBox(Context context, float x, float y) {
        int currentNumber = 0;
        boolean isBreak = false;
        if (xBoxs == null && yBoxs == null) {
            int singleBoxX = getScreenWidth(context) / 6;
            int singleBoxY = getScreenHeight(context) / 4;
            xBoxs = new int[]{singleBoxX, singleBoxX * 2, singleBoxX * 3, singleBoxX * 4, singleBoxX * 5, singleBoxX * 6};
            yBoxs = new int[]{singleBoxY, singleBoxY * 2, singleBoxY * 3, singleBoxY * 4};
        }
        for (int i = 0; i < xBoxs.length; i++) {
            int n = 0;
            while (true) {
                if (n >= yBoxs.length) {
                    break;
                } else if (x > xBoxs[i] || y > yBoxs[n]) {
                    n++;
                } else {
                    currentNumber = (n * 6) + i;
                    isBreak = true;
                    break;
                }
            }
            if (isBreak) {
                break;
            }
        }
        return currentNumber;
    }
}
