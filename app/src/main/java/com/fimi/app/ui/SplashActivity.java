package com.fimi.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.fimi.android.app.R;
import com.fimi.app.ui.main.HostNewMainActivity;
import com.fimi.host.HostConstants;
import com.fimi.host.HostLogBack;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.Constants;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.kernel.utils.ThreadUtils;
import com.fimi.libperson.entity.ImageSource;
import com.fimi.libperson.ui.me.login.LoginActivity;
import com.fimi.libperson.ui.setting.ServiceSettingActivity;
import com.fimi.libperson.widget.BitmapLoadTaskInstance;
import com.fimi.network.ApkVersionManager;
import com.fimi.network.FwManager;
import com.fimi.network.entity.NetModel;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.widget.LetterSpacingTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import router.Router;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private final int Skip_What = 0;
    private final long Skip_Time = 1500;
    TextView mTvBottom;
    LetterSpacingTextView mTvTitle;
    private ApkVersionManager mApkVersionManager;
    private BitmapLoadTaskInstance mBitmapLoadTaskInstance;

    @Override
    protected void setStatusBarColor() {
    }

    @Override
    @RequiresApi(api = 21)
    public void initData() {
        getWindow().setFlags(1024, 1024);
        this.mTvTitle = findViewById(R.id.tv_title);
        if (Constants.productType == ProductEnum.X8S) {
            this.mTvTitle.setText(getResources().getText(R.string.app_fimi_slogn));
        }
        this.mTvBottom = findViewById(R.id.tv_bottom);
        this.mTvBottom.setText(getSpannableString());
        FontUtil.changeFontLanTing(getAssets(), this.mTvTitle, this.mTvBottom);
        this.mBitmapLoadTaskInstance = BitmapLoadTaskInstance.getInstance();
        this.mBitmapLoadTaskInstance.setImage(ImageSource.asset("login_bg.jpg"), this.mContext);
        findViewById(R.id.img_splash).setBackgroundResource(getMetaDataInt(this, getString(R.string.splash_icon)));
        this.mApkVersionManager = new ApkVersionManager();
    }

    public int getMetaDataInt(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getApplicationContext().getPackageManager().getApplicationInfo(context.getApplicationContext().getPackageName(), 128);
            return appInfo.metaData.getInt(name);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void doTrans() {
        if ((getIntent().getFlags() & 4194304) != 0) {
            finish();
            return;
        }
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory("android.intent.category.LAUNCHER") && action != null && action.equals("android.intent.action.MAIN")) {
                finish();
                return;
            }
        }
        final String fimiId = HostConstants.getUserDetail().getFimiId();
        if (fimiId == null || "".equals(fimiId)) {
            this.mBitmapLoadTaskInstance = BitmapLoadTaskInstance.getInstance();
            this.mBitmapLoadTaskInstance.setImage(ImageSource.asset("login_bg.jpg"), this.mContext);
        }
        SPStoreManager.getInstance().saveInt("x9_grahpic_hint", 0);
        SPStoreManager.getInstance().saveInt(HostConstants.SP_KEY_UPDATE_CHECK, 2);
        ThreadUtils.execute(SplashActivity.this::getFwDetail);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    if (Constants.productType == ProductEnum.X9) {
                        Intent it = Router.invoke(SplashActivity.this.mContext, "service://x9.recordupload");
                        SplashActivity.this.mContext.startService(it);
                    } else if (Constants.productType == ProductEnum.GH2) {
                        Intent it2 = Router.invoke(SplashActivity.this.mContext, "service://gh2.recordupload");
                        SplashActivity.this.mContext.startService(it2);
                    }
                    int userType = SPStoreManager.getInstance().getInt(Constants.SP_PERSON_USER_TYPE);
                    if (userType > 0 || !TextUtils.isEmpty(fimiId)) {
                        Constants.isRefreshMainView = true;
                        SplashActivity.this.readyGoThenKill(HostNewMainActivity.class);
                    } else if (SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class) == null) {
                        SplashActivity.this.readyGoThenKill(ServiceSettingActivity.class);
                    } else {
                        SplashActivity.this.readyGoThenKill(LoginActivity.class);
                    }
                }
                super.handleMessage(msg);
            }
        }.sendEmptyMessageDelayed(Skip_What, Skip_Time);
    }

    public void getFwDetail() {
        FwManager x9FwManager = new FwManager();
        x9FwManager.getX9FwNetDetail(new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    NetModel netModel = JSON.parseObject(responseObj.toString(), NetModel.class);
                    LogUtil.d("moweiru", "responseObj:" + responseObj);
                    if (netModel.isSuccess() && netModel.getData() != null) {
                        List<UpfirewareDto> fwDtos = JSON.parseArray(netModel.getData().toString(), UpfirewareDto.class);
                        HostConstants.saveFirmwareDetail(fwDtos);
                    }
                } catch (Exception e) {
                    HostConstants.saveFirmwareDetail(new ArrayList<>());
                    HostLogBack.getInstance().writeLog("固件Json转换异常：" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        }));
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @NotNull
    private SpannableString getSpannableString() {
        String str = this.mContext.getString(R.string.splash_copyright_2017_2018fimi_all_rights_reserved);
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.splash_bottom1)), 0, 8, 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.splash_bottom2)), 9, 21, 33);
        spannableString.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.splash_bottom1)), 22, str.length(), 33);
        return spannableString;
    }
}
