package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.DensityUtil;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8MainElectricView extends View {

    float v;
    private int hightElectric;
    private int lowElectric;
    private final Paint mPaint;
    private State mState;
    private int middleElectric;
    private int mostlowElectric;
    private int percent;

    public X8MainElectricView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.percent = 100;
        this.mostlowElectric = 0;
        this.lowElectric = 0;
        this.middleElectric = 0;
        this.hightElectric = 0;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.X8MainElectric, 0, 0);
        this.lowElectric = typedArray.getColor(R.styleable.X8MainElectric_lowElectric, -1);
        this.middleElectric = typedArray.getColor(R.styleable.X8MainElectric_middleElectric, -1);
        this.hightElectric = typedArray.getColor(R.styleable.X8MainElectric_hightElectric, -1);
        this.mostlowElectric = context.getResources().getColor(R.color.x8_battery_most_low);
        typedArray.recycle();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(-1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.v = StateManager.getInstance().getX8Drone().getLowPowerValue();
        if (this.v <= 0.0f) {
            this.v = 20.0f;
        }
        this.mPaint.setColor(-1);
        if (this.percent <= 50) {
            if (this.percent > this.v) {
                drawType2(canvas);
                return;
            } else if (this.percent > 15) {
                drawType4(canvas);
                return;
            } else if (this.percent > 10) {
                drawType3(canvas);
                return;
            } else {
                this.mPaint.setColor(this.mostlowElectric);
                canvas.drawRect(0.0f, 0.0f, (((this.percent / 100.0f) * getWidth()) * 1624.0f) / 1920.0f, getHeight(), this.mPaint);
                return;
            }
        }
        drawType1(canvas);
    }

    public void setPercent(int percent) {
        if (this.percent != percent || this.v != StateManager.getInstance().getX8Drone().getLowPowerValue()) {
            this.percent = percent;
            invalidate();
        }
    }

    public void drawType4(@NonNull Canvas canvas) {
        this.mPaint.setColor(this.middleElectric);
        canvas.drawRect(0.0f, 0.0f, (((this.percent / 100.0f) * getWidth()) * 1624.0f) / 1920.0f, getHeight(), this.mPaint);
        this.mPaint.setColor(this.mostlowElectric);
        canvas.drawRect(0.0f, 0.0f, ((getWidth() * 0.1f) * 1624.0f) / 1920.0f, getHeight(), this.mPaint);
        float rightStart = ((getWidth() * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd = rightStart + DensityUtil.dip2px(getContext(), 2.0f);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart, 0.0f, rightEnd, getHeight(), this.mPaint);
        this.mPaint.setColor(this.lowElectric);
        float rightEnd2 = ((0.15f * getWidth()) * 1624.0f) / 1920.0f;
        canvas.drawRect(rightEnd, 0.0f, rightEnd2, getHeight(), this.mPaint);
        float rightEnd3 = rightEnd2 + DensityUtil.dip2px(getContext(), 2.0f);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightEnd2, 0.0f, rightEnd3, getHeight(), this.mPaint);
    }

    public void drawType3(@NonNull Canvas canvas) {
        this.mPaint.setColor(this.mostlowElectric);
        canvas.drawRect(0.0f, 0.0f, ((getWidth() * 0.1f) * 1624.0f) / 1920.0f, getHeight(), this.mPaint);
        float rightStart = ((getWidth() * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd = rightStart + DensityUtil.dip2px(getContext(), 2.0f);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart, 0.0f, rightEnd, getHeight(), this.mPaint);
        this.mPaint.setColor(this.lowElectric);
        canvas.drawRect(rightEnd, 0.0f, (((this.percent / 100.0f) * getWidth()) * 1624.0f) / 1920.0f, getHeight(), this.mPaint);
    }

    public void drawType2(@NonNull Canvas canvas) {
        float rightStart = ((getWidth() * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd = (((this.percent / 100.0f) * getWidth()) * 1624.0f) / 1920.0f;
        canvas.drawRect(rightStart, 0.0f, rightEnd, getHeight(), this.mPaint);
        this.mPaint.setColor(this.mostlowElectric);
        canvas.drawRect(0.0f, 0.0f, ((getWidth() * 0.1f) * 1624.0f) / 1920.0f, getHeight(), this.mPaint);
        float rightStart2 = ((getWidth() * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd2 = rightStart2 + DensityUtil.dip2px(getContext(), 2.0f);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart2, 0.0f, rightEnd2, getHeight(), this.mPaint);
        float rightStart3 = ((0.15f * getWidth()) * 1624.0f) / 1920.0f;
        float rightEnd3 = rightStart3 + DensityUtil.dip2px(getContext(), 2.0f);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart3, 0.0f, rightEnd3, getHeight(), this.mPaint);
        float maxStart = (((0.5f * getWidth()) * 1624.0f) / 1920.0f) - DensityUtil.dip2px(getContext(), 2.0f);
        float rightStart4 = (((this.v / 100.0f) * getWidth()) * 1624.0f) / 1920.0f;
        if (rightStart4 > maxStart) {
            rightStart4 = maxStart;
        }
        float rightEnd4 = rightStart4 + DensityUtil.dip2px(getContext(), 2.0f);
        canvas.drawRect(rightStart4, 0.0f, rightEnd4, getHeight(), this.mPaint);
    }

    public void drawType1(@NonNull Canvas canvas) {
        float rightStart = ((getWidth() * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd = ((0.5f * getWidth()) * 1624.0f) / 1920.0f;
        canvas.drawRect(rightStart, 0.0f, rightEnd, getHeight(), this.mPaint);
        float rightStart2 = getWidth() - (((0.5f * getWidth()) * 1624.0f) / 1920.0f);
        canvas.drawRect(rightStart2, 0.0f, rightStart2 + (((((this.percent - 50) / 100.0f) * getWidth()) * 1624.0f) / 1920.0f), getHeight(), this.mPaint);
        this.mPaint.setColor(this.mostlowElectric);
        canvas.drawRect(0.0f, 0.0f, ((getWidth() * 0.1f) * 1624.0f) / 1920.0f, getHeight(), this.mPaint);
        float rightStart3 = ((getWidth() * 0.1f) * 1624.0f) / 1920.0f;
        float rightEnd2 = rightStart3 + DensityUtil.dip2px(getContext(), 2.0f);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart3, 0.0f, rightEnd2, getHeight(), this.mPaint);
        float rightStart4 = ((0.15f * getWidth()) * 1624.0f) / 1920.0f;
        float rightEnd3 = rightStart4 + DensityUtil.dip2px(getContext(), 2.0f);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRect(rightStart4, 0.0f, rightEnd3, getHeight(), this.mPaint);
        float maxStart = (((0.5f * getWidth()) * 1624.0f) / 1920.0f) - DensityUtil.dip2px(getContext(), 2.0f);
        float rightStart5 = (((this.v / 100.0f) * getWidth()) * 1624.0f) / 1920.0f;
        if (rightStart5 > maxStart) {
            rightStart5 = maxStart;
        }
        float rightEnd4 = rightStart5 + DensityUtil.dip2px(getContext(), 2.0f);
        canvas.drawRect(rightStart5, 0.0f, rightEnd4, getHeight(), this.mPaint);
    }

    public enum State {
        LOW,
        MIDDLE,
        HIGHT
    }
}
