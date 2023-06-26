package com.fimi.app.x8s.viewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.camera.CameraParamStatus;
import com.fimi.app.x8s.controls.camera.X8CameraAwbItemController;
import com.fimi.app.x8s.controls.camera.X8CameraItemArrayController;
import com.fimi.app.x8s.entity.PhotoSubParamItemEntity;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.modulestate.StateManager;

import java.util.Map;


public class SubParamsViewHolder extends RecyclerView.ViewHolder implements JsonUiCallBackListener, SeekBar.OnSeekBarChangeListener {
    private final int delaySend;
    private final RelativeLayout contentLayout;
    private final Context context;
    private final Handler handler;
    private final TextView optionValue;
    private final SeekBar seekBar;
    private final ViewStub sharpViewStub;
    private final View styleView;
    private final TextView sub_options;
    private final ViewStub viewStub;
    SubParamItemListener listener;
    ImageView resetBtn;
    private X8CameraItemArrayController arrayController;
    private X8CameraAwbItemController awbController;
    private String contrast;
    private boolean fromUser;
    private TextView normalTv;
    private String paramKey;
    private String saturation;
    private TextView sharpTv;
    private View sharpView;
    private PhotoSubParamItemEntity subParamItemEntity;

    @SuppressLint("HandlerLeak")
    public SubParamsViewHolder(View itemView, SubParamItemListener itemListener) {
        super(itemView);
        this.delaySend = 1;
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (listener != null && optionValue != null) {
                        listener.setRecyclerScroller(true);
                        if (paramKey.equals(context.getResources().getString(R.string.x8_camera_saturation))) {
                            if (optionValue.getText().toString() != null) {
                                listener.styleParam("saturation", Integer.parseInt(optionValue.getText().toString()));
                                return;
                            }
                            return;
                        } else if (paramKey.equals(context.getResources().getString(R.string.x8_camera_contrast)) && optionValue.getText().toString() != null) {
                            listener.styleParam("contrast", Integer.parseInt(optionValue.getText().toString()));
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                }
            }
        };
        this.context = itemView.getContext();
        this.listener = itemListener;
        this.sub_options = itemView.findViewById(R.id.sub_option_name);
        this.optionValue = itemView.findViewById(R.id.sub_option_value);
        this.contentLayout = itemView.findViewById(R.id.sub_content_layout);
        this.viewStub = itemView.findViewById(R.id.camera_style_layout);
        this.styleView = this.viewStub.inflate();
        this.seekBar = this.styleView.findViewById(R.id.style_seekBar);
        this.seekBar.setOnSeekBarChangeListener(this);
        if (!StateManager.getInstance().getConectState().isCameraConnect()) {
            this.seekBar.setProgress(64);
            this.optionValue.setText("64");
        }
        this.resetBtn = this.styleView.findViewById(R.id.x8_btn_reset);
        this.sharpViewStub = itemView.findViewById(R.id.camera_sharp_layout);
    }

    public void initItemData(PhotoSubParamItemEntity itemEntity, int index, boolean enable, int select_index) {
        StateManager.getInstance().getCamera().isDelayedPhotography();
        if (StateManager.getInstance().getCamera().isDelayedPhotography()) {
            enable = false;
        }
        updateEnable(enable);
        this.subParamItemEntity = itemEntity;
        if (itemEntity != null) {
            this.paramKey = itemEntity.getOptions().get(index);
            if (itemEntity.getParamValue() != null) {
                if (!isMultilevelMenuView(itemEntity.getParamKey())) {
                    this.sub_options.setSelected(itemEntity.getParamValue().contains(this.paramKey));
                } else
                    this.sub_options.setSelected(itemEntity.getParamValue().equals(this.paramKey));
            } else if (this.paramKey.equals(CameraJsonCollection.KEY_CAMERA_STYLE)) {
            }
            Map<String, String> nickMap = itemEntity.getOptionMap();
            String nickValue = nickMap.get(this.paramKey);
            if (nickMap != null && nickValue != null) {
                this.sub_options.setText(nickValue);
            } else {
                this.sub_options.setText(paramKey2Title(this.paramKey));
            }
            if (this.paramKey.equals(this.context.getResources().getString(R.string.x8_camera_saturation))) {
                this.saturation = nickMap.get("saturation");
                this.optionValue.setVisibility(View.VISIBLE);
                if (this.styleView != null) {
                    this.styleView.setVisibility(View.VISIBLE);
                }
            } else if (this.paramKey.equals(this.context.getResources().getString(R.string.x8_camera_contrast))) {
                this.contrast = nickMap.get("contrast");
                this.optionValue.setVisibility(View.VISIBLE);
                if (this.styleView != null) {
                    this.styleView.setVisibility(View.VISIBLE);
                }
            } else if (this.paramKey.equals(this.context.getResources().getString(R.string.x8_camera_sharpness))) {
                if (this.sharpView != null) {
                    this.sharpView.setVisibility(View.VISIBLE);
                }
            } else {
                this.optionValue.setVisibility(View.GONE);
                if (this.styleView != null) {
                    this.styleView.setVisibility(View.GONE);
                }
                if (this.sharpView != null) {
                    this.sharpView.setVisibility(View.GONE);
                }
            }
            if (this.styleView != null && !this.fromUser) {
                if (this.saturation != null) {
                    this.seekBar.setProgress(Integer.parseInt(this.saturation));
                    this.optionValue.setText(this.saturation);
                }
                if (this.contrast != null) {
                    this.seekBar.setProgress(Integer.parseInt(this.contrast));
                    this.optionValue.setText(this.contrast);
                }
            }
        }
        if (select_index != index || isMultilevelMenuView(itemEntity.getParamKey())) {
            this.contentLayout.removeAllViews();
        } else if (CameraJsonCollection.isClearData) {
            this.contentLayout.removeAllViews();
            CameraJsonCollection.isClearData = false;
        }
    }

    private boolean isMultilevelMenuView(String paramKey) {
        return !paramKey.equals("capture_mode") && !paramKey.equals(CameraJsonCollection.KEY_RECORD_MODE) && !paramKey.equals("video_resolution");
    }

    private boolean isMultilevelMenuViewTwo(@NonNull String paramKey) {
        return !paramKey.equals("capture_mode") && !paramKey.equals(CameraJsonCollection.KEY_RECORD_MODE);
    }

    private String paramKey2Title(String paramKey) {
        if (this.context.getString(R.string.x8_timelapse_capture_0).contains(paramKey)) {
            return this.context.getString(R.string.x8_photo_signal_mode);
        } else if (this.context.getString(R.string.x8_timelapse_capture_1).contains(paramKey)) {
            return this.context.getString(R.string.x8_photo_delay_mode);
        } else if (this.context.getString(R.string.x8_timelapse_capture_8).contains(paramKey)) {
            return this.context.getString(R.string.x8_photo_panorama_mode);
        } else if (this.context.getString(R.string.x8_timelapse_record_0).contains(paramKey)) {
            return this.context.getString(R.string.x8_record_normal);
        } else if (this.context.getString(R.string.x8_timelapse_record_1).contains(paramKey)) {
            return this.context.getString(R.string.x8_record_cut_mode);
        } else {
            return paramKey;
        }
    }

    public void addParamContent(Context context, CameraManager cameraManager, int layout_id, @NonNull String... optionName) {
        this.contentLayout.removeAllViews();
        View paramView = LayoutInflater.from(context).inflate(layout_id, this.contentLayout, false);
        this.contentLayout.addView(paramView);
        if (optionName[0].equals(context.getResources().getString(R.string.x8_camera_awb))) {
            this.awbController = new X8CameraAwbItemController(paramView, cameraManager);
            this.optionValue.setVisibility(View.VISIBLE);
        } else if (!optionName[0].equals(context.getResources().getString(R.string.x8_camera_style))) {
            if (optionName[0].equals(context.getResources().getString(R.string.x8_video_resolution))) {
                this.arrayController = new X8CameraItemArrayController(paramView, cameraManager, "video_resolution", this.listener);
                Map<String, String> optionMap = this.subParamItemEntity.getOptionMap();
                this.arrayController.updateParamList(optionMap, optionName[1], optionName[2]);
            } else if (optionName[0].equals(context.getResources().getString(R.string.x8_photo_mode))) {
                this.arrayController = new X8CameraItemArrayController(paramView, cameraManager, "capture_mode", this.listener);
                Map<String, String> optionMap2 = this.subParamItemEntity.getOptionMap();
                this.arrayController.updateParamList(optionMap2, optionName[1], optionName[2]);
            } else if (optionName[0].equals(context.getResources().getString(R.string.x8_record_mode))) {
                this.arrayController = new X8CameraItemArrayController(paramView, cameraManager, CameraJsonCollection.KEY_RECORD_MODE, this.listener);
                Map<String, String> optionMap3 = this.subParamItemEntity.getOptionMap();
                this.arrayController.updateParamList(optionMap3, optionName[1], optionName[2]);
            }
        }
    }

    public void initViewStub() {
        if (this.styleView == null) {
            if (this.contrast != null) {
                this.seekBar.setProgress(Integer.parseInt(this.contrast));
                this.optionValue.setText(this.contrast);
            }
            if (this.saturation != null) {
                this.seekBar.setProgress(Integer.parseInt(this.saturation));
                this.optionValue.setText(this.saturation);
            }
        }
        this.optionValue.setVisibility(View.VISIBLE);
        this.styleView.setVisibility(View.VISIBLE);
        this.resetBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.setRecyclerScroller(true);
                if (!paramKey.equals(context.getResources().getString(R.string.x8_camera_saturation))) {
                    if (paramKey.equals(context.getResources().getString(R.string.x8_camera_contrast))) {
                        listener.styleParam("contrast", 64);
                        optionValue.setText(String.valueOf(64));
                        seekBar.setProgress(64);
                        return;
                    }
                    return;
                }
                listener.styleParam("saturation", 64);
                seekBar.setProgress(64);
                optionValue.setText(String.valueOf(64));
            }
        });
    }

    public void initSharpViewStub() {
        if (this.sharpView == null) {
            this.sharpView = this.sharpViewStub.inflate();
        }
        this.sharpView.setVisibility(View.VISIBLE);
        this.normalTv = this.sharpView.findViewById(R.id.normal_v);
        this.sharpTv = this.sharpView.findViewById(R.id.sharp_v);
    }

    @Override
    public void onComplete(JSONObject rt, Object o) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.fromUser = fromUser;
        if (this.styleView != null) {
            this.styleView.getParent().requestDisallowInterceptTouchEvent(true);
        }
        this.optionValue.setText(String.valueOf(progress));
        if (this.handler.hasMessages(1)) {
            this.handler.removeMessages(1);
        }
        this.handler.sendEmptyMessageDelayed(1, 400L);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.fromUser = false;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (this.fromUser) {
            if (this.listener != null) {
                this.listener.setRecyclerScroller(true);
                if (this.paramKey.equals(this.context.getResources().getString(R.string.x8_camera_saturation))) {
                    this.listener.styleParam("saturation", seekBar.getProgress());
                } else if (this.paramKey.equals(this.context.getResources().getString(R.string.x8_camera_contrast))) {
                    this.listener.styleParam("contrast", seekBar.getProgress());
                }
            }
            this.fromUser = false;
        }
    }

    private void updateEnable(boolean enable) {
        String keyString = this.subParamItemEntity != null ? this.subParamItemEntity.getTitleName() : "";
        if (enable) {
            if (!isSubOptionsSetTextColor(keyString)) {
                this.optionValue.setTextColor(this.context.getResources().getColor(R.color.x8_value_select));
                this.sub_options.setTextColor(this.context.getResources().getColor(R.color.x8_value_unselected));
            } else if (isRecordingUnclickableItem(keyString) || isDelayedPhotographyUnclickableItem(keyString)) {
                this.sub_options.setEnabled(false);
                int[][] statesRecording = {new int[]{16842913}, new int[]{-16842910}};
                int[] colorsRecording = {this.context.getResources().getColor(R.color.x8_value_disable_select), this.context.getResources().getColor(R.color.x8_value_disable)};
                ColorStateList colorStateListRecording = new ColorStateList(statesRecording, colorsRecording);
                this.sub_options.setTextColor(colorStateListRecording);
            } else {
                this.sub_options.setEnabled(true);
                int[][] states = {new int[]{16842913}, new int[]{16842910}};
                int[] colors = {this.context.getResources().getColor(R.color.x8_value_select), this.context.getResources().getColor(R.color.x8_value_unselected)};
                ColorStateList colorStateList = new ColorStateList(states, colors);
                this.sub_options.setTextColor(colorStateList);
            }
        } else if (!isSubOptionsSetTextColor(keyString)) {
            this.optionValue.setTextColor(this.context.getResources().getColor(R.color.x8_value_disable));
            this.sub_options.setTextColor(this.context.getResources().getColor(R.color.x8_value_disable));
        } else {
            this.sub_options.setEnabled(false);
            int[][] states2 = {new int[]{16842913}, new int[]{-16842910}};
            int[] colors2 = {this.context.getResources().getColor(R.color.x8_value_disable_select), this.context.getResources().getColor(R.color.x8_value_disable)};
            ColorStateList colorStateList2 = new ColorStateList(states2, colors2);
            this.sub_options.setTextColor(colorStateList2);
        }
        if (this.arrayController != null) {
            this.arrayController.onDroneConnected(enable);
        }
        if (this.seekBar != null) {
            this.seekBar.setEnabled(enable);
        }
        if (this.resetBtn != null) {
            if (!enable) {
                this.resetBtn.setAlpha(0.5f);
                this.seekBar.setAlpha(0.5f);
            } else {
                this.resetBtn.setAlpha(1.0f);
                this.seekBar.setAlpha(1.0f);
            }
            this.resetBtn.setEnabled(enable);
        }
        if (this.contentLayout != null && !enable) {
            this.contentLayout.removeAllViews();
        }
        if (this.sharpTv != null) {
            this.sharpTv.setEnabled(enable);
            if (enable) {
                this.sharpTv.setAlpha(1.0f);
            } else {
                this.sharpTv.setAlpha(0.5f);
            }
        }
        if (this.normalTv != null) {
            this.normalTv.setEnabled(enable);
            if (enable) {
                this.normalTv.setAlpha(1.0f);
            } else {
                this.normalTv.setAlpha(0.5f);
            }
        }
    }

    private boolean isSubOptionsSetTextColor(@NonNull String titleName) {
        return !titleName.equals(this.context.getResources().getString(R.string.x8_camera_style));
    }

    public RelativeLayout getContentLayout() {
        return this.contentLayout;
    }

    public boolean isRecordingUnclickableItem(String titleName) {
        if (CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.recording) {
            return titleName.equals(this.context.getResources().getString(R.string.x8_record_mode)) || titleName.equals(this.context.getResources().getString(R.string.x8_video_type)) || titleName.equals(this.context.getResources().getString(R.string.x8_video_resolution)) || titleName.equals(this.context.getResources().getString(R.string.x8_record_quality));
        }
        return false;
    }

    public boolean isDelayedPhotographyUnclickableItem(String titleName) {
        if (StateManager.getInstance().getCamera().isDelayedPhotography()) {
            return titleName.equals(this.context.getResources().getString(R.string.x8_photo_mode)) || titleName.equals(this.context.getResources().getString(R.string.x8_photo_size)) || titleName.equals(this.context.getResources().getString(R.string.x8_photo_format)) || titleName.equals(this.context.getResources().getString(R.string.x8_camera_awb)) || titleName.equals(this.context.getResources().getString(R.string.x8_camera_digita)) || titleName.equals(this.context.getResources().getString(R.string.x8_camera_style));
        }
        return false;
    }
}
