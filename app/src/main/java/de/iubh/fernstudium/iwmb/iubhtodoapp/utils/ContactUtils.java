package de.iubh.fernstudium.iwmb.iubhtodoapp.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.contact.ContactDTO;

public class ContactUtils {

    private static final String TAG = "ContactUtils";
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
                    //ContactsContract.CommonDataKinds.Email.ADDRESS
            };
    private static List<ContactDTO> contacts;

    private ContactUtils() {
    }

    public static void initializeContacts(Context currentContext) {
        Log.v(TAG, "Initlizing contacts... ");
        contacts = new ArrayList<>();
        ContentResolver contentResolver = currentContext.getContentResolver();
        Cursor cr = null;
        try {
            cr = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, null, null, null, null);
            if (cr != null && cr.moveToFirst()) {
                do {
                    String contactId = cr.getString(cr.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cr.getString(cr.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                    Cursor ce = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contactId}, null);
                    String email = null;
                    if (ce != null && ce.moveToFirst()) {
                        email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        ce.close();
                    }
                    contacts.add(new ContactDTO(Integer.valueOf(contactId), name, email));
                } while (cr.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ContactUtils", "Error loading Contacts");
        } finally {
            cr.close();
        }
        Log.v(TAG, "Finished initializing contacts... loaded: " + contacts.size());
    }

    private static ContactDTO createContactDtoFromCursorPosition(Cursor cr) {
        int contactId = cr.getInt(cr.getColumnIndex(ContactsContract.Contacts._ID));
        String name = cr.getString(cr.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
        String email = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
        return new ContactDTO(contactId, name, email);
    }

    public static List<ContactDTO> getContacts() {
        return contacts;
    }

    public static String getContactName(int contactId){
        ContactDTO c = findContactDtoById(contactId);
        if(c != null){
            return c.getName();
        }
        return null;
    }

    private static ContactDTO findContactDtoById(int id){
        for (ContactDTO c: contacts) {
            if(c.getId() == id){
                return c;
            }
        }
        return null;
    }
}
