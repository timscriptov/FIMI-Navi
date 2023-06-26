package com.fimi.app.x8s.controls.gimbal;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.IX8MainTopBarListener;
import com.fimi.app.x8s.widget.GimbalAdjustRelayout;
import com.fimi.kernel.Constants;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.dataparser.AckCloudParams;


public class X8GimbalAdjustController {
    Button btnCalibrate;
    Button btnGet;
    Button btnSave;
    IX8MainTopBarListener listener;
    Context mContext;
    X8GimbalManager mX8GimbalManager = new X8GimbalManager();
    GimbalAdjustRelayout relayoutPitch;
    GimbalAdjustRelayout relayoutRoll;
    GimbalAdjustRelayout rlYaw;

    public X8GimbalAdjustController(@NonNull View rootView) {
        this.btnGet = rootView.findViewById(R.id.btn_get);
        this.btnSave = rootView.findViewById(R.id.btn_save);
        this.relayoutPitch = rootView.findViewById(R.id.rl_pitch);
        this.relayoutPitch.getTvGimbalModel().setText("Pitch");
        this.relayoutRoll = rootView.findViewById(R.id.rl_roll);
        this.relayoutRoll.getTvGimbalModel().setText("Roll");
        this.rlYaw = rootView.findViewById(R.id.rl_yaw);
        this.rlYaw.getTvGimbalModel().setText("Yaw");
        this.mContext = rootView.getContext();
        this.btnCalibrate = rootView.findViewById(R.id.btn_calibrate);
        if (!Constants.isFactoryApp()) {
            rootView.findViewById(R.id.rl_gc_calibrate).setVisibility(View.GONE);
        }
        initClickAction();
    }

    public void setListener(IX8MainTopBarListener listener) {
        this.listener = listener;
    }

    private void initClickAction() {
        this.btnCalibrate.setOnClickListener(view -> {
            if (listener != null) {
                listener.onGcClick();
            }
        });
        this.btnSave.setOnClickListener(view -> mX8GimbalManager.setGcParams(1, 0.0f, (cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                X8ToastUtil.showToast(mContext, "保存云台参数成功", 0);
            } else {
                X8ToastUtil.showToast(mContext, "保存云台参数失败", 0);
            }
        }));
        this.btnGet.setOnClickListener(view -> mX8GimbalManager.getGcParams((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                AckCloudParams params = (AckCloudParams) o;
                if (params != null) {
                    relayoutPitch.getEtxValue().setText(NumberUtil.decimalPointStr(params.getParam1(), 4));
                    relayoutRoll.getEtxValue().setText(NumberUtil.decimalPointStr(params.getParam2(), 4));
                    rlYaw.getEtxValue().setText(NumberUtil.decimalPointStr(params.getParam3(), 4));
                    return;
                }
                X8ToastUtil.showToast(mContext, "获取云台参数失败", 0);
            }
        }));
        this.relayoutPitch.getBtnAdd().setOnClickListener(view -> {
            final float value = (float) (Float.parseFloat(relayoutPitch.getEtxValue().getText().toString()) + 0.004d);
            mX8GimbalManager.setGcParams(2, value, (cmdResult, o) -> {
                if (cmdResult.isSuccess()) {
                    relayoutPitch.getEtxValue().setText(NumberUtil.decimalPointStr(value, 4));
                } else {
                    X8ToastUtil.showToast(mContext, "设置云台参数失败", 0);
                }
            });
        });
        this.relayoutPitch.getBtnSub().setOnClickListener(view -> {
            final float value = (float) (Float.parseFloat(relayoutPitch.getEtxValue().getText().toString()) - 0.004d);
            mX8GimbalManager.setGcParams(2, value, (cmdResult, o) -> {
                if (cmdResult.isSuccess()) {
                    relayoutPitch.getEtxValue().setText(NumberUtil.decimalPointStr(value, 4));
                } else {
                    X8ToastUtil.showToast(mContext, "设置云台参数失败", 0);
                }
            });
        });
        this.relayoutRoll.getBtnAdd().setOnClickListener(view -> {
            final float value = (float) (Float.parseFloat(relayoutRoll.getEtxValue().getText().toString()) + 0.004d);
            mX8GimbalManager.setGcParams(4, value, (cmdResult, o) -> {
                if (cmdResult.isSuccess()) {
                    relayoutRoll.getEtxValue().setText(NumberUtil.decimalPointStr(value, 4));
                } else {
                    X8ToastUtil.showToast(mContext, "设置云台参数失败", 0);
                }
            });
        });
        this.relayoutRoll.getBtnSub().setOnClickListener(view -> {
            final float value = (float) (Float.parseFloat(relayoutRoll.getEtxValue().getText().toString()) - 0.004d);
            mX8GimbalManager.setGcParams(4, value, (cmdResult, o) -> {
                if (cmdResult.isSuccess()) {
                    relayoutRoll.getEtxValue().setText(NumberUtil.decimalPointStr(value, 4));
                } else {
                    X8ToastUtil.showToast(mContext, "设置云台参数失败", 0);
                }
            });
        });
        this.rlYaw.getBtnAdd().setOnClickListener(view -> {
            final float value = (float) (Float.parseFloat(rlYaw.getEtxValue().getText().toString()) - 0.004d);
            mX8GimbalManager.setGcParams(8, value, (cmdResult, o) -> {
                if (cmdResult.isSuccess()) {
                    rlYaw.getEtxValue().setText(NumberUtil.decimalPointStr(value, 4));
                } else {
                    X8ToastUtil.showToast(mContext, "设置云台参数失败", 0);
                }
            });
        });
        this.rlYaw.getBtnSub().setOnClickListener(view -> {
            final float value = (float) (Float.parseFloat(rlYaw.getEtxValue().getText().toString()) + 0.004d);
            mX8GimbalManager.setGcParams(8, value, (cmdResult, o) -> {
                if (cmdResult.isSuccess()) {
                    rlYaw.getEtxValue().setText(NumberUtil.decimalPointStr(value, 4));
                } else {
                    X8ToastUtil.showToast(mContext, "设置云台参数失败", 0);
                }
            });
        });
    }

    public void setmX8GimbalManager(X8GimbalManager mX8GimbalManager) {
        this.mX8GimbalManager = mX8GimbalManager;
    }
}
