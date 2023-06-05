package com.fimi.host;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fimi.host.Entity.DownloadFwSelectInfo;
import com.fimi.host.common.ProductEnum;
import com.fimi.kernel.Constants;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.network.entity.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class HostConstants {
    public static final String APP_ID = "2882303761517518920";
    public static final String APP_KEY = "5761751863920";
    public static final String BUCKET_NAME = "bucket-app-us";
    public static final String CHANEL_EMAIL = "1";
    public static final String CHANEL_PHONE = "0";
    public static final int CHECK_UPDATE = 2;
    public static final String FDS_BASE_URI = "https://awsusor0.fds.api.xiaomi.com";
    public static final String FORMATDATE = "yyyy-MM-dd HH:mm:ss";
    public static final int NO_CHECK_UPDATE = 1;
    public static final String OAUTH_APP_ID = "2882303761517518920";
    public static final String OAUTH_MAC_ALGORITHM = "HmacSHA1";
    public static final String OAUTH_PROVIDER = "XiaoMi";
    public static final String SIMPLEFORMATDATE = "MM-dd HH:mm";
    public static final String SP_KEY_CLOUD_VERSION = "sp_key_cloud_version";
    public static final String SP_KEY_FCVERSION = "sp_key_fc_verion";
    public static final String SP_KEY_HAND_VERSION = "sp_key_hand_version";
    public static final String SP_KEY_LOCAL_FIRMWARE_DETAIL = "sp_key_local_firmware_detail";
    public static final String SP_KEY_MCUVERSION = "sp_key_mcu_verion";
    public static final String SP_KEY_NET_FIRMWARE_DETAIL = "sp_key_net_firmware_detail";
    public static final String SP_KEY_NOT_TIPS = "sp_key_not_tips";
    public static final String SP_KEY_SYSVERION = "Sp_key_sys_Verion";
    public static final String SP_KEY_UPDATE_CHECK = "sp_key_update_check";
    public static final String SP_KEY_UPDATE_ZONE_FW = "sp_key_update_fw";
    public static final String SP_KEY_USER_DETAIL = "sp_key_user_detail";
    public static final String SP_KEY_USER_INFO_EMAIL_OR_IPHONE = "sp_key_user_info_email_or_iphone";
    public static final String SP_KEY_USER_INFO_FLAG = "sp_key_user_info_flag";
    public static final String SP_NOT_NEW_HAND = "sp_new_hand";
    public static final int SYSID_CLOUD_CONTROL = 3;
    public static final int SYSID_CLOUD_CONTROL_MODEL = 5;
    public static final int SYSID_CLOUD_CONTROL_Z = 8;
    public static final int SYSID_CLOUD_CONTROL_Z_MODEL = 5;
    public static final String USER_PROTOCOL = "user_protocol";
    public static final String VERIFYCODE_FORGET_PASSWORD = "2";
    public static final String VERIFYCODE_FUNC_FINDPWD = "1";
    public static final String VERIFYCODE_FUNC_LOGIN = "0";
    private static final String TAG = "HostConstants";
    public static String GET_APP_SETTING = null;
    public static String HostURL = null;
    public static String NEW_APK_URL = null;
    public static String QUESET_FDS_URL = null;
    public static String RIGHT_APPLY_V1 = null;
    public static String SAVE_FDS_URL_2_FIMI_URL = null;
    public static String SAVE_FDS_URL_2_FIMI_URL_PLAYBACK = null;
    public static String USER_LOGIN_URL = null;
    public static String USER_LOGIN_URL_V2 = null;
    public static HashMap urlDatas;

    public static void initUrl() {
        String url;
        ServiceItem serviceItem = SPStoreManager.getInstance().getObject(Constants.SERVICE_ITEM_KEY, ServiceItem.class);
        if (serviceItem == null) {
            url = ServiceItem.newusService;
        } else {
            url = serviceItem.getServiceUrl();
        }
        LogUtil.e("initUrl", url + "======================");
        HostURL = url;
        USER_LOGIN_URL = HostURL + "v1/user/";
        NEW_APK_URL = HostURL + "v1/firmware/";
        USER_LOGIN_URL_V2 = HostURL + "v2/user/";
        RIGHT_APPLY_V1 = HostURL + "v1/rightcenter/";
        QUESET_FDS_URL = HostURL + "xmfds/";
        SAVE_FDS_URL_2_FIMI_URL = HostURL + "v1/log/";
        SAVE_FDS_URL_2_FIMI_URL_PLAYBACK = HostURL + "v1/fly/";
        GET_APP_SETTING = HostURL + "v1/setting/";
    }

    public static void setHostURL(String hostURL) {
        HostURL = hostURL;
    }

    public static void saveFirmwareDetail(List<UpfirewareDto> fwDtos) {
        SPStoreManager.getInstance().saveListObject(SP_KEY_NET_FIRMWARE_DETAIL, fwDtos);
    }

    public static List<UpfirewareDto> getFirmwareDetail() {
        return SPStoreManager.getInstance().getListObject(SP_KEY_NET_FIRMWARE_DETAIL, UpfirewareDto.class);
    }

    public static List<UpfirewareDto> getZoneFirmwareDetail() {
        return SPStoreManager.getInstance().getListObject(SP_KEY_UPDATE_ZONE_FW, UpfirewareDto.class);
    }

    public static void saveLocalFirmware(LocalFwEntity entity) {
        if (entity != null) {
            List<LocalFwEntity> cacheList = getLocalFwEntitys();
            ArrayList arrayList = new ArrayList();
            LocalFwEntity remove = null;
            Iterator<LocalFwEntity> it = cacheList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                LocalFwEntity local = it.next();
                if (local.getType() == entity.getType() && local.getModel() == entity.getModel()) {
                    remove = local;
                    break;
                }
            }
            if (remove != null) {
                cacheList.remove(remove);
            }
            cacheList.add(entity);
            arrayList.addAll(cacheList);
            SPStoreManager.getInstance().saveListObject(SP_KEY_LOCAL_FIRMWARE_DETAIL, arrayList);
        }
    }

    public static List<LocalFwEntity> getLocalFwEntitys() {
        List<LocalFwEntity> localFwEntityList = SPStoreManager.getInstance().getListObject(SP_KEY_LOCAL_FIRMWARE_DETAIL, LocalFwEntity.class);
        if (localFwEntityList == null) {
            return new ArrayList<>();
        }
        return localFwEntityList;
    }

    public static void clearLocalFwEntitys() {
        SPStoreManager.getInstance().saveListObject(SP_KEY_LOCAL_FIRMWARE_DETAIL, new ArrayList());
    }

    /* JADX WARN: Code restructure failed: missing block: B:419:0x011b, code lost:
        r19.add(r20);
     */
    public static List<UpfirewareDto> getNeedDownFw() {
        boolean isDownZone;
        boolean isDownZone2;
        List<UpfirewareDto> netFws = getFirmwareDetail();
        if (netFws == null) {
            return new ArrayList();
        }
        List<UpfirewareDto> zoneFws = new ArrayList<>();
        List<UpfirewareDto> normalFws = new ArrayList<>();
        for (UpfirewareDto fw : netFws) {
            if (fw.getEndVersion() > 0) {
                zoneFws.add(fw);
            } else {
                normalFws.add(fw);
            }
        }
        Log.i("moweiru", "zoneFws:" + zoneFws.size() + ";normalFws:" + normalFws.size());
        ArrayList<UpfirewareDto> arrayList = new ArrayList();
        ArrayList<UpfirewareDto> arrayList2 = new ArrayList();
        List<LocalFwEntity> localFws = getLocalFwEntitys();
        List<UpfirewareDto> needDownFw = new ArrayList<>();
        if (netFws != null && netFws.size() > 0) {
            Iterator<UpfirewareDto> it = zoneFws.iterator();
            while (it.hasNext()) {
                UpfirewareDto upfireDto = it.next();
                for (LocalFwEntity localFwEntity : localFws) {
                    if (upfireDto.getType() == localFwEntity.getType() && upfireDto.getModel() == localFwEntity.getModel()) {
                        boolean normalUpdate = localFwEntity.getLogicVersion() < upfireDto.getLogicVersion() && "0".equals(upfireDto.getForceSign());
                        boolean forceUpdate = localFwEntity.getLogicVersion() < upfireDto.getLogicVersion() && "2".equals(upfireDto.getForceSign());
                        boolean ingoreUpdate = upfireDto.getLogicVersion() != localFwEntity.getLogicVersion() && "1".equals(upfireDto.getForceSign());
                        if (upfireDto.getStartVersion() == 0 || upfireDto.getEndVersion() == 0) {
                            isDownZone2 = true;
                        } else {
                            isDownZone2 = localFwEntity.getLogicVersion() >= ((long) upfireDto.getStartVersion()) && localFwEntity.getLogicVersion() <= ((long) upfireDto.getEndVersion());
                        }
                        if (normalUpdate || forceUpdate || ingoreUpdate) {
                        }
                    }
                }
            }
        }
        if (netFws != null && netFws.size() > 0) {
            Iterator<UpfirewareDto> it2 = normalFws.iterator();
            while (it2.hasNext()) {
                UpfirewareDto upfireDto2 = it2.next();
                for (LocalFwEntity localFwEntity2 : localFws) {
                    if (upfireDto2.getType() == localFwEntity2.getType() && upfireDto2.getModel() == localFwEntity2.getModel()) {
                        boolean normalUpdate2 = localFwEntity2.getLogicVersion() < upfireDto2.getLogicVersion() && "0".equals(upfireDto2.getForceSign());
                        boolean forceUpdate2 = localFwEntity2.getLogicVersion() < upfireDto2.getLogicVersion() && "2".equals(upfireDto2.getForceSign());
                        boolean ingoreUpdate2 = upfireDto2.getLogicVersion() != localFwEntity2.getLogicVersion() && "1".equals(upfireDto2.getForceSign());
                        if (upfireDto2.getStartVersion() == 0 || upfireDto2.getEndVersion() == 0) {
                            isDownZone = true;
                        } else {
                            isDownZone = localFwEntity2.getLogicVersion() >= ((long) upfireDto2.getStartVersion()) && localFwEntity2.getLogicVersion() <= ((long) upfireDto2.getEndVersion());
                        }
                        if (normalUpdate2 || forceUpdate2 || ingoreUpdate2) {
                        }
                    }
                }
            }
        }
        if (arrayList2.size() > 0 && arrayList.size() > 0) {
            for (UpfirewareDto dto1 : arrayList2) {
                for (UpfirewareDto dto2 : arrayList) {
                    boolean isAdd = dto1.getType() == dto2.getType() && dto1.getModel() == dto2.getModel();
                    if (!isAdd) {
                        needDownFw.add(dto1);
                    }
                }
            }
            needDownFw.addAll(arrayList);
        } else if (arrayList2.size() > 0 && arrayList.size() <= 0) {
            needDownFw.addAll(arrayList2);
        } else {
            needDownFw.addAll(arrayList);
        }
        SPStoreManager.getInstance().saveObject(SP_KEY_UPDATE_ZONE_FW, needDownFw);
        Log.i("moweiru", "updateNormalFws:" + arrayList2.size() + ";updateZoneFws:" + arrayList.size());
        List<UpfirewareDto> lastDownFw = new ArrayList<>();
        for (UpfirewareDto dto : needDownFw) {
            String localMdf5 = DirectoryPath.getFileOfMd5(DirectoryPath.getFwPath(dto.getSysName()));
            if (!dto.getFileEncode().equalsIgnoreCase(localMdf5)) {
                lastDownFw.add(dto);
            }
        }
        return lastDownFw;
    }

    public static List<UpfirewareDto> getNeedDownFw(boolean isWifiAuto, List<DownloadFwSelectInfo> selectList) {
        List<UpfirewareDto> list = new ArrayList<>();
        list.addAll(getNeedDownFw());
        if (!isWifiAuto) {
            Iterator<UpfirewareDto> it = list.iterator();
            while (it.hasNext()) {
                UpfirewareDto dto = it.next();
                if (!iteratorProductSelectList(dto, selectList)) {
                    it.remove();
                }
            }
        }
        return list;
    }

    public static boolean isX9ForceUpdate(List<UpfirewareDto> needownFws) {
        if (needownFws != null && needownFws.size() > 0) {
            for (UpfirewareDto dto : needownFws) {
                if ("2".equals(dto.getForceSign()) && ((4 == dto.getModel() && dto.getType() == 0) || (dto.getModel() == 0 && 13 == dto.getType()))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isGh2ForceUpdate(List<UpfirewareDto> needownFws) {
        if (needownFws != null && needownFws.size() > 0) {
            for (UpfirewareDto dto : needownFws) {
                if ("2".equals(dto.getForceSign()) && ((5 == dto.getModel() && 3 == dto.getType()) || (5 == dto.getModel() && 8 == dto.getType()))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isForceUpdate(List<UpfirewareDto> needownFws) {
        if (needownFws != null && needownFws.size() > 0) {
            for (UpfirewareDto dto : needownFws) {
                if ("2".equals(dto.getForceSign())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static List<UpfirewareDto> getDownFinishedFw() {
        List<UpfirewareDto> downFinishedFw = new ArrayList<>();
        List<UpfirewareDto> netFws = getFirmwareDetail();
        if (netFws != null && netFws.size() > 0) {
            for (UpfirewareDto u : netFws) {
                String md5 = DirectoryPath.getFileOfMd5(DirectoryPath.getFwPath(u.getSysName()));
                if (u.getFileEncode().equalsIgnoreCase(md5)) {
                    downFinishedFw.add(u);
                }
            }
        }
        return downFinishedFw;
    }

    public static List<UpfirewareDto> getDownZoneFinishedFw() {
        List<UpfirewareDto> downFinishedFw = new ArrayList<>();
        List<UpfirewareDto> netFws = getZoneFirmwareDetail();
        if (netFws != null && netFws.size() > 0) {
            for (UpfirewareDto u : netFws) {
                String md5 = DirectoryPath.getFileOfMd5(DirectoryPath.getFwPath(u.getSysName()));
                if (u.getFileEncode().equalsIgnoreCase(md5)) {
                    downFinishedFw.add(u);
                }
            }
        }
        return downFinishedFw;
    }

    public static void saveUserDetail(Object userjson) {
        SPStoreManager.getInstance().saveObject(SP_KEY_USER_DETAIL, userjson);
    }

    public static UserDto getUserDetail() {
        String userDtoStr = SPStoreManager.getInstance().getString(SP_KEY_USER_DETAIL);
        if (TextUtils.isEmpty(userDtoStr)) {
            return new UserDto();
        }
        UserDto dto = JSON.parseObject(userDtoStr, UserDto.class);
        if (dto == null) {
            return new UserDto();
        }
        return dto;
    }

    public static void saveUserInfo(String flag, String iphoneOrEmail) {
        if (flag != null) {
            SPStoreManager.getInstance().saveString(SP_KEY_USER_INFO_FLAG, flag);
            SPStoreManager.getInstance().saveString(SP_KEY_USER_INFO_EMAIL_OR_IPHONE, iphoneOrEmail);
            return;
        }
        SPStoreManager.getInstance().removeKey(SP_KEY_USER_INFO_FLAG);
        SPStoreManager.getInstance().removeKey(SP_KEY_USER_INFO_EMAIL_OR_IPHONE);
    }

    public static List<DownloadFwSelectInfo> getDownloadFwSelectInfoList() {
        ProductEnum[] values;
        List<DownloadFwSelectInfo> infoList = new ArrayList<>();
        for (ProductEnum s : ProductEnum.values()) {
            DownloadFwSelectInfo mDownloadFwSelectInfo = new DownloadFwSelectInfo();
            mDownloadFwSelectInfo.setProduct(s);
            infoList.add(mDownloadFwSelectInfo);
        }
        List<UpfirewareDto> mList = getNeedDownFw();
        for (UpfirewareDto dto : mList) {
            iteratorProductList(dto, infoList);
        }
        return infoList;
    }

    public static void iteratorProductList(UpfirewareDto dto, List<DownloadFwSelectInfo> infoList) {
        for (DownloadFwSelectInfo info : infoList) {
            if (info.getProduct() == ProductEnum.GH2) {
                if ((dto.getType() == 3 && dto.getModel() == 5) || (dto.getType() == 8 && dto.getModel() == 5)) {
                    updateDownloadFwSelectInfo(dto, info);
                    return;
                }
            } else if (info.getProduct() == ProductEnum.X9) {
                if ((dto.getType() == 0 && dto.getModel() == 4) || (dto.getType() == 13 && dto.getModel() == 0)) {
                    updateDownloadFwSelectInfo(dto, info);
                    return;
                }
            } else if (info.getProduct() == ProductEnum.X8S && isX8sFireware(dto)) {
                updateDownloadFwSelectInfo(dto, info);
                return;
            }
        }
    }

    public static boolean isX8sFireware(UpfirewareDto dto) {
        if (dto.getType() == 0 && dto.getModel() == 3) {
            return true;
        }
        if (dto.getType() == 1 && dto.getModel() == 3) {
            return true;
        }
        if (dto.getType() == 9 && dto.getModel() == 1) {
            return true;
        }
        if (dto.getType() == 11 && dto.getModel() == 3) {
            return true;
        }
        if (dto.getType() == 12 && dto.getModel() == 3) {
            return true;
        }
        if (dto.getType() == 14 && dto.getModel() == 0) {
            return true;
        }
        if (dto.getType() == 3 && dto.getModel() == 6) {
            return true;
        }
        if (dto.getType() == 5 && dto.getModel() == 3) {
            return true;
        }
        if (dto.getType() == 10 && dto.getModel() == 3) {
            return true;
        }
        if (dto.getType() == 4 && dto.getModel() == 2) {
            return true;
        }
        return dto.getType() == 13 && dto.getModel() == 1;
    }

    public static boolean iteratorProductSelectList(UpfirewareDto dto, List<DownloadFwSelectInfo> infoList) {
        for (DownloadFwSelectInfo info : infoList) {
            if (info.getProduct() == ProductEnum.GH2) {
                if ((dto.getType() == 3 && dto.getModel() == 5) || (dto.getType() == 8 && dto.getModel() == 5)) {
                    return true;
                }
            } else if (info.getProduct() == ProductEnum.X9) {
                if ((dto.getType() == 0 && dto.getModel() == 4) || (dto.getType() == 13 && dto.getModel() == 0)) {
                    return true;
                }
            } else if (info.getProduct() == ProductEnum.X8S && isX8sFireware(dto)) {
                return true;
            }
        }
        return false;
    }

    private static void updateDownloadFwSelectInfo(UpfirewareDto dto, DownloadFwSelectInfo info) {
        if ("2".equals(dto.getForceSign())) {
            info.setForceSign(true);
        }
        info.add2List(dto);
        info.addDetail(dto.getUpdateContent());
        info.setFileSize(dto.getFileSize());
    }

    public static String getFcVersion() {
        return SPStoreManager.getInstance().getString(SP_KEY_FCVERSION);
    }

    public static void setFcVersion(String fcVersion) {
        SPStoreManager.getInstance().saveString(SP_KEY_FCVERSION, fcVersion);
    }

    public static String getMcuVersion() {
        return SPStoreManager.getInstance().getString(SP_KEY_MCUVERSION);
    }

    public static void setMcuVersion(String mcuVersion) {
        SPStoreManager.getInstance().saveString(SP_KEY_MCUVERSION, mcuVersion);
    }

    public static String getSysVersion() {
        return SPStoreManager.getInstance().getString(SP_KEY_SYSVERION);
    }

    public static void setSysVersion(String sysVersion) {
        SPStoreManager.getInstance().saveString(SP_KEY_SYSVERION, sysVersion);
    }

    public static int getCloudVersion() {
        return SPStoreManager.getInstance().getInt(SP_KEY_CLOUD_VERSION, 0);
    }

    public static void setCloudVersion(int cloudVersion) {
        SPStoreManager.getInstance().saveInt(SP_KEY_CLOUD_VERSION, cloudVersion);
    }

    public static int getHandVersion() {
        return SPStoreManager.getInstance().getInt(SP_KEY_HAND_VERSION, 0);
    }

    public static void setHandVersion(int cloudVersion) {
        SPStoreManager.getInstance().saveInt(SP_KEY_HAND_VERSION, cloudVersion);
    }

    public void initDataUrl(int[] ints) {
        urlDatas = new HashMap();
        for (int i = 0; ints.length < i; i++) {
            urlDatas.put(Integer.valueOf(ints[i]), "");
        }
    }
}
