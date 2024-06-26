import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_keystore_platform_interface.dart';

class MethodChannelFlutterKeystore extends FlutterKeystorePlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('com.bleckshiba.dev/keystore');

  @override
  Future<String?> read(String key, {String? storageName}) async {
    final String? value = await methodChannel.invokeMethod<String?>('read', {
      'key': key,
      'storageName': storageName,
    });
    return value;
  }

  @override
  Future<void> write(String key, String value, {String? storageName}) async {
    await methodChannel.invokeMethod('write', {
      'key': key,
      'value': value,
      'storageName': storageName,
    });
  }

  @override
  Future<void> delete(String key, {String? storageName}) async {
    await methodChannel.invokeMethod('delete', {
      'key': key,
      'storageName': storageName,
    });
  }
}
