package com.fimi.kernel.store.sqlite.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.fimi.kernel.store.sqlite.entity.Student;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;


public class StudentDao extends AbstractDao<Student, Long> {
    public static final String TABLENAME = "STUDENT";

    public StudentDao(DaoConfig config) {
        super(config);
    }

    public StudentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"STUDENT\" (\"_id\" INTEGER PRIMARY KEY ,\"NAME\" TEXT,\"AGE\" TEXT,\"NUMBER\" TEXT);");
    }

    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STUDENT\"";
        db.execSQL(sql);
    }

    @Override
    public final void bindValues(DatabaseStatement stmt, Student entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(3, age);
        }
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(4, number);
        }
    }

    @Override
    public final void bindValues(SQLiteStatement stmt, Student entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id.longValue());
        }
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(3, age);
        }
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(4, number);
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
    public Student readEntity(Cursor cursor, int offset) {
        Student entity = new Student(cursor.isNull(offset) ? null : Long.valueOf(cursor.getLong(offset)), cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, Student entity, int offset) {
        entity.setId(cursor.isNull(offset) ? null : Long.valueOf(cursor.getLong(offset)));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAge(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNumber(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
    }

    @Override
    public final Long updateKeyAfterInsert(Student entity, long rowId) {
        entity.setId(Long.valueOf(rowId));
        return Long.valueOf(rowId);
    }

    @Override
    public Long getKey(Student entity) {
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }

    @Override
    public boolean hasKey(Student entity) {
        return entity.getId() != null;
    }

    @Override
    public final boolean isEntityUpdateable() {
        return true;
    }


    public static class Properties {
        public static final Property Id = new Property(0, Long.class, "id", true, "_id");
        public static final Property Name = new Property(1, String.class, "name", false, "NAME");
        public static final Property Age = new Property(2, String.class, "age", false, "AGE");
        public static final Property Number = new Property(3, String.class, "number", false, "NUMBER");
    }
}
