package com.securepreferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.kernel.connect.tcp.SocketOption;
import com.tozny.crypto.android.AesCbcWithIntegrity;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SecurePreferences implements SharedPreferences {
    private static final String TAG = SecurePreferences.class.getName();
    private static boolean sLoggingEnabled = false;
    private AesCbcWithIntegrity.SecretKeys keys;
    private String sharedPrefFilename;
    private SharedPreferences sharedPreferences;

    public SecurePreferences(Context context) {
        this(context, "", null);
    }

    public SecurePreferences(Context context, String password, String sharedPrefFilename) {
        this(context, null, password, sharedPrefFilename);
    }

    public SecurePreferences(Context context, AesCbcWithIntegrity.SecretKeys secretKey, String sharedPrefFilename) {
        this(context, secretKey, null, sharedPrefFilename);
    }

    private SecurePreferences(Context context, AesCbcWithIntegrity.SecretKeys secretKey, String password, String sharedPrefFilename) {
        if (this.sharedPreferences == null) {
            this.sharedPreferences = getSharedPreferenceFile(context, sharedPrefFilename);
        }
        if (secretKey != null) {
            this.keys = secretKey;
        } else if (TextUtils.isEmpty(password)) {
            try {
                String key = generateAesKeyName(context);
                String keyAsString = this.sharedPreferences.getString(key, null);
                if (keyAsString == null) {
                    this.keys = AesCbcWithIntegrity.generateKey();
                    boolean committed = this.sharedPreferences.edit().putString(key, this.keys.toString()).commit();
                    if (!committed) {
                        Log.w(TAG, "Key not committed to prefs");
                    }
                } else {
                    this.keys = AesCbcWithIntegrity.keys(keyAsString);
                }
                if (this.keys == null) {
                    throw new GeneralSecurityException("Problem generating Key");
                }
            } catch (GeneralSecurityException e) {
                if (sLoggingEnabled) {
                    Log.e(TAG, "Error init:" + e.getMessage());
                }
                throw new IllegalStateException(e);
            }
        } else {
            try {
                byte[] salt = getDeviceSerialNumber(context).getBytes();
                this.keys = AesCbcWithIntegrity.generateKeyFromPassword(password, salt);
                if (this.keys == null) {
                    throw new GeneralSecurityException("Problem generating Key From Password");
                }
            } catch (GeneralSecurityException e2) {
                if (sLoggingEnabled) {
                    Log.e(TAG, "Error init using user password:" + e2.getMessage());
                }
                throw new IllegalStateException(e2);
            }
        }
    }

    private static String generateAesKeyName(@NonNull Context context) throws GeneralSecurityException {
        String password = context.getPackageName();
        byte[] salt = getDeviceSerialNumber(context).getBytes();
        AesCbcWithIntegrity.SecretKeys generatedKeyName = AesCbcWithIntegrity.generateKeyFromPassword(password, salt);
        if (generatedKeyName == null) {
            throw new GeneralSecurityException("Key not generated");
        }
        return hashPrefKey(generatedKeyName.toString());
    }

    private static String getDeviceSerialNumber(Context context) {
        try {
            String deviceSerial = (String) Build.class.getField("SERIAL").get(null);
            if (TextUtils.isEmpty(deviceSerial)) {
                return Settings.Secure.getString(context.getContentResolver(), "android_id");
            }
            return deviceSerial;
        } catch (Exception e) {
            return Settings.Secure.getString(context.getContentResolver(), "android_id");
        }
    }

    @Nullable
    public static String hashPrefKey(@NonNull String prefKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = prefKey.getBytes(SocketOption.DEFAULT_CHARSET);
            digest.update(bytes, 0, bytes.length);
            return Base64.encodeToString(digest.digest(), 2);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            if (sLoggingEnabled) {
                Log.w(TAG, "Problem generating hash", e);
            }
            return null;
        }
    }

    public static boolean isLoggingEnabled() {
        return sLoggingEnabled;
    }

    public static void setLoggingEnabled(boolean loggingEnabled) {
        sLoggingEnabled = loggingEnabled;
    }

    private SharedPreferences getSharedPreferenceFile(Context context, String prefFilename) {
        this.sharedPrefFilename = this.sharedPrefFilename;
        return TextUtils.isEmpty(prefFilename) ? PreferenceManager.getDefaultSharedPreferences(context) : context.getSharedPreferences(prefFilename, 0);
    }

    public void destroyKeys() {
        this.keys = null;
    }

    public String encrypt(String cleartext) {
        if (!TextUtils.isEmpty(cleartext)) {
            try {
                return AesCbcWithIntegrity.encrypt(cleartext, this.keys).toString();
            } catch (UnsupportedEncodingException | GeneralSecurityException e) {
                if (sLoggingEnabled) {
                    Log.w(TAG, "encrypt", e);
                }
                return null;
            }
        }
        return cleartext;
    }

    @Nullable
    private String decrypt(String ciphertext) {
        if (!TextUtils.isEmpty(ciphertext)) {
            try {
                AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(ciphertext);
                return AesCbcWithIntegrity.decryptString(cipherTextIvMac, this.keys);
            } catch (UnsupportedEncodingException | GeneralSecurityException e) {
                if (sLoggingEnabled) {
                    Log.w(TAG, "decrypt", e);
                }
                return null;
            }
        }
        return ciphertext;
    }

    @Override
    public Map<String, String> getAll() {
        Map<String, ?> encryptedMap = this.sharedPreferences.getAll();
        Map<String, String> decryptedMap = new HashMap<>(encryptedMap.size());
        for (Map.Entry<String, ?> entry : encryptedMap.entrySet()) {
            try {
                Object cipherText = entry.getValue();
                if (cipherText != null && !cipherText.equals(this.keys.toString())) {
                    decryptedMap.put(entry.getKey(), decrypt(cipherText.toString()));
                }
            } catch (Exception e) {
                if (sLoggingEnabled) {
                    Log.w(TAG, "error during getAll", e);
                }
                decryptedMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return decryptedMap;
    }

    @Override
    public String getString(String key, String defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        if (encryptedValue != null) {
            return decrypt(encryptedValue);
        }
        return defaultValue;
    }

    public String getUnencryptedString(String key, String defaultValue) {
        String nonEncryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        return nonEncryptedValue != null ? nonEncryptedValue : defaultValue;
    }

    @Override
    @TargetApi(11)
    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        Set<String> encryptedSet = this.sharedPreferences.getStringSet(hashPrefKey(key), null);
        if (encryptedSet != null) {
            Set<String> decryptedSet = new HashSet<>(encryptedSet.size());
            for (String encryptedValue : encryptedSet) {
                decryptedSet.add(decrypt(encryptedValue));
            }
            return decryptedSet;
        }
        return defaultValues;
    }

    @Override
    public int getInt(String key, int defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public long getLong(String key, long defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        String encryptedValue = this.sharedPreferences.getString(hashPrefKey(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public boolean contains(String key) {
        return this.sharedPreferences.contains(hashPrefKey(key));
    }

    public void handlePasswordChange(String newPassword, Context context) throws GeneralSecurityException {
        byte[] salt = getDeviceSerialNumber(context).getBytes();
        AesCbcWithIntegrity.SecretKeys newKey = AesCbcWithIntegrity.generateKeyFromPassword(newPassword, salt);
        Map<String, ?> allOfThePrefs = this.sharedPreferences.getAll();
        Map<String, String> unencryptedPrefs = new HashMap<>(allOfThePrefs.size());
        for (String prefKey : allOfThePrefs.keySet()) {
            Object prefValue = allOfThePrefs.get(prefKey);
            if (prefValue instanceof String prefValueString) {
                String plainTextPrefValue = decrypt(prefValueString);
                unencryptedPrefs.put(prefKey, plainTextPrefValue);
            }
        }
        destroyKeys();
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.clear();
        editor.apply();
        this.sharedPreferences = null;
        this.sharedPreferences = getSharedPreferenceFile(context, this.sharedPrefFilename);
        this.keys = newKey;
        SharedPreferences.Editor updatedEditor = this.sharedPreferences.edit();
        for (String prefKey2 : unencryptedPrefs.keySet()) {
            String prefPlainText = unencryptedPrefs.get(prefKey2);
            updatedEditor.putString(prefKey2, encrypt(prefPlainText));
        }
        updatedEditor.apply();
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener, boolean decryptKeys) {
        if (!decryptKeys) {
            registerOnSharedPreferenceChangeListener(listener);
        }
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public class Editor implements SharedPreferences.Editor {
        private final SharedPreferences.Editor mEditor;

        private Editor() {
            this.mEditor = SecurePreferences.this.sharedPreferences.edit();
        }

        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(value));
            return this;
        }

        public SharedPreferences.Editor putUnencryptedString(String key, String value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), value);
            return this;
        }

        @Override
        @TargetApi(11)
        public SharedPreferences.Editor putStringSet(String key, @NonNull Set<String> values) {
            Set<String> encryptedValues = new HashSet<>(values.size());
            for (String value : values) {
                encryptedValues.add(SecurePreferences.this.encrypt(value));
            }
            this.mEditor.putStringSet(SecurePreferences.hashPrefKey(key), encryptedValues);
            return this;
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(Long.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(Float.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            this.mEditor.putString(SecurePreferences.hashPrefKey(key), SecurePreferences.this.encrypt(Boolean.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            this.mEditor.remove(SecurePreferences.hashPrefKey(key));
            return this;
        }

        @Override
        public SharedPreferences.Editor clear() {
            this.mEditor.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return this.mEditor.commit();
        }

        @Override
        @TargetApi(9)
        public void apply() {
            if (Build.VERSION.SDK_INT >= 9) {
                this.mEditor.apply();
            } else {
                commit();
            }
        }
    }
}
