package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.AbViewUtil;

public class X8MainTopReturnTimeView extends View {
    private static final String TAG = "X8TopReturnView";
    private int SPACING;
    private Bitmap mBpEmpty;
    private int mBpEmptySource;
    private Bitmap mBpFull;
    private int mBpFullSource;
    private Bitmap mBpMiddle;
    private int mBpMiddleSource;
    private Paint mPaint;
    private int percent;

    public X8MainTopReturnTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mBpEmptySource = 0;
        this.mBpMiddleSource = 0;
        this.mBpFullSource = 0;
        this.percent = 0;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.X8MainTopReturnTime, 0, 0);
        this.mBpEmptySource = typedArray.getResourceId(R.styleable.X8MainTopReturnTime_empty_image, 0);
        this.mBpMiddleSource = typedArray.getResourceId(R.styleable.X8MainTopReturnTime_middle_image, 0);
        this.mBpFullSource = typedArray.getResourceId(R.styleable.X8MainTopReturnTime_full_image, 0);
        if (this.mBpEmptySource != 0) {
            this.mBpEmpty = BitmapFactory.decodeResource(getResources(), this.mBpEmptySource);
        }
        if (this.mBpMiddleSource != 0) {
            this.mBpMiddle = BitmapFactory.decodeResource(getResources(), this.mBpMiddleSource);
        }
        if (this.mBpFullSource != 0) {
            this.mBpFull = BitmapFactory.decodeResource(getResources(), this.mBpFullSource);
        }
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(getResources().getColor(android.R.color.transparent));
        this.SPACING = (int) AbViewUtil.dip2px(context, 2.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mBpEmpty != null) {
        }
        canvas.drawBitmap(this.mBpEmpty, 0.0f, 0.0f, (Paint) null);
        if (this.percent > 0) {
            drawPercent(canvas, this.percent);
        }
    }

    public void drawPercent(@NonNull Canvas canvas, int percent) {
        canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        if (percent > 100) {
            percent = 100;
        }
        Path path = new Path();
        path.moveTo((getWidth() - ((getWidth() * (100 - percent)) / 100)) + this.SPACING, 0.0f);
        path.lineTo(getWidth() - ((getWidth() * (100 - percent)) / 100), getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), 0.0f);
        path.close();
        if (percent > 50) {
            canvas.drawBitmap(this.mBpFull, 0.0f, 0.0f, (Paint) null);
        } else {
            canvas.drawBitmap(this.mBpMiddle, 0.0f, 0.0f, (Paint) null);
        }
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawPath(path, this.mPaint);
        this.mPaint.setXfermode(null);
        canvas.restore();
    }

    public void setPercent(int percent) {
        if (this.percent != percent) {
            this.percent = percent;
            invalidate();
        }
    }
}
