package com.ryandzhunter.contact.addcontact;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Parcelable;
import android.view.View;

import com.ryandzhunter.contact.BaseActivity;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.contactdetail.ContactDetailActivity;
import com.ryandzhunter.contact.contactdetail.ContactDetailModule;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.databinding.ActivityAddContactBinding;
import com.ryandzhunter.contact.databinding.ActivityContactDetailBinding;

import org.parceler.Parcels;

import javax.inject.Inject;

/**
 * Created by aryandi on 7/4/17.
 */

public class AddContactActivity extends BaseActivity implements AddContactView {

    @Inject
    AddContactViewModel viewModel;
    Contact contact;
    private ActivityAddContactBinding binding;

    public static void openAddContactActivity(Context context){
        Intent intent = new Intent(context, AddContactActivity.class);
        context.startActivity(intent);
    }

    public static void openEditContactActivity(Context context, Contact contact){
        Intent intent = new Intent(context, AddContactActivity.class);
        intent.putExtra("contact", Parcels.wrap(contact));
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_contact;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getAppComponent()
                .addContactComponent(new AddContactModule(this, this))
                .inject(this);
        addViewModel(viewModel);
        binding = ActivityAddContactBinding.bind(contentView);
        binding.setAddContactVM(viewModel);
        viewModel.setContact(contact == null ? new Contact() : contact);
    }

    @Override
    protected void getIntentExtra() {
        Parcelable parcelable = getIntent().getParcelableExtra("contact");
        if (parcelable != null) contact = Parcels.unwrap(parcelable);
    }
}
