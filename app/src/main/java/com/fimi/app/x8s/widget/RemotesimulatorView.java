package com.fimi.app.x8s.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.tools.ConvertSize;

public class RemotesimulatorView extends View {
    private final int[] doughnutColors;
    private final int midRemoteCenter;
    private float IndexLeft;
    private float IndexRight;
    private float IndexTop;
    private Bitmap RemoteBG;
    private Bitmap RemoteCenter;
    private Bitmap RemoteOutline;
    private Bitmap batteryBG;
    private float batteryBGLeft;
    private Bitmap batteryPressBG;
    private int border;
    private float centerRidusTop;
    private Context context;
    private int currentLeftRemoteIndex;
    private int currentRightRemoteIndex;
    private boolean isBatteryPress;
    private boolean isLandPress;
    private boolean isReturnPress;
    private Bitmap landBG;
    private float landBGLeft;
    private Bitmap landPressBG;
    private float leftCenterRidus;
    private Matrix mMatrix;
    private int marginLeft;
    private int marginTop;
    private Paint paint;
    private Paint paint1;
    private Rect rect;
    private Bitmap returnBG;
    private float returnBGLeft;
    private Bitmap returnPressBG;
    private RectF rf;
    private float rightCenterRidus;
    private float rotateLeftAngle;
    private float rotateRightAngle;
    private float scale;
    private String text;
    private float textLeft;
    private Paint textPaint;

    public RemotesimulatorView(Context context) {
        super(context);
        this.midRemoteCenter = 512;
        this.border = 8;
        this.doughnutColors = new int[]{Color.parseColor("#0000E8FD"), Color.parseColor("#FF00E8FD")};
        this.text = "  ";
    }

