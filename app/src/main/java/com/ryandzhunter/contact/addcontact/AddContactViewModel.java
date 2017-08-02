package com.ryandzhunter.contact.addcontact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ryandzhunter.contact.ILifecycleViewModel;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;
import com.ryandzhunter.contact.util.Preferences;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

/**
 * Created by aryandi on 7/6/17.
 */

public class AddContactViewModel extends BaseObservable implements ILifecycleViewModel {

    private static final int REQUEST_CODE_CAPTURE_IMAGE = 0;
    private static final int REQUEST_CODE_GALLERY = 1;

    private final Context context;
    private final GetContactListUseCase useCase;
    private final AddContactView view;
    private final Preferences pref;

    public Contact contact;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ObservableField<Boolean> isValidFirstName = new ObservableField<>();
    public ObservableField<Boolean> isValidLastName = new ObservableField<>();
    public ObservableField<Boolean> isValidPhoneNumber = new ObservableField<>();
    public ObservableField<Boolean> isValidEmail = new ObservableField<>();

    ObservableBoolean isLoading = new ObservableBoolean();
    ObservableField<Throwable> obsError = new ObservableField<>();
    private Bitmap photo;
    private Uri imageUri;

    public AddContactViewModel(Context context, GetContactListUseCase usecase, AddContactView view, Preferences pref) {
        this.context = context;
        this.useCase = usecase;
        this.view = view;
        this.pref = pref;
    }

    public String titleBar() {
        if (contact != null && contact.id != null){
            return "Edit New Contact";
        } else {
            return "Add New Contact";
        }
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        setValidityFlag(contact);
    }

    private void setValidityFlag(Contact contact) {
        isValidFirstName.set(isValidName(contact.firstName));
        isValidLastName.set(isValidName(contact.lastName));
        isValidPhoneNumber.set(isValidPhoneNumber(contact.phoneNumber));
        isValidEmail.set((isValidEmail(contact.email)));
    }

    public String getFirstName() {
        return contact.firstName;
    }

    public String getLastName() {
        return contact.lastName;
    }

    public String getEmail() {
        return contact.email;
    }

    public String getPhoneNumber() {
        return contact.phoneNumber;
    }

    public String getProfilePic(String profilePic) {
        return contact.profilePic;
    }

    public void setFirstName(String firstName) {
        contact.firstName = firstName;
        isValidFirstName.set(isValidName(firstName));
    }

    public void setLastName(String lastName) {
        contact.lastName = lastName;
        isValidLastName.set(isValidName(lastName));
    }

    public void setEmail(String email) {
        contact.email = email;
        isValidEmail.set(isValidEmail(email));
    }

    public void setPhoneNumber(String phoneNumber) {
        contact.phoneNumber = phoneNumber;
        isValidPhoneNumber.set(isValidPhoneNumber(phoneNumber));
    }

    public void setProfilePic(String profilePic) {
        contact.profilePic = profilePic;
    }

    public void onPhotoClick() {
       view.openPhotoDialog();
    }

    public void onSaveClicked(){
        if (checkValidation(contact)){
            if (contact.id == null){
                if (imageUri != null){
                    addNewContactWithPhoto();
                } else {
                    addNewContact(contact);
                }
            } else {
                updateContactToAPI(contact);
            }
        };
    }

    private void addNewContact(Contact contact) {
        compositeDisposable.add(useCase.addContactToAPI(contact)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contacts -> {
                    addContactToCache(contact);
                }, throwable -> obsError.set(throwable)));
    }

    private void addNewContactWithPhoto(){
        File file = createImageFile(photo);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("contact[profile_pic]", file.getName(), imageBody);
        RequestBody firstNameBody = RequestBody.create(MediaType.parse("text/plain"), contact.firstName);
        RequestBody lastNameBody = RequestBody.create(MediaType.parse("text/plain"), contact.lastName);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), contact.email);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), contact.phoneNumber);

        compositeDisposable.add(useCase.addContactWithImage(imagePart, firstNameBody, lastNameBody, emailBody, phoneBody)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contacts -> {
                    addContactToCache(contact);
                }, throwable -> {
                    obsError.set(throwable);
                }));
    }

    public static File createImageFile(Bitmap image) {
        String filename = Environment.getExternalStorageDirectory() + "/profile.jpg";
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void addContactToCache(Contact contact) {
        this.contact = contact;
        useCase.saveCachedContact(contact)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(() -> {
                    Timber.d("onComplete - successfully added contact to cache");
                    pref.setIsShouldInvalidateList(true);
                    view.closeActivity();
                }, throwable -> obsError.set(throwable));
    }

    private void updateContactToAPI(Contact contact) {
        useCase.updateContactToAPI(contact.id, contact)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contacts -> {
                    onSuccessUpdateContact(contact);
                }, throwable -> obsError.set(throwable));;
    }

    private void onSuccessUpdateContact(Contact contact) {
        this.contact = contact;
        updateCachedContact(contact);
    }

    private void updateCachedContact(Contact contact) {
        useCase.updateCachedContact(contact)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(() -> {
                    Timber.d("onComplete - successfully update cache contact");
                    pref.setIsShouldReloadList(true);
                }, throwable -> obsError.set(throwable));
    }

    @BindingAdapter(value = {"bind:imageUrl"}, requireAll = false)
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_betty_allen)
                .error(R.drawable.ic_betty_allen)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }

    private boolean checkValidation(Contact contact) {
        return isValidLastName.get() && isValidLastName.get() && isValidEmail.get() && isValidPhoneNumber.get();
    }

    public boolean isValidName(String name){
        return !TextUtils.isEmpty(name) && name.length() > 3;
    }
    public boolean isValidPhoneNumber(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches() && phone.length() >= 10;
    }

    public boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    void onActivityResult(int requestCode, int resultCode, Intent data, AddContactActivity addContactActivity) {
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(context, photo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(addContactActivity)
                    .load(stream.toByteArray())
                    .asBitmap()
                    .error(R.drawable.ic_profile_large)
                    .into(addContactActivity.binding.addContactProfileImage);
        }

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            if (null != data) {
                imageUri = data.getData();
                try {
                    photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(addContactActivity)
                        .load(imageUri)
                        .error(R.drawable.ic_profile_large)
                        .into(addContactActivity.binding.addContactProfileImage);
            }
        }
    }

    public Uri getImageUri(Context context, Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
        return Uri.parse(path);
    }

}
