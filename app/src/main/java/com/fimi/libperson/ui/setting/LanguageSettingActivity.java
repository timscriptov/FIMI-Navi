package com.fimi.libperson.ui.setting;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.fimi.android.app.R;
import com.fimi.kernel.Constants;
import com.fimi.kernel.GlobalConfig;
import com.fimi.kernel.language.LanguageItem;
import com.fimi.kernel.language.LanguageModel;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.LanguageUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.adapter.LanguageAdapter;
import com.fimi.libperson.widget.TitleView;
import com.fimi.widget.NetworkDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class LanguageSettingActivity extends BasePersonActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    int currPosition;
    NetworkDialog mNetworkDialog;
    private LanguageAdapter adapter;
    private List<LanguageItem> languageItems;
    private Handler mHandler = new Handler() { // from class: com.fimi.libperson.ui.setting.LanguageSettingActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0 && LanguageSettingActivity.this.mNetworkDialog != null) {
                Intent intent = new Intent(Constants.CHANGELANGUGE);
                LocalBroadcastManager.getInstance(LanguageSettingActivity.this.mContext).sendBroadcast(intent);
                LanguageSettingActivity.this.restart();
            }
        }
    };
    private ListView mListView;
    private TitleView mTitleView;
    private LanguageModel selectedLanguageModel;

    @Override // com.fimi.libperson.BasePersonActivity, com.fimi.kernel.base.BaseActivity
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    private void initView() {
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mListView = (ListView) findViewById(R.id.lv_l_setting_setting);
        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) this.mTitleView.getLayoutParams();
        params.setMargins(0, this.statusBarHeight + this.marginStatus, 0, 0);
        this.mTitleView.setLayoutParams(params);
        this.mTitleView.setTvTitle(getString(R.string.language_setting_title));
        this.mNetworkDialog = new NetworkDialog(this.mContext, R.style.network_load_progress_dialog, true);
        this.mNetworkDialog.setCanceledOnTouchOutside(false);
        this.mNetworkDialog.setCancelable(false);
    }

    private void initListener() {
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void initData() {
        initView();
        initListener();
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void doTrans() {
        this.selectedLanguageModel = (LanguageModel) SPStoreManager.getInstance().getObject(Constants.LANGUAGETYPE, LanguageModel.class);
        if (this.selectedLanguageModel == null) {
            this.selectedLanguageModel = LanguageUtil.getLanguageModel(Locale.getDefault());
        }
        this.languageItems = new ArrayList();
        for (int i = 0; i < LanguageItem.languageModels.length; i++) {
            LanguageItem item = new LanguageItem();
            LanguageModel model = LanguageItem.languageModels[i];
            item.setInfo(model.getLanguageName());
            item.setCode(model.getLanguageCode());
            if (model.getLanguageCode().equals(this.selectedLanguageModel.getLanguageCode()) && model.getCountry().equals(this.selectedLanguageModel.getCountry())) {
                item.setSelect(true);
                this.currPosition = i;
            }
            this.languageItems.add(item);
        }
        this.adapter = new LanguageAdapter(this.languageItems, this);
        this.mListView.setAdapter((ListAdapter) this.adapter);
        this.mListView.setOnItemClickListener(this);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    protected int getContentViewLayoutID() {
        return R.layout.activity_user_language_setting;
    }

    @RequiresApi(api = 24)
    public void onSelectLanguage(int choseType) {
        if (choseType != this.currPosition) {
            this.currPosition = choseType;
            this.mNetworkDialog.show();
            this.selectedLanguageModel = LanguageItem.languageModels[choseType];
            LanguageUtil.changeAppLanguage(this.mContext, this.selectedLanguageModel.getLocale());
            GlobalConfig.getInstance().setLanguageModel(this.selectedLanguageModel);
            SPStoreManager.getInstance().saveObject(Constants.LANGUAGETYPE, this.selectedLanguageModel);
            this.mHandler.sendEmptyMessageDelayed(0, 2000L);
        }
    }

    public void restart() {
        finish();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    @RequiresApi(api = 24)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onSelectLanguage(position);
    }

    @Override
    // com.fimi.libperson.BasePersonActivity, com.fimi.kernel.base.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.mNetworkDialog != null && this.mNetworkDialog.isShowing()) {
            this.mNetworkDialog.dismiss();
        }
        this.mHandler.removeCallbacksAndMessages(null);
    }
}
