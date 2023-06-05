package com.fimi.thirdpartysdk;

import android.content.Context;

import com.fimi.android.app.R;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

public class ThirdLoginManager {
    public static final void initThirdLogin(Context context) {
        Twitter.initialize(context);
        String CONSUMER_KEY = context.getResources().getString(R.string.twitter_CONSUMER_KEY);
        String CONSUMER_SECRET = context.getResources().getString(R.string.twitter_CONSUMER_SECRET);
        TwitterConfig config = new TwitterConfig.Builder(context).logger(new DefaultLogger(3)).twitterAuthConfig(new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET)).debug(true).build();
        Twitter.initialize(config);
    }
}
