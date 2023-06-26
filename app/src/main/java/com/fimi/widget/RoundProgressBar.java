package com.fimi.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.internal.view.SupportMenu;

import com.fimi.android.app.R;

public class RoundProgressBar extends View {
    public static final int FILL = 1;
    public static final int STROKE = 0;
    private final int style;
    private final boolean textIsDisplayable;
    private final Paint paint;
    private int max;
    private int progress;
    private int roundColor;
    private int roundProgressColor;
    private float roundWidth;
    private float roundWidthPlan;
    private int textColor;
    private float textSize;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.roundWidthPlan = 4.5f;
        this.paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.DownRoundProgress);
        this.roundColor = mTypedArray.getColor(R.styleable.DownRoundProgress_roundColor, SupportMenu.CATEGORY_MASK);
        this.roundProgressColor = mTypedArray.getColor(R.styleable.DownRoundProgress_roundProgressColor, -16711936);
        this.textColor = mTypedArray.getColor(R.styleable.DownRoundProgress_textColor, -16711936);
        this.textSize = mTypedArray.getDimension(R.styleable.DownRoundProgress_textSize, 15.0f);
        this.roundWidth = mTypedArray.getDimension(R.styleable.DownRoundProgress_roundWidth, 5.0f);
        this.roundWidthPlan = mTypedArray.getDimension(R.styleable.DownRoundProgress_roundProgressWidth, 5.0f);
        this.max = mTypedArray.getInteger(R.styleable.DownRoundProgress_max, 100);
        this.textIsDisplayable = mTypedArray.getBoolean(R.styleable.DownRoundProgress_textIsDisplayable, true);
        this.style = mTypedArray.getInt(R.styleable.DownRoundProgress_style, StyleType.STROKE.value);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centre = getWidth() / 2;
        int radius = (int) (centre - (this.roundWidth / 2.0f));
        this.paint.setColor(this.roundColor);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.roundWidth);
        this.paint.setAntiAlias(true);
        canvas.drawCircle(centre, centre, radius, this.paint);
        this.paint.setStrokeWidth(0.0f);
        this.paint.setColor(this.textColor);
        this.paint.setTextSize(this.textSize);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setTypeface(Typeface.DEFAULT_BOLD);
        int percent = (int) ((this.progress / this.max) * 100.0f);
        float textWidth = this.paint.measureText(percent + "%");
        if (this.textIsDisplayable && this.style == 0) {
            canvas.drawText(percent + "%", centre - (textWidth / 2.0f), centre + (this.textSize / 2.0f), this.paint);
        }
        this.paint.setStrokeWidth(this.roundWidthPlan);
        this.paint.setColor(this.roundProgressColor);
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        switch (this.style) {
            case 0:
                this.paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 270.0f, (this.progress * 360) / this.max, false, this.paint);
                return;
            case 1:
                this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (this.progress != 0) {
                    canvas.drawArc(oval, 270.0f, (this.progress * 360) / this.max, true, this.paint);
                    return;
                }
                return;
            default:
        }
    }

    public synchronized int getMax() {
        return this.max;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    public synchronized int getProgress() {
        return this.progress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > this.max) {
            progress = this.max;
        }
        if (progress <= this.max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getCricleColor() {
        return this.roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return this.roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return this.roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public enum StyleType {
        FILL(1),
        STROKE(0);

        final int value;

        StyleType(int value) {
            this.value = value;
        }

        static RoundProgressBar.StyleType get(int value) {
            if (value == 1) {
                return FILL;
            }
            return STROKE;
        }
    }
}
