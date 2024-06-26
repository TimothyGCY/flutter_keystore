import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_keystore_method_channel.dart';

abstract class FlutterKeystorePlatform extends PlatformInterface {
  /// Constructs a FlutterKeystorePlatform.
  FlutterKeystorePlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterKeystorePlatform _instance = MethodChannelFlutterKeystore();

  /// The default instance of [FlutterKeystorePlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterKeystore].
  static FlutterKeystorePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterKeystorePlatform] when
  /// they register themselves.
  static set instance(FlutterKeystorePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> read(String key, {String? storageName}) {
    throw UnimplementedError('read(String) has not been implemented.');
  }

  Future<void> write(String key, String value, {String? storageName}) {
    throw UnimplementedError('write(String, String) has not been implemented.');
  }

  Future<void> delete(String key, {String? storageName}) {
    throw UnimplementedError('delete(String) has not been implemented.');
  }
}
