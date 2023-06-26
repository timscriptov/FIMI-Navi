package router.rule;

import android.content.Context;
import android.content.Intent;
import java.util.HashMap;

public abstract class BaseIntentRule<T> implements Rule<T, Intent> {
    private final HashMap<String, Class<T>> mIntentRules = new HashMap<>();

    public abstract void throwException(String str);

    @Override
    public void router(String pattern, Class<T> klass) {
        this.mIntentRules.put(pattern, klass);
    }

    @Override
    public Intent invoke(Context ctx, String pattern) {
        Class<T> klass = this.mIntentRules.get(pattern);
        if (klass == null) {
            throwException(pattern);
        }
        return new Intent(ctx, (Class<?>) klass);
    }

    @Override
    public boolean resolveRule(String pattern) {
        return this.mIntentRules.get(pattern) != null;
    }
}
