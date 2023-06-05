package com.fimi.libperson.ui.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fimi.android.app.R;
import com.fimi.apk.DownloadApkService;
import com.fimi.apk.iview.IApkVerisonView;
import com.fimi.apk.presenter.ApkVersionPrenster;
import com.fimi.host.HostConstants;
import com.fimi.kernel.Constants;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.FrescoImageLoader;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.adapter.PersettingFourAdapt;
import com.fimi.libperson.adapter.PersettingSecondAdapt;
import com.fimi.libperson.entity.ImageSource;
import com.fimi.libperson.entity.PersonSetting;
import com.fimi.libperson.ui.me.login.LoginActivity;
import com.fimi.libperson.ui.web.UserProtocolWebViewActivity;
import com.fimi.libperson.widget.BitmapLoadTaskInstance;
import com.fimi.libperson.widget.TitleView;
import com.fimi.network.entity.ApkVersionDto;
import com.fimi.widget.DialogManager;

import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

import java.util.ArrayList;
import java.util.List;

public class PersonSettingNewActivity extends BasePersonActivity implements BitmapLoadTaskInstance.OnLoadListener, IApkVerisonView, ApkVersionPrenster.onApkUpdateListerner {
    private ApkVersionPrenster mApkVersionPrenster;
    private BitmapLoadTaskInstance mBitmapLoadTaskInstance;
    private Button mBtnBack;
    private List<PersonSetting> mFourPersonSettings;
    private SimpleDraweeView mIvHead;
    private ListView mLvFirstSetting;
    private ListView mLvFourSetting;
    private ListView mLvSecondSetting;
    private ListView mLvThirdSetting;
    private PersettingFourAdapt mPersettingFourAdapt;
    private PersettingSecondAdapt mPersettingSecondAdapt;
    private List<PersonSetting> mSecondPersonSettings;
    private List<PersonSetting> mThirdPersonSettings;
    private TitleView mTitleView;
    private TextView mTvId;
    private TextView mTvName;
    private RelativeLayout rlLoginNow;
    private RelativeLayout rlPersonTitle;
    private boolean isRequestNewVersion = false;
    private final AdapterView.OnItemClickListener mSecondListerner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PersettingSecondAdapt.State positionIndex = PersonSettingNewActivity.this.mSecondPersonSettings.get(position).getSecondAdapt();
            if (positionIndex == PersettingSecondAdapt.State.LANGUAGE) {
                PersonSettingNewActivity.this.readyGoForResult(LanguageSettingActivity.class, com.fimi.x8sdk.common.Constants.A12_TCP_CMD_PORT);
            } else if (positionIndex == PersettingSecondAdapt.State.SERVICE) {
                PersonSettingNewActivity.this.readyGo(ServiceSettingActivity.class);
            }
        }
    };
    private final AdapterView.OnItemClickListener mFourListerner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PersonSettingNewActivity.this.isRequestNewVersion = true;
            PersettingFourAdapt.State positionIndex = PersonSettingNewActivity.this.mFourPersonSettings.get(position).getFourAdapt();
            if (positionIndex == PersettingFourAdapt.State.ABOUT) {
                PersonSettingNewActivity.this.readyGo(LibPersonAboutActivity.class);
            }
        }
    };

    @Override
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    private void handleUserType(Constants.UserType userType, String fimiId) {
        switch (userType) {
            case Guest:
                this.mIvHead.setImageURI(new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.icon_person_setting_head_unlogin)).build());
                this.mBtnBack.setVisibility(View.GONE);
                this.rlLoginNow.setVisibility(View.VISIBLE);
                return;
            case Register:
                String name = HostConstants.getUserDetail().getNickName();
                TextView textView = this.mTvName;
                if (TextUtils.isEmpty(name)) {
                    name = "";
                }
                textView.setText(name);
                this.mTvId.setText(getString(R.string.person_setting_fimi_id, fimiId));
                FrescoImageLoader.display(this.mIvHead, HostConstants.getUserDetail().getUserImgUrl());
                this.mBtnBack.setVisibility(View.VISIBLE);
                this.rlLoginNow.setVisibility(View.GONE);
                return;
            default:
        }
    }

    public void go2LoginAboutBigImage() {
        this.mBitmapLoadTaskInstance.setOnLoadListener(this);
        this.mBitmapLoadTaskInstance.setImage(ImageSource.asset("login_bg.jpg"), this.mContext);
    }

    @Override
    public void initData() {
        this.mTitleView = findViewById(R.id.title_view);
        this.mTitleView.setTvTitle(getResources().getString(R.string.person_setting_title));
        this.mIvHead = findViewById(R.id.iv_head);
        this.mBtnBack = findViewById(R.id.btn_back);
        this.mTvName = findViewById(R.id.tv_name);
        this.mTvId = findViewById(R.id.tv_id);
        this.rlPersonTitle = findViewById(R.id.rl_person_title);
        this.rlLoginNow = findViewById(R.id.rl_login_now);
        FontUtil.changeFontLanTing(getAssets(), this.mTvName, this.mTvId);
        final String fimiId = HostConstants.getUserDetail().getFimiId();
        handleUserType(TextUtils.isEmpty(fimiId) ? Constants.UserType.Guest : Constants.UserType.Register, fimiId);
        this.rlPersonTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(fimiId)) {
                    PersonSettingNewActivity.this.go2LoginAboutBigImage();
                }
            }
        });
        this.mBitmapLoadTaskInstance = BitmapLoadTaskInstance.getInstance();
        this.mBitmapLoadTaskInstance.setOnLoadListener(this);
        initListView();
        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) this.mTitleView.getLayoutParams();
        params.setMargins(0, this.statusBarHeight + this.marginStatus, 0, 0);
        this.mTitleView.setLayoutParams(params);
        this.mApkVersionPrenster = new ApkVersionPrenster(this, this);
        this.mApkVersionPrenster.getOnlineNewApkFileInfo();
        this.mApkVersionPrenster.setOnApkUpdateListerner(this);
    }

    private void initListView() {
        PersettingSecondAdapt.State[] values;
        PersettingFourAdapt.State[] values2;
        this.mLvSecondSetting = findViewById(R.id.lv_second_setting);
        this.mPersettingSecondAdapt = new PersettingSecondAdapt(this);
        this.mSecondPersonSettings = new ArrayList();
        for (PersettingSecondAdapt.State state : PersettingSecondAdapt.State.values()) {
            if (1 != 0) {
                PersonSetting setting = new PersonSetting();
                setting.setIsOPen(true);
                setting.setSecondAdapt(state);
                this.mSecondPersonSettings.add(setting);
            }
        }
        this.mPersettingSecondAdapt.setData(this.mSecondPersonSettings);
        this.mLvSecondSetting.setAdapter(this.mPersettingSecondAdapt);
        this.mLvSecondSetting.setOnItemClickListener(this.mSecondListerner);
        this.mLvFourSetting = findViewById(R.id.lv_four_setting);
        this.mPersettingFourAdapt = new PersettingFourAdapt(this);
        this.mFourPersonSettings = new ArrayList();
        for (PersettingFourAdapt.State state2 : PersettingFourAdapt.State.values()) {
            if (1 != 0) {
                PersonSetting setting2 = new PersonSetting();
                setting2.setIsOPen(false);
                setting2.setFourAdapt(state2);
                this.mFourPersonSettings.add(setting2);
            }
        }
        this.mPersettingFourAdapt.setData(this.mFourPersonSettings);
        this.mLvFourSetting.setAdapter(this.mPersettingFourAdapt);
        this.mLvFourSetting.setOnItemClickListener(this.mFourListerner);
    }

    @Override
    public void doTrans() {
        this.mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager dialogManager = new DialogManager(PersonSettingNewActivity.this.mContext, PersonSettingNewActivity.this.getString(R.string.person_setting_dialog_exit_title), PersonSettingNewActivity.this.getString(R.string.person_setting_dialog_exit_message), PersonSettingNewActivity.this.getString(R.string.person_setting_back), PersonSettingNewActivity.this.getString(R.string.person_setting_dialog_exit_left_text));
                dialogManager.setVerticalScreen(true);
                dialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
                    @Override
                    public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                        PersonSettingNewActivity.this.go2LoginAboutBigImage();
                    }

                    @Override
                    public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                    }
                });
                dialogManager.showDialog();
            }
        });
    }

    public void goWebActivity(String url, String title) {
        Intent it = new Intent(this, UserProtocolWebViewActivity.class);
        it.putExtra("web_url", url);
        it.putExtra("web_title", title);
        startActivity(it);
        overridePendingTransition(17432576, 17432577);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_person_new_setting;
    }

    @Override
    public void onComplete() {
        SPStoreManager.getInstance().removeKey(HostConstants.SP_KEY_USER_DETAIL);
        readyGoThenKillAllActivity(LoginActivity.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mBitmapLoadTaskInstance != null) {
            this.mBitmapLoadTaskInstance.setOnLoadListener(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10010) {
            recreate();
        }
    }

    @Override
    public void showNewApkVersionDialog(final ApkVersionDto dto, final String savePath) {
        DialogManager dialogManagerUpdate = new DialogManager(this, getString(R.string.fimi_sdk_app_update_title), dto.getUpdcontents(), getString(R.string.fimi_sdk_update_now), getString(R.string.fimi_sdk_update_ignore), 3);
        dialogManagerUpdate.setOnDiaLogListener(new DialogManager.OnDialogListener() {
            @Override
            public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                Intent updateService = new Intent(PersonSettingNewActivity.this.mContext, DownloadApkService.class);
                updateService.putExtra("down_url", dto.getUrl());
                updateService.putExtra("down_savepath", savePath);
                updateService.setFlags(NTLMConstants.FLAG_NEGOTIATE_128_BIT_ENCRYPTION);
                PersonSettingNewActivity.this.mContext.startService(updateService);
            }

            @Override
            public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
            }
        });
        dialogManagerUpdate.setClickOutIsCancle(true);
        dialogManagerUpdate.showDialog();
    }

    @Override
    public void haveUpdate(boolean isUpdate) {
        if (isUpdate) {
            this.mFourPersonSettings.get(0).setIsOPen(true);
        } else {
            if (this.isRequestNewVersion) {
                this.isRequestNewVersion = false;
                ToastUtil.showToast(this.mContext, getString(R.string.person_setting_check_updated_content), 1);
            }
            this.mFourPersonSettings.get(0).setIsOPen(false);
        }
        this.mPersettingFourAdapt.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == 0) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
