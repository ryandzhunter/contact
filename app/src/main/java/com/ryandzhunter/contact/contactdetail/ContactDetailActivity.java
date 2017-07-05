package com.ryandzhunter.contact.contactdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ryandzhunter.contact.BaseActivity;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.contactlist.ContactListActivity;
import com.ryandzhunter.contact.contactlist.ContactListModule;
import com.ryandzhunter.contact.contactlist.ContactListViewModel;
import com.ryandzhunter.contact.databinding.ActivityContactDetailBinding;

import javax.inject.Inject;

/**
 * Created by aryandi on 7/4/17.
 */

public class ContactDetailActivity extends BaseActivity {

    @Inject
    ContactDetailViewModel viewModel;
    private boolean isFavourite;
    private ActivityContactDetailBinding binding;
    private Menu menu;

    public static void openContactDetailActivity(Context context, int id){
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contact_detail;
    }

    @Override
    protected void setupDataBinding(View contentView) {
        getAppComponent()
                .contactDetailComponent(new ContactDetailModule(this))
                .inject(this);
        addViewModel(viewModel);
        binding = ActivityContactDetailBinding.bind(contentView);
        binding.setContactDetailVM(viewModel);

        int id = getIntent().getIntExtra("id", -1);
        if (id != -1) viewModel.fetchContactDetail(id);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_detail, menu);
        this.menu = menu;
        setFavouriteIcon(menu);
        return true;
    }

    private void setFavouriteIcon(Menu menu) {
        if (isFavourite) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favourite_filled));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favourite));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            viewModel.clickEdit();
            return true;
        } else if (id == R.id.action_favourite) {
            viewModel.updateContact(menu);
            return true;
        } else if (id == R.id.action_delete) {
            viewModel.deleteContact();
            return true;
        } else if (id == R.id.action_share) {
            viewModel.onShareMenuClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
