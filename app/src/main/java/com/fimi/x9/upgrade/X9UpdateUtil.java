package com.fimi.x9.upgrade;

import androidx.annotation.NonNull;

import com.fimi.host.HostConstants;
import com.fimi.host.LocalFwEntity;
import com.fimi.kernel.animutils.IOUtils;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.network.entity.UpfirewareDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class X9UpdateUtil {
    public static List<UpfirewareDto> list = null;
    static List<UpfirewareDto> downList = new ArrayList<>();

    public static int getLogicVersion(String version) {
        if (version == null || version.equals("")) {
            return -1;
        }
        try {
            int index = version.toLowerCase().lastIndexOf("sp");
            String str = version.substring(index - 2, index) + version.substring(index + 2, index + 4);
            return Integer.parseInt(str);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getX9FLogicVersion(String version) {
        if (version == null || version.equals("")) {
            return "-1";
        }
        try {
            int index = version.toLowerCase().lastIndexOf("sp");
            int index2 = version.lastIndexOf("-Release");
            System.out.println("index2:" + index2);
            return version.substring(index - 2, index) + version.substring(index + 2, index + 4) + version.charAt(index2 - 1);
        } catch (Exception e) {
            return version;
        }
    }

    @NonNull
    public static List<UpfirewareDto> getNeedUpgrade() {
        List<UpfirewareDto> upfirewareDtos = HostConstants.getDownZoneFinishedFw();
        List<UpfirewareDto> needUpdateDtos = new ArrayList<>();
        List<LocalFwEntity> entitys = HostConstants.getLocalFwEntitys();
        if (upfirewareDtos.size() > 0) {
            Iterator<UpfirewareDto> it = upfirewareDtos.iterator();
            while (it.hasNext()) {
                UpfirewareDto dto = it.next();
                if (entitys.size() > 0 && ((dto.getType() == 13 && dto.getModel() == 0) || (dto.getType() == 0 && dto.getModel() == 4))) {
                    Iterator<LocalFwEntity> it2 = entitys.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            LocalFwEntity l = it2.next();
                            if ((l.getType() == 13 && l.getModel() == 0) || (l.getType() == 0 && l.getModel() == 4)) {
                                if (l.getType() == dto.getType() && l.getModel() == dto.getModel()) {
                                    boolean normalUpdate = l.getLogicVersion() < dto.getLogicVersion() && "0".equals(dto.getForceSign());
                                    boolean forceUpdate = l.getLogicVersion() < dto.getLogicVersion() && "2".equals(dto.getForceSign());
                                    boolean ingoreUpdate = l.getLogicVersion() != dto.getLogicVersion() && "1".equals(dto.getForceSign());
                                    boolean isUpdateZone = dto.getEndVersion() == 0 || (l.getLogicVersion() <= ((long) dto.getEndVersion()) && l.getLogicVersion() >= ((long) dto.getStartVersion()));
                                    if (normalUpdate || forceUpdate || ingoreUpdate) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        FirmwareComparator comparator = new FirmwareComparator();
        Collections.sort(needUpdateDtos, comparator);
        return needUpdateDtos;
    }

    public static int getMucDownloadVersion() {
        List<UpfirewareDto> upfirewareDtos = HostConstants.getDownFinishedFw();
        for (UpfirewareDto dto : upfirewareDtos) {
            if (dto.getType() == 13 && dto.getModel() == 0) {
                return (int) dto.getLogicVersion();
            }
        }
        return 0;
    }

    public static int getNeedUpgradeSize() {
        return getNeedUpgrade().size();
    }

    @NonNull
    public static String getUpdateContent() {
        StringBuilder sb = new StringBuilder();
        List<UpfirewareDto> needUpgradeDto = getNeedUpgrade();
        if (needUpgradeDto.size() > 0) {
            int i = 0;
            for (UpfirewareDto dto : needUpgradeDto) {
                i++;
                sb.append(i).append(".").append(dto.getUpdateContent()).append(IOUtils.LINE_SEPARATOR_UNIX);
            }
        }
        return sb.toString();
    }

    public static List<UpfirewareDto> getDownList() {
        return downList;
    }

    public static void setDownList(List<UpfirewareDto> downList2) {
        downList = downList2;
    }

    @NonNull
    public static String getFirmwareSiZe() {
        double allFileSizeM = ((getUpdateFirmwareSize() * 1.0d) / 1024.0d) / 1024.0d;
        return NumberUtil.decimalPointStr(allFileSizeM, 2) + "M";
    }

    public static long getUpdateFirmwareSize() {
        List<UpfirewareDto> needUpgradeDto = getNeedUpgrade();
        long allFileSize = 0;
        if (needUpgradeDto.size() > 0) {
            for (UpfirewareDto dto : needUpgradeDto) {
                allFileSize += dto.getFileSize();
            }
        }
        return allFileSize;
    }

    public static boolean isForceUpdate() {
        List<UpfirewareDto> upfirewareDtos = getNeedUpgrade();
        if (upfirewareDtos.size() <= 0) {
            return false;
        }
        for (UpfirewareDto dto : upfirewareDtos) {
            if ("2".equalsIgnoreCase(dto.getForceSign())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(getLogicVersion("X9-C-V010SP19RTM171019R11513-CN-Release"));
        System.out.println(getLogicVersion("X9-M-V012SP05RTM170920RB8800B3-CN-Release"));
    }

    public static class FirmwareComparator implements Comparator {
        @Override
        public int compare(Object object1, Object object2) {
            UpfirewareDto firmwareInfo1 = (UpfirewareDto) object1;
            UpfirewareDto firmwareInfo2 = (UpfirewareDto) object2;
            return firmwareInfo1.getType() < firmwareInfo2.getType() ? 1 : -1;
        }
    }
}
