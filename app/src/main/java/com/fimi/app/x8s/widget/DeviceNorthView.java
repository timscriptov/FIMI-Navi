package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;

/**
 * После компиляции замени в smali класс полностью
 */
public class DeviceNorthView extends View {
    public final Context mContext;
    private int a;
    private int mMin;
    private int mMax;
    private float mDensity;
    private final RectF e;
    private Paint mCircle;
    private float north;
    private int h;
    private int i;
    private float j;
    private Drawable drawable;
    private int l;
    private int m;
    private float radianAngle;

    public DeviceNorthView(Context context) {
        super(context);
        mContext = context;
        this.a = 0;
        this.mMin = 0;
        this.mMax = 100;
        this.mDensity = 1.0f;
        this.e = new RectF();
        a(context, null);
    }

    public DeviceNorthView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        this.a = 0;
        this.mMin = 0;
        this.mMax = 100;
        this.mDensity = 1.0f;
        this.e = new RectF();
        a(context, attributeSet);
    }

    private void a(@NonNull Context context, AttributeSet attributeSet) {
        float f = context.getResources().getDisplayMetrics().density;
        int color = getResources().getColor(R.color.x8s_main_north);
        this.mDensity = f * this.mDensity;
        this.drawable = getResources().getDrawable(R.drawable.x8s_main_north);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DrawableStyleable, 0, 0);
            this.l = obtainStyledAttributes.getDimensionPixelSize(3, 50);
            this.mMin = obtainStyledAttributes.getInteger(1, this.mMin);
            this.mMax = obtainStyledAttributes.getInteger(0, this.mMax);
            this.m = (((((getPaddingLeft() + getPaddingRight()) + getPaddingBottom()) + getPaddingTop()) + getPaddingEnd()) + getPaddingStart()) / 6;
            obtainStyledAttributes.recycle();
        }
        int i = this.a;
        int i2 = this.mMax;
        if (i > i2) {
            i = i2;
        }
        this.a = i;
        int i3 = this.a;
        int i4 = this.mMin;
        if (i3 < i4) {
            i3 = i4;
        }
        this.a = i3;
        this.north = this.a / a();
        this.radianAngle = (float) (1.5707963267948966d - ((this.north * 3.141592653589793d) / 180.0d));
        this.mCircle = new Paint();
        this.mCircle.setColor(color);
        this.mCircle.setAntiAlias(true);
        this.mCircle.setStyle(Paint.Style.STROKE);
        this.mCircle.setStrokeWidth(this.mDensity);
    }

    public int getMax() {
        return this.mMax;
    }

    public int getMin() {
        return this.mMin;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.drawCircle(this.h, this.i, this.j, this.mCircle);
        if (this.drawable instanceof RotateDrawable) {
            int cos = (int) (this.h + (this.j * Math.cos(this.radianAngle)));
            int sin = (int) (this.i - (this.j * Math.sin(this.radianAngle)));
            Drawable drawable = this.drawable;
            int i = this.l;
            drawable.setBounds(cos - (i / 2), sin - (i / 2), cos + (i / 2), sin + (i / 2));
            this.drawable.draw(canvas);
        }
    }

    public void setNorthAngle(float f) {
        float f2 = (-f) + 180.0f + 90.0f;
        if (f2 < 0.0f) {
            f2 += 360.0f;
        }
        this.a = (int) ((f2 / 1.2d) / 3.0d);
        int i = this.a;
        int i2 = this.mMax;
        if (i > i2) {
            i = i2;
        }
        this.a = i;
        int i3 = this.a;
        int i4 = this.mMin;
        if (i3 < i4) {
            i3 = i4;
        }
        this.a = i3;
        final float n = this.a / a();
        this.north = n;
        this.radianAngle = (float) (Math.PI / 2 - ((this.north * Math.PI) / 180.0d));
        invalidate();
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        int min = Math.min(i, i2);
        int i5 = ((i - min) / 2) + min;
        int i6 = ((i2 - min) / 2) + min;
        this.h = (i5 / 2) + ((i - i5) / 2);
        this.i = (i6 / 2) + ((i2 - i6) / 2);
        float f = min - this.m;
        this.j = (float) (f / 2.05d);
        float f2 = f / 2.0f;
        float f3 = (i2 >> 1) - f2;
        float f4 = (i >> 1) - f2;
        this.e.set(f4, f3, f4 + f, f + f3);
        super.onSizeChanged(i, i2, i3, i4);
    }

    private float a() {
        return this.mMax / 360.0f;
    }
}
