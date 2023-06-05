package com.fimi.kernel.network.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.fimi.kernel.network.okhttp.exception.OkHttpException;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;


public class CommonUrlCallback implements Callback {
    protected final int NETWORK_ERROR = -1;
    private boolean isArray;
    private boolean isGetCode;
    private final Class<?> mClass;
    private final DisposeDataListener mListener;
    private final Handler mDeliveryHandler = new Handler(Looper.getMainLooper());

    public CommonUrlCallback(DisposeDataHandle handle, boolean isGetCode) {
        this.isArray = false;
        this.isGetCode = true;
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.isArray = handle.isArray;
        this.isGetCode = isGetCode;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        this.mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                CommonUrlCallback.this.mListener.onFailure(new OkHttpException(-1, e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        final HttpUrl url = response.request().url();
        this.mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (CommonUrlCallback.this.isGetCode) {
                    CommonUrlCallback.this.handleResponse(url);
                } else {
                    CommonUrlCallback.this.handleBodyResponse(result);
                }
            }
        });
    }

    public void handleResponse(HttpUrl url) {
        if (url != null) {
            this.mListener.onSuccess(url);
        } else {
            this.mListener.onFailure(null);
        }
    }

    public void handleBodyResponse(String body) {
        if (body != null) {
            this.mListener.onSuccess(body);
        } else {
            this.mListener.onFailure(null);
        }
    }
}
