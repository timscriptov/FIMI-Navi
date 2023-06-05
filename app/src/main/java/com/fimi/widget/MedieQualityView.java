package com.fimi.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.LogUtil;

import java.text.DecimalFormat;

public class MedieQualityView extends FrameLayout {
    private final Handler mHandler;
    int arg1;
    int arg2;
    boolean isAnimation;
    TextView tvArg1;
    TextView tvArg2;

    @SuppressLint("HandlerLeak")
    public MedieQualityView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.arg1 = 0;
        this.arg2 = 0;
        this.isAnimation = false;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    float showArg2 = MedieQualityView.this.arg2 / 10.0f;
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String arg2Str = decimalFormat.format(showArg2);
                    MedieQualityView.this.tvArg1.setText("" + MedieQualityView.this.arg1);
                    MedieQualityView.this.tvArg2.setText(arg2Str + "%");
                    MedieQualityView.this.mHandler.sendEmptyMessage(1);
                    return;
                }
                float showArg22 = MedieQualityView.this.arg2 / 10.0f;
                DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
                String arg2Str2 = decimalFormat2.format(showArg22);
                MedieQualityView.this.tvArg1.setText(" " + MedieQualityView.this.arg1);
                MedieQualityView.this.tvArg2.setText(" " + arg2Str2 + "%");
                MedieQualityView.this.mHandler.sendEmptyMessage(0);
            }
        };
        LayoutInflater.from(context).inflate(R.layout.item_media_quality, this);
        this.tvArg1 = (TextView) findViewById(R.id.tv_arg1);
        this.tvArg2 = (TextView) findViewById(R.id.tv_arg2);
    }

    public void setMediaQuality(int arg1, int arg2) {
        LogUtil.d("moweiru", "arg1:" + arg1 + ";arg2:" + arg2);
        float showArg2 = arg2 / 10.0f;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String arg2Str = decimalFormat.format(showArg2);
        if (this.isAnimation) {
            this.tvArg1.setText("" + arg1);
            this.tvArg2.setText(arg2Str + "%");
            this.isAnimation = false;
            return;
        }
        this.tvArg1.setText("  " + arg1);
        this.tvArg2.setText("  " + arg2Str + "%");
        this.isAnimation = true;
    }

    public void removeMessage() {
        this.mHandler.removeMessages(0);
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
    }
}
