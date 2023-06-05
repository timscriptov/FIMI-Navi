package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;

public class X8AiFollowTipBgView extends RelativeLayout implements View.OnClickListener {
    private View bgContent;
    private ImageView imgTipClose;
    private TextView tvTip;

    public X8AiFollowTipBgView(Context context) {
        super(context);
        initView(context);
    }

    public X8AiFollowTipBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public X8AiFollowTipBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.x8_ai_follow_tip_bg_view, this, true);
        this.tvTip = findViewById(R.id.tv_tip);
        this.imgTipClose = findViewById(R.id.img_tip_close);
        this.bgContent = findViewById(R.id.rl_bg_content);
        initActions();
    }

    public void initActions() {
        this.imgTipClose.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.img_tip_close) {
            this.bgContent.setVisibility(View.GONE);
        }
    }

    public void setTipText(String text) {
        this.tvTip.setText(text);
    }

    public void showTip() {
        this.bgContent.setVisibility(View.VISIBLE);
    }
}
