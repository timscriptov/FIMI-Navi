package com.fimi.app.x8s.ui.activity.update;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.UpdateContentAdapter;
import com.fimi.kernel.Constants;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.widget.impl.NoDoubleClickListener;
import com.fimi.x8sdk.controller.X8UpdateCheckManager;
import com.fimi.x8sdk.ivew.IUpdateCheckAction;
import com.fimi.x8sdk.update.UpdateUtil;

import java.util.List;


public class X8UpdateDetailActivity extends BaseActivity implements IUpdateCheckAction {
    Button btnStartUpdate;
    UpdateContentAdapter contentAdapter;
    ImageView imgLogo;
    ImageView imgUpdateFirmware;
    ListView listviewUpdateContent;
    TextView tvReason;
    List<UpfirewareDto> upfirewareDtos;
    ImageView x8IvUpdateDetailReturn;
    private boolean isCanUpdate;

    @Override
    protected void setStatusBarColor() {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    public void initData() {
        this.imgLogo = findViewById(R.id.img_update_firmware);
        this.imgLogo.setImageResource(R.drawable.x8_update_wait);
        this.tvReason = findViewById(R.id.tv_reason);
        this.imgUpdateFirmware = findViewById(R.id.img_update_firmware);
        this.x8IvUpdateDetailReturn = findViewById(R.id.x8_iv_update_detail_return);
        this.upfirewareDtos = UpdateUtil.getUpfireDtos();
        if (this.upfirewareDtos.size() > 0) {
            this.tvReason.setText(R.string.x8_update_ready);
        } else {
            this.tvReason.setText(R.string.x8_update_err_connect);
        }
        this.listviewUpdateContent = findViewById(R.id.listview_update_content);
        this.contentAdapter = new UpdateContentAdapter(this.upfirewareDtos, this);
        this.listviewUpdateContent.setAdapter(this.contentAdapter);
        this.btnStartUpdate = findViewById(R.id.btn_start_update);
        this.btnStartUpdate.setOnClickListener(new NoDoubleClickListener(800) {
            @Override
            protected void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.UPDATING_KEY, false);
                X8UpdateDetailActivity.this.readyGoThenKill(X8UpdatingActivity.class, bundle);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        X8UpdateCheckManager.getInstance().setOnIUpdateCheckAction(this, this);
        X8UpdateCheckManager.getInstance().queryCurSystemStatus();
    }

    @Override
    public void doTrans() {
        this.x8IvUpdateDetailReturn.setOnClickListener(view -> finish());
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.x8_activity_update_detail;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showIsUpdate(boolean isUpdate, int reason) {
        if (!isUpdate) {
            this.isCanUpdate = false;
            this.btnStartUpdate.setEnabled(false);
            if (reason == R.string.x8_update_err_insky) {
                this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_6);
            } else if (reason == R.string.x8_update_err_a12ununited) {
                this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_7);
            } else if (reason == R.string.x8_error_code_update_3 || reason == R.string.x8_error_code_update_3) {
                this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_1);
            } else if (reason == R.string.x8_error_code_update_5) {
                this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_2);
            } else if (reason == R.string.x8_update_err_lowpower || reason == R.string.x8_error_code_update_22) {
                this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_5);
            } else if (reason == R.string.x8_update_err_connect) {
                this.imgUpdateFirmware.setImageResource(R.drawable.x8s_update_error_7);
            } else if (reason == R.string.x8_update_err_updating) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.UPDATING_KEY, true);
                readyGoThenKill(X8UpdatingActivity.class, bundle);
            }
            this.tvReason.setText(reason);
            return;
        }
        this.isCanUpdate = true;
        this.tvReason.setText(R.string.x8_update_ready);
        this.btnStartUpdate.setEnabled(true);
        this.imgUpdateFirmware.setImageResource(R.drawable.fimisdk_update_wait);
    }

    @Override
    public void checkUpdate() {
    }
}
