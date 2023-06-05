package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.X8AiLineHistoryAdapter;
import com.fimi.app.x8s.controls.aifly.X8AiLineExcuteController;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.helper.X8AiLinePointInfoHelper;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.map.MapType;

import java.util.List;

/* loaded from: classes.dex */
public class X8AiLinesHistoryUi implements View.OnClickListener {
    private X8AiLineHistoryAdapter adapter;
    private Button btnNext;
    private View contentView;
    private X8AiLineExcuteController controller;
    private ListView lv;

    public X8AiLinesHistoryUi(Activity activity, View parent) {
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_line_history_layout, (ViewGroup) parent, true);
        initView(this.contentView);
        initAction();
    }

    public void initView(View rootView) {
        this.lv = (ListView) rootView.findViewById(R.id.lv);
        this.btnNext = (Button) rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        List<X8AiLinePointInfo> list = X8AiLinePointInfoHelper.getIntance().getLastItem(GlobalConfig.getInstance().getMapType() == MapType.AMap ? 1 : 0);
        this.adapter = new X8AiLineHistoryAdapter(this.contentView.getContext(), list);
        this.lv.setAdapter((ListAdapter) this.adapter);
    }

    public void initAction() {
        this.lv.setOnItemClickListener(this.adapter);
        this.btnNext.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ai_follow_confirm_ok) {
            X8AiLinePointInfo info = this.adapter.getItemSelect();
            if (info == null) {
                X8ToastUtil.showToast(this.contentView.getContext(), "" + this.contentView.getContext().getString(R.string.x8_ai_fly_line_history_select_tip), 0);
            } else {
                this.controller.historyUi2NextUi(info);
            }
        }
    }

    public void setController(X8AiLineExcuteController controller) {
        this.controller = controller;
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        if (isInSky && isLowPower) {
            this.btnNext.setEnabled(true);
        } else {
            this.btnNext.setEnabled(false);
        }
    }
}
