package com.fimi.app.x8s.tensortfloow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.internal.view.SupportMenu;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"WrongCall"})

public class TestOverlay extends View {
    private final String TAG;
    RectF rf;
    private final boolean enableCustomTestOverlay;
    private final int endX;
    private final int endY;
    private final boolean isLost;
    private final boolean isTracking;
    private final TestOverlayListener listener;
    private final int lostColor;
    private final List<RectF> mRectF;
    private final int selectedColor;
    private final int startX;
    private final int startY;
    private int viewH;
    private int viewW;
    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;

    public TestOverlay(Context context) {
        super(context);
        this.TAG = TestOverlay.class.getSimpleName();
        this.listener = null;
        this.enableCustomTestOverlay = false;
        this.isTracking = false;
        this.isLost = true;
        this.selectedColor = -15935891;
        this.lostColor = SupportMenu.CATEGORY_MASK;
        this.viewW = 0;
        this.viewH = 0;
        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
        this.mRectF = new ArrayList();
        this.rf = new RectF();
    }

    public TestOverlay(Context context, int w, int h) {
        super(context);
        this.TAG = TestOverlay.class.getSimpleName();
        this.listener = null;
        this.enableCustomTestOverlay = false;
        this.isTracking = false;
        this.isLost = true;
        this.selectedColor = -15935891;
        this.lostColor = SupportMenu.CATEGORY_MASK;
        this.viewW = 0;
        this.viewH = 0;
        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
        this.mRectF = new ArrayList();
        this.rf = new RectF();
        this.viewW = w;
        this.viewH = h;
    }

    public TestOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = TestOverlay.class.getSimpleName();
        this.listener = null;
        this.enableCustomTestOverlay = false;
        this.isTracking = false;
        this.isLost = true;
        this.selectedColor = -15935891;
        this.lostColor = SupportMenu.CATEGORY_MASK;
        this.viewW = 0;
        this.viewH = 0;
        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
        this.mRectF = new ArrayList();
        this.rf = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.rf != null) {
            Paint p = new Paint();
            p.setColor(this.lostColor);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(this.rf.left, this.rf.top, this.rf.right, this.rf.bottom, p);
        }
    }

    public void addRect(RectF r) {
        this.mRectF.add(r);
    }

    public void clear() {
        this.mRectF.clear();
    }

    public void onTracking(RectF r) {
        this.rf.left = r.left;
        this.rf.top = r.top;
        this.rf.right = r.right;
        this.rf.bottom = r.bottom;
    }


    public interface TestOverlayListener {
        void onDraw(Canvas canvas, Rect rect, boolean z);

        void onTouchActionDown();

        void onTouchActionUp(int i, int i2, int i3, int i4);
    }
}
