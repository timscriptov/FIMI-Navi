package com.fimi.app.x8s.tensortfloow;

import android.graphics.Matrix;

import androidx.annotation.NonNull;


public class ImageUtils {
    @NonNull
    public static Matrix getTransformationMatrix(int srcWidth, int srcHeight, int dstWidth, int dstHeight, int applyRotation, boolean maintainAspectRatio) {
        Matrix matrix = new Matrix();
        if (applyRotation != 0) {
            matrix.postTranslate((-srcWidth) / 2.0f, (-srcHeight) / 2.0f);
            matrix.postRotate(applyRotation);
        }
        boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;
        int inWidth = transpose ? srcHeight : srcWidth;
        int inHeight = transpose ? srcWidth : srcHeight;
        if (inWidth != dstWidth || inHeight != dstHeight) {
            float scaleFactorX = (float) dstWidth / inWidth;
            float scaleFactorY = (float) dstHeight / inHeight;
            if (maintainAspectRatio) {
                float scaleFactor = Math.max(scaleFactorX, scaleFactorY);
                matrix.postScale(scaleFactor, scaleFactor);
            } else {
                matrix.postScale(scaleFactorX, scaleFactorY);
            }
        }
        if (applyRotation != 0) {
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
        }
        return matrix;
    }
}
