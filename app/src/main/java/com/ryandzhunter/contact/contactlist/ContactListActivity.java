package com.ryandzhunter.contact.contactlist;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ryandzhunter.contact.BaseActivity;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.databinding.ActivityContactListBinding;
import com.ryandzhunter.contact.databinding.ToolbarBinding;
import com.ryandzhunter.contact.model.Contact;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aryandi on 7/1/17.
 */

public class ContactListActivity extends BaseActivity {

    @Inject
    ContactListViewModel viewModel;
    private ActivityContactListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.fetchContactList();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contact_list;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getAppComponent()
                .splashComponent(new ContactListModule(this))
                .inject(this);
        addViewModel(viewModel);
        binding = ActivityContactListBinding.bind(contentView);
        binding.setMainVM(viewModel);

        viewModel.setTitleBar(getString(R.string.contact_toolbar_title));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvContactList.setLayoutManager(layoutManager);
        ContactListAdapter contactAdapter = new ContactListAdapter(this, new ArrayList<>());
        binding.rvContactList.setAdapter(contactAdapter);

        viewModel.obsRequestResult.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                List<Contact> contacts = viewModel.obsRequestResult.get();
                ContactListAdapter contactAdapter = (ContactListAdapter) binding.rvContactList.getAdapter();
                contactAdapter.setContactList(contacts);
            }
        });

        viewModel.obsError.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showAlertDialog(ContactListActivity.this, getString(R.string.alert_network_error_title), getString(R.string.alert_network_error_message));
            }
        });

        viewModel.isLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.isLoading.get()){
                    showProgressDialog(getString(R.string.progress_message));
                } else {
                    hideProgressDialog();
                }
            }
        });
    }

}
