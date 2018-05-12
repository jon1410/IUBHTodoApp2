package de.iubh.fernstudium.iwmb.iubhtodoapp.activities.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.ContactAdapter;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.contact.ContactDTO;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ContactUtils;

public class SelectContactsDialog extends DialogFragment {

    private List<ContactDTO> contacts;
    private ContactAdapter contactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.contact_selection_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.selectContactRecyclerView);
        contacts = loadContacts();
        contactAdapter = new ContactAdapter(contacts);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    private List<ContactDTO> loadContacts() {
        return ContactUtils.getContacts();
    }
}
