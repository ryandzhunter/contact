package com.ryandzhunter.contact.contactdetail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.Observable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ryandzhunter.contact.BaseActivity;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.databinding.ActivityContactDetailBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by aryandi on 7/4/17.
 */

public class ContactDetailActivity extends BaseActivity implements ContactDetailView{

    private static final int REQ_STORAGE_PERMISSION = 1;

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
                .contactDetailComponent(new ContactDetailModule(this, this))
                .inject(this);
        addViewModel(viewModel);
        binding = ActivityContactDetailBinding.bind(contentView);
        binding.setContactDetailVM(viewModel);
        setSupportActionBar(binding.contactDetailToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        viewModel.obsError.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showAlertDialog(ContactDetailActivity.this, getString(R.string.alert_network_error_title), getString(R.string.alert_network_error_message));
            }
        });

        viewModel.isFavourite.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setFavouriteIcon(viewModel.isFavourite.get());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_detail, menu);
        this.menu = menu;
        return true;
    }

    private void setFavouriteIcon(boolean isFavourite) {
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
            viewModel.onEditClick();
            return true;
        } else if (id == R.id.action_favourite) {
            viewModel.onFavouriteClick();
            return true;
        } else if (id == R.id.action_delete) {
            viewModel.onDeleteClick();
            return true;
        } else if (id == R.id.action_share) {
            onShareMenuClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onShareMenuClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Share Contact")
                .setItems(R.array.shareContact, (dialog, which) -> {
                    switch (which){
                        case 0:
                            shareAsText(viewModel.getContact());
                            break;
                        case 1:
                            shareAsContact(viewModel.getContact());
                            break;
                    }
                }).create().show();
    }

    private void shareAsText(Contact contact) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + contact.firstName + " " + contact.lastName + " \n" +
                "Phone Number: " + contact.phoneNumber + " \n" +
                "Email: " + contact.email + "\n");
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shareIntent);
    }

    private void shareAsContact(Contact contact) {

        if (ContextCompat.checkSelfPermission(ContactDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQ_STORAGE_PERMISSION
            );
            return;
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(generateVCF(contact)));
        shareIntent.setType("text/x-vcard");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shareIntent);
    }

    private File generateVCF(Contact contact) {

        String filename = new String(Environment.getExternalStorageDirectory() + "/generated.vcf");
        Log.e("DetailActivity", filename);

        File vcfFile = new File(filename);
        FileWriter fw = null;
        try {
            fw = new FileWriter(vcfFile);
            fw.write("BEGIN:VCARD\r\n");
            fw.write("VERSION:3.0\r\n");
            fw.write("N:" + contact.firstName + ";" + contact.lastName + "\r\n");
            fw.write("FN:" + contact.firstName + " " + contact.lastName + "\r\n");
            fw.write("TEL;TYPE=HOME,VOICE:" + contact.phoneNumber + "\r\n");
            fw.write("EMAIL;TYPE=PREF,INTERNET:" + contact.email + "\r\n");
            fw.write("END:VCARD\r\n");
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return vcfFile;
    }
}
