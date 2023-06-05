package com.fimi.libperson.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.FontUtil;


public class TitleView extends FrameLayout {
    private final ImageView mIvLeft;
    private final TextView mTvRight;
    private final TextView mTvTitle;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.sub_login_title, this);
        this.mTvTitle = findViewById(R.id.tv_title);
        this.mIvLeft = findViewById(R.id.iv_return);
        this.mTvRight = findViewById(R.id.tv_right);
        this.mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) TitleView.this.getContext()).finish();
            }
        });
        FontUtil.changeFontLanTing(context.getAssets(), this.mTvRight, this.mTvTitle);
    }

    public void setTvTitle(String text) {
        this.mTvTitle.setText(text);
    }

    public void setTvRightText(String rightText) {
        this.mTvRight.setText(rightText);
    }

    public void setTvRightVisible(int visible) {
        this.mTvRight.setVisibility(visible);
    }

    public void setIvLeftListener(View.OnClickListener l) {
        this.mIvLeft.setOnClickListener(l);
    }

    public void setTvRightListener(View.OnClickListener l) {
        this.mTvRight.setOnClickListener(l);
    }

    public void setRightTvIsVisible(boolean isVisible) {
        this.mTvRight.setVisibility(isVisible ? 0 : 4);
    }
}
