package com.fimi.app.x8s.controls.aifly;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiFixedwingListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AutoFixedwingState;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8AiFixedwingExcuteController extends AbsX8AiController implements View.OnClickListener, X8DoubleCustomDialog.onDialogButtonClickListener {
    protected int MAX_WIDTH;
    protected boolean isShow;
    protected int width;
    int speedState;
    private final Activity activity;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private ImageView imgBack;
    private IX8AiFixedwingListener listener;
    private FcCtrlManager mFcCtrlManager;
    private X8AiTipWithCloseView mTipBgView;
    private TextView tvFlag;

    public X8AiFixedwingExcuteController(Activity activity, View rootView) {
        super(rootView);
        this.width = X8Application.ANIMATION_WIDTH;
        this.speedState = 0;
        this.activity = activity;
    }

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        setTypeDisEnable();
    }

    public void setListener(IX8AiFixedwingListener listener) {
        this.listener = listener;
    }

    @Override
    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            showExitDialog();
        } else if (id == R.id.rl_flag_small) {
            if (this.tvFlag.getVisibility() == 0) {
                this.tvFlag.setVisibility(8);
            } else {
                this.tvFlag.setVisibility(0);
            }
        }
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8AiController, com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void openUi() {
        this.isShow = true;
        LayoutInflater inflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_ai_fixedwing_excute_layout, (ViewGroup) this.rootView, true);
        this.mTipBgView = this.handleView.findViewById(R.id.v_content_tip);
        this.imgBack = this.handleView.findViewById(R.id.img_ai_follow_back);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.tvFlag = this.handleView.findViewById(R.id.tv_task_tip);
        this.listener.onFixedwingRunning();
        this.imgBack.setOnClickListener(this);
        this.flagSmall.setOnClickListener(this);
        super.openUi();
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8AiController, com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void closeUi() {
        this.isShow = false;
        super.closeUi();
    }

    public void setFcManager(FcCtrlManager fcManager) {
        this.mFcCtrlManager = fcManager;
    }

    @Override
    public boolean isShow() {
        if (StateManager.getInstance().getX8Drone().getCtrlMode() == 13) {
            return false;
        }
        return this.isShow;
    }

    public void showExitDialog() {
        String t = this.rootView.getContext().getString(R.string.x8_ai_fixedwing_exite_title);
        String m = this.rootView.getContext().getString(R.string.x8_ai_fixedwing_exite_tip);
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        this.dialog.show();
    }

    public void taskExit() {
        onTaskComplete();
    }

    @Override
    public void cancleByModeChange() {
        onTaskComplete();
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isShow) {
            if (!b) {
                ononDroneDisconnectedTaskComplete();
            }
            AutoFixedwingState state = StateManager.getInstance().getX8Drone().getAutoFixedwingState();
            if (state != null) {
                if (state.getFixedwingphase() == 0) {
                    this.speedState = 1;
                    if (!this.mTipBgView.isClose()) {
                        String h1 = X8NumberUtil.getDistanceNumberString(5.0f, 0, false);
                        String vs1 = X8NumberUtil.getSpeedNumberString(3.0f, 0, false);
                        String prex = this.handleView.getContext().getString(R.string.x8_ai_fixedwing_speed_tip1);
                        String tip = String.format(prex, vs1, h1);
                        this.mTipBgView.setTipText(tip);
                        this.mTipBgView.showTip();
                    }
                } else if (this.speedState == 1) {
                    this.speedState = 2;
                    String tip2 = this.handleView.getContext().getString(R.string.x8_ai_fixedwing_speed_tip3);
                    this.mTipBgView.setTipText(tip2);
                    this.mTipBgView.showTip();
                } else if (!this.mTipBgView.isClose()) {
                    String tip3 = this.handleView.getContext().getString(R.string.x8_ai_fixedwing_speed_tip3);
                    this.mTipBgView.setTipText(tip3);
                    this.mTipBgView.showTip();
                }
            }
        }
    }

    private void onTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onFixedwingComplete(true);
        }
    }

    public void ononDroneDisconnectedTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onFixedwingComplete(false);
        }
    }

    private void closeFixedwing() {
        closeUi();
        if (this.listener != null) {
            this.listener.onFixedwingBackClick();
        }
    }

    public void setTypeDisEnable() {
        this.mFcCtrlManager.setDisenableFixwing(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiFixedwingExcuteController.this.taskExit();
                }
            }
        });
    }
}
