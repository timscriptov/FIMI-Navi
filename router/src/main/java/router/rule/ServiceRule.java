package router.rule;

import android.app.Service;
import router.exception.ServiceNotRouteException;

public class ServiceRule extends BaseIntentRule<Service> {
    public static final String SERVICE_SCHEME = "service://";

    @Override
    public void throwException(String pattern) {
        throw new ServiceNotRouteException(pattern);
    }
}
