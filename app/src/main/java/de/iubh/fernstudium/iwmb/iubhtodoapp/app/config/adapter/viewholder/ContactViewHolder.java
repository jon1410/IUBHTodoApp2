package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.viewholder;

import android.support.v7.widget.RecyclerView;

import de.iubh.fernstudium.iwmb.iubhtodoapp.databinding.ContactItemBinding;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.contact.ContactDTO;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    ContactItemBinding contactItemBinding;

    public ContactViewHolder(ContactItemBinding contactItemBinding) {
        super(contactItemBinding.getRoot());
        this.contactItemBinding = contactItemBinding;
    }

    public void bind(ContactDTO contact) {
        //contactItemBinding.setContact(contact);
        contactItemBinding.executePendingBindings();
    }
}
