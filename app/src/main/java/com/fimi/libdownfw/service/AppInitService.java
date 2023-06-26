package com.fimi.libdownfw.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.ThreadUtils;
import com.fimi.libdownfw.presenter.OauthPresenter;
import com.fimi.network.DownloadManager;
import com.fimi.network.FwManager;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.network.oauth2.OauthConstant;

import java.util.List;


public class AppInitService extends Service {
    private static final int PROGRESS_MAX = 100;
    private final int checkingTaskCount = 0;
    List<UpfirewareDto> needDownDto;
    OauthPresenter oauthPresenter;
    FwManager x9FwManager = new FwManager();
    DownloadManager downloadManager = new DownloadManager();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                SPStoreManager.getInstance();
            }
        });
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                AppInitService.this.getOuthVerification();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getOuthVerification() {
        if (TextUtils.isEmpty(SPStoreManager.getInstance().getString(OauthConstant.ACCESS_TOKEN_SP))) {
            this.oauthPresenter = new OauthPresenter();
            this.oauthPresenter.getAccessToken();
        }
    }
}
