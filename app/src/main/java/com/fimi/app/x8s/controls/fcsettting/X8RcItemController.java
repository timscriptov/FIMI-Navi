package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8RcItemControllerListener;
import com.fimi.app.x8s.interfaces.IX8RcRockerListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8TabItem;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetRcMode;
import com.fimi.x8sdk.modulestate.StateManager;

/* loaded from: classes.dex */
public class X8RcItemController extends AbsX8Controllers implements View.OnClickListener {
    public static final int FIVE_KEY_CENTER = 4;
    public static final int FIVE_KEY_DOWN = 1;
    public static final int FIVE_KEY_LEFT = 2;
    public static final int FIVE_KEY_RIGHT = 3;
    public static final int FIVE_KEY_UP = 0;
    public static String[] FIVE_KEY_DATA_ARRAY = null;
    int currAPModel;
    IX8RcRockerListener rcCtrlModelListener;
    private Button btnFiveKeyCenter;
    private Button btnFiveKeyDown;
    private Button btnFiveKeyLeft;
    private Button btnFiveKeyRight;
    private Button btnFiveKeyUp;
    private Button btnRcCalibration;
    private Button btnRcCode;
    private Button btnRockerMode;
    private FcCtrlManager fcCtrlManager;
    private IX8RcItemControllerListener listener;
    private Context mConext;
    private View rlFcItem;
    private ViewStub stubFcItem;
    private X8TabItem thApModule;
    private X8DoubleCustomDialog x8DoubleCustomDialog;
    private Button x8RcBtnRestParams;

    public X8RcItemController(View rootView) {
        super(rootView);
        this.currAPModel = 0;
        this.rcCtrlModelListener = new IX8RcRockerListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RcItemController.3
            @Override // com.fimi.app.x8s.interfaces.IX8RcRockerListener
            public void onRcCtrlModelListener(int result) {
                X8RcItemController.this.showRcCtrlModel(result);
            }
        };
        this.mConext = rootView.getContext();
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initViews(View rootView) {
        this.stubFcItem = (ViewStub) rootView.findViewById(R.id.stub_rc_item);
        FIVE_KEY_DATA_ARRAY = rootView.getContext().getResources().getStringArray(R.array.x8_five_key_define_option);
    }

