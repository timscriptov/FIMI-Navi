package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.fimi.android.app.R;

public class BallProgress extends View {
    private float a;
    private float b;

    private Paint f2551c;

    public BallProgress(Context context) {
        super(context);
        this.a = 5000.0f;
        this.b = 0.0f;
    }

    public BallProgress(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.a = 5000.0f;
        this.b = 0.0f;
    }

    public BallProgress(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.a = 5000.0f;
        this.b = 0.0f;
        a();
    }

    private void a() {
        this.f2551c = new Paint();
        this.f2551c.setAntiAlias(true);
    }

    private Bitmap getRectangleBitmap() {
        int width = getWidth() - 5;
        int height = getHeight() - 5;
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        ClipDrawable clipDrawable = (ClipDrawable) getContext().getResources().getDrawable(R.drawable.x8s_main_clip_ball);
        clipDrawable.setBounds(new Rect(0, 0, width, height));
        clipDrawable.setLevel((int) this.a);
        Canvas canvas = new Canvas(createBitmap);
        canvas.rotate(this.b, width / 2, height / 2);
        clipDrawable.draw(canvas);
        return createBitmap;
    }

    public float getProgress() {
        return this.a;
    }

    public void setProgress(float f2) {
        double sin = Math.sin((f2 * 3.141592653589793d) / 180.0d);
        int i2 = (sin > 0.0d ? 1 : (sin == 0.0d ? 0 : -1));
        if (i2 == 0) {
            this.a = 5000.0f;
        } else if (i2 > 0) {
            this.a = (float) ((sin * 5000.0d) + 5000.0d);
        } else {
            this.a = (float) (5000.0d - Math.abs(sin * 5000.0d));
        }
        this.a = Math.abs(10000.0f - this.a);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap rectangleBitmap = getRectangleBitmap();
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        canvas.drawBitmap(rectangleBitmap, 0.0f, 0.0f, this.f2551c);
    }

    public void setAngle(float f2) {
        if (f2 >= 0.0f) {
            this.b = f2;
        } else {
            this.b = (180.0f - Math.abs(f2)) + 180.0f;
        }
        postInvalidate();
    }
}