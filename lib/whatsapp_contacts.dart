import 'dart:async';

import 'package:flutter/services.dart';

class WhatsappContacts {
  static const MethodChannel _channel =
      const MethodChannel('whatsapp_contacts');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<List<dynamic>> get contacts async {
    final contacts = await _channel.invokeMethod('getWhatsappContacts');
    return contacts;
  }
}
