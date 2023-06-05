package com.fimi.app.x8s.controls;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.gimbal.X8GimbalAdjustController;
import com.fimi.app.x8s.enums.NoFlyZoneEnum;
import com.fimi.app.x8s.interfaces.IControllers;
import com.fimi.app.x8s.interfaces.IFimiFpvShot;
import com.fimi.app.x8s.interfaces.IFimiOnSnapshotReady;
import com.fimi.app.x8s.interfaces.IX8GestureListener;
import com.fimi.app.x8s.interfaces.IX8MainTopBarListener;
import com.fimi.app.x8s.interfaces.IX8MapVideoControllerListerner;
import com.fimi.app.x8s.map.GglMap;
import com.fimi.app.x8s.map.interfaces.AbsFimiMap;
import com.fimi.app.x8s.media.FimiH264Video;
import com.fimi.app.x8s.media.IFrameDataListener;
import com.fimi.app.x8s.tools.X8sNavigationBarUtils;
import com.fimi.app.x8s.widget.X8MapVideoCardView;
import com.fimi.kernel.Constants;
import com.fimi.kernel.animutils.IOUtils;
import com.fimi.kernel.connect.session.VideodDataListener;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.connect.datatype.JsonDataChanel;
import com.fimi.x8sdk.controller.VideoManager;
import com.fimi.x8sdk.dataparser.AckNoFlyNormal;
import com.fimi.x8sdk.listener.NoFlyLinstener;
import com.fimi.x8sdk.map.MapType;
import com.fimi.x8sdk.modulestate.StateManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class X8MapVideoController implements View.OnClickListener, IControllers, VideodDataListener, NoFlyLinstener {
    public static FileOutputStream outputStream;
    AbsFimiMap mFimiMap;
    private X8GimbalAdjustController adjustController;
    private int drawNoFlightZoneCount;
    private FimiH264Video fimiVideoView;
    private int h;
    private boolean isPushDataToPlayer;
    private IX8MapVideoControllerListerner listener;
    private Activity mActivity;
    private Context mContext;
    private ViewGroup.LayoutParams mLayoutParams;
    private X8MapVideoCardView mapVideoCardView;
    private ViewGroup.MarginLayoutParams marginLayoutParams;
    private int oldh;
    private int oldw;
    private RelativeLayout rlFullScreen;
    private RelativeLayout rlShotScreen;
    private RelativeLayout rlSmallScreen;
    private RelativeLayout rlSwitchScreen;
    private TextView tvVedioFrame;
    private VideoManager videoManager;
    private int w;

    public X8MapVideoController(View rootView, Bundle savedInstanceState, Activity activity) {
        this.mActivity = activity;
        this.mContext = rootView.getContext();
        if (GlobalConfig.getInstance().getMapType() != MapType.AMap && !Constants.isFactoryApp()) {
            this.mFimiMap = new GglMap();
        }
        this.mFimiMap.setContext(rootView.getContext());
        this.mFimiMap.onCreate(rootView, savedInstanceState);
        initViews(rootView);
        initActions();
        this.mFimiMap.setUpMap();
    }

    public void setListener(IX8MainTopBarListener listener) {
        if (this.adjustController != null) {
            this.adjustController.setListener(listener);
        }
    }

    public void setUpMap() {
        if (this.mFimiMap != null) {
            this.mFimiMap.setUpMap();
        }
    }

    public void addHome(double latitude, double longitude) {
        if (this.mFimiMap != null) {
            this.mFimiMap.setHomeLocation(latitude, longitude);
        }
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initActions() {
        this.rlSwitchScreen.setOnClickListener(this);
        StateManager.getInstance().registerNoFlyListener(this);
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void openUi() {
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void closeUi() {
        this.drawNoFlightZoneCount = 0;
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void defaultVal() {
        if (this.mFimiMap != null) {
            this.mFimiMap.defaultMapValue();
        }
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public boolean onClickBackKey() {
        return false;
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initViews(View rootView) {
        this.adjustController = new X8GimbalAdjustController(rootView);
        this.rlSwitchScreen = (RelativeLayout) rootView.findViewById(R.id.rl_switchscreen);
        this.rlFullScreen = (RelativeLayout) rootView.findViewById(R.id.rl_fullscreen);
        this.rlSmallScreen = (RelativeLayout) rootView.findViewById(R.id.rl_smallscreen);
        this.rlShotScreen = (RelativeLayout) rootView.findViewById(R.id.rl_fullscreen_shot);
        this.rlSmallScreen.setVisibility(0);
        this.mapVideoCardView = (X8MapVideoCardView) rootView.findViewById(R.id.cv_map_video);
        this.fimiVideoView = new FimiH264Video(rootView.getContext());
        this.tvVedioFrame = (TextView) rootView.findViewById(R.id.tv_vedio_frame);
        this.fimiVideoView.setmIFrameDataListener(new IFrameDataListener() { // from class: com.fimi.app.x8s.controls.X8MapVideoController.1
            @Override // com.fimi.app.x8s.media.IFrameDataListener
            public void onCountFrame(int count, int remainder, int fpvSize) {
                X8MapVideoController.this.tvVedioFrame.setText(count + " / " + remainder + " / " + fpvSize + "/" + JsonDataChanel.testString);
            }
        });
        this.rlSmallScreen.addView(this.mFimiMap.getMapView());
        this.rlFullScreen.addView(this.fimiVideoView);
        this.mLayoutParams = this.fimiVideoView.getLayoutParams();
        if (this.mLayoutParams instanceof ViewGroup.MarginLayoutParams) {
            this.marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mLayoutParams;
        } else {
            this.marginLayoutParams = new ViewGroup.MarginLayoutParams(this.mLayoutParams);
        }
        this.videoManager = new VideoManager(this);
        try {
            outputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/zdy1.h264");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setSmallViewListener();
    }

    public AbsFimiMap getFimiMap() {
        return this.mFimiMap;
    }

    public void changeCamera() {
        if (this.mFimiMap instanceof GglMap) {
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_switchscreen) {
            switchMapVideo();
        }
    }

    public void switchMapVideo() {
        this.listener.switchMapVideo(this.mapVideoCardView.isFullVideo());
        if (!this.mapVideoCardView.isFullVideo()) {
            adjustFullScreenFimiVideoLayout();
        } else {
            adjustSmallScreenFimiVideoLayout();
        }
        this.mapVideoCardView.switchDrawingOrder(this.mFimiMap.getMapView());
    }

    public void setListener(IX8MapVideoControllerListerner listener) {
        this.listener = listener;
    }

    public void drawNoFlightZone(AckNoFlyNormal flyNormal) {
        this.mFimiMap.clearNoFlightZone();
        switch (flyNormal.getPolygonShape()) {
            case 0:
                this.mFimiMap.drawNoFlightZone(flyNormal, NoFlyZoneEnum.CIRCLE);
                return;
            case 1:
                this.mFimiMap.drawNoFlightZone(flyNormal, NoFlyZoneEnum.CIRCLE);
                return;
            case 2:
                this.mFimiMap.drawNoFlightZone(flyNormal, NoFlyZoneEnum.CANDY);
                return;
            case 3:
                this.mFimiMap.drawNoFlightZone(flyNormal, NoFlyZoneEnum.IRREGULAR);
                return;
            default:
                this.mFimiMap.clearNoFlightZone();
                return;
        }
    }

    public void switchDrawingOrderForAiFollow() {
        this.mapVideoCardView.switchDrawingOrderForAiFollow();
        this.rlSwitchScreen.setVisibility(8);
    }

    public void disShowSmall() {
        this.mapVideoCardView.disShowSmall();
        this.rlSwitchScreen.setVisibility(8);
    }

    public void resetShow() {
        this.mapVideoCardView.resetShow();
        this.rlSwitchScreen.setVisibility(0);
    }

    @Override // com.fimi.kernel.connect.session.VideodDataListener
    public void onRawdataCallBack(byte[] data) {
        if (isPushDataToPlayer()) {
            this.fimiVideoView.onRawdataCallBack(data);
        }
    }

    public void writeData(byte[] cmd) {
        try {
            int length = cmd.length;
            byte[] bytes = new byte[14];
            System.arraycopy(cmd, 0, bytes, 0, 14);
            int seq = ((bytes[2] & 255) << 8) | (bytes[3] & 255);
            int pts_cur = ((bytes[4] & 255) << 24) | ((bytes[5] & 255) << 16) | ((bytes[6] & 255) << 8) | (bytes[7] & 255);
            String hex = ByteHexHelper.bytesToHexString(bytes) + " seq=" + seq + " pts_cur=" + pts_cur + IOUtils.LINE_SEPARATOR_UNIX;
            outputStream.write(hex.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // com.fimi.x8sdk.listener.NoFlyLinstener
    public void showNoFly(AckNoFlyNormal flyNormal) {
        this.drawNoFlightZoneCount++;
        if (this.drawNoFlightZoneCount % 19 == 3) {
            drawNoFlightZone(flyNormal);
        }
    }

    public void showGridLine(int type) {
        this.fimiVideoView.showGridLine(type);
    }

    public void switchByPoint2PointMap() {
        adjustSmallScreenFimiVideoLayout();
        this.mapVideoCardView.switchDrawingOrderForPoint2Point();
    }

    public void switchByAiLineMap() {
        adjustSmallScreenFimiVideoLayout();
        this.mapVideoCardView.switchDrawingOrderForPoint2Point();
    }

    public void switchByAiLineVideo() {
        adjustFullScreenFimiVideoLayout();
        this.mapVideoCardView.switchDrawingOrderForAiLineVideo();
    }

    public void switchBySurroundMap() {
        this.mapVideoCardView.switchDrawingOrderForAiLineVideo();
    }

    public boolean isFullVideo() {
        return this.mapVideoCardView.isFullVideo();
    }

    public void switchDrawingOrderForGimbal() {
        this.mapVideoCardView.switchDrawingOrderForGimbal();
    }

    public FimiH264Video getVideoView() {
        return this.fimiVideoView;
    }

    public void showVideoBg(boolean b) {
        this.fimiVideoView.showVideoBg(b);
    }

    public void setX8GestureListener(IX8GestureListener listener) {
        this.fimiVideoView.getX8AiTrackContainterView().setX8GestureListener(listener);
    }

    public void onResume() {
        this.isPushDataToPlayer = true;
        adjustFullScreenFimiVideoLayout();
    }

    public void onPause() {
        this.isPushDataToPlayer = false;
    }

    public boolean isPushDataToPlayer() {
        return true;
    }

    public void snapshot(IFimiOnSnapshotReady callback) {
        this.mFimiMap.snapshot(callback);
    }

    public void setMapShot(Bitmap mapShot) {
        if (isFullVideo()) {
            this.rlSwitchScreen.setBackground(new BitmapDrawable(mapShot));
        } else {
            this.rlShotScreen.setBackground(new BitmapDrawable(mapShot));
        }
    }

    public void clearShotBitmap() {
        this.rlSwitchScreen.setBackground(null);
        this.rlShotScreen.setBackground(null);
    }

    public void fpvShot(IFimiFpvShot callback) {
        this.fimiVideoView.fpvShot(callback);
    }

    public void setFpvShot(Bitmap fpvShot) {
        if (isFullVideo()) {
            this.rlShotScreen.setBackground(new BitmapDrawable(fpvShot));
        } else {
            this.rlSwitchScreen.setBackground(new BitmapDrawable(fpvShot));
        }
    }

    public void setSmallViewListener() {
        this.rlSwitchScreen.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.fimi.app.x8s.controls.X8MapVideoController.2
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                X8MapVideoController.this.mapVideoCardView.changeSmallSize();
            }
        });
    }

    private void adjustFullScreenFimiVideoLayout() {
        DisplayMetrics dm = this.mContext.getResources().getDisplayMetrics();
        if (X8sNavigationBarUtils.isPad(this.mActivity)) {
            int height = dm.heightPixels;
            int diffHeight = (int) (height - X8sNavigationBarUtils.getRootHeightAndWidth(dm)[0]);
            this.fimiVideoView.setPadding(0, diffHeight / 2, 0, diffHeight / 2);
        } else if (X8sNavigationBarUtils.isHWProportion(dm)) {
            int width = dm.widthPixels;
            int diffWidth = (int) (width - X8sNavigationBarUtils.getRootHeightAndWidth(dm)[1]);
            this.fimiVideoView.setPadding(diffWidth / 2, 0, diffWidth / 2, 0);
        } else {
            int height2 = dm.heightPixels;
            int diffHeight2 = (int) (height2 - X8sNavigationBarUtils.getRootHeightAndWidth(dm)[0]);
            this.fimiVideoView.setPadding(0, diffHeight2 / 2, 0, diffHeight2 / 2);
        }
    }

    private void adjustSmallScreenFimiVideoLayout() {
        this.fimiVideoView.setPadding(0, 0, 0, 0);
    }
}
