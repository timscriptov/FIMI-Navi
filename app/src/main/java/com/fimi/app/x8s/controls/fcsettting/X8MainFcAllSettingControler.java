package com.fimi.app.x8s.controls.fcsettting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.controls.fcsettting.flightlog.X8FlightLogListController;
import com.fimi.app.x8s.enums.X8FcAllSettingMenuEnum;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.app.x8s.interfaces.IX8DroneStateListener;
import com.fimi.app.x8s.interfaces.IX8FcAllSettingListener;
import com.fimi.app.x8s.interfaces.IX8FcItemListener;
import com.fimi.app.x8s.interfaces.IX8FirmwareUpgradeControllerListener;
import com.fimi.app.x8s.interfaces.IX8FiveKeyDefineListener;
import com.fimi.app.x8s.interfaces.IX8FrequencyPointListener;
import com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener;
import com.fimi.app.x8s.interfaces.IX8GeneralItemControllerListerner;
import com.fimi.app.x8s.interfaces.IX8GimbalSettingListener;
import com.fimi.app.x8s.interfaces.IX8MainCoverListener;
import com.fimi.app.x8s.interfaces.IX8RcItemControllerListener;
import com.fimi.app.x8s.interfaces.IX8RcRockerListener;
import com.fimi.app.x8s.ui.activity.update.X8UpdateDetailActivity;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.host.HostConstants;
import com.fimi.kernel.Constants;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.FileUtil;
import com.fimi.widget.CustomLoadManage;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.dataparser.AutoFcBattery;

import java.io.File;

import router.Router;


public class X8MainFcAllSettingControler extends AbsX8MenuBoxControllers implements View.OnClickListener {
    public IX8CalibrationListener mX8CalibrationListener;
    public IX8FcItemListener mX8FcItemListener;
    public IX8FirmwareUpgradeControllerListener mX8FwUpgradeCtrlListener;
    public IX8RcItemControllerListener mX8RcItemControllerListener;
    public IX8FiveKeyDefineListener mx8FiveKeyCalibrationListener;
    public IX8CalibrationListener mx8RockerCalibrationListener;
    IX8MainCoverListener coverListener;
    X8MapVideoController mapVideoController;
    IX8RcRockerListener rcCtrlModelListener;
    private int contentViewTopMargin;
    private FcCtrlManager fcCtrlManager;
    private FcManager fcManager;
    private final View firstContentView;
    private X8GimbalManager gimbalManager;
    private final IX8GimbalSettingListener gimbalSettingListener;
    private boolean isCanClose;
    private boolean isfiveKeyOpen;
    private IX8FcAllSettingListener listener;
    private X8DoubleCustomDialog loginDialogRestart;
    private final Activity mActivity;
    private AbsX8MenuBoxControllers mCurrentFirstController;
    private AbsX8MenuBoxControllers mCurrentSecondController;
    private CustomLoadManage mCustomLoadManage;
    private FirstMenu mFirstMenu;
    private final IX8DroneStateListener mIX8DroneStateListener;
    private IX8GeneralItemControllerListerner mIX8GeneralItemControllerListerner;
    private SecondMenu mSecondMenu;
    private X8DroneCalibrationController mX8DroneCalibrationController;
    private X8DroneInfoStateController mX8DroneInfoStateController;
    private X8FcSettingMenuController mX8FcSettingMenuController;
    private X8FirmwareUpgradeController mX8FirmwareUpgradeController;
    private X8FrequencyPointController mX8FrequencyPointController;
    private final IX8GeneraModifyModeControllerListener modifyModeControllerListener;
    private X8RCCalibrationController rcCalibrationController;
    private X8RCMatchCodeController rcMatchCodeController;
    private final View rlFcAllSetting;
    private final View secondContentView;
    private X8CloudCalibrationController x8CloudCalibrationController;
    private X8FcExpSettingController x8FcExpSettingController;
    private X8FcSensitivitySettingController x8FcSensitivitySettingController;
    private X8FiveKeyDefineController x8FiveKeyDefineController;
    private X8FlightLogListController x8FlightLogListController;
    private X8GimbalAdvancedSetupController x8GimbalAdvancedSetupController;
    private X8ModifyModeController x8ModifyModeController;
    private X8RockerModeController x8RockerModeController;

