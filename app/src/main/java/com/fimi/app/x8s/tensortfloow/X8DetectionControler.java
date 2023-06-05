package com.fimi.app.x8s.tensortfloow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.SystemClock;

import com.fimi.TcpClient;
import com.fimi.app.x8s.media.FimiH264Video;
import com.fimi.app.x8s.media.OnX8VideoFrameBufferListener;
import com.fimi.app.x8s.widget.X8TrackOverlayView;
import com.fimi.kernel.FimiAppContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/* loaded from: classes.dex */
public class X8DetectionControler implements OnX8VideoFrameBufferListener {
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.2f;
    private static final int TF_OD_API_INPUT_SIZE = 300;
    private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/coco_labels_list.txt";
    private static final String TF_OD_API_MODEL_FILE = "detect_ssd_new.mp3";
    int cropSize = 300;
    private int SIZE_HEIGHT;
    private int SIZE_WIDTH;
    private Bitmap bitmap;
    private Integer classfier;
    private Activity context;
    private Matrix cropToFrameTransform;
    private Classifier detector;
    private Matrix frameToCropTransform;
    private boolean isFirstWaiting;
    private boolean isRev;
    private boolean isTouch;
    private long lastProcessingTimeMs;
    private onDetectionListener listener;
    private FimiH264Video mFimiH264Video;
    private int mFrameHeight;
    private int mFrameWidth;
    private X8TrackOverlayView overlay;
    private int rectH;
    private int rectW;
    private int rectX;
    private int rectY;
    private TestOverlay testoverlay;
    private boolean TF_OD_API_IS_QUANTIZED = true;
    private boolean isLog = true;
    private Bitmap croppedBitmap = null;
    private String objTitle = "";
    private int VIDEO_WIDTH = FimiAppContext.UI_HEIGHT;
    private int VIDEO_HEIGHT = FimiAppContext.UI_WIDTH;
    private int MAX_UNSIGNED_SHORT = 65535;
    private boolean isFirst = true;

