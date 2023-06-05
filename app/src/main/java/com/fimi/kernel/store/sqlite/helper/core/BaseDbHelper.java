package com.fimi.kernel.store.sqlite.helper.core;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/* loaded from: classes.dex */
public class BaseDbHelper<T, K> {
    private AbstractDao<T, K> mDao;

    public BaseDbHelper(AbstractDao dao) {
        this.mDao = dao;
    }

    public void save(T item) {
        this.mDao.insert(item);
    }

    public void save(T... items) {
        this.mDao.insertInTx(items);
    }

    public void save(List<T> items) {
        this.mDao.insertInTx(items);
    }

    public void saveOrUpdate(T item) {
        this.mDao.insertOrReplace(item);
    }

    public void saveOrUpdate(T... items) {
        this.mDao.insertOrReplaceInTx(items);
    }

    public void saveOrUpdate(List<T> items) {
        this.mDao.insertOrReplaceInTx(items);
    }

    public void deleteByKey(K key) {
        this.mDao.deleteByKey(key);
    }

    public void delete(T item) {
        this.mDao.delete(item);
    }

    public void delete(T... items) {
        this.mDao.deleteInTx(items);
    }

    public void delete(List<T> items) {
        this.mDao.deleteInTx(items);
    }

    public void deleteAll() {
        this.mDao.deleteAll();
    }

    public void update(T item) {
        this.mDao.update(item);
    }

    public void update(T... items) {
        this.mDao.updateInTx(items);
    }

    public void update(List<T> items) {
        this.mDao.updateInTx(items);
    }

    public T query(K key) {
        return this.mDao.load(key);
    }

    public List<T> queryAll() {
        return this.mDao.loadAll();
    }

    public List<T> query(String where, String... params) {
        return this.mDao.queryRaw(where, params);
    }

    public QueryBuilder<T> queryBuilder() {
        return this.mDao.queryBuilder();
    }

    public long count() {
        return this.mDao.count();
    }

    public void refresh(T item) {
        this.mDao.refresh(item);
    }

    public void detach(T item) {
        this.mDao.detach(item);
    }
}
