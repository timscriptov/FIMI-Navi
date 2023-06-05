package com.fimi.kernel.store.sqlite.helper.core;

import android.content.Context;

import com.fimi.kernel.store.sqlite.dao.DaoMaster;
import com.fimi.kernel.store.sqlite.dao.StudentDao;

import org.greenrobot.greendao.database.Database;

/* loaded from: classes.dex */
public class UpgradeOpenHelper extends DaoMaster.OpenHelper {
    public UpgradeOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override // org.greenrobot.greendao.database.DatabaseOpenHelper
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.getInstance().migrate(db, StudentDao.class);
    }
}
