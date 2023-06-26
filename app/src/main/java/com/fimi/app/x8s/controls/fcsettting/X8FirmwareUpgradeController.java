package com.fimi.app.x8s.controls.fcsettting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.FirmwareUpgradeAdapter;
import com.fimi.app.x8s.entity.VersionEntity;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8FirmwareUpgradeControllerListener;
import com.fimi.kernel.Constants;
import com.fimi.kernel.base.EventMessage;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.widget.impl.NoDoubleClickListener;
import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.modulestate.VersionState;
import com.fimi.x8sdk.update.UpdateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class X8FirmwareUpgradeController extends AbsX8MenuBoxControllers implements View.OnClickListener {
    private final FirmwareUpgradeAdapter adapter;
    private final ImageView imgReturn;
    private final ArrayList<VersionEntity> items;
    private final RecyclerView mRecyclerList;
    private final TextView tvFirmwareUpgrade;
    boolean currentConnectedState;
    private boolean availableUpgrades;
    private IX8FirmwareUpgradeControllerListener listener;

    public X8FirmwareUpgradeController(View rootView) {
        super(rootView);
        this.items = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.contentView = inflater.inflate(R.layout.x8_main_general_item_firmware_upgrade, (ViewGroup) rootView, true);
        this.imgReturn = this.contentView.findViewById(R.id.btn_return);
        this.tvFirmwareUpgrade = this.contentView.findViewById(R.id.tv_firmware_upgrade);
        this.mRecyclerList = this.contentView.findViewById(R.id.recycler_version_list);
        initData();
        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 2);
        this.mRecyclerList.setLayoutManager(layoutManager);
        this.adapter = new FirmwareUpgradeAdapter(this.items);
        this.mRecyclerList.setAdapter(this.adapter);
        this.imgReturn.setOnClickListener(this);
        this.tvFirmwareUpgrade.setOnClickListener(new NoDoubleClickListener(800) {
            @Override
            protected void onNoDoubleClick(View v) {
                if (X8FirmwareUpgradeController.this.listener != null) {
                    X8FirmwareUpgradeController.this.listener.onFirmwareUpgradeClick();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_return) {
            closeItem();
            if (this.listener != null) {
                this.listener.onFirmwareUpgradeReturn();
            }
        }
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

    @Override
    public void showItem() {
        super.showItem();
        this.contentView.setVisibility(View.VISIBLE);
        EventBus.getDefault().register(this);
    }

    @Override
    public void closeItem() {
        super.closeItem();
        this.contentView.setVisibility(View.GONE);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusUI(EventMessage eventMessage) {
        if (eventMessage.getKey() == Constants.X8_UPDATE_EVENT_KEY) {
            onVersionChange();
            this.adapter.notifyDataSetChanged();
        }
    }

    public void setOnFirmwareClickListener(IX8FirmwareUpgradeControllerListener listener) {
        this.listener = listener;
    }

    private void initData() {
        this.items.clear();
        VersionState versionState = StateManager.getInstance().getVersionState();
        AckVersion moduleFcAckVersion = versionState.getModuleFcAckVersion();
        AckVersion moduleRcVersion = versionState.getModuleRcVersion();
        AckVersion moduleCvVersion = versionState.getModuleCvVersion();
        AckVersion moduleRepeaterRcVersion = versionState.getModuleRepeaterRcVersion();
        AckVersion moduleRepeaterVehicleVersion = versionState.getModuleRepeaterVehicleVersion();
        AckVersion moduleEscVersion = versionState.getModuleEscVersion();
        AckVersion moduleGimbalVersion = versionState.getModuleGimbalVersion();
        AckVersion moduleBatteryVersion = versionState.getModuleBatteryVersion();
        AckVersion moduleNfzVersion = versionState.getModuleNfzVersion();
        AckVersion moduleCameraVersion = versionState.getModuleCameraVersion();
        AckVersion moduleUltrasonic = versionState.getModuleUltrasonic();
        VersionEntity flyControllerVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_fc_name), versionState.getModuleFcAckVersion());
        VersionEntity cameraVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_camera_name), versionState.getModuleCameraVersion());
        VersionEntity cloudTerraceVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_gimbal_name), versionState.getModuleGimbalVersion());
        VersionEntity batteryVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_battery_name), versionState.getModuleBatteryVersion());
        VersionEntity remoteControlVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_rc_name), versionState.getModuleRcVersion());
        VersionEntity remoteControlRepeaterVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_rc_rl_name), versionState.getModuleRepeaterRcVersion());
        VersionEntity cv = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_vc_name), versionState.getModuleCvVersion());
        VersionEntity fcRl = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_fc_rl_name), versionState.getModuleRepeaterVehicleVersion());
        VersionEntity servoVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_esc_name), versionState.getModuleEscVersion());
        VersionEntity noFlyZoneVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_noflyzone_name), versionState.getModuleNfzVersion());
        VersionEntity ultrasonicVersion = new VersionEntity(this.contentView.getContext(), this.contentView.getContext().getString(R.string.x8_fw_ultrasonic_name), versionState.getModuleUltrasonic());
        List<UpfirewareDto> upfirewareDtos = UpdateUtil.getUpfireDtos();
        this.availableUpgrades = false;
        Iterator<UpfirewareDto> it = upfirewareDtos.iterator();
        while (it.hasNext()) {
            UpfirewareDto upfirewareDto = it.next();
            if (versionState != null) {
                if (moduleFcAckVersion != null && upfirewareDto.getModel() == moduleFcAckVersion.getModel() && upfirewareDto.getType() == moduleFcAckVersion.getType()) {
                    flyControllerVersion.setHasNewVersion(true);
                    this.availableUpgrades = true;
                } else if (moduleGimbalVersion != null && upfirewareDto.getModel() == moduleGimbalVersion.getModel() && upfirewareDto.getType() == moduleGimbalVersion.getType()) {
                    cloudTerraceVersion.setHasNewVersion(true);
                    this.availableUpgrades = true;
                } else if (moduleRcVersion != null && upfirewareDto.getModel() == moduleRcVersion.getModel() && upfirewareDto.getType() == moduleRcVersion.getType()) {
                    remoteControlVersion.setHasNewVersion(true);
                    this.availableUpgrades = true;
                } else if (moduleCvVersion != null && upfirewareDto.getModel() == moduleCvVersion.getModel() && upfirewareDto.getType() == moduleCvVersion.getType()) {
                    cv.setHasNewVersion(true);
                    this.availableUpgrades = true;
                } else if (moduleEscVersion != null && upfirewareDto.getModel() == moduleEscVersion.getModel() && upfirewareDto.getType() == moduleEscVersion.getType()) {
                    servoVersion.setHasNewVersion(true);
                    this.availableUpgrades = true;
                } else if (moduleUltrasonic != null && upfirewareDto.getModel() == versionState.getModuleUltrasonic().getModel() && upfirewareDto.getType() == versionState.getModuleUltrasonic().getType()) {
                    ultrasonicVersion.setHasNewVersion(true);
                    this.availableUpgrades = true;
                } else {
                    this.availableUpgrades = false;
                }
            } else {
                this.availableUpgrades = false;
            }
        }
        this.items.add(flyControllerVersion);
        this.items.add(cameraVersion);
        this.items.add(cloudTerraceVersion);
        this.items.add(batteryVersion);
        this.items.add(remoteControlVersion);
        this.items.add(remoteControlRepeaterVersion);
        this.items.add(cv);
        this.items.add(fcRl);
        this.items.add(servoVersion);
        this.items.add(noFlyZoneVersion);
        this.items.add(ultrasonicVersion);
        showNewUpdate(this.availableUpgrades);
    }


    private void showNewUpdate(boolean isShow) {
        if (isShow) {
            this.tvFirmwareUpgrade.setAlpha(1.0f);
            this.tvFirmwareUpgrade.setEnabled(true);
            return;
        }
        this.tvFirmwareUpgrade.setAlpha(0.6f);
        this.tvFirmwareUpgrade.setEnabled(false);
    }

    @Override
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (this.currentConnectedState != b) {
            this.currentConnectedState = b;
            if (b && StateManager.getInstance().getCamera().getToken() > 0) {
                initData();
                this.adapter.notifyDataSetChanged();
                return;
            }
            showNewUpdate(b);
            if (StateManager.getInstance().getConectState().isConnectRelay()) {
                this.items.clear();
                initData();
                this.adapter.changeDatas(this.items);
            }
        }
    }

    public void onVersionChange() {
        if (this.contentView != null && this.adapter != null) {
            this.items.clear();
            initData();
            this.adapter.changeDatas(this.items);
        }
    }
}
