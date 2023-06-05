package com.fimi.apk.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.fimi.apk.iview.IApkVerisonView;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.SystemParamUtil;
import com.fimi.network.ApkVersionManager;
import com.fimi.network.entity.ApkVersionDto;
import com.fimi.network.entity.NetModel;

import java.net.URL;

public class ApkVersionPrenster {
    private final Context context;
    private final IApkVerisonView mApkVerisonView;
    private final ApkVersionManager mApkVersionManager = new ApkVersionManager();
    private onApkUpdateListerner mOnApkUpdateListerner;
    private onShowDialogListerner mOnShowDialogListerner;

    public ApkVersionPrenster(Context context, IApkVerisonView mApkVerisonView) {
        this.context = context;
        this.mApkVerisonView = mApkVerisonView;
    }

    public void getOnlineNewApkFileInfo() {
        String packageName = SystemParamUtil.getPackageName();
        String splitName = packageName.substring(packageName.lastIndexOf(".") + 1);
        final String savePath = DirectoryPath.getApkPath() + "/" + splitName;
        this.mApkVersionManager.getOnlineNewApkFileInfo(packageName, new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.apk.presenter.ApkVersionPrenster.1
            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onSuccess(Object responseObj) {
                try {
                    NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                    if (!netModel.isSuccess()) {
                        if (ApkVersionPrenster.this.mOnApkUpdateListerner != null) {
                            ApkVersionPrenster.this.mOnApkUpdateListerner.haveUpdate(false);
                        }
                    } else if (netModel.getData() != null) {
                        ApkVersionDto dto = (ApkVersionDto) JSON.parseObject(netModel.getData().toString(), ApkVersionDto.class);
                        new URL(dto.getUrl());
                        ApkVersionPrenster.this.compareApkVersion(dto, savePath);
                    }
                } catch (Exception e) {
                    if (ApkVersionPrenster.this.mOnApkUpdateListerner != null) {
                        ApkVersionPrenster.this.mOnApkUpdateListerner.haveUpdate(false);
                    }
                }
            }

            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onFailure(Object reasonObj) {
                if (ApkVersionPrenster.this.mOnApkUpdateListerner != null) {
                    ApkVersionPrenster.this.mOnApkUpdateListerner.haveUpdate(false);
                }
            }
        }));
    }

    public void compareApkVersion(ApkVersionDto dto, String savePath) {
        int onlineVersion = Integer.parseInt(dto.getNewVersion());
        int localVersion = SystemParamUtil.getVersionCode();
        if (onlineVersion > localVersion) {
            if (this.mOnApkUpdateListerner != null) {
                this.mOnApkUpdateListerner.haveUpdate(true);
            }
            if (this.mOnShowDialogListerner != null) {
                this.mOnShowDialogListerner.showDialog(dto, savePath);
            }
        } else if (this.mOnApkUpdateListerner != null) {
            this.mOnApkUpdateListerner.haveUpdate(false);
        }
    }

    public void showDialog(ApkVersionDto dto, String savePath) {
        this.mApkVerisonView.showNewApkVersionDialog(dto, savePath);
    }

    public void setOnApkUpdateListerner(onApkUpdateListerner onApkUpdateListerner2) {
        this.mOnApkUpdateListerner = onApkUpdateListerner2;
    }

    public void setOnShowDialogListerner(onShowDialogListerner onShowDialogListerner2) {
        this.mOnShowDialogListerner = onShowDialogListerner2;
    }

    /* loaded from: classes.dex */
    public interface onApkUpdateListerner {
        void haveUpdate(boolean z);
    }

    /* loaded from: classes.dex */
    public interface onShowDialogListerner {
        void showDialog(ApkVersionDto apkVersionDto, String str);
    }
}
