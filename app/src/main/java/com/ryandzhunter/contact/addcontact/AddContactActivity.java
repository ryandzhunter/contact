package com.ryandzhunter.contact.addcontact;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ryandzhunter.contact.BaseActivity;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.contactdetail.ContactDetailActivity;
import com.ryandzhunter.contact.contactdetail.ContactDetailModule;
import com.ryandzhunter.contact.contactlist.ContactListActivity;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.databinding.ActivityAddContactBinding;
import com.ryandzhunter.contact.databinding.ActivityContactDetailBinding;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by aryandi on 7/4/17.
 */

public class AddContactActivity extends BaseActivity implements AddContactView {

    private static final int REQUEST_CODE_CAPTURE_IMAGE = 0;
    private static final int REQUEST_CODE_GALLERY = 1;
    @Inject
    AddContactViewModel viewModel;
    Contact contact;
    private ActivityAddContactBinding binding;

    public static void openAddContactActivity(Context context) {
        Intent intent = new Intent(context, AddContactActivity.class);
        context.startActivity(intent);
    }

    public static void openEditContactActivity(Context context, Contact contact) {
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

        viewModel.obsError.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showAlertDialog(AddContactActivity.this, getString(R.string.alert_network_error_title), getString(R.string.alert_network_error_message));
            }
        });

        viewModel.isLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.isLoading.get()) {
                    showProgressDialog(getString(R.string.progress_message));
                } else {
                    hideProgressDialog();
                }
            }
        });
    }

    @Override
    public void openPhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
        builder.setTitle(R.string.choose_image)
                .setItems(R.array.array_choose_image, (dialogInterface, i) -> {
                    switch (i) {
                        case 0:
                            //Checking Permssion
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE);
                            break;
                        case 1:
                            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    selectImage();
                                } else {
                                    ActivityCompat.requestPermissions((this), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                }
                            } else {
                                selectImage();
                            }
                            break;
                    }
                }).create().show();
    }

    private void selectImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void getIntentExtra() {
        Parcelable parcelable = getIntent().getParcelableExtra("contact");
        if (parcelable != null) contact = Parcels.unwrap(parcelable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap photo;
        Uri imageUri;
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(this, photo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(this)
                    .load(stream.toByteArray())
                    .asBitmap()
                    .error(R.drawable.ic_profile_large)
                    .into(binding.addContactProfileImage);
        }

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            if (null != data) {
                imageUri = data.getData();
                try {
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(this)
                        .load(imageUri)
                        .error(R.drawable.ic_profile_large)
                        .into(binding.addContactProfileImage);
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
