package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8GimbalXYZAdjustListener;
import com.fimi.app.x8s.interfaces.IX8MainTopBarListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8GimbalXYZAdjustRelayout;
import com.fimi.app.x8s.widget.X8SingleCustomDialog;
import com.fimi.host.HostConstants;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.dataparser.AckCloudParamsNew;


public class X8GimbalXYZAdjustController extends AbsX8MenuBoxControllers implements View.OnClickListener {
    private final X8sMainActivity activity;
    Button btnCalibrate;
    Button x8BtnXyzAdjustExit;
    Button x8BtnXyzAdjustSave;
    private Button btnGet;
    private Button btnSave;
    private final String defaultUnit;
    private IX8MainTopBarListener ix8MainTopBarListener;
    private IX8GimbalXYZAdjustListener listener;
    private Context mContext;
    private X8GimbalManager mX8GimbalManager;
    private final float maxDefaultAdjustValue;
    private final float minDefaultAdjustValue;
    private final float valueOnFoot;
    private X8DoubleCustomDialog x8DoubleCustomDialog;
    private X8AiTipWithCloseView x8GimbalXYZAdiustTip;
    private X8SingleCustomDialog x8SingleCustomDialog;
    private X8GimbalXYZAdjustRelayout x8ViewGimbalP;
    private X8GimbalXYZAdjustRelayout x8ViewGimbalR;
    private X8GimbalXYZAdjustRelayout x8ViewGimbalY;
    private float[] xyzValue;

    public X8GimbalXYZAdjustController(View rootView, X8sMainActivity activity) {
        super(rootView);
        this.defaultUnit = "";
        this.valueOnFoot = 0.2f;
        this.minDefaultAdjustValue = -10.0f;
        this.maxDefaultAdjustValue = 10.0f;
        this.activity = activity;
    }

    @Override
    public void openUi() {
        this.isShow = true;
        LayoutInflater layoutInflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = layoutInflater.inflate(R.layout.x8_gimbal_xyz_adjust_layout, (ViewGroup) this.rootView, true);
        this.mContext = this.handleView.getContext();
        this.x8GimbalXYZAdiustTip = this.handleView.findViewById(R.id.x8_gimbal_xyz_adiust_tip);
        this.x8GimbalXYZAdiustTip.setTipText(getString(R.string.x8_gimbal_xyz_adjust_hint));
        this.x8BtnXyzAdjustExit = this.handleView.findViewById(R.id.x8_btn_xyz_adjust_exit);
        this.x8BtnXyzAdjustSave = this.handleView.findViewById(R.id.x8_btn_xyz_adjust_save);
        this.x8ViewGimbalP = this.handleView.findViewById(R.id.x8_view_gimbal_p);
        this.x8ViewGimbalR = this.handleView.findViewById(R.id.x8_view_gimbal_r);
        this.x8ViewGimbalY = this.handleView.findViewById(R.id.x8_view_gimbal_y);
        this.x8ViewGimbalR.getTvGimbalXyzName().setText(getString(R.string.x8_gimbal_xyz_adjust_r));
        this.x8ViewGimbalP.getTvGimbalXyzName().setText(getString(R.string.x8_gimbal_xyz_adjust_p));
        this.x8ViewGimbalY.getTvGimbalXyzName().setText(getString(R.string.x8_gimbal_xyz_adjust_y));
        initClickAction();
        super.openUi();
    }

    public void setListener(IX8GimbalXYZAdjustListener listener, IX8MainTopBarListener ix8MainTopBarListener) {
        this.listener = listener;
        this.ix8MainTopBarListener = ix8MainTopBarListener;
    }

