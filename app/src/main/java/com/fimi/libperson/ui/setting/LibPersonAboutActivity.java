package com.fimi.libperson.ui.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fimi.android.app.R;
import com.fimi.host.ComonStaticURL;
import com.fimi.host.HostConstants;
import com.fimi.kernel.Constants;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.adapter.PersettingThirdAdapt;
import com.fimi.libperson.adapter.PersonSettingAdapt;
import com.fimi.libperson.entity.ImageSource;
import com.fimi.libperson.entity.PersonSetting;
import com.fimi.libperson.ui.me.login.LoginActivity;
import com.fimi.libperson.ui.web.UserProtocolWebViewActivity;
import com.fimi.libperson.widget.BitmapLoadTaskInstance;
import com.fimi.libperson.widget.TitleView;
import com.fimi.network.ErrorMessage;
import com.fimi.network.UserManager;
import com.fimi.network.entity.NetModel;
import com.fimi.widget.DialogManager;

import java.util.ArrayList;
import java.util.List;


public class LibPersonAboutActivity extends BasePersonActivity implements BitmapLoadTaskInstance.OnLoadListener {
    BitmapLoadTaskInstance mBitmapLoadTaskInstance;
    boolean isLogin = false;
    private Button libpersonBtnRepeal;
    private TextView libpersonTvVersions;
    private ListView mLvMainSetting;
    private PersettingThirdAdapt mPersettingThirdAdapt;
    private List<PersonSetting> mThirdPersonSettings;
    private final AdapterView.OnItemClickListener mThirdListerner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PersettingThirdAdapt.State positionIndex = LibPersonAboutActivity.this.mThirdPersonSettings.get(position).getThirdAdapt();
            if (positionIndex == PersettingThirdAdapt.State.USER_PRIVACY) {
                LibPersonAboutActivity.this.goWebActivity(ComonStaticURL.getPrivacyUrl(), LibPersonAboutActivity.this.getString(R.string.person_setting_user_privacy));
            } else if (positionIndex == PersettingThirdAdapt.State.USER_AGREEMENT) {
                LibPersonAboutActivity.this.goWebActivity(ComonStaticURL.getPolicyUrl(), LibPersonAboutActivity.this.getString(R.string.person_setting_user_agreement));
            } else if (positionIndex == PersettingThirdAdapt.State.USER_RIGHT) {
                LibPersonAboutActivity.this.readyGo(LibPersonRightApplyActivity.class);
            }
        }
    };
    private TitleView mTitleView;

    @Override
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.libperson_activity_about;
    }

    @Override
    public void initData() {
        PersettingThirdAdapt.State[] values;
        initView();
        initListView();
        this.mPersettingThirdAdapt = new PersettingThirdAdapt(this);
        this.mThirdPersonSettings = new ArrayList();
        for (PersettingThirdAdapt.State state : PersettingThirdAdapt.State.values()) {
            if (this.isLogin || !state.equals(PersettingThirdAdapt.State.USER_RIGHT)) {
                PersonSetting setting = new PersonSetting();
                setting.setIsOPen(true);
                setting.setThirdAdapt(state);
                this.mThirdPersonSettings.add(setting);
            }
        }
        this.mPersettingThirdAdapt.setData(this.mThirdPersonSettings);
        this.mLvMainSetting.setAdapter(this.mPersettingThirdAdapt);
        this.mLvMainSetting.setOnItemClickListener(this.mThirdListerner);
        this.mBitmapLoadTaskInstance = BitmapLoadTaskInstance.getInstance();
    }

    private void initView() {
        String fimiId = HostConstants.getUserDetail().getFimiId();
        this.isLogin = !TextUtils.isEmpty(fimiId);
        this.mTitleView = findViewById(R.id.title_view);
        this.mTitleView.setTvTitle(getResources().getString(R.string.libperson_about));
        this.mLvMainSetting = findViewById(R.id.lv_main_setting);
        this.libpersonTvVersions = findViewById(R.id.libperson_tv_versions);
        this.libpersonBtnRepeal = findViewById(R.id.libperson_btn_repeal);
        this.libpersonBtnRepeal.setVisibility(this.isLogin ? 0 : 8);
        FontUtil.changeFontLanTing(getAssets(), this.libpersonTvVersions, findViewById(R.id.libperson_tv_rights_reserved), this.libpersonBtnRepeal);
        this.libpersonTvVersions.setText(getResources().getString(R.string.app_product_name) + " " + getResources().getString(R.string.app_version) + "");
    }

    private void initListView() {
        PersonSettingAdapt.State[] values;
        for (PersonSettingAdapt.State state : PersonSettingAdapt.State.values()) {
            boolean isAdd = true;
            int j = 0;
            while (true) {
                if (j < PersonSettingAdapt.mMainState.length) {
                    if (state != PersonSettingAdapt.mMainState[j]) {
                        j++;
                    } else {
                        isAdd = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (isAdd) {
                PersonSetting setting = new PersonSetting();
                setting.setIsOPen(true);
                setting.setSettingAdaptState(state);
            }
        }
    }

    @Override
    public void doTrans() {
        this.libpersonBtnRepeal.setOnClickListener(new AnonymousClass1());
    }

    public void goWebActivity(String url, String title) {
        Intent it = new Intent(this, UserProtocolWebViewActivity.class);
        it.putExtra("web_url", url);
        it.putExtra("web_title", title);
        startActivity(it);
        overridePendingTransition(17432576, 17432577);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10010) {
            recreate();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mBitmapLoadTaskInstance != null) {
            this.mBitmapLoadTaskInstance.setOnLoadListener(null);
        }
    }

    @Override
    public void onComplete() {
        SPStoreManager.getInstance().removeKey(HostConstants.SP_KEY_USER_DETAIL);
        SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, false);
        readyGoThenKillAllActivity(LoginActivity.class);
    }

    /* renamed from: com.fimi.libperson.ui.setting.LibPersonAboutActivity$1 */

    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override
        public void onClick(View view) {
            DialogManager dialogManager = new DialogManager(LibPersonAboutActivity.this.mContext, LibPersonAboutActivity.this.getString(R.string.libperson_repeal_accredit), LibPersonAboutActivity.this.getString(R.string.libperson_repeal_accredit_hint), LibPersonAboutActivity.this.getString(R.string.login_ensure), LibPersonAboutActivity.this.getString(R.string.person_setting_dialog_exit_left_text));
            dialogManager.setVerticalScreen(true);
            dialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
                @Override
                public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                    UserManager.getIntance(LibPersonAboutActivity.this).sendRepealAccredit(Constants.productType.name().toLowerCase(), new DisposeDataHandle(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            try {
                                NetModel netModel = JSON.parseObject(responseObj.toString(), NetModel.class);
                                if (netModel.isSuccess()) {
                                    LibPersonAboutActivity.this.mBitmapLoadTaskInstance.setOnLoadListener(LibPersonAboutActivity.this);
                                    LibPersonAboutActivity.this.mBitmapLoadTaskInstance.setImage(ImageSource.asset("login_bg.jpg"), LibPersonAboutActivity.this.mContext);
                                } else {
                                    ToastUtil.showToast(LibPersonAboutActivity.this, ErrorMessage.getUserModeErrorMessage(LibPersonAboutActivity.this, netModel.getErrCode()), 1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Object reasonObj) {
                            ToastUtil.showToast(LibPersonAboutActivity.this, LibPersonAboutActivity.this.mContext.getString(R.string.network_exception), 1);
                        }
                    }));
                }

                @Override
                public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                }
            });
            dialogManager.showDialog();
        }
    }
}
