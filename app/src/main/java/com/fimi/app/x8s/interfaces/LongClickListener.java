package com.fimi.app.x8s.interfaces;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;


public abstract class LongClickListener implements View.OnTouchListener {
    private final Handler handler = new Handler();
    private int viewId;
    private boolean isClickDown = false;
    private final Runnable longClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (LongClickListener.this.isClickDown) {
                LongClickListener.this.handler.postDelayed(this, 50L);
                LongClickListener.this.longClickCallback(LongClickListener.this.viewId);
            }
        }
    };

    public abstract void longClickCallback(int i);

    public abstract void onFingerUp(int i);

    @Override
    public boolean onTouch(@NonNull View v, @NonNull MotionEvent event) {
        this.viewId = v.getId();
        int action = event.getAction();
        switch (action) {
            case 0:
                this.isClickDown = true;
                this.handler.postDelayed(this.longClickRunnable, 500L);
                break;
            case 1:
                this.isClickDown = false;
                this.handler.removeCallbacks(this.longClickRunnable);
                onFingerUp(this.viewId);
                break;
        }
        return false;
    }
}
