import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_keystore/flutter_keystore.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _storeValue = '';
  final _flutterKeystorePlugin = FlutterKeystore();
  final String myDataKey = 'hello';
  final TextEditingController _valueController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadStoreData();
  }

  @override
  void dispose() {
    _valueController.dispose();
    super.dispose();
  }

  Future<void> _loadStoreData() async {
    String value;
    try {
      value = await _flutterKeystorePlugin.read(myDataKey) ?? '(Empty)';
    } on PlatformException {
      value = 'Failed to read store value';
    }

    if (!mounted) return;

    setState(() {
      _storeValue = value;
    });
  }

  Future<void> _onSave() async {
    final String value = _valueController.text.trim();
    if (value.isEmpty) {
      await _onDelete();
    } else {
      await _flutterKeystorePlugin.write(myDataKey, value);
    }
    _valueController.clear();
    _loadStoreData();
  }

  Future<void> _onDelete() async {
    await _flutterKeystorePlugin.delete(myDataKey);
    _loadStoreData();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            Expanded(
              child: Center(
                child: Text('Value in keystore: $_storeValue'),
              ),
            ),
            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 28.0),
                    child: TextField(
                      controller: _valueController,
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        label: Text('Value in store'),
                      ),
                    ),
                  ),
                  const SizedBox(height: 24),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      ElevatedButton(
                        onPressed: _onSave,
                        child: const Text('Save'),
                      ),
                      const SizedBox(width: 24),
                      ElevatedButton(
                        onPressed: _onDelete,
                        child: const Text('Delete'),
                      ),
                    ],
                  )
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
