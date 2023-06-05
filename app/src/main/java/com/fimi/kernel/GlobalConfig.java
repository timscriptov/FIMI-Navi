package com.fimi.kernel;

import androidx.annotation.NonNull;

import com.fimi.kernel.language.LanguageModel;
import com.fimi.kernel.store.shared.SPStoreManager;

public class GlobalConfig {
    static GlobalConfig config = new GlobalConfig();
    LanguageModel languageModel;

    private GlobalConfig() {
    }

    public static GlobalConfig getInstance() {
        return config;
    }

    public void init(@NonNull Builder builder) {
        this.languageModel = builder.languageModel;
    }

    public LanguageModel getLanguageModel() {
        return this.languageModel;
    }

    public void setLanguageModel(LanguageModel languageModel) {
        this.languageModel = languageModel;
    }

    public static class Builder {
        private LanguageModel languageModel;

        public Builder setLanguageModel(LanguageModel languageModel) {
            this.languageModel = languageModel;
            SPStoreManager.getInstance().saveObject(Constants.LANGUAGETYPE, languageModel);
            return this;
        }
    }
}
