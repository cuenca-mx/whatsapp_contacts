package com.cuenca.whatsapp_contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;


import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * WhatsappContactsPlugin
 */
public class WhatsappContactsPlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.cuenca.plugin.whatsapp_contacts");
        channel.setMethodCallHandler(new WhatsappContactsPlugin(registrar));
    }

    WhatsappContactsPlugin(Registrar registrar) {
        this.registrar = registrar;
    }

    private final Registrar registrar;

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("getWhatsappContacts")) {
            ArrayList<HashMap<String, String>> contacts = getWhatsappContacts();
            result.success(contacts);
        } else {
            result.notImplemented();
        }
    }

    // Implementation based on https://stackoverflow.com/questions/35448250/how-to-get-whatsapp-contacts-from-android by @mansukh-ahir
    private ArrayList<HashMap<String, String>> getWhatsappContacts() {
        Context context = registrar.context();
        ContentResolver cr = context.getContentResolver();

        Cursor contactCursor = cr.query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID,
                        ContactsContract.RawContacts.CONTACT_ID},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"},
                null);

        ArrayList<HashMap<String, String>> contacts = new ArrayList<>();

        if (contactCursor == null || contactCursor.getCount() == 0 || !contactCursor.moveToFirst()) {
            return contacts;
        }

        do {
            String whatsappContactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

            if (whatsappContactId == null) {
                continue;
            }

            Cursor whatsAppContactCursor = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    },
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{whatsappContactId}, null);

            if (whatsAppContactCursor == null) {
                continue;
            }

            whatsAppContactCursor.moveToFirst();
            String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            whatsAppContactCursor.close();

            HashMap<String, String> contact = new HashMap<>();
            contact.put("name", name);
            contact.put("phone_number", number);

            contacts.add(contact);
        } while (contactCursor.moveToNext());

        contactCursor.close();

        return contacts;
    }
}