    public X8MainFcAllSettingControler(View rootView, Activity activity) {
        super(rootView);
        this.isCanClose = true;
        this.mSecondMenu = SecondMenu.IDLE;
        this.mFirstMenu = FirstMenu.IDLE;
        this.mIX8DroneStateListener = new IX8DroneStateListener() {
            @Override
            public void onCalibrationItemClick() {
                X8MainFcAllSettingControler.this.showCampView();
            }
        };
        this.isfiveKeyOpen = false;
        this.mX8FcItemListener = new IX8FcItemListener() {
            @Override
            public void onCalibrationItemClick() {
                X8MainFcAllSettingControler.this.showCampView();
            }

            @Override
            public void onFcExpSettingClick() {
                X8MainFcAllSettingControler.this.showExpUi();
            }

            @Override
            public void onFcSensitivitySettingClick() {
                X8MainFcAllSettingControler.this.showSensitivityUi();
            }
        };
        this.mX8RcItemControllerListener = new IX8RcItemControllerListener() {
            @Override
            public void onRockerModeClicked(IX8RcRockerListener onRcCtrlModelListener) {
                X8MainFcAllSettingControler.this.rcCtrlModelListener = onRcCtrlModelListener;
                X8MainFcAllSettingControler.this.showRockerModeUi();
            }

            @Override
            public void onFiveKeyClicked(int key, int curIndex) {
                X8MainFcAllSettingControler.this.showFiveKeyUi(key, curIndex);
            }

            @Override
            public void onRcMatchCode() {
                X8MainFcAllSettingControler.this.showRcMatchCodeView();
            }

            @Override
            public void onRcCalibration() {
                X8MainFcAllSettingControler.this.showRcCalibrationUi();
            }
        };
        this.mx8FiveKeyCalibrationListener = new IX8FiveKeyDefineListener() {
            @Override
            public void onBack() {
                X8MainFcAllSettingControler.this.closeSecondUi();
            }

            @Override
            public void onSelected(int key, int position) {
                X8MainFcAllSettingControler.this.mX8FcSettingMenuController.setFiveKeyValue(key, position);
            }
        };
        this.mX8CalibrationListener = new IX8CalibrationListener() {
            @Override
            public void onCalibrationReturn() {
                X8MainFcAllSettingControler.this.closeSecondUi();
            }

            @Override
            public void onCalibrationStart() {
            }

            @Override
            public void onOpen() {
                X8MainFcAllSettingControler.this.isCanClose = false;
            }

            @Override
            public void onClose() {
                X8MainFcAllSettingControler.this.isCanClose = true;
            }
        };
        this.mx8RockerCalibrationListener = new IX8CalibrationListener() {
            @Override
            public void onCalibrationReturn() {
                X8MainFcAllSettingControler.this.closeSecondUi();
            }

            @Override
            public void onCalibrationStart() {
            }

            @Override
            public void onOpen() {
            }

            @Override
            public void onClose() {
            }
        };
        this.gimbalSettingListener = new IX8GimbalSettingListener() {
            @Override
            public void onGimbalCalibrationClick() {
                X8MainFcAllSettingControler.this.showGimbalCalibarationUi();
            }

            @Override
            public void onHorizontalTrimClick() {
                if (X8MainFcAllSettingControler.this.listener != null) {
                    X8MainFcAllSettingControler.this.listener.onGimbalHorizontalTrimClick();
                }
            }

            @Override
            public void onAdvancedSetup() {
                if (X8MainFcAllSettingControler.this.listener != null) {
                    X8MainFcAllSettingControler.this.listener.onGimbalAdvancedSetup();
                }
            }

            @Override
            public void onGimbalXYZAdjust() {
                if (X8MainFcAllSettingControler.this.listener != null) {
                    X8MainFcAllSettingControler.this.listener.onGimbalXYZAdjustClick();
                }
            }
        };
        this.mX8FwUpgradeCtrlListener = new IX8FirmwareUpgradeControllerListener() {
            @Override
            public void onFirmwareUpgradeReturn() {
                X8MainFcAllSettingControler.this.closeSecondUi();
            }

            @Override
            public void onFirmwareUpgradeClick() {
                Intent intent = new Intent(X8MainFcAllSettingControler.this.contentView.getContext(), X8UpdateDetailActivity.class);
                X8MainFcAllSettingControler.this.contentView.getContext().startActivity(intent);
            }
        };
        this.modifyModeControllerListener = new IX8GeneraModifyModeControllerListener() {
            @Override
            public void returnBack() {
                X8MainFcAllSettingControler.this.closeSecondUi();
            }

            @Override
            public void onOpen() {
                X8MainFcAllSettingControler.this.isCanClose = false;
            }

            @Override
            public void onClose() {
                X8MainFcAllSettingControler.this.isCanClose = true;
            }
        };
        this.mActivity = activity;
        this.rlFcAllSetting = rootView.findViewById(R.id.x8_rl_main_fc_all_setting);
        this.contentView = rootView.findViewById(R.id.rl_main_fc_all_setting_content);
        this.firstContentView = rootView.findViewById(R.id.x8_all_setting_first_content);
        this.secondContentView = rootView.findViewById(R.id.x8_all_setting_second_content);
        this.rlFcAllSetting.setOnClickListener(this);
    }

