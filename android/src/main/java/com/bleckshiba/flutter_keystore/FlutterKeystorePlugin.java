package com.bleckshiba.flutter_keystore;

import android.content.Context;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class FlutterKeystorePlugin implements FlutterPlugin, MethodCallHandler {
  private static final String CHANNEL = "com.bleckshiba.dev/keystore";

  private MethodChannel channel;

  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    final StorageAction action = StorageAction.fromString(call.method);
    if (action == null) {
      result.notImplemented();
      return;
    }
    final String storageName = call.argument("storageName");
    Storage storage;
    if (storageName == null || storageName.trim().isEmpty()) {
      storage = new Storage(context);
    } else {
      storage = new Storage(context, storageName);
    }
    final String key = call.argument("key");
    switch (action) {
      case READ: {
        result.success(storage.read(key));
      }
      break;
      case WRITE: {
        final String value = call.argument("value");
        storage.write(key,value);
        result.success(null);
      }
      break;
      case DELETE: {
        storage.delete(key);
        result.success(null);
      }
      break;
      default:
        result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
