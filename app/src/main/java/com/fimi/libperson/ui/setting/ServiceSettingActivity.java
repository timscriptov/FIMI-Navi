package com.fimi.libperson.ui.setting;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fimi.android.app.R;
import com.fimi.host.HostConstants;
import com.fimi.kernel.Constants;
import com.fimi.kernel.language.ConstantLanguages;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.libperson.BasePersonActivity;
import com.fimi.libperson.adapter.ServiceAdapter;
import com.fimi.libperson.entity.ImageSource;
import com.fimi.libperson.ui.me.login.LoginActivity;
import com.fimi.libperson.widget.BitmapLoadTaskInstance;
import com.fimi.libperson.widget.TitleView;
import com.fimi.widget.DialogManager;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ServiceSettingActivity extends BasePersonActivity implements AdapterView.OnItemClickListener, BitmapLoadTaskInstance.OnLoadListener {
    private ServiceAdapter adapter;
    private DialogManager dialogManager;
    private BitmapLoadTaskInstance mBitmapLoadTaskInstance;
    private String mCountrySelect;
    private ListView mListView;
    private int mPosition;
    private TitleView mTitleView;
    private int serviceID;
    private List<ServiceItem> serviceItems;
    private int mCountryType = -1;
    private boolean isSetting = false;

    @Override // com.fimi.libperson.BasePersonActivity, com.fimi.kernel.base.BaseActivity
    public void setStatusBarColor() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
        getWindow().getDecorView().setSystemUiVisibility(9216);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void initData() {
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) this.mTitleView.getLayoutParams();
        params.setMargins(0, this.statusBarHeight + this.marginStatus, 0, 0);
        this.mTitleView.setLayoutParams(params);
        this.mListView = (ListView) findViewById(R.id.lv_l_setting_setting);
        this.mListView.setOverScrollMode(2);
        this.mTitleView.setTvTitle(getString(R.string.libperson_service_select_title));
        this.isSetting = getIntent().getBooleanExtra("is_setting", true);
        this.mBitmapLoadTaskInstance = BitmapLoadTaskInstance.getInstance();
        this.mBitmapLoadTaskInstance.setOnLoadListener(this);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        if (SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class) != null) {
            Constants.isShowUserProtocol = false;
        }
        finish();
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void doTrans() {
        this.mTitleView.setIvLeftListener(new View.OnClickListener() { // from class: com.fimi.libperson.ui.setting.ServiceSettingActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class) != null) {
                    Constants.isShowUserProtocol = false;
                }
                ServiceSettingActivity.this.finish();
            }
        });
        ServiceItem serviceItem = (ServiceItem) SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class);
        this.serviceItems = new ArrayList();
        for (int i = 0; i < ServiceItem.getServicename().length; i++) {
            ServiceItem item = new ServiceItem();
            item.setInfo(ServiceItem.getServicename()[i]);
            this.serviceID = ServiceItem.getServicename()[i];
            if (R.string.region_Argentina == this.serviceID) {
                item.setCountryCode("es");
                item.setServiceUrl(ServiceItem.newusService);
            } else if (R.string.region_United_Arab_Emirates == this.serviceID) {
                item.setCountryCode("ar-SA");
                item.setServiceUrl(ServiceItem.newusService);
            } else if (R.string.region_The_US == this.serviceID) {
                item.setCountryCode("en");
                item.setServiceUrl(ServiceItem.newusService);
            } else if (R.string.region_other == this.serviceID) {
                item.setCountryCode("en");
                item.setServiceUrl(ServiceItem.newusService);
            } else if (R.string.region_Mexico == this.serviceID) {
                item.setCountryCode("es");
                item.setServiceUrl(ServiceItem.newusService);
            } else if (R.string.region_Saudi_Arabia == this.serviceID) {
                item.setCountryCode("ar-SA");
                item.setServiceUrl(ServiceItem.newusService);
            } else if (R.string.region_Iran == this.serviceID) {
                item.setCountryCode("fa-IR");
                item.setServiceUrl(ServiceItem.newusService);
            } else if (R.string.region_Poland == this.serviceID) {
                item.setCountryCode("pl");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_germany == this.serviceID) {
                item.setCountryCode("");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_France == this.serviceID) {
                item.setCountryCode(ConstantLanguages.FRANCE);
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_Slovakia == this.serviceID) {
                item.setCountryCode("sk-SK");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_Turkey == this.serviceID) {
                item.setCountryCode("tr-TR");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_Ukraine == this.serviceID) {
                item.setCountryCode("uk-UA");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_Spain == this.serviceID) {
                item.setCountryCode("es");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_Philippines == this.serviceID) {
                item.setCountryCode("en");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_Malaysia == this.serviceID) {
                item.setCountryCode("");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_Burma == this.serviceID) {
                item.setCountryCode("");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_Thailand == this.serviceID) {
                item.setCountryCode("th-TH");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_Singapore == this.serviceID) {
                item.setCountryCode("en");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_Indonesia == this.serviceID) {
                item.setCountryCode("");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_Vietnam == this.serviceID) {
                item.setCountryCode("vi-VN");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_hk == this.serviceID) {
                item.setCountryCode("tw");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_tw == this.serviceID) {
                item.setCountryCode("cn");
                item.setServiceUrl(ServiceItem.chinaService);
            } else if (R.string.region_Mainland_China == this.serviceID) {
                item.setCountryCode("cn");
                item.setServiceUrl(ServiceItem.chinaService);
            } else if (R.string.region_Russia == this.serviceID) {
                item.setCountryCode("ru");
                item.setServiceUrl(ServiceItem.moscowService);
            } else if (R.string.region_maroc == this.serviceID) {
                item.setCountryCode("ar-SA");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_uk == this.serviceID) {
                item.setCountryCode("en");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_italy == this.serviceID) {
                item.setCountryCode("it-IT");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_israel == this.serviceID) {
                item.setCountryCode("ar-SA");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_greco == this.serviceID) {
                item.setCountryCode("");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_japan == this.serviceID) {
                item.setCountryCode("");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_portugal == this.serviceID) {
                item.setCountryCode("");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_korea == this.serviceID) {
                item.setCountryCode("");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_colombia == this.serviceID) {
                item.setCountryCode("en");
                item.setServiceUrl(ServiceItem.newusService);
            } else if (R.string.region_pakistan == this.serviceID) {
                item.setCountryCode("en");
                item.setServiceUrl(ServiceItem.singaporeService);
            } else if (R.string.region_egpyt == this.serviceID) {
                item.setCountryCode("ar-SA");
                item.setServiceUrl(ServiceItem.frankfurtService);
            } else if (R.string.region_belarus == this.serviceID) {
                item.setCountryCode("ru");
                item.setServiceUrl(ServiceItem.frankfurtService);
            }
            if (serviceItem != null && item.getInfo() == serviceItem.getInfo()) {
                this.mPosition = i;
                item.setSelect(true);
            }
            this.serviceItems.add(item);
        }
        this.adapter = new ServiceAdapter(this.serviceItems, this);
        this.mListView.setAdapter((ListAdapter) this.adapter);
        this.mListView.setOnItemClickListener(this);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    protected int getContentViewLayoutID() {
        return R.layout.activity_user_language_setting;
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (this.mPosition != position || SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class) == null) {
            ServiceItem serviceItem = this.serviceItems.get(position);
            if (this.isSetting) {
                if (SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class) == null) {
                    SPStoreManager.getInstance().saveObject(Constants.SERVICE_ITEM_KEY, serviceItem);
                    HostConstants.initUrl();
                    readyGoThenKillAllActivity(LoginActivity.class);
                    return;
                }
                if (serviceItem.getServiceUrl().equalsIgnoreCase(this.serviceItems.get(this.mPosition).getServiceUrl())) {
                    this.dialogManager = new DialogManager(this.mContext, "", getString(R.string.libperson_service_dialog_exit_message), getString(R.string.libperson_service_ok), getString(R.string.person_setting_dialog_exit_left_text));
                } else {
                    this.dialogManager = new DialogManager(this.mContext, "", getString(R.string.libperson_service_dialog_exit_switch_message), getString(R.string.libperson_service_ok), getString(R.string.person_setting_dialog_exit_left_text));
                }
                this.dialogManager.setVerticalScreen(true);
                this.dialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() { // from class: com.fimi.libperson.ui.setting.ServiceSettingActivity.2
                    @Override // com.fimi.widget.DialogManager.OnDialogListener
                    public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                        ServiceSettingActivity.this.mPosition = position;
                        ServiceSettingActivity.this.mBitmapLoadTaskInstance.setImage(ImageSource.asset("login_bg.jpg"), ServiceSettingActivity.this.mContext);
                    }

                    @Override // com.fimi.widget.DialogManager.OnDialogListener
                    public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                    }
                });
                this.dialogManager.showDialog();
                return;
            }
            if (serviceItem.getServiceUrl().equalsIgnoreCase(this.serviceItems.get(this.mPosition).getServiceUrl())) {
                this.dialogManager = new DialogManager(this.mContext, "", getString(R.string.libperson_service_dialog_exit_message), getString(R.string.libperson_service_ok), getString(R.string.person_setting_dialog_exit_left_text));
            } else {
                this.dialogManager = new DialogManager(this.mContext, "", getString(R.string.libperson_service_dialog_exit_switch_message), getString(R.string.libperson_service_ok), getString(R.string.person_setting_dialog_exit_left_text));
            }
            this.dialogManager.setVerticalScreen(true);
            this.dialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() { // from class: com.fimi.libperson.ui.setting.ServiceSettingActivity.3
                @Override // com.fimi.widget.DialogManager.OnDialogListener
                public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                    SPStoreManager.getInstance().saveObject(Constants.SERVICE_ITEM_KEY, ServiceSettingActivity.this.serviceItems.get(position));
                    HostConstants.initUrl();
                    SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, false);
                    ServiceSettingActivity.this.finish();
                }

                @Override // com.fimi.widget.DialogManager.OnDialogListener
                public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                }
            });
            this.dialogManager.showDialog();
        }
    }

    private void onSelectLanguage(int code) {
    }

    @Override // com.fimi.libperson.widget.BitmapLoadTaskInstance.OnLoadListener
    public void onComplete() {
        if (SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class) != null) {
            SPStoreManager.getInstance().saveObject(Constants.SERVICE_ITEM_KEY, this.serviceItems.get(this.mPosition));
            HostConstants.initUrl();
            SPStoreManager.getInstance().removeKey(HostConstants.SP_KEY_USER_DETAIL);
            SPStoreManager.getInstance().saveBoolean(HostConstants.USER_PROTOCOL, false);
            readyGoThenKillAllActivity(LoginActivity.class);
        }
    }

    private int getServiceType(int position) {
        for (int i = 0; i < ServiceItem.SERVICECODE.length; i++) {
            if (position == ServiceItem.SERVICECODE[i]) {
                int type = ServiceItem.SERVICECODE[i];
                return type;
            }
        }
        return 0;
    }
}
