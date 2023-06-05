package com.fimi.thirdpartysdk.login.xiaomi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fimi.thirdpartysdk.login.ILogin;
import com.fimi.thirdpartysdk.login.LoginCallback;
import com.github.moduth.blockcanary.internal.BlockInfo;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class FaceBookManager implements ILogin {
    private static final String TAG = "FaceBookManager";
    LoginCallback loginCallback;
    private LoginManager loginManager;
    private CallbackManager mCallbackManager;

    @Override
    public void login(Context context, final LoginCallback callback) {
        this.loginCallback = callback;
        this.mCallbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(context);
        this.loginManager = LoginManager.getInstance();
        this.loginManager.registerCallback(this.mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i(FaceBookManager.TAG, "onCompleted: ");
                        if (object != null) {
                            Map<String, String> map = new HashMap<>();
                            map.put("name", object.optString("name"));
                            map.put(BlockInfo.KEY_UID, object.optString("id"));
                            FaceBookManager.this.loginCallback.loginSuccess(map);
                        }
                    }
                }).executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i(FaceBookManager.TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(FaceBookManager.TAG, "onError: ");
                callback.loginFail(error.getMessage());
            }
        });
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile", "user_friends"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