    public void clearAllSecondController() {
        this.mCurrentSecondController = null;
        this.mX8DroneCalibrationController = null;
        this.x8FiveKeyDefineController = null;
        this.x8FcSensitivitySettingController = null;
        this.x8FcExpSettingController = null;
        this.rcCalibrationController = null;
        this.rcMatchCodeController = null;
        this.x8RockerModeController = null;
        this.mX8FrequencyPointController = null;
        if (this.x8ModifyModeController != null) {
            this.x8ModifyModeController.closeItem();
            this.x8ModifyModeController = null;
        }
        if (this.x8GimbalAdvancedSetupController != null) {
            this.x8GimbalAdvancedSetupController.closeItem();
            this.x8GimbalAdvancedSetupController = null;
        }
        this.x8CloudCalibrationController = null;
        this.mX8FirmwareUpgradeController = null;
        this.x8FlightLogListController = null;
    }

    public void clearAllFirstController() {
        this.mCurrentFirstController = null;
        this.mX8FcSettingMenuController = null;
        this.mX8DroneInfoStateController = null;
    }

    @Override
    public void initViews(View rootView) {
    }

    @Override
    public void initActions() {
    }

    public void onRcConnected(boolean isConnect) {
        switch (this.mSecondMenu) {
            case ROCKERMODE:
                if (this.x8RockerModeController != null) {
                    this.x8RockerModeController.onRcConnected(isConnect);
                    break;
                }
                break;
            case RCCALIBRATION:
                if (this.rcCalibrationController != null) {
                    this.rcCalibrationController.checkRcConnect(isConnect);
                }
                if (this.rcMatchCodeController != null) {
                    this.rcMatchCodeController.checkRcConnect(isConnect);
                    break;
                }
                break;
        }
        if (this.mFirstMenu == FirstMenu.FCSETTINGMENU) {
            if (this.mX8FcSettingMenuController != null) {
                this.mX8FcSettingMenuController.onRcConnected(isConnect);
                return;
            }
            return;
        }
    }

    @Override
    public void defaultVal() {
    }

