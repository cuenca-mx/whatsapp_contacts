import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:whatsapp_contacts/whatsapp_contacts.dart';

void main() {
  const MethodChannel channel = MethodChannel('com.cuenca.whatsapp_contacts');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await WhatsappContacts.platformVersion, '42');
  });
}
