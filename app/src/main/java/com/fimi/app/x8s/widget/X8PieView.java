package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;

import com.fimi.android.app.R;

import org.jetbrains.annotations.Contract;

import java.text.DecimalFormat;
import java.util.Random;

public class X8PieView extends View {
    private int centerX;
    private int centerY;
    private float circleWidth;
    private int[] colors;
    private Paint dataPaint;
    private final Rect dataTextBound;
    private int dataTextColor;
    private float dataTextSize;
    private Paint mArcPaint;
    private String[] names;
    private int[] numbers;
    private float radius;
    private final Random random;
    private RectF rectF;
    private int sum;

    public X8PieView(Context context) {
        super(context);
        this.dataTextBound = new Rect();
        this.random = new Random();
        this.dataTextSize = 30.0f;
        this.dataTextColor = SupportMenu.CATEGORY_MASK;
        this.circleWidth = 100.0f;
        init();
    }

    public X8PieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public X8PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.dataTextBound = new Rect();
        this.random = new Random();
        this.dataTextSize = 30.0f;
        this.dataTextColor = SupportMenu.CATEGORY_MASK;
        this.circleWidth = 100.0f;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.X8PieView);
        this.dataTextSize = typedArray.getDimension(R.styleable.X8PieView_dataTextSize, this.dataTextSize);
        this.circleWidth = typedArray.getDimension(R.styleable.X8PieView_circleWidth, this.circleWidth);
        this.dataTextColor = typedArray.getColor(R.styleable.X8PieView_dataTextColor, this.dataTextColor);
        typedArray.recycle();
        init();
    }

    private void init() {
        this.mArcPaint = new Paint();
        this.mArcPaint.setStrokeWidth(this.circleWidth);
        this.mArcPaint.setAntiAlias(true);
        this.mArcPaint.setStyle(Paint.Style.FILL);
        this.dataPaint = new Paint();
        this.dataPaint.setStrokeWidth(2.0f);
        this.dataPaint.setTextSize(this.dataTextSize);
        this.dataPaint.setAntiAlias(true);
        this.dataPaint.setColor(this.dataTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (measureWidthMode == Integer.MIN_VALUE && measureHeightMode == Integer.MIN_VALUE) {
            setMeasuredDimension(measureWidthSize, measureHeightSize);
        } else if (measureWidthMode == Integer.MIN_VALUE) {
            setMeasuredDimension(measureWidthSize, measureHeightSize);
        } else if (measureHeightMode == Integer.MIN_VALUE) {
            setMeasuredDimension(measureWidthSize, measureHeightSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.centerX = getMeasuredWidth() / 2;
        this.centerY = getMeasuredHeight() / 2;
        this.radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 4;
        this.rectF = new RectF(this.centerX - this.radius, this.centerY - this.radius, this.centerX + this.radius, this.centerY + this.radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateAndDraw(canvas);
    }

    private void calculateAndDraw(Canvas canvas) {
        float angle;
        if (this.numbers != null && this.numbers.length != 0) {
            int startAngle = 0;
            for (int i = 0; i < this.numbers.length; i++) {
                float percent = this.numbers[i] / this.sum;
                if (i == this.numbers.length - 1) {
                    angle = 360 - startAngle;
                } else {
                    angle = (float) Math.ceil(360.0f * percent);
                }
                drawArc(canvas, startAngle, angle, this.colors[i], i);
                startAngle = (int) (startAngle + angle);
                if (this.numbers[i] > 0) {
                    float arcCenterDegree = (startAngle + 90) - (angle / 2.0f);
                    calculatePosition(arcCenterDegree);
                    drawData(canvas, i, percent);
                }
            }
        }
    }

    @NonNull
    @Contract("_ -> new")
    private float[] calculatePosition(float degree) {
        this.radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 4;
        float x = (float) (Math.sin(Math.toRadians(degree)) * this.radius);
        float y = (float) (Math.cos(Math.toRadians(degree)) * this.radius);
        float startX = this.centerX + x;
        float startY = this.centerY - y;
        return new float[]{startX, startY};
    }

    private void drawData(Canvas canvas, int i, float percent) {
        if (i == 2) {
            Path path = new Path();
            this.dataPaint.setColor(-1);
            this.dataPaint.setStyle(Paint.Style.STROKE);
            path.moveTo(this.rectF.centerX() + (this.radius * 0.6f), this.rectF.centerY() - (0.8f * this.radius));
            path.lineTo(this.rectF.centerX() + this.radius, this.rectF.centerY() - (this.radius * 1.2f));
            path.lineTo(this.rectF.centerX() + (this.radius * 2.0f), this.rectF.centerY() - (this.radius * 1.2f));
            canvas.drawPath(path, this.dataPaint);
            drawPieValue(canvas, this.names[i], this.rectF.centerX() + (this.radius * 1.5f), this.rectF.centerY() - (this.radius * 1.2f), percent);
        } else if (i == 1) {
            Path path2 = new Path();
            this.dataPaint.setColor(-1);
            this.dataPaint.setStyle(Paint.Style.STROKE);
            path2.moveTo(this.rectF.centerX() - (this.radius * 0.6f), this.rectF.centerY() + (this.radius * 0.6f));
            path2.lineTo(this.rectF.centerX() - this.radius, this.rectF.centerY() + this.radius);
            path2.lineTo(this.rectF.centerX() - (this.radius * 2.0f), this.rectF.centerY() + this.radius);
            canvas.drawPath(path2, this.dataPaint);
            drawPieValue(canvas, this.names[i], ((-1.5f) * this.radius) + this.rectF.centerX(), this.rectF.centerY() + this.radius, percent);
        } else if (i == 0) {
            Path path3 = new Path();
            this.dataPaint.setColor(-1);
            this.dataPaint.setStyle(Paint.Style.STROKE);
            path3.moveTo((this.radius * 0.6f) + this.rectF.centerX(), this.rectF.centerY() + (this.radius * 0.6f));
            path3.lineTo(this.radius + this.rectF.centerX(), this.rectF.centerY() + this.radius);
            path3.lineTo((this.radius * 2.0f) + this.rectF.centerX(), this.rectF.centerY() + this.radius);
            canvas.drawPath(path3, this.dataPaint);
            drawPieValue(canvas, this.names[i], (this.radius * 1.5f) + this.rectF.centerX(), this.rectF.centerY() + this.radius, percent);
        }
    }

    private void drawPieValue(@NonNull Canvas canvas, String name, float startX, float startY, float percent) {
        this.dataPaint.getTextBounds(name, 0, name.length(), this.dataTextBound);
        canvas.drawText(name, startX - (this.dataTextBound.width() / 2), ((this.dataTextBound.height() / 2) + startY) - 20.0f, this.dataPaint);
        DecimalFormat df = new DecimalFormat("0.0");
        String percentString = df.format(100.0f * percent) + "%";
        this.dataPaint.getTextBounds(percentString, 0, percentString.length(), this.dataTextBound);
        canvas.drawText(percentString, startX - (this.dataTextBound.width() / 2), (this.dataTextBound.height() / 2) + startY + 30.0f, this.dataPaint);
    }

    private void drawArc(@NonNull Canvas canvas, float startAngle, float angle, int color, int index) {
        this.mArcPaint.setColor(color);
        this.radius = (index * 8) + (Math.min(getMeasuredWidth(), getMeasuredHeight()) / 4);
        this.rectF = new RectF(this.centerX - this.radius, this.centerY - this.radius, this.centerX + this.radius, this.centerY + this.radius);
        canvas.drawArc(this.rectF, startAngle - 0.5f, angle + 0.5f, true, this.mArcPaint);
    }

    private int randomColor() {
        int red = this.random.nextInt(256);
        int green = this.random.nextInt(256);
        int blue = this.random.nextInt(256);
        return Color.rgb(red, green, blue);
    }

    public void setData(int[] numbers, String[] names) {
        if (numbers != null && numbers.length != 0 && names != null && names.length != 0 && numbers.length == names.length) {
            this.numbers = numbers;
            this.names = names;
            this.colors = new int[numbers.length];
            for (int i = 0; i < this.numbers.length; i++) {
                this.sum += numbers[i];
                this.colors[i] = randomColor();
            }
            invalidate();
        }
    }
}
