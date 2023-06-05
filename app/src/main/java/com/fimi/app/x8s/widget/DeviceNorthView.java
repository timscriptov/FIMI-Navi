package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.tools.StringHelper;

/**
 * После компиляции замени в smali класс полностью
 */
public class DeviceNorthView extends View {
    public final Context mContext;
    // Расстояние гипотенузы от дрона до цели
    public float aircraftAzimuth = 0f;
    // Расстояние катета от дрона до цели
    public float distance = 0f;
    // Высота дрона. Расстояние от земли до дрона под уголом 90 градусов
    public float droneHeight = 0f;
    // Угол камеры
    public float cameraAngle = 0f;
    private int a;
    private int mMin;
    private int mMax;
    private float mDensity;
    private final RectF e;
    private Paint mCircle;
    private float north;
    private int h;
    private int i;
    private float j;
    private Drawable drawable;
    private int l;
    private int m;
    private float radianAngle;
    private float northText = 0f;
    private float aircraft = 0f;
    private double bankAngle = 0;

    public DeviceNorthView(Context context) {
        super(context);
        mContext = context;
        this.a = 0;
        this.mMin = 0;
        this.mMax = 100;
        this.mDensity = 1.0f;
        this.e = new RectF();
        a(context, null);
    }

    public DeviceNorthView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        this.a = 0;
        this.mMin = 0;
        this.mMax = 100;
        this.mDensity = 1.0f;
        this.e = new RectF();
        a(context, attributeSet);
    }

    /**
     * Вернет true еслр текст начинается с цифры
     */
    public static boolean isFirstCharDigit(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char firstChar = str.charAt(0);
        return Character.isDigit(firstChar);
    }

    /**
     * Ищем корень макета, поднимаясь вверх по родителям, прка не найдет вьюху с id android.R.id.content
     */
    public static FrameLayout findRootView(View view) {
        if (view == null) {
            return null;
        }
        if (view.getId() == android.R.id.content && view instanceof FrameLayout) {
            return (FrameLayout) view;
        }
        ViewParent parent = view.getParent();
        if (parent instanceof View) {
            return findRootView((View) parent);
        }
        return null;
    }

    private void a(@NonNull Context context, AttributeSet attributeSet) {
        float f = context.getResources().getDisplayMetrics().density;
        int color = getResources().getColor(R.color.x8s_main_north);
        this.mDensity = f * this.mDensity;
        this.drawable = getResources().getDrawable(R.drawable.x8s_main_north);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DrawableStyleable, 0, 0);
            this.l = obtainStyledAttributes.getDimensionPixelSize(3, 50);
            this.mMin = obtainStyledAttributes.getInteger(1, this.mMin);
            this.mMax = obtainStyledAttributes.getInteger(0, this.mMax);
            this.m = (((((getPaddingLeft() + getPaddingRight()) + getPaddingBottom()) + getPaddingTop()) + getPaddingEnd()) + getPaddingStart()) / 6;
            obtainStyledAttributes.recycle();
        }
        int i = this.a;
        int i2 = this.mMax;
        if (i > i2) {
            i = i2;
        }
        this.a = i;
        int i3 = this.a;
        int i4 = this.mMin;
        if (i3 < i4) {
            i3 = i4;
        }
        this.a = i3;

        this.north = this.a / a();
        this.radianAngle = (float) (1.5707963267948966d - ((this.north * 3.141592653589793d) / 180.0d));
        this.mCircle = new Paint();
        this.mCircle.setColor(color);
        this.mCircle.setAntiAlias(true);
        this.mCircle.setStyle(Paint.Style.STROKE);
        this.mCircle.setStrokeWidth(this.mDensity);
    }

    public int getMax() {
        return this.mMax;
    }

    public int getMin() {
        return this.mMin;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.drawCircle(this.h, this.i, this.j, this.mCircle);
        if (this.drawable instanceof RotateDrawable) {
            int cos = (int) (this.h + (this.j * Math.cos(this.radianAngle)));
            int sin = (int) (this.i - (this.j * Math.sin(this.radianAngle)));
            Drawable drawable = this.drawable;
            int i = this.l;
            drawable.setBounds(cos - (i / 2), sin - (i / 2), cos + (i / 2), sin + (i / 2));
            this.drawable.draw(canvas);

            drawNorth(canvas);
            drawAircraft(canvas);
            drawAircraftAzimuth(canvas);
            drawBankAngle(canvas);
            drawDistance(canvas);
        }
    }

    /*
     * Сверху влево - север
     */
    private void drawNorth(Canvas canvas) {
        String text = "П: " + (int) this.northText;

        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(getTypeface());
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(13 * getResources().getDisplayMetrics().density);
        textPaint.setColor(0xFFFDFDFD);

        int width = (int) textPaint.measureText(text);
        StaticLayout staticLayout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        staticLayout.draw(canvas);
    }

    public void setNorthAngle(float f) {
        float f2 = (-f) + 180.0f + 90.0f;
        if (f2 < 0.0f) {
            f2 += 360.0f;
        }
        this.a = (int) ((f2 / 1.2d) / 3.0d);
        int i = this.a;
        int i2 = this.mMax;
        if (i > i2) {
            i = i2;
        }
        this.a = i;
        int i3 = this.a;
        int i4 = this.mMin;
        if (i3 < i4) {
            i3 = i4;
        }
        this.a = i3;
        final float n = this.a / a();
        this.north = n;
        northText = 360 - n;
        this.radianAngle = (float) (Math.PI / 2 - ((this.north * Math.PI) / 180.0d));
        updateDistance();
        invalidate();
    }

    /*
     * Сверху справа - камера
     */
    private void drawAircraftAzimuth(@NonNull Canvas canvas) {
        String text = "К: " + (int) this.aircraftAzimuth;

        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(getTypeface());
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(13 * getResources().getDisplayMetrics().density);
        textPaint.setColor(0xFFFDFDFD);

        int width = (int) textPaint.measureText(text);
        StaticLayout staticLayout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);

        canvas.save();
        //canvas.translate(140, 0);
        canvas.translate(0, 50);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    public void setAircraftAzimuth(int f) {
        if (f < 0) {
            aircraftAzimuth = f + 360;
        } else {
            aircraftAzimuth = f;
        }
        updateDistance();
        invalidate();
    }

    /*
     * Снизу справа - крен
     */
    private void drawBankAngle(@NonNull Canvas canvas) {
        String text = "В: " + (int) this.bankAngle;

        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(getTypeface());
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(13 * getResources().getDisplayMetrics().density);
        textPaint.setColor(0xFFFDFDFD);

        int width = (int) textPaint.measureText(text);
        StaticLayout staticLayout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);

        canvas.save();
        //canvas.translate(140, 10 + canvas.getHeight() - staticLayout.getHeight());
        canvas.translate(0, 50 * 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    public void setBankAngle(double f) {
        bankAngle = f;
        updateDistance();
        invalidate();
    }

    /*
     * Снизу слева - аз.бвс
     */
    private void drawAircraft(@NonNull Canvas canvas) {
        String text = "У: " + (int) this.aircraft;

        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(getTypeface());
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(13 * getResources().getDisplayMetrics().density);
        textPaint.setColor(0xFFFDFDFD);

        int width = (int) textPaint.measureText(text);
        StaticLayout staticLayout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);

        canvas.save();
        //canvas.translate(0, 10 + canvas.getHeight() - staticLayout.getHeight());
        canvas.translate(0, 50 * 3);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    public void setAircraft(int f) {
        aircraft = f;
        updateDistance();
        invalidate();
    }

    private void drawDistance(@NonNull Canvas canvas) {
        String text = "Д: " + (int) this.distance;

        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(getTypeface());
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(13 * getResources().getDisplayMetrics().density);
        textPaint.setColor(0xFFFDFDFD);

        int width = (int) textPaint.measureText(text);
        StaticLayout staticLayout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);

        canvas.save();
        //canvas.translate(0, 10 + canvas.getHeight() - staticLayout.getHeight());
        canvas.translate(0, 50 * 4);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    private Typeface getTypeface() {
        AssetManager am = mContext.getAssets();
        return Typeface.createFromAsset(am, "fonts/myfont.ttf");
    }

    public void updateDistance() {
        final FrameLayout frameLayout = findRootView(this);
        if (frameLayout != null) {
            final TextView tvDistance = frameLayout.findViewById(R.id.tv_hight);
            if (tvDistance != null) {
                // Получаем текст у вьюхи
                final String distanceText = tvDistance.getText().toString().trim();
                // Если текст начинается с цифры
                if (distanceText.charAt(0) == '-' || isFirstCharDigit(distanceText)) {
                    // Извлекаем число из текста
                    final float hight = StringHelper.parseFloat(distanceText);
                    droneHeight = hight;
                    // Ищем TextView с id R.id.tv_cloud
                    final TextView tvCloud = frameLayout.findViewById(R.id.tv_bottom_cloud);
                    if (tvCloud != null) {
                        // Получаем текст у вьюхи
                        final String cloudText = tvCloud.getText().toString().trim();
                        // Если текст начинается с цифры
                        if (cloudText.charAt(0) == '-' || isFirstCharDigit(cloudText)) {
                            // Извлекаем число из текста
                            final float course = StringHelper.parseFloat(cloudText);
                            cameraAngle = course;

                            if (course > -5 && course < 10) {
                                distance = 0;
                                return;
                            }
                            final double dist = hight / Math.tan((course * (-1)) * Math.PI / 180);
                            distance = (float) dist;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        int min = Math.min(i, i2);
        int i5 = ((i - min) / 2) + min;
        int i6 = ((i2 - min) / 2) + min;
        this.h = (i5 / 2) + ((i - i5) / 2);
        this.i = (i6 / 2) + ((i2 - i6) / 2);
        float f = min - this.m;
        this.j = (float) (f / 2.05d);
        float f2 = f / 2.0f;
        float f3 = (i2 >> 1) - f2;
        float f4 = (i >> 1) - f2;
        this.e.set(f4, f3, f4 + f, f + f3);
        super.onSizeChanged(i, i2, i3, i4);
    }

    private float a() {
        return this.mMax / 360.0f;
    }
}
