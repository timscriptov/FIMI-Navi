package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;

public class X8FollowSpeedView extends View {
    private int cursorColor;
    private int cursorH;
    private int cursorW;
    private boolean isClick;
    private boolean isInit;
    private boolean isRight;
    private boolean isSet;
    private int lineBgColor;
    private int lineH;
    private OnChangeListener listener;
    private Paint paint;
    private int panding;
    private float pos;
    private int progessColor;
    private RectF rectF;
    private int s;
    private int thumW;
    private int v;
    private int w;

    public X8FollowSpeedView(Context context) {
        super(context);
        this.lineBgColor = R.color.x8_follow_speed_line_bg;
        this.progessColor = R.color.x8_follow_speed_line_progess;
        this.cursorColor = R.color.x8_follow_speed_line_cursor;
        this.isRight = true;
        initView(context);
    }

    public X8FollowSpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.lineBgColor = R.color.x8_follow_speed_line_bg;
        this.progessColor = R.color.x8_follow_speed_line_progess;
        this.cursorColor = R.color.x8_follow_speed_line_cursor;
        this.isRight = true;
        initView(context);
    }

    public X8FollowSpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.lineBgColor = R.color.x8_follow_speed_line_bg;
        this.progessColor = R.color.x8_follow_speed_line_progess;
        this.cursorColor = R.color.x8_follow_speed_line_cursor;
        this.isRight = true;
        initView(context);
    }

    public void initView(@NonNull Context context) {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.lineH = context.getResources().getDimensionPixelSize(R.dimen.x8_follow_speed_line_h);
        this.cursorW = context.getResources().getDimensionPixelSize(R.dimen.x8_follow_speed_cursor_w);
        this.cursorH = context.getResources().getDimensionPixelSize(R.dimen.x8_follow_speed_cursor_h);
        this.thumW = context.getResources().getDimensionPixelSize(R.dimen.x8_follow_speed_thum_w);
        this.panding = this.thumW / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.w = getWidth();
        int h = getHeight();
        if (this.w > 0) {
            if (!this.isInit) {
                setInitSpeed(this.s, this.v);
                this.isInit = true;
            }
            this.rectF = new RectF(this.panding + 0, (h / 2) - (this.lineH / 2), this.w - this.panding, (h / 2) + (this.lineH / 2));
            this.paint.setColor(getContext().getResources().getColor(this.lineBgColor));
            canvas.drawRoundRect(this.rectF, 3.0f, 3.0f, this.paint);
            if (this.isRight) {
                this.rectF = new RectF(this.w / 2, (h / 2) - (this.lineH / 2), (this.w / 2) + this.pos, (h / 2) + (this.lineH / 2));
            } else {
                this.rectF = new RectF((this.w / 2) - this.pos, (h / 2) - (this.lineH / 2), this.w / 2, (h / 2) + (this.lineH / 2));
            }
            this.paint.setColor(getContext().getResources().getColor(this.progessColor));
            canvas.drawRect(this.rectF, this.paint);
            this.paint.setColor(getContext().getResources().getColor(this.cursorColor));
            if (this.isRight) {
                canvas.drawCircle(this.rectF.right, h / 2, this.thumW / 2, this.paint);
            } else {
                canvas.drawCircle(this.rectF.left, h / 2, this.thumW / 2, this.paint);
            }
            this.rectF = new RectF((this.w / 2) - (this.cursorW / 2), (h / 2) - (this.cursorH / 2), (this.w / 2) + (this.cursorW / 2), (h / 2) + (this.cursorH / 2));
            this.paint.setColor(getContext().getResources().getColor(this.cursorColor));
            canvas.drawRoundRect(this.rectF, 3.0f, 3.0f, this.paint);
            if (this.listener != null) {
                if (this.isSet) {
                    this.listener.onChange(this.v == 0 ? 0.0f : (Math.abs(this.s) * 1.0f) / this.v, this.isRight);
                    this.isSet = false;
                } else {
                    this.listener.onChange((this.pos * 1.0f) / ((this.w / 2) - this.panding), this.isRight);
                }
            }
            if (this.isClick) {
                this.isClick = false;
                if (this.listener != null) {
                    this.listener.onSendData();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case 0:
            default:
                return true;
            case 1:
                if (this.listener != null) {
                    this.listener.onSendData();
                    return true;
                }
                return true;
            case 2:
                calculProgress(event.getX());
                return true;
        }
    }

    private void calculProgress(float x) {
        if (x >= this.w / 2) {
            if (x > this.w - this.panding) {
                x = this.w - this.panding;
            }
            this.isRight = true;
            this.pos = x - (this.w / 2);
        } else {
            if (x < this.panding + 0) {
                x = this.panding + 0;
            }
            this.isRight = false;
            this.pos = (this.w / 2) - x;
        }
        postInvalidate();
    }

    public void setOnSpeedChangeListener(OnChangeListener listener) {
        this.listener = listener;
    }

    public void setLeftClick(int v, int MAX, int MIN) {
        int v2;
        if (this.isRight) {
            if (v < MIN) {
                this.isRight = false;
            }
            v2 = v - 10;
        } else {
            v2 = v - 10;
            if (Math.abs(v2) > MAX - MIN) {
                v2 = -(MAX - MIN);
            }
        }
        setSpeedByMeasure(v2, MAX - MIN);
        setInitSpeed(v2, MAX - MIN);
        this.isClick = true;
        postInvalidate();
    }

    public void setRightClick(int v, int MAX, int MIN) {
        int v2;
        if (this.isRight) {
            v2 = v + 10;
            if (v2 > MAX - MIN) {
                v2 = MAX - MIN;
            }
        } else {
            if (Math.abs(v) < MIN) {
                this.isRight = true;
            }
            v2 = v + 10;
        }
        setSpeedByMeasure(v2, MAX - MIN);
        setInitSpeed(v2, MAX - MIN);
        this.isClick = true;
        postInvalidate();
    }

    public void setSpeed(int s, int v) {
        setSpeedByMeasure(s, v);
        if (this.isInit) {
            if (s >= 0) {
                this.isRight = true;
            } else {
                s = -s;
                this.isRight = false;
            }
            float x = (s * 1.0f) / v;
            float x2 = ((((this.w / 2) - this.panding) * 1.0f) * s) / v;
            this.pos = x2;
            postInvalidate();
        }
    }

    private void setSpeedByMeasure(int s, int v) {
        this.s = s;
        this.v = v;
        this.isSet = true;
    }

    private void setInitSpeed(int s, int v) {
        if (s >= 0) {
            this.isRight = true;
        } else {
            s = -s;
            this.isRight = false;
        }
        float x = ((((this.w / 2) - this.panding) * 1.0f) * s) / v;
        this.pos = x;
    }

    public interface OnChangeListener {
        void onChange(float f, boolean z);

        void onSendData();
    }
}
