package com.bleckshiba.flutter_keystore;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

public class Storage {
    private static final String TAG = Storage.class.getSimpleName();

    private static final String KEYSTORE_NOT_FOUND = "KeyStore not found";

    private static final String SECRET_ALGORITHM = "DESede";

    private static final char[] keyStorePassword = TAG.toCharArray();

    private final File storeFile;

    public Storage(Context context) {
        this(context, "keystore");
    }

    public Storage(Context context, @NonNull String storageName) {
        if (!storageName.startsWith(".")) storageName = "." + storageName;
        this.storeFile = new File(String.format("%s/%s", context.getFilesDir().getAbsolutePath(), storageName));
    }

    public void write(String key, String value) {
        Log.d(TAG, "Write begin");
        try {
            Optional<KeyStore> keystore = getStore();
            if (!keystore.isPresent()) throw new KeyStoreException(KEYSTORE_NOT_FOUND);
            SecretKeySpec secretKeySpec = new SecretKeySpec(value.getBytes(), SECRET_ALGORITHM);
            SecretKeyEntry secretKeyEntry = new SecretKeyEntry(secretKeySpec);
            keystore.get().setEntry(key, secretKeyEntry, new PasswordProtection(keyStorePassword));
            saveChange(keystore.get());
        } catch (Exception e) {
            logError(e);
        } finally {
            Log.d(TAG, "Write complete");
        }
    }

    public String read(String key) {
        Log.d(TAG, "Read begin");
        try {
            Optional<KeyStore> keyStore = getStore();
            if (!keyStore.isPresent()) throw new KeyStoreException(KEYSTORE_NOT_FOUND);
            SecretKeyEntry entry = (SecretKeyEntry) keyStore.get()
                    .getEntry(key, new PasswordProtection(keyStorePassword));
            if (entry == null) return null;
            return new String(entry.getSecretKey().getEncoded());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
            logError(e);
            return null;
        } finally {
            Log.d(TAG, "Read complete");
        }
    }

    public void delete(String key) {
        Log.d(TAG, "Delete begin");
        try {
            Optional<KeyStore> keyStore = getStore();
            if (!keyStore.isPresent()) throw new KeyStoreException(KEYSTORE_NOT_FOUND);
            keyStore.get().deleteEntry(key);
            saveChange(keyStore.get());
        } catch (KeyStoreException e) {
            logError(e);
        } finally {
            Log.d(TAG, "Delete complete");
        }
    }

    private Optional<KeyStore> getStore() {
        KeyStore keystore = null;
        try {
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            if (storeFile.exists()) {
                try (FileInputStream inputStream = new FileInputStream(storeFile)) {
                    keystore.load(inputStream, keyStorePassword);
                }
            } else keystore.load(null, keyStorePassword);
        } catch (Exception e) {
            logError(e);
        }
        return Optional.ofNullable(keystore);
    }

    private void saveChange(KeyStore keyStore) {
        try (FileOutputStream outputStream = new FileOutputStream(storeFile)) {
            keyStore.store(outputStream, keyStorePassword);
        } catch (Exception e) {
            logError(e);
        }
    }

    private void logError(Exception e) {
        Log.e(TAG, "Something went wrong", e);
    }
}
