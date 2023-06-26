package router.exception;

import androidx.core.app.NotificationCompat;

public class ServiceNotRouteException extends NotRouteException {
    public ServiceNotRouteException(String pattern) {
        super(NotificationCompat.CATEGORY_SERVICE, pattern);
    }
}
