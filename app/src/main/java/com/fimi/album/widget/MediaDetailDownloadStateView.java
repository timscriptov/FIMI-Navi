package com.fimi.album.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.AbViewUtil;
import com.fimi.kernel.utils.FontUtil;


public class MediaDetailDownloadStateView extends View {
    private static final int DOWNLOADING_COLOR = Color.parseColor("#38bbff");
    private static final int DOWNLOAD_FAIL_COLOR = Color.parseColor("#f23206");
    private static final int TEXT_COLOR = Color.parseColor("#ffffffff");
    private final Context mContext;
    private Paint mPaint;
    private DownloadStateView.State mState;
    private Paint mTextPaint;
    private int sweepAngle;

    public MediaDetailDownloadStateView(Context context) {
        super(context);
        this.sweepAngle = 0;
        this.mState = DownloadStateView.State.PAUSE;
        this.mContext = context;
        initView();
    }

    public MediaDetailDownloadStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.sweepAngle = 0;
        this.mState = DownloadStateView.State.PAUSE;
        this.mContext = context;
        initView();
    }

    public MediaDetailDownloadStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.sweepAngle = 0;
        this.mState = DownloadStateView.State.PAUSE;
        this.mContext = context;
        initView();
    }

    private void initView() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(2.0f);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(DOWNLOADING_COLOR);
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(AbViewUtil.dip2px(this.mContext, 12.0f));
        this.mTextPaint.setColor(TEXT_COLOR);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        this.mTextPaint.setTypeface(FontUtil.getDINAlernateBold(this.mContext.getAssets()));
        setBackgroundResource(R.drawable.album_btn_media_pause);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(2.0f, 2.0f, getWidth() - 2, getWidth() - 2);
        if (this.mState == DownloadStateView.State.PAUSE) {
            setBackgroundResource(R.drawable.album_btn_media_pause);
            canvas.drawArc(rectF, -90.0f, this.sweepAngle, false, this.mPaint);
        } else if (this.mState == DownloadStateView.State.DOWNLOADING) {
            String text = Math.round(this.sweepAngle / 3.6f) + "%";
            Paint.FontMetrics fontMetrics = this.mTextPaint.getFontMetrics();
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            float textBaseY = (getHeight() - ((getHeight() - fontHeight) / 2.0f)) - fontMetrics.bottom;
            canvas.drawText(text, getWidth() / 2, textBaseY, this.mTextPaint);
            setBackgroundResource(R.drawable.album_btn_media_detail_download);
            this.mPaint.setColor(DOWNLOADING_COLOR);
            canvas.drawArc(rectF, -90.0f, this.sweepAngle, false, this.mPaint);
        } else if (this.mState == DownloadStateView.State.DOWNLOAD_FAIL) {
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

    public DownloadStateView.State getState() {
        return this.mState;
    }

    public void setState(DownloadStateView.State state) {
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
