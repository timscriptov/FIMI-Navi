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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

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

public class X8MapVideoController implements View.OnClickListener, IControllers, VideodDataListener, NoFlyLinstener {
    public static FileOutputStream outputStream;
    private final Activity mActivity;
    private final Context mContext;
    AbsFimiMap mFimiMap;
    private X8GimbalAdjustController adjustController;
    private int drawNoFlightZoneCount;
    private FimiH264Video fimiVideoView;
    private boolean isPushDataToPlayer;
    private IX8MapVideoControllerListerner listener;
    private ViewGroup.LayoutParams mLayoutParams;
    private X8MapVideoCardView mapVideoCardView;
    private ViewGroup.MarginLayoutParams marginLayoutParams;
    private RelativeLayout rlFullScreen;
    private RelativeLayout rlShotScreen;
    private RelativeLayout rlSmallScreen;
    private RelativeLayout rlSwitchScreen;
    private TextView tvVedioFrame;
    private VideoManager videoManager;
    private ImageButton mapSwitchPoseBallButton;

    public X8MapVideoController(@NonNull View rootView, Bundle savedInstanceState, Activity activity) {
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

    @Override
    public void initActions() {
        this.rlSwitchScreen.setOnClickListener(this);
        this.mapSwitchPoseBallButton.setOnClickListener(this);
        StateManager.getInstance().registerNoFlyListener(this);
    }

    @Override
    public void openUi() {
    }

    @Override
    public void closeUi() {
        this.drawNoFlightZoneCount = 0;
    }

    @Override
    public void defaultVal() {
        if (this.mFimiMap != null) {
            this.mFimiMap.defaultMapValue();
        }
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    @Override
    public void initViews(View rootView) {
        this.adjustController = new X8GimbalAdjustController(rootView);
        this.rlSwitchScreen = rootView.findViewById(R.id.rl_switchscreen);
        this.rlFullScreen = rootView.findViewById(R.id.rl_fullscreen);
        this.rlSmallScreen = rootView.findViewById(R.id.rl_smallscreen);
        this.rlShotScreen = rootView.findViewById(R.id.rl_fullscreen_shot);
        this.rlSmallScreen.setVisibility(View.VISIBLE);
        this.mapVideoCardView = rootView.findViewById(R.id.cv_map_video);
        this.fimiVideoView = new FimiH264Video(rootView.getContext());
        this.tvVedioFrame = rootView.findViewById(R.id.tv_vedio_frame);
        this.fimiVideoView.setmIFrameDataListener((count, remainder, fpvSize) -> {
            X8MapVideoController.this.tvVedioFrame.setText(count + " / " + remainder + " / " + fpvSize + "/" + JsonDataChanel.testString);
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
        this.mapSwitchPoseBallButton = rootView.findViewById(R.id.map_switch_pose_ball_button);

        setSmallViewListener();
    }

    public ImageButton getMapSwitchPoseBallButton() {
        return mapSwitchPoseBallButton;
    }


    public AbsFimiMap getFimiMap() {
        return mFimiMap;
    }

    public void changeCamera() {
        if (mFimiMap instanceof GglMap) {
        }
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.rl_switchscreen) {
            switchMapVideo();
        } else if (id == R.id.map_switch_pose_ball_button) {
            listener.hidePoseBallAndMap();
        }
    }

    public void switchMapVideo() {
        boolean isFullVideo = mapVideoCardView.isFullVideo();
        this.listener.switchMapVideo(isFullVideo);
        mapSwitchPoseBallButton.setVisibility(isFullVideo ? View.GONE : View.VISIBLE);
        if (!isFullVideo) {
            adjustFullScreenFimiVideoLayout();
        } else {
            adjustSmallScreenFimiVideoLayout();
        }
        this.mapVideoCardView.switchDrawingOrder(this.mFimiMap.getMapView());
    }

    public void setListener(IX8MapVideoControllerListerner listener) {
        this.listener = listener;
    }

    public void drawNoFlightZone(@NonNull AckNoFlyNormal flyNormal) {
        this.mFimiMap.clearNoFlightZone();
        switch (flyNormal.getPolygonShape()) {
            case 0, 1 -> this.mFimiMap.drawNoFlightZone(flyNormal, NoFlyZoneEnum.CIRCLE);
            case 2 -> this.mFimiMap.drawNoFlightZone(flyNormal, NoFlyZoneEnum.CANDY);
            case 3 -> this.mFimiMap.drawNoFlightZone(flyNormal, NoFlyZoneEnum.IRREGULAR);
            default -> this.mFimiMap.clearNoFlightZone();
        }
    }

    public void switchDrawingOrderForAiFollow() {
        this.mapVideoCardView.switchDrawingOrderForAiFollow();
        this.rlSwitchScreen.setVisibility(View.GONE);
    }

    public void disShowSmall() {
        this.mapVideoCardView.disShowSmall();
        this.rlSwitchScreen.setVisibility(View.GONE);
    }

    public void resetShow() {
        this.mapVideoCardView.resetShow();
        this.rlSwitchScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRawdataCallBack(byte[] data) {
        if (isPushDataToPlayer()) {
            this.fimiVideoView.onRawdataCallBack(data);
        }
    }

    public void writeData(@NonNull byte[] cmd) {
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

    @Override
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

    public RelativeLayout getRlFullScreen() {
        return rlFullScreen;
    }

    public RelativeLayout getRlSmallScreen() {
        return rlSmallScreen;
    }

    public X8MapVideoCardView getMapVideoCardView() {
        return mapVideoCardView;
    }

    public RelativeLayout getRlSwitchScreen() {
        return rlSwitchScreen;
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
        this.rlSwitchScreen.getViewTreeObserver().addOnGlobalLayoutListener(() -> X8MapVideoController.this.mapVideoCardView.changeSmallSize());
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
