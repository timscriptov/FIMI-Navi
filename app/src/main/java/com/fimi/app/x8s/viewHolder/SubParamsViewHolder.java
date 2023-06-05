package com.fimi.app.x8s.viewHolder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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

/* loaded from: classes.dex */
public class SubParamsViewHolder extends RecyclerView.ViewHolder implements JsonUiCallBackListener, SeekBar.OnSeekBarChangeListener {
    private final int delaySend;
    SubParamItemListener listener;
    ImageView resetBtn;
    private X8CameraItemArrayController arrayController;
    private X8CameraAwbItemController awbController;
    private RelativeLayout contentLayout;
    private Context context;
    private String contrast;
    private boolean fromUser;
    private Handler handler;
    private TextView normalTv;
    private TextView optionValue;
    private String paramKey;
    private String saturation;
    private SeekBar seekBar;
    private TextView sharpTv;
    private View sharpView;
    private ViewStub sharpViewStub;
    private View styleView;
    private PhotoSubParamItemEntity subParamItemEntity;
    private TextView sub_options;
    private ViewStub viewStub;

    public SubParamsViewHolder(View itemView, SubParamItemListener itemListener) {
        super(itemView);
        this.delaySend = 1;
        this.handler = new Handler() { // from class: com.fimi.app.x8s.viewHolder.SubParamsViewHolder.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        if (SubParamsViewHolder.this.listener != null && SubParamsViewHolder.this.optionValue != null) {
                            SubParamsViewHolder.this.listener.setRecyclerScroller(true);
                            if (SubParamsViewHolder.this.paramKey.equals(SubParamsViewHolder.this.context.getResources().getString(R.string.x8_camera_saturation))) {
                                if (SubParamsViewHolder.this.optionValue.getText().toString() != null) {
                                    SubParamsViewHolder.this.listener.styleParam("saturation", Integer.parseInt(SubParamsViewHolder.this.optionValue.getText().toString()));
                                    return;
                                }
                                return;
                            } else if (SubParamsViewHolder.this.paramKey.equals(SubParamsViewHolder.this.context.getResources().getString(R.string.x8_camera_contrast)) && SubParamsViewHolder.this.optionValue.getText().toString() != null) {
                                SubParamsViewHolder.this.listener.styleParam("contrast", Integer.parseInt(SubParamsViewHolder.this.optionValue.getText().toString()));
                                return;
                            } else {
                                return;
                            }
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.context = itemView.getContext();
        this.listener = itemListener;
        this.sub_options = (TextView) itemView.findViewById(R.id.sub_option_name);
        this.optionValue = (TextView) itemView.findViewById(R.id.sub_option_value);
        this.contentLayout = (RelativeLayout) itemView.findViewById(R.id.sub_content_layout);
        this.viewStub = (ViewStub) itemView.findViewById(R.id.camera_style_layout);
        this.styleView = this.viewStub.inflate();
        this.seekBar = (SeekBar) this.styleView.findViewById(R.id.style_seekBar);
        this.seekBar.setOnSeekBarChangeListener(this);
        if (!StateManager.getInstance().getConectState().isCameraConnect()) {
            this.seekBar.setProgress(64);
            this.optionValue.setText("64");
        }
        this.resetBtn = (ImageView) this.styleView.findViewById(R.id.x8_btn_reset);
        this.sharpViewStub = (ViewStub) itemView.findViewById(R.id.camera_sharp_layout);
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
                    if (itemEntity.getParamValue().contains(this.paramKey)) {
                        this.sub_options.setSelected(true);
                    } else {
                        this.sub_options.setSelected(false);
                    }
                } else if (itemEntity.getParamValue().equals(this.paramKey)) {
                    this.sub_options.setSelected(true);
                } else {
                    this.sub_options.setSelected(false);
                }
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
                this.optionValue.setVisibility(0);
                if (this.styleView != null) {
                    this.styleView.setVisibility(0);
                }
            } else if (this.paramKey.equals(this.context.getResources().getString(R.string.x8_camera_contrast))) {
                this.contrast = nickMap.get("contrast");
                this.optionValue.setVisibility(0);
                if (this.styleView != null) {
                    this.styleView.setVisibility(0);
                }
            } else if (this.paramKey.equals(this.context.getResources().getString(R.string.x8_camera_sharpness))) {
                if (this.sharpView != null) {
                    this.sharpView.setVisibility(0);
                }
            } else {
                this.optionValue.setVisibility(8);
                if (this.styleView != null) {
                    this.styleView.setVisibility(8);
                }
                if (this.sharpView != null) {
                    this.sharpView.setVisibility(8);
                }
            }
            if (this.styleView != null && !this.fromUser) {
                if (this.saturation != null) {
                    this.seekBar.setProgress(Integer.valueOf(this.saturation).intValue());
                    this.optionValue.setText(this.saturation);
                }
                if (this.contrast != null) {
                    this.seekBar.setProgress(Integer.valueOf(this.contrast).intValue());
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
        if (paramKey.equals("capture_mode") || paramKey.equals(CameraJsonCollection.KEY_RECORD_MODE) || paramKey.equals("video_resolution")) {
            return false;
        }
        return true;
    }

    private boolean isMultilevelMenuViewTwo(String paramKey) {
        if (paramKey.equals("capture_mode") || paramKey.equals(CameraJsonCollection.KEY_RECORD_MODE)) {
            return false;
        }
        return true;
    }

    private String paramKey2Title(String paramKey) {
        if (this.context.getString(R.string.x8_timelapse_capture_0).contains(paramKey)) {
            String string = this.context.getString(R.string.x8_photo_signal_mode);
            return string;
        } else if (this.context.getString(R.string.x8_timelapse_capture_1).contains(paramKey)) {
            String string2 = this.context.getString(R.string.x8_photo_delay_mode);
            return string2;
        } else if (this.context.getString(R.string.x8_timelapse_capture_8).contains(paramKey)) {
            String string3 = this.context.getString(R.string.x8_photo_panorama_mode);
            return string3;
        } else if (this.context.getString(R.string.x8_timelapse_record_0).contains(paramKey)) {
            String string4 = this.context.getString(R.string.x8_record_normal);
            return string4;
        } else if (this.context.getString(R.string.x8_timelapse_record_1).contains(paramKey)) {
            String string5 = this.context.getString(R.string.x8_record_cut_mode);
            return string5;
        } else {
            return paramKey;
        }
    }

    public void addParamContent(Context context, CameraManager cameraManager, int layout_id, String... optionName) {
        this.contentLayout.removeAllViews();
        View paramView = LayoutInflater.from(context).inflate(layout_id, (ViewGroup) this.contentLayout, false);
        this.contentLayout.addView(paramView);
        if (optionName[0].equals(context.getResources().getString(R.string.x8_camera_awb))) {
            this.awbController = new X8CameraAwbItemController(paramView, cameraManager);
            this.optionValue.setVisibility(0);
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
                this.seekBar.setProgress(Integer.valueOf(this.contrast).intValue());
                this.optionValue.setText(this.contrast);
            }
            if (this.saturation != null) {
                this.seekBar.setProgress(Integer.valueOf(this.saturation).intValue());
                this.optionValue.setText(this.saturation);
            }
        }
        this.optionValue.setVisibility(0);
        this.styleView.setVisibility(0);
        this.resetBtn.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.viewHolder.SubParamsViewHolder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SubParamsViewHolder.this.listener != null) {
                    SubParamsViewHolder.this.listener.setRecyclerScroller(true);
                    if (!SubParamsViewHolder.this.paramKey.equals(SubParamsViewHolder.this.context.getResources().getString(R.string.x8_camera_saturation))) {
                        if (SubParamsViewHolder.this.paramKey.equals(SubParamsViewHolder.this.context.getResources().getString(R.string.x8_camera_contrast))) {
                            SubParamsViewHolder.this.listener.styleParam("contrast", 64);
                            SubParamsViewHolder.this.optionValue.setText(String.valueOf(64));
                            SubParamsViewHolder.this.seekBar.setProgress(64);
                            return;
                        }
                        return;
                    }
                    SubParamsViewHolder.this.listener.styleParam("saturation", 64);
                    SubParamsViewHolder.this.seekBar.setProgress(64);
                    SubParamsViewHolder.this.optionValue.setText(String.valueOf(64));
                }
            }
        });
    }

    public void initSharpViewStub() {
        if (this.sharpView == null) {
            this.sharpView = this.sharpViewStub.inflate();
        }
        this.sharpView.setVisibility(0);
        this.normalTv = (TextView) this.sharpView.findViewById(R.id.normal_v);
        this.sharpTv = (TextView) this.sharpView.findViewById(R.id.sharp_v);
    }

    @Override // com.fimi.kernel.dataparser.usb.JsonUiCallBackListener
    public void onComplete(JSONObject rt, Object o) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
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

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.fromUser = false;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
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

    private boolean isSubOptionsSetTextColor(String titleName) {
        if (titleName.equals(this.context.getResources().getString(R.string.x8_camera_style))) {
            return false;
        }
        return true;
    }

    public RelativeLayout getContentLayout() {
        return this.contentLayout;
    }

    public boolean isRecordingUnclickableItem(String titleName) {
        if (CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.recording) {
            if (titleName.equals(this.context.getResources().getString(R.string.x8_record_mode)) || titleName.equals(this.context.getResources().getString(R.string.x8_video_type)) || titleName.equals(this.context.getResources().getString(R.string.x8_video_resolution)) || titleName.equals(this.context.getResources().getString(R.string.x8_record_quality))) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isDelayedPhotographyUnclickableItem(String titleName) {
        if (StateManager.getInstance().getCamera().isDelayedPhotography()) {
            if (titleName.equals(this.context.getResources().getString(R.string.x8_photo_mode)) || titleName.equals(this.context.getResources().getString(R.string.x8_photo_size)) || titleName.equals(this.context.getResources().getString(R.string.x8_photo_format)) || titleName.equals(this.context.getResources().getString(R.string.x8_camera_awb)) || titleName.equals(this.context.getResources().getString(R.string.x8_camera_digita)) || titleName.equals(this.context.getResources().getString(R.string.x8_camera_style))) {
                return true;
            }
            return false;
        }
        return false;
    }
}