    public RemotesimulatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.midRemoteCenter = 512;
        this.border = 8;
        this.doughnutColors = new int[]{Color.parseColor("#0000E8FD"), Color.parseColor("#FF00E8FD")};
        this.text = "  ";
    }

    public RemotesimulatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.midRemoteCenter = 512;
        this.border = 8;
        this.doughnutColors = new int[]{Color.parseColor("#0000E8FD"), Color.parseColor("#FF00E8FD")};
        this.text = "  ";
        this.context = context;
        this.RemoteBG = BitmapFactory.decodeResource(getResources(), R.drawable.img_playback_rc_bg).copy(Bitmap.Config.ARGB_8888, true);
        this.RemoteOutline = BitmapFactory.decodeResource(getResources(), R.drawable.img_playback_rc_scale);
        this.RemoteCenter = BitmapFactory.decodeResource(getResources(), R.drawable.img_palyback_rc_focus);
        this.batteryBG = BitmapFactory.decodeResource(getResources(), R.drawable.x8_playback_rocker_battery_normal);
        this.landBG = BitmapFactory.decodeResource(getResources(), R.drawable.x8_playback_rocker_land_normal);
        this.returnBG = BitmapFactory.decodeResource(getResources(), R.drawable.x8_playback_rocker_return_normal);
        this.batteryPressBG = BitmapFactory.decodeResource(getResources(), R.drawable.x8_playback_rocker_battery_pressed);
        this.landPressBG = BitmapFactory.decodeResource(getResources(), R.drawable.x8_playback_rocker_land_pressed);
        this.returnPressBG = BitmapFactory.decodeResource(getResources(), R.drawable.x8_playback_rocker_return_pressed);
        this.border = ConvertSize.dipToPx(context, this.border);
        this.IndexLeft = this.border;
        this.leftCenterRidus = (this.IndexLeft + (this.RemoteOutline.getWidth() / 2)) - (this.RemoteCenter.getWidth() / 2);
        this.IndexTop = (this.RemoteBG.getHeight() / 2) - (this.RemoteOutline.getWidth() / 2);
        this.IndexRight = (this.RemoteBG.getWidth() - this.RemoteOutline.getWidth()) - this.IndexLeft;
        this.centerRidusTop = (this.RemoteBG.getHeight() / 2) - (this.RemoteCenter.getWidth() / 2);
        this.rightCenterRidus = ((this.RemoteBG.getWidth() - (this.RemoteOutline.getWidth() / 2)) - this.IndexLeft) - (this.RemoteCenter.getWidth() / 2);
        this.batteryBGLeft = (this.RemoteBG.getWidth() / 2) - (this.batteryBG.getWidth() / 2);
        this.landBGLeft = (((this.RemoteBG.getWidth() / 2) + (this.batteryBG.getWidth() / 2)) - (this.landBG.getWidth() / 2)) + (this.border / 2);
        this.returnBGLeft = (((this.RemoteBG.getWidth() / 2) - (this.batteryBG.getWidth() / 2)) - (this.returnBG.getWidth() / 2)) - (this.border / 2);
        this.textLeft = this.RemoteBG.getWidth() / 2;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(-1);
        this.rect = new Rect();
        this.paint1 = new Paint();
        this.paint1.setAntiAlias(true);
        this.paint1.setColor(-1);
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setDither(true);
        this.textPaint.setTextSize(24.0f);
        this.textPaint.setColor(Color.parseColor("#ffffff"));
        this.mMatrix = new Matrix();
        this.rf = new RectF();
        this.scale = ((this.RemoteOutline.getHeight() / 2) * 0.657f) / this.midRemoteCenter;
        this.marginTop = 0;
        this.marginLeft = 0;
    }

    public static double getAngle(float x1, float y1, int x2, int y2) {
        int x = Math.abs((int) (x1 - x2));
        int y = Math.abs((int) (y1 - y2));
        double z = Math.sqrt((x * x) + (y * y));
        return Math.round((float) ((Math.acos(y / z) / 3.141592653589793d) * 180.0d));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = this.RemoteBG.getWidth();
        int height = this.RemoteBG.getHeight();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.RemoteBG, this.marginLeft, this.marginTop, this.paint);
        canvas.drawBitmap(this.RemoteOutline, this.IndexLeft + this.marginLeft, this.IndexTop + this.marginTop, this.paint);
        canvas.drawBitmap(this.RemoteOutline, this.IndexRight + this.marginLeft, this.IndexTop + this.marginTop, this.paint);
        if (this.isReturnPress) {
            canvas.drawBitmap(this.returnPressBG, this.returnBGLeft + this.marginLeft, (this.RemoteBG.getHeight() * 0.6f) + this.marginTop, this.paint1);
        } else {
            canvas.drawBitmap(this.returnBG, this.returnBGLeft + this.marginLeft, (this.RemoteBG.getHeight() * 0.6f) + this.marginTop, this.paint1);
        }
        if (this.isLandPress) {
            canvas.drawBitmap(this.landPressBG, this.landBGLeft + this.marginLeft, (this.RemoteBG.getHeight() * 0.6f) + this.marginTop, this.paint1);
        } else {
            canvas.drawBitmap(this.landBG, this.landBGLeft + this.marginLeft, (this.RemoteBG.getHeight() * 0.6f) + this.marginTop, this.paint1);
        }
        if (this.isBatteryPress) {
            canvas.drawBitmap(this.batteryPressBG, this.batteryBGLeft + this.marginLeft, (this.RemoteBG.getHeight() * 0.6f) + this.marginTop, this.paint1);
        } else {
            canvas.drawBitmap(this.batteryBG, this.batteryBGLeft + this.marginLeft, (this.RemoteBG.getHeight() * 0.6f) + this.marginTop, this.paint1);
        }
        this.paint.setColor(Color.parseColor("#FF00E8FD"));
        this.paint.setShader(new LinearGradient(this.leftCenterRidus + this.marginLeft, (this.RemoteBG.getHeight() / 2) + this.marginTop, this.leftCenterRidus + this.RemoteCenter.getWidth() + this.marginLeft, (this.RemoteBG.getHeight() / 2) - (this.currentLeftRemoteIndex * this.scale), this.doughnutColors, null, Shader.TileMode.MIRROR));
        this.rf.set(this.leftCenterRidus + this.marginLeft, (this.RemoteBG.getHeight() / 2) - (this.currentLeftRemoteIndex * this.scale), this.leftCenterRidus + this.RemoteCenter.getWidth() + this.marginLeft, (this.RemoteBG.getHeight() / 2) + this.marginTop);
        this.mMatrix.postRotate(this.rotateLeftAngle, this.leftCenterRidus + (this.RemoteCenter.getWidth() / 2) + this.marginLeft, this.centerRidusTop + (this.RemoteCenter.getWidth() / 2) + this.marginTop);
        canvas.setMatrix(this.mMatrix);
        canvas.drawRoundRect(this.rf, this.RemoteCenter.getWidth() / 2, this.RemoteCenter.getWidth() / 2, this.paint);
        this.mMatrix.setRotate(0.0f);
        canvas.setMatrix(this.mMatrix);
        this.paint.setShader(new LinearGradient(this.rightCenterRidus + this.marginLeft, (this.RemoteBG.getHeight() / 2) + this.marginTop, this.rightCenterRidus + this.RemoteCenter.getWidth() + this.marginLeft, ((this.RemoteBG.getHeight() / 2) - (this.currentRightRemoteIndex * this.scale)) + this.marginTop, this.doughnutColors, null, Shader.TileMode.MIRROR));
        this.rf.set(this.rightCenterRidus + this.marginLeft, ((this.RemoteBG.getHeight() / 2) - (this.currentRightRemoteIndex * this.scale)) + this.marginTop, this.rightCenterRidus + this.RemoteCenter.getWidth() + this.marginLeft, (this.RemoteBG.getHeight() / 2) + this.marginTop);
        this.mMatrix.postRotate(this.rotateRightAngle, this.rightCenterRidus + (this.RemoteCenter.getWidth() / 2) + this.marginLeft, this.centerRidusTop + (this.RemoteCenter.getWidth() / 2) + this.marginTop);
        canvas.setMatrix(this.mMatrix);
        canvas.drawRoundRect(this.rf, this.RemoteCenter.getWidth() / 2, this.RemoteCenter.getWidth() / 2, this.paint);
        this.mMatrix.setRotate(0.0f);
        canvas.setMatrix(this.mMatrix);
        canvas.drawBitmap(this.RemoteCenter, this.leftCenterRidus + this.marginLeft, this.centerRidusTop + this.marginTop, this.paint);
        canvas.drawBitmap(this.RemoteCenter, this.rightCenterRidus + this.marginLeft, this.centerRidusTop + this.marginTop, this.paint);
        this.textPaint.getTextBounds(this.text, 0, this.text.length(), this.rect);
        canvas.drawText(this.text, (this.textLeft + this.marginLeft) - (this.rect.width() / 2), (this.RemoteBG.getHeight() * 0.3f) + this.marginTop, this.textPaint);
    }

    private void recyleBitMap(@NonNull Bitmap... value) {
        for (Bitmap bitmap : value) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    public void setCurrentRemote(int value0, int value1, int value2, int value3, boolean isBatteryPress, boolean isLandPress, boolean isReturnPress) {
        int distanceLeft;
        int distanceRight;
        this.isBatteryPress = isBatteryPress;
        this.isLandPress = isLandPress;
        this.isReturnPress = isReturnPress;
        double angleLeft = 0.0d;
        if (value1 <= this.midRemoteCenter && value3 >= this.midRemoteCenter) {
            distanceLeft = (int) Math.sqrt(Math.pow(value3 - this.midRemoteCenter, 2.0d) + Math.pow(this.midRemoteCenter - value1, 2.0d));
            if (distanceLeft != 0) {
                angleLeft = getAngle(0.0f, 0.0f, value3 - this.midRemoteCenter, this.midRemoteCenter - value1);
            }
        } else if (value1 <= this.midRemoteCenter && value3 < this.midRemoteCenter) {
            distanceLeft = (int) Math.sqrt(Math.pow(this.midRemoteCenter - value3, 2.0d) + Math.pow(this.midRemoteCenter - value1, 2.0d));
            if (distanceLeft != 0) {
                angleLeft = 360.0d - getAngle(0.0f, 0.0f, this.midRemoteCenter - value3, this.midRemoteCenter - value1);
            }
        } else if (value1 > this.midRemoteCenter && value3 >= this.midRemoteCenter) {
            distanceLeft = (int) Math.sqrt(Math.pow(value3 - this.midRemoteCenter, 2.0d) + Math.pow(value1 - this.midRemoteCenter, 2.0d));
            if (distanceLeft != 0) {
                angleLeft = 180.0d - getAngle(0.0f, 0.0f, value3 - this.midRemoteCenter, value1 - this.midRemoteCenter);
            }
        } else {
            distanceLeft = (int) Math.sqrt(Math.pow(this.midRemoteCenter - value3, 2.0d) + Math.pow(value1 - this.midRemoteCenter, 2.0d));
            if (distanceLeft != 0) {
                angleLeft = 180.0d + getAngle(0.0f, 0.0f, this.midRemoteCenter - value3, value1 - this.midRemoteCenter);
            }
        }
        if (distanceLeft > this.midRemoteCenter) {
            distanceLeft = this.midRemoteCenter;
        }
        this.currentLeftRemoteIndex = distanceLeft;
        this.rotateLeftAngle = (float) angleLeft;
        double angleRight = 0.0d;
        if (value2 <= this.midRemoteCenter && value0 >= this.midRemoteCenter) {
            distanceRight = (int) Math.sqrt(Math.pow(value0 - this.midRemoteCenter, 2.0d) + Math.pow(this.midRemoteCenter - value2, 2.0d));
            if (distanceRight != 0) {
                angleRight = getAngle(0.0f, 0.0f, value0 - this.midRemoteCenter, this.midRemoteCenter - value2);
            }
        } else if (value2 <= this.midRemoteCenter && value0 < this.midRemoteCenter) {
            distanceRight = (int) Math.sqrt(Math.pow(this.midRemoteCenter - value0, 2.0d) + Math.pow(this.midRemoteCenter - value2, 2.0d));
            if (distanceRight != 0) {
                angleRight = 360.0d - getAngle(0.0f, 0.0f, this.midRemoteCenter - value0, this.midRemoteCenter - value2);
            }
        } else if (value2 > this.midRemoteCenter && value0 >= this.midRemoteCenter) {
            distanceRight = (int) Math.sqrt(Math.pow(value0 - this.midRemoteCenter, 2.0d) + Math.pow(value2 - this.midRemoteCenter, 2.0d));
            if (distanceRight != 0) {
                angleRight = 180.0d - getAngle(0.0f, 0.0f, value0 - this.midRemoteCenter, value2 - this.midRemoteCenter);
            }
        } else {
            distanceRight = (int) Math.sqrt(Math.pow(this.midRemoteCenter - value0, 2.0d) + Math.pow(value2 - this.midRemoteCenter, 2.0d));
            if (distanceRight != 0) {
                angleRight = 180.0d + getAngle(0.0f, 0.0f, this.midRemoteCenter - value0, value2 - this.midRemoteCenter);
            }
        }
        if (distanceRight > this.midRemoteCenter) {
            distanceRight = this.midRemoteCenter;
        }
        this.currentRightRemoteIndex = distanceRight;
        this.rotateRightAngle = (float) angleRight;
        invalidate();
    }

    public void showGetRcModeText(String text) {
        this.text = text;
        invalidate();
    }

    public void showDefaultView() {
        this.text = " ";
        setCurrentRemote(512, 512, 512, 512, false, false, false);
    }
}
