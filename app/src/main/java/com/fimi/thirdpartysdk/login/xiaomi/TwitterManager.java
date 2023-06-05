package com.fimi.thirdpartysdk.login.xiaomi;

import android.content.Context;
import android.content.Intent;

import com.fimi.thirdpartysdk.login.ILogin;
import com.fimi.thirdpartysdk.login.LoginCallback;
import com.fimi.thirdpartysdk.login.twitter.TwitterApiManager;

import java.util.Map;

/* loaded from: classes.dex */
public class TwitterManager implements ILogin {
    LoginCallback loginCallback;
    TwitterApiManager mTwitterApiClient;

    @Override // com.fimi.thirdpartysdk.login.ILogin
    public void login(Context mContext, LoginCallback callback) {
        this.loginCallback = callback;
        this.mTwitterApiClient = new TwitterApiManager(mContext);
        this.mTwitterApiClient.login(new TwitterApiManager.LoginCallback() { // from class: com.fimi.thirdpartysdk.login.xiaomi.TwitterManager.1
            @Override // com.fimi.thirdpartysdk.login.twitter.TwitterApiManager.LoginCallback
            public void onSuccess(Map<String, String> map) {
                TwitterManager.this.loginCallback.loginSuccess(map);
            }

            @Override // com.fimi.thirdpartysdk.login.twitter.TwitterApiManager.LoginCallback
            public void onFailure() {
            }
        });
    }

    @Override // com.fimi.thirdpartysdk.login.ILogin
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mTwitterApiClient.onActivityResult(requestCode, resultCode, data);
    }
}
