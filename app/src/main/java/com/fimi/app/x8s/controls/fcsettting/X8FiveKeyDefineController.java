package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.FiveKeyDefineAdapter;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8FiveKeyDefineListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.controller.FcCtrlManager;


public class X8FiveKeyDefineController extends AbsX8MenuBoxControllers implements View.OnClickListener {
    private FiveKeyDefineAdapter adapter;
    private int curPosition;
    private FcCtrlManager fcCtrlManager;
    private ImageView imgReturn;
    private int key;
    private IX8FiveKeyDefineListener listener;
    private Context mContext;
    private RecyclerView recyclerView;
    private TextView tvTitle;

    public X8FiveKeyDefineController(View rootView) {
        super(rootView);
    }

    @Override
    public void initViews(View rootView) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.contentView = inflater.inflate(R.layout.x8_main_rc_item_five_key_define, (ViewGroup) rootView, true);
        this.mContext = this.contentView.getContext();
        this.imgReturn = this.contentView.findViewById(R.id.img_return);
        this.tvTitle = this.contentView.findViewById(R.id.tv_title);
        this.recyclerView = this.contentView.findViewById(R.id.recycle_five_key);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.mContext, 3);
        this.recyclerView.setLayoutManager(layoutManager);
        this.adapter = new FiveKeyDefineAdapter(this.mContext, X8RcItemController.FIVE_KEY_DATA_ARRAY);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setItemAnimator(null);
        this.adapter.setOnItemClickListener(new FiveKeyDefineAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                X8FiveKeyDefineController.this.adapter.setItemSelect(index);
                if (X8FiveKeyDefineController.this.listener != null) {
                    if (X8FiveKeyDefineController.this.key != 0) {
                        if (X8FiveKeyDefineController.this.key != 1) {
                            if (X8FiveKeyDefineController.this.key != 2) {
                                if (X8FiveKeyDefineController.this.key != 3) {
                                    if (X8FiveKeyDefineController.this.key == 4) {
                                        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_CENTRE_KEY, index);
                                    }
                                } else {
                                    SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_RIGHT_KEY, index);
                                }
                            } else {
                                SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_LEFT_KEY, index);
                            }
                        } else {
                            SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_DOWN_KEY, index);
                        }
                    } else {
                        SPStoreManager.getInstance().saveInt(Constants.FIVE_KEY_UP_KEY, index);
                    }
                    X8FiveKeyDefineController.this.listener.onSelected(X8FiveKeyDefineController.this.key, index);
                }
            }
        });
        initActions();
    }

    @Override
    public void initActions() {
        if (this.contentView != null) {
            this.imgReturn.setOnClickListener(this);
        }
    }

    @Override
    public void onDroneConnected(boolean b) {
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_return) {
            closeItem();
            if (this.listener != null) {
                this.listener.onBack();
            }
        }
    }

    @Override
    public void showItem() {
        this.isShow = true;
        this.contentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeItem() {
        this.isShow = false;
        this.contentView.setVisibility(View.GONE);
        defaultVal();
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setCalibrationListener(IX8FiveKeyDefineListener listener) {
        this.listener = listener;
    }

    public void setCurIndex(int key, int curPosition) {
        this.key = key;
        this.curPosition = curPosition;
        initViewData();
    }

    private void initViewData() {
        switch (this.key) {
            case 0:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_up));
                break;
            case 1:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_down));
                break;
            case 2:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_left));
                break;
            case 3:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_right));
                break;
            case 4:
                this.tvTitle.setText(this.mContext.getString(R.string.x8_rc_setting_five_key_center));
                break;
        }
        if (this.adapter != null) {
            this.adapter.setItemSelect(this.curPosition);
        }
    }
}
