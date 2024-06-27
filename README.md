# flutter_keystore
Android KeyStore System for Flutter application

## Installation

1. Standard installation
```yaml
dependencies:
  flutter_keystore: ^0.0.1
```

2. Using GitHub url
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

