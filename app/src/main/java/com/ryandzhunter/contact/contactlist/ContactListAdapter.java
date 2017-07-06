package com.ryandzhunter.contact.contactlist;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryandzhunter.contact.BR;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.contactdetail.ContactDetailActivity;
import com.ryandzhunter.contact.databinding.ContactListItemBinding;
import com.ryandzhunter.contact.data.model.Contact;

import java.util.List;
import java.util.Random;

/**
 * Created by aryandi on 7/2/17.
 */

class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private List<Contact> contactList;
    private Context context;
    private ContactListItemBinding binding;
    private boolean isFirstIndex = true;

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
        ContactItemViewModel vm = new ContactItemViewModel(item);

        // set Favorite Index
        boolean isFavorite = false;
        if (position == 0) isFavorite = item.favorite;

        // set Alphabeth Index
        boolean isIndex = false;
        if (!item.favorite) {
            if (position == 0 || isFirstIndex){
                // firstIndex field to create alphabeth index at the first time
                isIndex = true;
                isFirstIndex = false;
            } else if (!item.firstName.substring(0,1).toLowerCase()
                    .equals(contactList.get(position - 1).firstName.substring(0,1).toLowerCase())){
                isIndex = true;
            }
        }

        holder.takeItem(item, vm, isFavorite, isIndex);
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
            binding.profilePicture.getBackground().setColorFilter(getRandomColor(), PorterDuff.Mode.MULTIPLY);
        }

        ViewDataBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }

        void takeItem(final Contact contact, ContactItemViewModel vm, boolean isFavorite, boolean isIndex) {
            getBinding().setVariable(BR.contact, contact);
            getBinding().setVariable(BR.contactItemVM, vm);
            getBinding().setVariable(BR.isFavorite, isFavorite);
            getBinding().setVariable(BR.isIndex, isIndex);
            getBinding().executePendingBindings();

            itemView.setOnClickListener(v -> {
                ContactDetailActivity.openContactDetailActivity(context, contact.id);
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

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
