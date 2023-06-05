package com.fimi.kernel.utils;

import com.fimi.kernel.GlobalConfig;
import com.fimi.network.entity.FwContenti18N;

/* loaded from: classes.dex */
public class I18NUtil {
    public static String getI18NStrin(FwContenti18N fwContenti18N) {
        if ("zh_CN".equalsIgnoreCase(GlobalConfig.getInstance().getLanguageModel().getCountryLanguage())) {
            String str = fwContenti18N.getZh_CN();
            return str;
        } else if ("zh_TW".equalsIgnoreCase(GlobalConfig.getInstance().getLanguageModel().getCountryLanguage())) {
            String str2 = fwContenti18N.getZh_TW();
            return str2;
        } else {
            String str3 = fwContenti18N.getEn_US();
            return str3;
        }
    }
}
