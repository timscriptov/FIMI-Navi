package com.fimi.libdownfw.presenter;

import com.fimi.network.oauth2.CallBackListner;
import com.fimi.network.oauth2.OuthVerificationTask;


public class OauthPresenter {
    OuthVerificationTask verificationTask = new OuthVerificationTask();

    public void getAccessToken() {
        this.verificationTask.getAuthorizationCode(new CallBackListner() {
            @Override
            public void onSuccess(Object result) {
                String code = (String) result;
                OauthPresenter.this.verificationTask.getAccessToken(code);
            }
        });
    }
}
