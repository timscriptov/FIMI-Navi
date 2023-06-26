package router.exception;

public class ActivityNotRouteException extends NotRouteException {
    public ActivityNotRouteException(String pattern) {
        super("activity", pattern);
    }
}
