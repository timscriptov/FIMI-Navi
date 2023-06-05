package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;

public class X8AiTipWithCloseView extends RelativeLayout implements View.OnClickListener {
    private boolean isClose;
    private TextView tvTip;
    private View vClose;

    public X8AiTipWithCloseView(Context context) {
        super(context);
        initView(context);
    }

    public X8AiTipWithCloseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public X8AiTipWithCloseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public boolean isClose() {
        return this.isClose;
    }

    public void setClose(boolean close) {
        this.isClose = close;
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.x8_ai_tip_with_close_view, this, true);
        this.tvTip = findViewById(R.id.tv_tip);
        this.vClose = findViewById(R.id.tl_close);
        initActions();
    }

    public void initActions() {
        this.vClose.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.tl_close) {
            setVisibility(View.GONE);
            this.isClose = true;
        }
    }

    public void setTipText(String text) {
        this.tvTip.setText(text);
    }

    public void showTip() {
        setVisibility(View.VISIBLE);
    }

    public boolean isVisibility() {
        return getVisibility() == View.VISIBLE;
    }
}
