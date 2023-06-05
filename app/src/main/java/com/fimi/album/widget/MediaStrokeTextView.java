package com.fimi.album.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fimi.kernel.utils.FontUtil;

/* loaded from: classes.dex */
public class MediaStrokeTextView extends TextView {
    private TextView borderText;

    public MediaStrokeTextView(Context context) {
        super(context);
        this.borderText = null;
        this.borderText = new TextView(context);
        FontUtil.changeDINAlernateBold(context.getAssets(), this.borderText, this);
        init();
    }

    public MediaStrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.borderText = null;
        this.borderText = new TextView(context, attrs);
        FontUtil.changeDINAlernateBold(context.getAssets(), this.borderText, this);
        init();
    }

    public MediaStrokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.borderText = null;
        this.borderText = new TextView(context, attrs, defStyle);
        FontUtil.changeDINAlernateBold(context.getAssets(), this.borderText, this);
        init();
    }

    public void init() {
        TextPaint tp1 = this.borderText.getPaint();
        tp1.setStrokeWidth(1.0f);
        tp1.setStyle(Paint.Style.STROKE);
        this.borderText.setTextColor(1275068416);
        this.borderText.setGravity(getGravity());
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        this.borderText.setLayoutParams(params);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = this.borderText.getText();
        if (tt == null || !tt.equals(getText())) {
            this.borderText.setText(getText());
            postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.borderText.layout(left, top, right, bottom);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        this.borderText.draw(canvas);
        super.onDraw(canvas);
    }
}
