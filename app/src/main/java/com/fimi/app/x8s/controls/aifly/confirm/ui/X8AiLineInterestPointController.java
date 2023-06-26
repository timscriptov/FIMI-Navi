package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimi.android.app.R;


public class X8AiLineInterestPointController {
    private final RelativeLayout group;
    private final TextView textView;
    private final ImageView view;
    private OnInterestTouchUp listener;
    private ImageView tmpView;

    public X8AiLineInterestPointController(RelativeLayout group, ImageView view, TextView textView) {
        this.group = group;
        this.view = view;
        this.textView = textView;
        addTouchEvent();
    }

    public void setInterestEnable(boolean interestEnable) {
        this.view.setEnabled(interestEnable);
    }

    public void setListener(OnInterestTouchUp listener) {
        this.listener = listener;
    }

    public void addTouchEvent() {
        this.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    X8AiLineInterestPointController.this.tmpView = new ImageView(X8AiLineInterestPointController.this.group.getContext());
                    X8AiLineInterestPointController.this.tmpView.setBackgroundResource(R.drawable.x8_img_ai_line_inreterst_max2);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                    layoutParams.setMargins(X8AiLineInterestPointController.this.view.getLeft(), X8AiLineInterestPointController.this.view.getTop(), 0, 0);
                    X8AiLineInterestPointController.this.group.addView(X8AiLineInterestPointController.this.tmpView, layoutParams);
                } else if (event.getAction() == 2) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) X8AiLineInterestPointController.this.tmpView.getLayoutParams();
                    lp.setMargins((X8AiLineInterestPointController.this.view.getLeft() + ((int) event.getX())) - (X8AiLineInterestPointController.this.tmpView.getWidth() / 2), (X8AiLineInterestPointController.this.view.getTop() + ((int) event.getY())) - ((int) (1.5f * X8AiLineInterestPointController.this.tmpView.getHeight())), 0, 0);
                    X8AiLineInterestPointController.this.tmpView.setLayoutParams(lp);
                } else if (event.getAction() == 1) {
                    X8AiLineInterestPointController.this.listener.onUp(X8AiLineInterestPointController.this.view.getLeft() + ((int) event.getX()), X8AiLineInterestPointController.this.view.getTop() + ((int) event.getY()));
                    X8AiLineInterestPointController.this.group.removeView(X8AiLineInterestPointController.this.tmpView);
                }
                return true;
            }
        });
    }

    public void showView(boolean b) {
        if (this.view != null && this.textView != null) {
            if (b) {
                this.view.setVisibility(View.VISIBLE);
                this.textView.setVisibility(View.VISIBLE);
                return;
            }
            this.view.setVisibility(View.GONE);
            this.textView.setVisibility(View.GONE);
        }
    }


    public interface OnInterestTouchUp {
        void onUp(int i, int i2);
    }
}
