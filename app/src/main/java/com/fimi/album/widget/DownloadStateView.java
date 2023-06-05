package com.fimi.album.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.fimi.android.app.R;


public class DownloadStateView extends View {
    private static final int DOWNLOADING_COLOR = Color.parseColor("#38bbff");
    private static final int DOWNLOAD_FAIL_COLOR = Color.parseColor("#f23206");
    private Paint mPaint;
    private State mState;
    private int sweepAngle;

    public DownloadStateView(Context context) {
        super(context);
        this.sweepAngle = 0;
        this.mState = State.PAUSE;
        initView();
    }

    public DownloadStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.sweepAngle = 0;
        this.mState = State.PAUSE;
        initView();
    }

    public DownloadStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.sweepAngle = 0;
        this.mState = State.PAUSE;
        initView();
    }

    private void initView() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(2.0f);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(DOWNLOADING_COLOR);
        setBackgroundResource(R.drawable.album_btn_media_pause);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(2.0f, 2.0f, getWidth() - 2, getWidth() - 2);
        if (this.mState == State.PAUSE) {
            setBackgroundResource(R.drawable.album_btn_media_pause);
            canvas.drawArc(rectF, -90.0f, this.sweepAngle, false, this.mPaint);
        } else if (this.mState == State.DOWNLOADING) {
            setBackgroundResource(R.drawable.album_btn_media_download);
            this.mPaint.setColor(DOWNLOADING_COLOR);
            canvas.drawArc(rectF, -90.0f, this.sweepAngle, false, this.mPaint);
        } else if (this.mState == State.DOWNLOAD_FAIL) {
            setBackgroundResource(R.drawable.album_btn_media_redownload);
            this.mPaint.setColor(DOWNLOAD_FAIL_COLOR);
            canvas.drawArc(rectF, -90.0f, this.sweepAngle, false, this.mPaint);
        }
    }

    public void setProgress(int progress) {
        if (this.sweepAngle != Math.round(progress * 3.6f)) {
            this.sweepAngle = Math.round(progress * 3.6f);
            invalidate();
        }
    }

    public State getState() {
        return this.mState;
    }

    public void setState(State state) {
        if (this.mState != state) {
            this.mState = state;
            invalidate();
        }
    }


    public enum State {
        PAUSE,
        DOWNLOADING,
        DOWNLOAD_FAIL
    }
}