    private void initClickAction() {
        this.x8BtnXyzAdjustExit.setOnClickListener(this);
        this.x8BtnXyzAdjustSave.setOnClickListener(this);
        this.mX8GimbalManager.getGcParamsNew(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    AckCloudParamsNew params = (AckCloudParamsNew) o;
                    if (params != null) {
                        X8GimbalXYZAdjustController.this.x8ViewGimbalR.getTvGimbalXyzValue().setText(params.getParam1() + X8GimbalXYZAdjustController.this.defaultUnit);
                        X8GimbalXYZAdjustController.this.x8ViewGimbalP.getTvGimbalXyzValue().setText(params.getParam2() + X8GimbalXYZAdjustController.this.defaultUnit);
                        X8GimbalXYZAdjustController.this.x8ViewGimbalY.getTvGimbalXyzValue().setText(params.getParam3() + X8GimbalXYZAdjustController.this.defaultUnit);
                        return;
                    }
                    X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, "获取云台参数失败", 0);
                }
            }
        });
        this.x8ViewGimbalR.getBtnGimbalXyzAdd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8GimbalXYZAdjustController.this.xyzValue = X8GimbalXYZAdjustController.this.getTxtValue();
                final float newValue = X8GimbalXYZAdjustController.this.xyzValue[0] + X8GimbalXYZAdjustController.this.valueOnFoot;
                if (X8GimbalXYZAdjustController.this.gimbalXyzAdjustMaxHint(newValue)) {
                    X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, X8GimbalXYZAdjustController.this.getString(R.string.x8_gimbal_xyz_adjust_max_hint), 1);
                } else {
                    X8GimbalXYZAdjustController.this.mX8GimbalManager.setGcParamsNew(2, newValue, X8GimbalXYZAdjustController.this.xyzValue[1], X8GimbalXYZAdjustController.this.xyzValue[2], new UiCallBackListener() {
                        @Override
                        public void onComplete(CmdResult cmdResult, Object o) {
                            if (cmdResult.isSuccess()) {
                                X8GimbalXYZAdjustController.this.x8ViewGimbalR.getTvGimbalXyzValue().setText(NumberUtil.decimalPointStr(newValue, 1));
                            } else {
                                X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, "设置云台参数失败", 0);
                            }
                        }
                    });
                }
            }
        });
        this.x8ViewGimbalR.getBtnGimbalXyzSubtract().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8GimbalXYZAdjustController.this.xyzValue = X8GimbalXYZAdjustController.this.getTxtValue();
                final float newValue = X8GimbalXYZAdjustController.this.xyzValue[0] - X8GimbalXYZAdjustController.this.valueOnFoot;
                if (X8GimbalXYZAdjustController.this.gimbalXyzAdjustMaxHint(newValue)) {
                    X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, X8GimbalXYZAdjustController.this.getString(R.string.x8_gimbal_xyz_adjust_max_hint), 1);
                } else {
                    X8GimbalXYZAdjustController.this.mX8GimbalManager.setGcParamsNew(2, newValue, X8GimbalXYZAdjustController.this.xyzValue[1], X8GimbalXYZAdjustController.this.xyzValue[2], new UiCallBackListener() {
                        @Override
                        public void onComplete(CmdResult cmdResult, Object o) {
                            if (cmdResult.isSuccess()) {
                                X8GimbalXYZAdjustController.this.x8ViewGimbalR.getTvGimbalXyzValue().setText(NumberUtil.decimalPointStr(newValue, 1));
                            } else {
                                X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, "设置云台参数失败", 0);
                            }
                        }
                    });
                }
            }
        });
        this.x8ViewGimbalP.getBtnGimbalXyzAdd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8GimbalXYZAdjustController.this.xyzValue = X8GimbalXYZAdjustController.this.getTxtValue();
                final float newValue = X8GimbalXYZAdjustController.this.xyzValue[1] + X8GimbalXYZAdjustController.this.valueOnFoot;
                if (X8GimbalXYZAdjustController.this.gimbalXyzAdjustMaxHint(newValue)) {
                    X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, X8GimbalXYZAdjustController.this.getString(R.string.x8_gimbal_xyz_adjust_max_hint), 1);
                } else {
                    X8GimbalXYZAdjustController.this.mX8GimbalManager.setGcParamsNew(2, X8GimbalXYZAdjustController.this.xyzValue[0], newValue, X8GimbalXYZAdjustController.this.xyzValue[2], new UiCallBackListener() {
                        @Override
                        public void onComplete(CmdResult cmdResult, Object o) {
                            if (cmdResult.isSuccess()) {
                                X8GimbalXYZAdjustController.this.x8ViewGimbalP.getTvGimbalXyzValue().setText(NumberUtil.decimalPointStr(newValue, 1) + X8GimbalXYZAdjustController.this.defaultUnit);
                            } else {
                                X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, "设置云台参数失败", 0);
                            }
                        }
                    });
                }
            }
        });
        this.x8ViewGimbalP.getBtnGimbalXyzSubtract().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8GimbalXYZAdjustController.this.xyzValue = X8GimbalXYZAdjustController.this.getTxtValue();
                final float newValue = X8GimbalXYZAdjustController.this.xyzValue[1] - X8GimbalXYZAdjustController.this.valueOnFoot;
                if (X8GimbalXYZAdjustController.this.gimbalXyzAdjustMaxHint(newValue)) {
                    X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, X8GimbalXYZAdjustController.this.getString(R.string.x8_gimbal_xyz_adjust_max_hint), 1);
                } else {
                    X8GimbalXYZAdjustController.this.mX8GimbalManager.setGcParamsNew(2, X8GimbalXYZAdjustController.this.xyzValue[0], newValue, X8GimbalXYZAdjustController.this.xyzValue[2], new UiCallBackListener() {
                        @Override
                        public void onComplete(CmdResult cmdResult, Object o) {
                            if (cmdResult.isSuccess()) {
                                X8GimbalXYZAdjustController.this.x8ViewGimbalP.getTvGimbalXyzValue().setText(NumberUtil.decimalPointStr(newValue, 1) + X8GimbalXYZAdjustController.this.defaultUnit);
                            } else {
                                X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, "设置云台参数失败", 0);
                            }
                        }
                    });
                }
            }
        });
        this.x8ViewGimbalY.getBtnGimbalXyzAdd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8GimbalXYZAdjustController.this.xyzValue = X8GimbalXYZAdjustController.this.getTxtValue();
                final float newValue = X8GimbalXYZAdjustController.this.xyzValue[2] + X8GimbalXYZAdjustController.this.valueOnFoot;
                if (X8GimbalXYZAdjustController.this.gimbalXyzAdjustMaxHint(newValue)) {
                    X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, X8GimbalXYZAdjustController.this.getString(R.string.x8_gimbal_xyz_adjust_max_hint), 1);
                } else {
                    X8GimbalXYZAdjustController.this.mX8GimbalManager.setGcParamsNew(2, X8GimbalXYZAdjustController.this.xyzValue[0], X8GimbalXYZAdjustController.this.xyzValue[1], newValue, new UiCallBackListener() {
                        @Override
                        public void onComplete(CmdResult cmdResult, Object o) {
                            if (cmdResult.isSuccess()) {
                                X8GimbalXYZAdjustController.this.x8ViewGimbalY.getTvGimbalXyzValue().setText(NumberUtil.decimalPointStr(newValue, 1));
                            } else {
                                X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, "设置云台参数失败", 0);
                            }
                        }
                    });
                }
            }
        });
        this.x8ViewGimbalY.getBtnGimbalXyzSubtract().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8GimbalXYZAdjustController.this.xyzValue = X8GimbalXYZAdjustController.this.getTxtValue();
                final float newValue = X8GimbalXYZAdjustController.this.xyzValue[2] - X8GimbalXYZAdjustController.this.valueOnFoot;
                if (X8GimbalXYZAdjustController.this.gimbalXyzAdjustMaxHint(newValue)) {
                    X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, X8GimbalXYZAdjustController.this.getString(R.string.x8_gimbal_xyz_adjust_max_hint), 1);
                } else {
                    X8GimbalXYZAdjustController.this.mX8GimbalManager.setGcParamsNew(2, X8GimbalXYZAdjustController.this.xyzValue[0], X8GimbalXYZAdjustController.this.xyzValue[1], newValue, new UiCallBackListener() {
                        @Override
                        public void onComplete(CmdResult cmdResult, Object o) {
                            if (cmdResult.isSuccess()) {
                                X8GimbalXYZAdjustController.this.x8ViewGimbalY.getTvGimbalXyzValue().setText(NumberUtil.decimalPointStr(newValue, 1));
                            } else {
                                X8ToastUtil.showToast(X8GimbalXYZAdjustController.this.mContext, "设置云台参数失败", 0);
                            }
                        }
                    });
                }
            }
        });
    }

    public void setX8GimbalManager(X8GimbalManager mX8GimbalManager) {
        this.mX8GimbalManager = mX8GimbalManager;
    }

    @Override
    public void initViews(View rootView) {
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    public float[] getTxtValue() {
        float[] xyzValue = {NumberUtil.convertToFloat(this.x8ViewGimbalR.getTvGimbalXyzValue().getText().toString(), 0), NumberUtil.convertToFloat(this.x8ViewGimbalP.getTvGimbalXyzValue().getText().toString(), 0), NumberUtil.convertToFloat(this.x8ViewGimbalY.getTvGimbalXyzValue().getText().toString(), 0)};
        return xyzValue;
    }

    public boolean gimbalXyzAdjustMaxHint(float currentValue) {
        return currentValue > this.maxDefaultAdjustValue || currentValue < this.minDefaultAdjustValue;
    }

    @Override
    public void onClick(View view) {
        int vid = view.getId();
        if (vid == R.id.x8_btn_xyz_adjust_exit) {
            this.x8DoubleCustomDialog = new X8DoubleCustomDialog(this.handleView.getContext(), getString(R.string.x8_gimbal_xyz_adjust_signout), getString(R.string.x8_gimbal_xyz_adjust_exit_hint), new X8DoubleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                    X8GimbalXYZAdjustController.this.x8DoubleCustomDialog.dismiss();
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8GimbalXYZAdjustController.this.mX8GimbalManager.setGcParamsNew(1, 0.0f, 0.0f, 0.0f, new UiCallBackListener() {
                        @Override
                        public void onComplete(CmdResult cmdResult, Object o) {
                        }
                    });
                    X8GimbalXYZAdjustController.this.x8DoubleCustomDialog.dismiss();
                    X8GimbalXYZAdjustController.this.listener.gimbalXYZAdjustExit();
                }
            });
            this.x8DoubleCustomDialog.show();
        } else if (vid == R.id.x8_btn_xyz_adjust_save) {
            if (SPStoreManager.getInstance().getBoolean(HostConstants.SP_KEY_NOT_TIPS, false)) {
                if (this.listener != null) {
                    this.mX8GimbalManager.setGcParamsNew(3, 0.0f, 0.0f, 0.0f, new UiCallBackListener() {
                        @Override
                        public void onComplete(CmdResult cmdResult, Object o) {
                            X8GimbalXYZAdjustController.this.ix8MainTopBarListener.onGcClick();
                        }
                    });
                    return;
                }
                return;
            }
            this.x8SingleCustomDialog = new X8SingleCustomDialog(this.handleView.getContext(), getString(R.string.x8_save), getString(R.string.x8_gimbal_xyz_adjust_save_hint), getString(R.string.x8_gimbal_xyz_adjust_promptly), true, new X8SingleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8SingleCustomDialog.onDialogButtonClickListener
                public void onSingleButtonClick() {
                    X8GimbalXYZAdjustController.this.x8SingleCustomDialog.dismiss();
                    if (X8GimbalXYZAdjustController.this.listener != null) {
                        X8GimbalXYZAdjustController.this.mX8GimbalManager.setGcParamsNew(3, 0.0f, 0.0f, 0.0f, new UiCallBackListener() {
                            @Override
                            public void onComplete(CmdResult cmdResult, Object o) {
                                X8GimbalXYZAdjustController.this.ix8MainTopBarListener.onGcClick();
                            }
                        });
                    }
                }
            });
            this.x8SingleCustomDialog.show();
        }
    }
}
