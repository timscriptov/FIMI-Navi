package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.fimi.android.app.R;
import com.fimi.app.x8s.tools.X8NumberUtil;

public class X8MainPitchingAngle extends View {
    private static final String TAG = "X8MainPitchingAngle";
    private final int mProjectionColor;
    private int MAX;
    private int MAXPROGRESS;
    private int MIN;
    private OnProgressListener listener;
    private int mBgColor;
    private Rect mBounds;
    private Paint mPaint;
    private float mPercent;
    private int mProgressColor;
    private float mProgressMarginLeft;
    private float mProgressWidth;
    private float mRadius;
    private Paint mTextBgPaint;
    private Paint mTextPaint;
    private int rectangleColor;
    private float rectangleHeight;
    private float rectangleLeft;
    private float rectangleWidth;
    private int textColor;
    private float textSize;

    public X8MainPitchingAngle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mPercent = 0.0f;
        this.mBounds = new Rect();
        this.MIN = 5;
        this.MAX = 120;
        this.MAXPROGRESS = this.MAX - this.MIN;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.X8MainPitching, 0, 0);
        this.mBgColor = typedArray.getColor(R.styleable.X8MainPitching_bg_color, ViewCompat.MEASURED_STATE_MASK);
        this.mProjectionColor = typedArray.getColor(R.styleable.X8MainPitching_progress_projection_color, ViewCompat.MEASURED_STATE_MASK);
        this.mProgressColor = typedArray.getColor(R.styleable.X8MainPitching_progress_color, ViewCompat.MEASURED_STATE_MASK);
        this.mProgressWidth = typedArray.getDimension(R.styleable.X8MainPitching_progress_bar_width, 0.0f);
        this.mProgressMarginLeft = typedArray.getDimension(R.styleable.X8MainPitching_progress_margin_left, 0.0f);
        this.mRadius = typedArray.getDimension(R.styleable.X8MainPitching_radius, 0.0f);
        this.rectangleLeft = typedArray.getDimension(R.styleable.X8MainPitching_rectangle_left, 0.0f);
        this.rectangleWidth = typedArray.getDimension(R.styleable.X8MainPitching_rectangle_width, 0.0f);
        this.rectangleHeight = typedArray.getDimension(R.styleable.X8MainPitching_rectangle_height, 0.0f);
        this.rectangleColor = typedArray.getColor(R.styleable.X8MainPitching_rectangle_color, 0);
        this.textSize = typedArray.getDimension(R.styleable.X8MainPitching_text_size, 0.0f);
        this.textColor = typedArray.getColor(R.styleable.X8MainPitching_text_color, 0);
        this.mPaint = new Paint();
        this.mPaint.setColor(this.mBgColor);
        this.mPaint.setAntiAlias(true);
        typedArray.recycle();
        this.mTextBgPaint = new Paint();
        this.mTextBgPaint.setAntiAlias(true);
        this.mTextBgPaint.setColor(this.rectangleColor);
        this.mTextPaint = new Paint();
        this.mTextPaint.setTextSize(this.textSize);
        this.mTextPaint.setColor(this.textColor);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setOnProgressListener(OnProgressListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        drawText(canvas);
    }

    private void drawProgress(@NonNull Canvas canvas) {
        float left = (this.mRadius - this.mProgressWidth) + this.mProgressMarginLeft;
        this.mPaint.setColor(this.mBgColor);
        this.mPaint.setShadowLayer(5.0f, 0.0f, 0.0f, this.mProjectionColor);
        Log.i(TAG, "drawProgress: " + left + "," + this.mProgressWidth);
        RectF rectF = new RectF(left / 2.0f, 10.0f, (left / 2.0f) + this.mProgressWidth, getHeight() - 10);
        canvas.drawRoundRect(rectF, 3.0f, 3.0f, this.mPaint);
        this.mPaint.setColor(this.mProgressColor);
        this.mPaint.setColor(this.mProgressColor);
        int circularY = (int) ((getHeight() - (((getHeight() - this.mRadius) * this.mPercent) / 100.0f)) - (this.mRadius / 2.0f));
        RectF rectFProgress = new RectF(left / 2.0f, circularY, (left / 2.0f) + this.mProgressWidth, getHeight() - 10);
        this.mPaint.clearShadowLayer();
        canvas.drawRoundRect(rectFProgress, 3.0f, 3.0f, this.mPaint);
        this.mPaint.setShadowLayer(5.0f, 0.0f, 0.0f, this.mProjectionColor);
        canvas.drawCircle((this.mRadius / 2.0f) + (this.mProgressMarginLeft / 2.0f), circularY, this.mRadius / 2.0f, this.mPaint);
    }

    private void drawText(@NonNull Canvas canvas) {
        float heightValue = getRegulationProgress();
        Paint.FontMetrics fontMetrics = this.mTextPaint.getFontMetrics();
        float dy = ((fontMetrics.descent - fontMetrics.ascent) / 2.0f) - fontMetrics.descent;
        int intHeightValue = (int) heightValue;
        String text = X8NumberUtil.getDistanceNumberString(intHeightValue, 0, true);
        this.mTextPaint.getTextBounds(text, (int) this.rectangleLeft, text.length(), this.mBounds);
        float textWidth = this.mBounds.width();
        float textHeight = this.mBounds.height();
        RectF rectF = new RectF();
        rectF.left = this.rectangleLeft;
        rectF.top = ((getHeight() / 2) - (textHeight / 2.0f)) - dy;
        rectF.right = this.rectangleLeft + textWidth + (2.0f * dy);
        rectF.bottom = (getHeight() / 2) + (textHeight / 2.0f) + dy;
        canvas.drawRoundRect(rectF, 3.0f, 3.0f, this.mTextBgPaint);
        canvas.drawText(text, this.rectangleLeft, (getHeight() / 2) + (textHeight / 2.0f), this.mTextPaint);
        if (this.listener != null) {
            this.listener.onProgressChanage(intHeightValue);
        }
    }

    public void setPercent(float percent) {
        if (this.mPercent != percent) {
            this.mPercent = percent;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case 0:
            case 1:
            default:
                return true;
            case 2:
                calculProgress(event.getY());
                return true;
        }
    }

    private void calculProgress(float y) {
        float progress = 0.0f;
        if (y > 0.0f) {
            if (y >= getHeight()) {
                progress = 100.0f;
            } else {
                int h = getHeight();
                progress = (y / h) * 100.0f;
            }
        }
        setPercent(Math.round((100.0f - progress) * 10.0f) / 10.0f);
    }

    public float getRegulationProgress() {
        float heightValue = ((this.mPercent * this.MAXPROGRESS) / 100.0f) + this.MIN;
        return Math.round(heightValue * 10.0f) / 10.0f;
    }

    public void setProcess(float y) {
        float progress = ((y - this.MIN) * 100.0f) / 115.0f;
        setPercent(Math.round(progress * 10.0f) / 10.0f);
    }

    public interface OnProgressListener {
        void onProgressChanage(float f);
    }
}
