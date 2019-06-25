import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:whatsapp_contacts/whatsapp_contacts.dart';

void main() {
  const MethodChannel channel = MethodChannel('com.cuenca.plugin.whatsapp_contacts');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async => [
          {'phone': '12345', 'name': 'foo'},
          {'phone': '43215', 'name': 'bar'},
        ]);
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('get whatsapp contacts', () async {
    expect(await WhatsappContacts.contacts, [
      {'phone': '12345', 'name': 'foo'},
      {'phone': '43215', 'name': 'bar'},
    ]);
  });
}
