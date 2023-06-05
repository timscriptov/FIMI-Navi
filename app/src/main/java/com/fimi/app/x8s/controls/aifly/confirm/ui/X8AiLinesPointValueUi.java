package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.X8AiLinePointValueAdapter;
import com.fimi.app.x8s.adapter.decoration.DividerGridItemDecoration;
import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.controls.aifly.X8AiLineExcuteController;
import com.fimi.app.x8s.entity.X8AiLinePointEntity;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.app.x8s.widget.X8TabHost;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.util.UnityUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class X8AiLinesPointValueUi implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    int count = 0;
    private X8AiLinePointValueAdapter adapter;
    private Button btnAll;
    private Button btnOk;
    private View contentView;
    private X8AiLineExcuteController controller;
    private ImageView img4xSlow;
    private ImageView img5sPhoto;
    private ImageView imgHover;
    private ImageView imgNa;
    private ImageView imgOnePhoto;
    private ImageView imgRecord;
    private ImageView imgThreePhoto;
    private boolean isShowMetric;
    private List<X8AiLinePointEntity> mEntityList;
    private RecyclerView mRecyclerView;
    private MapPointLatLng mapPointLatLng;
    private X8MapVideoController mapVideoController;
    private View minus;
    private int mode;
    private View plus;
    private TextView pos;
    private SeekBar sbValue;
    private String suffix;
    private X8TabHost tabRorate;
    private TextView tvBindPoint;
    private TextView tvDvOrientation;
    private TextView tvGbOrientation;
    private TextView tvHeight;
    private View v4xSlow;
    private View v5sPhoto;
    private View vHover;
    private View vNa;
    private View vOnePhoto;
    private View vRecord;
    private View vThreePhoto;
    private View[] arraysView = new View[7];
    private int index = 0;
    private int MAX = 120;
    private int MIN = 5;
    private int SB_MAX = this.MAX - this.MIN;

    public X8AiLinesPointValueUi(Activity activity, View rootView, int mode, MapPointLatLng mpl, X8MapVideoController mapVideoController, X8AiLineExcuteController controller) {
        this.mode = mode;
        this.mapVideoController = mapVideoController;
        this.mapPointLatLng = mpl;
        this.controller = controller;
        if (mode == 0) {
            if (mpl.isIntertestPoint) {
                this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_line_point_interest_layout, (ViewGroup) rootView, true);
            } else {
                this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_line_point_value_layout, (ViewGroup) rootView, true);
            }
        } else if (mode == 3) {
            if (mpl.isIntertestPoint) {
                this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_line_point_interest_layout, (ViewGroup) rootView, true);
            } else {
                this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_line_curve_value_layout, (ViewGroup) rootView, true);
            }
        } else {
            this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_line_point_value_2fpv_layout, (ViewGroup) rootView, true);
        }
        this.isShowMetric = GlobalConfig.getInstance().isShowmMtric();
        this.suffix = this.isShowMetric ? "M" : "FT";
        initView(this.contentView, mpl.isIntertestPoint);
        initAction(mpl.isIntertestPoint);
        setMapPointLatLng(mpl);
    }

    private void initAction(boolean isIntertestPoint) {
        if (!isIntertestPoint) {
            this.vNa.setOnClickListener(this);
            this.vHover.setOnClickListener(this);
            this.vRecord.setOnClickListener(this);
            this.v4xSlow.setOnClickListener(this);
            this.vOnePhoto.setOnClickListener(this);
            this.v5sPhoto.setOnClickListener(this);
            this.vThreePhoto.setOnClickListener(this);
        }
        this.minus.setOnClickListener(this);
        this.plus.setOnClickListener(this);
        this.sbValue.setOnSeekBarChangeListener(this);
        this.btnOk.setOnClickListener(this);
    }

    private void initView(View view, boolean isIntertestPoint) {
        this.btnOk = (Button) view.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.pos = (TextView) view.findViewById(R.id.tv_ai_follow_pos);
        this.tvHeight = (TextView) view.findViewById(R.id.tv_height);
        this.minus = view.findViewById(R.id.rl_minus);
        this.plus = view.findViewById(R.id.rl_plus);
        this.sbValue = (SeekBar) view.findViewById(R.id.sb_value);
        this.sbValue.setMax(this.SB_MAX);
        if (!isIntertestPoint) {
            this.vNa = view.findViewById(R.id.rl_ai_item1);
            this.imgNa = (ImageView) view.findViewById(R.id.img_ai_line_action_na);
            int i = 0 + 1;
            this.arraysView[0] = this.imgNa;
            this.vHover = view.findViewById(R.id.rl_ai_item2);
            this.imgHover = (ImageView) view.findViewById(R.id.img_ai_line_action_hover);
            int i2 = i + 1;
            this.arraysView[i] = this.imgHover;
            this.vRecord = view.findViewById(R.id.rl_ai_item3);
            this.imgRecord = (ImageView) view.findViewById(R.id.img_ai_line_action_record);
            int i3 = i2 + 1;
            this.arraysView[i2] = this.imgRecord;
            this.v4xSlow = view.findViewById(R.id.rl_ai_item4);
            this.img4xSlow = (ImageView) view.findViewById(R.id.img_ai_lind_action_4xslow);
            int i4 = i3 + 1;
            this.arraysView[i3] = this.img4xSlow;
            this.vOnePhoto = view.findViewById(R.id.rl_ai_item5);
            this.imgOnePhoto = (ImageView) view.findViewById(R.id.img_ai_lind_action_one_photo);
            int i5 = i4 + 1;
            this.arraysView[i4] = this.imgOnePhoto;
            this.v5sPhoto = view.findViewById(R.id.rl_ai_item6);
            this.img5sPhoto = (ImageView) view.findViewById(R.id.img_ai_lind_action_5s_photo);
            int i6 = i5 + 1;
            this.arraysView[i5] = this.img5sPhoto;
            this.vThreePhoto = view.findViewById(R.id.rl_ai_item7);
            this.imgThreePhoto = (ImageView) view.findViewById(R.id.img_ai_lind_three_photo);
            int i7 = i6 + 1;
            this.arraysView[i6] = this.imgThreePhoto;
            if (this.mode == 1) {
                this.tvDvOrientation = (TextView) view.findViewById(R.id.tv_ai_line_dv_orientation_value);
                this.tvGbOrientation = (TextView) view.findViewById(R.id.tv_ai_line_gb_orientation_value);
                this.tabRorate = (X8TabHost) view.findViewById(R.id.x8_ai_line_rorate);
                this.tabRorate.setSelect(this.mapPointLatLng.roration);
                this.tabRorate.setOnSelectListener(new X8TabHost.OnSelectListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLinesPointValueUi.1
                    @Override // com.fimi.app.x8s.widget.X8TabHost.OnSelectListener
                    public void onSelect(int index, String text, int last) {
                        if (index != last) {
                            X8AiLinesPointValueUi.this.mapPointLatLng.roration = index;
                            X8AiLinesPointValueUi.this.mapVideoController.getFimiMap().getAiLineManager().updateSmallMarkerAngle(X8AiLinesPointValueUi.this.mapPointLatLng);
                        }
                    }
                });
                if (this.controller.getOration() == 1) {
                    view.findViewById(R.id.rl_rorate).setVisibility(0);
                    return;
                } else {
                    view.findViewById(R.id.rl_rorate).setVisibility(View.GONE);
                    return;
                }
            } else if (this.mode == 3) {
                this.tvBindPoint = (TextView) view.findViewById(R.id.x8_ai_line_bind_point);
                this.mRecyclerView = (RecyclerView) view.findViewById(R.id.ryv_ai_line_point);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.contentView.getContext(), 5);
                this.mRecyclerView.setLayoutManager(layoutManager);
                this.mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this.contentView.getContext()));
                ((SimpleItemAnimator) this.mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                lablesPointEvent();
                return;
            } else {
                this.tvBindPoint = (TextView) view.findViewById(R.id.x8_ai_line_bind_point);
                this.mRecyclerView = (RecyclerView) view.findViewById(R.id.ryv_ai_line_point);
                RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(this.contentView.getContext(), 5);
                this.mRecyclerView.setLayoutManager(layoutManager2);
                this.mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this.contentView.getContext()));
                ((SimpleItemAnimator) this.mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                this.tabRorate = (X8TabHost) view.findViewById(R.id.x8_ai_line_rorate);
                this.tabRorate.setSelect(this.mapPointLatLng.roration);
                this.tabRorate.setOnSelectListener(new X8TabHost.OnSelectListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLinesPointValueUi.2
                    @Override // com.fimi.app.x8s.widget.X8TabHost.OnSelectListener
                    public void onSelect(int index, String text, int last) {
                    }
                });
                lablesPointEvent();
                return;
            }
        }
        this.btnAll = (Button) view.findViewById(R.id.btn_x8_ai_line_bind_point);
        this.btnAll.setOnClickListener(this);
        this.tvBindPoint = (TextView) view.findViewById(R.id.x8_ai_line_bind_point);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.ryv_ai_line_point);
        RecyclerView.LayoutManager layoutManager3 = new GridLayoutManager(this.contentView.getContext(), 5);
        this.mRecyclerView.setLayoutManager(layoutManager3);
        this.mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this.contentView.getContext()));
        ((SimpleItemAnimator) this.mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        ((TextView) view.findViewById(R.id.tv_ai_follow_title)).setText(view.getContext().getString(R.string.x8_ai_fly_lines_interest_point_title) + this.mapPointLatLng.nPos);
        lablesInterestEvent();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_ai_item1) {
            this.index = 0;
            setSelect(this.index);
        } else if (id == R.id.rl_ai_item2) {
            this.index = 1;
            setSelect(this.index);
        } else if (id == R.id.rl_ai_item3) {
            this.index = 2;
            setSelect(this.index);
        } else if (id == R.id.rl_ai_item4) {
            this.index = 3;
            setSelect(this.index);
        } else if (id == R.id.rl_ai_item5) {
            this.index = 4;
            setSelect(this.index);
        } else if (id == R.id.rl_ai_item6) {
            this.index = 5;
            setSelect(this.index);
        } else if (id == R.id.rl_ai_item7) {
            this.index = 6;
            setSelect(this.index);
        } else if (id == R.id.rl_minus) {
            if (this.sbValue.getProgress() != 0) {
                this.sbValue.setProgress(this.sbValue.getProgress() - 1);
            }
        } else if (id == R.id.rl_plus) {
            if (this.sbValue.getProgress() != this.SB_MAX) {
                this.sbValue.setProgress(this.sbValue.getProgress() + 1);
            }
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            if (this.mapPointLatLng.isIntertestPoint) {
                saveInterestBindPoint();
            } else {
                if (this.mode == 0) {
                    savePointBindInterest();
                }
                if (this.mode == 3) {
                    this.mapPointLatLng.roration = 0;
                } else {
                    this.mapPointLatLng.roration = this.tabRorate.getSelectIndex();
                }
            }
            this.mapPointLatLng.altitude = this.sbValue.getProgress() + this.MIN;
            if (this.mode == 3) {
                this.mapPointLatLng.action = -1;
            } else {
                this.mapPointLatLng.action = this.index;
            }
            this.mapPointLatLng.isActionSave = true;
            this.controller.onChangeMarkerAltitude(this.mapPointLatLng.altitude);
            this.controller.closeNextUi(true);
        } else if (id == R.id.btn_x8_ai_line_bind_point) {
            if (this.adapter.isAll()) {
                onSelectAll(false);
            } else {
                onSelectAll(true);
            }
        }
    }

    public void onSelectAll(boolean isAll) {
        int state = isAll ? 1 : 0;
        for (int i = 0; i < this.mEntityList.size(); i++) {
            if (this.mEntityList.get(i).getState() != 2) {
                this.mEntityList.get(i).setState(state);
            }
        }
        if (isAll) {
            this.btnAll.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_point_bind_interest_unselect));
            this.adapter.setAll(true);
        } else {
            this.btnAll.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_point_bind_interest_select));
            this.adapter.setAll(false);
        }
        this.adapter.notifyDataSetChanged();
    }

    public void setSelect(int index) {
        for (int i = 0; i < this.arraysView.length; i++) {
            if (index == i) {
                this.arraysView[i].setSelected(true);
            } else {
                this.arraysView[i].setSelected(false);
            }
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.tvHeight.setText(getProgressString(this.MIN + progress));
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public String getProgressString(int progress) {
        String v = NumberUtil.decimalPointStr(GlobalConfig.getInstance().isShowmMtric() ? progress : UnityUtil.meterToFoot(progress), 1);
        return v + this.suffix;
    }

    public void setMapPointLatLng(MapPointLatLng mapPointLatLng) {
        this.tvHeight.setText(getProgressString((int) mapPointLatLng.altitude));
        this.sbValue.setProgress(((int) mapPointLatLng.altitude) - this.MIN);
        if (!mapPointLatLng.isActionSave) {
            if (this.mode == 3) {
                this.btnOk.setText(this.contentView.getContext().getString(R.string.x8_setting_fc_loastaction_tips_content_confirm));
            } else {
                this.btnOk.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_lines_point_action_add));
            }
        } else {
            this.btnOk.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_lines_point_action_update));
        }
        if (!mapPointLatLng.isIntertestPoint) {
            this.pos.setText("" + mapPointLatLng.nPos);
            this.index = mapPointLatLng.action;
            setSelect(this.index);
            if (this.mode == 1) {
                this.tvDvOrientation.setText("" + mapPointLatLng.angle + "°");
                int pitchAngle = StateManager.getInstance().getGimbalState().getPitchAngle();
                mapPointLatLng.gimbalPitch = pitchAngle;
                double angle = pitchAngle / 100.0d;
                String angleStr = NumberUtil.decimalPointStr(angle, 1) + "°";
                this.tvGbOrientation.setText(angleStr);
            }
        }
    }

    public void lablesInterestEvent() {
        List<MapPointLatLng> list = this.mapVideoController.getFimiMap().getAiLineManager().getMapPointList();
        if (list.size() == 0) {
            this.tvBindPoint.setVisibility(8);
            this.btnAll.setVisibility(8);
            return;
        }
        this.mEntityList = new ArrayList();
        this.count = list.size();
        int selectCount = 0;
        for (int i = 0; i < list.size(); i++) {
            X8AiLinePointEntity entity = new X8AiLinePointEntity();
            MapPointLatLng temp = list.get(i);
            if (temp.mInrertestPoint != null) {
                if (list.get(i).mInrertestPoint == this.mapPointLatLng) {
                    entity.setState(1);
                    selectCount++;
                } else {
                    entity.setState(2);
                    this.count--;
                }
            } else {
                entity.setState(0);
            }
            entity.setnPos(temp.nPos);
            this.mEntityList.add(entity);
        }
        this.adapter = new X8AiLinePointValueAdapter(this.contentView.getContext(), this.mEntityList, 1);
        this.adapter.setOnItemClickListener(new X8AiLinePointValueAdapter.OnItemClickListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLinesPointValueUi.3
            @Override // com.fimi.app.x8s.adapter.X8AiLinePointValueAdapter.OnItemClickListener
            public void onItemClicked(int index, int last, boolean isSelect) {
                if (isSelect) {
                    ((X8AiLinePointEntity) X8AiLinesPointValueUi.this.mEntityList.get(index)).setState(1);
                } else {
                    ((X8AiLinePointEntity) X8AiLinesPointValueUi.this.mEntityList.get(index)).setState(0);
                }
                X8AiLinesPointValueUi.this.adapter.notifyItemChanged(index);
                int selectCount2 = 0;
                for (int i2 = 0; i2 < X8AiLinesPointValueUi.this.mEntityList.size(); i2++) {
                    if (((X8AiLinePointEntity) X8AiLinesPointValueUi.this.mEntityList.get(i2)).getState() == 1) {
                        selectCount2++;
                    }
                }
                if (X8AiLinesPointValueUi.this.count == selectCount2) {
                    X8AiLinesPointValueUi.this.btnAll.setText(X8AiLinesPointValueUi.this.contentView.getContext().getString(R.string.x8_ai_fly_point_bind_interest_unselect));
                    X8AiLinesPointValueUi.this.adapter.setAll(true);
                    return;
                }
                X8AiLinesPointValueUi.this.btnAll.setText(X8AiLinesPointValueUi.this.contentView.getContext().getString(R.string.x8_ai_fly_point_bind_interest_select));
                X8AiLinesPointValueUi.this.adapter.setAll(false);
            }
        });
        if (this.count == selectCount) {
            this.btnAll.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_point_bind_interest_unselect));
            this.adapter.setAll(true);
        } else {
            this.btnAll.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_point_bind_interest_select));
            this.adapter.setAll(false);
        }
        this.mRecyclerView.setAdapter(this.adapter);
    }

    public void saveInterestBindPoint() {
        if (this.mEntityList != null && this.mEntityList.size() > 0) {
            int i = 0;
            for (X8AiLinePointEntity entity : this.mEntityList) {
                if (entity.getState() != 2) {
                    if (entity.getState() == 0) {
                        this.mapVideoController.getFimiMap().getAiLineManager().updateInterestBindPoint(null, i);
                    } else if (entity.getState() == 1) {
                        this.mapVideoController.getFimiMap().getAiLineManager().updateInterestBindPoint(this.mapPointLatLng, i);
                    }
                }
                i++;
            }
            this.mapVideoController.getFimiMap().getAiLineManager().addSmallMarkerByInterest();
            this.mapVideoController.getFimiMap().getAiLineManager().updateInterestPoint();
        }
    }

    public void lablesPointEvent() {
        List<MapPointLatLng> list = this.mapVideoController.getFimiMap().getAiLineManager().getInterestPointList();
        this.mEntityList = new ArrayList();
        if (list.size() == 0) {
            this.tvBindPoint.setVisibility(8);
            return;
        }
        int selectIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            X8AiLinePointEntity entity = new X8AiLinePointEntity();
            MapPointLatLng temp = list.get(i);
            if (this.mapPointLatLng.mInrertestPoint == null) {
                entity.setState(0);
            } else if (this.mapPointLatLng.mInrertestPoint == temp) {
                selectIndex = i;
                entity.setState(1);
            } else {
                entity.setState(0);
            }
            entity.setnPos(temp.nPos);
            this.mEntityList.add(entity);
        }
        this.adapter = new X8AiLinePointValueAdapter(this.contentView.getContext(), this.mEntityList, 0);
        if (selectIndex != -1) {
            this.adapter.setSelectIndex(selectIndex);
        }
        this.adapter.setOnItemClickListener(new X8AiLinePointValueAdapter.OnItemClickListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLinesPointValueUi.4
            @Override // com.fimi.app.x8s.adapter.X8AiLinePointValueAdapter.OnItemClickListener
            public void onItemClicked(int index, int last, boolean isSelect) {
                if (isSelect) {
                    ((X8AiLinePointEntity) X8AiLinesPointValueUi.this.mEntityList.get(index)).setState(1);
                    if (last != -1) {
                        ((X8AiLinePointEntity) X8AiLinesPointValueUi.this.mEntityList.get(last)).setState(0);
                        X8AiLinesPointValueUi.this.adapter.notifyItemChanged(last);
                    }
                    X8AiLinesPointValueUi.this.adapter.notifyItemChanged(index);
                    return;
                }
                ((X8AiLinePointEntity) X8AiLinesPointValueUi.this.mEntityList.get(index)).setState(0);
                X8AiLinesPointValueUi.this.adapter.notifyItemChanged(index);
            }
        });
        this.mRecyclerView.setAdapter(this.adapter);
    }

    public void savePointBindInterest() {
        if (this.mEntityList.size() > 0) {
            int nPos = -1;
            Iterator<X8AiLinePointEntity> it = this.mEntityList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                X8AiLinePointEntity entity = it.next();
                if (entity.getState() == 1) {
                    nPos = entity.getnPos();
                    break;
                }
            }
            if (nPos == -1) {
                if (this.mapPointLatLng.mInrertestPoint != null) {
                    this.mapVideoController.getFimiMap().getAiLineManager().notityChangeView(this.mapPointLatLng, false);
                }
                this.mapPointLatLng.mInrertestPoint = null;
            } else {
                List<MapPointLatLng> list = this.mapVideoController.getFimiMap().getAiLineManager().getInterestPointList();
                if (list.size() != 0) {
                    if (this.mapPointLatLng.mInrertestPoint != null && this.mapPointLatLng.mInrertestPoint != list.get(nPos - 1)) {
                        this.mapVideoController.getFimiMap().getAiLineManager().notityChangeView(this.mapPointLatLng, false);
                    }
                    this.mapPointLatLng.mInrertestPoint = list.get(nPos - 1);
                    this.mapPointLatLng.setAngle(this.mapVideoController.getFimiMap().getAiLineManager().getPointAngle(this.mapPointLatLng, this.mapPointLatLng.mInrertestPoint));
                } else {
                    return;
                }
            }
            this.mapVideoController.getFimiMap().getAiLineManager().notityChangeView(this.mapPointLatLng);
            this.mapVideoController.getFimiMap().getAiLineManager().addSmallMarkerByInterest();
        }
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        if (isInSky && isLowPower) {
            this.btnOk.setEnabled(true);
        } else {
            this.btnOk.setEnabled(false);
        }
    }
}
