package com.fimi.album.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;

/* loaded from: classes.dex */
public class MediaDownloadProgressView extends View {
    private static final int[] SECTION_COLORS = {-16711936, InputDeviceCompat.SOURCE_ANY, SupportMenu.CATEGORY_MASK};
    private int backColor;
    private float currentCount;
    private int frontColor;
    private int mHeight;
    private Paint mPaint;
    private int mWidth;
    private float maxCount;

    public MediaDownloadProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.frontColor = 0;
        this.backColor = 0;
        initView(context);
    }

    public MediaDownloadProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.frontColor = 0;
        this.backColor = 0;
        initView(context);
    }

    public MediaDownloadProgressView(Context context) {
        super(context);
        this.frontColor = 0;
        this.backColor = 0;
        initView(context);
    }

    private void initView(Context context) {
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        int round = this.mHeight / 2;
        if (this.backColor == 0) {
            this.mPaint.setColor(771751935);
        } else {
            this.mPaint.setColor(this.backColor);
        }
        RectF rectBg = new RectF(0.0f, 0.0f, this.mWidth, this.mHeight);
        canvas.drawRoundRect(rectBg, round, round, this.mPaint);
        float section = this.currentCount / this.maxCount;
        RectF rectProgressBg = new RectF(0.0f, 0.0f, this.mWidth * section, this.mHeight);
        if (this.frontColor == 0) {
            this.mPaint.setColor(1895825407);
        } else {
            this.mPaint.setColor(this.frontColor);
        }
        canvas.drawRoundRect(rectProgressBg, round, round, this.mPaint);
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (((dip >= 0 ? 1 : -1) * 0.5f) + (dip * scale));
    }

    public float getMaxCount() {
        return this.maxCount;
    }

    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    public float getCurrentCount() {
        return this.currentCount;
    }

    public void setCurrentCount(float currentCount) {
        if (currentCount > this.maxCount) {
            currentCount = this.maxCount;
        }
        this.currentCount = currentCount;
        invalidate();
    }

    public void setFrontColor(int frontColor) {
        this.frontColor = frontColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == 1073741824 || widthSpecMode == Integer.MIN_VALUE) {
            this.mWidth = widthSpecSize;
        } else {
            this.mWidth = 0;
        }
        if (heightSpecMode == Integer.MIN_VALUE || heightSpecMode == 0) {
            this.mHeight = dipToPx(15);
        } else {
            this.mHeight = heightSpecSize;
        }
        setMeasuredDimension(this.mWidth, this.mHeight);
    }
}
