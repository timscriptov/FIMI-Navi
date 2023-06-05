package com.fimi.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.fimi.android.app.R;
import com.fimi.kernel.utils.AbViewUtil;

public class SwitchButton extends View {
    private final int borderColor;
    private final int onColor;
    private final RectF rect;
    SimpleSpringListener springListener;
    private int borderWidth;
    private float centerY;
    private boolean defaultAnimate;
    private float endX;
    private OnSwitchListener listener;
    private int offSpotColor;
    private int onSpotColor;
    private Paint paint;
    private float radius;
    private int spotColor;
    private float spotMaxX;
    private float spotMinX;
    private int spotSize;
    private float spotX;
    private Spring spring;
    private SpringSystem springSystem;
    private float startX;
    private boolean toggleOn;

    private SwitchButton(Context context) {
        super(context);
        this.onColor = 0;
        this.borderColor = Color.parseColor("#ffffff");
        this.spotColor = Color.parseColor("#bcbcbd");
        this.offSpotColor = Color.parseColor("#bcbcbd");
        this.onSpotColor = Color.parseColor("#ff5400");
        this.toggleOn = false;
        this.borderWidth = 1;
        this.rect = new RectF();
        this.defaultAnimate = true;
        this.springListener = new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                double value = spring.getCurrentValue();
                SwitchButton.this.calculateEffect(value);
            }
        };
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.onColor = 0;
        this.borderColor = Color.parseColor("#ffffff");
        this.spotColor = Color.parseColor("#bcbcbd");
        this.offSpotColor = Color.parseColor("#bcbcbd");
        this.onSpotColor = Color.parseColor("#ff5400");
        this.toggleOn = false;
        this.borderWidth = 1;
        this.rect = new RectF();
        this.defaultAnimate = true;
        this.springListener = new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                double value = spring.getCurrentValue();
                SwitchButton.this.calculateEffect(value);
            }
        };
        setup(attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.onColor = 0;
        this.borderColor = Color.parseColor("#ffffff");
        this.spotColor = Color.parseColor("#bcbcbd");
        this.offSpotColor = Color.parseColor("#bcbcbd");
        this.onSpotColor = Color.parseColor("#ff5400");
        this.toggleOn = false;
        this.borderWidth = 1;
        this.rect = new RectF();
        this.defaultAnimate = true;
        this.springListener = new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                double value = spring.getCurrentValue();
                SwitchButton.this.calculateEffect(value);
            }
        };
        setup(attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.spring.removeListener(this.springListener);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.spring.addListener(this.springListener);
    }

    public void setup(AttributeSet attrs) {
        this.paint = new Paint(1);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.springSystem = SpringSystem.create();
        this.spring = this.springSystem.createSpring();
        this.spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(50.0d, 7.0d));
        setOnClickListener(arg0 -> SwitchButton.this.onViewSwitch());
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        this.onSpotColor = typedArray.getColor(R.styleable.SwitchButton_onColor, this.onSpotColor);
        this.offSpotColor = typedArray.getColor(R.styleable.SwitchButton_spotColor, this.offSpotColor);
        this.toggleOn = typedArray.getBoolean(R.styleable.SwitchButton_onToggle, this.toggleOn);
        this.spotColor = this.offSpotColor;
        this.borderWidth = typedArray.getDimensionPixelSize(R.styleable.SwitchButton_borderWidth, (int) AbViewUtil.dip2px(getContext(), this.borderWidth));
        this.defaultAnimate = typedArray.getBoolean(R.styleable.SwitchButton_animate, this.defaultAnimate);
        typedArray.recycle();
    }

    public void setSwitchState(boolean state) {
        setSwitchState(state, true);
    }

    public void setSwitchState(boolean state, boolean animate) {
        this.toggleOn = state;
        takeEffect(animate);
    }

    public void onViewSwitch() {
        if (this.listener != null) {
            this.listener.onSwitch(this, this.toggleOn);
        }
    }

    public void onSwitch(boolean isFlag) {
        this.toggleOn = isFlag;
        takeEffect(true);
        setSwitchState(this.toggleOn);
    }

    private void takeEffect(boolean animate) {
        if (animate) {
            this.spring.setEndValue(this.toggleOn ? 1.0d : 0.0d);
            return;
        }
        this.spring.setCurrentValue(this.toggleOn ? 1.0d : 0.0d);
        calculateEffect(this.toggleOn ? 1.0d : 0.0d);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        View.MeasureSpec.getMode(heightMeasureSpec);
        View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        Resources r = Resources.getSystem();
        if (widthMode == 0 || widthMode == Integer.MIN_VALUE) {
            int widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50.0f, r.getDisplayMetrics());
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        if (heightSize == 0 || heightSize == Integer.MIN_VALUE) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30.0f, r.getDisplayMetrics()), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean getToggleOn() {
        return this.toggleOn;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getWidth();
        int height = getHeight();
        this.radius = Math.min(width, height) * 0.5f;
        this.centerY = this.radius;
        this.startX = this.radius;
        this.endX = width - this.radius;
        this.spotMinX = this.startX + this.borderWidth;
        this.spotMaxX = this.endX - this.borderWidth;
        this.spotSize = height - (this.borderWidth * 4);
        this.spotX = this.toggleOn ? this.spotMaxX : this.spotMinX;
    }

    private int clamp(int value, int low, int high) {
        return Math.min(Math.max(value, low), high);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setAntiAlias(true);
        float padding = AbViewUtil.dip2px(getContext(), 0.5f);
        this.rect.set(padding, padding, getWidth() - padding, getHeight() - padding);
        this.paint.setColor(603979775);
        this.paint.setStrokeWidth(AbViewUtil.dip2px(getContext(), 0.7f));
        canvas.drawRoundRect(this.rect, this.radius, this.radius, this.paint);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        this.rect.set((this.spotX - 1.0f) - this.radius, this.centerY - this.radius, this.spotX + 1.1f + this.radius, this.centerY + this.radius);
        this.paint.setColor(0);
        canvas.drawRoundRect(this.rect, this.radius, this.radius, this.paint);
        float spotR = this.spotSize * 0.45f;
        this.rect.set(this.spotX - spotR, this.centerY - spotR, this.spotX + spotR, this.centerY + spotR);
        this.paint.setColor(this.spotColor);
        canvas.drawRoundRect(this.rect, spotR, spotR, this.paint);
    }

    public void calculateEffect(double value) {
        this.spotX = (float) SpringUtil.mapValueFromRangeToRange(value, 0.0d, 1.0d, this.spotMinX, this.spotMaxX);
        float mapValueFromRangeToRange = (float) SpringUtil.mapValueFromRangeToRange(1.0d - value, 0.0d, 1.0d, 10.0d, this.spotSize);
        int spotFB = Color.blue(this.onSpotColor);
        int spotFR = Color.red(this.onSpotColor);
        int spotFG = Color.green(this.onSpotColor);
        int spotTB = Color.blue(this.offSpotColor);
        int spotTR = Color.red(this.offSpotColor);
        int spotTG = Color.green(this.offSpotColor);
        int spotSB = (int) SpringUtil.mapValueFromRangeToRange(1.0d - value, 0.0d, 1.0d, spotFB, spotTB);
        int spotSR = (int) SpringUtil.mapValueFromRangeToRange(1.0d - value, 0.0d, 1.0d, spotFR, spotTR);
        int spotSG = (int) SpringUtil.mapValueFromRangeToRange(1.0d - value, 0.0d, 1.0d, spotFG, spotTG);
        this.spotColor = Color.rgb(clamp(spotSR, 0, 255), clamp(spotSG, 0, 255), clamp(spotSB, 0, 255));
        postInvalidate();
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.listener = onSwitchListener;
    }

    public boolean isAnimate() {
        return this.defaultAnimate;
    }

    public void setAnimate(boolean animate) {
        this.defaultAnimate = animate;
    }

    public interface OnSwitchListener {
        void onSwitch(View view, boolean z);
    }
}
