package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.kernel.FimiAppContext;

@SuppressLint({"WrongCall"})
public class X8TrackOverlayView extends View {
    final int MARGING_TOP;
    final int MAX_PATH;
    private final String TAG;
    public boolean hasTouch;
    public int selectedColor;
    int initialValue;
    private int MAX_HEIGHT;
    private int MAX_UNSIGNED_SHORT;
    private int MAX_WIDTH;
    private float d;
    private boolean enableCustomOverlay;
    private float endX;
    private float endY;
    private String errorMsg;
    private boolean isClean;
    private boolean isErrorSelect;
    private boolean isLost;
    private boolean isTracking;
    private int k;
    private OverlayListener listener;
    private int lostColor;
    private int lostfillColor;
    private int lostfillErrorColor;
    private Rect mBounds;
    private Handler mHandler;
    private Paint p;
    private int previewH;
    private int previewH2;
    private int previewW;
    private int previewW2;
    private RectF r1;
    private RectF r2;
    private int selectedErrorColor;
    private float startX;
    private float startY;
    private int viewH;
    private int viewW;
    private float x1;
    private float x2;
    private float y1;
    private float y2;

    @SuppressLint("HandlerLeak")
    public X8TrackOverlayView(Context context) {
        super(context);
        this.TAG = X8TrackOverlayView.class.getSimpleName();
        this.listener = null;
        this.enableCustomOverlay = false;
        this.isTracking = false;
        this.isLost = false;
        this.isClean = true;
        this.viewW = 0;
        this.viewH = 0;
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.endX = 0.0f;
        this.endY = 0.0f;
        this.x1 = 0.0f;
        this.y1 = 0.0f;
        this.x2 = 0.0f;
        this.y2 = 0.0f;
        this.hasTouch = false;
        this.initialValue = 0;
        this.isErrorSelect = false;
        this.previewW = FimiAppContext.UI_HEIGHT;
        this.previewH = FimiAppContext.UI_WIDTH;
        this.MAX_UNSIGNED_SHORT = 65535;
        this.k = 0;
        this.r2 = new RectF();
        this.r1 = new RectF();
        this.p = new Paint();
        this.MAX_PATH = 104;
        this.mBounds = new Rect();
        this.errorMsg = "";
        this.MARGING_TOP = 20;
        this.previewW2 = 1;
        this.previewH2 = 1;
        this.mHandler = new Handler() { // from class: com.fimi.app.x8s.widget.X8TrackOverlayView.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                X8TrackOverlayView.this.cleanTrackerRect();
            }
        };
        initView();
    }

    @SuppressLint("HandlerLeak")
    public X8TrackOverlayView(Context context, int w, int h) {
        super(context);
        this.TAG = X8TrackOverlayView.class.getSimpleName();
        this.listener = null;
        this.enableCustomOverlay = false;
        this.isTracking = false;
        this.isLost = false;
        this.isClean = true;
        this.viewW = 0;
        this.viewH = 0;
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.endX = 0.0f;
        this.endY = 0.0f;
        this.x1 = 0.0f;
        this.y1 = 0.0f;
        this.x2 = 0.0f;
        this.y2 = 0.0f;
        this.hasTouch = false;
        this.initialValue = 0;
        this.isErrorSelect = false;
        this.previewW = FimiAppContext.UI_HEIGHT;
        this.previewH = FimiAppContext.UI_WIDTH;
        this.MAX_UNSIGNED_SHORT = 65535;
        this.k = 0;
        this.r2 = new RectF();
        this.r1 = new RectF();
        this.p = new Paint();
        this.MAX_PATH = 104;
        this.mBounds = new Rect();
        this.errorMsg = "";
        this.MARGING_TOP = 20;
        this.previewW2 = 1;
        this.previewH2 = 1;
        this.mHandler = new Handler() { // from class: com.fimi.app.x8s.widget.X8TrackOverlayView.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                X8TrackOverlayView.this.cleanTrackerRect();
            }
        };
        this.viewW = w;
        this.viewH = h;
        initView();
    }

    @SuppressLint("HandlerLeak")
    public X8TrackOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = X8TrackOverlayView.class.getSimpleName();
        this.listener = null;
        this.enableCustomOverlay = false;
        this.isTracking = false;
        this.isLost = false;
        this.isClean = true;
        this.viewW = 0;
        this.viewH = 0;
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.endX = 0.0f;
        this.endY = 0.0f;
        this.x1 = 0.0f;
        this.y1 = 0.0f;
        this.x2 = 0.0f;
        this.y2 = 0.0f;
        this.hasTouch = false;
        this.initialValue = 0;
        this.isErrorSelect = false;
        this.previewW = FimiAppContext.UI_HEIGHT;
        this.previewH = FimiAppContext.UI_WIDTH;
        this.MAX_UNSIGNED_SHORT = 65535;
        this.k = 0;
        this.r2 = new RectF();
        this.r1 = new RectF();
        this.p = new Paint();
        this.MAX_PATH = 104;
        this.mBounds = new Rect();
        this.errorMsg = "";
        this.MARGING_TOP = 20;
        this.previewW2 = 1;
        this.previewH2 = 1;
        this.mHandler = new Handler() { // from class: com.fimi.app.x8s.widget.X8TrackOverlayView.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                X8TrackOverlayView.this.cleanTrackerRect();
            }
        };
        initView();
    }

    public void setOverlayListener(OverlayListener listener) {
        this.listener = listener;
    }

    public void setCustomOverlay(boolean flag) {
        this.enableCustomOverlay = flag;
    }

    private void initView() {
        this.selectedColor = getContext().getResources().getColor(R.color.x8_track_select);
        this.lostColor = getContext().getResources().getColor(R.color.x8_track_lost);
        this.lostfillColor = getContext().getResources().getColor(R.color.x8_track_fill_lost);
        this.selectedErrorColor = getContext().getResources().getColor(R.color.x8_track_select_error);
        this.lostfillErrorColor = getContext().getResources().getColor(R.color.x8_track_fill_lost_error);
    }

    public boolean getTracking() {
        return this.isTracking;
    }

    public void setTracking(boolean flag) {
        this.isTracking = flag;
    }

    public void setLost(boolean flag) {
        this.isLost = flag;
    }

    public void setSelectedColor(int color) {
        this.selectedColor = color;
    }

    public void setLostColor(int color) {
        this.lostColor = color;
    }

    public void refreshTrackerRect(float x1, float y1, float x2, float y2) {
        if (!this.hasTouch) {
            this.startX = x1;
            this.startY = y1;
            this.endX = x2;
            this.endY = y2;
            postInvalidate();
        }
    }

    public void cleanTrackerRect() {
        if (!this.hasTouch) {
            this.errorMsg = "";
            float f = this.initialValue;
            this.x1 = f;
            this.startX = f;
            float f2 = this.initialValue;
            this.y1 = f2;
            this.startY = f2;
            float f3 = this.initialValue;
            this.x2 = f3;
            this.endX = f3;
            float f4 = this.initialValue;
            this.y2 = f4;
            this.endY = f4;
            setLostColor(this.selectedColor);
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == 0) {
            this.hasTouch = true;
            setTracking(false);
            this.errorMsg = "";
            this.mHandler.removeMessages(0);
            this.lostColor = this.selectedColor;
            float f = this.initialValue;
            this.x1 = f;
            this.startX = f;
            float f2 = this.initialValue;
            this.y1 = f2;
            this.startY = f2;
            float f3 = this.initialValue;
            this.x2 = f3;
            this.endX = f3;
            float f4 = this.initialValue;
            this.y2 = f4;
            this.endY = f4;
            if (this.listener != null) {
                this.listener.onTouchActionDown();
            }
            float x = event.getX();
            this.endX = x;
            this.startX = x;
            float y = event.getY();
            this.endY = y;
            this.startY = y;
            this.isErrorSelect = false;
            invalidate();
        } else if (event.getAction() == 2) {
            this.endX = event.getX();
            this.endY = event.getY();
            invalidate();
        } else if (event.getAction() == 1) {
            if (this.listener != null) {
                this.listener.onTouchActionUp(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1, (int) this.x1, (int) this.y1, (int) this.x2, (int) this.y2);
            }
            this.hasTouch = false;
        }
        return true;
    }

    public void onTracking(int x, int y, int w, int h) {
        if (!this.hasTouch && this.MAX_WIDTH != 0 && this.MAX_HEIGHT != 0) {
            setTracking(true);
            this.lostColor = this.selectedColor;
            this.x1 = ((this.MAX_WIDTH * x) * 1.0f) / this.MAX_UNSIGNED_SHORT;
            this.x2 = this.x1 + (((this.MAX_WIDTH * w) * 1.0f) / this.MAX_UNSIGNED_SHORT);
            this.y1 = ((this.MAX_HEIGHT * y) * 1.0f) / this.MAX_UNSIGNED_SHORT;
            this.y2 = this.y1 + (((this.MAX_HEIGHT * h) * 1.0f) / this.MAX_UNSIGNED_SHORT);
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!this.isTracking) {
            changeXY();
            drawRect(canvas);
            onDrawPath(canvas);
        } else if (this.isTracking) {
            drawRect(canvas);
            onDrawPath(canvas);
            drawText(canvas);
            if (this.listener != null) {
                this.listener.onChangeGoLocation(this.x1, this.x2, this.y1, this.y2, getWidth(), getHeight());
            }
        }
    }

    public void changeXY() {
        if (this.startX <= this.endX) {
            this.x1 = this.startX;
            this.x2 = this.endX;
        } else {
            this.x1 = this.endX;
            this.x2 = this.startX;
        }
        if (this.startY <= this.endY) {
            this.y1 = this.startY;
            this.y2 = this.endY;
            return;
        }
        this.y1 = this.endY;
        this.y2 = this.startY;
    }

    public void drawRect(Canvas canvas) {
        if (!this.isErrorSelect) {
            this.p.setColor(this.lostfillColor);
        } else {
            this.p.setColor(this.lostfillErrorColor);
        }
        this.p.setStyle(Paint.Style.FILL);
        this.p.setStrokeWidth(0.0f);
        this.r1.left = this.x1;
        this.r1.right = this.x2;
        this.r1.top = this.y1;
        this.r1.bottom = this.y2;
        canvas.drawRect(this.r1, this.p);
        if (!this.isErrorSelect) {
            this.p.setColor(this.lostColor);
        } else {
            this.p.setColor(this.selectedErrorColor);
        }
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeWidth(3.0f);
        this.p.setTextSize(0.0f);
        this.r2.left = this.x1;
        this.r2.right = this.x2;
        this.r2.top = this.y1;
        this.r2.bottom = this.y2;
        canvas.drawRect(this.r2, this.p);
    }

    public void onDrawPath(Canvas canvas) {
        this.d = (this.x2 - this.x1) / 4.0f;
        if (this.d >= 52.0f) {
            this.d = ((this.x2 - this.x1) - 104.0f) / 2.0f;
        }
        if (!this.isErrorSelect) {
            this.p.setColor(this.lostColor);
        } else {
            this.p.setColor(this.selectedErrorColor);
        }
        this.p.setStyle(Paint.Style.FILL);
        this.p.setStrokeWidth(0.0f);
        if (this.y2 - this.y1 > 12.0f) {
            this.k = 6;
        } else {
            this.k = ((int) (this.y2 - this.y1)) / 2;
        }
        for (int i = 0; i < this.k; i++) {
            float xd1 = this.x1 + this.d + (i * 1.5f);
            float xd2 = (this.x2 - this.d) - (i * 1.5f);
            if (xd1 < this.x2 && xd2 > this.x1) {
                canvas.drawLine(xd1, i + this.y1, xd2, i + this.y1, this.p);
            }
        }
        for (int i2 = 0; i2 < this.k; i2++) {
            float xd12 = this.x1 + this.d + (i2 * 1.5f);
            float xd22 = (this.x2 - this.d) - (i2 * 1.5f);
            if (xd12 < this.x2 && xd22 > this.x1) {
                canvas.drawLine(xd12, this.y2 - i2, xd22, this.y2 - i2, this.p);
            }
        }
        this.d = (this.y2 - this.y1) / 4.0f;
        if (this.d >= 52.0f) {
            this.d = ((this.y2 - this.y1) - 104.0f) / 2.0f;
        }
        if (this.x2 - this.x1 > 12.0f) {
            this.k = 6;
        } else {
            this.k = ((int) (this.x2 - this.x1)) / 2;
        }
        for (int i3 = 0; i3 < this.k; i3++) {
            float yd1 = this.y1 + this.d + (i3 * 1.5f);
            float yd2 = (this.y2 - this.d) - (i3 * 1.5f);
            if (yd1 < this.y2 && yd2 > this.y1) {
                canvas.drawLine(this.x1 + i3, yd1, this.x1 + i3, yd2, this.p);
            }
        }
        for (int i4 = 0; i4 < this.k; i4++) {
            float yd12 = this.y1 + this.d + (i4 * 1.5f);
            float yd22 = (this.y2 - this.d) - (i4 * 1.5f);
            if (yd12 < this.y2 && yd22 > this.y1) {
                canvas.drawLine(this.x2 - i4, yd12, this.x2 - i4, yd22, this.p);
            }
        }
    }

    public void setSelectError(boolean b) {
        this.isErrorSelect = b;
    }

    public void drawText(Canvas canvas) {
        if (this.isErrorSelect && this.errorMsg.length() > 0) {
            this.p.setTextSize(21.0f);
            this.p.getTextBounds(this.errorMsg, 0, this.errorMsg.length(), this.mBounds);
            float textWidth = this.mBounds.width();
            float textHeight = this.mBounds.height();
            canvas.drawText(this.errorMsg, (this.x1 + ((this.x2 - this.x1) / 2.0f)) - (textWidth / 2.0f), this.y2 + 20.0f + textHeight, this.p);
        }
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        this.mHandler.removeMessages(0);
        this.mHandler.sendEmptyMessageDelayed(0, 1500L);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            this.MAX_WIDTH = w;
            this.MAX_HEIGHT = h;
        }
    }

    public int getMaxWidth() {
        return this.MAX_WIDTH;
    }

    public int getMaxHeight() {
        return this.MAX_HEIGHT;
    }

    public interface OverlayListener {
        boolean isCanSelect();

        void onChangeGoLocation(float f, float f2, float f3, float f4, int i, int i2);

        void onDraw(Canvas canvas, RectF rectF, boolean z);

        void onTouchActionDown();

        void onTouchActionUp(float f, float f2, float f3, float f4, int i, int i2, int i3, int i4);
    }
}
