package com.fimi.thirdpartysdk.login.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/* loaded from: classes.dex */
public class TwitterApiManager {
    final WeakReference<Context> activityRef;
    volatile TwitterAuthClient authClient;

    public TwitterApiManager(Context context) {
        this.activityRef = new WeakReference<>(context);
    }

    public void login(final LoginCallback loginCallback) {
        getTwitterAuthClient().authorize((Activity) this.activityRef.get(), new Callback<TwitterSession>() { // from class: com.fimi.thirdpartysdk.login.twitter.TwitterApiManager.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<TwitterSession> result) {
                String name = result.data.getUserName();
                long userId = result.data.getUserId();
                TwitterApiManager.this.getTwitterUserInfo(name, userId, loginCallback);
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException e) {
                e.printStackTrace();
                loginCallback.onFailure();
            }
        });
    }

    TwitterAuthClient getTwitterAuthClient() {
        if (this.authClient == null) {
            this.authClient = new TwitterAuthClient();
        }
        return this.authClient;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getTwitterAuthClient().getRequestCode()) {
            getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getTwitterUserInfo(String name, final long userId, final LoginCallback callback) {
        TwitterSession activeSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterApiClient client = new TwitterApiClient(activeSession);
        AccountService accountService = client.getAccountService();
        Call<User> show = accountService.verifyCredentials(false, false, false);
        show.enqueue(new Callback<User>() { // from class: com.fimi.thirdpartysdk.login.twitter.TwitterApiManager.2
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<User> result) {
                User data = result.data;
                String profileImageUrl = data.profileImageUrl.replace("_normal", "");
                String name2 = data.name;
                Map<String, String> map = new HashMap<>();
                map.put("name", name2);
                map.put("userId", userId + "");
                map.put("iconurl", profileImageUrl);
                callback.onSuccess(map);
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                callback.onFailure();
            }
        });
    }

    /* loaded from: classes.dex */
    public interface LoginCallback {
        void onFailure();

        void onSuccess(Map<String, String> map);
    }
}
