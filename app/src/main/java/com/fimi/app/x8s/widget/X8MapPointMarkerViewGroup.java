package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.tools.X8NumberUtil;

public class X8MapPointMarkerViewGroup extends ViewGroup {
    private final int defaultMagin;
    int tempWidth;
    private boolean isRelation;
    private boolean isSelect;
    private boolean isTopResShow;
    private int lintTop;
    private int magin1;
    private int magin2;
    private int magin3;
    private int paintColor;
    private int textBg;
    private int type;

    public X8MapPointMarkerViewGroup(Context context) {
        this(context, null);
    }

    public X8MapPointMarkerViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X8MapPointMarkerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.defaultMagin = 10;
        this.isTopResShow = true;
        getAttrs(context, attrs);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.X8MapPointMarker);
            this.type = mTypedArray.getInt(R.styleable.X8MapPointMarker_type, 1);
            this.magin1 = mTypedArray.getDimensionPixelSize(R.styleable.X8MapPointMarker_margin1, dip2px(this.defaultMagin));
            this.magin2 = mTypedArray.getDimensionPixelSize(R.styleable.X8MapPointMarker_margin2, dip2px(this.defaultMagin));
            this.magin3 = mTypedArray.getDimensionPixelSize(R.styleable.X8MapPointMarker_margin3, dip2px(this.defaultMagin));
            mTypedArray.recycle();
        }
        this.paintColor = getContext().getResources().getColor(R.color.black_65);
        this.textBg = R.drawable.x8_ai_follow_marker_info_bg;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.type == 1) {
            onLayoutForTyp1(changed, l, t, r, b);
        } else if (this.type == 2) {
            onLayoutForTyp2(changed, l, t, r, b);
        } else if (this.type == 3) {
            onLayoutForTyp3(changed, l, t, r, b);
        } else {
            onLayoutForTyp4(changed, l, t, r, b);
        }
    }

    public void onLayoutForTyp4(boolean changed, int l, int t, int r, int b) {
        int countH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            if (i == 0) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                bottom = h;
                countH = bottom;
            } else if (i == 1) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                top = countH + this.magin1;
                bottom = top + h;
                countH = bottom;
            } else if (i == 2) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                top = countH + this.magin2;
                bottom = top + h;
                countH = bottom;
            } else if (i == 3) {
                View tempViw = getChildAt(2);
                left = (this.tempWidth - w) / 2;
                right = (this.tempWidth + w) / 2;
                int tmeph = tempViw.getMeasuredHeight();
                top = (int) ((countH - (0.5f * tmeph)) - (0.5f * h));
                bottom = (int) ((countH - (0.5f * tmeph)) + (0.5f * h));
                float measuredHeight = (countH - (0.5f * tmeph)) / getMeasuredHeight();
            }
            child.layout(left, top, right, bottom);
        }
    }

    public void onLayoutForTyp3(boolean changed, int l, int t, int r, int b) {
        int countH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            if (i == 0) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                bottom = h;
                countH = bottom;
            } else if (i == 1) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                top = countH + this.magin1;
                bottom = top + h;
                countH = bottom;
            } else if (i == 2) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                top = countH + this.magin2;
                bottom = top + h;
                countH = bottom;
            } else if (i == 3) {
                View tempViw = getChildAt(2);
                left = (this.tempWidth - w) / 2;
                right = (this.tempWidth + w) / 2;
                int tmeph = tempViw.getMeasuredHeight();
                top = (int) ((countH - (0.5f * tmeph)) - (0.5f * h));
                bottom = (int) ((countH - (0.5f * tmeph)) + (0.5f * h));
                float measuredHeight = (countH - (0.5f * tmeph)) / getMeasuredHeight();
            } else if (i == 4) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                top = countH + this.magin3;
                bottom = top + h;
                countH = bottom;
            }
            child.layout(left, top, right, bottom);
        }
    }

    public void onLayoutForTyp2(boolean changed, int l, int t, int r, int b) {
        int countH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            if (i == 0) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                bottom = h;
                countH = bottom;
            } else if (i == 1) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                top = countH + this.magin2;
                bottom = top + h;
                countH = bottom;
            } else if (i == 2) {
                View tempViw = getChildAt(1);
                left = (this.tempWidth - w) / 2;
                right = (this.tempWidth + w) / 2;
                int tmeph = tempViw.getMeasuredHeight();
                top = (int) ((countH - (0.5f * tmeph)) - (0.5f * h));
                bottom = (int) ((countH - (0.5f * tmeph)) + (0.5f * h));
                float measuredHeight = (countH - (0.5f * tmeph)) / getMeasuredHeight();
            }
            child.layout(left, top, right, bottom);
        }
    }

    public void onLayoutForTyp1(boolean changed, int l, int t, int r, int b) {
        int countH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();
            if (i == 0) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                bottom = h;
                countH = bottom;
            } else if (i == 1) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                top = countH + this.magin2;
                bottom = top + h;
                countH = bottom;
            } else if (i == 2) {
                View tempViw = getChildAt(1);
                left = (this.tempWidth - w) / 2;
                right = (this.tempWidth + w) / 2;
                int tmeph = tempViw.getMeasuredHeight();
                top = (int) ((countH - (0.5f * tmeph)) - (0.5f * h));
                bottom = (int) ((countH - (0.5f * tmeph)) + (0.5f * h));
                float measuredHeight = (countH - (0.5f * tmeph)) / getMeasuredHeight();
                this.lintTop = top;
            } else if (i == 3) {
                if (this.tempWidth == w) {
                    right = w;
                } else {
                    left = (this.tempWidth - w) / 2;
                    right = (this.tempWidth + w) / 2;
                }
                top = countH + this.magin3;
                bottom = top + h;
                countH = bottom;
            }
            child.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        if (this.isSelect) {
            this.paintColor = getContext().getResources().getColor(R.color.colorAccent);
            this.textBg = R.drawable.x8_ai_follow_marker_info_select_bg;
        } else {
            this.paintColor = getContext().getResources().getColor(R.color.black_65);
            this.textBg = R.drawable.x8_ai_follow_marker_info_bg;
        }
        p.setColor(this.paintColor);
        p.setStrokeWidth(5.0f);
        if (this.type == 1) {
            View v1 = getChildAt(0);
            View v2 = getChildAt(3);
            v1.setBackgroundResource(this.textBg);
            int startY = v1.getMeasuredHeight();
            int endY = getMeasuredHeight() - v2.getMeasuredHeight();
            if (this.isRelation) {
                v2.setBackgroundResource(R.drawable.x8_ai_follow_marker_info_select_bg);
                canvas.drawLine(getMeasuredWidth() / 2, startY, getMeasuredWidth() / 2, endY - this.lintTop, p);
                p.setColor(getContext().getResources().getColor(R.color.colorAccent));
                canvas.drawLine(getMeasuredWidth() / 2, this.lintTop, getMeasuredWidth() / 2, endY, p);
            } else {
                v2.setBackgroundResource(this.textBg);
                canvas.drawLine(getMeasuredWidth() / 2, startY, getMeasuredWidth() / 2, endY, p);
            }
        } else if (this.type == 2) {
            View v12 = getChildAt(0);
            View v22 = getChildAt(1);
            v12.setBackgroundResource(this.textBg);
            canvas.drawLine(getMeasuredWidth() / 2, v12.getMeasuredHeight(), getMeasuredWidth() / 2, getMeasuredHeight() - (v22.getMeasuredHeight() / 2), p);
        } else if (this.type == 3) {
            View v13 = getChildAt(0);
            View v23 = getChildAt(4);
            View v3 = getChildAt(1);
            getChildAt(1).setBackgroundResource(this.textBg);
            v23.setBackgroundResource(this.textBg);
            if (this.isTopResShow) {
                canvas.drawLine(getMeasuredWidth() / 2, v13.getMeasuredHeight() / 2, getMeasuredWidth() / 2, getMeasuredHeight() - v23.getMeasuredHeight(), p);
            } else {
                canvas.drawLine(getMeasuredWidth() / 2, v13.getMeasuredHeight() + (v3.getMeasuredHeight() / 2), getMeasuredWidth() / 2, getMeasuredHeight() - v23.getMeasuredHeight(), p);
            }
        } else if (this.type == 4) {
            View v14 = getChildAt(0);
            View v24 = getChildAt(2);
            View v32 = getChildAt(1);
            getChildAt(1).setBackgroundResource(this.textBg);
            if (this.isTopResShow) {
                canvas.drawLine(getMeasuredWidth() / 2, v14.getMeasuredHeight() / 2, getMeasuredWidth() / 2, getMeasuredHeight() - (v24.getMeasuredHeight() / 2), p);
            } else {
                canvas.drawLine(getMeasuredWidth() / 2, v14.getMeasuredHeight() + (v32.getMeasuredHeight() / 2), getMeasuredWidth() / 2, getMeasuredHeight() - (v24.getMeasuredHeight() / 2), p);
            }
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            int w = childAt.getMeasuredWidth();
            int h = childAt.getMeasuredHeight();
            if (this.type == 1 || this.type == 2) {
                if (i != 2) {
                    if (w > this.tempWidth) {
                        this.tempWidth = w;
                    }
                    height += h;
                }
            } else if ((this.type == 3 || this.type == 4) && i != 3) {
                if (w > this.tempWidth) {
                    this.tempWidth = w;
                }
                height += h;
            }
        }
        int height2 = calcAllHeight(height);
        int width = this.tempWidth;
        setMeasuredDimension(measureWidth(widthMeasureSpec, width), measureHeight(heightMeasureSpec, height2));
    }

    public int calcAllHeight(int height) {
        if (this.type == 1) {
            return this.magin2 + height + this.magin3;
        }
        if (this.type == 2) {
            return height + this.magin2;
        }
        if (this.type == 3) {
            return this.magin1 + height + this.magin2 + this.magin3;
        }
        if (this.type == 4) {
            return this.magin1 + height + this.magin2;
        }
        return height;
    }

    private int measureHeight(int measureSpec, int height) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        if (mode == 1073741824) {
            return size;
        }
        if (mode != Integer.MIN_VALUE) {
            return height;
        }
        return Math.min(height, size);
    }

    public int dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * scale) + 0.5f);
    }

    private int measureWidth(int measureSpec, int width) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        if (mode == 1073741824) {
            return size;
        }
        if (mode != Integer.MIN_VALUE) {
            return width;
        }
        return Math.min(width, size);
    }

    public void setAngle(float angle) {
        ImageView t = (ImageView) getChildAt(1);
        t.setRotation(angle);
        postInvalidate();
    }

    public void setValueWithPio(int bottomRes, float heightVale, int i, int poi, float angle, boolean select, boolean isRelation) {
        String s = X8NumberUtil.getDistanceNumberString(heightVale, 0, true);
        ((TextView) getChildAt(0)).setText("" + s);
        getChildAt(1).setBackgroundResource(bottomRes);
        getChildAt(1).setRotation(angle);
        ((TextView) getChildAt(2)).setText("" + i);
        ((TextView) getChildAt(3)).setText("" + poi);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }

    public void setValueNoPio(int bottomRes, float heightVale, int i, float angle, boolean select, boolean isRelation) {
        String s = X8NumberUtil.getDistanceNumberString(heightVale, 0, true);
        ((TextView) getChildAt(0)).setText("" + s);
        getChildAt(1).setBackgroundResource(bottomRes);
        getChildAt(1).setRotation(angle);
        ((TextView) getChildAt(2)).setText("" + i);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }

    public void setPioValue(int bottomRes, float heightVale, int i, boolean select, boolean isRelation) {
        String s = X8NumberUtil.getDistanceNumberString(heightVale, 0, true);
        ((TextView) getChildAt(0)).setText("" + s);
        getChildAt(1).setBackgroundResource(bottomRes);
        ((TextView) getChildAt(2)).setText("");
        ((TextView) getChildAt(3)).setText("POI" + i);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }

    public void setPointEventValue(int topRes, int bottomRes, float heightVale, int i, float angle, boolean select, boolean isRelation) {
        if (topRes == -1) {
            this.isTopResShow = false;
            ((ImageView) getChildAt(0)).setImageResource(topRes);
            getChildAt(0).setVisibility(View.INVISIBLE);
        } else {
            this.isTopResShow = true;
            ((ImageView) getChildAt(0)).setImageResource(topRes);
        }
        String s = X8NumberUtil.getDistanceNumberString(heightVale, 0, true);
        ((TextView) getChildAt(1)).setText("" + s);
        getChildAt(2).setBackgroundResource(bottomRes);
        getChildAt(2).setRotation(angle);
        ((TextView) getChildAt(3)).setText("" + i);
        ((TextView) getChildAt(4)).setText("" + i);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }

    public void setPointEventNoPioValue(int topRes, int bottomRes, float heightVale, int i, float angle, boolean select, boolean isRelation) {
        if (topRes == -1) {
            this.isTopResShow = false;
            ((ImageView) getChildAt(0)).setImageResource(topRes);
            getChildAt(0).setVisibility(View.INVISIBLE);
        } else {
            this.isTopResShow = true;
            ((ImageView) getChildAt(0)).setImageResource(topRes);
        }
        String s = X8NumberUtil.getDistanceNumberString(heightVale, 0, true);
        ((TextView) getChildAt(1)).setText("" + s);
        getChildAt(2).setBackgroundResource(bottomRes);
        getChildAt(2).setRotation(angle);
        ((TextView) getChildAt(3)).setText("" + i);
        this.isSelect = select;
        this.isRelation = isRelation;
        postInvalidate();
    }
}
