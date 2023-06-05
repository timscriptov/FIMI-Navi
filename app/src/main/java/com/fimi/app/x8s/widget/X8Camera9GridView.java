package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.fimi.android.app.R;
import com.fimi.kernel.Constants;

public class X8Camera9GridView extends View {
    private int indexStartX;
    private int indexStartY;
    private Paint paint;
    private float screenHeight;
    private float screenWidth;
    private int type;

    public X8Camera9GridView(Context context) {
        super(context);
        this.type = 2;
        initPaint();
    }

    public X8Camera9GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.type = 2;
        initPaint();
    }

    public X8Camera9GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.type = 2;
        initPaint();
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
        postInvalidate();
    }

    private void initPaint() {
        this.paint = new Paint();
        this.paint.setColor(getResources().getColor(R.color.white_100));
        this.paint.setAlpha(153);
        this.paint.setStrokeWidth(1.0f);
        this.paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, this.indexStartY, this.screenWidth, this.indexStartY, this.paint);
        canvas.drawLine(0.0f, this.indexStartY * 2, this.screenWidth, this.indexStartY * 2, this.paint);
        canvas.drawLine(this.indexStartX, 0.0f, this.indexStartX, this.screenHeight, this.paint);
        canvas.drawLine(this.indexStartX * 2, 0.0f, this.indexStartX * 2, this.screenHeight, this.paint);
        if (this.type == 1) {
            canvas.drawLine(0.0f, 0.0f, this.screenWidth, this.screenHeight, this.paint);
            canvas.drawLine(this.screenWidth, 0.0f, 0.0f, this.screenHeight, this.paint);
            if (Constants.isFactoryApp()) {
                canvas.drawLine(this.screenWidth / 2.0f, 0.0f, this.screenWidth / 2.0f, this.screenHeight, this.paint);
                canvas.drawLine(0.0f, this.screenHeight / 2.0f, this.screenWidth, this.screenHeight / 2.0f, this.paint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            this.screenWidth = w;
            this.screenHeight = h;
            this.indexStartX = (int) (this.screenWidth / 3.0f);
            this.indexStartY = (int) (this.screenHeight / 3.0f);
        }
    }
}
