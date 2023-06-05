package com.fimi.app.x8s.tensortfloow;

import android.graphics.Bitmap;

import androidx.core.view.ViewCompat;


public class H264ToBitmap {
    public static Bitmap rgb2Bitmap(byte[] data, int width, int height) {
        int[] colors = convertByteToColor(data);
        if (colors == null) {
            return null;
        }
        return Bitmap.createBitmap(colors, 0, width, width, height, Bitmap.Config.ARGB_8888);
    }

    public static int[] convertByteToColor(byte[] data) {
        int size = data.length;
        if (size == 0) {
            return null;
        }
        int[] color = new int[size / 4];
        int colorLen = color.length;
        for (int i = 0; i < colorLen; i++) {
            int red = data[i * 4] & 255;
            int green = data[(i * 4) + 1] & 255;
            int blue = data[(i * 4) + 2] & 255;
            color[i] = (red << 16) | (green << 8) | blue | ViewCompat.MEASURED_STATE_MASK;
        }
        return color;
    }
}
