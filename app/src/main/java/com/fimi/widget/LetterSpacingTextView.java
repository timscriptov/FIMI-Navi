package com.fimi.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class LetterSpacingTextView extends androidx.appcompat.widget.AppCompatTextView {
    private float spacing;

    public LetterSpacingTextView(Context context) {
        super(context);
        this.spacing = 0.0f;
    }

    public LetterSpacingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.spacing = 0.0f;
    }

    public LetterSpacingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.spacing = 0.0f;
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = dip2px(spacing);
        applySpacing();
    }

    public int dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * scale) + 0.5f);
    }

    private void applySpacing() {
        CharSequence originalText = getText();
        if (this != null && originalText != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < originalText.length(); i++) {
                builder.append(originalText.charAt(i));
                if (i + 1 < originalText.length()) {
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
