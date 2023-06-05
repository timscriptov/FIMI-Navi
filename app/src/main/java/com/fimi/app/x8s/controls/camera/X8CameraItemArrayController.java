package com.fimi.app.x8s.controls.camera;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.PhotoArrayParamsAdapter;
import com.fimi.app.x8s.entity.X11CmdConstants;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.viewHolder.SubParamItemListener;
import com.fimi.app.x8s.widget.DividerGridItemDecoration;
import com.fimi.kernel.Constants;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;
import com.fimi.x8sdk.modulestate.StateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class X8CameraItemArrayController extends AbsX8Controllers implements JsonUiCallBackListener, PhotoArrayParamsAdapter.PhotoArrayItemClickListener {
    Map<String, String> keyMap;
    List<String> paramList;
    X8CameraParamsValue x8CameraParamsValue;
    private PhotoArrayParamsAdapter arrayParamsAdapter;
    private CameraManager cameraManager;
    private Context context;
    private GridLayoutManager layoutManager;
    private SubParamItemListener listener;
    private String paramKey;
    private String paramValue;
    private List<String> params;
    private RecyclerView recyclerView;
    private RadioGroup x8RgCameraResolution;

    public X8CameraItemArrayController(View rootView, CameraManager manager, String paramType, SubParamItemListener itemListener) {
        super(rootView);
        this.params = new ArrayList();
        this.paramList = new ArrayList();
        this.keyMap = new HashMap();
        this.x8CameraParamsValue = X8CameraParamsValue.getInstance();
        this.cameraManager = manager;
        this.listener = itemListener;
        if (paramType.equals("sharpness")) {
            if (StateManager.getInstance().getCamera().getToken() < 0) {
                String[] styleArray = rootView.getContext().getResources().getStringArray(R.array.x8_style_array);
                for (String style : styleArray) {
                    this.params.add(style);
                }
                this.arrayParamsAdapter.updateData(this.params, null, this.paramKey, 0);
            }
        } else if (paramType.equals(CameraJsonCollection.KEY_RECORD_MODE)) {
            this.x8RgCameraResolution.setVisibility(8);
        } else {
            this.x8RgCameraResolution.setVisibility(8);
        }
        this.paramKey = paramType;
    }

    public void updateParamList(Map<String, String> optionMap, String paramKey, String paramValue) {
        String paramValue2;
        String paramValue3;
        if (optionMap != null && optionMap.size() > 0) {
            this.keyMap = optionMap;
            for (Map.Entry<String, String> entry : optionMap.entrySet()) {
                if (entry.getKey().contains(paramKey)) {
                    this.paramList.add(entry.getKey());
                }
            }
            if (this.paramKey.equals("video_resolution")) {
                if (paramValue != null) {
                    int index = this.paramList.indexOf(paramValue);
                    this.arrayParamsAdapter.updateData(this.paramList, optionMap, this.paramKey, index);
                }
            } else if (this.paramKey.equals("capture_mode")) {
                if (paramValue != null) {
                    if (paramValue.contains(getString(R.string.x8_timelapse_capture_8).split("\\s+")[0])) {
                        this.arrayParamsAdapter.updateData(this.paramList, optionMap, this.paramKey, Constants.panoramaType);
                        return;
                    }
                    if (this.x8CameraParamsValue != null && this.x8CameraParamsValue.getCurParamsJson().getPhoto_timelapse() != null && this.x8CameraParamsValue.getCurParamsJson().getPhoto_timelapse().contains(paramValue)) {
                        paramValue3 = this.x8CameraParamsValue.getCurParamsJson().getPhoto_timelapse();
                    } else {
                        paramValue3 = paramValue + " " + this.x8CameraParamsValue.getCurParamsJson().getPhoto_timelapse();
                    }
                    int index2 = this.paramList.indexOf(paramValue3);
                    this.arrayParamsAdapter.updateData(this.paramList, optionMap, this.paramKey, index2);
                    return;
                }
                this.arrayParamsAdapter.updateData(this.paramList, optionMap, this.paramKey, 0);
            } else if (this.paramKey.equals(CameraJsonCollection.KEY_RECORD_MODE) && paramValue != null) {
                if (this.x8CameraParamsValue != null && this.x8CameraParamsValue.getCurParamsJson().getVideo_timelapse().contains(paramValue)) {
                    paramValue2 = this.x8CameraParamsValue.getCurParamsJson().getVideo_timelapse();
                } else {
                    paramValue2 = paramValue + " " + this.x8CameraParamsValue.getCurParamsJson().getVideo_timelapse();
                }
                int index3 = this.paramList.indexOf(paramValue2);
                this.arrayParamsAdapter.updateData(this.paramList, optionMap, this.paramKey, index3);
            }
        }
    }

    public void setRgCameraResolutionVisibility() {
        this.x8RgCameraResolution.setVisibility(8);
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.x8RgCameraResolution = (RadioGroup) rootView.findViewById(R.id.x8_rg_camera_resolution);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.paramRecycler);
        this.arrayParamsAdapter = new PhotoArrayParamsAdapter(this.context, this.params);
        this.arrayParamsAdapter.setItemClickListener(this);
        this.recyclerView.setAdapter(this.arrayParamsAdapter);
        this.layoutManager = new GridLayoutManager(this.context, 4);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.addItemDecoration(new DividerGridItemDecoration(this.context, 3, 17170445));
        this.recyclerView.setAnimation(null);
        this.recyclerView.setHasFixedSize(true);
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initActions() {
        this.x8RgCameraResolution.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.fimi.app.x8s.controls.camera.X8CameraItemArrayController.1
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String systemType = X8CameraItemArrayController.this.x8CameraParamsValue.getCurParamsJson().getSystem_type();
                if (R.id.x8_rbtn_4k == i) {
                    if (systemType.equals("PAL")) {
                        X8CameraItemArrayController.this.cameraManager.setCameraKeyParams(X11CmdConstants.VALUE_VIDEO_RESOLUTION_2160P_25F_PAL_4K, "video_resolution", X8CameraItemArrayController.this);
                    } else {
                        X8CameraItemArrayController.this.cameraManager.setCameraKeyParams("3840x2160 30P 16:9", "video_resolution", X8CameraItemArrayController.this);
                    }
                } else if (R.id.x8_rbtn_2_5k == i) {
                    if (systemType.equals("PAL")) {
                        X8CameraItemArrayController.this.cameraManager.setCameraKeyParams("2560x1440 25P 16:9", "video_resolution", X8CameraItemArrayController.this);
                    } else {
                        X8CameraItemArrayController.this.cameraManager.setCameraKeyParams(CameraJsonCollection.VALUE_VIDEO_RESOLUTION_1440P_60F_2K, "video_resolution", X8CameraItemArrayController.this);
                    }
                } else if (R.id.x8_rbtn_1080p == i) {
                    if (systemType.equals("PAL")) {
                        X8CameraItemArrayController.this.cameraManager.setCameraKeyParams("1920x1080 25P 16:9", "video_resolution", X8CameraItemArrayController.this);
                    } else {
                        X8CameraItemArrayController.this.cameraManager.setCameraKeyParams("1920x1080 30P 16:9", "video_resolution", X8CameraItemArrayController.this);
                    }
                }
            }
        });
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void defaultVal() {
    }

    @Override // com.fimi.kernel.dataparser.usb.JsonUiCallBackListener
    public void onComplete(JSONObject rt, Object o) {
        CameraParamsJson paramsJson;
        if (rt != null && (paramsJson = (CameraParamsJson) JSON.parseObject(rt.toString(), CameraParamsJson.class)) != null) {
            if (paramsJson.getMsg_id() == 9) {
                if (paramsJson.getParam().equals("sharpness")) {
                    this.params = paramsJson.getOptions();
                    this.arrayParamsAdapter.updateData(this.params, null, this.paramKey, 0);
                }
            } else if (paramsJson.getMsg_id() == 2 && paramsJson.getType() != null && !"".equals(paramsJson.getType())) {
                if (paramsJson.getType().equals("video_resolution") && paramsJson.getRval() >= 0) {
                    if (this.paramValue != null && this.paramList.size() > 0) {
                        int index = this.paramList.indexOf(this.paramValue);
                        this.arrayParamsAdapter.updateData(this.paramList, this.keyMap, this.paramKey, index);
                        this.x8CameraParamsValue.getCurParamsJson().setVideo_resolution(this.paramValue);
                        if (this.listener != null) {
                            this.listener.updateAddContent(this.paramKey, this.paramValue);
                        }
                    }
                } else if (paramsJson.getType().equals(CameraJsonCollection.KEY_PHOTO_TIMELAPSE) && paramsJson.getRval() >= 0) {
                    if (this.paramValue != null && this.paramList.size() > 0) {
                        int index2 = this.paramList.indexOf(this.paramValue);
                        this.arrayParamsAdapter.updateData(this.paramList, this.keyMap, this.paramKey, index2);
                        this.x8CameraParamsValue.getCurParamsJson().setPhoto_timelapse(this.paramValue);
                        if (this.listener != null) {
                            this.listener.updateAddContent(this.paramKey, this.paramValue);
                        }
                    }
                } else if (paramsJson.getType().equals(CameraJsonCollection.KEY_VIDEO_TIMELAPSE) && paramsJson.getRval() >= 0 && this.paramValue != null && this.paramList.size() > 0) {
                    int index3 = this.paramList.indexOf(this.paramValue);
                    this.arrayParamsAdapter.updateData(this.paramList, this.keyMap, this.paramKey, index3);
                    this.x8CameraParamsValue.getCurParamsJson().setPhoto_timelapse(this.paramValue);
                    if (this.listener != null) {
                        this.listener.updateAddContent(this.paramKey, this.paramValue);
                    }
                }
            }
        }
    }

    @Override // com.fimi.app.x8s.adapter.PhotoArrayParamsAdapter.PhotoArrayItemClickListener
    public void onItemClickListener(String paramKey, String paramValue) {
        this.paramValue = paramValue;
        if (paramKey.equalsIgnoreCase("capture_mode")) {
            if (paramValue.contains(getString(R.string.x8_timelapse_capture_8).split("\\s+")[0])) {
                if (paramValue != null && this.paramList.size() > 0) {
                    int index = this.paramList.indexOf(paramValue);
                    this.arrayParamsAdapter.updateData(this.paramList, this.keyMap, this.paramKey, index);
                    Constants.panoramaType = index;
                    return;
                }
                return;
            }
            this.cameraManager.setCameraKeyParams(paramValue.split("\\s+")[1], CameraJsonCollection.KEY_PHOTO_TIMELAPSE, this);
        } else if (paramKey.equalsIgnoreCase(CameraJsonCollection.KEY_RECORD_MODE)) {
            this.cameraManager.setCameraKeyParams(paramValue.split("\\s+")[1], CameraJsonCollection.KEY_VIDEO_TIMELAPSE, this);
        } else {
            this.cameraManager.setCameraKeyParams(paramValue, paramKey, this);
        }
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public boolean onClickBackKey() {
        return false;
    }
}
