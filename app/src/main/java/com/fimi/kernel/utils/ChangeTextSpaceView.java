package com.fimi.kernel.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;


public class ChangeTextSpaceView extends TextView {
    private CharSequence originalText;
    private float spacing;

    public ChangeTextSpaceView(Context context) {
        super(context);
        this.spacing = 0.0f;
        this.originalText = "";
    }

    public ChangeTextSpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.spacing = 0.0f;
        this.originalText = "";
    }

    public ChangeTextSpaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.spacing = 0.0f;
        this.originalText = "";
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        applySpacing();
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        this.originalText = text;
        applySpacing();
    }

    @Override
    public CharSequence getText() {
        return this.originalText;
    }

    private void applySpacing() {
        if (this != null && this.originalText != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < this.originalText.length(); i++) {
                builder.append(this.originalText.charAt(i));
                if (i + 1 < this.originalText.length()) {
                    builder.append(" ");
                }
            }
            SpannableString finalText = new SpannableString(builder.toString());
            if (builder.toString().length() > 1) {
                for (int i2 = 1; i2 < builder.toString().length(); i2 += 2) {
                    finalText.setSpan(new ScaleXSpan((this.spacing + 1.0f) / 10.0f), i2, i2 + 1, 33);
                }
            }
            super.setText(finalText, TextView.BufferType.SPANNABLE);
        }
    }

    public class Spacing {
        public static final float NORMAL = 0.0f;

        public Spacing() {
        }
    }
}
