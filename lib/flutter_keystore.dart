import 'flutter_keystore_platform_interface.dart';

class FlutterKeystore {
  Future<String?> read(String key, {String? storageName}) {
    return FlutterKeystorePlatform.instance.read(key);
  }

  Future<void> write(String key, String value, {String? storageName}) async {
    await FlutterKeystorePlatform.instance
        .write(key, value, storageName: storageName);
  }

  Future<void> delete(String key, {String? storageName}) async {
    return FlutterKeystorePlatform.instance.delete(key);
  }
}
