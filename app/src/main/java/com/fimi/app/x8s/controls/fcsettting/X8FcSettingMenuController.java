package com.fimi.app.x8s.controls.fcsettting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.enums.X8FcAllSettingMenuEnum;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8FcItemListener;
import com.fimi.app.x8s.interfaces.IX8GeneralItemControllerListerner;
import com.fimi.app.x8s.interfaces.IX8GimbalSettingListener;
import com.fimi.app.x8s.interfaces.IX8MainCoverListener;
import com.fimi.app.x8s.interfaces.IX8RcItemControllerListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.dataparser.AutoFcBattery;


public class X8FcSettingMenuController extends AbsX8MenuBoxControllers implements View.OnClickListener {
    private final AbsX8Controllers[] itemControllers;
    X8MapVideoController mapVideoController;
    private ImageView imgBattery;
    private ImageView imgFc;
    private ImageView imgGenneal;
    private ImageView imgGimbal;
    private ImageView imgRc;
    private X8BatteryItemController mX8BatteryItemController;
    private X8FcItemController mX8FcItemController;
    private X8GeneralItemController mX8GeneralItemController;
    private X8GimbalItemController mX8GimbalItemController;
    private X8RcItemController mX8RcItemController;
    private X8FcAllSettingMenuEnum menu;
    private View parentView;
    private RelativeLayout rlBattery;
    private RelativeLayout rlFc;
    private RelativeLayout rlGenneal;
    private RelativeLayout rlGimbal;
    private RelativeLayout rlRc;

    public X8FcSettingMenuController(View rootView) {
        super(rootView);
        this.itemControllers = new AbsX8Controllers[5];
        this.menu = X8FcAllSettingMenuEnum.FC_ITEM;
    }

    public X8FcAllSettingMenuEnum getMenu() {
        return this.menu;
    }

