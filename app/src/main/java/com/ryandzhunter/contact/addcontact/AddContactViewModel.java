package com.ryandzhunter.contact.addcontact;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ryandzhunter.contact.ILifecycleViewModel;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by aryandi on 7/6/17.
 */

public class AddContactViewModel extends BaseObservable implements ILifecycleViewModel {

    private final Context context;
    private final GetContactListUseCase usecase;
    private final AddContactView view;

    public Contact contact;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AddContactViewModel(Context context, GetContactListUseCase usecase, AddContactView view) {
        this.context = context;
        this.usecase = usecase;
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
    }

    public void setLastName(String lastName) {
        contact.lastName = lastName;
    }

    public void setEmail(String email) {
        contact.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        contact.phoneNumber = phoneNumber;
    }

    public void setProfilePic(String profilePic) {
        contact.profilePic = profilePic;
    }

    public void onPhotoClick() {

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
}
