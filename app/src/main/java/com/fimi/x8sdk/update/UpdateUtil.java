package com.fimi.x8sdk.update;

import android.content.Context;

import com.fimi.android.app.R;
import com.fimi.host.HostConstants;
import com.fimi.host.LocalFwEntity;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.x8sdk.update.fwpack.FwInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class UpdateUtil {
    public static List<UpfirewareDto> getUpfireDtos() {
        List<UpfirewareDto> list = HostConstants.getDownZoneFinishedFw();
        List<UpfirewareDto> upfirewareDtoList = new ArrayList<>();
        List<LocalFwEntity> entitys = HostConstants.getLocalFwEntitys();
        if (list.size() > 0) {
            Iterator<UpfirewareDto> it = list.iterator();
            while (it.hasNext()) {
                UpfirewareDto dto = it.next();
                if (entitys.size() > 0 && ((dto.getType() == 0 && dto.getModel() == 3) || ((dto.getType() == 1 && dto.getModel() == 3) || ((dto.getType() == 9 && dto.getModel() == 1) || ((dto.getType() == 11 && dto.getModel() == 3) || ((dto.getType() == 12 && dto.getModel() == 3) || ((dto.getType() == 14 && dto.getModel() == 0) || ((dto.getType() == 3 && dto.getModel() == 6) || ((dto.getType() == 5 && dto.getModel() == 3) || ((dto.getType() == 10 && dto.getModel() == 3) || ((dto.getType() == 4 && dto.getModel() == 2) || (dto.getType() == 13 && dto.getModel() == 1)))))))))))) {
                    Iterator<LocalFwEntity> it2 = entitys.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            LocalFwEntity localFwEntity = it2.next();
                            if ((localFwEntity.getType() == 0 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 1 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 9 && localFwEntity.getModel() == 1) || ((localFwEntity.getType() == 11 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 12 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 14 && localFwEntity.getModel() == 0) || ((localFwEntity.getType() == 3 && localFwEntity.getModel() == 6) || ((localFwEntity.getType() == 5 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 10 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 4 && localFwEntity.getModel() == 2) || (localFwEntity.getType() == 13 && localFwEntity.getModel() == 1))))))))))) {
                                if (localFwEntity.getType() == dto.getType() && localFwEntity.getModel() == dto.getModel()) {
                                    boolean normalUpdate = localFwEntity.getLogicVersion() < dto.getLogicVersion() && "0".equals(dto.getForceSign());
                                    boolean forceUpdate = localFwEntity.getLogicVersion() < dto.getLogicVersion() && "2".equals(dto.getForceSign());
                                    boolean ingoreUpdate = localFwEntity.getLogicVersion() != dto.getLogicVersion() && "1".equals(dto.getForceSign());
                                    boolean isUpdateZone = dto.getEndVersion() == 0 || (localFwEntity.getLogicVersion() <= ((long) dto.getEndVersion()) && localFwEntity.getLogicVersion() >= ((long) dto.getStartVersion()));
                                    if (normalUpdate || forceUpdate || ingoreUpdate) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return upfirewareDtoList;
    }


    public static boolean isForceUpdate() {
        List<UpfirewareDto> upfirewareDtos = getUpfireDtos();
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

    public static List<FwInfo> toFwInfo() {
        List<FwInfo> fws = new ArrayList<>();
        List<UpfirewareDto> upfirewareDtos = getUpfireDtos();
        for (UpfirewareDto upfirewareDto : upfirewareDtos) {
            FwInfo fwInfo = new FwInfo();
            fwInfo.setModelId((byte) upfirewareDto.getModel());
            fwInfo.setTypeId((byte) upfirewareDto.getType());
            fwInfo.setFwType(Byte.parseByte(upfirewareDto.getForceSign()));
            fwInfo.setSysName(upfirewareDto.getSysName());
            fwInfo.setSoftwareVer((short) upfirewareDto.getLogicVersion());
            fws.add(fwInfo);
        }
        return fws;
    }

    public static List<UpfirewareDto> getServerFwInfo() {
        return HostConstants.getFirmwareDetail();
    }

    public static int getErrorCodeString(Context context, int result) {
        switch ((byte) result) {
            case -1:
                int strId = R.string.x8_error_code_update_255;
                return strId;
            case 0:
                int strId2 = R.string.x8_error_code_update_0;
                return strId2;
            case 1:
                int strId3 = R.string.x8_error_code_update_1;
                return strId3;
            case 2:
                int strId4 = R.string.x8_error_code_update_2;
                return strId4;
            case 3:
                int strId5 = R.string.x8_error_code_update_3;
                return strId5;
            case 4:
                int strId6 = R.string.x8_error_code_update_4;
                return strId6;
            case 5:
                int strId7 = R.string.x8_error_code_update_5;
                return strId7;
            case 6:
                int strId8 = R.string.x8_error_code_update_6;
                return strId8;
            case 7:
                int strId9 = R.string.x8_error_code_update_7;
                return strId9;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            default:
                return 0;
            case 33:
                int strId10 = R.string.x8_error_code_update_21;
                return strId10;
            case 34:
                int strId11 = R.string.x8_error_code_update_22;
                return strId11;
            case 35:
                int strId12 = R.string.x8_error_code_update_23;
                return strId12;
            case 36:
                int strId13 = R.string.x8_error_code_update_24;
                return strId13;
            case 37:
                int strId14 = R.string.x8_error_code_update_25;
                return strId14;
            case 38:
                int strId15 = R.string.x8_error_code_update_26;
                return strId15;
            case 39:
                int strId16 = R.string.x8_error_code_update_27;
                return strId16;
            case 40:
                int strId17 = R.string.x8_error_code_update_28;
                return strId17;
            case 41:
                int strId18 = R.string.x8_error_code_update_29;
                return strId18;
        }
    }
}
