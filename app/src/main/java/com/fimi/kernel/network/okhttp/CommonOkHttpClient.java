package com.fimi.kernel.network.okhttp;

import androidx.annotation.NonNull;

import com.fimi.kernel.network.okhttp.cookie.SimpleCookieJar;
import com.fimi.kernel.network.okhttp.https.HttpsUtils;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.response.CommonFileCallback;
import com.fimi.kernel.network.okhttp.response.CommonJsonCallback;
import com.fimi.kernel.network.okhttp.response.CommonUrlCallback;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class CommonOkHttpClient {
    private static final int TIME_OUT = 30;
    private static final OkHttpClient mOkHttpClient;

    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier((hostname, session) -> true);
        okHttpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("User-Agent", "Imooc-Mobile").build();
            return chain.proceed(request);
        });
        okHttpClientBuilder.cookieJar(new SimpleCookieJar());
        okHttpClientBuilder.connectTimeout(30L, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(30L, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(30L, TimeUnit.SECONDS);
        okHttpClientBuilder.followRedirects(true);
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    @NonNull
    public static Call get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    @NonNull
    public static Call post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    @NonNull
    public static Call downloadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }

    @NonNull
    public static Call getUrl(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonUrlCallback(handle, true));
        return call;
    }

    @NonNull
    public static Call postUrl(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonUrlCallback(handle, false));
        return call;
    }
}
