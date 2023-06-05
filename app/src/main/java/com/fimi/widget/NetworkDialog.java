package com.fimi.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.fimi.android.app.R;

public class NetworkDialog extends Dialog {
    private final boolean isHint;
    private final Handler mHandler;
    private int count;
    private TextView mTvLoad;

    @SuppressLint("HandlerLeak")
    public NetworkDialog(Context context, int theme, boolean isHint) {
        super(context, theme);
        this.count = 0;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    if (NetworkDialog.this.count == 0) {
                        NetworkDialog.this.count = 1;
                        NetworkDialog.this.mTvLoad.setText(R.string.network_loading1);
                    } else if (NetworkDialog.this.count == 1) {
                        NetworkDialog.this.count = 2;
                        NetworkDialog.this.mTvLoad.setText(R.string.network_loading2);
                    } else {
                        NetworkDialog.this.count = 0;
                        NetworkDialog.this.mTvLoad.setText(R.string.network_loading3);
                    }
                    NetworkDialog.this.mHandler.sendEmptyMessageDelayed(0, 500L);
                }
            }
        };
        this.isHint = isHint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fimisdk_dialog_network_loading);
        this.mTvLoad = findViewById(R.id.tv_load);
        if (this.isHint) {
            this.mTvLoad.setVisibility(View.VISIBLE);
            this.mHandler.sendEmptyMessage(0);
            return;
        }
        this.mTvLoad.setVisibility(View.GONE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (this.isHint) {
            this.mHandler.removeMessages(0);
        }
    }
}
