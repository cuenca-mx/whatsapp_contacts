import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:permission_handler/permission_handler.dart';
import 'package:flutter/services.dart';
import 'package:whatsapp_contacts/whatsapp_contacts.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<dynamic> _contacts = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    List<dynamic> contacts;
    try {
      final handler = PermissionHandler();
      final permission = PermissionGroup.contacts;
      final status = await handler.checkPermissionStatus(permission);
      if (status != PermissionStatus.granted) {
        final statuses = await handler.requestPermissions([permission]);
      }

      // Implemented only for android
      if (!Platform.isAndroid) return;
      contacts = await WhatsappContacts.contacts;
    } on PlatformException {}

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _contacts = contacts;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: ListView.builder(
          itemCount: _contacts.length,
          itemBuilder: (context, index) {
            return Card(
              child: ListTile(
                title: Text(_contacts[index]['name']),
                subtitle: Text(_contacts[index]['phone_number']),
              ),
            );
          },
        ),
      ),
    );
  }
}
