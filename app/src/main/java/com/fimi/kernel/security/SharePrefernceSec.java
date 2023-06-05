package com.fimi.kernel.security;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.fimi.kernel.FimiAppContext;

import org.jetbrains.annotations.Contract;

public class SharePrefernceSec {
    private static final String KEY_SP_STORE_MANAGER = "SPStoreManager";
    private static final boolean isEncrypt = true;

    @NonNull
    @Contract("_ -> new")
    public static SharedPreferences getSharedPreferences(String key_storeName) {
        return new SecurePreferences(FimiAppContext.getContext(), "", key_storeName);
    }

    @NonNull
    @Contract(" -> new")
    public static SharedPreferences getSharedPreferences() {
        return new SecurePreferences(FimiAppContext.getContext(), "", KEY_SP_STORE_MANAGER);
    }
}
