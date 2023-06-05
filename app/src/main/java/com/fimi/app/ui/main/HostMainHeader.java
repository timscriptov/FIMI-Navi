package com.fimi.app.ui.main;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;
import com.fimi.app.presenter.HostMainPresenter;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.x8sdk.common.Constants;

import router.Router;

public class HostMainHeader extends FrameLayout {
    private final Context mContext;
    ImageButton ibtnFeedback;
    ImageButton ibtnMore;
    TextView tvDeviceName;
    private HostMainPresenter presenter;

    public HostMainHeader(@NonNull final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_host_main_header, this);
        this.ibtnMore = findViewById(R.id.ibtn_more);
        this.ibtnFeedback = findViewById(R.id.ibtn_feedback);
        this.tvDeviceName = findViewById(R.id.tv_device_name);
        FontUtil.changeFontLanTing(context.getAssets(), this.tvDeviceName);
        this.ibtnMore.setOnClickListener(v -> {
            ((HostNewMainActivity) context).stopAnim();
            Intent it = Router.invoke(context, "activity://person.setting");
            ((HostNewMainActivity) context).startActivityForResult(it, Constants.A12_TCP_CMD_PORT);
        });
        this.ibtnFeedback.setOnClickListener(v -> {
            HostMainHeader.this.presenter.gotoTeacher("activity://gh2.teacher");
        });
    }

    public void setDeviceName(int resid) {
        this.tvDeviceName.setText(resid);
    }

    public void setPositon(int positon) {
        if (com.fimi.kernel.Constants.productType == ProductEnum.GH2) {
            this.ibtnFeedback.setImageResource(R.drawable.teacher_btn_selector);
            this.ibtnFeedback.setVisibility(View.VISIBLE);
            return;
        }
        this.ibtnFeedback.setVisibility(View.GONE);
    }

    public void setPresenter(HostMainPresenter presenter) {
        this.presenter = presenter;
    }
}
