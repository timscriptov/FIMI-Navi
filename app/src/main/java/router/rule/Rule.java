package router.rule;

import android.content.Context;

public interface Rule<T, V> {
    V invoke(Context context, String str);

    boolean resolveRule(String str);

    void router(String str, Class<T> cls);
}