    public void initView(Activity context, X8TrackOverlayView overlay, FimiH264Video mFimiH264Video, TestOverlay testoverlay, onDetectionListener listener) {
        this.context = context;
        this.overlay = overlay;
        this.testoverlay = testoverlay;
        this.listener = listener;
        overlay.setCustomOverlay(false);
        this.mFimiH264Video = mFimiH264Video;
        mFimiH264Video.setX8VideoFrameBufferListener(this);
        initTensortfloow();
        this.bitmap = Bitmap.createBitmap(this.VIDEO_WIDTH, this.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.fimi.app.x8s.tensortfloow.X8DetectionControler$1] */
    private void initTensortfloow() {
        new Thread() { // from class: com.fimi.app.x8s.tensortfloow.X8DetectionControler.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                if (X8DetectionControler.this.detector == null) {
                    try {
                        X8DetectionControler.this.detector = TFLiteObjectDetectionAPIModel.create(X8DetectionControler.this.context.getAssets(), X8DetectionControler.TF_OD_API_MODEL_FILE, X8DetectionControler.TF_OD_API_LABELS_FILE, 300, X8DetectionControler.this.TF_OD_API_IS_QUANTIZED);
                    } catch (IOException e) {
                    }
                }
            }
        }.start();
    }

    public void onTouchActionUp(int x, int y, int w, int h, int x1, int y1, int x2, int y2) {
        if (w >= 0 && h >= 0) {
            if (w < 25 || h < 25) {
                w = 0;
                h = 0;
            }
            this.rectX = x;
            this.rectY = y;
            this.rectW = w;
            this.rectH = h;
            this.mFrameWidth = this.VIDEO_WIDTH;
            this.mFrameHeight = this.VIDEO_HEIGHT;
        } else {
            this.overlay.cleanTrackerRect();
        }
        if (this.detector != null && !this.isRev && !this.isFirstWaiting && !this.isTouch) {
            this.isRev = true;
            this.isTouch = true;
            this.mFimiH264Video.setEnableCallback(1);
        }
    }

    public void onTouchActionDown() {
    }

    @Override // com.fimi.app.x8s.media.OnX8VideoFrameBufferListener
    public void onFrameBuffer(byte[] rgb) {
        if (this.isRev && this.isTouch) {
            this.bitmap.setPixels(H264ToBitmap.convertByteToColor(rgb), 0, this.VIDEO_WIDTH, 0, 0, this.VIDEO_WIDTH, this.VIDEO_HEIGHT);
            this.isRev = false;
            this.mFimiH264Video.setEnableCallback(0);
            runThread();
        }
    }

    @Override // com.fimi.app.x8s.media.OnX8VideoFrameBufferListener
    public void onH264Frame(ByteBuffer buffer) {
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.fimi.app.x8s.tensortfloow.X8DetectionControler$2] */
    public void runThread() {
        if (this.isTouch && !this.isFirstWaiting) {
            this.isFirstWaiting = true;
            new Thread() { // from class: com.fimi.app.x8s.tensortfloow.X8DetectionControler.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    X8DetectionControler.this.runTf();
                }
            }.start();
        }
    }

    public Bitmap cropBitmap(Bitmap src, int x, int y, int width, int height, boolean isRecycle) {
        if (x != 0 || y != 0 || width != src.getWidth() || height != src.getHeight()) {
            Bitmap dst = Bitmap.createBitmap(src, x, y, width, height);
            if (isRecycle && dst != src) {
                src.recycle();
            }
            return dst;
        }
        return src;
    }

    boolean RectOverlap(RectF r, RectF rOther) {
        return r.right > rOther.left && rOther.right > r.left && r.bottom > rOther.top && rOther.bottom > r.top;
    }

    public void onResult(float x1, float y1, float x2, float y2, boolean b, int classfier) {
        if (this.listener != null) {
            float preX = ((1.0f * x1) / this.overlay.getMaxWidth()) * this.MAX_UNSIGNED_SHORT;
            float preY = ((1.0f * y1) / this.overlay.getMaxHeight()) * this.MAX_UNSIGNED_SHORT;
            float preW = (((x2 - x1) * 1.0f) / this.overlay.getMaxWidth()) * this.MAX_UNSIGNED_SHORT;
            float preH = (((y2 - y1) * 1.0f) / this.overlay.getMaxHeight()) * this.MAX_UNSIGNED_SHORT;
            this.listener.onDetectionResult((int) preX, (int) preY, (int) preW, (int) preH, classfier);
        }
    }

    public void onResult2(float x1, float y1, float x2, float y2, boolean b, int classfier) {
        if (this.listener != null) {
            float preX = ((1.0f * x1) / this.overlay.getMaxWidth()) * this.MAX_UNSIGNED_SHORT;
            float preY = ((1.0f * y1) / this.overlay.getMaxHeight()) * this.MAX_UNSIGNED_SHORT;
            float preW = ((1.0f * x2) / this.overlay.getMaxWidth()) * this.MAX_UNSIGNED_SHORT;
            float preH = ((1.0f * y2) / this.overlay.getMaxHeight()) * this.MAX_UNSIGNED_SHORT;
            this.listener.onDetectionResult((int) preX, (int) preY, (int) preW, (int) preH, classfier);
        }
    }

    private float getScaleX(int f, int c) {
        if (f <= c) {
            c = f;
            f = c;
        }
        float scaleX = c / f;
        return scaleX;
    }

    private float getScaleX1(int f, int c) {
        float scaleX = c / f;
        return scaleX;
    }

    public void runTf() {
        if (this.SIZE_WIDTH == 0) {
            this.SIZE_WIDTH = this.overlay.getMaxWidth();
            this.SIZE_HEIGHT = this.overlay.getMaxHeight();
        }
        tfLiteApiModel();
    }

    public void tfLiteApiModel() {
        boolean bFound;
        if (this.isTouch) {
            int x1 = 0;
            int y1 = 0;
            int x2 = 0;
            int y2 = 0;
            int isDianXuan = 0;
            float scaleFrameX = getScaleX(this.mFrameWidth, this.SIZE_WIDTH);
            float scaleFrameY = getScaleX(this.mFrameHeight, this.SIZE_HEIGHT);
            if (this.rectH == 0 || this.rectW == 0) {
                isDianXuan = 1;
                x1 = ((int) (this.rectX * scaleFrameX)) - (this.cropSize / 2);
                y1 = ((int) (this.rectY * scaleFrameY)) - (this.cropSize / 2);
                x2 = ((int) (this.rectX * scaleFrameX)) + (this.cropSize / 2);
                y2 = ((int) (this.rectY * scaleFrameY)) + (this.cropSize / 2);
                if (x1 < 0) {
                    x2 -= x1;
                    x1 = 0;
                }
                if (y1 < 0) {
                    y2 -= y1;
                    y1 = 0;
                }
                if (x2 > this.mFrameWidth) {
                    x1 -= x2 - this.mFrameWidth;
                    x2 = this.mFrameWidth;
                }
                if (y2 > this.mFrameHeight) {
                    y1 -= y2 - this.mFrameHeight;
                    y2 = this.mFrameHeight;
                }
            }
            int targetX = 0;
            int targetY = 0;
            int targetW = 0;
            int targetH = 0;
            if (isDianXuan == 1) {
                if (this.croppedBitmap == null) {
                    this.croppedBitmap = Bitmap.createBitmap(this.cropSize, this.cropSize, Bitmap.Config.ARGB_8888);
                }
                this.frameToCropTransform = ImageUtils.getTransformationMatrix(x2 - x1, y2 - y1, this.cropSize, this.cropSize, this.context.getRequestedOrientation(), false);
                this.cropToFrameTransform = new Matrix();
                this.frameToCropTransform.invert(this.cropToFrameTransform);
                RectF roiRect = new RectF(this.rectX, this.rectY, this.rectX + this.rectW, this.rectY + this.rectH);
                if (this.isLog) {
                    TcpClient.getIntance().sendLog("选框屏幕坐标：-<" + roiRect.toString() + "->    ");
                }
                try {
                    RectF roiRect2 = new RectF(x1, y1, x2, y2);
                    try {
                        if (this.isLog) {
                            TcpClient.getIntance().sendLog("识别图片在PFV坐标： -<" + roiRect2 + "->   ");
                        }
                        Bitmap roibitmap = cropBitmap(this.bitmap, x1, y1, x2 - x1, y2 - y1, false);
                        Canvas canvas = new Canvas(this.croppedBitmap);
                        canvas.drawBitmap(roibitmap, this.frameToCropTransform, null);
                        roibitmap.recycle();
                    } catch (Exception e) {
                        e = e;
                        e.printStackTrace();
                        TcpClient.getIntance().sendLog("roiRect " + e.toString());
                        long startTime = SystemClock.uptimeMillis();
                        List<Classifier.Recognition> results = this.detector.recognizeImage(this.croppedBitmap);
                        this.lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
                        this.objTitle = "";
                        RectF roiRect3 = new RectF(this.rectX, this.rectY, this.rectX + this.rectW, this.rectY + this.rectH);
                        int x = (int) (roiRect3.left * scaleFrameX);
                        int y = (int) (roiRect3.top * scaleFrameY);
                        int w = (int) (roiRect3.right * scaleFrameX);
                        int h = (int) (roiRect3.bottom * scaleFrameY);
                        RectF touchRoi = new RectF(x, y, w, h);
                        if (this.isLog) {
                        }
                        if (this.isLog) {
                        }
                        bFound = false;
                        if (bFound) {
                        }
                        this.isTouch = false;
                        this.isFirst = false;
                        this.isRev = false;
                        this.isFirstWaiting = false;
                    }
                } catch (Exception e2) {
                }
                long startTime2 = SystemClock.uptimeMillis();
                List<Classifier.Recognition> results2 = this.detector.recognizeImage(this.croppedBitmap);
                this.lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime2;
                this.objTitle = "";
                RectF roiRect32 = new RectF(this.rectX, this.rectY, this.rectX + this.rectW, this.rectY + this.rectH);
                int x3 = (int) (roiRect32.left * scaleFrameX);
                int y3 = (int) (roiRect32.top * scaleFrameY);
                int w2 = (int) (roiRect32.right * scaleFrameX);
                int h2 = (int) (roiRect32.bottom * scaleFrameY);
                RectF touchRoi2 = new RectF(x3, y3, w2, h2);
                if (this.isLog) {
                    TcpClient.getIntance().sendLog("选框在Fpv坐标：" + touchRoi2.toString() + "    ");
                }
                if (this.isLog) {
                    TcpClient.getIntance().sendLog("results size= " + results2.size());
                }
                for (Classifier.Recognition result : results2) {
                    RectF location = result.getLocation();
                    if (this.isLog) {
                        TcpClient.getIntance().sendLog(" getConfidence= " + result.getConfidence() + " 识别结果： " + location.toString() + "   ");
                    }
                    if (location != null && result.getConfidence().floatValue() >= MINIMUM_CONFIDENCE_TF_OD_API && Math.abs(location.left) <= 290.0f && Math.abs(location.top) <= 290.0f && Math.abs(location.right) <= 350.0f && Math.abs(location.bottom) <= 350.0f) {
                        int roiLeft = x1 + ((int) location.left);
                        int roiTop = y1 + ((int) location.top);
                        int roiRight = x1 + ((int) location.right);
                        int roiBottom = y1 + ((int) location.bottom);
                        if (roiLeft < 0) {
                            roiLeft = 0;
                        }
                        if (roiTop < 0) {
                            roiTop = 0;
                        }
                        if (roiRight > this.VIDEO_WIDTH) {
                            roiRight = this.VIDEO_WIDTH;
                        }
                        if (roiBottom > this.VIDEO_HEIGHT) {
                            roiBottom = this.VIDEO_HEIGHT;
                        }
                        RectF objRoi = new RectF(roiLeft, roiTop, roiRight, roiBottom);
                        boolean overlapR = false;
                        if (isDianXuan == 0) {
                            overlapR = RectOverlap(objRoi, touchRoi2);
                        }
                        if (this.isLog) {
                            TcpClient.getIntance().sendLog(" 识别结果在Fpv坐标: " + objRoi.toString() + " --- " + overlapR + "         ===");
                        }
                        if (0 == 0 && (isDianXuan == 1 || (isDianXuan == 0 && overlapR))) {
                            bFound = true;
                            targetX = (int) objRoi.left;
                            targetY = (int) objRoi.top;
                            targetW = (int) objRoi.width();
                            targetH = (int) objRoi.height();
                            this.objTitle = result.getTitle() + "-[procTime=(" + this.lastProcessingTimeMs + ")ms,confidence=" + result.getConfidence() + "]";
                            if (result.getId() != null) {
                                this.classfier = Integer.valueOf(result.getId());
                            }
                            if (bFound) {
                                float scaleFrameX2 = getScaleX1(this.mFrameWidth, this.SIZE_WIDTH);
                                float scaleFrameY2 = getScaleX1(this.mFrameHeight, this.SIZE_HEIGHT);
                                int x4 = (int) (targetX * scaleFrameX2);
                                int y4 = (int) (targetY * scaleFrameY2);
                                int w3 = x4 + ((int) (targetW * scaleFrameX2));
                                int h3 = y4 + ((int) (targetH * scaleFrameY2));
                                RectF cf2Fpv = new RectF(x4, y4, w3, h3);
                                if (this.isLog) {
                                    TcpClient.getIntance().sendLog(" 识别结果FPV坐标还原到屏幕坐标=  --- " + scaleFrameX2 + " " + cf2Fpv.toString());
                                }
                                onResult(cf2Fpv.left, cf2Fpv.top, cf2Fpv.right, cf2Fpv.bottom, bFound, this.classfier.intValue());
                            } else if (isDianXuan == 0) {
                                onResult2(this.rectX, this.rectY, this.rectW, this.rectH, true, 0);
                            } else {
                                onResult2(this.rectX - 50, this.rectY - 50, 100.0f, 100.0f, true, 0);
                            }
                            this.isTouch = false;
                            this.isFirst = false;
                            this.isRev = false;
                            this.isFirstWaiting = false;
                        }
                    }
                }
            }
            bFound = false;
            if (bFound) {
            }
            this.isTouch = false;
            this.isFirst = false;
            this.isRev = false;
            this.isFirstWaiting = false;
        }
    }

    /* loaded from: classes.dex */
    public interface onDetectionListener {
        void onDetectionFailed();

        void onDetectionResult(int i, int i2, int i3, int i4, int i5);
    }

}
