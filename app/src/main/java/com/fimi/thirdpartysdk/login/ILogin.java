package com.fimi.thirdpartysdk.login;

import android.content.Context;
import android.content.Intent;

/* loaded from: classes.dex */
public interface ILogin {
    void login(Context context, LoginCallback loginCallback);

    void onActivityResult(int i, int i2, Intent intent);
}
