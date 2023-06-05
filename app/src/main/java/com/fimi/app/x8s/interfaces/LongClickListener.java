package com.fimi.app.x8s.interfaces;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: classes.dex */
public abstract class LongClickListener implements View.OnTouchListener {
    private int viewId;
    private boolean isClickDown = false;
    private Handler handler = new Handler();
    private Runnable longClickRunnable = new Runnable() { // from class: com.fimi.app.x8s.interfaces.LongClickListener.1
        @Override // java.lang.Runnable
        public void run() {
            if (LongClickListener.this.isClickDown) {
                LongClickListener.this.handler.postDelayed(this, 50L);
                LongClickListener.this.longClickCallback(LongClickListener.this.viewId);
            }
        }
    };

    public abstract void longClickCallback(int i);

    public abstract void onFingerUp(int i);

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
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
