package com.fimi.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.fimi.android.app.R;

import java.lang.reflect.Field;

public class StrokeTextView extends androidx.appcompat.widget.AppCompatTextView {
    private final boolean m_bDrawSideLine;
    int mInnerColor;
    int mOuterColor;
    TextPaint m_TextPaint;

    public StrokeTextView(Context context, int outerColor, int innnerColor) {
        super(context);
        this.m_bDrawSideLine = true;
        this.m_TextPaint = getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.m_bDrawSideLine = true;
        this.m_TextPaint = getPaint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        this.mInnerColor = a.getColor(R.styleable.StrokeTextView_innnerColor, ViewCompat.MEASURED_SIZE_MASK);
        this.mOuterColor = a.getColor(R.styleable.StrokeTextView_outerColor, 1275068416);
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle, int outerColor, int innnerColor) {
        super(context, attrs, defStyle);
        this.m_bDrawSideLine = true;
        this.m_TextPaint = getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.m_bDrawSideLine) {
            setTextColorUseReflection(this.mOuterColor);
            this.m_TextPaint.setStrokeWidth(3.0f);
            this.m_TextPaint.setStyle(Paint.Style.STROKE);
            this.m_TextPaint.setFakeBoldText(false);
            super.onDraw(canvas);
            setTextColorUseReflection(this.mInnerColor);
            this.m_TextPaint.setStrokeWidth(0.0f);
            this.m_TextPaint.setStyle(Paint.Style.STROKE);
            this.m_TextPaint.setFakeBoldText(false);
        }
        super.onDraw(canvas);
    }

    private void setTextColorUseReflection(int color) {
        try {
            @SuppressLint("DiscouragedPrivateApi") Field textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        this.m_TextPaint.setColor(color);
    }
}
