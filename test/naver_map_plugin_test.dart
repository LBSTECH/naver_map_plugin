import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:naver_map_plugin/naver_map_plugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('naver_map_plugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });
}
