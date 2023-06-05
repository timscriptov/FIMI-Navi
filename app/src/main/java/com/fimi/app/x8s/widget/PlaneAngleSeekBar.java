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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.fimi.android.app.R;

public class PlaneAngleSeekBar extends View {
    Drawable k;
    private int a;
    private int b;
    private int c;
    private float d;
    private final RectF e;
    private Paint f;
    private float g;
    private int h;
    private int i;
    private float j;
    private int l;
    private int m;
    private double n;
    private float o;
    private float p;

    public PlaneAngleSeekBar(Context context) {
        super(context);
        this.a = 0;
        this.b = 0;
        this.c = 100;
        this.d = 1.0f;
        this.e = new RectF();
        a(context, null);
    }

    public PlaneAngleSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.a = 0;
        this.b = 0;
        this.c = 100;
        this.d = 1.0f;
        this.e = new RectF();
        a(context, attributeSet);
    }

    private void a(Context context, AttributeSet attributeSet) {
        float f2 = context.getResources().getDisplayMetrics().density;
        int color = ContextCompat.getColor(context, R.color.x8s_main_seek_bar);
        this.d = f2 * this.d;
        this.k = ContextCompat.getDrawable(context, R.drawable.x8s_main_plane_angle);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DrawableStyleable, 0, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.DrawableStyleable_csb_thumbDrawable);
            if (drawable != null) {
                this.k = drawable;
            }
            this.l = obtainStyledAttributes.getDimensionPixelSize(R.styleable.DrawableStyleable_csb_thumbSize, 50);
            this.b = obtainStyledAttributes.getInteger(R.styleable.DrawableStyleable_csb_min, this.b);
            this.c = obtainStyledAttributes.getInteger(R.styleable.DrawableStyleable_csb_max, this.c);
            this.m = (((((getPaddingLeft() + getPaddingRight()) + getPaddingBottom()) + getPaddingTop()) + getPaddingEnd()) + getPaddingStart()) / 6;
            obtainStyledAttributes.recycle();
        }
        int i2 = this.a;
        int i3 = this.c;
        if (i2 > i3) {
            i2 = i3;
        }
        this.a = i2;
        int i4 = this.a;
        int i5 = this.b;
        if (i4 < i5) {
            i4 = i5;
        }
        this.a = i4;
        this.g = this.a / a();
        this.n = 1.5707963267948966d - ((this.g * 3.141592653589793d) / 180.0d);
        this.f = new Paint();
        this.f.setColor(color);
        this.f.setAntiAlias(true);
        this.f.setStyle(Paint.Style.STROKE);
        this.f.setStrokeWidth(this.d);
    }

    public double getAngle() {
        return this.n;
    }

    public int getMax() {
        return this.c;
    }

    public int getMin() {
        return this.b;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.drawCircle(this.h, this.i, this.j, this.f);
        canvas.rotate(this.o, getWidth() / 2, getHeight() / 2);
        int cos = (int) (this.h + (this.j * Math.cos(this.n)));
        int sin = (int) (this.i - (this.j * Math.sin(this.n)));
        Drawable drawable = this.k;
        int i2 = this.l;
        drawable.setBounds(cos - (i2 / 2), sin - (i2 / 2), cos + (i2 / 2), sin + (i2 / 2));
        Drawable drawable2 = this.k;
        if (drawable2 instanceof RotateDrawable) {
            drawable2.setLevel((int) this.p);
        }
        this.k.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int i2, int i3, int i4, int i5) {
        int min = Math.min(i2, i3);
        int i6 = ((i2 - min) / 2) + min;
        int i7 = ((i3 - min) / 2) + min;
        this.h = (i6 / 2) + ((i2 - i6) / 2);
        this.i = (i7 / 2) + ((i3 - i7) / 2);
        float f2 = min - this.m;
        this.j = (float) (f2 / 2.05d);
        float f3 = f2 / 2.0f;
        float f4 = (i3 >> 1) - f3;
        float f5 = (i2 >> 1) - f3;
        this.e.set(f5, f4, f5 + f2, f2 + f4);
        super.onSizeChanged(i2, i3, i4, i5);
    }

    public void setHeadOrientation(int i2) {
        this.p = i2 / 0.036f;
        invalidate();
    }

    public void setPlaneOrientation(int i2) {
        this.a = (int) ((i2 / 1.2d) / 3.0d);
        int i3 = this.a;
        int i4 = this.c;
        if (i3 > i4) {
            i3 = i4;
        }
        this.a = i3;
        int i5 = this.a;
        int i6 = this.b;
        if (i5 < i6) {
            i5 = i6;
        }
        this.a = i5;
        this.g = this.a / a();
        this.n = 1.5707963267948966d - ((this.g * 3.141592653589793d) / 180.0d);
        invalidate();
    }

    public void setViewAngle(float f2) {
        this.o = (-f2) + 180.0f + 90.0f;
        float f3 = this.o;
        if (f3 < 0.0f) {
            this.o = f3 + 360.0f;
        }
        invalidate();
    }

    private float a() {
        return this.c / 360.0f;
    }

    // TODO
    public double a(double d2, double d3) {
//        double d4;
//        double d5;
//        double d6;
//        double d7;
//        if (!SPStoreManager.getInstance().getBoolean("fimi.gaode.map", false) && !com.fimi.kernel.Constants.isFactoryApp()) {
//            LatLng latLng = com.fimi.app.x8s.i.d.b.d.l;
//            if (latLng != null) {
//                d4 = latLng.longitude;
//                d5 = latLng.latitude;
//                d7 = d4;
//                d6 = d5;
//            }
//            d6 = 0.0d;
//            d7 = 0.0d;
//        } else {
//            LatLng latLng2 = GaodeMapLocationManager.;
//            if (latLng2 != null) {
//                d4 = latLng2.longitude;
//                d5 = latLng2.latitude;
//                d7 = d4;
//                d6 = d5;
//            }
//            d6 = 0.0d;
//            d7 = 0.0d;
//        }
//        return a(d6, d7, d3, d2);
        return 0;
    }

    private double a(double d2, double d3, double d4, double d5) {
        double d6 = d5 - d3;
        return 360.0d - ((Math.toDegrees(Math.atan2(Math.sin(d6) * Math.cos(d4), (Math.cos(d2) * Math.sin(d4)) - ((Math.sin(d2) * Math.cos(d4)) * Math.cos(d6)))) + 360.0d) % 360.0d);
    }
}