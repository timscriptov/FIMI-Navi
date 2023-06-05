package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.kernel.utils.FontUtil;

@SuppressLint({"AppCompatCustomView"})
public class X8DinaStrokeTextView extends TextView {
    private TextView borderText;

    public X8DinaStrokeTextView(Context context) {
        super(context);
        this.borderText = null;
        this.borderText = new TextView(context);
        init(context);
    }

    public X8DinaStrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.borderText = null;
        this.borderText = new TextView(context, attrs);
        init(context);
    }

    public X8DinaStrokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.borderText = null;
        this.borderText = new TextView(context, attrs, defStyle);
        init(context);
    }

    public void init(@NonNull Context mContext) {
        TextPaint tp1 = this.borderText.getPaint();
        tp1.setStrokeWidth(1.0f);
        tp1.setStyle(Paint.Style.STROKE);
        this.borderText.setTextColor(855638016);
        this.borderText.setGravity(getGravity());
        FontUtil.changeDINAlernateBold(mContext.getAssets(), this.borderText, this);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        this.borderText.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = this.borderText.getText();
        if (tt == null || !tt.equals(getText())) {
            this.borderText.setText(getText());
            postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.borderText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.borderText.draw(canvas);
        super.onDraw(canvas);
    }
}
