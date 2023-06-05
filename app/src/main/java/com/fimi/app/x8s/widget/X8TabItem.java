package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;

public class X8TabItem extends LinearLayout implements View.OnClickListener {
    private int backBg;
    private int curIndex;
    private int lineColor;
    private int lineStroke;
    private OnSelectListener mOnSelectListener;
    private int radius;
    private int selectTabBg;
    private int selectTextColor;
    private int space;
    private int tabHeight;
    private int tabWidth;
    private String[] textArr;
    private float textSize;
    private int unSelectTabBg;
    private int unSelectTextColor;

    public X8TabItem(Context context) {
        this(context, null);
    }

    public X8TabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X8TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.backBg = -1;
        this.unSelectTabBg = -16776961;
        this.selectTabBg = -1;
        this.tabWidth = 80;
        this.tabHeight = -1;
        this.unSelectTextColor = -1;
        this.selectTextColor = -16776961;
        this.textSize = 16.0f;
        this.space = 1;
        this.radius = 0;
        this.curIndex = 1;
        this.textArr = new String[0];
        setOrientation(LinearLayout.HORIZONTAL);
        readAttr(context, attrs);
        sove();
    }

    private void readAttr(@NonNull Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.X8Tab);
        this.curIndex = a.getInt(R.styleable.X8Tab_default_index, 0);
        this.radius = a.getDimensionPixelSize(R.styleable.X8Tab_radiusC, dpToPx(this.radius));
        this.backBg = a.getColor(R.styleable.X8Tab_bg, -1);
        this.lineColor = a.getColor(R.styleable.X8Tab_lineColor, -1);
        this.lineStroke = a.getDimensionPixelSize(R.styleable.X8Tab_lineStroke, dpToPx(this.lineStroke));
        this.unSelectTabBg = a.getColor(R.styleable.X8Tab_tab_unselect_color, Color.parseColor("#51B5EF"));
        this.selectTabBg = a.getColor(R.styleable.X8Tab_tab_select_color, -1);
        this.unSelectTextColor = a.getColor(R.styleable.X8Tab_text_unselect_color, -1);
        this.selectTextColor = a.getColor(R.styleable.X8Tab_text_select_color, Color.parseColor("#51B5EF"));
        this.space = a.getDimensionPixelSize(R.styleable.X8Tab_tab_space, 1);
        this.tabWidth = a.getDimensionPixelSize(R.styleable.X8Tab_tab_width, dpToPx(this.tabWidth));
        this.tabHeight = a.getDimensionPixelSize(R.styleable.X8Tab_tab_height, -1);
        this.textSize = a.getDimension(R.styleable.X8Tab_text_sizeC, this.textSize);
        CharSequence[] arr = a.getTextArray(R.styleable.X8Tab_src);
        if (arr != null) {
            String[] tArr = new String[arr.length];
            for (int i = 0; i < arr.length; i++) {
                tArr[i] = String.valueOf(arr[i]);
            }
            this.textArr = tArr;
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = View.MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = View.MeasureSpec.getMode(heightMeasureSpec);
        View.MeasureSpec.getSize(widthMeasureSpec);
        View.MeasureSpec.getSize(heightMeasureSpec);
        if (modeWidth != 1073741824 && this.tabWidth > -1) {
            for (int i = 0; i < getChildCount(); i++) {
                TextView view = (TextView) getChildAt(i);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                lp.width = this.tabWidth;
            }
        }
        if (modeHeight != 1073741824 && this.tabHeight > -1) {
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                TextView view2 = (TextView) getChildAt(i2);
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) view2.getLayoutParams();
                lp2.height = this.tabHeight;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void sove() {
        GradientDrawable dd = new GradientDrawable();
        dd.setCornerRadii(new float[]{this.radius, this.radius, this.radius, this.radius, this.radius, this.radius, this.radius, this.radius});
        dd.setStroke(this.lineStroke, this.lineColor);
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(dd);
        } else {
            setBackgroundDrawable(dd);
        }
        removeAllViews();
        if (this.curIndex >= this.textArr.length || this.curIndex < 0) {
            this.curIndex = 0;
        }
        for (int i = 0; i < this.textArr.length; i++) {
            TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -1);
            if (i > 0) {
                params.leftMargin = this.space;
            }
            GradientDrawable d = getFitGradientDrawable(i);
            if (this.curIndex == i) {
                tv.setTextColor(this.selectTextColor);
                d.setColor(this.selectTabBg);
            } else {
                tv.setTextColor(this.unSelectTextColor);
                d.setColor(this.unSelectTabBg);
            }
            tv.setText(this.textArr[i]);
            tv.setGravity(17);
            tv.setTextSize(0, this.textSize);
            if (Build.VERSION.SDK_INT >= 16) {
                tv.setBackground(d);
            } else {
                tv.setBackgroundDrawable(d);
            }
            params.weight = 1.0f;
            tv.setLayoutParams(params);
            tv.setTag(i);
            tv.setOnClickListener(this);
            addView(tv);
        }
    }

    @NonNull
    private GradientDrawable getFitGradientDrawable(int index) {
        if (index == 0 && index == this.textArr.length - 1) {
            GradientDrawable d = new GradientDrawable();
            d.setCornerRadii(new float[]{this.radius, this.radius, this.radius, this.radius, this.radius, this.radius, this.radius, this.radius});
            return d;
        } else if (index == 0) {
            GradientDrawable d2 = new GradientDrawable();
            d2.setCornerRadii(new float[]{this.radius, this.radius, 0.0f, 0.0f, 0.0f, 0.0f, this.radius, this.radius});
            return d2;
        } else if (index == this.textArr.length - 1) {
            GradientDrawable d3 = new GradientDrawable();
            d3.setCornerRadii(new float[]{0.0f, 0.0f, this.radius, this.radius, this.radius, this.radius, 0.0f, 0.0f});
            return d3;
        } else {
            GradientDrawable d4 = new GradientDrawable();
            d4.setCornerRadii(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f});
            return d4;
        }
    }

    @Override
    public void onClick(@NonNull View v) {
        int index = (Integer) v.getTag();
        if (this.mOnSelectListener != null) {
            this.mOnSelectListener.onSelect(index, this.textArr[index]);
        }
    }

    public void setSelect(int index) {
        if (index != this.curIndex) {
            TextView tv = (TextView) getChildAt(this.curIndex);
            tv.setTextColor(this.unSelectTextColor);
            GradientDrawable d = getFitGradientDrawable(this.curIndex);
            d.setColor(this.unSelectTabBg);
            if (Build.VERSION.SDK_INT >= 16) {
                tv.setBackground(d);
            } else {
                tv.setBackgroundDrawable(d);
            }
            this.curIndex = index;
            TextView tv2 = (TextView) getChildAt(this.curIndex);
            tv2.setTextColor(this.selectTextColor);
            GradientDrawable d2 = getFitGradientDrawable(this.curIndex);
            d2.setColor(this.selectTabBg);
            if (Build.VERSION.SDK_INT >= 16) {
                tv2.setBackground(d2);
            } else {
                tv2.setBackgroundDrawable(d2);
            }
        }
    }

    public void upSelect(int index) {
        if (index != this.curIndex) {
            TextView tv = (TextView) getChildAt(this.curIndex);
            tv.setTextColor(this.unSelectTextColor);
            GradientDrawable d = getFitGradientDrawable(this.curIndex);
            d.setColor(this.unSelectTabBg);
            if (Build.VERSION.SDK_INT >= 16) {
                tv.setBackground(d);
            } else {
                tv.setBackgroundDrawable(d);
            }
            this.curIndex = index;
            TextView tv2 = (TextView) getChildAt(this.curIndex);
            tv2.setTextColor(this.selectTextColor);
            GradientDrawable d2 = getFitGradientDrawable(this.curIndex);
            d2.setColor(this.selectTabBg);
            if (Build.VERSION.SDK_INT >= 16) {
                tv2.setBackground(d2);
            } else {
                tv2.setBackgroundDrawable(d2);
            }
            if (this.mOnSelectListener != null) {
                this.mOnSelectListener.onSelect(index, this.textArr[index]);
            }
        }
    }

    public int getSelectIndex() {
        return this.curIndex;
    }

    int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    int spToPx(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    public void setOnSelectListener(OnSelectListener mOnSelectListener) {
        this.mOnSelectListener = mOnSelectListener;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("zdy", "......" + getWidth() + " " + getHeight());
        if (getWidth() != 0) {
            int count = this.textArr.length;
            Paint p = new Paint();
            p.setColor(this.lineColor);
            p.setStyle(Paint.Style.FILL);
            float with = (getWidth() * 1.0f) / this.textArr.length;
            for (int i = 1; i < count; i++) {
                RectF r1 = new RectF();
                float nPos = with * i;
                r1.left = nPos - (this.space / 2.0f);
                r1.right = (this.space / 2.0f) + nPos;
                r1.top = this.lineStroke + 0;
                r1.bottom = getHeight() - this.lineStroke;
                canvas.drawRect(r1, p);
            }
        }
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setEnabled(b);
        }
    }

    public void resetCurIndex() {
        this.curIndex = 1;
    }

    public interface OnSelectListener {
        void onSelect(int i, String str);
    }
}
