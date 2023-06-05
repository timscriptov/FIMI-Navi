package com.fimi.app.x8s.ui.activity.update;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
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

/* loaded from: classes.dex */
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

    @Override // com.fimi.kernel.base.BaseActivity
    protected void setStatusBarColor() {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void initData() {
        this.imgLogo = (ImageView) findViewById(R.id.img_update_firmware);
        this.imgLogo.setImageResource(R.drawable.x8_update_wait);
        this.tvReason = (TextView) findViewById(R.id.tv_reason);
        this.imgUpdateFirmware = (ImageView) findViewById(R.id.img_update_firmware);
        this.x8IvUpdateDetailReturn = (ImageView) findViewById(R.id.x8_iv_update_detail_return);
        this.upfirewareDtos = UpdateUtil.getUpfireDtos();
        if (this.upfirewareDtos.size() > 0) {
            this.tvReason.setText(R.string.x8_update_ready);
        } else {
            this.tvReason.setText(R.string.x8_update_err_connect);
        }
        this.listviewUpdateContent = (ListView) findViewById(R.id.listview_update_content);
        this.contentAdapter = new UpdateContentAdapter(this.upfirewareDtos, this);
        this.listviewUpdateContent.setAdapter((ListAdapter) this.contentAdapter);
        this.btnStartUpdate = (Button) findViewById(R.id.btn_start_update);
        this.btnStartUpdate.setOnClickListener(new NoDoubleClickListener(800) { // from class: com.fimi.app.x8s.ui.activity.update.X8UpdateDetailActivity.1
            @Override // com.fimi.widget.impl.NoDoubleClickListener
            protected void onNoDoubleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.UPDATING_KEY, false);
                X8UpdateDetailActivity.this.readyGoThenKill(X8UpdatingActivity.class, bundle);
            }
        });
    }

    @Override
    // com.fimi.kernel.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        X8UpdateCheckManager.getInstance().setOnIUpdateCheckAction(this, this);
        X8UpdateCheckManager.getInstance().queryCurSystemStatus();
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void doTrans() {
        this.x8IvUpdateDetailReturn.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.activity.update.X8UpdateDetailActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                X8UpdateDetailActivity.this.finish();
            }
        });
    }

    @Override // com.fimi.kernel.base.BaseActivity
    protected int getContentViewLayoutID() {
        return R.layout.x8_activity_update_detail;
    }

    @Override
    // com.fimi.kernel.base.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.fimi.x8sdk.ivew.IUpdateCheckAction
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

    @Override // com.fimi.x8sdk.ivew.IUpdateCheckAction
    public void checkUpdate() {
    }
}
