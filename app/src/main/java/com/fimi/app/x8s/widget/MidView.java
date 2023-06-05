package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.AbViewUtil;

import java.util.ArrayList;
import java.util.Iterator;

public class MidView extends View {
    private final int maxValue;
    float centerX;
    float centerY;
    boolean joyOkay;
    Paint paint;
    ArrayList<clipType> type;
    private Bitmap birmapbg;
    private Canvas canvas;
    private boolean clean;
    private float endX;
    private float endY;
    private float margin;
    private float maxLen;
    private float radius;
    private Bitmap ringbg;
    private Bitmap rtBmp;

    public MidView(Context context) {
        super(context);
        this.centerX = 50.0f;
        this.centerY = 50.0f;
        this.radius = 0.0f;
        this.maxValue = 512;
        this.clean = false;
        this.joyOkay = false;
    }

    public MidView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.centerX = 50.0f;
        this.centerY = 50.0f;
        this.radius = 0.0f;
        this.maxValue = 512;
        this.clean = false;
        this.joyOkay = false;
    }

    public MidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.centerX = 50.0f;
        this.centerY = 50.0f;
        this.radius = 0.0f;
        this.maxValue = 512;
        this.clean = false;
        this.joyOkay = false;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MidView);
        array.recycle();
        this.paint = new Paint();
        this.birmapbg = BitmapFactory.decodeResource(getResources(), R.drawable.x8_mid_view_bg);
        this.ringbg = BitmapFactory.decodeResource(getResources(), R.drawable.x8_samll_calibration_icon);
        this.rtBmp = BitmapFactory.decodeResource(getResources(), R.drawable.x8_rc_joy_success);
        this.radius = this.ringbg.getWidth() / 2;
        this.maxLen = AbViewUtil.dip2px(context, 23.5f);
        this.margin = AbViewUtil.dip2px(context, 10.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        this.paint.setStrokeWidth(8.0f);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        if (this.clean) {
            this.clean = false;
            removeAll(canvas);
            recycle(this.birmapbg, this.ringbg);
            canvas.drawBitmap(this.birmapbg, 0.0f, 0.0f, this.paint);
            this.paint.setColor(getResources().getColor(R.color.white_100));
            canvas.drawBitmap(this.ringbg, this.endX - this.radius, this.endY - this.radius, this.paint);
            return;
        }
        if (this.joyOkay) {
            this.joyOkay = false;
            removeAll(canvas);
            recycle(this.birmapbg, this.ringbg);
            canvas.drawBitmap(this.rtBmp, 0.0f, 0.0f, this.paint);
        } else {
            canvas.drawBitmap(this.birmapbg, 0.0f, 0.0f, this.paint);
            this.paint.setColor(getResources().getColor(R.color.white_100));
            canvas.drawLine(this.centerX, this.centerY, this.centerX, this.endY, this.paint);
            canvas.drawLine(this.centerX, this.centerY, this.endX, this.centerY, this.paint);
            clipPath(this.type);
            canvas.drawBitmap(this.ringbg, this.endX - this.radius, this.endY - this.radius, this.paint);
        }
        recycle(this.birmapbg, this.ringbg, this.rtBmp);
    }

    @NonNull
    private void removeAll(Canvas canvas) {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    public void recycle(Bitmap... bitmap) {
        for (Bitmap bmp : bitmap) {
            if (bmp != null && bmp.isRecycled()) {
                bmp.recycle();
            }
        }
    }

    public void setFxFy(float x, float y) {
        if (this.centerX != x || this.centerY != y) {
            this.endX = (float) (this.margin + Math.ceil((this.maxLen * x) / 512.0f));
            this.endY = (float) (this.margin + Math.ceil((this.maxLen * y) / 512.0f));
            invalidate();
        }
    }

    public void setType(ArrayList<clipType> typeArray) {
        this.type = typeArray;
        invalidate();
    }

    private void clipPath(ArrayList<clipType> clips) {
        if (clips != null && clips.size() > 0) {
            this.paint.setColor(getResources().getColor(R.color.x8_value_select));
            Iterator<clipType> it = clips.iterator();
            while (it.hasNext()) {
                clipType mType = it.next();
                if (mType == clipType.left) {
                    float endX = (float) (this.margin + Math.ceil((0.0f * this.maxLen) / 512.0f));
                    this.canvas.drawLine(this.centerX, this.centerY, endX, this.centerY, this.paint);
                }
                if (mType == clipType.top) {
                    float endY = (float) (this.margin + Math.ceil((0.0f * this.maxLen) / 512.0f));
                    this.canvas.drawLine(this.centerX, this.centerY, this.centerX, endY, this.paint);
                }
                if (mType == clipType.right) {
                    float endX2 = (float) (this.margin + Math.ceil((1024.0f * this.maxLen) / 512.0f));
                    this.canvas.drawLine(this.centerX, this.centerY, endX2, this.centerY, this.paint);
                }
                if (mType == clipType.bottom) {
                    float endY2 = (float) (this.margin + Math.ceil((1024.0f * this.maxLen) / 512.0f));
                    this.canvas.drawLine(this.centerX, this.centerY, this.centerX, endY2, this.paint);
                }
            }
        }
    }

    public void releaseAll() {
        this.endX = this.centerX;
        this.endY = this.centerY;
        this.joyOkay = false;
        this.clean = true;
        postInvalidate();
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = wSpecSize;
        int resultHeight = hSpecSize;
        if (wSpecMode == Integer.MIN_VALUE && hSpecMode == Integer.MIN_VALUE) {
            resultWidth = this.birmapbg.getWidth();
            resultHeight = this.birmapbg.getHeight();
        } else if (wSpecMode == Integer.MIN_VALUE) {
            resultWidth = this.birmapbg.getWidth();
            resultHeight = hSpecSize;
        } else if (hSpecMode == Integer.MIN_VALUE) {
            resultWidth = wSpecSize;
            resultHeight = this.birmapbg.getHeight();
        }
        int resultWidth2 = Math.min(resultWidth, wSpecSize);
        int resultHeight2 = Math.min(resultHeight, hSpecSize);
        this.centerX = this.birmapbg.getWidth() / 2;
        this.centerY = this.birmapbg.getHeight() / 2;
        this.endX = this.centerX;
        this.endY = this.centerY;
        setMeasuredDimension(resultWidth2, resultHeight2);
    }

    public void joyFinish() {
        this.joyOkay = true;
        invalidate();
    }

    /* loaded from: classes.dex */
    public enum clipType {
        left,
        top,
        right,
        bottom
    }
}
