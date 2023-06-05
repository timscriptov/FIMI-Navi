package com.fimi.libdownfw.presenter;

import com.fimi.network.oauth2.CallBackListner;
import com.fimi.network.oauth2.OuthVerificationTask;

/* loaded from: classes.dex */
public class OauthPresenter {
    OuthVerificationTask verificationTask = new OuthVerificationTask();

    public void getAccessToken() {
        this.verificationTask.getAuthorizationCode(new CallBackListner() { // from class: com.fimi.libdownfw.presenter.OauthPresenter.1
            @Override // com.fimi.network.oauth2.CallBackListner
            public void onSuccess(Object result) {
                String code = (String) result;
                OauthPresenter.this.verificationTask.getAccessToken(code);
            }
        });
    }
}
