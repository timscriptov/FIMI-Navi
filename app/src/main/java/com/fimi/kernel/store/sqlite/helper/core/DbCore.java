package com.fimi.kernel.store.sqlite.helper.core;

import android.content.Context;

import com.fimi.kernel.store.sqlite.dao.DaoMaster;
import com.fimi.kernel.store.sqlite.dao.DaoSession;

/* loaded from: classes.dex */
public class DbCore {
    private static final String DEFAULT_DB_NAME = "_sql.db";
    private static String DB_NAME = null;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static Context mContext;

    public static void init(Context context) {
        init(context, DEFAULT_DB_NAME);
    }

    public static void init(Context context, String dbName) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        mContext = context.getApplicationContext();
        DB_NAME = dbName;
    }

    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new UpgradeOpenHelper(mContext, DB_NAME);
            daoMaster = new DaoMaster(helper.getWritableDb());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
