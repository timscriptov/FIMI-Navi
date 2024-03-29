package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.widget.MidView;
import com.fimi.app.x8s.widget.RcRollerView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckRcCalibrationState;
import com.fimi.x8sdk.modulestate.StateManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class X8RCCalibrationController extends AbsX8MenuBoxControllers implements View.OnClickListener, UiCallBackListener {
    private final int centerValue;
    private final int exitCmd;
    private final int joyCmd;
    private final int midCmd;
    private final int rollerCmd;
    RcStatus curStatus;
    ArrayList<MidView.clipType> leftClips;
    ArrayList<MidView.clipType> rightClips;
    private ImageView backBtn;
    private Button cali_btn;
    private CheckTask checkTask;
    private Context context;
    private RelativeLayout control_layout;
    private X8DoubleCustomDialog dialog;
    private boolean downRoller;
    private boolean droneOkay;
    private TextView errorTip;
    private FcCtrlManager fcCtrlManager;
    private IX8CalibrationListener ix8CalibrationListener;
    private MidView lefMidView;
    private RcRollerView leftDownRoller;
    private ImageView leftMidBottom;
    private ImageView leftMidLeft;
    private int leftMidResult;
    private ImageView leftMidRight;
    private ImageView leftMidTop;
    private RcRollerView leftUpRoller;
    private boolean rcConnect;
    private RcStatus rcStatus;
    private RelativeLayout rc_layout;
    private ImageView rightMidBottom;
    private ImageView rightMidLeft;
    private int rightMidResult;
    private ImageView rightMidRight;
    private ImageView rightMidTop;
    private MidView rightMidView;
    private Button rtBtn;
    private ImageView rtImage;
    private TextView rtTip;
    private RelativeLayout rt_layout;
    private Timer timer;
    private TextView tipTV;
    private boolean upRoller;

    public X8RCCalibrationController(View rootView) {
        super(rootView);
        this.midCmd = 1;
        this.joyCmd = 2;
        this.rollerCmd = 3;
        this.exitCmd = 4;
        this.centerValue = 512;
        this.rcStatus = RcStatus.ideal;
        this.leftMidResult = 0;
        this.rightMidResult = 0;
        this.leftClips = new ArrayList<>();
        this.rightClips = new ArrayList<>();
        this.droneOkay = false;
        this.rcConnect = false;
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setIx8CalibrationListener(IX8CalibrationListener ix8CalibrationListener) {
        this.ix8CalibrationListener = ix8CalibrationListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_calibration) {
            if (this.fcCtrlManager != null) {
                this.fcCtrlManager.rcCalibration(1, this);
            }
        } else if (i == R.id.img_return) {
            breakOutDone();
        } else if (i == R.id.btn_rt) {
            if (this.rcStatus == RcStatus.finish) {
                if (this.ix8CalibrationListener != null) {
                    this.ix8CalibrationListener.onCalibrationReturn();
                    closeUi();
                }
            } else if (this.rcStatus == RcStatus.fail) {
                if (this.fcCtrlManager != null) {
                    this.fcCtrlManager.rcCalibration(4, this);
                }
                this.rcStatus = RcStatus.ideal;
                gotoModel(this.rcStatus);
            }
            if (this.rcStatus == RcStatus.conBroken) {
                this.rcStatus = RcStatus.ideal;
                gotoModel(this.rcStatus);
                this.cali_btn.setAlpha(0.6f);
                this.cali_btn.setEnabled(false);
            }
        }
    }

    private void breakOutDone() {
        this.dialog = new X8DoubleCustomDialog(this.context, this.context.getString(R.string.x8_rc_exit_calibration), this.context.getString(R.string.x8_rc_exit_tip), new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
                X8RCCalibrationController.this.dialog.dismiss();
            }

            @Override
            public void onRight() {
                if (X8RCCalibrationController.this.fcCtrlManager != null) {
                    X8RCCalibrationController.this.fcCtrlManager.rcCalibration(4, X8RCCalibrationController.this);
                }
                X8RCCalibrationController.this.dialog.dismiss();
                if (X8RCCalibrationController.this.ix8CalibrationListener != null) {
                    X8RCCalibrationController.this.ix8CalibrationListener.onCalibrationReturn();
                    X8RCCalibrationController.this.closeUi();
                }
            }
        });
        if (this.rcStatus != RcStatus.ideal && this.rcStatus != RcStatus.finish && this.rcStatus != RcStatus.conBroken) {
            this.dialog.show();
        } else if (this.ix8CalibrationListener != null) {
            this.ix8CalibrationListener.onCalibrationReturn();
            closeUi();
        }
    }

    @Override
    public void onComplete(CmdResult cmdResult, Object o) {
        if (cmdResult.isSuccess() && o != null) {
            AckRcCalibrationState caliState = (AckRcCalibrationState) o;
            if (caliState.getMsgId() == 15) {
                if (caliState.getStatus() == 7 && this.rcStatus != RcStatus.ideal) {
                    this.rcStatus = RcStatus.fail;
                    gotoModel(this.rcStatus);
                    return;
                }
                if (caliState.getProgress() == 1) {
                    if (caliState.getStatus() == 1) {
                        this.fcCtrlManager.rcCalibration(2, this);
                    } else {
                        this.rcStatus = RcStatus.midModel;
                    }
                } else if (caliState.getProgress() == 2) {
                    if (caliState.getStatus() == 2) {
                        this.fcCtrlManager.rcCalibration(3, this);
                    } else {
                        this.rcStatus = RcStatus.joyModel;
                        int rc1 = caliState.getRc1();
                        int rc0 = caliState.getRc0();
                        int rc2 = caliState.getRc2();
                        int rc3 = caliState.getRc3();
                        this.lefMidView.setFxFy(rc1, rc0);
                        this.rightMidView.setFxFy(rc3, rc2);
                    }
                } else if (caliState.getProgress() == 3) {
                    if (caliState.getStatus() == 3) {
                        if (this.rcStatus != RcStatus.finish && this.rcStatus != RcStatus.ideal) {
                            this.rcStatus = RcStatus.finish;
                        }
                    } else {
                        this.rcStatus = RcStatus.rollerModel;
                        int rc4 = caliState.getRc4();
                        if (rc4 > 512 && !this.upRoller) {
                            this.leftUpRoller.upRollerValue(rc4 - 512);
                        } else if (rc4 < 512 && !this.downRoller) {
                            this.leftDownRoller.upRollerValue(512 - rc4);
                        } else {
                            if (!this.upRoller) {
                                this.leftDownRoller.clean();
                            }
                            if (!this.downRoller) {
                                this.leftUpRoller.clean();
                            }
                        }
                    }
                }
                int cmdStatus = caliState.getCmdStatus();
                if (this.rcStatus == RcStatus.joyModel) {
                    if ((cmdStatus & 1) == 1 && !this.leftMidTop.isSelected()) {
                        this.leftMidResult++;
                        this.leftClips.add(MidView.clipType.top);
                        this.leftMidTop.setSelected(true);
                    }
                    if (((cmdStatus & 2) >> 1) == 1 && !this.leftMidLeft.isSelected()) {
                        this.leftMidResult++;
                        this.leftClips.add(MidView.clipType.left);
                        this.leftMidLeft.setSelected(true);
                    }
                    if (((cmdStatus & 128) >> 7) == 1 && !this.leftMidRight.isSelected()) {
                        this.leftMidResult++;
                        this.leftClips.add(MidView.clipType.right);
                        this.leftMidRight.setSelected(true);
                    }
                    if (((cmdStatus & 64) >> 6) == 1 && !this.leftMidBottom.isSelected()) {
                        this.leftMidResult++;
                        this.leftClips.add(MidView.clipType.bottom);
                        this.leftMidBottom.setSelected(true);
                    }
                    if (((cmdStatus & 4) >> 2) == 1 && !this.rightMidTop.isSelected()) {
                        this.rightMidResult++;
                        this.rightClips.add(MidView.clipType.top);
                        this.rightMidTop.setSelected(true);
                    }
                    if (((cmdStatus & 8) >> 3) == 1 && !this.rightMidLeft.isSelected()) {
                        this.rightMidResult++;
                        this.rightClips.add(MidView.clipType.left);
                        this.rightMidLeft.setSelected(true);
                    }
                    if (((cmdStatus & 512) >> 9) == 1 && !this.rightMidRight.isSelected()) {
                        this.rightMidResult++;
                        this.rightClips.add(MidView.clipType.right);
                        this.rightMidRight.setSelected(true);
                    }
                    if (((cmdStatus & 256) >> 8) == 1 && !this.rightMidBottom.isSelected()) {
                        this.rightMidResult++;
                        this.rightClips.add(MidView.clipType.bottom);
                        this.rightMidBottom.setSelected(true);
                    }
                    this.lefMidView.setType(this.leftClips);
                    this.rightMidView.setType(this.rightClips);
                    if (this.leftMidResult == 4) {
                        this.lefMidView.joyFinish();
                    }
                    if (this.rightMidResult == 4) {
                        this.rightMidView.joyFinish();
                    }
                } else if (this.rcStatus == RcStatus.rollerModel) {
                    if (((cmdStatus & 16) >> 4) == 1) {
                        this.upRoller = true;
                    }
                    if (((cmdStatus & 1024) >> 10) == 1) {
                        this.downRoller = true;
                    }
                }
                if (this.rcStatus == RcStatus.ideal) {
                    int rc12 = caliState.getRc1();
                    int rc02 = caliState.getRc0();
                    int rc22 = caliState.getRc2();
                    int rc32 = caliState.getRc3();
                    this.lefMidView.setFxFy(rc12, rc02);
                    this.rightMidView.setFxFy(rc32, rc22);
                    int rc42 = caliState.getRc4();
                    if (rc42 > 512) {
                        this.leftUpRoller.upRollerValue(rc42 - 512);
                    } else if (rc42 < 512) {
                        this.leftDownRoller.upRollerValue(512 - rc42);
                    } else {
                        this.leftDownRoller.clean();
                        this.leftUpRoller.clean();
                    }
                }
                if (!this.droneOkay) {
                    gotoModel(this.rcStatus);
                }
            }
        }
    }

    private void gotoModel(RcStatus rcStatus) {
        if (this.curStatus != rcStatus) {
            this.curStatus = rcStatus;
            this.rc_layout.setVisibility(View.VISIBLE);
            this.rt_layout.setVisibility(View.GONE);
            this.leftMidTop.setVisibility(View.GONE);
            this.leftMidBottom.setVisibility(View.GONE);
            this.leftMidLeft.setVisibility(View.GONE);
            this.leftMidRight.setVisibility(View.GONE);
            this.rightMidTop.setVisibility(View.GONE);
            this.rightMidRight.setVisibility(View.GONE);
            this.rightMidBottom.setVisibility(View.GONE);
            this.rightMidLeft.setVisibility(View.GONE);
            this.leftDownRoller.setVisibility(View.GONE);
            this.leftUpRoller.setVisibility(View.GONE);
            this.cali_btn.setVisibility(View.GONE);
            this.errorTip.setVisibility(View.GONE);
            if (this.curStatus == RcStatus.error) {
                this.control_layout.setBackground(new BitmapDrawable(ImageUtils.getBitmapByPath(this.control_layout.getContext(), R.drawable.x8_rc_unable_bg)));
            } else if (rcStatus == RcStatus.rollerModel) {
                this.control_layout.setBackground(new BitmapDrawable(ImageUtils.getBitmapByPath(this.control_layout.getContext(), R.drawable.x8_rc_roller_bg)));
            } else {
                this.control_layout.setBackground(new BitmapDrawable(ImageUtils.getBitmapByPath(this.control_layout.getContext(), R.drawable.x8_rc_roller_bg)));
            }
            if (rcStatus == RcStatus.ideal) {
                this.leftDownRoller.setVisibility(View.VISIBLE);
                this.leftUpRoller.setVisibility(View.VISIBLE);
                this.tipTV.setText(getString(R.string.x8_rc_lead_tip));
                this.cali_btn.setEnabled(true);
                this.cali_btn.setAlpha(1.0f);
                this.cali_btn.setVisibility(View.VISIBLE);
                this.leftClips.clear();
                this.rightClips.clear();
                this.lefMidView.setType(this.leftClips);
                this.rightMidView.setType(this.rightClips);
                this.rightMidView.releaseAll();
                this.lefMidView.releaseAll();
                this.leftUpRoller.clean();
                this.leftDownRoller.clean();
            } else if (rcStatus == RcStatus.joyModel) {
                this.tipTV.setText(getString(R.string.x8_rc_joy_tip));
                this.leftMidTop.setVisibility(View.VISIBLE);
                this.leftMidBottom.setVisibility(View.VISIBLE);
                this.leftMidLeft.setVisibility(View.VISIBLE);
                this.leftMidRight.setVisibility(View.VISIBLE);
                this.lefMidView.setAlpha(1.0f);
                this.rightMidView.setAlpha(1.0f);
                this.rightMidTop.setVisibility(View.VISIBLE);
                this.rightMidRight.setVisibility(View.VISIBLE);
                this.rightMidBottom.setVisibility(View.VISIBLE);
                this.rightMidLeft.setVisibility(View.VISIBLE);
            } else if (rcStatus == RcStatus.rollerModel) {
                this.tipTV.setText(getString(R.string.x8_rc_roller_tip));
                this.leftDownRoller.setVisibility(View.VISIBLE);
                this.leftUpRoller.setVisibility(View.VISIBLE);
                this.leftClips.clear();
                this.rightClips.clear();
                this.lefMidView.setAlpha(0.4f);
                this.rightMidView.setAlpha(0.4f);
                this.lefMidView.setType(this.leftClips);
                this.rightMidView.setType(this.rightClips);
                this.rightMidView.releaseAll();
                this.lefMidView.releaseAll();
            } else if (rcStatus == RcStatus.midModel) {
                this.tipTV.setText(getString(R.string.x8_rc_mid_tip));
                this.lefMidView.setAlpha(1.0f);
                this.rightMidView.setAlpha(1.0f);
            } else if (rcStatus == RcStatus.finish) {
                if (this.fcCtrlManager != null) {
                    this.fcCtrlManager.rcCalibration(4, this);
                }
                this.rc_layout.setVisibility(View.GONE);
                this.rt_layout.setVisibility(View.VISIBLE);
                this.rtImage.setImageResource(R.drawable.x8_calibration_success_icon);
                this.rtTip.setText(getString(R.string.x8_compass_result_success));
                this.rtBtn.setText(R.string.x8_compass_reuslt_success_confirm);
            } else if (rcStatus == RcStatus.fail) {
                this.rt_layout.setVisibility(View.VISIBLE);
                this.rc_layout.setVisibility(View.GONE);
                this.rtTip.setText(getString(R.string.x8_compass_result_failed));
                this.rtBtn.setText(R.string.x8_compass_reuslt_failed_confirm);
                this.rtImage.setImageResource(R.drawable.x8_calibration_fail_icon);
                this.errorTip.setText(getString(R.string.x8_compass_result_failed_tip));
                this.errorTip.setVisibility(View.VISIBLE);
            } else if (rcStatus == RcStatus.conBroken) {
                this.rt_layout.setVisibility(View.VISIBLE);
                this.rc_layout.setVisibility(View.GONE);
                this.rtImage.setImageResource(R.drawable.x8_calibration_fail_icon);
                this.rtTip.setText(getString(R.string.x8_compass_result_failed));
                this.rtBtn.setText(R.string.x8_compass_reuslt_failed_confirm);
                this.errorTip.setText(getString(R.string.x8_rc_calibration_tip));
                this.errorTip.setVisibility(View.VISIBLE);
                this.rtBtn.setEnabled(true);
                this.rtBtn.setAlpha(1.0f);
            } else if (rcStatus == RcStatus.error) {
                this.cali_btn.setVisibility(View.VISIBLE);
                this.cali_btn.setEnabled(false);
                this.cali_btn.setAlpha(0.6f);
            }
        }
    }

    @Override
    public void initViews(View rootView) {
        this.context = rootView.getContext();
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_rc_calibration_layout, (ViewGroup) rootView, true);
        this.leftUpRoller = this.handleView.findViewById(R.id.left_up);
        this.leftDownRoller = this.handleView.findViewById(R.id.left_down);
        this.lefMidView = this.handleView.findViewById(R.id.mid_left);
        this.rightMidView = this.handleView.findViewById(R.id.mid_right);
        this.lefMidView.setAlpha(0.4f);
        this.rightMidView.setAlpha(0.4f);
        this.cali_btn = this.handleView.findViewById(R.id.btn_calibration);
        this.cali_btn.setOnClickListener(this);
        this.leftMidTop = this.handleView.findViewById(R.id.left_top_icon);
        this.leftMidBottom = this.handleView.findViewById(R.id.left_bottom_icon);
        this.leftMidLeft = this.handleView.findViewById(R.id.left_left_icon);
        this.leftMidRight = this.handleView.findViewById(R.id.left_right_icon);
        this.rightMidTop = this.handleView.findViewById(R.id.right_top_icon);
        this.rightMidBottom = this.handleView.findViewById(R.id.right_bottom_icon);
        this.rightMidLeft = this.handleView.findViewById(R.id.right_left_icon);
        this.rightMidRight = this.handleView.findViewById(R.id.right_right_icon);
        this.backBtn = this.handleView.findViewById(R.id.img_return);
        this.backBtn.setOnClickListener(this);
        this.tipTV = this.handleView.findViewById(R.id.tv_tip);
        this.rt_layout = this.handleView.findViewById(R.id.rl_rc_calibration_result);
        this.errorTip = this.handleView.findViewById(R.id.tv_error_tip);
        this.rtTip = this.handleView.findViewById(R.id.tv_result_tip);
        this.rtBtn = this.handleView.findViewById(R.id.btn_rt);
        this.rtBtn.setOnClickListener(this);
        this.rc_layout = this.handleView.findViewById(R.id.rc_calibration_content);
        this.control_layout = this.handleView.findViewById(R.id.control_layout);
        this.leftDownRoller.setOnClickListener(this);
        this.rtImage = rootView.findViewById(R.id.img_result);
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void openUi() {
        super.openUi();
        this.isShow = true;
        this.droneOkay = StateManager.getInstance().getConectState().isConnectDrone();
        if (this.droneOkay) {
            this.tipTV.setText(getString(R.string.x8_rc_plane_connect));
            gotoModel(RcStatus.error);
        } else if (StateManager.getInstance().getConectState().isConnectRelay()) {
            updateViewEnable(true, this.rc_layout, this.rt_layout);
            gotoModel(this.rcStatus);
        } else {
            this.tipTV.setText(getString(R.string.x8_rc_no_connect_tip));
            gotoModel(RcStatus.error);
        }
    }

    @Override
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (b && StateManager.getInstance().getX8Drone().isInSky()) {
            this.tipTV.setText(getString(R.string.x8_rc_plane_inSky));
            gotoModel(RcStatus.error);
            return;
        }
        if (this.droneOkay != b) {
            this.droneOkay = b;
        }
        if (this.droneOkay) {
            this.tipTV.setText(getString(R.string.x8_rc_plane_connect));
            gotoModel(RcStatus.error);
        } else if (this.rcConnect) {
            if (this.rcStatus == RcStatus.ideal) {
                startCheck();
                gotoModel(RcStatus.ideal);
            } else if (this.rcStatus == RcStatus.midModel) {
                gotoModel(RcStatus.midModel);
            } else if (this.rcStatus == RcStatus.rollerModel) {
                gotoModel(RcStatus.rollerModel);
            } else if (this.rcStatus == RcStatus.joyModel) {
                gotoModel(RcStatus.joyModel);
            }
        }
    }

    public void checkRcConnect(boolean isConnect) {
        if (this.rcConnect != isConnect) {
            this.rcConnect = isConnect;
            if (this.isShow && !this.droneOkay) {
                if (!isConnect) {
                    this.droneOkay = false;
                    stopTask();
                    if (this.rcStatus != RcStatus.ideal && this.rcStatus != RcStatus.finish && this.rcStatus != RcStatus.fail) {
                        this.rcStatus = RcStatus.conBroken;
                        gotoModel(this.rcStatus);
                        return;
                    }
                    this.tipTV.setText(getString(R.string.x8_rc_no_connect_tip));
                    gotoModel(RcStatus.error);
                    return;
                }
                updateViewEnable(true, this.rc_layout, this.rt_layout);
                startCheck();
                gotoModel(this.rcStatus);
            }
        }
    }

    @Override
    public void closeUi() {
        super.closeUi();
        this.isShow = false;
        releaseDone();
    }

    private void releaseDone() {
        stopTask();
        this.droneOkay = false;
        this.rcConnect = false;
        this.leftClips.clear();
        this.rightClips.clear();
        this.lefMidView.setType(this.leftClips);
        this.rightMidView.setType(this.rightClips);
        this.rightMidView.releaseAll();
        this.lefMidView.releaseAll();
        this.leftUpRoller.clean();
        this.leftDownRoller.clean();
        this.rcStatus = RcStatus.ideal;
        this.curStatus = null;
        this.leftMidTop.setSelected(false);
        this.leftMidLeft.setSelected(false);
        this.leftMidRight.setSelected(false);
        this.leftMidBottom.setSelected(false);
        this.rightMidTop.setSelected(false);
        this.rightMidLeft.setSelected(false);
        this.rightMidRight.setSelected(false);
        this.rightMidBottom.setSelected(false);
        this.upRoller = false;
        this.downRoller = false;
        this.leftMidResult = 0;
        this.rightMidResult = 0;
        this.cali_btn.setVisibility(View.VISIBLE);
        this.rc_layout.setVisibility(View.VISIBLE);
        this.rt_layout.setVisibility(View.GONE);
        this.errorTip.setVisibility(View.GONE);
        gotoModel(this.rcStatus);
        this.cali_btn.setEnabled(false);
        this.cali_btn.setAlpha(0.6f);
    }

    private void stopTask() {
        if (this.checkTask != null) {
            this.checkTask.cancel();
            this.checkTask = null;
        }
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    private void startCheck() {
        stopTask();
        this.timer = new Timer();
        this.checkTask = new CheckTask();
        this.timer.schedule(this.checkTask, 0L, 1000L);
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    public boolean isCalibrationing() {
        if (this.rcStatus == RcStatus.ideal || this.rcStatus == RcStatus.fail || this.rcStatus == RcStatus.finish || this.rcStatus == RcStatus.error) {
            return false;
        }
        breakOutDone();
        return true;
    }


    public enum RcStatus {
        ideal,
        midModel,
        joyModel,
        rollerModel,
        fail,
        finish,
        conBroken,
        error
    }


    public class CheckTask extends TimerTask {
        CheckTask() {
        }

        @Override
        public void run() {
            if (X8RCCalibrationController.this.fcCtrlManager != null) {
                X8RCCalibrationController.this.fcCtrlManager.checkRcCalibrationProgress(X8RCCalibrationController.this);
            }
        }
    }
}