    public void showApDialog(final int index) {
        X8DoubleCustomDialog apDialog = new X8DoubleCustomDialog(this.rootView.getContext(), getString(R.string.x8_rc_setting_ap_dialog_title), getString(R.string.x8_rc_setting_ap_dialog_content), new X8DoubleCustomDialog.onDialogButtonClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RcItemController.1
            @Override // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
            public void onLeft() {
                X8RcItemController.this.thApModule.setSelect(X8RcItemController.this.currAPModel);
            }

            @Override // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
            public void onRight() {
                X8RcItemController.this.fcCtrlManager.setApMode((byte) index, new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RcItemController.1.1
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8RcItemController.this.thApModule.setSelect(index);
                            X8RcItemController.this.currAPModel = index;
                            X8RcItemController.this.fcCtrlManager.setApModeRestart(new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RcItemController.1.1.1
                                @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                                public void onComplete(CmdResult cmdResult2, Object o2) {
                                    if (cmdResult2.isSuccess()) {
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        apDialog.show();
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initActions() {
        if (this.rlFcItem != null) {
            this.thApModule.setOnSelectListener(new X8TabItem.OnSelectListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RcItemController.2
                @Override // com.fimi.app.x8s.widget.X8TabItem.OnSelectListener
                public void onSelect(int index, String text) {
                    if (X8RcItemController.this.currAPModel != index) {
                        if (index == 0) {
                            index = 1;
                        } else if (index == 1) {
                            index = 0;
                        }
                        X8RcItemController.this.showApDialog(index);
                    }
                }
            });
        }
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void defaultVal() {
    }

    public void requestRcCtrlModeValue() {
        if (this.fcCtrlManager != null) {
            this.fcCtrlManager.getRcCtrlMode(new UiCallBackListener<AckGetRcMode>() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RcItemController.4
                @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                public void onComplete(CmdResult cmdResult, AckGetRcMode obj) {
                    if (obj != null) {
                        X8RcItemController.this.showRcCtrlModel(obj.getMode());
                    }
                }
            });
        }
    }

    public void showRcCtrlModel(int result) {
        switch (result) {
            case 1:
                this.btnRockerMode.setText(R.string.x8_rc_setting_america_rocker);
                return;
            case 2:
                this.btnRockerMode.setText(R.string.x8_rc_setting_japanese_rocker);
                return;
            case 3:
                this.btnRockerMode.setText(R.string.x8_rc_setting_chinese_rocker);
                return;
            default:
                return;
        }
    }

    public void onRcConnected(boolean isConnect) {
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void showItem() {
        if (this.rlFcItem == null) {
            View view = this.stubFcItem.inflate();
            this.rlFcItem = view.findViewById(R.id.x8_rl_main_rc_item);
            this.thApModule = (X8TabItem) this.rlFcItem.findViewById(R.id.th_ap_module);
            this.btnRcCalibration = (Button) this.rlFcItem.findViewById(R.id.btn_rc_calibration);
            this.btnRockerMode = (Button) this.rlFcItem.findViewById(R.id.btn_rocker_mode);
            this.btnRcCode = (Button) this.rlFcItem.findViewById(R.id.btn_rc_code);
            this.btnFiveKeyUp = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_up);
            this.btnFiveKeyDown = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_down);
            this.btnFiveKeyLeft = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_left);
            this.btnFiveKeyRight = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_right);
            this.btnFiveKeyCenter = (Button) this.rlFcItem.findViewById(R.id.btn_five_key_center);
            this.x8RcBtnRestParams = (Button) this.rlFcItem.findViewById(R.id.x8_rc_btn_rest_params);
            this.btnRcCalibration.setOnClickListener(this);
            this.btnRockerMode.setOnClickListener(this);
            this.btnRcCode.setOnClickListener(this);
            this.btnFiveKeyUp.setOnClickListener(this);
            this.btnFiveKeyDown.setOnClickListener(this);
            this.btnFiveKeyLeft.setOnClickListener(this);
            this.btnFiveKeyRight.setOnClickListener(this);
            this.btnFiveKeyCenter.setOnClickListener(this);
            this.x8RcBtnRestParams.setOnClickListener(this);
            resetFiveKey();
            initActions();
        }
        this.rlFcItem.setVisibility(0);
        requestRcCtrlModeValue();
        this.isShow = true;
    }

    private void resetFiveKey() {
        this.btnFiveKeyUp.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_UP_KEY)]);
        this.btnFiveKeyDown.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_DOWN_KEY, 1)]);
        this.btnFiveKeyLeft.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_LEFT_KEY, 2)]);
        this.btnFiveKeyRight.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_RIGHT_KEY, 3)]);
        this.btnFiveKeyCenter.setText(FIVE_KEY_DATA_ARRAY[SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_CENTRE_KEY, 4)]);
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void onDroneConnected(boolean b) {
        float f = 1.0f;
        boolean z = true;
        if (this.isShow) {
            if (this.thApModule != null) {
                boolean canSet = b && StateManager.getInstance().getX8Drone().isOnGround();
                this.thApModule.setAlpha(canSet ? 1.0f : 0.4f);
                this.thApModule.setEnabled(canSet);
            }
            boolean isOpenRockerModer = (b && StateManager.getInstance().getX8Drone().isInSky()) ? false : true;
            if (this.btnRockerMode != null) {
                this.btnRockerMode.setAlpha(isOpenRockerModer ? 1.0f : 0.4f);
                this.btnRockerMode.setEnabled(isOpenRockerModer);
            }
            int appModle = StateManager.getInstance().getRelayState().getApModel();
            if (appModle == 0) {
                this.currAPModel = 1;
            } else {
                this.currAPModel = 0;
            }
            this.thApModule.setSelect(this.currAPModel);
            boolean isOngroud = StateManager.getInstance().getX8Drone().isOnGround();
            Button button = this.x8RcBtnRestParams;
            if (!isOngroud || !b) {
                z = false;
            }
            button.setEnabled(z);
            Button button2 = this.x8RcBtnRestParams;
            if (!isOngroud || !b) {
                f = 0.4f;
            }
            button2.setAlpha(f);
        }
    }

    private void setViewEnabled(boolean isEnabled) {
        this.btnRockerMode.setEnabled(isEnabled);
        this.btnRcCode.setEnabled(isEnabled);
        this.btnFiveKeyUp.setEnabled(isEnabled);
        this.btnFiveKeyDown.setEnabled(isEnabled);
        this.btnFiveKeyLeft.setEnabled(isEnabled);
        this.btnFiveKeyRight.setEnabled(isEnabled);
        this.btnFiveKeyCenter.setEnabled(isEnabled);
        this.btnRockerMode.setAlpha(isEnabled ? 1.0f : 0.4f);
        this.btnRcCode.setAlpha(isEnabled ? 1.0f : 0.4f);
        this.btnFiveKeyUp.setAlpha(isEnabled ? 1.0f : 0.4f);
        this.btnFiveKeyDown.setAlpha(isEnabled ? 1.0f : 0.4f);
        this.btnFiveKeyLeft.setAlpha(isEnabled ? 1.0f : 0.4f);
        this.btnFiveKeyRight.setAlpha(isEnabled ? 1.0f : 0.4f);
        this.btnFiveKeyCenter.setAlpha(isEnabled ? 1.0f : 0.4f);
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void closeItem() {
        if (this.rlFcItem != null) {
            this.rlFcItem.setVisibility(8);
            this.isShow = false;
        }
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_rocker_mode) {
            if (this.listener != null) {
                this.listener.onRockerModeClicked(this.rcCtrlModelListener);
            }
        } else if (i == R.id.btn_five_key_up) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(0, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_UP_KEY));
            }
        } else if (i == R.id.btn_five_key_down) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(1, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_DOWN_KEY, 1));
            }
        } else if (i == R.id.btn_five_key_left) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(2, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_LEFT_KEY, 2));
            }
        } else if (i == R.id.btn_five_key_right) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(3, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_RIGHT_KEY, 3));
            }
        } else if (i == R.id.btn_five_key_center) {
            if (this.listener != null) {
                this.listener.onFiveKeyClicked(4, SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_CENTRE_KEY, 4));
            }
        } else if (i == R.id.btn_rc_code) {
            if (this.listener != null) {
                this.listener.onRcMatchCode();
                closeUi();
            }
        } else if (i == R.id.btn_rc_calibration) {
            if (this.listener != null) {
                this.listener.onRcCalibration();
                closeUi();
            }
        } else if (i == R.id.x8_rc_btn_rest_params) {
            showRestParamDialog();
        }
    }

    public void showRestParamDialog() {
        if (this.x8DoubleCustomDialog == null) {
            this.x8DoubleCustomDialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_rc_reset_params), this.rootView.getContext().getString(R.string.x8_rc_reset_params_hint), this.rootView.getContext().getString(R.string.x8_general_rest), new X8DoubleCustomDialog.onDialogButtonClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RcItemController.5
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8RcItemController.this.restRcSystemParams();
                }
            });
        }
        this.x8DoubleCustomDialog.show();
    }

    public void restRcSystemParams() {
        this.fcCtrlManager.setRcCtrlMode(new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RcItemController.6
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8RcItemController.this.setDefaultFiveKey();
                    X8RcItemController.this.requestRcCtrlModeValue();
                    X8ToastUtil.showToast(X8RcItemController.this.mConext, X8RcItemController.this.mConext.getString(R.string.x8_rc_reset_params_successd), 1);
                    return;
                }
                X8ToastUtil.showToast(X8RcItemController.this.mConext, X8RcItemController.this.mConext.getString(R.string.x8_rc_reset_params_hint_failed), 1);
            }
        }, (byte) 1);
    }

    public void setDefaultFiveKey() {
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_UP_KEY, 0);
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_DOWN_KEY, 1);
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_LEFT_KEY, 2);
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_RIGHT_KEY, 3);
        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_CENTRE_KEY, 4);
        resetFiveKey();
    }

    public void setListener(IX8RcItemControllerListener listener) {
        this.listener = listener;
    }

    public void setFiveKeyValue(int key, int position) {
        switch (key) {
            case 0:
                this.btnFiveKeyUp.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            case 1:
                this.btnFiveKeyDown.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            case 2:
                this.btnFiveKeyLeft.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            case 3:
                this.btnFiveKeyRight.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            case 4:
                this.btnFiveKeyCenter.setText(FIVE_KEY_DATA_ARRAY[position]);
                return;
            default:
                return;
        }
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public boolean onClickBackKey() {
        return false;
    }
}
