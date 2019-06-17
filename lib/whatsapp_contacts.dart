import 'dart:async';

import 'package:flutter/services.dart';

class WhatsappContacts {
  static const MethodChannel _channel =
      const MethodChannel('com.cuenca.plugin.whatsapp_contacts');

  static Future<List<dynamic>> get contacts async {
    final contacts = await _channel.invokeMethod('getWhatsappContacts');
    return contacts;
  }
}
