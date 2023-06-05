package com.fimi.libperson.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.fimi.android.app.R;
import com.fimi.host.HostConstants;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.kernel.utils.ToastUtil;
import com.fimi.libperson.ivew.IThirdLoginView;
import com.fimi.network.ErrorMessage;
import com.fimi.network.FwManager;
import com.fimi.network.UserManager;
import com.fimi.network.entity.NetModel;
import com.fimi.network.entity.ThirdAcountDto;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.thirdpartysdk.ThirdPartyConstants;
import com.fimi.thirdpartysdk.login.LoginCallback;
import com.fimi.thirdpartysdk.login.ThirdLoginManager;
import com.github.moduth.blockcanary.internal.BlockInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ThirdLoginPresenter {
    private static final int DELAY = 50000;
    private static final int sUPDATE_PROGRESS = 1;
    IThirdLoginView loginView;
    Context mContext;
    private Handler mHandler = new Handler() { // from class: com.fimi.libperson.presenter.ThirdLoginPresenter.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ThirdLoginPresenter.this.loginView.updateProgress(false);
            }
        }
    };
    private ThirdLoginManager mThirdLoginManager = ThirdLoginManager.getInstance();

    public ThirdLoginPresenter(IThirdLoginView loginView) {
        this.loginView = loginView;
        this.mContext = (Context) loginView;
        this.mThirdLoginManager.init();
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, 8192);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void loginFacebook() {
        if (AbAppUtil.isNetworkAvailable(this.mContext)) {
            if (this.loginView != null) {
                this.loginView.updateProgress(true);
            }
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 50000L);
            this.mThirdLoginManager.login("2", this.mContext, new LoginCallback() { // from class: com.fimi.libperson.presenter.ThirdLoginPresenter.2
                @Override // com.fimi.thirdpartysdk.login.LoginCallback
                public void loginSuccess(Object object) {
                    if (ThirdLoginPresenter.this.loginView != null) {
                        ThirdLoginPresenter.this.loginView.updateProgress(true);
                    }
                    Map<String, String> db = (Map) object;
                    ThirdAcountDto thirdAcountDto = new ThirdAcountDto();
                    new StringBuilder();
                    thirdAcountDto.setName(db.get("name"));
                    thirdAcountDto.setThirdId(db.get(BlockInfo.KEY_UID));
                    thirdAcountDto.setUserImgUrl(db.get("iconurl"));
                    thirdAcountDto.setLoginChannel("2");
                    UserManager.getIntance(ThirdLoginPresenter.this.mContext).thirdUserLogin(thirdAcountDto, new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.libperson.presenter.ThirdLoginPresenter.2.1
                        @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                        public void onSuccess(Object responseObj) {
                            NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                            if (netModel.isSuccess()) {
                                HostConstants.saveUserDetail(netModel.getData());
                                HostConstants.saveUserInfo(null, null);
                                ThirdLoginPresenter.this.getFwDetail();
                                return;
                            }
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, ErrorMessage.getUserModeErrorMessage(ThirdLoginPresenter.this.mContext, netModel.getErrCode()));
                        }

                        @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                        public void onFailure(Object reasonObj) {
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, reasonObj.toString());
                        }
                    }));
                }

                @Override // com.fimi.thirdpartysdk.login.LoginCallback
                public void loginFail(String error) {
                    if (error.contains("net::ERR_CONNECTION_RESET")) {
                        error = ThirdLoginPresenter.this.mContext.getResources().getString(R.string.libperson_facebook_connection);
                    }
                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, error);
                }
            });
        } else if (this.loginView != null) {
            this.loginView.loginThirdListener(false, this.mContext.getString(R.string.network_exception));
        }
    }

    public void loginTwitter() {
        if (!checkApkExist(this.mContext, "com.twitter.android")) {
            ToastUtil.showToast(this.mContext, R.string.login_install_twitter, 1);
        } else if (AbAppUtil.isNetworkAvailable(this.mContext)) {
            if (this.loginView != null) {
                this.loginView.updateProgress(true);
            }
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 50000L);
            this.mThirdLoginManager.login(ThirdPartyConstants.LOGIN_CHANNEL_TW, this.mContext, new LoginCallback() { // from class: com.fimi.libperson.presenter.ThirdLoginPresenter.3
                @Override // com.fimi.thirdpartysdk.login.LoginCallback
                public void loginSuccess(Object object) {
                    if (ThirdLoginPresenter.this.loginView != null) {
                        ThirdLoginPresenter.this.loginView.updateProgress(true);
                    }
                    Map<String, String> db = (Map) object;
                    ThirdAcountDto thirdAcountDto = new ThirdAcountDto();
                    StringBuilder sb = new StringBuilder();
                    sb.append(db.get("name") + " ");
                    thirdAcountDto.setName(sb.toString());
                    thirdAcountDto.setThirdId(db.get("userId"));
                    thirdAcountDto.setUserImgUrl(db.get("iconurl"));
                    thirdAcountDto.setLoginChannel(ThirdPartyConstants.LOGIN_CHANNEL_TW);
                    UserManager.getIntance(ThirdLoginPresenter.this.mContext).thirdUserLogin(thirdAcountDto, new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.libperson.presenter.ThirdLoginPresenter.3.1
                        @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                        public void onSuccess(Object responseObj) {
                            NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                            if (netModel.isSuccess()) {
                                HostConstants.saveUserDetail(netModel.getData());
                                HostConstants.saveUserInfo(null, null);
                                ThirdLoginPresenter.this.getFwDetail();
                                return;
                            }
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, ErrorMessage.getUserModeErrorMessage(ThirdLoginPresenter.this.mContext, netModel.getErrCode()));
                        }

                        @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                        public void onFailure(Object reasonObj) {
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, ThirdLoginPresenter.this.mContext.getString(R.string.login_result));
                        }
                    }));
                }

                @Override // com.fimi.thirdpartysdk.login.LoginCallback
                public void loginFail(String error) {
                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, error);
                }
            });
        } else if (this.loginView != null) {
            this.loginView.loginThirdListener(false, this.mContext.getString(R.string.network_exception));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mThirdLoginManager.onActivityResult(requestCode, resultCode, data);
    }

    public void loginMi() {
        if (AbAppUtil.isNetworkAvailable(this.mContext)) {
            if (this.loginView != null) {
                this.loginView.updateProgress(true);
            }
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 50000L);
            this.mThirdLoginManager.login("1", this.mContext, new LoginCallback() { // from class: com.fimi.libperson.presenter.ThirdLoginPresenter.4
                @Override // com.fimi.thirdpartysdk.login.LoginCallback
                public void loginSuccess(Object object) {
                    if (ThirdLoginPresenter.this.loginView != null) {
                        ThirdLoginPresenter.this.loginView.updateProgress(true);
                    }
                    JSONObject dataJsonObject = (JSONObject) object;
                    ThirdAcountDto thirdAcountDto = new ThirdAcountDto();
                    if (dataJsonObject.has("miliaoIcon_orig")) {
                        try {
                            thirdAcountDto.setUserImgUrl(dataJsonObject.getString("miliaoIcon_orig"));
                            if (dataJsonObject.isNull("userId")) {
                                thirdAcountDto.setThirdId(dataJsonObject.getString("unionId"));
                            } else {
                                thirdAcountDto.setThirdId(dataJsonObject.getString("userId"));
                            }
                            thirdAcountDto.setName(dataJsonObject.getString("miliaoNick"));
                            thirdAcountDto.setNickName(dataJsonObject.getString("miliaoNick"));
                            thirdAcountDto.setLoginChannel("1");
                            UserManager.getIntance(ThirdLoginPresenter.this.mContext).thirdUserLogin(thirdAcountDto, new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.libperson.presenter.ThirdLoginPresenter.4.1
                                @Override
                                // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                                public void onSuccess(Object responseObj) {
                                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                                    if (netModel.isSuccess()) {
                                        HostConstants.saveUserDetail(netModel.getData());
                                        HostConstants.saveUserInfo(null, null);
                                        ThirdLoginPresenter.this.getFwDetail();
                                        return;
                                    }
                                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, ErrorMessage.getUserModeErrorMessage(ThirdLoginPresenter.this.mContext, netModel.getErrCode()));
                                }

                                @Override
                                // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                                public void onFailure(Object reasonObj) {
                                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, ThirdLoginPresenter.this.mContext.getString(R.string.network_exception));
                                }
                            }));
                        } catch (JSONException e) {
                            ThirdLoginPresenter.this.loginView.loginThirdListener(false, e.toString());
                        }
                    }
                }

                @Override // com.fimi.thirdpartysdk.login.LoginCallback
                public void loginFail(String error) {
                    ThirdLoginPresenter.this.loginView.loginThirdListener(false, error);
                }
            });
        } else if (this.loginView != null) {
            this.loginView.loginThirdListener(false, this.mContext.getString(R.string.network_exception));
        }
    }

    public void setPause() {
        this.mHandler.removeMessages(1);
    }

    public void getFwDetail() {
        FwManager x9FwManager = new FwManager();
        HostConstants.saveFirmwareDetail(new ArrayList());
        x9FwManager.getX9FwNetDetail(new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.libperson.presenter.ThirdLoginPresenter.5
            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onSuccess(Object responseObj) {
                try {
                    try {
                        NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                        LogUtil.d("moweiru", "responseObj:" + responseObj);
                        if (netModel.isSuccess() && netModel.getData() != null) {
                            List<UpfirewareDto> fwDtos = JSON.parseArray(netModel.getData().toString(), UpfirewareDto.class);
                            HostConstants.saveFirmwareDetail(fwDtos);
                        }
                        if (ThirdLoginPresenter.this.loginView != null) {
                            ThirdLoginPresenter.this.loginView.loginSuccess();
                        }
                    } catch (Exception e) {
                        HostConstants.saveFirmwareDetail(new ArrayList());
                        HostLogBack.getInstance().writeLog("固件Json转换异常：" + e.getMessage());
                        if (ThirdLoginPresenter.this.loginView != null) {
                            ThirdLoginPresenter.this.loginView.loginSuccess();
                        }
                    }
                } catch (Throwable th) {
                    if (ThirdLoginPresenter.this.loginView != null) {
                        ThirdLoginPresenter.this.loginView.loginSuccess();
                    }
                    throw th;
                }
            }

            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onFailure(Object reasonObj) {
                if (ThirdLoginPresenter.this.loginView != null) {
                    ThirdLoginPresenter.this.loginView.loginSuccess();
                }
            }
        }));
    }
}