    public void onBatteryReceiveListener(AutoFcBattery autoFcBattery) {
        if (this.mX8FcSettingMenuController != null) {
            this.mX8FcSettingMenuController.onBatteryReceive(autoFcBattery);
        }
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isShow) {
            defaultVal();
            if (this.mCurrentFirstController != null) {
                this.mCurrentFirstController.onDroneConnected(b);
            }
            if (this.mCurrentSecondController != null) {
                this.mCurrentSecondController.onDroneConnected(b);
            }
            if (this.mX8FcSettingMenuController != null) {
                this.mX8FcSettingMenuController.onDroneConnected(b);
            }
            if (this.x8CloudCalibrationController != null) {
                this.x8CloudCalibrationController.onDroneConnected(b);
            }
            if (this.mX8FirmwareUpgradeController != null) {
                this.mX8FirmwareUpgradeController.onDroneConnected(b);
            }
            if (this.mX8FrequencyPointController != null) {
                this.mX8FrequencyPointController.onDroneConnected(b);
            }
            if (this.x8GimbalAdvancedSetupController != null) {
                this.x8GimbalAdvancedSetupController.onDroneConnected(b);
            }
        }
    }

    public synchronized void showAllSettingUi(X8FcAllSettingMenuEnum menu) {
        if (this.firstContentView != null && ((RelativeLayout) this.firstContentView).getChildCount() == 0) {
            this.rlFcAllSetting.setVisibility(0);
            if (menu == X8FcAllSettingMenuEnum.DRONE_STATE) {
                showDroneStateView();
            } else {
                showFcMenuView(menu);
            }
            if (!this.isShow) {
                this.isShow = true;
                if (this.width == 0) {
                    this.contentView.setAlpha(0.0f);
                    this.contentView.post(new Runnable() {
                        @Override
                        public void run() {
                            X8MainFcAllSettingControler.this.contentView.setAlpha(1.0f);
                            X8MainFcAllSettingControler.this.contentView.getWidth();
                            X8MainFcAllSettingControler.this.width = X8MainFcAllSettingControler.this.contentView.getHeight();
                            PercentRelativeLayout.LayoutParams lp = (PercentRelativeLayout.LayoutParams) X8MainFcAllSettingControler.this.contentView.getLayoutParams();
                            X8MainFcAllSettingControler.this.contentViewTopMargin = lp.topMargin;
                            ObjectAnimator animatorY = ObjectAnimator.ofFloat(X8MainFcAllSettingControler.this.contentView, "translationY", (-X8MainFcAllSettingControler.this.width) - X8MainFcAllSettingControler.this.contentViewTopMargin, 0.0f);
                            animatorY.setDuration(250L);
                            animatorY.start();
                        }
                    });
                } else {
                    ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.contentView, "translationY", (-this.width) - this.contentViewTopMargin, 0.0f);
                    animatorY.setDuration(250L);
                    animatorY.start();
                }
            }
            if (this.listener != null) {
                this.listener.showAllSetting();
            }
        }
    }

    public void setMapVideoController(X8MapVideoController mapVideoController) {
        this.mapVideoController = mapVideoController;
    }

    public void setRlCoverListener(IX8MainCoverListener coverListener) {
        this.coverListener = coverListener;
    }

    public void showFcMenuView(X8FcAllSettingMenuEnum menu) {
        this.firstContentView.setVisibility(0);
        this.rlFcAllSetting.setVisibility(0);
        this.mX8FcSettingMenuController = new X8FcSettingMenuController(this.firstContentView);
        this.mX8FcSettingMenuController.setListener(this.mapVideoController, this.fcManager, this.fcCtrlManager, this.gimbalManager, this.mX8FcItemListener, this.mX8RcItemControllerListener, this.mIX8GeneralItemControllerListerner, this.gimbalSettingListener, this.coverListener);
        this.mX8FcSettingMenuController.switchMenu(menu);
        this.mX8FcSettingMenuController.showItem();
        this.mCurrentFirstController = this.mX8FcSettingMenuController;
        this.mFirstMenu = FirstMenu.FCSETTINGMENU;
    }

    public void showDroneStateView() {
        this.firstContentView.setVisibility(0);
        this.rlFcAllSetting.setVisibility(0);
        this.mX8DroneInfoStateController = new X8DroneInfoStateController(this.firstContentView);
        this.mX8DroneInfoStateController.setListener(this.mIX8DroneStateListener);
        this.mCurrentFirstController = this.mX8DroneInfoStateController;
        this.mFirstMenu = FirstMenu.DRONESTATE;
    }

    public void showCampView() {
        this.firstContentView.setVisibility(8);
        this.secondContentView.setVisibility(0);
        this.mX8DroneCalibrationController = new X8DroneCalibrationController(this.secondContentView);
        this.mX8DroneCalibrationController.setCalibrationListener(this.mX8CalibrationListener);
        this.mX8DroneCalibrationController.setFcCtrlManager(this.fcCtrlManager);
        this.mX8DroneCalibrationController.showItem();
        this.mCurrentSecondController = this.mX8DroneCalibrationController;
        this.mSecondMenu = SecondMenu.DRONECALIBRATION;
    }

    public void closeFcSettingUi(final boolean needResponse) {
        this.isCanClose = true;
        if (this.isShow) {
            this.isShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.contentView, "translationY", 0.0f, (-this.width) - this.contentViewTopMargin);
            translationRight.setDuration(250L);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                @Override
                // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8MainFcAllSettingControler.this.rlFcAllSetting.setVisibility(View.GONE);
                    X8MainFcAllSettingControler.this.closeSecondUi();
                    X8MainFcAllSettingControler.this.closeRootUi();
                    if (needResponse && X8MainFcAllSettingControler.this.listener != null) {
                        X8MainFcAllSettingControler.this.listener.closeAllSetting();
                    }
                }
            });
        }
    }

    public void setX8GeneralItemControllerListerner(IX8GeneralItemControllerListerner ix8GeneralItemControllerListerner) {
        this.mIX8GeneralItemControllerListerner = ix8GeneralItemControllerListerner;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.x8_rl_main_fc_all_setting && this.isCanClose && !(this.mCurrentSecondController instanceof X8RCMatchCodeController) && !(this.mCurrentSecondController instanceof X8CloudCalibrationController) && !(this.mCurrentSecondController instanceof X8RCCalibrationController)) {
            closeFcSettingUi(true);
        }
    }

    public void fiveKeySwitchUI() {
        if (this.rlFcAllSetting.getVisibility() != View.VISIBLE) {
            showAllSettingUi(X8FcAllSettingMenuEnum.BATTERY_ITEM);
            this.isfiveKeyOpen = true;
        } else if (this.isCanClose && this.mFirstMenu != FirstMenu.DRONESTATE && this.isfiveKeyOpen) {
            closeFcSettingUi(true);
            this.isfiveKeyOpen = false;
        }
    }

    public void fiveKeySwitchDronestateUI() {
        if (this.rlFcAllSetting.getVisibility() != View.VISIBLE) {
            showAllSettingUi(X8FcAllSettingMenuEnum.DRONE_STATE);
        } else if (this.isCanClose && this.mFirstMenu != FirstMenu.FCSETTINGMENU) {
            closeFcSettingUi(true);
        }
    }

    public void setListener(IX8FcAllSettingListener mX8FcAllSettingListener) {
        this.listener = mX8FcAllSettingListener;
    }

    public void showSensitivityUi() {
        this.firstContentView.setVisibility(View.GONE);
        this.secondContentView.setVisibility(View.VISIBLE);
        this.x8FcSensitivitySettingController = new X8FcSensitivitySettingController(this.secondContentView);
        this.x8FcSensitivitySettingController.setCalibrationListener(this.mX8CalibrationListener);
        this.x8FcSensitivitySettingController.setFcCtrlManager(this.fcCtrlManager);
        this.x8FcSensitivitySettingController.showItem();
        this.mCurrentSecondController = this.x8FcSensitivitySettingController;
        this.mSecondMenu = SecondMenu.SENSITIVITY;
    }

    public void showExpUi() {
        this.firstContentView.setVisibility(View.GONE);
        this.secondContentView.setVisibility(View.VISIBLE);
        this.x8FcExpSettingController = new X8FcExpSettingController(this.secondContentView);
        this.x8FcExpSettingController.setCalibrationListener(this.mX8CalibrationListener);
        this.x8FcExpSettingController.setFcCtrlManager(this.fcCtrlManager);
        this.x8FcExpSettingController.showItem();
        this.mCurrentSecondController = this.x8FcExpSettingController;
        this.mSecondMenu = SecondMenu.FCEXP;
    }

    public void closeSecondUi() {
        if (this.x8FlightLogListController != null) {
            this.x8FlightLogListController.remioveDownNoticeList();
        }
        clearAllSecondController();
        this.mSecondMenu = SecondMenu.IDLE;
        this.firstContentView.setVisibility(View.VISIBLE);
        ((ViewGroup) this.secondContentView).removeAllViews();
        this.secondContentView.setVisibility(View.GONE);
    }

    public void closeRootUi() {
        if (this.mX8FcSettingMenuController != null) {
            this.mX8FcSettingMenuController.closeItem();
        }
        clearAllFirstController();
        ((ViewGroup) this.firstContentView).removeAllViews();
        this.firstContentView.setVisibility(8);
        this.mFirstMenu = FirstMenu.IDLE;
    }

    public void showFiveKeyUi(int key, int curIndex) {
        this.firstContentView.setVisibility(8);
        this.secondContentView.setVisibility(0);
        this.x8FiveKeyDefineController = new X8FiveKeyDefineController(this.secondContentView);
        this.x8FiveKeyDefineController.setCalibrationListener(this.mx8FiveKeyCalibrationListener);
        this.x8FiveKeyDefineController.showItem();
        this.x8FiveKeyDefineController.setCurIndex(key, curIndex);
        this.mCurrentSecondController = this.x8FiveKeyDefineController;
        this.mSecondMenu = SecondMenu.FIVEKEY;
    }

    public void showRcCalibrationUi() {
        this.firstContentView.setVisibility(8);
        this.secondContentView.setVisibility(0);
        this.rcCalibrationController = new X8RCCalibrationController(this.secondContentView);
        this.rcCalibrationController.setFcCtrlManager(this.fcCtrlManager);
        this.rcCalibrationController.setIx8CalibrationListener(this.mx8RockerCalibrationListener);
        this.rcCalibrationController.openUi();
        this.mCurrentSecondController = this.rcCalibrationController;
        this.mSecondMenu = SecondMenu.RCCALIBRATION;
    }

    public void showRcMatchCodeView() {
        this.firstContentView.setVisibility(8);
        this.secondContentView.setVisibility(0);
        this.rcMatchCodeController = new X8RCMatchCodeController(this.secondContentView);
        this.rcMatchCodeController.setIx8CalibrationListener(this.mx8RockerCalibrationListener);
        this.rcMatchCodeController.openUi();
        this.mCurrentSecondController = this.rcMatchCodeController;
        this.mSecondMenu = SecondMenu.RCMATCHCODE;
    }

    public void showRockerModeUi() {
        this.firstContentView.setVisibility(8);
        this.secondContentView.setVisibility(0);
        this.x8RockerModeController = new X8RockerModeController(this.secondContentView, this.rcCtrlModelListener);
        this.x8RockerModeController.setCalibrationListener(this.mx8RockerCalibrationListener);
        this.x8RockerModeController.setFcCtrlManager(this.fcCtrlManager);
        this.x8RockerModeController.showItem();
        this.mCurrentSecondController = this.x8RockerModeController;
        this.mSecondMenu = SecondMenu.ROCKERMODE;
    }

    public void showGimbalCalibarationUi() {
        this.firstContentView.setVisibility(8);
        this.secondContentView.setVisibility(0);
        this.x8CloudCalibrationController = new X8CloudCalibrationController(this.secondContentView);
        this.x8CloudCalibrationController.setFcCtrlManager(this.fcCtrlManager);
        this.x8CloudCalibrationController.setIx8CalibrationListener(this.mX8CalibrationListener);
        this.x8CloudCalibrationController.openUi();
        this.mCurrentSecondController = this.x8CloudCalibrationController;
        this.mSecondMenu = SecondMenu.GIMBALCALIBARATION;
    }

    public void startGimbalCalibaration() {
        showGimbalCalibarationUi();
        this.x8CloudCalibrationController.startCalibration();
    }

    public void setVersion() {
        this.firstContentView.setVisibility(8);
        this.secondContentView.setVisibility(0);
        this.mX8FirmwareUpgradeController = new X8FirmwareUpgradeController(this.secondContentView);
        this.mX8FirmwareUpgradeController.setOnFirmwareClickListener(this.mX8FwUpgradeCtrlListener);
        this.mX8FirmwareUpgradeController.showItem();
        this.mCurrentSecondController = this.mX8FirmwareUpgradeController;
        this.mSecondMenu = SecondMenu.VERSION;
    }

    public void showModifyMode() {
        this.firstContentView.setVisibility(8);
        this.secondContentView.setVisibility(0);
        this.x8ModifyModeController = new X8ModifyModeController(this.secondContentView);
        this.x8ModifyModeController.setFcCtrlManager(this.fcCtrlManager);
        this.x8ModifyModeController.setX8GimbalManager(this.gimbalManager);
        this.x8ModifyModeController.setModeControllerListener(this.modifyModeControllerListener);
        this.x8ModifyModeController.showItem();
        this.mCurrentSecondController = this.x8ModifyModeController;
        this.mSecondMenu = SecondMenu.MODIFYMODE;
    }

    public void showFlightlog() {
        if (HostConstants.getUserDetail().getFimiId() != null && !HostConstants.getUserDetail().getFimiId().equals("")) {
            if (this.mCustomLoadManage == null) {
                this.mCustomLoadManage = new CustomLoadManage();
            }
            CustomLoadManage customLoadManage = this.mCustomLoadManage;
            CustomLoadManage.x8ShowNoClick(this.mActivity);
            try {
                File flightPlaybackPath = new File(DirectoryPath.getX8flightPlaybackPath(""));
                try {
                    if (flightPlaybackPath.list().length > 0) {
                        File loginFlightPlaybackPath = new File(DirectoryPath.getX8LoginFlightPlaybackPath(""));
                        try {
                            FileUtil.copyDir(flightPlaybackPath.getAbsolutePath(), loginFlightPlaybackPath.getAbsolutePath());
                            FileUtil.deleteFile(new File(DirectoryPath.getX8flightPlaybackPath(null)));
                        } catch (Exception e) {
                            e = e;
                            CustomLoadManage customLoadManage2 = this.mCustomLoadManage;
                            CustomLoadManage.dismiss();
                            e.printStackTrace();
                            this.firstContentView.setVisibility(8);
                            this.secondContentView.setVisibility(0);
                            this.x8FlightLogListController = new X8FlightLogListController(this.secondContentView, this.mCustomLoadManage);
                            this.x8FlightLogListController.setModeControllerListener(this.modifyModeControllerListener);
                            this.x8FlightLogListController.showItem();
                            this.mCurrentSecondController = this.x8FlightLogListController;
                            this.mSecondMenu = SecondMenu.FLIGHT_LOG;
                            return;
                        }
                    }
                } catch (Exception e2) {
                }
            } catch (Exception e3) {
            }
            this.firstContentView.setVisibility(8);
            this.secondContentView.setVisibility(0);
            this.x8FlightLogListController = new X8FlightLogListController(this.secondContentView, this.mCustomLoadManage);
            this.x8FlightLogListController.setModeControllerListener(this.modifyModeControllerListener);
            this.x8FlightLogListController.showItem();
            this.mCurrentSecondController = this.x8FlightLogListController;
            this.mSecondMenu = SecondMenu.FLIGHT_LOG;
            return;
        }
        final Context context = this.contentView.getContext();
        this.loginDialogRestart = new X8DoubleCustomDialog(context, context.getString(R.string.x8_modify_black_box_login_title), context.getString(R.string.x8_playback_login_hint), context.getString(R.string.x8_modify_black_box_login_go), new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
                X8MainFcAllSettingControler.this.loginDialogRestart.dismiss();
            }

            @Override
            public void onRight() {
                SPStoreManager.getInstance().saveInt(Constants.SP_PERSON_USER_TYPE, Constants.UserType.Ideal.ordinal());
                Intent intent = Router.invoke(context, "activity://app.SplashActivity");
                context.startActivity(intent);
            }
        });
        this.loginDialogRestart.show();
    }

    public void showGimbalAdvancedSetupMode() {
        this.firstContentView.setVisibility(View.GONE);
        this.secondContentView.setVisibility(View.VISIBLE);
        this.x8GimbalAdvancedSetupController = new X8GimbalAdvancedSetupController(this.secondContentView);
        this.x8GimbalAdvancedSetupController.setGimbalManager(this.gimbalManager);
        this.x8GimbalAdvancedSetupController.setModeControllerListener(this.modifyModeControllerListener);
        this.x8GimbalAdvancedSetupController.setOnGimbalSettingListener(this.gimbalSettingListener);
        this.x8GimbalAdvancedSetupController.showItem();
        this.mCurrentSecondController = this.x8GimbalAdvancedSetupController;
        this.mSecondMenu = SecondMenu.ADVANCED_SETUP;
    }

    public void showFrequencyPoint() {
        this.firstContentView.setVisibility(View.GONE);
        this.secondContentView.setVisibility(View.VISIBLE);
        this.mX8FrequencyPointController = new X8FrequencyPointController(this.secondContentView);
        this.mX8FrequencyPointController.setListener(new IX8FrequencyPointListener() {
            @Override
            public void onBack() {
                X8MainFcAllSettingControler.this.closeSecondUi();
            }
        });
        this.mX8FrequencyPointController.showItem();
        this.mCurrentSecondController = this.mX8FrequencyPointController;
        this.mSecondMenu = SecondMenu.FREQUENCYPOINT;
    }

    public void onVersionChange() {
        if (this.mX8FirmwareUpgradeController != null) {
            this.mX8FirmwareUpgradeController.onVersionChange();
        }
    }

    public void setFcManager(FcManager fcManager) {
        this.fcManager = fcManager;
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setGimbalManager(X8GimbalManager gimbalManager) {
        this.gimbalManager = gimbalManager;
    }

    @Override
    public void unMountError(boolean mountError) {
        if (this.mCurrentSecondController != null) {
            this.mCurrentSecondController.unMountError(mountError);
        }
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    @Override
    public boolean isRunningTask() {
        if (this.mCurrentSecondController == null) {
            return false;
        }
        return this.mCurrentSecondController.isRunningTask();
    }


    public enum FirstMenu {
        IDLE,
        DRONESTATE,
        FCSETTINGMENU
    }


    public enum SecondMenu {
        IDLE,
        DRONECALIBRATION,
        FIVEKEY,
        FCEXP,
        SENSITIVITY,
        RCCALIBRATION,
        RCMATCHCODE,
        ROCKERMODE,
        GIMBALCALIBARATION,
        MODIFYMODE,
        VERSION,
        FREQUENCYPOINT,
        ADVANCED_SETUP,
        FLIGHT_LOG
    }
}
