package router.exception;

public class ReceiverNotRouteException extends NotRouteException {
    public ReceiverNotRouteException(String pattern) {
        super("receiver", pattern);
    }
}
