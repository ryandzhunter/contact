package com.ryandzhunter.contact.contactlist;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.databinding.ContactListItemBinding;
import com.ryandzhunter.contact.model.Contact;

import java.util.List;

/**
 * Created by aryandi on 7/2/17.
 */

class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private List<Contact> contactList;
    private Context context;
    private ContactListItemBinding binding;

    ContactListAdapter(Context context, List<Contact> contactList) {
        inflater = LayoutInflater.from(context);
        this.contactList = contactList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact item = contactList.get(position);
        holder.takeItem(item);
    }

    @Override
    public int getItemCount() {
        if(contactList != null) {
            return contactList.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }

        void takeItem(final Contact contact) {
            getBinding().setVariable(BR.contact, contact);
            getBinding().executePendingBindings();

            itemView.setOnClickListener(v -> {

            });
        }
    }


    public List<Contact> getContactList() {
        return contactList;
    }

    void setContactList(List<Contact> contactLists) {
        this.contactList = contactLists;
        notifyDataSetChanged();
    }
}
