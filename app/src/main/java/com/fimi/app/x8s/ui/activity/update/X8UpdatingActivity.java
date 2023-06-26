package com.fimi.app.x8s.ui.activity.update;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.manager.X8FpvManager;
import com.fimi.host.HostConstants;
import com.fimi.host.LocalFwEntity;
import com.fimi.kernel.Constants;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.base.EventMessage;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.widget.DownRoundProgress;
import com.fimi.x8sdk.controller.UpdateManager;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.process.RelayProcess;
import com.fimi.x8sdk.update.UpdateUtil;
import com.fimi.x8sdk.update.fwpack.FwInfo;
import com.twitter.sdk.android.core.internal.scribe.EventsFilesManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class X8UpdatingActivity extends BaseActivity {
    private Button btnReconnect;
    private DownRoundProgress downRoundProgress;
    private ImageView imgUpdateResult;
    private boolean isUpdating;
    private TextView tvUpdateWarnming;
    private TextView tvUpdating;
    private UpdateManager updateManager;
    private TextView x8TvUpdateFailureHint;
    private TextView x8sTvUpdatingProgress;

    @Override
    protected void setStatusBarColor() {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            this.isUpdating = intent.getBooleanExtra(Constants.UPDATING_KEY, false);
        }
        initView();
        this.updateManager = new UpdateManager(this);
        if (!this.isUpdating) {
            this.updateManager.firmwareBuildPack(UpdateUtil.toFwInfo());
        } else {
            this.updateManager.queryCurUpdateStatus(null);
        }
    }

    void initView() {
        this.downRoundProgress = findViewById(R.id.rpb_update_progress);
        this.imgUpdateResult = findViewById(R.id.img_update_result);
        this.tvUpdating = findViewById(R.id.tv_updating);
        this.x8TvUpdateFailureHint = findViewById(R.id.x8_tv_update_failure_hint);
        this.tvUpdateWarnming = findViewById(R.id.tv_update_warnming);
        this.x8sTvUpdatingProgress = findViewById(R.id.x8s_tv_updating_progress);
        this.btnReconnect = findViewById(R.id.btn_reconnect);
        X8FpvManager.isUpdateing = true;
        FontUtil.changeFontLanTing(getAssets(), this.tvUpdateWarnming, this.tvUpdating);
    }

    @Override
    public void doTrans() {
        this.btnReconnect.setOnClickListener(view -> {
            X8FpvManager.isUpdateing = false;
            finish();
        });
        this.updateManager.setOnUpdateProgress((isSuccess, progress, dtos, name) -> {
            String tempString;
            if (!isSuccess) {
                btnReconnect.setVisibility(View.VISIBLE);
                btnReconnect.setText(R.string.x8_update_connect);
                imgUpdateResult.setVisibility(View.VISIBLE);
                tvUpdateWarnming.setVisibility(View.INVISIBLE);
                tvUpdating.setText(name);
                x8sTvUpdatingProgress.setVisibility(View.GONE);
            } else if (100 != progress || dtos == null) {
                downRoundProgress.setProgress(progress);
                if (name == null) {
                    tempString = getString(R.string.x8_update_hint_one);
                } else {
                    tempString = String.format(getString(R.string.x8_updating), name);
                }
                tvUpdating.setText(tempString);
                x8sTvUpdatingProgress.setText(progress + "%");
                if (btnReconnect.getVisibility() == View.VISIBLE) {
                    btnReconnect.setVisibility(View.GONE);
                    tvUpdateWarnming.setText(getString(R.string.x8_update_warning));
                }
            } else {
                btnReconnect.postDelayed(() -> {
                    HostConstants.clearLocalFwEntitys();
                    RelayProcess.getRelayProcess().getAllVersion();
                    btnReconnect.setVisibility(View.VISIBLE);
                    btnReconnect.setText(R.string.x8_update_success);
                    tvUpdateWarnming.setVisibility(View.INVISIBLE);
                    x8sTvUpdatingProgress.setVisibility(View.GONE);
                    updateResult(dtos);
                    imgUpdateResult.setVisibility(View.VISIBLE);
                }, 10000L);
            }
        });
    }

    public void updateResult(@NonNull List<FwInfo> dtos) {
        StringBuilder sucessSb = new StringBuilder();
        StringBuilder failedSb = new StringBuilder();
        boolean hasFailed = false;
        for (FwInfo dto : dtos) {
            if ("0".equalsIgnoreCase(dto.getUpdateResult())) {
                sucessSb.append(dto.getSysName()).append("、");
                HostConstants.saveLocalFirmware(new LocalFwEntity(dto.getTypeId(), dto.getModelId(), dto.getSoftwareVer(), ""));
            }
            if ("1".equalsIgnoreCase(dto.getUpdateResult())) {
                failedSb.append(dto.getSysName()).append(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR).append(dto.getErrorCode()).append("、");
            }
        }
        if (!TextUtils.isEmpty(sucessSb.toString())) {
            sucessSb.deleteCharAt(sucessSb.length() - 1);
            sucessSb.append(getString(R.string.x8_update_success1));
        }
        if (!TextUtils.isEmpty(failedSb.toString())) {
            failedSb.deleteCharAt(failedSb.length() - 1);
            failedSb.append(getString(R.string.x8_update_failed));
        }
        StringBuilder info = new StringBuilder();
        if (!TextUtils.isEmpty(failedSb.toString())) {
            info.append(failedSb).append("\n");
            hasFailed = true;
        }
        if (!TextUtils.isEmpty(sucessSb.toString())) {
            info.append(sucessSb);
        }
        if (hasFailed) {
            this.imgUpdateResult.setImageResource(R.drawable.x8s_update_error_4);
            this.x8TvUpdateFailureHint.setVisibility(View.VISIBLE);
        } else {
            this.imgUpdateResult.setImageResource(R.drawable.x8_img_updating_success);
            this.x8TvUpdateFailureHint.setVisibility(View.GONE);
        }
        if (hasFailed) {
            this.tvUpdating.setText(info.toString());
        } else {
            this.tvUpdating.setText(info.toString());
        }
        this.downRoundProgress.setProgress(100);
        EventBus.getDefault().post(new EventMessage<>(Constants.X8_UPDATE_EVENT_KEY, ""));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.updateManager.removeNoticeList();
        X8FpvManager.isUpdateing = false;
        StateManager.getInstance().getVersionState().setModuleFcAckVersion(null);
    }

    @Override
    public void onBackPressed() {
        X8FpvManager.isUpdateing = false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.x8_activity_updating;
    }
}
