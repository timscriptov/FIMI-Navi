package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.DensityUtil;

import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class X8SeekBarView extends View {
    private int bgColor;
    private float cicleR;
    private float cicleX;
    private float cicleY;
    private float downX;
    private float downY;
    private final float dpLineH;
    private final float dpMaginW;
    private final float dpR;
    private final float dpThumpW;
    private final float dph;
    private float endX;
    private float endY;
    private int h;
    private boolean isInnerClick;
    private int lineH;
    private SlideChangeListener listener;
    private float locationX;
    private Paint mPaint;
    private float maginW;
    private int maxProgress;
    private int progress;
    private int progressColor;
    private float r;
    private float startX;
    private float startY;
    private int w;

    public X8SeekBarView(Context context) {
        super(context);
        this.maxProgress = 100;
        this.w = 0;
        this.h = 0;
        this.dph = 40.0f;
        this.dpMaginW = 30.0f;
        this.dpThumpW = 15.33f;
        this.dpLineH = 1.33f;
        this.dpR = 0.667f;
        this.r = 1.33f;
    }

    public X8SeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.maxProgress = 100;
        this.w = 0;
        this.h = 0;
        this.dph = 40.0f;
        this.dpMaginW = 30.0f;
        this.dpThumpW = 15.33f;
        this.dpLineH = 1.33f;
        this.dpR = 0.667f;
        this.r = 1.33f;
        init();
    }

    public X8SeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.maxProgress = 100;
        this.w = 0;
        this.h = 0;
        this.dph = 40.0f;
        this.dpMaginW = 30.0f;
        this.dpThumpW = 15.33f;
        this.dpLineH = 1.33f;
        this.dpR = 0.667f;
        this.r = 1.33f;
        init();
    }

    public static int getDefaultSize(int size, int measureSpec) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
            case NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE /* 1073741824 */:
                return specSize;
            case 0:
                return size;
            default:
                return size;
        }
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.listener != null) {
            this.listener.onProgress(this, progress);
        }
        invalidate();
    }

    public void init() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.bgColor = getContext().getResources().getColor(R.color.white_15);
        this.progressColor = getContext().getResources().getColor(R.color.white_60);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (this.w == 0) {
            this.w = getMeasuredWidth();
        }
        if (this.h != 0) {
            this.mPaint.setColor(this.bgColor);
            this.startX = this.maginW / 2.0f;
            this.endX = this.w - (this.maginW / 2.0f);
            this.startY = (this.h / 2.0f) - (this.lineH / 2.0f);
            this.endY = (this.h / 2.0f) + (this.lineH / 2.0f);
            RectF rf = new RectF(this.startX, this.startY, this.endX, this.endY);
            canvas.drawRoundRect(rf, this.r, this.r, this.mPaint);
            float x = this.startX + (((this.w - this.maginW) * this.progress) / this.maxProgress);
            this.cicleX = x;
            this.cicleY = (int) (this.h / 2.0f);
            this.mPaint.setColor(this.progressColor);
            RectF rf1 = new RectF(this.startX, this.startY, this.cicleX, this.endY);
            canvas.drawRoundRect(rf1, this.r, this.r, this.mPaint);
            this.mPaint.setColor(-1);
            canvas.drawCircle(this.cicleX, this.cicleY, this.cicleR, this.mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.h = DensityUtil.dip2px(getContext(), this.dph);
        this.maginW = DensityUtil.dip2px(getContext(), this.dpMaginW);
        this.lineH = DensityUtil.dip2px(getContext(), this.dpLineH);
        this.r = DensityUtil.dip2px(getContext(), this.dpR);
        this.cicleR = DensityUtil.dip2px(getContext(), this.dpThumpW) / 2.0f;
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), this.h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case 0:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    this.isInnerClick = isInnerMthum(event);
                    if (this.isInnerClick && this.listener != null) {
                        this.listener.onStart(this, this.progress);
                    }
                    this.downX = event.getX();
                    this.downY = event.getY();
                    break;
                case 1:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    if (this.isInnerClick && this.listener != null) {
                        this.listener.onStop(this, this.progress);
                        break;
                    }
                    break;
                case 2:
                    if (this.isInnerClick) {
                        this.locationX = event.getX();
                        fixLocationX();
                        float p = ((this.locationX - this.startX) / (this.w - this.maginW)) * this.maxProgress;
                        this.progress = Math.round(p);
                        if (this.listener != null) {
                            this.listener.onProgress(this, this.progress);
                        }
                        this.downY = event.getY();
                        this.downX = event.getX();
                        invalidate();
                        break;
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    private boolean isInnerMthum(@NonNull MotionEvent event) {
        return !(event.getX() < 0.0f) && !(event.getX() > this.w) && !(event.getY() < 0.0f) && !(event.getY() > this.h);
    }

    private void fixLocationX() {
        if (this.locationX < this.startX) {
            this.locationX = this.startX;
        } else if (this.locationX > this.endX) {
            this.locationX = this.endX;
        }
    }

    public void setOnSlideChangeListener(SlideChangeListener l) {
        this.listener = l;
    }

    public interface SlideChangeListener {
        void onProgress(X8SeekBarView x8SeekBarView, int i);

        void onStart(X8SeekBarView x8SeekBarView, int i);

        void onStop(X8SeekBarView x8SeekBarView, int i);
    }
}
