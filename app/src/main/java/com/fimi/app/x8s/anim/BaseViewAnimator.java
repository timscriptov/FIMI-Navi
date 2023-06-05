package com.fimi.app.x8s.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.core.view.ViewCompat;

import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class BaseViewAnimator {
    public static final long DURATION = 1000;
    private long mDuration = 1000;
    private int mRepeatTimes = 0;
    private int mRepeatMode = 1;
    private AnimatorSet mAnimatorSet = new AnimatorSet();

    protected abstract void prepare(View view);

    public BaseViewAnimator setTarget(View target) {
        reset(target);
        prepare(target);
        return this;
    }

    public void animate() {
        start();
    }

    public void restart() {
        this.mAnimatorSet = this.mAnimatorSet.clone();
        start();
    }

    public void reset(View target) {
        ViewCompat.setAlpha(target, 1.0f);
        ViewCompat.setScaleX(target, 1.0f);
        ViewCompat.setScaleY(target, 1.0f);
        ViewCompat.setTranslationX(target, 0.0f);
        ViewCompat.setTranslationY(target, 0.0f);
        ViewCompat.setRotation(target, 0.0f);
        ViewCompat.setRotationY(target, 0.0f);
        ViewCompat.setRotationX(target, 0.0f);
    }

    public void start() {
        Iterator<Animator> it = this.mAnimatorSet.getChildAnimations().iterator();
        while (it.hasNext()) {
            Animator animator = it.next();
            if (animator instanceof ValueAnimator) {
                ((ValueAnimator) animator).setRepeatCount(this.mRepeatTimes);
                ((ValueAnimator) animator).setRepeatMode(this.mRepeatMode);
            }
        }
        this.mAnimatorSet.setDuration(this.mDuration);
        this.mAnimatorSet.start();
    }

    public long getStartDelay() {
        return this.mAnimatorSet.getStartDelay();
    }

    public BaseViewAnimator setStartDelay(long delay) {
        getAnimatorAgent().setStartDelay(delay);
        return this;
    }

    public BaseViewAnimator addAnimatorListener(Animator.AnimatorListener l) {
        this.mAnimatorSet.addListener(l);
        return this;
    }

    public void cancel() {
        this.mAnimatorSet.cancel();
    }

    public boolean isRunning() {
        return this.mAnimatorSet.isRunning();
    }

    public boolean isStarted() {
        return this.mAnimatorSet.isStarted();
    }

    public void removeAnimatorListener(Animator.AnimatorListener l) {
        this.mAnimatorSet.removeListener(l);
    }

    public void removeAllListener() {
        this.mAnimatorSet.removeAllListeners();
    }

    public BaseViewAnimator setInterpolator(Interpolator interpolator) {
        this.mAnimatorSet.setInterpolator(interpolator);
        return this;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public BaseViewAnimator setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }

    public AnimatorSet getAnimatorAgent() {
        return this.mAnimatorSet;
    }

    public BaseViewAnimator setRepeatTimes(int repeatTimes) {
        this.mRepeatTimes = repeatTimes;
        return this;
    }

    public BaseViewAnimator setRepeatMode(int repeatMode) {
        this.mRepeatMode = repeatMode;
        return this;
    }
}
