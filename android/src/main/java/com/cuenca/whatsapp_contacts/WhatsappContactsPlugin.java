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
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "whatsapp_contacts");
        channel.setMethodCallHandler(new WhatsappContactsPlugin(registrar));
    }

    WhatsappContactsPlugin(Registrar registrar) {
        this.registrar = registrar;
    }

    private final Registrar registrar;

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("getWhatsappContacts")) {
            ArrayList<HashMap<String, String>> contacts = getWhatsappContacts();
            result.success(contacts);
        } else {
            result.notImplemented();
        }
    }

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

        if (contactCursor == null || contactCursor.getCount() == 0) {
            return contacts;
        }

        if (contactCursor.moveToFirst()) {
            do {
                //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                String whatsappContactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                if (whatsappContactId != null) {
                    //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                    Cursor whatsAppContactCursor = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{whatsappContactId}, null);

                    if (whatsAppContactCursor != null) {
                        whatsAppContactCursor.moveToFirst();
                        String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        whatsAppContactCursor.close();

                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("number", number);

                        contacts.add(contact);
                    }
                }
            } while (contactCursor.moveToNext());
            contactCursor.close();
        }

        return contacts;
    }
}
