package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.X8AiTrackController;
import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiPoint2PointExcuteConfirmModule;
import com.fimi.app.x8s.enums.X8AiMapItem;
import com.fimi.app.x8s.enums.X8AiPointState;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiItemMapListener;
import com.fimi.app.x8s.interfaces.IX8MarkerListener;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.interfaces.IX8Point2PointExcuteConttrollerListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8MainPitchingAngle;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.dataparser.AckGetAiPoint;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8AiD2PExcuteController extends AbsX8AiController implements View.OnClickListener, X8MainPitchingAngle.OnProgressListener, IX8MarkerListener, X8DoubleCustomDialog.onDialogButtonClickListener, X8AiTrackController.OnAiTrackControllerListener {
    private final X8sMainActivity activity;
    private final IX8NextViewListener mIX8NextViewListener;
    protected int MAX_WIDTH;
    protected boolean isNextShow;
    protected boolean isShow;
    protected int width;
    private View blank;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private ImageView imgBack;
    private ImageView imgNext;
    private ImageView imgVcToggle;
    private boolean isDraw;
    private IX8Point2PointExcuteConttrollerListener listener;
    private AiGetPointState mAiGetPointState;
    private X8AiTipWithCloseView mTipBgView;
    private X8AiPoint2PointExcuteConfirmModule mX8AiFollowPoint2PointExcuteConfirmModule;
    private X8MapVideoController mapVideoController;
    private View nextRootView;
    private X8AiPointState state;
    private ViewStub stubPoint2Point;
    private int timeSend;
    private TextView tvP2PTip;
    private X8MainPitchingAngle vHeight;

    public X8AiD2PExcuteController(X8sMainActivity activity, View rootView, X8AiPointState state) {
        super(rootView);
        this.width = X8Application.ANIMATION_WIDTH;
        this.state = X8AiPointState.IDLE;
        this.mAiGetPointState = AiGetPointState.IDLE;
        this.timeSend = 0;
        this.mIX8NextViewListener = new IX8NextViewListener() {
            @Override
            public void onBackClick() {
                closeNextUi(true);
            }

            @Override
            public void onExcuteClick() {
                X8AiD2PExcuteController.this.state = X8AiPointState.RUNNING;
                isDraw = true;
                setAiVcOpen();
                openVcToggle();
                mTipBgView.setVisibility(View.GONE);
                closeNextUi(false);
                mapVideoController.getFimiMap().getAiPoint2PointManager().resetMapEvent();
                mapVideoController.getFimiMap().getAiPoint2PointManager().setRunning();
            }

            @Override
            public void onSaveClick() {
            }
        };
        this.activity = activity;
        this.state = state;
    }

    @Override
    public void onMarkerSelect(boolean onSelect, float altitude, MapPointLatLng mpl, boolean isClick) {
        if (onSelect) {
            this.vHeight.setVisibility(View.VISIBLE);
            this.vHeight.setProcess(altitude);
            this.imgNext.setEnabled(true);
            return;
        }
        this.vHeight.setVisibility(View.GONE);
        this.imgNext.setEnabled(true);
    }

    @Override
    public void onMarkerSizeChange(int size) {
    }

    @Override
    public void onInterestSizeEnable(boolean b) {
    }

    @Override
    public Rect getDeletePosition() {
        return null;
    }

    @Override
    public void onRunIndex(int index, int action) {
    }

    @Override
    public int getOration() {
        return 0;
    }

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        if (this.state == X8AiPointState.RUNNING || this.state == X8AiPointState.RUNNING2) {
            p2pTaskExite();
        }
    }

    public void setListener(IX8Point2PointExcuteConttrollerListener listener) {
        this.listener = listener;
    }

    @Override
    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            if (this.state == X8AiPointState.RUNNING || this.state == X8AiPointState.RUNNING2) {
                showExitDialog();
                return;
            }
            closeUi();
            if (this.listener != null) {
                this.listener.onPoint2PointBackClick();
            }
        } else if (id == R.id.img_ai_follow_next) {
            if (this.mapVideoController.getFimiMap().getAiPoint2PointManager().getMapPointLatLng() != null && this.listener != null) {
                openNextUi();
                this.mapVideoController.getFimiMap().getAiPoint2PointManager().getMapPointLatLng().altitude = this.vHeight.getRegulationProgress();
                this.mapVideoController.getFimiMap().getAiPoint2PointManager().calcDistance();
                setMapPoint(this.mapVideoController.getFimiMap().getAiPoint2PointManager().getMapPointLatLng());
            }
        } else if (id == R.id.x8_main_ai_point2point_next_blank) {
            closeNextUi(true);
        } else if (id == R.id.img_vc_targgle) {
            if (!this.imgVcToggle.isSelected()) {
                setAiVcOpen();
            } else {
                setAiVcClose();
            }
        } else if (id == R.id.rl_flag_small) {
            if (this.tvP2PTip.getVisibility() == 0) {
                this.tvP2PTip.setVisibility(View.GONE);
            } else {
                this.tvP2PTip.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void openUi() {
        this.isShow = true;
        LayoutInflater inflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_ai_follow_point2point_layout, (ViewGroup) this.rootView, true);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.imgNext = this.handleView.findViewById(R.id.img_ai_follow_next);
        this.imgNext.setEnabled(false);
        this.imgBack = this.handleView.findViewById(R.id.img_ai_follow_back);
        this.tvP2PTip = this.handleView.findViewById(R.id.img_ai_p2p_tip);
        this.vHeight = this.handleView.findViewById(R.id.x8_pitching_angle);
        this.mTipBgView = this.handleView.findViewById(R.id.img_ai_follow_tip);
        this.imgVcToggle = this.handleView.findViewById(R.id.img_vc_targgle);
        this.imgVcToggle.setSelected(true);
        this.imgVcToggle.setOnClickListener(this);
        if (this.state == X8AiPointState.IDLE) {
            this.imgNext.setVisibility(View.VISIBLE);
            this.imgVcToggle.setVisibility(View.GONE);
            this.mTipBgView.setTipText(getString(R.string.x8_ai_fly_follow_select_point));
            this.mTipBgView.setVisibility(View.VISIBLE);
            this.mapVideoController.getFimiMap().getAiPoint2PointManager().setPoint2PointMarkerSelectListener(this);
            this.mapVideoController.getFimiMap().getAiPoint2PointManager().setOnMapClickListener();
        } else {
            this.imgNext.setVisibility(View.GONE);
            setAiVcOpen();
            openVcToggle();
            this.mTipBgView.setVisibility(View.GONE);
            this.vHeight.setVisibility(View.GONE);
        }
        this.nextRootView = this.rootView.findViewById(R.id.x8_main_ai_point2point_next_content);
        this.blank = this.rootView.findViewById(R.id.x8_main_ai_point2point_next_blank);
        this.mX8AiFollowPoint2PointExcuteConfirmModule = new X8AiPoint2PointExcuteConfirmModule();
        this.vHeight.setOnProgressListener(this);
        this.mapVideoController.getFimiMap().setmX8AiItemMapListener(() -> X8AiMapItem.AI_POINT_TO_POINT);
        this.activity.getmX8AiTrackController().setOnAiTrackControllerListener(this);
        this.imgNext.setOnClickListener(this);
        this.imgBack.setOnClickListener(this);
        this.blank.setOnClickListener(this);
        this.flagSmall.setOnClickListener(this);
        super.openUi();
    }

    @Override
    public void closeUi() {
        this.isShow = false;
        this.activity.getmX8AiTrackController().closeUi();
        this.state = X8AiPointState.IDLE;
        this.mapVideoController.getFimiMap().setmX8AiItemMapListener(null);
        this.mapVideoController.getFimiMap().getAiPoint2PointManager().clearPoint2PointMarker();
        this.mapVideoController.getFimiMap().getAiPoint2PointManager().resetMapEvent();
        this.imgVcToggle.setVisibility(View.GONE);
        setAiVcClose();
        super.closeUi();
    }

    public void openNextUi() {
        this.nextRootView.setVisibility(View.VISIBLE);
        this.blank.setVisibility(View.VISIBLE);
        closeIconByNextUi();
        this.mX8AiFollowPoint2PointExcuteConfirmModule.init(this.activity, this.nextRootView);
        if (this.mX8AiFollowPoint2PointExcuteConfirmModule != null) {
            this.mX8AiFollowPoint2PointExcuteConfirmModule.setListener(this.mIX8NextViewListener, this.activity.getFcManager());
        }
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", this.width, 0.0f);
            animatorY.setDuration(300L);
            animatorY.start();
        }
    }

    @Override
    public void onProgressChanage(float progress) {
        this.mapVideoController.getFimiMap().getAiPoint2PointManager().setMarkerViewInfo(progress);
    }

    public void closeNextUi(final boolean b) {
        this.blank.setVisibility(View.GONE);
        if (this.isNextShow) {
            this.isNextShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.nextRootView, "translationX", 0.0f, this.width);
            translationRight.setDuration(300L);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                @Override
                // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    nextRootView.setVisibility(View.GONE);
                    ((ViewGroup) nextRootView).removeAllViews();
                    imgBack.setVisibility(View.VISIBLE);
                    flagSmall.setVisibility(View.VISIBLE);
                    if (b) {
                        imgNext.setVisibility(View.VISIBLE);
                        vHeight.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    public void setMapPoint(MapPointLatLng mapPoint) {
        this.mX8AiFollowPoint2PointExcuteConfirmModule.setMapPoint(mapPoint);
    }

    public void setMapVideoController(X8MapVideoController mapVideoController) {
        this.mapVideoController = mapVideoController;
    }

    public void showAiPointView() {
        if (this.state == X8AiPointState.RUNNING) {
            this.state = X8AiPointState.RUNNING2;
            if (this.listener != null) {
                this.listener.onPoint2PointRunning();
            }
        }
        if (!this.isDraw) {
            getRunningPoint();
        }
    }

    public void cancleByModeChange(int taskMode) {
        onTaskComplete(taskMode == 1);
    }

    public void getPoint() {
        this.activity.getFcManager().getAiFollowPoint((UiCallBackListener<AckGetAiPoint>) (cmdResult, ackGetAiPoint) -> {
            if (cmdResult.isSuccess() && ackGetAiPoint.getfLatLng() != null) {
                mapVideoController.getFimiMap().getAiPoint2PointManager().setMarkerByDevice(ackGetAiPoint.getfLatLng().latitude, ackGetAiPoint.getfLatLng().longitude, ackGetAiPoint.getAltitude());
                mAiGetPointState = AiGetPointState.END;
                isDraw = true;
                return;
            }
            mAiGetPointState = AiGetPointState.IDLE;
        });
    }

    @Override
    public boolean isShow() {
        if (StateManager.getInstance().getX8Drone().getCtrlMode() == 4) {
            return false;
        }
        return this.isShow;
    }

    public void showExitDialog() {
        if (this.dialog == null) {
            String t = this.rootView.getContext().getString(R.string.x8_ai_fly_point_to_point);
            String m = this.rootView.getContext().getString(R.string.x8_ai_fly_p2p_exite);
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        }
        this.dialog.show();
    }

    public void p2pTaskExite() {
        this.activity.getFcManager().setAiFollowPoint2PointExite((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                onTaskComplete(false);
            }
        });
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isShow) {
            if (!b) {
                onDroneDisconnectTaskComplete();
            } else {
                sysAiVcCtrlMode();
            }
        }
    }

    public void onTaskComplete(boolean isShow) {
        closeUi();
        if (this.listener != null) {
            this.listener.onPoint2PointBackClick();
            this.listener.onPoint2PointComplete(isShow);
        }
    }

    private void onDroneDisconnectTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeUi();
        if (this.listener != null) {
            this.listener.onPoint2PointBackClick();
            this.listener.onPoint2PointComplete(false);
        }
    }

    public void setAiVcOpen() {
        this.activity.getFcManager().setAiVcOpen((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                imgVcToggle.setSelected(true);
                activity.getmX8AiTrackController().openUi();
            }
        });
    }

    public void setAiVcClose() {
        this.activity.getFcManager().setAiVcClose((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                imgVcToggle.setSelected(false);
                activity.getmX8AiTrackController().closeUi();
            }
        });
    }

    public void setAiVcNotityFc() {
        this.activity.getFcManager().setAiVcNotityFc((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
            }
        });
    }

    @Override
    public void onChangeGoLocation(float left, float right, float top, float bottom, int w, int h) {
    }

    @Override
    public void setGoEnabled(boolean b) {
        if (b) {
            setAiVcNotityFc();
        }
    }

    @Override
    public void onTouchActionDown() {
    }

    @Override
    public void onTouchActionUp() {
    }

    @Override
    public void onTracking() {
        this.imgVcToggle.setEnabled(true);
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    public void getRunningPoint() {
        if (this.mAiGetPointState == AiGetPointState.IDLE) {
            this.mAiGetPointState = AiGetPointState.GET_POINT;
            getPoint();
        }
    }

    public void openVcToggle() {
        if (this.mapVideoController.isFullVideo()) {
            this.imgVcToggle.setVisibility(View.VISIBLE);
        } else {
            this.imgVcToggle.setVisibility(View.GONE);
        }
    }

    public void switchMapVideo(boolean isVideo) {
        if (this.isDraw) {
            if (isVideo) {
                this.imgVcToggle.setVisibility(View.GONE);
            } else {
                this.imgVcToggle.setVisibility(View.VISIBLE);
            }
        }
    }

    public void closeIconByNextUi() {
        this.imgNext.setVisibility(View.GONE);
        this.imgBack.setVisibility(View.GONE);
        this.flagSmall.setVisibility(View.GONE);
        this.vHeight.setVisibility(View.GONE);
    }

    public void sysAiVcCtrlMode() {
        if (this.state == X8AiPointState.IDLE) {
            if (this.timeSend == 0) {
                this.timeSend = 1;
                this.activity.getFcManager().sysCtrlMode2AiVc((cmdResult, o) -> {
                }, X8Task.VCM_FLY_TO.ordinal());
                return;
            }
            this.timeSend = 0;
        }
    }


    public enum AiGetPointState {
        IDLE,
        GET_POINT,
        END
    }
}
