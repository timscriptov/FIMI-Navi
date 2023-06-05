package com.fimi.network;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.network.entity.RestPswDto;
import com.fimi.network.entity.ThirdAcountDto;
import com.umeng.commonsdk.proguard.g;

/* loaded from: classes.dex */
public class UserManager extends BaseManager {
    private static UserManager mUserManager;

    public UserManager(Context context) {
    }

    public static UserManager getIntance(Context context) {
        if (mUserManager == null) {
            mUserManager = new UserManager(context.getApplicationContext());
        }
        return mUserManager;
    }

    public void thirdUserLogin(ThirdAcountDto dto, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(dto).toString());
        String url = HostConstants.USER_LOGIN_URL + "loginByThirdAccount";
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }

    public void getSecurityCode(String account, String codeFun, String codeType, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("codeFunc", codeFun);
        requestParams.put("codeType", codeType);
        String url = HostConstants.USER_LOGIN_URL_V2 + "getSecurityCode";
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }

    public void loginFmUser(String account, String password, String loginType, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", account);
        requestParams.put("password", password);
        requestParams.put("loginType", loginType);
        String url = HostConstants.USER_LOGIN_URL_V2 + "loginUser";
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }

    public void registerFmUser(String account, String code, String password, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", account);
        requestParams.put("code", code);
        requestParams.put("password", password);
        String url = HostConstants.USER_LOGIN_URL + "registerFmAcountByPhone";
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }

    public void registerByEmail(String email, String pwd, String confirmPwd, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", email);
        requestParams.put("password", pwd);
        requestParams.put("confirmPwd", confirmPwd);
        String url = HostConstants.USER_LOGIN_URL + "registerFmAcountByEmail";
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }

    public void sendEmail(String email, String language, String whatApp, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", email);
        requestParams.put(g.M, language);
        requestParams.put("whatApp", whatApp);
        String url = HostConstants.RIGHT_APPLY_V1 + "reqPersonalData";
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }

    public void sendRepealAccredit(String whatApp, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("whatApp", whatApp);
        String url = HostConstants.RIGHT_APPLY_V1 + "revokeAuthorization";
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }

    public void resetPassword(RestPswDto restPswDto, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(restPswDto).toString());
        String url = HostConstants.USER_LOGIN_URL + "restAccountPwdByEmail";
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }

    public void resetIphonePassword(RestPswDto restPswDto, DisposeDataHandle disposeDataHandle) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jsonContent", JSON.toJSON(restPswDto).toString());
        String url = HostConstants.USER_LOGIN_URL + "restPasswordByPhone";
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, getRequestParams(requestParams)), disposeDataHandle);
    }
}
