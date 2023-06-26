package com.fimi.libperson.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.fimi.kernel.utils.AbViewUtil;


public class LargeView extends View {
    private static final String TAG = "LargeView";
    private static final int sHeight = 1920;
    private static final int sWidth = 1080;
    private final Paint bitmapPaint;
    private final long duration;
    private final Matrix matrix;
    private final PointF vTranslate;
    private Bitmap bitmap;
    private boolean bitmapIsCached;
    private boolean isFirst;
    private boolean isUp;
    private boolean mReady;
    private float scale;
    private long startTime;

    public LargeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mReady = false;
        this.isUp = true;
        this.duration = 20000L;
        this.isFirst = true;
        this.vTranslate = new PointF();
        this.bitmapPaint = new Paint();
        this.bitmapPaint.setAntiAlias(true);
        this.bitmapPaint.setFilterBitmap(true);
        this.bitmapPaint.setDither(true);
        this.matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.matrix.reset();
        if (this.mReady) {
            this.scale = AbViewUtil.getScreenWidth(getContext()) / (this.bitmap.getWidth() * 1.0f);
            Log.i(TAG, "onDraw: " + this.bitmap.getHeight() + "," + this.bitmap.getWidth() + "," + AbViewUtil.getScreenWidth(getContext()) + "," + AbViewUtil.getScreenHeight(getContext()));
            if (this.isFirst) {
                this.startTime = System.currentTimeMillis();
                this.isFirst = false;
            }
            long scaleElapsed = System.currentTimeMillis() - this.startTime;
            boolean finished = scaleElapsed > this.duration;
            float percent = ((float) scaleElapsed) / (((float) this.duration));
            if (!this.isUp) {
                percent = 1.0f - percent;
            }
            this.vTranslate.y = (-percent) * ((this.bitmap.getHeight() * this.scale) - AbViewUtil.getScreenHeight(getContext()));
            Log.i(TAG, "onDraw: " + this.vTranslate.y + ",scale:" + this.scale + ",percent:" + percent);
            if (finished) {
                this.startTime = System.currentTimeMillis();
                this.isUp = !this.isUp;
            }
            invalidate();
        }
        this.matrix.setScale(this.scale, this.scale);
        this.matrix.postTranslate(0.0f, this.vTranslate.y);
        if (this.bitmap != null) {
            canvas.drawBitmap(this.bitmap, this.matrix, this.bitmapPaint);
        }
    }

    public boolean isReady() {
        return this.mReady;
    }

    public void setReady(boolean ready) {
        this.mReady = ready;
        if (this.mReady) {
            this.startTime = System.currentTimeMillis();
            invalidate();
        }
    }

    public void setRecyle() {
        this.mReady = false;
        if (this.bitmap != null && !this.bitmap.isRecycled()) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
    }

    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (bitmap != null) {
            this.startTime = System.currentTimeMillis();
            this.mReady = true;
            invalidate();
        }
    }
}
