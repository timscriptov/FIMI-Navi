package com.fimi.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.fimi.TcpClient;
import com.fimi.android.app.R;
import com.fimi.app.interfaces.IProductControllers;
import com.fimi.app.ui.main.HostNewMainActivity;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.host.HostConstants;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.AppBlockCanaryContext;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.permission.PermissionManager;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.libdownfw.ivew.IFirmwareDownView;
import com.fimi.libdownfw.update.DownloadFwSelectActivity;
import com.fimi.network.DownFwService;
import com.fimi.network.DownNoticeMananger;
import com.fimi.network.IDownProgress;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.widget.DialogManager;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.map.MapType;
import com.github.moduth.blockcanary.BlockCanary;

import java.util.ArrayList;
import java.util.List;

import router.Router;


public class HostMainPresenter implements IDownProgress {
    static List<UpfirewareDto> list = new ArrayList();
    private final Context mContext;
    DialogManager dialogManager;
    IFirmwareDownView iFirmwareDownView;
    boolean isProviderEnabled;
    int position = 0;
    private LocationManager lm;
    private IProductControllers productControler;

    public HostMainPresenter(Context mContext, IFirmwareDownView iFirmwareDownView) {
        this.mContext = mContext;
        this.iFirmwareDownView = iFirmwareDownView;
        initSessionAndNotice();
        initTTs();
        initDownListener();
    }

    public static void checkUpfireList() {
        list = HostConstants.getNeedDownFw();
    }

    public static List<UpfirewareDto> getUpfireList() {
        return list;
    }

    public static boolean isDownFirmwareTip() {
        return (list != null && list.size() > 0 && DownFwService.getState().equals(DownFwService.DownState.UnStart)) || DownFwService.getState().equals(DownFwService.DownState.StopDown);
    }

    private void initDownListener() {
        DownNoticeMananger.getDownNoticManger().addDownNoticeList(this);
    }

    public boolean isForce() {
        boolean isForce = HostConstants.isForceUpdate(list);
        return isForce;
    }

    private void initTTs() {
    }

    private void initSessionAndNotice() {
        SessionManager.initInstance();
        NoticeManager.initInstance();
    }

    public void requestPermissions() {
        PermissionManager.requestStoragePermissions();
    }

