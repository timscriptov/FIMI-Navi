package com.fimi.app.x8s.controls;

import android.app.Activity;
import android.content.Intent;

import com.fimi.android.app.R;
import com.fimi.app.x8s.ui.activity.update.X8UpdateDetailActivity;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.host.HostConstants;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.x8sdk.controller.X8UpdateCheckManager;
import com.fimi.x8sdk.ivew.IUpdateCheckAction;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.update.UpdateUtil;

import java.util.List;


public class X8UpdateHintController {
    private final Activity context;
    private X8DoubleCustomDialog dialogManagerUpdate;
    private final IUpdateCheckAction iUpdateCheckAction = new IUpdateCheckAction() {
        @Override
        public void showIsUpdate(boolean isUpdate, int reason) {
        }

        @Override
        public void checkUpdate() {
            X8UpdateHintController.this.showUpdateDialog();
        }
    };

    public X8UpdateHintController(Activity context) {
        this.context = context;
    }

    public void showUpdateDialog() {
        if (StateManager.getInstance().getX8Drone().isOnGround()) {
            List<UpfirewareDto> upfirewareDtos = UpdateUtil.getUpfireDtos();
            if (upfirewareDtos.size() > 0) {
                SPStoreManager.getInstance().saveInt(HostConstants.SP_KEY_UPDATE_CHECK, 1);
                if (UpdateUtil.isForceUpdate()) {
                    if (this.dialogManagerUpdate == null || !this.dialogManagerUpdate.isShowing()) {
                        this.dialogManagerUpdate = new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_update_fw_title), this.context.getString(R.string.x8_update_tip), this.context.getString(R.string.fimi_sdk_update_now), this.context.getString(R.string.fimi_sdk_update_return), new X8DoubleCustomDialog.onDialogButtonClickListener() {
                            @Override
                            public void onLeft() {
                                dialogManagerUpdate.dismiss();
                                Intent intent = new Intent(context, X8UpdateDetailActivity.class);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onRight() {
                                context.finish();
                            }
                        });
                        this.dialogManagerUpdate.setCanceledOnTouchOutside(false);
                        this.dialogManagerUpdate.setOnKeyListener((dialog, keyCode, event) -> {
                            if (keyCode != 4 || event.getRepeatCount() == 0) {
                            }
                            return true;
                        });
                        if (!this.context.isFinishing()) {
                            this.dialogManagerUpdate.show();
                            return;
                        }
                        return;
                    }
                    return;
                }
                this.dialogManagerUpdate = new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_update_fw_title), this.context.getString(R.string.x8_update_tip), this.context.getString(R.string.fimi_sdk_update_ignore), this.context.getString(R.string.fimi_sdk_update_now), new X8DoubleCustomDialog.onDialogButtonClickListener() {
                    @Override
                    public void onLeft() {
                        dialogManagerUpdate.dismiss();
                    }

                    @Override
                    public void onRight() {
                        dialogManagerUpdate.dismiss();
                        Intent intent = new Intent(context, X8UpdateDetailActivity.class);
                        context.startActivity(intent);
                    }
                });
                this.dialogManagerUpdate.setCanceledOnTouchOutside(false);
                if (!this.context.isFinishing()) {
                    this.dialogManagerUpdate.show();
                }
            }
        }
    }

    public void queryCurSystemStatus() {
        X8UpdateCheckManager.getInstance().setOnIUpdateCheckAction(this.context, this.iUpdateCheckAction);
        X8UpdateCheckManager.getInstance().queryCurSystemStatus();
    }

    public void setPresenterLockMotor() {
        if (StateManager.getInstance().getX8Drone().getAutoFcHeart().getTakeOffCap() == -14) {
            X8UpdateCheckManager.getInstance().setPresenterLockMotor(0);
        }
    }
}
