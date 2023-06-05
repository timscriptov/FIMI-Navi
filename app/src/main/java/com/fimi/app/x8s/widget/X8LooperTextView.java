package com.fimi.app.x8s.widget;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.fimi.app.x8s.anim.FlashAnimator;
import com.fimi.app.x8s.anim.YoYo;

import java.util.List;

public class X8LooperTextView extends FrameLayout {
    private final int ANIM_DELAYED_MILLIONS;
    private final int ANIM_DURATION;
    private final String DEFAULT_TEXT_COLOR;
    private final int DEFAULT_TEXT_SIZE;
    private final String TIP_PREFIX;
    private Animation anim_in;
    private Animation anim_out;
    private int curTipIndex;
    private long lastTimeMillis;
    private Drawable leftDrawable;
    private boolean needFlash;
    private List<String> tipList;
    private TextView tv_tip_in;
    private TextView tv_tip_out;

    public X8LooperTextView(Context context) {
        super(context);
        this.curTipIndex = 0;
        this.ANIM_DELAYED_MILLIONS = PathInterpolatorCompat.MAX_NUM_POINTS;
        this.ANIM_DURATION = 1000;
        this.DEFAULT_TEXT_SIZE = 14;
        this.DEFAULT_TEXT_COLOR = "#FFFFFF";
        this.TIP_PREFIX = "";
        this.needFlash = false;
        initTipFrame();
        initAnimation();
    }

    public X8LooperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.curTipIndex = 0;
        this.ANIM_DELAYED_MILLIONS = PathInterpolatorCompat.MAX_NUM_POINTS;
        this.ANIM_DURATION = 1000;
        this.DEFAULT_TEXT_SIZE = 14;
        this.DEFAULT_TEXT_COLOR = "#FFFFFF";
        this.TIP_PREFIX = "";
        this.needFlash = false;
        initTipFrame();
        initAnimation();
    }

    public X8LooperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.curTipIndex = 0;
        this.ANIM_DELAYED_MILLIONS = PathInterpolatorCompat.MAX_NUM_POINTS;
        this.ANIM_DURATION = 1000;
        this.DEFAULT_TEXT_SIZE = 14;
        this.DEFAULT_TEXT_COLOR = "#FFFFFF";
        this.TIP_PREFIX = "";
        this.needFlash = false;
        initTipFrame();
        initAnimation();
    }

    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public void setDrawableLeft(int resId) {
        this.leftDrawable = loadDrawable(resId);
    }

    private void initTipFrame() {
        this.tv_tip_out = newTextView();
        this.tv_tip_in = newTextView();
        addView(this.tv_tip_in);
        addView(this.tv_tip_out);
    }

    @NonNull
    private TextView newTextView() {
        TextView textView = new TextView(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1, 16);
        textView.setLayoutParams(lp);
        textView.setCompoundDrawablePadding(10);
        textView.setPadding(10, 0, 0, 0);
        textView.setGravity(16);
        textView.setLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(1, 14.0f);
        return textView;
    }

    @NonNull
    private Drawable loadDrawable(int ResId) {
        Drawable drawable = getResources().getDrawable(ResId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth() - 10, drawable.getMinimumHeight() - 10);
        return drawable;
    }

    private void initAnimation() {
        this.anim_out = newAnimation(0.0f, -1.0f);
        this.anim_in = newAnimation(1.0f, 0.0f);
        this.anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                X8LooperTextView.this.updateTipAndPlayAnimationWithCheck();
            }
        });
    }

    @NonNull
    private Animation newAnimation(float fromYValue, float toYValue) {
        Animation anim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, fromYValue, 1, toYValue);
        anim.setDuration(1000L);
        anim.setStartOffset(3000L);
        anim.setInterpolator(new DecelerateInterpolator());
        return anim;
    }

    public void updateTipAndPlayAnimationWithCheck() {
        if (System.currentTimeMillis() - this.lastTimeMillis >= 1000) {
            this.lastTimeMillis = System.currentTimeMillis();
            updateTipAndPlayAnimation();
        }
    }

    private void updateTipAndPlayAnimation() {
        if (this.curTipIndex % 2 == 0) {
            updateTip(this.tv_tip_out);
            this.tv_tip_in.startAnimation(this.anim_out);
            this.tv_tip_out.startAnimation(this.anim_in);
            bringChildToFront(this.tv_tip_in);
        } else {
            updateTip(this.tv_tip_in);
            this.tv_tip_out.startAnimation(this.anim_out);
            this.tv_tip_in.startAnimation(this.anim_in);
            bringChildToFront(this.tv_tip_out);
        }
        if (this.needFlash) {
            setFlashAnimator(this);
        }
    }

    public void needFlash(boolean needFlash) {
        this.needFlash = needFlash;
    }

    private void updateTip(TextView tipView) {
        if (this.leftDrawable != null) {
            tipView.setCompoundDrawables(this.leftDrawable, null, null, null);
        }
        String tip = getNextTip();
        if (!TextUtils.isEmpty(tip)) {
            tipView.setText(tip + "");
        }
    }

    @Nullable
    private String getNextTip() {
        if (isListEmpty(this.tipList)) {
            return null;
        }
        List<String> list = this.tipList;
        int i = this.curTipIndex;
        this.curTipIndex = i + 1;
        return list.get(i % this.tipList.size());
    }

    public void setTipList(List<String> tipList) {
        this.tipList = tipList;
        this.curTipIndex = 0;
        updateTip(this.tv_tip_out);
        updateTipAndPlayAnimation();
    }

    private void setFlashAnimator(View view) {
        FlashAnimator flashAnimator = new FlashAnimator();
        YoYo.with(flashAnimator).duration(1200L).repeat(1).pivot(Float.MAX_VALUE, Float.MAX_VALUE).interpolate(new AccelerateDecelerateInterpolator()).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).playOn(view);
    }
}
