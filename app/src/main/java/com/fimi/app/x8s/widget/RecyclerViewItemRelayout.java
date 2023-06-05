package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class RecyclerViewItemRelayout extends RelativeLayout {
    public RecyclerViewItemRelayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecyclerViewItemRelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewItemRelayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), (int) (getDefaultSize(0, widthMeasureSpec) * 0.75f));
        int childWidthSize = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        int heightMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec2, heightMeasureSpec2);
    }
}
