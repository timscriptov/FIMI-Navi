package com.fimi.app.x8s.controls;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fimi.TcpClient;
import com.fimi.android.app.R;
import com.fimi.app.x8s.entity.X8AiModeState;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8CameraPersonLacationListener;
import com.fimi.app.x8s.manager.X8MapGetCityManager;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.x8sdk.dataparser.AutoFcHeart;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;

/* loaded from: classes.dex */
public class X8RightIconForMapController extends AbsX8Controllers implements View.OnClickListener {
    private X8sMainActivity activity;
    private ImageButton imbAiFly;
    private ImageButton imbLocation;
    private ImageView imgAiReturnHome;
    private ImageView imgAiTakeLandOff;
    private ImageView imgSetHomeByDv;
    private ImageView imgSetHomeByMan;
    private boolean isAiRunning;
    private boolean isVPUMode;
    private X8AiModeState mX8AiModeState;
    private X8TLRDialogManager mX8TLRDialogManager;
    private IX8CameraPersonLacationListener personLacationListener;
    private View root;
    private LinearLayout vSetHomePoint;
    private LinearLayout vTakeoffLandingAiFly;

    public X8RightIconForMapController(View root) {
        super(root);
    }

    public X8RightIconForMapController(View root, X8sMainActivity activity, IX8CameraPersonLacationListener personLacationListener, X8AiModeState mX8AiModeState) {
        super(root);
        this.root = root;
        this.activity = activity;
        this.personLacationListener = personLacationListener;
        this.mX8AiModeState = mX8AiModeState;
        this.mX8TLRDialogManager = new X8TLRDialogManager(this);
        this.imbLocation = (ImageButton) root.findViewById(R.id.imb_location);
        this.imbAiFly = (ImageButton) root.findViewById(R.id.imb_ai_fly);
        this.vSetHomePoint = (LinearLayout) root.findViewById(R.id.ll_set_home_point);
        this.vTakeoffLandingAiFly = (LinearLayout) root.findViewById(R.id.ll_takeoff_landing_aifly);
        this.imgAiTakeLandOff = (ImageView) root.findViewById(R.id.imb_x8_take_off_land);
        this.imgAiReturnHome = (ImageView) root.findViewById(R.id.imb_x8_ai_reture);
        this.imgSetHomeByDv = (ImageView) root.findViewById(R.id.img_set_home_by_dv);
        this.imgSetHomeByMan = (ImageView) root.findViewById(R.id.img_set_home_by_man);
        getDroneState();
        if (this.isConect) {
            onFcHeart(null, this.isLowpower);
        } else {
            setAiFlyEnabled(false);
        }
        changeState();
        this.imgSetHomeByDv.setEnabled(false);
        this.imgSetHomeByMan.setEnabled(false);
        switchByCloseFullScreen(true);
        initAction();
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initViews(View rootView) {
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initActions() {
    }

    public void initAction() {
        this.imbLocation.setOnClickListener(this);
        this.imgSetHomeByDv.setOnClickListener(this);
        this.imgSetHomeByMan.setOnClickListener(this);
        this.imbAiFly.setOnClickListener(this);
        this.imgAiTakeLandOff.setOnClickListener(this);
        this.imgAiReturnHome.setOnClickListener(this);
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void defaultVal() {
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public boolean onClickBackKey() {
        return false;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == this.imbAiFly.getId()) {
            this.activity.onAiFlyClick();
        } else if (id == this.imbLocation.getId()) {
            showPersonLocation();
        } else if (id == R.id.imb_x8_take_off_land) {
            openTakeOffOrLandingUi();
        } else if (id == R.id.imb_x8_ai_reture) {
            this.mX8TLRDialogManager.showReturnDialog();
        } else if (id == R.id.img_set_home_by_dv) {
            String t = this.root.getContext().getString(R.string.x8_switch_home2_title);
            String m = this.root.getContext().getString(R.string.x8_switch_home2_drone_msg);
            X8DoubleCustomDialog dialog = new X8DoubleCustomDialog(this.root.getContext(), t, m, new X8DoubleCustomDialog.onDialogButtonClickListener() { // from class: com.fimi.app.x8s.controls.X8RightIconForMapController.1
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8MapGetCityManager.onSetHomeEvent(X8RightIconForMapController.this.activity, 0);
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else if (id == R.id.img_set_home_by_man) {
            String t2 = this.root.getContext().getString(R.string.x8_switch_home2_title);
            String m2 = this.root.getContext().getString(R.string.x8_switch_home2_phone_title);
            X8DoubleCustomDialog dialog2 = new X8DoubleCustomDialog(this.root.getContext(), t2, m2, new X8DoubleCustomDialog.onDialogButtonClickListener() { // from class: com.fimi.app.x8s.controls.X8RightIconForMapController.2
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8MapGetCityManager.onSetHomeEvent(X8RightIconForMapController.this.activity, 1);
                }
            });
            dialog2.setCanceledOnTouchOutside(false);
            dialog2.show();
        }
    }

    private void openTakeOffOrLandingUi() {
        DroneState droneState = StateManager.getInstance().getX8Drone();
        if (droneState.isInSky()) {
            this.mX8TLRDialogManager.showLandingDialog();
        } else {
            this.mX8TLRDialogManager.showTakeOffDialog(this.isVPUMode);
        }
    }

    public void showPersonLocation() {
        if (this.personLacationListener != null) {
            this.personLacationListener.showPersonLocation();
        }
    }

    public void switchByCloseFullScreen(boolean isFullVideo) {
        this.imbLocation.setVisibility(isFullVideo ? 8 : 0);
        this.vSetHomePoint.setVisibility(isFullVideo ? 8 : 0);
    }

    public void switch2Map(boolean isShow) {
        if (!this.mX8AiModeState.isAiModeStateReady()) {
            this.imbLocation.setVisibility(isShow ? 8 : 0);
            this.vSetHomePoint.setVisibility(isShow ? 8 : 0);
        }
    }

    public void closeUiForSetting() {
        showAll(false);
    }

    public void closeUiForTaskRunning() {
        this.vTakeoffLandingAiFly.setVisibility(8);
        boolean isShow = (1 == 0 || this.activity.getmMapVideoController().isFullVideo()) ? false : true;
        this.imbLocation.setVisibility(!isShow ? 8 : 0);
        this.vSetHomePoint.setVisibility(isShow ? 0 : 8);
    }

    public void openUiForSetting() {
        this.vTakeoffLandingAiFly.setVisibility(1 == 0 ? 8 : 0);
        boolean isShow = (1 == 0 || this.activity.getmMapVideoController().isFullVideo()) ? false : true;
        this.imbLocation.setVisibility(!isShow ? 8 : 0);
        this.vSetHomePoint.setVisibility(isShow ? 0 : 8);
        TcpClient.getIntance().sendLog("openUiForSetting--->" + isShow);
    }

    public void openUiForTaskRunning() {
        if (this.mX8AiModeState.isAiModeStateIdle()) {
            this.vTakeoffLandingAiFly.setVisibility(0);
        }
        boolean isShow = !this.activity.getmMapVideoController().isFullVideo();
        this.imbLocation.setVisibility(!isShow ? 8 : 0);
        this.vSetHomePoint.setVisibility(isShow ? 0 : 8);
    }

    public void showAll(boolean isShow) {
        this.imbLocation.setVisibility(!isShow ? 8 : 0);
        this.vSetHomePoint.setVisibility(!isShow ? 8 : 0);
        this.vTakeoffLandingAiFly.setVisibility(isShow ? 0 : 8);
    }

    public void showTakeoffLanding() {
        this.vTakeoffLandingAiFly.setVisibility(0);
    }

    public void showLocation() {
        boolean isShow = !this.activity.getmMapVideoController().isFullVideo();
        this.imbLocation.setVisibility(!isShow ? 8 : 0);
        this.vSetHomePoint.setVisibility(isShow ? 0 : 8);
    }

    public void showAiFlyIcon() {
        this.vTakeoffLandingAiFly.setVisibility(0);
    }

    public void d() {
        int i = R.drawable.x8_img_take_off_small;
        int res = R.drawable.x8_img_landing_small;
        int res2 = R.drawable.x8_img_return_small;
    }

    public void onFcHeart(AutoFcHeart fcHeart, boolean isLowPow) {
        if (fcHeart != null && fcHeart.getCtrlType() == 3) {
            this.isVPUMode = true;
        } else {
            this.isVPUMode = false;
        }
        DroneState state = StateManager.getInstance().getX8Drone();
        if (state.isInSky()) {
            this.imgAiTakeLandOff.setBackgroundResource(R.drawable.x8_btn_ai_small_landing);
            this.imgAiReturnHome.setEnabled(true);
            boolean takeoffLanding = false;
            if (StateManager.getInstance().getX8Drone().isPilotModePrimary()) {
                takeoffLanding = true;
            } else {
                int ctrtype = StateManager.getInstance().getX8Drone().getCtrlType();
                if (ctrtype == 1) {
                    takeoffLanding = false;
                } else if (ctrtype == 3) {
                    takeoffLanding = true;
                } else if (ctrtype == 2) {
                    takeoffLanding = true;
                }
            }
            if (this.isAiRunning) {
                this.vTakeoffLandingAiFly.setVisibility(8);
            }
            this.imgAiTakeLandOff.setEnabled(takeoffLanding);
        }
        if (state.isOnGround()) {
            if (state.isCanFly()) {
                this.imgAiTakeLandOff.setBackgroundResource(R.drawable.x8_btn_ai_small_takeoff);
                int ctrtype2 = StateManager.getInstance().getX8Drone().getCtrlType();
                boolean takeoffLanding2 = false;
                if (ctrtype2 == 1) {
                    takeoffLanding2 = false;
                } else if (ctrtype2 == 3) {
                    takeoffLanding2 = true;
                } else if (ctrtype2 == 2) {
                    takeoffLanding2 = true;
                }
                this.imgAiTakeLandOff.setEnabled(takeoffLanding2);
            } else {
                this.imgAiTakeLandOff.setEnabled(false);
            }
            this.imgAiReturnHome.setEnabled(false);
            int ctrtype3 = StateManager.getInstance().getX8Drone().getCtrlType();
            if (ctrtype3 != 1 && ctrtype3 != 3 && ctrtype3 == 2) {
            }
        }
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        getDroneState();
        changeState();
        if (!b) {
            this.imgAiTakeLandOff.setBackgroundResource(R.drawable.x8_btn_ai_small_takeoff);
            setAiFlyEnabled(false);
            this.mX8TLRDialogManager.onDroneConnected(b);
        }
    }

    public void changeState() {
        if (this.isConect && this.isInSky) {
            setChangeHomeEnabled(true);
        } else {
            setChangeHomeEnabled(false);
        }
    }

    public void setAiFlyEnabled(boolean b) {
        this.imgAiTakeLandOff.setEnabled(b);
        this.imgAiReturnHome.setEnabled(b);
    }

    public void setChangeHomeEnabled(boolean b) {
        this.imgSetHomeByDv.setEnabled(b);
        this.imgSetHomeByMan.setEnabled(b);
    }

    public void showSportState(AutoFcSportState state) {
        this.mX8TLRDialogManager.showSportState(state);
    }

    public X8sMainActivity getActivity() {
        return this.activity;
    }

    public void setAiRunning(boolean aiRunning) {
        this.isAiRunning = aiRunning;
    }
}
