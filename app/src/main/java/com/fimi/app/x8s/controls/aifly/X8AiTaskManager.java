package com.fimi.app.x8s.controls.aifly;

import android.view.View;
import android.view.ViewGroup;

import com.fimi.android.app.R;
import com.fimi.app.x8s.enums.X8AiAutoPhotoState;
import com.fimi.app.x8s.enums.X8AiFollowState;
import com.fimi.app.x8s.enums.X8AiLineState;
import com.fimi.app.x8s.enums.X8AiPointState;
import com.fimi.app.x8s.enums.X8AiSuroundState;
import com.fimi.app.x8s.interfaces.IX8AerialGraphListener;
import com.fimi.app.x8s.interfaces.IX8AiAutoPhotoExcuteControllerListener;
import com.fimi.app.x8s.interfaces.IX8AiFixedwingListener;
import com.fimi.app.x8s.interfaces.IX8AiFollowExcuteListener;
import com.fimi.app.x8s.interfaces.IX8AiGravitationExcuteControllerListener;
import com.fimi.app.x8s.interfaces.IX8AiHeadLockListener;
import com.fimi.app.x8s.interfaces.IX8AiLineExcuteControllerListener;
import com.fimi.app.x8s.interfaces.IX8AiSarListener;
import com.fimi.app.x8s.interfaces.IX8AiSurroundExcuteControllerListener;
import com.fimi.app.x8s.interfaces.IX8AiTripodListener;
import com.fimi.app.x8s.interfaces.IX8Point2PointExcuteConttrollerListener;
import com.fimi.app.x8s.interfaces.IX8ScrewListener;
import com.fimi.app.x8s.interfaces.IX8TLRListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.listener.IX8AiStateListener;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8AiTaskManager {
    private final X8sMainActivity activity;
    private final View aiExcuteView;
    private X8AiAerialPhotographExcuteController mAiAerailShot;
    public IX8AerialGraphListener mIX8AerialGraphListener = new IX8AerialGraphListener() {
        @Override
        public void onAerialGraphBackClick() {
            mAiAerailShot = null;
        }

        @Override
        public void onAerialGraphRunning() {
            activity.runFixedwing();
        }

        @Override
        public void onAerialGraphComplete(boolean showText) {
            removeAlls();
           activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_aerail_graph_complete));
        }
    };
    private X8AiAutoPhototExcuteController mAiAutoPhoto;
    public IX8AiAutoPhotoExcuteControllerListener mX8AiAutoPhotoExcuteControllerListener = new IX8AiAutoPhotoExcuteControllerListener() {
        @Override
        public void onAutoPhotoBackClick() {
          activity.onTaskBack();
          removeAlls();
        mAiAutoPhoto = null;
        }

        @Override
        public void onAutoPhotoRunning() {
            activity.onTaskRunning();
        }

        @Override
        public void onAutoPhotoComplete(boolean showText) {
          removeAlls();
           activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_auto_photo_compelete_tip));
           mAiAutoPhoto = null;
        }
    };
    private X8AiFixedwingExcuteController mAiFixedwing;
    public IX8AiFixedwingListener mIX8AiFixedwingListener = new IX8AiFixedwingListener() {
        @Override
        public void onFixedwingBackClick() {
            mAiFixedwing = null;
        }

        @Override
        public void onFixedwingRunning() {
            activity.runFixedwing();
        }

        @Override
        public void onFixedwingComplete(boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_fixedwing_complete));
        }
    };
    private X8AiFollowExcuteController mAiFollow;
    public IX8AiFollowExcuteListener mX8AiFollowExcuteListener = new IX8AiFollowExcuteListener() {
        @Override
        public void onAiFollowRunning() {
            activity.onTaskRunning();
        }

        @Override
        public void onComplete(String s, boolean showText) {
           removeAlls();
          activity.onTaskComplete(showText, s);
            mAiFollow = null;
        }

        @Override
        public void onAiFollowExcuteBackClick() {
           activity.onTaskBack();
           removeAlls();
         mAiFollow = null;
        }
    };
    private X8AiGravitationExcuteController mAiGravitation;
    private final IX8AiGravitationExcuteControllerListener mIX8AiGravitationExcuteControllerListener = new IX8AiGravitationExcuteControllerListener() {
        @Override
        public void onAiGravitationBackClick() {
           activity.onTaskBack();
         removeAlls();
           mAiGravitation = null;
        }

        @Override
        public void onAiGravitationComplete(boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_fly_gravitation_complete));
            mAiGravitation = null;
        }

        @Override
        public void onAiGravitaionRunning() {
            activity.onTaskRunning();
        }

        @Override
        public void onAiGravitationInterrupt() {
            removeAlls();
            activity.onTaskComplete(true, activity.getString(R.string.x8_ai_fly_gravitation_interrupt));
            mAiGravitation = null;
        }
    };
    private X8AiHeadLockExcuteController mAiHeadlock;
    public IX8AiHeadLockListener mIX8AiHeadLockListener = new IX8AiHeadLockListener() {
        @Override
        public void onAiHeadLockBackClick() {
            mAiHeadlock = null;
        }

        @Override
        public void onAiHeadLockRunning() {
            activity.runFixedwing();
        }

        @Override
        public void onAiHeadLockComplete(boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_heading_lock_complete));
        }
    };
    private X8AiLineExcuteController mAiLine;
    public IX8AiLineExcuteControllerListener mIX8AiLineExcuteControllerListener = new IX8AiLineExcuteControllerListener() {
        @Override
        public void onLineBackClick() {
            activity.onTaskBack();
            removeAlls();
            mAiLine = null;
        }

        @Override
        public void onLineRunning() {
            activity.onTaskRunning();
        }

        @Override
        public void onLineComplete(boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_fly_line_compelete_tip));
            mAiLine = null;
        }
    };
    private X8AiD2PExcuteController mAiPoint2Point;
    public IX8Point2PointExcuteConttrollerListener mIX8Point2PointExcuteConttrollerListener = new IX8Point2PointExcuteConttrollerListener() {
        @Override
        public void onPoint2PointBackClick() {
            activity.onTaskBack();
            removeAlls();
            mAiPoint2Point = null;
        }

        @Override
        public void onPoint2PointRunning() {
            activity.onTaskRunning();
        }

        @Override
        public void onPoint2PointComplete(boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_fly_follow_point_to_point_complete));
            mAiPoint2Point = null;
        }
    };
    private X8AiSarExcuteController mAiSar;
    private final IX8AiSarListener mIX8AiSarListener = new IX8AiSarListener() {
        @Override
        public void onAiSarBackClick() {
            mAiSar = null;
        }

        @Override
        public void onAiSarRunning() {
            activity.runFixedwing();
        }

        @Override
        public void onAiSarComplete(boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_fly_sar_complete_tip));
        }
    };
    private X8AiScrewExcuteController mAiScrew;
    public IX8ScrewListener mIX8ScrewListener = new IX8ScrewListener() {
        @Override
        public void onAiScrewBackClick() {
            activity.onTaskBack();
            removeAlls();
            mAiScrew = null;
        }

        @Override
        public void onAiScrewRunning() {
            activity.onTaskRunning();
        }

        @Override
        public void onAiScrewComplete(boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_fly_screw_complete));
            mAiScrew = null;
        }
    };
    private X8AiSurroundExcuteController mAiSurround;
    public IX8AiSurroundExcuteControllerListener mIX8AiSurroundExcuteControllerListener = new IX8AiSurroundExcuteControllerListener() {
        @Override
        public void onSurroundBackClick() {
            activity.onTaskBack();
            removeAlls();
            mAiSurround = null;
        }

        @Override
        public void onSurroundRunning() {
            activity.onTaskRunning();
        }

        @Override
        public void onSurroundComplete(boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_fly_follow_surround_compelete_tip));
            mAiSurround = null;
        }
    };
    private X8AiTakeoffLandingReturnHomeExcuteController mAiTLRController;
    private final IX8TLRListener mX8TLRListener = new IX8TLRListener() {
        @Override
        public void onRunning() {
            activity.onTaskRunning();
        }

        @Override
        public void onComplete(String s, boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, s);
            mAiTLRController = null;
        }
    };
    private X8AiTripodExcuteController mAiTripod;
    public IX8AiTripodListener mIX8AiTripodListener = new IX8AiTripodListener() {
        @Override
        public void onAiTripodBackClick() {
            mAiTripod = null;
        }

        @Override
        public void onAiTripodRunning() {
            activity.runFixedwing();
        }

        @Override
        public void onAiTripodComplete(boolean showText) {
            removeAlls();
            activity.onTaskComplete(showText, activity.getString(R.string.x8_ai_tripod_complete));
        }
    };
    private CameraManager mCameraManager;
    private FcCtrlManager mFcCtrlManager;
    private FcManager mFcManager;
    private IX8AiStateListener mIX8AiStateListener;
    private int lastMode = 1;

    public X8AiTaskManager(View aiExcuteView, X8sMainActivity activity) {
        this.aiExcuteView = aiExcuteView;
        this.activity = activity;
    }

    public boolean isTaskRunning() {
        boolean ret = false;
        if (StateManager.getInstance().getX8Drone().getCtrlMode() != 1) {
            ret = true;
        } else if (this.mAiSar != null) {
            return true;
        } else {
            if (this.mAiLine != null || this.mAiHeadlock != null || this.mAiTripod != null || this.mAiAerailShot != null || this.mAiScrew != null || this.mAiSurround != null || this.mAiAutoPhoto != null || this.mAiFollow != null) {
                ret = true;
            }
        }
        return ret;
    }

    public boolean isTaskCanChangeBottom() {
        if (StateManager.getInstance().getX8Drone().getCtrlMode() != 1) {
            return true;
        }
        return this.mAiLine == null && this.mAiSurround == null && this.mAiAutoPhoto == null && this.mAiFollow == null && this.mAiScrew == null;
    }

    public void showAiView() {
        int currentMode = StateManager.getInstance().getX8Drone().getCtrlMode();
        if (currentMode != this.lastMode && this.lastMode != 1) {
            cancleLastMode(this.lastMode, currentMode);
            cancleByModeChange(currentMode);
            removeAlls();
        }
        if (currentMode != 1 && this.mAiSar != null) {
            this.mAiSar.cancleByModeChange();
            this.mAiSar = null;
        }
        if (this.lastMode == 1) {
            if (currentMode == 7 || currentMode == 8 || currentMode == 9) {
                cancleByModeChange(currentMode);
            } else if (currentMode == 3) {
                cancleByModeChange(currentMode);
            }
        }
        this.lastMode = currentMode;
        doCurrentMode(currentMode);
    }

    public void doCurrentMode(int mode) {
        switch (mode) {
            case 2:
                initAiTLRController(2);
                setX8AiState(true);
                return;
            case 3:
                initAiTLRController(3);
                setX8AiState(true);
                return;
            case 4:
                intAiD2Point(X8AiPointState.RUNNING);
                this.mAiPoint2Point.showAiPointView();
                setX8AiState(true);
                return;
            case 5:
                initAiSurround(X8AiSuroundState.RUNNING);
                setX8AiState(true);
                return;
            case 6:
                initAiLine(X8AiLineState.RUNNING, -1, -1L);
                this.mAiLine.showAiPointView();
                setX8AiState(true);
                return;
            case 7:
                initAiTLRController(7);
                setX8AiState(true);
                return;
            case 8:
                initAiTLRController(8);
                setX8AiState(true);
                return;
            case 9:
                initAiTLRController(9);
                setX8AiState(true);
                return;
            case 10:
                initAiAutoPhoto(X8AiAutoPhotoState.RUNNING, -1);
                setX8AiState(true);
                return;
            case 11:
                initAiFollow(X8AiFollowState.RUNNING, -1);
                setX8AiState(true);
                return;
            case 12:
                if (this.mAiHeadlock == null) {
                    this.mAiHeadlock = new X8AiHeadLockExcuteController(this.activity, this.aiExcuteView);
                    this.mAiHeadlock.setListener(this.mIX8AiHeadLockListener);
                    this.mAiHeadlock.setFcManager(this.mFcCtrlManager);
                    this.mAiHeadlock.openUi();
                }
                setX8AiState(true);
                return;
            case 13:
                if (this.mAiFixedwing == null) {
                    this.mAiFixedwing = new X8AiFixedwingExcuteController(this.activity, this.aiExcuteView);
                    this.mAiFixedwing.setListener(this.mIX8AiFixedwingListener);
                    this.mAiFixedwing.setFcManager(this.mFcCtrlManager);
                    this.mAiFixedwing.openUi();
                }
                setX8AiState(true);
                return;
            case 14:
                initScrewExcuteController(X8AiScrewExcuteController.ScrewState.RUNNING);
                setX8AiState(true);
                return;
            case 15:
                if (this.mAiTripod == null) {
                    this.mAiTripod = new X8AiTripodExcuteController(this.activity, this.aiExcuteView);
                    this.mAiTripod.setListener(this.mIX8AiTripodListener);
                    this.mAiTripod.setFcManager(this.mFcCtrlManager);
                    this.mAiTripod.openUi();
                }
                setX8AiState(true);
                return;
            case 16:
                if (this.mAiAerailShot == null) {
                    this.mAiAerailShot = new X8AiAerialPhotographExcuteController(this.activity, this.aiExcuteView);
                    this.mAiAerailShot.setListener(this.mIX8AerialGraphListener);
                    this.mAiAerailShot.setFcManager(this.mFcCtrlManager);
                    this.mAiAerailShot.openUi();
                }
                setX8AiState(true);
                return;
            case 17:
            default:
                return;
            case 18:
                if (this.mAiGravitation == null) {
                    this.mAiGravitation = new X8AiGravitationExcuteController(this.activity, this.aiExcuteView, X8AiGravitationExcuteController.X8GravitationState.RUNNING);
                    this.mAiGravitation.setListener(this.mIX8AiGravitationExcuteControllerListener);
                    this.mAiGravitation.setFcManager(this.mFcManager);
                    this.mAiGravitation.setCameraManager(this.mCameraManager);
                    this.mAiGravitation.openUi();
                }
                setX8AiState(true);
        }
    }

    public void cancleLastMode(int mode, int currentMode) {
        switch (mode) {
            case 2:
            case 8:
            case 9:
            case 7:
            case 3:
                if (this.mAiTLRController != null) {
                    this.mAiTLRController.cancleByModeChange(currentMode);
                }
                setX8AiState(false);
                return;
            case 4:
                if (this.mAiPoint2Point != null) {
                    this.mAiPoint2Point.cancleByModeChange(currentMode);
                }
                setX8AiState(false);
                return;
            case 5:
                if (this.mAiSurround != null) {
                    this.mAiSurround.cancleByModeChange();
                }
                setX8AiState(false);
                return;
            case 6:
                if (this.mAiLine != null) {
                    this.mAiLine.cancleByModeChange(currentMode);
                }
                setX8AiState(false);
                return;
            case 10:
                if (this.mAiAutoPhoto != null) {
                    this.mAiAutoPhoto.cancleByModeChange(currentMode);
                }
                setX8AiState(false);
                return;
            case 11:
                if (this.mAiFollow != null) {
                    this.mAiFollow.cancleByModeChange(currentMode);
                }
                setX8AiState(false);
                return;
            case 12:
            case 14:
            case 15:
            case 17:
            case 16:
            default:
                setX8AiState(false);
                return;
            case 13:
                if (this.mAiFixedwing != null) {
                    this.mAiFixedwing.cancleByModeChange();
                }
                setX8AiState(false);
                return;
            case 18:
                if (this.mAiGravitation != null) {
                    this.mAiGravitation.cancleByModeChange(currentMode);
                }
                setX8AiState(false);
        }
    }

    public void removeAlls() {
        ((ViewGroup) this.aiExcuteView).removeAllViews();
    }

    public void cancleByModeChange(int mode) {
        if (this.mAiFixedwing != null) {
            this.mAiFixedwing.closeUi();
            if (mode == 1) {
                this.activity.onShowAiFlyIcon();
            }
            this.mAiFixedwing = null;
        }
        if (this.mAiHeadlock != null) {
            this.mAiHeadlock.closeUi();
            if (mode == 1) {
                this.activity.onShowAiFlyIcon();
            }
            this.mAiHeadlock = null;
        }
        if (this.mAiTripod != null) {
            this.mAiTripod.closeUi();
            if (mode == 1) {
                this.activity.onShowAiFlyIcon();
            }
            this.mAiTripod = null;
        }
        if (this.mAiAerailShot != null) {
            this.mAiAerailShot.closeUi();
            if (mode == 1) {
                this.activity.onShowAiFlyIcon();
            }
            this.mAiAerailShot = null;
        }
        if (this.mAiGravitation != null) {
            this.mAiGravitation.cancleByModeChange(mode);
            this.mAiGravitation = null;
        }
        if (this.mAiScrew != null) {
            this.mAiScrew.cancleByModeChange(mode);
            this.mAiScrew = null;
        }
        if (this.mAiSurround != null) {
            this.mAiSurround.cancleByModeChange(mode);
            this.mAiSurround = null;
        }
        if (this.mAiLine != null) {
            this.mAiLine.cancleByModeChange(mode);
            this.mAiLine = null;
        }
        if (this.mAiFollow != null) {
            this.mAiFollow.cancleByModeChange(mode);
            this.mAiFollow = null;
        }
        if (this.mAiAutoPhoto != null) {
            this.mAiAutoPhoto.cancleByModeChange(mode);
            this.mAiAutoPhoto = null;
        }
        if (this.mAiPoint2Point != null) {
            this.mAiPoint2Point.cancleByModeChange(mode);
            this.mAiPoint2Point = null;
        }
    }

    public void showSportState(AutoFcSportState state) {
        if (this.mAiHeadlock != null) {
            this.mAiHeadlock.showSportState(state);
        } else if (this.mAiAerailShot != null) {
            this.mAiAerailShot.showSportState(state);
        } else if (this.mAiScrew != null) {
            this.mAiScrew.showSportState(state);
        } else if (this.mAiSurround != null) {
            this.mAiSurround.showSportState(state);
        } else if (this.mAiGravitation != null) {
            this.mAiGravitation.showSportState(state);
        }
    }

    public void onDroneConnected(boolean b) {
        if (this.mAiFixedwing != null) {
            this.mAiFixedwing.onDroneConnected(b);
        }
        if (this.mAiHeadlock != null) {
            this.mAiHeadlock.onDroneConnected(b);
        }
        if (this.mAiTripod != null) {
            this.mAiTripod.onDroneConnected(b);
        }
        if (this.mAiAerailShot != null) {
            this.mAiAerailShot.onDroneConnected(b);
        }
        if (this.mAiScrew != null) {
            this.mAiScrew.onDroneConnected(b);
        }
        if (this.mAiSurround != null) {
            this.mAiSurround.onDroneConnected(b);
        }
        if (this.mAiSar != null) {
            this.mAiSar.onDroneConnected(b);
        }
        if (this.mAiLine != null) {
            this.mAiLine.onDroneConnected(b);
        }
        if (this.mAiFollow != null) {
            this.mAiFollow.onDroneConnected(b);
        }
        if (this.mAiAutoPhoto != null) {
            this.mAiAutoPhoto.onDroneConnected(b);
        }
        if (this.mAiTLRController != null) {
            this.mAiTLRController.onDroneConnected(b);
        }
        if (this.mAiPoint2Point != null) {
            this.mAiPoint2Point.onDroneConnected(b);
        }
        if (this.mAiGravitation != null) {
            this.mAiGravitation.onDroneConnected(b);
        }
        if (!b) {
            this.lastMode = 1;
        }
    }

    public void switchUnityEvent() {
        if (this.mAiSurround != null) {
            this.mAiSurround.switchUnityEvent();
        }
        if (this.mAiFollow != null) {
            this.mAiFollow.switchUnityEvent();
        }
    }

    public void switchMapVideo(boolean isVideo) {
        if (this.mAiGravitation != null) {
            this.mAiGravitation.switchMapVideo(isVideo);
        }
        if (this.mAiScrew != null) {
            this.mAiScrew.switchMapVideo(isVideo);
        }
        if (this.mAiSar != null) {
            this.mAiSar.switchMapVideo(isVideo);
        } else if (this.mAiLine != null) {
            this.mAiLine.switchMapVideo(isVideo);
        } else if (this.mAiAutoPhoto != null) {
            this.mAiAutoPhoto.switchMapVideo(isVideo);
        } else if (this.mAiSurround != null) {
            this.mAiSurround.switchMapVideo(isVideo);
        } else if (this.mAiPoint2Point != null) {
            this.mAiPoint2Point.switchMapVideo(isVideo);
        }
    }

    public void setManager(FcManager mFcManager, FcCtrlManager mFcCtrlManager) {
        this.mFcCtrlManager = mFcCtrlManager;
        this.mFcManager = mFcManager;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.mCameraManager = cameraManager;
    }

    public void initScrewExcuteController() {
        initScrewExcuteController(X8AiScrewExcuteController.ScrewState.IDLE);
    }

    public void initScrewExcuteController(X8AiScrewExcuteController.ScrewState state) {
        if (this.mAiScrew == null) {
            this.mAiScrew = new X8AiScrewExcuteController(this.activity, this.aiExcuteView, state);
            this.mAiScrew.setListener(this.mIX8ScrewListener);
            this.mAiScrew.setFcManager(this.mFcCtrlManager, this.mFcManager);
            this.mAiScrew.openUi();
        }
    }

    public void openAiGravitation() {
        initAiGravitation(X8AiGravitationExcuteController.X8GravitationState.IDLE);
    }

    private void initAiGravitation(X8AiGravitationExcuteController.X8GravitationState state) {
        if (this.mAiGravitation == null) {
            this.mAiGravitation = new X8AiGravitationExcuteController(this.activity, this.aiExcuteView, state);
        }
        this.mAiGravitation.setListener(this.mIX8AiGravitationExcuteControllerListener);
        this.mAiGravitation.setFcManager(this.mFcManager);
        this.mAiGravitation.setCameraManager(this.mCameraManager);
        this.mAiGravitation.openUi();
    }

    public void changeSarProceess(boolean b) {
        if (this.mAiSar != null) {
            this.mAiSar.changeProcessByRc(b);
        }
    }

    public boolean isInSARMode() {
        return this.mAiSar != null;
    }

    public void openAiSarUi() {
        if (this.mAiSar == null) {
            this.mAiSar = new X8AiSarExcuteController(this.activity, this.aiExcuteView);
            this.mAiSar.setListener(this.mIX8AiSarListener);
            this.mAiSar.setFcManager(this.mFcCtrlManager);
            this.mAiSar.openUi();
        }
    }

    public void updateSarValue() {
        if (this.mAiSar != null) {
            this.aiExcuteView.postDelayed(() -> mAiSar.setProgress(), 500L);
        }
    }

    public void openAiLine(int mode, long lineId) {
        initAiLine(X8AiLineState.IDLE, mode, lineId);
        SPStoreManager.getInstance().saveBoolean("isExecuteCurveProcess", mode == 3);
    }

    public void initAiLine(X8AiLineState state, int mode, long lineId) {
        if (this.mAiLine == null) {
            this.mAiLine = new X8AiLineExcuteController(this.activity, this.aiExcuteView, state, mode, lineId);
            this.mAiLine.setListener(this.mIX8AiLineExcuteControllerListener);
            this.mAiLine.setFcManager(this.mFcManager);
            this.mAiLine.setCameraManager(this.mCameraManager);
            this.mAiLine.openUi();
        }
    }

    public void switchLine(long lineId, int type) {
        if (this.mAiLine != null) {
            this.mAiLine.switchLine(lineId, type);
        }
    }

    public void initAiTLRController(int type) {
        if (this.mAiTLRController == null) {
            this.mAiTLRController = new X8AiTakeoffLandingReturnHomeExcuteController(this.activity, this.aiExcuteView, type);
            this.mAiTLRController.setListener(this.mX8TLRListener);
            this.mAiTLRController.setFcManager(this.mFcManager);
            this.mAiTLRController.openUi();
        }
    }

    public void openAiD2Point() {
        intAiD2Point(X8AiPointState.IDLE);
    }

    private void intAiD2Point(X8AiPointState state) {
        if (this.mAiPoint2Point == null) {
            this.mAiPoint2Point = new X8AiD2PExcuteController(this.activity, this.aiExcuteView, state);
            this.mAiPoint2Point.setListener(this.mIX8Point2PointExcuteConttrollerListener);
            this.mAiPoint2Point.setMapVideoController(this.activity.getmMapVideoController());
            this.mAiPoint2Point.openUi();
        }
    }

    public void openAiSurround() {
        initAiSurround(X8AiSuroundState.IDLE);
    }

    public void initAiSurround(X8AiSuroundState state) {
        if (this.mAiSurround == null) {
            this.mAiSurround = new X8AiSurroundExcuteController(this.activity, this.aiExcuteView, state);
            this.mAiSurround.setListener(this.mIX8AiSurroundExcuteControllerListener);
            this.mAiSurround.setFcManager(this.mFcManager);
            this.mAiSurround.openUi();
        }
    }

    public void openAiAutoPhoto(int type) {
        initAiAutoPhoto(X8AiAutoPhotoState.IDLE, type);
    }

    private void initAiAutoPhoto(X8AiAutoPhotoState state, int type) {
        if (this.mAiAutoPhoto == null) {
            this.mAiAutoPhoto = new X8AiAutoPhototExcuteController(this.activity, this.aiExcuteView, state, type);
            this.mAiAutoPhoto.setListener(this.mX8AiAutoPhotoExcuteControllerListener);
            this.mAiAutoPhoto.openUi();
        }
    }

    public void openAiFollow(int type) {
        initAiFollow(X8AiFollowState.IDLE, type);
    }

    public void initAiFollow(X8AiFollowState state, int type) {
        if (this.mAiFollow == null) {
            this.mAiFollow = new X8AiFollowExcuteController(this.activity, this.aiExcuteView, state, type);
            this.mAiFollow.setX8AiFollowExcuteListener(this.mX8AiFollowExcuteListener);
            this.mAiFollow.openUi();
        }
    }

    public void setIX8AiStateListener(IX8AiStateListener IX8AiStateListener) {
        this.mIX8AiStateListener = IX8AiStateListener;
    }

    public void setX8AiState(boolean aiRunning) {
        if (this.mIX8AiStateListener != null) {
            this.mIX8AiStateListener.aiState(aiRunning);
        }
    }
}
