package com.fimi.kernel.store.sqlite.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.fimi.kernel.store.sqlite.entity.MediaDownloadInfo;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;


public class MediaDownloadInfoDao extends AbstractDao<MediaDownloadInfo, Long> {
    public static final String TABLENAME = "MEDIA_DOWNLOAD_INFO";

    public MediaDownloadInfoDao(DaoConfig config) {
        super(config);
    }

    public MediaDownloadInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"MEDIA_DOWNLOAD_INFO\" (\"_id\" INTEGER PRIMARY KEY ,\"START_POS\" INTEGER NOT NULL ,\"END_POS\" INTEGER NOT NULL ,\"COMPELETE_ZIZE\" INTEGER NOT NULL ,\"URL\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MEDIA_DOWNLOAD_INFO\"";
        db.execSQL(sql);
    }

    @Override
    public final void bindValues(DatabaseStatement stmt, MediaDownloadInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindLong(2, entity.getStartPos());
        stmt.bindLong(3, entity.getEndPos());
        stmt.bindLong(4, entity.getCompeleteZize());
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(5, url);
        }
    }

    @Override
    public final void bindValues(SQLiteStatement stmt, MediaDownloadInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        stmt.bindLong(2, entity.getStartPos());
        stmt.bindLong(3, entity.getEndPos());
        stmt.bindLong(4, entity.getCompeleteZize());
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(5, url);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        if (cursor.isNull(offset)) {
            return null;
        }
        return Long.valueOf(cursor.getLong(offset));
    }

    @Override
    public MediaDownloadInfo readEntity(Cursor cursor, int offset) {
        MediaDownloadInfo entity = new MediaDownloadInfo(cursor.isNull(offset) ? null : Long.valueOf(cursor.getLong(offset)), cursor.getLong(offset + 1), cursor.getLong(offset + 2), cursor.getLong(offset + 3), cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, MediaDownloadInfo entity, int offset) {
        entity.setId(cursor.isNull(offset) ? null : Long.valueOf(cursor.getLong(offset)));
        entity.setStartPos(cursor.getLong(offset + 1));
        entity.setEndPos(cursor.getLong(offset + 2));
        entity.setCompeleteZize(cursor.getLong(offset + 3));
        entity.setUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
    }

    @Override
    public final Long updateKeyAfterInsert(MediaDownloadInfo entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    @Override
    public Long getKey(MediaDownloadInfo entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    @Override
    public boolean hasKey(MediaDownloadInfo entity) {
        return entity.getId() != null;
    }

    @Override
    public final boolean isEntityUpdateable() {
        return true;
    }


    public static class Properties {
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property StartPos = new Property(1, Long.TYPE, "startPos", false, "START_POS");
        public static final Property EndPos = new Property(2, Long.TYPE, "endPos", false, "END_POS");
        public static final Property CompeleteZize = new Property(3, Long.TYPE, "compeleteZize", false, "COMPELETE_ZIZE");
        public static final Property Url = new Property(4, String.class, "url", false, "URL");
    }
}
