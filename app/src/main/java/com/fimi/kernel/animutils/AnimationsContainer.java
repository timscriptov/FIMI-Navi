package com.fimi.kernel.animutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.widget.ImageView;

import com.fimi.kernel.base.BaseApplication;

import java.lang.ref.SoftReference;


public class AnimationsContainer {
    private static AnimationsContainer mInstance;
    public int FPS = 58;
    private final Context mContext = BaseApplication.getContext();
    private int resId;

    public static AnimationsContainer getInstance(int resId, int fps) {
        if (mInstance == null) {
            mInstance = new AnimationsContainer();
        }
        mInstance.setResId(resId, fps);
        return mInstance;
    }

    public void setResId(int resId, int fps) {
        this.resId = resId;
        this.FPS = fps;
    }

    public FramesSequenceAnimation createProgressDialogAnim(ImageView imageView) {
        return new FramesSequenceAnimation(imageView, getData(this.resId), this.FPS);
    }

    private int[] getData(int resId) {
        TypedArray array = this.mContext.getResources().obtainTypedArray(resId);
        int len = array.length();
        int[] intArray = new int[array.length()];
        for (int i = 0; i < len; i++) {
            intArray[i] = array.getResourceId(i, 0);
        }
        array.recycle();
        return intArray;
    }


    public interface OnAnimationStoppedListener {
        void AnimationStopped();
    }


    public class FramesSequenceAnimation {
        private Bitmap mBitmap;
        private BitmapFactory.Options mBitmapOptions;
        private final int[] mFrames;
        private OnAnimationStoppedListener mOnAnimationStoppedListener;
        private final SoftReference<ImageView> mSoftReferenceImageView;
        private final Handler mHandler = new Handler();
        private int mIndex = -1;
        private boolean mShouldRun = false;
        private boolean mIsRunning = false;
        private final int mDelayMillis = 100;

        public FramesSequenceAnimation(ImageView imageView, int[] frames, int fps) {
            this.mBitmap = null;
            this.mFrames = frames;
            this.mSoftReferenceImageView = new SoftReference<>(imageView);
            imageView.setImageResource(this.mFrames[0]);
            if (Build.VERSION.SDK_INT >= 11) {
                Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                int width = bmp.getWidth();
                int height = bmp.getHeight();
                Bitmap.Config config = bmp.getConfig();
                this.mBitmap = Bitmap.createBitmap(width, height, config);
                this.mBitmapOptions = new BitmapFactory.Options();
                this.mBitmapOptions.inBitmap = this.mBitmap;
                this.mBitmapOptions.inMutable = true;
                this.mBitmapOptions.inSampleSize = 1;
                this.mBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            }
        }

        public boolean isShouldRun() {
            return this.mShouldRun;
        }

        public int getNext() {
            this.mIndex++;
            if (this.mIndex >= this.mFrames.length) {
                this.mIndex = 0;
            }
            return this.mFrames[this.mIndex];
        }

        public synchronized void start() {
            this.mShouldRun = true;
            if (!this.mIsRunning) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView = FramesSequenceAnimation.this.mSoftReferenceImageView.get();
                        if (!FramesSequenceAnimation.this.mShouldRun || imageView == null) {
                            FramesSequenceAnimation.this.mIsRunning = false;
                            if (FramesSequenceAnimation.this.mOnAnimationStoppedListener != null) {
                                FramesSequenceAnimation.this.mOnAnimationStoppedListener.AnimationStopped();
                                return;
                            }
                            return;
                        }
                        FramesSequenceAnimation.this.mIsRunning = true;
                        FramesSequenceAnimation.this.mHandler.postDelayed(this, FramesSequenceAnimation.this.mDelayMillis);
                        if (imageView.isShown()) {
                            int imageRes = FramesSequenceAnimation.this.getNext();
                            if (FramesSequenceAnimation.this.mBitmap != null) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = BitmapFactory.decodeResource(imageView.getResources(), imageRes, FramesSequenceAnimation.this.mBitmapOptions);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (bitmap != null) {
                                    imageView.setImageBitmap(bitmap);
                                    return;
                                }
                                imageView.setImageResource(imageRes);
                                FramesSequenceAnimation.this.mBitmap.recycle();
                                FramesSequenceAnimation.this.mBitmap = null;
                                return;
                            }
                            imageView.setImageResource(imageRes);
                        }
                    }
                };
                this.mHandler.post(runnable);
            }
        }

        public synchronized void stop() {
            this.mShouldRun = false;
        }

        public void setOnAnimStopListener(OnAnimationStoppedListener listener) {
            this.mOnAnimationStoppedListener = listener;
        }
    }
}