    @Override
    public void initViews(View rootView) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.parentView = inflater.inflate(R.layout.x8_main_fc_setting_items, (ViewGroup) rootView, true);
        this.imgFc = this.parentView.findViewById(R.id.img_fc);
        this.imgRc = this.parentView.findViewById(R.id.img_rc);
        this.imgGimbal = this.parentView.findViewById(R.id.img_gimbal);
        this.imgBattery = this.parentView.findViewById(R.id.img_battery);
        this.imgGenneal = this.parentView.findViewById(R.id.img_general);
        this.rlFc = this.parentView.findViewById(R.id.rl_fc);
        this.rlRc = this.parentView.findViewById(R.id.rl_rc);
        this.rlGimbal = this.parentView.findViewById(R.id.rl_gimbal);
        this.rlBattery = this.parentView.findViewById(R.id.rl_battery);
        this.rlGenneal = this.parentView.findViewById(R.id.rl_general);
        initItemsControler(this.parentView);
    }

    @Override
    public void initActions() {
        this.rlFc.setOnClickListener(this);
        this.rlRc.setOnClickListener(this);
        this.rlGimbal.setOnClickListener(this);
        this.rlBattery.setOnClickListener(this);
        this.rlGenneal.setOnClickListener(this);
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void closeItem() {
        super.closeItem();
        if (this.mX8FcItemController != null) {
            this.mX8FcItemController.closeItem();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_fc) {
            this.menu = X8FcAllSettingMenuEnum.FC_ITEM;
            switchMenu();
        } else if (id == R.id.rl_rc) {
            this.menu = X8FcAllSettingMenuEnum.RC_ITEM;
            switchMenu();
        } else if (id == R.id.rl_gimbal) {
            this.menu = X8FcAllSettingMenuEnum.GIMBAL_ITEM;
            switchMenu();
        } else if (id == R.id.rl_battery) {
            this.menu = X8FcAllSettingMenuEnum.BATTERY_ITEM;
            switchMenu();
        } else if (id == R.id.rl_general) {
            this.menu = X8FcAllSettingMenuEnum.GENERAL_ITEM;
            switchMenu();
        }
    }

    public void setListener(X8MapVideoController mapVideoController, FcManager fcManager, FcCtrlManager fcCtrlManager, X8GimbalManager gimbalManager, IX8FcItemListener mX8FcItemListener, IX8RcItemControllerListener mX8RcItemControllerListener, IX8GeneralItemControllerListerner mIX8GeneralItemControllerListerner, IX8GimbalSettingListener gimbalSettingListener, IX8MainCoverListener coverListener) {
        this.mX8FcItemController.setFcCtrlManager(fcCtrlManager);
        this.mX8RcItemController.setFcCtrlManager(fcCtrlManager);
        this.mX8GimbalItemController.setFcCtrlManager(fcCtrlManager);
        this.mX8GimbalItemController.setGimbalManager(gimbalManager);
        this.mX8BatteryItemController.setFcCtrlManager(fcCtrlManager);
        this.mX8GeneralItemController.setFcCtrlManager(fcCtrlManager);
        this.mX8GeneralItemController.setGimbalManager(gimbalManager);
        this.mX8FcItemController.setFcManager(fcManager);
        this.mX8FcItemController.setMapVideoController(mapVideoController);
        this.mX8FcItemController.setListener(mX8FcItemListener);
        this.mX8FcItemController.setCoverListener(coverListener);
        this.mX8RcItemController.setListener(mX8RcItemControllerListener);
        this.mX8GeneralItemController.setListerner(mIX8GeneralItemControllerListerner);
        this.mX8GimbalItemController.setListener(gimbalSettingListener);
        this.itemControllers[0] = this.mX8FcItemController;
        this.itemControllers[1] = this.mX8RcItemController;
        this.itemControllers[2] = this.mX8GimbalItemController;
        this.itemControllers[3] = this.mX8BatteryItemController;
        this.itemControllers[4] = this.mX8GeneralItemController;
    }

    public void switchMenu(X8FcAllSettingMenuEnum menu) {
        this.menu = menu;
        switchMenu();
    }

    public void initItemsControler(View view) {
        this.mX8FcItemController = new X8FcItemController(view);
        this.mX8RcItemController = new X8RcItemController(view);
        this.mX8GimbalItemController = new X8GimbalItemController(view);
        this.mX8BatteryItemController = new X8BatteryItemController(view);
        this.mX8GeneralItemController = new X8GeneralItemController(view);
    }

    public void setItemsSelected(boolean... all) {
        this.imgFc.setSelected(all[0]);
        this.imgRc.setSelected(all[1]);
        this.imgGimbal.setSelected(all[2]);
        this.imgBattery.setSelected(all[3]);
        this.imgGenneal.setSelected(all[4]);
    }

    public void switchItemShow(boolean... all) {
        for (int i = 0; i < this.itemControllers.length; i++) {
            if (all[i]) {
                this.itemControllers[i].showItem();
            } else {
                this.itemControllers[i].closeItem();
            }
        }
    }

    public void onBatteryReceive(AutoFcBattery autoFcBattery) {
        if (this.mX8BatteryItemController != null) {
            this.mX8BatteryItemController.onBatteryReceive(autoFcBattery);
        }
    }

    private void switchMenu() {
        switch (this.menu) {
            case FC_ITEM:
                setItemsSelected(true, false, false, false, false);
                switchItemShow(true, false, false, false, false);
                return;
            case RC_ITEM:
                setItemsSelected(false, true, false, false, false);
                switchItemShow(false, true, false, false, false);
                return;
            case GIMBAL_ITEM:
                setItemsSelected(false, false, true, false, false);
                switchItemShow(false, false, true, false, false);
                return;
            case BATTERY_ITEM:
                setItemsSelected(false, false, false, true, false);
                switchItemShow(false, false, false, true, false);
                return;
            case GENERAL_ITEM:
                setItemsSelected(false, false, false, false, true);
                switchItemShow(false, false, false, false, true);
                return;
            default:
        }
    }

    public void setFiveKeyValue(int key, int position) {
        this.mX8RcItemController.setFiveKeyValue(key, position);
    }

    public void onRcConnected(boolean isConnect) {
        if (this.mX8RcItemController != null) {
            this.mX8RcItemController.onRcConnected(isConnect);
        }
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.mX8FcItemController != null) {
            this.mX8FcItemController.onDroneConnected(b);
        }
        if (this.mX8RcItemController != null) {
            this.mX8RcItemController.onDroneConnected(b);
        }
        if (this.mX8BatteryItemController != null) {
            this.mX8BatteryItemController.onDroneConnected(b);
        }
        if (this.mX8GimbalItemController != null) {
            this.mX8GimbalItemController.onDroneConnected(b);
        }
        if (this.mX8GeneralItemController != null) {
            this.mX8GeneralItemController.onDroneConnected(b);
        }
    }
}
