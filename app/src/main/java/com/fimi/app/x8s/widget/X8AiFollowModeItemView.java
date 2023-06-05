package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;

public class X8AiFollowModeItemView extends RelativeLayout implements View.OnClickListener {
    private final ImageView item1;
    private final ImageView item2;
    private final ImageView item3;
    private final ImageView openClose;
    private final int index;
    private final int[] mode;
    private final int[] res;
    private boolean isOpen;
    private OnModeSelectListner listener;

    public X8AiFollowModeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.res = new int[]{R.drawable.x8_btn_ai_follow_lockup, R.drawable.x8_btn_ai_follow_parallel, R.drawable.x8_btn_ai_follow_normal};
        this.mode = new int[]{2, 1, 0};
        this.index = 0;
        this.isOpen = true;
        LayoutInflater.from(context).inflate(R.layout.x8_ai_follow_item_mode_layout, this, true);
        this.openClose = findViewById(R.id.img_open_close);
        this.item1 = findViewById(R.id.img_item1);
        this.item2 = findViewById(R.id.img_item2);
        this.item3 = findViewById(R.id.img_item3);
        this.openClose.setOnClickListener(this);
        this.item1.setOnClickListener(this);
        this.item2.setOnClickListener(this);
        this.item3.setOnClickListener(this);
        this.item3.setSelected(true);
    }

    public void setListener(OnModeSelectListner listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.img_open_close) {
            if (this.isOpen) {
                this.isOpen = false;
                this.openClose.setBackgroundResource(R.drawable.x8_btn_ai_follow_mode_open);
                this.item1.setVisibility(View.GONE);
                this.item2.setVisibility(View.GONE);
                return;
            }
            this.isOpen = true;
            this.openClose.setBackgroundResource(R.drawable.x8_btn_ai_follow_mode_close);
            this.item1.setVisibility(View.VISIBLE);
            this.item2.setVisibility(View.VISIBLE);
        } else if (id == R.id.img_item1) {
            findIndexByMode(2);
            this.listener.onModeSelect(this.mode[0]);
        } else if (id == R.id.img_item2) {
            findIndexByMode(1);
            this.listener.onModeSelect(this.mode[1]);
        } else if (id == R.id.img_item3) {
            findIndexByMode(0);
            this.listener.onModeSelect(this.mode[2]);
        }
    }

    public void switchItem() {
        int temp = this.mode[2];
        this.mode[2] = this.mode[this.index];
        this.mode[this.index] = temp;
        int temp2 = this.res[2];
        this.res[2] = this.res[this.index];
        this.res[this.index] = temp2;
        for (int i = 0; i < this.mode.length; i++) {
            switch (i) {
                case 0:
                    this.item1.setBackgroundResource(this.res[i]);
                    break;
                case 1:
                    this.item2.setBackgroundResource(this.res[i]);
                    break;
                case 2:
                    this.item3.setBackgroundResource(this.res[i]);
                    break;
            }
        }
        Log.i("istep", " " + this.mode[2]);
    }

    public void findIndexByMode(int type) {
        if (type == 2) {
            this.item1.setSelected(true);
            this.item2.setSelected(false);
            this.item3.setSelected(false);
        } else if (type == 1) {
            this.item1.setSelected(false);
            this.item2.setSelected(true);
            this.item3.setSelected(false);
        } else if (type == 0) {
            this.item1.setSelected(false);
            this.item2.setSelected(false);
            this.item3.setSelected(true);
        }
    }

    public interface OnModeSelectListner {
        void onModeSelect(int i);
    }
}
