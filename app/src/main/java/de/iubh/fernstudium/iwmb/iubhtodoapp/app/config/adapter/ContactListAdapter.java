package de.iubh.fernstudium.iwmb.iubhtodoapp.app.config.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.iubh.fernstudium.iwmb.iubhtodoapp.R;
import de.iubh.fernstudium.iwmb.iubhtodoapp.activities.TodoDetailActivity;
import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.contact.ContactDTO;

public class ContactListAdapter extends ArrayAdapter<ContactDTO> implements Filterable {

    private List<ContactDTO> contacts;
    List<ContactDTO> tempItems;
    List<ContactDTO> suggestions;
    Filter nameFilter;
    int resource;
    LayoutInflater inflater;
    Context context;

    public ContactListAdapter(@NonNull Context context, int resource, List<ContactDTO> contacts) {
        super(context, resource, contacts);
        this.tempItems = new ArrayList<>(contacts);
        this.contacts = new ArrayList<>(contacts);
        this.suggestions = new ArrayList<>();
        this.nameFilter = createFilter();
        this.resource = resource;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ContactDTO contactDTO = getItem(position);
        if (view == null) {
            view = inflater.inflate(resource, parent, false);
        }

        TextView contactName = view.findViewById(R.id.idContactName);
        contactName.setText(contactDTO.getName());
        view.setTag(contactDTO);
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter createFilter() {
        return new Filter() {

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                ContactDTO contactDTO = (ContactDTO) resultValue;
                return contactDTO.getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence filterString) {
                FilterResults results = new FilterResults();
                if (!TextUtils.isEmpty(filterString)) {
                    suggestions.clear();
                    for (ContactDTO c : tempItems) {
                        if (c.getName().toLowerCase().contains(filterString.toString().toLowerCase())) {
                            suggestions.add(c);
                        }
                    }
                    results.values = suggestions;
                    results.count = suggestions.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    clear();
                    List<ContactDTO> selection = (List<ContactDTO>) results.values;
                    for (ContactDTO c : selection) {
                        add(c);
                    }
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

}
