package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiPoint2PointExcuteConfirmModule;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8BaseModule;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8Point2PointExcuteListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.x8sdk.controller.FcManager;


public class X8MainAiFollowConfirmController extends AbsX8MenuBoxControllers implements View.OnClickListener {
    private Activity activity;
    private View blank;
    private X8BaseModule currentModule;
    private FcManager fcManager;
    private IX8Point2PointExcuteListener listener;
    private X8AiPoint2PointExcuteConfirmModule mX8AiFollowPoint2PointExcuteConfirmModule;
    private View mainLayout;

    public X8MainAiFollowConfirmController(View rootView) {
        super(rootView);
    }

    public void setAcitivity(Activity acitivity) {
        this.activity = acitivity;
    }

    @Override
    public void initViews(View rootView) {
        this.mainLayout = rootView.findViewById(R.id.x8_main_ai_follow_confirm_main_layout);
        this.blank = rootView.findViewById(R.id.x8_main_ai_follow_confirm_main_layout_content_blank);
        this.contentView = rootView.findViewById(R.id.x8_main_ai_follow_confirm_main_layout_content);
        this.mX8AiFollowPoint2PointExcuteConfirmModule = new X8AiPoint2PointExcuteConfirmModule();
        this.currentModule = this.mX8AiFollowPoint2PointExcuteConfirmModule;
    }

    @Override
    public void initActions() {
        this.blank.setOnClickListener(this);
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.x8_main_ai_follow_confirm_main_layout_content_blank) {
            closeAiUi();
        }
    }

    @Override
    public void openUi() {
        this.mainLayout.setVisibility(View.VISIBLE);
        this.blank.setVisibility(View.VISIBLE);
        this.currentModule.init(this.activity, this.contentView);
        if (!this.isShow) {
            Log.i("zdy", "showAiUi...........");
            this.isShow = true;
            if (this.width == 0) {
                this.contentView.setAlpha(0.0f);
                this.contentView.post(new Runnable() {
                    @Override
                    public void run() {
                        X8MainAiFollowConfirmController.this.contentView.setAlpha(1.0f);
                        X8MainAiFollowConfirmController.this.MAX_WIDTH = X8MainAiFollowConfirmController.this.mainLayout.getWidth();
                        X8MainAiFollowConfirmController.this.width = X8MainAiFollowConfirmController.this.contentView.getWidth();
                        X8MainAiFollowConfirmController.this.contentView.getHeight();
                        ObjectAnimator animatorY = ObjectAnimator.ofFloat(X8MainAiFollowConfirmController.this.contentView, "translationX", X8MainAiFollowConfirmController.this.width, 0.0f);
                        animatorY.setDuration(300L);
                        animatorY.start();
                    }
                });
                return;
            }
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.contentView, "translationX", this.width, 0.0f);
            animatorY.setDuration(300L);
            animatorY.start();
        }
    }

    public void closeAiUi() {
        this.blank.setVisibility(View.GONE);
        if (this.isShow) {
            Log.i("zdy", "closeAiUi...........");
            this.isShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.contentView, "translationX", 0.0f, this.width);
            translationRight.setDuration(300L);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                @Override
                // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8MainAiFollowConfirmController.this.mainLayout.setVisibility(View.INVISIBLE);
                    ((ViewGroup) X8MainAiFollowConfirmController.this.contentView).removeAllViews();
                }
            });
        }
    }

    public void setMapPoint(MapPointLatLng mapPoint) {
        this.mX8AiFollowPoint2PointExcuteConfirmModule.setMapPoint(mapPoint);
    }

    public void setPoint2PointExcuteListener(IX8Point2PointExcuteListener listener, FcManager fcManager) {
        this.listener = listener;
        this.fcManager = fcManager;
    }
}
