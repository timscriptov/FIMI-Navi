package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.fimi.android.app.R;

public class X8MainReturnTimeTextView extends View {

    private static final String TAG = "X8MainReturnTimeTextVie";
    private float fontSize;
    private final Paint mPaint;
    private final Paint mPaintStrock;
    private final Paint mPaintText;
    private String mStrTime;
    private int percent;
    private float photoWidth;

    public X8MainReturnTimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.photoWidth = 0.0f;
        this.fontSize = 0.0f;
        this.mStrTime = "00:00";
        this.percent = 100;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.X8MainReturnTimeText, 0, 0);
        this.photoWidth = typedArray.getDimension(R.styleable.X8MainReturnTimeText_width, 0.0f);
        this.fontSize = typedArray.getDimension(R.styleable.X8MainReturnTimeText_fontSize, 0.0f);
        typedArray.recycle();
        this.mPaint = new Paint();
        this.mPaint.setColor(getResources().getColor(R.color.x8_main_return_time_bg));
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setAntiAlias(true);
        this.mPaintStrock = new Paint();
        this.mPaintStrock.setColor(getResources().getColor(R.color.black_70));
        this.mPaintStrock.setStrokeWidth(1.0f);
        this.mPaintStrock.setStyle(Paint.Style.STROKE);
        this.mPaintStrock.setAntiAlias(true);
        this.mPaint.setAntiAlias(true);
        this.mPaintText = new Paint();
        this.mPaintText.setTextSize(this.fontSize);
        this.mPaintText.setColor(getResources().getColor(R.color.black_70));
        this.mPaintText.setAntiAlias(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int position = (getWidth() * this.percent) / 100;
        if (position + this.photoWidth > getWidth()) {
            position = (int) (getWidth() - this.photoWidth);
        }
        RectF rect = new RectF(position, 0.0f, position + this.photoWidth, getHeight());
        canvas.drawRoundRect(rect, 2.0f, 2.0f, this.mPaint);
        RectF rect1 = new RectF(position, 0.0f, position + this.photoWidth, getHeight() - 1);
        canvas.drawRoundRect(rect1, 2.0f, 2.0f, this.mPaintStrock);
        float textWidth = this.mPaintText.measureText(this.mStrTime);
        float Textx = (position + (this.photoWidth / 2.0f)) - (textWidth / 2.0f);
        Paint.FontMetrics fontMetrics = this.mPaintText.getFontMetrics();
        float dy = ((fontMetrics.descent - fontMetrics.ascent) / 2.0f) - fontMetrics.descent;
        float Texty = (getHeight() / 2) + dy;
        canvas.drawText(this.mStrTime, Textx, Texty, this.mPaintText);
    }

    public void setPercent(int percent) {
        if (this.percent != percent) {
            this.percent = percent;
            invalidate();
        }
    }

    public void setStrTime(String strTime) {
        this.mStrTime = strTime;
        invalidate();
    }
}
