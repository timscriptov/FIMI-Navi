package com.fimi.kernel.base;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class BaseAppManager {
    private static final String TAG = BaseAppManager.class.getSimpleName();
    private static BaseAppManager instance = null;
    private static List<Activity> mActivities = new LinkedList();

    public static BaseAppManager getInstance() {
        if (instance == null) {
            synchronized (BaseAppManager.class) {
                if (instance == null) {
                    instance = new BaseAppManager();
                }
            }
        }
        return instance;
    }

    public int size() {
        return mActivities.size();
    }

    public synchronized Activity getForwardActivity() {
        return size() > 0 ? mActivities.get(size() - 1) : null;
    }

    public synchronized void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public synchronized void removeActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    public synchronized void clear() {
        int i = mActivities.size() - 1;
        while (i > -1) {
            Activity activity = mActivities.get(i);
            removeActivity(activity);
            activity.finish();
            int i2 = mActivities.size();
            i = i2 - 1;
        }
    }

    public synchronized void clearToTop() {
        int i = mActivities.size() - 2;
        while (i > -1) {
            Activity activity = mActivities.get(i);
            removeActivity(activity);
            activity.finish();
            int i2 = mActivities.size() - 1;
            i = i2 - 1;
        }
    }
}
