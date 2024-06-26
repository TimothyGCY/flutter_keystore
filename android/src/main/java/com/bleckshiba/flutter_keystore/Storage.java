package com.bleckshiba.flutter_keystore;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.spec.SecretKeySpec;

public class Storage {
    private static final String TAG = Storage.class.getSimpleName();

    private static final char[] keyStorePassword = null;


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
        SecretKeyEntry entry;

        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            if (storeFile.exists()) {
                try (FileInputStream inputStream = new FileInputStream(storeFile)) {
                    keyStore.load(inputStream, keyStorePassword);
                }
            } else keyStore.load(null, keyStorePassword);
            SecretKeySpec secretKeySpec = new SecretKeySpec(value.getBytes(), "DESede");
            entry = new SecretKeyEntry(secretKeySpec);
            keyStore.setEntry(key, entry, new PasswordProtection(keyStorePassword));
            try (FileOutputStream outputStream = new FileOutputStream(storeFile)) {
            keyStore.store(outputStream, keyStorePassword);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "KeyStore not found");
        } catch (IOException e) {
            Log.e(TAG, "Failed to write file");
        } catch (KeyStoreException e) {
            Log.e(TAG, "Unexpected error while loading keystore");
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Invalid algorithm");
        } catch (CertificateException e) {
            Log.e(TAG, "Invalid certificate");
        }
        Log.d(TAG, "Write complete");
    }

    public String read(String key) {
        Log.d(TAG, "Read begin");
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream inputStream;
            if (storeFile.exists()) {
                inputStream = new FileInputStream(storeFile);
                keyStore.load(inputStream, keyStorePassword);
                SecretKeyEntry entry = (SecretKeyEntry) keyStore.getEntry(key, new PasswordProtection(keyStorePassword));
                inputStream.close();
                Log.d(TAG, "Read complete");
                if (entry != null) {
                    return new String(entry.getSecretKey().getEncoded());
                } else return null;
            } else keyStore.load(null, keyStorePassword);
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException |
                 UnrecoverableEntryException e) {
            Log.e(TAG, "Something went wrong");
            return null;
        }
        return "";
    }

    public void delete(String key) {
        Log.d(TAG, "Delete begin");
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream inputStream;
            if (storeFile.exists()) {
                inputStream = new FileInputStream(storeFile);
                keyStore.load(inputStream, keyStorePassword);
                keyStore.deleteEntry(key);
                inputStream.close();
                Log.d(TAG, "Delete complete");
            } else keyStore.load(null, keyStorePassword);
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            Log.e(TAG, "Something went wrong");
        }
    }
}
