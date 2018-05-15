package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ContactUtils;

public class ContactLoaderIntentService extends IntentService {

    private static final String ACTION_LOAD_CONTACTS = "de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.action.LOAD_CONTACTS";

    public ContactLoaderIntentService() {
        super("ContactLoaderIntentService");
    }

    public static void startActionLoadContacts(Context context) {
        Intent intent = new Intent(context, ContactLoaderIntentService.class);
        intent.setAction(ACTION_LOAD_CONTACTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_CONTACTS.equals(action)) {
                handleActionLoadContacts();
            } else  {
                throw new RuntimeException("Unknown Intent");
            }
        }
    }

    private void handleActionLoadContacts() {
        ContactUtils.initializeContacts(getApplicationContext());
    }

}
