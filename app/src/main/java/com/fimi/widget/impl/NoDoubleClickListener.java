package com.fimi.widget.impl;

import android.view.View;

import java.util.Calendar;

public abstract class NoDoubleClickListener implements View.OnClickListener {
    public int minClickDelayTime;
    private long lastClickTime = 0;

    public NoDoubleClickListener(int minClickDelayTime) {
        this.minClickDelayTime = 1000;
        this.minClickDelayTime = minClickDelayTime;
    }

    protected abstract void onNoDoubleClick(View view);

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - this.lastClickTime > this.minClickDelayTime) {
            this.lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
}
