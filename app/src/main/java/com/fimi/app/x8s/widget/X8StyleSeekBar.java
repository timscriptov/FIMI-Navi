package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatSeekBar;

public class X8StyleSeekBar extends AppCompatSeekBar {
    public X8StyleSeekBar(Context context) {
        super(context);
    }

    public X8StyleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public X8StyleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
