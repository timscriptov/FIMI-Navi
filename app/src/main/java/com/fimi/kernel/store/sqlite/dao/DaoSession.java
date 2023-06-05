package com.fimi.kernel.store.sqlite.dao;

import com.fimi.kernel.store.sqlite.entity.DataStaticInfo;
import com.fimi.kernel.store.sqlite.entity.GH2DataStaticInfo;
import com.fimi.kernel.store.sqlite.entity.MediaDownloadInfo;
import com.fimi.kernel.store.sqlite.entity.Student;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointLatlngInfo;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

/* loaded from: classes.dex */
public class DaoSession extends AbstractDaoSession {
    private final DataStaticInfoDao dataStaticInfoDao;
    private final DaoConfig dataStaticInfoDaoConfig;
    private final GH2DataStaticInfoDao gH2DataStaticInfoDao;
    private final DaoConfig gH2DataStaticInfoDaoConfig;
    private final MediaDownloadInfoDao mediaDownloadInfoDao;
    private final DaoConfig mediaDownloadInfoDaoConfig;
    private final StudentDao studentDao;
    private final DaoConfig studentDaoConfig;
    private final X8AiLinePointInfoDao x8AiLinePointInfoDao;
    private final DaoConfig x8AiLinePointInfoDaoConfig;
    private final X8AiLinePointLatlngInfoDao x8AiLinePointLatlngInfoDao;
    private final DaoConfig x8AiLinePointLatlngInfoDaoConfig;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        super(db);
        this.dataStaticInfoDaoConfig = daoConfigMap.get(DataStaticInfoDao.class).clone();
        this.dataStaticInfoDaoConfig.initIdentityScope(type);
        this.gH2DataStaticInfoDaoConfig = daoConfigMap.get(GH2DataStaticInfoDao.class).clone();
        this.gH2DataStaticInfoDaoConfig.initIdentityScope(type);
        this.mediaDownloadInfoDaoConfig = daoConfigMap.get(MediaDownloadInfoDao.class).clone();
        this.mediaDownloadInfoDaoConfig.initIdentityScope(type);
        this.studentDaoConfig = daoConfigMap.get(StudentDao.class).clone();
        this.studentDaoConfig.initIdentityScope(type);
        this.x8AiLinePointInfoDaoConfig = daoConfigMap.get(X8AiLinePointInfoDao.class).clone();
        this.x8AiLinePointInfoDaoConfig.initIdentityScope(type);
        this.x8AiLinePointLatlngInfoDaoConfig = daoConfigMap.get(X8AiLinePointLatlngInfoDao.class).clone();
        this.x8AiLinePointLatlngInfoDaoConfig.initIdentityScope(type);
        this.dataStaticInfoDao = new DataStaticInfoDao(this.dataStaticInfoDaoConfig, this);
        this.gH2DataStaticInfoDao = new GH2DataStaticInfoDao(this.gH2DataStaticInfoDaoConfig, this);
        this.mediaDownloadInfoDao = new MediaDownloadInfoDao(this.mediaDownloadInfoDaoConfig, this);
        this.studentDao = new StudentDao(this.studentDaoConfig, this);
        this.x8AiLinePointInfoDao = new X8AiLinePointInfoDao(this.x8AiLinePointInfoDaoConfig, this);
        this.x8AiLinePointLatlngInfoDao = new X8AiLinePointLatlngInfoDao(this.x8AiLinePointLatlngInfoDaoConfig, this);
        registerDao(DataStaticInfo.class, this.dataStaticInfoDao);
        registerDao(GH2DataStaticInfo.class, this.gH2DataStaticInfoDao);
        registerDao(MediaDownloadInfo.class, this.mediaDownloadInfoDao);
        registerDao(Student.class, this.studentDao);
        registerDao(X8AiLinePointInfo.class, this.x8AiLinePointInfoDao);
        registerDao(X8AiLinePointLatlngInfo.class, this.x8AiLinePointLatlngInfoDao);
    }

    public void clear() {
        this.dataStaticInfoDaoConfig.clearIdentityScope();
        this.gH2DataStaticInfoDaoConfig.clearIdentityScope();
        this.mediaDownloadInfoDaoConfig.clearIdentityScope();
        this.studentDaoConfig.clearIdentityScope();
        this.x8AiLinePointInfoDaoConfig.clearIdentityScope();
        this.x8AiLinePointLatlngInfoDaoConfig.clearIdentityScope();
    }

    public DataStaticInfoDao getDataStaticInfoDao() {
        return this.dataStaticInfoDao;
    }

    public GH2DataStaticInfoDao getGH2DataStaticInfoDao() {
        return this.gH2DataStaticInfoDao;
    }

    public MediaDownloadInfoDao getMediaDownloadInfoDao() {
        return this.mediaDownloadInfoDao;
    }

    public StudentDao getStudentDao() {
        return this.studentDao;
    }

    public X8AiLinePointInfoDao getX8AiLinePointInfoDao() {
        return this.x8AiLinePointInfoDao;
    }

    public X8AiLinePointLatlngInfoDao getX8AiLinePointLatlngInfoDao() {
        return this.x8AiLinePointLatlngInfoDao;
    }
}