    public void requestX9Permissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            PermissionManager.checkRequiredPermission(activity);
        }
    }

    public void go2DownSelectActivty() {
        if (HostConstants.getNeedDownFw().size() > 0) {
            Intent intent = new Intent(this.mContext, DownloadFwSelectActivity.class);
            this.mContext.startActivity(intent);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 7) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                toGh2MainActivity(false);
            }
        } else if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                PermissionManager.requestRecordAudioPermissions();
            } else {
                PermissionManager.requestRecordAudioPermissions();
            }
        } else if (requestCode == 8) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                PermissionManager.requestCameraPermissions();
            } else {
                PermissionManager.requestCameraPermissions();
            }
        } else if (requestCode == 1314) {
            gh2PermissionDetection();
        } else if (requestCode == 9) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != 0) {
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) this.mContext, permissions[i]);
                }
            }
        }
    }

    private void initSDK() {
        BlockCanary.install(this.mContext, new AppBlockCanaryContext()).start();
        X8AiConfig.getInstance().init();
        GlobalConfig.Builder builder = new GlobalConfig.Builder();
        builder.setMapType(SPStoreManager.getInstance().getBoolean(Constants.X8_MAP_OPTION, false) ? MapType.AMap : MapType.GoogleMap).setMapStyle(SPStoreManager.getInstance().getInt(Constants.X8_MAP_STYLE)).setRectification(SPStoreManager.getInstance().getBoolean(Constants.X8_MAP_RECTIFYIN_OPTION, true)).setShowLog(SPStoreManager.getInstance().getBoolean(Constants.X8_SHOW_LOG_OPTION, true)).setShowmMtric(SPStoreManager.getInstance().getBoolean(Constants.X8_UNITY_OPTION, true)).setGridLine(SPStoreManager.getInstance().getInt(Constants.X8_GLINE_LINE_OPTION));
        GlobalConfig.getInstance().init(builder);
        X8NumberUtil.resetPrexString(this.mContext);
        TcpClient.createInit();
    }

    private void startX8s() {
        SPStoreManager.getInstance().saveBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE_SETTING, false);
        boolean b = SPStoreManager.getInstance().getBoolean(Constants.X8_MAP_OPTION, false);
        GlobalConfig.getInstance().setMapType(b ? MapType.AMap : MapType.GoogleMap);
        Intent it = new Intent(this.mContext, X8sMainActivity.class);
        this.mContext.startActivity(it);
    }

    public void onConnectDevice() {
        if (com.fimi.kernel.Constants.productType == ProductEnum.X8S) {
            startX8s();
        } else if (com.fimi.kernel.Constants.productType == ProductEnum.FIMIAPP) {
            if (this.position == 0) {
                startX8s();
            } else if (this.position == 1) {
                if (PermissionManager.isLocationEnabled(this.mContext)) {
                    if (!PermissionManager.hasLocaltionPermissions()) {
                        showLocaltionPermissionDialog();
                        return;
                    }
                    boolean isStartBegnnerGuide = SPStoreManager.getInstance().getBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE);
                    this.productControler.stopAnimation();
                    if (isStartBegnnerGuide) {
                        Intent it = Router.invoke(this.mContext, "activity://x9.main");
                        this.mContext.startActivity(it);
                        ((Activity) this.mContext).overridePendingTransition(17432576, 17432577);
                        return;
                    }
                    SPStoreManager.getInstance().saveBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE_SETTING, false);
                    Intent it2 = Router.invoke(this.mContext, "activity://x9.guide");
                    this.mContext.startActivity(it2);
                    return;
                }
                showGpsDialog();
            } else {
                gh2PermissionDetection();
            }
        } else if (com.fimi.kernel.Constants.productType == ProductEnum.X9) {
            if (PermissionManager.isLocationEnabled(this.mContext)) {
                if (!PermissionManager.hasLocaltionPermissions()) {
                    showLocaltionPermissionDialog();
                    return;
                }
                boolean isStartBegnnerGuide2 = SPStoreManager.getInstance().getBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE);
                this.productControler.stopAnimation();
                if (isStartBegnnerGuide2) {
                    Intent it3 = Router.invoke(this.mContext, "activity://x9.main");
                    this.mContext.startActivity(it3);
                    ((Activity) this.mContext).overridePendingTransition(17432576, 17432577);
                    return;
                }
                SPStoreManager.getInstance().saveBoolean(com.fimi.kernel.Constants.X9_BEGNNER_GUIDE_SETTING, false);
                Intent it4 = Router.invoke(this.mContext, "activity://x9.guide");
                this.mContext.startActivity(it4);
                return;
            }
            showGpsDialog();
        } else {
            gh2PermissionDetection();
        }
    }

    void gh2PermissionDetection() {
    }

    public void toGh2MainActivity(boolean isProvider) {
        Context context = this.mContext;
        Context context2 = this.mContext;
        this.lm = (LocationManager) context.getSystemService("location");
        this.isProviderEnabled = this.lm.isProviderEnabled("gps");
        if (!this.isProviderEnabled) {
            this.isProviderEnabled = isProvider;
        }
        if (!this.isProviderEnabled) {
            openCameraDialog(this.mContext.getString(R.string.fimi_sdk_open_gps_permission_hint));
            return;
        }
        Intent it = Router.invoke(this.mContext, "activity://gh2.main");
        this.mContext.startActivity(it);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private void openCameraDialog(final String dialoghint) {
        this.dialogManager = new DialogManager(this.mContext, null, dialoghint, this.mContext.getString(R.string.fimi_sdk_go_setting), this.mContext.getString(R.string.cancel));
        this.dialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
            @Override
            public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                if (!dialoghint.equals(HostMainPresenter.this.mContext.getString(R.string.fimi_sdk_open_gps_permission_hint))) {
                    ((Activity) HostMainPresenter.this.mContext).startActivityForResult(new Intent("android.settings.SETTINGS"), 0);
                    return;
                }
                Intent intent = new Intent();
                intent.setAction("android.settings.LOCATION_SOURCE_SETTINGS");
                ((Activity) HostMainPresenter.this.mContext).startActivityForResult(intent, PermissionManager.ACTION_LOCATION_SOURCE_SETTINGS);
            }

            @Override
            public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
                if (dialoghint.equals(HostMainPresenter.this.mContext.getString(R.string.fimi_sdk_open_gps_permission_hint))) {
                    HostMainPresenter.this.isProviderEnabled = true;
                    HostMainPresenter.this.toGh2MainActivity(true);
                }
            }
        });
        if (!((Activity) this.mContext).isFinishing()) {
            this.dialogManager.showDialog();
        }
    }

    @Override
    public void onProgress(DownFwService.DownState downState, int progrss, String fileName) {
        this.iFirmwareDownView.showDownFwProgress(downState, progrss, fileName);
    }

    public void removeNoticDownListener() {
        DownNoticeMananger.getDownNoticManger().remioveDownNoticeList(this);
    }

    public void setProductControler(IProductControllers productControler) {
        this.productControler = productControler;
    }

    public void showGpsDialog() {
        DialogManager gpsDialogManager = new DialogManager(this.mContext, null, this.mContext.getString(R.string.fimi_sdk_open_gps_permission_hint), this.mContext.getString(R.string.fimi_sdk_go_setting), this.mContext.getString(R.string.fimi_sdk_update_ignore));
        gpsDialogManager.setOnDiaLogListener(new DialogManager.OnDialogListener() {
            @Override
            public void dialogBtnRightOrSingleListener(View customView, DialogInterface dialogInterface, int which) {
                Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                HostMainPresenter.this.mContext.startActivity(intent);
            }

            @Override
            public void dialogBtnLeftListener(View customView, DialogInterface dialogInterface, int which) {
            }
        });
        gpsDialogManager.showDialog();
    }

    public void showLocaltionPermissionDialog() {
    }

    public void gotoTeacher(String pattern) {
        if (Build.VERSION.SDK_INT == 23) {
            if (!Settings.System.canWrite(this.mContext)) {
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS", Uri.parse("package:" + this.mContext.getPackageName()));
                HostNewMainActivity activity = (HostNewMainActivity) this.mContext;
                activity.getClass();
                activity.startActivityForResult(intent, 1);
                return;
            }
            Intent intent2 = Router.invoke(this.mContext, pattern);
            this.mContext.startActivity(intent2);
            return;
        }
        Intent intent3 = Router.invoke(this.mContext, pattern);
        this.mContext.startActivity(intent3);
    }
}
