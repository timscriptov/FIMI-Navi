package router.rule;

import android.content.BroadcastReceiver;
import router.exception.ReceiverNotRouteException;

public class ReceiverRule extends BaseIntentRule<BroadcastReceiver> {
    public static final String RECEIVER_SCHEME = "receiver://";

    @Override
    public void throwException(String pattern) {
        throw new ReceiverNotRouteException(pattern);
    }
}
