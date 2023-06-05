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

/* loaded from: classes.dex */
public class AppInitService extends Service {
    private static final int PROGRESS_MAX = 100;
    List<UpfirewareDto> needDownDto;
    OauthPresenter oauthPresenter;
    FwManager x9FwManager = new FwManager();
    DownloadManager downloadManager = new DownloadManager();
    private volatile int checkingTaskCount = 0;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        ThreadUtils.execute(new Runnable() { // from class: com.fimi.libdownfw.service.AppInitService.1
            @Override // java.lang.Runnable
            public void run() {
                SPStoreManager.getInstance();
            }
        });
        ThreadUtils.execute(new Runnable() { // from class: com.fimi.libdownfw.service.AppInitService.2
            @Override // java.lang.Runnable
            public void run() {
                AppInitService.this.getOuthVerification();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override // android.app.Service
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
