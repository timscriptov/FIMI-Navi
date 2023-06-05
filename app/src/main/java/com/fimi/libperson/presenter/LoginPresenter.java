package com.fimi.libperson.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.fimi.android.app.R;
import com.fimi.host.HostConstants;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.utils.DataValidatorUtil;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.libperson.ivew.ILoginView;
import com.fimi.network.ErrorMessage;
import com.fimi.network.FwManager;
import com.fimi.network.UserManager;
import com.fimi.network.entity.NetModel;
import com.fimi.network.entity.UpfirewareDto;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LoginPresenter {
    private static final int TIMER = 1;
    private static final int sUPDATE_TIME = 1000;
    Context mContext;
    ILoginView mView;
    private int mSeconds = 60;

    public LoginPresenter(ILoginView mView) {
        this.mView = mView;
        this.mContext = (Context) mView;
    }

    public void getVerificationCode(String phone) {
        if (!DataValidatorUtil.isMobile(phone)) {
            this.mView.getCodeResult(false, this.mContext.getString(R.string.register_input_right_phone));
        } else {
            UserManager.getIntance(this.mContext).getSecurityCode(phone, "0", "0", new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.libperson.presenter.LoginPresenter.1
                @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                public void onSuccess(Object responseObj) {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (netModel.isSuccess()) {
                        LoginPresenter.this.mSeconds = 60;
                        LoginPresenter.this.mView.getCodeResult(true, null);
                        return;
                    }
                    LoginPresenter.this.mView.getCodeResult(false, ErrorMessage.getUserModeErrorMessage(LoginPresenter.this.mContext, netModel.getErrCode()));
                }

                @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                public void onFailure(Object reasonObj) {
                    LoginPresenter.this.mView.getCodeResult(false, LoginPresenter.this.mContext.getString(R.string.network_exception));
                }
            }));
        }
    }

    public void loginByPhone(final String phone, String code) {
        if (TextUtils.isEmpty(phone)) {
            this.mView.iphoneLoginResult(false, this.mContext.getString(R.string.login_hint_iphone));
        } else if (!DataValidatorUtil.isMobile(phone)) {
            this.mView.iphoneLoginResult(false, this.mContext.getString(R.string.register_input_right_phone));
        } else {
            UserManager.getIntance(this.mContext).loginFmUser(phone, code, "0", new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.libperson.presenter.LoginPresenter.2
                @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                public void onSuccess(Object responseObj) {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (netModel.isSuccess()) {
                        HostConstants.saveUserDetail(netModel.getData());
                        HostConstants.saveUserInfo("0", phone);
                        LoginPresenter.this.getFwDetail();
                        return;
                    }
                    LoginPresenter.this.mView.iphoneLoginResult(false, ErrorMessage.getUserModeErrorMessage(LoginPresenter.this.mContext, netModel.getErrCode()));
                }

                @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                public void onFailure(Object reasonObj) {
                    LoginPresenter.this.mView.iphoneLoginResult(false, LoginPresenter.this.mContext.getString(R.string.network_exception));
                }
            }));
        }
    }

    public void loginByEmail(final String email, String password) {
        if (TextUtils.isEmpty(email)) {
            this.mView.emailLoginResult(false, this.mContext.getString(R.string.register_email_not_null));
        } else if (!DataValidatorUtil.isEmail(email)) {
            this.mView.emailLoginResult(false, this.mContext.getString(R.string.register_input_right_email));
        } else {
            UserManager.getIntance(this.mContext).loginFmUser(email, password, "1", new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.libperson.presenter.LoginPresenter.3
                @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                public void onSuccess(Object responseObj) {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (netModel.isSuccess()) {
                        HostConstants.saveUserDetail(netModel.getData());
                        HostConstants.saveUserInfo("1", email);
                        LoginPresenter.this.getFwDetail();
                        return;
                    }
                    LoginPresenter.this.mView.emailLoginResult(false, ErrorMessage.getUserModeErrorMessage(LoginPresenter.this.mContext, netModel.getErrCode()));
                    if (ErrorMessage.VERIFICATION_CODE_LOGIN_OUTTIME.equals(netModel.getErrCode())) {
                        LoginPresenter.this.mView.freorgottenPasswords(true);
                    } else {
                        LoginPresenter.this.mView.freorgottenPasswords(false);
                    }
                }

                @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
                public void onFailure(Object reasonObj) {
                    LoginPresenter.this.mView.emailLoginResult(false, LoginPresenter.this.mContext.getString(R.string.network_exception));
                }
            }));
        }
    }

    public void getFwDetail() {
        FwManager x9FwManager = new FwManager();
        HostConstants.saveFirmwareDetail(new ArrayList());
        x9FwManager.getX9FwNetDetail(new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.libperson.presenter.LoginPresenter.4
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
                        if (LoginPresenter.this.mView != null) {
                            LoginPresenter.this.mView.loginSuccess();
                        }
                    } catch (Exception e) {
                        HostConstants.saveFirmwareDetail(new ArrayList());
                        HostLogBack.getInstance().writeLog("固件Json转换异常：" + e.getMessage());
                        if (LoginPresenter.this.mView != null) {
                            LoginPresenter.this.mView.loginSuccess();
                        }
                    }
                } catch (Throwable th) {
                    if (LoginPresenter.this.mView != null) {
                        LoginPresenter.this.mView.loginSuccess();
                    }
                    throw th;
                }
            }

            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onFailure(Object reasonObj) {
                if (LoginPresenter.this.mView != null) {
                    LoginPresenter.this.mView.loginSuccess();
                }
            }
        }));
    }
}
