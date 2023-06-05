package com.fimi.kernel.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.fimi.kernel.Constants;
import com.fimi.kernel.GlobalConfig;
import com.fimi.kernel.language.ConstantLanguages;
import com.fimi.kernel.language.LanguageItem;
import com.fimi.kernel.language.LanguageModel;
import com.fimi.kernel.store.shared.SPStoreManager;

import java.util.Locale;

public class LanguageUtil {
    public static final String[] ZH_SERVER = {"CN", ConstantLanguages.REGION_TW, "HK"};

    @RequiresApi(api = 24)
    public static void changeAppLanguage(@NonNull Context context, Locale setLocale) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = getLocaleByLanguage(setLocale);
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= 24) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            context.getApplicationContext().createConfigurationContext(config);
            Locale.setDefault(locale);
        }
        resources.updateConfiguration(config, dm);
    }

    public static Locale getLocaleByLanguage(Locale locale) {
        return isSupportLanguage(locale) ? getLanguageModel(locale).getLocale() : LanguageItem.defaultLanguage.getLocale();
    }

    public static LanguageModel getLanguageModel(Locale locale) {
        LanguageModel model = null;
        for (int i = 0; i < LanguageItem.languageModels.length; i++) {
            LanguageModel languageModel = LanguageItem.languageModels[i];
            if (languageModel.getLanguageCode().equals(locale.getLanguage()) && languageModel.getCountry().equals(locale.getCountry())) {
                model = languageModel;
            }
        }
        if (model == null) {
            LanguageModel model2 = LanguageItem.defaultLanguage;
            return model2;
        }
        return model;
    }

    private static boolean isSupportLanguage(Locale locale) {
        boolean isSupport = false;
        for (int i = 0; i < LanguageItem.languageModels.length; i++) {
            LanguageModel languageModel = LanguageItem.languageModels[i];
            if (languageModel.getLanguageCode().equals(locale.getLanguage()) && languageModel.getCountry().equals(locale.getCountry())) {
                isSupport = true;
            }
        }
        return isSupport;
    }

    public static Context attachBaseContext(Context context) {
        LanguageModel model = GlobalConfig.getInstance().getLanguageModel();
        return attachBaseContext(context, model.getLocale());
    }

    public static Context attachBaseContext(Context context, Locale locale) {
        return updateResources(context, locale);
    }

    @TargetApi(24)
    private static Context updateResources(@NonNull Context context, Locale locale2) {
        Resources resources = context.getResources();
        Locale locale = getLocaleByLanguage(locale2);
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= 24) {
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
        } else {
            configuration.setLocale(locale);
        }
        return context.createConfigurationContext(configuration);
    }

    public static LanguageModel getCurrentLanguage() {
        LanguageModel model = SPStoreManager.getInstance().getObject(Constants.LANGUAGETYPE, LanguageModel.class);
        if (model == null) {
            return getLanguageModel(Locale.getDefault());
        }
        return model;
    }

    public static boolean isZh() {
        LanguageModel model = GlobalConfig.getInstance().getLanguageModel();
        return model.getCountry().equals(Locale.SIMPLIFIED_CHINESE.getCountry()) || model.getCountry().equals(Locale.TAIWAN.getCountry());
    }
}
