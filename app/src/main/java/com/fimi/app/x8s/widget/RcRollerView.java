package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.fimi.android.app.R;

public class RcRollerView extends View {
    private final int leftDown;
    private final int leftUp;
    private final int maxValue;
    private Bitmap bitmap;
    private int bottom;
    private Paint paint;
    private int right;
    private int rollerType;
    private int totalH;
    private int totalW;
    private PorterDuffXfermode xfermode;

    public RcRollerView(Context context) {
        super(context);
        this.leftUp = 0;
        this.leftDown = 1;
        this.rollerType = 0;
        this.maxValue = 512;
        init();
    }

    public RcRollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.leftUp = 0;
        this.leftDown = 1;
        this.rollerType = 0;
        this.maxValue = 512;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RcRoller);
        this.rollerType = array.getInteger(R.styleable.RcRoller_viewType, 0);
        int rollerSrc = array.getResourceId(R.styleable.RcRoller_rollerSrc, 0);
        this.bitmap = BitmapFactory.decodeResource(getResources(), rollerSrc);
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setDither(true);
        this.paint.setFilterBitmap(true);
        this.xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int sc = canvas.saveLayer(0.0f, 0.0f, this.totalW, this.totalH, this.paint, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, null);
        this.paint.setXfermode(this.xfermode);
        this.paint.setColor(-1);
        if (this.rollerType == 1) {
            RectF rectF = new RectF(0.0f, 0.0f, this.bitmap.getWidth(), this.bottom);
            canvas.rotate(35.0f);
            canvas.drawRect(rectF, this.paint);
        } else {
            RectF rectF2 = new RectF(0.0f, 0.0f, this.right, this.bottom);
            canvas.drawRect(rectF2, this.paint);
        }
        this.paint.setXfermode(null);
        canvas.restoreToCount(sc);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.totalW = w;
        this.totalH = h;
    }

    public void upRollerValue(int rollerValue) {
        if (this.rollerType == 1) {
            if (rollerValue <= 5) {
                this.bottom = 0;
                postInvalidate();
            }
            if (this.bottom <= this.bitmap.getHeight()) {
                this.right = this.bitmap.getWidth();
                this.bottom += (this.bitmap.getHeight() * rollerValue) / 512;
                postInvalidate();
            }
        } else if (this.rollerType == 0) {
            if (rollerValue <= 5) {
                this.right = 0;
                postInvalidate();
            }
            if (this.right <= this.bitmap.getWidth()) {
                this.bottom = this.bitmap.getHeight();
                this.right += (this.bitmap.getWidth() * rollerValue) / 512;
                postInvalidate();
            }
        }
    }

    public void clean() {
        this.right = 0;
        this.bottom = 0;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = wSpecSize;
        int resultHeight = hSpecSize;
        if (wSpecMode == Integer.MIN_VALUE && hSpecMode == Integer.MIN_VALUE) {
            resultWidth = this.bitmap.getWidth();
            resultHeight = this.bitmap.getHeight();
        } else if (wSpecMode == Integer.MIN_VALUE) {
            resultWidth = this.bitmap.getWidth();
            resultHeight = hSpecSize;
        } else if (hSpecMode == Integer.MIN_VALUE) {
            resultWidth = wSpecSize;
            resultHeight = this.bitmap.getHeight();
        }
        setMeasuredDimension(resultWidth, resultHeight);
    }
}
