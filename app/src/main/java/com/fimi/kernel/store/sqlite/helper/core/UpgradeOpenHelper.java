package com.fimi.kernel.store.sqlite.helper.core;

import android.content.Context;

import com.fimi.kernel.store.sqlite.dao.DaoMaster;
import com.fimi.kernel.store.sqlite.dao.StudentDao;

import org.greenrobot.greendao.database.Database;


public class UpgradeOpenHelper extends DaoMaster.OpenHelper {
    public UpgradeOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.getInstance().migrate(db, StudentDao.class);
    }
}
