package router;

import android.content.Context;

import androidx.annotation.NonNull;

import router.rule.Rule;

public class Router {
    @NonNull
    public static RouterInternal addRule(String scheme, Rule rule) {
        RouterInternal router2 = RouterInternal.get();
        router2.addRule(scheme, rule);
        return router2;
    }

    public static <T> RouterInternal router(String pattern, Class<T> klass) {
        return RouterInternal.get().router(pattern, klass);
    }

    public static <V> V invoke(Context ctx, String pattern) {
        return (V) RouterInternal.get().invoke(ctx, pattern);
    }

    public static boolean resolveRouter(String pattern) {
        return RouterInternal.get().resolveRouter(pattern);
    }
}
