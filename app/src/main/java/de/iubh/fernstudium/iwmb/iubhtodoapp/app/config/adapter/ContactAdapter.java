package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter.viewholder.ContactViewHolder;
import de.iubh.fernstudium.iwmb.iubhtodoapp.databinding.ContactItemBinding;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.contact.ContactDTO;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> implements Filterable{

    List<ContactDTO> contacts;
    List<ContactDTO> filteredContacts;
    ContactViewHolder viewHolder;

    public ContactAdapter(List<ContactDTO> contacts) {
        this.contacts = contacts;
        this.filteredContacts = contacts;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ContactItemBinding itemBinding = DataBindingUtil.inflate(layoutInflater,  R.layout.contact_item, parent, false);
        viewHolder = new ContactViewHolder(itemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        viewHolder.bind(filteredContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredContacts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence filterString) {
                if(TextUtils.isEmpty(filterString)){
                    filteredContacts = contacts;
                }else{
                    List<ContactDTO> filterdList = new ArrayList<>();
                    for (ContactDTO c : contacts){
                        if(c.getName().toLowerCase().contains(filterString.toString().toLowerCase())){
                            filterdList.add(c);
                        }
                    }
                    filteredContacts = filterdList;
                }
                FilterResults results = new FilterResults();
                results.values = filteredContacts;
                results.count = filteredContacts.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredContacts = (List<ContactDTO>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
