package com.fimi.kernel.store.sqlite.helper;

import com.fimi.kernel.store.sqlite.dao.MediaDownloadInfoDao;
import com.fimi.kernel.store.sqlite.entity.MediaDownloadInfo;
import com.fimi.kernel.store.sqlite.helper.core.DbCore;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;


public class MediaDownloadInfoHelper {
    private static final MediaDownloadInfoHelper mMediaDownloadInfoHelper = new MediaDownloadInfoHelper();
    private final MediaDownloadInfoDao dao = DbCore.getDaoSession().getMediaDownloadInfoDao();

    public static MediaDownloadInfoHelper getIntance() {
        return mMediaDownloadInfoHelper;
    }

    public void addMediaDownloadInfo(MediaDownloadInfo info) {
        this.dao.insert(info);
    }

    public void updateMediaDownloadInfo(String url, MediaDownloadInfo info) {
        MediaDownloadInfo findUser = this.dao.queryBuilder().where(MediaDownloadInfoDao.Properties.Url.eq(url), new WhereCondition[0]).build().unique();
        if (findUser != null) {
            findUser.setUrl(info.getUrl());
            findUser.setCompeleteZize(info.getCompeleteZize());
            findUser.setEndPos(info.getEndPos());
            findUser.setStartPos(info.getStartPos());
            this.dao.update(findUser);
        }
    }

    public void deleteMediaDownloadInfo(String url) {
        MediaDownloadInfo findUser = this.dao.queryBuilder().where(MediaDownloadInfoDao.Properties.Url.eq(url), new WhereCondition[0]).build().unique();
        if (findUser != null) {
            this.dao.deleteByKey(findUser.getId());
        }
    }

    public MediaDownloadInfo queryMediaDownloadInfo(String url) {
        MediaDownloadInfo findUser = this.dao.queryBuilder().where(MediaDownloadInfoDao.Properties.Url.eq(url), new WhereCondition[0]).build().unique();
        return findUser;
    }

    public void deleteByUrl(String url) {
        String sql = "delete from MEDIA_DOWNLOAD_INFO where URL='" + url + "'";
        this.dao.getDatabase().execSQL(sql);
    }

    public void deleteAll() {
        this.dao.deleteAll();
    }

    private void delete(String url) {
        MediaDownloadInfo findUser = this.dao.queryBuilder().where(MediaDownloadInfoDao.Properties.Url.eq(url), new WhereCondition[0]).build().unique();
        if (findUser != null) {
            this.dao.deleteByKey(findUser.getId());
        }
    }

    private void update(String url, MediaDownloadInfo info) {
        MediaDownloadInfo findUser = this.dao.queryBuilder().where(MediaDownloadInfoDao.Properties.Url.eq(url), new WhereCondition[0]).build().unique();
        if (findUser != null) {
            findUser.setUrl(info.getUrl());
            findUser.setCompeleteZize(info.getCompeleteZize());
            findUser.setEndPos(info.getEndPos());
            findUser.setStartPos(info.getStartPos());
            this.dao.update(findUser);
        }
    }

    private List<MediaDownloadInfo> queryAllList() {
        QueryBuilder<MediaDownloadInfo> qb = this.dao.queryBuilder();
        List<MediaDownloadInfo> list = qb.list();
        return list;
    }

    private List<MediaDownloadInfo> queryList(String url) {
        QueryBuilder<MediaDownloadInfo> qb = this.dao.queryBuilder();
        qb.where(MediaDownloadInfoDao.Properties.Url.eq(url));
        List<MediaDownloadInfo> list = qb.list();
        return list;
    }

    public void test() {
        queryAllList();
    }

    private void add() {
        MediaDownloadInfo st = new MediaDownloadInfo();
        st.setUrl("ddd");
        st.setCompeleteZize(5L);
        this.dao.insert(st);
    }
}
