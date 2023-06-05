package com.fimi.kernel.store.sqlite.helper;

import com.fimi.app.x8s.entity.X11CmdConstants;
import com.fimi.kernel.store.sqlite.dao.DataStaticInfoDao;
import com.fimi.kernel.store.sqlite.entity.DataStaticInfo;
import com.fimi.kernel.store.sqlite.helper.core.DbCore;
import com.fimi.kernel.utils.LogUtil;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


public class DataStasicInfoHelper {
    private static final DataStasicInfoHelper sDataStasicInfoHelper = new DataStasicInfoHelper();
    private final DataStaticInfoDao dao = DbCore.getDaoSession().getDataStaticInfoDao();

    public static DataStasicInfoHelper getInstance() {
        return sDataStasicInfoHelper;
    }

    public void addRecord(DataStaticInfo dataStaticInfo) {
        DataStaticInfo unique = this.dao.queryBuilder().where(DataStaticInfoDao.Properties.CurrentTime.eq(dataStaticInfo.getCurrentTime()), new WhereCondition[0]).where(DataStaticInfoDao.Properties.Type.eq(Byte.valueOf(dataStaticInfo.getType())), new WhereCondition[0]).where(DataStaticInfoDao.Properties.DeviceType.eq(Byte.valueOf(dataStaticInfo.getDeviceType())), new WhereCondition[0]).build().unique();
        if (unique != null) {
            LogUtil.i(X11CmdConstants.OPTION_APPSTATUS_RECORD, "addRecord: 1");
            unique.setUseTime(dataStaticInfo.getUseTime());
            unique.setFlyTime(dataStaticInfo.getFlyTime());
            this.dao.update(unique);
            return;
        }
        LogUtil.i(X11CmdConstants.OPTION_APPSTATUS_RECORD, "addRecord: 2");
        this.dao.insert(dataStaticInfo);
    }

    public List<DataStaticInfo> queryX9FlyTime() {
        QueryBuilder<DataStaticInfo> qb = this.dao.queryBuilder();
        qb.where(DataStaticInfoDao.Properties.Type.eq(1), new WhereCondition[0]).where(DataStaticInfoDao.Properties.DeviceType.eq(0));
        List<DataStaticInfo> list = qb.list();
        return list;
    }

    public List<DataStaticInfo> queryX9UseTime() {
        QueryBuilder<DataStaticInfo> qb = this.dao.queryBuilder();
        qb.where(DataStaticInfoDao.Properties.Type.eq(0), new WhereCondition[0]).where(DataStaticInfoDao.Properties.DeviceType.eq(0));
        List<DataStaticInfo> list = qb.list();
        return list;
    }

    public void deleteList(List<DataStaticInfo> notes) {
        this.dao.deleteInTx(notes);
    }
}
