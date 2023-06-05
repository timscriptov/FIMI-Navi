package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.fimi.android.app.R;

public class X8MainPowerView extends View {
    private static final String TAG = "X8PowerView";
    Bitmap mBitmap;
    private int mBpEmptySource;
    private Paint mPaint;
    private int percent;

    public X8MainPowerView(Context context) {
        super(context);
        this.mBpEmptySource = 0;
        this.percent = 67;
    }

    public X8MainPowerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mBpEmptySource = 0;
        this.percent = 67;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.X8MainPower, 0, 0);
        this.mBpEmptySource = typedArray.getResourceId(R.styleable.X8MainPower_image, 0);
        if (this.mBpEmptySource != 0) {
            this.mBitmap = BitmapFactory.decodeResource(getResources(), this.mBpEmptySource);
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.GREEN);
    }

    public X8MainPowerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mBpEmptySource = 0;
        this.percent = 67;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, this.mBitmap.getHeight());
    }

    @Override
    @SuppressLint({"WrongConstant"})
    protected void onDraw(Canvas canvas) {
        float src;
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT <= 19) {
            this.mPaint.setColor(Color.argb(1, 0, 0, 0));
        }
        canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), null, 31);
        Rect dst = new Rect();
        dst.left = 0;
        dst.top = 0;
        dst.right = getWidth();
        dst.bottom = getHeight();
        canvas.drawBitmap(this.mBitmap, null, dst, null);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        if (this.percent == 0) {
            src = 1.0f;
        } else {
            src = (100.0f - (((this.percent / 100.0f) * 85.0f) + 15.0f)) / 100.0f;
        }
        canvas.drawRect(new Rect((int) (getWidth() - (getWidth() * src)), 0, getWidth(), getHeight()), this.mPaint);
        this.mPaint.setXfermode(null);
    }

    public void setPercent(int percent) {
        if (this.percent != percent) {
            this.percent = percent;
            invalidate();
        }
    }
}
