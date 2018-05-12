package de.iubh.fernstudium.iwmb.iubhtodoapp.utils;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
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
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.CommonDataKinds.Email.ADDRESS
            };
    private static Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            //ContactsContract.Contacts.CONTENT_URI;

    private static List<ContactDTO> contacts;

    private ContactUtils() {
    }

    public static void initializeContacts(Context currentContext) {
        Log.v(TAG, "Initlizing contacts... ");
        contacts = new ArrayList<>();
        ContentResolver contentResolver = currentContext.getContentResolver();
        ContentProviderClient contentProviderClient = contentResolver.acquireContentProviderClient(uri);
        try {
            Cursor cr = contentProviderClient.query(uri, PROJECTION, null, null, null, null);
            cr.moveToFirst();
            while (!cr.isAfterLast()) {
                contacts.add(createContactDtoFromCursorPosition(cr));
                cr.moveToNext();
            }
        } catch (RemoteException e) {
            Log.e("ContactUtils", "Error loading Contacts");
        }finally {
            contentProviderClient.close();
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
}
