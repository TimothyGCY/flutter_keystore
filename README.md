# flutter_keystore
Android KeyStore System for Flutter application

## Installation

```yaml
dependencies:
  flutter_keystore:
    url: https://github.com/TimothyGCY/flutter_keystore.git
```

## Usage

```dart
// Instantiate plugin
final FlutterKeyStore keyStore = FlutterKeyStore();

// To write data into keystore
await keyStore.write('key', 'value');

// To read data from keystore by key
await keyStore.read('key');

// To delete data from keystore by key
await keyStore.delete('key');

```

