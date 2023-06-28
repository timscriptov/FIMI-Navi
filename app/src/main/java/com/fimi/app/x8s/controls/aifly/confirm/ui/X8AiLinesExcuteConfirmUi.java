package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fimi.TcpClient;
import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.controls.aifly.X8AiLineExcuteController;
import com.fimi.app.x8s.entity.X8AilinePrameter;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.manager.X8MapGetCityManager;
import com.fimi.app.x8s.map.interfaces.AbsAiLineManager;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.tools.GeoTools;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8TabHost;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;
import com.fimi.kernel.store.sqlite.helper.X8AiLinePointInfoHelper;
import com.fimi.widget.SwitchButton;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePoints;
import com.fimi.x8sdk.dataparser.cmd.CmdAiLinePointsAction;
import com.fimi.x8sdk.entity.FLatLng;
import com.fimi.x8sdk.map.MapType;
import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.rtp.X8Rtp;
import com.fimi.x8sdk.util.GpsCorrect;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class X8AiLinesExcuteConfirmUi implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SwitchButton.OnSwitchListener, X8DoubleCustomDialog.onDialogButtonClickListener {
    private static final int CURVE_MODE = 3;
    private final View contentView;
    private final CameraManager mCameraManager;
    private final X8sMainActivity mX8sMainActivity;
    private final float MIN = 1.0f;
    private final float MAX = 10.0f;
    private final int MAX_PROGRESS = (int) ((this.MAX - this.MIN) * 10.0f);
    private final SaveData mSaveData = new SaveData();
    private final float DEFAULE_SPEED = 2.0f;
    boolean isInSky;
    int i = 4;
    private Button btnGo;
    private X8AiLinePointInfo dataByHistory;
    private X8DoubleCustomDialog dialog;
    private FcManager fcManager;
    private ImageView imgBack;
    private IX8NextViewListener listener;
    private X8TabHost mCurveVidotape;
    private RelativeLayout mRlCurveVideotape;
    private SwitchButton mSbCurve;
    private SwitchButton mSbRecord;
    private TextView mTvDistanceTitle;
    private TextView mTvLineFollowTitle;
    private X8AiLineExcuteController mX8AiLineExcuteController;
    private X8AilinePrameter mX8AilinePrameter;
    private List<MapPointLatLng> mapPointList;
    private X8MapVideoController mapVideoController;
    private X8AiLineExcuteController.LineModel model;
    private SeekBar sbSeekBar;
    private X8TabHost tbDisconnect;
    private X8TabHost tbEnd;
    private TextView tvDistance;
    private X8TabHost tvOrientation1;
    private X8TabHost tvOrientation2;
    private TextView tvPointSize;
    private TextView tvSpeed;
    private TextView tvTime;
    private View vMinus;
    private View vPlus;
    private float distance = 0.0f;
    private int successCount = 0;
    private int aiLineMode = 0;

    public X8AiLinesExcuteConfirmUi(Activity activity, View parent, CameraManager cameraManager) {
        DroneState state = StateManager.getInstance().getX8Drone();
        this.isInSky = state.isInSky();
        this.mX8sMainActivity = (X8sMainActivity) activity;
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_line_excute_confirm_layout, (ViewGroup) parent, true);
        this.mCameraManager = cameraManager;
        initView(this.contentView);
        initAction();
    }

    public void setListener(IX8NextViewListener listener, FcManager fcManager, X8MapVideoController mapVideoController, X8AilinePrameter mX8AilinePrameter, X8AiLineExcuteController mX8AiLineExcuteController) {
        this.listener = listener;
        this.fcManager = fcManager;
        this.mapVideoController = mapVideoController;
        this.mX8AilinePrameter = mX8AilinePrameter;
        this.mX8AiLineExcuteController = mX8AiLineExcuteController;
    }

    public void initView(View rootView) {
        this.imgBack = rootView.findViewById(R.id.img_ai_follow_return);
        this.mTvLineFollowTitle = rootView.findViewById(R.id.tv_ai_follow_title);
        this.tvDistance = rootView.findViewById(R.id.tv_ai_follow_distance);
        this.mTvDistanceTitle = rootView.findViewById(R.id.tv_ai_follow_confirm_title1);
        this.tvTime = rootView.findViewById(R.id.tv_ai_follow_time);
        this.tvSpeed = rootView.findViewById(R.id.tv_ai_follow_speed);
        this.tvPointSize = rootView.findViewById(R.id.tv_ai_follow_size);
        this.vMinus = rootView.findViewById(R.id.rl_minus);
        this.sbSeekBar = rootView.findViewById(R.id.sb_value);
        this.vPlus = rootView.findViewById(R.id.rl_plus);
        this.btnGo = rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.mRlCurveVideotape = rootView.findViewById(R.id.rl_ai_line_curve_videotape);
        this.mCurveVidotape = rootView.findViewById(R.id.x8_ai_line_curve_videotape);
        this.mCurveVidotape.setSelect(0);
        this.tvOrientation1 = rootView.findViewById(R.id.x8_ai_line_rorate1);
        this.tvOrientation1.setOnSelectListener(new X8TabHost.OnSelectListener() {
            @Override
            public void onSelect(int index, String text, int last) {
                if (index != last) {
                    if (index == 1) {
                        X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setOrientation(2);
                        if (X8AiLinesExcuteConfirmUi.this.mX8AiLineExcuteController != null && X8AiLinesExcuteConfirmUi.this.dataByHistory == null) {
                            X8AiLinesExcuteConfirmUi.this.mX8AiLineExcuteController.getmX8AiLineInterestPointController().showView(false);
                        }
                        X8AiLinesExcuteConfirmUi.this.changeOrientation(2);
                        return;
                    }
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setOrientation(0);
                    if (X8AiLinesExcuteConfirmUi.this.mX8AiLineExcuteController != null && X8AiLinesExcuteConfirmUi.this.dataByHistory == null) {
                        X8AiLinesExcuteConfirmUi.this.mX8AiLineExcuteController.getmX8AiLineInterestPointController().showView(true);
                    }
                    X8AiLinesExcuteConfirmUi.this.changeOrientation(0);
                }
            }
        });
        this.tvOrientation2 = rootView.findViewById(R.id.x8_ai_line_rorate2);
        this.tvOrientation2.setOnSelectListener(new X8TabHost.OnSelectListener() {
            @Override
            public void onSelect(int index, String text, int last) {
                if (index != last) {
                    if (index == 2) {
                        index = 1;
                    } else if (index == 1) {
                        index = 2;
                    }
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setOrientation(index);
                    X8AiLinesExcuteConfirmUi.this.changeOrientationForVideo(index);
                }
            }
        });
        this.tbDisconnect = rootView.findViewById(R.id.tb_disconnect);
        this.tbDisconnect.setOnSelectListener(new X8TabHost.OnSelectListener() {
            @Override
            public void onSelect(int index, String text, int last) {
                if (index != last) {
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setDisconnectActioin(index);
                }
            }
        });
        this.tbEnd = rootView.findViewById(R.id.tb_ai_excute_end);
        this.tbEnd.setOnSelectListener(new X8TabHost.OnSelectListener() {
            @Override
            public void onSelect(int index, String text, int last) {
                if (index != last) {
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setEndAction(index);
                }
            }
        });
        this.mSbRecord = rootView.findViewById(R.id.sb_ai_auto_record);
        this.mSbCurve = rootView.findViewById(R.id.swb_ai_curve);
        this.sbSeekBar.setMax(this.MAX_PROGRESS);
        this.mSbCurve.setEnabled(true);
        this.mSbRecord.setEnabled(true);
        if (!this.isInSky) {
            this.btnGo.setText(rootView.getContext().getString(R.string.x8_ai_fly_line_save));
        }
    }

    public void changeOrientation(int orientation) {
        float angle;
        int size = this.mapPointList.size();
        for (int i = 0; i < size; i++) {
            MapPointLatLng point = this.mapPointList.get(i);
            point.yawMode = orientation;
            if (orientation == 0) {
                point.setAngle(0.0f);
            } else {
                if (i == 0) {
                    MapPointLatLng point2 = this.mapVideoController.getFimiMap().getDeviceLatlng();
                    angle = getAngle(point2, point);
                } else {
                    MapPointLatLng point22 = this.mapPointList.get(i - 1);
                    angle = getAngle(point22, point);
                }
                point.setAngle(angle);
            }
        }
        this.mapVideoController.getFimiMap().getAiLineManager().clearAllSmallMarker();
        if (orientation != 0) {
            this.mapVideoController.getFimiMap().getAiLineManager().addSmallMarkerByMap(0);
        }
    }

    public void changeOrientationForVideo(int orientation) {
        float angle;
        int size = this.mapPointList.size();
        for (int i = 0; i < size; i++) {
            MapPointLatLng point = this.mapPointList.get(i);
            point.yawMode = orientation;
            if (orientation == 0) {
                point.showAngle = 0.0f;
            } else if (orientation == 2) {
                if (i == 0) {
                    MapPointLatLng point2 = this.mapVideoController.getFimiMap().getDeviceLatlng();
                    angle = getAngle(point2, point);
                } else {
                    MapPointLatLng point22 = this.mapPointList.get(i - 1);
                    angle = getAngle(point22, point);
                }
                point.showAngle = angle;
            } else {
                point.showAngle = point.angle;
            }
        }
        if (orientation == 0) {
            this.mapVideoController.getFimiMap().getAiLineManager().clearAllSmallMarker();
        } else if (orientation == 1) {
            this.mapVideoController.getFimiMap().getAiLineManager().addOrUpdateSmallMarkerForVideo(1);
        } else {
            this.mapVideoController.getFimiMap().getAiLineManager().addOrUpdateSmallMarkerForVideo(2);
        }
    }

    public void initAction() {
        this.imgBack.setOnClickListener(this);
        this.tvDistance.setOnClickListener(this);
        this.tvTime.setOnClickListener(this);
        this.tvSpeed.setOnClickListener(this);
        this.vMinus.setOnClickListener(this);
        this.sbSeekBar.setOnSeekBarChangeListener(this);
        this.vPlus.setOnClickListener(this);
        this.btnGo.setOnClickListener(this);
        this.mSbCurve.setOnSwitchListener(this);
        this.mSbRecord.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
            @Override
            public void onSwitch(View view, boolean on) {
                if (on) {
                    X8AiLinesExcuteConfirmUi.this.mSbRecord.setSwitchState(false);
                    X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setAutoRecorde(0);
                    return;
                }
                X8AiLinesExcuteConfirmUi.this.mSbRecord.setSwitchState(true);
                X8AiLinesExcuteConfirmUi.this.mX8AilinePrameter.setAutoRecorde(1);
            }
        });
    }    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (X8AiLinesExcuteConfirmUi.this.i < X8AiLinesExcuteConfirmUi.this.mapPointList.size()) {
                X8AiLinesExcuteConfirmUi.this.mHandler.sendEmptyMessageDelayed(0, 2000L);
                AbsAiLineManager aiLineManager = X8AiLinesExcuteConfirmUi.this.mapVideoController.getFimiMap().getAiLineManager();
                X8AiLinesExcuteConfirmUi x8AiLinesExcuteConfirmUi = X8AiLinesExcuteConfirmUi.this;
                int i = x8AiLinesExcuteConfirmUi.i;
                x8AiLinesExcuteConfirmUi.i = i + 1;
                aiLineManager.setAiLineIndexPoint(i);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_minus) {
            if (this.sbSeekBar.getProgress() != 0) {
                this.sbSeekBar.setProgress(this.sbSeekBar.getProgress() - 10);
            }
        } else if (id == R.id.rl_plus) {
            if (this.sbSeekBar.getProgress() != this.MAX_PROGRESS) {
                this.sbSeekBar.setProgress(this.sbSeekBar.getProgress() + 10);
            }
        } else if (id == R.id.img_ai_follow_return) {
            this.listener.onBackClick();
            if (this.dialog != null) {
                this.dialog.dismiss();
            }
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            LatLng fristPointLatLng = new LatLng(this.mapPointList.get(0).latitude, this.mapPointList.get(0).longitude);
            LatLng droneLatLng = new LatLng(StateManager.getInstance().getX8Drone().getLatitude(), StateManager.getInstance().getX8Drone().getLongitude());
            double distance = GeoTools.getDistance(fristPointLatLng, droneLatLng).valueInMeters();
            if (distance > 100.0d) {
                showDialog();
            } else {
                sendRecordMode();
            }
        }
    }

    @Override
    public void onLeft() {
        sendRecordMode();
    }

    @Override
    public void onRight() {
        startAiLineSetPoint(true);
    }

    public void showDialog() {
        if (this.dialog == null) {
            String t = this.contentView.getContext().getString(R.string.x8_ai_fly_route);
            String m = this.contentView.getContext().getString(R.string.x8_ai_line_save_collector);
            this.dialog = new X8DoubleCustomDialog(this.contentView.getContext(), t, m, this.contentView.getContext().getString(R.string.x8_ai_line_save_fly), this.contentView.getContext().getString(R.string.x8_save), this);
        }
        this.dialog.show();
    }

    private void sendRecordMode() {
        if (this.aiLineMode == 3) {
            if (this.mCurveVidotape.getSelectIndex() == 0) {
                this.mCameraManager.setCameraKeyParams("normal_record", CameraJsonCollection.KEY_RECORD_MODE, new JsonUiCallBackListener() {
                    @Override
                    public void onComplete(JSONObject rt, Object o) {
                        X8AiLinesExcuteConfirmUi.this.startVideo();
                    }
                });
                return;
            } else if (this.mCurveVidotape.getSelectIndex() == 1) {
                this.mCameraManager.setCameraKeyParams("5s", CameraJsonCollection.KEY_VIDEO_TIMELAPSE, new JsonUiCallBackListener() {
                    @Override
                    public void onComplete(JSONObject rt, Object o) {
                        X8AiLinesExcuteConfirmUi.this.startVideo();
                    }
                });
                return;
            } else {
                startAiLineSetPoint(false);
                return;
            }
        }
        startAiLineSetPoint(false);
    }

    public void startVideo() {
        this.mCameraManager.startVideo(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess) {
                    X8AiLinesExcuteConfirmUi.this.startAiLineSetPoint(false);
                    return;
                }
                String rtpCamera = X8Rtp.getRtpStringCamera(X8AiLinesExcuteConfirmUi.this.contentView.getContext(), cmdResult.getmMsgRpt());
                if (!rtpCamera.equals("")) {
                    X8ToastUtil.showToast(X8AiLinesExcuteConfirmUi.this.contentView.getContext(), rtpCamera, 1);
                }
            }
        });
    }

    public void stopVideo() {
        this.mCameraManager.stopVideo(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess) {
                    String rtpCamera = X8Rtp.getRtpStringCamera(X8AiLinesExcuteConfirmUi.this.contentView.getContext(), cmdResult.getmMsgRpt());
                    if (!rtpCamera.equals("")) {
                        X8ToastUtil.showToast(X8AiLinesExcuteConfirmUi.this.contentView.getContext(), rtpCamera, 1);
                    }
                }
            }
        });
    }

    public void startAiLineSetPoint(boolean isSaveData2Collector) {
        int orientation = 0;
        int compeletEvent = 0;
        int disconnectEvent = 0;
        if (this.tvOrientation1.getVisibility() == 0) {
            int i = this.tvOrientation1.getSelectIndex();
            if (i == 0) {
                orientation = 0;
            } else if (i == 1) {
                orientation = 2;
            }
        }
        if (this.tvOrientation2.getVisibility() == 0) {
            int i2 = this.tvOrientation2.getSelectIndex();
            if (i2 == 0) {
                orientation = 0;
            } else if (i2 == 1) {
                orientation = 2;
            } else if (i2 == 2) {
                orientation = 1;
            }
        }
        if (this.tbDisconnect.getSelectIndex() == 0) {
            disconnectEvent = 0;
        } else if (this.tbDisconnect.getSelectIndex() == 1) {
            disconnectEvent = 1;
        }
        if (this.tbEnd.getSelectIndex() == 0) {
            compeletEvent = 0;
        } else if (this.tbEnd.getSelectIndex() == 1) {
            compeletEvent = 4;
        }
        this.successCount = 0;
        float speed = this.MIN + (this.sbSeekBar.getProgress() / 10.0f);
        int s = (int) (10.0f * speed);
        int size = this.mapPointList.size();
        List<CmdAiLinePoints> mCmdAiLinePoints = new ArrayList<>();
        List<X8AiLinePointLatlngInfo> list = new ArrayList<>();
        for (int i3 = 0; i3 < size; i3++) {
            MapPointLatLng point = this.mapPointList.get(i3);
            CmdAiLinePoints cmdPoint = new CmdAiLinePoints();
            FLatLng mFlatlng = GpsCorrect.Mars_To_Earth0(point.latitude, point.longitude);
            if (point.mInrertestPoint != null) {
                cmdPoint.pioEnbale = 1;
            } else {
                cmdPoint.pioEnbale = 0;
            }
            float angle = point.showAngle;
            if (orientation == 1) {
                angle = point.angle;
            }
            point.yawMode = orientation;
            int altitude = ((int) point.altitude) * 10;
            X8AiLinePointLatlngInfo mLatlngInfo = new X8AiLinePointLatlngInfo();
            mLatlngInfo.setNumber(i3);
            mLatlngInfo.setTotalnumber(size);
            mLatlngInfo.setLongitude(point.longitude);
            mLatlngInfo.setLatitude(point.latitude);
            mLatlngInfo.setAltitude((int) point.altitude);
            mLatlngInfo.setGimbalPitch(point.gimbalPitch);
            mLatlngInfo.setYaw(angle);
            mLatlngInfo.setSpeed(s);
            mLatlngInfo.setYawMode(point.yawMode);
            if (this.aiLineMode == 3) {
                mLatlngInfo.setPointActionCmd(-1);
            } else {
                mLatlngInfo.setPointActionCmd(point.action);
            }
            mLatlngInfo.setRoration(point.roration);
            if (point.mInrertestPoint != null) {
                mLatlngInfo.setLongitudePOI(point.mInrertestPoint.longitude);
                mLatlngInfo.setLatitudePOI(point.mInrertestPoint.latitude);
                mLatlngInfo.setAltitudePOI((int) point.mInrertestPoint.altitude);
            }
            list.add(mLatlngInfo);
            cmdPoint.speed = s;
            cmdPoint.angle = switchScreenAngle2DroneAngle(angle);
            cmdPoint.gimbalPitch = point.gimbalPitch;
            cmdPoint.orientation = point.yawMode;
            cmdPoint.rotation = point.roration;
            cmdPoint.latitude = mFlatlng.latitude;
            cmdPoint.longitude = mFlatlng.longitude;
            cmdPoint.altitude = altitude;
            cmdPoint.count = size;
            cmdPoint.nPos = i3;
            cmdPoint.autoRecord = this.mSbRecord.getToggleOn() ? 1 : 0;
            MapPointLatLng pointInterest = point.mInrertestPoint;
            if (pointInterest != null) {
                FLatLng mFlatlngInterest = GpsCorrect.Mars_To_Earth0(pointInterest.latitude, pointInterest.longitude);
                cmdPoint.latitudePIO = mFlatlngInterest.latitude;
                cmdPoint.longitudePIO = mFlatlngInterest.longitude;
                cmdPoint.altitudePIO = ((int) pointInterest.altitude) * 10;
            }
            cmdPoint.disconnectEvent = disconnectEvent;
            cmdPoint.compeletEvent = compeletEvent;
            if ((this.aiLineMode == 3) | (this.dataByHistory != null && this.dataByHistory.getIsCurve() == 1)) {
                cmdPoint.coordinatedTurnOff = 1;
            } else {
                cmdPoint.coordinatedTurnOff = 0;
            }
            mCmdAiLinePoints.add(cmdPoint);
        }
        this.mSaveData.orientation = orientation;
        this.mSaveData.speed = s;
        this.mSaveData.disConnectType = disconnectEvent;
        this.mSaveData.endType = compeletEvent;
        this.mSaveData.list = list;
        if (this.isInSky && !isSaveData2Collector) {
            sendPoint(mCmdAiLinePoints, size);
            return;
        }
        saveData(this.mSaveData.orientation, this.mSaveData.speed, this.mSaveData.disConnectType, this.mSaveData.endType, list, isSaveData2Collector);
        if (this.dataByHistory == null) {
            X8ToastUtil.showToast(this.contentView.getContext(), "" + this.contentView.getContext().getString(R.string.x8_ai_fly_line_save_tip), 1);
        }
        this.listener.onSaveClick();
    }

    public void sendPoint(List<CmdAiLinePoints> mCmdAiLinePoints, int size) {
        if (mCmdAiLinePoints.size() > 0) {
            sendPointCmdOneByOne(mCmdAiLinePoints, 0, size);
        }
    }

    public void sendPointCmdOneByOne(final List<CmdAiLinePoints> mCmdAiLinePoints, final int index, final int size) {
        this.fcManager.setAiLinePoints(mCmdAiLinePoints.get(index), new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess()) {
                    X8AiLinesExcuteConfirmUi.this.stopVideo();
                } else if (index == size - 1) {
                    X8AiLinesExcuteConfirmUi.this.setPointsAction();
                } else {
                    X8AiLinesExcuteConfirmUi.this.sendPointCmdOneByOne(mCmdAiLinePoints, index + 1, size);
                }
            }
        });
    }

    public void sendActionCmdOneByOne() {
    }

    public void testUI() {
        this.mapVideoController.getFimiMap().getAiLineManager().startAiLineProcess();
        this.mHandler.sendEmptyMessageDelayed(0, 3000L);
    }

    public float getAngle(MapPointLatLng from, MapPointLatLng to) {
        float angle = this.mapVideoController.getFimiMap().getAiLineManager().getPointAngle(from, to);
        return angle;
    }

    public float switchScreenAngle2DroneAngle(float angle) {
        if (angle < 0.0f) {
            return angle;
        }
        if (0.0f > angle || angle > 180.0f) {
            angle -= 360.0f;
        }
        return angle;
    }

    public void saveData(int orientation, int speed, int disconnectType, int endType, List<X8AiLinePointLatlngInfo> list, boolean isSaveData2Collector) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        if (this.dataByHistory == null) {
            X8AiLinePointInfo lineInfo = new X8AiLinePointInfo();
            lineInfo.setTime(System.currentTimeMillis());
            lineInfo.setType(orientation);
            lineInfo.setSpeed(speed);
            lineInfo.setDistance(this.distance);
            lineInfo.setIsCurve(this.aiLineMode == 3 ? 1 : 0);
            lineInfo.setMapType(GlobalConfig.getInstance().getMapType() == MapType.AMap ? 1 : 0);
            lineInfo.setRunByMapOrVedio(this.model == X8AiLineExcuteController.LineModel.VEDIO ? 1 : 0);
            lineInfo.setDisconnectType(disconnectType);
            lineInfo.setExcuteEnd(endType);
            lineInfo.setLocality(X8MapGetCityManager.locality);
            lineInfo.setAutoRecord(this.mSbRecord.getToggleOn() ? 1 : 0);
            X8AiLinePointInfoHelper.getIntance().addLineDatas(lineInfo, list);
            if (isSaveData2Collector) {
                lineInfo.setSaveFlag(1);
                X8AiLinePointInfoHelper.getIntance().updatelineSaveFlag(1, lineInfo.getId().longValue());
            }
        }
    }

    public void startExcute() {
        this.fcManager.setAiLineExcute(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess()) {
                    X8AiLinesExcuteConfirmUi.this.stopVideo();
                    return;
                }
                X8AiLinesExcuteConfirmUi.this.saveData(X8AiLinesExcuteConfirmUi.this.mSaveData.orientation, X8AiLinesExcuteConfirmUi.this.mSaveData.speed, X8AiLinesExcuteConfirmUi.this.mSaveData.disConnectType, X8AiLinesExcuteConfirmUi.this.mSaveData.endType, X8AiLinesExcuteConfirmUi.this.mSaveData.list, false);
                X8AiLinesExcuteConfirmUi.this.listener.onExcuteClick();
            }
        });
    }

    public void setDistance(float distance) {
        this.distance = distance;
        int d = Math.round(distance);
        this.tvDistance.setText(X8NumberUtil.getDistanceNumberString(d, 0, true));
    }

    private void setSpeed(float speed) {
        int progress = (int) (10.0f * speed);
        this.sbSeekBar.setProgress(progress);
    }

    public void setViewValue() {
        float speed = this.MIN + (this.sbSeekBar.getProgress() / 10.0f);
        int time = Math.round(getAllTime(speed));
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString(speed, 1, true));
        if (this.aiLineMode == 3) {
            this.tvTime.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_lines_setting_curve_lessthan) + time + "S");
            this.mTvLineFollowTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_lines_curve));
            this.mRlCurveVideotape.setVisibility(View.VISIBLE);
        } else if (this.aiLineMode == 0) {
            this.tvTime.setText("" + time + "S");
            this.mTvLineFollowTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_lines_straight));
            this.mRlCurveVideotape.setVisibility(View.GONE);
        } else if (this.aiLineMode == 1) {
            this.tvTime.setText("" + time + "S");
            this.mTvLineFollowTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_lines_setting));
            this.mRlCurveVideotape.setVisibility(View.GONE);
        } else if (this.aiLineMode == 2) {
            this.tvTime.setText("" + time + "S");
            this.mTvLineFollowTitle.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_line_history));
            this.mRlCurveVideotape.setVisibility(View.GONE);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float speed = this.MIN + (progress / 10.0f);
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString(speed, 1, true));
        int time = Math.round(getAllTime(speed));
        this.tvTime.setText("" + time + "S");
        if (this.mX8AilinePrameter != null) {
            this.mX8AilinePrameter.setSpeed(speed - 1.0f);
        }
    }

    public float getAllTime(float speed) {
        float runTime;
        if (this.mapPointList == null) {
            return 0.0f;
        }
        int size = this.mapPointList.size();
        float time = 0.0f;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                float alt = StateManager.getInstance().getX8Drone().getHeight();
                MapPointLatLng point1 = this.mapVideoController.getFimiMap().getDeviceLatlng();
                MapPointLatLng point2 = this.mapPointList.get(i);
                runTime = getRunTime(point2.altitude, alt, point1, point2, speed);
            } else {
                MapPointLatLng point12 = this.mapPointList.get(i - 1);
                MapPointLatLng point22 = this.mapPointList.get(i);
                runTime = getRunTime(point22.altitude, point12.altitude, point12, point22, speed);
            }
            time += runTime;
        }
        return time;
    }

    public float getRunTime(float height, float alt, MapPointLatLng point1, MapPointLatLng point2, float speed) {
        float temp;
        boolean isUp;
        float distance = this.mapVideoController.getFimiMap().getAiLineManager().getDistance(point1, point2);
        if (height - alt >= 0.0f) {
            temp = height - alt;
            isUp = true;
        } else {
            temp = alt - height;
            isUp = false;
        }
        float degrees = (float) Math.toDegrees(Math.atan(temp / distance));
        double radians = Math.toRadians(degrees);
        float sh = ((float) Math.cos(radians)) * speed;
        float sv = ((float) Math.sin(radians)) * speed;
        if (isUp) {
            if (sv > 4.0f) {
                sv = 4.0f;
            }
        } else if (sv > 3.0f) {
            sv = 3.0f;
        }
        float tv = temp / sv;
        float th = distance / sh;
        if (tv <= th) {
            return th;
        }
        return tv;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onSwitch(View view, boolean on) {
        this.mSbCurve.setSwitchState(!on);
    }

    public void setPointSizeAndDistance(int aiLinePointSize, float aiLineDistance, List<MapPointLatLng> mapPointList, X8AiLineExcuteController.LineModel model) {
        if (this.aiLineMode == 3) {
            this.mTvDistanceTitle.setVisibility(View.GONE);
            this.tvDistance.setVisibility(View.GONE);
        }
        setDistance(aiLineDistance);
        this.mapPointList = mapPointList;
        this.tvPointSize.setText("" + aiLinePointSize);
        setViewValue();
        this.model = model;
        if (model == X8AiLineExcuteController.LineModel.VEDIO) {
            this.tvOrientation2.setVisibility(View.VISIBLE);
        } else {
            this.tvOrientation1.setVisibility(View.VISIBLE);
        }
        setDat();
    }

    public void setDat() {
        TcpClient.getIntance().sendLog("initView ......." + this.mX8AilinePrameter.toString());
        int orientation = this.mX8AilinePrameter.getOrientation();
        int disconnectEvent = this.mX8AilinePrameter.getDisconnectActioin();
        int endEvent = this.mX8AilinePrameter.getEndAction();
        int autoRecord = this.mX8AilinePrameter.getAutoRecorde();
        if (this.model == X8AiLineExcuteController.LineModel.VEDIO) {
            this.tvOrientation2.setVisibility(View.VISIBLE);
            if (orientation == 0) {
                this.tvOrientation2.setSelect(0);
            } else if (orientation == 1) {
                this.tvOrientation2.setSelect(2);
            } else if (orientation == 2) {
                this.tvOrientation2.setSelect(1);
            }
        } else {
            this.tvOrientation1.setVisibility(View.VISIBLE);
            TcpClient.getIntance().sendLog("orientation ......." + orientation);
            if (orientation == 0) {
                this.tvOrientation1.setSelect(0);
            } else if (orientation == 2) {
                TcpClient.getIntance().sendLog("orientation .....1111111111111.." + orientation);
                this.tvOrientation1.setSelect(1);
            }
        }
        TcpClient.getIntance().sendLog("disconnectEvent ......." + disconnectEvent);
        if (disconnectEvent == 0) {
            this.tbDisconnect.setSelect(0);
        } else if (disconnectEvent == 1) {
            this.tbDisconnect.setSelect(1);
        }
        TcpClient.getIntance().sendLog("endEvent ......." + endEvent);
        if (endEvent == 0) {
            this.tbEnd.setSelect(0);
        } else if (endEvent == 1) {
            this.tbEnd.setSelect(1);
        }
        this.mSbRecord.setSwitchState(autoRecord == 1);
        setSpeed(this.mX8AilinePrameter.getSpeed());
        setViewValue();
    }

    public void setDataByHistory(long lineId) {
        X8AiLinePointInfo lineInfo = X8AiLinePointInfoHelper.getIntance().getLineInfoById(lineId);
        if (lineInfo != null) {
            setDataByHistory(lineInfo);
        }
    }

    public void setDataByHistory(X8AiLinePointInfo info) {
        this.dataByHistory = info;
    }

    public void setPointsAction() {
        int size = this.mapPointList.size();
        this.successCount = 0;
        List<CmdAiLinePointsAction> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MapPointLatLng point = this.mapPointList.get(i);
            CmdAiLinePointsAction cmd = new CmdAiLinePointsAction();
            cmd.pos = i;
            cmd.count = size;
            switch (point.action) {
                case 1:
                    cmd.cmd0 = CmdAiLinePointsAction.Cmd.HOVER.ordinal();
                    cmd.time = 10;
                    cmd.para0 = 1;
                    break;
                case 2:
                    cmd.cmd0 = CmdAiLinePointsAction.Cmd.VIDEO.ordinal();
                    cmd.time = 10;
                    cmd.para0 = 1;
                    break;
                case 4:
                    cmd.cmd0 = CmdAiLinePointsAction.Cmd.PHOTO.ordinal();
                    cmd.time = 1;
                    cmd.para0 = 1;
                    break;
                case 5:
                    cmd.cmd0 = CmdAiLinePointsAction.Cmd.HOVER.ordinal();
                    cmd.cmd1 = CmdAiLinePointsAction.Cmd.PHOTO.ordinal();
                    cmd.time = 5;
                    cmd.para0 = 1;
                    cmd.para1 = 1;
                    break;
                case 6:
                    cmd.cmd0 = CmdAiLinePointsAction.Cmd.PHOTO.ordinal();
                    cmd.time = 1;
                    cmd.para0 = 3;
                    break;
            }
            list.add(cmd);
        }
        if (list.size() > 0) {
            sendActionCmdOneByOne(list, 0, list.size());
        }
    }

    public void sendActionCmdOneByOne(final List<CmdAiLinePointsAction> list, final int index, final int size) {
        this.fcManager.setAiLinePointsAction(list.get(index), new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess()) {
                    X8AiLinesExcuteConfirmUi.this.stopVideo();
                    X8ToastUtil.showToast(X8AiLinesExcuteConfirmUi.this.contentView.getContext(), "setAiLinePointsAction ERROR", 0);
                } else if (index == size - 1) {
                    X8AiLinesExcuteConfirmUi.this.startExcute();
                } else {
                    X8AiLinesExcuteConfirmUi.this.sendActionCmdOneByOne(list, index + 1, size);
                }
            }
        });
    }

    public void setAiLineMode(int aiLineMode) {
        this.aiLineMode = aiLineMode;
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.btnGo.setEnabled(isInSky && isLowPower);
    }

    public CameraManager getCameraManager() {
        return this.mCameraManager;
    }

    public class SaveData {
        public int disConnectType;
        public int endType;
        public int orientation;
        public int speed;
        List<X8AiLinePointLatlngInfo> list;

        private SaveData() {
        }
    }




}
