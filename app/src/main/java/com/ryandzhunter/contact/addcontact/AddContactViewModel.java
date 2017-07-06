package com.ryandzhunter.contact.addcontact;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ryandzhunter.contact.ILifecycleViewModel;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by aryandi on 7/6/17.
 */

public class AddContactViewModel extends BaseObservable implements ILifecycleViewModel {

    private final Context context;
    private final GetContactListUseCase useCase;
    private final AddContactView view;

    public Contact contact;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ObservableField<Boolean> isValidFirstName = new ObservableField<>();
    public ObservableField<Boolean> isValidLastName = new ObservableField<>();
    public ObservableField<Boolean> isValidPhoneNumber = new ObservableField<>();
    public ObservableField<Boolean> isValidEmail = new ObservableField<>();

    ObservableBoolean isLoading = new ObservableBoolean();
    ObservableField<Throwable> obsError = new ObservableField<>();

    public AddContactViewModel(Context context, GetContactListUseCase usecase, AddContactView view) {
        this.context = context;
        this.useCase = usecase;
        this.view = view;
    }

    public String titleBar() {
        return "Add new contact";
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
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
                addNewContact(contact);
            } else {
                updateContact(contact);
            }
        };
    }

    private void addNewContact(Contact contact) {
        compositeDisposable.add(useCase.addContact(contact)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contacts -> {
                    addContactToCache(contact);
                }, throwable -> obsError.set(throwable)));
    }

    private void addContactToCache(Contact contact) {
        this.contact = contact;
        useCase.saveCachedContact(contact);
    }

    private void updateContact(Contact contact) {
        useCase.updateContact(contact.id, contact)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contacts -> {
                    onSuccessUpdateContact(contact);
                }, throwable -> obsError.set(throwable));;
    }

    private void onSuccessUpdateContact(Contact contact) {
        this.contact = contact;
        useCase.updateCachedContact(contact);
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
}
